package aarogya.master.eod.server;

import aarogya.master.eod.shared.EOD_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class MISAppUpdation_Server extends AbstractEventHandler<Event> {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCatRP = pvc.IPD_ROOM_POSTING;
    private String filterRPCat = "'" + pvc.IPD + "','" + pvc.EMERGENCY + "'";

    public MISAppUpdation_Server() {
        super(STEnum.ProcessEOD.MIS_APP_UPDATION.getValue());
    }

    @Override
    public void handle(Event event) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        SchedulerMast_Mdl schedulerMast_Mdl;
        EOD_Mdl eodMdl = new EOD_Mdl();
        String sqlQuery, serialKey = "";
        SimpleDateFormat simpleDateFormat;

        try {
            schedulerMast_Mdl = event.get(DmEnum.DmEventSystemArgs.SchedulerMaster_Model.toString(), SchedulerMast_Mdl.class);

            cnTemp = DmServer.getConnection(schedulerMast_Mdl.getActiveDB());
            spBegin = new DmSavePoint(cnTemp);

            Date dtTemp = event.get(DmEnum.DmEventSystemArgs.CurrentDateTime.toString(), Date.class);
            simpleDateFormat = new SimpleDateFormat("HH.MM");

            for (Integer i : schedulerMast_Mdl.getLstSites()) {
                if (DmShared.iNull(i).intValue() != 0) {
                    eodMdl = new EOD_Mdl();

                    eodMdl.setCurrentDate(DmShared.dNull(dtTemp));
                    eodMdl.setCurrentTime(DmServer.vNull(simpleDateFormat.format(dtTemp), "0.00"));
                    eodMdl.setCompanyCode(DmShared.xNull(schedulerMast_Mdl.getCompanyCode()));
                    eodMdl.setDatabaseName(DmShared.xNull(schedulerMast_Mdl.getActiveDB()));
                    eodMdl.setVoucherPrefix(DmShared.xNull(schedulerMast_Mdl.getVoucherPrefix()));
                    eodMdl.setUserName(schedulerMast_Mdl.getUserName());
                    eodMdl.setSiteCode(i.toString());

                    sqlQuery = "Select manualCode, serialKey from siteMast where code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(eodMdl.getSiteCode()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        eodMdl.setSiteShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                        serialKey = DmShared.xNull(rsTemp.getString("serialKey"));
                    }
                    rsTemp.close();
                    psTemp.close();

                    /*====================================< Save MIS Posting >====================================*/
                    save(eodMdl, serialKey, cnTemp);
                }
            }

            spBegin.commit();
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void save(EOD_Mdl eodMdl, String serialKey, Connection cnTemp) {
        try {
            /*======================< Posting for diagnostic >=======================*/
            diagnostic(eodMdl, serialKey, cnTemp);
            /*======================< Posting for opd >=======================*/
            opd(eodMdl, serialKey, cnTemp);
            /*======================< Posting for ipd >=======================*/
            ipd(eodMdl, serialKey, cnTemp);
            /*======================< Posting for pharmacy >=======================*/
            pharmacy(eodMdl, serialKey, cnTemp);

            /*====================================< Delete EOD >====================================*/
            deleteEOD(eodMdl.getCurrentDate(), STEnum.ProcessEOD.MIS_APP_UPDATION.getId(), eodMdl.getSiteCode(), cnTemp);

            /*====================================< Save EOD >====================================*/
            saveEOD(eodMdl.getCurrentDate(), STEnum.ProcessEOD.MIS_APP_UPDATION.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), cnTemp);

            /*====================================< Save EOD History >====================================*/
            saveEODHistory(eodMdl.getCurrentDate(), STEnum.ProcessEOD.MIS_APP_UPDATION.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Added successfully.", cnTemp);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public void diagnostic(EOD_Mdl eodMdl, String serialKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double tempValue = 0;
        StringBuilder sb = new StringBuilder();

        try {
            sqlQuery = "Select  psm.categoryCode, count(distinct pr.regDocId) as noOfPatient, max(pscm.name) as categoryName, "
                    + "         sum(psd.oh_Amt_Net) as oh_Amt_Net, max(sm.manualCode) as siteSName "
                    + "         from patientRegistration pr "
                    + "         left join patientServiceDetail psd on pr.regDocId = psd.regDocId "
                    + "         left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "         left join patientServiceCatMast pscm on pscm.code = psm.categoryCode "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = psd.site_Code "
                    + "         where psd.v_date = :currentDate and vt.category in ('" + pvc.LAB_REGISTRATION + "') "
                    + "         and pr.isCancelled = 0 and psd.isCancelled = 0 and psd.site_Code = :site_Code "
                    + "         group by psm.categoryCode, psd.site_Code "
                    + "         order By max(sm.manualCode), max(pscm.name) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            sb.append("<br/><span><font color='Red'>");
            sb.append(DmShared.dNull(eodMdl.getCurrentDate()) + " " + DmServer.vNull(DmShared.vNull(eodMdl.getCurrentTime()), "00.00"));
            sb.append("</font></span>");
            tempValue = 0;
            while (rsTemp.next()) {
                sb.append("<br/><br/><span><font color='olive'>");
                sb.append(DmShared.xNull(rsTemp.getString("categoryName")));
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append("No. of Patients : " + DmShared.iNull(rsTemp.getString("noOfPatient")).toString());
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append("Revenue : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("oh_Amt_Net")))));
                sb.append("</font></span>");

                tempValue += DmShared.vNull(rsTemp.getString("oh_Amt_Net")).doubleValue();
            }
            rsTemp.close();
            psTemp.close();

            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Total Revenue : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(tempValue))));
            sb.append("</font></span><br/>");

            sqlQuery = "Select  cl.cardTypeCode, max(ctm.name) as paymentModeName, "
                    + "         (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(cl.amtDr),0) - isNull(sum(cl.amtCr),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from patientReceipt prc "
                    + "         left join patientRegistration pr on pr.regDocId = prc.regDocId "
                    + "         left join cardLedger cl on cl.docId = prc.docId "
                    + "         left join cardTypeMast ctm on cl.cardTypeCode = ctm.code "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = prc.site_Code "
                    + "         where prc.v_date = :currentDate and vt.category in ('" + pvc.LAB_REGISTRATION + "') "
                    + "         and prc.isCancelled = 0 and prc.site_Code = :site_Code "
                    + "         group by cl.cardTypeCode, prc.site_Code, (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end)  "
                    + " union all "
                    + " Select  pla.nature, max(case when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " then 'Credit To Organization' "
                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " then 'Credit To Registration'  "
                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " then 'Credit To Patient' else 'Unknown' end) as paymentModeName, "
                    + "         (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(pla.amount),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from patientLimitApproval pla "
                    + "         left join patientRegistration pr on pr.regDocId = pla.docId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = pla.site_Code "
                    + "         where pla.v_date = :currentDate and vt.category in ('" + pvc.LAB_REGISTRATION + "') "
                    + "         and pla.nature in (" + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ") "
                    + "         and pla.isCancelled = 0 and pla.site_Code = :site_Code "
                    + "         group by pla.nature, pla.site_Code, (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) "
                    + "         order By siteSName, paymentModeName, pn desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            tempValue = 0;
            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Receipt");
            sb.append("</font></span>");
            while (rsTemp.next()) {
                sb.append("<br/><br/><span><font color='olive'>");
                sb.append(DmShared.xNull(rsTemp.getString("paymentModeName")));
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amount")))));
                sb.append("</font></span>");

                tempValue += DmShared.vNull(rsTemp.getString("amount")).doubleValue();
            }
            rsTemp.close();
            psTemp.close();

            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Total Receipt : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(tempValue))));
            sb.append("</font></span>");

            misUpdateMessage(DmShared.xNull(serialKey), DmShared.xNull(eodMdl.getSiteShortName()), "Diagnostics", sb.toString(), "");

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void pharmacy(EOD_Mdl eodMdl, String serialKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, patientType = "";
        double tempValue = 0;
        StringBuilder sb = new StringBuilder();

        try {
            patientType = "CASE WHEN sb.regDocId IS NOT NULL THEN "
                    + "				CASE WHEN prvt.category = '" + pvc.IPD + "' THEN 'IPD' "
                    + "					WHEN prvt.category = '" + pvc.DAY_CARE + "' THEN 'Day Care' "
                    + "					WHEN prvt.category = '" + pvc.EMERGENCY + "' THEN 'Emergency' "
                    + "					WHEN prvt.category IN ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "') THEN 'OPD' "
                    + "				END "
                    + "			WHEN sb.subCode IS NOT NULL THEN 'Ledger' "
                    + "			ELSE 'Walk-In' "
                    + "		END";
            sqlQuery = "SELECT " + patientType + " AS patientType "
                    + "		, MAX(CASE WHEN sb.regDocId IS NOT NULL THEN  "
                    + "				CASE WHEN prvt.category = '" + pvc.IPD + "' THEN 1 "
                    + "					WHEN prvt.category = '" + pvc.DAY_CARE + "' THEN 2 "
                    + "					WHEN prvt.category = '" + pvc.EMERGENCY + "' THEN 3 "
                    + "					WHEN prvt.category IN ('" + pvc.OPD + "', '" + pvc.EMERGENCY_OPD + "') THEN 4 "
                    + "				END "
                    + "				WHEN sb.subCode IS NOT NULL THEN 5 "
                    + "				ELSE 6 "
                    + "			END) patientTypeSr "
                    + "		, SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(sbd.oh_Amt_Net,0)) AS oh_Amt_Net "
                    + "		, MAX(sm.manualCode) AS siteSName  "
                    + "		FROM saleBill sb "
                    + "		LEFT JOIN saleBillDetail sbd on sb.docId = sbd.docId  "
                    + "		LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid "
                    + "		LEFT JOIN voucher_type prvt ON pr.v_Type=prvt.v_Type "
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type  "
                    + "		LEFT JOIN siteMast sm ON sm.code = sb.site_Code  "
                    + "		WHERE sb.v_date = :currentDate "
                    + "		AND vt.category in ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "')  "
                    + "		AND sb.isCancelled = 0  "
                    + "		AND sb.site_Code = :site_Code "
                    + "		GROUP BY sb.site_Code, " + patientType + " "
                    + "		ORDER BY MAX(sm.manualCode), patientTypeSr ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            sb.append("<br/><span><font color='Red'>");
            sb.append(DmShared.dNull(eodMdl.getCurrentDate()) + " " + DmServer.vNull(DmShared.vNull(eodMdl.getCurrentTime()), "00.00"));
            sb.append("</font></span>");
            tempValue = 0;
            while (rsTemp.next()) {
                sb.append("<br/><br/><span><font color='olive'>");
                sb.append(DmShared.xNull(rsTemp.getString("patientType")));
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append("Sale : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("oh_Amt_Net")))));
                sb.append("</font></span>");

                tempValue += DmShared.vNull(rsTemp.getString("oh_Amt_Net")).doubleValue();
            }
            rsTemp.close();
            psTemp.close();

            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Total Sale : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(tempValue))));
            sb.append("</font></span><br/>");

//            sqlQuery = "Select  cl.cardTypeCode, max(ctm.name) as paymentModeName, "
//                    + "         (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end) as pn, "
//                    + "         isNull(sum(cl.amtDr),0) - isNull(sum(cl.amtCr),0) as amount, max(sm.manualCode) as siteSName "
//                    + "         from patientReceipt prc "
//                    + "         left join ledgerMast as lm on prc.lmCode = lm.code "
//                    + "         left join cardLedger cl on cl.docId = prc.docId "
//                    + "         left join cardTypeMast ctm on cl.cardTypeCode = ctm.code "
//                    + "         left join voucher_Type vt on vt.v_Type = prc.v_Type "
//                    + "         left join siteMast sm on sm.code = prc.site_Code "
//                    + "         where prc.v_date = :currentDate "
//                    + "         and vt.category in ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PATIENT_RECEIPT + "','" + pvc.PATIENT_REFUND_RECEIPT + "') "
//                    + "         and prc.isCancelled = 0 and prc.site_Code = :site_Code "
//                    + "         group by cl.cardTypeCode, prc.site_Code, (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end)  "
//                    + " union all "
//                    + " Select  pla.nature, max(case when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " then 'Credit To Organization' "
//                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " then 'Credit To Registration'  "
//                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " then 'Credit To Patient' else 'Unknown' end) as paymentModeName, "
//                    + "         (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) as pn, "
//                    + "         isNull(sum(pla.amount),0) as amount, max(sm.manualCode) as siteSName "
//                    + "         from patientLimitApproval pla "
//                    + "         left join saleBill as mb on mb.docId = pla.docId "
//                    + "         left join saleBillDetail as mbd on mb.docId = mbd.docId "
//                    + "         left join voucher_Type as vt on vt.v_Type = mb.v_type "
//                    + "         left join siteMast sm on sm.code = mb.site_Code "
//                    + "         where pla.nature IN (" + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ") "
//                    + "         and pla.isCancelled = 0 "
//                    + "         and vt.category in ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') "
//                    + "         and mb.isCancelled = 0  "
//                    + "         and mb.site_Code = :site_Code "
//                    + "         and mb.v_Date = :currentDate  "
//                    + "         group by pla.nature, pla.site_Code, (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) "
//                    + "         order By siteSName, paymentModeName, pn desc ";
            sqlQuery = "Select  cl.cardTypeCode, max(ctm.name) as paymentModeName, "
                    + "         (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(cl.amtDr),0) - isNull(sum(cl.amtCr),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from patientReceipt prc "
                    + "         left join ledgerMast as lm on prc.lmCode = lm.code "
                    + "         left join cardLedger cl on cl.docId = prc.docId "
                    + "         left join cardTypeMast ctm on cl.cardTypeCode = ctm.code "
                    + "         left join voucher_Type vt on vt.v_Type = prc.v_Type "
                    + "         left join siteMast sm on sm.code = prc.site_Code "
                    + "         where prc.v_date = :currentDate "
                    + "         and vt.category in ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') "
                    + "         and prc.isCancelled = 0 and prc.site_Code = :site_Code "
                    + "         group by cl.cardTypeCode, prc.site_Code, (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end)  "
                    + " union all "
                    + " Select  mb.paymentSource as nature, max(case when mb.paymentSource = " + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " then 'Credit To Organization' "
                    + "         when mb.paymentSource = " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " then 'Credit To Registration'  "
                    + "         when mb.paymentSource = " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " then 'Credit To Patient' else 'Unknown' end) as paymentModeName, "
                    + "         (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(pla.amount),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from saleBill as mb "
                    + "         left join patientLimitApproval pla on mb.docId = pla.sbDocid AND mb.paymentSource = pla.nature  "
                    + "         left join voucher_Type as vt on vt.v_Type = mb.v_type "
                    + "         left join siteMast sm on sm.code = mb.site_Code "
                    + "         where mb.paymentSource IN (" + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ") "
                    + "         and pla.isCancelled = 0 "
                    + "         and vt.category in ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') "
                    + "         and mb.isCancelled = 0  "
                    + "         and mb.site_Code = :site_Code "
                    + "         and mb.v_Date = :currentDate  "
                    + "         group by mb.paymentSource, mb.site_Code, (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) "
                    + "         order By siteSName, paymentModeName, pn desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            tempValue = 0;
            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Receipt");
            sb.append("</font></span>");
            while (rsTemp.next()) {
                sb.append("<br/><br/><span><font color='olive'>");
                sb.append(DmShared.xNull(rsTemp.getString("paymentModeName")));
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amount")))));
                sb.append("</font></span>");

                tempValue += DmShared.vNull(rsTemp.getString("amount")).doubleValue();
            }
            rsTemp.close();
            psTemp.close();

            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Total Receipt : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(tempValue))));
            sb.append("</font></span>");

            misUpdateMessage(DmShared.xNull(serialKey), DmShared.xNull(eodMdl.getSiteShortName()), "Pharmacy", sb.toString(), "");

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void opd(EOD_Mdl eodMdl, String serialKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        double tempValue = 0;
        StringBuilder sb = new StringBuilder();

        try {
            sqlQuery = "Select  case when pr.isPatientOldUHID = 0 then 'New patients' else 'Old patients' end patientStatus, "
                    + "         count(distinct pr.regDocId) as noOfPatient, "
                    + "         max(sm.manualCode) as siteSName "
                    + "         from patientRegistration pr "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = pr.site_Code "
                    + "         where pr.v_date = :currentDate and vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "') "
                    + "         and pr.isCancelled = 0 and pr.site_Code = :site_Code "
                    + "         group by pr.isPatientOldUHID, pr.site_Code "
                    + "         order By max(sm.manualCode), pr.isPatientOldUHID ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            sb.append("<br/><span><font color='Red'>");
            sb.append(DmShared.dNull(eodMdl.getCurrentDate()) + " " + DmServer.vNull(DmShared.vNull(eodMdl.getCurrentTime()), "00.00"));
            sb.append("</font></span>");
            while (rsTemp.next()) {
                sb.append("<br/><br/><span><font color='olive'>");
                sb.append(DmShared.xNull(rsTemp.getString("patientStatus")));
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmShared.iNull(rsTemp.getString("noOfPatient")).toString());
                sb.append("</font></span>");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select  cl.cardTypeCode, max(ctm.name) as paymentModeName, "
                    + "         (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(cl.amtDr),0) - isNull(sum(cl.amtCr),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from patientReceipt prc "
                    + "         left join patientRegistration pr on pr.regDocId = prc.regDocId "
                    + "         left join cardLedger cl on cl.docId = prc.docId "
                    + "         left join cardTypeMast ctm on cl.cardTypeCode = ctm.code "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = prc.site_Code "
                    + "         where prc.v_date = :currentDate and vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "') "
                    + "         and prc.isCancelled = 0 and prc.site_Code = :site_Code "
                    + "         group by cl.cardTypeCode, prc.site_Code, (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end)  "
                    + " union all "
                    + " Select  pla.nature, max(case when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " then 'Credit To Organization' "
                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " then 'Credit To Registration'  "
                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " then 'Credit To Patient' else 'Unknown' end) as paymentModeName, "
                    + "         (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(pla.amount),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from patientLimitApproval pla "
                    + "         left join patientRegistration pr on pr.regDocId = pla.docId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = pla.site_Code "
                    + "         where pla.v_date = :currentDate and vt.category in ('" + pvc.OPD + "','" + pvc.EMERGENCY_OPD + "') "
                    + "         and pla.nature in (" + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ") "
                    + "         and pla.isCancelled = 0 and pla.site_Code = :site_Code "
                    + "         group by pla.nature, pla.site_Code, (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) "
                    + "         order By siteSName, paymentModeName, pn desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            tempValue = 0;
            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Receipt");
            sb.append("</font></span>");
            while (rsTemp.next()) {
                sb.append("<br/><br/><span><font color='olive'>");
                sb.append(DmShared.xNull(rsTemp.getString("paymentModeName")));
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amount")))));
                sb.append("</font></span>");

                tempValue += DmShared.vNull(rsTemp.getString("amount")).doubleValue();
            }
            rsTemp.close();
            psTemp.close();

            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Total Receipt : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(tempValue))));
            sb.append("</font></span>");

            misUpdateMessage(DmShared.xNull(serialKey), DmShared.xNull(eodMdl.getSiteShortName()), "OPD", sb.toString(), "");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public void ipd(EOD_Mdl eodMdl, String serialKey, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        double tempValue = 0;
        StringBuilder sb = new StringBuilder();

        try {
            sqlQuery = "Select  sum(case when pr.v_date = :currentDate and pr.regDocIdOld is Null then 1 else 0 end) newPatient,  "
                    + "         sum(case when pr.v_date = :currentDate and pr.regDocIdOld is not Null then 1 else 0 end) oldPatient, "
                    + "         count(distinct pr.regDocId) as noOfPatient, max(sm.manualCode) as siteSName "
                    + "         from roomMast rm "
                    + "         left join patientRegistration pr on rm.regDocId = pr.regDocId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = rm.site_Code "
                    + "         where vt.category in ('" + pvc.IPD + "') "
                    + "         and pr.isCancelled = 0 and rm.site_Code = :site_Code "
                    + "         group by pr.site_Code "
                    + "         order By max(sm.manualCode) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            sb.append("<br/><span><font color='Red'>");
            sb.append(DmShared.dNull(eodMdl.getCurrentDate()) + " " + DmServer.vNull(DmShared.vNull(eodMdl.getCurrentTime()), "00.00"));
            sb.append("</font></span>");
            while (rsTemp.next()) {
                sb.append("<br/><br/><span><font color='olive'>");
                sb.append("New patients");
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmShared.iNull(rsTemp.getString("newPatient")).toString());
                sb.append("</font></span>");

                sb.append("<br/><br/><span><font color='olive'>");
                sb.append("Old patients");
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmShared.iNull(rsTemp.getString("oldPatient")).toString());
                sb.append("</font></span>");

                sb.append("<br/><br/><span><font color='olive'>");
                sb.append("In patients");
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmShared.iNull(rsTemp.getString("noOfPatient")).toString());
                sb.append("</font></span>");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select  cl.cardTypeCode, max(ctm.name) as paymentModeName, "
                    + "         (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(cl.amtDr),0) - isNull(sum(cl.amtCr),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from patientReceipt prc "
                    + "         left join patientRegistration pr on pr.regDocId = prc.regDocId "
                    + "         left join cardLedger cl on cl.docId = prc.docId "
                    + "         left join cardTypeMast ctm on cl.cardTypeCode = ctm.code "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = prc.site_Code "
                    + "         where prc.v_date = :currentDate and vt.category in ('" + pvc.IPD + "') "
                    + "         and prc.isCancelled = 0 and prc.site_Code = :site_Code "
                    + "         group by cl.cardTypeCode, prc.site_Code, (case when isNull(cl.amtDr,0) - isNull(cl.amtCr,0) > 0 then 'p' else 'n' end)  "
                    + " union all "
                    + " Select  pla.nature, max(case when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " then 'Credit To Organization' "
                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " then 'Credit To Registration'  "
                    + "         when pla.nature = " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " then 'Credit To Patient' else 'Unknown' end) as paymentModeName, "
                    + "         (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) as pn, "
                    + "         isNull(sum(pla.amount),0) as amount, max(sm.manualCode) as siteSName "
                    + "         from patientLimitApproval pla "
                    + "         left join patientRegistration pr on pr.regDocId = pla.docId "
                    + "         left join voucher_Type vt on vt.v_Type = pr.v_Type "
                    + "         left join siteMast sm on sm.code = pla.site_Code "
                    + "         where pla.v_date = :currentDate and vt.category in ('" + pvc.IPD + "') "
                    + "         and pla.nature in (" + STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ", " + STEnum.PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ") "
                    + "         and pla.isCancelled = 0 and pla.site_Code = :site_Code "
                    + "         group by pla.nature, pla.site_Code, (case when isNull(pla.amount,0) > 0 then 'p' else 'n' end) "
                    + "         order By siteSName, paymentModeName, pn desc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("currentDate", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            tempValue = 0;
            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Receipt");
            sb.append("</font></span>");
            while (rsTemp.next()) {

                sb.append("<br/><br/><span><font color='olive'>");
                sb.append(DmShared.xNull(rsTemp.getString("paymentModeName")));
                sb.append("</font></span>");

                sb.append("<br/><span><font color='purple'>");
                sb.append(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("amount")))));
                sb.append("</font></span>");

                tempValue += DmShared.vNull(rsTemp.getString("amount")).doubleValue();
            }
            rsTemp.close();
            psTemp.close();

            sb.append("<br/><br/><span><font color='fuchsia'>");
            sb.append("Total Receipt : " + DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(tempValue))));
            sb.append("</font></span>");

            misUpdateMessage(DmShared.xNull(serialKey), DmShared.xNull(eodMdl.getSiteShortName()), "IPD", sb.toString(), "");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void misUpdateMessage(String serialKey, String siteName, String module, String remark, String mac_address) {
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String finUrl;

        try {
            finUrl = "http://web.dataman.in/datamancontrolpanel/appservice.asmx/UpdateMessage?";
            List<BasicNameValuePair> listParams = new ArrayList<>();
            listParams.add(new BasicNameValuePair("serial_key", serialKey));
            listParams.add(new BasicNameValuePair("site_name", siteName));
            listParams.add(new BasicNameValuePair("module", module));
            listParams.add(new BasicNameValuePair("mac_address", mac_address));
            listParams.add(new BasicNameValuePair("remark", StringEscapeUtils.escapeHtml4(remark).replace("'", "&apos;")));

            HttpPost httpGet = new HttpPost(finUrl);

            httpGet.setEntity(new UrlEncodedFormEntity(listParams));

            httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpGet.setHeader("Accept-Language", "en-US,en;q=0.5");
            httpGet.setHeader("Cache-Control", "max-age=0");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate");
            httpGet.setHeader("Connection", "keep-alive");

            CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            System.out.println(httpResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteEOD(String eodDate, int processCode, String siteCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Delete record from eod >====================================*/
            sqlQuery = "Delete from eod where processCode = :processCode and eodDate = :eodDate and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("processCode", DmShared.iNull(processCode).toString());
            psTemp.setDate("eodDate", DmServer.convertDate(eodDate));
            psTemp.setString("site_Code", siteCode);
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    private void saveEOD(String eodDate, int processCode, String siteCode, String userName, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Insert into eod >====================================*/
            sqlQuery = "INSERT INTO eod(processCode, eodDate, site_Code, u_EntDt, u_Name)"
                    + " VALUES (:processCode, :eodDate, :site_Code, :u_EntDt, :u_Name)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("processCode", DmShared.iNull(processCode).toString());
            psTemp.setDate("eodDate", DmServer.convertDate(eodDate));
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("u_Name", userName);
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }

    private void saveEODHistory(String eodDate, int processCode, String siteCode, String userName, String remark, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            /*====================================< Insert into eodHistory >====================================*/
            sqlQuery = "INSERT INTO eodHistory(code, processCode, eodDate, site_Code, remark, u_EntDt, u_Name)"
                    + " VALUES (:code, :processCode, :eodDate, :site_Code, :remark, :u_EntDt, :u_Name)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", UUID.randomUUID().toString());
            psTemp.setString("processCode", DmShared.iNull(processCode).toString());
            psTemp.setDate("eodDate", DmServer.convertDate(eodDate));
            psTemp.setString("site_Code", siteCode);
            psTemp.setString("remark", remark);
            psTemp.setString("u_Name", userName);
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }

    }
}
