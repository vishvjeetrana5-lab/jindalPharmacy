package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptSalesPurchase_Srv;
import aarogya.pharmacy.reports.shared.RptSalesPurchase_Vld;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
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

public class RptSalesPurchase_SrvImpl extends DmRSSPrivate implements RptSalesPurchase_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String filterCategory = "'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "','"
            + pvc.PHARMACY_PURCHASE_CHALLAN + "','" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "','" + pvc.MEDICINE_OPENING + "'";

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = "", saleType = "", saleTypeReturn = "", returnFilter = "";

        try {
            RptSalesPurchase_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")
                    || !DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += "and sb.v_Date between :fromDate and :toDate ";
            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.departmentCode in (" + DmServer.getParamList(":department ", filter.get("department").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.site_Code in (" + DmServer.getParamList(":site ", filter.get("site").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemGroup in (" + DmServer.getParamList(":medicineGroup ", filter.get("medicineGroup").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sbd.itemCode in  (" + DmServer.getParamList(":medicine ", filter.get("medicine").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode in  (" + DmServer.getParamList(":medicineCat ", filter.get("medicineCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("returnInclude").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.iNull(filter.get("returnInclude").getTag()).intValue() == 1) {
                    returnFilter = "'" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_CASH + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_CASH_SALE_RETURN + "'";
                } else {
                    returnFilter = "'" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_CASH+ "'";
                }
            }

            if (!DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("Cash Sale")) {
                    saleType = "'" + DmShared.xNull(filter.get("saleType").getTag()) + "'";
                    saleTypeReturn = "'PHCSR'";
                } else if (DmShared.xNull(filter.get("saleType").getText()).trim().equalsIgnoreCase("Credit Sale")) {
                    saleType = "'" + DmShared.xNull(filter.get("saleType").getTag()) + "'";
                    saleTypeReturn = "'PHBRT'";
                } else {
                    saleType = "'" + DmShared.xNull(filter.get("saleType").getTag()).replace(",", "','") + "'";
                    saleTypeReturn = "'PHCSR','PHBRT'";
                }

            }
            sqlQuery = "select main.v_Date,DATENAME(weekday,main.v_Date) as weekDayName\n"
                    + ",sum(purchaseValueForSale) as purchaseValueForSale\n"
                    + ",sum(purchaseValueForReturn) as purchaseValueForReturn\n"
                    + ",sum(purchaseValueForSale)-sum(purchaseValueForReturn) as netPurchaseValue\n"
                    + ",sum(main.SaleValue) as SaleValue,sum(main.ReturnValue) as ReturnValue\n"
                    + ",sum(main.SaleValue) -sum(main.ReturnValue) as netSale\n"                   
                    + "\n"
                    + "	from (\n"
                    + "			select sb.v_Date,sb.site_Code,vt.category as vType\n"
                    + "			--,sb.departmentCode,sbd.itemCode,im.itemGroup,im.itemCategoryCode\n"
                    + "			,case when vt.category in (" + saleType + ") then sbd.quantity else 0 end as SaleQty,\n"
                    + "			case when vt.category in (" + saleTypeReturn + ") then sbd.quantity else 0 end as ReturnQty,\n"
                    + "			case when vt.category in (" + saleType + ") then sbd.oh_Amt_Net else 0 end as SaleValue,\n"
                    + "			case when vt.category in (" + saleTypeReturn + ") then sbd.oh_Amt_Net else 0 end as ReturnValue\n"
                    + "			,(case when vt.category in (" + saleType + ") then sbd.quantity else 0 end)*a.skuRate as purchaseValueForSale\n"
                    + "			,(case when vt.category in (" + saleTypeReturn + ") then sbd.quantity else 0 end)*a.skuRate as purchaseValueForReturn\n"
                    + "			,a.skuRate  as purchaseRate\n"
                    + "				from saleBill sb\n"
                    + "				left join saleBillDetail sbd on sbd.docid=sb.docid\n"
                    + " 			left join itemMast im on im.code = sbd.itemCode\n"
                    + "				left join voucher_Type vt on vt.v_Type=sb.v_Type\n"
                    + "				left join (				\n"
                    + "						select sum(stk.landedSkuValue)/sum(stk.recQty) as skuRate,max(stk.ibCode) as ibCode\n"
                    + "							from stock stk\n"
                    + "							left join itemMast im on stk.itemCode=im.code\n"
                    + "							left join voucher_Type vts on vts.v_Type=stk.v_Type\n"
                    + "							where  vts.category in(" + filterCategory + ") \n"
                    + "							and stk.recQty > 0 \n"
                    + "							group by stk.ibCode\n"
                    + "						) a on a.ibCode =sbd.ibCode \n"
                    + "			where sb.isCancelled=0 and vt.category in(" + returnFilter + ")\n"
                    + "			and sbd.isFooter = 0 " + condition
                    + "			) as main\n"
                    + "group by main.v_Date"
                    + " order by main.v_Date";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);

            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("Pharmacy_SaleVsPurchase");
            jasperPrint = DmServer.fillJasper(this, new pmPharmacy().SALES_VS_PURCHASE.caption, jasperReport, null, filter, rsTemp);
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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site", DmShared.xNull(filter.get("site").getTag()));
            }

            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }

            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCat", DmShared.xNull(filter.get("medicineCat").getTag()));
            }

            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicine", DmShared.xNull(filter.get("medicine").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PH') and sg.isActive = 1 ");
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
                    + " WHERE (ic.filterItemType = :filterItemType OR ic.isCommon = 1)");
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
                    + "   WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1)");;
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSaleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHBIL");
            listNode.setSharedKeyFieldName("Credit Sale");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PHBCS");
            listNode.setSharedKeyFieldName("Cash Sale");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReturnIncluded(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
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
