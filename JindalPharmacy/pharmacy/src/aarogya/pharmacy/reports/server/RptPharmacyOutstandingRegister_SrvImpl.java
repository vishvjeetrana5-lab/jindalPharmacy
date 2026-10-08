package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptPharmacyOutstandingRegister_Srv;
import aarogya.pharmacy.reports.shared.RptPharmacyOutstandingRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
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

public class RptPharmacyOutstandingRegister_SrvImpl extends DmRSSPrivate implements RptPharmacyOutstandingRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery, sqlOpeningQuery, sqlPeriodQuery;
        String condition = "", conditionHaving = "", siteCodeParamList = "", groupCode = "", groupName = "";

        try {
            RptPharmacyOutstandingRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("lmCode").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  lm.code IN (" + DmServer.getParamList(":lmCode", filter.get("lmCode").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("organisation").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pr.organizationCode IN (" + DmServer.getParamList(":organisation", filter.get("organisation").getTag()) + " ) ";
            }
            if (DmShared.xNull(filter.get("group").getTag()).trim().equalsIgnoreCase("PatientType")) {
                groupCode = "  CASE WHEN MAX(lm.regDocId) IS NOT NULL THEN MAX(vt.category) ELSE CASE WHEN MAX(lm.subCode) IS NOT NULL THEN 'Ledger' ELSE 'WALK-IN' END END ";
                groupName = "  CASE WHEN MAX(lm.regDocId) IS NOT NULL THEN MAX(vt.category) ELSE CASE WHEN MAX(lm.subCode) IS NOT NULL THEN 'Ledger' ELSE 'WALK-IN' END END ";
            } else if (DmShared.xNull(filter.get("group").getTag()).trim().equalsIgnoreCase("Organisation")) {
                groupCode = "  MAX(pr.organizationCode)  ";
                groupName = "  MAX(sgo.name) ";
            } else {
                groupCode = "''";
                groupName = "''";
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

            if (DmShared.xNull(filter.get("balanceType").getTag()).equalsIgnoreCase("Z")) {
                conditionHaving = " HAVING (ISNULL(SUM(vL2.opening),0) + ISNULL(SUM(vL2.billAmt),0) - ISNULL(SUM(vL2.ctrCtoAmt),0) - ISNULL(SUM(vL2.receiptAmt),0)) = 0 ";
            } else if (DmShared.xNull(filter.get("balanceType").getTag()).equalsIgnoreCase("P")) {
                conditionHaving = " HAVING (ISNULL(SUM(vL2.opening),0) + ISNULL(SUM(vL2.billAmt),0) - ISNULL(SUM(vL2.ctrCtoAmt),0) - ISNULL(SUM(vL2.receiptAmt),0)) > 0 ";
            } else if (DmShared.xNull(filter.get("balanceType").getTag()).equalsIgnoreCase("N")) {
                conditionHaving = " HAVING (ISNULL(SUM(vL2.opening),0) + ISNULL(SUM(vL2.billAmt),0) - ISNULL(SUM(vL2.ctrCtoAmt),0) - ISNULL(SUM(vL2.receiptAmt),0)) < 0 ";
            } else if (DmShared.xNull(filter.get("balanceType").getTag()).equalsIgnoreCase("N,P")) {
                conditionHaving = " HAVING (ISNULL(SUM(vL2.opening),0) + ISNULL(SUM(vL2.billAmt),0) - ISNULL(SUM(vL2.ctrCtoAmt),0) - ISNULL(SUM(vL2.receiptAmt),0)) <> 0 ";
            } else if (DmShared.xNull(filter.get("balanceType").getTag()).equalsIgnoreCase("N,Z")) {
                conditionHaving = " HAVING (ISNULL(SUM(vL2.opening),0) + ISNULL(SUM(vL2.billAmt),0) - ISNULL(SUM(vL2.ctrCtoAmt),0) - ISNULL(SUM(vL2.receiptAmt),0)) <= 0 ";
            } else if (DmShared.xNull(filter.get("balanceType").getTag()).equalsIgnoreCase("Z,P")) {
                conditionHaving = " HAVING (ISNULL(SUM(vL2.opening),0) + ISNULL(SUM(vL2.billAmt),0) - ISNULL(SUM(vL2.ctrCtoAmt),0) - ISNULL(SUM(vL2.receiptAmt),0)) >= 0 ";
            } else if (DmShared.xNull(filter.get("balanceType").getTag()).equalsIgnoreCase("Z,P,N")) {
            }

            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                siteCodeParamList = DmServer.getParamList(":site_Code ", filter.get("site").getTag());
            } else {
                siteCodeParamList = DmServer.getParamList(":site_Code ", DmServer.getSsnSiteList(this));
            }

//            sqlOpeningQuery = " SELECT vL1.lmCode "
//                    + " 	, ISNULL(SUM(vL1.billAmt), 0)  - ISNULL(SUM(vL1.ctrCtoAmt),0) - isNull(SUM(vL1.receiptAmt), 0) AS opening "
//                    + " 	, 0 AS billAmt, 0 AS ctrCtoAmt, 0 AS receiptAmt 		   "
//                    + " 	FROM ( "
//                    + " 			SELECT sb.lmCode, SUM(CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END) AS billAmt  "
//                    + " 			, ISNULL(SUM(vPla.amount),0) as ctrCtoAmt	  "
//                    + " 			, 0 AS receiptAmt "
//                    + " 			FROM saleBill sb "
//                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type "
//                    + " 			LEFT JOIN ( "
//                    + " 					SELECT pla.docid, pla.amount "
//                    + " 					FROM patientLimitApproval pla "
//                    + " 					LEFT JOIN voucher_Type vt ON vt.v_Type = pla.v_Type "
//                    + " 					WHERE pla.isCancelled = 0 "
//                    + " 					AND pla.nature in (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") "
//                    + " 					AND vt.category in ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') "
//                    + " 				) vPla ON vPla.docid = sb.docid "
//                    + " 			WHERE sb.isCancelled = 0 "
//                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') "
//                    + " 			AND sb.v_Date < :fromDate	 "
//                    + "                         AND  sb.site_Code IN (" + siteCodeParamList + " )"
//                    + " 			GROUP BY sb.lmCode  "
//                    + " 			UNION ALL  "
//                    + " 			SELECT pr.lmCode, 0 AS billAmt, 0 AS ctrCtoAmt, SUM(pr.amount) AS receiptAmt "
//                    + " 			FROM patientReceipt pr "
//                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type		"
//                    + " 			WHERE pr.isCancelled = 0 "
//                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') "
//                    + " 			AND pr.v_Date < :fromDate "
//                    + "                         AND  pr.site_Code IN (" + siteCodeParamList + " )"
//                    + " 			GROUP BY pr.lmCode "
//                    + " 		) vL1 "
//                    + " 	GROUP BY vL1.lmCode ";
//
//            sqlPeriodQuery = " 	SELECT vL1.lmCode "
//                    + " 	,   0 AS opening "
//                    + " 	, ISNULL(SUM(vL1.billAmt), 0) AS billAmt, ISNULL(SUM(vL1.ctrCtoAmt),0) AS ctrCtoAmt, isNull(SUM(vL1.receiptAmt), 0) AS receiptAmt 		   "
//                    + " 	FROM ( "
//                    + " 			SELECT sb.lmCode, SUM(CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "')  THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END) AS billAmt  						        "
//                    + " 			, ISNULL(SUM(vPla.amount),0) as ctrCtoAmt	 "
//                    + " 			, 0 AS receiptAmt		 "
//                    + " 			FROM saleBill sb 						        "
//                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type 				                        "
//                    + " 			LEFT JOIN ( "
//                    + " 					SELECT pla.docid, pla.amount "
//                    + " 					FROM patientLimitApproval pla  "
//                    + " 					LEFT JOIN voucher_Type vt ON vt.v_Type = pla.v_Type "
//                    + " 					WHERE pla.isCancelled = 0 "
//                    + " 					AND pla.nature in (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") "
//                    + " 					AND vt.category in ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "')			 "
//                    + " 				) vPla ON vPla.docid = sb.docid "
//                    + " 			WHERE sb.isCancelled = 0 				                        "
//                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') 				                        "
//                    + " 			AND sb.v_Date >= :fromDate AND sb.v_Date <= :toDate 		 "
//                    + "                         AND  sb.site_Code IN (" + siteCodeParamList + " )"
//                    + " 			GROUP BY sb.lmCode 			                      "
//                    + " 			UNION ALL  "
//                    + " 			SELECT pr.lmCode "
//                    + " 			, 0 AS billAmt, 0 AS ctrCtoAmt "
//                    + " 			, SUM(pr.amount) AS receiptAmt 						       "
//                    + " 			FROM patientReceipt pr 						       "
//                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type		"
//                    + " 			WHERE pr.isCancelled = 0 						       "
//                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') "
//                    + " 			AND pr.v_Date >= :fromDate AND pr.v_Date <= :toDate 		               "
//                    + "                         AND  pr.site_Code IN (" + siteCodeParamList + " )"
//                    + " 			GROUP BY pr.lmCode 				             "
//                    + " 			) vL1   	 "
//                    + " 		GROUP BY vL1.lmCode      ";
            String amountField = "CASE WHEN vt.category = ('" + pvc.PHARMACY_BILL_RETURN + "') THEN -ISNULL(sb.oh_Amt_Net, 0) ELSE ISNULL(sb.oh_Amt_Net, 0) END";
            sqlOpeningQuery = " SELECT vL1.lmCode "
                    + " 	, ISNULL(SUM(vL1.billAmt), 0)  - ISNULL(SUM(vL1.ctrCtoAmt),0) - isNull(SUM(vL1.receiptAmt), 0) AS opening "
                    + " 	, 0 AS billAmt, 0 AS ctrCtoAmt, 0 AS receiptAmt 		   "
                    + " 	FROM ( "
                    + " 			SELECT sb.lmCode, SUM(" + amountField + ") AS billAmt  "
                    + " 			, SUM(CASE WHEN sb.paymentSource IN (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") THEN " + amountField + " ELSE 0 END) as ctrCtoAmt	  "
                    + " 			, 0 AS receiptAmt "
                    + " 			FROM saleBill sb "
                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type "
                    + " 			WHERE sb.isCancelled = 0 "
                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') "
                    + " 			AND sb.v_Date < :fromDate	 "
                    + "                         AND  sb.site_Code IN (" + siteCodeParamList + " )"
                    + " 			GROUP BY sb.lmCode  "
                    + " 			UNION ALL  "
                    + " 			SELECT pr.lmCode, 0 AS billAmt, 0 AS ctrCtoAmt, SUM(pr.amount) AS receiptAmt "
                    + " 			FROM patientReceipt pr "
                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type		"
                    + " 			WHERE pr.isCancelled = 0 "
                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') "
                    + " 			AND pr.v_Date < :fromDate "
                    + "                         AND  pr.site_Code IN (" + siteCodeParamList + " )"
                    + " 			GROUP BY pr.lmCode "
                    + " 		) vL1 "
                    + " 	GROUP BY vL1.lmCode ";

            sqlPeriodQuery = " 	SELECT vL1.lmCode "
                    + " 	,   0 AS opening "
                    + " 	, ISNULL(SUM(vL1.billAmt), 0) AS billAmt"
                    + "         , ISNULL(SUM(vL1.ctrCtoAmt),0) AS ctrCtoAmt, isNull(SUM(vL1.receiptAmt), 0) AS receiptAmt 		   "
                    + " 	FROM ( "
                    + " 			SELECT sb.lmCode, SUM(" + amountField + ") AS billAmt  						        "
                    + " 			, SUM(CASE WHEN sb.paymentSource IN (" + String.valueOf(STEnum.PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue()) + "," + String.valueOf(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()) + ") THEN " + amountField + " ELSE 0 END) as ctrCtoAmt	  "
                    + " 			, 0 AS receiptAmt		 "
                    + " 			FROM saleBill sb 						        "
                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type 				                        "
                    + " 			WHERE sb.isCancelled = 0 				                        "
                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL + "','" + pvc.PHARMACY_BILL_RETURN + "','" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "') 				                        "
                    + " 			AND sb.v_Date >= :fromDate AND sb.v_Date <= :toDate 		 "
                    + "                         AND  sb.site_Code IN (" + siteCodeParamList + " )"
                    + " 			GROUP BY sb.lmCode 			                      "
                    + " 			UNION ALL  "
                    + " 			SELECT pr.lmCode "
                    + " 			, 0 AS billAmt, 0 AS ctrCtoAmt "
                    + " 			, SUM(pr.amount) AS receiptAmt 						       "
                    + " 			FROM patientReceipt pr 						       "
                    + " 			LEFT JOIN voucher_Type vt ON vt.v_Type = pr.v_Type		"
                    + " 			WHERE pr.isCancelled = 0 						       "
                    + " 			AND vt.category IN ('" + pvc.PHARMACY_BILL_RECEIPT + "','" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "','" + pvc.PHARMACY_RECEIPT + "','" + pvc.PHARMACY_REFUND + "') "
                    + " 			AND pr.v_Date >= :fromDate AND pr.v_Date <= :toDate 		               "
                    + "                         AND  pr.site_Code IN (" + siteCodeParamList + " )"
                    + " 			GROUP BY pr.lmCode 				             "
                    + " 			) vL1   	 "
                    + " 		GROUP BY vL1.lmCode      ";

            sqlQuery = "SELECT "
                    + "   vL2.lmCode as code"
                    + " , MAX(lm.name) AS name"
                    + " , isNull(MAX(lm.address),'') AS address"
                    + " , isNull(max(cityName),'') as cityName"
                    + " , max(vt.short_Name) as type"
                    + " , max(pr.recid) as regNo "
                    + " , SUM(vL2.opening) AS opening "
                    + " , SUM(vL2.ctrCtoAmt) AS ctrCtoAmt "
                    + " , SUM(vL2.billAmt) AS billAmt "
                    + " , SUM(vL2.receiptAmt) AS receiptAmt "
                    + " , " + groupCode + " as groupCode "
                    + " , " + groupName + " as groupName "
                    + " , ISNULL(SUM(vL2.opening),0) + ISNULL(SUM(vL2.billAmt),0) - ISNULL(SUM(vL2.ctrCtoAmt),0) - ISNULL(SUM(vL2.receiptAmt),0) AS balanceAmt   "
                    + " FROM ( ";
            if (DmShared.xNull(filter.get("includeOpening").getTag()).equalsIgnoreCase("1")) {
                sqlQuery += sqlOpeningQuery;
                sqlQuery += " 	UNION ALL ";
            }
            sqlQuery += sqlPeriodQuery;
            sqlQuery += " 	) AS vL2 	  "
                    + " LEFT JOIN ledgerMast lm ON lm.code = vL2.lmCode "
                    + " LEFT JOIN patientRegistration pr ON pr.regDocId = lm.regDocId 	  "
                    + "	LEFT JOIN organizationMast om ON om.subCode = pr.organizationCode "
                    + "	LEFT JOIN subGroup as sgo ON om.subCode = sgo.subCode "
                    + " LEFT JOIN voucher_type as vt ON vt.v_type = pr.v_Type	  "
                    + " left join city as c on lm.cityCode=c.cityCode    "
                    + " WHERE 1=1 " + condition
                    + " GROUP BY vL2.lmCode "
                    + conditionHaving;
            if (!DmShared.xNull(groupName).trim().equalsIgnoreCase("''")) {
                sqlQuery += " ORDER BY " + groupName + " , MAX(lm.name) ";
            } else {
                sqlQuery += " ORDER BY  MAX(lm.name) ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            jasperReport = DmServer.compileJasper("Pharmacy_OutstandingRegister");
            jasperPrint = DmServer.fillJasper(this, "Pharmcy Outstanding", jasperReport, null, filter, rsTemp);
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
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
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
                    + " left join voucher_Type as vt on vt.v_Type=pr.v_type"
                    + " left join subGroup as sg on lm.subCode=sg.subCode"
                    + " where 1=1 " + condition);
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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
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
            if (!DmShared.xNull(filter.get("organisation").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("organisation", DmShared.xNull(filter.get("organisation").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBalanceType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("N");
            listNode.setSharedKeyFieldName("Negative Balance");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Positive Balance");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Z");
            listNode.setSharedKeyFieldName("Zero Balance");
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
    public DmResultPageing<List<KeyFieldExtended>> getGroupName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PatientType");
            listNode.setSharedKeyFieldName("Patient Type");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Organisation");
            listNode.setSharedKeyFieldName("Organisation");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOrganization(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select om.subCode as code "
                    + " ,sg.name as name "
                    + " from organizationMast om "
                    + " LEFT JOIN subGroup sg ON sg.subCode=om.subCode ");
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
        } finally {

        }
        return rstTemp;
    }

}
