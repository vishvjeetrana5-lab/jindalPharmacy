package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.accounts.reports.shared.TrialDetailDrCrExcel_Model;
import dataman.accounts.reports.shared.TrialDetailDrCrExcel_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import java.sql.Connection;
import java.sql.ResultSet;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.accounts.self.shared.AccountsShared;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import java.util.Set;
import java.util.UUID;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.UnderlineStyle;
import jxl.write.Label;

public class TrialDetailDrCrExcel_SrvImpl extends DmRSSPrivate implements TrialDetailDrCrExcel_Srv {

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
            sqlWithParam.setSQL("SELECT AG.GroupCode,AG.GroupName FROM AcGroup AG ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportFor(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("B");
            listNode.setSharedKeyFieldName("Both");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("C");
            listNode.setSharedKeyFieldName("Credit");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Debit");
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

//        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
//        List<KeyFieldExtended> lstTemp = new ArrayList<>();
//        KeyFieldExtended listNode;
//
//        try {
//            rstTemp.setData(lstTemp);
//
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("B");
//            listNode.setSharedKeyFieldName("Both");
//            lstTemp.add(listNode);
//
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("C");
//            listNode.setSharedKeyFieldName("Credit");
//            lstTemp.add(listNode);
//
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("D");
//            listNode.setSharedKeyFieldName("Debit");
//            lstTemp.add(listNode);
//
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
//            rstTemp.setStatus(true);
//
//        } catch (Exception e) {
//            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        }
//        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIncludeOpening(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<TrialDetailDrCrExcel_Model.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TrialDetailDrCrExcel_Model.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<TrialDetailDrCrExcel_Model.TxtHAccountName> lstTemp = new ArrayList<>();
        TrialDetailDrCrExcel_Model.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode, SG.Name, Sg.ManualCode, isNull(CT.CityName, '') AS City, AG.GroupName "
                    + "FROM SubGroup SG "
                    + "  LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + "  LEFT JOIN City CT ON SG.CityCode = CT.CityCode "
                    + "  LEFT JOIN subgroupsitelist SL ON SG.subcode = SL.subcode  "
                    + "  WHERE  SL.site_Code = :site_Code ");
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TrialDetailDrCrExcel_Model.TxtHAccountName();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("City")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHZeroBalance(DmHelper helper, boolean blnToEndOfFile) {

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

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("All");
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

//        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
//        List<KeyFieldExtended> lstTemp = new ArrayList<>();
//        KeyFieldExtended listNode;
//
//        try {
//            rstTemp.setData(lstTemp);
//
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("Y");
//            listNode.setSharedKeyFieldName("Yes");
//            lstTemp.add(listNode);
//
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("N");
//            listNode.setSharedKeyFieldName("No");
//            lstTemp.add(listNode);
//
//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("A");
//            listNode.setSharedKeyFieldName("All");
//            lstTemp.add(listNode);
//
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
//            rstTemp.setStatus(true);
//
//        } catch (Exception e) {
//            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        }
//        return rstTemp;
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
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {

        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String filePath = "";
        String fileName = "Trial Detail_Dr_Cr_Excel";//widgetName.replaceAll(" ", "");
        int rowIndex = 0;
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        String strCondition1, strSQLQuery, strConditionZeroBal = "", strConditionOP = "", strConditionsite;
        try {
            TrialDetailDrCrExcel_Model.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(widgetName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + File.separatorChar + sessionId;
            String serverFileName = AccountsShared.getValidFileName(fileName) + ".xls";
            String fullFilePath = directoryPath + File.separatorChar + serverFileName;

            File sFile = new File(directoryPath);
            if (!sFile.exists()) {
                sFile.mkdirs();
            }
            File nFile = new File(fullFilePath);
            if (nFile.exists()) {
                nFile.delete();
            }
            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            filePath = DmBaseServer.Path.sessions + "/" + getLocalRequest().getSession().getId().trim();
            fileName = UUID.randomUUID().toString() + ".xls";
            DmXlsWriter xlsWriter = new DmXlsWriter(directoryPath, serverFileName);
            xlsWriter = xlsWriter.setWorkSheetCreationCallback(new DmXlsWriter.IXlsWriterWorkSheetCreationCallback() {
                @Override
                public boolean onWorkSheetCreating(DmXlsSheet workSheet) {
                    return true;
                }

                @Override
                public String getDefaultFontName() {
                    return "Arial";
                }

                @Override
                public int getDefaultFontSize() {
                    return 10;
                }

                @Override
                public Alignment getDefaultHorizontalAlignment() {
                    return Alignment.LEFT;
                }

                @Override
                public VerticalAlignment getDefaultVerticalAlignment() {
                    return VerticalAlignment.CENTRE;
                }
            }).build(new lmAccounts().TRIAL_DETAIL_DR_CR.caption);

            DmXlsSheet workSheet = xlsWriter.getDefaultWorkSheet();
            workSheet.setDefaultFontName(fileName);
            workSheet.getWorkSheet().getSettings().setShowGridLines(false);

            strCondition1 = " WHERE isNull(LG.isPosted,0) = 1 AND (LG.V_Date BETWEEN :fromDate AND :toDate ) \n";
            //========== For Picking Only Current Year Data Of Expense / Revenue ==============
            strCondition1 += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix\n";

            strConditionOP = " WHERE isNull(LG.isPosted,0) = 1 AND LG.V_Date < :fromDate \n";
            strConditionOP += " AND (Case When isNull(AG.GroupNature,'') In ('2','3','4','5') THEN LG.V_Prefix ELSE :voucherPrefix END) = :voucherPrefix\n";

            strConditionsite = "";

            if (!DmShared.xNull(filter.get("accountGroup").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '') \n "
                        + "     FROM AcGroupPath AGP \n"
                        + "     WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + ")) \n"
                        + "     OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))\n";

                strConditionOP += " AND (isNull(CAST(SG.GroupCode AS VARCHAR), '') IN (SELECT isNull(CAST(AGP.GroupCode AS VARCHAR), '')\n "
                        + "     FROM AcGroupPath AGP \n"
                        + "     WHERE AGP.GroupUnder IN ( " + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))\n "
                        + "     OR SG.GroupCode IN (" + DmServer.getParamList(":accountGroup", filter.get("accountGroup").getTag()) + "))\n";
            }

            if (!DmShared.xNull(filter.get("accountName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")\n";
                strConditionOP += " AND LG.SubCode IN (" + DmServer.getParamList(":accountName", filter.get("accountName").getTag()) + ")\n";
            }

            if (!DmShared.xNull(filter.get("siteName").getTag()).trim().equalsIgnoreCase("")) {
                strCondition1 += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ")\n";
                strConditionOP += " And LG.Site_Code In (" + DmServer.getParamList(":siteName", DmShared.xNull(filter.get("siteName").getTag())) + ")\n";
            } else {
                strCondition1 += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ")\n";
                strConditionOP += " And LG.Site_Code In  (" + DmServer.getParamList(":siteName", DmShared.xNull(DmServer.getSsnSiteList(this))) + ")\n";
            }

            if (DmShared.xNull(filter.get("reportFor").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("reportFor").setTag("B");
            }

            if (DmShared.xNull(filter.get("includeOpening").getTag()).trim().equalsIgnoreCase("")) {
                filter.get("includeOpening").setTag("Y");
            }

            if (DmShared.xNull(filter.get("zeroBalance").getTag().trim()).equals("Y")) {
                strConditionZeroBal = " Having ((isNull(Sum(OPBalDr),0) + isNull(Sum(AmtDr),0)) - (isNull(Sum(OPBalCr),0) + isNull(Sum(AmtCr),0))) = 0\n";
            } else if (DmShared.xNull(filter.get("zeroBalance").getTag().trim()).equals("N")) {
                strConditionZeroBal = " Having ((isNull(Sum(OPBalDr),0) + isNull(Sum(AmtDr),0)) - (isNull(Sum(OPBalCr),0) + isNull(Sum(AmtCr),0))) <> 0\n";
            }

            strSQLQuery = "  Select t.GroupName ,t.AcName [Ac Name],t.OpeningDr as [Opening Dr],t.OpeningCr as [Opening Cr],t.AmtDr [Amt Dr],t.AmtCr [Amt Cr],\n"
                    + "  case when t.calulatedValue >0 then cast(abs(t.calulatedValue) as numeric(18,2)) else 0 end as [Closing Dr],\n"
                    + "  case when t.calulatedValue <0 then cast(abs(t.calulatedValue) as numeric(18,2)) else 0 end as [Closing Cr]\n"
                    + " from("
                    + "Select Max(GroupName) AS GroupName,Max(SName) As AcName\n"
                    + " , cast(isNull(Sum(OPBalDr),0) as numeric(18,2)) As OpeningDr\n"
                    + " ,cast(isNull(Sum(OPBalCr),0) as numeric(18,2)) As OpeningCr\n"
                    + " ,cast(isNull(Sum(AmtDr),0) as numeric(18,2)) As AmtDr\n"
                    + " , cast(isNull(Sum(AmtCr),0) as numeric(18,2)) As AmtCr\n"
                    + " , sum(case when (OPBalDr-OPBalCr+AmtDr-AmtCr)>0 then cast(abs(OPBalDr-OPBalCr+AmtDr-AmtCr) as numeric(18,2)) else 0 end) as [Closing Dr]\n"
                    + " , sum(case when (OPBalDr-OPBalCr+AmtDr-AmtCr)<0 then cast(abs(OPBalDr-OPBalCr+AmtDr-AmtCr) as numeric(18,2)) else 0 end) as [Closing Cr]\n"
                    + ",cast((Sum(OPBalDr)-Sum(OPBalCr)+Sum(AmtDr)-Sum(AmtCr)) as numeric(18,2)) as calulatedValue\n"
                    + "     From\n"
                    + "     ( ";

            if (DmShared.xNull(filter.get("includeOpening").getTag().trim()).equals("Y")) {

                strSQLQuery += "    Select Max(isNull(AG.GroupName,'')) AS GroupName,isNull(CAST(AG.GroupCode AS VARCHAR),'') As GroupCode,\n"
                        + "         isNull(CAST(SG.SubCode AS VARCHAR),'') As SubCode, Max(isNull(SG.Name,'')) As SName,\n"
                        + "         (Case When (isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0))>0 Then\n"
                        + "         (isNull(Sum(LG.AmtDr),0)-isNull(Sum(LG.AmtCr),0)) Else 0 End) As OPBalDr,\n"
                        + "         (Case When (isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0))>0 Then\n"
                        + "         (isNull(Sum(LG.AmtCr),0)-isNull(Sum(LG.AmtDr),0)) Else 0 End) As OPBalCr,0 As AmtDr,0 As AmtCr\n"
                        + "             From Ledger LG Left Join SubGroup SG On LG.SubCode=SG.SubCode\n"
                        + "             LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode\n"
                        + "             " + strConditionOP + " " + strConditionsite + "\n"
                        + "         Group By isNull(CAST(AG.GroupCode AS VARCHAR),''),isNull(CAST(SG.SubCode AS VARCHAR),'')\n"
                        + "         Union All\n";

            }

            strSQLQuery += "    Select	Max(isNull(AG.GroupName,'')) AS GroupName,isNull(CAST(AG.GroupCode AS VARCHAR),'') As GroupCode,\n"
                    + "         isNull(CAST(SG.SubCode AS VARCHAR),'') As SubCode,\n"
                    + "         Max(isNull(SG.Name,'')) As SName, 0 As OPBalDr,0 As OPBalCr,\n";

            if (!DmShared.xNull(filter.get("reportFor").getTag().trim()).equals("C")) {
                strSQLQuery += "        isNull(Sum(LG.AmtDr),0) As AmtDr, ";
            } else {
                strSQLQuery += "        0 As AmtDr, ";
            }
            if (!DmShared.xNull(filter.get("reportFor").getTag().trim()).equals("D")) {
                strSQLQuery += "        isNull(Sum(LG.AmtCr),0) As AmtCr  ";
            } else {
                strSQLQuery += "        0 As AmtCr, ";
            }
            strSQLQuery += "        From Ledger LG Left Join SubGroup SG On LG.SubCode=SG.SubCode\n"
                    + "             LEFT JOIN AcGroup AG ON AG.GroupCode=SG.GroupCode\n"
                    + "             " + strCondition1 + " " + strConditionsite + "\n"
                    + "             Group By isNull(CAST(AG.GroupCode AS VARCHAR),''),isNull(CAST(SG.SubCode AS VARCHAR),'')\n"
                    + " ) As Tmp\n"
                    + " Group By GroupCode,SubCode\n"
                    + strConditionZeroBal + "\n"
                    //                  + " Order By Max(GroupName),Max(SName)"
                    + ")\n"
                    + "as t\n"
                    + "Order By t.GroupName,t.AcName";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQLQuery);

            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

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
            psTemp.setString("voucherPrefix", DmShared.xNull(DmServer.getSsnVoucherPrefix(this)), true);
            rsTemp = psTemp.executeQuery();

            //===========================================<Creating Excel>=========================================================
            rowIndex = createXLHeader(workSheet, rowIndex, rsTemp, filter);
            rowIndex += 5;
            rowIndex = createColumnHeader(workSheet, rowIndex, rsTemp);
            rowIndex = createDataRow(workSheet, rowIndex, rsTemp);

            //===========================================<Printed By On>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, 11, rowIndex);

            cellFormat = setFontGeneral(11, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);

            lblHeader = new Label(1, rowIndex, "", cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            //===========================================<END OF REPORT>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, 11, rowIndex);

            cellFormat = setFontGeneral(11, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);

            lblHeader = new Label(1, rowIndex, "", cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

//                DmResultSetToExcelWriter xlsRsTemp = new DmResultSetToExcelWriter(rsTemp, directoryPath, serverFileName)
//                        .setDefaultFontName("Tahoma")
//                        .setDefaultFontSize(11)
//                        .setDefaultHorizontalAlignment(Alignment.LEFT)
//                        .setDefaultVerticalAlignment(VerticalAlignment.TOP);
//                                xlsRsTemp.setWorkSheetCallback(new DmResultSetToExcelWriter.IWorksheetCallback() {
//                    @Override
//                    public void onPreWorksheetWriting(DmXlsSheet workSheet) throws Exception {
//
//                    }
//
//                    @Override
//                    public void configureWorksheetColumns(List<DmXlsColumn> column) throws Exception {
//                        Iterator<DmXlsColumn> i = column.iterator();
//                    
//                       
//
//                    }
//
//                    @Override
//                    public void onPostWorksheetWritten(DmXlsSheet workSheet) throws Exception {
//
//                    }
//                });
//                xlsRsTemp.setRecordColumnCallback(new DmResultSetToExcelWriter.IWorksheetRecordColumnCallback() {
//                    @Override
//                    public String configureNumericCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, Number value) throws Exception {
//                        cellFormat.setAlignment(Alignment.RIGHT);
////                        if (DmShared.xNull(column.getColumnName()).equalsIgnoreCase("firstGroupCode")
////                                || DmShared.xNull(column.getColumnName()).equalsIgnoreCase("secondGroupCode")
////                                || DmShared.xNull(column.getColumnName()).equalsIgnoreCase("regDocId")) {
////                            return "";
////                        }
//                       return DmServer.vNull(value, "00");
//                    }
//
//                    @Override
//                    public String configureTextCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, String text) throws Exception {
//                        return text;
//                    }
//
//                    @Override
//                    public String configureDateCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, java.util.Date value) throws Exception {
//                        return DmShared.dNull(value);
//                    }
//
//                    @Override
//                    public String configureTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, java.util.Date value) throws Exception {
//                        return DmServer.vNull(value, "00.00");
//                    }
//
//                    @Override
//                    public String configureDateTimeCell(DmXlsColumn column, WritableCellFormat cellFormat, WritableFont cellFont, java.util.Date value) throws Exception {
//                        return "";
//                    }
//
//                });
//                xlsRsTemp.writeWorkBook(STServer.setXLSFileHeader(fileName, xlsRsTemp, widgetName, PageOrientation.LANDSCAPE, PaperSize.LEGAL, filter, this), 2);
//
//                xlsRsTemp.commitChanges();
            xlsWriter.commit();
            xlsWriter.close();

            rstTemp.setData(keyField);
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

    private int createXLHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp, LinkedHashMap<String, FieldUDT_Mdl> filter) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        String filterString = "";
        int colCount = 0;
        try {
            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            colCount = rsTemp.getMetaData().getColumnCount() + 1;
            //===========================================<HOSPITAL SITE  Name> =========================================================
//            rowIndex = 0;
            workSheet.mergeCells(0, rowIndex, colCount, rowIndex);
            workSheet.getWorkSheet().setRowView(rowIndex, 300);

            //============================================< set column width>================================================================
            workSheet.getWorkSheet().setColumnView(0, 3);
            workSheet.getWorkSheet().setColumnView(1, 40);
//            workSheet.getWorkSheet().setColumnView(2, 11);
//            workSheet.getWorkSheet().setColumnView(3, 11);
//            workSheet.getWorkSheet().setColumnView(4, 11);

            lblHeader = new Label(0, rowIndex, DmServer.getSsnSiteDisplayName(this), cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            //===========================================<REPORT NAME>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex);

//            cellFormat = setFontGeneral(11, true, Alignment.LEFT, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            lblHeader = new Label(1, rowIndex, new lmAccounts().DETAIL_TRIAL_BALANCE_DISPLAY.caption, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);
            //===========================================<Filters>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex + 4);

            if (filter != null) {
                Set<String> keySet = filter.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        FieldUDT_Mdl filter_Mdl = filter.get(key);
                        if (filter_Mdl != null) {
                            if (!DmShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
                                filterString += DmShared.xNull(filter_Mdl.getFieldCaption()) + ":" + DmShared.xNull(filter_Mdl.getText()) + "; ";
                            }
                        }
                    }
                }
            }

            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
            setBorder(cellFormat, false, false, false, false);
            lblHeader = new Label(1, rowIndex, filterString, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private int createColumnHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        try {

            rowIndex++;
            for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                String colName = rsTemp.getMetaData().getColumnName(colIndex);
                int collen = colName.length();
//                if (colName.equalsIgnoreCase("consultant") || colName.equalsIgnoreCase("total")) {
//                    cellFormat = setFontGeneral(true, Alignment.LEFT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
//                } else if (colName.equalsIgnoreCase("total")) {
//                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
//                } else
                {
                    workSheet.getWorkSheet().setColumnView(colIndex, 6 + collen);
                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                }

                setBorder(cellFormat, false, false, true, false);

                lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(colName), cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private void setBorder(WritableCellFormat cellFormat, boolean isTop, boolean isRight, boolean isBottom, boolean isLeft) {
        try {
            if (isBottom) {
                cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            }
            if (isTop) {
                cellFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
            }
            if (isLeft) {
                cellFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
            }
            if (isRight) {
                cellFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

    }

    private int createDataRow(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        jxl.write.Number number;
        jxl.write.DateTime date;
        try {
            while (rsTemp.next()) {

                rowIndex++;
                for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                    String coldata = rsTemp.getString(colIndex);//float//date//varchar
                    cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                    setBorder(cellFormat, false, false, false, false);

                    if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("float")) {

                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } //                    else if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("date")) {
                    //                        date = new jxl.write.DateTime(colIndex++, rowIndex, DmServer.convertDate(DmShared.dNull(coldata)), cellFormat);
                    //                        workSheet.getWorkSheet().addCell(date);
                    //                    }
                    else {
                        lblHeader = new Label(colIndex++, rowIndex, (DmShared.xNull(coldata)), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);
                    }
                    /*
                    if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("consultant")) {

                        cellFormat = setFontGeneral(false, Alignment.LEFT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        setBorder(cellFormat, false, true, false, true);
                        lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);

                    } else if (rsTemp.getMetaData().getColumnName(colIndex).equalsIgnoreCase("Total")) {
                        cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

                        setBorder(cellFormat, false, true, false, false);
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);

                    } else {
                        cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                        setBorder(cellFormat, false, false, false, false);
                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);

                    }
                     */
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    public static WritableCellFormat setFontGeneral(int fontSize, boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

    public static WritableCellFormat setFontGeneral(boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

}
