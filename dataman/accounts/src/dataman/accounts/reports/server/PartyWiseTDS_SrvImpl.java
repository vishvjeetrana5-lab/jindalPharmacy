package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.PartyWiseTDS_Model;
import dataman.accounts.reports.shared.PartyWiseTDS_Srv;
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

public class PartyWiseTDS_SrvImpl extends DmRSSPrivate implements PartyWiseTDS_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategoryName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("Select Code,Name From TdsCat ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSeperate(DmHelper helper, boolean blnToEndOfFile) {
         DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHWithNarration(DmHelper helper, boolean blnToEndOfFile) {
         DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Y");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("R");
            listNode.setSharedKeyFieldName("Registered");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("Un Registered");
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
    public DmResultPageing<List<PartyWiseTDS_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<PartyWiseTDS_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<PartyWiseTDS_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        PartyWiseTDS_Model.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode AS CODE, SG.Name AS NAME, Sg.ManualCode, isNull(CT.CityName, '') AS City "
                    + "FROM SubGroup SG "
                    + "  LEFT JOIN City CT ON SG.CityCode = CT.CityCode "
                    + "  LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + "  WHERE  SL.site_Code = :site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new PartyWiseTDS_Model.TxtHAccountName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<PartyWiseTDS_Model.TxtHTDSDeductFrom>> getTxtHTDSDeductFrom(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<PartyWiseTDS_Model.TxtHTDSDeductFrom>> rstTemp = new DmResultPageing<>();
        List<PartyWiseTDS_Model.TxtHTDSDeductFrom> lstTemp = new ArrayList<>();
        PartyWiseTDS_Model.TxtHTDSDeductFrom listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select SG.SubCode,Max(SG.Name) as Name,"
                    + " Max(SG.ManualCode) as ManualCode,isNull(Max(CT.CityName),'') as City "
                    + " From SubGroup SG  Left Join Ledger LG On SG.SubCode=LG.TDSDeductFrom"
                    + " LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + " Left Join City CT On SG.CityCode=CT.CityCode "
                    + " WHERE  SL.site_Code =:site_Code "
                    // + "SG.SiteList LIKE '%|" + DmServer.getSsnSiteCode(this) + "|%' "
                    + " ANd isNull(LG.TDSDeductFrom,'') <> ''  Group by SG.SubCode ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new PartyWiseTDS_Model.TxtHTDSDeductFrom();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
        String strCondition1, strSQLQuery, tempField, strNarr;

        try {
            PartyWiseTDS_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            // StrConditionMain = " Where (V_Date Between :fromDate And :toDate) ";
            strCondition1 = " And (L.V_Date Between :fromDate And :toDate) ";

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And SG.SubCode In (" + DmServer.getParamList(":accountName", DmShared.xNull(filter.get("accountName").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("categoryName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And TC.Code In (" + DmServer.getParamList(":categoryName", DmShared.xNull(filter.get("categoryName").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("tdsDeductFrom").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + "And SG3.SubCode  In (" + DmServer.getParamList(":tdsDeductFrom", DmShared.xNull(filter.get("tdsDeductFrom").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And (Case isNull(SG.PartyType,'') When '' Then 'U' Else SG.PartyType End) In (" + DmServer.getParamList(":partyType", DmShared.xNull(filter.get("partyType").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And L.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And L.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            strSQLQuery = "Select Party,RecID,VType,V_Date,Narration, "
                    + " TSDCat,Description , "
                    + " (Case When V_Sno=TDSVSno Then TdsOnAmt Else 0 End) as TdsOnAmt ,"
                    + " TdsPer,TdsAmt,PostingAc, "
                    + "TDSDeductFrom,PAN,PartyType , VTypeShortName , DocId "
                    + " From ( "
                    + " SELECT L.RecID As RecID ,L.V_Sno,(Select Min(V_Sno) from Ledger LG Where LG.Tds_Of_V_SNo=L.Tds_Of_V_SNo) As TDSVSno, "
                    + "  concat(isNull(SG.Name, ''),' ',isNull(C.CityName, '')) as Party, L.RecID As V_No,L.V_Type as VType,L.V_Date,L.Narration, "
                    + " TC.Name AS TSDCat,TCD.Name AS Description,L.TdsOnAmt,L.TdsPer, "
                    + " (Case When isNull(L.AmtCr,0)>0 Then isNull(L.AmtCr,0) Else 0 - isNull(L.AmtDr,0) END) As TdsAmt, "
                    + " sg2.Name AS PostingAc ,  "
                    + " Case isNull(SG.PartyType,'') When 'R' Then 'Registered' Else 'Un Registered' End As PartyType, "
                    + " (Case When isNull(CAST(SG3.SubCode AS VARCHAR),'') = '' Then SG.Name Else SG3.Name End) AS TDSDeductFrom, "
                    + " L.Tds_Of_V_Sno, "
                    + " (Case When isNull(CAST(SG3.SubCode AS VARCHAR),'') = '' Then SG.PAN Else SG3.PAN End) As PAN , VT.short_Name As VTypeShortName , L.DocId As DocId   "
                    + " FROM Ledger L "
                    + " LEFT JOIN SubGroup SG ON SG.SubCode =L.ContraSub "
                    + " LEFT JOIN TDSCat TC ON TC.Code=L.TDSCategory "
                    + " LEFT JOIN TDSCat_Description  TCD ON TCD.Code=L.TdsDesc "
                    + " LEFT JOIN TdsCat_Det TD ON TD.TdsDesc =TCD.Code AND TD.Code=TC.Code "
                    + " LEFT JOIN SubGroup SG2 ON SG2.SubCode=TD.AcCode "
                    + " LEFT JOIN SubGroup SG3 ON SG3.SubCode=(SELECT Distinct TDSDeductFrom FROM Ledger "
                    + " WHERE DocId=L.Docid AND isNull(CAST(TDSDeductFrom AS VARCHAR),'')<>'' AND V_SNO=L.Tds_Of_V_Sno)  "
                    + " LEFT JOIN City C ON C.CityCode=SG.CityCode "
                    + " LEFT JOIN voucher_Type VT on Vt.v_Type=L.v_Type  "
                    + " WHERE isNull(L.TDSCategory,'')<>'' AND isNull(L.tdsdesc,'')<>'' "
                    + " AND L.isSystem_Generated ='1' "
                    + strCondition1
                    + ") As Temp  order by party " ;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("accountName", DmShared.xNull(filter.get("accountName").getTag()));
            }

            if (!DmShared.xNull(filter.get("categoryName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("categoryName", DmShared.xNull(filter.get("categoryName").getTag()));
            }

            if (!DmShared.xNull(filter.get("tdsDeductFrom").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("tdsDeductFrom", DmShared.xNull(filter.get("tdsDeductFrom").getTag()));
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("siteName", DmShared.xNull(filter.get("siteName").getTag()));
            } else {
                psTemp.setStringList("siteName", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }

            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("partyType", DmShared.xNull(filter.get("partyType").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();
            hm.put("seperatePage", filter.get("seperatePage").getTag().trim().equalsIgnoreCase("Y") ? 1 : 0);
            hm.put("withNarration", filter.get("withNarration").getTag().trim().equalsIgnoreCase("Y") ? 1 : 0);

            jasperReport = DmServer.compileJasper("AC_PartyWiseTDSReport");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().PARTY_WISE_TDS.caption, jasperReport, hm, filter, rsTemp);
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
