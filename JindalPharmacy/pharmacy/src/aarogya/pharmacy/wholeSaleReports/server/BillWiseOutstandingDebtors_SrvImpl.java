package aarogya.pharmacy.wholeSaleReports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.wholeSaleReports.shared.BillWiseOutstandingDebtors_Model;
import aarogya.pharmacy.wholeSaleReports.shared.BillWiseOutstandingDebtors_Srv;
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

public class BillWiseOutstandingDebtors_SrvImpl extends DmRSSPrivate implements BillWiseOutstandingDebtors_Srv {

    @Override
    public DmResultPageing<List<BillWiseOutstandingDebtors_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile, String strReportFor) {
        DmResultPageing<List<BillWiseOutstandingDebtors_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<BillWiseOutstandingDebtors_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        BillWiseOutstandingDebtors_Model.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
//        String strCondition1 = getVarValue("A");
        String strCondition1 = getVarValue(strReportFor);
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode, SG.Name, Sg.ManualCode, isNull(CT.CityName, '') AS City, AG.GroupName "
                    + "FROM SubGroup SG "
                    + "  LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + "  LEFT JOIN City CT ON SG.CityCode = CT.CityCode"
                    + "  LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + "  WHERE  SL.site_Code =:site_Code " + strCondition1);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
//            sqlWithParam.setSQL("Select SG.SubCode,SG.Name,SG.ManualCode,AG.GroupName From SubGroup  SG Left Join   "
//                    + " AcGroup AG On AG.GroupCode=SG.GroupCode   "
//                    + " LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
//                    + " Where 1 = 1   " + strCondition1
//                    + " And SL.site_Code = :site_Code ");
//            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BillWiseOutstandingDebtors_Model.TxtHAccountName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setGroupName(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAccountGroup(DmHelper helper, boolean blnToEndOfFile, String strReportFor) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
//        String strCondition1 = getVarValue("A");
        String strCondition1 = getVarValue(strReportFor);
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Ag.GroupCode,Ag.GroupName FROM AcGroup  AG Where 1 = 1 " + strCondition1 + "  ");
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHZoneName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String strCondition1 = getVarValue("A");
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Zm.Code,Zm.Name From ZoneMast Zm ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportOnChoice(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Summary");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Details");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBalance(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Cummulative Balance");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("B");
            listNode.setSharedKeyFieldName("Bill Wise Balance");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Due Date");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("V");
            listNode.setSharedKeyFieldName("Vr Date");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHHavingBalance(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);
            
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("All");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHShowAdjustmentOf(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("DE");
            listNode.setSharedKeyFieldName("Debit Entry");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("CE");
            listNode.setSharedKeyFieldName("Credit Entry");
            lstTemp.add(listNode);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    public String getVarValue(String strReportFor) {
        //String strReportFor="A";
        String returnVal = "";
        if (strReportFor.equals("A")) {
            returnVal = " And AG.GroupNature = '1' ";
        } else if (strReportFor.equals("L")) {
            returnVal = " And AG.GroupNature = '1' ";
        }

        return returnVal;
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
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String strReportFor) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1 = "", strSQLQuery = "", reportName = "", reportTitle = "";
        try {
            BillWiseOutstandingDebtors_Model.validateInput(filter);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (strReportFor.equalsIgnoreCase("A")) {
                strCondition1 += " And AG.GroupNature = '1' ";
                reportTitle = "Bill Wise Outstanding (Debtors)";
            } else if (strReportFor.equalsIgnoreCase("L")) {
                strCondition1 += " And AG.GroupNature = '1' ";
                reportTitle = "Bill Wise Outstanding (Debtors)";

            }

            if (!DmShared.dNull(filter.get("asOnDate").getText()).trim().equals("")) {
                if (DmShared.xNull(strReportFor).equalsIgnoreCase("D")) {
                    strCondition1 += " And DateAdd(D,SG.DueDays,LG.V_Date) <= '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "' ";
                } else if (strReportFor.equalsIgnoreCase("L")) {
                    strCondition1 += "And LG.V_Date <= '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "' ";

                }
            }
            if (!DmShared.dNull(filter.get("billAfterOn").getText()).trim().equals("")) {
                if (strReportFor.equalsIgnoreCase("L")) {
                    strCondition1 += "And LG.V_Date >= '" + DmServer.convertDate(filter.get("billAfterOn").getText()) + "' ";

                }
            }

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And (isNull(CAST(SG.GroupCode AS VARCHAR),'') In (Select isNull(CAST(AGP.GroupCode AS VARCHAR),'') From AcGroupPath AGP Where AGP.GroupUnder In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) Or SG.GroupCode In ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + " )) ";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.SubCode In ( " + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ") ";
            }

            if (!DmShared.xNull(filter.get("zoneName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And ZM.Code In ( " + DmServer.getParamList(":zoneName", filter.get("zoneName").getTag()) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            strSQLQuery = " Select * From ( "
                    + " Select LG.DocId,   Max(lg.refNo) as partyBillNo,\n"
                    + "   Max(lg.refDate) as partyBillDate,\n"
                    + "DateDiff(D,Max(LG.V_Date),'" + DmServer.convertDate(filter.get("asOnDate").getText()) + "') as IntDays, "
                    + " (Case When isNull(LG.isSystem_Generated,'0')='1' And isNull(LG.TDSDesc,'')='' And "
                    + " isNull(LG.TDS_Of_V_SNo,-1)<>-1 Then LG.TDS_Of_V_SNo Else LG.V_SNo End) As V_SNo, "
                    + " Max(LG.RecID) As VNo,Max(vt.short_Name) As VType, "
                    + " Max(LG.V_Date) As VDate,Max(SG.Name) As PName,CONCAT(MAX(SG.Name) , '/' , MAX(LG.SubCode)) As SubCode, "
                    + " Max((Case When isNull(LG.isSystem_Generated,'0')='1' And isNull(LG.TDSDesc,'')='' And isNull(LG.TDS_Of_V_SNo,-1)<>-1 Then '' Else LG.Narration End)) As Narration, ";

            if (DmShared.xNull(filter.get("showAdjustmentOf").getTag()).equalsIgnoreCase("DE")) {
                strSQLQuery += " (Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) > 0 Then  "
                        + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End) As Amt1, "
                        + " ABS((Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) < 0 Then "
                        + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End)) As Amt2, ";
            } else {
                strSQLQuery += " ABS(Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) < 0 Then  "
                        + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End) As Amt1, "
                        + " ((Case When ((isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0)) > 0 Then "
                        + " (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) - isNull(Max(LA_V.Adjustment),0) Else 0 End)) As Amt2, ";
            }
            strSQLQuery += " isNull(Max(LA_A.Adjustment),0) As Amt, "
                    + " Max(SG.Add1)As Add1,Max(SG.Add2)As Add2,Max(C.CityName)As CityName, "
                    + " Max(CT.Name) As Country,MAx(ST.Name) As SiteName,Max(Ag.GroupName) As AcGroupName, "
                    + "'" + DmShared.xNull(filter.get("reportOnChoice").getTag()) + "' As RepChoice, '" + DmServer.convertDate(filter.get("billAfterOn").getText()) + "' As B_Show_AfterOn, "
                    + " Max(SG.DueDays) As DueDays,"
                    + " DateAdd(D,Max(SG.DueDays),Max(LG.V_Date)) As DueDate,  "
                    + " DateDiff(Day,Max(LG.V_Date),"
                    + "'" + DmServer.convertDate(filter.get("asOnDate").getText()) + "') "
                    + " As DueNo,'" + DmShared.xNull(filter.get("balance").getTag()) + "' As BalanceType  "
                    + " From Ledger LG  "
                    + " Left Join SubGroup SG On LG.Subcode=SG.SubCode  "
                    + " Left Join City C on SG.CityCode=C.CityCode  "
                    + " Left Join Country CT on C.CountryCode=CT.Code  "
                    + " Left Join AcGroup AG ON SG.GroupCode =AG.GroupCode   "
                    + " Left Join SiteMast ST ON LG.Site_Code =St.code   "
                    + " Left Join ZoneMast ZM ON ZM.Code =SG.Zone "
                    + " left join voucher_Type as vt on vt.v_Type=LG.v_Type  "
                    + " Left Join  "
                    + " ( Select	Sum(LA_A.Adjustment) As Adjustment,LA_A.Adj_DocId,LA_A.Adj_V_SNo   "
                    + " From LedgerAdj LA_A   "
                    + " Left Join Ledger LG On LG.DocId = LA_A.Vr_DocId And LG.V_SNo = LA_A.Vr_V_SNo   "
                    + " Where LA_A.Vr_SubCode = LA_A.Adj_SubCode And isNull(LG.isPosted,0) = 1 "
                    + " And Adj_V_Date<= '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "' "
                    + " And Vr_V_Date<= '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "' "
                    + " Group By LA_A.Adj_DocId , LA_A.Adj_V_SNo    "
                    + " ) As LA_A On LG.DocId=LA_A.Adj_DocId And LG.V_SNo=LA_A.Adj_V_SNo  "
                    + " Left Join  "
                    + "( Select	Sum(LA_V.Adjustment) As Adjustment,LA_V.Vr_DocId,LA_V.Vr_V_SNo   "
                    + " From LedgerAdj LA_V   "
                    + " Left Join Ledger LG On LG.DocId = LA_V.Adj_DocId And LG.V_SNo = LA_V.Adj_V_SNo   "
                    + " Where LA_V.Vr_SubCode = LA_V.Adj_SubCode And isNull(LG.isPosted,0) = 1 "
                    + " And Adj_V_Date<= '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "' "
                    + " And Vr_V_Date<= '" + DmServer.convertDate(filter.get("asOnDate").getText()) + "' "
                    + " Group By LA_V.Vr_DocId , LA_V.Vr_V_SNo   "
                    + " ) As LA_V On LG.DocId=LA_V.Vr_DocId And LG.V_SNo=LA_V.Vr_V_SNo   "
                    + " Where isNull(LG.isPosted,0) = 1 " + strCondition1
                    + " Group By LG.DocId, "
                    + " (Case When isNull(LG.isSystem_Generated,'0')='1' And isNull(LG.TDSDesc,'')='' And isNull(LG.TDS_Of_V_SNo,-1)<>-1 Then LG.TDS_Of_V_SNo Else LG.V_SNo End) "
                    + " ) As T "
                    + " Where (isNull(Amt1,0) <> 0 Or isNull(Amt2,0) <> 0 Or isNull(Amt,0) <> 0) ";
            if (DmShared.xNull(filter.get("havingBalance").getTag()).equalsIgnoreCase("N")) {
                strSQLQuery += " And (isNull(Amt1,0) + isNull(Amt2,0) + isNull(Amt,0)) = 0 ";
            } else if (DmShared.xNull(filter.get("havingBalance").getTag()).equalsIgnoreCase("Y")) {
                strSQLQuery += " And (isNull(Amt1,0) + isNull(Amt2,0) + isNull(Amt,0)) <> 0 ";
            }
            strSQLQuery += " Order By AcGroupName,PName,VDate,VType,VNo ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountGroup", DmShared.xNull(filter.get("accountGroup").getTag()), true);
            }

            if (!DmShared.xNull(filter.get("zoneName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("zoneName", DmShared.xNull(filter.get("zoneName").getTag()), true);
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

            if (DmShared.xNull(filter.get("reportOnChoice").getTag()).equalsIgnoreCase("D")) {
                reportName = "AC_BillwiseOutstandingDetailWholeSale";
            } else {
                reportName = "AC_BillwiseOutstandingSummaryWholeSale";
            }

            jasperReport = DmServer.compileJasper(reportName);

            HashMap<String, Object> objHashMap = new HashMap();
            objHashMap.put("dmReportFor", DmShared.xNull(strReportFor));
            objHashMap.put("dmShowAdjustmentOf", DmShared.xNull(filter.get("showAdjustmentOf").getTag()));
            jasperPrint = DmServer.fillJasper(this, reportTitle, jasperReport, objHashMap, filter, rsTemp);
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
