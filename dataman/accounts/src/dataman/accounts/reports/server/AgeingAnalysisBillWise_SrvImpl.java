package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.AgeingAnalysisBillWise_Model;
import dataman.accounts.reports.shared.AgeingAnalysisBillWise_Srv;
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

public class AgeingAnalysisBillWise_SrvImpl extends DmRSSPrivate implements AgeingAnalysisBillWise_Srv {

    @Override
    public DmResultPageing<List<AgeingAnalysisBillWise_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<AgeingAnalysisBillWise_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<AgeingAnalysisBillWise_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        AgeingAnalysisBillWise_Model.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String strDefaultGroup = getVarValue();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select SG.SubCode,SG.Name,SG.ManualCode,isNull(CT.CityName,'') as City,AG.GroupName From SubGroup  SG Left Join  "
                    + " AcGroup AG On AG.GroupCode=SG.GroupCode  "
                    + " Left Join City CT On SG.CityCode=CT.CityCode "
                    + " LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode   "
                    + " Where SG.GroupCode In (SELECT AGP.GroupCode FROM AcGroupPath AGP WHERE "
                    + " AGP.GroupUnder='" + strDefaultGroup + "') Or SG.GroupCode='" + strDefaultGroup + "' AND SL.site_Code = :site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new AgeingAnalysisBillWise_Model.TxtHAccountName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("GroupName")));

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
            sqlWithParam.setSQL("Select Sm.Code,Sm.Name From Sitemast Sm "
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String strDefaultGroup = getVarValue();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Ag.GroupCode,Ag.GroupName FROM AcGroup  AG "
                    + " Where AG.GroupUnder In (SELECT AGP.GroupCode FROM AcGroupPath AGP"
                    + "  WHERE AGP.GroupUnder='" + strDefaultGroup + "') Or AG.GroupUnder='" + strDefaultGroup + "'  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("GroupCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("GroupName")));
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

    public String getVarValue() {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String strSQLQuery, strVal = "Sundry Debtors", returnVal = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            strSQLQuery = "Select GroupCode From AcGroup Where GroupName='" + strVal + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                returnVal = DmShared.xNull(rsTemp.getString("GroupCode"));
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
        return returnVal;
    }

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery, strDate = "", strVal = "", strDefaultGroup = "";
        Boolean BlnForDebtors = true;
        Double D1 = 0.0;

        if (BlnForDebtors) {
            strVal = "Sundry Debtors";
        } else {
            strVal = "Sundry Creditors";
        }
        try {

            AgeingAnalysisBillWise_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strSQLQuery = "Select GroupCode From AcGroup Where GroupName='" + strVal + "'";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                strDefaultGroup = DmShared.xNull(rsTemp.getString("GroupCode"));
            }

            rsTemp.close();
            psTemp.close();

            strCondition1 = " Where LG.V_Date <= :asOnDate  ";

            strDate = DmServer.convertDate(DmShared.xNull(filter.get("asOnDate").getText())).toString();
            D1 = DmShared.vNull(filter.get("interval").getText());

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And (isNull(CAST(SG.GroupCode AS VARCHAR),'') In (Select isNull(CAST(AGP.GroupCode AS VARCHAR),'') From AcGroupPath AGP Where AGP.GroupUnder In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) Or SG.GroupCode In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + " )) ";
            } else {
                strCondition1 += " And (isNull(CAST(SG.GroupCode AS VARCHAR),'') In (Select isNull(CAST(AGP.GroupCode AS VARCHAR),'') From AcGroupPath AGP Where AGP.GroupUnder = '" + strDefaultGroup + "') Or SG.GroupCode = '" + strDefaultGroup + "') ";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.SubCode In ( " + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            strSQLQuery = "SELECT * FROM ( Select	LG.DocId, "
             + " (Case When isNull(LG.isSystem_Generated,'0')='1' And isNull(LG.TDSDesc,'')='' And "
             + " isNull(LG.TDS_Of_V_SNo,-1)<>-1 Then LG.TDS_Of_V_SNo Else LG.V_SNo End) As V_SNo, "
             + " Max(LG.RecID) As VNo, "
             + " Max(LG.V_Type) As VType, Max(LG.V_Date) As VDate,Max(SG.Name) As PName, "
             + " Max(LG.SubCode) As SubCode, "
             + " Max((Case When isNull(LG.isSystem_Generated,'0')='1' And isNull(LG.TDSDesc,'')='' And  "
             + " isNull(LG.TDS_Of_V_SNo,-1)<>-1 Then '' Else LG.Narration End)) As Narration, ";

            if (BlnForDebtors) {

                strSQLQuery += " (Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) > 0 Then "
                 + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End) As Amt1, "
                 + " (Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) < 0 Then "
                 + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End) As Amt2, ";
            } else {

                strSQLQuery += " (Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) < 0 Then "
                 + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End) As Amt1, "
                 + " (Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) > 0 Then "
                 + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End) As Amt2, ";
            }

            strSQLQuery += " isNull(Max(LA_A.Adjustment),0) As Amt, "
                    + " DateDiff(day, '" + strDate + "',Max(LG.V_Date)) As Age_In_Days ,Max(SG.Add1)As Add1, "
                    + " Max(SG.Add2)As Add2,Max(isNull(C.CityName,''))As CityName, Max(isNull(CT.Name,'')) As Country, "
                    + " Max(ST.Name) As SiteName,Max(Ag.GroupName) As AcGroupName," + D1 + " As Days  "
                    + " From Ledger LG  "
                    + " Left Join SubGroup SG On LG.Subcode=SG.SubCode  "
                    + " Left Join City C on SG.CityCode=C.CityCode  "
                    + " Left Join Country CT on C.CountryCode=CT.Code  "
                    + " Left Join AcGroup AG ON SG.GroupCode =AG.GroupCode   "
                    + " Left Join SiteMast ST ON LG.Site_Code =St.code   "
                    + " Left Join ZoneMast ZM ON ZM.Code =SG.Zone  "
                    + " Left Join  "
                    + " ( "
                    + " Select	Sum(LA_A.Adjustment) As Adjustment,LA_A.Adj_DocId,LA_A.Adj_V_SNo "
                    + " From LedgerAdj LA_A   "
                    + " Where LA_A.Vr_SubCode = LA_A.Adj_SubCode And LA_A.Vr_V_Date <= '" + strDate + "' "
                    + " Group By LA_A.Adj_DocId , LA_A.Adj_V_SNo   "
                    + " ) As LA_A On LG.DocId=LA_A.Adj_DocId And LG.V_SNo=LA_A.Adj_V_SNo   "
                    + " Left Join  "
                    + " ( "
                    + " Select	Sum(LA_V.Adjustment) As Adjustment,LA_V.Vr_DocId,LA_V.Vr_V_SNo "
                    + " From LedgerAdj LA_V   "
                    + " Where LA_V.Vr_SubCode = LA_V.Adj_SubCode And LA_V.Vr_V_Date <= '" + strDate + "' "
                    + " Group By LA_V.Vr_DocId , LA_V.Vr_V_SNo   "
                    + " ) As LA_V On LG.DocId=LA_V.Vr_DocId And LG.V_SNo=LA_V.Vr_V_SNo   "
                    + strCondition1
                    + " Group By LG.DocId, "
                    + " (Case When isNull(LG.isSystem_Generated,'0')='1' And isNull(LG.TDSDesc,'')='' And "
                    + " isNull(LG.TDS_Of_V_SNo,-1)<>-1 Then LG.TDS_Of_V_SNo Else LG.V_SNo End) "
                    + " Having (((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) -  "
                    + " isNull(Max(LA_V.Adjustment),0)) + isNull(Max(LA_A.Adjustment),0)) <> 0  "
                    + " ) AS tab1 ORDER BY  ACGroupName,PName  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            psTemp.setDate("asOnDate", DmServer.convertDate(filter.get("asOnDate").getText()), true);

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("AC_BillwiseOutstandingAgeing");
            jasperPrint = DmServer.fillJasper(this, "Ageing Analysis Bill Wise", jasperReport, new HashMap(), filter, rsTemp);
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
