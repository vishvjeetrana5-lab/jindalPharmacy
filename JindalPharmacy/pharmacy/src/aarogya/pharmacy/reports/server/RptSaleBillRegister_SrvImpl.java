package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptSaleBillRegister_Srv;
import aarogya.pharmacy.reports.shared.TxtHSaleBillNo_Mdl;
import aarogya.pharmacy.reports.shared.TxtHLMCode_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
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

public class RptSaleBillRegister_SrvImpl extends DmRSSPrivate implements RptSaleBillRegister_Srv {

    private ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, String widgetName, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String category, String recIdPrefixFrom, String vNoFrom, String recIdPrefixTo, String vNoTo) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        FileWriter fileWriter = null;
        String fileName = widgetName.replaceAll(" ", "");
        String sqlQuery = " ", conditionDetail = " ", conditionSummary = "", conditionItem = "";
        String sgOrgJoin = "", sgOrgOrderBy = "", sgOrgName = "'' as organizaionName,";
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
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

            if (!DmShared.xNull(recIdPrefixFrom).toString().trim().equals("") && !DmShared.xNull(recIdPrefixTo).toString().trim().equals("")) {
                if (DmShared.xNull(recIdPrefixFrom).toString().trim().equalsIgnoreCase(recIdPrefixTo)) {
                    conditionDetail += " and sb.recIdPrefix = :recIdPrefix ";
                    conditionSummary += " and sb.recIdPrefix = :recIdPrefix ";

                    if (DmShared.iNull(vNoFrom).intValue() < DmShared.iNull(vNoTo).intValue()) {
                        conditionDetail += " and  sb.v_No >= :vNoFrom ";
                        conditionSummary += " and  sb.v_No >= :vNoFrom ";

                        conditionDetail += " and  sb.v_No <= :vNoTo ";
                        conditionSummary += " and  sb.v_No <= :vNoTo ";
                    } else {
                        throw new Exception("'From Bill No' should be Prior to 'To Bill No'.");
                    }

                } else {
                    throw new Exception("Please select Bill from same series.");
                }
            }

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date >= :fromDate ";
                conditionSummary += " and  sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date <= :toDate ";
                conditionSummary += " and  sb.v_Date <= :toDate ";
            }
            if (DmShared.xNull(filter.get("valueType").getText()).trim().equalsIgnoreCase("Negative")) {
                conditionDetail += " and  sb.oh_Amt_Net < 0 ";
                conditionSummary += " and  sb.oh_Amt_Net < 0 ";
            } else if (DmShared.xNull(filter.get("valueType").getText()).trim().equalsIgnoreCase("Positive")) {
                conditionDetail += " and  sb.oh_Amt_Net > 0 ";
                conditionSummary += " and  sb.oh_Amt_Net > 0 ";
            } else if (DmShared.xNull(filter.get("valueType").getText()).trim().equalsIgnoreCase("Zero")) {
                conditionDetail += " and  sb.oh_Amt_Net = 0 ";
                conditionSummary += " and  sb.oh_Amt_Net = 0 ";
            }
            if (!DmShared.xNull(filter.get("saleType").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("saleType").getTag()) + " ) ";
                conditionSummary += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("saleType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("billNo").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("billNo").getTag()) + " ) ";
                conditionSummary += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("billNo").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + " )";
                conditionSummary += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and sb.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultantName").getTag()) + " )";
                conditionSummary += " and sb.consultantCode in (" + DmServer.getParamList(":consultantCode", filter.get("consultantName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("operation").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.serviceCode   = :serviceCode";
                conditionSummary += " and  sb.serviceCode   = :serviceCode";
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
            if (!DmShared.xNull(filter.get("subCat").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND im.subCategoryCode IN (" + DmServer.getParamList(":subCatCode ", filter.get("subCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("restrictGroup").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND im.restrictGroup in (" + DmServer.getParamList(":restrictGroup", filter.get("restrictGroup").getTag()) + " )";
                conditionItem += " AND im.restrictGroup in (" + DmServer.getParamList(":restrictGroup", filter.get("restrictGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
                conditionSummary += " AND  sb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("paymentSource").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.paymentSource IN (" + DmServer.getParamList(":paymentSource ", filter.get("paymentSource").getTag()) + " )";
                conditionSummary += " AND  sb.paymentSource IN (" + DmServer.getParamList(":paymentSource ", filter.get("paymentSource").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.OPD)) {
                    conditionDetail += " and  vtpr.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                    conditionSummary += " and  vtpr.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.IPD)) {
//                    conditionDetail += " and  vtpr.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
//                    conditionSummary += " and  vtpr.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
                    conditionDetail += " and    sb.regType_HIS = ('" + pvc.IPD + "')  ";
                    conditionSummary += " and    sb.regType_HIS = ('" + pvc.IPD + "')  ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
                    conditionDetail += " and  vtpr.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                    conditionSummary += " and  vtpr.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
                    conditionDetail += " and  vtpr.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                    conditionSummary += " and  vtpr.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("L")) {
                    conditionDetail += " and isNull(lm.subCode,'')<>''  ";
                    conditionSummary += " and isNull(lm.subCode,'')<>''  ";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("W")) {
                    conditionDetail += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
                    conditionSummary += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
                }
            }

            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  pu.organizationCode IN (" + DmServer.getParamList(":organization", filter.get("organization").getTag()) + " ) ";
                conditionSummary += " AND  pu.organizationCode IN (" + DmServer.getParamList(":organization", filter.get("organization").getTag()) + " ) ";
                sgOrgJoin = " left join " + STServer.getSsnParentDateBase(this) + ".dbo.patientUHID pu on pu.docid = sb.lmCode\n"
                        + " left join " + STServer.getSsnParentDateBase(this) + ".dbo.subGroup sgOrg on sgOrg.subCode = pu.organizationCode \n";
                sgOrgOrderBy = "sgOrg.name,";
                sgOrgName = "sgOrg.name as organizaionName,";
            }

            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.paymentSource IN (" + DmServer.getParamList(":paymentSource", filter.get("paymentSource").getTag()) + " ) ";
                conditionSummary += " AND  sb.paymentSource (" + DmServer.getParamList(":paymentSource", filter.get("paymentSource").getTag()) + " ) ";
            }
            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                sqlQuery = "   select " + sgOrgName + "sb.departmentCode,sdm.name as departmentName,\n"
                        + " sb.recid as billNo,sb.v_Date as billDate,vt.short_Name as type,vt.description,\n"
                        + " sb.v_Type,est.recid as estimateNo,\n"
                        + " case when isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) \n"
                        + " else lm.displayName end as patientName,pr.recid as regNo,\n"
                        + " vtpr.short_Name as regType , sb.oh_Amt_Gross as grossAmount,\n"
                        + " ISNULL(sb.oh_Amt_Discount,0) + ISNULL(sb.oh_Amt_Discount2,0) as discountAmt, sb.oh_Amt_Taxable, \n"
                        + " sb.oh_Amt_BAMT as netAmount, sb.oh_Amt_UGST, sb.oh_Amt_SGST,sb.oh_Amt_CGST,sb.oh_Amt_Net,sb.oh_Amt_Roundoff \n"
                        + " from saleBIll as sb\n"
                        + " left join voucher_Type as vt on sb.v_Type=vt.v_Type\n"
                        + " left join ledgerMast as lm on sb.lmCode=lm.Code\n"
                        + " left join saleBill as est on sb.docId=est.estimateDocId\n"
                        + " LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid  \n"
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type\n"
                        + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode\n" + sgOrgJoin
                        + " left join \n"
                        + "         (\n"
                        + "		 select sbd.docId,sum(sbd.oh_Amt_Net) as netAmount\n"
                        + "		 from saleBillDetail as sbd\n"
                        + "		 LEFT JOIN saleBill sb ON sbd.docId = sb.docid\n"
                        + "		 LEFT JOIN voucher_Type vtp ON vtp.v_Type = sb.v_Type\n"
                        + "		 LEFT JOIN itemMast im ON im.code = sbd.itemCode\n"
                        + "		 WHERE vtp.category in( '" + category + "' )\n" + conditionItem
                        + "		 group by sbd.docId\n"
                        + "         )as sbd on sb.docid=sbd.docid\n"
                        + " where vt.category in(  '" + category + "' )  and sb.isCancelled=0 \n"
                        + " and sbd.docid is not null\n" + conditionSummary
                        + " order by sdm.name," + sgOrgOrderBy + " sb.v_Date , vt.short_Name, sb.v_no , sb.recId\n";

            } else {
                sqlQuery = " select " + sgOrgName + " sb.remark,sb.departmentCode,sdm.name as departmentName,sb.docId,sb.recid as billNo,sb.v_Date as billDate,vt.short_Name as type \n"
                        + ",sb.v_Type,est.recid as estimateNo,rq.recid as requisitionNo,\n"
                        + " case when isNull(lm.Initial,'')<>'' then concat(lm.initial,'',lm.displayName) else lm.displayName end as patientName,\n"
                        + " pr.recid as regNo,vtpr.short_Name as regType , im.name AS medName, sbd.batchNo,sbd.expiryDate, sbd.mrp, sbd.Rate as saleRate,\n"
                        + " sbd.quantity, umsale.unit AS saleUnitName,\n"
                        + " sbd.oh_Amt_Gross,sbd.oh_Amt_Taxable, sbd.oh_Amt_UGST, sbd.oh_Amt_SGST,sbd.landedSkuValue,\n"
                        + " sbd.oh_Amt_CGST,sbd.oh_Amt_IGST,sbd.oh_Amt_Net as amount,\n"
                        + " sb.oh_Amt_RoundOff, sb.oh_Amt_Net, ISNULL(sbd.oh_Amt_Discount,0) + ISNULL(sbd.oh_Amt_Discount2,0) AS discountAmt \n"
                        + " , msc.name AS subCategory,sb.preparedBy as userName"
                        + " from saleBIll as sb\n"
                        + " left join ledgerMast as lm on sb.lmCode=lm.Code\n"
                        + " left join voucher_Type as vt on sb.v_Type=vt.v_Type\n"
                        + " left join saleBillDetail as sbd on sb.docId=sbd.docId\n"
                        + " left join requisitionDetail as rqd on sbd.requisitionDocId=rqd.docId AND sbd.requisitionV_Sno = rqd.v_SNo\n"
                        + " left join requisition as rq on rq.docId = rqd.DocId\n"
                        + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode\n"
                        + " left join saleBill as est on sb.estimateDocId = est.docid\n"
                        + " LEFT JOIN patientregistration pr ON sb.regDocid = pr.regDocid  \n"
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type\n"
                        + " LEFT JOIN itemMast im ON im.code = sbd.itemCode \n" + sgOrgJoin
                        + " LEFT JOIN medicineSubCategoryMast msc ON im.subCategoryCode = msc.code"
                        + " left join unitMast AS umsale ON umsale.code = sbd.saleUnitCode \n"
                        + " where sbd.isFooter=0   and sb.isCancelled=0 and vt.category in(  '" + category + "' )\n" + conditionDetail
                        + "  ORDER BY departmentName," + sgOrgOrderBy + "sb.v_Date, vt.short_Name, sb.v_No, sb.recId, sbd.v_SNo ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp, vNoFrom, vNoTo, recIdPrefixFrom, recIdPrefixTo);
            rsTemp = psTemp.executeQuery();

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                    throw new DmException(20, "Choose report type detail.");
                }

                String FILE_HEADER = "Bill No,Bill Date,Type ,Department Name,Estimate No.,Reg. No., Reg. Type,Patient Name ,Medicine,SubCategory,Requisition No.,BatchNo.,Expiry Dt.,MRP,"
                        + "Sale Rate,Quantity,Sale Unit Name,Gross Amt.,Disc. Amt.,Taxable Value,GST,Remark,userName";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                double total_TaxAbleAmt = 0, total_GST = 0, total_ROundOff = 0, total_NetAmt = 0;
                while (rsTemp.next()) {
                    lineData = "\"" + DmShared.xNull(rsTemp.getString("billNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("billDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("type")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("departmentName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("estimateNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("regNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("regType")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("patientName")) + "\"" + COMMA_DELIMITER;

                    String medName = rsTemp.getString("medName");
                    String backSlash = "";
                    if (medName.contains("\"")) {
                        backSlash = "\"";
                    }

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("medName")) + backSlash + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("subCategory")) + backSlash + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("requisitionNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("batchNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("expiryDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("mrp")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("saleRate")) + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("quantity")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("saleUnitName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("oh_Amt_Gross")) + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmShared.xNull(rsTemp.getString("discountAmt")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("oh_Amt_Taxable")) + "\"" + COMMA_DELIMITER;

                    total_TaxAbleAmt += DmShared.iNull(rsTemp.getString("oh_Amt_Taxable")).doubleValue();

                    double GST = 0, CGST = 0, UGST = 0, SGST = 0;
                    CGST = DmShared.iNull(rsTemp.getString("oh_Amt_CGST")).doubleValue();
                    UGST = DmShared.iNull(rsTemp.getString("oh_Amt_UGST")).doubleValue();
                    SGST = DmShared.iNull(rsTemp.getString("oh_Amt_SGST")).doubleValue();
                    GST = CGST + UGST + SGST;

                    lineData += "\"" + DmShared.iNull(GST).doubleValue() + "\"" + COMMA_DELIMITER;
                    total_GST += DmShared.iNull(GST).doubleValue();

//                    lineData += "\"" + DmShared.xNull(rsTemp.getString("oh_Amt_RoundOff")) + "\"" + COMMA_DELIMITER;
//                    total_ROundOff += DmShared.iNull(rsTemp.getString("oh_Amt_RoundOff")).doubleValue();
//
//                    lineData += "\"" + DmShared.xNull(rsTemp.getString("oh_Amt_Net")) + "\"" + COMMA_DELIMITER;
//                    total_NetAmt += DmShared.iNull(rsTemp.getString("oh_Amt_Net")).doubleValue();
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("remark")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("userName")) + "\"" + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }
//
//                lineData = "\"" + "" + "\"" + COMMA_DELIMITER;
//                for (int i = 0; i < 16; i++) {
//                    lineData += "\"" + "" + "\"" + COMMA_DELIMITER;
//                }
//                lineData += "\"" + "Total :-" + "\"" + COMMA_DELIMITER;
//                lineData = "\"" + "" + "\"" + COMMA_DELIMITER;
//
                fileWriter.flush();
                fileWriter.close();

            }

            LinkedHashMap hm = new LinkedHashMap();

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
//                if (category.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL))
                if (DmShared.xNull(category).contains(new ProjectVoucherCategory().PHARMACY_BILL)) {
                    if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                        jasperReport = DmServer.compileJasper("Pharmacy_SaleBillSummaryRegister");
                        jasperPrint = DmServer.fillJasper(this, "Sale Bill Report (Summary) ", jasperReport, hm, filter, rsTemp);
                        keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                    } else {
                        jasperReport = DmServer.compileJasper("Pharmacy_SaleBillDetailRegister");
                        jasperPrint = DmServer.fillJasper(this, "Sale Bill Report (Detail) ", jasperReport, hm, filter, rsTemp);
                        keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                    }
                } else {
                    if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                        jasperReport = DmServer.compileJasper("Pharmacy_EstimateBillSummaryRegister");
                        jasperPrint = DmServer.fillJasper(this, "Estimate Bill Report (Summary) ", jasperReport, hm, filter, rsTemp);
                        keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                    } else {
                        jasperReport = DmServer.compileJasper("Pharmacy_EstimateBillDetailRegister");
                        jasperPrint = DmServer.fillJasper(this, "Estimate Bill Report (Detail) ", jasperReport, hm, filter, rsTemp);
                        keyField.setSharedKeyFieldCode(DmServer.showJasper(this, jasperPrint, "", reportProperty));
                    }
                }
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
        DmShared.isValidText(hashMapFilter.get("reportType").getTag(), "Type", 200);
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
            if (!DmShared.xNull(filter.get("medicineName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicineName").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCat", DmShared.xNull(filter.get("medicineCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("subCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCatCode", DmShared.xNull(filter.get("subCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("restrictGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("restrictGroup", DmShared.xNull(filter.get("restrictGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("organization", DmShared.xNull(filter.get("organization").getTag()));
            }
            if (!DmShared.xNull(filter.get("paymentSource").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("paymentSource", DmShared.xNull(filter.get("paymentSource").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReportType(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResultPageing<List<TxtHSaleBillNo_Mdl>> getTxtHBillNo(String category, String siteCode, DmHelper helper, boolean blnToEndOfFile) {
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
                    + " WHERE vt.category IN ('" + category + "')" + condition;
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
                    + "  im.Code,im.name,im.manualCode  "
                    + " FROM itemMast im "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1) ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = " select code ,name from medicineSubCategoryMast ";
            sqlWithParam.setSQL(sqlQuery);

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);;
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
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
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
                    + "   WHERE (ig.filterItemType = :filterItemType or isCommon=1) ");;
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBillType(String category, DmHelper pageing, boolean blnToEndOfFile) {
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
                    + " WHERE vt.category IN ('" + category + "')");
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
    public DmResultPageing<List<KeyFieldExtended>> getOrganization(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select om.subCode, sg.name "
                    + "	from organizationMast om"
                    + "	left join subGroup sg on sg.subCode = om.subCode"
                    + "	left join subGroupNature sgn on sgn.code = sg.nature"
                    + "	where sgn.manualCode ='OR' and sg.isActive = 1");
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
    public DmResultPageing<List<KeyFieldExtended>> getpaymentSource(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(STEnum.PaymentSource.PAYMENT.getEnumValue()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(STEnum.PaymentSource.PAYMENT.getEnumName()));
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(DmShared.iNull(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue()).toString());
            listNode.setSharedKeyFieldName(DmShared.xNull(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumName()));
            lstTemp.add(listNode);

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
