package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptABCAnalysisRegister_Srv;
import aarogya.pharmacy.reports.shared.RptABCAnalysisRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHCompany_Mdl;
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

public class RptABCAnalysisRegister_SrvImpl extends DmRSSPrivate implements RptABCAnalysisRegister_Srv {

    ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = " ", innerQuery = "", outerQuery = " null ", innerCondition = "", aClass = "", bClass = "", groupByFieldCode = "", groupByFieldName = "";
        String orderByUnit = "", valueField = "";

        try {
            RptABCAnalysisRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                innerCondition += " AND  sb1.v_Date >= :fromDate ";
                condition += " AND  sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                innerCondition += " AND  sb1.v_Date <= :toDate ";
                condition += " AND  sb.v_Date <= :toDate ";
            }

            aClass += DmShared.vNull(filter.get("aClass").getText()).toString();
            bClass += DmShared.vNull(filter.get("bClass").getText()).toString();

            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sbd.itemCode IN (" + DmServer.getParamList(":medicine", filter.get("medicine").getTag()) + " )";
                innerCondition += " AND sbd1.itemCode IN (" + DmServer.getParamList(":medicine", filter.get("medicine").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemGroup in (" + DmServer.getParamList(":medGroup", filter.get("medGroup").getTag()) + " )";
                innerCondition += " and im.itemGroup in (" + DmServer.getParamList(":medGroup", filter.get("medGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode in (" + DmServer.getParamList(":medCat", filter.get("medCat").getTag()) + " )";
                innerCondition += " and im.itemCategoryCode in (" + DmServer.getParamList(":medCat", filter.get("medCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medMake").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemMakeCode in (" + DmServer.getParamList(":medMake", filter.get("medMake").getTag()) + " )";
                innerCondition += " and im.itemMakeCode in (" + DmServer.getParamList(":medMake", filter.get("medMake").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
                innerCondition += " AND sb1.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            } else {
                condition += " AND sb.site_Code in (" + DmServer.getSsnSiteCode(this) + ")";
                innerCondition += " AND sb1.site_Code in (" + DmServer.getSsnSiteCode(this) + ")";
            }
            if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Medicine Category")) {
                groupByFieldCode = " max(im.itemCategoryCode) ";
                groupByFieldName = " max(ic.name)  ";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Medicine Group")) {
                groupByFieldCode = "  max(im.itemGroup) ";
                groupByFieldName = " max(ig.name)  ";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Medicine Company")) {
                groupByFieldCode = "  max(im.itemMakeCode) ";
                groupByFieldName = " max(imm.companyName)";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("")) {
                groupByFieldCode = "''";
                groupByFieldName = "''";
            }

            if (DmShared.xNull(filter.get("analysisOn").getTag()).trim().equalsIgnoreCase("Value")) {
                innerQuery = " LEFT JOIN ( "
                        + " SELECT  "
                        + "		 '' as abcType "
                        + "		 ,'' as abcTypeCode "
                        + "		 , SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(sbd1.oh_Amt_Taxable,0)) as abcTotal "
                        + "		 FROM saleBillDetail sbd1 "
                        + "		 LEFT JOIN saleBill sb1 ON sb1.docId=sbd1.docId "
                        + "		 LEFT JOIN voucher_Type vt ON vt.v_Type=sb1.v_Type "
                        + "              LEFT JOIN itemMast im ON im.Code=sbd1.itemCode "
                        + "		 WHERE  vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_CASH + "','"+ pvc.PHARMACY_BILL_RETURN +"','" + pvc.PHARMACY_CASH_SALE_RETURN+ "') "
                        + "              AND sbd1.isFooter=0 "
                        + "              AND sb1.isCancelled=0 " + innerCondition
                        + "           )vsbd ON vsbd.abcType='' ";

//                outerQuery = " (CASE WHEN MAX(vt.category) = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(SUM(sbd.oh_Amt_Taxable),0)) as value "
//                        + " ,ROUND((((CASE WHEN MAX(vt.category) = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(SUM(sbd.oh_Amt_Taxable),0))/MAX(vsbd.abcTotal))*100) , 2) as perOfTotal "
//                        + " ,ROUND(MAX(vsbd.abcTotal),2) as abcTotal "
//                        + " ,MAX(vsbd.abcType) as abcType "
//                        + " ,MAX(vsbd.abcTypeCode) as abcTypeCode "
//                        + " ,'value' as analysisOn "
//                        + " , CASE "
//                        + "         WHEN (ISNULL((CASE WHEN MAX(vt.category) = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(SUM(sbd.oh_Amt_Taxable),0)),0)) >= ROUND((ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01),2) "
//                        + "                     THEN 'A Class' "
//                        + " 	WHEN (ROUND((ISNULL(MAX(vsbd.abcTotal),0) * " + bClass + " * 0.01),2)<=(ISNULL((CASE WHEN MAX(vt.category) = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(SUM(sbd.oh_Amt_Taxable),0)),0)) "
//                        + "                 AND ((ISNULL((CASE WHEN MAX(vt.category) = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(SUM(sbd.oh_Amt_Taxable),0)),0)) < ROUND((ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01),2))) "
//                        + "                     THEN 'B Class' "
//                        + "                 ELSE 'C Class' "
//                        + "                     END as ABCClassType ";
                valueField = " SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(sbd.oh_Amt_Taxable,0)) ";
                outerQuery = " ROUND(" + valueField + ", 2) as value "
                        + " ,ROUND((( " + valueField + " /MAX(vsbd.abcTotal))*100), 2) as perOfTotal "
                        + " ,ROUND(MAX(vsbd.abcTotal),2) as abcTotal "
                        + " ,MAX(vsbd.abcType) as abcType "
                        + " ,MAX(vsbd.abcTypeCode) as abcTypeCode "
                        + " ,'value' as analysisOn "
                        + " , CASE WHEN  ROUND(" + valueField + ", 2) >= ROUND((ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01),2) THEN 'A Class' "
                        + "         WHEN (ROUND(" + valueField + ", 2) < ROUND((ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01),2) AND ROUND(" + valueField + ", 2) >= ROUND((ISNULL(MAX(vsbd.abcTotal),0) * " + bClass + " * 0.01),2)) THEN 'B Class' "
                        + "         ELSE 'C Class' "
                        + "     END as ABCClassType ";
                orderByUnit = "";
            } else if (DmShared.xNull(filter.get("analysisOn").getTag()).trim().equalsIgnoreCase("Quantity")) {
                innerQuery = " LEFT JOIN ( "
                        + "     SELECT  "
                        + "		 MAX(um1.unit) as abcType "
                        + "		 , MAX(sbd1.saleUnitCode) as abcTypeCode "
                        + "		 , SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(sbd1.quantity,0)) as abcTotal "
                        + "		 FROM saleBillDetail sbd1 "
                        + "		 LEFT JOIN saleBill sb1 ON sb1.docId=sbd1.docId "
                        + "		 LEFT JOIN voucher_Type vt ON vt.v_Type=sb1.v_Type "
                        + "              LEFT JOIN itemMast im ON im.Code=sbd1.itemCode "
                        + "              LEFT JOIN unitMast um1 ON um1.Code=sbd1.saleUnitCode "
                        + "		 WHERE  vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "') "
                        + "              AND sbd1.isFooter=0 "
                        + "              AND sb1.isCancelled=0 " + innerCondition
                        + "          GROUP BY sbd1.saleUnitCode "
                        + "           )vsbd ON vsbd.abcTypeCode=sbd.saleUnitCode ";

//                outerQuery = " (CASE WHEN MAX(vt.category) = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(SUM(sbd.quantity),0)) as value "
//                        + " ,ROUND((((CASE WHEN MAX(vt.category) = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(SUM(sbd.quantity),0))/MAX(vsbd.abcTotal))*100) , 2) as perOfTotal "
//                        + " , MAX(vsbd.abcTotal) as abcTotal  "
//                        + " , MAX(vsbd.abcType) as abcType  "
//                        + " , MAX(vsbd.abcTypeCode) as abcTypeCode  "
//                        + " , 'quantity' as analysisOn  "
//                        + " , CASE WHEN (ISNULL(SUM(sbd.quantity),0)-ISNULL(SUM(sbd.returnedQty),0)) >= (ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01) THEN 'A Class'  "
//                        + "	WHEN ((ISNULL(MAX(vsbd.abcTotal),0) * " + bClass + " * 0.01)<=(ISNULL(SUM(sbd.quantity),0)-ISNULL(SUM(sbd.returnedQty),0)) AND (ISNULL(SUM(sbd.quantity),0)-ISNULL(SUM(sbd.returnedQty),0)) <(ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01)) THEN 'B Class' "
//                        + " ELSE 'C Class' END as ABCClassType ";
                valueField = " SUM(CASE WHEN vt.category = '" + pvc.PHARMACY_BILL_RETURN + "' THEN -1 ELSE 1 END * ISNULL(sbd.quantity,0)) ";
                outerQuery = " " + valueField + " as value "
                        + " ,ROUND((( " + valueField + " / MAX(vsbd.abcTotal))*100), 2) as perOfTotal "
                        + " , MAX(vsbd.abcTotal) as abcTotal  "
                        + " , MAX(vsbd.abcType) as abcType  "
                        + " , MAX(vsbd.abcTypeCode) as abcTypeCode  "
                        + " , 'quantity' as analysisOn  "
                        + " , CASE WHEN  " + valueField + " >= (ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01) THEN 'A Class'  "
                        + "         WHEN (" + valueField + " < (ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01) AND " + valueField + " >= (ISNULL(MAX(vsbd.abcTotal),0) * " + bClass + " * 0.01)) THEN 'B Class' "
                        + "         WHEN ((ISNULL(MAX(vsbd.abcTotal),0) * " + bClass + " * 0.01)<=(ISNULL(SUM(sbd.quantity),0)-ISNULL(SUM(sbd.returnedQty),0)) AND (ISNULL(SUM(sbd.quantity),0)-ISNULL(SUM(sbd.returnedQty),0)) <(ISNULL(MAX(vsbd.abcTotal),0) * " + aClass + " * 0.01)) THEN 'B Class' "
                        + "         ELSE 'C Class' "
                        + "     END as ABCClassType ";
                orderByUnit = " MAX(um.unit), ";
            }

            sqlQuery = " SELECT "
                    + outerQuery
                    + " ," + groupByFieldCode + " as groupCode "
                    + " ," + groupByFieldName + " as groupName "
                    + " ,MAX(sbd.saleUnitCode) as saleUnitCode "
                    + " ,MAX(um.unit) as saleUnitName "
                    + " ,MAX(im.name) as medicineName "
                    + " ,MAX(sbd.itemCode) as medicineCode "
                    + " FROM saleBillDetail sbd "
                    + " LEFT JOIN saleBill sb ON sb.docId=sbd.docId "
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type=sb.v_Type "
                    + " LEFT JOIN itemMast im ON im.Code=sbd.itemCode "
                    + " LEFT JOIN unitMast um ON um.code=sbd.saleUnitCode "
                    + " LEFT JOIN itemCat ic ON ic.code=im.itemCategoryCode "
                    + " LEFT JOIN itemGroup as ig on im.itemGroup=ig.code "
                    + " LEFT JOIN itemMakeMast as imm on im.itemmakeCode=imm.code "
                    + innerQuery
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "') "
                    + " AND (im.filterItemType = :filterItemType OR im.isCommon = 1) "
                    + " AND sbd.isFooter=0 "
                    + " AND sb.isCancelled=0 " + condition
                    + " GROUP BY sbd.itemCode ";
            if (DmShared.xNull(groupByFieldName).trim().equals("''")) {
                sqlQuery += " ORDER BY " + orderByUnit + " ABCClassType, MAX(im.name)";
            } else {
                sqlQuery += " ORDER BY " + orderByUnit + groupByFieldName + ", ABCClassType, MAX(im.name)";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            jasperReport = DmServer.compileJasper("Pharmacy_ABCAnalysisRegister");
            jasperPrint = DmServer.fillJasper(this, "ABC Analysis Register", jasperReport, null, filter, rsTemp);

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
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicine", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCat", DmShared.xNull(filter.get("medCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(filter.get("medGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("medMake").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medMake", DmShared.xNull(filter.get("medMake").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT CODE, companyName, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "   WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPurchaseDisPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer"), "0.00"));
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
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<TxtHMedicine_Mdl>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT  im.code, im.name, im.sku, um.unit,ig.name medGroup,ic.name medcategory,im.manualCode "
                    + " FROM itemMast im  "
                    + " LEFT JOIN unitmast um ON im.sku = um.code  "
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup "
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1) ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setMedicineGroup(DmShared.xNull(rsTemp.getString("medGroup")));
                listNode.setMedicineCategory(DmShared.xNull(rsTemp.getString("medcategory")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedCat(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select CODE, NAME from itemcat  "
                    + " where (filterItemType = :filterItemType or isCommon=1)");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT CODE, NAME, filteritemType "
                    + " FROM itemgroup  "
                    + " where filterItemType = :filterItemType or isCommon=1");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Landed Rate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Purchase Rate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Sale Rate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("M");
            listNode.setSharedKeyFieldName("MRP");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroupBy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Medicine Category");
            listNode.setSharedKeyFieldName("Medicine Category");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Medicine Group");
            listNode.setSharedKeyFieldName("Medicine Group");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Medicine Company");
            listNode.setSharedKeyFieldName("Medicine Company");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHAnalysisOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Quantity");
            listNode.setSharedKeyFieldName("Quantity");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Value");
            listNode.setSharedKeyFieldName("Value");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

}
