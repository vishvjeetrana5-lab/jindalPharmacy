package aarogya.master.eod.server;

import aarogya.master.eod.shared.AcPosting_Mdl;
import aarogya.master.eod.shared.AcPosting_Vld;
import aarogya.master.eod.shared.EOD_Mdl;
import aarogya.master.eod.shared.GrdAcPostedDetail_Mdl;
import aarogya.master.eod.shared.GrdAcPosting_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum.ProcessEOD;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmEventbus.server.AbstractEventHandler;
import dataman.dmBase.dmEventbus.server.Event;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmEnum.CardType;
import dataman.dmBase.self.shared.DmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DayCareAcPosting_Server extends AbstractEventHandler<Event> {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCatAP = pvc.DAY_CARE_AC_POSTING;
    private String vCat_Service = "'" + pvc.DAY_CARE + "','" + pvc.DAY_CARE_SERVICE + "','" + pvc.DAY_CARE_SERVICE_CANCELLATION + "'";
    private String vCat_Receipt = "'" + pvc.DAY_CARE_RECEIPT + "','" + pvc.DAY_CARE_REFUND_RECEIPT + "','"
            + pvc.TPA_PATIENT_RECEIPT + "','" + pvc.TPA_PATIENT_REFUND_RECEIPT + "'";

    public DayCareAcPosting_Server() {
        super(ProcessEOD.DAY_CARE_AC_POSTING.getValue());
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

                    sqlQuery = "Select	sm.manualCode, e.dayCareControlAc, sg.name as controlAcName "
                            + "		from siteMast as sm "
                            + "		left join enviro as e on sm.code = e.site_Code "
                            + "		left join subGroup as sg on e.dayCareControlAc = sg.subCode "
                            + "		where sm.code = :code ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.iNull(eodMdl.getSiteCode()).toString());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        eodMdl.setSiteShortName(DmShared.xNull(rsTemp.getString("manualCode")));
                        eodMdl.setControlAc(DmShared.xNull(rsTemp.getString("dayCareControlAc")));
                        eodMdl.setControlAcName(DmShared.xNull(rsTemp.getString("controlAcName")));
                    }
                    rsTemp.close();
                    psTemp.close();

                    if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                        throw new DmException(0, "Please set Day Care Control A/c in Environment Setting.");
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
        String sqlQuery, voucherType, receiptDocId = "";

        try {
            voucherType = DmServer.getVType(vCatAP, cnTemp);

            if (DmShared.iNull(voucherType).intValue() == 0) {
                throw new DmException(0, "Day Care A/c Posting voucher type not found.");
            }
            if (DmShared.iNull(eodMdl.getControlAc()).longValue() == 0l) {
                throw new DmException(0, "Please set Day Care Control A/c in Environment Setting.");
            }

            /*====================================< Get voucher info >====================================*/
            sqlQuery = "Select docId, v_No, recId, recIdPrefix from saleBill Where v_Date = :v_Date and v_Type = :v_Type and site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
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

            AcPosting_Vld.chkSave(mrTemp);
            AcPosting_Vld.chkPostingData(listNode);

            /*====================================< Get docId's of patientReceipt >====================================*/
            sqlQuery = "Select	prc.docId "
                    + "		from patientReceipt as prc "
                    + "		left join voucher_Type as vt on vt.v_Type = prc.v_Type "
                    + "		left join patientRegistration as pr on pr.regDocId = prc.regDocId "
                    + "		left join voucher_Type as vtpr on vtpr.v_Type = pr.v_type "
                    + "		where vt.category in (" + vCat_Receipt + ") and vtpr.category in ('" + pvc.DAY_CARE + "') "
                    + "		and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date = :v_Date "
                    + "		group by prc.docId ";
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

            if (receiptDocId.trim().length() == 0) {
                throw new DmException(0, "No record found.");
            }

            if (DmShared.xNull(receiptDocId).trim().equalsIgnoreCase("")) {
                receiptDocId = "0";
            }

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

                            psTemp.setString("site_Code", eodMdl.getSiteCode());
                            psTemp.setString("isPosted", "1");
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }

                /*====================================< Update patientReceipt >====================================*/
                sqlQuery = "Update patientReceipt set sbDocId = :sbDocId where docId in (" + receiptDocId + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("sbDocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                /*====================================< Delete EOD >====================================*/
                deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.DAY_CARE_AC_POSTING.getId(), eodMdl.getSiteCode(), cnTemp);

                /*====================================< Save EOD >====================================*/
                saveEOD(eodMdl.getCurrentDate(), ProcessEOD.DAY_CARE_AC_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), cnTemp);

                /*====================================< Save EOD History >====================================*/
                saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.DAY_CARE_AC_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Added successfully.", cnTemp);
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

    public void delete(EOD_Mdl eodMdl, Connection cnTemp, boolean isEODDelete) {
        DmPreparedStatement psTemp = null;
        String sqlQuery, voucherType;
        ResultSet rsTemp = null;

        try {
            voucherType = DmServer.getVType(vCatAP, cnTemp);

            /*====================================< Update patientServiceDetail >====================================*/
            sqlQuery = "Update  psd set psd.sbDocId = :sbDocId, sbV_Sno = :sbV_Sno "
                    + "         from patientServiceDetail as psd "
                    + "         left join saleBill as sb on psd.sbDocId = sb.docId "
                    + "         where sb.v_Date = :v_Date and sb.v_Type = :v_Type and psd.site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("sbDocId", null);
            psTemp.setString("sbV_Sno", null);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Update patientReceipt >====================================*/
            sqlQuery = "Update  prec set prec.sbDocId = :sbDocId "
                    + "         from patientReceipt as prec "
                    + "         left join saleBill as sb on prec.sbDocId = sb.docId "
                    + "         where sb.v_Date = :v_Date and sb.v_Type = :v_Type and prec.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("sbDocId", null);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from ledger >====================================*/
            sqlQuery = "Delete From ledger Where v_Date = :v_Date and v_Type = :v_Type  and site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
            psTemp.setString("v_Type", voucherType);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from saleBill >====================================*/
//            sqlQuery = "Delete From saleBill Where v_Date = :v_Date and v_Type = :v_Type ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
//            psTemp.setString("v_Type", voucherType);
//            psTemp.executeUpdate();
//            psTemp.close();
            if (isEODDelete) {
                /*====================================< Delete EOD >====================================*/
                deleteEOD(eodMdl.getCurrentDate(), ProcessEOD.DAY_CARE_AC_POSTING.getId(), eodMdl.getSiteCode(), cnTemp);

                /*====================================< Save EOD History >====================================*/
                saveEODHistory(eodMdl.getCurrentDate(), ProcessEOD.DAY_CARE_AC_POSTING.getId(), eodMdl.getSiteCode(), eodMdl.getUserName(), "Deleted successfully.", cnTemp);
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
            sqlQuery = "Select  prc.v_Date "
                    + "         from patientReceipt as prc "
                    + "		left join voucher_Type as vt on vt.v_Type = prc.v_type "
                    + "         left join patientRegistration as pr on pr.regDocId = prc.regDocId "
                    + "		left join voucher_Type as vtpr on vtpr.v_Type = pr.v_type "
                    + "		where vt.category in (" + vCat_Receipt + ") and vtpr.category in ('" + pvc.DAY_CARE + "') "
                    + "         and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date < :v_Date "
                    + "         group by prc.v_Date "
                    + "         order by prc.v_Date ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", eodMdl.getSiteCode());
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
            /*====================================< Get Receipt Detail >====================================*/
            sqlQuery = "Select	temp.serviceAc, max(sg.name) as serviceAcName, sum(temp.amount) as amount, "
                    + "         stuff((\n"
                    + "			select	',{' + concat(min(prc.recId), ' To ', max(prc.recId))+'}' \n"
                    + "				from patientReceipt prc \n"
                    + "				left join voucher_Type vt on vt.v_Type = prc.v_type \n"
                    + "				left join cardLedger cl on prc.docId = cl.docId \n"
                    + "				left join cardTypeMast ctm on ctm.code = cl.cardTypeCode \n"
                    + "				left join patientRegistration pr on pr.regDocId = prc.regDocId \n"
                    + "				left join voucher_Type as vtpr on vtpr.v_Type = pr.v_type\n"
                    + "				where vt.category in (" + vCat_Receipt + ") and vtpr.category in ('" + pvc.DAY_CARE + "') "
                    + "                         and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date = :v_Date "
                    + "                         and ctm.cardType <> :cardType "
                    + "				group by prc.v_Type \n"
                    + "				order by max(vt.short_Name) \n"
                    + "				for xml path(''), type).value('.[1]','nvarchar(max)'), 1,1,'') as narration, "
                    + "         temp.controlAc, '" + DmShared.xNull(eodMdl.getControlAcName()) + "' as controlAcName, max(temp.serviceName) as serviceName, "
                    + "         max(temp.site_Code) as siteCode, max(sm.name) as siteName "
                    + "		from ( "
                    + "			select	cl.subCode as serviceAc, isNull(cl.amtDr,0) - isNull(cl.amtCr,0) as amount, prc.site_Code, "
                    + "                         " + DmShared.iNull(eodMdl.getControlAc()).toString() + " as controlAc, ctm.name as serviceName "
                    + "				from patientReceipt as prc "
                    + "				left join voucher_Type as vt on vt.v_Type = prc.v_type "
                    + "				left join cardLedger as cl on prc.docId = cl.docId "
                    + "				left join cardTypeMast as ctm on ctm.code = cl.cardTypeCode "
                    + "				left join patientRegistration as pr on pr.regDocId = prc.regDocId "
                    + "                         left join voucher_Type as vtpr on vtpr.v_Type = pr.v_type "
                    + "				where vt.category in (" + vCat_Receipt + ") and vtpr.category in ('" + pvc.DAY_CARE + "') "
                    + "                         and prc.isCancelled = 0 and prc.sbDocId is null and prc.site_Code = :site_Code and prc.v_Date = :v_Date "
                    + "                         and ctm.cardType <> :cardType "
                    + "		) as temp "
                    + "		left join subGroup sg on sg.subcode = temp.serviceAc "
                    + "		left join siteMast as sm on temp.site_Code = sm.code "
                    + "		group by temp.serviceAc, temp.controlAc "
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
                    + "		concat('Payment by ', pr.recId, ' instrument No.:', cl.cardNo, ' Date:', format(cl.cardDate, 'dd-MMM-yyyy')) as narration\n"
                    + "		, cl.cardNo as chqNo, cl.cardDate as chqDate,cl.cardHolderName\n"
                    + "		from patientReceipt as prc \n"
                    + "		left join voucher_Type as vt on vt.v_Type = prc.v_type \n"
                    + "		left join cardLedger as cl on prc.docId = cl.docId \n"
                    + "		left join cardTypeMast as ctm on ctm.code = cl.cardTypeCode \n"
                    + "		left join patientRegistration as pr on pr.regDocId = prc.regDocId \n"
                    + "		left join voucher_Type as vtpr on vtpr.v_Type = pr.v_type\n"
                    + "		left join subGroup sg on sg.subcode = cl.subCode\n"
                    + "		left join siteMast sm on prc.site_Code = sm.code\n"
                    + "		where vt.category in(" + vCat_Receipt + ") and vtpr.category in ('" + pvc.DAY_CARE + "')  and prc.isCancelled = 0 \n"
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

    public List<GrdAcPostedDetail_Mdl> getPostedDetail(EOD_Mdl eodMdl, Connection cnTemp) throws Exception {
        List<GrdAcPostedDetail_Mdl> listNode = new ArrayList<GrdAcPostedDetail_Mdl>();
        GrdAcPostedDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Ledger Detail >====================================*/
            sqlQuery = "select	lg.docId, lg.recId as voucherNo, ac.name as accountName, con.name as contraName, lg.narration, amtDr, amtCr "
                    + "		from ledger as lg "
                    + "		left join subGroup as ac on lg.subCode = ac.subCode "
                    + "		left join subGroup as con on lg.contraSub = con.subCode "
                    + "		left join voucher_Type as vt on vt.v_Type = lg.v_type "
                    + "		where vt.category = '" + vCatAP + "' and lg.v_Date = :v_Date and lg.site_Code = :site_Code "
                    + "		order by ac.name, con.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(eodMdl.getSiteCode()).toString());
            psTemp.setDate("v_Date", DmServer.convertDate(eodMdl.getCurrentDate()));
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
