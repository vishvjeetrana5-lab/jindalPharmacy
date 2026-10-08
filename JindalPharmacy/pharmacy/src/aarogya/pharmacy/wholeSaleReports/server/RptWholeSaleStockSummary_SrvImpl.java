
package aarogya.pharmacy.wholeSaleReports.server;

import aarogya.pharmacy.wholeSaleReports.shared.TxtHMedicine_Mdl;
import aarogya.pharmacy.wholeSaleReports.shared.BatchNo_Mdl;
import aarogya.pharmacy.wholeSaleReports.shared.RptWholeSaleStockSummary_Srv;
import aarogya.pharmacy.wholeSaleReports.shared.RptWholeSaleStockSummary_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class RptWholeSaleStockSummary_SrvImpl extends DmRSSPrivate implements RptWholeSaleStockSummary_Srv {
    
    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String fileName = widgetName.replaceAll(" ", "");
        FileWriter fileWriter = null;
        String sqlQuery = " ", condition = "", condition2 = "", group1Code = "", group2Code = "", group1Name = "", group2Name = "";
        String valueField = "";
        String openingValueSql = "", totalReceiveValueSql = "", purchaseValueSql = "", transferReceiveValueSql = "", otherReceiveValueSql = "", joinItemBatch = "";
        String totalIssueValueSql = "", transferIssueValueSql = "", otherIssueValueSql = "", saleValueSql = "";
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";
        String openingIncludedJoin = "";

        try {
            RptWholeSaleStockSummary_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(widgetName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                String directoryPath = DmServer.Path.sessions + "/" + sessionId;
                String serverFileName = fileName + ".csv";
                String fullFilePath = directoryPath + "/" + serverFileName;

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
                fileWriter = new FileWriter(fullFilePath, true);
            }

            if (DmShared.xNull(filter.get("group1").getTag()).trim().equalsIgnoreCase("1")) {
                group1Code = "vStk2.departmentCode";
                group1Name = "isNull(sgd.name,'')";

            } else if (DmShared.xNull(filter.get("group1").getTag()).trim().equalsIgnoreCase("2")) {
                group1Code = " im.itemGroup ";
                group1Name = "isNull(ig.name,'')";

            } else if (DmShared.xNull(filter.get("group1").getTag()).trim().equalsIgnoreCase("3")) {
                group1Code = " im.itemCategoryCode ";
                group1Name = "isNull(ic.name,'')";

            }
            if (DmShared.xNull(filter.get("group2").getTag()).trim().equalsIgnoreCase("1")) {
                group2Code = "vStk2.departmentCode";
                group2Name = "isNull(sgd.name,'')";

            } else if (DmShared.xNull(filter.get("group2").getTag()).trim().equalsIgnoreCase("2")) {
                group2Code = " im.itemGroup ";
                group2Name = "isNull(ig.name,'')";

            } else if (DmShared.xNull(filter.get("group2").getTag()).trim().equalsIgnoreCase("3")) {
                group2Code = " im.itemCategoryCode ";
                group2Name = "isNull(ic.name,'')";

            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vStk2.departmentCode in (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND vStk2.itemCode in (" + DmServer.getParamList(":medicineCode", filter.get("medicine").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup in (" + DmServer.getParamList(":medicineGroup", filter.get("medicineGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode in (" + DmServer.getParamList(":medicineCat", filter.get("medicineCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                condition2 += " and stk.ibcode in (" + DmServer.getParamList(":batchNo", filter.get("batchNo").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vStk2.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("resGrp").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.restrictGroup  in (" + DmServer.getParamList(":resGrp ", filter.get("resGrp").getTag()) + " )";
            }

            if (DmShared.xNull(filter.get("zeroValueIncludeOrExclude").getTag()).trim().equalsIgnoreCase("exclude")) {
                condition += "  and (ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0))>0";
            }
            if (DmShared.xNull(filter.get("zeroValueIncludeOrExclude").getTag()).trim().equalsIgnoreCase("include")) {
                condition += "  and (ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0))>=0";
            }
            if (DmShared.xNull(filter.get("zeroValueIncludeOrExclude").getTag()).trim().equalsIgnoreCase("zero")) {
                condition += "  and (ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0))=0";
            }

            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")) {
//                valueField = "stk.landedSkuValue";
                valueField = " (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("LW")) {

                valueField = "  (stk.landedSkuValue*100/(100+stk.totalTaxPerc))  ";

            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {
                valueField = "stk.skuValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {
                valueField = "ib.skuSaleRate";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")) {
                valueField = "ib.skuMrp";
            }

            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("LW") || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(" + valueField + ",0) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueField + ",0)) \n"
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(" + valueField + ",0) 	 \n"
                        + "						WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0	THEN ISNULL(" + valueField + ",0) \n"
                        + "						ELSE 0  \n"
                        + "					END) AS openingValue  \n";

                purchaseValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(" + valueField + ",0) ELSE  0 END AS purchaseValue  \n";

                transferReceiveValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(" + valueField + ",0) ELSE  0 END AS transferReceiveValue  \n";

                otherReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return','Sale', 'Sale Return','Stock Transfer') THEN 0 \n"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0)) \n"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(" + valueField + ",0) \n"
                        + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL(" + valueField + ",0) \n"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                               END AS otherReceiveValue ";
                totalReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(" + valueField + ",0) \n"
                        + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0  \n"
                        + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(" + valueField + ",0)  \n"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0)) \n"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(" + valueField + ",0) \n"
                        + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL(" + valueField + ",0) \n"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                                 END AS totalReceiveValue \n";

                saleValueSql = "			  , CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(" + valueField + ",0) ELSE 0 END AS saleValue  \n";
                transferIssueValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(" + valueField + ",0) ELSE 0  END AS transferIssueValue  \n";
                otherIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return', 'Stock Transfer', 'Sale', 'Sale Return') THEN 0  \n"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(" + valueField + ",0) \n"
                        + "						ELSE 0"
                        + "					END"
                        + "			END AS otherIssueValue";
                totalIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(" + valueField + ",0) \n"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0 \n"
                        + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(" + valueField + ",0)  \n"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(" + valueField + ",0) \n"
                        + "						ELSE 0"
                        + "					END"
                        + "			END AS totalIssueValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {

                joinItemBatch = " LEFT JOIN itemBatch ib ON ib.code = stk.ibCode  \n";

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(stk.recQty ,0)  \n"
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0))  \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(stk.isuQty,0))  \n"
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(stk.recQty,0)   \n"
                        + "						ELSE 0	  \n"
                        + "					END * ISNULL(" + valueField + ",0) )  AS openingValue  \n";

                purchaseValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0) ELSE 0 END  * ISNULL(" + valueField + ",0) AS purchaseValue  \n";

                transferReceiveValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(stk.recQty,0) ELSE 0 END  * ISNULL(" + valueField + ",0) AS transferReceiveValue  \n";

                otherReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return','Sale', 'Sale Return','Stock Transfer') THEN 0 \n"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) \n"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(stk.recQty,0) \n"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                               END  * ISNULL(" + valueField + ",0) AS otherReceiveValue";

                totalReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0) \n"
                        + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0  \n"
                        + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(stk.recQty,0)  \n"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) \n"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(stk.recQty,0) \n"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                                 END  * ISNULL(" + valueField + ",0) AS totalReceiveValue \n";

                saleValueSql = "			  , CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0) ELSE 0 END  * ISNULL(" + valueField + ",0) AS saleValue  \n";
                transferIssueValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0) ELSE 0  END  * ISNULL(" + valueField + ",0) AS transferIssueValue  \n";
                otherIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return','Stock Transfer','Sale', 'Sale Return') THEN 0  \n"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(stk.isuQty,0) \n"
                        + "						ELSE 0"
                        + "					END"
                        + "			END  * ISNULL(" + valueField + ",0) AS otherIssueValue";
                totalIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0) \n"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0 \n"
                        + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0)  \n"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(stk.isuQty,0) \n"
                        + "						ELSE 0"
                        + "					END"
                        + "			END  * ISNULL(" + valueField + ",0) AS totalIssueValue";
            }

            if (!DmShared.xNull(filter.get("openingInclude").getTag()).trim().equalsIgnoreCase("") && DmShared.xNull(filter.get("openingInclude").getTag()).trim().equalsIgnoreCase("1")) {
                openingIncludedJoin += "             SELECT 0 AS docid, 0 AS v_SNo, null as v_Type, null recId, 0 as v_No, DATEADD(DAY, -1, '2018-04-01') AS v_Date,  \n"
                        + "				vStk.itemCode, vStk.site_Code, vStk.departmentCode, null AS stockContraSubCode \n"
                        + "				, vStk.openingQty, vStk.openingValue \n"
                        + "				, 0 AS purchaseQty, 0 AS transferReceiveQty, 0 AS otherReceiveQty, 0 as totalReceiveQty \n"
                        + "				, 0 AS purchaseValue, 0 AS transferReceiveValue, 0 AS otherReceiveValue, 0 AS totalReceiveValue \n"
                        + "				, 0 AS saleQty, 0 AS transferIssueQty	, 0 AS otherIssueQty, 0 AS totalIssueQty \n"
                        + "				, 0 AS saleValue, 0 AS transferIssueValue, 0 AS otherIssueValue	, 0 AS totalIssueValue	 \n"
                        + "				FROM  "
                        + "				    (				 \n"
                        + "					     SELECT stk.itemCode, stk.departmentCode, stk.site_Code, SUM(ISNULL(stk.recQty,0) - ISNULL(stk.isuQty,0)) AS openingQty  \n" + openingValueSql
                        + "						 FROM stock stk  \n"
                        + "                                          INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n" + joinItemBatch
                        + "						 WHERE stk.v_Date < :fromDate  \n" + condition2
                        + "						 GROUP BY stk.itemCode, stk.departmentCode, stk.site_Code  \n"
                        + "					) AS vStk		"
                        + "				 \n"
                        + "				UNION ALL		 \n";
            }

            sqlQuery = "SELECT sm.name as site_Name, vStk2.site_Code ,vStk2.departmentCode, vStk2.itemCode , sgd.name as departmentName, isNull(im.Name,'') as itemName,im.purchaseUnitConversionRate, isNull(um.unit,'') as sku \n"
                    + "," + group1Code + " as group1Code ," + group2Code + " as group2Code," + group1Name + " as group1Name ," + group2Name + " as group2Name,"
                    + " format(vStk2.openingQty,'00.00') as openingQty"
                    + ", format(ABS(CAST(CASE WHEN ISNULL(vStk2.openingQty,0) = 0 THEN 0 ELSE ISNULL(vStk2.openingValue,0) / vStk2.openingQty END AS decimal(18,7))),'00.00')  AS openingRate \n"
                    + ",  CASE WHEN ISNULL(vStk2.openingQty,0) = 0  THEN '00.00'  ELSE  format(vStk2.openingValue,'00.00') END AS  openingValue"
                    + ", format(vStk2.purchaseQty,'00.00') as purchaseQty, format(vStk2.transferReceiveQty,'00.00') as transferReceiveQty, format(vStk2.otherReceiveQty,'00.00') as otherReceiveQty, format(vStk2.totalReceiveQty,'00.00') as  totalReceiveQty\n"
                    + ",  CASE WHEN ISNULL(vStk2.purchaseQty,0) = 0  THEN format(vStk2.purchaseValue,'00.00')  ELSE format(vStk2.purchaseValue,'00.00') END AS  purchaseValue,"
                    + " vStk2.transferReceiveValue, vStk2.otherReceiveValue \n"
                    + ", format(ABS(CAST(CASE WHEN ISNULL(vStk2.totalReceiveQty,0) = 0 THEN 0 ELSE ISNULL(vStk2.totalReceiveValue,0) / vStk2.totalReceiveQty   END AS decimal(18,7))),'00.00') AS receiveRate \n"
                    + ", CASE WHEN ISNULL(vStk2.totalReceiveQty,0) = 0  THEN '00.00'  ELSE format(vStk2.totalReceiveValue,'00.00') END AS totalReceiveValue"
                    + ", format(vStk2.saleQty,'00.00') AS saleQty, format(vStk2.transferIssueQty,'00.00') AS transferIssueQty, format(vStk2.otherIssueQty,'00.00') AS otherIssueQty, format(vStk2.totalIssueQty,'00.00') AS totalIssueQty\n"
                    + ", vStk2.saleValue, vStk2.transferIssueValue, vStk2.otherIssueValue \n"
                    + ", format(ABS(CAST(CASE WHEN ISNULL(vStk2.totalIssueQty,0) = 0 THEN 0 ELSE ISNULL(vStk2.totalIssueValue,0) / vStk2.totalIssueQty   END AS decimal(18,7))),'00.00') AS issueRate \n"
                    + ",CASE WHEN ISNULL(vStk2.totalIssueQty,0) = 0  THEN '00.00'  ELSE format(vStk2.totalIssueValue,'00.00') END AS totalIssueValue "
                    + ", format(SUM(ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0))  OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name),'00.00') AS balanceQty \n"
                    + " ,"
                    + " "
                    + " cast(( CASE WHEN ISNULL(vStk2.openingQty,0) = 0  THEN '00.00'  ELSE  format(vStk2.openingValue,'00.00') END )\n"
                    + "	+(CASE WHEN ISNULL(vStk2.totalReceiveQty,0) = 0  THEN '00.00'  ELSE  vStk2.totalReceiveValue  END)\n"
                    + "	-(CASE WHEN ISNULL(vStk2.totalIssueQty,0) = 0  THEN '00.00'  ELSE  vStk2.totalIssueValue END)\n"
                    + "					"
                    //                    + "case when SUM(ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0)\n"
                    //                    + "  ) OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name) = 0 then '00.00' \n"
                    //                    + "  else\n"
                    //                    + "  format(SUM(ISNULL(vStk2.openingValue,0) + ISNULL(vStk2.totalReceiveValue,0) - ISNULL(vStk2.totalIssueValue,0)\n"
                    //                    + "  ) OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name),'00.00') end "
                    //                    + ""
                    + ""
                    + " as decimal(18,2))as balanceValue  \n"
                    + " FROM "
                    + "      (	 \n"
                    + "	     SELECT vStk1.itemCode, vStk1.site_Code, vStk1.departmentCode \n"
                    + "		 , SUM(vStk1.openingQty) AS openingQty, SUM(vStk1.openingValue) AS openingValue \n"
                    + "		 , SUM(vStk1.purchaseQty) AS purchaseQty, SUM(vStk1.transferReceiveQty) AS transferReceiveQty, SUM(vStk1.otherReceiveQty) AS otherReceiveQty, SUM(vStk1.totalReceiveQty) AS totalReceiveQty \n"
                    + "		 , SUM(vStk1.purchaseValue) AS purchaseValue, SUM(vStk1.transferReceiveValue) AS transferReceiveValue, SUM(vStk1.otherReceiveValue) AS otherReceiveValue, SUM(vStk1.totalReceiveValue) AS totalReceiveValue \n"
                    + "		 , SUM(vStk1.saleQty) AS saleQty, SUM(vStk1.transferIssueQty) AS transferIssueQty, "
                    + "   SUM(vStk1.otherIssueQty) AS otherIssueQty, SUM(vStk1.totalIssueQty) AS totalIssueQty \n"
                    + "		 , SUM(vStk1.saleValue) AS saleValue, SUM(vStk1.transferIssueValue) AS transferIssueValue, "
                    + "   SUM(vStk1.otherIssueValue) AS otherIssueValue, SUM(vStk1.totalIssueValue) AS totalIssueValue	 \n"
                    + "		 FROM "
                    + "		    (		 \n"
                    + "	            "
                    + openingIncludedJoin
                    + "				SELECT stk.docid, stk.v_SNo, stk.v_Type, stk.recId, stk.v_No, stk.v_Date, stk.itemCode, stk.site_Code, stk.departmentCode		 \n"
                    + "				, stk.stockContraSubCode, 0 as openingQty, 0 AS openingValue \n"
                    + "				, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0) ELSE 0 END AS purchaseQty		 \n"
                    + "				, CASE WHEN vt.parentVoucher = 'Stock Transfer' THEN ISNULL(stk.recQty,0) ELSE 0 END AS transferReceiveQty		 \n"
                    + "				, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0	 \n"
                    + "						WHEN vt.parentVoucher = 'Stock Transfer' THEN 0				 \n"
                    + "						ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) ELSE ISNULL(stk.recQty,0) END	 \n"
                    + "						END AS otherReceiveQty		 \n"
                    + "				 , CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0)	 \n"
                    + "						ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) ELSE ISNULL(stk.recQty,0) END	 \n"
                    + "						END AS totalReceiveQty		 \n"
                    + "" + purchaseValueSql + transferReceiveValueSql + otherReceiveValueSql + totalReceiveValueSql
                    + "			   , 0 AS saleQty, 0 AS transferIssueQty, 0 AS otherIssueQty, 0 AS totalIssueQty \n"
                    + "			   , 0 AS saleValue, 0 AS transferIssueValue	, 0 AS otherIssueValue, 0 AS totalIssueValue		 \n"
                    + "			   FROM stock stk		 \n"
                    + "			   INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n" + joinItemBatch
                    + "			   WHERE stk.v_Date >= :fromDate		 \n"
                    + "			   AND stk.v_Date <= :toDate		 \n"
                    + "			   AND CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 1  \n"
                    + "						WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0				 \n"
                    + "						ELSE CASE WHEN (ISNULL(stk.isuQty,0) < 0 OR ISNULL(stk.recQty,0) > 0) THEN 1 ELSE 0 END			 \n"
                    + "					END = 1 " + condition2
                    + ""
                    + "			  UNION ALL  \n"
                    + ""
                    + "			  SELECT stk.docid, stk.v_SNo, stk.v_Type, stk.recId, stk.v_No, stk.v_Date, stk.itemCode, stk.site_Code, stk.departmentCode, stk.stockContraSubCode \n"
                    + "			  , 0 as openingQty, 0 AS openingValue \n"
                    + "			  , 0 AS purchaseQty, 0 AS transferReceiveQty, 0 AS otherReceiveQty, 0 as totalReceiveQty		 \n"
                    + "			  , 0 AS purchaseValue, 0 AS transferReceiveValue, 0 AS otherReceiveValue, 0 AS totalReceiveValue		 \n"
                    + "			  , CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0) ELSE 0 END AS saleQty		 \n"
                    + "			  , CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0) ELSE 0 END AS transferIssueQty		 \n"
                    + "			  , CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0 \n"
                    + "						WHEN vt.parentVoucher = 'Stock Transfer' THEN 0				 \n"
                    + "						ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) ELSE ISNULL(stk.isuQty,0) END	 \n"
                    + "						END AS otherIssueQty		 \n"
                    + "			  , CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)							 \n"
                    + "						ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) ELSE ISNULL(stk.isuQty,0) END	 \n"
                    + "						END AS totalIssueQty	 \n"
                    + "" + saleValueSql + transferIssueValueSql + otherIssueValueSql + totalIssueValueSql
                    + "			  FROM stock stk  \n" + joinItemBatch
                    + "			  INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type		 \n"
                    + "			  WHERE stk.v_Date >= :fromDate		 \n"
                    + "			  AND stk.v_Date <= :toDate		 \n"
                    + "			  AND CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 1	 \n"
                    + "						WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0				 \n"
                    + "						ELSE CASE WHEN (ISNULL(stk.isuQty,0) > 0 OR ISNULL(stk.recQty,0) < 0) THEN 1 ELSE 0 END \n"
                    + "				  END = 1  	 \n" + condition2
                    + "	    ) vStk1		"
                    + "	 GROUP BY vStk1.itemCode, vStk1.site_Code, vStk1.departmentCode	 \n"
                    + "  ) vStk2	"
                    + "  LEFT JOIN itemMast im ON im.code = vStk2.itemCode	 \n"
                    + "   LEFT JOIN itemGroup ig on im.itemGroup = ig.code\n"
                    + "  LEFT JOIN itemCat ic on ic.code = im.itemCategoryCode"
                    + "  LEFT JOIN subGroup sgd ON sgd.subCode = vStk2.departmentCode	 \n"
                    + "  LEFT JOIN siteMast sm ON sm.code = vStk2.site_Code	 \n"
                    + "  LEFT JOIN unitMast um ON um.code = im.sku	 \n"
                    + "  WHERE im.filterItemType=:filterItemType     \n" + condition
                    + "  ORDER BY  " + group1Code + "," + group2Code + ",sgd.name, im.Name";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "Group By (First),Group By (Second),Medicine ,SKU,Opening Quantity,Opening Rate ,Opening Value,Purchase (Receive),Transfer (Receive),Other (Receive),Total (Receive),"
                        + "Rate (Receive),Value (Receive),Sale (Issue),Transfer (Issue),Other (Issue),Total (Issue),Rate (Issue),Value (Issue),Closing Quantity, Closing Rate,Closing Value";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {
                    String medName = rsTemp.getString("itemName");
                    String backSlash = "";
                    if (medName.contains("\"")) {
                        backSlash = "\"";
                    }
                    lineData = "\"" + DmShared.xNull(rsTemp.getString("group1Name")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("group2Name")) + "\"" + COMMA_DELIMITER;
//                  lineData += "\"" + DmShared.xNull(rsTemp.getString("itemName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("itemName")) + backSlash + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("sku")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("openingQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("openingRate")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("openingValue")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("purchaseQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("transferReceiveQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("otherReceiveQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("totalReceiveQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("receiveRate")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("totalReceiveValue")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("saleQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("transferIssueQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("otherIssueQty")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("totalIssueQty")).toString() + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.vNull(rsTemp.getString("issueRate")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("totalIssueValue")).toString() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.vNull(rsTemp.getString("balanceQty")).toString() + "\"" + COMMA_DELIMITER;

                    double balanceRate = 0;
                    double balanceQty = 0;
                    balanceQty = DmShared.iNull(rsTemp.getString("balanceQty")).doubleValue();

                    balanceRate = DmShared.iNull(rsTemp.getString("balanceValue")).doubleValue() / (balanceQty <= 0 ? 1 : balanceQty);

                    lineData += "\"" + DmShared.iNull(balanceRate).doubleValue() + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceValue")) + "\"" + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();

            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperReport = DmServer.compileJasper("Pharmacy_StockSummary");
                jasperPrint = DmServer.fillJasper(this, new pmPharmacy().MEDICINE_STOCK_SUMMARY.caption, jasperReport, null, filter, rsTemp);
                keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }

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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("department", DmShared.xNull(filter.get("department").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCat", DmShared.xNull(filter.get("medicineCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("batchNo", DmShared.xNull(filter.get("batchNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("resGrp").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("resGrp", DmShared.xNull(filter.get("resGrp").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getGroupName(String excludeGroup, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("1")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Department");
                lstTemp.add(listNode);
            }
            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("2")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("2");
                listNode.setSharedKeyFieldName("Medicine Group");
                lstTemp.add(listNode);
            }
            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("3")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("3");
                listNode.setSharedKeyFieldName("Medicine Category");
                lstTemp.add(listNode);
            }

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHValueOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("LW");
            listNode.setSharedKeyFieldName("Landed Without Tax");
            lstTemp.add(listNode);

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
//    public DmResultPageing<List<BatchNo_Mdl>> getTxtHBatchNo(DmHelper helper, boolean blnToEndOfFile) {
    public DmResultPageing<List<BatchNo_Mdl>> getTxtHBatchNo(String medicineCodeList, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<BatchNo_Mdl> lstTemp = new ArrayList<BatchNo_Mdl>();
        BatchNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = " WHERE  im.filterItemType = :filterItemType ";
            if (!DmShared.xNull(medicineCodeList).trim().equalsIgnoreCase("")) {
                condition += " AND ib.itemCode in (" + DmServer.getParamList(":medicineCode", medicineCodeList) + " )";
            }
            sqlQuery = "SELECT"
                    + "   ib.code"
                    + " , ib.batchNo"
                    + " , ib.mrp"
                    + " , ib.saleRate"
                    + " , ib.expiryDate"
                    + " , um.unit AS skuName,im.name as medicineName"
                    + " FROM itemBatch ib"
                    + " LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + " LEFT JOIN unitMast um ON um.code = im.sku " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            if (!DmShared.xNull(medicineCodeList).trim().equalsIgnoreCase("")) {
                sqlWithParam.setStringList("medicineCode", medicineCodeList);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setData(lstTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEvaluationStock(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("F");
            listNode.setSharedKeyFieldName("Run FIFO Process");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHResGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE, NAME FROM restrictedgrpmast");

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHZeroValueIncludeOrExclude(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("include");
            listNode.setSharedKeyFieldName("Include");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("exclude");
            listNode.setSharedKeyFieldName("Exclude");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("zero");
            listNode.setSharedKeyFieldName("Zero");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOpeningInclude(DmHelper helper, boolean blnToEndOfFile) {
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
}
