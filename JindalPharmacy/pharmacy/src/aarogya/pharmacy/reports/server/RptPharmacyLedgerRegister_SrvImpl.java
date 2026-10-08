package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptPharmacyLedgerRegister_Srv;
import aarogya.pharmacy.reports.shared.RptPharmacyLedgerRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.server.DmIJasperCompiled;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class RptPharmacyLedgerRegister_SrvImpl extends DmRSSPrivate implements RptPharmacyLedgerRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTempSubReport = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, subReportQuery = "";
        DmIJasperCompiled jasperCompiled = null;
        String condition = "", siteCodeParamList = "", rateType = "", skuMrpCondition = "", skuHeaderMrpCondition = "";

        try {
            RptPharmacyLedgerRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("lmCode").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vL2.lmCode IN (" + DmServer.getParamList(":lmCode", filter.get("lmCode").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vt.category = ('" + pvc.OPD + "') and lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vt.category = ('" + pvc.IPD + "') and lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vt.category = ('" + pvc.EMERGENCY + "') and lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vt.category = ('" + pvc.DAY_CARE + "') lm.regDocId IS NOT NULL ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("L")) {
                    condition += " and lm.subCode IS NOT NULL  ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("W")) {
                    condition += " and lm.subCode IS NULL and lm.regDocId IS NULL ";
                }
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                siteCodeParamList = DmServer.getParamList(":site_Code ", filter.get("site").getTag());
            } else {
                siteCodeParamList = DmServer.getParamList(":site_Code ", DmServer.getSsnSiteList(this));
            }

            if (DmShared.xNull(filter.get("rateType").getTag()).trim().equalsIgnoreCase("M")) {
                rateType += " 'M' as rateType ";
            }
            if (DmShared.xNull(filter.get("rateType").getTag()).trim().equalsIgnoreCase("S")) {
                rateType += " 'S' as rateType ";
            }
            if (!DmShared.xNull(filter.get("selectionMrp").getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("rateType").getTag()).trim().equalsIgnoreCase("M") && (!DmShared.xNull(filter.get("selectionMrp").getText()).trim().equalsIgnoreCase("0"))) {
                    condition += " and mrpAmount <> '' ";
//                    if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.PHARMACY_BILL_RETURN)) {
//                        skuMrpCondition += "AND 0-(sbd.skuMrp*sbd.quantity) >=:selectionMrp ";
//                    } else {
//                        skuMrpCondition += "AND sbd.skuMrp*sbd.quantity >=:selectionMrp ";
//                    }
                    skuMrpCondition += " AND  sbd.skuMrp >= :selectionMrp ";
                } else {
                    throw new DmException(0, "Please select the 'MRP' of Rate Type filter. ");
                }

                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.PHARMACY_BILL_RETURN)) {
                    skuHeaderMrpCondition += "AND  sbd.skuMrp >= :selectionMrp ";
                } else {
                    skuHeaderMrpCondition += "AND  sbd.skuMrp >= :selectionMrp ";
                }
//                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.PHARMACY_BILL_RETURN)) {
//                    skuHeaderMrpCondition += "AND 0-(sbd.skuMrp*sbd.quantity) >=:selectionMrp ";
//                } else {
//                    skuHeaderMrpCondition += "AND sbd.skuMrp*sbd.quantity >=:selectionMrp ";
//                }
            }

//            sqlQuery = "SELECT lm.code "
//                    + " , lm.name "
//                    + " , isNull(prg.recId,'') AS regNo"
//                    + ", isNull(vt.description,'') AS regType"
//                    + " , isNull(lm.address,'') as address "
//                    + " , isNull(ct.cityName,'') AS city "
//                    + " , vL2.v_Date "
//                    + " , vL2.recId "
//                    + " , vL2.docId "
//                    + " , vL2.short_Name "
//                    + " , vL2.description "
//                    + " , vL2.billAmt "
//                    + " , vL2.ctrCtoAmt "
//                    + " , vL2.receiptAmt as ajustAmount "
//                    + " , vL2.remark "
//                    + " , vL2.opening "
//                    + " , SUM(ISNULL(vL2.opening,0) + ISNULL(vL2.billAmt,0) - ISNULL(vL2.ctrCtoAmt,0) - ISNULL(vL2.receiptAmt,0)) OVER (PARTITION BY vL2.lmCode ORDER BY lm.name, vL2.printSr, vL2.v_date, vL2.short_Name, vL2.v_no, vL2.recId) as runningTotal "
//                    + " FROM ( "
//                    + " 		SELECT vL1.lmCode, NULL AS v_Date, NULL AS recId, NULL AS docId "
//                    + " 		, NULL AS v_no "
//                    + " 		, NULL AS short_Name		 "
//                    + " 		, 'Opening' AS description	 "
//                    + " 		, 0 AS billAmt, 0 as ctrCtoAmt, 0 AS receiptAmt		 "
//                    + " 		, ISNULL(SUM(vL1.billAmt),0) - ISNULL(SUM(vL1.ctrCtoAmt),0) - ISNULL(SUM(vL1.receiptAmt),0)  AS opening		 "
//                    + " 		, 0 AS printSr, NULL AS remark		 "
//                    + " 		FROM ( "
//                    + " 			SELECT sb.lmCode "
//                    + " 			, SUM(CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END) AS billAmt "
//                    + " 			, ISNULL(SUM(vPla.amount),0) as ctrCtoAmt		 "
//                    + " 			, 0 AS receiptAmt "
//                    + " 			FROM saleBill sb 		 "
//                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type		 "
//                    + " 			LEFT JOIN ( "
//                    + " 					SELECT pla.docid, pla.amount "
//                    + " 					FROM patientLimitApproval pla  "
//                    + " 					LEFT JOIN voucher_Type vt ON vt.v_Type = pla.v_Type "
//                    + " 					WHERE pla.isCancelled = 0 "
//                    + " 					AND pla.nature in (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") "
//                    + " 					AND vt.category in ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "')			 "
//                    + " 				) vPla ON vPla.docid = sb.docid "
//                    + " 			WHERE sb.lmCode IS NOT NULL and  sb.isCancelled = 0  		 "
//                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') "
//                    + " 			AND sb.v_Date < :fromDate  "
//                    + "                         AND  sb.site_Code IN (" + siteCodeParamList + " )"
//                    + " 			GROUP BY sb.lmCode "
//                    + "  "
//                    + " 			UNION ALL	"
//                    + "  			"
//                    + "  			SELECT pr.lmCode"
//                    + "  			, 0 AS billAmt, 0 as ctrCtoAmt		"
//                    + "  			, ISNULL(SUM(pr.amount),0) AS receiptAmt"
//                    + "  			FROM patientReceipt pr 		"
//                    + "  			LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type		"
//                    + "  			WHERE pr.lmcode IS NOT NULL and  pr.isCancelled = 0 		"
//                    + "  			AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') "
//                    + "  			AND pr.v_Date < :fromDate"
//                    + "                         AND  pr.site_Code IN (" + siteCodeParamList + " )"
//                    + "  			GROUP BY pr.lmCode"
//                    + " 		) vL1 "
//                    + " 		GROUP BY vL1.lmCode "
//                    + " 		HAVING ISNULL(SUM(vL1.billAmt),0) - ISNULL(SUM(vL1.ctrCtoAmt),0) - ISNULL(SUM(vL1.receiptAmt),0) <> 0 "
//                    + "  "
//                    + " 		UNION ALL "
//                    + " 		 "
//                    + " 		SELECT sb.lmCode , sb.v_Date, sb.recId, sb.docId "
//                    + " 		, sb.v_no, vt.short_Name, vt.description		 "
//                    + " 		, CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END AS billAmt, vPla.amount as ctrCtoAmt		 "
//                    + " 		, 0 AS receiptAmt		, 0 AS opening		 "
//                    + " 		, 1 AS printSr          "
//                    + " 		, sb.remark		 "
//                    + " 		FROM saleBill sb 	 "
//                    + " 		LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type		 "
//                    + " 		LEFT JOIN ( "
//                    + " 				SELECT pla.docid, pla.amount "
//                    + " 				FROM patientLimitApproval pla  "
//                    + " 				LEFT JOIN voucher_Type vt ON vt.v_Type = pla.v_Type "
//                    + " 				WHERE pla.isCancelled = 0 "
//                    + " 				AND pla.nature in (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") "
//                    + " 				AND vt.category in ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "')			 "
//                    + " 			) vPla ON vPla.docid = sb.docid "
//                    + " 		WHERE sb.lmCode IS NOT NULL and  sb.isCancelled = 0  		 "
//                    + " 		AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "')		 "
//                    + " 		AND sb.v_Date >= :fromDate  "
//                    + " 		AND sb.v_Date <= :toDate "
//                    + "                 AND  sb.site_Code IN (" + siteCodeParamList + " )"
//                    + " "
//                    + " 		UNION ALL "
//                    + " "
//                    + " 		SELECT pr.lmCode, pr.v_Date, pr.recId, NULL AS docId "
//                    + " 		, pr.v_no, vt.short_Name, vt.description, 0 AS billAmt, 0 as ctrCtoAmt, pr.amount AS receiptAmt, 0 AS opening "
//                    + " 		, 2 AS printSr, pr.remark		 "
//                    + " 		FROM patientReceipt pr 		 "
//                    + " 		LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type		 "
//                    + " 		WHERE pr.lmcode IS NOT NULL and  pr.isCancelled = 0 		 "
//                    + " 		AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') "
//                    + " 		AND pr.v_Date >= :fromDate AND pr.v_Date <=:toDate  "
//                    + "                 AND  pr.site_Code IN (" + siteCodeParamList + " )"
//                    + " 	) vL2 "
//                    + " LEFT JOIN ledgerMast lm ON lm.code = vL2.lmCode "
//                    + " LEFT JOIN patientRegistration prg ON prg.regDocId = lm.regDocId "
//                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = prg.v_Type"
//                    + " LEFT JOIN city ct ON ct.cityCode = lm.cityCode "
//                    + " WHERE 1=1 " + condition
//                    + " ORDER BY lm.name, vL2.printSr, vL2.v_date, vL2.short_Name, vL2.v_no, vL2.recId ";
            String amountField = "CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END";
            sqlQuery = "SELECT lm.code, " + rateType
                    + " , lm.name "
                    + " , isNull(prg.recId,'') AS regNo \n"
                    + ", isNull(vt.description,'') AS regType"
                    + " , isNull(lm.address,'') as address "
                    + " , isNull(ct.cityName,'') AS city "
                    + " , vL2.v_Date \n"
                    + " , vL2.recId "
                    + " , vL2.docId "
                    + " , vL2.short_Name "
                    + " , vL2.description "
                    + " , vL2.billAmt \n"
                    + " , vL2.ctrCtoAmt "
                    + " , vL2.receiptAmt as ajustAmount "
                    + " , vL2.remark "
                    + " , vL2.opening \n"
                    + " , SUM(ISNULL(vL2.opening,0) + ISNULL(vL2.billAmt,0) - ISNULL(vL2.ctrCtoAmt,0) - ISNULL(vL2.receiptAmt,0)) OVER (PARTITION BY vL2.lmCode ORDER BY lm.name, vL2.printSr, vL2.v_date, vL2.short_Name, vL2.v_no, vL2.recId) as runningTotal \n"
                    + " , vl2.mrpAmount, vl2.mrpNetAmount, vl2.skuMrp \n"
                    + " FROM ( "
                    + " 		SELECT vL1.lmCode, NULL AS v_Date, NULL AS recId, NULL AS docId \n"
                    + " 		, NULL AS v_no "
                    + " 		, NULL AS short_Name		 "
                    + " 		, 'Opening' AS description	 "
                    + " 		, 0 AS billAmt, 0 as ctrCtoAmt, 0 AS receiptAmt \n"
                    + " 		, ISNULL(SUM(vL1.billAmt),0) - ISNULL(SUM(vL1.ctrCtoAmt),0) - ISNULL(SUM(vL1.receiptAmt),0)  AS opening "
                    + " 		, 0 AS printSr, NULL AS remark \n"
                    + "                 ,max(vl1.skuMrp) skuMrp					   \n"
                    + "					   ,sum(vl1. mrpAmount) mrpAmount \n"
                    + "					   ,sum(vl1.mrpNetAmount) mrpNetAmount		 "
                    + " 		FROM ( \n"
                    + " 			SELECT sb.lmCode "
                    + " 			, SUM(" + amountField + ") AS billAmt "
                    + " 			, SUM(CASE WHEN sb.paymentSource IN (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") THEN " + amountField + " ELSE 0 END) as ctrCtoAmt \n"
                    + " 			, 0 AS receiptAmt "
                    + "                         ,0 as skuMrp\n"
                    + "					   ,max(sbd.mrpAmount) as mrpAmount,max(sbd.mrpNetAmount) as mrpNetAmount "
                    + " 			FROM saleBill sb 		 "
                    //                    + "                         left join saleBillDetail sbd on sbd.docId = sb.docId "
                    + "                         left join ( \n"
                    + "                         	select sbd.docid \n"
                    + "                                 ,sum(CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-(sbd.skuMrp*sbd.quantity) else (sbd.skuMrp*sbd.quantity) end) as mrpAmount \n"
                    + "                                 , sum(CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-((sbd.skuMrp*sbd.quantity) - isNull(sbd.oh_Amt_Discount,0)) else ((sbd.skuMrp*sbd.quantity) - isNull(sbd.oh_Amt_Discount,0)) end) as mrpNetAmount \n"
                    + "                                 from saleBillDetail sbd \n"
                    + "                                 left join saleBill sb on sbd.docID=sb.docID \n"
                    + "                                 left join voucher_Type vt on sb.v_Type=vt.v_Type \n"
                    + "                                  where 1=1 " + skuHeaderMrpCondition
                    + "                                 group By sbd.docid \n"
                    + "                                 ) as sbd on sb.docID=sbd.docId \n"
                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type \n"
                    + " 			WHERE sb.lmCode IS NOT NULL and  sb.isCancelled = 0 \n"
                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') \n"
                    + " 			AND sb.v_Date < :fromDate \n"
                    + "                         AND  sb.site_Code IN (" + siteCodeParamList + " ) \n"
                    + " 			GROUP BY sb.lmCode \n"
                    + " 			UNION ALL \n"
                    + "  			SELECT pr.lmCode"
                    + "  			, 0 AS billAmt, 0 as ctrCtoAmt		"
                    + "  			, ISNULL(SUM(pr.amount),0) AS receiptAmt"
                    + "                         ,0 as skuMrp"
                    + "					   ,0 as mrpAmount\n"
                    + "					   ,0 as mrpNetAmount"
                    + "  			FROM patientReceipt pr \n"
                    + "                         left join saleBill sb on sb.docid = pr.sbDocId"
                    + "  			LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type \n"
                    + "                         left join voucher_Type vts on vts.v_Type = sb.v_Type"
                    + "  			WHERE pr.lmcode IS NOT NULL and  pr.isCancelled = 0 \n"
                    + "  			AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') \n"
                    + "  			AND pr.v_Date < :fromDate \n"
                    + "                         AND  pr.site_Code IN (" + siteCodeParamList + " ) and and vts.category not in ('" + pvc.PHARMACY_AC_POSTING + "') \n"
                    + "  			GROUP BY pr.lmCode \n"
                    + " 		) vL1 \n"
                    + " 		GROUP BY vL1.lmCode \n"
                    + " 		HAVING ISNULL(SUM(vL1.billAmt),0) - ISNULL(SUM(vL1.ctrCtoAmt),0) - ISNULL(SUM(vL1.receiptAmt),0) <> 0 \n"
                    + " 		UNION ALL \n"
                    + " 		SELECT sb.lmCode , sb.v_Date, sb.recId, sb.docId "
                    + " 		, sb.v_no, vt.short_Name, vt.description \n"
                    + " 		, " + amountField + " AS billAmt"
                    + "                 , CASE WHEN sb.paymentSource IN (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") THEN " + amountField + " ELSE 0 END as ctrCtoAmt \n"
                    + " 		, 0 AS receiptAmt"
                    + "                 , 0 AS opening"
                    + " 		, 1 AS printSr"
                    + " 		, sb.remark \n"
                    + "                  ,0 as skuMrp ,sbd.mrpAmount as mrpAmount,sbd.mrpNetAmount as mrpNetAmount \n"
                    + " 		FROM saleBill sb \n"
                    //                    + "                 left join saleBillDetail sbd on sbd.docId = sb.docId	 "
                    + "                         left join ( \n"
                    + "                         	select sbd.docid \n"
                    + "                                 ,sum(CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-(sbd.skuMrp*sbd.quantity) else (sbd.skuMrp*sbd.quantity) end) as mrpAmount \n"
                    + "                                 , sum(CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN 0-((sbd.skuMrp*sbd.quantity) - isNull(sbd.oh_Amt_Discount,0)) else ((sbd.skuMrp*sbd.quantity) - isNull(sbd.oh_Amt_Discount,0)) end) as mrpNetAmount \n"
                    + "                                 from saleBillDetail sbd \n"
                    + "                                 left join saleBill sb on sbd.docID=sb.docID \n"
                    + "                                 left join voucher_Type vt on sb.v_Type=vt.v_Type \n"
                    + "                                 where 1=1 " + skuHeaderMrpCondition
                    + "                                 group By sbd.docid \n"
                    + "                                 ) as sbd on sb.docID=sbd.docId \n"
                    + " 		LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type \n"
                    + " 		WHERE sb.lmCode IS NOT NULL and  sb.isCancelled = 0 \n"
                    + " 		AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "','" + pvc.PHARMACY_BILL_CASH + "') \n"
                    + " 		AND sb.v_Date >= :fromDate  "
                    + " 		AND sb.v_Date <= :toDate \n"
                    + "                 AND  sb.site_Code IN (" + siteCodeParamList + " ) \n"
                    + " 		UNION ALL \n"
                    + " 		SELECT pr.lmCode, pr.v_Date, pr.recId, NULL AS docId \n"
                    + " 		, pr.v_no, vt.short_Name, vt.description, 0 AS billAmt, 0 as ctrCtoAmt, pr.amount AS receiptAmt, 0 AS opening \n"
                    + " 		, 2 AS printSr, pr.remark,0 as skuMrp,0 as mrpAmount,0 as mrpNetAmount \n"
                    + " 		FROM patientReceipt pr \n"
                    + " 		LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type \n"
                    + " 		WHERE pr.lmcode IS NOT NULL and  pr.isCancelled = 0 \n"
                    + " 		AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') \n"
                    + " 		AND pr.v_Date >= :fromDate AND pr.v_Date <=:toDate  \n"
                    + "                 AND  pr.site_Code IN (" + siteCodeParamList + " ) \n"
                    + " 	) vL2 \n"
                    + " LEFT JOIN ledgerMast lm ON lm.code = vL2.lmCode \n"
                    + " LEFT JOIN patientRegistration prg ON prg.regDocId = lm.regDocId \n"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = prg.v_Type \n"
                    + " LEFT JOIN city ct ON ct.cityCode = lm.cityCode \n"
                    + " WHERE 1=1 " + condition
                    + " ORDER BY lm.name, vL2.printSr, vL2.v_date, vL2.short_Name, vL2.v_no, vL2.recId ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            //, sbd.oh_at_Discount, sbd.oh_Amt_Discount, sbd.oh_Amt_Gross
            subReportQuery = " SELECT "
                    + "  sbd.itemCode AS medicineCode "
                    + " , im.name as medicineName "
                    + " , im.displayName as medicineDisplayName "
                    + " , CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN (0-ISNULL(sbd.quantity,0)) ELSE sbd.quantity END as quantity "
                    + " , CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN (0-ISNULL(sbd.freeQty,0)) ELSE sbd.freeQty END as freeQty  "
                    + " , sbd.batchNo "
                    + " , sbd.expiryDate "
                    + " , im.saleUnit as saleUnitCode "
                    + " , um.unit as saleUnitName "
                    + " , CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN (0-ISNULL(sbd.oh_Amt_Gross,0)) ELSE sbd.oh_Amt_Gross END as grossAmount"
                    + " , sbd.oh_at_Discount AS discountPer "
                    + " , CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN (0-ISNULL(sbd.oh_Amt_Discount,0)) ELSE sbd.oh_Amt_Discount END as discountAmt "
                    + " , CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN (0-ISNULL(sbd.oh_Amt_Net,0)) ELSE sbd.oh_Amt_Net END as netAmount "
                    + " , im.itemPacking AS packing "
                    + " , ic.name as medicineCategory "
                    + " , sbd.skuRate as rate, sbd.mrp, sbd.skuMrp "
                    + " , CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN (0-sbd.skuMrp*sbd.quantity) else sbd.skuMrp*sbd.quantity end as mrpAmount "
                    + " , CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN (0-((sbd.skuMrp*sbd.quantity)-sbd.oh_Amt_Discount)) else (sbd.skuMrp*sbd.quantity)-sbd.oh_Amt_Discount end as mrpNetAmount"
                    + " FROM saleBillDetail sbd "
                    + " LEFT JOIN saleBill sb ON sb.docId=sbd.docId "
                    + " LEFT JOIN itemMast im ON im.code=sbd.itemCode "
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode "
                    + " LEFT JOIN unitMast um ON um.code=sbd.saleUnitCode "
                    + " LEFT JOIN voucher_type vt ON vt.v_Type=sb.v_Type "
                    + " WHERE sbd.isFooter = 0 "
                    + " AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') "
                    + " AND sb.isCancelled = 0 "
                    + skuMrpCondition
                    + " AND sbd.docId = $P{docId} ";

            psTempSubReport = DmPreparedStatement.prepareStatement(cnTemp, subReportQuery);
            if (!DmShared.xNull(filter.get("selectionMrp").getText()).trim().equalsIgnoreCase("")) {
                psTempSubReport.setString("selectionMrp", DmShared.xNull(filter.get("selectionMrp").getText()));
            }
            subReportQuery = getParameter(filter, psTempSubReport);
            HashMap hm = new HashMap();

            jasperCompiled = DmServer.compileJasper("Pharmacy_LedgerRegister", "", true);

            hm.put("REPORT_CONNECTION", cnTemp);
            if (filter.get("medicineDetail").getTag().equalsIgnoreCase("1")) {
                hm.putAll(jasperCompiled.getSubReports());
            }
            hm.put("subReportQuery", subReportQuery);

            jasperReport = DmServer.compileJasper("Pharmacy_LedgerRegister");
            jasperPrint = DmServer.fillJasper(this, "Pharmcy Ledger", jasperReport, hm, filter, rsTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));

            rstTemp.setStatus(true);
            cnTemp.close();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.IPD);
            listNode.setSharedKeyFieldName(pvc.IPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.OPD);
            listNode.setSharedKeyFieldName(pvc.OPD);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.DAY_CARE);
            listNode.setSharedKeyFieldName("Day Care");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(pvc.EMERGENCY);
            listNode.setSharedKeyFieldName("Emergency");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("L");
            listNode.setSharedKeyFieldName("Ledger");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("W");
            listNode.setSharedKeyFieldName("Walk-In");
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
    public DmResultPageing<List<TxtHLMCode_Mdl>> getTxtHLMCode(String type, DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHLMCode_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHLMCode_Mdl> lstTemp = new ArrayList<>();
        TxtHLMCode_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String condition = "";

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(type).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vt.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vt.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vt.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vt.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("L")) {
                    condition += " and lm.subCode IS NOT NULL  ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("W")) {
                    condition += " and lm.subCode IS NULL and lm.regDocId IS NULL ";
                }
            }
            sqlWithParam.setSQL("select lm.code,lm.name,lm.manualCode,c.cityName,pr.recid as regNo,sg.name as acName"
                    + " from ledgerMast as lm"
                    + " left join city as c on lm.cityCode=c.cityCode"
                    + " left join patientRegistration as pr on lm.regDocId=pr.regDocId"
                    + " left join subGroup as sg on lm.subCode=sg.subCode"
                    + " left join voucher_Type as vt on pr.v_type=vt.v_type"
                    + "  where 1=1 " + condition);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHLMCode_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
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

    private String getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("lmCode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("lmCode", DmShared.xNull(filter.get("lmCode").getTag()));
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            } else {
                psTemp.setStringList("site_Code", DmShared.xNull(DmServer.getSsnSiteList(this)));
            }
            if (!DmShared.xNull(filter.get("selectionMrp").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setString("selectionMrp", DmShared.xNull(filter.get("selectionMrp").getText()));
            }
            psTemp.executeQuery();
        } catch (Exception e) {
        }
        return psTemp.getQuery();
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
            DmServer.getSsnSiteList(this);
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
    public DmResultPageing<List<KeyFieldExtended>> getRateType(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Sale Rate");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("M");
            listNode.setSharedKeyFieldName("MRP");
            lstTemp.add(listNode);

            lstTemp = DmShared.filterKeyFieldList(pageing, lstTemp);
            rstTemp.setData(lstTemp);

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }
}
