package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.TDSReport_Model;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.TDSReport_Srv;
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

public class TDSReport_SrvImpl extends DmRSSPrivate implements TDSReport_Srv {

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPartyWise(DmHelper helper, boolean blnToEndOfFile) {
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
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("R");
            listNode.setSharedKeyFieldName("Registered");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("Un Registered");
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
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String strCondition1, strSQLQuery, tempField, strNarr;

        try {
            TDSReport_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            strCondition1 = " And (L.V_Date Between :fromDate And :toDate) ";

            if (!DmShared.xNull(filter.get("categoryName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 = strCondition1 + " And TC.Code In (" + DmServer.getParamList(":categoryName", DmShared.xNull(filter.get("categoryName").getTag())) + ") ";
            }

            if (!DmShared.xNull(filter.get("partyType").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += "  And (Case isNull(SG.PartyType,'') When '' Then 'U' Else SG.PartyType End) In (" + DmServer.getParamList(":partyType", DmShared.xNull(filter.get("partyType").getTag())) + ")  ";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And L.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ") ";
            } else {
                strCondition1 += " And L.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ") ";
            }

            strSQLQuery = "Select Party,V_No,VType,V_Date,Narration,TSDCat,Description,TdsAmt,TdsPer,\n"
                    + " (Case When Tmp.V_SNo = Tmp.SUP Then TdsOnAmt Else 0 End) As TdsOnAmt,\n"
                    + " PostingAc,CityName,PWise ,PartyType,PANNo ,VTypeShortName ,DocId\n"
                    + " From (\n"
                    + "      SELECT SG.Name  AS Party,L.RecID As V_No,L.V_Type as VType,L.V_Date,L.Narration,\n"
                    + "      TC.Name AS TSDCat,TCD.Name AS Description,L.TdsOnAmt,L.TdsPer,\n"
                    + "      (Case When isNull(L.AmtCr,0)>0 Then isNull(L.AmtCr,0) Else 0 - isNull(L.AmtDr,0) END) As TdsAmt,\n"
                    + "      SG2.Name AS PostingAc,isNull(C.CityName,'') As CityName\n"
                    + "      ,'" + DmShared.xNull(filter.get("partyWise").getTag()).trim() + "' As PWise ,\n"
                    + "      L.Tds_Of_V_Sno,\n"
                    + "      L.V_SNo,\n"
                    + "      (Select Min(LG.V_SNo) As V_SNo "
                    + "      From Ledger LG\n"
                    + "      Where LG.DocId = L.DocId And LG.Tds_Of_V_Sno = L.Tds_Of_V_Sno) As SUP\n"
                    + "      ,Case isNull(SG.PartyType,'') When 'R' Then 'Registered' Else 'Un Registered' End As PartyType \n"
                    + "      ,isNull(SG.PAN,'') As PANNo  , VT.short_Name As VTypeShortName ,L.DocId  As DocId\n"
                    + "         FROM Ledger L\n"
                    + "         LEFT JOIN SubGroup SG ON SG.SubCode =L.ContraSub\n"
                    + "         LEFT JOIN TDSCat TC ON TC.Code=L.TDSCategory\n"
                    + "         LEFT JOIN TDSCat_Description  TCD ON TCD.Code=L.TdsDesc\n"
                    + "         LEFT JOIN (\n"
                    + "                 select Code,TdsDesc,AcCode\n"
                    + "                 	from TdsCat_Det \n"
                    + "                 	group by Code,TdsDesc,AcCode\n"
                    + "                 ) TD ON TD.TdsDesc =TCD.Code AND TD.Code=TC.Code\n"
                    + "         LEFT JOIN SubGroup SG2 ON SG2.SubCode=TD.AcCode\n"
                    + "         LEFT JOIN City C ON C.CityCode=SG.CityCode\n"
                    + "         LEFT JOIN voucher_Type VT on Vt.v_Type=L.v_Type\n"
                    + "         WHERE isNull(L.TDSCategory,'')<>'' AND isNull(L.tdsdesc,'')<>''\n"
                    + "         AND L.isSystem_Generated ='1'\n"
                    + "         "+strCondition1
                    + "         )As Tmp\n"
                    + " ORDER BY TSDCat,Party, V_Date   ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("categoryName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("categoryName", DmShared.xNull(filter.get("categoryName").getTag()));
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

            jasperReport = DmServer.compileJasper("AC_TDSCategoryWiseReport");
            jasperPrint = DmServer.fillJasper(this, new lmAccounts().TDS_REPORT.caption, jasperReport, hm, filter, rsTemp);

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
}
