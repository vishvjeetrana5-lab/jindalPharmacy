package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptMedicineRateSchemeChangeRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import aarogya.pharmacy.reports.shared.RptMedicineRateSchemeChangeRegister_Srv;

public class RptMedicineRateSchemeChangeRegister_SrvImpl extends DmRSSPrivate implements RptMedicineRateSchemeChangeRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, condition = "";

        try {
            RptMedicineRateSchemeChangeRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND iu.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND iu.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND iu.subCode IN (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup IN (" + DmServer.getParamList(":medicineGroup ", filter.get("medicineGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":medicineCatCode ", filter.get("medicineCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND iu.itemCode IN (" + DmServer.getParamList(":medicineCode ", filter.get("medicine").getTag()) + " )";
            }

            sqlQuery = "SELECT"
                    + "   iu.docId"
                    + " , iu.v_Date"
                    + " , iu.recId"
                    + " , CASE WHEN sg.cityCode IS NULL THEN sg.name ELSE sg.name + ', ' + ct.cityName END AS supplier"
                    + " , im.name AS item"
                    + " , iu.previousMrp"
                    + " , iu.currentMrp"
                    + " , iu.previousSaleRate"
                    + " , iu.currentSaleRate"
                    + " , iu.previousPurchaseRate"
                    + " , iu.currentPurchaseRate"
                    + " , iu.previousPurchaseDiscountPer"
                    + " , iu.currentPurchaseDiscountPer"
                    + " , iu.previousSchemeOnQty"
                    + " , iu.currentSchemeOnQty"
                    + " , iu.previousSchemeFreeQty"
                    + " , iu.currentSchemeFreeQty"
                    + " FROM itemUpdateHistory iu"
                    + " LEFT JOIN subGroup sg ON sg.subCode = iu.subCode"
                    + " LEFT JOIN city ct ON ct.cityCode = sg.cityCode"
                    + " LEFT JOIN itemMast im ON im.code = iu.itemCode"
                    + " WHERE 1 = 1 " + condition
                    + " ORDER BY im.name, iu.v_Date, iu.v_Type, iu.v_No, iu.recId, iu.id ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("Pharmacy_MedicineRateSchemeChangeRegister");
            jasperPrint = DmServer.fillJasper(this, "Medicine Rate & Scheme Change Register", jasperReport, null, filter, rsTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSupplier(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name AS name,sg.manualCode,c.cityName as city"
                    + " FROM subgroup sg"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " left join city as c on c.cityCode = sg.cityCode "
                    + " WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_CHALLAN + "','" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "')"
                    + " )");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("city")));
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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT "
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode "
                    + " FROM itemMast AS im "
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCat(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ic.code, ic.name "
                    + "FROM itemCat ic"
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemgroup ig"
                    + "   WHERE (ig.filterItemType = :filterItemType or isCommon=1) ");;
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("NAME")));
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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCatCode", DmShared.xNull(filter.get("medicineCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }
        } catch (Exception e) {
        }
    }

}
