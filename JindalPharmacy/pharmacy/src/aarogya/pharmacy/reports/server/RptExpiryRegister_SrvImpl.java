package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.BatchNo_Mdl;
import aarogya.pharmacy.reports.shared.RptExpiryRegister_Srv;
import aarogya.pharmacy.reports.shared.RptExpiryRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.ReportsOtherParameters_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptExpiryRegister_SrvImpl extends DmRSSPrivate implements RptExpiryRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, ReportsOtherParameters_Mdl othp) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        DmPreparedStatement psTemp = null, psTempSubReport = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DmIJasperCompiled jasperCompiled = null;
        String sqlQuery = "", subReportQuery = "", mainCondition = "", subQueryCondition = "", valueOnField = "", expiryCondition = "", groupByFieldCode = "", groupByFieldName = "";
        String totalIssueValueSql = "", totalReceiveValueSql = "", openingValueSql = "", orderByCondition = "";
        String innerQueryCondition = "";
        KeyFieldExtended keyField = new KeyFieldExtended();
        try {

            RptExpiryRegister_Vld.validateInput(filter);
            if (othp.isCallFromScheduler()) {
                cnTemp = DmServer.getConnection(othp.getActiveDB());
            } else {
                cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            }

            cnTempCmp = DmServer.getConnection(DmServer.dbCompany);
            String startDate = DmServer.executeScalar(" select top 1  format(start_Dt,'dd-MMM-yyyy') start_Dt    from company order by start_Dt desc ", cnTempCmp);
            cnTempCmp.close();

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                innerQueryCondition += "and ib.expiryDate >= :fromDate";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                innerQueryCondition += " and ib.expiryDate <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("department").getTag()).trim().equalsIgnoreCase("")) {
                innerQueryCondition += " and  stk.departmentCode IN (" + DmServer.getParamList(":department", filter.get("department").getTag()) + " ) ";
            }
            if (filter.get("batchNo") != null) {
                if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                    innerQueryCondition += " and stk.ibCode in (" + DmServer.getParamList(":batchNo", filter.get("batchNo").getTag()) + " )";
                }
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                innerQueryCondition += " AND stk.itemCode in (" + DmServer.getParamList(":medicineCode", filter.get("medicine").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " and im.itemGroup in (" + DmServer.getParamList(":medGroup", filter.get("medGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " and im.itemCategoryCode in (" + DmServer.getParamList(":medCat", filter.get("medCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " and im.itemMakeCode in (" + DmServer.getParamList(":medCompany", filter.get("medCompany").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Medicine Category")) {
                groupByFieldCode = " im.itemCategoryCode ";
                groupByFieldName = " ic.name  ";
                orderByCondition = "ORDER BY  im.itemCategoryCode,vStk2.departmentCode, im.itemGroup ,sgd.name, im.Name";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Medicine Group")) {
                groupByFieldCode = "  im.itemGroup ";
                groupByFieldName = " ig.name  ";

                orderByCondition = "ORDER BY  im.itemGroup,vStk2.departmentCode,sgd.name, im.Name";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("Medicine Company")) {
                groupByFieldCode = "im.itemMakeCode ";
                groupByFieldName = "imm.companyName";
                orderByCondition = "ORDER BY  im.itemMakeCode,vStk2.departmentCode, im.itemGroup ,sgd.name, im.Name";
            } else if (DmShared.xNull(filter.get("groupBy").getTag()).trim().equalsIgnoreCase("")) {
                groupByFieldCode = "  '' ";
                groupByFieldName = " '' ";
                orderByCondition = "ORDER BY  vStk2.departmentCode, im.itemGroup ,sgd.name, im.Name";
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                subQueryCondition += " AND vStk2.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            } else {
                subQueryCondition += " AND vStk2.site_Code IN (" + DmServer.getParamList(":site_Code ", DmServer.getSsnSiteList(this)) + " )";
            }

            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")) {
                valueOnField = " (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {
                valueOnField = "stk.skuValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {
                valueOnField = "ib.skuSaleRate";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")) {
                valueOnField = "ib.skuMrp";
            }

            if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("L")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("P")) {

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(" + valueOnField + ",0) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(" + valueOnField + ",0)) \n"
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(" + valueOnField + ",0) 	 \n"
                        + "						WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0	THEN ISNULL(" + valueOnField + ",0) \n"
                        + "						ELSE 0  \n"
                        + "					END) AS openingValue  \n";

                totalReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(" + valueOnField + ",0) \n"
                        + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0  \n"
                        + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(" + valueOnField + ",0)  \n"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0)) \n"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(" + valueOnField + ",0) \n"
                        + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL(" + valueOnField + ",0) \n"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                                 END AS totalReceiveValue \n";

                totalIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(" + valueOnField + ",0) \n"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0 \n"
                        + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(" + valueOnField + ",0)  \n"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(" + valueOnField + ",0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(" + valueOnField + ",0) \n"
                        + "						ELSE 0"
                        + "					END"
                        + "			END AS totalIssueValue";
            } else if (DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("M")
                    || DmShared.xNull(filter.get("valueOn").getTag()).trim().equalsIgnoreCase("S")) {

                openingValueSql = "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL(stk.recQty ,0)  \n"
                        + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0))  \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL(stk.isuQty,0))  \n"
                        + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL(stk.recQty,0)   \n"
                        + "						ELSE 0	  \n"
                        + "					END * ISNULL(" + valueOnField + ",0) )  AS openingValue  \n";

                totalReceiveValueSql = "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0) \n"
                        + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0  \n"
                        + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL(stk.recQty,0)  \n"
                        + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) \n"
                        + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL(stk.recQty,0) \n"
                        + "							ELSE 0 "
                        + "                                         END"
                        + "                                 END  * ISNULL(" + valueOnField + ",0) AS totalReceiveValue \n";

                totalIssueValueSql = "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0) \n"
                        + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0 \n"
                        + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0)  \n"
                        + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) \n"
                        + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL(stk.isuQty,0) \n"
                        + "						ELSE 0"
                        + "					END"
                        + "			END  * ISNULL(" + valueOnField + ",0) AS totalIssueValue";
            }

            sqlQuery = " select batchNo,vStk2.mrp,vStk2.code," + groupByFieldCode + "      as groupCode," + groupByFieldName + "     as groupName,vStk2.itemCode,vStk2.expiryDate,\n"
                    + " vStk2.departmentCode,sgd.name as department, im.name as itemName,imm.companyName as medcompany,\n"
                    + " format(SUM(ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0))  OVER (PARTITION BY sm.name, sgd.name, im.Name,vStk2.code ORDER BY sm.name, sgd.name, im.Name),'00.00') AS balanceQty,\n"
                    + "\n"
                    + "  case when SUM(ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0)\n"
                    + "  ) OVER (PARTITION BY sm.name, sgd.name, im.Name,vStk2.code ORDER BY sm.name, sgd.name, im.Name) = 0 then '00.00' \n"
                    + "  else\n"
                    + "  format(SUM(ISNULL(vStk2.openingValue,0) + ISNULL(vStk2.totalReceiveValue,0) - ISNULL(vStk2.totalIssueValue,0)\n"
                    + "  ) OVER (PARTITION BY sm.name, sgd.name, im.Name,vStk2.code ORDER BY sm.name, sgd.name, im.Name),'00.00') end as expiryValue\n"
                    + "\n"
                    + " from (\n"
                    + " \n"
                    + " SELECT max(batchNo) as batchNo,max(mrp) as mrp,max(vStk1.code) as code,max(vStk1.expiryDate) as expiryDate,vStk1.itemCode, vStk1.site_Code, vStk1.departmentCode \n"
                    + ", SUM(vStk1.openingQty) AS openingQty, \n"
                    + "SUM(vStk1.purchaseQty) AS purchaseQty, SUM(vStk1.transferReceiveQty) AS transferReceiveQty, SUM(vStk1.otherReceiveQty) AS otherReceiveQty, SUM(vStk1.totalReceiveQty) AS totalReceiveQty ,\n"
                    + " SUM(vStk1.saleQty) AS saleQty, SUM(vStk1.transferIssueQty) AS transferIssueQty, SUM(vStk1.otherIssueQty) AS otherIssueQty, SUM(vStk1.totalIssueQty) AS totalIssueQty, SUM(vStk1.openingValue) AS openingValue ,\n"
                    + " SUM(vStk1.totalReceiveValue) AS totalReceiveValue ,\n"
                    + " SUM(vStk1.totalIssueValue) AS totalIssueValue \n"
                    + " \n"
                    + "FROM 		   \n"
                    + "(\n"
                    + "		 \n"
                    + "\n"
                    + "SELECT batchNo,mrp,code,vStk.expiryDate,0 AS docid, 0 AS v_SNo, null as v_Type, null recId, 0 as v_No, DATEADD(DAY, -1, '2018-04-01') AS v_Date,  \n"
                    + "vStk.itemCode, vStk.site_Code, vStk.departmentCode, null AS stockContraSubCode \n"
                    + ", vStk.openingQty\n"
                    + ", 0 AS purchaseQty, 0 AS transferReceiveQty, 0 AS otherReceiveQty, 0 as totalReceiveQty \n"
                    + ", 0 AS saleQty, 0 AS transferIssueQty	, 0 AS otherIssueQty, 0 AS totalIssueQty,vStk.openingValue\n"
                    + ",0 AS totalReceiveValue , 0 AS totalIssueValue		 \n"
                    + "FROM  				    \n"
                    + "(				 \n"
                    + "			SELECT max(ib.batchNo) as batchNo,max(ib.mrp) as mrp,max(stk.ibCode) as code,max(ib.expiryDate) as expiryDate,stk.itemCode, stk.departmentCode, stk.site_Code, SUM(ISNULL(stk.recQty,0) - ISNULL(stk.isuQty,0)) AS openingQty\n"
                    + openingValueSql
                    + "			 \n"
                    + "			FROM stock stk \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1 and"
                    + "                 stk.v_Date < :startDate  \n" + innerQueryCondition
                    + "			GROUP BY stk.itemCode, stk.departmentCode, stk.site_Code,stk.ibCode   \n"
                    + "					) AS vStk						 \n"
                    + "				\n"
                    + "				\n"
                    + "UNION ALL		 \n"
                    + "	            				\n"
                    + "								\n"
                    + "			SELECT ib.batchNo,ib.mrp,stk.ibCode as code,ib.expiryDate,stk.docid, stk.v_SNo, stk.v_Type, stk.recId, stk.v_No, stk.v_Date, stk.itemCode, stk.site_Code, stk.departmentCode		 \n"
                    + "			, stk.stockContraSubCode, 0 as openingQty\n"
                    + "			, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0) ELSE 0 END AS purchaseQty		 \n"
                    + "			, CASE WHEN vt.parentVoucher = 'Stock Transfer' THEN ISNULL(stk.recQty,0) ELSE 0 END AS transferReceiveQty		 \n"
                    + "			, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0	 \n"
                    + "			WHEN vt.parentVoucher = 'Stock Transfer' THEN 0				 \n"
                    + "			ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) ELSE ISNULL(stk.recQty,0) END	 \n"
                    + "			END AS otherReceiveQty		 \n"
                    + "			, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL(stk.recQty,0)	 \n"
                    + "			ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL(stk.isuQty,0)) ELSE ISNULL(stk.recQty,0) END	 \n"
                    + "			END AS totalReceiveQty\n"
                    + "			, 0 AS saleQty, 0 AS transferIssueQty, 0 AS otherIssueQty, 0 AS totalIssueQty,\n"
                    + "			\n"
                    + "			0 AS openingValue \n" + totalReceiveValueSql
                    + " ,0 AS totalIssueValue	\n"
                    + "\n"
                    + "\n"
                    + "			FROM stock stk	\n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1"
                    + "                 and stk.v_Date >= :startDate		 \n" + innerQueryCondition
                    //                    + "			AND stk.v_Date <= '2023-05-06'		 \n"
                    + "			AND CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 1  \n"
                    + "			WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0				 \n"
                    + "			ELSE CASE WHEN (ISNULL(stk.isuQty,0) < 0 OR ISNULL(stk.recQty,0) > 0) THEN 1 ELSE 0 END			 \n"
                    + "			END = 1  \n"
                    + "					\n"
                    + "UNION ALL  \n"
                    + "			SELECT ib.batchNo,ib.mrp,stk.ibCode as code,ib.expiryDate,stk.docid, stk.v_SNo, stk.v_Type, stk.recId, stk.v_No, stk.v_Date, stk.itemCode, stk.site_Code, stk.departmentCode, stk.stockContraSubCode \n"
                    + "			, 0 as openingQty, \n"
                    + "			 0 AS purchaseQty, 0 AS transferReceiveQty, 0 AS otherReceiveQty, 0 as totalReceiveQty		 \n"
                    + "\n"
                    + "			, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0) ELSE 0 END AS saleQty	\n"
                    + "			\n"
                    + "			, CASE WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL(stk.isuQty,0) ELSE 0 END AS transferIssueQty		\n"
                    + "			\n"
                    + "			, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0 \n"
                    + "			WHEN vt.parentVoucher = 'Stock Transfer' THEN 0				 \n"
                    + "			ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) ELSE ISNULL(stk.isuQty,0) END	 \n"
                    + "			END AS otherIssueQty\n"
                    + "			\n"
                    + "			, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL(stk.isuQty,0)							 \n"
                    + "			ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL(stk.recQty,0)) ELSE ISNULL(stk.isuQty,0) END	 \n"
                    + "			END AS totalIssueQty,\n"
                    + "			0 AS openingValue ,\n"
                    + "			0 AS totalReceiveValue	\n" + totalIssueValueSql
                    + "\n"
                    + "\n"
                    + "                 FROM stock stk  \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type		 \n"
                    + "			WHERE 1=1"
                    + "                 and stk.v_Date >= :startDate		 \n" + innerQueryCondition
                    //                    + "			AND stk.v_Date <= '2023-05-06'		 \n"
                    + "			AND CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 1	 \n"
                    + "			WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0				 \n"
                    + "			ELSE CASE WHEN (ISNULL(stk.isuQty,0) > 0 OR ISNULL(stk.recQty,0) < 0) THEN 1 ELSE 0 END \n"
                    + "			END = 1  	 \n"
                    + " \n"
                    + " \n"
                    + " ) vStk1			\n"
                    + " GROUP BY vStk1.itemCode, vStk1.site_Code, vStk1.code, vStk1.departmentCode	\n"
                    + "\n"
                    + " \n"
                    + "  ) vStk2	 \n"
                    + "  \n"
                    + "  \n"
                    + "  LEFT JOIN itemMast im ON im.code = vStk2.itemCode	 \n"
                    + "  LEFT JOIN itemGroup ig on im.itemGroup = ig.code\n"
                    + "  LEFT JOIN itemCat ic on ic.code = im.itemCategoryCode \n"
                    + "  left join itemMakeMast as imm on im.itemmakeCode=imm.code \n"
                    + "  LEFT JOIN subGroup sgd ON sgd.subCode = vStk2.departmentCode	 \n"
                    + "  LEFT JOIN siteMast sm ON sm.code = vStk2.site_Code	 \n"
                    + "  LEFT JOIN unitMast um ON um.code = im.sku \n"
                    + "  WHERE im.filterItemType=:filterItemType     \n" + subQueryCondition
                    + "  and isNull(vStk2.expiryDate,'') <> ''  and ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0) <> 0\n"
                    + orderByCondition;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            psTemp.setDate("startDate", DmServer.convertDate(startDate));
            if (othp.isCallFromScheduler()) {
                getParameter(filter, psTemp, othp.getSiteCode());
            } else {
                getParameter(filter, psTemp, DmServer.getSsnSiteList(this));
            }

            rsTemp = psTemp.executeQuery();

            subReportQuery = " SELECT "
                    + "	 pb.docId as pBillDocId "
                    + "	,pb.recId as voucherNo "
                    + "	,pb.v_Date as voucherDate "
                    + "	,pb.partyBillNo as pBillNo "
                    + "	,pb.partyBillDate as pBillDate "
                    + "	,CONCAT(sg.initial,'',sg.name,',',ct.cityName) as supplierName "
                    + "	,pb.subCode as supplierCode "
                    + "	from pbilldetail pbd "
                    + "	LEFT JOIN pbill pb ON pb.docId=pbd.docId "
                    + "	LEFT JOIN voucher_Type vt ON vt.v_Type=pb.v_Type "
                    + "	LEFT JOIN subGroup sg ON sg.subCode=pb.subCode "
                    + " left join city as ct on sg.cityCode = ct.cityCode "
                    + "	WHERE pbd.ibCode=$P{code} "
                    + " AND pbd.isFooter = 0"
                    + " AND ISNULL(pb.approvedBy,'')<>'' "
                    + " AND pb.v_Date <= '" + DmServer.convertDate(filter.get("toDate").getText()) + "'"
                    + "	AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "')  ";

            HashMap hm = new HashMap();
            jasperCompiled = DmServer.compileJasper("Pharmacy_ExpiryRegister", "", true);
            hm.put("REPORT_CONNECTION", cnTemp);
            if (DmShared.xNull(filter.get("purchaseDetail").getTag()).trim().equalsIgnoreCase("1")) {
                hm.putAll(jasperCompiled.getSubReports());
            }
            hm.put("subReportQuery", subReportQuery);
            jasperReport = DmServer.compileJasper("Pharmacy_ExpiryRegister");

            if (othp.isCallFromScheduler()) {

                HashMap parameter = STServer.setJasperVariablesMain(othp.getSiteCode(), othp.getReportTitle(), jasperReport, filter, null);

                jasperPrint = DmServer.fillJasper(othp.getReportTitle(), jasperReport, parameter, filter, rsTemp);
                keyField.setSharedKeyFieldName(DmServer.showJasper(othp.getFilePaths(), othp.getFileNames(), jasperPrint, "", reportProperty));

                String filePathsFinal = othp.getFilePaths() + "/" + keyField.getSharedKeyFieldName();
                STServer.sendMailSystemReports(filePathsFinal, "SA", othp.getToEmail(), othp.getCcEmail(), othp.getSubject(), othp.getEncodedMsg(), DmShared.removeSpace(othp.getFileNames()));
                File existingFile = new File(filePathsFinal);

                if (existingFile.exists() && existingFile.isFile()) {
                    existingFile.delete();
                }

            } else {
//                jasperReport = DmServer.compileJasper("Pharmacy_ExpiryRegister");
                jasperPrint = DmServer.fillJasper(this, "Expiry Register", jasperReport, hm, filter, rsTemp);
                rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                rstTemp.setStatus(true);
            }

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
                if (cnTempCmp != null) {
                    cnTempCmp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp, String siteCode) {
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
            if (filter.get("batchNo") != null) {
                if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                    psTemp.setStringList("batchNo", DmShared.xNull(filter.get("batchNo").getTag()));
                }
            }

            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCat", DmShared.xNull(filter.get("medCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCompany", DmShared.xNull(filter.get("medCompany").getTag()));
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(filter.get("medGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            } else {
                psTemp.setStringList("site_Code", siteCode);
            }
        } catch (Exception e) {
        }

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
            sqlWithParam.setSQL(" select im.code,im.name,um.code as unitCode,um.unit,"
                    + " ig.name medGroup,ic.name medcategory,im.manualCode "
                    + " from itemMast as im "
                    + " left join unitmast as um on im.sku=um.code "
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup "
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)  ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setMedicineGroup(DmShared.xNull(rsTemp.getString("medGroup")));
                listNode.setMedicineCategory(DmShared.xNull(rsTemp.getString("medcategory")));
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
            sqlWithParam.setSQL("select ic.code, ic.name "
                    + " from itemcat ic "
                    + " where (ic.filterItemType = :filterItemType or ic.isCommon=1)");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlWithParam.setSQL("select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "         where sgn.manualCode IN ('CM','PH') and sg.isActive = 1 " + condition);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResultPageing<List<BatchNo_Mdl>> getTxtHBatchNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<BatchNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<BatchNo_Mdl> lstTemp = new ArrayList<BatchNo_Mdl>();
        BatchNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   ib.code"
                    + " , ib.batchNo"
                    + " , ib.mrp"
                    + " , ib.saleRate"
                    + " , ib.expiryDate"
                    + " , um.unit AS skuName ,im.name as medicineName"
                    + " FROM itemBatch ib"
                    + " LEFT JOIN itemMast im ON im.code = ib.itemCode "
                    + " LEFT JOIN unitMast um ON um.code = im.sku"
                    + " WHERE  im.filterItemType = :filterItemType and ISNULL(ib.batchNo, '') <> ''";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new BatchNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));

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

//            listNode = new KeyFieldExtended();
//            listNode.setSharedKeyFieldCode("Medicine Name");
//            listNode.setSharedKeyFieldName("Medicine Name");
//            lstTemp.add(listNode);
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
            sqlWithParam.setSQL("SELECT CODE, companyName AS NAME, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "   WHERE filterItemType = :filterItemType ");
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

    //==========================================Filter For Send Email from Scheduler ===================================
    public LinkedHashMap<String, FieldUDT_Mdl> getRptFIlterList(String siteCode, Connection cnTemp) {

        //Write code and name as alias
        //Set Tag =Date for date type filter
        LinkedHashMap<String, FieldUDT_Mdl> filterLst = new LinkedHashMap<>();
        FieldUDT_Mdl mdl;
        String key;

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Site Name");
        mdl.setText("");
        mdl.setTag("select code ,siteDisplayName as name from siteMast");
        key = "site";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("From Date");
        mdl.setText("");
        mdl.setTag("Date");

        key = "fromDate";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("To Date");
        mdl.setText("");
        mdl.setTag("Date");
        mdl.setIsMandatory("1");
        key = "toDate";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Department");
        mdl.setText("");
        mdl.setTag("select	dm.subCode as code, sg.name   \n"
                + "from departmentMast as dm  \n"
                + "left join subGroup as sg on dm.subCode = sg.subCode  \n"
                + "left join subGroupNature sgn on sgn.code = sg.nature \n"
                + "where sgn.manualCode IN ('CM','PH') and sg.isActive = 1 ");
        key = "department";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Medicine Group");
        mdl.setText("");
        mdl.setTag(" SELECT CODE, NAME, filteritemType "
                + " FROM itemgroup  "
                + " where (filterItemType = " + DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)) + " or isCommon=1)");
        key = "medGroup";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Medicine Category");
        mdl.setText("");
        mdl.setTag(" select ic.code, ic.name  \n"
                + "from itemcat ic \n"
                + "where (ic.filterItemType = " + DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)) + " or ic.isCommon=1)");
        key = "medCat";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Medicine");
        mdl.setText("");
        mdl.setTag(" select im.code,im.name,um.code as unitCode,um.unit,"
                + " ig.name medGroup,ic.name medcategory,im.manualCode "
                + " from itemMast as im "
                + " left join unitmast as um on im.sku=um.code "
                + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup "
                + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode "
                + " WHERE (im.filterItemType = " + DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)) + " or im.isCommon=1)");
        key = "medicine";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Value On");
        mdl.setText("singleHelp");
        mdl.setTag(" select 'L' as code , 'Landed Rate' as name\n"
                + "union  \n"
                + "select 'P' as code , 'Purchase Rate' as name\n"
                + " union  \n"
                + "select 'S' as code , 'Sale Rate' as name\n"
                + " union  \n"
                + "select 'M' as code , 'MRP' as name");
        key = "valueOn";
        mdl.setIsMandatory("1");
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Group By");
        mdl.setText("singleHelp");
        mdl.setTag(" select 'Medicine Category' as code , 'Medicine Category' as name\n"
                + "union all \n"
                + "select 'Medicine Group' as code , 'Medicine Group' as name\n"
                + " union all \n"
                + "select 'Medicine Company' as code , 'Medicine Company' as name ");
        key = "groupBy";
        filterLst.put(key, mdl);

        mdl = new FieldUDT_Mdl();
        mdl.setFieldCaption("Purchase Detail");
        mdl.setText("singleHelp");
        mdl.setTag(" select 0 as code , 'No' as name\n"
                + "union all \n"
                + "select 1 as code , 'Yes' as name\n");
        key = "purchaseDetail";
        mdl.setIsMandatory("1");
        filterLst.put(key, mdl);

        return filterLst;

    }

}
