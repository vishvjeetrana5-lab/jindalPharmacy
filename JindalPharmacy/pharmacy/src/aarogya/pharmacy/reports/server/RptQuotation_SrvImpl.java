package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.RptQuotation_Srv;
import aarogya.pharmacy.reports.shared.RptQuotation_Vld;
import aarogya.pharmacy.reports.shared.TxtHMedicine_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
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

public class RptQuotation_SrvImpl extends DmRSSPrivate implements RptQuotation_Srv {

    private String vCat = "'" + new ProjectVoucherCategory().PHARMACY_QUOTATION + "'";
    private String vCatEnquiry = "'" + new ProjectVoucherCategory().PHARMACY_ENQUIRY + "'";
    private String vCatIndent = "'" + new ProjectVoucherCategory().PHARMACY_INDENT + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "";
        String condition = " ", joinIndent = "";
        KeyFieldExtended keyField = new KeyFieldExtended();
        FileWriter fileWriter = null;
        String fileName = widgetName.replaceAll(" ", "");
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            RptQuotation_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  q.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " and  q.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and q.subCode in (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("enquiryno").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and qd.enquiryDocId in (" + DmServer.getParamList(":enquiryDocId", filter.get("enquiryno").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("quotationNo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and q.docId in (" + DmServer.getParamList(":docId", filter.get("quotationNo").getTag()) + " )";
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
            if (!DmShared.xNull(filter.get("indent").getTag()).trim().equalsIgnoreCase("")) {
                joinIndent = "INNER JOIN ("
                        + "         SELECT eid.docId, eid.v_SNo "
                        + "         FROM quotationIndentDetail eid "
                        + "         WHERE eid.indentDocid in (" + DmServer.getParamList(":indent", filter.get("indent").getTag()) + " ) "
                        + "         GROUP BY eid.docId, eid.v_SNo "
                        + "         ) vEid ON vEid.docid = qd.docid AND vEid.v_SNo = qd.v_SNo ";

            }
            if (!DmShared.xNull(filter.get("quotationtype").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  q.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("quotationtype").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemGroup  in (" + DmServer.getParamList(":medGroup ", filter.get("medGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCategory").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and im.itemCategoryCode  in (" + DmServer.getParamList(":medCategoryCode ", filter.get("medCategory").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and qd.itemCode  in (" + DmServer.getParamList(":medicineCode ", filter.get("medicine").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND   q.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("companyName").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  im.itemMakeCode IN (" + DmServer.getParamList(":companyName ", filter.get("companyName").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  im.marginRateFix IN (" + DmServer.getParamList(":rateMarginFix ", filter.get("rateMarginFix").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("subCategory").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  im.subCategoryCode IN (" + DmServer.getParamList(":subCategory ", filter.get("subCategory").getTag()) + " )";
            }
            sqlQuery = " select	q.docId, qd.v_SNo, q.recId, vt.short_name AS qType, q.recId, q.v_Date, "
                    + " sgv.name  as supplier, "
                    + "         CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as supCity, "
                    + "		im.name as itemname, qd.deliveryDate, qd.oh_Amt_Gross as amount, qd.rate,qd.quantity AS reqqty,qd.oh_Amt_Discount, "
                    + "         qd.oh_Amt_Net AS netAmount, um.unit,isNull(qd.oh_Amt_Taxable,0) AS oh_Amt_Taxable,qd.remark,"
                    + "         enq.recId as enquiryNo,qd.mrp,im.marginRateFix AS MarginFixRateFix, imm.companyName as itemCompanyName, mscm.name AS subCategory ,"
                    + "         ISNULL(qd.oh_Amt_SGST,0) AS sgst ,ISNULL(qd.oh_Amt_CGST,0) AS cgst, ISNULL(qd.oh_Amt_IGST,0) AS igst,ISNULL(qd.oh_Amt_UGST,0) AS ugst,"
                    + "         ISNULL(qd.oh_at_CGST,0)+ISNULL(qd.oh_at_SGST,0) +\n"
                    + "         ISNULL(qd.oh_at_IGST,0)+ISNULL(qd.oh_at_UGST,0) as GSTper\n"
                    + "		from quotation as q "
                    + "		left join quotationdetail as qd on qd.docId = q.docId "
                    + "" + joinIndent
                    + "		left join enquiry as enq on enq.docId = qd.enquiryDocId "
                    + "		left join voucher_Type as vt on q.v_Type = vt.v_Type "
                    + "		left join subGroup as sgv on q.subCode = sgv.subCode "
                    + "		left join itemMast as im on im.code=qd.itemCode "
                    + "		left join itemGroup as ig on ig.code = im.itemGroup "
                    + "		left join itemCat as ic on ic.code = im.itemCategoryCode "
                    + "		left join unitMast as um on um.code = im.purchaseUnit "
                    + "         left join city as c on c.cityCode = sgv.cityCode "
                    + "         Left join itemMakeMast imm on imm.code =im.itemMakeCode"
                    + "         Left join medicineSubCategoryMast mscm on mscm.code =im.subCategoryCode"
                    + "		where vt.category = " + vCat + " " + condition
                    + "         ORDER BY q.v_Date, vt.short_Name, q.v_No, q.recId, qd.v_SNo";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "Bill No.,Bill Date,Supplier Name,Enquiry No.,Medicine Name,Delivery Dt.,Medicine Company,Sub Category,"
                        + "Quantity,Unit,Margin/Rate Fix,Taxable Value,Total Tax Amount,GST(%),MRP,Rate,Amount,Discount,Net Amt.,Remark";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {

                    lineData = DmShared.xNull(rsTemp.getString("recId")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("v_Date")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("supplier")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("enquiryNo")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("itemname")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("deliveryDate")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("itemCompanyName")) + COMMA_DELIMITER;

                    lineData += DmShared.xNull(rsTemp.getString("subCategory")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("reqqty")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("unit")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("MarginFixRateFix")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00") + COMMA_DELIMITER;

                    double oh_at_CGST = DmShared.vNull(rsTemp.getString("cgst")).doubleValue();
                    double oh_at_SGST = DmShared.vNull(rsTemp.getString("sgst")).doubleValue();
                    double oh_at_UGST = DmShared.vNull(rsTemp.getString("ugst")).doubleValue();
                    double oh_at_IGST = DmShared.vNull(rsTemp.getString("igst")).doubleValue();
                    double totalTaxAmount = oh_at_CGST + oh_at_SGST + oh_at_UGST + oh_at_IGST;

                    lineData += "\"" + DmServer.vNull(totalTaxAmount, "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("GSTper"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("mrp"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("rate"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("amount"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00") + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("netAmount"), "0.00") + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("remark")) + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();
            }
            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperReport = DmServer.compileJasper("Pharmacy_Quotation");
                jasperPrint = DmServer.fillJasper(this, "Quotation Register", jasperReport, null, filter, rsTemp);
                keyField.setSharedKeyFieldName(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            }

//            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
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
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("enquiryno").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("enquiryDocId", DmShared.xNull(filter.get("enquiryno").getTag()));
            }
            if (!DmShared.xNull(filter.get("indent").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("indent", DmShared.xNull(filter.get("indent").getTag()));
            }
            if (!DmShared.xNull(filter.get("quotationNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(filter.get("quotationNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("quotationtype").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmServer.vNull(filter.get("quotationtype").getTag(), "0"), true);
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCategoryCode", DmShared.xNull(filter.get("medCategory").getTag()));
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(filter.get("medGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("companyName").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("companyName", DmShared.xNull(filter.get("companyName").getTag()));
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("rateMarginFix", DmShared.xNull(filter.get("rateMarginFix").getTag()));
            }
            if (!DmShared.xNull(filter.get("subCategory").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCategory", DmShared.xNull(filter.get("subCategory").getTag()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSupplier(DmHelper helper, boolean blnToEndOfFile) {
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
                    + "   sg.subCode AS code"
                    + " , sg.name as name,sg.manualCode,c.cityName as city"
                    + " FROM subgroup sg"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " left join city as c on c.cityCode = sg.cityCode "
                    + " WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE vt.category IN (" + vCat + ")"
                    + " )");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("city")));
                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEnquiryNo(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT docid,recid FROM enquiry e "
                    + " LEFT JOIN voucher_Type vt ON e.v_Type=vt.v_Type "
                    + "	WHERE vt.category = " + vCatEnquiry
                    + " and  isNull(e.approvedBy,'') <> '' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHQuotationType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" select v_Type as code, description as name "
                    + " from voucher_Type where category = " + vCat + " and isActive = 1 ");
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
                    + " FROM itemgroup  where (filterItemType = :filterItemType or isCommon=1)");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code, name from itemcat  "
                    + " where (filterItemType = :filterItemType or isCommon=1)");
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
            sqlWithParam.setSQL(" select im.code,im.name,um.code as unitCode,um.unit,ig.name medGroup,ic.name medcategory,im.manualCode "
                    + " from itemMast as im "
                    + " left join unitmast as um on im.sku=um.code "
                    + " LEFT JOIN itemGroup ig ON ig.code = im.itemGroup "
                    + " LEFT JOIN itemCat ic ON ic.code = im.itemCategoryCode "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1) ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHQuotationNo(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT docid,recid FROM quotation q "
                    + " LEFT JOIN voucher_Type vt ON q.v_Type=vt.v_Type "
                    + "	WHERE vt.category = " + vCat);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIndent(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" Select docId,recId AS name from indent i "
                    + " LEFT JOIN voucher_Type vt ON i.v_Type=vt.v_Type "
                    + "	WHERE vt.category = " + vCatIndent
                    + " and  isNull(i.approvedBy,'') <> '' ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCompany(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code, companyName as name, shortName from itemMakeMast where filterItemType =:filterItemType");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("select code ,name from medicineSubCategoryMast ");
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
}
