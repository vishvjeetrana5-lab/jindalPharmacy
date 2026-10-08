package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.BankBook_Srv;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class BankBook_SrvImpl extends DmRSSPrivate implements BankBook_Srv {

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
            sqlWithParam.setSQL("SELECT Sm.Code,Sm.Name FROM Sitemast Sm"
                    + " WHERE CODE IN (" + DmServer.getParamList(":siteCode", DmServer.getSsnSiteList(this)) + ") ");
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
        String condition = "", conditionChk = "", strSQLQuery;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and lg.v_Date <= :toDate and lg.isPosted = 1 ";
            conditionChk += "and lg1.v_Date <= :toDate and lg1.isPosted = 1 ";

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                condition += "and lg.site_Code in (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
                conditionChk += "and lg1.site_Code in (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                condition += "and lg.site_Code in (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
                conditionChk += "and lg1.site_Code in (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            strSQLQuery = "declare @tempTable table (docId bigint,recId varchar(20),v_date date,v_type_short varchar(5),isBankFound tinyInt); "
                    //insert into temp table
                    + "insert into @tempTable (docId,recId,v_date,v_type_short,isBankFound)  "
                    + "select	lg.docId,max(lg.recId) as recId,max(lg.v_date) as v_date,max(vt.short_Name) as short_Name, "
                    + "min(case when sn.manualCode in ('BK','CS') and lg.subCode <> :subCode then 0 else 1 end) as isBankFound "
                    + "from ledger lg "
                    + "left join subGroup sg on sg.subCode = lg.subCode "
                    + "left join subgroupNature sn on sn.code = sg.nature "
                    + "left join voucher_type vt on vt.v_Type = lg.v_Type "
                    + "where vt.isBankCashBook = 1 "
                    + condition
                    + "and docId in (select lg1.docId from ledger lg1 where lg1.subCode = :subCode " + conditionChk + " ) "
                    + "group by docId "
                    + "order by isBankFound; "
                    //select temp table 
                    + "select * from @tempTable; "
                    //select main query
                    + "select  max(case when temp.v_date < :fromDate then null else docId end) as docId, "
                    + "max(case when temp.v_date < :fromDate then null else v_sno end) as v_sno, "
                    + "max(case when temp.v_date < :fromDate then 0 else 1 end) as sno, "
                    + "max(case when temp.v_date < :fromDate then null else v_no end) as  v_no, "
                    + "max(case when temp.v_date < :fromDate then null else recId end) as  recId, "
                    + "max(case when temp.v_date < :fromDate then null else recIdPrefix end) as  recIdPrefix, "
                    + "max(case when temp.v_date < :fromDate then null else v_date end) as v_date, "
                    + "max(case when temp.v_date < :fromDate then null else vTypeSName end) as vTypeSName, "
                    + "max(case when temp.v_date < :fromDate then 'OPENING BALANCE' else contraName end) as contraName, "
                    + "isNull(sum(case when temp.v_date < :fromDate then null else amtDr end),0) as amtDr, "
                    + "isNull(sum(case when temp.v_date < :fromDate then null else amtCr end),0) as amtCr, "
                    + "isNull(sum(case when temp.v_date < :fromDate then amtDr else null end),0) as amtDr_OP, "
                    + "isNull(sum(case when temp.v_date < :fromDate then amtCr else null end),0) as amtCr_OP, "
                    + "isnull(max(case when temp.v_date < :fromDate then null else chq_no end),'') as chq_no, "
                    + "isnull(max(case when temp.v_date < :fromDate then null else chq_Date end) ,'')as chq_Date, "
                    + "Format(isnull(max(case when temp.v_date < :fromDate then null else clg_Date end),'') ,'dd-MMM-yyyy') as clg_Date, "
                    + "isnull(max(case when temp.v_date < :fromDate then null else refNo end),'') as refNo, "
                    + "isnull(max(case when temp.v_date < :fromDate then null else refDate end),'') as refDate, "
                    + "isnull(max(case when temp.v_date < :fromDate then null else narration end),'') as narration , "
                    + "isnull(max(case when temp.v_date < :fromDate then null else serialNo end),'') as serialNo "
                    + "from ( "
                    + "     select	lg.docId,lg.v_sno,lg.v_no,lg.recId,lg.recIdPrefix,lg.v_date,vt.short_name as vTypeSName, "
                    + "			sg.name as contraName,lg.amtCr as amtDr,lg.amtDr as amtCr,lg.chq_no,"
                    + "                 lg.chq_Date,lg.clg_Date,lg.refNo,lg.refDate,lg.narration , vt.serialNo As serialNo "
                    + "			from ledger lg "
                    + "			left join subGroup sg on sg.subCode = lg.subCode "
                    + "			left join voucher_type vt on vt.v_Type = lg.v_Type "
                    + "			where vt.isBankCashBook = 1 and lg.isBankCashBook = 1 "
                    + condition
                    + "			and lg.docId in (select t.docId from @tempTable t) "
                    + "	union all "
                    + "	select	lg.docId,lg.v_sno,lg.v_no,lg.recId,lg.recIdPrefix,lg.v_date,vt.short_name as vTypeSName, "
                    + "			sg.name as contraName,lg.amtDr,lg.amtCr,lg.chq_no,"
                    + "                 lg.chq_Date,lg.clg_Date,lg.refNo,lg.refDate,lg.narration ,vt.serialNo As serialNo "
                    + "			from ledger lg "
                    + "			left join subGroup sg on sg.subCode = lg.contraSub "
                    + "			left join voucher_type vt on vt.v_Type = lg.v_Type "
                    + "			where vt.isBankCashBook = 0 and lg.subCode = :subCode "
                    + condition
                    + ") as temp "
                    + "group by (case when temp.v_date < :fromDate then null else docId end),(case when temp.v_date < :fromDate then null else v_sno end) "
                    + "order by sno,v_date,recIdPrefix,v_no,recId,docId,v_sno,serialNo ; ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("siteName").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            psTemp.setString("subCode", DmShared.xNull(filter.get("accountName").getTag()));

            psTemp.execute();
            psTemp.getMoreResults();
            rsTemp = psTemp.getResultSet();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("isBankFound")).intValue() == 0) {

                    throw new DmException(0, "Invalid entry please check Vr. No.: "+DmShared.xNull(rsTemp.getString("recId"))+", Dated:"+DmShared.xNull(rsTemp.getString("V_date"))+" Type:"+DmShared.xNull(rsTemp.getString("v_Type_short_")));
                }
            }
            rsTemp.close();

            psTemp.getMoreResults();
            rsTemp = psTemp.getResultSet();

            LinkedHashMap hm = new LinkedHashMap();

            jasperReport = DmServer.compileJasper("AC_BankBook");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().BANK_BOOK.caption, jasperReport, hm, filter, rsTemp);
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountName(DmHelper pageing, boolean blnToEndOfFile) {
    
     DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));


            sqlQuery = " SELECT SG.SubCode As code , SG.Name As name "
                    +"FROM SubGroup SG  " 
                    +"where nature =2 ";

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));

                lstTemp.add(listNode);
            }

            rsTemp.close();
            psTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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

 
     

}

