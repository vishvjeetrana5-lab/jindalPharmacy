package dataman.accounts.bankLetterCreation.server;

import dataman.accounts.bankLetterCreation.shared.*;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.modifyReason.server.ModifyReason_Server;
import dataman.dmToolkit.modifyReason.shared.ModifyData_Mdl;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.google.gson.reflect.TypeToken;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Mdl;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.self.server.DmJasperCompiler;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
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

public class BankLetterCreation_SrvImpl extends DmRSSPrivate implements BankLetterCreation_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private String v_Category = pvc.BANL_LETTER_CREATION;
    private final String vCatFinalPayment = "'" + pvc.FINAL_PAYMENT_ENTRY + "'";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        List<KeyFieldExtended> lstKeyField = new ArrayList<>();
        KeyFieldExtended lstKeyFieldTmp;
        String sqlQuery = "", sqlCondition = "";
        dmrTemp.setData(lstKeyField);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT bnk.docid as code "
                    + "   FROM bankLetter bnk "
                    + "   LEFT JOIN voucher_Type vt ON vt.v_Type=bnk.v_Type"
                    + "   where vt.category='" + v_Category + "' and bnk.site_Code = :site_Code "
                    + "   and bnk.v_prefix = :vPrefix "
                    + " Order By bnk.v_Date desc , vt.short_Name, bnk.v_No desc, bnk.recId";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstKeyFieldTmp = new KeyFieldExtended();
                lstKeyFieldTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                lstKeyField.add(lstKeyFieldTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<BankLetterCreation_Mdl> getMoveRec(String strKey) {
        DmResult<BankLetterCreation_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        BankLetterCreation_Mdl mrTemp = new BankLetterCreation_Mdl();
        List<GrdBankLetterCreation_Mdl> lstTemp = new ArrayList<GrdBankLetterCreation_Mdl>();
        dmrTemp.setData(mrTemp);
        GrdBankLetterCreation_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select bnk.docid,bnk.recid,bnk.v_Date,bnk.fromFinalPaymentDate,bnk.toFinalPaymentDate ,\n"
                    + "       bnk.approvedBy,bnk.preparedby,bnk.u_Name as modifiedBy ,bnk.wtaCode ,bnk.isDiscarded \n"
                    + "from bankLetter as bnk\n"
                    + "\n"
                    + "where bnk.docid= :docid  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));

                mrTemp.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));

                mrTemp.setFromFinalPaymentDate(DmShared.dNull(rsTemp.getDate("fromFinalPaymentDate")));
                mrTemp.setToFinalPaymentDate(DmShared.dNull(rsTemp.getDate("toFinalPaymentDate")));

                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedby")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("modifiedBy")));
                mrTemp.setIsDiscarded(DmShared.iNull(rsTemp.getString("isDiscarded")).toString());
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = " select bnkd.finPayDocid , bnkd.chqNo,bnkd.finPayRecid as payNo,\n"
                    + "bnkd.payDate,bnkd.module,sg.subcode as supplierCode,sg.name as supplierName,bnkd.orgAmt as finalAmount,bnkd.tds,bnkd.amtPaid\n"
                    + ",cm.code as payModeCode,cm.name as payModeName,ac.subCode as acCode,ac.name as acName,bnkd.remark , bnkd.chqDate ,"
                    + " bnkd.supplieraccountNo, bnkd.supplierbankName, bnkd.supplierbranch, bnkd.supplierifscCode \n"
                    + "from bankLetterDetail  bnkd\n"
                    + "left join subGroup sg on sg.subCode=bnkd.supplier\n"
                    + "left join cardTypeMast  cm on cm.code=bnkd.payMode\n"
                    + "left join subgroup ac on ac.subcode=bnkd.bank\n"
                    + "\n"
                    + "where bnkd.docid= :docid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                node = new GrdBankLetterCreation_Mdl();

                node.setCheck(true);

                node.setChqNo(DmShared.xNull(rsTemp.getString("chqNo")));

                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("payNo")));

                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("finPayDocid")).toString());

                node.setPayDate(DmShared.dNull(rsTemp.getDate("payDate")));

                node.setModule(DmShared.xNull(rsTemp.getString("module")));

                node.setSupplier(DmShared.xNull(rsTemp.getString("supplierName")));
                node.setSupplierCode(DmShared.iNull(rsTemp.getString("supplierCode")).toString());

                node.setOrgAmt(DmServer.vNull(rsTemp.getString("finalAmount"), "0.00"));
                node.setTds(DmServer.vNull(rsTemp.getString("tds"), "0.00"));
                node.setAmtPaid(DmServer.vNull(rsTemp.getString("amtPaid"), "0.00"));

                node.setPayMode(DmShared.xNull(rsTemp.getString("payModeName")));
                node.setCardTypeCode(DmShared.iNull(rsTemp.getString("payModeCode")).toString());

                node.setBank(DmShared.xNull(rsTemp.getString("acName")));
                node.setBankCode(DmShared.iNull(rsTemp.getString("acCode")).toString());

                node.setChqDate(DmShared.dNull(rsTemp.getDate("chqDate")));

                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                node.setSupplieraccountNo(DmShared.xNull(rsTemp.getString("supplieraccountNo")));
                node.setSupplierbankName(DmShared.xNull(rsTemp.getString("supplierbankName")));
                node.setSupplierbranch(DmShared.xNull(rsTemp.getString("supplierbranch")));
                node.setSupplierifscCode(DmShared.xNull(rsTemp.getString("supplierifscCode")));

                lstTemp.add(node);

            }

            mrTemp.getGrdData().addAll(lstTemp);
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            dmrTemp.setStatus(true);

            DmServer.setSsnOldData(getClass().getName(), mrTemp, this);

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
    public DmResult<String> saveRecord(BankLetterCreation_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, voucherType = "", vNo = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            BankLetterCreation_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            voucherType = DmShared.xNull(mrTemp.getvType());

            spBegin = new DmSavePoint(cnTemp);

            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {

                sqlQuery = "select count(*) as cnt from  bankLetter where docid = :docId and ISNULL(approvedBy,'') <> '' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                        throw new DmException(0, "Record is approved. You can not save this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

            }

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(voucherType), voucherPrefix, cnTemp, this));
                mrTemp.setRecid(DmServer.getRecId("bankLetter", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDate(), DmShared.xNull(voucherType), voucherPrefix, cnTemp, this, true));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), mrTemp.getvType(), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("bankLetter", "docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecid(), mrTemp.getDate(), DmShared.xNull(voucherType), voucherPrefix, cnTemp, this, true);
            }

            vNo = DmShared.iNull(mrTemp.getRecid().getCounter()).toString();

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {

                sqlQuery = "Select isnull(isDiscarded,0) as isDiscarded  From bankLetter Where DocId=:DocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();

                if (rsTemp.next()) {

                    if (DmShared.iNull(rsTemp.getString("isDiscarded")).intValue() == 1) {
                        throw new UnsupportedOperationException("Record has been Discarded. You Can not modify This Record.");
                    }

                }
                rsTemp.close();
                psTemp.close();

            }

            if (blnAdd) {

                sqlQuery = "INSERT INTO  bankLetter (docId,v_Type,v_No,recIdPrefix,recId,v_Date,v_Prefix,site_Code,\n"
                        + "                         preparedBy,u_EntDt,fromFinalPaymentDate,toFinalPaymentDate,wtaCode )\n"
                        + "     VALUES (:docId ,:v_Type ,:v_No ,:recIdPrefix ,:recId ,:v_Date ,:v_Prefix ,:site_Code ,:preparedBy ,\n"
                        + "	        :uEntDt ,:fromFinalPaymentDate ,:toFinalPaymentDate ,:wtaCode   ) ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                psTemp.setString("docid", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("v_type", DmShared.xNull(voucherType));
                psTemp.setString("v_No", DmShared.iNull(vNo).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecid().getPrefix()));
                psTemp.setString("recid", DmShared.xNull(mrTemp.getRecid().getRecIdValue()));
                psTemp.setDate("v_date", DmServer.convertDate(mrTemp.getDate()));
                psTemp.setString("v_prefix", DmShared.xNull(voucherPrefix));
                psTemp.setString("site_code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
                psTemp.setString("preparedby", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setDate("fromFinalPaymentDate", DmServer.convertDate(mrTemp.getFromFinalPaymentDate()));
                psTemp.setDate("toFinalPaymentDate", DmServer.convertDate(mrTemp.getToFinalPaymentDate()));
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));

                psTemp.executeUpdate();
                psTemp.close();

            } else {

                sqlQuery = "UPDATE bankLetter set "
                        + " u_Name= :uName ,"
                        + " u_EntDt= :uEntDt ,"
                        + " fromFinalPaymentDate= :fromFinalPaymentDate ,"
                        + " toFinalPaymentDate= :toFinalPaymentDate "
                        + " where docId=:docId   ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

                psTemp.setString("uName", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setDate("fromFinalPaymentDate", DmServer.convertDate(mrTemp.getFromFinalPaymentDate()));
                psTemp.setDate("toFinalPaymentDate", DmServer.convertDate(mrTemp.getToFinalPaymentDate()));

                psTemp.executeUpdate();
                psTemp.close();
            }

            //==================================================================
            sqlQuery = "Delete From bankLetterDetail Where DocId = :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            int vSNo = 0;

            for (GrdBankLetterCreation_Mdl row : mrTemp.getGrdData()) {

                if (!DmShared.xNull(row.getSharedKeyFieldName()).trim().equals("")) {

                    if (row.isCheck()) {

                        //==================================================Check for already generated===========
                        sqlQuery = "select bnkd.docid  from  bankLetterDetail bnkd "
                                + " left join bankLetter bnk on bnk.docid=bnkd.docid "
                                + " where  bnkd.finPayDocid  = :finPayDocid and ISNULL(bnk.isDiscarded,0) = 0 ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("finPayDocid", DmShared.iNull(row.getSharedKeyFieldCode()).toString());
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            if (DmShared.iNull(rsTemp.getString("docid")).longValue() > 0) {
                                throw new DmException(0, "Bank Letter already created for payment No." + DmShared.xNull(row.getSharedKeyFieldName()));
                            }
                        }
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = "select bnkd.docid , bnkd.chqNo , bnkd.bank , bnk.recid from  bankLetterDetail bnkd "
                                + " left join bankLetter bnk on bnk.docid=bnkd.docid "
                                + " where  bnkd.chqNo  = :chqNo and ISNULL(bnk.isDiscarded,0) = 0 and bnk.docId<>:docId  and bnkd.bank=:bank ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("chqNo", DmShared.xNull(row.getChqNo()));
                        psTemp.setString("bank", DmShared.xNull(row.getBankCode()));
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        rsTemp = psTemp.executeQuery();
                        if (rsTemp.next()) {
                            if (DmShared.iNull(rsTemp.getString("docid")).longValue() > 0) {
                                throw new DmException(0, "Bank Letter already created for Chque No." + DmShared.xNull(row.getChqNo()) + " in Bank Letter No. " + DmShared.xNull(rsTemp.getString("recid")));
                            }
                        }
                        rsTemp.close();
                        psTemp.close();

                        //==================================================End================================
                        sqlQuery = "  INSERT INTO bankLetterDetail (docid,v_SNo,payDate,module,supplier,amtPaid,orgAmt,tds,payMode\n"
                                + "                              ,bank,chqDate,chqNo,remark,finalPayApprovedBy,finPayDocid,finPayRecid , supplieraccountNo, supplierbankName, supplierbranch, supplierifscCode   )\n"
                                + " VALUES (:docid ,:v_sno,:payDate ,:module ,:supplier ,:amtPaid ,:orgAmt ,:tds ,:payMode ,:bank \n"
                                + "        ,:chqDate ,:chqNo ,:remark ,:finalPayApprovedBy ,:finPayDocid,:finPayRecid  , :supplieraccountNo, :supplierbankName, :supplierbranch, :supplierifscCode) ";

                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("v_SNo", DmShared.iNull(++vSNo).toString());
                        psTemp.setDate("payDate", DmServer.convertDate(row.getPayDate()));
                        psTemp.setString("module", row.getModule());
                        psTemp.setString("supplier", DmShared.iNull(row.getSupplierCode()).toString());

                        psTemp.setString("amtPaid", DmShared.vNull(row.getAmtPaid()).toString());
                        psTemp.setString("orgAmt", DmShared.vNull(row.getOrgAmt()).toString());
                        psTemp.setString("tds", DmShared.vNull(row.getTds()).toString());

                        psTemp.setString("payMode", DmShared.iNull(row.getCardTypeCode()).toString());
                        psTemp.setString("bank", DmShared.iNull(row.getBankCode()).toString());
                        psTemp.setDate("chqDate", DmServer.convertDate(row.getChqDate()));

                        psTemp.setString("chqNo", DmShared.xNull(row.getChqNo()));
                        psTemp.setString("remark", DmShared.xNull(row.getRemark()));
                        psTemp.setString("finalPayApprovedBy", DmShared.xNull(row.getApprovedBy()));

                        psTemp.setString("finPayDocid", DmShared.iNull(row.getSharedKeyFieldCode()).toString());
                        psTemp.setString("finPayRecid", DmShared.xNull(row.getSharedKeyFieldName()));

                        psTemp.setString("supplieraccountNo", DmShared.xNull(row.getSupplieraccountNo()));
                        psTemp.setString("supplierbankName", DmShared.xNull(row.getSupplierbankName()));
                        psTemp.setString("supplierbranch", DmShared.xNull(row.getSupplierbankName()));
                        psTemp.setString("supplierifscCode", DmShared.xNull(row.getSupplierifscCode()));

                        psTemp.executeUpdate();
                        psTemp.close();

                        //===========================Payment Ref. No. Update/Lock==================
                        sqlQuery = "UPDATE FinPayEnt set  bankLetterDocid= :bankLetterDocid  where docId=:finPayDocid   ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("bankLetterDocid", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("finPayDocid", DmShared.iNull(row.getSharedKeyFieldCode()).toString());
                        psTemp.executeUpdate();
                        psTemp.close();
                        //========================================================================
                    }

                }
            }

            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }

            mrTemp.setSharedModifyDate(DmServer.formatTimestamp(DmServer.getServerTimestamp()));
            if (!blnAdd && mrTemp.getModifyReason() != null) {
                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                modifyData_Mdl.setKeyId_1(mrTemp.getSharedKeyFieldCode());
                modifyData_Mdl.setKeyName_1(mrTemp.getRecid().getRecIdValue());
                modifyData_Mdl.setKeyDescription_1(mrTemp.getRecid().getRecIdValue());
                modifyData_Mdl.setModuleName(new lmAccounts().moduleName);
                modifyData_Mdl.setMenuName(new lmAccounts().BANK_LETTER_CREATION.caption);
                modifyData_Mdl.setModelClassName(BankLetterCreation_Mdl.class.getName());
                modifyData_Mdl.setTableName("bankLetter");

                modifyData_Mdl.setOldData(DmServer.convertToJson((BankLetterCreation_Mdl) DmServer.getSsnOldData(getClass().getName(), this), new TypeToken<BankLetterCreation_Mdl>() {
                }));
                modifyData_Mdl.setNewData(DmServer.convertToJson(mrTemp, new TypeToken<BankLetterCreation_Mdl>() {
                }));
                ModifyReason_Server.saveNew(modifyData_Mdl, mrTemp.getModifyReason(), this);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
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
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select  bnk.isDiscarded ,  bnk.v_No, bnk.docid,bnk.recid,bnk.v_Date,bnk.fromFinalPaymentDate,bnk.toFinalPaymentDate ,\n"
                    + "       bnk.approvedBy,bnk.preparedby,bnk.u_Name as modifiedBy ,bnk.wtaCode \n"
                    + "from bankLetter as bnk "
                    + " where bnk.site_Code = :site_Code and bnk.v_prefix = :vPrefix ";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setFromFinalPaymentDate(DmShared.dNull(rsTemp.getDate("fromFinalPaymentDate")));
                listNode.setToFinalPaymentDate(DmShared.dNull(rsTemp.getDate("toFinalPaymentDate")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                listNode.setIsDiscarded(DmShared.iNull(rsTemp.getString("isDiscarded")).toString());
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT V_type as code, Description as name FROM Voucher_Type VT WHERE VT.IsActive=1 And Category = '" + v_Category + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setBankLetterCreationVTypeCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setBankLetterCreationVTypeName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();
            mrTemp.setCurrentDate(DmServer.getServerDateTime()[0]);
            mrTemp.setCurrentTime(DmServer.getServerDateTime()[1]);

            dmrTemp.setStatus(true);
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

    private String calendar(int value, String date) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar cal = Calendar.getInstance();
        Date dtSchedule = sdf.parse(date);

        cal.setTime(dtSchedule);
        cal.add(Calendar.DATE, value);

        String newDate = sdf.format(cal.getTime());

        return newDate;

    }

    @Override
    public DmResult<List<GrdBankLetterCreation_Mdl>> getFinalPaymentDetail(String fromDate, String toDate, String mode, String chqNo, String bankCode) {
        DmResult<List<GrdBankLetterCreation_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        GrdBankLetterCreation_Mdl node;
        List<GrdBankLetterCreation_Mdl> lstNode = new ArrayList<>();
        dmrTemp.setData(lstNode);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(mode).equalsIgnoreCase("Edit")) {
                condition += "  and cl.cardNo= '" + chqNo + "' and sg.subCode= " + bankCode + " ";
            } else {
                condition = "  and fpe.bankLetterDocid is null  ";
            }

            sqlQuery = "  Select fpe.docid ,fpe.recid ,fpe.v_No,fpe.v_date as date ,fpe.module,fpe.supplierCode,sg1.name as supplierName,\n"
                    + "  fpe.remark ,cm.code as cardTypeCode, cm.name As cardTypeName, cm.cardType, cm.shortName as cardShortName, cl.amtDr, cl.amtCr, \n"
                    + "  sg.subcode, sg.name as acName, fpe.TdsDeduction As TDSDeducted, lg.tdsOnAmt, fpe.OriginalAmt as finalAmt,  \n"
                    + "  sg.nature as acNature, cm.nature as cardTypeNature,cl.cardNo as insNo,cl.cardHolderName as remak,cl.cardDate as insDate,\n"
                    + "  fpe.approvedBy \n "
                    + "  ,bd.accountNo supplieraccountNo,bd.bankName supplierbankName,bd.branch supplierbranch,bd.ifscCode supplierifscCode  "
                    + "  from FinPayEnt fpe  \n"
                    + "    LEFT JOIN voucher_Type AS vt ON fpe.v_Type = vt.v_Type \n"
                    + "    LEFT JOIN cardLedger cl  On fpe.docId = cl.docid    LEFT JOIN ledger lg  On cl.docid = lg.docid  and cl.V_SNo    = LG.V_SNo \n"
                    + "    Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                    + "    left join cardTypeMast cm On cl.cardTypeCode = cm.code \n"
                    + "	  left join subGroup sg On cl.subcode = sg.subCode 	 "
                    + "   left join subGroup sg1 On fpe.supplierCode = sg1.subCode 	\n "
                    + "    left join bankDetail bd on bd.subCode=fpe.supplierCode and bd.isDefault=1 "
                    + "	  \n"
                    + "	 WHERE vt.category in (" + vCatFinalPayment + ")  and ISNULL(fpe.approvedBy ,'')<>'' and fpe.v_date between :fromDate and :todate  "
                    + "AND fpe.site_Code = :site_Code   AND fpe.v_prefix = :vPrefix and cm.name='Bank Letter'  " + condition;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(fromDate));
            psTemp.setDate("todate", DmServer.convertDate(toDate));
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                node = new GrdBankLetterCreation_Mdl();

                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                node.setCardTypeCode(DmShared.iNull(rsTemp.getString("cardTypeCode")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                node.setSupplier(DmShared.xNull(rsTemp.getString("supplierName")));
                node.setSupplierCode(DmShared.iNull(rsTemp.getString("supplierCode")).toString());
                node.setPayDate(DmShared.dNull(rsTemp.getDate("date")));
                node.setModule(DmShared.xNull(rsTemp.getString("module")).trim().equalsIgnoreCase("PO") ? "Purchase" : "Pharmacy");

                if (DmShared.vNull(rsTemp.getString("amtCr")).doubleValue() != 0.00) {
                    node.setAmtPaid(DmServer.vNull(rsTemp.getString("amtCr"), "0.00"));
                } else {
                    node.setAmtPaid(DmServer.vNull(rsTemp.getString("amtDr"), "0.00"));
                }
                node.setOrgAmt(DmServer.vNull(rsTemp.getString("finalAmt"), "0.00"));
                node.setTds(DmServer.vNull(rsTemp.getString("TDSDeducted"), "0.00"));
                node.setBank(DmShared.xNull(rsTemp.getString("acName")));
                node.setBankCode(DmShared.iNull(rsTemp.getString("subcode")).toString());
                node.setPayMode(DmShared.xNull(rsTemp.getString("cardTypeName")));
                node.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                node.setChqNo(DmShared.xNull(rsTemp.getString("insNo")));
                node.setChqDate(DmShared.dNull(rsTemp.getDate("insDate")));

                node.setSupplieraccountNo(DmShared.xNull(rsTemp.getString("supplieraccountNo")));
                node.setSupplierbankName(DmShared.xNull(rsTemp.getString("supplierbankName")));
                node.setSupplierbranch(DmShared.xNull(rsTemp.getString("supplierbranch")));
                node.setSupplierifscCode(DmShared.xNull(rsTemp.getString("supplierifscCode")));

                lstNode.add(node);

            }
            psTemp.close();
            rsTemp.close();

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

            sqlQuery = "Select isnull(isDiscarded,0) as isDiscarded  From bankLetter Where DocId=:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {

                if (DmShared.iNull(rsTemp.getString("isDiscarded")).intValue() == 1) {
                    throw new UnsupportedOperationException("Record has been Discarded. You Can not modify This Record.");
                }

            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from bankLetter where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = " Update bankLetter set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update bankLetter set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
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
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    documentApproval(walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    private WalkThroughDefault_Mdl getWalkThroughFor(BankLetterCreation_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getvType());

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey, BankLetterCreation_Mdl mrTemp) {
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
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Check for before deleting data >>===============================*/
            sqlQuery = "Select isnull(approvedBy,'') as approvedBy  From bankLetter Where DocId=:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {

                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new UnsupportedOperationException("Record has been Approved. You Can not discard This Record.");
                }

            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "update bankletter Set isDiscarded=1 Where DocId=:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //==================================Re-Set Final Pay. Entry===================================
            sqlQuery = "update   fpe set fpe.bankLetterDocid =NULL \n"
                    + "from bankletterDetail bnkd  \n"
                    + "left join finPayEnt fpe on fpe.docid=bnkd.finPayDocid\n"
                    + "where bnkd.docid= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //===================================End====================================================
            //======================Save Log========================
            mrTemp.setSharedModifyDate(DmServer.formatTimestamp(DmServer.getServerTimestamp()));
            if (mrTemp.getModifyReason() != null) {
                ModifyData_Mdl modifyData_Mdl = new ModifyData_Mdl();
                modifyData_Mdl.setKeyId_1(mrTemp.getSharedKeyFieldCode());
                modifyData_Mdl.setKeyName_1(mrTemp.getSharedKeyFieldName());
                modifyData_Mdl.setKeyDescription_1(mrTemp.getSharedKeyFieldName());
                modifyData_Mdl.setModuleName(new lmAccounts().moduleName);
                modifyData_Mdl.setMenuName(new lmAccounts().BANK_LETTER_CREATION.caption);
                modifyData_Mdl.setModelClassName(BankLetterCreation_Mdl.class.getName());
                modifyData_Mdl.setTableName("bankLetter");

                mrTemp.getModifyReason().setMode("Discarded");

                modifyData_Mdl.setOldData(DmServer.convertToJson((BankLetterCreation_Mdl) DmServer.getSsnOldData(getClass().getName(), this), new TypeToken<BankLetterCreation_Mdl>() {
                }));
                modifyData_Mdl.setNewData(DmServer.convertToJson(mrTemp, new TypeToken<BankLetterCreation_Mdl>() {
                }));
                ModifyReason_Server.saveNew(modifyData_Mdl, mrTemp.getModifyReason(), this);
            }
            //======================================================

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
                    + "            'code_1' as code "
                    + "           , '' as name "
                    + "           , 1 v_SNo  "
                    + "           , max(bnk.v_Date) v_Date "
                    + "           , 1 as isCrossCheque "
                    + "           , '' short_Name "
                    + "           , '' as accountName "
                    + "           , max(bnkd.chqNo) as chqNo"
                    + "           , format(max(bnk.v_Date),'ddMMyyyy') as  chqDate "
                    + "           , 'Yourself for Payments' as favourOf "
                    + "           , sum(bnkd.amtPaid) as amount "
                    + "         FROM bankLetter bnk  "
                    + " left join bankLetterDetail as bnkd on bnkd.docid=bnk.docid "
                    + " "
                    + "         WHERE bnk.site_Code = :site_Code and bnk.docid=:docId  "
                    + "  group by bnkd.docid   ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            }

            rsTemp = psTemp.executeQuery();

            mrTemp.setHeaderAccountCode(DmServer.executeScalar("	select distinct bnkd.bank as drAccountCode  \n"
                    + "					from  bankLetterDetail bnkd \n"
                    + "					left join bankLetter bnk on bnk.docid=bnkd.docid  \n"
                    + "					where ISNULL(bnk.isDiscarded,0) = 0 and bnk.docId=" + DmShared.iNull(mrTemp.getSharedKeyFieldCode()) + "", cnTemp));

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
}
