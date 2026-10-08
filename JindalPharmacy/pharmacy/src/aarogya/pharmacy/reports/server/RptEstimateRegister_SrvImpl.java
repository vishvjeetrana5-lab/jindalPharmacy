package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptEstimateRegister_Srv;
import aarogya.pharmacy.reports.shared.TxtHRegNo_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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

public class RptEstimateRegister_SrvImpl extends DmRSSPrivate implements RptEstimateRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<String> execute(LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = " ", conditionDetail = " ", conditionSummary = "", conditionItem = "";

        try {
            validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date >= :fromDate ";
                conditionSummary += " and  sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date <= :toDate ";
                conditionSummary += " and  sb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("estimateType").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("estimateType").getTag()) + " ) ";
                conditionSummary += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("estimateType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("estimateNo").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("estimateNo").getTag()) + " ) ";
                conditionSummary += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("estimateNo").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + " )";
                conditionSummary += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and sb.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultantName").getTag()) + " )";
                conditionSummary += " and sb.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultantName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("regNo").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.regdocId  in (" + DmServer.getParamList(":regdocId", filter.get("regNo").getTag()) + " )";
                conditionSummary += " and  sb.regdocId  in (" + DmServer.getParamList(":regdocId", filter.get("regNo").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("medicineName").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sbd.itemCode  in (" + DmServer.getParamList(":medicineCode", filter.get("medicineName").getTag()) + " )";
                conditionItem += " and  sbd.itemCode  in (" + DmServer.getParamList(":medicineCode", filter.get("medicineName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND im.itemGroup in (" + DmServer.getParamList(":medicineGroup", filter.get("medicineGroup").getTag()) + " )";
                conditionItem += " AND im.itemGroup in (" + DmServer.getParamList(":medicineGroup", filter.get("medicineGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND im.itemCategoryCode in (" + DmServer.getParamList(":medicineCat", filter.get("medicineCat").getTag()) + " )";
                conditionItem += " AND im.itemCategoryCode in (" + DmServer.getParamList(":medicineCat", filter.get("medicineCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("restrictGroup").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND im.restrictGroup in (" + DmServer.getParamList(":restrictGroup", filter.get("restrictGroup").getTag()) + " )";
                conditionItem += " AND im.restrictGroup in (" + DmServer.getParamList(":restrictGroup", filter.get("restrictGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
                conditionSummary += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("billedType").getTag()).trim().equalsIgnoreCase("B")) {
                conditionDetail += " and isNull(bill.docId,'') <>''";
                conditionSummary += " and isNull(bill.docId,'')<>''";

            }
            if (DmShared.xNull(filter.get("billedType").getTag()).trim().equalsIgnoreCase("U")) {
                conditionDetail += " and isNull(bill.docId,'')=''";
                conditionSummary += " and isNull(bill.docId,'')=''";

            }
            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("S")) {
                sqlQuery = "    select  bill.docId as billNo,sb.departmentCode,sdm.name as departmentName,sb.recid as estimateNo,"
                        + " sb.v_Date as estimateDate,vt.short_Name as type,vt.description,"
                        + " case when isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) else lm.displayName end as patientName,"
                        + " pr.recid as regNo,vtpr.short_Name as regType , "
                        + " sb.oh_Amt_BAMT as netAmount, sb.oh_Amt_UGST, sb.oh_Amt_SGST,sb.oh_Amt_CGST,sb.oh_Amt_Net,sb.oh_Amt_Roundoff,"
                        + " case when isNull(bill.docId,'') <> '' then 'Billed' else 'UnBilled' end as billedStatus "
                        + " from saleBIll as sb"
                        + " left join voucher_Type as vt on sb.v_Type=vt.v_Type"
                        + " left join ledgerMast as lm on sb.lmCode=lm.Code "
                        + " left join saleBill as bill on  sb.DocId=bill.estimatedocId "
                        + " LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid  "
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type"
                        + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode"
                        + " left join "
                        + "         ("
                        + "		 select sbd.docId"
                        + "		 from saleBillDetail as sbd"
                        + "		 LEFT JOIN saleBill sb ON sbd.docId = sb.docid"
                        + "		 LEFT JOIN voucher_Type vtp ON vtp.v_Type = sb.v_Type"
                        + "		 LEFT JOIN itemMast im ON im.code = sbd.itemCode"
                        + "		 AND vtp.category=('" + pvc.PHARMACY_ESTIMATE + "')  " + conditionItem
                        + "		 group by sbd.docId"
                        + "         )as sbd on sb.docid=sbd.docid"
                        + " where vt.category=('" + pvc.PHARMACY_ESTIMATE + "')  and sb.isCancelled=0 "
                        + " and sbd.docid is not null" + conditionSummary
                        + " order by departmentName,sb.v_date";

            } else {
                sqlQuery = " select  bill.docId as billNo,sb.departmentCode,sdm.name as departmentName,sb.docId,sb.recid as estimateNo,"
                        + " sb.v_Date as estimateDate,vt.short_Name as type,sb.v_Type,case when "
                        + " isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) else lm.displayName end as patientName,"
                        + " pr.recid as regNo,vtpr.short_Name as regType , im.name AS medName, sbd.batchNo,sbd.expiryDate, "
                        + " sbd.mrp, sbd.Rate as saleRate,sbd.quantity, umsale.unit AS saleUnitName,"
                        + " sbd.oh_Amt_Gross,sbd.oh_Amt_Taxable, sbd.oh_Amt_UGST, sbd.oh_Amt_SGST,sbd.oh_Amt_CGST,sbd.oh_Amt_Net as Amount,"
                        + " sb.oh_Amt_RoundOff,sb.oh_Amt_Net,"
                        + " case when isNull(bill.docId,'') <> '' then 'Billed' else 'UnBilled' end as billedStatus "
                        + " from saleBIll as sb"
                        + " left join voucher_Type as vt on sb.v_Type=vt.v_Type"
                        + " left join ledgerMast as lm on sb.lmCode=lm.Code "
                        + " left join saleBill as bill on  sb.DocId=bill.estimatedocId"
                        + " left join saleBillDetail as sbd on sb.docId=sbd.docId"
                        + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode"
                        + " LEFT JOIN patientregistration pr ON sb.regDocid = pr.regDocid  "
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type"
                        + " LEFT JOIN itemMast im ON im.code = sbd.itemCode "
                        + " left join unitMast AS umsale ON umsale.code = sbd.saleUnitCode "
                        + " where sbd.isFooter=0  and sb.isCancelled=0  and vt.category=(  '" + pvc.PHARMACY_ESTIMATE + "' ) " + conditionDetail
                        + " ORDER BY departmentName,sb.v_Date, vt.short_Name, sb.v_No, sb.recId, sbd.v_SNo ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            LinkedHashMap hm = new LinkedHashMap();
            if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("S")) {
                jasperReport = DmServer.compileJasper("Pharmacy_EstimateSummaryRegister");
                jasperPrint = DmServer.fillJasper(this, "Estimate Report (Summary) ", jasperReport, hm, filter, rsTemp);
            } else {
                jasperReport = DmServer.compileJasper("Pharmacy_EstimateDetailRegister");
                jasperPrint = DmServer.fillJasper(this, "Estimate Report (Detail) ", jasperReport, hm, filter, rsTemp);
            }
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
        if ((!DmShared.dNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) && (!DmShared.dNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase(""))) {
            if (DmShared.convertDate(hashMapFilter.get("fromDate").getText()).getTime() > DmShared.convertDate(hashMapFilter.get("toDate").getText()).getTime()) {
                throw new DmException(100, "From Date should be less than or equal to To Date.");
            }
        }
        DmShared.isValidText(hashMapFilter.get("type").getTag(), "Type", 200);
        DmShared.isValidText(hashMapFilter.get("site").getTag(), "Site", 300);
    }

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("estimateType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("estimateType").getTag()));
            }

            if (!DmShared.xNull(filter.get("estimateNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(filter.get("estimateNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("departmentName").getTag()));
            }
            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("consultantCode", DmShared.xNull(filter.get("consultantName").getTag()));
            }
            if (!DmShared.xNull(filter.get("regNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("regdocId", DmShared.xNull(filter.get("regNo").getTag()));
            }

            if (!DmShared.xNull(filter.get("medicineName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicineName").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCat", DmShared.xNull(filter.get("medicineCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("restrictGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("restrictGroup", DmShared.xNull(filter.get("restrictGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }

        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Detail");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Summary");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBilledType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("B");
            listNode.setSharedKeyFieldName("Billed");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("UnBilled");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String vCat = "'ESTMT'";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT"
                    + "   sb.docId "
                    + " , sb.recId AS name"
                    + " FROM saleBill sb"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = sb.v_Type"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_ESTIMATE + "')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineName(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT   "
                    + "  im.Code,im.name ,im.manualCode "
                    + " FROM itemMast im "
                    + " WHERE  (im.filterItemType = :filterItemType or isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
                    + " WHERE (ic.filterItemType = :filterItemType or isCommon=1)");
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
                    + "   WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1) ");;
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
    public DmResultPageing<List<TxtHRegNo_Mdl>> getTxtHRegNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRegNo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHRegNo_Mdl> lstTemp = new ArrayList<>();
        TxtHRegNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "select pr.regDocId, max(pr.recId) regNo, max(pr.v_Date) as v_Date, max(pr.dobDisplay) as dobDisplay, max(lm.displayName) as patientName, "
                    + "		max(sb.mobile) as mobile , max(gen.name) gender, max(vtr.short_Name) as type"
                    + "	from saleBill as sb"
                    + " left join ledgerMast as lm on sb.lmCode=lm.code "
                    + " left join patientRegistration as pr on sb.regDocId=pr.regDocId "
                    + "	left join patientUHID pu on pu.docId = pr.uhid "
                    + "	left join patientUHID2 pu2 on pu2.uhid = pr.uhid "
                    + "	left join hl7Mast gen on gen.code = pu2.genderCode "
                    + "	left join voucher_Type vt on sb.v_Type = vt.v_Type"
                    + " left join voucher_Type vtr on pr.v_Type = vtr.v_Type "
                    + "	where pr.site_Code = :siteCode and pr.isCancelled = 0 "
                    + " and vt.category in  ('" + pvc.PHARMACY_ESTIMATE + "') "
                    + " group by pr.regDocId";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRegNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("regdocid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("regNo")));
                listNode.setType(DmShared.xNull(rsTemp.getString("type")));
                listNode.setAge(DmShared.xNull(rsTemp.getString("dobDisplay")));
                listNode.setGender(DmShared.xNull(rsTemp.getString("gender")));
                listNode.setMobile(DmShared.xNull(rsTemp.getString("mobile")));
                listNode.setPatientName(DmShared.xNull(rsTemp.getString("patientName")));

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
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_ESTIMATE + "')");
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

}
