package aarogya.pharmacy.reports.server;

import aarogya.pharmacy.reports.shared.BatchNo_Mdl;
import aarogya.pharmacy.reports.shared.RptProformaPurchaseReturnRegister_Srv;
import aarogya.pharmacy.reports.shared.RptProformaPurchaseReturnRegister_Vld;
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

public class RptProformaPurchaseReturnRegister_SrvImpl extends DmRSSPrivate implements RptProformaPurchaseReturnRegister_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        String sqlQuery = "", condition = "";
        KeyFieldExtended keyField = new KeyFieldExtended();
        FileWriter fileWriter = null;
        String fileName = widgetName.replaceAll(" ", "");
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";

        try {
            RptProformaPurchaseReturnRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(filter.get("fromDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND ppr.v_Date >= :fromDate  \n";
            }
            if (!DmShared.xNull(filter.get("toDate").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND ppr.v_Date <= :toDate \n ";
            }
            if (!DmShared.xNull(filter.get("entryType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND  vt.v_Type IN (" + DmServer.getParamList(":v_Type", filter.get("entryType").getTag()) + " ) \n ";
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND ppr.subCode IN (" + DmServer.getParamList(":subCode", filter.get("supplier").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("pBill").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pprd.pbDocId IN (" + DmServer.getParamList(":pbDocId", filter.get("pBill").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("proformaReturn").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND ppr.docId IN (" + DmServer.getParamList(":docid", filter.get("proformaReturn").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup IN (" + DmServer.getParamList(":medicineGroup ", filter.get("medicineGroup").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":medicineCatCode ", filter.get("medicineCat").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("medCom").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemMakeCode IN (" + DmServer.getParamList(":medCom ", filter.get("medCom").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pprd.itemCode IN (" + DmServer.getParamList(":medicineCode ", filter.get("medicine").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                condition += " and  pprd.ibcode in (" + DmServer.getParamList(":batchNo", filter.get("batchNo").getTag()) + " ) \n";
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND   ppr.site_Code IN (" + DmServer.getParamList(":site_Code ", filter.get("site").getTag()) + " ) \n";
            }
            if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("A")) {
                condition += " AND ISNULL(ppr.approvedBy, '') <> '' \n";
            }
            if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("P")) {
                condition += " AND ISNULL(ppr.approvedBy, '') = '' AND ppr.isCancelled = 0 \n";
            }
            if (DmShared.xNull(filter.get("status").getTag()).trim().equalsIgnoreCase("D")) {
                condition += " AND ppr.isCancelled = 1  \n";
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

            sqlQuery = "SELECT  vt.short_Name AS vTypeShortName,ppr.docId  , ppr.recId AS entryNo , ppr.v_Date AS entryDate \n"
                    + ", pb.recId AS billNo , pb.v_Date AS billDate , pb.partyBillNo AS refNo , pb.partyBillDate AS refDate , sg.name  as supplierName   \n"
                    + ", CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as supCity \n"
                    + ", im.name AS itemName  , icm.name AS colourName , ism.name AS sizeName , imm.companyName AS companyName  \n"
                    + ", im.itemMakeCode AS companyCode  , um.unit , ums.unit AS sku , pprd.itemCode , pprd.unitCode \n"
                    + ", pprd.quantity , pprd.freeQty , pprd.skuQty , pprd.skuQtyForRate , pprd.rate , pprd.mrp \n"
                    + ", pprd.batchNo, pprd.saleRate , pprd.manufacturingDate , pprd.expiryDate , pprd.oh_Amt_Gross \n"
                    + ", pprd.oh_Amt_Discount , pprd.oh_Amt_Taxable , pprd.oh_Amt_CGST , pprd.oh_Amt_SGST \n"
                    + ", pprd.oh_Amt_IGST , pprd.oh_Amt_UGST , pprd.oh_Amt_Net , pprd.itemGSTNature "
                    + ", pprd.remark,pbp.recId as returnNo"
                    + ",case when ppr.isCancelled =1 then  'Discard'\n"
                    + "when ISNULL(ppr.approvedBy, '') <> ''  then  'Approved'\n"
                    + "when ISNULL(ppr.approvedBy, '') = '' AND ppr.isCancelled = 0 then 'Pending' end as status \n"
                    + "FROM proformaPurchaseReturn ppr\n"
                    + "LEFT JOIN voucher_Type vt ON vt.v_Type = ppr.v_Type\n"
                    + "LEFT JOIN subgroup sg ON sg.subCode = ppr.subCode \n"
                    + " LEFT JOIN proformaPurchaseReturnDetail pprd ON pprd.docId = ppr.docId\n"
                    + " LEFT JOIN pBillDetail pbdp ON pbdp.pfprdocId = pprd.DocId AND pbdp.pfprv_SNo =  pprd.V_SNo\n"
                    + " LEFT JOIN pBill pbp ON pbp.docId = pbdp.docId  \n"
                    + " LEFT JOIN pBillDetail pbd ON pbd.docId = pprd.pbDocId AND pbd.v_SNo =  pprd.pbV_SNo\n"
                    + " LEFT JOIN pBill pb ON pb.docId = pbd.docId \n"
                    + "LEFT JOIN itemMast im ON im.code = pprd.itemCode\n"
                    + "LEFT JOIN itemmakemast imm ON imm.code = im.itemMakeCode \n"
                    + "LEFT JOIN itemColourMast icm ON icm.code = pprd.colourCode\n"
                    + "LEFT JOIN itemSizeMast ism ON ism.code = pprd.sizeCode \n"
                    + "LEFT JOIN unitMast um ON um.code = pprd.unitCode \n"
                    + "LEFT JOIN unitMast ums ON ums.code = im.sku \n"
                    + "LEFT JOIN itemBatch as ib on pprd.ibcode=ib.Code \n"
                    + "LEFT JOIN city as c on c.cityCode = sg.cityCode  "
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN + "','" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED + "')"
                    //                    + " AND ISNULL(ppr.approvedBy, '') <> ''"
                    + " AND ISNULL(pprd.isFooter, 0) = 0 " + condition
                    + " ORDER BY ppr.v_Date, vt.short_Name, ppr.v_No, ppr.recId, pprd.v_SNo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            getParameter(filter, psTemp);
            rsTemp = psTemp.executeQuery();

            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {

                String FILE_HEADER = "Entry No.,Entry Date,Type,Bill No.,Bill Date,Ref No.,Ref Date,Supplier Name,Medicine Name,Medicine Company"
                        + ",Batch No.,Expiry Date,MRP,Sale Rate,Quantity ,Quantity Sku,Free Qty,Rate,Amount,"
                        + "Discount,Taxable Value,Tax Amount,Total,Return No.,Status";

                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);

                while (rsTemp.next()) {

                    lineData = DmShared.xNull(rsTemp.getString("entryNo")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("entryDate")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("vTypeShortName")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("billNo")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("billDate")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("refNo")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("refDate")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("supplierName")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("itemName")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("companyName")) + COMMA_DELIMITER;

                    lineData += DmShared.xNull(rsTemp.getString("batchNo")) + COMMA_DELIMITER;
                    lineData += DmShared.dNull(rsTemp.getDate("expiryDate")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("mrp")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("saleRate")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("quantity")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("skuQty")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("freeQty")) + COMMA_DELIMITER;

                    lineData += DmServer.vNull(rsTemp.getString("rate")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Gross")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Discount")) + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Taxable")) + COMMA_DELIMITER;

                    double oh_at_CGST = DmShared.vNull(rsTemp.getString("oh_Amt_CGST")).doubleValue();
                    double oh_at_SGST = DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue();
                    double oh_at_UGST = DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue();
                    double oh_at_IGST = DmShared.vNull(rsTemp.getString("oh_Amt_IGST")).doubleValue();
                    double totalTaxAmount = oh_at_CGST + oh_at_SGST + oh_at_UGST + oh_at_IGST;

                    lineData += "\"" + DmServer.vNull(totalTaxAmount, "0.00") + "\"" + COMMA_DELIMITER;
                    lineData += DmServer.vNull(rsTemp.getString("oh_Amt_Net")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("returnNo")) + COMMA_DELIMITER;
                    lineData += DmShared.xNull(rsTemp.getString("status")) + COMMA_DELIMITER;

                    fileWriter.append(lineData);
                    fileWriter.append(NEW_LINE_SEPARATOR);

                }

                fileWriter.flush();
                fileWriter.close();
            }

            if (reportProperty.getExportType() == DmEnum.DmExportType.PDF) {
                jasperReport = DmServer.compileJasper("Pharmacy_ProformaPurchaseReturnRegister");
                jasperPrint = DmServer.fillJasper(this, "Proforma Purchase Return Register", jasperReport, null, filter, rsTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEntryType(DmHelper pageing, boolean blnToEndOfFile) {
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
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN + "','" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED + "')");
            //   + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')");
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
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN + "','" + pvc.PHARMACY_PROFORMA_PURCHASE_RETURN_UNREGISTERED
                    + "'))");

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
                    + " , um.unit,im.manualCode"
                    + " FROM itemMast im"
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedcineCat(DmHelper helper, boolean blnToEndOfFile) {
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
                    + "   ic.code"
                    + " , ic.name"
                    + " FROM itemCat ic"
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
            sqlWithParam.setSQL("SELECT"
                    + "   ig.code"
                    + " , ig.name"
                    + " FROM itemGroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType or ig.isCommon=1)");
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
                    + " WHERE vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "',"
                    + "'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHProformaReturn(DmHelper helper, boolean blnToEndOfFile) {
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
                    + " , pb.recId AS name, vt.short_Name , pb.v_Date, pb.v_No "
                    + " FROM pBill pb"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type"
                    + " WHERE vt.category IN('" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')"
                    + " AND ISNULL(pb.approvedBy, '') <> ''");
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
            if (!DmShared.xNull(filter.get("entryType").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("v_Type", DmShared.xNull(filter.get("entryType").getTag()));
            }
            if (!DmShared.xNull(filter.get("supplier").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("subCode", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("pBill").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("pbDocId", DmShared.xNull(filter.get("pBill").getTag()));
            }
            if (!DmShared.xNull(filter.get("proformaReturn").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("docId", DmShared.xNull(filter.get("proformaReturn").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCode", DmShared.xNull(filter.get("medicine").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineCatCode", DmShared.xNull(filter.get("medicineCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("medCom").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medCom", DmShared.xNull(filter.get("medCom").getTag()));
            }
            if (!DmShared.xNull(filter.get("medicineGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("medicineGroup", DmShared.xNull(filter.get("medicineGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("batchNo").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("batchNo", DmShared.xNull(filter.get("batchNo").getTag()));
            }
            if (!DmShared.xNull(filter.get("site").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("site_Code", DmShared.xNull(filter.get("site").getTag()));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHStatus(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Pending ");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("A");
            listNode.setSharedKeyFieldName("Approved");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("D");
            listNode.setSharedKeyFieldName("Discard");
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
