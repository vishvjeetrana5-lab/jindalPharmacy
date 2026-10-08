package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.JournalBook_Model;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.JournalBook_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class JournalBook_SrvImpl extends DmRSSPrivate implements JournalBook_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT V_TYPE AS CODE, CONCAT(LTRIM(RTRIM(Description)) , ' Book') AS NAME FROM voucher_type WHERE category='JV' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSiteName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT Sm.Code,Sm.Name FROM Sitemast Sm WHERE CODE IN (" + DmServer.getParamList(":siteCode", DmServer.getSsnSiteList(this)) + ") ");
            sqlWithParam.setStringList("siteCode", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResultPageing<KeyFieldExtended> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended mrTemp = new KeyFieldExtended();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where code=:siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
            }
            psTemp.close();
            rsTemp.close();

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



    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery;

        try {

            JournalBook_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition1 = " WHERE isNull(LG.isPosted,0) = 1 AND LG.V_Date BETWEEN :fromDate AND :toDate AND VType.Category = 'JV' AND LG.V_Prefix = :voucherPrefix ";

            if (!DmShared.xNull(filter.get("voucherType").getTag()).equals("")) {
                strCondition1 = strCondition1 + " AND LG.V_type IN (" + DmServer.getParamList(":voucherType", DmShared.xNull(filter.get("voucherType").getTag())) + ")";
            }
            if (!DmShared.xNull(filter.get("siteName").getTag()).equals("")) {
                strCondition1 = strCondition1 + "  AND LG.Site_Code IN (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 = strCondition1 + "  AND LG.Site_Code IN (" + DmServer.getParamList(":siteName", DmServer.getSsnSiteList(this)) + ") ";
            }

            //========== Head Query Date Wise Grouping ====================================
            strSQLQuery = "SELECT LG.V_date,LG.Amtcr,LG.AmtDr,LG.V_type,LG.RecID AS RecID,LG.V_prefix AS V_add,LG.Chq_No,lg.v_no ,lg.recidPrefix, LG.V_prefix AS V_add ,VType.short_Name As VTypeShortName,"
                    + " FORMAT(LG.Chq_Date , 'dd-MMM-yyyy')  As Chq_Date  ,LG.Narration AS narr,LG.V_Sno,LedgerM.Narration AS mnarration,LG.Docid,SG.Name AS NAME,St.name AS SiteName ,LG.Site_Code  "
                    + "FROM Ledger LG LEFT  JOIN  LedgerM ON LG.DocId = LedgerM.DocId  "
                    + "LEFT JOIN Subgroup SG ON SG.Subcode=LG.Subcode  "
                    + "LEFT JOIN Voucher_type VType ON Vtype.V_Type=LG.V_Type  "
                    + "LEFT JOIN SiteMast ST ON LG.Site_Code =St.code  "
                    + strCondition1
                    + "ORDER BY LG.V_DATE,LG.recidPrefix,LG.v_no,LG.RecID,lg.docId,LG.V_SNO ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            psTemp.setStringList("voucherType", DmShared.xNull(filter.get("voucherType").getTag()), true);
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("AC_Journal");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().JOURNAL_BOOK.caption , jasperReport, new HashMap(), filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setStatus(false);
            rstTemp.setErrorMessage(e.getMessage());
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
