package aarogya.pharmacy.wholeSaleReports.server;

import aarogya.pharmacy.wholeSaleReports.shared.RptWholeSaleBillReturnRegister_Vld;
import aarogya.pharmacy.wholeSaleReports.shared.TxtHLMCode_Mdl;
import aarogya.pharmacy.wholeSaleReports.shared.RptWholeSaleBillReturnRegister_Srv;
import aarogya.pharmacy.wholeSaleReports.shared.TxtHParty_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSerializationPolicyClient;
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

public class RptWholeSaleBillReturnRegister_SrvImpl extends DmRSSPrivate implements RptWholeSaleBillReturnRegister_Srv {

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
        String sqlQuery = " ", conditionDetail = " ", conditionSummary = "", conditionItem = "";
        String sgOrgJoin = "", sgOrgOrderBy = "", sgOrgName = "'' as organizaionName,";
        FileWriter fileWriter = null;
        String fileName = widgetName.replaceAll(" ", "");
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            RptWholeSaleBillReturnRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date >= :fromDate ";
                conditionSummary += " and  sb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and  sb.v_Date <= :toDate ";
                conditionSummary += " and  sb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("saleType").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("saleType").getTag()) + " ) ";
                conditionSummary += " AND  sb.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("saleType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("returnNo").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("returnNo").getTag()) + " ) ";
                conditionSummary += " AND  sb.docId IN (" + DmServer.getParamList(":docId", filter.get("returnNo").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + " )";
                conditionSummary += " and sb.departmentCode in (" + DmServer.getParamList(":departmentCode", filter.get("departmentName").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("")) {
                if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("D")) {
                    conditionDetail += "And sb.sBillType in (" + STEnum.WholeSaleType.DEBTOR.getId() + ")";
                    conditionSummary += "And sb.sBillType in (" + STEnum.WholeSaleType.DEBTOR.getId() + ")";
                } else if (DmShared.xNull(filter.get("type").getTag()).trim().equalsIgnoreCase("W")) {
                    conditionDetail += " And sb.sBillType in (" + STEnum.WholeSaleType.WALK_IN.getId() + ") ";
                    conditionSummary += " And sb.sBillType in (" + STEnum.WholeSaleType.WALK_IN.getId() + ") ";
                }
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
            } else {
                conditionDetail += " AND  sb.site_Code in (" + DmServer.getSsnSiteCode(this) + ")";
                conditionSummary += " AND  sb.site_Code in (" + DmServer.getSsnSiteCode(this) + ")";
            }

            if (!DmShared.xNull(filter.get("paymentSource").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND  sb.paymentSource IN (" + DmServer.getParamList(":paymentSource ", filter.get("paymentSource").getTag()) + " )";
                conditionSummary += " AND  sb.paymentSource IN (" + DmServer.getParamList(":paymentSource ", filter.get("paymentSource").getTag()) + " )";
            }

            if (!DmShared.xNull(filter.get("customerName").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " AND sb.partySubCode IN (" + DmServer.getParamList(":customerName", filter.get("customerName").getTag()) + " ) \n";
                conditionSummary += " AND sb.partySubCode IN (" + DmServer.getParamList(":customerName", filter.get("customerName").getTag()) + " ) \n";
            }

            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                conditionDetail += " and im.itemMakeCode in (" + DmServer.getParamList(":medCompany", filter.get("medCompany").getTag()) + " )";
                conditionItem += " and im.itemMakeCode in (" + DmServer.getParamList(":medCompany", filter.get("medCompany").getTag()) + " )";
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

            if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                sqlQuery = "    select case when isNull(sgOrg.name,'') = '' then 'Walk-In' else \n"
                        + "	sgOrg.name end as organizaionName,sb.departmentCode,sdm.name as departmentName,sb.recid as returnNo,sb.v_Date as returnDate,vt.short_Name as type,vt.description,\n"
                        + " sb.refNo,sb.refDate,vtpr.short_Name as regType , \n"
                        + " sgd.name as patientName,pr.recid as regNo,\n"
                        + " sb.oh_Amt_Gross as grossAmount,sb.oh_Amt_Discount as discAmount,sb.oh_Amt_Taxable as taxableAmount,\n"
                        + " sb.oh_Amt_BAMT as netAmount, sb.oh_Amt_UGST, sb.oh_Amt_SGST,sb.oh_Amt_CGST,"
                        + "isNull(det.totalIGST,0) as oh_Amt_IGST,"
                        + " sb.oh_Amt_Net,sb.oh_Amt_Roundoff\n"
                        + " from saleBIll as sb\n"
                        + " left join ledgerMast as lm on sb.lmCode=lm.Code\n"
                        + " LEFT JOIN (\n"
                        + "     SELECT sbd.docId, SUM(ISNULL(sbd.oh_Amt_IGST,0)) AS totalIGST\n"
                        + "     FROM saleBillDetail sbd\n"
                        + "     GROUP BY sbd.docId\n"
                        + ") det ON sb.docId = det.docId"
                        + " left join voucher_Type as vt on sb.v_Type=vt.v_Type\n"
                        + " LEFT JOIN patientregistration pr ON sb.regDocid=pr.regDocid  \n"
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type\n"
                        + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode\n"
                        + " LEFT JOIN subgroup sgd ON sgd.subCode =  sb.partySubCode\n"
                        + " left join " + DmServer.getSsnDBName(this) + ".dbo.patientUHID pu on pu.docid = sb.lmCode\n"
                        + " left join " + DmServer.getSsnDBName(this) + ".dbo.subGroup sgOrg on sgOrg.subCode = pu.organizationCode \n"
                        + " left join \n"
                        + "         (\n"
                        + "		 select sbd.docId\n"
                        + "		 from saleBillDetail as sbd\n"
                        + "		 LEFT JOIN saleBill sb ON sbd.docId = sb.docid\n"
                        + "		 LEFT JOIN voucher_Type vtp ON vtp.v_Type = sb.v_Type\n"
                        + "		 LEFT JOIN itemMast im ON im.code = sbd.itemCode\n"
                        + "		 WHERE vtp.category in('" + pvc.WHOLE_SALE_RETURN_CREDIT + "') \n" + conditionItem
                        + "		 group by sbd.docId\n"
                        + "         )as sbd on sb.docid=sbd.docid\n"
                        + " where vt.category in('" + pvc.WHOLE_SALE_RETURN_CREDIT + "') and sb.isCancelled=0 \n"
                        + " " + conditionSummary
                        + " order by departmentName,sgOrg.name,sb.v_date";

            } else {
                sqlQuery = " select case when isNull(sgOrg.name,'') = '' then 'Walk-In' else \n"
                        + "	sgOrg.name end as organizaionName,sb.departmentCode,sdm.name as departmentName,sb.docId,sb.recid as returnNo,\n"
                        + " sb.v_Date as returnDate,vt.short_Name as type,sb.v_Type,sb.refNo,sb.refDate,\n"
                        + " sgd.name as patientName,\n"
                        + " pr.recid as regNo, vtpr.short_Name as regType , im.name AS medName, sbd.batchNo,sbd.expiryDate, sbd.mrp, sbd.Rate as saleRate,\n"
                        + " sbd.quantity, umsale.unit AS saleUnitName,sbd.oh_Amt_Gross,sbd.oh_Amt_Discount,sbd.oh_Amt_Taxable, sbd.oh_Amt_UGST,\n"
                        + " isNull(sbd.oh_Amt_SGST,0) as oh_Amt_SGST,isNull(sbd.oh_Amt_CGST,0) as oh_Amt_CGST,isNull(sbd.oh_Amt_IGST,0) as oh_Amt_IGST,\n"
                        + " isNull(sbd.oh_Amt_Net,0) as Amount, isNull(sb.oh_Amt_RoundOff,0) as oh_Amt_RoundOff,\n"
                        + " sb.oh_Amt_Net,sb.oh_Amt_Gross as grossAmount,sb.oh_Amt_Discount as discAmount,sb.oh_Amt_Taxable as taxableAmount \n"
                        + " from saleBIll as sb\n"
                        + " left join ledgerMast as lm on sb.lmCode=lm.Code\n"
                        + " left join voucher_Type as vt on sb.v_Type=vt.v_Type\n"
                        + " left join saleBillDetail as sbd on sb.docId=sbd.docId\n"
                        + " LEFT JOIN subgroup sdm ON sb.departmentCode=sdm.subcode\n"
                        + " LEFT JOIN subgroup sgd ON sgd.subCode =  sb.partySubCode\n"
                        //                       + " left join " + DmServer.getSsnDBName(this) + ".dbo.patientUHID pu on pu.docid = sb.lmCode\n"
                        //                        + " left join " + DmServer.getSsnDBName(this) + ".dbo.subGroup sgOrg on sgOrg.subCode = pu.organizationCode \n"
                        + " left join " + DmServer.getSsnDBName(this) + ".dbo.patientUHID pu on pu.docid = sb.lmCode\n"
                        + " left join " + DmServer.getSsnDBName(this) + ".dbo.subGroup sgOrg on sgOrg.subCode = pu.organizationCode \n"
                        + " LEFT JOIN patientregistration pr ON sb.regDocid = pr.regDocid  \n"
                        + " LEFT JOIN voucher_Type as vtpr ON pr.v_type=vtpr.v_type\n"
                        + " LEFT JOIN itemMast im ON im.code = sbd.itemCode \n"
                        + " left join unitMast AS umsale ON umsale.code = sbd.saleUnitCode \n"
                        + " where sbd.isFooter=0 and sb.isCancelled=0   and vt.category in('" + pvc.WHOLE_SALE_RETURN_CREDIT + "')\n" + conditionDetail
                        + " ORDER BY  departmentName,sgOrg.name,sb.v_Date, vt.short_Name, sb.v_No, sb.recId, sbd.v_SNo ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();
            //===================================================Export Data to CSV=====================
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "";

                FILE_HEADER = "Return No. ,Return Date,Type,Ref No.,Ref Date,Debtor Name,"
                        + "Gross Amt,Discount Amt,Taxable Value,GST,Round Off,Net Amount";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {

                    lineData = DmShared.xNull(rsTemp.getString("returnNo")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("returnDate")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("type")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("refNo")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("refDate")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("patientName")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("grossAmount"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("discAmount"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("taxableAmount"), "0.00") + COMMA_DELIMITER;

                    double oh_at_CGST = DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue();
                    double oh_at_SGST = DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue();
                    double oh_at_UGST = DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue();
                    double oh_at_IGST = DmShared.vNull(rsTemp.getString("oh_Amt_IGST")).doubleValue();
                    double GSTAmt = oh_at_CGST + oh_at_SGST + oh_at_UGST + oh_at_IGST;
                    lineData += "\"" + DmServer.vNull(GSTAmt, "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Roundoff"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00") + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();
            }
            //=========================================================================================

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                LinkedHashMap hm = new LinkedHashMap();

                if (DmShared.xNull(filter.get("reportType").getTag()).trim().equalsIgnoreCase("S")) {
                    jasperReport = DmServer.compileJasper("Pharmacy_WholeSaleBillReturnSummaryRegister");
                    jasperPrint = DmServer.fillJasper(this, "Whole Sale Bill Return Report (Summary) ", jasperReport, hm, filter, rsTemp);
                } else {
                    jasperReport = DmServer.compileJasper("Pharmacy_WholeSaleBillReturnDetailRegister");
                    jasperPrint = DmServer.fillJasper(this, "Whole Sale Bill Return Report (Detail) ", jasperReport, hm, filter, rsTemp);
                }
                keyField.setSharedKeyFieldName(DmServer.showJasper(this, jasperPrint, "", reportProperty));
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
            if (!DmShared.xNull(filter.get("saleType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("saleType").getTag()));
            }
            if (!DmShared.xNull(filter.get("returnNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(filter.get("returnNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("departmentName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(filter.get("departmentName").getTag()));
            }
//            if (!DmShared.xNull(filter.get("consultantName").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("consultantCode", DmShared.xNull(filter.get("consultantName").getTag()));
//            }
//            if (!DmShared.xNull(filter.get("phAccount").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("phAccount", DmShared.xNull(filter.get("phAccount").getTag()));
//            }
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
//            if (!DmShared.xNull(filter.get("organization").getTag()).trim().equalsIgnoreCase("")) {
//                psTemp.setStringList("organization", DmShared.xNull(filter.get("organization").getTag()));
//            }

            if (!DmShared.xNull(filter.get("paymentSource").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("paymentSource", DmShared.xNull(filter.get("paymentSource").getTag()));
            }
            if (!DmShared.xNull(filter.get("customerName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("customerName", DmShared.xNull(filter.get("customerName").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCompany").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCompany", DmShared.xNull(filter.get("medCompany").getTag()));
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

//    @Override
//    public DmResultPageing<List<KeyFieldExtended>> getTxtHConsultant(DmHelper helper, boolean blnToEndOfFile) {
//        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
//        List<KeyFieldExtended> lstTemp = new ArrayList<>();
//        KeyFieldExtended listNode;
//        Connection cnTemp = null;
//        ResultSet rsTemp = null;
//        DmPreparedStatement psTemp = null;
//        String sqlQuery;
//        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
//
//        try {
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            sqlQuery = " select	cm.subCode as code, sg.name  "
//                    + "		from consultantMast as cm  "
//                    + "		left join subGroupSiteList as sgsl on cm.subCode = sgsl.subCode "
//                    + "		left join subGroup as sg on cm.subCode = sg.subCode  "
//                    + "		left join subgroupNature as sgn on sg.nature = sgn.code  "
//                    + "		where sgn.manualCode = 'CN' and sg.isActive = 1 and sgsl.site_Code = :site_Code ";
//            sqlWithParam.setSQL(sqlQuery);
//            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                listNode = new KeyFieldExtended();
//                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
//                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
//                lstTemp.add(listNode);
//            }
//            rsTemp.close();
//            psTemp.close();
//            helper.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(helper.getPaging());
//
//            rstTemp.setStatus(true);
//            rstTemp.setData(lstTemp);
//            cnTemp.close();
//        } catch (Exception e) {
//            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return rstTemp;
//    }
    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReturnNo(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " WHERE vt.category IN ('" + pvc.WHOLE_SALE_RETURN_CREDIT + "') and sb.isCancelled=0");
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
                    + "  im.Code,im.name,im.manualCode  "
                    + " FROM itemMast im "
                    + " WHERE  (im.filterItemType = :filterItemType or im.isCommon=1)");
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
                    + "   WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1)");;
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHReturnType(DmHelper pageing, boolean blnToEndOfFile) {
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
                    + " WHERE vt.category IN ('" + pvc.WHOLE_SALE_RETURN_CREDIT + "') and isActive=1");
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

//    @Override
//    public DmResultPageing<List<TxtHLMCode_Mdl>> getTxtHLMCode(String type, DmHelper pageing, boolean blnToEndOfFile) {
//        DmResultPageing<List<TxtHLMCode_Mdl>> rstTemp = new DmResultPageing<>();
//        List<TxtHLMCode_Mdl> lstTemp = new ArrayList<>();
//        TxtHLMCode_Mdl listNode;
//        Connection cnTemp = null;
//        ResultSet rsTemp = null;
//        DmPreparedStatement psTemp = null;
//        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
//        String condition = "";
//        try {
//            rstTemp.setData(lstTemp);
//            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            if (!DmShared.xNull(type).trim().equalsIgnoreCase("")) {
//                if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.OPD)) {
//                    condition += " and  vt.category = ('" + pvc.OPD + "') and isNull(lm.regDocId,'')<>''";
//                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.IPD)) {
//                    condition += " and  vt.category = ('" + pvc.IPD + "') and isNull(lm.regDocId,'')<>''";
//                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.EMERGENCY)) {
//                    condition += " and  vt.category = ('" + pvc.EMERGENCY + "') and isNull(lm.regDocId,'')<>''";
//                } else if (DmShared.xNull(type).trim().equalsIgnoreCase(pvc.DAY_CARE)) {
//                    condition += " and  vt.category = ('" + pvc.DAY_CARE + "') and isNull(lm.regDocId,'')<>'' ";
//                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("L")) {
//                    condition += " and isNull(lm.subCode,'')<>''  ";
//                } else if (DmShared.xNull(type).trim().equalsIgnoreCase("W")) {
//                    condition += " and isNull(lm.subCode,'')= '' and isNull(lm.regDocId,'')=''  ";
//                }
//            }
//            sqlWithParam.setSQL("select lm.code,lm.name,lm.manualCode,c.cityName,pr.recid as regNo,sg.name as acName"
//                    + " from ledgerMast as lm"
//                    + " left join city as c on lm.cityCode=c.cityCode"
//                    + " left join patientRegistration as pr on lm.regDocId=pr.regDocId"
//                    + " left join voucher_Type as vt on vt.v_Type=pr.v_type"
//                    + " left join subGroup as sg on lm.subCode=sg.subCode"
//                    + " where 1=1 " + condition);
//            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
//            rsTemp = psTemp.executeQuery();
//            while (rsTemp.next()) {
//                listNode = new TxtHLMCode_Mdl();
//                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
//                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
//                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
//                listNode.setCity(DmShared.xNull(rsTemp.getString("cityName")));
//                listNode.setRegNo(DmShared.xNull(rsTemp.getString("regNo")));
//                listNode.setAcName(DmShared.xNull(rsTemp.getString("acName")));
//                lstTemp.add(listNode);
//            }
//            pageing.positioning(lstTemp.size(), blnToEndOfFile);
//            rstTemp.setPaging(pageing.getPaging());
//            rstTemp.setStatus(true);
//            rsTemp.close();
//            psTemp.close();
//            cnTemp.close();
//        } catch (Exception e) {
//            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
//        } finally {
//            try {
//                if (rsTemp != null) {
//                    rsTemp.close();
//                }
//
//                if (psTemp != null) {
//                    psTemp.close();
//                }
//
//                if (cnTemp != null && !cnTemp.isClosed()) {
//                    cnTemp.close();
//                }
//            } catch (Exception e) {
//            }
//        }
//        return rstTemp;
//    }
    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Debtor");
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

    //   @Override
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

    @Override
    public DmResultPageing<List<TxtHParty_Mdl>> getTxtHCustomer(DmHelper pageing, boolean blnToEndOfFile) {

        DmResultPageing<List<TxtHParty_Mdl>> dmrTemp = new DmResultPageing<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHParty_Mdl> lstTemp = new ArrayList<TxtHParty_Mdl>();
        TxtHParty_Mdl listNode;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        Connection cnTemp = null;
        dmrTemp.setData(lstTemp);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT sg.subCode AS code, sg.name as customerName, sg.manualCode, c.cityName \n"
                    + " FROM subGroup sg \n"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode \n"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode \n"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature \n"
                    + " WHERE sg.isActive = 1 \n"
                    + " AND sgsl.site_Code = :site_Code \n"
                    + " AND sgn.manualCode = 'CT' \n";

            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHParty_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("customerName")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(lstTemp);
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            dmrTemp.setPaging(pageing.getPaging());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            new DmException(0, ex.getMessage());
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
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

}
