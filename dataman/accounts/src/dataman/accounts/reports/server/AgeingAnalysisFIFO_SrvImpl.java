package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.AgeingAnalysisFIFO_Model;
import dataman.dmBase.self.shared.*;
import dataman.accounts.reports.shared.AgeingAnalysisFIFO_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
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

public class AgeingAnalysisFIFO_SrvImpl extends DmRSSPrivate implements AgeingAnalysisFIFO_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select  Code ,Name   From subGroupNature where Name like '%Supplier%' or Name like '%Customer%'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
            sqlWithParam.setSQL("SELECT Sm.Code, Sm.Name FROM Sitemast Sm "
                    + "WHERE CODE IN (" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ") ");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHShowRecords(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("All");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("HB");
            listNode.setSharedKeyFieldName("Having Balance");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportOnChoice(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("AG");
            listNode.setSharedKeyFieldName("Account Group Wise");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("AC");
            listNode.setSharedKeyFieldName("Account Name Wise");
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
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strSQLQuery1, strCondition1, strSQLQuery, strconditionsite, strAmtDr, strAmtCr, reptitle, repdebit, repcredit, repDays, strDate, strConditionGrpOn, strChoice;
        Double D1, D2, D3, D4, D5, D6;

        try {

            AgeingAnalysisFIFO_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.vNull(filter.get("iInterval").getText()) > DmShared.vNull(filter.get("iiInterval").getText())) {
                throw new UnsupportedOperationException("II Interval Must Be Greater Than I Interval ");
            }
            if (DmShared.vNull(filter.get("iiInterval").getText()) > DmShared.vNull(filter.get("iiiInterval").getText())) {
                throw new UnsupportedOperationException("III Interval Must Be Greater Than II Interval ");
            }
            if (DmShared.vNull(filter.get("iiiInterval").getText()) > DmShared.vNull(filter.get("ivInterval").getText())) {
                throw new UnsupportedOperationException("IV Interval Must Be Greater Than III Interval ");
            }
            if (DmShared.vNull(filter.get("ivInterval").getText()) > DmShared.vNull(filter.get("vInterval").getText())) {
                throw new UnsupportedOperationException("V Interval Must Be Greater Than IV Interval  ");
            }
            if (DmShared.vNull(filter.get("vInterval").getText()) > DmShared.vNull(filter.get("viInterval").getText())) {
                throw new UnsupportedOperationException("VI Interval Must Be Greater Than V Interval  ");
            }
            strconditionsite = "";

            strDate = DmServer.convertDate(filter.get("upToDate").getText()).toString();

            strCondition1 = " LG.V_Date <=  :upToDate  ";

            if (!DmShared.xNull(filter.get("accountType").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And ag.nature In (" + DmServer.getParamList(":accountType", filter.get("accountType").getTag()) + ")";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            if (DmShared.xNull(filter.get("accountType").getText()).trim().equals("Customer")) {
                strAmtDr = "AmtDr";
                strAmtCr = "AmtCr";
                reptitle = "Ageing Analysis of Debtors";
                repdebit = "Total Debit";
                repcredit = "Total Credit";
                repDays = "Amount Debited From Days";
            } else {

                strAmtDr = "AmtCr";
                strAmtCr = "AmtDr";
                reptitle = "Ageing Analysis of Creditors";
                repdebit = "Total Credit";
                repcredit = "Total Debit";
                repDays = "Amount Credited From Days";
            }

            if (!DmShared.xNull(filter.get("reportOnChoice").getTag()).trim().equals("AC")) {
                strConditionGrpOn = " Group By AG.GroupName ";
                strChoice = "AG";
            } else {
                strConditionGrpOn = " Group By SG.Name ";
                strChoice = "AC";
            }
            D1 = DmShared.vNull(filter.get("iInterval").getText());
            D2 = DmShared.vNull(filter.get("iiInterval").getText());
            D3 = DmShared.vNull(filter.get("iiiInterval").getText());
            D4 = DmShared.vNull(filter.get("ivInterval").getText());
            D5 = DmShared.vNull(filter.get("vInterval").getText());
            D6 = DmShared.vNull(filter.get("viInterval").getText());

            //''*********** For trans Purpose **************''
            strSQLQuery = "Select  isNull(Sum(" + strAmtDr + "),0) As Amt1,0 As Amt2,0 As Amt3,0 As Amt4,"
                    + "0 As Amt5,0 As Amt6,0 As Amt7,0 As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,Max(SM.Name) As Division,0 As AmtPR_Contra,Max(AG.GroupName) as GroupName, isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays,isNull(Max(CT.CityName),'') AS CityName    \n From Ledger As Lg ";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where DATEDIFF(day,V_Date,  '" + strDate + "' )>=0 And\n";
            strSQLQuery += " DATEDIFF(day,V_Date, '" + strDate + "' )< " + D1 + " \n and ";
            strSQLQuery += strCondition1 + strconditionsite;
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "' \n";
            strSQLQuery += strConditionGrpOn + "\n";

            strSQLQuery += " Union All\n";

            strSQLQuery += " Select  0 As Amt1,isNull(Sum(" + strAmtDr + "),0) As Amt2,0 As Amt3,0 As Amt4,0 As Amt5,0 As Amt6,0 As Amt7,0 As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,Max(SM.Name) As Division,0 As AmtPR_Contra,Max(AG.GroupName) as GroupName,isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays,isNull(Max(CT.CityName),'') AS CityName \n  From Ledger As Lg\n";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where DATEDIFF(day,V_Date,'" + strDate + "' )>=" + D1 + "\nAnd ";
            strSQLQuery += " DATEDIFF(day,V_Date,'" + strDate + "' )<" + D2 + " \n and";
            strSQLQuery += strCondition1 + strconditionsite + "\n";
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "'  \n";
            strSQLQuery += strConditionGrpOn + "\n";

            strSQLQuery += " Union All\n";

            strSQLQuery += " Select  0 As Amt1,0 As Amt2,isNull(Sum(" + strAmtDr + "),0) As Amt3,0 As Amt4,0 As Amt5,0 As Amt6,0 As Amt7,0 As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,max(sm.name) As Division,0 As AmtPR_Contra,Max(AG.GroupName) as GroupName,isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays,isNull(Max(CT.CityName),'') AS CityName \n From Ledger As Lg\n";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where DATEDIFF(day,V_Date,'" + strDate + "' )>=" + D2 + "\nAnd ";
            strSQLQuery += " DATEDIFF(day,V_Date,'" + strDate + "' )<" + D3 + "\n and ";
            strSQLQuery += strCondition1 + strconditionsite + "\n";
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "' \n";
            strSQLQuery += strConditionGrpOn + "\n";

            strSQLQuery += " Union All\n";

            strSQLQuery += " Select  0 As Amt1,0 As Amt2,0 As Amt3,isNull(Sum(" + strAmtDr + "),0) As Amt4,0 As Amt5,0 As Amt6,0 As Amt7,0 As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,max(sm.name) As Division,0 As AmtPR_Contra,Max(AG.GroupName) as GroupName,isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays ,isNull(Max(CT.CityName),'') AS CityName \n From Ledger As Lg\n";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where DATEDIFF(day,V_Date,'" + strDate + "' )>=" + D3 + "\nAnd ";
            strSQLQuery += " DATEDIFF(day,V_Date,'" + strDate + "' )<" + D4 + "\n and ";
            strSQLQuery += strCondition1 + strconditionsite + "\n";
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "' \n ";
            strSQLQuery += strConditionGrpOn + "\n";

            strSQLQuery += " Union All\n";

            strSQLQuery += "Select  0 As Amt1,0 As Amt2,0 As Amt3,0 As Amt4,isNull(Sum(" + strAmtDr + "),0) As Amt5,0 As Amt6,0 As Amt7,0 As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,max(sm.name) As Division,0 As AmtPR_Contra,Max(AG.GroupName) as GroupName,isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays,isNull(Max(CT.CityName),'') AS CityName \n From Ledger As Lg\n";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where DATEDIFF(day,V_Date,'" + strDate + "' )>=" + D4 + "\nAnd ";
            strSQLQuery += " DATEDIFF(day,V_Date,'" + strDate + "' )<" + D5 + "\n and ";
            strSQLQuery += strCondition1 + strconditionsite + "\n";
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "' \n ";
            strSQLQuery += strConditionGrpOn + "\n";

            strSQLQuery += " Union All\n";

            strSQLQuery += " Select  0 As Amt1,0 As Amt2,0 As Amt3,0 As Amt4,0 As Amt5,isNull(Sum(" + strAmtDr + "),0) As Amt6,0 As Amt7,0 As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,max(sm.name) As Division,0 As AmtPR_Contra ,Max(AG.GroupName) as GroupName,isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays,isNull(Max(CT.CityName),'') AS CityName \n From Ledger As Lg\n";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where DATEDIFF(day,V_Date,'" + strDate + "' )>=" + D5 + "\nAnd ";
            strSQLQuery += " DATEDIFF(day,V_Date,'" + strDate + "' )<" + D6 + "\n and ";
            strSQLQuery += strCondition1 + strconditionsite + "\n";
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "' \n ";
            strSQLQuery += strConditionGrpOn + "\n";

            strSQLQuery += " Union All\n";

            strSQLQuery += "Select  0 As Amt1,0 As Amt2,0 As Amt3,0 As Amt4,0 As Amt5,0 As Amt6,isNull(Sum(" + strAmtDr + "),0) As Amt7,0 As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,max(sm.name) As Division,0 As AmtPR_Contra,Max(AG.GroupName) as GroupName,isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays,isNull(Max(CT.CityName),'') AS CityName \n From Ledger As Lg\n";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where DATEDIFF(day,V_Date,'" + strDate + "' )>=" + D6 + "\n and ";
            strSQLQuery += strCondition1 + strconditionsite + "\n";
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "' \n ";
            strSQLQuery += strConditionGrpOn + "\n";

            strSQLQuery += " Union All\n";

            strSQLQuery += " Select  0 As Amt1,0 As Amt2,0 As Amt3,0\n";
            strSQLQuery += "As Amt4,0 As Amt5,0 As Amt6,0 As Amt7,isNull(Sum(" + strAmtCr + " ),0) As AmtPR,\n";
            strSQLQuery += " max(Sg.Name) As PName,max(sm.name) As Division,isNull(Sum(" + strAmtDr + " ),0) As AmtPR_Contra,Max(AG.GroupName) as GroupName,\n";
            strSQLQuery += " isNull(Max(SG.CreditLimit),0) as CreditLimit ,isNull(Max(SG.DueDays),0) as DueDays,isNull(Max(CT.CityName),'') AS CityName \n From Ledger As Lg\n";
            strSQLQuery += " Left Join SubGroup As SG On Lg.SubCode=Sg.SubCode\n";
            strSQLQuery += " Left Join AcGroup As AG On ag.GroupCode=Sg.GroupCode\n";
            strSQLQuery += " Left Join Sitemast As SM On SM.Code=Lg.site_Code\n";
            strSQLQuery += " LEFT JOIN City CT ON CT.CityCode =SG.CityCode\n";
            strSQLQuery += " Where ";
            strSQLQuery += strCondition1 + strconditionsite + "\n";
            strSQLQuery += " And Lg.V_Type<>'" + DmServer.getVType("F_AO", cnTemp) + "'  \n ";
            strSQLQuery += strConditionGrpOn;

//            if (!DmShared.xNull(filter.get("reportOnChoice").getTag()).trim().equals("AC")) {
//                strSQLQuery += " Order By GroupName ";
//            } else {
//                strSQLQuery += " Order By GroupName,PName,cityName ";
//            }
            strSQLQuery1 = "SELECT  \n"
                    + "    cast(SUM(tmp.Amt1) as numeric(18,2)) AS Amt1,\n"
                    + "    cast(SUM(tmp.Amt2)as numeric(18,2)) AS Amt2,\n"
                    + "    cast(SUM(tmp.Amt3)as numeric(18,2)) AS Amt3,\n"
                    + "    cast(SUM(tmp.Amt4)as numeric(18,2)) AS Amt4,\n"
                    + "    cast(SUM(tmp.Amt5)as numeric(18,2)) AS Amt5,\n"
                    + "    cast(SUM(tmp.Amt6)as numeric(18,2)) AS Amt6,\n"
                    + "    cast(SUM(tmp.Amt7) as numeric(18,2))AS Amt7,\n"
                    + "    cast(SUM(tmp.AmtPR)as numeric(18,2)) AS AmtPR,\n"
                    + "    cast(SUM(tmp.AmtPR_Contra)as numeric(18,2)) AS AmtPR_Contra, "
                    + "    SUM(tmp.AmtPR_Contra) AS AmtPR_Contra,\n"
                    + "    MAX(tmp.CityName) AS CityName,\n"
                    + "    MAX(tmp.Division) AS Division,\n"
                    + "    MAX(tmp.DueDays) AS DueDays,\n"
                    + "    MAX(tmp.CreditLimit) AS CreditLimit,\n"
                    + "    MAX(tmp.GroupName) AS GroupName,\n"
                    + "    tmp.PName AS PName\n"
                    + "FROM ( " + strSQLQuery + ")tmp\n"
                    + "GROUP BY tmp.PName\n"
                    + "ORDER BY tmp.PName";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery1);

            psTemp.setDate("upToDate", DmServer.convertDate(filter.get("upToDate").getText()), true);

            if (!DmShared.xNull(filter.get("accountType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountType", DmShared.xNull(filter.get("accountType").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            rsTemp = psTemp.executeQuery();

            HashMap<String, Object> ObjHashMap = new HashMap();

            ObjHashMap.put("dmD1", DmShared.xNull(String.valueOf(D1)));
            ObjHashMap.put("dmD2", DmShared.xNull(String.valueOf(D2)));
            ObjHashMap.put("dmD3", DmShared.xNull(String.valueOf(D3)));
            ObjHashMap.put("dmD4", DmShared.xNull(String.valueOf(D4)));
            ObjHashMap.put("dmD5", DmShared.xNull(String.valueOf(D5)));
            ObjHashMap.put("dmD6", DmShared.xNull(String.valueOf(D6)));
            ObjHashMap.put("dmTITLE2", DmShared.xNull(reptitle));
            ObjHashMap.put("dmREPCREDIT", DmShared.xNull(repcredit));
            ObjHashMap.put("dmREPDEBIT", DmShared.xNull(repdebit));
            ObjHashMap.put("dmREPDAYS", DmShared.xNull(repDays));
            ObjHashMap.put("dmFRMSHOWRECORDS", DmShared.xNull(filter.get("showRecords").getTag()).trim());
            ObjHashMap.put("dmFRMCHOICE", DmShared.xNull(strChoice));

            jasperReport = DmServer.compileJasper("AC_FaAgeing");
            jasperPrint = DmServer.fillJasper(this, "Ageing Analysis FIFO", jasperReport, ObjHashMap, filter, rsTemp);
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
