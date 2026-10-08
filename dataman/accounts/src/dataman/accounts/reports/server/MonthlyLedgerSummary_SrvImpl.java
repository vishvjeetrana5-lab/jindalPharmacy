package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.MonthlyLedgerSummary_Model;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.MonthlyLedgerSummary_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class MonthlyLedgerSummary_SrvImpl extends DmRSSPrivate implements MonthlyLedgerSummary_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select Distinct  S.GroupCode As Code,AG.GroupName AS Name From SubGroup S "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode=S.GroupCode ");
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
            sqlWithParam.setSQL("Select Sm.Code,Sm.Name From Sitemast Sm where code in (" + DmServer.getSsnSiteList(this) + ") ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonth(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("F");
            listNode.setSharedKeyFieldName("First Six Month");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Last Six Month");
            lstTemp.add(listNode);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
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
        Connection cnTemp = null, cnTempCompany = null;
        DmPreparedStatement psTemp = null, psTempCompany = null;
        ResultSet rsTemp = null, rsTempCompany = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery, strCompanyCode, tempField;
        Integer intFirstYear, intSecondYear;
        Date DtStart = new Date();
        Date DtEnd = new Date();
        try {

            MonthlyLedgerSummary_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /////////////////////////////////////////////////////////////////////////
            cnTempCompany = DmServer.getConnection(DmServer.dbCompany);
            strCompanyCode = DmServer.getSsnCompanyCode(this);
            psTempCompany = DmPreparedStatement.prepareStatement(cnTempCompany, "SELECT Start_Dt, End_Dt  FROM company WHERE Comp_Code='" + strCompanyCode + "'");
            rsTempCompany = psTempCompany.executeQuery();
            while (rsTempCompany.next()) {
                DtStart = DmServer.convertDate(DmShared.dNull(rsTempCompany.getDate("Start_Dt")));
                DtEnd = DmServer.convertDate(DmShared.dNull(rsTempCompany.getDate("End_Dt")));

            }
            rsTempCompany.close();
            psTempCompany.close();
            cnTempCompany.close();
            //////////////////////////////////////////////////////////////////////////    
            strCondition1 = "";
            if (DmShared.xNull(filter.get("month").getTag()).equals("F")) {
                tempField = "0 As Sel ";

                Calendar cal = Calendar.getInstance();
                cal.setTime(DtStart);
                intFirstYear = cal.get(Calendar.YEAR);
                intSecondYear = cal.get(Calendar.YEAR);
                strCondition1 += " Where Month(LG.V_Date) In (4,5,6,7,8,9) And Year(LG.V_Date) In ('" + intFirstYear + "') ";
            } else {
                tempField = "1 As Sel ";
                Calendar cal = Calendar.getInstance();
                cal.setTime(DtStart);
                intFirstYear = cal.get(Calendar.YEAR);
                cal.setTime(DtEnd);
                intSecondYear = cal.get(Calendar.YEAR);
                strCondition1 += " Where Month(LG.V_Date) In (10,11,12,1,2,3) And Year(LG.V_Date) In ('" + intFirstYear + "','" + intSecondYear + "') ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code IN (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") And LG.isPosted = 1  ";
            } else {
                strCondition1 += " And LG.Site_Code IN (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") And LG.isPosted = 1  ";
            }

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + "  And (isNull(CAST(SG.GroupCode AS VARCHAR),'') In (Select isNull(CAST(AGP.GroupCode AS VARCHAR),'')"
                        + "  From AcGroupPath AGP Where AGP.GroupUnder In (" + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) Or SG.GroupCode In ( " + DmServer.getParamList(":accountGroup", DmShared.xNull(filter.get("accountGroup").getTag())) + ")) ";
            }

            strSQLQuery = "Select GroupName,PName,SubCode, "
             + "(Case When (isNull(DR_1,0)-isNull(CR_1,0))>0 Then (isNull(DR_1,0)-isNull(CR_1,0)) Else 0 End) As DR_1, "
             + "(Case When (isNull(CR_1,0)-isNull(DR_1,0))>0 Then (isNull(CR_1,0)-isNull(DR_1,0)) Else 0 End) As CR_1, "
             + "(Case When (isNull(DR_2,0)-isNull(CR_2,0))>0 Then (isNull(DR_2,0)-isNull(CR_2,0)) Else 0 End) As DR_2, "
             + "(Case When (isNull(CR_2,0)-isNull(DR_2,0))>0 Then (isNull(CR_2,0)-isNull(DR_2,0)) Else 0 End) As CR_2, "
             + "(Case When (isNull(DR_3,0)-isNull(CR_3,0))>0 Then (isNull(DR_3,0)-isNull(CR_3,0)) Else 0 End) As DR_3, "
             + "(Case When (isNull(CR_3,0)-isNull(DR_3,0))>0 Then (isNull(CR_3,0)-isNull(DR_3,0)) Else 0 End) As CR_3, "
             + "(Case When (isNull(DR_4,0)-isNull(CR_4,0))>0 Then (isNull(DR_4,0)-isNull(CR_4,0)) Else 0 End) As DR_4, "
             + "(Case When (isNull(CR_4,0)-isNull(DR_4,0))>0 Then (isNull(CR_4,0)-isNull(DR_4,0)) Else 0 End) As CR_4, "
             + "(Case When (isNull(DR_5,0)-isNull(CR_5,0))>0 Then (isNull(DR_5,0)-isNull(CR_5,0)) Else 0 End) As DR_5, "
             + "(Case When (isNull(CR_5,0)-isNull(DR_5,0))>0 Then (isNull(CR_5,0)-isNull(DR_5,0)) Else 0 End) As CR_5, "
             + "(Case When (isNull(DR_6,0)-isNull(CR_6,0))>0 Then (isNull(DR_6,0)-isNull(CR_6,0)) Else 0 End) As DR_6, "
             + "(Case When (isNull(CR_6,0)-isNull(DR_6,0))>0 Then (isNull(CR_6,0)-isNull(DR_6,0)) Else 0 End) As CR_6, "
             + "Sel "
             + "From ( "
             + "Select	AG.GroupName,Max(SG.Name) As PName,LG.SubCode, "
             + "Sum(Case When (Month(LG.V_Date)=4 Or Month(LG.V_Date)=10)And Year(LG.V_Date)='" + intFirstYear + "' Then LG.AmtDr Else 0 End) As DR_1, "
             + "Sum(Case When (Month(LG.V_Date)=4 Or Month(LG.V_Date)=10)And Year(LG.V_Date)='" + intFirstYear + "' Then LG.AmtCr Else 0 End) As CR_1, "
             + "Sum(Case When (Month(LG.V_Date)=5 Or Month(LG.V_Date)=11)And Year(LG.V_Date)='" + intFirstYear + "' Then LG.AmtDr Else 0 End) As DR_2, "
             + "Sum(Case When (Month(LG.V_Date)=5 Or Month(LG.V_Date)=11)And Year(LG.V_Date)='" + intFirstYear + "' Then LG.AmtCr Else 0 End) As CR_2, "
             + "Sum(Case When (Month(LG.V_Date)=6 Or Month(LG.V_Date)=12)And Year(LG.V_Date)='" + intFirstYear + "' Then LG.AmtDr Else 0 End) As DR_3, "
             + "Sum(Case When (Month(LG.V_Date)=6 Or Month(LG.V_Date)=12)And Year(LG.V_Date)='" + intFirstYear + "' Then LG.AmtCr Else 0 End) As CR_3, "
             + "Sum(Case When (Month(LG.V_Date)=7 Or Month(LG.V_Date)=1)And Year(LG.V_Date)='" + intSecondYear + "' Then LG.AmtDr Else 0 End) As DR_4, "
             + "Sum(Case When (Month(LG.V_Date)=7 Or Month(LG.V_Date)=1)And Year(LG.V_Date)='" + intSecondYear + "' Then LG.AmtCr Else 0 End) As CR_4, "
             + "Sum(Case When (Month(LG.V_Date)=8 Or Month(LG.V_Date)=2)And Year(LG.V_Date)='" + intSecondYear + "' Then LG.AmtDr Else 0 End) As DR_5, "
             + "Sum(Case When (Month(LG.V_Date)=8 Or Month(LG.V_Date)=2)And Year(LG.V_Date)='" + intSecondYear + "' Then LG.AmtCr Else 0 End) As CR_5, "
             + "Sum(Case When (Month(LG.V_Date)=9 Or Month(LG.V_Date)=3)And Year(LG.V_Date)='" + intSecondYear + "' Then LG.AmtDr Else 0 End) As DR_6, "
             + "Sum(Case When (Month(LG.V_Date)=9 Or Month(LG.V_Date)=3)And Year(LG.V_Date)='" + intSecondYear + "' Then LG.AmtCr Else 0 End) As CR_6, "
             + tempField
             + "From Ledger LG Left Join "
             + "SubGroup SG ON LG.SubCode=SG.SubCode Left Join "
             + "AcGroup AG ON AG.GroupCode=SG.GroupCode "
             + strCondition1
             + "Group By AG.GroupName,LG.SubCode "
             + ") As Tmp "
             + "Order By GroupName,PName " ;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("AC_MonthlyLedgerSummary");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().MONTHLY_LEDGER_SUMMARY.caption , jasperReport, new HashMap(), filter, rsTemp);
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
