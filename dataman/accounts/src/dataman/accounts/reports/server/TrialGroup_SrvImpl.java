package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.TrialGroup_Model;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.TrialGroup_Srv;
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

public class TrialGroup_SrvImpl extends DmRSSPrivate implements TrialGroup_Srv {

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
            sqlWithParam.setSQL("Select Code ,Name From SiteMast ");
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
        String strCondition1, StrConditionSite, StrSQLQuery;

        try {
            TrialGroup_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition1 = " Where isNull(LG.isPosted,0)=1 And LG.V_Date <=:asOnDate ";

            StrConditionSite = "";
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                StrConditionSite = " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                StrConditionSite = " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " And (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') Then LG.V_Prefix Else ";
            strCondition1 += ":voucherPrefix End) = :voucherPrefix ";
            //=================================================================================

            //========== For Detail Section =======
            StrSQLQuery = "Select (Case isNull(CAST(AG1.GroupCode AS VARCHAR),'') When '' Then isNull(CAST(AG.GroupCode AS VARCHAR),'')  "
             + "Else isNull(CAST(AG1.GroupCode AS VARCHAR),'') End)  As GroupCode,  "
             + "Max(Case isNull(AG1.GroupName,'') When '' Then isNull(AG.GroupName,'')  "
             + "Else isNull(AG1.GroupName,'') End)  As GName, "
             + "(Case When (isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0))>0 Then  "
             + "(isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0)) Else 0 End) As AmtDr, "
             + "(Case When (isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0))>0 Then "
             + "(isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0)) Else 0 End) As AmtCr "
             + "From Ledger LG Left Join SubGroup SG On LG.SubCode=SG.SubCode  Left Join "
             + "AcGroup AG On AG.GroupCode=SG.GroupCode Left Join "
             + "AcGroupPath AGP On AGP.GroupCode=AG.GroupCode And AGP.SNo=1 Left Join "
             + "AcGroup AG1 On AG1.GroupCode=AGP.GroupUnder "
             + strCondition1 + StrConditionSite

             + "Group By (Case isNull(CAST(AG1.GroupCode AS VARCHAR),'') When '' Then isNull(CAST(AG.GroupCode AS VARCHAR),'') "
             + "Else isNull(CAST(AG1.GroupCode AS VARCHAR),'') End) "
             + "Having (isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0)) <> 0 "
             + "Order By Max(Case isNull(AG1.GroupName,'') When '' Then isNull(AG.GroupName,'') "
             + "Else isNull(AG1.GroupName,'') End) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, StrSQLQuery);
            psTemp.setDate("asOnDate", DmServer.convertDate(filter.get("asOnDate").getText()), true);
            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("AC_TrialGroup");
            jasperPrint = DmServer.fillJasper(this, "Trial Group", jasperReport, new HashMap(), filter, rsTemp);
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
