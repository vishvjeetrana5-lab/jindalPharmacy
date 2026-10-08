package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.BatchNo_Mdl;
import aarogya.pharmacy.reports.shared.RptPurchaseRegister_Srv;
import aarogya.pharmacy.reports.shared.RptPurchaseRegister_Vld;
import aarogya.pharmacy.reports.shared.TxtHCompany_Mdl;
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

public class RptPurchaseRegister_SrvImpl extends DmRSSPrivate implements RptPurchaseRegister_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "";
        FileWriter fileWriter = null;
        String fileName = widgetName.replaceAll(" ", "");
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            RptPurchaseRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.v_Date >= :fromDate ";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vt.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("billType").getTag()) + " ) ";
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.subCode IN (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("pOrder").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pbd.poDocId IN (" + DmServer.getParamList(":poDocId", filter.get("pOrder").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("pBill").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.docId IN (" + DmServer.getParamList(":pbDocId", filter.get("pBill").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup IN (" + DmServer.getParamList(":medGroup ", filter.get("medGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":medCatCode ", filter.get("medCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("subCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.subCategoryCode IN (" + DmServer.getParamList(":subCatCode ", filter.get("subCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medCom").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemMakeCode IN (" + DmServer.getParamList(":medCom ", filter.get("medCom").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pbd.itemCode IN (" + DmServer.getParamList(":medicineCode ", filter.get("medicine").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and pbd.ibcode in (" + DmServer.getParamList(":batchNo", filter.get("batchNo").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  pb.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("isApproved").getTag()).trim().equalsIgnoreCase("A")) {
                condition += " AND ISNULL(pb.approvedBy, '') <> ''";
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  im.marginRateFix IN (" + DmServer.getParamList(":rateMarginFix ", filter.get("rateMarginFix").getTag()) + " )";
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

            sqlQuery = "SELECT"
                    + "   pb.approvedBy, vt.short_Name AS vTypeShortName "
                    + " , pb.recId AS billNo,pb.docId "
                    + " , pb.v_Date AS billDate"
                    + " , pb.partyBillNo"
                    + " , pb.partyBillDate"
                    + " , pc.recId AS grnNo"
                    + " , pc.v_Date AS grnDate"
                    + " , pc.pChallanNo"
                    + " , pc.pChallanDate"
                    + " ,  sg.name as supplierName,sg.gsTin "
                    + "  ,CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as supCity "
                    + " , im.name AS itemName"
                    + " , msc.name AS subCategory"
                    + " , imm.companyName AS companyName "
                    + " , im.itemMakeCode AS companyCode "
                    + " , icm.name AS colourName"
                    + " , ism.name AS sizeName"
                    + " , um.unit"
                    + " , ums.unit AS sku"
                    + " , pbd.itemCode"
                    + " , pbd.unitCode"
                    + " , cast( pbd.docQty as numeric(18,2)) as docQty "
                    + " , pbd.recQty"
                    + " , pbd.rejQty"
                    + " , pbd.returnedQty"
                    + " , cast( pbd.acceptQty  as numeric(18,2)) as acceptQty "
                    + " , pbd.freeQty"
                    + " , umpu.unit as freeQtyUnit"
                    + " , pbd.skuDifference"
                    + " , pbd.skuQty"
                    + " , pbd.skuQtyForRate"
                    + " , pbd.rate"
                    + " , pod.mrp AS poMRP"
                    + " , pbd.mrp \n"
                    + " , ISNULL (pbd.mrp,0) - ISNULL(pod.mrp,0) as MRPDiff \n"
                    + " , pbd.saleRate"
                    + " , pbd.batchNo"
                    + " , pbd.manufacturingDate"
                    + " , pbd.expiryDate"
                    + " , pbd.oh_Amt_Gross"
                    + " , pbd.oh_at_Discount"
                    + " , pbd.oh_Amt_Discount"
                    + " , pbd.oh_Amt_Taxable"
                    + " , pbd.oh_Amt_CGST"
                    + " , pbd.oh_Amt_SGST"
                    + " , pbd.oh_Amt_IGST"
                    + " , pbd.oh_Amt_UGST"
                    + " , pbd.oh_Amt_Net"
                    + " , pbd.oh_at_CGST"
                    + " , pbd.oh_at_SGST"
                    + " , pbd.oh_at_IGST"
                    + " , pbd.oh_at_UGST"
                    + " , isNull(pbd.oh_at_CGST,0)+isNull(pbd.oh_at_SGST,0)+isNull(pbd.oh_at_IGST,0)+isNull(pbd.oh_at_UGST,0) as taxRate"
                    + " , pbd.skuRate"
                    + " , pbd.landedSkuValue"
                    + " , pbd.landedSkuRate"
                    + " , pbd.itemGSTNature"
                    + " , pbd.remark"
                    + " , po.recId As purchaseRecId"
                    + " , po.v_Date As purchaseDate"
                    + " , im.marginRateFix As MarginRateFix"
                    + " , CASE WHEN pbd.isStatus = 1 THEN 'Cancel' WHEN pbd.isStatus = 2 THEN 'Completed' ELSE 'Open' END AS status"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type"
                    + " LEFT JOIN subgroup sg ON sg.subCode = pb.subCode"
                    + " LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId"
                    + " LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId"
                    + " LEFT JOIN itemMast im ON im.code = pbd.itemCode"
                    + " LEFT JOIN medicineSubCategoryMast msc ON im.subCategoryCode = msc.code"
                    + " LEFT JOIN itemmakemast imm ON imm.code = im.itemMakeCode"
                    + " LEFT JOIN itemColourMast icm ON icm.code = pbd.colourCode"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = pbd.sizeCode"
                    + " LEFT JOIN unitMast um ON um.code = pbd.unitCode"
                    + " LEFT JOIN unitMast ums ON ums.code = im.sku "
                    + " LEFT JOIN unitMast umpu ON umpu.code = im.purchaseUnit "
                    + "  left join city as c on c.cityCode = sg.cityCode "
                    + " left join itemBatch as ib on pbd.ibcode=ib.Code "
                    + " LEFT JOIN pOrderDetail pod ON pod.docId = pbd.poDocId AND pod.v_SNo = pbd.poV_SNo "
                    + " LEFT JOIN pOrder po ON po.docId = pod.docId "
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "'"
                    + " ,'" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    //                    + " AND ISNULL(pb.approvedBy, '') <> ''"
                    + " AND ISNULL(pbd.isFooter, 0) = 0 " + condition
                    + " ORDER BY pb.v_Date, vt.short_Name, pb.v_No, pb.recId, pbd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "Bill No.,Bill Date,Type,Sup. Bill No.,Sup. Bill Dt.,PO No.,PO Date,GRN No.,GRN Date,Supplier Name,"
                        + "Medicine,SubCategory,Batch No.,Expiry Dt.,Company Name,poMRP,MRP,MRP Diff ,Sale Rate,Doc. Qty.,Rec. Qty.,Rej. Qty.,Free Qty.,Acc. Qty.,Rate,Amount,"
                        + "Discount %,Disc. Amt.,Taxable Val.,Tax %,Tax Amount,Total,Rate/margin Fix";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                double netGrossAmount = 0, netAccQty = 0, netTaxableValue = 0, netTotalValue = 0;
                while (rsTemp.next()) {
                    lineData = "\"" + DmShared.xNull(rsTemp.getString("billNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("billDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("vTypeShortName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("partyBillNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("partyBillDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("purchaseRecId")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("purchaseDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("grnNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("grnDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("supplierName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("itemName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("subCategory")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("batchNo")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.dNull(rsTemp.getDate("expiryDate")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("companyName")) + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("poMRP"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("mrp"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("MRPDiff"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("saleRate"), "0.00") + "\"" + COMMA_DELIMITER;

                    String unitNameDoc = "", unitNameRec = "";
                    if (DmShared.iNull(rsTemp.getString("docQty")).doubleValue() != 0) {
                        unitNameDoc = " " + DmShared.xNull(rsTemp.getString("unit"));
                    }
                    if (DmShared.iNull(rsTemp.getString("recQty")).doubleValue() != 0) {
                        unitNameRec = " " + DmShared.xNull(rsTemp.getString("unit"));
                    }

                    lineData += "\"" + DmServer.vNull(rsTemp.getString("docQty"), "0.00") + unitNameDoc + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("recQty"), "0.00") + unitNameRec + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmServer.vNull(rsTemp.getString("rejQty"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("freeQty"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("acceptQty"), "0.00") + "\"" + COMMA_DELIMITER;
                    netAccQty += DmShared.iNull(rsTemp.getString("acceptQty")).doubleValue();

                    lineData += "\"" + DmServer.vNull(rsTemp.getString("rate"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00") + "\"" + COMMA_DELIMITER;
                    netGrossAmount += DmShared.iNull(rsTemp.getString("oh_Amt_Gross")).doubleValue();

                    lineData += "\"" + DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00") + "\"" + COMMA_DELIMITER;

                    double oh_at_CGST = DmShared.vNull(rsTemp.getString("oh_at_CGST")).doubleValue();
                    double oh_at_SGST = DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue();
                    double oh_at_UGST = DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue();
                    double oh_at_IGST = DmShared.vNull(rsTemp.getString("oh_at_IGST")).doubleValue();
                    double taxable_percent = oh_at_CGST + oh_at_SGST + oh_at_UGST + oh_at_IGST;

                    double taxable_amount = DmShared.vNull(rsTemp.getString("oh_Amt_Taxable")).doubleValue() / 100;

                    lineData += "\"" + DmServer.vNull(taxable_percent, "0.00") + "\"" + COMMA_DELIMITER;

                    lineData += "\"" + DmServer.vNull(taxable_amount * taxable_percent, "0.00") + "\"" + COMMA_DELIMITER;
                    netTaxableValue += (taxable_amount * taxable_percent);

                    lineData += "\"" + DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00") + "\"" + COMMA_DELIMITER;
                    netTotalValue += DmShared.iNull(rsTemp.getString("oh_Amt_Net")).doubleValue();
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("MarginRateFix")) + "\"" + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

//                lineData = "\"" + "" + "\"" + COMMA_DELIMITER;
//                for (int i = 0; i < 17; i++) {
//                    lineData += "\"" + "" + "\"" + COMMA_DELIMITER;
//                }
//                lineData += "\"" + "Total :-" + "\"" + COMMA_DELIMITER;
//
//                lineData += "\" " + netAccQty + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + netGrossAmount + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + "" + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + netTaxableValue + "\"" + COMMA_DELIMITER;
//                lineData += "\"" + netTotalValue + "\"" + COMMA_DELIMITER;
//               
//                fileWriter.append(lineData);
//                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.flush();
                fileWriter.close();

            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperReport = DmServer.compileJasper("Pharmacy_PurchaseRegister");
                jasperPrint = DmServer.fillJasper(this, "Purchase Register", jasperReport, null, filter, rsTemp);
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
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "',"
                    + "'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVendor(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " , sg.name as name,sg.manualcode,c.cityName as city"
                    + " FROM subgroup sg"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " left join city as c on c.cityCode = sg.cityCode "
                    + " WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " )");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualcode")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("city")));
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
            sqlWithParam.setSQL("SELECT "
                    + "   im.code"
                    + " , im.name"
                    + " , um.code AS unitCode"
                    + " , um.unit,im.manualCode "
                    + " FROM itemMast AS im "
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPorder(DmHelper helper, boolean blnToEndOfFile) {
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
                    + "   po.docId AS code"
                    + " , po.recId AS name"
                    + " FROM pOrder po"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = po.v_Type"
                    + " WHERE vt.category = '" + pvc.PHARMACY_PURCHASE_ORDER + "'"
                    + " AND ISNULL(po.approvedBy, '') <> ''");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPbill(DmHelper helper, boolean blnToEndOfFile) {
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
                    + "   pb.docId AS code"
                    + " , pb.recId AS name, vt.short_Name , pb.v_Date, pb.v_No, pb.partyBillNo"
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "',"
                    + "'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " AND ISNULL(pb.approvedBy, '') <> ''");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("partyBillNo")));
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
                    + " , um.unit AS skuName,im.name as medicineName"
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
                listNode.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setMedicineName(DmShared.xNull(rsTemp.getString("medicineName")));

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

    private void getParameter(LinkedHashMap<String, FieldUDT_Mdl> filter, DmPreparedStatement psTemp) {
        try {
            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);
            }
            if (!DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("billType").getTag()));
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("pOrder").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("poDocId", DmShared.xNull(filter.get("pOrder").getTag()));
            }
            if (!DmShared.xNull(filter.get("pBill").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("pbDocId", DmShared.xNull(filter.get("pBill").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCatCode", DmShared.xNull(filter.get("medCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("subCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCatCode", DmShared.xNull(filter.get("subCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCom").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCom", DmShared.xNull(filter.get("medCom").getTag()));
            }
            if (!DmShared.xNull(filter.get("medGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medGroup", DmShared.xNull(filter.get("medGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("batchNo", DmShared.xNull(filter.get("batchNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
            }
            if (!DmShared.xNull(filter.get("rateMarginFix").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("rateMarginFix", DmShared.xNull(filter.get("rateMarginFix").getTag()));
            }
        } catch (Exception e) {
        }
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
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT CODE, companyName, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "   WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPurchaseDisPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer"), "0.00"));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHIsActive(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("U");
            listNode.setSharedKeyFieldName("Un-Approved");
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

}
