package aarogya.pharmacy.wholeSalePaymentRecipt.server;

import aarogya.pharmacy.wholeSalePaymentRecipt.shared.DefaultValue_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.Find_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.TxtHDepositAc_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.WholeSalePaymentRecipt_Mdl;
import aarogya.pharmacy.wholeSalePaymentRecipt.shared.WholeSalePaymentRecipt_Srv;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.RecId;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WholeSalePaymentRecipt_SrvImpl extends DmRSSPrivate implements WholeSalePaymentRecipt_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String vCat = pvc.WHOLE_SALE_PAYMENT_RECIPT;
    private final String voucherTypefieldName = "lgm.v_Type", voucherDateFieldName = "lgm.v_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  lgm.docId "
                    + "         from wholeSalePaymentReceipt as lgm "
                    + "         left join voucher_Type as vt on lgm.v_Type = vt.v_Type  "
                    + "         where vt.category in ('" + vCat + "') and lgm.site_Code = :site_Code and lgm.v_prefix = :v_prefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlQuery += " order by lgm.v_Date desc, vt.short_Name, lgm.v_No desc, lgm.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("v_prefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public DmResult<WholeSalePaymentRecipt_Mdl> getMoveRec(String strKey) {
        DmResult<WholeSalePaymentRecipt_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        WholeSalePaymentRecipt_Mdl mrTemp = new WholeSalePaymentRecipt_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	lgm.docId, lgm.recId, lgm.v_Date, lgm.v_Type, vt.description, lgm.chequeNo, lgm.receivedAmt, lgm.preparedBy, lgm.u_Name, "
                    + "         lgm.partyCode, sgo.name as partyName, lgm.tdsAc, sgt.name as tdsAcName, lgm.disallowType, "
                    + "         lgm.depositInSubCode, sgd.name as depositInName, sgn.name as nature, lgm.instrumentCode, lgm.remark, lgm.chequeDate, lgm.recIdPrefix, lgm.v_No, "
                    + "         lgm.tdsAmt, lgm.receivedAmt, (isNull(lgm.receivedAmt,0) - isNull(lgm.SettledAmt,0)) as unsettledAmt ,lgm.SettledAmt  "
                    + "         from wholeSalePaymentReceipt as lgm "
                    + "         left join subGroup as sgo on lgm.partyCode = sgo.subCode "
                    + "         left join subGroup as sgt on lgm.tdsAc = sgt.subCode "
                    + "         left join subGroup as sgd on lgm.depositInSubCode = sgd.subCode "
                    + "         left join subGroupNature as sgn on sgd.nature = sgn.code "
                    + "         left join voucher_Type as vt on lgm.v_Type = vt.v_Type "
                    + "         where lgm.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setObjRecId(new RecId());
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setDepositInCode(DmShared.xNull(rsTemp.getString("depositInSubCode")));
                mrTemp.setDepositInName(DmShared.xNull(rsTemp.getString("depositInName")));
                mrTemp.setPartyCode(DmShared.xNull(rsTemp.getString("partyCode")));
                mrTemp.setPartyName(DmShared.xNull(rsTemp.getString("partyName")));
                mrTemp.setTdsAcCode(DmShared.xNull(rsTemp.getString("tdsAc")));
                mrTemp.setTdsAcName(DmShared.xNull(rsTemp.getString("tdsAcName")));
                mrTemp.setInstrumentNo(DmShared.xNull(rsTemp.getString("chequeNo")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setInstrumentDate(DmShared.dNull(rsTemp.getDate("chequeDate")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setBankCashType(DmShared.xNull(rsTemp.getString("nature")));
                mrTemp.setInstrumentCode(DmShared.xNull(rsTemp.getString("instrumentCode")));
                mrTemp.setInstrumentName(DmShared.xNull(rsTemp.getString("instrumentCode")).equalsIgnoreCase("1") ? "Cheque"
                        : DmShared.xNull(rsTemp.getString("instrumentCode")).equalsIgnoreCase("2") ? "DD"
                        : DmShared.xNull(rsTemp.getString("instrumentCode")).equalsIgnoreCase("3") ? "NEFT"
                        : DmShared.xNull(rsTemp.getString("instrumentCode")).equalsIgnoreCase("4") ? "RTGS" : "None");
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setDisallowTypeCode(DmShared.iNull(rsTemp.getString("disallowType")).toString());
                mrTemp.setDisallowTypeName(DmShared.iNull(rsTemp.getString("disallowType")).intValue() == 1 ? "Disallow (Reg. wise)" : "Disallow (Service wise)");
                mrTemp.setReceivedAmt(DmServer.vNull(rsTemp.getString("receivedAmt"), "0.00"));
                mrTemp.setTdsAmt(DmServer.vNull(rsTemp.getString("tdsAmt"), "0.00"));
                mrTemp.setUnsettledAmt(DmServer.vNull(rsTemp.getString("unsettledAmt"), "0.00"));
                mrTemp.setSettledAmt(DmServer.vNull(rsTemp.getString("SettledAmt"), "0.00"));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*================< Check whether record approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select  lgm.postedBy, lgm.site_Code, lgm.v_Prefix "
                    + "         from wholeSalePaymentReceipt as lgm "
                    + "         where lgm.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("postedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can not delete this record.");
                }
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(DmServer.getSsnSiteCode(this)).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not delete this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(DmServer.getSsnVoucherPrefix(this)).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

//            /*====================================< Check whether record is in use or not >====================================*/
//            sqlQuery = "Select docId from patientReceiptDetail where docid = :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
//                throw new DmException(0, "Record is in use. You can not delete this record.");
//            }
//            rsTemp.close();
//            psTemp.close();
//
//            /*====================================< Delete records from patientDisallowDetail >====================================*/
//            sqlQuery = "Delete From patientDisallowDetail Where sbdocid= :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
//            psTemp.executeUpdate();
//            psTemp.close();
//
//            /*====================================< Delete records from patientReceiptDetail >====================================*/
//            sqlQuery = "Delete From patientReceiptDetail Where docId= :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
//            psTemp.executeUpdate();
//            psTemp.close();

            /*====================================< Delete records from ledger >====================================*/
            sqlQuery = "Delete From ledger Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from saleBill >====================================*/
            sqlQuery = "Delete From saleBill Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Delete records from wholeSalePaymentReceipt >====================================*/
            sqlQuery = "Delete From wholeSalePaymentReceipt Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return dmrTemp;
    }

    @Override
    public DmResult<String> saveRecord(WholeSalePaymentRecipt_Mdl mrTemp, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
//            WholeSalePaymentRecipt_Mdl.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0l) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("wholeSalePaymentReceipt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
            }

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0l) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            STServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");

            /*================< Check whether Organization / TDS A/c can change or not >================*/
//            sqlQuery = "Select	lm.docId, lm.v_Date, lm.tdsAc, lm.disallowType "
//                    + "		from patientReceiptDetail as prd "
//                    + "		left join wholeSalePaymentReceipt as lm on prd.docId = lm.docId "
//                    + "		where prd.docId = :docId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
//            rsTemp = psTemp.executeQuery();
//            if (rsTemp.next()) {
////                if (DmShared.iNull(mrTemp.getPartyCode()).longValue() != DmShared.iNull(rsTemp.getString("subCode")).longValue()) {
////                    throw new DmException(0, "Record is in used. You can not change Organization.");
////                }
//                if (DmShared.convertDate(mrTemp.getVoucherDate()).getTime() != DmShared.convertDate(DmShared.dNull(rsTemp.getDate("v_Date"))).getTime()) {
//                    throw new DmException(400, "Record is in used. You can not change Voucher Date.");
//                }
//                if (DmShared.iNull(mrTemp.getTdsAcCode()).longValue() != DmShared.iNull(rsTemp.getString("tdsAc")).longValue()) {
//                    throw new DmException(0, "Record is in used. You can not change TDS A/c.");
//                }
//                if (DmShared.iNull(mrTemp.getDisallowTypeCode()).intValue() != DmShared.iNull(rsTemp.getString("disallowType")).intValue()) {
//                    throw new DmException(0, "Record is in used. You can not change Disallow Type.");
//                }
//            }
//            rsTemp.close();
//            psTemp.close();

            /*================< Check whether record approved/ different site/ different financial year or not >================*/
            sqlQuery = "Select  lgm.postedBy, lgm.site_Code, lgm.v_Prefix "
                    + "         from wholeSalePaymentReceipt as lgm "
                    + "         where lgm.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("postedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("site_Code")).intValue() != DmShared.iNull(siteCode).intValue()) {
                    throw new DmException(0, "Record generated from different site. You can not modify this record.");
                }
                if (DmShared.iNull(rsTemp.getString("v_Prefix")).intValue() != DmShared.iNull(voucherPrefix).intValue()) {
                    throw new DmException(0, "Record generated from different financial year. You can not modify this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Delete records from ledger >====================================*/
            sqlQuery = "Delete From ledger Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert/Update record into wholeSalePaymentReceipt >====================================*/
            if (blnAdd) {
                sqlQuery = "INSERT INTO wholeSalePaymentReceipt (\n"
                        + "    docId, v_No, recIdPrefix, recId, v_Type, v_Prefix, v_Date, tdsAc, depositInSubCode, \n"
                        + "    instrumentCode, chequeNo, chequeDate, receivedAmt, remark, disallowType, \n"
                        + "    site_Code, transferDt, preparedBy, u_EntDt, partyCode\n"
                        + ") VALUES (\n"
                        + "    :docId, :v_No, :recIdPrefix, :recId, :v_Type, :v_Prefix, :v_Date, :tdsAc, :depositInSubCode, \n"
                        + "    :instrumentCode, :chequeNo, :chequeDate, :receivedAmt, :remark, :disallowType, \n"
                        + "    :site_Code, :transferDt, :preparedBy, :u_EntDt, :partyCode )" ;
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("tdsAc", DmShared.foriegnKey(mrTemp.getTdsAcCode()));
                psTemp.setString("depositInSubCode", DmShared.foriegnKey(mrTemp.getDepositInCode()));
                psTemp.setString("instrumentCode", DmShared.iNull(mrTemp.getInstrumentCode()).toString());
                psTemp.setString("chequeNo", DmShared.xNull(mrTemp.getInstrumentNo()));
                psTemp.setDate("chequeDate", DmServer.convertDate(mrTemp.getInstrumentDate()));
                psTemp.setString("receivedAmt", DmServer.vNull(mrTemp.getReceivedAmt(), "0.00"));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()).trim());
                psTemp.setString("disallowType", DmShared.iNull(mrTemp.getDisallowTypeCode()).toString());
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("partyCode", DmShared.foriegnKey(mrTemp.getPartyCode()));

            } else {
                sqlQuery = "Update wholeSalePaymentReceipt Set "
                        + " v_Date = :v_Date, "
                        + " tdsAc = :tdsAc, "
                        + " depositInSubCode = :depositInSubCode, "
                        + " instrumentCode = :instrumentCode, "
                        + " chequeNo = :chequeNo, "
                        + " chequeDate = :chequeDate, "
                        + " receivedAmt = :receivedAmt, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :u_EntDt, "
                        + " remark = :remark, "
                        + " disallowType = :disallowType , "
                        + " partyCode = :partyCode "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("tdsAc", DmShared.foriegnKey(mrTemp.getTdsAcCode()));
                psTemp.setString("depositInSubCode", DmShared.foriegnKey(mrTemp.getDepositInCode()));
                psTemp.setString("instrumentCode", DmShared.iNull(mrTemp.getInstrumentCode()).toString());
                psTemp.setString("chequeNo", DmShared.xNull(mrTemp.getInstrumentNo()));
                psTemp.setDate("chequeDate", DmServer.convertDate(mrTemp.getInstrumentDate()));
                psTemp.setString("receivedAmt", DmServer.vNull(mrTemp.getReceivedAmt(), "0.00"));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()).trim());
                psTemp.setString("disallowType", DmShared.iNull(mrTemp.getDisallowTypeCode()).toString());
                psTemp.setString("transferDt", null);
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.setString("partyCode", DmShared.foriegnKey(mrTemp.getPartyCode()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert/Update record into saleBill >====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into saleBill(docId, v_Type, v_No, v_Date, v_Prefix, recId, recIdPrefix, "
                        + " subCode, remark, site_Code, transferDt, preparedBy, u_EntDt)"
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId, :recIdPrefix, "
                        + " :subCode, :remark, :site_Code, :transferDt, :preparedBy, :u_EntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getPartyCode()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()).trim());
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "Update saleBill Set "
                        + " v_Date = :v_Date, "
                        + " subCode = :subCode, "
                        + " remark = :remark, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :u_EntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getPartyCode()));
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("transferDt", null);
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*====================================< Insert records into ledger >====================================*/
            int sno = 0;
            for (int i = 0; i < 2; i++) {
                sqlQuery = "Insert into ledger (docId, v_SNo, v_No, recId, recIdPrefix, v_Type, v_Prefix, v_Date, subCode, "
                        + " instrumentCode, chq_No, chq_Date, narration, contraSub, contraText, amtDr, amtCr, "
                        + " isSystem_Generated, site_Code, isPosted, u_Name, u_EntDt)  "
                        + " Values (:docId, :v_SNo, :v_No, :recId, :recIdPrefix, :v_Type, :v_Prefix, :v_Date, :subCode, "
                        + " :instrumentCode, :chq_No, :chq_Date, :narration, :contraSub, :contraText, :amtDr, :amtCr, "
                        + " :isSystem_Generated, :site_Code, :isPosted, :u_Name, :u_EntDt) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setInt("v_SNo", ++sno);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("instrumentCode", DmShared.iNull(mrTemp.getInstrumentCode()).toString());
                psTemp.setString("chq_No", DmShared.xNull(mrTemp.getInstrumentNo()));
                psTemp.setDate("chq_Date", DmServer.convertDate(mrTemp.getInstrumentDate()));
                if (i == 0) {
                    psTemp.setString("amtDr", DmServer.vNull(mrTemp.getReceivedAmt(), "0.00"));
                    psTemp.setString("amtCr", "0.00");
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getDepositInCode()));
                    psTemp.setString("contraSub", DmShared.foriegnKey(mrTemp.getPartyCode()));
                    psTemp.setString("contraText", DmShared.xNull(mrTemp.getPartyName()));
                } else if (i == 1) {
                    psTemp.setString("amtCr", DmServer.vNull(mrTemp.getReceivedAmt(), "0.00"));
                    psTemp.setString("amtDr", "0.00");
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getPartyCode()));
                    psTemp.setString("contraSub", DmShared.foriegnKey(mrTemp.getDepositInCode()));
                    psTemp.setString("contraText", DmShared.xNull(mrTemp.getDepositInName()));
                }
                psTemp.setString("narration", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("isSystem_Generated", "1");
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("isPosted", "1");
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl node;
        String sqlQuery, sqlCondition;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	lgm.docId, lgm.recId, lgm.v_Date, (isNull(lgm.tdsAmt,0) + isNull(lgm.receivedAmt,0)) as amount, "
                    + "         sgt.name as tdsAcName, (isNull(lgm.receivedAmt,0) - isNull(lgm.SettledAmt,0)) as unsettledAmt,isNull(lgm.tdsAmt,0) as tdsAmt,"
                    + "         isNull(lgm.receivedAmt,0) as recdAmt, \n"
                    + "         lgm.chequeNo, lgm.chequeDate, sgo.name as partyName, sgd.name as depositInName, vt.short_Name,lgm.SettledAmt "
                    + "         from wholeSalePaymentReceipt as lgm "
                    + "         left join subGroup as sgo on lgm.partyCode = sgo.subCode "
                    + "         left join subGroup as sgt on lgm.tdsAc = sgt.subCode "
                    + "         left join subGroup as sgd on lgm.depositInSubCode = sgd.subCode "
                    + "         left join voucher_Type as vt on lgm.v_Type = vt.v_Type "
                     + "         where vt.category in ('" + vCat + "') and lgm.site_Code = :site_Code and lgm.v_prefix = :v_prefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += " and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_prefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new Find_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                node.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                node.setChequeDate(DmShared.dNull(rsTemp.getDate("chequeDate")));
                node.setChequeNo(DmShared.xNull(rsTemp.getString("chequeNo")));
                node.setPartyName(DmShared.xNull(rsTemp.getString("partyName")));
                node.setTdsAc(DmShared.xNull(rsTemp.getString("tdsAcName")));
                node.setDepositeAC(DmShared.xNull(rsTemp.getString("depositInName")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "00.00"));
                node.setUnSetteldAmt(DmServer.vNull(rsTemp.getString("unsettledAmt"), "0.00"));
                node.setTdsAmt(DmServer.vNull(rsTemp.getString("tdsAmt"), "0.00"));
                node.setReceivedAmt(DmServer.vNull(rsTemp.getString("recdAmt"), "0.00"));
                node.setSetteldAmt(DmServer.vNull(rsTemp.getString("SettledAmt"), "0.00"));
                lstTemp.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select v_Type as code, description as name from voucher_Type where category = '" + vCat + "' and isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDebtorsName(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> listNode = new ArrayList<>();
        KeyFieldExtended node;
        String sqlQuery;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            sqlQuery = " Select   max(om.subCode) as code, max(sg.name) as name , max(om.tdsAc) as tdsAc, max(sgt.name) as tdsAcName ,\n"
//                    + " max(tpaCA.subCode) as tpaControlAc,max(tpaCA.name)  tpaControlAcName 	\n"
//                    + " from organizationMast as om 		\n"
//                    + " left join subGroup as sg on om.subCode = sg.subCode         \n"
//                    + " left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode         \n"
//                    + " left join subGroupNature as sgn ON sgn.code = sg.nature         \n"
//                    + " left join subGroup as sgt on om.tdsAc = sgt.subCode        \n"
//                    + " left join subGroup as tpaCA on om.tpaControlAc = tpaCA.subCode \n"
//                    + " \n"
//                    + " where  om.tpaControlAc  is not null         \n"
//                    + " and sg.isActive = 1 and sgsl.site_Code = :site_Code \n"
//                    + "\n"
//                    + " group by tpaCA.name";
            sqlQuery = "select sg.name AS Name,sg.subCode AS code  from subGroup sg \n"
                    + " left join subGroupNature sgn on sgn.code = sg.nature \n"
                    + " WHERE sgn.manualCode = 'CT' ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTDSAccount(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select	sg.subCode as code, sg.name "
                    + "		from subGroup as sg "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "		left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :site_Code and sgn.manualCode = 'LG'  ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDisallowType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Disallow (Reg. wise)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Disallow (Service wise)");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHDepositAc_Mdl>> getTxtHDepositIn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHDepositAc_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHDepositAc_Mdl> lstTemp = new ArrayList<TxtHDepositAc_Mdl>();
        TxtHDepositAc_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	sg.subCode as code, sg.name, sgn.name as nature, sgn.manualCode  "
                    + "		from subGroup as sg "
                    + "		left join subGroupNature as sgn on sg.nature = sgn.code "
                    + "		left join subGroupSiteList as sgsl on sg.subCode = sgsl.subCode "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :site_Code and sgn.manualCode in ('BK','CS') ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHDepositAc_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHInstrument(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Cheque");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("DD");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("NEFT");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("None");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("4");
            listNode.setSharedKeyFieldName("RTGS");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get voucher details >====================================*/
            sqlQuery = "Select v_Type as code, description as name from voucher_Type where category = '" + vCat + "' and isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
            }
            rsTemp.close();
            psTemp.close();

            /*====================================< Get details from enviro >====================================*/
            sqlQuery = "Select	en.disallowType "
                    + "		from enviro as en "
                    + "		where en.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDisallowType(DmShared.xNull(rsTemp.getString("disallowType")).trim());
            } else {
                throw new UnsupportedOperationException("Please set default values in Environment Setting.");
            }
            rsTemp.close();
            psTemp.close();

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);

            dmrTemp.setData(defaultValue);
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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

        return dmrTemp;
    }

    @Override
    public String[] getDefaultTDSAc() {
        String sqlQuery = "";
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String[] defaultTDSAc = new String[2];

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select env.tdsAc as tscAcCode,sg.name as tdsAcName"
                    + " from enviro as env "
                    + "left join subgroup as sg on env.tdsAc=sg.subCode "
                    + "where env.site_Code= :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultTDSAc[0] = DmShared.iNull(rsTemp.getString("tscAcCode")).toString();
                defaultTDSAc[1] = DmShared.xNull(rsTemp.getString("tdsAcName"));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception e) {

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

        return defaultTDSAc;

    }

}
