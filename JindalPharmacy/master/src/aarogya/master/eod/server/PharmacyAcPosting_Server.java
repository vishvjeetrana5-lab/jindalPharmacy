package aarogya.master.eod.server;

import aarogya.master.eod.shared.AcPosting_Mdl;
import aarogya.master.eod.shared.AcPosting_Vld;
import aarogya.master.eod.shared.EOD_Mdl;
import aarogya.master.eod.shared.GrdAcPostedDetail_Mdl;
import aarogya.master.eod.shared.GrdAcPosting_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
import aarogya.selfToolkit.stStatic.shared.STEnum.ProcessEOD;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmEnum.CardType;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PharmacyAcPosting_Server extends AbstractEventHandler<Event> {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCatAP = pvc.PHARMACY_AC_POSTING;
    private String vCat_Service = "'" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "'";
    private String vCat_Receipt = "'" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "'";
    private String vCat_Sale = "'" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "'";

    public PharmacyAcPosting_Server() {
        super(ProcessEOD.PHARMACY_AC_POSTING.getValue());
    }

    @Override
    public void handle(Event event) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        SchedulerMast_Mdl schedulerMast_Mdl;
        EOD_Mdl eodMdl = new EOD_Mdl();
        String sqlQuery = "";
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

                    /*=========================< Get Control Ac >========================*/
                    sqlQuery = "Select  sm.manualCode, e.pharmacyControlAc, sg.name as controlAcName, e.pharmacySelfAc, sgs.name as pharmacySelfAcName, "
                            + "         e.pharmacyHospitalAc, sgh.name as pharmacyHospitalAcName "
                            + "		from siteMast as sm "
                            + "		left join enviro as e on sm.code = e.site_Code "
                            + "         left join subGroup as sg on e.pharmacyControlAc = sg.subCode "
                            + "         left join subGroup as sgs on e.pharmacySelfAc = sgs.subCode "
                            + "         left join subGroup as sgh on e.pharmacyHospitalAc = sgh.subCode "
                            + "         where sm.code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", eodMdl.getSiteCode());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        eodMdl.setSiteShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                        eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("pharmacyControlAc")));
                        eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                        eodMdl.setSelfAc(DmShared.xNull(rsTemp.getString("pharmacySelfAc")));
                        eodMdl.setSelfAcName(DmShared.xNull(rsTemp.getString("pharmacySelfAcName")));
                        eodMdl.setHospitalAc(DmShared.xNull(rsTemp.getString("pharmacyHospitalAc")));
                        eodMdl.setHospitalAcName(DmShared.xNull(rsTemp.getString("pharmacyHospitalAcName")));
                    }
                    rsTemp.close();
                    psTemp.close();

                    if (DmShared.iNull(eodMdl.getSelfAc()).longValue() == 0l) {
                        throw new DmException(0, "Please set Pharmacy A/c in Environment Setting.");
                    }
                    if (DmShared.iNull(eodMdl.getHospitalAc()).longValue() == 0l) {
                        throw new DmException(0, "Please set Hospital A/c in Environment Setting.");
                    }
                    if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                        throw new DmException(0, "Please set Pharmacy Control A/c in Environment Setting.");
                    }

                    /*====================================< Save Account Posting >====================================*/
                    AcPosting_Mdl posting_Mdl = new AcPosting_Mdl();
                    posting_Mdl.setForDate(eodMdl.getCurrentDate());
                    save(posting_Mdl, eodMdl, cnTemp);
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

    public void save(AcPosting_Mdl mrTemp, EOD_Mdl eodMdl, Connection cnTemp) {
        List<GrdAcPosting_Mdl> listNode = new ArrayList<GrdAcPosting_Mdl>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, voucherType, serviceDocId = "", receiptDocId = "";

        try {
            voucherType = DmServer.getVType(vCatAP, cnTemp);

            if (DmShared.iNull(eodMdl.getSelfAc()).longValue() == 0l) {
                throw new DmException(0, "Please set Pharmacy A/c in Environment Setting.");
            }
            if (DmShared.iNull(eodMdl.getHospitalAc()).longValue() == 0l) {
                throw new DmException(0, "Please set Hospital A/c in Environment Setting.");
            }
            if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                throw new DmException(0, "Please set Pharmacy Control A/c in Environment Setting.");
            }

            /*====================================< Get voucher info >====================================*/
            sqlQuery = "Select docId, v_No, recId, recIdPrefix from saleBill Where v_Date = :v_Date and v_Type = :v_Type and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setString("v_Type", voucherType);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")).trim());
                mrTemp.setObjRecId(new RecId());
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")).trim());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")).trim());
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
            }
            rsTemp.close();
            psTemp.close();

            /*===============< Delete EOD >==============*/
            delete(eodMdl, cnTemp, false);

            listNode.addAll(getReceiptPostingDetail(eodMdl, cnTemp));
            listNode.addAll(getBankPostingDetail(eodMdl, cnTemp));
            listNode.addAll(getServicePostingDetail(eodMdl, cnTemp));
//            listNode.addAll(getCTR_CTO_ServicePostingDetail(eodMdl, cnTemp));
            listNode.addAll(getSaleStockPostingDetail(eodMdl, cnTemp));

            AcPosting_Vld.chkSave(mrTemp);
            AcPosting_Vld.chkPostingData(listNode);

            /*====================================< Get docId's of patientReceipt >====================================*/
            sqlQuery = "Select	prc.docId "
                    + "		from patientReceipt as prc "
                    + "		left join voucher_Type as vt on vt.v_Type = prc.v_Type "
                    + "		where vt.category in (" + vCat_Receipt + ") "
                    + "		and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date = :v_Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                receiptDocId += "'" + DmShared.iNull(rsTemp.getString("docId")).toString() + "',";
            }
            rsTemp.close();
            psTemp.close();

            if (receiptDocId.trim().contains(",")) {
                receiptDocId = receiptDocId.substring(0, receiptDocId.length() - 1);
            }

            /*====================================< Get docId's of saleBill >====================================*/
            sqlQuery = "select	mb.docId "
                    + "		from saleBill as mb "
                    + "		left join saleBillDetail as mbd on mb.docId = mbd.docId "
                    + "		left join voucher_Type as vt on vt.v_Type = mb.v_type "
                    + "		where vt.category in (" + vCat_Service + ") "
                    + "		and mb.isCancelled = 0 and mbd.eodSbDocId is null and mb.site_Code = :site_Code and mb.v_Date = :v_Date "
                    + "		group by mb.docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                serviceDocId += "'" + DmShared.iNull(rsTemp.getString("docId")).toString() + "',";
            }
            rsTemp.close();
            psTemp.close();

            if (serviceDocId.trim().contains(",")) {
                serviceDocId = serviceDocId.substring(0, serviceDocId.length() - 1);
            }

            if (serviceDocId.trim().length() == 0 && receiptDocId.trim().length() == 0) {
                throw new DmException(0, "No record found.");
            }

            if (DmShared.xNull(serviceDocId).trim().equalsIgnoreCase("")) {
                serviceDocId = "0";
            }

            if (DmShared.xNull(receiptDocId).trim().equalsIgnoreCase("")) {
                receiptDocId = "0";
            }

            /*====================================< Check whether already posted or not >====================================*/
            sqlQuery = "Select	psd.docId "
                    + "		from saleBillDetail as psd "
                    + "		left join saleBill as sb on psd.eodSbDocId = sb.docId "
                    + "		where psd.docId in (" + serviceDocId + ") and isNull(sb.approvedBy,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Record has been already posted..");
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Check whether already posted or not >====================================*/
            sqlQuery = "Select	prec.docId "
                    + "		from patientReceipt as prec "
                    + "		left join saleBill as sb on prec.sbDocId = sb.docId "
                    + "		where prec.docId in (" + receiptDocId + ") and isNull(sb.approvedBy,'') <> '' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new DmException(0, "Record has been already posted..");
            }
            rsTemp.close();
            psTemp.close();

            if (listNode.size() > 0) {
                if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0l) {
                    mrTemp = new AcPosting_Mdl();
                    mrTemp.setSharedKeyFieldCode(DmServer.getDocId(voucherType, eodMdl.getVoucherPrefix(), eodMdl.getSiteCode(), cnTemp));
                    mrTemp.setObjRecId(DmServer.getRecId("saleBill", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), eodMdl.getCurrentDate(), "", DmShared.xNull(voucherType), eodMdl.getVoucherPrefix(), eodMdl.getSiteCode(), eodMdl.getSiteShortName(), eodMdl.getCompanyCode(), cnTemp, true, null));
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));

                    /*====================================< Insert records into saleBill >====================================*/
                    sqlQuery = "Insert Into saleBill(docId, v_Type, v_Date, v_Prefix, v_No, recId, recIdPrefix, "
                            + " subCode, site_Code, transferDt, preparedBy, approvedBy, u_EntDt) "
                            + " values (:docId, :v_Type, :v_Date,  :v_Prefix, :v_No, :recId, :recIdPrefix, "
                            + " :subCode, :site_Code, :transferDt, :preparedBy, :preparedBy, :u_EntDt) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_Type", DmShared.foriegnKey(voucherType));
                    psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
                    psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
                    psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                    psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                    psTemp.setString("subCode", null);
                    psTemp.setString("site_Code", DmShared.foriegnKey(eodMdl.getSiteCode()));
                    psTemp.setString("transferDt", null);
                    psTemp.setString("preparedBy", eodMdl.getUserName());
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                    psTemp.executeUpdate();
                    psTemp.close();
                }

                /*====================================< Delete records from ledger >====================================*/
                sqlQuery = "Delete From ledger Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                int count = 0;
                double amount = 0;
                String subCode, subCodeName, contraCode, contraName;
                for (GrdAcPosting_Mdl node : listNode) {
                    if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {

                        if (node.isReceipt()) {
                            if (DmShared.vNull(node.getAmount()).doubleValue() >= 0) {
                                subCode = DmShared.xNull(node.getSharedKeyFieldCode());
                                subCodeName = DmShared.xNull(node.getSharedKeyFieldName());
                                contraCode = DmShared.xNull(node.getContraSubCode());
                                contraName = DmShared.xNull(node.getContraSubName());
                            } else {
                                subCode = DmShared.xNull(node.getContraSubCode());
                                subCodeName = DmShared.xNull(node.getContraSubName());
                                contraCode = DmShared.xNull(node.getSharedKeyFieldCode());
                                contraName = DmShared.xNull(node.getSharedKeyFieldName());
                            }
                        } else {
                            if (DmShared.vNull(node.getAmount()).doubleValue() >= 0) {
                                subCode = DmShared.xNull(node.getContraSubCode());
                                subCodeName = DmShared.xNull(node.getContraSubName());
                                contraCode = DmShared.xNull(node.getSharedKeyFieldCode());
                                contraName = DmShared.xNull(node.getSharedKeyFieldName());
                            } else {
                                subCode = DmShared.xNull(node.getSharedKeyFieldCode());
                                subCodeName = DmShared.xNull(node.getSharedKeyFieldName());
                                contraCode = DmShared.xNull(node.getContraSubCode());
                                contraName = DmShared.xNull(node.getContraSubName());
                            }
                        }
                        amount = Math.abs(DmShared.vNull(node.getAmount()).doubleValue());
                        if (!DmShared.xNull(subCode).trim().equalsIgnoreCase(DmShared.xNull(contraCode).trim())) {
                            for (int i = 0; i < 2; i++) {
                                /*====================================< Insert records into ledger >====================================*/
                                sqlQuery = "Insert into ledger (docId, v_Sno, v_Type, v_Prefix, v_Date, v_No, recId, recIdPrefix, narration, "
                                        + " subCode, contraSub, contraText, amtDr, amtCr, site_Code, isPosted, chq_No, chq_Date, favourOf) "
                                        + " values (:docId, :v_Sno, :v_Type, :v_Prefix, :v_Date, :v_No, :recId, :recIdPrefix, :narration, "
                                        + " :subCode, :contraSub, :contraText, :amtDr, :amtCr, :site_Code, :isPosted, :chq_No, :chq_Date, :favourOf) ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                                psTemp.setInt("v_Sno", ++count);
                                psTemp.setString("v_Type", DmShared.foriegnKey(voucherType));
                                psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
                                psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
                                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                                psTemp.setString("narration", DmShared.xNull(node.getNarration()).trim());
                                psTemp.setDate("chq_Date", DmServer.convertDate(node.getChqDate()));
                                psTemp.setString("chq_No", DmShared.xNull(node.getChqNo()).trim());
                                psTemp.setString("favourOf", DmShared.xNull(node.getFavourOff()).trim());

                                if (i == 0) {
                                    psTemp.setString("subCode", DmShared.foriegnKey(subCode));
                                    psTemp.setString("contraSub", DmShared.foriegnKey(contraCode));
                                    psTemp.setString("contraText", contraName);
                                    psTemp.setString("amtDr", DmServer.vNull(amount, "0.00"));
                                    psTemp.setString("amtCr", "0");
                                } else {
                                    psTemp.setString("subCode", DmShared.foriegnKey(contraCode));
                                    psTemp.setString("contraSub", DmShared.foriegnKey(subCode));
                                    psTemp.setString("contraText", subCodeName);
                                    psTemp.setString("amtDr", "0");
                                    psTemp.setString("amtCr", DmServer.vNull(amount, "0.00"));
                                }
                                if (node.isCtr()) {
                                    psTemp.setString("site_Code", DmShared.foriegnKey(node.getSiteCode()));
                                } else {
                                    psTemp.setString("site_Code", DmShared.foriegnKey(eodMdl.getSiteCode()));
                                }
                                psTemp.setString("isPosted", "1");
                                psTemp.executeUpdate();
                                psTemp.close();
                            }
                        }
                    }
                }

                /*====================================< Update saleBillDetail >====================================*/
                sqlQuery = "Update saleBillDetail set eodSbDocId = :sbDocId where docId in (" + serviceDocId + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("sbDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                /*====================================< Update patientReceipt >====================================*/
                sqlQuery = "Update patientReceipt set sbDocId = :sbDocId where docId in (" + receiptDocId + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("sbDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                postIPD_CTR_PharmacyCharges(mrTemp, voucherType, eodMdl, cnTemp);

                /*====================================< Delete EOD >====================================*/
                deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.PHARMACY_AC_POSTING.getId(), eodMdl.getSiteCode(), cnTemp);

                /*====================================< Save EOD >====================================*/
                saveEOD(eodMdl.getCurrentDate(), ProcessEOD.PHARMACY_AC_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), cnTemp);

                /*====================================< Save EOD History >====================================*/
                saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.PHARMACY_AC_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Added successfully.", cnTemp);
            }
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

    private void postIPD_CTR_PharmacyCharges(AcPosting_Mdl mrTemp, String VoucherType, EOD_Mdl eodMdl, Connection cnTemp) {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        String sqlQuery = "";
        String fromBillNo = "", toBillNo = "", drAccountName = "", drAccountCode = "", crAccountName = "", crAccountCode = "";
        double amount = 0;
        int count = 0;

        String maxVsno = "";
        try {

            maxVsno = DmServer.executeScalar("select max(v_SNo) as maxVsno from ledger where docId=" + DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString() + "", cnTemp);

            count = DmShared.iNull(maxVsno).intValue();

            sqlQuery = " select max(vt.parentVoucher)  parentVoucher , sum(sb.oh_Amt_Net) as amount, min(sb.recId) as fromBillNo,max(sb.recId)  toBillNo ,max(sb.v_No)  v_No ,max(sb.recIdPrefix)  recIdPrefix  \n"
                    + "  ,sb.docId,max(sb.v_Type) v_Type , max(sb.regNo_HIS) as regNo_HIS   \n"
                    + " ,max(sgdr.name) drAccountName,max(sgdr.subCode) as drAccountCode,max(sgcr.name) crAccountName,max(sgcr.subcode) as crAccountCode\n"
                    + "\n"
                    + "from saleBill sb \n"
                    + "left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "left join customEnviro as ce on ce.site_Code=sb.siteCode_HIS\n"
                    + "left join subGroup sgdr on sgDr.subCode=ce.outPharmacyCustomerAc\n"
                    + "left join subGroup sgCr on sgCr.subCode=ce.outPharmacySalesAc\n"
                    + "where sb.v_Date= :v_Date  and vt.parentVoucher in ('sale','Sale Return') and sb.paymentSource=40 and sb.regType_HIS='IPD' \n"
                    + "\n"
                    + "and isnull(sb.ispostedAsOutSource,0)=0 "
                    //                    + " group by sb.siteCode_HIS ";
                    + " group by sb.siteCode_HIS,sb.docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                amount = Math.abs(DmShared.vNull(rsTemp.getString("amount")));

                fromBillNo = DmShared.xNull(rsTemp.getString("fromBillNo"));
                toBillNo = DmShared.xNull(rsTemp.getString("toBillNo"));
                drAccountName = DmShared.xNull(rsTemp.getString("drAccountName"));
                drAccountCode = DmShared.xNull(rsTemp.getString("drAccountCode"));
                crAccountName = DmShared.xNull(rsTemp.getString("crAccountName"));
                crAccountCode = DmShared.xNull(rsTemp.getString("crAccountCode"));
//=========================================== Insert into ledger================================================================

                //============================================Insert Into Indler=====================
                if (amount != 0 && !drAccountCode.equalsIgnoreCase("") && !crAccountCode.equalsIgnoreCase("")) {
                    for (int i = 0; i < 2; i++) {
                        /*====================================< Insert records into ledger >====================================*/
                        sqlQuery = "Insert into ledger (docId, v_Sno, v_Type, v_Prefix, v_Date, v_No, recId, recIdPrefix, narration, "
                                + " subCode, contraSub, contraText, amtDr, amtCr, site_Code,isPosted ) "
                                + " values (:docId, :v_Sno, :v_Type, :v_Prefix, :v_Date, :v_No, :recId, :recIdPrefix, :narration, "
                                + " :subCode, :contraSub, :contraText, :amtDr, :amtCr, :site_Code ,:isPosted) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//                    psTemp.setString("docId", DmShared.iNull(rsTemp.getString("docId")).toString());
                        psTemp.setInt("v_Sno", ++count);
                        psTemp.setString("v_Type", DmShared.foriegnKey(VoucherType));
//                    psTemp.setString("v_Type", DmShared.foriegnKey(rsTemp.getString("v_Type")));
                        psTemp.setString("v_Prefix", eodMdl.getVoucherPrefix());
                        psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
//                    psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                        psTemp.setString("v_No", DmShared.xNull(rsTemp.getString("v_No")));
//                    psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                        psTemp.setString("recId", DmShared.xNull(fromBillNo));
//                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                        psTemp.setString("recIdPrefix", DmShared.xNull(rsTemp.getString("recIdPrefix")));
//                    psTemp.setString("narration", DmShared.xNull("From Bill No. " + fromBillNo + " to " + toBillNo).trim());
                        psTemp.setString("narration", DmShared.xNull("IPD No. " + DmShared.xNull(rsTemp.getString("regNo_HIS")) + " Payment Mode : CTR").trim());

                        if (DmShared.xNull(rsTemp.getString("parentVoucher")).equalsIgnoreCase("Sale")) {
                            if (i == 0) {
                                psTemp.setString("subCode", DmShared.foriegnKey(drAccountCode));
                                psTemp.setString("contraSub", DmShared.foriegnKey(crAccountCode));
                                psTemp.setString("contraText", "");
                                psTemp.setString("amtDr", DmServer.vNull(amount, "0.00"));
                                psTemp.setString("amtCr", "0");
                            } else {
                                psTemp.setString("subCode", DmShared.foriegnKey(crAccountCode));
                                psTemp.setString("contraSub", DmShared.foriegnKey(drAccountCode));
                                psTemp.setString("contraText", "");
                                psTemp.setString("amtDr", "0");
                                psTemp.setString("amtCr", DmServer.vNull(amount, "0.00"));
                            }
                        } else if (DmShared.xNull(rsTemp.getString("parentVoucher")).equalsIgnoreCase("Sale Return")) {
                            if (i == 0) {
                                psTemp.setString("subCode", DmShared.foriegnKey(drAccountCode));
                                psTemp.setString("contraSub", DmShared.foriegnKey(crAccountCode));
                                psTemp.setString("contraText", "");
                                psTemp.setString("amtDr", "0");
                                psTemp.setString("amtCr", DmServer.vNull(amount, "0.00"));

                            } else {
                                psTemp.setString("subCode", DmShared.foriegnKey(crAccountCode));
                                psTemp.setString("contraSub", DmShared.foriegnKey(drAccountCode));
                                psTemp.setString("contraText", "");
                                psTemp.setString("amtDr", DmServer.vNull(amount, "0.00"));
                                psTemp.setString("amtCr", "0");

                            }
                        }

                        psTemp.setString("site_Code", "1");
                        psTemp.setString("isPosted", "1");

                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }

//============================================================================================================
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
        }

    }

    public void delete(EOD_Mdl eodMdl, Connection cnTemp, boolean isEODDelete) {
        DmPreparedStatement psTemp = null;
        String sqlQuery, voucherType, eodDocId = "";
        ResultSet rsTemp = null;

        try {
            voucherType = DmServer.getVType(vCatAP, cnTemp);

            /*====================================< Get EOD DocId >====================================*/
            sqlQuery = "Select docId from saleBill Where v_Date = :v_Date and v_Type = :v_Type and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setString("v_Type", voucherType);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                eodDocId = DmShared.xNull(rsTemp.getString("docId"));
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Update saleBillDetail >====================================*/
            sqlQuery = "Update  mbd set mbd.eodSbDocId = :sbDocId "
                    + "         from saleBillDetail as mbd "
                    + "         left join saleBill as lm on mbd.eodSbDocId = lm.docId "
                    + "         where lm.v_Date = :v_Date and lm.v_Type = :v_Type "
                    + "         and lm.docId = :docId and lm.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("docId", DmShared.iNull(eodDocId).toString());
            psTemp.setString("sbDocId", null);
            psTemp.setString("site_Code", eodMdl.getSiteCode());

            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Update patientReceipt >====================================*/
            sqlQuery = "Update  prec set prec.sbDocId = :sbDocId "
                    + "         from patientReceipt as prec "
                    + "         left join saleBill as lm on prec.sbDocId = lm.docId "
                    + "         where lm.v_Date = :v_Date and lm.v_Type = :v_Type "
                    + "         and lm.docId = :docId and prec.site_Code = :site_Code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("docId", DmShared.iNull(eodDocId).toString());
            psTemp.setString("sbDocId", null);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from ledger >====================================*/
            sqlQuery = "Delete From ledger "
                    + " Where v_Date = :v_Date and v_Type = :v_Type "
                    + " and docId = :docId and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("docId", DmShared.iNull(eodDocId).toString());
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from ledgerM >====================================*/
//            sqlQuery = "Delete From saleBill Where v_Date = :v_Date and v_Type = :v_Type ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
//            psTemp.setString("v_Type", voucherType);
//            psTemp.executeUpdate();
            psTemp.close();

            if (isEODDelete) {
                /*====================================< Delete EOD >====================================*/
                deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.PHARMACY_AC_POSTING.getId(), eodMdl.getSiteCode(), cnTemp);

                /*====================================< Save EOD History >====================================*/
                saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.PHARMACY_AC_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Deleted successfully.", cnTemp);
            }

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

    public List<KeyFieldExtended> getPendingDates(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<KeyFieldExtended> lstPendingDate = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get pending Dates >====================================*/
//            sqlQuery = "Select	temp.v_Date "
//                    + "		from ( "
//                    + "			select  prc.v_Date "
//                    + "                         from patientReceipt as prc "
//                    + "				left join cardLedger as cl on prc.docId = cl.docId "
//                    + "				left join patientRegistration as pr on pr.regDocId = prc.regDocId "
//                    + "				left join voucher_Type as vt on vt.v_Type = prc.v_Type "
//                    + "				where vt.category in (" + vCat_Receipt + ") "
//                    + "				and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date < :v_Date "
//                    + "			union all "
//                    + "			select	mb.v_Date "
//                    + "                         from patientLimitApproval as pla "
//                    + "				left join saleBill as mb on mb.docId = pla.docId  "
//                    + "				left join saleBillDetail as mbd on mb.docId = mbd.docId "
//                    + "				left join voucher_Type as vt on vt.v_Type = mb.v_type "
//                    + "                         where pla.nature IN (:ctrNature,:ctoNature) and pla.isCancelled = 0 "
//                    + "                         and vt.category in (" + vCat_Service + ") and mb.isCancelled = 0 and mbd.sbDocId is null  "
//                    + "                         and mb.site_Code = :site_Code and mb.v_Date < :v_Date "
//                    + "		) as temp "
//                    + "		group by temp.v_Date "
//                    + "		order by temp.v_Date ";
            sqlQuery = "Select	temp.v_Date "
                    + "		from ( "
                    + "			select  prc.v_Date "
                    + "                         from patientReceipt as prc "
                    + "				left join cardLedger as cl on prc.docId = cl.docId "
                    + "				left join patientRegistration as pr on pr.regDocId = prc.regDocId "
                    + "				left join voucher_Type as vt on vt.v_Type = prc.v_Type "
                    + "				where vt.category in (" + vCat_Receipt + ") "
                    + "				and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date < :v_Date "
                    + "			union all "
                    + "			select	mb.v_Date "
                    + "                         from saleBill as mb  "
                    + "				left join saleBillDetail as mbd on mb.docId = mbd.docId "
                    + "				left join voucher_Type as vt on vt.v_Type = mb.v_type "
                    + "                         where vt.category in (" + vCat_Service + ") "
                    + "                         and mb.isCancelled = 0 and mbd.eodSbDocId is null  " // and mb.paymentSource IN (:ctrNature,:ctoNature)
                    + "                         and mb.site_Code = :site_Code and mb.v_Date < :v_Date "
                    + "		) as temp "
                    + "		group by temp.v_Date "
                    + "		order by temp.v_Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
//            psTemp.setString("ctrNature", DmShared.iNull(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()).toString());
//            psTemp.setString("ctoNature", DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setSharedKeyFieldName(node.getSharedKeyFieldCode());
                lstPendingDate.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
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
        return lstPendingDate;
    }

    public List<GrdAcPosting_Mdl> getReceiptPostingDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdAcPosting_Mdl> listNode = new ArrayList<GrdAcPosting_Mdl>();
        GrdAcPosting_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Sale Receipt Detail >====================================*/
            sqlQuery = "Select	temp.serviceAc, max(sg.name) as serviceAcName, sum(temp.amount) as amount,  "
                    + "         stuff((\n"
                    + "			select	',{' + concat(min(prc.recId), ' To ', max(prc.recId))+'}' \n"
                    + "				from patientReceipt prc \n"
                    + "				left join voucher_Type vt on vt.v_Type = prc.v_type \n"
                    + "				left join cardLedger cl on prc.docId = cl.docId \n"
                    + "				left join cardTypeMast ctm on ctm.code = cl.cardTypeCode \n"
                    + "				where vt.category in (" + vCat_Receipt + ") "
                    + "                         and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date = :v_Date "
                    + "                         and ctm.cardType <> :cardType "
                    + "				group by prc.v_Type \n"
                    + "				order by max(vt.short_Name) \n"
                    + "				for xml path(''), type).value('.[1]','nvarchar(max)'), 1,1,'') as narration, "
                    + "         temp.controlAc, max(sgca.name) as controlAcName, max(temp.serviceName) as serviceName, "
                    + "         max(temp.site_Code) as siteCode, max(sm.name) as siteName "
                    + "		from ( "
                    + "			select	cl.subCode as serviceAc, isNull(cl.amtDr,0) - isNull(cl.amtCr,0) as amount, prc.site_Code, "
                    + "                         case when lm.subCode is null then " + DmShared.iNull(eodMdl.getControlAc()).toString() + " else lm.subCode end as controlAc, "
                    + "                         ctm.name as serviceName,vt.category "
                    + "				from patientReceipt as prc "
                    + "				left join ledgerMast as lm on prc.lmCode = lm.code "
                    + "				left join cardLedger as cl on prc.docId = cl.docId "
                    + "				left join cardTypeMast as ctm on ctm.code = cl.cardTypeCode "
                    + "				left join voucher_Type as vt on vt.v_Type = prc.v_Type "
                    + "				where vt.category in (" + vCat_Receipt + ") "
                    + "                         and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date = :v_Date "
                    + "                         and ctm.cardType <> :cardType "
                    + "		) as temp "
                    + "		left join subGroup sg on sg.subcode = temp.serviceAc "
                    + "		left join subGroup sgca on sgca.subcode = temp.controlAc "
                    + "		left join siteMast as sm on temp.site_Code = sm.code "
                    + "		group by temp.serviceAc, temp.controlAc,temp.category "
                    + "         order by max(sg.name), temp.serviceAc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setInt("cardType", CardType.BANK.getEnumValue());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAcPosting_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("serviceAc")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceAcName")));
                node.setContraSubCode(DmShared.iNull(rsTemp.getString("controlAc")).toString());
                node.setContraSubName(DmShared.xNull(rsTemp.getString("controlAcName")));
                node.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                node.setSiteCode(DmShared.xNull(rsTemp.getString("siteCode")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00").equalsIgnoreCase("0.00") ? "" : DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                node.setReceipt(true);
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
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
        return listNode;
    }

    public List<GrdAcPosting_Mdl> getServicePostingDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdAcPosting_Mdl> listNode = new ArrayList<GrdAcPosting_Mdl>();
        GrdAcPosting_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, sqlQueryTemp;
        String cGSTAc = "", sGSTAc = "", uGSTAc = "", iGSTAc = "";
        boolean isIncludeTaxInCost = false;
        try {
            /*====================================< Get Service Detail >====================================*/
//            sqlQuery = "Select	temp.serviceAc, max(sg.name) as serviceAcName, sum(temp.amount) as amount, '' as narration, "
//                    + "		temp.controlAc, max(sgca.name) as controlAcName, max(temp.billNo) as billNo, "
//                    + "		max(temp.site_Code) as siteCode, max(sm.name) as siteName "
//                    + "		from ( "
//                    + "			select	(case when spl.typeCode = 1 then mbd.oh_Ac_Sales "
//                    + "                         when spl.typeCode = 2 then mbd.oh_Ac_CGST "
//                    + "				when spl.typeCode = 3 then mbd.oh_Ac_SGST "
//                    + "				when spl.typeCode = 4 then mbd.oh_Ac_UGST "
//                    + "				when spl.typeCode = 5 then mbd.oh_Ac_IGST end) as serviceAc, "
//                    + "				case when mb.subCode is null then " + DmShared.iNull(eodMdl.getControlAc()).toString() + " else mb.subCode end as controlAc, "
//                    + "				(case when spl.typeCode = 1 then mbd.oh_Amt_Taxable "
//                    + "				when spl.typeCode = 2 then mbd.oh_Amt_CGST "
//                    + "				when spl.typeCode = 3 then mbd.oh_Amt_SGST "
//                    + "				when spl.typeCode = 4 then mbd.oh_Amt_UGST "
//                    + "				when spl.typeCode = 5 then mbd.oh_Amt_IGST end) as amount, "
//                    + "				mb.recId as billNo, mb.site_Code "
//                    + "				from saleBill as mb "
//                    + "				left join saleBillDetail as mbd on mb.docId = mbd.docId "
//                    + "				left join voucher_Type as vt on vt.v_Type = mb.v_type "
//                    + "				left join ( "
//                    + "                                 select  1 as typeCode "
//                    + "                                 union all "
//                    + "					select 2 as typeCode "
//                    + "					union all "
//                    + "					select 3 as typeCode "
//                    + "					union all "
//                    + "					select 4 as typeCode "
//                    + "					union all "
//                    + "					select 5 as typeCode "
//                    + "				) as spl on 1 = 1 "
//                    + "				where vt.category in (" + vCat_Service + ") "
//                    + "				and mb.isCancelled = 0 and mbd.sbDocId is null and mb.site_Code = :site_Code and mb.v_Date = :v_Date "
//                    + "		) as temp "
//                    + "		left join subGroup as sg on temp.serviceAc = sg.subCode "
//                    + "		left join siteMast as sm on temp.site_Code = sm.code "
//                    + "		left join subGroup as sgca on temp.controlAc = sgca.subCode "
//                    + "         where isNull(temp.amount,0) <> 0 "
//                    + "		group by temp.serviceAc, temp.controlAc "
//                    + "		order by  max(sgca.name), max(sg.name), temp.serviceAc ";
            sqlQueryTemp = "SELECT e.isIncludeTaxInCost \n"
                    + "FROM enviro e \n"
                    + "WHERE e.site_Code = " + eodMdl.getSiteCode();
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQueryTemp);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isIncludeTaxInCost") == null) {
                    throw new DmException(0, "Include Tax in Cost is not defined in Environment Setting.");
                }

                isIncludeTaxInCost = (DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();

            if (isIncludeTaxInCost) {
                cGSTAc = " mbd.oh_Ac_Sales ";
                sGSTAc = " mbd.oh_Ac_Sales ";
                uGSTAc = " mbd.oh_Ac_Sales ";
                iGSTAc = " mbd.oh_Ac_Sales ";
            } else {
                cGSTAc = " mbd.oh_Ac_CGST ";
                sGSTAc = " mbd.oh_Ac_SGST ";
                uGSTAc = " mbd.oh_Ac_UGST ";
                iGSTAc = " mbd.oh_Ac_IGST ";
            }

            String amountField = "(case when spl.typeCode = 1 then ISNULL(mbd.oh_Amt_Taxable,0) \n"
                    + "				when spl.typeCode = 2 then ISNULL(mbd.oh_Amt_CGST,0) \n"
                    + "				when spl.typeCode = 3 then ISNULL(mbd.oh_Amt_SGST,0) \n"
                    + "				when spl.typeCode = 4 then ISNULL(mbd.oh_Amt_UGST,0) \n"
                    + "				when spl.typeCode = 5 then ISNULL(mbd.oh_Amt_IGST,0) end) \n";
            sqlQuery = "Select	temp.serviceAc, max(sg.name) as serviceAcName, sum(temp.amount) as amount, \n";
            /*===========================Club Gross+Tax amt when Tax included in cost in Enviro====================*/
            if (isIncludeTaxInCost) {
                sqlQuery += " case when (max(temp.typeCode) = 1 or max(temp.typeCode) = 2 or max(temp.typeCode) = 3 or max(temp.typeCode) = 4) then ";
            } else {
                sqlQuery += " case when temp.typeCode = 1 then ";
            }

            sqlQuery += "stuff((\n select  ',{' + concat(min(ps.recId), ' To ', max(ps.recId))+'}'\n"
                    + "				from saleBill ps \n"
                    //                    + "				left join patientServiceDetail psd on psd.docId = ps.docId\n"
                    + "				left join voucher_Type vt on vt.v_Type = ps.v_type\n"
                    + "				where (vt.category in (" + vCat_Service + ")) and (ps.isCancelled = 0)  \n"
                    + "                         and (ps.site_Code = :site_Code) and (ps.v_Date = :v_Date) \n"
                    + "				group by ps.v_Type\n"
                    + "				order by max(vt.short_name), max(ps.v_no), max(ps.recId)\n"
                    + "				for xml path(''), type).value('.[1]','nvarchar(max)'), 1,1,'') else '' end as narration, \n"
                    + "		temp.controlAc, max(sgca.name) as controlAcName, max(temp.billNo) as billNo, \n"
                    + "		max(temp.site_Code) as siteCode, max(sm.name) as siteName \n"
                    + "		from ( \n"
                    + "			select	(case when spl.typeCode = 1 then mbd.oh_Ac_Sales \n"
                    + "                         when spl.typeCode = 2 then " + cGSTAc + " \n"
                    + "				when spl.typeCode = 3 then " + sGSTAc + " \n"
                    + "				when spl.typeCode = 4 then " + uGSTAc + " \n"
                    + "				when spl.typeCode = 5 then " + iGSTAc + " end) as serviceAc, \n"
                    + "				case when mb.subCode is null then " + DmShared.iNull(eodMdl.getControlAc()).toString() + " else mb.subCode end as controlAc, \n"
                    + "				CASE WHEN vt.category in('" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "') THEN -" + amountField + " ELSE " + amountField + " END as amount, \n"
                    + "				mb.recId as billNo, mb.site_Code,vt.category,spl.TypeCode \n"
                    + "				from saleBill as mb \n"
                    + "				left join saleBillDetail as mbd on mb.docId = mbd.docId \n"
                    + "				left join voucher_Type as vt on vt.v_Type = mb.v_type \n"
                    + "				left join ( \n"
                    + "                                 select  1 as typeCode \n"
                    + "                                 union all \n"
                    + "					select 2 as typeCode \n"
                    + "					union all \n"
                    + "					select 3 as typeCode \n"
                    + "					union all \n"
                    + "					select 4 as typeCode \n"
                    + "					union all \n"
                    + "					select 5 as typeCode \n"
                    + "				) as spl on 1 = 1 \n"
                    + "				where vt.category in (" + vCat_Service + ") \n"
                    + "				and mb.isCancelled = 0 and mbd.eodSbDocId is null and mb.site_Code = :site_Code and mb.v_Date = :v_Date \n"
                    + "		) as temp \n"
                    + "		left join subGroup as sg on temp.serviceAc = sg.subCode \n"
                    + "		left join siteMast as sm on temp.site_Code = sm.code \n"
                    + "		left join subGroup as sgca on temp.controlAc = sgca.subCode \n"
                    + "         where isNull(temp.amount,0) <> 0 \n"
                    + "		group by temp.serviceAc, temp.controlAc,temp.category \n";
            if (!isIncludeTaxInCost) {
                sqlQuery += ",temp.typeCode \n";
            }
            sqlQuery += " order by  max(sgca.name), max(sg.name), temp.serviceAc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAcPosting_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("serviceAc")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceAcName")));
                node.setContraSubCode(DmShared.iNull(rsTemp.getString("controlAc")).toString());
                node.setContraSubName(DmShared.xNull(rsTemp.getString("controlAcName")));
                node.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("billNo")));
                node.setSiteCode(DmShared.xNull(rsTemp.getString("siteCode")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00").equalsIgnoreCase("0.00") ? "" : DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                node.setReceipt(false);
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
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

        return listNode;
    }

    public List<GrdAcPosting_Mdl> getCTR_CTO_ServicePostingDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdAcPosting_Mdl> listNode = new ArrayList<GrdAcPosting_Mdl>();
        GrdAcPosting_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Item Detail >====================================*/
//            sqlQuery = "Select	t.caseType, t.serviceAc, sgs.name as serviceAcName, t.controlAc, sgc.name as controlAcName,  "
//                    + "		t.billNo, t.narration, t.amount, t.site_Code as siteCode, sm.name as siteName "
//                    + "		from ( "
//                    + "			Select	2 as caseType, temp.serviceAc, sum(temp.amount) as amount, '' as narration, "
//                    + "				temp.controlAc, '' as billNo, max(temp.site_Code) as site_Code "
//                    + "                         from ( "
//                    + "                             select  " + DmShared.iNull(eodMdl.getControlAc()).toString() + " as serviceAc, "
//                    + "                                     " + DmShared.iNull(eodMdl.getHospitalAc()).toString() + " as controlAc, "
//                    + "                                     (isNull(mbd.oh_Amt_Taxable,0) + isNull(mbd.oh_Amt_CGST,0) +isNull(mbd.oh_Amt_SGST,0) "
//                    + "                                     +isNull(mbd.oh_Amt_UGST,0) +isNull(mbd.oh_Amt_IGST,0) ) as amount, "
//                    + "                                     mb.site_Code "
//                    + "                                     from patientLimitApproval as pla  "
//                    + "                                     left join saleBill as mb on mb.docId = pla.docId "
//                    + "                                     left join saleBillDetail as mbd on mb.docId = mbd.docId "
//                    + "                                     left join voucher_Type as vt on vt.v_Type = mb.v_type "
//                    + "                                     where pla.nature IN (:ctrNature,:ctoNature) and pla.isCancelled = 0 "
//                    + "                                     and vt.category in (" + vCat_Service + ") and mb.isCancelled = 0 and mbd.sbDocId is null  "
//                    + "                                     and mb.site_Code = :site_Code and mb.v_Date = :v_Date  "
//                    + "				) as temp "
//                    + "				group by temp.serviceAc, temp.controlAc, temp.site_Code "
//                    + "			UNION ALL "
//                    + "			Select	3 as caseType, temp.serviceAc, sum(temp.amount) as amount, '' as narration, "
//                    + "				temp.controlAc, '' as billNo, max(temp.site_Code) as site_Code "
//                    + "                         from ( "
//                    + "                             select  " + DmShared.iNull(eodMdl.getSelfAc()).toString() + " as serviceAc, " //CR
//                    + "                                     psm.serviceAc as controlAc, " //DR
//                    + "                                     (isNull(mbd.oh_Amt_Taxable,0) + isNull(mbd.oh_Amt_CGST,0) +isNull(mbd.oh_Amt_SGST,0) "
//                    + "                                     +isNull(mbd.oh_Amt_UGST,0) +isNull(mbd.oh_Amt_IGST,0) ) as amount, "
//                    + "                                     pr.site_Code "
//                    + "                                     from patientLimitApproval as pla  "
//                    + "                                     left join saleBill as mb on mb.docId = pla.docId "
//                    + "                                     left join saleBillDetail as mbd on mb.docId = mbd.docId "
//                    + "                                     left join voucher_Type as vt on vt.v_Type = mb.v_type "
//                    + "                                     left join patientRegistration as pr on mb.regDocId = pr.regDocId "
//                    + "                                     left join patientServiceDetail as psd on psd.docId = mb.docId "
//                    + "                                     left join patientServiceMast psm on psm.code = psd.serviceCode "
//                    + "                                     where pla.nature IN (:ctrNature,:ctoNature) and pla.isCancelled = 0 "
//                    + "                                     and vt.category in (" + vCat_Service + ") and mb.isCancelled = 0 and mbd.sbDocId is null  "
//                    + "                                     and mb.site_Code = :site_Code and mb.v_Date = :v_Date "
//                    + "				) as temp "
//                    + "				group by temp.serviceAc, temp.controlAc, temp.site_Code "
//                    + "             ) as t "
//                    + "		left join siteMast as sm on t.site_Code = sm.code "
//                    + "		left join subgroup as sgc on sgc.subcode = t.controlAc "
//                    + "		left join subgroup as sgs on sgs.subcode = t.serviceAc "
//                    + "         where isNull(t.amount,0) <> 0 "
//                    + "		order by t.controlAc, t.serviceAc ";
            //CASE WHEN vt.category = :sbrCategory THEN -
            String amountField = "(isNull(mbd.oh_Amt_Taxable,0) + isNull(mbd.oh_Amt_CGST,0) +isNull(mbd.oh_Amt_SGST,0)+isNull(mbd.oh_Amt_UGST,0) +isNull(mbd.oh_Amt_IGST,0))";
            sqlQuery = "Select	t.caseType, t.serviceAc, sgs.name as serviceAcName, t.controlAc, sgc.name as controlAcName,  "
                    + "		t.billNo, t.narration, t.amount, t.site_Code as siteCode, sm.name as siteName "
                    + "		from ( "
                    + "			Select	2 as caseType, temp.serviceAc, sum(temp.amount) as amount, '' as narration, "
                    + "				temp.controlAc, '' as billNo, max(temp.site_Code) as site_Code "
                    + "                         from ( "
                    + "                             select  " + DmShared.iNull(eodMdl.getControlAc()).toString() + " as serviceAc, "
                    + "                                     " + DmShared.iNull(eodMdl.getHospitalAc()).toString() + " as controlAc, "
                    + "                                     CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -" + amountField + " ELSE " + amountField + " END AS amount, "
                    + "                                     mb.site_Code "
                    + "                                     from saleBill as mb "
                    + "                                     left join saleBillDetail as mbd on mb.docId = mbd.docId "
                    + "                                     left join voucher_Type as vt on vt.v_Type = mb.v_type "
                    + "                                     where mb.paymentSource IN (:ctrNature,:ctoNature) "
                    + "                                     and vt.category in (" + vCat_Service + ") and mb.isCancelled = 0 and mbd.eodSbDocId is null  "
                    + "                                     and mb.site_Code = :site_Code and mb.v_Date = :v_Date  "
                    + "				) as temp "
                    + "				group by temp.serviceAc, temp.controlAc, temp.site_Code "
                    + "			UNION ALL "
                    + "			Select	3 as caseType, temp.serviceAc, sum(temp.amount) as amount, '' as narration, "
                    + "				temp.controlAc, '' as billNo, max(temp.site_Code) as site_Code "
                    + "                         from ( "
                    + "                             select  " + DmShared.iNull(eodMdl.getSelfAc()).toString() + " as serviceAc, " //CR
                    + "                                     psm.serviceAc as controlAc, " //DR
                    + "                                     CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -" + amountField + " ELSE " + amountField + " END AS amount, "
                    + "                                     pr.site_Code "
                    + "                                     from saleBill as mb "
                    + "                                     left join saleBillDetail as mbd on mb.docId = mbd.docId "
                    + "                                     left join voucher_Type as vt on vt.v_Type = mb.v_type "
                    + "                                     left join patientRegistration as pr on mb.regDocId = pr.regDocId "
                    + "                                     left join patientServiceDetail as psd on psd.docId = mb.docId "
                    + "                                     left join patientServiceMast psm on psm.code = psd.serviceCode "
                    + "                                     where mb.paymentSource IN (:ctrNature,:ctoNature) "
                    + "                                     and vt.category in (" + vCat_Service + ") and mb.isCancelled = 0 and mbd.eodSbDocId is null  "
                    + "                                     and mb.site_Code = :site_Code and mb.v_Date = :v_Date "
                    + "				) as temp "
                    + "				group by temp.serviceAc, temp.controlAc, temp.site_Code "
                    + "             ) as t "
                    + "		left join siteMast as sm on t.site_Code = sm.code "
                    + "		left join subgroup as sgc on sgc.subcode = t.controlAc "
                    + "		left join subgroup as sgs on sgs.subcode = t.serviceAc "
                    + "         where isNull(t.amount,0) <> 0 "
                    + "		order by t.controlAc, t.serviceAc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setString("ctrNature", DmShared.iNull(PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()).toString());
            psTemp.setString("ctoNature", DmShared.iNull(PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAcPosting_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("serviceAc")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceAcName")));
                node.setContraSubCode(DmShared.iNull(rsTemp.getString("controlAc")).toString());
                node.setContraSubName(DmShared.xNull(rsTemp.getString("controlAcName")));
                node.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceAcName")));
                node.setSiteCode(DmShared.xNull(rsTemp.getString("siteCode")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00").equalsIgnoreCase("0.00") ? "" : DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                node.setReceipt(false);
                node.setCtr(true);
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
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

        return listNode;
    }

    public List<GrdAcPostedDetail_Mdl> getPostedDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdAcPostedDetail_Mdl> listNode = new ArrayList<GrdAcPostedDetail_Mdl>();
        GrdAcPostedDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, voucherType = "", eodDocId = "";

        try {
            voucherType = DmServer.getVType(vCatAP, cnTemp);

            /*====================================< Get EOD DocId >====================================*/
            sqlQuery = "Select docId from saleBill Where v_Date = :v_Date and v_Type = :v_Type and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.setString("v_Type", voucherType);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                eodDocId = DmShared.xNull(rsTemp.getString("docId"));
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Get Ledger Detail >====================================*/
            sqlQuery = "select	lg.docId, lg.recId as voucherNo, ac.name as accountName, con.name as contraName, lg.narration, amtDr, amtCr "
                    + "		from ledger as lg "
                    + "		left join subGroup as ac on lg.subCode = ac.subCode "
                    + "		left join subGroup as con on lg.contraSub = con.subCode "
                    + "		where lg.v_type = :v_Type and lg.v_Date = :v_Date and lg.docId = :docId "
                    + "		order by ac.name, con.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("docId", DmShared.iNull(eodDocId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAcPostedDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("voucherNo")));
                node.setAccountName(DmShared.xNull(rsTemp.getString("accountName")));
                node.setContraSubName(DmShared.xNull(rsTemp.getString("contraName")));
                node.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                node.setAmtCr(DmServer.vNull(rsTemp.getString("amtCr"), "0.00").equalsIgnoreCase("0.00") ? "" : DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                node.setAmtDr(DmServer.vNull(rsTemp.getString("amtDr"), "0.00").equalsIgnoreCase("0.00") ? "" : DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
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
        return listNode;
    }

    public List<GrdAcPosting_Mdl> getBankPostingDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdAcPosting_Mdl> listNode = new ArrayList<GrdAcPosting_Mdl>();
        GrdAcPosting_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get OPD Receipt Detail >====================================*/
            sqlQuery = "select  cl.subCode as serviceAc, isNull(cl.amtDr, 0) - isNull(cl.amtCr, 0) as amount, prc.site_Code, \n"
                    + "		" + eodMdl.getControlAc() + " as controlAc, '" + eodMdl.getControlAcName() + "' as controlAcName, \n"
                    + "		ctm.name as serviceName, sm.name as siteName, sg.name as serviceAcName, \n"
                    + "		concat('Payment by ', lm.manualCode, ' instrument No.:', cl.cardNo, ' Date:', cl.cardDate) as narration\n"
                    + "		, cl.cardNo as chqNo, cl.cardDate as chqDate,cl.cardHolderName"
                    + "		from patientReceipt as prc \n"
                    + "		left join voucher_Type as vt on vt.v_Type = prc.v_type \n"
                    + "		left join cardLedger as cl on prc.docId = cl.docId \n"
                    + "		left join cardTypeMast as ctm on ctm.code = cl.cardTypeCode \n"
                    + "		left join ledgerMast lm on lm.code = prc.lmCode \n"
                    + "		left join subGroup sg on sg.subcode = cl.subCode\n"
                    + "		left join siteMast sm on prc.site_Code = sm.code\n"
                    + "		where vt.category in(" + vCat_Receipt + ") and prc.isCancelled = 0 \n"
                    + "		and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date = :v_Date and ctm.cardType = :cardType \n"
                    + "		order by prc.docId, cl.v_Sno, sg.name, cl.subCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setInt("cardType", CardType.BANK.getEnumValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAcPosting_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("serviceAc")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceAcName")));
                node.setContraSubCode(DmShared.iNull(rsTemp.getString("controlAc")).toString());
                node.setContraSubName(DmShared.xNull(rsTemp.getString("controlAcName")));
                node.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                node.setSiteCode(DmShared.xNull(rsTemp.getString("site_Code")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00").equalsIgnoreCase("0.00") ? "" : DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                node.setChqNo(DmShared.xNull(rsTemp.getString("chqNo")));
                node.setChqDate(DmShared.dNull(rsTemp.getDate("chqDate")));
                node.setFavourOff(DmShared.xNull(rsTemp.getString("cardHolderName")));
                node.setReceipt(true);
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
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
        return listNode;
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

    public List<GrdAcPosting_Mdl> getSaleStockPostingDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdAcPosting_Mdl> listNode = new ArrayList<GrdAcPosting_Mdl>();
        GrdAcPosting_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Sale Receipt Detail >====================================*/
            sqlQuery = "Select temp.serviceAc as controlAc, max(sg.name) as controlAcName, sum(temp.amount) as amount,stuff(( \n"
                    + "			select ',{' + concat(min(prc.recId), ' To ', max(prc.recId))+'}'  \n"
                    + "				from stock prc \n"
                    + "				left join saleBill sb on prc.docID = sb.docID \n"
                    + "				left join voucher_Type vt on sb.v_Type = prc.v_type \n"
                    + "				where vt.category in (" + vCat_Sale + ") and sb.isCancelled = 0 and sb.site_Code = :site_Code and \n"
                    + "				sb.v_Date =:v_Date  group by sb.v_Type \n"
                    + "                         order by max(vt.short_Name) \n"
                    + "				for xml path(''), type).value('.[1]','nvarchar(max)'), 1,1,'') as narration \n"
                    + "				,temp.controlAc as serviceAc, max(sgca.name) as serviceAcName, max(temp.serviceName) as serviceName, \n"
                    + "                         max(temp.site_Code) as siteCode, max(sm.name) as siteName \n"
                    + "		from ( \n"
                    + "			select im.subCode as serviceAc, isNull(grossPurchaseRate,0) as amount, prc.site_Code, \n"
                    + "                         im.cogsAc as controlAc, '' as serviceName,vt.category \n"
                    + "                         from stock prc \n"
                    + "				left join saleBill sb on prc.docID = sb.docID \n"
                    + "				left join voucher_Type vt on sb.v_Type = vt.v_type \n"
                    + "				left join itemMast im on prc.itemCode = im.code \n"
                    + "				where vt.category in (" + vCat_Sale + ") and sb.isCancelled = 0 \n"
                    + "				and sb.site_Code = :site_Code and prc.v_Date  =:v_Date \n"
                    + "		) as temp \n"
                    + "		left join subGroup sg on sg.subcode = temp.serviceAc \n"
                    + "		left join subGroup sgca on sgca.subcode = temp.controlAc \n"
                    + "		left join siteMast as sm on temp.site_Code = sm.code \n"
                    + "		group by temp.serviceAc, temp.controlAc,temp.category \n"
                    + "         order by max(sg.name), temp.serviceAc ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAcPosting_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("serviceAc")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("serviceAcName")));
                node.setContraSubCode(DmShared.iNull(rsTemp.getString("controlAc")).toString());
                node.setContraSubName(DmShared.xNull(rsTemp.getString("controlAcName")));
                node.setNarration(DmShared.xNull(rsTemp.getString("narration")));
                node.setServiceName(DmShared.xNull(rsTemp.getString("serviceName")));
                node.setSiteCode(DmShared.xNull(rsTemp.getString("siteCode")));
                node.setSiteName(DmShared.xNull(rsTemp.getString("siteName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00").equalsIgnoreCase("0.00") ? "" : DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                node.setReceipt(true);
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
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
        return listNode;
    }
}
