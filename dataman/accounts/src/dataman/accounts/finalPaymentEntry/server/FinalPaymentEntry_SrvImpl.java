package dataman.accounts.finalPaymentEntry.server;

import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Mdl;
import dataman.accounts.finalPaymentEntry.shared.*;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

public class FinalPaymentEntry_SrvImpl extends DmRSSPrivate implements FinalPaymentEntry_Srv {

    private LibraryVoucherCategory pvc = new LibraryVoucherCategory();

    private final String vCat = "'" + pvc.FINAL_PAYMENT_ENTRY + "'";

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

            sqlQuery = "SELECT  vt.v_Type, vt.description, vt.guiTemplateCode "
                    + "         FROM voucher_Type vt "
                    + "         WHERE vt.short_Name = " + vCat + " AND vt.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.iNull(rsTemp.getString("v_Type")).toString());
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.PURCHASE_ORDER);
            listNode.setSharedKeyFieldName("Purchase");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.PHARMACY_PURCHASE_ORDER);
            listNode.setSharedKeyFieldName("Pharmacy");
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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, c.stateCode, sg.partyType, sg.gstin, sg.gstinDate ,"
                    + " sg.tds_Catg AS tdsCode,tc.Name AS tdsName,sg.displayName,bnk.accountDet,bnk.accountNo "
                    + " FROM subGroup sg "
                    + " LEFT JOIN tdsCat tc ON sg.tds_Catg = tc.code "
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode "
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode "
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature "
                    + "left join (\n"
                    + "          select subCode,isNull(bankName,'') + ' A/c No : '+isnull(accountNo,'') +' IFSC : ' "
                    + "          + isNull(ifscCode,'') as accountDet,isNull(accountNo,'') as accountNo \n "
                    + "             from bankDetail bnk "
                    + "             where isDefault=1 "
                    + "             ) as bnk on sg.subCode=bnk.subCode \n"
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type) "
                    + " AND sg.isActive = 1 "
                    + " AND sgsl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setTdsCatCode(DmShared.xNull(rsTemp.getString("tdsCode")));
                listNode.setTdsCatName(DmShared.xNull(rsTemp.getString("tdsName")));
                if (!DmShared.xNull(rsTemp.getString("accountNo")).trim().equalsIgnoreCase("")) {
                    listNode.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")) + "#" + DmShared.xNull(rsTemp.getString("accountDet")));
                } else {
                    listNode.setDisplayName(DmShared.xNull(rsTemp.getString("name")));
                }

                lstTemp.add(listNode);
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

    @Override
    public DmResult<String> saveRecord(FinalPaymentEntry_Mdl mrTemp, boolean isApprove) {
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
        int vSNo = 0, prvVSNo = 0, payVSno = 0, lgAmountV_Sno = 0;
        try {
            FinalPaymentEntry_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            voucherPrefix = DmServer.getSsnVoucherPrefix(this);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this));
                mrTemp.setRecId(DmServer.getRecId("FinPayEnt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getV_Date(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecId().getRecIdValue()));
//            } else {
//                DmServer.getRecId("FinPayEnt", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecId(), mrTemp.getV_Date(), DmShared.xNull(mrTemp.getVoucherTypeTag()), voucherPrefix, cnTemp, this, true);
            }

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Check for Approvedby if already approved >>====================================*/
                sqlQuery = "select approvedBy  FROM FinPayEnt WHERE  docId = :docId ";
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
            //======== update advanceEntry finpayRequestDocid to null============
            sqlQuery = "    Update advanceEntry set "
                    + "     finPayReqDocId=NUll"
                    + "     where finPayReqDocId=:finPayReqDocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("finPayReqDocId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From FinPayEntDetail Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            ////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From FinPayEnt_OthDetail Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
/////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From cardLedger Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /////////////////////////////////////////////////////////////////////////////////////
            sqlQuery = "Delete From LedgerAdj Where vr_DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From Ledger Where DocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();
            /*==============================<< Delete TDS Entry of current Bill >>====================================*/
            TdsCategoryPopup_SrvImpl.deleteTDS(mrTemp.getSharedKeyFieldCode(), cnTemp);

            if (blnAdd) {
                sqlQuery = "Insert Into FinPayEnt( docId, recId,recIdPrefix,v_No,v_Type,v_Prefix,v_Date,module,supplierCode, preparedBy,amtPaid,originalAmt,tdsDeduction,\n"
                        + "                        preparedDate,site_Code,remark,drAdjustAmt)"
                        + " values ( :docId, :recId,:recIdPrefix,:v_No,:v_Type,:v_Prefix,:v_Date,:module,:supplierCode, :preparedBy,:amtPaid,:originalAmt,:tdsDeduction,\n"
                        + "                        :preparedDate,:site_Code,:remark,:drAdjustAmt) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeTag()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getV_Date()));
                psTemp.setString("module", DmShared.xNull(mrTemp.getModuleTag()).toString());
                psTemp.setString("supplierCode", DmShared.iNull(mrTemp.getSupplierTag()).toString());
                psTemp.setString("amtPaid", DmServer.vNull(mrTemp.getPaidAmt(), "0.00"));
                psTemp.setString("originalAmt", DmServer.vNull(mrTemp.getOriginalAmt(), "0.00"));
                psTemp.setString("tdsDeduction", DmServer.vNull(mrTemp.getTdsDeduction(), "0.00"));
                psTemp.setString("drAdjustAmt", DmServer.vNull(mrTemp.getDrAdjustAmt(), "0.00"));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("preparedBy", userName);
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));

            } else {
                sqlQuery = "Update FinPayEnt set "
                        + " amtPaid= :amtPaid,"
                        + " originalAmt= :originalAmt,"
                        + " tdsDeduction= :tdsDeduction,"
                        + " drAdjustAmt= :drAdjustAmt,"
                        + " remark= :remark,"
                        + " U_EntDt= :U_EntDt,"
                        + " u_Name=:u_Name "
                        + " where docId=:docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("amtPaid", DmServer.vNull(mrTemp.getPaidAmt(), "0.00"));
                psTemp.setString("originalAmt", DmServer.vNull(mrTemp.getOriginalAmt(), "0.00"));
                psTemp.setString("tdsDeduction", DmServer.vNull(mrTemp.getTdsDeduction(), "0.00"));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("u_Name", userName);
                psTemp.setString("drAdjustAmt", DmServer.vNull(mrTemp.getDrAdjustAmt(), "0.00"));
                psTemp.setTimestamp("U_EntDt", DmServer.getServerTimestamp());

            }
            psTemp.executeUpdate();
            psTemp.close();
            narration = "Against Bill :";
            for (GrdPendingFPReqDetail_Mdl node : mrTemp.getLstPendingFPayReq()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0 && DmShared.xNull(node.getChecked()).equalsIgnoreCase("1")) {
                    payVSno++;

                    if (payVSno - 1 == 0) {
                        narration += mrTemp.getLstPendingFPayReq().get(payVSno - 1).getPartyBillNo();
                    } else {
                        narration += ", " + mrTemp.getLstPendingFPayReq().get(payVSno - 1).getPartyBillNo();
                    }

                    sqlQuery = "Insert Into FinPayEntDetail (docId, recId, recIdPrefix, V_No, V_SNo,V_Date,v_prefix,payReqDocId,finReqAmt,drAdjustAmt) "
                            + "Values(:docId, :recId, :recIdPrefix, :V_No, :V_SNo,:V_Date,:v_prefix,:payReqDocId,:finReqAmt,:drAdjustAmt)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(payVSno, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                    psTemp.setString("payReqDocId", node.getSharedKeyFieldCode());
                    psTemp.setString("finReqAmt", DmServer.vNull(node.getFinalPayReq(), "0.00"));
                    psTemp.setString("V_Prefix", voucherPrefix);
                    psTemp.setString("drAdjustAmt", DmServer.vNull(node.getDrAdjustAmt(), "0.00"));
                    psTemp.executeUpdate();
                    psTemp.close();

                }
            }
            narration += "    ";
            if (DmShared.vNull(mrTemp.getOriginalAmt()).doubleValue() > 0) {

                for (int i = 0; i < mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().size(); i++) {
                    if (!DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getAcNameCode()).equalsIgnoreCase("")) {
                        vSNo++;

                        sqlQuery = "Insert Into Ledger (DocId, RecId, recIdPrefix, V_No, V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr,"
                                + "remark,V_Type,V_Prefix,Site_Code,ContraText,"
                                + "RefNo,RefDate,TDSFor, u_Name,u_EntDt, chq_No, chq_Date, favourOf, narration) "
                                + "Values(:DocId,:RecId, :recIdPrefix, :V_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr,:remark,"
                                + ":V_Type,:V_Prefix,:Site_Code,:ContraText,:RefNo,"
                                + ":RefDate,:TDSFor,:u_Name,:u_EntDt, :chq_No, :chq_Date, :favourOf, :narration)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                        psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                        psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                        psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                        psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                        psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getAcNameCode()));
                        psTemp.setString("ContraSub", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                        psTemp.setString("AmtDr", "0.00");
                        psTemp.setString("AmtCr", DmServer.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getNetPay(), "0.00"));
                        psTemp.setString("remark", mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getRemark());
                        psTemp.setString("Narration", narration + mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getRemark());
                        psTemp.setString("V_Type", mrTemp.getVoucherTypeTag());
                        psTemp.setString("V_Prefix", voucherPrefix);
                        psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                        psTemp.setString("ContraText", DmServer.prepareContraText(mrTemp.getSupplierText(), DmShared.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getNetPay()), "Dr"));
                        psTemp.setString("RefNo", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getInsNo()));
                        psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getInsDate()));

                        psTemp.setString("TDSFor", "P");
                        psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                        psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                        psTemp.setString("chq_No", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsNo()));
                        psTemp.setDate("chq_Date", DmServer.convertDate(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsDate()));
                        psTemp.setString("favourOf", mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getFavourOf());
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
                        psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getV_Date()));
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
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("ContraSub", DmShared.foriegnKey(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getAcNameCode()));
                psTemp.setString("AmtDr", DmServer.vNull(mrTemp.getPaidAmt(), "0.00")); //& ", "  //"" & IIf(FGMain.Columns(GDebit).Visible, Val(LblDrAmt.Text)                
                psTemp.setString("AmtCr", "0.00");
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("Narration", narration + DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("V_Type", mrTemp.getVoucherTypeTag());
                psTemp.setString("V_Prefix", voucherPrefix);
                psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("ContraText", DmServer.prepareContraText(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getAcNameName(), DmShared.vNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getNetPay()), "Cr"));
                psTemp.setString("chq_No", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsNo()));
                psTemp.setDate("chq_Date", DmServer.convertDate(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getInsDate()));
                psTemp.setString("favourOf", DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(0).getFavourOf()));
                psTemp.executeUpdate();
                psTemp.close();

                lgAmountV_Sno = vSNo;
            }

            payVSno = 0;

            for (GrdOtherAc_Mdl node : mrTemp.getLstOtherAc()) {
                if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                    payVSno++;

                    sqlQuery = "Insert Into FinPayEnt_OthDetail (docId, recId, recIdPrefix, V_No, V_SNo,V_Date,v_prefix,otherAc,amount,remark) "
                            + "Values(:docId, :recId, :recIdPrefix, :V_No, :V_SNo,:V_Date,:v_prefix,:otherAc,:amount,:remark)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(payVSno, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                    psTemp.setString("otherAc", node.getSharedKeyFieldCode());
                    psTemp.setString("amount", DmServer.vNull(node.getAmount(), "0.00"));
                    psTemp.setString("remark", DmShared.xNull(node.getRemark()));
                    psTemp.setString("V_Prefix", voucherPrefix);
                    psTemp.executeUpdate();
                    psTemp.close();

                    /**
                     * ****************************************<<Ledger Posting for Other Deduction>>******************************************
                     */
                    vSNo++;
                    sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr, "
                            + "Narration,V_Type,V_Prefix,Site_Code,isSystem_Generated,ContraText,remark) Values "
                            + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr, "
                            + ":Narration,:V_Type,:V_Prefix,:Site_Code,'1',:ContraText,:remark)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                    psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                    psTemp.setString("ContraSub", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("AmtDr", DmServer.vNull(node.getAmount(), "0.00"));
                    psTemp.setString("AmtCr", "0.00");
                    psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                    psTemp.setString("Narration", DmShared.xNull(node.getRemark()));
                    psTemp.setString("V_Type", mrTemp.getVoucherTypeTag());
                    psTemp.setString("V_Prefix", voucherPrefix);
                    psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("ContraText", DmServer.prepareContraText(node.getSharedKeyFieldName(), DmShared.vNull(node.getAmount()), "Cr"));
                    psTemp.executeUpdate();
                    psTemp.close();

                    vSNo++;
                    sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr, "
                            + "Narration,V_Type,V_Prefix,Site_Code,isSystem_Generated,ContraText,remark) Values "
                            + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr, "
                            + ":Narration,:V_Type,:V_Prefix,:Site_Code,'1',:ContraText,:remark)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                    psTemp.setString("SubCode", DmShared.foriegnKey(node.getSharedKeyFieldCode()));
                    psTemp.setString("ContraSub", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                    psTemp.setString("AmtDr", "0.00");
                    psTemp.setString("AmtCr", DmServer.vNull(node.getAmount(), "0.00"));
                    psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                    psTemp.setString("Narration", DmShared.xNull(node.getRemark()));
                    psTemp.setString("V_Type", mrTemp.getVoucherTypeTag());
                    psTemp.setString("V_Prefix", voucherPrefix);
                    psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("ContraText", DmServer.prepareContraText(mrTemp.getSupplierText(), DmShared.vNull(node.getAmount()), "Dr"));
                    psTemp.executeUpdate();
                    psTemp.close();
                    /**
                     * ****************************************<<End Ledger Posting Other Deduction>>********************************************
                     */
                }
            }
            if (blnAdd) {
                for (int i = 0; i < mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().size(); i++) {
                    if (!DmShared.xNull(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getAcNameCode()).equalsIgnoreCase("")) {
                        //===================================================== << TDS SAVE >>========================================================
                        if (mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl() != null) {
                            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setSharedKeyFieldCode(mrTemp.getSharedKeyFieldCode());
                            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setRecId(mrTemp.getRecId());
                            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setVoucherDate(mrTemp.getV_Date());
                            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setVoucherPrefix(voucherPrefix);
                            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setVoucherTypeCode(mrTemp.getVoucherTypeTag());
                            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl().setCommonNarration(narration);
                            TdsCategoryPopup_SrvImpl.saveTDS(mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().get(i).getTdsCatMdl(), DmServer.getSsnSiteCode(this), cnTemp);
                        }
                    }
                }
            }

            //insert LedgerAdj
            /*==============================<< Delete record from ladgerAdj >>===============================*/
            sqlQuery = " Delete From ledgerAdj Where finPayDocId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {

                for (Map.Entry<String, List<HashMap<String, LedgerAdj_Mdl>>> entry : mrTemp.getListHmMain().entrySet()) {
                    String vr_DocId = entry.getKey();
                    List<HashMap<String, LedgerAdj_Mdl>> listOfMaps = entry.getValue();
                    for (HashMap<String, LedgerAdj_Mdl> innerMap : listOfMaps) {
                        for (Map.Entry<String, LedgerAdj_Mdl> innerEntry : innerMap.entrySet()) {
                            String adj_DocId = innerEntry.getKey();
                            LedgerAdj_Mdl ledgerMdlValue = innerEntry.getValue();
                            if (DmShared.vNull(ledgerMdlValue.getAdjustment()) != 0) {
                                sqlQuery = "INSERT INTO ledgerAdj (vr_DocId, vr_V_SNo, vr_SubCode, vr_V_Date, adj_DocId, adj_V_SNo, adj_SubCode, adj_V_Date, adjustment,finPayDocId)"
                                        + " VALUES (:vr_DocId, :vr_V_SNo, :vr_SubCode, :vr_V_Date, :adj_DocId, :adj_V_SNo, :adj_SubCode, :adj_V_Date, :adjustment,:finPayDocId)";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("vr_DocId", DmShared.foriegnKey(vr_DocId));
                                psTemp.setString("vr_V_SNo", DmShared.foriegnKey(ledgerMdlValue.getVr_V_SNo()));
                                psTemp.setString("vr_SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                                psTemp.setDate("vr_V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                                psTemp.setString("adj_DocId", DmShared.foriegnKey(adj_DocId));
                                psTemp.setString("adj_V_SNo", DmShared.foriegnKey(DmShared.xNull(ledgerMdlValue.getAdj_V_SNo())));
                                psTemp.setString("adj_SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                                psTemp.setDate("adj_V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                                psTemp.setString("adjustment", DmServer.vNull(ledgerMdlValue.getAdjustment()));
                                psTemp.setString("finPayDocId", mrTemp.getSharedKeyFieldCode());
                                psTemp.executeUpdate();
                                psTemp.close();
                            }
                        }
                    }
                }

            } else {

                for (GridDrdAdjust_Mdl gddaj : mrTemp.getLstdrAdjustAmt()) {
                    if (!DmShared.xNull(gddaj.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                        sqlQuery = "INSERT INTO ledgerAdj (vr_DocId, vr_V_SNo, vr_SubCode, vr_V_Date, adj_DocId, adj_V_SNo, adj_SubCode, adj_V_Date, adjustment,finPayDocId)"
                                + " VALUES (:vr_DocId, :vr_V_SNo, :vr_SubCode, :vr_V_Date, :adj_DocId, :adj_V_SNo, :adj_SubCode, :adj_V_Date, :adjustment,:finPayDocId)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("vr_DocId", DmShared.foriegnKey(gddaj.getSharedKeyFieldCode()));
                        psTemp.setString("vr_V_SNo", DmShared.foriegnKey(gddaj.getVr_V_SNo()));
                        psTemp.setString("vr_SubCode", DmShared.foriegnKey(gddaj.getSupplierTag()));
                        psTemp.setDate("vr_V_Date", DmServer.convertDate(gddaj.getVr_v_date()));
                        psTemp.setString("adj_DocId", DmShared.foriegnKey(gddaj.getSharedPreparedBy()));
                        psTemp.setString("adj_V_SNo", DmShared.foriegnKey(gddaj.getAdj_V_SNo()));
                        psTemp.setString("adj_SubCode", DmShared.foriegnKey(gddaj.getSupplierTag()));
                        psTemp.setDate("adj_V_Date", DmServer.convertDate(gddaj.getVr_v_date()));
                        psTemp.setString("adjustment", DmServer.vNull(gddaj.getPendingAdjustment()));
                        psTemp.setString("finPayDocId", mrTemp.getSharedKeyFieldCode());
                        psTemp.executeUpdate();
                        psTemp.close();
                    }
                }
            }
            // Insert into ledger adj 
            if (DmShared.vNull(mrTemp.getOriginalAmt()).doubleValue() > 0) {
                double pendingOrgAmt = DmShared.vNull(mrTemp.getOriginalAmt()).doubleValue();
                for (int i = 0; i < mrTemp.getLstPendingFPayReq().size(); i++) {
                    if (DmShared.vNull(pendingOrgAmt).doubleValue() > 0) {
                        double PendingBillAmtToAdj = DmShared.vNull(mrTemp.getLstPendingFPayReq().get(i).getFinalPayReq()).doubleValue()
                                - DmShared.vNull(mrTemp.getLstPendingFPayReq().get(i).getDrAdjustAmt()).doubleValue();
                        if (PendingBillAmtToAdj > 0) {
                            sqlQuery = "INSERT INTO ledgerAdj (vr_DocId, vr_V_SNo, vr_SubCode, vr_V_Date, adj_DocId, adj_V_SNo, adj_SubCode, adj_V_Date, adjustment,finPayDocId)"
                                    + " VALUES (:vr_DocId, :vr_V_SNo, :vr_SubCode, :vr_V_Date, :adj_DocId, :adj_V_SNo, :adj_SubCode, :adj_V_Date, :adjustment,:finPayDocId)";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("vr_DocId", mrTemp.getSharedKeyFieldCode());
                            psTemp.setString("vr_V_SNo", DmServer.vNull(lgAmountV_Sno, "0"));
                            psTemp.setString("vr_SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                            psTemp.setDate("vr_V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                            psTemp.setString("adj_DocId", DmShared.iNull(mrTemp.getLstPendingFPayReq().get(i).getPbillDocId()).toString());
                            psTemp.setString("adj_V_SNo", DmShared.xNull(mrTemp.getLstPendingFPayReq().get(i).getLgV_SNo()));
                            psTemp.setString("adj_SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                            psTemp.setDate("adj_V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                            psTemp.setString("adjustment", DmServer.vNull(PendingBillAmtToAdj, "0.00"));
                            psTemp.setString("finPayDocId", mrTemp.getSharedKeyFieldCode());
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                        pendingOrgAmt -= PendingBillAmtToAdj;
                    }

                }
            }

            //------------------------------------Post Dr. Adjustment--------------------------//
            if (DmShared.vNull(mrTemp.getDrAdjustAmt()).doubleValue() != 0) {
                for (int i = 1; i <= 2; i++) {
                    int maxV_SNo = DmShared.iNull(DmServer.executeScalar("select MAX(v_sno)+1 from ledger   where docId=" + mrTemp.getSharedKeyFieldCode() + " ", cnTemp)).intValue();
                    sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr, "
                            + "Narration,V_Type,V_Prefix,Site_Code,isSystem_Generated,ContraText,remark,isDrAdjusted) Values "
                            + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr, "
                            + ":Narration,:V_Type,:V_Prefix,:Site_Code,'0',:ContraText,:remark,:isDrAdjusted)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(maxV_SNo++, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getV_Date()));
                    psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                    psTemp.setString("ContraSub", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                    if (i == 1) {
                        psTemp.setString("AmtDr", DmServer.vNull(mrTemp.getDrAdjustAmt(), "0.00"));
                        psTemp.setString("AmtCr", "0.00");
                    } else {
                        psTemp.setString("AmtDr", "0.00");
                        psTemp.setString("AmtCr", DmServer.vNull(mrTemp.getDrAdjustAmt(), "0.00"));
                    }

                    psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                    psTemp.setString("Narration", "");
                    psTemp.setString("V_Type", mrTemp.getVoucherTypeTag());
                    psTemp.setString("V_Prefix", voucherPrefix);
                    psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("isDrAdjusted", "1");
                    psTemp.setString("ContraText", DmServer.prepareContraText(mrTemp.getSupplierText(), DmShared.vNull(mrTemp.getDrAdjustAmt()), "Cr"));
                    psTemp.executeUpdate();
                    psTemp.close();
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
    public DmResult<FinalPaymentEntry_Mdl> getMoveRec(String strKey) {
        DmResult<FinalPaymentEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        FinalPaymentEntry_Mdl mrTemp = new FinalPaymentEntry_Mdl();
        PaymentPopUp_Mdl payment_Mdl = new PaymentPopUp_Mdl();
        GrdOtherAc_Mdl otherAc = new GrdOtherAc_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select fpe.docId, fpe.recId,fpe.recIdPrefix,fpe.v_No,fpe.v_Type,VT.Description,"
                    + " fpe.v_Prefix,fpe.v_Date,"
                    + " fpe.module,fpe.supplierCode,sg.name as supplierName, \n"
                    + " fpe.preparedBy,fpe.amtPaid,fpe.originalAmt,fpe.tdsDeduction,fpe.approvedBy,fpe.u_Name,\n"
                    + " fpe.preparedDate,fpe.site_Code ,sg.tds_Catg AS tdsCode,tc.Name AS tdsName,fpe.remark,sg.displayName , \n"
                    + " fpe.bankLetterDocid,fpe.drAdjustAmt "
                    + " from FinPayEnt fpe \n"
                    + " left join subGroup sg on sg.subCode=fpe.supplierCode\n"
                    + " Left Join Voucher_Type VT On VT.V_Type = fpe.V_Type\n"
                    + " LEFT JOIN tdsCat tc ON sg.tds_Catg = tc.code "
                    + " where fpe.docId= :docId";
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
                mrTemp.setV_Date(DmShared.dNull(rsTemp.getDate("v_date")));
                mrTemp.setSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                mrTemp.setSupplierText(DmShared.xNull(rsTemp.getString("supplierName")));
                mrTemp.setSupplierDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
                mrTemp.setTdsCategoryCode(DmShared.xNull(rsTemp.getString("tdsCode")));
                mrTemp.setTdsCategoryName(DmShared.xNull(rsTemp.getString("tdsName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setModuleTag(DmShared.xNull(rsTemp.getString("module")).trim());
                mrTemp.setModuleText(DmShared.xNull(mrTemp.getModuleTag()).trim().equalsIgnoreCase("PO") ? "Purchase" : "Pharmacy");
                mrTemp.setPaidAmt(DmShared.xNull(rsTemp.getString("amtPaid")));
                mrTemp.setOriginalAmt(DmShared.xNull(rsTemp.getString("originalAmt")));
                mrTemp.setTdsDeduction(DmShared.xNull(rsTemp.getString("tdsDeduction")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setBankLetterDocid(DmShared.xNull(rsTemp.getString("bankLetterDocid")));
                mrTemp.setDrAdjustAmt(DmServer.vNull(rsTemp.getString("drAdjustAmt"), "0.00"));

            }

            psTemp.close();
            rsTemp.close();

            mrTemp.setPartyAcBalance(DmServer.vNull(getPartyLedgerBalance(mrTemp.getSupplierTag(), cnTemp), "0.00"));


            /*====================================< Get FinPayEntDetail Detail >====================================*/
            mrTemp.getLstPendingFPayReq().addAll(getPeningFpReqDetail(DmShared.xNull(strKey), cnTemp));
            List<GrdPendingFPReqDetail_Mdl> lstNode = mrTemp.getLstPendingFPayReq();
            double billAmt = 0;
            for (GrdPendingFPReqDetail_Mdl node : lstNode) {
                billAmt = DmShared.vNull(getTotalBillPaymnet(true, "", DmShared.xNull(node.getPbillDocId()), mrTemp.getSupplierTag(), cnTemp)).doubleValue();
                if (DmShared.vNull(billAmt).doubleValue() != 0) {
                    node.setTotalBillpayment(DmServer.vNull(billAmt, "0.00"));
                    double finDuePaymt = DmShared.vNull(node.getNetBill()).doubleValue() - (DmShared.vNull(node.getTotalAdvNet()).doubleValue() + DmShared.vNull(node.getTotalBillpayment()).doubleValue());
                    node.setFinalDuePaymnet(DmServer.vNull(finDuePaymt, "0.00"));
                }
            }


            /*====================================< Get FinPayEnt_OthDetail Detail >====================================*/
            sqlQuery = "Select  fpod.docid, fpod.v_sno,  fpod.otherAc, sg.name as acName,cast (fpod.amount as numeric) as  amount ,fpod.remark "
                    + "         from FinPayEnt_OthDetail as fpod "
                    + "         left join subGroup sg on sg.subcode = fpod.otherAc "
                    + "         Where fpod.docId = :docId "
                    + "         order by fpod.v_No ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("docid", DmShared.iNull(strKey).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                otherAc = new GrdOtherAc_Mdl();
                otherAc.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("otherAc")));
                otherAc.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("acName")));
                otherAc.setAmount(DmShared.xNull(rsTemp.getString("amount")));
                otherAc.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.getLstOtherAc().add(otherAc);
            }
            psTemp.close();
            rsTemp.close();

            /*==============================< Get Paymnet pop up  Detail >==============================*/
            payment_Mdl = getPaymnetDetail(mrTemp.getSharedKeyFieldCode(), cnTemp);
            mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl().addAll(payment_Mdl.getLstPaymentMdl());
            mrTemp.setPaymentPopUp_Mdl(payment_Mdl);
            double amt = 0d;
            for (GrdPayment_Mdl node : mrTemp.getPaymentPopUp_Mdl().getLstPaymentMdl()) {

                amt += getBankCashLedgerBalance(DmShared.xNull(node.getAcNameCode()), cnTemp);

            }
            mrTemp.setBankCashLedgerBalance(DmServer.vNull(amt));

            mrTemp.setLstdrAdjustAmt(getDrAdjustmentAmount(strKey, cnTemp));

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

    private List<GrdPendingFPReqDetail_Mdl> getPeningFpReqDetail(String docId, Connection cnTemp) {
        List<GrdPendingFPReqDetail_Mdl> lstNode = new ArrayList<>();
        GrdPendingFPReqDetail_Mdl node = new GrdPendingFPReqDetail_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        double fineDuePay = 0.0;
        try {

            sqlQuery = " select max(partyBillNo) partyBillNo, max(pbill.advance)advanceSettled, max(pBill.paidAmt) paidBillAmt ,"
                    + "     fpr.docId as finapPayReqDocId,max(fpr.recId) as finapPayReqRecId,     "
                    + "     max(fpr.v_date) as finapPayReqDate,     max(fpr.pBDocId) as pBDocId,max(pbd.v_SNo)as pbv_sno,    \n"
                    + "     max(pb.recId) as pBillNo,max(pb.v_date) as pBillDt,     max(pb.PaymentDueDate)as billDueDt,     "
                    + "     max(po.oh_Amt_Net) as totalPo,  max(grn.grnAmount) as totalGrn,     "
                    + "     max(pb.oh_Amt_Net) as pBillAmt,                     \n"
                    + "    max(pb.oh_Amt_Net)-max(pB.tdsAmt) as netBill,       "
                    + "     max(fpr.finDuePay) as finPayReq ,                   \n"
                    + "     max(fpr.paymDt) as paymDt,max(fpr.preparedBy) as preparedBy, TDSDeducted, advanceAmt\n"
                    + "     , advancePaid, max(pB.tdsAmt)   as pBillTDS  , max(fped.drAdjustAmt) As drAdjustAmt  "
                    + "     from FinPayReqEnt fpr\n"
                    + "     left join finPayEntDetail fped on fped.payReqDocId = fpr.docid\n"
                    + "     left join pBill pb on pb.docId =fpr.pBDocId\n"
                    + "     left join pBillDetail pbd on pbd.docId =fpr.pBDocId and pbd.isFooter = 0  \n"
                    + "     left join pOrder po on po.docId=pbd.poDocId \n"
                    + "     left join pOrderDetail pod on pod.docId=pbd.poDocId and pod.v_SNo=pbd.v_SNo\n"
                    + "  left join (\n"
                    + "			select pbd.docid,sum(case when pbd.pcDocId is not null then pbd.oh_Amt_Net else 0 end) as grnAmount\n"
                    + "				from pBillDetail pbd\n"
                    + "				group by pbd.docid\n"
                    + "		   ) as grn on pb.docid=grn.docId\n"
                    + "  left join (\n"
                    + "			select sum(fpre.advanceSettled) advance, sum(fped.finReqAmt) paidAmt, pb.docid  \n"
                    + "			from pBill pb \n"
                    + "			left join finPayReqEnt fpre on fpre.pbDocId = pb.docId and fpre.supplierCode = pb.consignee     \n"
                    + "			left join finPayEntDetail fped on fped.payReqDocId = fpre.docId     \n"
                    + "			group by pb.docid) pbill on pbill.docid = pb.docId      \n"
                    + "  LEFT join (	\n"
                    + "					select  sum(finReqAmt) paidAmt, fped.payReqDocId \n"
                    + "					from finPayEntDetail fped \n"
                    + "					where isNull(fped.approvedBy,'') <>''\n"
                    + "					group by fped.payReqDocId)finalPay on finalPay.payReqDocId = fpr.docId\n"
                    + "  LEFT JOIN ( \n"
                    + "			select poDocId, sum(ad.tdsDeduction) As TDSDeducted, sum(ad.OriginalAmt) as advanceAmt\n"
                    + "			, sum(cl.amtCr)as advancePaid                  "
                    + "                 from advanceReqEntry adr  \n"
                    + "			LEFT JOIN advanceEntry ad  ON ad.reqDocId = adr.docId\n"
                    + "                 LEFT JOIN cardLedger cl  On ad.docId = cl.docid                                                      \n"
                    + "			left join ledger lg  On cl.docId = lg.docid And cl.V_SNo = LG.V_SNo                                               \n"
                    + "			Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')=''  \n       "
                    + "			group by  adr.poDocId ) ad on ad.poDocid = po.docId      \n"
                    + "                     where fped.docId =:docId \n"
                    + "			  group by fpr.docid, TDSDeducted, advanceAmt\n"
                    + "     ,advancePaid  \n"
                    + "  order by  fpr.docid  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("docId", DmShared.iNull(docId).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdPendingFPReqDetail_Mdl();

                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("finapPayReqDocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("finapPayReqRecId")));
                node.setDate(DmShared.dNull(rsTemp.getDate("finapPayReqDate")));
                node.setPbillNo(DmShared.xNull(rsTemp.getString("pBillNo")));
                node.setPbillDocId(DmShared.xNull(rsTemp.getString("pBDocId")));
                node.setPbillDate(DmShared.dNull(rsTemp.getDate("pBillDt")));
                node.setTotalPo(DmServer.vNull(rsTemp.getString("totalPo"), "0.00"));
                node.setTotalGrn(DmServer.vNull(rsTemp.getString("totalGrn"), "0.00"));
                node.setNetBill(DmServer.vNull(rsTemp.getString("netBill"), "0.00"));
                node.setTotalBill(DmServer.vNull(rsTemp.getString("pBillAmt"), "0.00"));
                node.setTotalTds(DmServer.vNull(rsTemp.getString("pBillTDS"), "0.00"));
                node.setTotalAdv(DmServer.vNull(rsTemp.getString("advanceAmt"), "0.00"));
                node.setTotalAdvNet(DmServer.vNull(rsTemp.getString("advancePaid"), "0.00"));
                node.setTotalAdvTds(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                node.setFinalPayReq(DmServer.vNull(rsTemp.getString("finPayReq"), "0.00"));
                node.setDrAdjustAmt(DmServer.vNull(rsTemp.getString("drAdjustAmt"), "0.00"));
                node.setAdvanceAdjusted(DmServer.vNull(rsTemp.getString("advanceSettled"), "0.00"));
                node.setTotalBillpayment(DmServer.vNull(rsTemp.getString("paidBillAmt"), "0.00"));
                fineDuePay = DmShared.vNull(rsTemp.getString("netBill")) - (DmShared.vNull(rsTemp.getString("advanceSettled")) + DmShared.vNull(rsTemp.getString("paidBillAmt")));
                node.setFinalDuePaymnet(DmServer.vNull(fineDuePay, "0.00"));
                node.setBillDueDt(DmShared.dNull(rsTemp.getDate("billDueDt")));
                node.setPayDueDt(DmShared.dNull(rsTemp.getDate("paymDt")));
                node.setEnterdBy(DmShared.xNull(rsTemp.getString("preparedBy")).trim());
                node.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setChecked("1");
                String LgV_SNo = DmServer.executeScalar("SELECT MAX(v_sno) FROM ledger WHERE docId ="
                        + "'" + node.getPbillDocId() + "'", cnTemp);
                node.setLgV_SNo(DmShared.xNull(LgV_SNo));

                lstNode.add(node);

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
        return lstNode;
    }

    private PaymentPopUp_Mdl getPaymnetDetail(String docId, Connection cnTemp) {
        List<GrdPayment_Mdl> lstNode = new ArrayList<GrdPayment_Mdl>();
        GrdPayment_Mdl node;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        PaymentPopUp_Mdl mrTemp = new PaymentPopUp_Mdl();
        TdsCategoryGrid_Mdl tdsCat = new TdsCategoryGrid_Mdl();
        try {
            /*====================================< Get Paymnet Detail >====================================*/

            sqlQuery = " Select	adv.tdsDeduction advTdsDeduction, cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType,                                    "
                    + "                         cm.shortName as cardShortName, cl.amtDr, cl.amtCr, LGTDS.AmtDr As TDSDeducted,lg.tdsOnAmt,adv.OriginalAmt,       "
                    + "                       sg.subcode, sg.name as acName,lg.tdsCategory,TC.Name As TDSCName,                                                 "
                    + "                       sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,                                            "
                    + "                       cl.cardHolderName as remark,cl.cardDate as insDate,lg.docid as ledgerDocId ,LG.v_SNo,lg.favourOf                  "
                    + "                       from cardLedger cl "
                    + "			      left join ledger lg  On cl.docId = lg.docid  And cl.V_SNo=LG.V_SNo                                                "
                    + "                       Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')=''    "
                    + "                       Left Join TDSCat TC On TC.Code=LG.TDSCategory                 "
                    + "                       left join cardTypeMast cm On cl.cardTypeCode = cm.code        "
                    + "                       left join subGroup sg On cl.subcode = sg.subCode              "
                    + "                      left join finPayEnt adv on adv.docId = cl.docId           "
                    + "                       where cl.docId = :docId and   lg.isSystem_Generated=0         "
                    + "                       order By cl.V_SNo                                             ";

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

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select fpe.docId "
                    + "                        from FinPayEnt fpe "
                    + "                   	left join voucher_Type as vt on fpe.v_Type = vt.v_Type "
                    + "                         where fpe.site_code=:site_code and vt.category = " + vCat + ""
                    + "                   	Order by  fpe.v_Date,  fpe.v_No, fpe.recId ");
            psTemp.setString("site_Code", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
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

            sqlQuery = " Select fpe.docid ,fpe.recid ,fpe.v_No,fpe.v_date as date ,fpe.module,fpe.supplierCode,sg1.name as supplierName,\n"
                    + "  fpe.remark ,cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, cm.shortName as cardShortName, cl.amtDr, cl.amtCr, \n"
                    + "  sg.subcode, sg.name as acName, fpe.TdsDeduction As TDSDeducted, lg.tdsOnAmt, fpe.OriginalAmt as finalAmt,  \n"
                    + "  sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,cl.cardHolderName as remak,cl.cardDate as insDate,\n"
                    + "  fpe.approvedBy \n"
                    + "  from FinPayEnt fpe  \n"
                    + "    LEFT JOIN voucher_Type AS vt ON fpe.v_Type = vt.v_Type \n"
                    + "    LEFT JOIN cardLedger cl  On fpe.docId = cl.docid"
                    + "    LEFT JOIN ledger lg  On cl.docid = lg.docid  and cl.V_SNo    = LG.V_SNo \n"
                    + "    Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                    + "    left join cardTypeMast cm On cl.cardTypeCode = cm.code \n"
                    + "	  left join subGroup sg On cl.subcode = sg.subCode "
                    + "	  left join subGroup sg1 On fpe.supplierCode = sg1.subCode "
                    + "	 WHERE vt.category in (" + vCat + ") "
                    + "  AND fpe.site_Code = :site_Code "
                    + "  AND fpe.v_prefix = :vPrefix";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setSupplier(DmShared.xNull(rsTemp.getString("supplierName")));
                listNode.setPayDate(DmShared.xNull(rsTemp.getString("date")));
                listNode.setModule(DmShared.xNull(rsTemp.getString("module")).trim().equalsIgnoreCase("PO") ? "Purchase" : "Pharmacy");
                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    listNode.setAmtPaid(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    listNode.setAmtPaid(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                listNode.setOrgAmt(DmServer.vNull(rsTemp.getString("finalAmt"), "0.00"));
                listNode.setTds(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                listNode.setBank(DmShared.xNull(rsTemp.getString("acName")));
                listNode.setPayMode(DmShared.xNull(rsTemp.getString("cardTypeName")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                listNode.setChqNo(DmShared.xNull(rsTemp.getString("insNo")));
                listNode.setChqDate(DmShared.dNull(rsTemp.getDate("insDate")));
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
            sqlQuery = "select approvedBy  FROM FinPayEnt WHERE  docId = :docId ";
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

            /*==============================<< Delete record from ladgerAdj >>===============================*/
            sqlQuery = " Delete From ledgerAdj Where finPayDocId= :docId ";
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

            /*==============================<< Delete record from FinPayEnt_OthDetail >>===============================*/
            sqlQuery = " Delete From FinPayEnt_OthDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();
            /*==============================<< Delete record from FinPayEntDetail >>===============================*/
            sqlQuery = " Delete From FinPayEntDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();
            /*==============================<< Delete record from FinPayEnt >>===============================*/
            sqlQuery = " Delete From FinPayEnt Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

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

            sqlQuery = "Select  cm.code as cardTypeCode, cm.Name as cardTypeName, cm.shortName as cardShortName, cm.currencyCode, cm.isAccountEditable,"
                    + "                           crr.name as currencyName, sg.subcode, sg.name as acName, sg.nature as acNature, cm.cardType, cm.nature as cardTypeNature "
                    + "                           from cardTypeMast cm "
                    + "                           left join subGroup sg on cm.subcode = sg.subCode "
                    + "                           left join currency crr on crr.code=cm.currencyCode "
                    + "                           where cm.isActive = 1 and cm.shortName in ('BK','CS','cheque','NEFT','RTGS','FNDTRF','BankLetter') ";
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

            sqlWithParam.setSQL("select  sg.subCode, sg.name, sg.nature, sg.displayName "
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
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("displayName")));
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
                    + "from FinPayEnt where docId = :docId ";
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

                        sqlQuery = " Update FinPayEnt set "
                                + " u_EntDt=:u_EntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = " Update finPayEntDetail set "
                                + " approvedBy=:approvedBy, "
                                + " approvedDt=:approvedDt  "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(keyCode));
                        psTemp.setString("approvedBy", DmShared.xNull(userName).trim());
                        psTemp.setTimestamp("approvedDt", DmServer.getServerTimestamp());
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

                            sqlQuery = " Update FinPayEnt set "
                                    + " u_EntDt=:u_EntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();

                            sqlQuery = " Update finPayEntDetail set "
                                    + " approvedBy=:approvedBy, "
                                    + " approvedDt=:approvedDt  "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(keyCode));
                            psTemp.setString("approvedBy", null);
                            psTemp.setTimestamp("approvedDt", null);
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

    @Override
    public DmResult<List<GrdPendingFPReqDetail_Mdl>> getPendingFinalPaymnetReqDetail(String supplierCode, String moduleType) {
        DmResult<List<GrdPendingFPReqDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GrdPendingFPReqDetail_Mdl> lstNode = new ArrayList<>();
        String sqlQuery = "", voucherType = "";
        dmrTemp.setData(lstNode);
        double fineDuePay = 0.0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(moduleType).equalsIgnoreCase(pvc.PURCHASE_ORDER)) {
                voucherType = " AND vt.category IN ('" + pvc.PURCHASE_BILL + "', '" + pvc.PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PURCHASE_BILL_DIRECT + "', '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', '" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "')";

            } else {
                voucherType = " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "', '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')";

            }
            /*====================================< Get Grid Detail  >====================================*/
            sqlQuery = " select max(pb.partyBillNo)partyBillNo, max(pbill.advance)advanceSettled, max(pBill.paidAmt) paidBillAmt ,"
                    + "     fpr.docId as finapPayReqDocId,max(fpr.recId) as finapPayReqRecId,"
                    + "     max(fpr.v_date) as finapPayReqDate,"
                    + "     max(fpr.pBDocId) as pBDocId,    \n"
                    + "     max(pb.recId) as pBillNo,max(pb.v_date) as pBillDt,"
                    + "     max(pb.PaymentDueDate)as billDueDt,"
                    + "     max(po.oh_Amt_Net) as totalPo,  max(grn.grnAmount) as totalGrn,"
                    + "     max(pb.oh_Amt_Net) as pBillAmt,\n"
                    + "       max(pb.oh_Amt_Net)-max(pB.tdsAmt) as netBill      "
                    + "     ,max(fpr.finDuePay) as finPayReq ,\n"
                    + "     max(fpr.paymDt) as paymDt,max(fpr.preparedBy) as preparedBy,"
                    //                    + " max(LGTDS.AmtDr) As TDSDeducted,"
                    //                    + " max(LG.OrignalAmt) as advanceAmt,"
                    //                    + " max(cl.amtCr)as advancePaid\n"
                    + " TDSDeducted, advanceAmt\n"
                    + ", advancePaid, max(pB.tdsAmt)   as pBillTDS  ,MAX(lg.v_SNo) as lgV_SNo    "
                    + " from FinPayReqEnt fpr   \n "
                    + " left join finPayEntDetail fped on fped.payReqDocId = fpr.docid  "
                    + " left join pBill pb on pb.docId =fpr.pBDocId\n"
                    + " LEFT join ledger lg on lg.subcode = pb.subCode and lg.docId = pb.docId and ISNULL(lg.amtDr,0) =0 and ISNULL(pb.oh_Amt_Net,0) = ISNULL(lg.amtCr,0) \n"
                    + " left join pBillDetail pbd on pbd.docId =fpr.pBDocId and pbd.isFooter=0 \n"
                    + " left join pOrder po on po.docId=pbd.poDocId \n"
                    + " left join pOrderDetail pod on pod.docId=pbd.poDocId and pod.v_SNo=pbd.v_SNo\n"
                    + "  left join (\n"
                    + "			select pbd.docid,sum(case when pbd.pcDocId is not null then pbd.oh_Amt_Net else 0 end) as grnAmount\n"
                    + "				from pBillDetail pbd\n"
                    + "				group by pbd.docid\n"
                    + "		   ) as grn on pb.docid=grn.docId\n"
                    + "  left join (\n"
                    + "			select sum(fpre.advanceSettled) advance, sum(fped.finReqAmt) paidAmt, pb.docid  \n"
                    + "			from pBill pb \n"
                    + "			left join finPayReqEnt fpre on fpre.pbDocId = pb.docId and fpre.supplierCode = pb.consignee     \n"
                    + "			left join finPayEntDetail fped on fped.payReqDocId = fpre.docId     \n"
                    + "			group by pb.docid) pbill on pbill.docid = pb.docId      \n"
                    + " LEFT join (	\n"
                    + "			select  sum(finReqAmt) paidAmt, fped.payReqDocId    \n"
                    + "			from finPayEntDetail fped    \n"
                    + "			where   isNull(fped.approvedBy,'') <>''      \n"
                    + "			group by fped.payReqDocId)finalPay on finalPay.payReqDocId = fpr.docId "
                    + " LEFT JOIN ( \n"
                    + "			select poDocId, sum(ad.tdsDeduction) As TDSDeducted, sum(ad.OriginalAmt) as advanceAmt       \n"
                    + "			, sum(cl.amtCr)as advancePaid "
                    + "                 from advanceReqEntry adr  \n"
                    + "			LEFT JOIN advanceEntry ad  ON ad.reqDocId = adr.docId                           \n"
                    + "			left join cardLedger cl  On ad.docId = cl.docid   \n"
                    + "			left join ledger lg  On ad.docId = lg.docid And cl.V_SNo=LG.V_SNo  \n"
                    + "			Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                    + "			group by  adr.poDocId ) ad on ad.poDocid = po.docId  "
                    + "                 left join voucher_Type as vt on vt.v_Type = pb.V_type   "
                    + "                 where fpr.supplierCode=:supplierCode and fpr.site_code =:site_code "
                    + "                 and (isNull(fped.approvedBy,'')='' and isNull(fped.payReqDocId,'')='') " + voucherType
                    + "                 group by fpr.docid, TDSDeducted, advanceAmt, advancePaid    \n"
                    + "                 order by fpr.docid, TDSDeducted, advanceAmt, advancePaid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("supplierCode", DmShared.iNull(supplierCode).longValue());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdPendingFPReqDetail_Mdl node = new GrdPendingFPReqDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("finapPayReqDocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("finapPayReqRecId")));
                node.setDate(DmShared.dNull(rsTemp.getDate("finapPayReqDate")));
                node.setPbillNo(DmShared.xNull(rsTemp.getString("pBillNo")));
                node.setPbillDocId(DmShared.xNull(rsTemp.getString("pBDocId")));
                node.setLgV_SNo(DmShared.xNull(rsTemp.getString("lgV_SNo")));
                node.setPbillDate(DmShared.dNull(rsTemp.getDate("pBillDt")));
                node.setTotalPo(DmServer.vNull(rsTemp.getString("totalPo"), "0.00"));
                node.setTotalGrn(DmServer.vNull(rsTemp.getString("totalGrn"), "0.00"));
                node.setNetBill(DmServer.vNull(rsTemp.getString("netBill"), "0.00"));
                node.setTotalBill(DmServer.vNull(rsTemp.getString("pBillAmt"), "0.00"));
                node.setTotalTds(DmServer.vNull(rsTemp.getString("pBillTDS"), "0.00"));
                node.setTotalAdv(DmServer.vNull(rsTemp.getString("advanceAmt"), "0.00"));
                node.setTotalAdvNet(DmServer.vNull(rsTemp.getString("advancePaid"), "0.00"));
                node.setTotalAdvTds(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                node.setFinalPayReq(DmServer.vNull(rsTemp.getString("finPayReq"), "0.00"));
                node.setPendingToAdjustAmt(DmServer.vNull(rsTemp.getString("finPayReq"), "0.00"));
                node.setAdvanceAdjusted(DmServer.vNull(rsTemp.getString("advanceSettled"), "0.00"));
                node.setTotalBillpayment(DmServer.vNull(rsTemp.getString("paidBillAmt"), "0.00"));
                fineDuePay = DmShared.vNull(rsTemp.getString("netBill")) - (DmShared.vNull(rsTemp.getString("advanceSettled")) + DmShared.vNull(rsTemp.getString("paidBillAmt")));
                node.setFinalDuePaymnet(DmServer.vNull(fineDuePay, "0.00"));
                node.setBillDueDt(DmShared.dNull(rsTemp.getDate("billDueDt")));
                node.setPayDueDt(DmShared.dNull(rsTemp.getDate("paymDt")));
                node.setEnterdBy(DmShared.xNull(rsTemp.getString("preparedBy")).trim());
                node.setPartyBillNo(DmShared.xNull(rsTemp.getString("partyBillNo")));
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            double amt = -1;
            for (GrdPendingFPReqDetail_Mdl node : lstNode) {
                amt = DmShared.vNull(getTotalBillPaymnet(false, DmShared.xNull(node.getSharedKeyFieldCode()), DmShared.xNull(node.getPbillDocId()), supplierCode, cnTemp)).doubleValue();
                if (DmShared.vNull(amt).doubleValue() == 0) {
                    node.setIsRemove(true);
                }
            }

            Iterator<GrdPendingFPReqDetail_Mdl> i = lstNode.iterator();

            while (i.hasNext()) {
                GrdPendingFPReqDetail_Mdl e = i.next();
                if (e.isIsRemove()) {
                    i.remove();
                }
            }

            //=======================================================
            dmrTemp.setValue1(DmServer.vNull(getPartyLedgerBalance(supplierCode, cnTemp)));

            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
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

    private double getTotalBillPaymnet(boolean isMoveRec, String reqDocId, String pbDocId, String supplierCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", cond = "";
        double reqAmt = 0d, billAmt = 0d, finAmt = 0d, diff = -1;

        try {
            if (DmShared.iNull(supplierCode).longValue() != 0 && DmShared.iNull(pbDocId).longValue() != 0) {
                cond = " and fpe.supplierCode=:supplierCode and fpre.pbDocid=:pbDocid";
            }
            if (DmShared.iNull(reqDocId).longValue() != 0) {
                cond += " and fpre.docid=:docId ";
            } else {
                cond += " and 1!=1";
            }
            sqlQuery = " select sum(pb.oh_Amt_Net)-sum(pb.tdsAmt)  as billAmt ,sum(fpe.originalAmt)as finAmt,sum(fpre.finDuePay) as reqAmt\n"
                    + "		 from finPayEnt fpe\n"
                    + "		 left join finPayEntDetail fped on fped.docid=fpe.docid\n"
                    + "		 left join finPayReqEnt fpre on fpre.docId=fped.payReqDocId\n"
                    + "		 left join pBill  pb on pb.docId=fpre.pbDocId"
                    + "		 where  isNull(fped.approvedby,'')<>''  and fpe.docid is not null" + cond
                    + "		 group by fpe.supplierCode,fpre.pbDocid ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            if (DmShared.iNull(supplierCode).longValue() != 0 && DmShared.iNull(pbDocId).longValue() != 0) {
                psTemp.setLong("supplierCode", DmShared.iNull(supplierCode).longValue());
                psTemp.setLong("pbDocid", DmShared.iNull(pbDocId).longValue());

            }
            if (DmShared.iNull(reqDocId).longValue() != 0) {
                psTemp.setLong("docId", DmShared.iNull(reqDocId).longValue());
            }
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                reqAmt = DmShared.vNull(rsTemp.getString("reqAmt")).doubleValue();
                billAmt = DmShared.vNull(rsTemp.getString("billAmt")).doubleValue();
                finAmt = DmShared.vNull(rsTemp.getString("finAmt")).doubleValue();
                if (reqAmt == finAmt) {
                    diff = 0;
                }

            }
            if (isMoveRec) {
                diff = finAmt;
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
        return diff;
    }

    private double getPartyLedgerBalance(String supplierCode, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", cond = "";
        double balance = 0d;

        try {
            if (DmShared.iNull(supplierCode).longValue() != 0) {
                cond = " and lg.subcode=:subcode";
            } else {
                cond = "and 1!=1";
            }
            sqlQuery = "Select  sum(lg.amtDr) as amtDr, sum(lg.amtCr) as amtCr "
                    + "	from ledger lg where "
                    + "	 lg.site_Code=:site_Code and lg.v_Date<=:toDate " + cond
                    + " group by lg.subcode";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(supplierCode).longValue() != 0) {
                psTemp.setLong("subcode", DmShared.iNull(supplierCode).longValue());
            }
            psTemp.setDate("toDate", DmServer.convertDate(DmServer.getServerDateTime()[0]));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                double amt = DmShared.vNull(rsTemp.getString("amtDr")).doubleValue() - DmShared.vNull(rsTemp.getString("amtCr")).doubleValue();
                balance = DmShared.vNull(amt).doubleValue();

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
        return balance;
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
                    + "	 lg.site_Code=:site_Code " + cond
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

    @Override
    public DmResultPageing<List<TxtHOtherAC_Mdl>> getTxtHOtherAC(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHOtherAC_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHOtherAC_Mdl> lstTemp = new ArrayList<>();
        TxtHOtherAC_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  sg.subCode, sg.name, sg.nature,sgn.manualCode  "
                    + "		from subGroup sg "
                    + "		left join subGroupSiteList sgsl on sgsl.subCode = sg.subCode "
                    + "		left join subGroupNature sgn on sgn.code = sg.nature "
                    + "		where sg.isActive = 1 and sgsl.site_Code = :siteCode "
                    + "		and sgn.manualCode not in ('BK','CS')");
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHOtherAC_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResult<List<GridDrdAdjust_Mdl>> getPendingFinalDebitAdjmentRec(String supplierCode) {
        DmResult<List<GridDrdAdjust_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<GridDrdAdjust_Mdl> lstNode = new ArrayList<>();
        String sqlQuery = "", voucherType = "";
        dmrTemp.setData(lstNode);
        double fineDuePay = 0.0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  lg.docId,lg.recId AS voucherNo ,lg.v_SNo,sg.name supplier,sg.subcode as supplierCode,sgc.name as contraAc,vt.description as voucherType ,\n"
                    + "lg.amtDr,isnull(adj.adjustment,0) as adjAmt,lg.amtDr-isnull(adj.adjustment,0) pendingAdjustment\n"
                    + "\n"
                    + "FROM LEDGER LG\n"
                    + "left join subgroup sg on sg.subcode=lg.subcode\n"
                    + "left join subGroupNature sgn on sgn.code=sg.nature\n"
                    + "left join subgroup sgc on sgc.subcode=lg.contraSub\n"
                    + "left join voucher_Type vt on vt.v_Type=lg.v_Type\n"
                    + " left join customEnviro ce on ce.site_Code=lg.site_Code \n "
                    + "left join (select adj.vr_DocId as docid,sum(adj.adjustment) as adjustment\n"
                    + "\n"
                    + "from ledgerAdj as adj\n"
                    + "group by adj.vr_DocId\n"
                    + ") as adj on adj.docid=lg.docid\n"
                    + "\n"
                    + "WHERE"
                    //                    + " sgn.manualCode='PS' and"
                    + " isnull(lg.amtDr,0)<>0 and lg.site_Code=" + DmServer.getSsnSiteCode(this) + "\n"
                    + "and lg.isPosted=1\n"
//                    + "and lg.v_Prefix=" + DmServer.getSsnVoucherPrefix(this) + " \n"
                    + "and vt.description in ('Debit Note','Pharmacy Purchase Return','Pharmacy Purchase Return (U)','Purchase Return')  \n"
                    + "and lg.amtdr-isnull(adj.adjustment,0)<>0\n"
                    + "and lg.subcode= :supplierCode\n"
                    + "and lg.v_Date>= isnull(ce.drAdjCutOffDate,'') \n"
                    + "order by lg.docId,vt.description ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("supplierCode", DmShared.iNull(supplierCode).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GridDrdAdjust_Mdl node = new GridDrdAdjust_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setVr_V_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setVr_V_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setVoucherNo(DmShared.xNull(rsTemp.getString("voucherNo")));
                node.setSupplierTag(DmShared.xNull(rsTemp.getString("supplierCode")));
                node.setSupplierText(DmShared.xNull(rsTemp.getString("supplier")));
                node.setContraAC(DmShared.xNull(rsTemp.getString("contraAc")));
                node.setVoucherType(DmShared.xNull(rsTemp.getString("voucherType")));
                node.setAmtDr(DmShared.xNull(rsTemp.getString("amtDr")));
                node.setAdjAmt(DmShared.xNull(rsTemp.getString("adjAmt")));
                node.setPendingAdjustment(DmShared.xNull(rsTemp.getString("pendingAdjustment")));

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            e.printStackTrace();
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

    private List<GridDrdAdjust_Mdl> getDrAdjustmentAmount(String docId, Connection cnTemp) {
        List<GridDrdAdjust_Mdl> lstNode = new ArrayList<>();
        GridDrdAdjust_Mdl node = new GridDrdAdjust_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        try {

            sqlQuery = " select la.vr_DocId,la.vr_V_SNo,la.vr_SubCode,la.vr_V_Date,la.adj_DocId\n "
                    + "  ,la.adj_SubCode,la.adj_V_Date,la.adj_V_SNo\n"
                    + "  ,la.adjustment AS adjustmentAmount\n"
                    + " ,lg.recId As voucherNo\n"
                    + " ,vt.description AS voucherType\n"
                    + " from ledgeradj la\n"
                    + "left join ledger lg on lg.docId  = la.vr_DocId and lg.v_SNo = la.vr_V_SNo \n"
                    + "left join voucher_Type vt on vt.v_type = lg.v_Type\n"
                    + "where ISNULL(vt.category, '') <> " + vCat + " and la.finPayDocId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setLong("docId", DmShared.iNull(docId).longValue());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GridDrdAdjust_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("vr_DocId")));
                node.setVr_V_SNo(DmShared.xNull(rsTemp.getString("vr_V_SNo")));
                node.setAdj_V_SNo(DmShared.xNull(rsTemp.getString("adj_V_SNo")));
                node.setSupplierTag(DmShared.xNull(rsTemp.getString("vr_SubCode")));
                node.setVr_v_date(DmShared.dNull(rsTemp.getDate("vr_V_Date")));
                node.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("adj_DocId")));
                node.setVoucherNo(DmShared.xNull(rsTemp.getString("voucherNo")));
                node.setVoucherType(DmShared.xNull(rsTemp.getString("voucherType")));
                node.setPendingAdjustment(DmServer.vNull(rsTemp.getString("adjustmentAmount"), "0.00"));
                node.setChecked("1");
                lstNode.add(node);

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
        return lstNode;
    }
}
