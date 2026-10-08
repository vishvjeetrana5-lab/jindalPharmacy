package aarogya.inventory.reports.server;

import aarogya.inventory.reports.shared.RptStockSummary_Srv;
import aarogya.inventory.reports.shared.RptStockSummary_Vld;
import aarogya.inventory.reports.shared.TxtHItem_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
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

public class RptStockSummary_SrvImpl extends DmRSSPrivate implements RptStockSummary_Srv {

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
        String sqlQuery = " ", condition = "", condition2 = "", group1Code = "", group2Code = "", group1Name = "", group2Name = "";
        String valueField = "";
        String openingValueSql = "", totalReceiveValueSql = "", purchaseValueSql = "", transferReceiveValueSql = "", otherReceiveValueSql = "", joinItemBatch = "";
        String totalIssueValueSql = "", transferIssueValueSql = "", otherIssueValueSql = "";
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";
        String fileName = widgetName.replaceAll(" ", "");
        FileWriter fileWriter = null;

        try {
            RptStockSummary_Vld.validateInput(filter);
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
                group1Name = "sgd.name";

            } else if (DmShared.xNull(filter.get("group1").getTag()).trim().equalsIgnoreCase("2")) {
                group1Code = " im.itemGroup ";
                group1Name = "ig.name";

            } else if (DmShared.xNull(filter.get("group1").getTag()).trim().equalsIgnoreCase("3")) {
                group1Code = " im.itemCategoryCode ";
                group1Name = "ic.name";

            }
            if (DmShared.xNull(filter.get("group2").getTag()).trim().equalsIgnoreCase("1")) {
                group2Code = "vStk2.departmentCode";
                group2Name = "sgd.name";

            } else if (DmShared.xNull(filter.get("group2").getTag()).trim().equalsIgnoreCase("2")) {
                group2Code = " im.itemGroup ";
                group2Name = "ig.name";

            } else if (DmShared.xNull(filter.get("group2").getTag()).trim().equalsIgnoreCase("3")) {
                group2Code = " im.itemCategoryCode ";
                group2Name = "ic.name";

            }

            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vStk2.departmentCode in (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND vStk2.itemCode in (" + DmServer.getParamList(":itemCode", filter.get("item").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup in (" + DmServer.getParamList(":itemGroup", filter.get("itemGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode in (" + DmServer.getParamList(":itemCat", filter.get("itemCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vStk2.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
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
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  im.marginRateFix IN (" + DmServer.getParamList(":rateMarginFix ", filter.get("rateMarginFix").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemMakeCode in (" + DmServer.getParamList(":medCompany", filter.get("medCompany").getTag()) + " )";
            }

            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")) {
//                valueField = "stk.landedSkuValue";
                valueField = " (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {
                valueField = "stk.skuValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {
                valueField = "ib.skuSaleRate";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")) {
                valueField = "ib.skuMrp";
            }

            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(" + valueField + ",0)"
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueField + ",0))"
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(" + valueField + ",0) 	"
                        + "						WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0	THEN ISNULL(" + valueField + ",0)"
                        + "						ELSE 0 "
                        + "					END) AS openingValue ";

                purchaseValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(" + valueField + ",0) ELSE  0 END AS purchaseValue ";

                transferReceiveValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(" + valueField + ",0) ELSE  0 END AS transferValue ";

                otherReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return','Sale', 'Sale Return','Stock Transfer') THEN 0"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(" + valueField + ",0)"
                        + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL(" + valueField + ",0)"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                               END AS otherReceiveValue";
                totalReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(" + valueField + ",0)"
                        + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0 "
                        + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(" + valueField + ",0) "
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(" + valueField + ",0)"
                        + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL(" + valueField + ",0)"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                                 END AS totalReceiveValue";

                transferIssueValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(" + valueField + ",0) ELSE 0  END AS transferIssueValue ";
                otherIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(" + valueField + ",0)"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return','Stock Transfer') THEN 0 "
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(" + valueField + ",0)"
                        + "						ELSE 0"
                        + "					END"
                        + "			END AS otherIssueValue";
                totalIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(" + valueField + ",0)"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0"
                        + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(" + valueField + ",0) "
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(" + valueField + ",0))"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(" + valueField + ",0)"
                        + "						ELSE 0"
                        + "					END"
                        + "			END AS totalIssueValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {

                joinItemBatch = " LEFT JOIN itemBatch ib ON ib.code = stk.ibCode ";

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(stk.recQty ,0) "
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) "
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(stk.isuQty,0)) "
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(stk.recQty,0)  "
                        + "						ELSE 0	 "
                        + "					END * ISNULL(" + valueField + ",0) )  AS openingValue ";

                purchaseValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0) ELSE 0 END  * ISNULL(" + valueField + ",0) AS purchaseValue ";

                transferReceiveValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(stk.recQty,0) ELSE 0 END  * ISNULL(" + valueField + ",0) AS transferValue ";

                otherReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return','Sale', 'Sale Return','Stock Transfer') THEN 0"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0))"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(stk.recQty,0)"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                               END  * ISNULL(" + valueField + ",0) AS otherReceiveValue";

                totalReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0)"
                        + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0 "
                        + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(stk.recQty,0) "
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0))"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(stk.recQty,0)"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                                 END  * ISNULL(" + valueField + ",0) AS totalReceiveValue";

                transferIssueValueSql = "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0) ELSE 0  END  * ISNULL(" + valueField + ",0) AS transferIssueValue ";
                otherIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return','Stock Transfer') THEN 0 "
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0))"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(stk.isuQty,0)"
                        + "						ELSE 0"
                        + "					END"
                        + "			END  * ISNULL(" + valueField + ",0) AS otherIssueValue";
                totalIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0"
                        + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0) "
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0))"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(stk.isuQty,0)"
                        + "						ELSE 0"
                        + "					END"
                        + "			END  * ISNULL(" + valueField + ",0) AS totalIssueValue";
            }

            sqlQuery = "SELECT sm.name as site_Name, vStk2.site_Code ,vStk2.departmentCode, vStk2.itemCode "
                    + ", sgd.name as departmentName"
                    + ", im.Name as itemName,imm.companyName As medicineCompany,im.purchaseUnitConversionRate"
                    + ", um.unit as sku"
                    + "," + group1Code + " as group1Code ," + group2Code + " as group2Code," + group1Name + " as group1Name ," + group2Name + " as group2Name"
                    + ", format(vStk2.openingQty,'00.00') as openingQty"
                    + ", format(ABS(CAST(CASE WHEN ISNULL(vStk2.openingQty,0) = 0 THEN 0 ELSE ISNULL(vStk2.openingValue,0) / vStk2.openingQty END AS decimal(18,7))),'00.00') AS openingRate"
                    + ",  CASE WHEN ISNULL(vStk2.openingQty,0) = 0  THEN '00.00'  ELSE  format(vStk2.openingValue,'00.00') END AS  openingValue"
                    + ", format(vStk2.purchaseQty,'00.00') as purchaseQty"
                    + ", format(vStk2.transferReceiveQty,'00.00') as transferReceiveQty"
                    + ", format(vStk2.otherReceiveQty,'00.00') as otherReceiveQty"
                    + ", format(vStk2.totalReceiveQty,'00.00') as totalReceiveQty"
                    + ", CASE WHEN ISNULL(vStk2.purchaseQty,0) = 0  THEN '00.00'  ELSE format(vStk2.purchaseValue,'00.00') END AS  purchaseValue"
                    //  + ", format(vStk2.saleQty,'00.00') AS saleQty"

                    + ", format(vStk2.transferReceiveValue,'00.00') as transferReceiveValue"
                    + ", format(vStk2.otherReceiveValue,'00.00') as otherReceiveValue"
                    + ", format(ABS(CAST(CASE WHEN ISNULL(vStk2.totalReceiveQty,0) = 0 THEN 0 ELSE ISNULL(vStk2.totalReceiveValue,0) / vStk2.totalReceiveQty END AS decimal(18,7))),'00.00') as  receiveRate"
                    + ",CASE WHEN ISNULL(vStk2.totalReceiveQty,0) = 0  THEN '00.00'  ELSE format(vStk2.totalReceiveValue,'00.00') END AS totalReceiveValue "
                    + ", format(vStk2.transferIssueQty,'00.00') as transferIssueQty"
                    + ", format(vStk2.otherIssueQty,'00.00') as otherIssueQty"
                    + ", format(vStk2.totalIssueQty,'00.00') as totalIssueQty"
                    + ", format(vStk2.transferIssueValue,'00.00') as transferIssueValue"
                    + ", format(vStk2.otherIssueValue,'00.00') as otherIssueValue"
                    + ", format(ABS(CAST(CASE WHEN ISNULL(vStk2.totalReceiveValue,0) = 0 THEN 0 ELSE ISNULL(vStk2.totalIssueValue,0) / vStk2.totalReceiveValue END AS decimal(18,7))),'00.00') as  issueRate"
                    + ",CASE WHEN ISNULL(vStk2.totalIssueQty,0) = 0  THEN '00.00'  ELSE format(vStk2.totalIssueValue,'00.00') END AS totalIssueValue"
                    + ", format(SUM(ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0)) OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name),'00.00') as  balanceQty"
                    + ",case when SUM(ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0)\n"
                    + "  ) OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name) = 0 then '00.00' \n"
                    + "  else\n"
                    + "  format(SUM(ISNULL(vStk2.openingValue,0) + ISNULL(vStk2.totalReceiveValue,0) - ISNULL(vStk2.totalIssueValue,0)\n"
                    + "  ) OVER (PARTITION BY sm.name, sgd.name, im.Name ORDER BY sm.name, sgd.name, im.Name),'00.00') end as balanceValue "
                    + " FROM ("
                    + "	SELECT vStk1.itemCode"
                    + "	, vStk1.site_Code"
                    + "	, vStk1.departmentCode"
                    + "	, SUM(vStk1.openingQty) AS openingQty"
                    + "	, SUM(vStk1.openingValue) AS openingValue"
                    + "	, SUM(vStk1.purchaseQty) AS purchaseQty"
                    + "	, SUM(vStk1.transferReceiveQty) AS transferReceiveQty"
                    + "	, SUM(vStk1.otherReceiveQty) AS otherReceiveQty"
                    + "	, SUM(vStk1.totalReceiveQty) AS totalReceiveQty"
                    + "	, SUM(vStk1.purchaseValue) AS purchaseValue"
                    + "	, SUM(vStk1.transferReceiveValue) AS transferReceiveValue"
                    + "	, SUM(vStk1.otherReceiveValue) AS otherReceiveValue"
                    + "	, SUM(vStk1.totalReceiveValue) AS totalReceiveValue"
                    + "	, SUM(vStk1.transferIssueQty) AS transferIssueQty"
                    + "	, SUM(vStk1.otherIssueQty) AS otherIssueQty"
                    + "	, SUM(vStk1.totalIssueQty) AS totalIssueQty"
                    + "	, SUM(vStk1.transferIssueValue) AS transferIssueValue"
                    + "	, SUM(vStk1.otherIssueValue) AS otherIssueValue"
                    + "	, SUM(vStk1.totalIssueValue) AS totalIssueValue"
                    + "	 FROM ("
                    + "		SELECT 0 AS docid, 0 AS v_SNo, null as v_Type, null recId, 0 as v_No"
                    + "		, DATEADD(DAY, -1, :fromdate) AS v_Date"
                    + "		, vStk.itemCode"
                    + "		, vStk.site_Code"
                    + "		, vStk.departmentCode"
                    + "		, null AS stockContraSubCode"
                    + "		, vStk.openingQty"
                    + "		, vStk.openingValue"
                    + "		, 0 AS purchaseQty"
                    + "		, 0 AS transferReceiveQty"
                    + "		, 0 AS otherReceiveQty"
                    + "		, 0 as totalReceiveQty"
                    + "		, 0 AS purchaseValue"
                    + "		, 0 AS transferReceiveValue"
                    + "		, 0 AS otherReceiveValue"
                    + "		, 0 AS totalReceiveValue"
                    + "		, 0 AS transferIssueQty"
                    + "		, 0 AS otherIssueQty"
                    + "		, 0 AS totalIssueQty"
                    + "		, 0 AS transferIssueValue"
                    + "		, 0 AS otherIssueValue"
                    + "		, 0 AS totalIssueValue	FROM ("
                    + "				SELECT stk.itemCode"
                    + "				, stk.departmentCode"
                    + "				, stk.site_Code"
                    + "				, SUM(ISNULL(stk.recQty,0) - ISNULL(stk.isuQty,0)) AS openingQty " + openingValueSql
                    + "				FROM stock stk "
                    + "                         INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type " + joinItemBatch
                    + "				WHERE stk.v_Date < :fromDate " + condition2
                    + "				GROUP BY stk.itemCode"
                    + "				, stk.departmentCode"
                    + "				, stk.site_Code"
                    + "			) AS vStk"
                    + "		UNION ALL"
                    + "		SELECT stk.docid, stk.v_SNo, stk.v_Type, stk.recId, stk.v_No, stk.v_Date"
                    + "		, stk.itemCode"
                    + "		, stk.site_Code"
                    + "		, stk.departmentCode"
                    + "		, stk.stockContraSubCode"
                    + "		, 0 as openingQty"
                    + "		, 0 AS openingValue"
                    + "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0) ELSE 0 END AS purchaseQty"
                    + "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' THEN ISNULL(stk.recQty,0) ELSE 0 END AS transferReceiveQty"
                    + "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0"
                    + "				WHEN vt.parentVoucher = 'Stock Transfer' THEN 0"
                    + "				ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) ELSE ISNULL(stk.recQty,0) END"
                    + "			END AS otherReceiveQty"
                    + "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0)					"
                    + "				ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) ELSE ISNULL(stk.recQty,0) END"
                    + "			END AS totalReceiveQty "
                    + "" + purchaseValueSql + transferReceiveValueSql + otherReceiveValueSql + totalReceiveValueSql
                    + "		, 0 AS transferIssueQty"
                    + "		, 0 AS otherIssueQty"
                    + "		, 0 AS totalIssueQty"
                    + "		, 0 AS transferIssueValue"
                    + "		, 0 AS otherIssueValue"
                    + "		, 0 AS totalIssueValue"
                    + "		FROM stock stk "
                    + "		INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type " + joinItemBatch
                    + "		WHERE stk.v_Date >= :fromDate"
                    + "		AND stk.v_Date <= :toDate"
                    + "		AND CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 1"
                    + "				WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0"
                    + "				ELSE CASE WHEN (ISNULL(stk.isuQty,0) < 0 OR ISNULL(stk.recQty,0) > 0 OR (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0)) = 0) THEN 1 ELSE 0 END"
                    + "			END = 1 " + condition2
                    + ""
                    + "		UNION ALL"
                    + ""
                    + "		SELECT stk.docid, stk.v_SNo, stk.v_Type, stk.recId, stk.v_No, stk.v_Date"
                    + "		, stk.itemCode"
                    + "		, stk.site_Code"
                    + "		, stk.departmentCode"
                    + "		, stk.stockContraSubCode"
                    + "		, 0 as openingQty"
                    + "		, 0 AS openingValue"
                    + "		, 0 AS purchaseQty"
                    + "		, 0 AS transferReceiveQty"
                    + "		, 0 AS otherReceiveQty"
                    + "		, 0 as totalReceiveQty"
                    + "		, 0 AS purchaseValue"
                    + "		, 0 AS transferReceiveValue"
                    + "		, 0 AS otherReceiveValue"
                    + "		, 0 AS totalReceiveValue"
                    + "		, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0) ELSE 0 END AS transferIssueQty"
                    + "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)			"
                    + "				WHEN vt.parentVoucher = 'Stock Transfer' THEN 0"
                    + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) ELSE ISNULL(stk.isuQty,0) END"
                    + "			END AS otherIssueQty"
                    + "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)			"
                    + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) ELSE ISNULL(stk.isuQty,0) END"
                    + "			END AS totalIssueQty"
                    + "" + transferIssueValueSql + otherIssueValueSql + totalIssueValueSql
                    + "		FROM stock stk "
                    + "		INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type " + joinItemBatch
                    + "		WHERE stk.v_Date >= :fromDate"
                    + "		AND stk.v_Date <= :toDate"
                    + "		AND CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 1"
                    + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0"
                    + "				ELSE CASE WHEN (ISNULL(stk.isuQty,0) > 0 OR ISNULL(stk.recQty,0) < 0) THEN 1 ELSE 0 END"
                    + "			END = 1  " + condition2
                    + "		) vStk1"
                    + "		GROUP BY vStk1.itemCode, vStk1.site_Code, vStk1.departmentCode"
                    + "	) vStk2"
                    + "	LEFT JOIN itemMast im ON im.code = vStk2.itemCode"
                    + " LEFT JOIN itemGroup ig on im.itemGroup = ig.code\n"
                    + " LEFT JOIN itemCat ic on ic.code = im.itemCategoryCode"
                    + "	LEFT JOIN subGroup sgd ON sgd.subCode = vStk2.departmentCode"
                    + "	LEFT JOIN siteMast sm ON sm.code = vStk2.site_Code"
                    + "	LEFT JOIN unitMast um ON um.code = im.sku"
                    + "  LEFT JOIN itemmakemast imm ON imm.code = im.itemMakeCode	 \n"
                    + "	WHERE   im.filterItemType=:filterItemType  " + condition
                    + "	ORDER BY " + group1Code + "," + group2Code + ",sm.name, im.Name";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "Group By (First),Group By (Second),Medicine,Company Name,SKU,Opening Quantity,Opening Rate ,Opening Value,Purchase (Receive),Transfer (Receive),Other (Receive),Total (Receive),"
                        + "Rate (Receive),Value (Receive),Transfer (Issue),Other (Issue),Total (Issue),Rate (Issue),Value (Issue),Closing Quantity, Closing Rate,Closing Value";

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
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("medicineCompany")) + backSlash + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("sku")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("openingQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("openingRate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("openingValue")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("transferReceiveQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("otherReceiveQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("totalReceiveQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("receiveRate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("totalReceiveValue")) + "\"" + COMMA_DELIMITER;
                    //  lineData += "\"" + DmShared.xNull(rsTemp.getString("saleQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("transferIssueQty")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("otherIssueQty")) + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("totalIssueQty")) + "\"" + COMMA_DELIMITER;

                    double issueRate = 0;
                    double totalIssueQty = 0;
                    totalIssueQty = DmShared.iNull(rsTemp.getString("totalIssueQty")).doubleValue();
                    issueRate = DmShared.iNull(rsTemp.getString("totalIssueValue")).doubleValue() / (totalIssueQty <= 0 ? 1 : totalIssueQty);

                    lineData += "\"" + DmShared.iNull(issueRate).doubleValue() + "\"" + COMMA_DELIMITER;
                    //  lineData += "\"" + DmShared.xNull(rsTemp.getString("issueRate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("totalIssueValue")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceQty")) + "\"" + COMMA_DELIMITER;

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
                jasperReport = DmServer.compileJasper("Inventory_StockSummary");
                jasperPrint = DmServer.fillJasper(this, "Stock Summary", jasperReport, null, filter, rsTemp);
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
            if (!DmShared.xNull(filter.get("item").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", DmShared.xNull(filter.get("item").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemCat", DmShared.xNull(filter.get("itemCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemGroup", DmShared.xNull(filter.get("itemGroup").getTag()));
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("rateMarginFix", DmShared.xNull(filter.get("rateMarginFix").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCompany", DmShared.xNull(filter.get("medCompany").getTag()));
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
                    + "         where sgn.manualCode IN ('CM','PR') and sg.isActive = 1 ");
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
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode"
                    + " FROM itemMast im"
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemCat(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " FROM itemCat ic"
                    + " WHERE (ic.filterItemType = :filterItemType or isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemGroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
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
                listNode.setSharedKeyFieldName("Item Group");
                lstTemp.add(listNode);
            }
            if (!DmShared.xNull(excludeGroup).equalsIgnoreCase("3")) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("3");
                listNode.setSharedKeyFieldName("Item Category");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRateMarginFix(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Rate Fix");
            listNode.setSharedKeyFieldName("Rate Fix");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Margin Fix");
            listNode.setSharedKeyFieldName("Margin Fix");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedCompany(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT CODE, companyName as NAME FROM itemmakemast");
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

}
