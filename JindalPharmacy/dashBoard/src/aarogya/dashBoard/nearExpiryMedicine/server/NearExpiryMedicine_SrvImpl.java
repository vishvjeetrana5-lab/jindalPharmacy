package aarogya.dashBoard.nearExpiryMedicine.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.nearExpiryMedicine.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmDashBoard;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmChart.shared.DmChart_Mdl;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

public class NearExpiryMedicine_SrvImpl extends DmRSSPrivate implements NearExpiryMedicine_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResultPageing<List<NearExpiryMedicine_Mdl>> getData(String daysCount, DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue) {
        DmResultPageing<List<NearExpiryMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<NearExpiryMedicine_Mdl> lstTemp = new ArrayList<NearExpiryMedicine_Mdl>();
        NearExpiryMedicine_Mdl listNode;
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sql = "", startDate = "", toDate = "";
        try {

            cnTempCmp = DmServer.getConnection(DmServer.dbCompany);
            startDate = DmServer.executeScalar(" select top 1  format(start_Dt,'dd-MMM-yyyy') start_Dt    from company order by start_Dt desc ", cnTempCmp);
            cnTempCmp.close();

            toDate = DmServer.addDate(Calendar.DATE, DmShared.iNull(daysCount).intValue(), DmShared.dNull(DmServer.getServerDateTime()[0]));

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sql = "   select batchNo,vStk2.mrp,vStk2.code, im.itemCategoryCode       as groupCode, ic.name       as groupName,vStk2.itemCode,vStk2.expiryDate,\n"
                    + " vStk2.departmentCode,sgd.name as department, im.name as itemName,\n"
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
                    + "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) 	 \n"
                    + "						WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0	THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						ELSE 0  \n"
                    + "					END) AS openingValue  \n"
                    + "			 \n"
                    + "			FROM stock stk \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1 and                 stk.v_Date < :startDate  \n"
                    + " and ib.expiryDate <= :toDate 			GROUP BY stk.itemCode, stk.departmentCode, stk.site_Code,stk.ibCode   \n"
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
                    + "			0 AS openingValue \n"
                    + "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0  \n"
                    + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)  \n"
                    + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "							ELSE 0                                          END                                 END AS totalReceiveValue \n"
                    + " ,0 AS totalIssueValue	\n"
                    + "\n"
                    + "\n"
                    + "			FROM stock stk	\n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1                 and stk.v_Date >= :startDate		 \n"
                    + " and ib.expiryDate <= :toDate 			AND CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 1  \n"
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
                    + "			0 AS totalReceiveValue	\n"
                    + "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0 \n"
                    + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)  \n"
                    + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						ELSE 0					END			END AS totalIssueValue\n"
                    + "\n"
                    + "                 FROM stock stk  \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type		 \n"
                    + "			WHERE 1=1                 and stk.v_Date >= :startDate		 \n"
                    + " and ib.expiryDate <= :toDate 			AND CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 1	 \n"
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
                    + "  LEFT JOIN unitMast um ON um.code = im.sku	 \n"
                    + "  WHERE im.filterItemType='11'     \n"
                    + " AND vStk2.site_Code = :siteCode   and isNull(vStk2.expiryDate,'') <> ''  and ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0) <> 0\n"
                    + "  ";

            sqlWithParam.setSQL(sql);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setDate("startDate", DmServer.convertDate(startDate));
            sqlWithParam.setDate("toDate", DmServer.convertDate(toDate));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                listNode = new NearExpiryMedicine_Mdl();

                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("itemName")));
                listNode.setMedicineGroup(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setExpiry(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setBalanceQty(DmShared.xNull(rsTemp.getString("balanceQty")));
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setValue(DmShared.xNull(rsTemp.getString("expiryValue")));

                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);
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
    public DmResult<String> getNewDate(int value, String date) {
        DmResult<String> rstTemp = new DmResult<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Calendar calendar;
        Date dt;
        try {
            calendar = Calendar.getInstance();
            dt = sdf.parse(date);
            calendar.setTime(dt);
            calendar.add(Calendar.DATE, value);
            rstTemp.setStatus(true);
            rstTemp.setData(sdf.format(calendar.getTime()));
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getDefaultValue() {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<KeyFieldExtended>();
        KeyFieldExtended defaultValue = new KeyFieldExtended();
        try {
            defaultValue.setSharedKeyFieldCode(DmServer.getServerDateTime()[0]);
            dmrTemp.setData(defaultValue);
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
        } finally {
        }
        return dmrTemp;
    }

    @Override
    public DmResult<CardDetail_Mdl> getCardDetail(String daysCount) {
        DmResult<CardDetail_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        CardDetail_Mdl mrTemp = new CardDetail_Mdl();
        List<DmChart_Mdl> listNode = new ArrayList<DmChart_Mdl>();
        DmChart_Mdl node;
        dmrTemp.setData(mrTemp);
        String sql = "", startDate = "", toDate = "";

        try {

            cnTempCmp = DmServer.getConnection(DmServer.dbCompany);
            startDate = DmServer.executeScalar(" select top 1  format(start_Dt,'dd-MMM-yyyy') start_Dt    from company order by start_Dt desc ", cnTempCmp);
            cnTempCmp.close();

            toDate = DmServer.addDate(Calendar.DATE, DmShared.iNull(daysCount).intValue(), DmShared.dNull(DmServer.getServerDateTime()[0]));

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "  select \n"
                    + " \n"
                    + "  count(im.name) as cnt\n"
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
                    + "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) 	 \n"
                    + "						WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0	THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						ELSE 0  \n"
                    + "					END) AS openingValue  \n"
                    + "			 \n"
                    + "			FROM stock stk \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1 and                 stk.v_Date < :startDate  \n"
                    + " and ib.expiryDate <= :toDate 			GROUP BY stk.itemCode, stk.departmentCode, stk.site_Code,stk.ibCode   \n"
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
                    + "			0 AS openingValue \n"
                    + "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0  \n"
                    + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)  \n"
                    + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "							ELSE 0                                          END                                 END AS totalReceiveValue \n"
                    + " ,0 AS totalIssueValue	\n"
                    + "\n"
                    + "\n"
                    + "			FROM stock stk	\n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1                 and stk.v_Date >= :startDate		 \n"
                    + " and ib.expiryDate <= :toDate 			AND CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 1  \n"
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
                    + "			0 AS totalReceiveValue	\n"
                    + "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0 \n"
                    + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)  \n"
                    + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						ELSE 0					END			END AS totalIssueValue\n"
                    + "\n"
                    + "                 FROM stock stk  \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type		 \n"
                    + "			WHERE 1=1                 and stk.v_Date >= :startDate		 \n"
                    + " and ib.expiryDate <= :toDate 			AND CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 1	 \n"
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
                    + "  LEFT JOIN unitMast um ON um.code = im.sku	 \n"
                    + "  WHERE im.filterItemType='11'     \n"
                    + " AND vStk2.site_Code = :siteCode  and isNull(vStk2.expiryDate,'') <> ''  \n"
                    + " and ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0) <> 0 ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("startDate", DmServer.convertDate(startDate));
            psTemp.setDate("toDate", DmServer.convertDate(toDate));

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setCount(DmShared.iNull(rsTemp.getString("cnt")).toString());

                node = new DmChart_Mdl();
                node.setLabelName(DmShared.xNull(rsTemp.getString("cnt")));
                node.setLabelCode(DmShared.xNull(rsTemp.getString("cnt")));
                node.setDataValue(DmShared.iNull(rsTemp.getString("cnt")).intValue());
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListChardData().addAll(listNode);

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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId, String daysUpto) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        Connection cnTempCmp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
        DmHandelFile handleFile = null;
        String[] currentDateTime;
        char separator = File.separatorChar;
        String csvHeader = "", condition = "", startDate = "", toDate = "";
        dmrTemp.setData(keyField);
        try {
             csvHeader = "Medicine Name,Medicine Category,Department,Batch No.,Expiry,Balance Qty.,MRP,Value";

            cnTempCmp = DmServer.getConnection(DmServer.dbCompany);
            startDate = DmServer.executeScalar(" select top 1  format(start_Dt,'dd-MMM-yyyy') start_Dt    from company order by start_Dt desc ", cnTempCmp);
            cnTempCmp.close();

            toDate = DmServer.addDate(Calendar.DATE, DmShared.iNull(daysUpto).intValue(), DmShared.dNull(DmServer.getServerDateTime()[0]));

            currentDateTime = DmServer.getServerDateTime();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            fileName = new pmDashBoard().NEAR_EXPIRY_MEDICINE.caption + String.valueOf(new java.util.Date().getTime()).trim();
            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
            String fullFilePath = directoryPath + separator + serverFileName;
            new File(directoryPath).mkdir();
            handleFile = new DmHandelFile(fullFilePath, true);

            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

            handleFile.writeLine(csvHeader);

            sqlQuery = " select tmp.* from (\n"
                    + "\n"
                    + " select batchNo,vStk2.mrp,vStk2.code, im.itemCategoryCode       as groupCode, ic.name       as groupName,vStk2.itemCode,vStk2.expiryDate,\n"
                    + " vStk2.departmentCode,sgd.name as department, im.name as itemName,\n"
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
                    + "			, SUM(CASE WHEN ISNULL(stk.recQty,0) > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) > 0 THEN (0 - ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.recQty,0) < 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) 	 \n"
                    + "						WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0	THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						ELSE 0  \n"
                    + "					END) AS openingValue  \n"
                    + "			 \n"
                    + "			FROM stock stk \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1 and                 stk.v_Date < :startDate  \n"
                    + " and ib.expiryDate <= :toDate 			GROUP BY stk.itemCode, stk.departmentCode, stk.site_Code,stk.ibCode   \n"
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
                    + "			0 AS openingValue \n"
                    + "		, CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "                                     WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 0  \n"
                    + "                                     WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.recQty,0)  > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)  \n"
                    + "                                     ELSE CASE WHEN ISNULL(stk.isuQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "							WHEN ISNULL(stk.recQty,0) > 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "							WHEN (ISNULL(stk.isuQty,0) + ISNULL(stk.recQty,0) ) = 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "							ELSE 0                                          END                                 END AS totalReceiveValue \n"
                    + " ,0 AS totalIssueValue	\n"
                    + "\n"
                    + "\n"
                    + "			FROM stock stk	\n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type  \n"
                    + "			WHERE 1=1                 and stk.v_Date >= :startDate		 \n"
                    + " and ib.expiryDate <= :toDate 			AND CASE WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 1  \n"
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
                    + "			0 AS totalReceiveValue	\n"
                    + "		, CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "				WHEN vt.parentVoucher IN ('Purchase', 'Purchase Return') THEN 0 \n"
                    + "				WHEN vt.parentVoucher = 'Stock Transfer' AND ISNULL(stk.isuQty,0)  > 0 THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)  \n"
                    + "				ELSE CASE WHEN ISNULL(stk.recQty,0) < 0 THEN ABS(ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0)) \n"
                    + "						WHEN ISNULL(stk.isuQty,0) > 0  THEN ISNULL( (ISNULL(stk.landedSkuValue,0) + (CASE WHEN ISNULL(vt.parentVoucher,'') IN ('Purchase', 'Purchase Return', 'Item Opening', 'Item Receive', 'Stock Transfer', 'Physical Stock') THEN ISNULL(stk.otherAdjustment,0) ELSE 0 END)) ,0) \n"
                    + "						ELSE 0					END			END AS totalIssueValue\n"
                    + "\n"
                    + "                 FROM stock stk  \n"
                    + "			left join itemBatch as ib on stk.ibCode = ib.Code	\n"
                    + "			INNER JOIN voucher_Type vt ON vt.v_Type = stk.v_Type		 \n"
                    + "			WHERE 1=1                 and stk.v_Date >= :startDate		 \n"
                    + " and ib.expiryDate <= :toDate 			AND CASE WHEN vt.parentVoucher IN ('Sale', 'Sale Return') THEN 1	 \n"
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
                    + "  LEFT JOIN unitMast um ON um.code = im.sku	 \n"
                    + "  WHERE im.filterItemType='11'     \n"
                    + " AND vStk2.site_Code = :siteCode   and isNull(vStk2.expiryDate,'') <> ''  and ISNULL(vStk2.openingQty,0) + ISNULL(vStk2.totalReceiveQty,0) - ISNULL(vStk2.totalIssueQty,0) <> 0\n"
                    + " \n"
                    + " ) as tmp\n"
                    + "\n"
                    + " order by tmp.itemName,tmp.expiryDate  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setDate("startDate", DmServer.convertDate(startDate));
            psTemp.setDate("toDate", DmServer.convertDate(toDate));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                lineData = "\"" + DmShared.xNull(rsTemp.getString("itemName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("groupName")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("department")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("batchNo")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.dNull(rsTemp.getDate("expiryDate")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("balanceQty")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("mrp")) + "\"";
                lineData += commaDelimiter;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("expiryValue")) + "\"";

                handleFile.writeLine(lineData);

            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            /////
            handleFile.close();
            dmrTemp.setData(keyField);
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
                if (handleFile != null) {
                    handleFile.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
