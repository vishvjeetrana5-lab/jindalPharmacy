package dataman.accounts.reports.server;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.accounts.reports.shared.VoucherEntryPaymentPrint_Mdl;
import dataman.accounts.reports.shared.VoucherEntryPaymentPrint_Srv;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class VoucherEntryPaymentPrint_SrvImpl extends DmRSSPrivate implements VoucherEntryPaymentPrint_Srv {

    LibraryVoucherCategory lvc = new LibraryVoucherCategory();

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT VT.V_Type as Code,VT.Description,VT.DefaultAc,SG.Name AS DAName "
                    + "FROM Voucher_Type VT "
                    + "LEFT JOIN SubGroup SG ON VT.DefaultAc=SG.SubCode "
                    + "WHERE VT.Category IN ('" + lvc.BANK_PAYMENT + "','" + lvc.BANK_RECEIPT + "','" + lvc.JOURNAL + "') "
                    + "ORDER BY VT.Description ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResultPageing<List<VoucherEntryPaymentPrint_Mdl.TxtHAccountName>> getTxtHAccountName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<VoucherEntryPaymentPrint_Mdl.TxtHAccountName>> rstTemp = new DmResultPageing<>();
        List<VoucherEntryPaymentPrint_Mdl.TxtHAccountName> lstTemp = new ArrayList<>();
        VoucherEntryPaymentPrint_Mdl.TxtHAccountName listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String SiteList = "";

        try {
            SiteList = "|" + DmServer.getSsnSiteCode(this) + "|";
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT SG.SubCode AS Code,SG.Name,SG.ManualCode,isNull(CT.CityName,'') as CityName,AG.GroupName                                                       \n"
                    + "FROM SubGroup SG "
                    + "LEFT JOIN City CT ON CT.CityCode = SG.CityCode "
                    + "LEFT JOIN AcGroup AG ON AG.GroupCode = SG.GroupCode "
                    + "WHERE SG.SiteList LIKE :siteList AND "
                    + "SG.isActive = 1 "
                    + "ORDER BY SG.Name ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("siteList", "%" + SiteList + "%");
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new VoucherEntryPaymentPrint_Mdl.TxtHAccountName();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherNo(DmHelper helper, boolean blnToEndOfFile, String txtFromDate, String txtToDate, String txtHVoucherType, String txtHAccountName) {
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
            StrSQL += " Where LG.Site_Code =:siteList And ";
            StrSQL += " LG.V_Type =:voucherType And ";
            StrSQL += " LG.V_Date >=:fromDate And LG.V_Date <= :toDate ";
            if (!DmShared.xNull(txtHAccountName).equalsIgnoreCase("")) {
                StrSQL += " And LG.DocId In ( ";
                StrSQL += " Select L.DocId ";
                StrSQL += " From Ledger L ";
                StrSQL += " Where L.SubCode In (" + DmServer.getParamList(":accountName", DmShared.xNull(txtHAccountName)) + ")";
                StrSQL += " And L.Site_Code = :siteList ";
                StrSQL += " And L.V_Type = :voucherType ";
                StrSQL += " And L.V_Date >= :fromDate And L.V_Date <= :toDate ";
                StrSQL += " ) ";
            }
            StrSQL += " Order By LG.V_Date ";

            sqlWithParam.setSQL(StrSQL);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("siteList", SiteList);
            psTemp.setString("voucherType", DmShared.xNull(txtHVoucherType));
            if (!DmShared.xNull(txtHAccountName).equalsIgnoreCase("")) {
                psTemp.setStringList("accountName", DmShared.xNull(txtHAccountName));
            }
            psTemp.setDate("fromDate", DmServer.convertDate(txtFromDate));
            psTemp.setDate("toDate", DmServer.convertDate(txtToDate));
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
