package dataman.accounts.voucherEntry.server;

import dataman.accounts.chequePrintSetup.shared.ChequePrintSetup_Mdl;
import dataman.accounts.voucherEntry.client.VoucherEntry;
import dataman.dmBase.self.shared.*;
import dataman.accounts.voucherEntry.shared.*;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmJasperCompiler;
import dataman.dmToolkit.accountAdjustment.server.AccountAdjustment_Server;
import dataman.dmToolkit.accountAdjustment.shared.*;
import dataman.dmToolkit.itemAdjustment.server.ItemAdjustment_SrvImpl;
import dataman.dmToolkit.itemAdjustment.shared.*;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRBreak;
import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JREllipse;
import net.sf.jasperreports.engine.JRFrame;
import net.sf.jasperreports.engine.JRGenericElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRTextField;
import net.sf.jasperreports.engine.JRVisitor;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRElementsVisitor;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class VoucherEntry_SrvImpl extends DmRSSPrivate implements VoucherEntry_Srv {

    private LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    private String voucherTypefieldName = "lm.V_Type", voucherDateFieldName = "lm.V_Date";

    //<editor-fold defaultstate="collapsed" desc="TextHelps">
    @Override
    public DmResultPageing<List<TxtHVoucherType_Mdl>> getTxtHVoucherType(String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHVoucherType_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHVoucherType_Mdl> lstTemp = new ArrayList<>();
        TxtHVoucherType_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";

        try {
            rstTemp.setData(lstTemp);
            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.JOURNAL)) {
                condition += " AND vt.category IN ('" + lvc.JOURNAL + "')";
            } else {
                condition += " AND vt.category = '" + voucherCategory + "'";
            }
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT VT.V_Type,VT.Description,VT.DefaultAc,SG.Name AS DAName,SG.Nature,VT.Category,sg.manualCode as defaultAcManualCode "
                    + " FROM Voucher_Type VT "
                    + " LEFT JOIN SubGroup SG ON VT.DefaultAc=SG.SubCode "
                    + " WHERE VT.NCat = 'FA' " + condition;
            DmSQLWithParam sqlWithParam = new DmSQLWithParam();
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHVoucherType_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_Type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setDefaultAc(DmShared.xNull(rsTemp.getString("DefaultAc")));
                listNode.setDefaultAcManualCode(DmShared.xNull(rsTemp.getString("defaultAcManualCode")));
                listNode.setDAName(DmShared.xNull(rsTemp.getString("DAName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("Category")));
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
        String sqlQuery, StrCCenter;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        //ravi//  If Not BlnInsert Then StrSQL += "And isNull(CCM.Site_Code,'" & LIPublic.StrPubSiteCode & "') = '" & LIPublic.StrPubSiteCode & "' "
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            StrCCenter = "";
            if (!DmServer.getSsnCostCenterList(this).equalsIgnoreCase("")) {
                StrCCenter = "And Code In (" + DmServer.getSsnCostCenterList(this) + ")";
            }

            sqlQuery = "Select Code,Name,ManualCode,Site_Code "
                    + "From CostCenterMast "
                    + "Where isNull(UnderCostCenter,'') = '' And isActive = 1 " + StrCCenter + "";

            sqlWithParam.setSQL(sqlQuery);
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
    public DmResultPageing<List<TxtHCostCenterGrid_Mdl>> getTxtHCostCenterGrid(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCostCenterGrid_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCostCenterGrid_Mdl> lstTemp = new ArrayList<>();
        TxtHCostCenterGrid_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, StrCCenter;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        //ravi//  If Not BlnInsert Then StrSQL += "And isNull(CCM.Site_Code,'" & LIPublic.StrPubSiteCode & "') = '" & LIPublic.StrPubSiteCode & "' "
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            StrCCenter = "";
            if (!DmServer.getSsnCostCenterList(this).equalsIgnoreCase("")) {
                StrCCenter = "And Code In (" + DmServer.getSsnCostCenterList(this) + ")";
            }

            sqlQuery = "Select Code,Name,ManualCode,Site_Code "
                    + "From CostCenterMast  "
                    + "Where isNull(UnderCostCenter,'') = '' And isActive = 1 " + StrCCenter + " ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCostCenterGrid_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setSiteCode(DmShared.xNull(rsTemp.getString("Site_Code")));
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

    public DmResultPageing<List<TxtHAccountName_Mdl>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile, String v_Type) {
        DmResultPageing<List<TxtHAccountName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountName_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , c.cityName"
                    + " , sg.manualCode"
                    + " , sgn.name AS nature"
                    + " , sgn.manualCode AS natureManualCode"
                    + " , sg.tds_Catg AS tdsCode"
                    + " , tc.Name AS tdsName"
                    + "	FROM subGroup sg"
                    + "	LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupSiteList sgl ON sg.subCode = sgl.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sg.nature = sgn.code"
                    + " LEFT JOIN tdsCat tc ON sg.tds_Catg = tc.code "
                    + "	WHERE sgl.site_Code = :site_Code"
                    + " AND sgn.code IN (SELECT nature FROM acFilteration WHERE v_Type = :v_Type)"
                    + "	AND sg.isActive = 1";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("nature")));
                listNode.setNatureManualCode(DmShared.xNull(rsTemp.getString("natureManualCode")));
                listNode.setTdsCatCode(DmShared.xNull(rsTemp.getString("tdsCode")));
                listNode.setTdsCatName(DmShared.xNull(rsTemp.getString("tdsName")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResultPageing<List<TxtHAccountNameGrid_Mdl>> getTxtHAccountNameGrid(DmHelper helper, boolean blnToEndOfFile, String widgetName) {
        DmResultPageing<List<TxtHAccountNameGrid_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountNameGrid_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountNameGrid_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (widgetName.equalsIgnoreCase(new lmAccounts().CONTRA_VOUCHER.caption)) {
                condition += " AND sgn.manualCode in ('BK','CS') ";
            } else {
                if (!widgetName.equalsIgnoreCase(new lmAccounts().JOURNAL_VOUCHER.caption)) {
                    condition += " AND sgn.manualCode not in ('BK','CS') ";
                }
            }

            sqlQuery = "SELECT"
                    + "   SG.subCode"
                    + " , SG.name"
                    + " , SG.manualCode"
                    + " , isNull(CT.CityName,'') AS CityName"
                    + " , SG.Nature"
                    + " , SG.Pan"
                    + " , SG.Tan"
                    + " , CCM.Name AS CCName"
                    + " , SG.CostCenter"
                    + " , AG.GroupName"
                    + " , sg.tds_Catg as tdsCode"
                    + " , isNull(SG.DisplayName,SG.Name) AS DisplayName"
                    + " , sgn.Name AS SGNature"
                    + " , sgn.manualCode AS natureManualCode"
                    + " , tc.Name AS tdsName"
                    + " , tc.tdsFor AS tdsFor"
                    + " , bd2.accountNo"
                    + " , bd2.bank"
                    + " , bd2.branch"
                    + " , bd2.swiftCode"
                    + " , bd2.branchAddress"
                    + " , bd2.ifscCode"
                    + " , bd2.isDefault"
                    + " FROM SubGroup SG "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + " LEFT JOIN CostCenterMast CCM ON CCM.Code = SG.CostCenter "
                    + " LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + " LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code "
                    + " LEFT JOIN subgroupsitelist SGL ON SG.subCode = SGL.subCode "
                    + " LEFT JOIN tdsCat tc on sg.tds_Catg = tc.code "
                    + " LEFT JOIN ( "
                    + "     SELECT bd.subCode, bd.accountNo,bd.bankName as bank, "
                    + "	    bd.branch,bd.swiftCode,bd.branchAddress,bd.ifscCode,bd.isDefault "
                    + "	    FROM bankDetail bd 	             "
                    + "	    WHERE bd.isDefault = 1  "
                    + "	) bd2 ON bd2.subCode = sg.subCode "
                    + " WHERE SGL.site_Code = '" + DmServer.getSsnSiteCode(this) + "'"
                    + " AND SG.isActive = 1 " + condition;

            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountNameGrid_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setDisplayName(DmShared.xNull(rsTemp.getString("DisplayName")));
                listNode.setSCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
                listNode.setNature(DmShared.xNull(rsTemp.getString("Nature")));
                listNode.setCostCenter(DmShared.xNull(rsTemp.getString("CostCenter")));
                listNode.setGroup(DmShared.xNull(rsTemp.getString("GroupName")));
                listNode.setNatureManualCode(DmShared.xNull(rsTemp.getString("natureManualCode")));
                listNode.setTdsCatCode(DmShared.xNull(rsTemp.getString("tdsCode")));
                listNode.setTdsCatName(DmShared.xNull(rsTemp.getString("tdsName")));
                listNode.setTdsForCode(DmShared.xNull(rsTemp.getString("tdsFor")));
                listNode.setTdsForName(DmShared.xNull(rsTemp.getString("tdsFor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("tdsFor")).equalsIgnoreCase("R") ? "Receivable" : "");
                listNode.setBank(DmShared.xNull(rsTemp.getString("bank")));
                listNode.setBankAddress(DmShared.xNull(rsTemp.getString("branchAddress")));
                listNode.setIfscCode(DmShared.xNull(rsTemp.getString("ifscCode")));
                listNode.setSwiftCode(DmShared.xNull(rsTemp.getString("swiftCode")));
                listNode.setBranch(DmShared.xNull(rsTemp.getString("branch")));
                listNode.setIsDefault(DmShared.iNull(rsTemp.getString("isDefault")).intValue() == 1);
                listNode.setAccountNo(DmShared.xNull(rsTemp.getString("accountNo")));
                listNode.setPan(DmShared.xNull(rsTemp.getString("pan")));
                listNode.setTan(DmShared.xNull(rsTemp.getString("tan")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsCategory(String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select Code, Name,tdsfor From TDSCat ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("tdsfor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("tdsfor")).equalsIgnoreCase("R") ? "Receivable" : "");
                lstTemp.add(listNode);
            }

            Iterator<KeyFieldExtended> i = lstTemp.iterator();

            while (i.hasNext()) {
                KeyFieldExtended e = i.next();
                if (DmShared.xNull(e.getSharedModifiedBy()).equalsIgnoreCase("Receivable") && DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_PAYMENT)) {
                    i.remove();
                } else if (DmShared.xNull(e.getSharedModifiedBy()).equalsIgnoreCase("Payable") && DmShared.xNull(voucherCategory).equalsIgnoreCase(new LibraryVoucherCategory().BANK_RECEIPT)) {
                    i.remove();
                }
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
    public DmResultPageing<List<TxtHTdsDeduct_Mdl>> getTxtHTdsDeduct(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTdsDeduct_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTdsDeduct_Mdl> lstTemp = new ArrayList<>();
        TxtHTdsDeduct_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        //ravi//  If Not BlnInsert Then StrSQL += "And isNull(CCM.Site_Code,'" & LIPublic.StrPubSiteCode & "') = '" & LIPublic.StrPubSiteCode & "' "
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select SG.SubCode,SG.Name,isNull(CT.CityName,'') as CityName,SG.ManualCode From"
                    + " SubGroup SG Left Join City CT On CT.CityCode=SG.CityCode"
                    + " LEFT JOIN subgroupsitelist SGL ON SG.subCode=SGL.subCode "
                    + " Where SGL.site_Code='" + DmServer.getSsnSiteCode(this) + "' ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTdsDeduct_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SubCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setSCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTdsFor(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Payable");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("R");
            listNode.setSharedKeyFieldName("Receivable");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDrCr(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Dr");
            listNode.setSharedKeyFieldName("Dr");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Cr");
            listNode.setSharedKeyFieldName("Cr");
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
    public DmResultPageing<List<VoucherTypePrint_Mdl>> getTxtHVoucherTypePrint(String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<VoucherTypePrint_Mdl>> rstTemp = new DmResultPageing<>();
        List<VoucherTypePrint_Mdl> lstTemp = new ArrayList<>();
        VoucherTypePrint_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT VT.V_Type As Code,VT.Description,VT.Category "
                    + " FROM Voucher_Type VT "
                    + " WHERE VT.NCat = 'FA' "
                    + " AND VT.Category = '" + voucherCategory + "' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new VoucherTypePrint_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
                listNode.setCategory(DmShared.xNull(rsTemp.getString("Category")));
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
    public DmResultPageing<List<VoucherEntryPrint_Mdl>> getTxtHAccountNamePrint(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<VoucherEntryPrint_Mdl>> rstTemp = new DmResultPageing<>();
        List<VoucherEntryPrint_Mdl> lstTemp = new ArrayList<>();
        VoucherEntryPrint_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode AS Code,SG.Name,SG.ManualCode,isNull(CT.CityName,'') as CityName,AG.GroupName "
                    + " FROM SubGroup SG "
                    + " LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + " LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + " LEFT JOIN subgroupsitelist AS sgsl ON sg.subCode = sgsl.subCode "
                    + " WHERE sgsl.site_Code = :site_Code AND "
                    + " SG.isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new VoucherEntryPrint_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("CityName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherNoPrint(DmHelper helper, boolean blnToEndOfFile, String txtFromDate, String txtToDate, String txtHVoucherType, String txtHAccountName) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String StrSQL = "", SiteList = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            SiteList = DmServer.getSsnSiteCode(this);

            StrSQL = " Select LG.DocId as Code,LG.RecId as Name";
            StrSQL += " From LedgerM LG ";
            if (!DmShared.xNull(txtHAccountName).equalsIgnoreCase("")) {
                StrSQL += " Inner Join ( ";
                StrSQL += "     Select L.DocId ";
                StrSQL += "     From Ledger L ";
                StrSQL += "     Where L.SubCode In (" + DmServer.getParamList(":accountName", DmShared.xNull(txtHAccountName)) + ")";
                StrSQL += "     And L.Site_Code = :siteList ";
                StrSQL += "     And L.V_Type = :voucherType ";
                StrSQL += "     And L.V_Date >= :fromDate And L.V_Date <= :toDate ";
                StrSQL += "     Group By L.Docid ";
                StrSQL += " ) vL On vL.Docid = Lg.Docid";
            }
            StrSQL += " Where LG.Site_Code =:siteList And ";
            StrSQL += " LG.V_Type =:voucherType And ";
            StrSQL += " LG.V_Date >=:fromDate And LG.V_Date <= :toDate ";

            sqlWithParam.setSQL(StrSQL);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("siteList", SiteList);
            psTemp.setString("voucherType", DmShared.xNull(txtHVoucherType));
            if (!DmShared.xNull(txtHAccountName).equalsIgnoreCase("")) {
                psTemp.setStringList("accountName", DmShared.xNull(txtHAccountName));
            }
            psTemp.setString("fromDate", DmShared.dNull(txtFromDate));
            psTemp.setString("toDate", DmShared.dNull(txtToDate));
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
    public DmResult<Boolean> isNegativeCash(String docId, String acCode, double amount) {
        DmResult<Boolean> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        boolean returnValue = false;
        String sqlQuery = "";

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0) + :amount ) As Balance "
                    + " From Ledger LG"
                    + " Where LG.SubCode = :subCode ";
            if (DmShared.iNull(docId).longValue() > 0) {
                sqlQuery += " and lg.docId != :docId ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", DmShared.iNull(acCode).toString());
            psTemp.setDouble("amount", DmShared.vNull(amount));
            if (DmShared.iNull(docId).longValue() > 0) {
                psTemp.setString("docId", docId);
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("Balance")) < 0) {
                    returnValue = true;
                }
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            dmrTemp.setData(returnValue);
            dmrTemp.setStatus(true);

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
    public DmResultPageing<List<TxtHAccountNo_Mdl>> getTxtHAccountNo(String accountCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHAccountNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHAccountNo_Mdl> lstTemp = new ArrayList<>();
        TxtHAccountNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select subcode,accountNo,bankName as bank,branch,swiftCode,branchAddress,ifscCode,isDefault "
                    + " from bankdetail"
                    + " where subCode=:subCode ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("subCode", DmShared.iNull(accountCode).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHAccountNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subcode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("accountNo")));
                listNode.setBank(DmShared.xNull(rsTemp.getString("bank")));
                listNode.setBranch(DmShared.xNull(rsTemp.getString("branch")));
                listNode.setIfscCode(DmShared.xNull(rsTemp.getString("ifscCode")));
                listNode.setBankAddress(DmShared.xNull(rsTemp.getString("branchAddress")));
                listNode.setSwiftNo(DmShared.xNull(rsTemp.getString("swiftCode")));
                listNode.setDefaultAcNo(DmShared.iNull(rsTemp.getString("isDefault")).intValue() == 1);

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
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
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Approval">

    @Override
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
            }
            dmrTemp.setData(docId);

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private WalkThroughDefault_Mdl getWalkThroughFor(VoucherEntry_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
    }

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {
            sqlQuery = "Select Count(*) As Cnt From DataAudit Where DocId=:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select postedBy, postedByDateTime,"
                    + "(case when :userName IN (ISNULL(postedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from LedgerM where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("postedBy")).trim().equalsIgnoreCase("")) {
                        rsTemp.updateString("postedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("postedByDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = "Update Ledger Set "
                                + "isPosted = 1 "
                                + "Where DocId = :DocId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", keyCode);
                        psTemp.executeUpdate();
                        psTemp.close();

                    } else {
                        throw new Exception("Document already posted.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("postedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {
                            rsTemp.updateString("postedBy", null);
                            rsTemp.updateDate("postedByDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = "Update Ledger Set "
                                    + "isPosted = 0 "
                                    + "Where DocId = :DocId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("DocId", keyCode);
                            psTemp.executeUpdate();
                            psTemp.close();

                        } else {
                            throw new Exception("Unauthorized user to unpost.");
                        }
                    } else {
                        throw new Exception("Document already un-posted.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    documentApproval(walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Other">
    @Override
    public DmResult<Double[]> getGetLedgerBalance(String search) {
        DmResult<Double[]> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        Double[] data = new Double[3];
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) As Balance,Sum(LG.AmtDr) AS amtDr,Sum(LG.amtCr) AS amtCr "
                    + " From Ledger LG "
                    + " Where LG.SubCode = :SubCode and LG.Site_Code = :Site_Code  AND LG.v_Prefix=:v_Prefix and  LG.isPosted=1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", DmShared.iNull(search).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("V_Prefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                data[0] = DmShared.vNull(rsTemp.getString("Balance"));
                data[1] = DmShared.vNull(rsTemp.getString("amtDr"));
                data[2] = DmShared.vNull(rsTemp.getString("amtCr"));

            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(data);
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
    public DmResult<List<TdsCategoryGrid_Mdl>> getTDSData(String strCode, double tdsOnAmt, String vr_Date) {
        DmResult<List<TdsCategoryGrid_Mdl>> rstTemp = new DmResult<>();
        List<TdsCategoryGrid_Mdl> lstTemp = new ArrayList<>();
        TdsCategoryGrid_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, ac_RoundOffTds = "N", condition = "";
        boolean slabWiseTDS = false;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT e.ac_RoundOffTDS,e.ac_TDS_With_Salb FROM enviro e where e.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                ac_RoundOffTds = DmShared.xNull(rsTemp.getString("ac_RoundOffTDS"));
                slabWiseTDS = (DmShared.iNull(rsTemp.getString("ac_TDS_With_Salb")).intValue() == 1 ? true : false);
            }
            rsTemp.close();
            psTemp.close();

            if (slabWiseTDS) {
                condition = " and (:amount between tc.FromAmt and tc.toAmt) and (:vr_Date between TC.applicableFrom and TC.applicableTo) ";
            }
            sqlQuery = "Select TC.TDSDesc,tcd.manualCode as tcdManualCode,TCD.Name As DName,TC.ACCode,"
                    + " SG.Name As AName,TC.Percentage,TC.FormulaString,SG.ManualCode as postingManualCode "
                    + " From TDSCat_Det TC "
                    + " Left Join TDSCat_Description TCD On TC.TDSDesc=TCD.Code "
                    + " Left Join SubGroup SG On TC.AcCode=SG.SubCode "
                    + " Where TC.Code=:code " + condition
                    + " Order By TC.SrNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(strCode).toString());
            if (slabWiseTDS) {
                psTemp.setString("amount", DmServer.vNull(tdsOnAmt).toString());
            }
            psTemp.setString("vr_Date", vr_Date);
            rsTemp = psTemp.executeQuery();
            Integer srnNo = 0;
            while (rsTemp.next()) {
                listNode = new TdsCategoryGrid_Mdl();
//                listNode.setStrCode(String.valueOf(++srnNo));
                listNode.setDescription(DmShared.xNull(rsTemp.getString("DName")));
                listNode.setDescriptionCode(DmShared.xNull(rsTemp.getString("TDSDesc")));
                listNode.setDescriptionMCode(DmShared.xNull(rsTemp.getString("tcdManualCode")));
                listNode.setPostingAccount(DmShared.xNull(rsTemp.getString("AName")));
                listNode.setPostingAccountCode(DmShared.xNull(rsTemp.getString("ACCode")));
                listNode.setPostingManualCode(DmShared.xNull(rsTemp.getString("postingManualCode")));
                listNode.setPercent(DmServer.vNull(rsTemp.getString("Percentage"), "0.000"));
                //listNode.setAmount(DmShared.vNull("0.00"));
                listNode.setFormula(DmShared.xNull(rsTemp.getString("FormulaString")));
                listNode.setRoundOffTDS(DmShared.xNull(ac_RoundOffTds));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            cnTemp.close();

            rstTemp.setStatus(true);

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
    public DmResult<String> printRecords(VoucherPrint_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        String strSQL, strSQL1, condition = "", conditionContra = "", amount = "";
        ResultSet rsTemp = null, rsTemp1 = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        ChequePrintSetup_Mdl mrTempChquePrintSetup = new ChequePrintSetup_Mdl();
        dmrTemp.setData("");

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            amount = " CASE WHEN ISNULL(lm.amtDr,0)>0 THEN ISNULL(lm.amtDr,0) ELSE ISNULL(lm.amtCr,0) END ";

            if (DmShared.iNull(mrTemp.getHeaderAccountCode()).longValue() > 0) {
                condition += " AND lm.contraSub=:contraSub ";
            }
            if (DmShared.iNull(mrTemp.getVoucherTypeTag()).longValue() > 0) {
                condition += " AND lm.v_type=:v_type ";
            }
            if (!DmShared.xNull(mrTemp.getVoucherNoCode()).trim().equalsIgnoreCase("")) {
                condition += " AND lm.docId IN (" + DmServer.getParamList(":docId", mrTemp.getVoucherNoCode()) + " )";
            }
            if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                condition += " and lm.subCode IN (" + DmServer.getParamList(":subCode", mrTemp.getAccountCode()) + " )";
            }
            if (!DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date>=:fromDate ";
            }
            if (!DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
                condition += " AND lm.v_Date<=:toDate ";
            }

            strSQL = "        SELECT "
                    + "             lm.docid as code "
                    + "           , lm.recid as name "
                    + "           , lm.v_SNo  "
                    + "           , lm.v_Date "
                    + "           , 1 as isCrossCheque "
                    + "           , vt.short_Name "
                    + "           , sgc.displayName as accountName "
                    + "           , lm.chq_No as chqNo"
                    + "           , format(lm.chq_Date,'ddMMyyyy') as  chqDate "
                    + "           , lm.favourOf as favourOf "
                    + "           , " + amount + " as amount "
                    + "         FROM ledger lm "
                    + "         LEFT JOIN subGroup sg ON lm.subCode=sg.subCode "
                    + "         LEFT JOIN subGroup sgc ON lm.contraSub=sgc.subCode "
                    + "         LEFT JOIN voucher_type vt ON vt.v_Type=lm.v_Type "
                    + "		LEFT JOIN subGroupSiteList SGL ON SG.subCode = SGL.subCode  "
                    + "         WHERE SGL.site_Code = :site_Code "
                    + "		AND sg.isActive = 1  " + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(mrTemp.getHeaderAccountCode()).longValue() > 0) {
                psTemp.setString("contraSub", DmShared.iNull(mrTemp.getHeaderAccountCode()).toString());
            }
            if (DmShared.iNull(mrTemp.getVoucherTypeTag()).longValue() > 0) {
                psTemp.setString("v_type", DmShared.iNull(mrTemp.getVoucherTypeTag()).toString());
            }
            if (!DmShared.xNull(mrTemp.getVoucherNoCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(mrTemp.getVoucherNoCode()));
            }
            if (!DmShared.dNull(mrTemp.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(DmShared.dNull(mrTemp.getFromDate())));
            }
            if (!DmShared.dNull(mrTemp.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(DmShared.dNull(mrTemp.getToDate())));
            }
            if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(mrTemp.getAccountCode()));
            }
            rsTemp = psTemp.executeQuery();
            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(DmShared.xNull(lvc.CONTRA))) {
                conditionContra += " where cps.bankCode IN (" + DmServer.getParamList(":bankCode", mrTemp.getAccountCode()) + " )";
            } else {
                conditionContra += "where cps.bankCode= :bankCode ";
            }
            strSQL1 = "  SELECT  "
                    + "   cps.bankCode "
                    + " , cps.chequeDateTop "
                    + " , cps.chequeDateLeft "
                    + " , cps.crossChequeTop "
                    + " , cps.crossChequeLeft "
                    + " , cps.favourOfTop "
                    + " , cps.favourOfLeft "
                    + " , cps.AmtInWordsTop "
                    + " , cps.AmtInWordsLeft "
                    + " , cps.AmtTop "
                    + " , cps.AmtLeft "
                    + " , cps.authSignatory "
                    + " , cps.authSignatoryTop "
                    + " , cps.authSignatoryLeft "
                    + " , cps.companyName "
                    + " , cps.companyNameTop "
                    + " , cps.companyNameLeft"
                    + " , cps.paperWidth"
                    + " , cps.paperHeight"
                    + " FROM chequePrintingSetup as cps "
                    + conditionContra;
//                    + " where cps.bankCode= :bankCode ";
            psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, strSQL1);

            if (DmShared.xNull(mrTemp.getVoucherCategory()).equalsIgnoreCase(DmShared.xNull(lvc.CONTRA))) {
                if (!DmShared.xNull(mrTemp.getAccountCode()).trim().equalsIgnoreCase("")) {
                    psTemp1.setStringList("bankCode", DmShared.xNull(mrTemp.getAccountCode()));
                }
            } else {
                psTemp1.setString("bankCode", DmShared.iNull(mrTemp.getHeaderAccountCode()).toString());
            }
            rsTemp1 = psTemp1.executeQuery();
            if (rsTemp1.next()) {
                mrTempChquePrintSetup.setBankCode(DmShared.xNull(rsTemp1.getString("bankCode")));
                mrTempChquePrintSetup.setChequeDateTop(DmServer.vNull(rsTemp1.getString("chequeDateTop"), "0.00"));
                mrTempChquePrintSetup.setChequeDateLeft(DmServer.vNull(rsTemp1.getString("chequeDateLeft"), "0.00"));
                mrTempChquePrintSetup.setFavourOfTop(DmServer.vNull(rsTemp1.getString("favourOfTop"), "0.00"));
                mrTempChquePrintSetup.setFavourOfLeft(DmServer.vNull(rsTemp1.getString("favourOfLeft"), "0.00"));
                mrTempChquePrintSetup.setAmtInWordsTop(DmServer.vNull(rsTemp1.getString("AmtInWordsTop"), "0.00"));
                mrTempChquePrintSetup.setAmtInWordsLeft(DmServer.vNull(rsTemp1.getString("AmtInWordsLeft"), "0.00"));
                mrTempChquePrintSetup.setAmtTop(DmServer.vNull(rsTemp1.getString("AmtTop"), "0.00"));
                mrTempChquePrintSetup.setAmtLeft(DmServer.vNull(rsTemp1.getString("AmtLeft"), "0.00"));
                mrTempChquePrintSetup.setAuthSign(DmShared.xNull(rsTemp1.getString("authSignatory")));
                mrTempChquePrintSetup.setAuthSignTop(DmServer.vNull(rsTemp1.getString("authSignatoryTop"), "0.00"));
                mrTempChquePrintSetup.setAuthSignLeft(DmServer.vNull(rsTemp1.getString("authSignatoryLeft"), "0.00"));
                mrTempChquePrintSetup.setCompanyName(DmShared.xNull(rsTemp1.getString("companyName")));
                mrTempChquePrintSetup.setCompanyNameTop(DmServer.vNull(rsTemp1.getString("companyNameTop"), "0.00"));
                mrTempChquePrintSetup.setCompanyNameLeft(DmServer.vNull(rsTemp1.getString("companyNameLeft"), "0.00"));
                mrTempChquePrintSetup.setCrossChequeTop(DmServer.vNull(rsTemp1.getString("crossChequeTop"), "0.00"));
                mrTempChquePrintSetup.setCrossChequeLeft(DmServer.vNull(rsTemp1.getString("crossChequeLeft"), "0.00"));
                mrTempChquePrintSetup.setPaperWidth(DmServer.vNull(rsTemp1.getString("paperWidth"), "0.00"));
                mrTempChquePrintSetup.setPaperHeight(DmServer.vNull(rsTemp1.getString("paperHeight"), "0.00"));
            }
            psTemp1.close();
            rsTemp1.close();

            //<editor-fold defaultstate="collapsed" desc="JASPER">
            LinkedHashMap hm = new LinkedHashMap();
            hm.put("companyName", DmShared.xNull(mrTempChquePrintSetup.getCompanyName()));
            hm.put("authSignatory", DmShared.xNull(mrTempChquePrintSetup.getAuthSign()));

            //<editor-fold defaultstate="collapsed" desc="comment">
            String reportPath = DmBaseServer.Path.reports;
            DmJasperCompiler jasperCompiler = null;

            String subDirectory = "";
            if (DmBaseShared.xNull(subDirectory).equalsIgnoreCase("")) {
                subDirectory = "system";
            }

            reportPath = reportPath + "/" + subDirectory + "/AC_ChequePrinting.jrxml";

            JasperDesign jasperDesign = JRXmlLoader.load(reportPath);

            jasperDesign.setPageHeight(DmShared.iNull(mrTempChquePrintSetup.getPaperHeight()).intValue());
            jasperDesign.setPageWidth(DmShared.iNull(mrTempChquePrintSetup.getPaperWidth()).intValue());

            jasperReport = JasperCompileManager.compileReport(jasperDesign);
//</editor-fold>

            JRElementsVisitor.visitReport(jasperReport, new JRVisitor() {
                @Override
                public void visitBreak(JRBreak jrb) {
                    try {
                        Field topField = JRBaseElement.class.getDeclaredField("y");
                        topField.setAccessible(true);
                        int oldValue = topField.getInt(jrb);
                        int newTopValue = 0;
                        if (DmShared.xNull(jrb.getKey()).equalsIgnoreCase("breakPage")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getPaperHeight()).doubleValue())).intValue();
                            topField.setInt(jrb, newTopValue);
                            ((JRBaseElement) jrb).getEventSupport().firePropertyChange(topField.getName(), oldValue, newTopValue);
                            jrb.setX(0);
                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void visitChart(JRChart jrc) {
                }

                @Override
                public void visitCrosstab(JRCrosstab jrc) {
                }

                @Override
                public void visitElementGroup(JRElementGroup jreg) {
                }

                @Override
                public void visitEllipse(JREllipse jre) {

                }

                @Override
                public void visitFrame(JRFrame jrf) {

                }

                @Override
                public void visitImage(JRImage jri) {

                }

                @Override
                public void visitLine(JRLine jrline) {

                }

                @Override
                public void visitRectangle(JRRectangle jrr) {

                }

                @Override
                public void visitStaticText(JRStaticText jrst) {
                }

                @Override
                public void visitSubreport(JRSubreport jrs) {

                }

                @Override
                public void visitTextField(JRTextField textField) {
                    try {
                        Field topField = JRBaseElement.class.getDeclaredField("y");
                        topField.setAccessible(true);
                        int oldValue = topField.getInt(textField);
                        boolean flagSetValue = false;
                        int newTopValue = 0, newLeftValue = 0;

                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("chqDate")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getChequeDateTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getChequeDateLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("amountInWords")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtInWordsTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtInWordsLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("favourOf")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getFavourOfTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getFavourOfLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("amount")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAmtLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("authSignatory")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAuthSignTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getAuthSignLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("companyName")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCompanyNameTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCompanyNameLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (DmShared.xNull(textField.getKey()).equalsIgnoreCase("acPayee")) {
                            newTopValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCrossChequeTop()).doubleValue())).intValue();
                            newLeftValue = DmShared.iNull(Math.floor(DmShared.vNull(mrTempChquePrintSetup.getCrossChequeLeft()).doubleValue())).intValue();
                            flagSetValue = true;
                        }
                        if (flagSetValue) {
                            topField.setInt(textField, newTopValue);
                            ((JRBaseElement) textField).getEventSupport().firePropertyChange(topField.getName(), oldValue, newTopValue);
                            textField.setX(newLeftValue);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void visitComponentElement(JRComponentElement jrce) {
                }

                @Override
                public void visitGenericElement(JRGenericElement jrge) {

                }
            });
            jasperPrint = DmServer.fillJasper(this, "", jasperReport, hm, null, rsTemp);
            DmReportProperty_Mdl property_Mdl = new DmReportProperty_Mdl();
            property_Mdl.setExportType(DmEnum.DmExportType.PDF);
            dmrTemp.setData(DmServer.showJasper(this, jasperPrint, "", property_Mdl));
//</editor-fold>

            spBegin.commit();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    //</editor-fold>

    @Override
    public DmResult<SaveRecord_Mdl> saveRecord(UserPermission userPermission, VoucherEntry_Mdl mrTemp) {
        DmResult<SaveRecord_Mdl> dmrTemp = new DmResult();
        SaveRecord_Mdl srmTemp = new SaveRecord_Mdl();
        List<GrdVoucherEntry_Mdl> listData;
        List<CostCenterGrid_Mdl> listDataCC;
        List<TdsCategoryGrid_Mdl> listDataTDS;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        StringBuilder sqlQueryBankInsert = new StringBuilder();
        String sqlQuery, contraTextOther = "", contra = "", contraDr = "", contraCr = "", contraTextJV = "";
        String contraTDS = "", contraTDSBF, chequeNo = "", chequeDt = "", narration = "";
        String narrationTemp = "", contraSubCodeTDS = "";
        ResultSet rsTemp = null;
        int vSNo = 0, prvVSNo = 0;
        double totalCredit = 0, totalDebit = 0, credit = 0, debit = 0;
        boolean autoApprove = false;
        int isBankCashBook = 0;

        try {
            VoucherEntry_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            autoApprove = DmShared.haveControlPermission(userPermission, "", "", VoucherEntry.approvedByBlog);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getVoucherPrefix(), cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getVoucherPrefix(), cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("ledgerM", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getVoucherPrefix(), cnTemp, this, true);

                sqlQuery = "Select Count(*) As Cnt From DataAudit Where DocId=:DocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "Select Count(DocId) As Cnt From LedgerM Where DocId=:DocId And isNull(PostedBy,'')<>''";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
                }
                rsTemp.close();
                psTemp.close();
            }

            String drAdjustDocId = DmServer.executeScalar("select finPayDocId from ledgerAdj where vr_DocId = " + mrTemp.getSharedKeyFieldCode() + "", cnTemp);
            if (!DmShared.xNull(drAdjustDocId).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Record Has Been Adjusted. You Can Not Edit This Record.");
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            listData = mrTemp.getgrdData();
            //================================================
            //================= For JV / Contra =======================
            if (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.JOURNAL)
                    || mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CONTRA)) {
                for (int i = 0; i < listData.size(); i++) {
                    if (!DmShared.xNull(listData.get(i).getAccCode()).equalsIgnoreCase("")) {
                        if (!contraTextJV.equalsIgnoreCase("")) {
                            contraTextJV = contraTextJV + "";
                        }

                        if (DmShared.vNull(listData.get(i).getDebit()) > 0) {
                            if (contraDr.trim().equalsIgnoreCase("")) {
                                contraDr = listData.get(i).getAccCode();
                            }
                            contraTextJV = contraTextJV + DmServer.prepareContraText(listData.get(i).getAccName(), DmShared.vNull(listData.get(i).getDebit()), "Dr");
                        } else {
                            if (contraCr.trim().equalsIgnoreCase("")) {
                                contraCr = listData.get(i).getAccCode();
                            }
                            contraTextJV = contraTextJV + DmServer.prepareContraText(listData.get(i).getAccName(), DmShared.vNull(listData.get(i).getCredit()), "Cr");
                        }
                    }
                }
            }

            //================================================
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete From Stock Where DocId = :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From LedgerCostCenter Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            AccountAdjustment_Server.deleteRecord(mrTemp.getSharedKeyFieldCode(), cnTemp);
            ItemAdjustment_SrvImpl.deleteRecord(mrTemp.getSharedKeyFieldCode(), cnTemp);

            sqlQuery = "Delete From Ledger Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From tdsTrans Where DocId =:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "Insert Into LedgerM (DocId,V_Type,V_Prefix,Site_Code, V_No, V_Date,SubCode,"
                        + " Narration,PostedBy,RecId, recIdPrefix, U_EntDt,PreparedBy,wtaCode,remark) "
                        + "Values (:DocId,:V_Type,:V_Prefix,:Site_Code, :V_No, :V_Date,:SubCode,:Narration,:PostedBy,:RecId, :recIdPrefix, :uEntDt,"
                        + " :preparedBy,:wtaCode,:remark)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
                psTemp.setString("Narration", "");
                psTemp.setString("PostedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
                psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                psTemp.setString("wtaCode", DmShared.foriegnKey(mrTemp.getWtaCode()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            } else {

                sqlQuery = "update LedgerM Set "
                        + "V_Date=:V_Date,"
                        + "SubCode=:SubCode,"
                        + "Narration=:Narration,"
                        + "PostedBy=:PostedBy,"
                        + "transferDt=:transferDt,"
                        + "U_Name=:uName,"
                        + "remark=:remark, "
                        + "U_EntDt=:uEntDt "
                        + "Where DocId=:DocId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
                psTemp.setString("Narration", "");
                psTemp.setString("PostedBy", (autoApprove ? DmServer.getSsnUserName(this) : null));
                psTemp.setString("transferDt", null);
                psTemp.setString("uName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            vSNo = 0;
            chequeNo = "";
            chequeDt = "";
            for (int i = 0; i < listData.size(); i++) {
                if (!DmShared.xNull(listData.get(i).getAccCode()).equalsIgnoreCase("")
                        && !listData.get(i).isHeaderAcRecord()
                        && !listData.get(i).isTdsRecord()) {
                    if (!contraTextOther.equalsIgnoreCase("")) {
                        contraTextOther = contraTextOther + "";
                    }
                    if (DmShared.vNull(listData.get(i).getDebit()) > 0) {
                        contra = contraCr;
                        contraTextOther = contraTextOther + DmServer.prepareContraText(listData.get(i).getAccName(), DmShared.vNull(listData.get(i).getDebit()), "Dr");
                    } else {
                        contra = contraDr;
                        contraTextOther = contraTextOther + DmServer.prepareContraText(listData.get(i).getAccName(), DmShared.vNull(listData.get(i).getCredit()), "Cr");
                    }

                    isBankCashBook = 0;
                    if (!mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.JOURNAL)
                            && !mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CONTRA)) {

                        isBankCashBook = 1;
                        contra = mrTemp.getAccountNameCode();

                        if (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.BANK_PAYMENT)) {
                            contraTextJV = DmServer.prepareContraText(mrTemp.getAccountName(), DmShared.vNull(listData.get(i).getDebit()), "Cr");
                        } else {
                            contraTextJV = DmServer.prepareContraText(mrTemp.getAccountName(), DmShared.vNull(listData.get(i).getCredit()), "Dr");
                        }
                    }

                    if (!DmShared.xNull(mrTemp.getRemark().trim()).equalsIgnoreCase("")) {
                        narrationTemp = DmShared.xNull(mrTemp.getRemark()) + "";
                    }
                    narrationTemp = narrationTemp + DmShared.xNull(listData.get(i).getNarration());
                    vSNo++;
                    listData.get(i).setVSNo(DmShared.iNull(vSNo).toString());
                    sqlQuery = "Insert Into Ledger (DocId, RecId, recIdPrefix, V_No, V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr,"
                            + "remark,V_Type,V_Prefix,Site_Code,chq_No,chq_Date,clg_Date,clg_Remark,favourOf,TDSCategory,TDSOnAmt,ContraText,"
                            + "OrignalAmt,TDSDeductFrom,RefNo,RefDate,TDSFor,narration,isBankCashBook,accountNo,bankName,branchAddress,ifscCode, "
                            + "swiftCode,branch,isCrossCheque,pan,tan,u_Name,u_EntDt) "
                            + "Values(:DocId,:RecId, :recIdPrefix, :V_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr,:remark,"
                            + ":V_Type,:V_Prefix,:Site_Code,:chq_No,:chq_Date,:clg_Date,:clg_Remark,:favourOf,:TDSCategory,:TDSOnAmt,:ContraText,:OrignalAmt,:TDSDeductFrom,:RefNo,"
                            + ":RefDate,:TDSFor,:narration,:isBankCashBook,:accountNo,:bankName,:branchAddress,:ifscCode,:swiftCode,:branch,:isCrossCheque,:pan,:tan,:u_Name,:u_EntDt)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                    psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                    psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
                    psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                    psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                    psTemp.setString("SubCode", DmShared.foriegnKey(listData.get(i).getAccCode()));
                    psTemp.setString("ContraSub", DmShared.foriegnKey(contra));
                    psTemp.setString("AmtDr", DmServer.vNull(listData.get(i).getDebit(), "0.00"));
                    psTemp.setString("AmtCr", DmServer.vNull(listData.get(i).getCredit(), "0.00"));
                    psTemp.setString("remark", listData.get(i).getNarration());
                    psTemp.setString("narration", narrationTemp);
                    psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                    psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                    psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                    psTemp.setString("Chq_No", listData.get(i).getChequeDetail_Mdl().getChequeNo());
                    psTemp.setDate("Chq_Date", DmServer.convertDate(listData.get(i).getChequeDetail_Mdl().getChequeDate()));
                    psTemp.setDate("clg_Date", DmServer.convertDate(listData.get(i).getChequeDetail_Mdl().getClearDate()));
                    psTemp.setString("favourOf", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getFavourOf()));
                    psTemp.setString("clg_Remark", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getClearRemark()));
                    psTemp.setString("accountNo", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getAccountNo()));
                    psTemp.setString("bankName", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getBank()));
                    psTemp.setString("branch", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getBranch()));
                    psTemp.setString("isCrossCheque", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getIsCrossChequeCode()));
                    psTemp.setString("branchAddress", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getBranchAddress()));
                    psTemp.setString("ifscCode", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getIfscCode()));
                    psTemp.setString("swiftCode", DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getSwiftCode()));
                    psTemp.setString("TDSCategory", DmShared.foriegnKey(listData.get(i).getTds_Mdl().getTdsCategoryCode()));
                    psTemp.setString("TDSOnAmt", DmServer.vNull(listData.get(i).getTds_Mdl().getTdsOnAmount(), "0.00"));
                    psTemp.setString("ContraText", contraTextJV);
                    psTemp.setString("OrignalAmt", DmServer.vNull(listData.get(i).getTds_Mdl().getOrignalAmount(), "0.00"));
                    psTemp.setString("TDSDeductFrom", DmShared.foriegnKey(listData.get(i).getTds_Mdl().getTdsDeductFormCode()));
                    psTemp.setString("RefNo", mrTemp.getReferenceNo());
                    psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getReferenceDate()));
                    psTemp.setString("isBankCashBook", String.valueOf(isBankCashBook));
                    psTemp.setString("TDSFor", listData.get(i).getTds_Mdl().getTdsForTag());
                    psTemp.setString("pan", DmShared.xNull(listData.get(i).getPan()));
                    psTemp.setString("tan", DmShared.xNull(listData.get(i).getTan()));
                    psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                    psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());

                    psTemp.executeUpdate();
                    psTemp.close();

                    if (DmShared.xNull(mrTemp.getCosetCenterCode()).equalsIgnoreCase("")) {
                        listDataCC = listData.get(i).getCostCenterGrdData();

                        if (listDataCC != null) {
                            for (int vSNoCC = 0; vSNoCC < listDataCC.size(); vSNoCC++) {
                                if (!DmShared.xNull(listDataCC.get(vSNoCC).getCode()).equalsIgnoreCase("")) {
                                    sqlQuery = "Insert Into LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtDr,AmtCr) Values"
                                            + "(:DocId,:V_SNo,:SNo,:CostCenter,:AmtDr,:AmtCr)";
                                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                                    psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                                    psTemp.setString("SNo", DmServer.vNull(vSNoCC + 1, "0"));
                                    psTemp.setString("CostCenter", listDataCC.get(vSNoCC).getCode());
                                    psTemp.setString("AmtDr", (DmShared.vNull(listData.get(i).getDebit()) != 0 ? DmServer.vNull(listDataCC.get(vSNoCC).getAmount(), "0.00") : null));
                                    psTemp.setString("AmtCr", (DmShared.vNull(listData.get(i).getCredit()) != 0 ? DmServer.vNull(listDataCC.get(vSNoCC).getAmount(), "0.00") : null));
                                    psTemp.executeUpdate();
                                    psTemp.close();
                                }
                            }
                        }
                    } else {
                        sqlQuery = "Insert Into LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtDr,AmtCr) Values"
                                + "(:DocId,:V_SNo,:SNo,:CostCenter,:AmtDr,:AmtCr)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                        psTemp.setString("SNo", DmServer.vNull(vSNo, "0"));
                        psTemp.setString("CostCenter", mrTemp.getCosetCenterCode());
                        psTemp.setString("AmtDr", DmServer.vNull((DmShared.vNull(listData.get(i).getDebit()) != 0 ? DmShared.vNull(listData.get(i).getDebit()) : null), "0.00"));
                        psTemp.setString("AmtCr", DmServer.vNull((DmShared.vNull(listData.get(i).getCredit()) != 0 ? DmShared.vNull(listData.get(i).getCredit()) : null), "0.00"));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }

                    prvVSNo = vSNo;
                    if (!DmShared.xNull(listData.get(i).getChequeDetail_Mdl().getChequeNo()).equalsIgnoreCase("")) {
                        if (chequeNo.trim().equalsIgnoreCase("")) {
                            chequeNo = listData.get(i).getChequeDetail_Mdl().getChequeNo().trim();
                            chequeDt = listData.get(i).getChequeDetail_Mdl().getChequeDate();
                        }
                    }
                    contraTDS = "";
                    //For TDS 
                    if (!DmShared.xNull(listData.get(i).getTds_Mdl().getTdsCategoryCode()).trim().equalsIgnoreCase("")) {
                        totalCredit = 0;
                        totalDebit = 0;
                        narration = "TDS Against: " + DmShared.xNull(listData.get(i).getTds_Mdl().getTdsCategoryName()).trim() + " On " + DmServer.vNull(listData.get(i).getTds_Mdl().getTdsOnAmount(), "0.00") + " From " + DmShared.xNull(listData.get(i).getTds_Mdl().getTdsDeductFormName()).trim();
                        listDataTDS = listData.get(i).getTds_Mdl().getTdsGridList();
                        for (int j = 0; j < listDataTDS.size(); j++) {
                            if (!DmShared.xNull(listDataTDS.get(j).getDescriptionCode()).trim().equalsIgnoreCase("")) {
                                if (DmShared.xNull(listData.get(i).getTds_Mdl().getTdsForTag()).trim().equalsIgnoreCase("R")) {
                                    credit = 0;
                                    debit = DmShared.vNull(listDataTDS.get(j).getAmount());
                                    totalDebit = 0;
                                    totalCredit = totalCredit + debit;

                                    contraTDSBF = DmServer.prepareContraText(listData.get(i).getAccName().trim(), debit, "Cr");
                                    if (!contraTDS.equalsIgnoreCase("")) {
                                        contraTDS = contraTDS + "";
                                    }
                                    contraTDS = contraTDS + DmServer.prepareContraText(listDataTDS.get(j).getPostingAccount().trim(), debit, "Dr");
                                } else {
                                    credit = DmShared.vNull(listDataTDS.get(j).getAmount());
                                    debit = 0;
                                    totalDebit = totalDebit + credit;
                                    totalCredit = 0;

                                    contraTDSBF = DmServer.prepareContraText(listData.get(i).getAccName().trim(), credit, "Dr");
                                    if (!contraTDS.equalsIgnoreCase("")) {
                                        contraTDS = contraTDS + "";
                                    }
                                    contraTDS = contraTDS + DmServer.prepareContraText(listDataTDS.get(j).getPostingAccount().trim(), credit, "Cr");
                                }
                                narrationTemp = "";
                                if (!DmShared.xNull(mrTemp.getRemark()).trim().equalsIgnoreCase("")) {
                                    narrationTemp = DmShared.xNull(mrTemp.getRemark()) + "";
                                }
                                narrationTemp = narrationTemp + (narration + " @ " + DmShared.vNull(listDataTDS.get(j).getPercent()));
                                if (DmShared.iNull(contraSubCodeTDS).longValue() == 0) {
                                    contraSubCodeTDS = DmShared.iNull(listDataTDS.get(j).getPostingAccountCode()).toString();
                                }
                                vSNo++;
                                sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr, "
                                        + "Narration,V_Type,V_Prefix,Site_Code,Chq_No,Chq_Date,"
                                        + "TDSCategory,TDSOnAmt,TDSDesc,TDSPer,TDS_Of_V_SNo,"
                                        + "isSystem_Generated,FormulaString,roundOffTDS,ContraText,RefNo,RefDate,remark) Values "
                                        + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr, "
                                        + ":Narration,:V_Type,:V_Prefix,:Site_Code,:Chq_No,:Chq_Date,"
                                        + ":TDSCategory,:TDSOnAmt,:TDSDesc,:TDSPer,:TDS_Of_V_SNo,"
                                        + "'1',:FormulaString,:roundOffTDS,:ContraText,:RefNo,:RefDate,:remark) ";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                                psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                                psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
                                psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                                psTemp.setString("SubCode", listDataTDS.get(j).getPostingAccountCode());
                                psTemp.setString("ContraSub", listData.get(i).getAccCode());
                                psTemp.setString("AmtDr", DmServer.vNull(debit, "0.00"));
                                psTemp.setString("AmtCr", DmServer.vNull(credit, "0.00"));
                                psTemp.setString("remark", narration + " @ " + DmShared.vNull(listDataTDS.get(j).getPercent()));
                                psTemp.setString("Narration", narrationTemp);
                                psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                                psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                                psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                                psTemp.setString("Chq_No", null);
                                psTemp.setDate("Chq_Date", null);
                                psTemp.setString("TDSCategory", DmShared.foriegnKey(listData.get(i).getTds_Mdl().getTdsCategoryCode()));
                                psTemp.setString("TDSOnAmt", DmServer.vNull(listData.get(i).getTds_Mdl().getTdsOnAmount(), "0.00"));
                                psTemp.setString("TDSDesc", DmShared.foriegnKey(listDataTDS.get(j).getDescriptionCode()));
                                psTemp.setString("TDSper", DmServer.vNull(listDataTDS.get(j).getPercent(), "0.00"));
                                psTemp.setString("TDS_Of_V_SNo", DmServer.vNull(prvVSNo, "0"));
                                psTemp.setString("formulaString", listDataTDS.get(j).getFormula());
                                psTemp.setString("roundOffTDS", DmShared.xNull(listDataTDS.get(j).getRoundOffTDS()));
                                psTemp.setString("ContraText", contraTDSBF);
                                psTemp.setString("RefNo", mrTemp.getReferenceNo());
                                psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getReferenceDate()));
                                psTemp.executeUpdate();
                                psTemp.close();
                                saveCCBifurcation(cnTemp, mrTemp, i, vSNo, DmShared.vNull(listData.get(i).getDebit()) + DmShared.vNull(listData.get(i).getCredit()), (DmShared.xNull(listData.get(i).getTds_Mdl().getTdsForTag()).trim().equalsIgnoreCase("R") ? true : false), debit + credit);
                            }
                        }
                        narrationTemp = "";
                        if (!DmShared.xNull(mrTemp.getRemark()).trim().equalsIgnoreCase("")) {
                            narrationTemp = DmShared.xNull(mrTemp.getRemark()) + "";
                        }
                        narrationTemp = narrationTemp + narration;
                        //======== Inserting Sum Of TDS In Party A/c 
                        vSNo++;
                        sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,contraSub,AmtDr,AmtCr, "
                                + "Narration,V_Type,V_Prefix,Site_Code,Chq_No,Chq_Date,TDSCategory,"
                                + "TDSOnAmt,isSystem_Generated,ContraText,RefNo,RefDate,TDS_Of_V_SNo,remark) Values "
                                + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:contraSub,:AmtDr,:AmtCr, "
                                + ":Narration,:V_Type,:V_Prefix,:Site_Code,:Chq_No,:Chq_Date,:TDSCategory,"
                                + ":TDSOnAmt,'1',:ContraText,:RefNo,:RefDate,:TDS_Of_V_SNo,:remark) ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                        psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                        psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
                        psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                        psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                        psTemp.setString("SubCode", listData.get(i).getAccCode());
                        psTemp.setString("ContraSub", contraSubCodeTDS);
                        psTemp.setString("AmtDr", DmServer.vNull(totalDebit, "0.00"));
                        psTemp.setString("AmtCr", DmServer.vNull(totalCredit, "0.00"));
                        psTemp.setString("remark", narration);
                        psTemp.setString("Narration", narrationTemp);
                        psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                        psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                        psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                        psTemp.setString("Chq_No", null);
                        psTemp.setDate("Chq_Date", null);
                        psTemp.setString("TDSCategory", DmShared.foriegnKey(listData.get(i).getTds_Mdl().getTdsCategoryCode()));
                        psTemp.setString("TDSOnAmt", DmServer.vNull(listData.get(i).getTds_Mdl().getTdsOnAmount(), "0.00"));
                        psTemp.setString("ContraText", contraTDS);
                        psTemp.setString("RefNo", mrTemp.getReferenceNo());
                        psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getReferenceDate()));
                        psTemp.setString("TDS_Of_V_SNo", DmServer.vNull(prvVSNo, "0"));
                        psTemp.executeUpdate();
                        psTemp.close();

                        sqlQuery = "Insert Into LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtCr,AmtDr) "
                                + "Select  LGC.DocId,:V_SNo As V_SNo, row_number() OVER (ORDER BY LGC.docId) As SNO,LGC.CostCenter As CostCenter, "
                                + "Sum(LGC.AmtDr) As AmtDr,Sum(LGC.AmtCr) As AmtCr "
                                + "From LedgerCostCenter LGC  "
                                + "Left Join Ledger LG On LG.DocId = LGC.DocId And LG.V_SNo = LGC.V_SNo "
                                + "Where LGC.DocId = :DocId And isNull(LG.isSystem_Generated,'0') = '1' And "
                                + "LG.Tds_Of_V_SNo = :Tds_Of_V_SNo And LG.V_SNo <> :V_SNo  "
                                + "Group By LGC.DocId,LGC.CostCenter ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                        psTemp.setString("Tds_Of_V_SNo", DmServer.vNull(prvVSNo, "0"));
                        psTemp.executeUpdate();
                        psTemp.close();
                    }

                    GrdVoucherEntry_Mdl gveMdl = listData.get(i);
                    //=======================================< Adjustment >=======================================//
                    if (gveMdl != null) {
                        if (gveMdl.getLstAdjustments() != null) {
                            if (!gveMdl.getLstAdjustments().isEmpty()) {
                                AdjustmentSave_Mdl adjSaveMdl = new AdjustmentSave_Mdl();
                                adjSaveMdl.setVr_DocId(mrTemp.getSharedKeyFieldCode());
                                adjSaveMdl.setVr_V_SNo(gveMdl.getVSNo());
                                adjSaveMdl.setVr_V_Date(mrTemp.getVoucherDate());
                                adjSaveMdl.setVr_SubCode(gveMdl.getAccCode());
                                adjSaveMdl.setLstAdjustments(gveMdl.getLstAdjustments());
                                AccountAdjustment_Server.saveRecord(adjSaveMdl, cnTemp);
                            }
                        }
                    }
                    //=======================================< ********** >=======================================//
                    //=====================================< Item Adjustment >====================================//
                    if (gveMdl != null) {
                        if (gveMdl.getLstItemAdjustments() != null) {
                            if (!gveMdl.getLstItemAdjustments().isEmpty()) {
                                ItemAdjustmentSave_Mdl iadjSaveMdl = new ItemAdjustmentSave_Mdl();
                                iadjSaveMdl.setVr_DocId(mrTemp.getSharedKeyFieldCode());
                                iadjSaveMdl.setVr_V_SNo(gveMdl.getVSNo());
                                iadjSaveMdl.setVr_V_Date(mrTemp.getVoucherDate());
                                iadjSaveMdl.setVr_SubCode(gveMdl.getAccCode());
                                iadjSaveMdl.setLstItemAdjustments(gveMdl.getLstItemAdjustments());
                                ItemAdjustment_SrvImpl.saveRecord(iadjSaveMdl, cnTemp);
                            }
                        }
                    }
                    //=======================================< ********** >=======================================//
                }
            }

            if (!mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.JOURNAL)
                    && !mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.CONTRA)) {
//                String narrationRemarkHeader = "";
//                if (!DmShared.xNull(mrTemp.getRemark()).trim().equalsIgnoreCase("")) {
//                    narrationRemarkHeader = DmShared.xNull(mrTemp.getRemark()) + " \ ";
//                }
//                narrationRemarkHeader = narrationRemarkHeader + narrationForHeader;
                vSNo++;
                sqlQuery = "Insert Into Ledger (DocId,RecId, recIdPrefix, v_No,V_SNo,V_Date,SubCode,ContraSub,AmtDr,AmtCr, "
                        + "Narration,V_Type,V_Prefix,Site_Code,isSystem_Generated,ContraText,Chq_No,Chq_Date,RefNo,RefDate,remark) Values "
                        + "(:DocId,:RecId, :recIdPrefix, :v_No,:V_SNo,:V_Date,:SubCode,:ContraSub,:AmtDr,:AmtCr, "
                        + ":Narration,:V_Type,:V_Prefix,:Site_Code,'1',:ContraText,:Chq_No,:Chq_Date,:RefNo,:RefDate,:remark)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("RecId", mrTemp.getSharedKeyFieldName());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("V_No", mrTemp.getObjRecId().getCounter().toString());
                psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                psTemp.setDate("V_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("SubCode", DmShared.foriegnKey(mrTemp.getAccountNameCode()));
                psTemp.setString("ContraSub", DmShared.foriegnKey(listData.get(0).getAccCode()));
                psTemp.setString("AmtDr", DmServer.vNull(!mrTemp.getVoucherCategory().equalsIgnoreCase(lvc.BANK_PAYMENT) ? DmShared.vNull(mrTemp.getTotalDebit()) : null, "0.00")); //& ", "  //"" & IIf(FGMain.Columns(GDebit).Visible, Val(LblDrAmt.Text)                
                psTemp.setString("AmtCr", DmServer.vNull(mrTemp.getVoucherCategory().equalsIgnoreCase(lvc.BANK_PAYMENT) ? DmShared.vNull(mrTemp.getTotalCredit()) : null, "0.00"));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("Narration", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("V_Type", mrTemp.getVoucherTypeCode());
                psTemp.setString("V_Prefix", mrTemp.getVoucherPrefix());
                psTemp.setString("Site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("ContraText", contraTextOther);
                psTemp.setString("Chq_No", chequeNo);
                psTemp.setDate("Chq_Date", DmServer.convertDate(chequeDt));
                psTemp.setString("RefNo", mrTemp.getReferenceNo());
                psTemp.setDate("RefDate", DmServer.convertDate(mrTemp.getReferenceDate()));
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "Insert Into LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtCr,AmtDr) "
                        + "Select  LGC.DocId,:V_SNo As V_SNo, row_number() OVER (ORDER BY LGC.docId) As SNO, LGC.CostCenter As CostCenter, "
                        + (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.BANK_PAYMENT) ? "Sum(LGC.AmtDr) As AmtDr,Null As AmtCr " : "Null As AmtDr,Sum(LGC.AmtCr) As AmtCr ")
                        + "From LedgerCostCenter LGC  "
                        + "Left Join Ledger LG On LG.DocId = LGC.DocId And LG.V_SNo = LGC.V_SNo "
                        + "Where LGC.DocId = :DocId And isNull(LG.isSystem_Generated,'0') <> '1' "
                        + "Group By LGC.DocId, LGC.CostCenter  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (!sqlQueryBankInsert.toString().equals("")) {
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQueryBankInsert.toString().replace("#BANK_V_NO#", DmServer.vNull(vSNo, "0")));
                // psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                psTemp.executeUpdate();
                psTemp.close();
            }


            /*==============================<< Approve Document >>====================================*/
            if (autoApprove) {
                if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0) {
                    documentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
                } else {
                    sqlQuery = "Update Ledger Set "
                            + "isPosted = 1 "
                            + "Where DocId = :DocId ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                    psTemp.executeUpdate();
                    psTemp.close();

                }
            }
            /*==============================<< checking for Debit/Credit >>====================================*/
            sqlQuery = "SELECT CAST((ISNULL(SUM(l.amtDr),0) - ISNULL(SUM(l.amtCr),0)) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM ledger l\n"
                    + "WHERE l.docId = :docId\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() != 0) {
                    throw new DmException(0, "Total debit amount and credit amount is not equal.");
                }
            }
            rsTemp.close();
            psTemp.close();
            /*==============================<< **************** >>====================================*/
            sqlQuery = "Select isNull(Max(SG.Name),'') As SName "
                    + "From LedgerCostCenter LGC "
                    + "Left Join Ledger LG On LG.DocId = LGC.DocId And LG.V_SNo = LGC.V_SNo  "
                    + "Left Join SubGroup SG On SG.SubCode = LG.Subcode "
                    + "Where LGC.DocId = :DocId "
                    + "Group By LGC.V_SNo "
                    + "Having isNull(Sum(LGC.AmtDr), 0) <> isNull(Max(LG.AmtDr), 0) Or isNull(Sum(LGC.AmtCr), 0) <> isNull(Max(LG.AmtCr), 0) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("SName")).equalsIgnoreCase("")) {
                    throw new DmException(0, DmShared.xNull(rsTemp.getString("SName")) + " " + "Bifurcation of cost center has not been done properly.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select  isNull(Max(CCM.Name),'') As CCMName  "
                    + "From LedgerCostCenter LGC "
                    + "Left Join CostCenterMast CCm On CCM.Code = LGC.CostCenter  "
                    + "Where LGC.DocId = :DocId "
                    + "Group By LGC.CostCenter "
                    + "Having isNull(Sum(LGC.AmtDr), 0) <> isNull(Sum(LGC.AmtCr), 0) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("CCMName")).equalsIgnoreCase("")) {
                    throw new DmException(0, DmShared.xNull(rsTemp.getString("CCMName")) + " " + "Cost center debit / credit in not equal.");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.BANK_PAYMENT)
                    || mrTemp.getVoucherCategory().trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
                sqlQuery = "select  count(docId) as cnt "
                        + "from ledger lg  "
                        + "where lg.docId = :docId and lg.subCode = :subCode  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("subCode", mrTemp.getAccountNameCode());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 1) {
                        throw new DmException(0, "Same header a/c can not be used in detail grid.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            sqlQuery = "Insert Into tdsTrans (DocId, RecId, recIdPrefix, V_No, V_SNo,V_Date,V_Prefix,V_Type,Site_Code,pan,tan,"
                    + " SubCode,ContraSub,TDSFor,TDSCategory,TDSOnAmt,TDSDeductFrom,tdsDesc,tdsPer,"
                    + " tds_Of_V_Sno ,formulaString,roundOffTds,AmtDr,AmtCr,u_Name,u_EntDt) "
                    + "  ( select lg.DocId, lg.RecId, lg.recIdPrefix, lg.V_No, lg.V_SNo,lg.V_Date,lg.V_Prefix,lg.V_Type,lg.Site_Code,lgp.pan,lgp.tan,"
                    + " lg.SubCode,lg.ContraSub,"
                    + " lgp.TDSFor,lg.TDSCategory,lg.TDSOnAmt,lgp.TDSDeductFrom,LG.tdsDesc,LG.tdsPer,"
                    + " lg.tds_Of_V_Sno,lg.formulaString,lg.roundOffTds,lg.AmtDr,lg.AmtCr,lgp.u_Name,lgp.u_EntDt "
                    + " from ledger lg "
                    + "Left Join Ledger lgp On lgp.DocId=LG.DocId And lg.TDS_Of_V_SNo=lgp.V_SNo "
                    + "where lg.docid=:DocId and lg.TDS_Of_V_SNo>0)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());

            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Save Record Model">
            srmTemp.setDocId(mrTemp.getSharedKeyFieldCode());
            srmTemp.setVoucherTypeCode(mrTemp.getVoucherTypeCode());
            srmTemp.setVoucherTypeName(mrTemp.getVoucherTypeName());
            srmTemp.setVoucherCategory(mrTemp.getVoucherCategory());
            srmTemp.setRecId(mrTemp.getSharedKeyFieldName());
            srmTemp.setVoucherDate(mrTemp.getVoucherDate());
            srmTemp.setAccountName(DmShared.xNull(mrTemp.getAccountName()));
            srmTemp.setAccountNameCode(DmShared.xNull(mrTemp.getAccountNameCode()));
            srmTemp.setAccountNameNature(DmShared.xNull(mrTemp.getAccountNameNature()));

            //</editor-fold>
//            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            dmrTemp.setData(srmTemp);
            spBegin.commit();
            dmrTemp.setStatus(true);

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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

    private void saveCCBifurcation(Connection cnTemp, VoucherEntry_Mdl mrTemp, int rowIndex, int vSNo, double mainAmount, boolean isDebit, double amount) throws SQLException {
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        double amtCCTotal, amtBF, amtBFTotal;
        int vSNoCC;
        List<CostCenterGrid_Mdl> listDataCC;

        if (rowIndex < 0) {
            return;
        }

        if (DmShared.xNull(mrTemp.getCosetCenterCode()).trim().equalsIgnoreCase("")) {
            listDataCC = mrTemp.getgrdData().get(rowIndex).getCostCenterGrdData();
            if (listDataCC != null) {
                amtCCTotal = 0;
                amtBF = 0;
                amtBFTotal = 0;
                for (vSNoCC = 0; vSNoCC < listDataCC.size(); vSNoCC++) {
                    if (!DmShared.xNull(listDataCC.get(vSNoCC).getCode()).trim().equalsIgnoreCase("")) {

                        amtCCTotal += DmShared.vNull(listDataCC.get(vSNoCC).getAmount());
                        if (amtCCTotal == mainAmount) {
                            amtBF = amount - amtBFTotal;
                        } else {
                            amtBF = Math.floor((DmShared.vNull(listDataCC.get(vSNoCC).getAmount()) / mainAmount) * amount);
                        }

                        amtBFTotal += amtBF;
                        sqlQuery = "Insert Into LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtDr,AmtCr) Values "
                                + "(:DocId,:V_SNo,:SNo,:CostCenter,:AmtDr,:AmtCr)";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
                        psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
                        psTemp.setString("SNo", DmServer.vNull(vSNoCC + 1, "0"));
                        psTemp.setString("CostCenter", listDataCC.get(vSNoCC).getCode());
                        psTemp.setString("AmtDr", (isDebit ? DmServer.vNull(amtBF, "0.00") : null));
                        psTemp.setString("AmtCr", (!isDebit ? DmServer.vNull(amtBF, "0.00") : null));
                        psTemp.executeUpdate();
                    }
                }
            }
        } else {
            sqlQuery = "Insert Into LedgerCostCenter (DocId,V_SNo,SNo,CostCenter,AmtDr,AmtCr) Values "
                    + "(:DocId,:V_SNo,:SNo,:CostCenter,:AmtDr,:AmtCr)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", mrTemp.getSharedKeyFieldCode());
            psTemp.setString("V_SNo", DmServer.vNull(vSNo, "0"));
            psTemp.setString("SNo", DmServer.vNull(vSNo, "0"));
            psTemp.setString("CostCenter", mrTemp.getCosetCenterCode());
            psTemp.setString("AmtDr", (isDebit ? DmServer.vNull(amount, "0.00") : null));
            psTemp.setString("AmtCr", (!isDebit ? DmServer.vNull(amount, "0.00") : null));
            psTemp.executeUpdate();
        }
    }

    @Override
    public DmResult<VoucherEntry_Mdl> getMoveRec(String strKey) {
        DmResult<VoucherEntry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery, sqlColumn = "";
        double countCC;
        VoucherEntry_Mdl mrTemp = new VoucherEntry_Mdl();
        GrdVoucherEntry_Mdl voucherData;
        TdsCategoryGrid_Mdl tdsData;
        CostCenterGrid_Mdl costCenterData;

        dmrTemp.setData(mrTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "Select LM.DocId,LM.V_Type,LM.V_Prefix,LM.Site_Code,LM.V_Date,LM.SubCode,  "
                    + "SG.Name As AcName, sg.manualCode as acManualCode, LM.PostedBy,LM.RecId, lm.v_No, lm.recIdPrefix, "
                    + "LM.Narration,LM.U_Name,LM.PreparedBy,LC.Cnt_CCM,LC.CostCenter As CCMCode,LC.CCMName, LM.PostedBy,"
                    + "VT.Description,VT.Category,sgn.manualCode As SGNature,LM.wtaCode,LM.remark "
                    + "From LedgerM LM "
                    + "Left Join SubGroup SG On LM.SubCode = SG.SubCode "
                    + "Left Join Voucher_Type VT On VT.V_Type = LM.V_Type "
                    + "LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code "
                    + "Left Join "
                    + "(Select  LGC.DocId,Count(Distinct LGC.CostCenter) As Cnt_CCM,Max(LGC.CostCenter) As CostCenter,Max(CCM.Name) As CCMName "
                    + "From LedgerCostCenter LGC "
                    + "Left Join CostCenterMast CCM On CCM.Code = LGC.CostCenter "
                    + "Where LGC.DocId = :key "
                    + "Group By LGC.DocId "
                    + ") As LC On LC.DocId = LM.DocId "
                    + "Where LM.DocId = :key Order By lm.V_Date,lm.V_Type,lm.v_no,lm.recid ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("key", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("RecId")));
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("V_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("Description")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("Category")));
                mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("PreparedBy")));
                mrTemp.setModifiedBy(DmShared.xNull(rsTemp.getString("U_Name")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("PostedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                countCC = DmShared.vNull(rsTemp.getString("Cnt_CCM"));
                if (countCC == 1) {
                    mrTemp.setCosetCenterCode(DmShared.xNull(rsTemp.getString("CCMCode")));
                    mrTemp.setCosetCenterName(DmShared.xNull(rsTemp.getString("CCMName")));
                }

                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("V_Date")));
                mrTemp.setAccountName(DmShared.xNull(rsTemp.getString("AcName")));
                mrTemp.setAccountNameCode(DmShared.xNull(rsTemp.getString("SubCode")));
                mrTemp.setAccountNameNature(DmShared.xNull(rsTemp.getString("SGNature")));
                mrTemp.setAccountManualCode(DmShared.xNull(rsTemp.getString("acManualCode")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));

                sqlColumn = " ,case when isNull(lg.isSystem_Generated, 0) = 1 AND isNull(lg.TDS_Of_V_SNo,0) = 0 And (isNull(lg.AmtDr,0)+isNull(lg.AmtCr,0)) > 0 then -1 else 0 end as isHeaderRecord  ";
                rsTemp.close();
                psTemp.close();

                mrTemp.setgrdData(new ArrayList());
                sqlQuery = "Select LG.DOCID, LG.v_SNo,\n"
                        + " CT.CityName,LG.RefNo,LG.RefDate,LG.SubCode,lg.TDS_Of_V_SNo,\n"
                        + " SG.Name As AcName,SG.ManualCode,sgn.manualCode As SGNature,LG.V_SNo, \n"
                        + " LG.AmtDr,LG.AmtCr,LG.remark as Narration,LG.Chq_No,LG.Chq_Date,LG.TDSOnAmt,LG.TDSCategory,\n"
                        + " TC.Name As TDSCName,LG.OrignalAmt,LG.TDSDeductFrom,TDF.Name As TDFName,tdf.manualcode as TDSDeductFromManualCode,\n"
                        + " LGTDS.AmtDr As TDSDeducted,LG.TDSFor,isNull(LG.Favourof,SG.DisplayName) As Favourof,\n"
                        + " LG.Clg_Date As BR_Clg_Date,isNull(lg.clg_Remark,'') As clg_Remark,\n"
                        + " LG.accountNo,LG.bankName,LG.branchAddress,LG.ifscCode,LG.swiftCode,LG.branch,LG.isCrossCheque,lg.isSystem_Generated,LG.PAN,LG.TAN  \n"
                        + "  " + sqlColumn
                        + " , LGTDSP.subcode as tdsParentSubCode \n"
                        + " From Ledger LG \n"
                        + " Left Join Ledger LGTDS On LGTDS.DocId=LG.DocId And LGTDS.TDS_Of_V_SNo=LG.V_SNo And isNull(LGTDS.TDSDesc,'')='' \n"
                        + " Left Join Ledger LGTDSP On LGTDSP.DocId=LG.DocId And LGTDSP.V_Sno = LG.TDS_Of_V_SNo \n"
                        + " Left Join SubGroup SG On LG.SubCode=SG.SubCode \n"
                        + " LEFT JOIN subGroupNature as sgn on sg.nature = sgn.code \n"
                        + " Left Join TDSCat TC On TC.Code=LG.TDSCategory \n"
                        + " Left Join SubGroup TDF On TDF.SubCode=LG.TDSDeductFrom \n"
                        + " Left Join City CT On CT.CityCode=SG.CityCode  \n"
                        + " Where LG.DocId = :key ";
                //  + " order by isHeaderRecord, lg.v_sno ";

                //+ "Where LG.DocId = :key ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("key", DmShared.iNull(strKey).toString());
                rsTemp = psTemp.executeQuery();

                while (rsTemp.next()) {
                    voucherData = new GrdVoucherEntry_Mdl();
                    mrTemp.setReferenceNo(DmShared.xNull(rsTemp.getString("RefNo")));
                    mrTemp.setReferenceDate(DmShared.dNull(rsTemp.getDate("RefDate")));

                    voucherData.setVSNo(DmServer.vNull(rsTemp.getString("V_SNo"), "0"));

                    voucherData.setAccNature(DmShared.xNull(rsTemp.getString("SGNature")));
                    if (DmShared.xNull(rsTemp.getString("CityName")).equalsIgnoreCase("")) {
                        voucherData.setAccName(DmShared.xNull(rsTemp.getString("AcName")));
                    } else {
                        voucherData.setAccName(DmShared.xNull(rsTemp.getString("AcName")) + " - " + DmShared.xNull(rsTemp.getString("CityName")));
                    }

                    voucherData.setAccCode(DmShared.xNull(rsTemp.getString("SubCode")));
                    voucherData.setAccMCode(DmShared.xNull(rsTemp.getString("ManualCode")));

                    voucherData.setNarration(DmShared.xNull(rsTemp.getString("Narration")));
                    voucherData.setPan(DmShared.xNull(rsTemp.getString("pan")));
                    voucherData.setTan(DmShared.xNull(rsTemp.getString("tan")));

                    if (DmShared.vNull(rsTemp.getString("AmtDr")) > 0) {
                        voucherData.setDebit(DmServer.vNull(rsTemp.getString("AmtDr"), "0.00"));
                    }

                    if (DmShared.vNull(rsTemp.getString("AmtCr")) > 0) {
                        voucherData.setCredit(DmServer.vNull(rsTemp.getString("AmtCr"), "0.00"));
                    }
                    voucherData.setParentAccountCode(DmShared.xNull(rsTemp.getString("tdsParentSubCode")));
                    voucherData.setTdsOfV_Sno(DmShared.xNull(rsTemp.getString("TDS_Of_V_SNo")));
                    voucherData.setTdsRecord((DmShared.iNull(rsTemp.getString("TDS_Of_V_SNo")).intValue() > 0));

                    voucherData.setHeaderAcRecord((DmShared.iNull(rsTemp.getString("isHeaderRecord")).intValue() == -1));
                    voucherData.getChequeDetail_Mdl().setChequeNo(DmShared.xNull(rsTemp.getString("Chq_No")));
                    voucherData.getChequeDetail_Mdl().setChequeDate(DmShared.dNull(rsTemp.getDate("Chq_Date")));
                    voucherData.getChequeDetail_Mdl().setClearDate(DmShared.dNull(rsTemp.getDate("BR_Clg_Date")));
                    voucherData.getChequeDetail_Mdl().setFavourOf(DmShared.xNull(rsTemp.getString("Favourof")));
                    voucherData.getChequeDetail_Mdl().setAccountNo(DmShared.xNull(rsTemp.getString("accountNo")));
                    voucherData.getChequeDetail_Mdl().setAccountCode(DmShared.xNull(rsTemp.getString("SubCode")));
                    voucherData.getChequeDetail_Mdl().setBank(DmShared.xNull(rsTemp.getString("bankName")));
                    voucherData.getChequeDetail_Mdl().setBranch(DmShared.xNull(rsTemp.getString("branch")));
                    voucherData.getChequeDetail_Mdl().setIsCrossChequeCode(DmShared.iNull(rsTemp.getString("isCrossCheque")).toString());
                    voucherData.getChequeDetail_Mdl().setIsCrossChequeText(DmShared.iNull(rsTemp.getString("isCrossCheque")).intValue() == 1 ? "Yes" : "No");
                    voucherData.getChequeDetail_Mdl().setBranchAddress(DmShared.xNull(rsTemp.getString("branchAddress")));
                    voucherData.getChequeDetail_Mdl().setIfscCode(DmShared.xNull(rsTemp.getString("ifscCode")));
                    voucherData.getChequeDetail_Mdl().setSwiftCode(DmShared.xNull(rsTemp.getString("swiftCode")));
                    voucherData.getChequeDetail_Mdl().setClearRemark(DmShared.xNull(rsTemp.getString("clg_Remark")));

                    voucherData.getTds_Mdl().setTdsDetailBind(true);
                    voucherData.getTds_Mdl().setTdsCategoryCode(DmShared.xNull(rsTemp.getString("TDSCategory")));
                    voucherData.getTds_Mdl().setTdsCategoryName(DmShared.xNull(rsTemp.getString("TDSCName")));

                    voucherData.getTds_Mdl().setTdsDeductFormName(DmShared.xNull(rsTemp.getString("TDFName")));
                    voucherData.getTds_Mdl().setTdsDeductFormCode(DmShared.xNull(rsTemp.getString("TDSDeductFrom")));
                    voucherData.getTds_Mdl().setTdsDeductManualCode(DmShared.xNull(rsTemp.getString("TDSDeductFromManualCode")));

                    voucherData.getTds_Mdl().setTdsOnAmount(DmServer.vNull(rsTemp.getString("TDSOnAmt"), "0.00"));
                    voucherData.getTds_Mdl().setOrignalAmount(DmServer.vNull(DmShared.vNull(rsTemp.getString("OrignalAmt")) > 0 ? DmShared.vNull(rsTemp.getString("OrignalAmt")) : DmShared.vNull(rsTemp.getString("TDSOnAmt")), "0.00"));
                    voucherData.getTds_Mdl().setTdsForTag(DmShared.xNull(rsTemp.getString("TDSFor")));
                    voucherData.getTds_Mdl().setTdsForText(DmShared.xNull(rsTemp.getString("TDSFor")).equalsIgnoreCase("P") ? "Payable" : DmShared.xNull(rsTemp.getString("TDSFor")).equalsIgnoreCase("R") ? "Receivable" : "");

                    voucherData.getTds_Mdl().setTdsGridList(new ArrayList());
                    if (!DmShared.xNull(voucherData.getTds_Mdl().getTdsCategoryCode()).trim().equalsIgnoreCase("")) {
                        sqlQuery = "Select LG.FormulaString, Lg.roundOffTDS,LG.SubCode,SG.Name As AcName,sg.ManualCode as postingManualCode,"
                                + " LG.AmtDr,LG.AmtCr,LG.TDSDesc,TCD.manualCode as tcdManualCode,"
                                + "TCD.Name As DName,LG.TDSPer ,lg.isSystem_Generated "
                                + "From Ledger LG "
                                + "Left Join SubGroup SG On LG.SubCode=SG.SubCode "
                                + "Left Join TDSCat_Description TCD On TCD.Code=LG.TDSDesc  "
                                + "Where LG.DocId = :key And isNull(LG.isSystem_Generated,'0')='1' And "
                                + "isNull(LG.TDSDesc,'') <> '' And LG.TDS_Of_V_SNo = " + voucherData.getVSNo().toString() + "  "
                                + "Order By V_SNo";
                        psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp1.setString("key", DmShared.iNull(strKey).toString());
                        rsTemp1 = psTemp1.executeQuery();
                        while (rsTemp1.next()) {
                            tdsData = new TdsCategoryGrid_Mdl();
                            tdsData.setDescriptionCode(DmShared.xNull(rsTemp1.getString("TDSDesc")));
                            tdsData.setDescriptionMCode(DmShared.xNull(rsTemp1.getString("tcdManualCode")));
                            tdsData.setDescription(DmShared.xNull(rsTemp1.getString("DName")));
                            tdsData.setPostingAccountCode(DmShared.xNull(rsTemp1.getString("SubCode")));
                            tdsData.setPostingAccount(DmShared.xNull(rsTemp1.getString("AcName")));
                            tdsData.setPostingManualCode(DmShared.xNull(rsTemp1.getString("postingManualCode")));
                            tdsData.setFormula(DmShared.xNull(rsTemp1.getString("FormulaString")));
                            tdsData.setRoundOffTDS(DmShared.xNull(rsTemp1.getString("roundOffTDS")));

                            tdsData.setPercent(DmServer.vNull(rsTemp1.getString("TDSPer"), "0.000"));
                            if (DmShared.vNull(rsTemp1.getString("AmtDr")) > 0) {
                                tdsData.setAmount(DmServer.vNull(rsTemp1.getString("AmtDr"), "0.00"));
                            } else {
                                tdsData.setAmount(DmServer.vNull(rsTemp1.getString("AmtCr"), "0.00"));
                            }
                            voucherData.getTds_Mdl().getTdsGridList().add(tdsData);
                        }

                        rsTemp1.close();
                        psTemp1.close();
                    }

                    voucherData.setCostCenterGrdData(new ArrayList());
                    //if (countCC > 1) {
                    sqlQuery = "Select LGC.CostCenter,isNull(LGC.AmtDr,0) + isNull(LGC.AmtCr,0) As Amount,CCM.Name As CCMName "
                            + "From LedgerCostCenter LGC "
                            + "Left Join CostCenterMast CCM On CCM.Code = LGC.CostCenter "
                            + "Where LGC.DocId = :key And "
                            + "LGC.V_SNo = " + voucherData.getVSNo().toString() + " "
                            + "Order By LGC.SNo";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp1.setString("key", DmShared.iNull(strKey).toString());
                    rsTemp1 = psTemp1.executeQuery();
                    while (rsTemp1.next()) {
                        costCenterData = new CostCenterGrid_Mdl();
                        costCenterData.setCode(DmShared.xNull(rsTemp1.getString("CostCenter")));
                        costCenterData.setName(DmShared.xNull(rsTemp1.getString("CCMName")));
                        costCenterData.setAmount(DmServer.vNull(rsTemp1.getString("Amount"), "0.00"));
                        voucherData.getCostCenterGrdData().add(costCenterData);
                    }
                    rsTemp1.close();
                    psTemp1.close();
                    //}

                    //======================================< Get Adjustments >======================================//
                    List<GrdAdjustment_Mdl> lstAdjustments = AccountAdjustment_Server.getAdjustments(mrTemp.getSharedKeyFieldCode(), voucherData.getVSNo(), cnTemp);
                    if (lstAdjustments != null) {
                        voucherData.setLstAdjustments(lstAdjustments);
                    }
                    //======================================< *************** >======================================//
                    //======================================< Get Adjustments >======================================//
                    List<GrdItemAdjustment_Mdl> lstItemAdjustments = ItemAdjustment_SrvImpl.getItemAdjustments(mrTemp.getSharedKeyFieldCode(), voucherData.getVSNo(), cnTemp);
                    if (lstItemAdjustments != null) {
                        voucherData.setLstItemAdjustments(lstItemAdjustments);
                    }
                    //======================================< *************** >======================================//

                    // lstAccountDetail.add(voucherData);
                    mrTemp.getgrdData().add(voucherData);
                }

            }
            // getTrdVno(mrTemp);

            psTemp.close();
            rsTemp.close();
//
//            for (GrdVoucherEntry_Mdl vrMdl : lstAccountDetail) {
//                mrTemp.getgrdData().addAll(addNewRow(vrMdl));
//            }

            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
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
    public DmResult<String> deleteRecord(UserPermission userPermission, String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, DocId = strKey, wtaCode = "";;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Check for before deleting data >>===============================*/
            sqlQuery = "Select Count(*) As Cnt From DataAudit Where DocId=:DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Audited. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "Select Count(DocId) As Cnt From LedgerM Where DocId=:DocId And isNull(PostedBy,'')<>''";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("Cnt") > 0) {
                throw new UnsupportedOperationException("Record Has Been Posted. You Can Not Edit/ Delete This Record.");
            }
            rsTemp.close();
            psTemp.close();

            String drAdjustDocId = DmServer.executeScalar("select finPayDocId from ledgerAdj where vr_DocId = " + DmShared.iNull(DocId).toString() + "", cnTemp);
            if (!DmShared.xNull(drAdjustDocId).equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Record Has Been Adjusted. You Can Not Delete This Record.");
            }

            /*==============================<< ****************************** >>===============================*/
            sqlQuery = "Delete From LedgerCostCenter Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();

//            sqlQuery = "Delete From BankReco Where DocId= :DocId ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
//            psTemp.executeUpdate();
//            psTemp.close();
            sqlQuery = "Delete From Stock Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            AccountAdjustment_Server.deleteRecord(DocId, cnTemp);
            ItemAdjustment_SrvImpl.deleteRecord(DocId, cnTemp);

            sqlQuery = "Delete From TdsTrans Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From Ledger Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Delete From LedgerM Where DocId= :DocId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("DocId", DmShared.iNull(DocId).toString());
            psTemp.executeUpdate();
            psTemp.close();

            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);

            spBegin.commit();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission, String posted, String voucherCategory) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        String sqlQuery, sqlCondition, condition = "";
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.JOURNAL)) {
                condition += " AND vt.category IN ('" + lvc.JOURNAL + "')";
            } else {
                condition += " AND vt.category = '" + voucherCategory + "'";
            }

            sqlQuery = "Select LM.DocId As SearchCode ";
            sqlQuery += "From LedgerM LM ";
            sqlQuery += "Left Join Voucher_Type VT On LM.V_Type = VT.V_Type ";
            sqlQuery += " Where LM.Site_Code = '" + DmServer.getSsnSiteCode(this) + "'";
            sqlQuery += " AND LM.V_Prefix = '" + DmServer.getSsnVoucherPrefix(this) + "'";
            sqlQuery += condition;

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            if (posted.equalsIgnoreCase("P")) {
                sqlQuery += "and isNull(LM.PostedBy,'') <> '' ";
            }
            if (posted.equalsIgnoreCase("U")) {
                sqlQuery += "and isNull(LM.PostedBy,'') = '' ";
            }

            sqlQuery += "Order By lm.V_Date,lm.V_Type,lm.v_no,lm.recid ";

            rsTemp = cnTemp.createStatement().executeQuery(sqlQuery);

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("SearchCode")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, String voucherCategory, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl mrTemp;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition, condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.JOURNAL)) {
                condition += " AND vt.category IN ('" + lvc.JOURNAL + "')";
            } else {
                condition += " AND vt.category = '" + voucherCategory + "'";
            }

            sqlQuery = "Select	LM.DocId, LM.V_Type, LM.V_Date, VT.Description As Voucher_Type, "
                    + " LM.RecId As Voucher_No, LM.V_Date As Voucher_Date, LG.RefNo As Reference_No, "
                    + " LG.RefDate As Reference_Date, isNull(SG.Name,'') As Account1, isNull(SG1.Name,'') As Account2, "
                    + " IsNull(LG.AmtDr,0) As Debit, IsNull(LG.AmtCr,0) As Credit, LG.Narration,  "
                    + " isNull(LM.PreparedBy,'') As Prepared_By, isNull(LM.U_Name,'') As Modified_By, "
                    + " FORMAT(lm.u_EntDt,'dd-MMM-yyyy HH.mm') AS entryDateTime ,"
                    + " isNull(LM.PostedBy,'') As Posted_By "
                    + " From LedgerM LM "
                    + " Left Join Voucher_Type VT On LM.V_Type=VT.V_Type   "
                    + " Left Join Ledger LG On LM.DocId=LG.DocId And LG.V_SNo=1  "
                    + " Left Join SubGroup SG On LM.SubCode=SG.SubCode   "
                    + " Left Join SubGroup SG1 On LG.SubCode=SG1.SubCode   "
                    + " Where  LM.Site_Code = :siteCode "
                    + " And LM.V_Prefix = :voucherPrefix " + condition;

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("voucherPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                try {
                    mrTemp = new Find_Mdl();
                    mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("DocId")));
                    mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Voucher_No")));
                    mrTemp.setVoucherType(DmShared.xNull(rsTemp.getString("Voucher_Type")));
                    mrTemp.setV_Date(DmShared.dNull(rsTemp.getDate("Voucher_Date")));
                    mrTemp.setAccount1(DmShared.xNull(rsTemp.getString("Account1")));
                    mrTemp.setAccount2(DmShared.xNull(rsTemp.getString("Account2")));
                    mrTemp.setDebit(DmServer.vNull(rsTemp.getString("Debit"), "0.00"));
                    mrTemp.setCredit(DmServer.vNull(rsTemp.getString("Credit"), "0.00"));
                    mrTemp.setNaration(DmShared.xNull(rsTemp.getString("Narration")));
                    mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("Prepared_By")));
                    mrTemp.setModifiyedBy(DmShared.xNull(rsTemp.getString("Modified_By")));
                    mrTemp.setPostedBy(DmShared.xNull(rsTemp.getString("Posted_By")));
                    mrTemp.setEntryDtTime(DmShared.xNull(rsTemp.getString("entryDateTime")));
                    lstTemp.add(mrTemp);
                } catch (Exception e) {
                    rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
                }
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

}
