package dataman.accounts.advanceEntry.server;

import dataman.accounts.advanceEntry.shared.*;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Mdl;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmJasperCompiler;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.tdsCategoryPopup.server.TdsCategoryPopup_SrvImpl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class AdvanceEntry_SrvImpl extends DmRSSPrivate implements AdvanceEntry_Srv {

    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    private final String vCat = "'" + pvc.ADVANCE_ENTRY + "'";

    //<editor-fold defaultstate="collapsed" desc="Approval">
    @Override
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
            }
            dmrTemp.setData(docId);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            /*==============================<< Check if document is in use >>====================================*/
            sqlQuery = "select approvedBy,approvedDt,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from advanceEntry where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {

                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvedDt", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = " Update advanceEntry set "
                                + " u_EntDt=:u_EntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = "Update Ledger Set "
                                + "isPosted = 1 "
                                + "Where DocId = :DocId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", keyCode);
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already approved.");
                    }

                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvedDt", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update advanceEntry set "
                                    + " u_EntDt=:u_EntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = "Update Ledger Set "
                                    + "isPosted = 0 "
                                    + "Where DocId = :DocId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("DocId", keyCode);
                            psTemp.executeUpdate();
                            psTemp.close();

                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }

                    } else {
                        throw new Exception("Document already unapproved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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

    //</editor-fold>
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

            sqlQuery = "SELECT  vt.v_Type, vt.description ,vt.attachPaymentMode "
                    + "         FROM voucher_Type vt "
                    + "         WHERE vt.short_Name = " + vCat + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("v_Type")).toString());
                defaultValue.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("description")));
                defaultValue.setPaymentMode(DmShared.xNull(rsTemp.getString("attachPaymentMode")));

            }
            rsTemp.close();
            psTemp.close();

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
    public String[] getServerDateTime() {
        String[] dateTime, returnValue;

        dateTime = DmServer.getServerDateTime();
        returnValue = new String[3];
        returnValue[0] = dateTime[0];
        returnValue[1] = dateTime[1];
        returnValue[2] = UUID.randomUUID().toString();

        return returnValue;
    }

    @Override
    public DmResultPageing<List<GrdAdvReqSearchDetail_Mdl>> getAdvReqSearch(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
        DmResultPageing<List<GrdAdvReqSearchDetail_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        List<GrdAdvReqSearchDetail_Mdl> lstTemp = new ArrayList<GrdAdvReqSearchDetail_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select adr.docid as advReqDocId,adr.v_date as advReqDate,adr.recid as advReqNo,adr.dueDate,adr.advanceReqAmt,adr.module ,adr.preparedBy ,"
                    + " po.docid as poDocId,po.v_date as poDate,po.recId as poNo,po.subcode,sg.name as supplierName ,PO.oh_Amt_Net as poAmt "
                    + " from advanceReqEntry adr "
                    + " left join advanceEntry ad on ad.reqDocid=adr.docid "
                    + " left join pOrder po on po.docid = adr.podocid "
                    + " left join subgroup sg on sg.subcode = po.subcode "
                    + " left join voucher_Type as vt on vt.v_Type = adr.V_type "
                    + " where adr.site_Code = :site_Code and  ad.docid is null and coalesce(adr.isStatus,'')<> 1  "
                    + " and vt.category = '" + new LibraryVoucherCategory().ADVANCE_REQUEST_ENTRY + "' ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdAdvReqSearchDetail_Mdl objTemp = new GrdAdvReqSearchDetail_Mdl();
                objTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("advReqDocId")));
                objTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("advReqNo")));
                objTemp.setAdvReqDate(DmShared.dNull(rsTemp.getDate("advReqDate")));
                objTemp.setPoNo(DmShared.xNull(rsTemp.getString("poNo")));
                objTemp.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                objTemp.setPoDate(DmShared.dNull(rsTemp.getDate("poDate")));
                objTemp.setDueDate(DmShared.dNull(rsTemp.getDate("dueDate")));
                objTemp.setModuleTag(DmShared.xNull(rsTemp.getString("module")));
                objTemp.setModuleText(DmShared.xNull(objTemp.getModuleTag()).equalsIgnoreCase(pvc.PURCHASE_ORDER) ? "Purchase" : "Pharmacy");
                objTemp.setSupplier(DmShared.xNull(rsTemp.getString("supplierName")));
                objTemp.setAdvReqAmt(DmServer.vNull(rsTemp.getString("advanceReqAmt")));
                objTemp.setPoAmt(DmServer.vNull(rsTemp.getString("poAmt")));
                objTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                lstTemp.add(objTemp);
            }

            double amt = 0;
            for (GrdAdvReqSearchDetail_Mdl node : lstTemp) {
                amt = DmShared.vNull(getTotalBalacePaidandPoAmt(DmShared.xNull(node.getPoDocId()), cnTemp)).doubleValue();
                if (DmShared.vNull(node.getPoAmt()).doubleValue() == amt) {
                    node.setFinalamt(DmServer.vNull(amt));
                }
            }

            Iterator<GrdAdvReqSearchDetail_Mdl> i = lstTemp.iterator();

            while (i.hasNext()) {
                GrdAdvReqSearchDetail_Mdl e = i.next();
                if (DmShared.vNull(e.getPoAmt()).doubleValue() == DmShared.vNull(e.getFinalamt()).doubleValue()) {
                    i.remove();
                }
            }

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
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

    private double getTotalBalacePaidandPoAmt(String poDocId, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        double amt = 0d;
        try {

            sqlQuery = " select sum(ad.paidAmt) as paidAmt "
                    + "	from advanceEntry  ad"
                    + "	left join advanceReqEntry adr on adr.docid=ad.reqDocId "
                    + " left join pOrder po on po.docid=adr.poDocId "
                    + "	 where adr.poDocid= :docId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setLong("docId", DmShared.iNull(poDocId).longValue());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                amt = DmShared.vNull(rsTemp.getString("paidAmt")).doubleValue();

            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
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
        return amt;
    }

    @Override
    public DmResult<String> saveRecord(AdvanceEntry_Mdl mrTemp, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, vNo = "", userName = "", voucherPrefix;
        List<TdsCategoryGrid_Mdl> listTdsRecord;
        boolean blnAdd = false;
        double totalCredit = 0, totalDebit = 0, credit = 0, debit = 0;
        String contraTDS = "", narration = "", narrationTemp = "", contraTDSBF = "", contraSubCodeTDS = "";
        int vSNo = 0, prvVSNo = 0;
        try {
            AdvanceEntry_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            voucherPrefix = DmServer.getSsnVoucherPrefix(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("advanceEntry", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
//            } else {
//                DmServer.getRecId("advanceEntry", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true);
            }

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Check for Approvedby if already approved >>====================================*/
                sqlQuery = "select approvedBy  FROM advanceEntry WHERE  docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                        throw new DmException(0, "Record is approved. You can't edit this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

            }

            vNo = DmShared.iNull(mrTemp.getRecId().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From cardLedger Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From Ledger Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete TDS Entry of current Bill >>====================================*/
            TdsCategoryPopup_SrvImpl.deleteTDS(mrTemp.getSharedKeyFieldCode(), cnTemp);

            if (blnAdd) {
                sqlQuery = "Insert Into advanceEntry(docId, recId,recIdPrefix,v_No,v_Type,v_Prefix,v_Date,reqDocId, preparedBy,paidAmt,originalAmt,tdsDeduction,"
                        + " preparedDate,site_Code)"
                        + " values (:docId, :recId,:recIdPrefix,:v_No,:v_Type,:v_Prefix,:v_Date,:reqDocId, :preparedBy,:paidAmt,:originalAmt,:tdsDeduction,"
                        + " :preparedDate,:site_Code) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeTag()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("reqDocId", DmShared.iNull(mrTemp.getAdvReqDocId()).toString());
                psTemp.setString("paidAmt", DmServer.vNull(mrTemp.getPaidAmount(), "0.00"));
                psTemp.setString("originalAmt", DmServer.vNull(mrTemp.getOriginalAmount(), "0.00"));
                psTemp.setString("tdsDeduction", DmServer.vNull(mrTemp.getTdsDeduction(), "0.00"));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.executeUpdate();
                psTemp.close();

            } else {
                sqlQuery = "Update advanceEntry set "
                        + " paidAmt= :paidAmt,"
                        + " originalAmt= :originalAmt,"
                        + " tdsDeduction= :tdsDeduction,"
                        + " U_EntDt= :U_EntDt,"
                        + " u_Name=:u_Name "
                        + " where docId=:docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("paidAmt", DmServer.vNull(mrTemp.getPaidAmount(), "0.00"));
                psTemp.setString("originalAmt", DmServer.vNull(mrTemp.getOriginalAmount(), "0.00"));
                psTemp.setString("tdsDeduction", DmServer.vNull(mrTemp.getTdsDeduction(), "0.00"));
                psTemp.setString("u_Name", userName);
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

            for (int i = 0; i < mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().size(); i++) {
                if (!DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getAcNameCode()).equalsIgnoreCase("")) {
                    vSNo++;

//                    sqlQuery = "Insert Into Ledger (DocId, RecId, recIdPrefix, V_No, V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr,"
//                            + "remark,V_Type,V_Prefix,Site_Code,TDSCategory,TDSOnAmt,ContraText,"
//                            + "OrignalAmt,TDSDeductFrom,RefNo,RefDate,TDSFor, "
//                            + " u_Name,u_EntDt,favourOf) "
//                            + "Values(:DocId,:RecId, :recIdPrefix, :V_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr,:remark,"
//                            + ":V_Type,:V_Prefix,:Site_Code,:TDSCategory,:TDSOnAmt,:ContraText,:OrignalAmt,:TDSDeductFrom,:RefNo,"
//                            + ":RefDate,:TDSFor,:u_Name,:u_EntDt,:favourOf)";
                    sqlQuery = "Insert Into Ledger (DocId, RecId, recIdPrefix, V_No, V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr,"
                            + "remark,V_Type,V_Prefix,Site_Code,ContraText,"
                            + "RefNo,RefDate, "
                            + " u_Name,u_EntDt,chq_No, chq_Date, favourOf) "
                            + "Values(:DocId,:RecId, :recIdPrefix, :V_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr,:remark,"
                            + ":V_Type,:V_Prefix,:Site_Code,:ContraText,:RefNo,"
                            + ":RefDate,:u_Name,:u_EntDt, :chq_No, :chq_Date, :favourOf)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getAcNameCode()));
                    psTemp.setString("ContraSub", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                    psTemp.setString("AmtDr", "0.00");
                    psTemp.setString("AmtCr", DmServer.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getNetPay(), "0.00"));
                    psTemp.setString("remark", mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getRemark());
                    psTemp.setString("chq_No", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsNo()));
                    psTemp.setDate("chq_Date", DmServer.convertDate(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsDate()));
                    psTemp.setString("favourOf", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getFavourOf()));
                    psTemp.setString("V_Type", mrTemp.getVoucherTypeTag());
                    psTemp.setString("V_Prefix", voucherPrefix);
                    psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("ContraText", DmServer.prepareContraText(mrTemp.getSupplierText(), DmShared.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getNetPay()), "Dr"));
                    psTemp.setString("RefNo", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getInsNo()));
                    psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getInsDate()));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
//                    psTemp.setString("TDSCategory", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCategoryCode()));
//                    psTemp.setString("TDSOnAmt", DmServer.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsOnAmount(), "0.00"));
//                    psTemp.setString("OrignalAmt", DmServer.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getOriginalAmount(), "0.00"));
//                    psTemp.setString("TDSDeductFrom", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsDeductFormCode()));
//                    psTemp.setString("TDSFor", "P");
                    psTemp.executeUpdate();
                    psTemp.close();

                    /*==============================<< Insert record into cardLedger >>==========================*/
                    sqlQuery = "Insert into cardLedger(docId, v_SNo, v_Type, recId, v_Date, cardTypeCode, subCode, "
                            + " amtDr, amtCr, site_Code,cardNo,cardHolderName,cardDate"
                            + " ) "
                            + " values (:docId, :v_SNo, :v_Type, :recId, :v_Date, :cardTypeCode, :subCode, "
                            + " :amtDr, :amtCr, :site_Code,:cardNo,:cardHolderName ,:cardDate"
                            + " ) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmServer.vNull(vSNo, "0"));
                    psTemp.setString("v_Type", DmShared.iNull(mrTemp.getVoucherTypeTag()).toString());
                    psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setString("cardTypeCode", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getCardTypeCode()));
                    psTemp.setString("cardNo", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getInsNo()));
                    psTemp.setString("cardHolderName", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getRemark()));
                    psTemp.setDate("cardDate", DmServer.convertDate(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getInsDate()));
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getAcNameCode()));
                    psTemp.setString("amtDr", "0");
                    psTemp.setString("amtCr", DmServer.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getNetPay(), "0.00"));
                    psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.executeUpdate();
                    psTemp.close();

                    prvVSNo = vSNo; //For TDS 
         
                }

            }
            vSNo++;
            sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr, "
                    + "Narration,V_Type,V_Prefix,Site_Code,isSystem_Generated,ContraText,remark, chq_No, chq_Date, favourOf) Values "
                    + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr, "
                    + ":Narration,:V_Type,:V_Prefix,:Site_Code,'0',:ContraText,:remark, :chq_No, :chq_Date, :favourOf)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
            psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
            psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
            psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
            psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
            psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
            psTemp.setString("ContraSub", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getAcNameCode()));
            psTemp.setString("AmtDr", DmServer.vNull(mrTemp.getPaidAmount(), "0.00")); //& ", "  //"" & IIf(FGMain.Columns(GDebit).Visible, Val(LblDrAmt.Text)                
            psTemp.setString("AmtCr", "0.00");
            psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("chq_No", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsNo()));
            psTemp.setDate("chq_Date", DmServer.convertDate(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsDate()));
            psTemp.setString("favourOf", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getFavourOf()));
            psTemp.setString("Narration", DmShared.xNull(mrTemp.getRemark()));
            psTemp.setString("V_Type", mrTemp.getVoucherTypeTag());
            psTemp.setString("V_Prefix", voucherPrefix);
            psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("ContraText", DmServer.prepareContraText(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getAcNameName(), DmShared.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getNetPay()), "Cr"));

            psTemp.executeUpdate();
            psTemp.close();

            //===================================================== << TDS SAVE >>========================================================
            for (int i = 0; i < mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().size(); i++) {
                if (!DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getAcNameCode()).equalsIgnoreCase("")) {

                    if (mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl() != null) {
                        mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setSharedKeyFieldCode(mrTemp.getSharedKeyFieldCode());
                        mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setRecId(mrTemp.getRecId());
                        mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setVoucherDate(mrTemp.getVoucherDate());
                        mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setVoucherPrefix(voucherPrefix);
                        mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setVoucherTypeCode(mrTemp.getVoucherTypeTag());
                        TdsCategoryPopup_SrvImpl.saveTDS(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl(), DmServer.getSsnSiteCode(this), cnTemp);

                    }

                }
            }
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<AdvanceEntry_Mdl> getMoveRec(String strKey) {
        DmResult<AdvanceEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        AdvanceEntry_Mdl mrTemp = new AdvanceEntry_Mdl();
        PaymentPopUp_Mdl payment_Mdl = new PaymentPopUp_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select ad.docId,ad.recId,adr.docid as advReqDocId, ad.recIdPrefix,ad.v_No,ad.V_Type,VT.Description,adr.recId as advReqRecId,ad.paidamt,ad.originalAmt,ad.tdsDeduction,"
                    + "   ad.v_date as v_date ,adr.module,adr.poDocId,adr.dueDate ,adr.remark as reqRemark,adr.advanceReqAmt,po.recId as poRecId ,po.subCode,sg.name as supplierName,"
                    + "   po.v_date as poDate,PO.oh_Amt_Net as poAmt,po.subCode,sg.name as supplierName,sg.tds_Catg AS tdsCode,tc.Name AS tdsName,"
                    + "   ad.approvedBy, ad.preparedBy,ad.u_Name,sg.displayName  "
                    + " from advanceEntry ad "
                    + " left join advanceReqEntry adr on adr.docid=ad.reqDocId "
                    + " Left Join Voucher_Type VT On VT.V_Type = ad.V_Type "
                    + " left join porder po on po.docid=adr.poDocId "
                    + " left join subGroup sg on sg.subcode=po.subCode "
                    + " LEFT JOIN tdsCat tc ON sg.tds_Catg = tc.code "
                    + " where ad.docId= :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));

                mrTemp.getRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherTypeTag(DmShared.xNull(rsTemp.getString("V_Type")));
                mrTemp.setVoucherTypeText(DmShared.xNull(rsTemp.getString("Description")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_date")));
                mrTemp.setAdvReqDocId(DmShared.iNull(rsTemp.getString("advReqDocId")).toString());
                mrTemp.setAdvReqRecId(DmShared.xNull(rsTemp.getString("advReqRecId")));
                mrTemp.setPoDocId(DmShared.xNull(rsTemp.getString("poDocId")));
                mrTemp.setPoRecId(DmShared.xNull(rsTemp.getString("poRecId")).toString());
                mrTemp.setPoDate(DmShared.dNull(rsTemp.getDate("poDate")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setSupplierDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setPoAmt(DmServer.vNull(rsTemp.getString("poAmt")));

                mrTemp.setTdsCategoryCode(DmShared.xNull(rsTemp.getString("tdsCode")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("tdsName")));

                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("reqRemark")));

                mrTemp.setModuleTag(DmShared.xNull(rsTemp.getString("module")));
                mrTemp.setModuleText(DmShared.xNull(mrTemp.getModuleTag()).equalsIgnoreCase(pvc.PURCHASE_ORDER) ? "Purchase" : "Pharmacy");

                mrTemp.setDueDate(DmShared.dNull(rsTemp.getDate("dueDate")));

                mrTemp.setAdvanceReqAmt(DmShared.xNull(rsTemp.getString("advanceReqAmt")));
                mrTemp.setPaidAmount(DmShared.xNull(rsTemp.getString("paidamt")));
                mrTemp.setOriginalAmount(DmShared.xNull(rsTemp.getString("originalAmt")));
                mrTemp.setTdsDeduction(DmShared.xNull(rsTemp.getString("tdsDeduction")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));

            }

            psTemp.close();
            rsTemp.close();

            getPartyLedgerBalance(mrTemp, cnTemp);


            /*==============================< Get Paymnet pop up  Detail >==============================*/
            payment_Mdl = getPaymnetDetail(mrTemp.getSharedKeyFieldCode(),mrTemp.getPoDocId(), cnTemp);
            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().addAll(payment_Mdl.getLstPaymentMdl());
            mrTemp.setPaymentPopUp_Mdl(payment_Mdl);
            double amt = 0d;
            for (GrdPayment_Mdl node : mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl()) {

                amt += getBankCashLedgerBalance(DmShared.xNull(node.getAcNameCode()), cnTemp);

            }
            mrTemp.setBankCashLedgerBal(DmServer.vNull(amt));

            mrTemp.getLstGrdPoDetail().addAll(getGrdPoDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdMrnDetail().addAll(getGrdMrnDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdBillDetail().addAll(getGrdBillDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdAdvPaymentDetails().addAll(getGrdAdvPaymnetDetail(mrTemp.getPoDocId(), cnTemp));

            sqlQuery = " select ad.docid,ad.reqDocid ,fpr.pbDocid\n"
                    + "			from advanceEntry ad\n"
                    + "			left join advanceReqEntry adr on adr.docId=ad.reqDocId\n"
                    + "			left join pBillDetail pb on pb.poDocId=adr.poDocid\n"
                    + "			left join FinPayReqEnt fpr on fpr.pbDocid=pb.docId\n"
                    + "			where adr.poDocid=:docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setpBillDocid(DmShared.iNull(rsTemp.getString("pbDocid")).toString());

            }

            psTemp.close();
            rsTemp.close();
            if (DmShared.iNull(mrTemp.getpBillDocid()).longValue() != 0) {
                mrTemp.getLstGrdFinalPaymentDetails().addAll(getGrdFinalPaymentDetail(mrTemp.getpBillDocid(), cnTemp));
            }

            cnTemp.close();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
        } catch (SQLException | DmException e) {
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

    private List<GrdFinalPaymentDetail_Mdl> getGrdFinalPaymentDetail(String docId, Connection cnTemp) {
        List<GrdFinalPaymentDetail_Mdl> nodeList = new ArrayList<GrdFinalPaymentDetail_Mdl>();
        GrdFinalPaymentDetail_Mdl node = new GrdFinalPaymentDetail_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            /*====================================< Get Final Pay Detail >====================================*/
//           
            sqlQuery = "Select (fpe.docid) as docid ,max(fpe.recid) as recid ,"
                    + " max(fpe.v_No) as v_No,max(fpe.v_date) as date ,"
                    + " max(fpe.module) as module,max(fpe.supplierCode)as supplierCode,"
                    + " max(sg1.name) as supplierName,\n"
                    + " max(fpe.remark) as remark ,max(cm.code) as cardTypeCode, "
                    + " max(cm.name) As cardTypeName, "
                    + " max(cm.cardType) as cardType, max(cm.shortName) as cardShortName,"
                    + " max(cl.amtDr) as amtDr\n"
                    + " , max(cl.amtCr) as amtCr, \n"
                    + " max(sg.subcode) as subcode, max(sg.name) as acName,"
                    + " max(LGTDS.AmtDr) As TDSDeducted,"
                    + " max(lg.tdsOnAmt) as tdsOnAmt,max(LG.OrignalAmt) as finalAmt , \n"
                    + " max(sg.nature) as acNature, max(cm.nature) as cardTypeNature,"
                    + " max(cl.cardNo) as insNo,"
                    + " max(cl.cardHolderName) as remak,max(cl.cardDate) as insDate\n"
                    + " from FinPayEnt fpe  \n"
                    + " LEFT JOIN FinPayEntDetail AS fped ON fped.docid = fpe.docid \n"
                    + " LEFT JOIN FinPayReqEnt AS fpre ON fpre.docid = fped.payReqDocId  \n"
                    + " LEFT JOIN pBill AS pbd ON pbd.docid = fpre.pbDocId \n"
                    + " LEFT JOIN advanceEntry AS ad ON ad.finPayReqDocId = fpre.docid \n"
                    + " LEFT JOIN advanceReqEntry AS adre ON adre.docid = ad.docId\n"
                    + " LEFT JOIN voucher_Type AS vt ON fpe.v_Type = vt.v_Type \n"
                    + " left join ledger lg  On fpe.docId = lg.docid \n"
                    + " Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                    + " left join cardLedger cl  On LG.docId = cl.docid  And cl.V_SNo=LG.V_SNo  \n"
                    + " left join cardTypeMast cm On cl.cardTypeCode = cm.code \n"
                    + " left join subGroup sg On cl.subcode = sg.subCode \n"
                    + " left join subGroup sg1 On fpe.supplierCode = sg1.subCode\n"
                    + " WHERE fpre.pbDocid=:docId \n"
                    + " and lg.isSystem_Generated=0 \n"
                    + " group by fpe.docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdFinalPaymentDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("date")));
                node.setNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setMode(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setChqNo(DmShared.xNull(rsTemp.getString("insNo")));
                node.setChqDate(DmShared.dNull(rsTemp.getDate("insDate")));
                node.setBank(DmShared.xNull(rsTemp.getString("subcode")));
                node.setBank(DmShared.xNull(rsTemp.getString("acName")));
                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                node.setAmt(DmServer.vNull(rsTemp.getString("finalAmt"), "0.00"));
                node.setTds(DmServer.vNull(rsTemp.getString("tdsOnAmt"), "0.00"));
                node.setTdsDeduction(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private List<GrdAdvPaymentDetail_Mdl> getGrdAdvPaymnetDetail(String docId, Connection cnTemp) {
        List<GrdAdvPaymentDetail_Mdl> nodeList = new ArrayList<GrdAdvPaymentDetail_Mdl>();
        GrdAdvPaymentDetail_Mdl node = new GrdAdvPaymentDetail_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {

            /*====================================< Get advanceEntry Detail >====================================*/
            sqlQuery = "Select ad.docId,ad.v_date,ad.recId,cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, cm.shortName as cardShortName, cl.amtDr, cl.amtCr, "
                    + "     sg.subcode, sg.name as acName,LGTDS.AmtDr As TDSDeducted,lg.tdsOnAmt,LG.OrignalAmt,"
                    + "     sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,cl.cardHolderName as remak,cl.cardDate as insDate"
                    + "     from advanceEntry ad"
                    + "      left join advanceReqEntry adr on adr.docid=ad.reqDocId "
                    + "      left join pOrder po on po.docid=adr.poDocId "
                    + "	     left join ledger lg  On ad.docId = lg.docid "
                    + "      Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' "
                    + "      left join cardLedger cl  On LG.docId = cl.docid  And cl.V_SNo=LG.V_SNo "
                    + "      left join cardTypeMast cm On cl.cardTypeCode = cm.code "
                    + "      left join subGroup sg On cl.subcode = sg.subCode "
                    + "      where adr.poDocid=:docId and  ad.approvedBy is not null and lg.isSystem_Generated=0 "
                    + "      order by ad.v_no ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdAdvPaymentDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setMode(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setChqNo(DmShared.xNull(rsTemp.getString("insNo")));
                node.setChqDate(DmShared.dNull(rsTemp.getDate("insDate")));
                node.setBank(DmShared.xNull(rsTemp.getString("subcode")));
                node.setBank(DmShared.xNull(rsTemp.getString("acName")));
                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                node.setAmt(DmServer.vNull(rsTemp.getString("OrignalAmt"), "0.00"));
                node.setTds(DmServer.vNull(rsTemp.getString("tdsOnAmt"), "0.00"));
                node.setTdsDeduction(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private PaymentPopUp_Mdl getPaymnetDetail(String docId,String strPoDocID, Connection cnTemp) {
        List<GrdPayment_Mdl> lstNode = new ArrayList<GrdPayment_Mdl>();
        GrdPayment_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PaymentPopUp_Mdl mrTemp = new PaymentPopUp_Mdl();
        TdsCategoryGrid_Mdl tdsCat = new TdsCategoryGrid_Mdl();
        try {
            /*====================================< Get Paymnet Detail >====================================*/

            sqlQuery = " Select 	adv.tdsDeduction advTdsDeduction, cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, "
                    + "                     cm.shortName as cardShortName, cl.amtDr, cl.amtCr,                                              "
                    + "                      LGTDS.AmtDr As TDSDeducted, lg.tdsOnAmt, adv.OriginalAmt,                                        "
                    + "                       sg.subcode, sg.name as acName,lg.tdsCategory,TC.Name As TDSCName,"
                    + "                    sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,"
                    + "                     cl.cardHolderName as remark,cl.cardDate as insDate,lg.docid as ledgerDocId ,LG.v_SNo ,lg.favourOf "
                    + "                      from cardLedger cl "
                    + "			      left join ledger lg  On cl.docId = lg.docid  And cl.V_SNo=LG.V_SNo   "
                    + "                       Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')=''        "
                    + "                       Left Join TDSCat TC On TC.Code=LG.TDSCategory "
                    + "                       left join cardTypeMast cm On cl.cardTypeCode = cm.code        "
                    + "                       left join subGroup sg On cl.subcode = sg.subCode              "
                    + "                       left join advanceEntry adv on adv.docId = cl.docId            "
                    + "                       where cl.docId = :docId and   lg.isSystem_Generated=0         "
                    + "                       order By cl.V_SNo ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPayment_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));

                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                node.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setCardTypeShortName(DmShared.xNull(rsTemp.getString("cardShortName")));
                node.setCardTypeNatureCode(DmShared.xNull(rsTemp.getString("cardType")));
                node.setCardTypeNatureName(DmEnum.CardType.getValueOf(DmShared.iNull(rsTemp.getString("cardType")).intValue()).getEnumName());
                node.setAcNameCode(DmShared.xNull(rsTemp.getString("subcode")));
                node.setAcNameName(DmShared.xNull(rsTemp.getString("acName")));
                node.setAcNature(DmShared.xNull(rsTemp.getString("acNature")));
                node.setInsNo(DmShared.xNull(rsTemp.getString("insNo")));
                node.setInsDate(DmShared.dNull(rsTemp.getDate("insDate")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setFavourOf(DmShared.xNull(rsTemp.getString("favourOf")));
                node.setLedgerDocId(DmShared.xNull(rsTemp.getString("ledgerDocId")));
                node.setLedgerVSno(DmServer.vNull(rsTemp.getString("V_SNo"), "0"));
                node.setTdsCategoryCode(DmShared.xNull(rsTemp.getString("TDSCategory")));
                node.setTdsCategoryName(DmShared.xNull(rsTemp.getString("TDSCName")));
                node.setPoDocID(DmShared.xNull(strPoDocID));

                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    node.setNetPay(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                node.setOriginalAmount(DmServer.vNull(rsTemp.getString("OriginalAmt"), "0.00"));
                node.setTdsOnAmount(DmServer.vNull(rsTemp.getString("tdsOnAmt"), "0.00"));
                node.setTdsDeduction(DmServer.vNull(rsTemp.getString("advTdsDeduction"), "0.00"));
                lstNode.add(node);

            }

            psTemp.close();
            rsTemp.close();
            mrTemp.getLstPaymentMdl().addAll(lstNode);
            for (GrdPayment_Mdl temp : mrTemp.getLstPaymentMdl()) {
                if (!DmShared.xNull(temp.getTdsCategoryCode()).trim().equalsIgnoreCase("")) {
                    sqlQuery = "Select LG.FormulaString, Lg.roundOffTDS,LG.SubCode,SG.Name As AcName,sg.ManualCode as postingManualCode,"
                            + " LG.AmtDr,LG.AmtCr,LG.TDSDesc,TCD.manualCode as tcdManualCode,"
                            + " TCD.Name As DName,LG.TDSPer ,lg.isSystem_Generated "
                            + " From Ledger LG "
                            + " Left Join SubGroup SG On LG.SubCode=SG.SubCode "
                            + " Left Join TDSCat_Description TCD On TCD.Code=LG.TDSDesc  "
                            + " Where LG.DocId = :key And isNull(LG.isSystem_Generated,'0')='1' And "
                            + " isNull(LG.TDSDesc,'') <> '' And LG.TDS_Of_V_SNo = " + temp.getLedgerVSno().toString() + "  "
                            + " Order By V_SNo";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("key", DmShared.iNull(temp.getLedgerDocId()).toString());
                    rsTemp = psTemp.executeQuery();
                    while (rsTemp.next()) {
                        tdsCat = new TdsCategoryGrid_Mdl();
                        tdsCat.setDescriptionCode(DmShared.xNull(rsTemp.getString("TDSDesc")));
                        tdsCat.setDescriptionMCode(DmShared.xNull(rsTemp.getString("tcdManualCode")));
                        tdsCat.setDescription(DmShared.xNull(rsTemp.getString("DName")));
                        tdsCat.setPostingAccountCode(DmShared.xNull(rsTemp.getString("SubCode")));
                        tdsCat.setPostingAccount(DmShared.xNull(rsTemp.getString("AcName")));
                        tdsCat.setPostingManualCode(DmShared.xNull(rsTemp.getString("postingManualCode")));
                        tdsCat.setFormula(DmShared.xNull(rsTemp.getString("FormulaString")));
                        tdsCat.setRoundOffTDS(DmShared.xNull(rsTemp.getString("roundOffTDS")));
                        
                        tdsCat.setPercent(DmServer.vNull(rsTemp.getString("TDSPer"), "0.000"));
                        if (DmShared.vNull(rsTemp.getString("AmtDr")) > 0) {
                            tdsCat.setAmount(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                        } else {
                            tdsCat.setAmount(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                        }
                        temp.getTdsGridList().add(tdsCat);
                    }

                    rsTemp.close();
                    psTemp.close();
                }
                TdsCategoryPopup_SrvImpl.getTDSDetail(temp.getLedgerDocId(), temp.getTdsCatMdl(), cnTemp);

            }

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return mrTemp;
    }

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select adr.docId "
                    + "                        from advanceEntry adr "
                    + "                   	left join voucher_Type as vt on adr.v_Type = vt.v_Type "
                    + "                         where  vt.category = " + vCat + ""
                    + "                   	Order by  adr.v_Date,  adr.v_No, adr.recId ");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery;
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select ad.docid ,ad.recid as adReqNo,ad.v_No,ad.v_date ,adr.recId as adRecId,adr.poDocid,po.recId as poNo,adr.advanceReqAmt,\n"
                    + "  ad.approvedBy ,cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, cm.shortName as cardShortName, cl.amtDr, cl.amtCr, \n"
                    + "  sg.subcode, sg.name as acName, ad.TdsDeduction As TDSDeducted,lg.tdsOnAmt, ad.OriginalAmt as advanceAmt,sgs.name as supplierName,\n"
                    + "  sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,cl.cardHolderName as remak,cl.cardDate as insDate\n"
                    + "  from advanceEntry ad \n"
                    + "    LEFT JOIN advanceReqEntry adr on adr.docid=ad.reqDocId  \n"
                    + "    LEFT JOIN pOrder po on po.docid=adr.podocid \n"
                    + "    LEFT JOIN voucher_Type AS vt ON ad.v_Type = vt.v_Type \n"
                    + "    LEFT join cardLedger cl  On ad.docId = cl.docid "
                    + "    left join ledger lg  On ad.docId = lg.docid  and lg.isSystem_Generated=0 and lg.amtDr>0 \n"
                    + "    Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                    + "    left join cardTypeMast cm On cl.cardTypeCode = cm.code \n"
                    + "	   left join subGroup sgs On po.subcode = sgs.subCode "
                    + "	   left join subGroup sg On cl.subcode = sg.subCode "
                    + "	 WHERE vt.category in (" + vCat + ") "
                    + "  AND ad.site_Code = :site_Code "
                    + "  AND ad.v_prefix = :vPrefix";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("adReqNo")));
                listNode.setReqDate(DmShared.dNull(rsTemp.getDate("v_date")));
                listNode.setReqNo(DmShared.xNull(rsTemp.getString("adRecId")));
                listNode.setPoNoTag(DmShared.xNull(rsTemp.getString("poDocid")));
                listNode.setPoNoText(DmShared.xNull(rsTemp.getString("poNo")));
                listNode.setAdvanceReqAmt(DmServer.vNull(rsTemp.getString("advanceReqAmt"), "0.00"));
                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    listNode.setPaidAmt(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    listNode.setPaidAmt(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                listNode.setAdvanceAmt(DmServer.vNull(rsTemp.getString("advanceAmt"), "0.00"));
                listNode.setTdsDeduction(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                listNode.setBankName(DmShared.xNull(rsTemp.getString("acName")));
                listNode.setPayMode(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            sqlQuery = "select approvedBy  FROM advanceEntry WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Delete record from cardledger >>===============================*/
            sqlQuery = " Delete From cardledger Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from ledger >>===============================*/
            sqlQuery = " Delete From ledger Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from advanceEntry >>===============================*/
            sqlQuery = " Delete From advanceEntry Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //==================================<< TDS DELETE>>==============================================================
            TdsCategoryPopup_SrvImpl.deleteTDS(strKey, cnTemp);

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
                }
            } catch (Exception ex) {
                dmrTemp.setErrorMessage(DmShared.xNull(ex.getMessage()));
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
    public DmResult<AdvanceEntry_Mdl> getAdvReqEntryDetail(String advReqNo, String advReqDocId, String type) {
        DmResult<AdvanceEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Purchase Order detail >>===============================*/
            DmResult<AdvanceEntry_Mdl> resultAdvReqEntryDetail = getAdvReqEntryDetail(false, advReqNo, advReqDocId, type, cnTemp);
            dmrTemp.setData(resultAdvReqEntryDetail.getData());

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    public DmResult<AdvanceEntry_Mdl> getAdvReqEntryDetail(boolean isMoveRec, String advReqNo, String advReqDocId, String type, Connection cnTemp) throws DmException, SQLException {
        DmResult<AdvanceEntry_Mdl> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", condition = "";
        PaymentPopUp_Mdl paymentPopUp_Mdl = new PaymentPopUp_Mdl();
        AdvanceEntry_Mdl mrTemp = new AdvanceEntry_Mdl();
        dmrTemp.setData(mrTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(type).equalsIgnoreCase(pvc.ADVANCE_REQUEST_ENTRY)) {
                type = "'" + pvc.ADVANCE_REQUEST_ENTRY + "'";
            }
            if (!DmShared.xNull(type).trim().equalsIgnoreCase("")) {
                condition += " and (vt.category in (" + "'" + pvc.ADVANCE_REQUEST_ENTRY + "'" + ")) ";
            }
            if (DmShared.iNull(advReqDocId).longValue() != 0) {
                condition += " and adr.docId = :advReqDocId ";
            } else if (!DmShared.xNull(advReqNo).trim().equalsIgnoreCase("")) {
                condition += " and adr.recId = :advReqNo ";
            }

            sqlQuery = "Select   adr.docid as adrdocId,adr.recid as adrRecId,po.docid as poDocId , po.recId as poNo,po.v_date as poDate,po.subCode,\n"
                    + " sg.name as supplierName,sg.tds_Catg AS tdsCode,tc.Name AS tdsName,PO.oh_Amt_Net as poAmt,\n"
                    + " adr.module,adr.advanceReqAmt,adr.dueDate,adr.Remark,sg.displayName,bnk.accountDet,bnk.accountNo  \n"
                    + "		 from advanceReqEntry adr \n"
                    + "	         left join pOrder po on adr.podocid = po.docid \n"
                    + "          left join voucher_Type vt on vt.v_Type = adr.v_Type \n"
                    + "          left join subgroup sg on sg.subcode=po.subcode\n"
                    + "          left join (\n"
                    + "                     select subCode,isNull(bankName,'') + ' A/c No : '+isnull(accountNo,'') +' IFSC : ' "
                    + "                         + isNull(ifscCode,'') as accountDet,isNull(accountNo,'') as accountNo \n "
                    + "                         from bankDetail bnk "
                    + "                          where isDefault=1 "
                    + "                     ) as bnk on sg.subCode=bnk.subCode \n"
                    + "          LEFT JOIN tdsCat tc ON sg.tds_Catg = tc.code \n"
                    + "		where adr.site_Code = :siteCode \n" + condition
                    + "		order by adr.v_Date desc,  adr.v_No desc, adr.recId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(advReqDocId).longValue() != 0) {
                psTemp.setString("advReqDocId", DmShared.iNull(advReqDocId).toString());
            } else if (!DmShared.xNull(advReqNo).trim().equalsIgnoreCase("")) {
                psTemp.setString("advReqNo", DmShared.xNull(advReqNo));
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setAdvReqDocId(DmShared.iNull(rsTemp.getString("adrdocId")).toString());
                mrTemp.setAdvReqRecId(DmShared.xNull(rsTemp.getString("adrRecId")).toString());

                mrTemp.setPoDocId(DmShared.iNull(rsTemp.getString("poDocId")).toString());
                mrTemp.setPoRecId(DmShared.xNull(rsTemp.getString("poNo")).toString());

                mrTemp.setPoDate(DmShared.dNull(rsTemp.getDate("poDate")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("subcode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                
                if (!DmShared.xNull(rsTemp.getString("accountNo")).trim().equalsIgnoreCase("")){
                    mrTemp.setSupplierDisplayName(DmShared.xNull(rsTemp.getString("displayName")) +"#"+DmShared.xNull(rsTemp.getString("accountDet")));
                }else {
                    mrTemp.setSupplierDisplayName(DmShared.xNull(rsTemp.getString("supplierName")));
                }
                
                mrTemp.setPoAmt(DmServer.vNull(rsTemp.getString("poAmt")));

                mrTemp.setTdsCategoryCode(DmShared.xNull(rsTemp.getString("tdsCode")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("tdsName")));

                mrTemp.setModuleTag(DmShared.xNull(rsTemp.getString("module")));
                mrTemp.setModuleText(DmShared.xNull(mrTemp.getModuleTag()).equalsIgnoreCase(pvc.PURCHASE_ORDER) ? "Purchase" : "Pharmacy");
                mrTemp.setAdvanceReqAmt(DmServer.vNull(rsTemp.getString("advanceReqAmt")));
                mrTemp.setDueDate(DmShared.dNull(rsTemp.getDate("dueDate")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("Remark")));
            } else {
                throw new DmException(0, "No record found. Try Again.");
            }
            rsTemp.close();
            psTemp.close();

            getPartyLedgerBalance(mrTemp, cnTemp);
            /*==============================< Get Paymnet pop up  Detail >==============================*/
            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() != 0) {
                paymentPopUp_Mdl = getPaymnetDetail(mrTemp.getSharedKeyFieldCode(),mrTemp.getPoDocId(), cnTemp);
                mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().addAll(paymentPopUp_Mdl.getLstPaymentMdl());
                mrTemp.setPaymentPopUp_Mdl(paymentPopUp_Mdl);
                double amt = 0d;
                for (GrdPayment_Mdl node : mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl()) {

                    amt += getBankCashLedgerBalance(DmShared.xNull(node.getAcNameCode()), cnTemp);

                }
                mrTemp.setBankCashLedgerBal(DmServer.vNull(amt));
            }

            mrTemp.getLstGrdPoDetail().addAll(getGrdPoDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdMrnDetail().addAll(getGrdMrnDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdBillDetail().addAll(getGrdBillDetail(mrTemp.getPoDocId(), cnTemp));
            mrTemp.getLstGrdAdvPaymentDetails().addAll(getGrdAdvPaymnetDetail(mrTemp.getPoDocId(), cnTemp));

            sqlQuery = " select ad.docid,ad.reqDocid ,fpr.pbDocid\n"
                    + "			from advanceEntry ad\n"
                    + "			left join advanceReqEntry adr on adr.docId=ad.reqDocId\n"
                    + "			left join pBillDetail pb on pb.poDocId=adr.poDocid\n"
                    + "			left join FinPayReqEnt fpr on fpr.pbDocid=pb.docId\n"
                    + "			where adr.poDocid=:docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getPoDocId()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setpBillDocid(DmShared.iNull(rsTemp.getString("pbDocid")).toString());

            }

            psTemp.close();
            rsTemp.close();
            if (DmShared.iNull(mrTemp.getpBillDocid()).longValue() != 0) {
                mrTemp.getLstGrdFinalPaymentDetails().addAll(getGrdFinalPaymentDetail(mrTemp.getpBillDocid(), cnTemp));
            }

            dmrTemp.setStatus(true);
            cnTemp.close();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp!=null){
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void getPartyLedgerBalance(AdvanceEntry_Mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", cond = "";

        try {
            if (DmShared.iNull(mrTemp.getSupplierTag()).longValue() != 0) {
                cond = " and lg.subcode=:subcode";
            } else {
                cond = "and 1!=1";
            }
            sqlQuery = "Select  sum(lg.amtDr) as amtDr, sum(lg.amtCr) as amtCr "
                    + "	from ledger lg where "
                    + "	 lg.site_Code=:site_Code and lg.v_Date<=:toDate and lg.isPosted =1 " + cond
                    + " group by lg.subcode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(mrTemp.getSupplierTag()).longValue() != 0) {
                psTemp.setLong("subcode", DmShared.iNull(mrTemp.getSupplierTag()).longValue());
            }
            if (DmShared.dNull(mrTemp.getVoucherDate()).trim().equals("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmServer.getServerDateTime()[0]));

            } else {
                psTemp.setDate("toDate", DmServer.convertDate(mrTemp.getVoucherDate()));
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                double amt = DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() - DmShared.vNull(rsTemp.getString("amtCr")).doubleValue();
                mrTemp.setPartyLedgerBal(DmServer.vNull(amt));

            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
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

    private double getBankCashLedgerBalance(String subcode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", cond = "";
        double amt = 0d;
        try {
            if (DmShared.iNull(subcode).longValue() != 0) {
                cond = " and lg.subcode=:subcode";
            } else {
                cond = "and 1!=1";
            }
            sqlQuery = "Select  sum(lg.amtDr) as amtDr, sum(lg.amtCr) as amtCr "
                    + "	from ledger lg where "
                    + "	 lg.site_Code=:site_Code and lg.isposted =1 " + cond
                    + " group by lg.subcode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(subcode).longValue() != 0) {
                psTemp.setLong("subcode", DmShared.iNull(subcode).longValue());
            }

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                amt = DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() - DmShared.vNull(rsTemp.getString("amtCr")).doubleValue();

            }
            rsTemp.close();
            psTemp.close();

        } catch (SQLException | DmException e) {
            throw new DmException(0, e.getMessage());
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
        return amt;
    }

    private List<GrdPODetail_Mdl> getGrdPoDetail(String poDocId, Connection cnTemp) {
        List<GrdPODetail_Mdl> nodeList = new ArrayList<GrdPODetail_Mdl>();
        GrdPODetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get PO Detail >====================================*/
            sqlQuery = "Select pod.docid,pod.itemcode,im.name as itemName,"
                    + " pod.unitcode,um.unit as unit,pod.quantity,pod.rate,"
                    + " pod.oh_amt_net as amt,pod.oh_Amt_Taxable as taxableAmt, "
                    + " pod.oh_Amt_CGST+pod.oh_Amt_IGST+pod.oh_Amt_SGST+pod.oh_Amt_UGST as taxAmt "
                    + " from pOrderDetail pod "
                    + " LEFT JOIN itemMast im ON pod.itemcode = im.code "
                    + " LEFT JOIN unitmast um ON pod.unitcode = um.code "
                    + " where pod.docid = :docid  "
                    + " order by pod.v_sno ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(poDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPODetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                node.setQty(DmServer.vNull(rsTemp.getString("quantity"), "0.00"));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmt(DmServer.vNull(rsTemp.getString("amt"), "0.00"));
                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("taxableAmt"), "0.00"));
                node.setTaxAmt(DmServer.vNull(rsTemp.getString("taxAmt"), "0.00"));
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private List<GrdGRNDetail_Mdl> getGrdMrnDetail(String poDocId, Connection cnTemp) {
        List<GrdGRNDetail_Mdl> nodeList = new ArrayList<GrdGRNDetail_Mdl>();
        GrdGRNDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get GRN Detail >====================================*/
            sqlQuery = "Select  pc.docid,pc.v_date as grnDate,pc.recid as grnNo,"
                    + "pc.pChallanNo,pc.pChallanDate,pcd.oh_amt_net as value,"
                    + "pcd.podocid,pc.preparedBy,pcd.isStatus, im.name as itemName "
                    + " from pChallan pc "
                    + " LEFT JOIN pChallanDetail pcd  ON pc.docid = pcd.docid "
                    + "  LEFT JOIN itemMast im ON pcd.itemcode = im.code "
                    + " where  pcd.poDocId = :docid and pcd.poDocId is not null  "
                    + "and   pc.approvedBy is not null"
                    + " order by pc.v_no ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(poDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdGRNDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("grnDate")));
                node.setGrnNo(DmShared.xNull(rsTemp.getString("grnNo")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setChallanNo(DmShared.xNull(rsTemp.getString("pChallanNo")));
                node.setChallanDate(DmShared.dNull(rsTemp.getDate("pChallanDate")));
                node.setValue(DmServer.vNull(rsTemp.getString("value"), "0.00"));
                node.setEntryBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 0) {
                    node.setStatus("Pending");
                } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 1) {
                    node.setStatus("Cancelled");
                } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 2) {
                    node.setStatus("Completed");
                }
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    private List<GrdBillDetail_Mdl> getGrdBillDetail(String poDocId, Connection cnTemp) {
        List<GrdBillDetail_Mdl> nodeList = new ArrayList<GrdBillDetail_Mdl>();
        GrdBillDetail_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            /*====================================< Get Bill Detail >====================================*/
            sqlQuery = "Select pb.docId,pb.v_date,pb.recId,pb.partyBillNo,  "
                    + " pb.partyBillDate,pbd.oh_Amt_Net as value,   "
                    + " pbd.oh_Amt_Net as netPay,pb.preparedBy,im.name as itemName, pbd.isStatus "
                    + " from pBill pb "
                    + " LEFT JOIN pBillDetail pbd  ON pbd.docid = pb.docid "
                    + " LEFT JOIN itemMast im ON pbd.itemcode = im.code "
                    + " where  pbd.poDocId = :docid and pbd.poDocId is not null  and   pb.approvedBy is not null "
                    + " order by pb.v_no ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(poDocId).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdBillDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("v_date")));
                node.setBillNo(DmShared.xNull(rsTemp.getString("recId")));
                node.setPartyNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setPartyDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                node.setValue(DmServer.vNull(rsTemp.getString("value"), "0.00"));
                node.setNetPay(DmServer.vNull(rsTemp.getString("netPay"), "0.00"));
                node.setEntryBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 0) {
                    node.setStatus("Pending");
                } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 1) {
                    node.setStatus("Cancelled");
                } else if (DmShared.iNull(rsTemp.getString("isStatus")).intValue() == 2) {
                    node.setStatus("Completed");
                }
                nodeList.add(node);
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, DmShared.xNull(e.getMessage()));
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
        return nodeList;
    }

    @Override
    public DmResultPageing<List<GrdPayment_Mdl>> getTxtHCardType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdPayment_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdPayment_Mdl> lstTemp = new ArrayList<>();
        GrdPayment_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select  cm.code as cardTypeCode, cm.Name as cardTypeName, cm.shortName as cardShortName, "
                    + "                           sg.subcode, sg.name as acName, sg.nature as acNature, cm.cardType, cm.nature as cardTypeNature "
                    + "                           from cardTypeMast cm "
                    + "                           left join subGroup sg on cm.subcode = sg.subCode "
                    + "                           where cm.isActive = 1 and cm.shortName in ('BK','CS','cheque','NEFT','RTGS','FNDTRF') ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new GrdPayment_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setCardTypeCode(DmShared.xNull(rsTemp.getString("cardTypeCode")));
                listNode.setCardTypeName(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setCardTypeNatureCode(DmShared.xNull(rsTemp.getString("cardType")));
                listNode.setCardTypeNatureName(DmEnum.CardType.getValueOf(DmShared.iNull(rsTemp.getString("cardType")).intValue()).getEnumName());
                listNode.setCardTypeShortName(DmShared.xNull(rsTemp.getString("cardShortName")));
                listNode.setAcNameCode(DmShared.xNull(rsTemp.getString("subcode")));
                listNode.setAcNameName(DmShared.xNull(rsTemp.getString("acName")));
                listNode.setAcNature(DmShared.xNull(rsTemp.getString("acNature")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccounts(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("select  sg.subCode, sg.name, sg.nature "
                    + "		from subGroup sg "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		left join subGroupNature sgn on sgn.code = sg.nature "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :siteCode "
                    + "		and sgn.manualCode in ('BK','CS')");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("nature")));
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
    public DmResult<String> printRecords(ChequePrint_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String strSQL, strSQL1, condition = "", amount = "";
        ResultSet rsTemp = null, rsTemp1 = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        ChequePrintSetup_Mdl mrTempChquePrintSetup = new ChequePrintSetup_Mdl();
        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            amount = " CASE WHEN ISNULL(lm.amtDr,0)>0 THEN ISNULL(lm.amtDr,0) ELSE ISNULL(lm.amtCr,0) END ";

            if (DmShared.iNull(mrTemp.getHeaderAccountCode()).longValue() > 0) {
                condition += " AND lm.contraSub=:contraSub ";
            }
            if (DmShared.iNull(mrTemp.getVoucherTypeTag()).longValue() > 0) {
                condition += " AND lm.v_type=:v_type ";
            }
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                condition += " AND lm.docId IN (" + DmServer.getParamList(":docId", mrTemp.getSharedKeyFieldCode()) + " )";
            }
            if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                condition += " and lm.subCode IN (" + DmServer.getParamList(":subCode", mrTemp.getAccountCode()) + " )";
            }

            strSQL = "        SELECT "
                    + "             lm.docid as code "
                    + "           , lm.recid as name "
                    + "           , lm.v_SNo  "
                    + "           , lm.v_Date "
                    + "           , 1 as isCrossCheque "
                    + "           , lm.isCrossCheque "
                    + "           , vt.short_Name "
                    + "           , sgc.displayName as accountName "
                    + "           , lm.chq_No as chqNo"
                    + "           , format(lm.chq_Date,'ddMMyyyy') as  chqDate "
                    + "           , lm.favourOf as favourOf "
                    + "           , " + amount + " as amount "
                    + "         FROM ledger lm "
                    + "         LEFT JOIN subGroup sg ON lm.subCode=sg.subCode "
                    + "         LEFT JOIN subGroup sgc ON lm.contraSub=sgc.subCode "
                    + "         LEFT JOIN voucher_type vt ON vt.v_Type=lm.v_Type "
                    + "		LEFT JOIN subGroupSiteList SGL ON SG.subCode = SGL.subCode  "
                    + "         WHERE SGL.site_Code = :site_Code "
                    + "		AND sg.isActive = 1  and lm.isPosted =1 " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(mrTemp.getHeaderAccountCode()).longValue() > 0) {
                psTemp.setString("contraSub", DmShared.iNull(mrTemp.getHeaderAccountCode()).toString());
            }
            if (DmShared.iNull(mrTemp.getVoucherTypeTag()).longValue() > 0) {
                psTemp.setString("v_type", DmShared.iNull(mrTemp.getVoucherTypeTag()).toString());
            }
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            }

            if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(mrTemp.getAccountCode()));
            }
            rsTemp = psTemp.executeQuery();

            strSQL1 = "  SELECT  "
                    + "   cps.bankCode "
                    + " , cps.chequeDateTop "
                    + " , cps.chequeDateLeft "
                    + " , cps.crossChequeTop "
                    + " , cps.crossChequeLeft "
                    + " , cps.favourOfTop "
                    + " , cps.favourOfLeft "
                    + " , cps.AmtInWordsTop "
                    + " , cps.AmtInWordsLeft "
                    + " , cps.AmtTop "
                    + " , cps.AmtLeft "
                    + " , cps.authSignatory "
                    + " , cps.authSignatoryTop "
                    + " , cps.authSignatoryLeft "
                    + " , cps.companyName "
                    + " , cps.companyNameTop "
                    + " , cps.companyNameLeft"
                    + " , cps.paperWidth"
                    + " , cps.paperHeight"
                    + " FROM chequePrintingSetup as cps "
                    + " where cps.bankCode= :bankCode ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL1);
            psTemp1.setString("bankCode", DmShared.iNull(mrTemp.getHeaderAccountCode()).toString());
            rsTemp1 = psTemp1.executeQuery();
            if (rsTemp1.next()) {
                mrTempChquePrintSetup.setBankCode(DmShared.xNull(rsTemp1.getString("bankCode")));
                mrTempChquePrintSetup.setChequeDateTop(DmServer.vNull(rsTemp1.getString("chequeDateTop"), "0.00"));
                mrTempChquePrintSetup.setChequeDateLeft(DmServer.vNull(rsTemp1.getString("chequeDateLeft"), "0.00"));
                mrTempChquePrintSetup.setFavourOfTop(DmServer.vNull(rsTemp1.getString("favourOfTop"), "0.00"));
                mrTempChquePrintSetup.setFavourOfLeft(DmServer.vNull(rsTemp1.getString("favourOfLeft"), "0.00"));
                mrTempChquePrintSetup.setAmtInWordsTop(DmServer.vNull(rsTemp1.getString("AmtInWordsTop"), "0.00"));
                mrTempChquePrintSetup.setAmtInWordsLeft(DmServer.vNull(rsTemp1.getString("AmtInWordsLeft"), "0.00"));
                mrTempChquePrintSetup.setAmtTop(DmServer.vNull(rsTemp1.getString("AmtTop"), "0.00"));
                mrTempChquePrintSetup.setAmtLeft(DmServer.vNull(rsTemp1.getString("AmtLeft"), "0.00"));
                mrTempChquePrintSetup.setAuthSign(DmShared.xNull(rsTemp1.getString("authSignatory")));
                mrTempChquePrintSetup.setAuthSignTop(DmServer.vNull(rsTemp1.getString("authSignatoryTop"), "0.00"));
                mrTempChquePrintSetup.setAuthSignLeft(DmServer.vNull(rsTemp1.getString("authSignatoryLeft"), "0.00"));
                mrTempChquePrintSetup.setCompanyName(DmShared.xNull(rsTemp1.getString("companyName")));
                mrTempChquePrintSetup.setCompanyNameTop(DmServer.vNull(rsTemp1.getString("companyNameTop"), "0.00"));
                mrTempChquePrintSetup.setCompanyNameLeft(DmServer.vNull(rsTemp1.getString("companyNameLeft"), "0.00"));
                mrTempChquePrintSetup.setCrossChequeTop(DmServer.vNull(rsTemp1.getString("crossChequeTop"), "0.00"));
                mrTempChquePrintSetup.setCrossChequeLeft(DmServer.vNull(rsTemp1.getString("crossChequeLeft"), "0.00"));
                mrTempChquePrintSetup.setPaperWidth(DmServer.vNull(rsTemp1.getString("paperWidth"), "0.00"));
                mrTempChquePrintSetup.setPaperHeight(DmServer.vNull(rsTemp1.getString("paperHeight"), "0.00"));
            }
            psTemp1.close();
            rsTemp1.close();

            //<editor-fold defaultstate="collapsed" desc="JASPER">
            LinkedHashMap hm = new LinkedHashMap();
            hm.put("companyName", DmShared.xNull(mrTempChquePrintSetup.getCompanyName()));
            hm.put("authSignatory", DmShared.xNull(mrTempChquePrintSetup.getAuthSign()));

            //<editor-fold defaultstate="collapsed" desc="comment">
            String reportPath = DmBaseServer.Path.reports;
            DmJasperCompiler jasperCompiler = null;

            String subDirectory = "";
            if (DmBaseShared.xNull(subDirectory).equalsIgnoreCase("")) {
                subDirectory = "system";
            }

            reportPath = reportPath + "/" + subDirectory + "/AC_ChequePrinting.jrxml";

            JasperDesign jasperDesign = JRXmlLoader.load(reportPath);

            jasperDesign.setPageHeight(DmShared.iNull(mrTempChquePrintSetup.getPaperHeight()).intValue());
            jasperDesign.setPageWidth(DmShared.iNull(mrTempChquePrintSetup.getPaperWidth()).intValue());

            jasperReport = JasperCompileManager.compileReport(jasperDesign);
//</editor-fold>

            JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {
                @Override
                public void visitBreak(JRBreak jrb) {
                    try {
                        Field topField = JRBaseElement.class.getDeclaredField("y");
                        topField.setAccessible(true);
                        int oldValue = topField.getInt(jrb);
                        int newTopValue = 0;
                        if (DmShared.xNull(jrb.getKey()).equalsIgnoreCase("breakPage")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getPaperHeight()).doubleValue())).intValue();
                            topField.setInt(jrb, newTopValue);
                            ((JRBaseElement) jrb).getEventSupport().firePropertyChange(topField.getName(), oldValue, newTopValue);
                            jrb.setX(0);
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void visitChart(JRChart jrc) {
                }

                @Override
                public void visitCrosstab(JRCrosstab jrc) {
                }

                @Override
                public void visitElementGroup(JRElementGroup jreg) {
                }

                @Override
                public void visitEllipse(JREllipse jre) {

                }

                @Override
                public void visitFrame(JRFrame jrf) {

                }

                @Override
                public void visitImage(JRImage jri) {

                }

                @Override
                public void visitLine(JRLine jrline) {

                }

                @Override
                public void visitRectangle(JRRectangle jrr) {

                }

                @Override
                public void visitStaticText(JRStaticText jrst) {
                }

                @Override
                public void visitSubreport(JRSubreport jrs) {

                }

                @Override
                public void visitTextField(JRTextField textField) {
                    try {
                        Field topField = JRBaseElement.class.getDeclaredField("y");
                        topField.setAccessible(true);
                        int oldValue = topField.getInt(textField);
                        boolean flagSetValue = false;
                        int newTopValue = 0, newLeftValue = 0;

                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("chqDate")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getChequeDateTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getChequeDateLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("amountInWords")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtInWordsTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtInWordsLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("favourOf")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getFavourOfTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getFavourOfLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("amount")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("authSignatory")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAuthSignTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAuthSignLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("companyName")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCompanyNameTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCompanyNameLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("acPayee")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCrossChequeTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCrossChequeLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (flagSetValue) {
                            topField.setInt(textField, newTopValue);
                            ((JRBaseElement) textField).getEventSupport().firePropertyChange(topField.getName(), oldValue, newTopValue);
                            textField.setX(newLeftValue);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void visitComponentElement(JRComponentElement jrce) {
                }

                @Override
                public void visitGenericElement(JRGenericElement jrge) {

                }
            });
            jasperPrint = DmServer.fillJasper(this, "", jasperReport, hm, null, rsTemp);
            DmReportProperty_Mdl property_Mdl = new DmReportProperty_Mdl();
            property_Mdl.setExportType(DmEnum.DmExportType.PDF);
            dmrTemp.setData(DmServer.showJasper(this, jasperPrint, "", property_Mdl));
//</editor-fold>

            spBegin.commit();
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
    //</editor-fold>

    @Override
    public DmResult<List<TdsCategoryGrid_Mdl>> getTDSData(long tdsCatCode) {
        DmResult<List<TdsCategoryGrid_Mdl>> rstTemp = new DmResult<>();
        List<TdsCategoryGrid_Mdl> lstTemp = new ArrayList<>();
        TdsCategoryGrid_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, ac_RoundOffTds = "N";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT e.ac_RoundOffTDS FROM enviro e where e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                ac_RoundOffTds = DmShared.xNull(rsTemp.getString("ac_RoundOffTDS"));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select TC.TDSDesc,tcd.manualCode as tcdManualCode,TCD.Name As DName,TC.ACCode,"
                    + " SG.Name As AName,TC.Percentage,TC.FormulaString,SG.ManualCode as postingManualCode "
                    + " From TDSCat_Det TC "
                    + " Left Join TDSCat_Description TCD On TC.TDSDesc=TCD.Code "
                    + " Left Join SubGroup SG On TC.AcCode=SG.SubCode "
                    + " Where TC.Code=:code "
                    + " Order By TC.SrNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(tdsCatCode).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TdsCategoryGrid_Mdl();
                listNode.setDescription(DmShared.xNull(rsTemp.getString("DName")));
                listNode.setDescriptionCode(DmShared.xNull(rsTemp.getString("TDSDesc")));
                listNode.setDescriptionMCode(DmShared.xNull(rsTemp.getString("tcdManualCode")));
                listNode.setPostingAccount(DmShared.xNull(rsTemp.getString("AName")));
                listNode.setPostingAccountCode(DmShared.xNull(rsTemp.getString("ACCode")));
                listNode.setPostingManualCode(DmShared.xNull(rsTemp.getString("postingManualCode")));
                listNode.setPercent(DmServer.vNull(rsTemp.getString("Percentage"), "0.000"));
                listNode.setFormula(DmShared.xNull(rsTemp.getString("FormulaString")));
                listNode.setRoundOffTDS(DmShared.xNull(ac_RoundOffTds));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();

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
}
