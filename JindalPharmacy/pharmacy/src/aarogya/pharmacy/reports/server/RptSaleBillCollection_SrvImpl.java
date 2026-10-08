package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptSaleBillCollection_Srv;
import aarogya.pharmacy.reports.shared.TxtHSaleBillNo_Mdl;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.PaymentSource;
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

public class RptSaleBillCollection_SrvImpl extends DmRSSPrivate implements RptSaleBillCollection_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String recIdPrefixFrom, String vNoFrom, String recIdPrefixTo, String vNoTo) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", condition = "", condition_PLA = "", condition_Rect = "";

        try {
            validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(recIdPrefixFrom).toString().trim().equals("") && !DmShared.xNull(recIdPrefixTo).toString().trim().equals("")) {
                if (DmShared.xNull(recIdPrefixFrom).toString().trim().equalsIgnoreCase(recIdPrefixTo)) {
                    condition += " and sb.recIdPrefix = :recIdPrefix ";

                    if (DmShared.iNull(vNoFrom).intValue() < DmShared.iNull(vNoTo).intValue()) {
                        condition += " and  sb.v_No >= :vNoFrom ";

                        condition += " and  sb.v_No <= :vNoTo ";
                    } else {
                        throw new Exception("'From Bill No' should be Prior to 'To Bill No'.");
                    }

                } else {
                    throw new Exception("Please select Bill from same series.");
                }
            }
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  sb.v_Date <= :toDate ";
            }
            if (DmShared.xNull(filter.get("valueType").getText()).trim().equalsIgnoreCase("Negative")) {
                condition += " and  sb.oh_Amt_Net < 0 ";
            } else if (DmShared.xNull(filter.get("valueType").getText()).trim().equalsIgnoreCase("Positive")) {
                condition += " and  sb.oh_Amt_Net > 0 ";
            } else if (DmShared.xNull(filter.get("valueType").getText()).trim().equalsIgnoreCase("Zero")) {
                condition += " and  sb.oh_Amt_Net = 0 ";
            }
            if (!DmShared.xNull(filter.get("saleType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("saleType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("billNo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("billNo").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and sb.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultantName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("operation").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and  sb.serviceCode   = :serviceCode";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.OPD)) {
                    condition += " and  vtpr.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.IPD)) {
                    condition += " and  vtpr.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    condition += " and  vtpr.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    condition += " and  vtpr.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("L")) {
                    condition += " and isNull(lm.subCode,'')<>''  ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("W")) {
                    condition += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
                }
            }

            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  lm.code IN (" + DmServer.getParamList(":phAccount", filter.get("phAccount").getTag()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("payMode").getTag()).trim().equalsIgnoreCase("")) {
                condition_Rect += " AND cl.cardTypeCode IN (" + DmServer.getParamList(":cardTypeCode", filter.get("payMode").getTag()) + " ) ";
//                condition_PLA += " AND cast(pla.nature as int) IN (" + DmServer.getParamList(":cardTypeCode", filter.get("payMode").getTag()) + " ) ";
                condition_PLA += " AND cast(sb.paymentSource as int) IN (" + DmServer.getParamList(":cardTypeCode", filter.get("payMode").getTag()) + " ) ";
                condition += " AND vPrec.docId IS NOT NULL ";
            } else {
//                condition_PLA += " and pla.nature in( " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ", " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") ";
                condition_PLA += " and sb.paymentSource in( " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + ", " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + ", " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + ") ";
            }

            if (!DmShared.xNull(filter.get("organisation").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pr.organizationCode IN (" + DmServer.getParamList(":organisation", filter.get("organisation").getTag()) + " ) ";
            }

            if (!DmShared.xNull(filter.get("userName").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND  sb.preparedBy IN (" + DmServer.getParamList(":userName ", filter.get("userName").getTag()) + " )";
            }

//            sqlQuery = "   select sb.docId, sb.departmentCode,sdm.name as departmentName,"
//                    + " sb.recid as billNo,sb.v_Date as billDate,vt.short_Name as type,vt.description,"
//                    + " sb.v_Type, sb.v_No,est.recid as estimateNo,"
//                    + " case when isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) "
//                    + " else lm.displayName end as patientName,pr.recid as regNo,"
//                    + " vtpr.short_Name as regType , sb.oh_Amt_Gross as grossAmount,"
//                    + " ISNULL(sb.oh_Amt_Discount,0) + ISNULL(sb.oh_Amt_Discount2,0) as discountAmt, sb.oh_Amt_Taxable, "
//                    + " sb.oh_Amt_BAMT as netAmount, sb.oh_Amt_UGST, sb.oh_Amt_SGST,sb.oh_Amt_CGST,sb.oh_Amt_Net,sb.oh_Amt_Roundoff, "
//                    + " vPrec.cardName, "
//                    + " vPrec.collectionAmt "
//                    + " from saleBIll as sb"
//                    + " left join voucher_Type as vt on sb.v_Type=vt.v_Type"
//                    + " left join ledgerMast as lm on sb.lmCode=lm.Code"
//                    + " left join saleBill as est on sb.docId=est.estimateDocId"
//                    + " LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid  "
//                    + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type"
//                    + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode"
//                    + " LEFT JOIN ( "
//                    + "    SELECT prec.docId, cm.name As cardName,  (ISNULL(cl.amtDr,0) - ISNULL(cl.amtCr,0)) AS collectionAmt   "
//                    + "     FROM patientReceipt prec  "
//                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = prec.v_Type "
//                    + "     LEFT JOIN cardLedger cl ON cl.docid = prec.docid   "
//                    + "     left join cardTypeMast cm On cl.cardTypeCode = cm.code  "
//                    + "     WHERE vt.category = '" + pvc.PHARMACY_BILL_RECEIPT + "' " + condition_Rect //cl.cardTypeCode IN (100010001)
//                    + "     UNION ALL  "
//                    + "     SELECT pla.docId,  "
//                    + "     		CASE pla.nature WHEN " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " THEN 'CTP' "
//                    + "                           WHEN " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " THEN 'CTO'                       "
//                    + "     			  WHEN " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " THEN 'CTR'  "
//                    + "     		END AS cardName,   "
//                    + "     		pla.amount AS collectionAmt   "
//                    + "     FROM patientLimitApproval pla "
//                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = pla.v_Type "
//                    + "     WHERE vt.category = '" + pvc.PHARMACY_BILL + "' " + condition_PLA //prec.nature IN (100010001)
//                    + " ) vPrec  ON vPrec.docid = sb.docid  "
//                    + " where vt.category =(  '" + pvc.PHARMACY_BILL + "' )  "
//                    + " and sb.isCancelled=0"
//                    + " " + condition 
//                    + " order by departmentName, billDate, type, v_no, billNo ";
            sqlQuery = "   select sb.docId, sb.departmentCode,sdm.name as departmentName,"
                    + " sb.recid as billNo,sb.v_Date as billDate,vt.short_Name as type,vt.description,"
                    + " sb.v_Type, sb.v_No,est.recid as estimateNo,"
                    + " case when isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) "
                    + " else lm.displayName end as patientName,pr.recid as regNo,"
                    + " vtpr.short_Name as regType , sb.oh_Amt_Gross as grossAmount,"
                    + " ISNULL(sb.oh_Amt_Discount,0) + ISNULL(sb.oh_Amt_Discount2,0) as discountAmt, sb.oh_Amt_Taxable, "
                    + " sb.oh_Amt_BAMT as netAmount, sb.oh_Amt_UGST, sb.oh_Amt_SGST,sb.oh_Amt_CGST,sb.oh_Amt_Net,sb.oh_Amt_Roundoff, "
                    + " vPrec.cardName, "
                    + " vPrec.collectionAmt "
                    + " from saleBIll as sb"
                    + " left join voucher_Type as vt on sb.v_Type=vt.v_Type"
                    + " left join ledgerMast as lm on sb.lmCode=lm.Code"
                    + " left join saleBill as est on sb.docId=est.estimateDocId"
                    + " LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid  "
                    + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type"
                    + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode"
                    + " LEFT JOIN ( "
                    + "    SELECT prec.docId, cm.name As cardName,  (ISNULL(cl.amtDr,0) - ISNULL(cl.amtCr,0)) AS collectionAmt   "
                    + "     FROM patientReceipt prec  "
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = prec.v_Type "
                    + "     LEFT JOIN cardLedger cl ON cl.docid = prec.docid   "
                    + "     left join cardTypeMast cm On cl.cardTypeCode = cm.code  "
                    + "     WHERE vt.category = '" + pvc.PHARMACY_BILL_RECEIPT + "' " + condition_Rect //cl.cardTypeCode IN (100010001)
                    + "     UNION ALL  "
                    + "     SELECT sb.docId,  "
                    + "     		CASE sb.paymentSource WHEN " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " THEN 'CTP' "
                    + "                           WHEN " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " THEN 'CTO'                       "
                    + "     			  WHEN " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " THEN 'CTR'  "
                    + "     		END AS cardName,   "
                    + "     		pla.amount AS collectionAmt   "
                    + "     FROM saleBill sb "
                    + "     LEFT JOIN patientLimitApproval pla ON pla.sbDocid = sb.docid AND sb.paymentSource = pla.nature "
                    + "     LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type "
                    + "     WHERE vt.category = '" + pvc.PHARMACY_BILL + "' AND pla.isCancelled = 0 " + condition_PLA //prec.nature IN (100010001)
                    + " ) vPrec  ON vPrec.docid = sb.docid  "
                    + " where vt.category =(  '" + pvc.PHARMACY_BILL + "' )  "
                    + " and sb.isCancelled=0"
                    + " " + condition
                    + " order by departmentName, billDate, type, v_no, billNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp, vNoFrom, vNoTo, recIdPrefixFrom, recIdPrefixTo);
            rsTemp = psTemp.executeQuery();

            LinkedHashMap hm = new LinkedHashMap();
            jasperReport = DmServer.compileJasper("Pharmacy_SaleBillCollection");
            jasperPrint = DmServer.fillJasper(this, "Sale Bill Collection", jasperReport, hm, filter, rsTemp);

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

    public static void validateInput(LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(20, "From Date is mandatory.");
        }
        if (DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(40, "To Date is mandatory.");
        }

        if (DmShared.iNull(hashMapFilter.get("fromBillNo").getTag()).longValue() != 0 && DmShared.iNull(hashMapFilter.get("toBillNo").getTag()).longValue() == 0) {
            throw new DmException(400, "To Bill No. is mandatory.");
        }
        if (DmShared.iNull(hashMapFilter.get("fromBillNo").getTag()).longValue() == 0 && DmShared.iNull(hashMapFilter.get("toBillNo").getTag()).longValue() != 0) {
            throw new DmException(500, "From Bill No. is mandatory.");
        }

        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }

        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 300);
    }

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp, String vNoFrom, String vNoTo, String recIdPrefixFrom, String recIdPrefixTo) {
        try {
            if (!DmShared.xNull(recIdPrefixFrom).toString().trim().equals("") && !DmShared.xNull(recIdPrefixTo).toString().trim().equals("")) {
                if (DmShared.xNull(recIdPrefixFrom).toString().trim().equalsIgnoreCase(recIdPrefixTo)) {
                    psTemp.setString("recIdPrefix", DmShared.xNull(recIdPrefixFrom).toString());

                    if (DmShared.iNull(vNoFrom).intValue() < DmShared.iNull(vNoTo).intValue()) {
                        psTemp.setString("vNoFrom", DmShared.iNull(vNoFrom).toString());
                        psTemp.setString("vNoTo", DmShared.iNull(vNoTo).toString());
                    }
                }
            }
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("saleType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("saleType").getTag()));
            }
            if (!DmShared.xNull(filter.get("billNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(filter.get("billNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("departmentName").getTag()));
            }
            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultantCode", DmShared.xNull(filter.get("consultantName").getTag()));
            }
            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("phAccount", DmShared.xNull(filter.get("phAccount").getTag()));
            }
            if (!DmShared.xNull(filter.get("operation").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setString("serviceCode", DmShared.xNull(filter.get("operation").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("payMode").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("cardTypeCode", DmShared.xNull(filter.get("payMode").getTag()));
            }
            if (!DmShared.xNull(filter.get("organisation").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("organisation", DmShared.xNull(filter.get("organisation").getTag()));
            }
            if (!DmShared.xNull(filter.get("userName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("userName", DmShared.xNull(filter.get("userName").getTag()));
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
            sqlWithParam.setSQL("select dm.subCode, sg.name "
                    + "	from departmentMast dm"
                    + "	left join subGroup sg on sg.subCode = dm.subCode"
                    + "	left join subGroupNature sgn on sgn.code = sg.nature"
                    + "	where sgn.manualCode in ('PH','CM') and sg.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select	cm.subCode as code, sg.name  "
                    + "		from consultantMast as cm  "
                    + "		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
                    + "		left join subGroup as sg on cm.subCode = sg.subCode  "
                    + "		left join subgroupNature as sgn on sg.nature = sgn.code  "
                    + "		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<TxtHSaleBillNo_Mdl>> getTxtHBillNo(String siteCode, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSaleBillNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSaleBillNo_Mdl> lstTemp = new ArrayList<>();
        TxtHSaleBillNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "", condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(siteCode).trim().equals("")) {
                siteCode = DmServer.getSsnSiteCode(this);
            }

            condition += " and sb.site_Code in (" + DmServer.getParamList(":site_Code", siteCode) + " )";

            sqlQuery = "SELECT"
                    + "   sb.docId "
                    + " , sb.recId AS name"
                    + " , sb.recIdPrefix"
                    + " , sb.v_No"
                    + " , vt.short_Name"
                    + " , sb.v_Date"
                    + " FROM saleBill sb"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_BILL + "')" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setStringList("site_Code", DmShared.xNull(siteCode));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSaleBillNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
                listNode.setVNo(DmShared.xNull(rsTemp.getString("v_No")));
                listNode.setRecIdPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHOperation(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code, name "
                    + "	from patientServiceMast psm "
                    + "	where psm.isActive = 1 and serviceNature = :serviceNature");
            sqlWithParam.setString("serviceNature", DmShared.iNull(STEnum.ServiceNature.OPERATION.getId()).toString());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(DmHelper pageing, boolean blnToEndOfFile) {
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
                    + "    vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_BILL + "')");
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
                    condition += " and isNull(lm.subCode,'')<>''  ";
                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("W")) {
                    condition += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHValueType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Positive");
            listNode.setSharedKeyFieldName("Positive");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Negative");
            listNode.setSharedKeyFieldName("Negative");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Zero");
            listNode.setSharedKeyFieldName("Zero");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPayMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select code, name "
                    + "from ( "
                    + "select ctm.code, ctm.name from cardTypeMast as ctm "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() + " as code , 'CTR' as name "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_ORGANIZATION.getEnumValue() + " as code , 'CTO' as name "
                    + "union all "
                    + "select " + PaymentSource.CREDIT_TO_PATIENT.getEnumValue() + " as code , 'CTP' as name "
                    + ") as ctm ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUserName(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("select um.user_Name as name "
                    + " from userMast um "
                    + " ");
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
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

}
