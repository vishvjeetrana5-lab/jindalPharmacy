package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.RptBudgetAnalysis_Model;
import dataman.accounts.reports.shared.RptBudgetAnalysis_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptBudgetAnalysis_SrvImpl extends DmRSSPrivate implements RptBudgetAnalysis_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBudgetDescription(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select distinct 'o' As Tick,ba.budgetDescription as Code,ba.budgetDescription as Name From budgetAllocation ba ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBudgetHead(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select distinct 'o' As Tick,bhm.headCode as code,bhm.headName as Name,bhmu.headName as underHead\n"
                    + " From budgetHeadMast bhm\n"
                    + " left join budgetHeadMast bhmu on bhm.headUnder=bhmu.headCode");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("underHead")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCostCenter(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select 'o' As Tick,ccm.code,ccm.Name\n"
                    + " From costCenterMast ccm\n"
                    + " where ccm.underCostCenter is null");
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
            sqlWithParam.setSQL("Select 'o' As Tick,Sm.Code,Sm.Name From Sitemast Sm "
                    + " where code in (" + DmServer.getSsnSiteList(this) + ") ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select '0' as code,'Detail' as Name union all\n"
                    + " Select '1' as code,'Summary' as Name ");
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
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition, strSQLQuery,fldShortageExcess="",additionalgrp="";

        try {
            RptBudgetAnalysis_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition = " where (bad.toDate Between :fromDate And :toDate) ";

            if (!DmShared.xNull(filter.get("budgetDescription").getTag()).trim().equalsIgnoreCase("")) {
                strCondition = strCondition + "  And ba.budgetDescription In (" + DmServer.getParamList(":budgetDescription", DmShared.xNull(filter.get("budgetDescription").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("budgetHead").getTag()).trim().equalsIgnoreCase("")) {
                strCondition = strCondition + "  And bad.headCode In (" + DmServer.getParamList(":budgetHead", DmShared.xNull(filter.get("budgetHead").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("costCenter").getTag()).trim().equalsIgnoreCase("")) {
                strCondition = strCondition + "  And bad.costCenter In (" + DmServer.getParamList(":costCenter", DmShared.xNull(filter.get("costCenter").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition = strCondition + " And ba.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition = strCondition + " And ba.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            if (!DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("0")) {
//                fldShortageExcess=",(sum(case when vt.category='BUDDI' then bad.budget else 0 end)*100)/\n"
//                        +"sum(case whe/n vt.category<>'BUDDI' then (case when bad.plusMinus=1 then bad.budget else -bad.budget end) else 0 end) as shortageExcess\n ";
            fldShortageExcess=",0 as shortageExcess\n";
            }else {
                fldShortageExcess=",0 as shortageExcess\n";
                additionalgrp=",ba.docID,ba.v_Date";
            }
            
            strSQLQuery = "select max(ba.docID) as docID,max(vt.description) as description,max(vt.v_Type) as v_Type,max(ba.recID) as recID\n"
                    + " ,max(bad.fromDate) as fromDate,max(bad.toDate) as toDate,max(ba.v_Date) as v_Date\n"
                    + " ,max(ba.budgetDescription) as budgetDescription,max(bhm.headName) as headName,max(ccm.name) as costCenter\n"
                    + " ,sum(case when vt.category<>'BUDDI' then (case when bad.plusMinus=1 then bad.budget else -bad.budget end) else 0 end) as budgetAllocated\n"
                    + " ,sum(case when vt.category='BUDDI' then bad.budget else 0 end) as budgetDisbursed\n"
                    + " ,sum(case when bad.plusMinus=1 then bad.budget else -bad.budget end) as budgetBalance\n"
                    + fldShortageExcess
                    + " ,max(case vt.category when 'BUDAL' then 1 when 'BUDAD' then 2 when 'BUDTR' then 3 when 'BUDRE' then 4 when 'BUDDI' then 5 end) as Budget_SNo\n"
                    + "  from budgetAllocation ba \n"
                    + "  left join budgetAllocationDetail bad on ba.docid=bad.docid\n"
                    + "  left join budgetHeadMast bhm on bad.headCode=bhm.headCode\n"
                    + "  left join costCenterMast ccm on bad.costCenter=ccm.code\n"
                    + "  left join voucher_Type vt on ba.v_Type=vt.v_Type\n"
                    + strCondition
                    + "  group by ba.budgetDescription,bad.headCode,bad.costCenter"
                    + additionalgrp
                    + "  order by ba.budgetDescription, headName,Budget_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("budgetDescription").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("budgetDescription", DmShared.xNull(filter.get("budgetDescription").getTag()), true);
            }
            if (!DmShared.xNull(filter.get("budgetHead").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("budgetHead", DmShared.xNull(filter.get("budgetHead").getTag()), true);
            }
            if (!DmShared.xNull(filter.get("costCenter").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("costCenter", DmShared.xNull(filter.get("costCenter").getTag()), true);
            }
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            rsTemp = psTemp.executeQuery();

            if (!DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("0")) {
                jasperReport = DmServer.compileJasper("AC_BudgetAnalysisReport");
                jasperPrint = DmServer.fillJasper(this, "Budget Analysis Report", jasperReport, new HashMap(), filter, rsTemp);
            }else {
                jasperReport = DmServer.compileJasper("AC_BudgetAnalysisReport_Detail");
                jasperPrint = DmServer.fillJasper(this, "Budget Analysis Report(Detail)", jasperReport, new HashMap(), filter, rsTemp);
            }
            
            
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
