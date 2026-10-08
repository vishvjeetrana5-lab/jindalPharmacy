package dataman.accounts.bulkChequePrinting.server;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Mdl;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Srv;
import dataman.accounts.bulkChequePrinting.shared.BulkChequePrinting_Vld;
import dataman.accounts.bulkChequePrinting.shared.ChangeChqNo_Mdl;
import dataman.accounts.bulkChequePrinting.shared.GrdBulkChequePrinting_Mdl;
import dataman.accounts.bulkChequePrinting.shared.Print_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHAccountName_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHBankName_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHVoucherNo_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHVoucherType_Mdl;
import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Mdl;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmJasperCompiler;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.awt.Color;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRExpressionChunk;
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

public class BulkChequePrinting_SrvImpl extends DmRSSPrivate implements BulkChequePrinting_Srv {

    @Override
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;

        try {
            rstTemp.setData(lstTemp);
            listNode = new TxtHVoucherType_Mdl();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT VT.V_Type,VT.Description,VT.DefaultAc,SG.Name AS DAName,SG.Nature,VT.Category "
                    + "FROM Voucher_Type VT "
                    + "LEFT JOIN SubGroup SG ON VT.DefaultAc=SG.SubCode "
                    + "WHERE VT.NCat = 'FA' "
                    + "AND VT.category NOT IN ('" + new LibraryVoucherCategory().BANK_RECEIPT + "','" + new LibraryVoucherCategory().CASH_RECEIPT + "')";
            DmSQLWithParam sqlWithParam = new DmSQLWithParam();
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setDefaultAc(DmShared.xNull(rsTemp.getString("DefaultAc")));
                listNode.setDAName(DmShared.xNull(rsTemp.getString("DAName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("Category")));
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
    public DmResultPageing<List<TxtHBankName_Mdl>> getTxtHBankName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHBankName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHBankName_Mdl> lstTemp = new ArrayList<>();
        TxtHBankName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  SG.SubCode, SG.Name, ISNULL(c.CityName, '') AS CityName, SG.ManualCode"
                    + "		FROM SubGroup SG  "
                    + "		LEFT JOIN city c ON c.CityCode = SG.CityCode  "
                    + "		LEFT JOIN subGroupSiteList SGL ON SG.subCode = SGL.subCode  "
                    + "		WHERE SGL.site_Code = :site_Code "
                    + "         AND sg.nature = :bankCode "
                    + "		AND SG.isActive = 1  ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("bankCode", DmServer.getCodeViaManualCode("BK", "manualCode", "subGroupNature", "code", cnTemp));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHBankName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setSCode(DmShared.xNull(rsTemp.getString("ManualCode")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile, String contraSub, String fromDate, String toDate, String voucherNoDocid) {
        DmResultPageing<List<TxtHAccountName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountName_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(contraSub).longValue() > 0) {
                condition += " AND lm.contraSub=:contraSub ";
            }
            if (DmShared.iNull(voucherNoDocid).longValue() > 0) {
                condition += " AND lm.docId=IN (" + DmServer.getParamList(":docId", voucherNoDocid) + " )";
            }
            if (!DmShared.dNull(fromDate).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date>=:fromDate ";
            }
            if (!DmShared.dNull(toDate).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date<=:toDate ";
            }

            sqlQuery = "        SELECT "
                    + "            lm.subCode as code "
                    + "          , max(sg.displayName) as name"
                    + "          , max(ISNULL(c.CityName, '')) AS CityName "
                    + "          , max(sg.manualCode) AS manualCode "
                    + "         FROM ledger lm "
                    + "         LEFT JOIN subGroup sg ON lm.subCode=sg.subCode "
                    + "         LEFT JOIN city c ON c.CityCode = sg.CityCode "
                    + "		LEFT JOIN subGroupSiteList SGL ON SG.subCode = SGL.subCode  "
                    + "         WHERE SGL.site_Code = :site_Code "
                    + "         AND lm.isPosted=1 "
                    + "		AND sg.isActive = 1  " + condition
                    + "         GROUP BY lm.subCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(contraSub).longValue() > 0) {
                psTemp.setString("contraSub", DmShared.iNull(contraSub).toString());
            }
            if (!DmShared.xNull(voucherNoDocid).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(voucherNoDocid));
            }
            if (!DmShared.dNull(fromDate).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(fromDate)));
            }
            if (!DmShared.dNull(toDate).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(toDate)));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<TxtHVoucherNo_Mdl>> getTxtHVoucherNo(DmHelper helper, boolean blnToEndOfFile, String contraSub, String fromDate, String toDate) {
        DmResultPageing<List<TxtHVoucherNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherNo_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(contraSub).longValue() > 0) {
                condition += " AND contraSub=:contraSub ";
            }
            if (!DmShared.dNull(fromDate).equalsIgnoreCase("")) {
                condition += " AND v_Date>=:fromDate ";
            }
            if (!DmShared.dNull(toDate).equalsIgnoreCase("")) {
                condition += " AND v_Date<=:toDate ";
            }

            sqlQuery = "        SELECT "
                    + "             max(lm.recId) as name "
                    + "           , lm.docid as code "
                    + "         FROM ledger lm "
                    + "         LEFT JOIN subGroup sg ON lm.subCode=sg.subCode "
                    + "		LEFT JOIN subGroupSiteList SGL ON SG.subCode = SGL.subCode  "
                    + "         WHERE SGL.site_Code = :site_Code "
                    + "         AND lm.isPosted=1 "
                    + "		AND sg.isActive = 1  " + condition
                    + "         GROUP BY lm.docId ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(contraSub).longValue() > 0) {
                psTemp.setString("contraSub", DmShared.iNull(contraSub).toString());
            }
            if (!DmShared.dNull(fromDate).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(fromDate)));
            }
            if (!DmShared.dNull(toDate).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(toDate)));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rsTemp.close();
            psTemp.close();

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
//<editor-fold defaultstate="collapsed" desc="Comment'">

//    @Override
//    public DmResult<List<GrdBulkChequePrinting_Mdl>> showRecords(String contraSub, String fromDate, String toDate, String voucherNoDocid, String accountCode, String v_type, String recordTypeCode) {
//        DmResult<List<GrdBulkChequePrinting_Mdl>> rstTemp = new DmResult<>();
//        Connection cnTemp = null;
//        DmPreparedStatement psTemp = null;
//
//        List<GrdBulkChequePrinting_Mdl> lstTemp = new ArrayList<>();
//        GrdBulkChequePrinting_Mdl listNode;
//        ResultSet rsTemp = null;
//        String sqlQuery, condition = "";
//
//        try {
//
//            rstTemp.setData(lstTemp);
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//
//            if (DmShared.iNull(contraSub).longValue() > 0) {
//                condition += " AND lm.contraSub=:contraSub ";
//            }
//            if (DmShared.iNull(v_type).longValue() > 0) {
//                condition += " AND lm.v_type=:v_type ";
//            }
//            if (DmShared.iNull(voucherNoDocid).longValue() > 0) {
//                condition += " AND lm.docId=IN (" + DmServer.getParamList(":docId", voucherNoDocid) + " )";
//            }
//            if (!DmShared.xNull(accountCode).trim().equalsIgnoreCase("")) {
//                condition += " and lm.subCode NOT IN (" + DmServer.getParamList(":subCode", accountCode) + " )";
//            }
//            if (DmShared.iNull(recordTypeCode).intValue() == 1) {
//                condition += " AND ISNULL(lm.amtCr,0)<>0 ";
//            } else if (DmShared.iNull(recordTypeCode).intValue() == 2) {
//                condition += " AND ISNULL(lm.amtDr,0)<>0 ";
//            }
//            if (!DmShared.dNull(fromDate).equalsIgnoreCase("")) {
//                condition += " AND lm.v_Date>=:fromDate ";
//            }
//            if (!DmShared.dNull(toDate).equalsIgnoreCase("")) {
//                condition += " AND lm.v_Date<=:toDate ";
//            }
//
//            sqlQuery = "        SELECT "
//                    + "             lm.docid as code "
//                    + "           , lm.recid as name "
//                    + "           , lm.v_SNo "
//                    + "           , lm.v_Date "
//                    + "           , vt.short_Name "
//                    + "           , sgc.displayName as accountName "
//                    + "           , lm.chq_No "
//                    + "           , lm.chq_Date "
//                    + "           , lm.favourOf as printName "
//                    + "           , ISNULL(lm.amtDr,0) as debit "
//                    + "           , ISNULL(lm.amtCr,0) as credit "
//                    + "         FROM ledger lm "
//                    + "         LEFT JOIN subGroup sg ON lm.subCode=sg.subCode "
//                    + "         LEFT JOIN subGroup sgc ON lm.contraSub=sgc.subCode "
//                    + "         INNER JOIN voucher_type vt ON vt.v_Type=lm.v_Type "
//                    + "		LEFT JOIN subGroupSiteList SGL ON SG.subCode = SGL.subCode  "
//                    + "         WHERE SGL.site_Code = :site_Code "
//                    + "         AND lm.isPosted=1 "
//                    + "		AND sg.isActive = 1  " + condition;
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
//            if (DmShared.iNull(contraSub).longValue() > 0) {
//                psTemp.setString("contraSub", DmShared.iNull(contraSub).toString());
//            }
//            if (DmShared.iNull(v_type).longValue() > 0) {
//                psTemp.setString("v_type", DmShared.iNull(v_type).toString());
//            }
//            if (!DmShared.xNull(voucherNoDocid).equalsIgnoreCase("")) {
//                psTemp.setStringList("docId", DmShared.xNull(voucherNoDocid));
//            }
//            if (!DmShared.dNull(fromDate).equalsIgnoreCase("")) {
//                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(fromDate)));
//            }
//            if (!DmShared.dNull(toDate).equalsIgnoreCase("")) {
//                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(toDate)));
//            }
//            if (!DmShared.xNull(accountCode).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("subCode", DmShared.xNull(accountCode));
//            }
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                listNode = new GrdBulkChequePrinting_Mdl();
//                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
//                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
//                listNode.setV_TypeShortName(DmShared.xNull(rsTemp.getString("short_Name")));
//                listNode.setV_SNo(DmShared.iNull(rsTemp.getString("v_SNo")).toString());
//                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
//                listNode.setAccountName(DmShared.xNull(rsTemp.getString("accountName")));
//                listNode.setChqNo(DmShared.xNull(rsTemp.getString("chq_No")));
//                listNode.setChqDate(DmShared.dNull(rsTemp.getDate("chq_Date")));
//                listNode.setDebit(DmServer.vNull(rsTemp.getString("credit")));
//                listNode.setCredit(DmServer.vNull(rsTemp.getString("debit")));
//                listNode.setPrintName(DmShared.xNull(rsTemp.getString("printName")));
//
//                lstTemp.add(listNode);
//            }
//
//            rsTemp.close();
//            psTemp.close();
//
//            rstTemp.setStatus(true);
//            cnTemp.close();
//        } catch (Exception e) {
//            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return rstTemp;
//    }
    //</editor-fold>
    @Override
    public DmResult<List<GrdBulkChequePrinting_Mdl>> showRecords(BulkChequePrinting_Mdl mrTemp) {
        DmResult<List<GrdBulkChequePrinting_Mdl>> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        List<GrdBulkChequePrinting_Mdl> lstTemp = new ArrayList<>();
        GrdBulkChequePrinting_Mdl listNode;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        DmPreparedStatement psTemp = null;

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(mrTemp.getContraSub()).longValue() > 0) {
                condition += " AND lm.contraSub=:contraSub ";
            }
            if (DmShared.iNull(mrTemp.getV_type()).longValue() > 0) {
                condition += " AND lm.v_type=:v_type ";
            }
            if (!DmShared.xNull(mrTemp.getVoucherNoDocid()).trim().equalsIgnoreCase("")) {
                condition += " AND lm.docId IN (" + DmServer.getParamList(":docId", mrTemp.getVoucherNoDocid()) + " )";
            }
            if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                condition += " and lm.subCode NOT IN (" + DmServer.getParamList(":subCode", mrTemp.getAccountCode()) + " )";
            }
            if (DmShared.iNull(mrTemp.getRecordTypeCode()).intValue() == 2) {
                condition += " AND ISNULL(lm.amtCr,0)>0 ";
            } else if (DmShared.iNull(mrTemp.getRecordTypeCode()).intValue() == 1) {
                condition += " AND ISNULL(lm.amtDr,0)>0 ";
            }
            if (!DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date>=:fromDate ";
            }
            if (!DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date<=:toDate ";
            }

            sqlQuery = "        SELECT "
                    + "             lm.docid as code "
                    + "           , lm.recid as name "
                    + "           , 1 as isCrossCheque "
                    + "           , lm.v_SNo "
                    + "           , lm.v_Date "
                    + "           , vt.short_Name "
                    + "           , sgc.displayName as accountName "
                    + "           , lm.chq_No "
                    + "           , lm.chq_Date "
                    + "           , lm.favourOf as printName "
                    + "           , ISNULL(lm.amtDr,0) as debit "
                    + "           , ISNULL(lm.amtCr,0) as credit "
                    + "           , CASE WHEN ISNULL(lm.amtCr,0)>0 THEN lm.amtCr ELSE ISNULL(lm.amtDr,0) END as displayAmount "
                    + "         FROM ledger lm "
                    + "         LEFT JOIN subGroup sg ON lm.subCode=sg.subCode "
                    + "         LEFT JOIN subGroup sgc ON lm.contraSub=sgc.subCode "
                    + "         INNER JOIN voucher_type vt ON vt.v_Type=lm.v_Type "
                    + "         WHERE lm.site_Code = :site_Code "
                    + "         AND lm.isPosted=1 "
                    + "		AND sg.isActive = 1  " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(mrTemp.getContraSub()).longValue() > 0) {
                psTemp.setString("contraSub", DmShared.iNull(mrTemp.getContraSub()).toString());
            }
            if (DmShared.iNull(mrTemp.getV_type()).longValue() > 0) {
                psTemp.setString("v_type", DmShared.iNull(mrTemp.getV_type()).toString());
            }
            if (!DmShared.xNull(mrTemp.getVoucherNoDocid()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(mrTemp.getVoucherNoDocid()));
            }
            if (!DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(mrTemp.getFromDate())));
            }
            if (!DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(mrTemp.getToDate())));
            }
            if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(mrTemp.getAccountCode()));
            }
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new GrdBulkChequePrinting_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setV_TypeShortName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setV_SNo(DmShared.iNull(rsTemp.getString("v_SNo")).toString());
                listNode.setIsCrossChqCode(DmShared.iNull(rsTemp.getString("isCrossCheque")).toString());
                listNode.setIsCrossChqText(DmShared.iNull(rsTemp.getString("isCrossCheque")).intValue() == 1 ? "Yes" : "No");
                listNode.setDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setAccountName(DmShared.xNull(rsTemp.getString("accountName")));
                listNode.setPrvChqNo(DmShared.xNull(rsTemp.getString("chq_No")));
                listNode.setChqNo(DmShared.xNull(rsTemp.getString("chq_No")));
                listNode.setChqDate(DmShared.dNull(rsTemp.getDate("chq_Date")));
                listNode.setDebit(DmServer.vNull(rsTemp.getString("debit"), "0.00"));
                listNode.setCredit(DmServer.vNull(rsTemp.getString("credit"), "0.00"));
                listNode.setPrintName(DmShared.xNull(rsTemp.getString("printName")));
                listNode.setDisplayAmount(DmServer.vNull(rsTemp.getString("displayAmount"), "0.00"));
                listNode.setIsChecked(DmShared.iNull("1").toString());

                lstTemp.add(listNode);
            }

            rsTemp.close();
            psTemp.close();

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
    public DmResult<String> executePrint(BulkChequePrinting_Mdl mrTemp, List<Print_Mdl> listPrint, boolean isMerge) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String strSQL, strSQL1, condition = "", amount = "", conditionPrint = "";
        ResultSet rsTemp = null, rsTemp1 = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        ChequePrintSetup_Mdl mrTempChquePrintSetup = new ChequePrintSetup_Mdl();
        dmrTemp.setData("");

        try {
            BulkChequePrinting_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            amount = " CASE WHEN SUM(ISNULL(lm.amtDr,0))>0 THEN SUM(ISNULL(lm.amtDr,0)) ELSE SUM(ISNULL(lm.amtCr,0)) END ";
            for (Print_Mdl field : listPrint) {
                if (DmShared.iNull(field.getDocId()).longValue() != 0) {
                    if (!conditionPrint.trim().equalsIgnoreCase("")) {
                        conditionPrint += " or ";
                    }
                    conditionPrint += " (lm.docId = '" + DmShared.iNull(field.getDocId()).toString() + "' ";
                    if (isMerge) {
                        conditionPrint += "         and lm.chq_No = '" + DmShared.xNull(field.getChqNo()) + "' ";
                    } else {
                        conditionPrint += "         and lm.v_SNo = '" + DmShared.xNull(field.getV_Sno()) + "' ";
                    }

//                    conditionPrint += " and " + amount + " = '" + DmShared.xNull(field.getAmount()) + "'  "
                    conditionPrint += " ) ";
                }
            }

            if (DmShared.iNull(mrTemp.getContraSub()).longValue() > 0) {
                condition += " AND lm.contraSub=:contraSub ";
            }
            if (DmShared.iNull(mrTemp.getV_type()).longValue() > 0) {
                condition += " AND lm.v_type=:v_type ";
            }
            if (!DmShared.xNull(mrTemp.getVoucherNoDocid()).trim().equalsIgnoreCase("")) {
                condition += " AND lm.docId IN (" + DmServer.getParamList(":docId", mrTemp.getVoucherNoDocid()) + " )";
            }
            if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                condition += " and lm.subCode NOT IN (" + DmServer.getParamList(":subCode", mrTemp.getAccountCode()) + " )";
            }
            if (DmShared.iNull(mrTemp.getRecordTypeCode()).intValue() == 2) {
                condition += " AND ISNULL(lm.amtCr,0)>0 ";
            } else if (DmShared.iNull(mrTemp.getRecordTypeCode()).intValue() == 1) {
                condition += " AND ISNULL(lm.amtDr,0)>0 ";
            }
            if (!DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date>=:fromDate ";
            }
            if (!DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date<=:toDate ";
            }

            strSQL = "        SELECT "
                    + "             lm.docid as code "
                    + "           , MAX(lm.recid) as name "
                    + "           , MAX(lm.isCrossCheque) as isCrossCheque ";
            if (!isMerge) {
                strSQL += "           , lm.v_SNo ";
            } else {
                strSQL += "           , MIN(lm.v_SNo) as  v_SNo ";
            }
            strSQL += "           , MAX(lm.v_Date) as v_Date"
                    + "           , MAX(vt.short_Name) as short_Name"
                    + "           , MAX(sgc.displayName) as accountName ";
            if (isMerge) {
                strSQL += "           , lm.chq_No as chqNo";
            } else {
                strSQL += "           , MAX(lm.chq_No) as  chqNo ";
            }
            strSQL += "           , format(MAX(lm.chq_Date),'ddMMyyyy') as  chqDate "
                    + "           , MAX(lm.favourOf) as favourOf "
                    + "           , " + amount + " as amount "
                    + "         FROM ledger lm "
                    + "         LEFT JOIN subGroup sg ON lm.subCode=sg.subCode "
                    + "         LEFT JOIN subGroup sgc ON lm.contraSub=sgc.subCode "
                    + "         INNER JOIN voucher_type vt ON vt.v_Type=lm.v_Type "
                    + "		LEFT JOIN subGroupSiteList SGL ON SG.subCode = SGL.subCode  "
                    + "         WHERE SGL.site_Code = :site_Code "
                    + "         AND lm.isPosted=1 "
                    + "		AND sg.isActive = 1  " + condition;
            strSQL += isMerge ? " GROUP BY lm.docid , lm.chq_No " : " GROUP BY lm.docid , lm.v_SNo ";
            strSQL += "         HAVING (" + conditionPrint + ")";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(mrTemp.getContraSub()).longValue() > 0) {
                psTemp.setString("contraSub", DmShared.iNull(mrTemp.getContraSub()).toString());
            }
            if (DmShared.iNull(mrTemp.getV_type()).longValue() > 0) {
                psTemp.setString("v_type", DmShared.iNull(mrTemp.getV_type()).toString());
            }
            if (!DmShared.xNull(mrTemp.getVoucherNoDocid()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(mrTemp.getVoucherNoDocid()));
            }
            if (!DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(mrTemp.getFromDate())));
            }
            if (!DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(mrTemp.getToDate())));
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
            psTemp1.setString("bankCode", DmShared.iNull(mrTemp.getContraSub()).toString());
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRecordType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Debit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Credit");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(helper, lstTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> updateVoucher(GrdBulkChequePrinting_Mdl bulkChequePrinting_Mdl, List<ChangeChqNo_Mdl> mdlLst, String chkNoLength, boolean isMerged) {

        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL = "";
        dmrTemp.setData("");
        

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            if (bulkChequePrinting_Mdl != null) {
                strSQL = " UPDATE ledger "
                        + " SET "
                        + " favourOf = :favourOf "
                        + " ,chq_No = :chq_No "
                        + " ,chq_Date = :chq_Date "
                        + " ,U_Name = :u_Name "
                        + " ,U_EntDt = :uEntDt "
                        + " ,isCrossCheque = :isCrossCheque "
                        + " Where docId = :docId ";
                if (!isMerged) {
                    strSQL += " AND v_SNo = :v_SNo ";
                } else {
                    strSQL += " AND chq_No = :prvChqNo ";
                }
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("favourOf", bulkChequePrinting_Mdl.getPrintName());
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.iNull(bulkChequePrinting_Mdl.getSharedKeyFieldCode()).toString());
                psTemp.setString("chq_No", DmShared.xNull(bulkChequePrinting_Mdl.getChqNo()));
                psTemp.setString("isCrossCheque", DmShared.iNull(bulkChequePrinting_Mdl.getIsCrossChqCode()).toString());
                psTemp.setDate("chq_Date", DmServer.convertDate(bulkChequePrinting_Mdl.getChqDate()));
                if (!isMerged) {
                    psTemp.setString("v_SNo", bulkChequePrinting_Mdl.getV_SNo());
                } else {
                    psTemp.setString("prvChqNo", DmShared.iNull(bulkChequePrinting_Mdl.getPrvChqNo()).toString());
                }
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                for (ChangeChqNo_Mdl mdl : mdlLst) {
                    strSQL = " UPDATE ledger "
                            + " SET "
                            + " chq_No = :chq_No "
                            + " , chq_Date = :chq_Date "
                            + " , U_Name = :u_Name "
                            + " , U_EntDt = :uEntDt "
                            + " , isCrossCheque = :isCrossCheque "
                            + " Where docId = :docId ";
                    if (!isMerged) {
                        strSQL += " AND v_SNo = :v_SNo ";
                    } else {
                        strSQL += " AND chq_No = :prvChqNo ";
                    }
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                    psTemp.setString("docId", DmShared.iNull(mdl.getDocId()).toString());
                    psTemp.setString("chq_No", DmShared.xNull(String.format("%0" + (chkNoLength) + "d", DmShared.iNull(mdl.getChqNo()).intValue())));
                    psTemp.setDate("chq_Date", DmServer.convertDate(mdl.getChqDate()));
                    psTemp.setString("isCrossCheque", DmShared.iNull(mdl.getIsCrossChqCode()).toString());
                    if (!isMerged) {
                        psTemp.setString("v_SNo", mdl.getV_SNo());
                    } else {
                        psTemp.setString("prvChqNo", DmShared.iNull(mdl.getPrvChqNo()).toString());
                    }
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

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
