package dataman.accounts.reports.server;

import dataman.accounts.reports.shared.RptPurchaseRegister_Srv;
import dataman.accounts.reports.shared.RptPurchaseRegister_Vld;
import dataman.accounts.reports.shared.TxtHItemMedCompany_Mdl;
import dataman.accounts.reports.shared.TxtHItemMedicine_Mdl;
import dataman.accounts.self.shared.AccountsShared;
import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dataman.dmBase.self.xls.server.DmXlsWriter;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import jxl.CellReferenceHelper;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Orientation;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;

public class RptPurchaseRegister_SrvImpl extends DmRSSPrivate implements RptPurchaseRegister_Srv {

    private final LibraryVoucherCategory pvc = new LibraryVoucherCategory();
    private final String V_CATEGORY = "'" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "','" + pvc.PURCHASE_BILL_DIRECT + "' "
            + ",'" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "'"
            + ",'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_RETURN + "'"
            + ",'" + pvc.PURCHASE_RETURN + "','" + pvc.PURCHASE_RETURN_UNREGISTERED + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "','" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "'";

    @Override
    public DmResult<KeyFieldExtended> execute(String sessionId, LinkedHashMap<String, FieldUDT_Mdl> filter, DmReportProperty_Mdl reportProperty, String widgetName) {
        DmResult<KeyFieldExtended> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        WritableCellFormat cellFormat = null;
        String sqlQuery = "", condition = "", filePath = "";
        String fileName = widgetName.replaceAll(" ", "");
        int rowIndex = 0;
        FileWriter fileWriter = null;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "";
        try {

            RptPurchaseRegister_Vld.validateInput(filter);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmSession.getSessions().containsKey(sessionId)) {
                throw new DmException(0, "Invalid request.");
            }

            if (DmShared.xNull(widgetName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }

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

            condition += "where pb.v_Date between :fromDate and :toDate ";

            // ===================================<Bill Status>============================================
            if (DmShared.xNull(filter.get("isApproved").getTag()).trim().equalsIgnoreCase("A")) {
                condition += "  and isNull(pb.approvedBy,'')<>'' ";
            }
            if (DmShared.xNull(filter.get("isApproved").getTag()).trim().equalsIgnoreCase("U")) {
                condition += "  and isNull(pb.approvedBy,'')='' ";
            }

            // ===================================<Bill Module>============================================
            if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase("PU")) {
                condition += " AND vt.category IN ('" + pvc.PURCHASE_BILL + "','" + pvc.PURCHASE_BILL_UNREGISTERED + "', "
                        + "'" + pvc.PURCHASE_BILL_DIRECT + "','" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', "
                        + "'" + pvc.SERVICE_BILL + "', '" + pvc.SERVICE_BILL_UNREGISTERED + "', '" + pvc.PURCHASE_RETURN + "', '" + pvc.PURCHASE_RETURN_UNREGISTERED + "')";
            } else if (DmShared.xNull(filter.get("module").getTag()).trim().equalsIgnoreCase("PH")) {
                condition += " AND vt.category IN ('" + pvc.PHARMACY_PURCHASE_BILL + "','" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', "
                        + "   '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "',"
                        + "   '" + pvc.PHARMACY_PURCHASE_RETURN + "','" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')";
            }

            if (!DmShared.xNull(filter.get("pOrder").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pbd.poDocId IN (" + DmServer.getParamList(":poDocId", filter.get("pOrder").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("pBill").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.docId IN (" + DmServer.getParamList(":pbDocId", filter.get("pBill").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemMedGroup").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemGroup IN (" + DmServer.getParamList(":itemMedGroup ", filter.get("itemMedGroup").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemMedCat").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":itemMedCat", filter.get("itemMedCat").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemMedCom").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND im.itemMakeCode IN (" + DmServer.getParamList(":itemMedCom", filter.get("itemMedCom").getTag()) + " )";
            }
            if (!DmShared.xNull(filter.get("itemMedicine").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pbd.itemCode IN (" + DmServer.getParamList(":itemMedicineCode", filter.get("itemMedicine").getTag()) + " )";
            }
            if (DmShared.xNull(filter.get("isApproved").getTag()).trim().equalsIgnoreCase("A")) {
                condition += " AND ISNULL(pb.approvedBy, '') <> ''";
            }
            if (DmShared.xNull(filter.get("isApproved").getTag()).trim().equalsIgnoreCase("U")) {
                condition += " AND ISNULL(pb.approvedBy, '') = ''";
            }
            if (!DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.v_Type IN (" + DmServer.getParamList(":billType", filter.get("billType").getTag()) + " )";
            }
            // ===================================<Bill Supplier>============================================
            if (!DmShared.xNull(filter.get("supplier").getText()).trim().equalsIgnoreCase("")) {
                condition += " AND pb.subCode in (" + DmServer.getParamList(":supplier", filter.get("supplier").getText()) + " ) ";
            }
            // ===================================<Bill Type>============================================

            sqlQuery = "SELECT  vt.short_Name AS VType , pb.recId AS VoucherNo , format( pb.v_Date, 'dd-MMM-yyyy')  VoucherDate \n "
                    + " , pb.partyBillNo   , format(pb.partyBillDate, 'dd-MMM-yyyy') as  PBillDt \n"
                    + " , pc.recId AS GRNNo , format( pc.v_Date,'dd-MMM-yyyy') AS GRNDate , pc.pChallanNo as PChallanNo \n "
                    + "	, format( pc.pChallanDate,'dd-MMM-yyyy') as PChallanDt,  sg.name as  SupplierName,sg.gsTin  as GSTIN\n"
                    + "	,CASE WHEN isNull(c.cityName,'') <>''  THEN ', '+c.cityName ELSE '' END  as City, im.name AS ItemName ,hs.name as HSN\n"
                    + " , imm.companyName AS CompanyName,ig.name as ItemGroup,ic.name as ItemCategory, um.unit as Unit, ums.unit AS SKU\n"
                    + "	, cast(round(pbd.docQty,2) as numeric(18,2)) as DocQty, cast(round(pbd.recQty,2) as numeric(18,2)) as RecQty\n"
                    + " , cast(round(pbd.acceptQty,2) as numeric(18,2)) as AccQty, cast(round(pbd.skuQty,2) as numeric(18,2)) as SKUQty\n"
                    + "	, cast(round(pbd.rate,2) as numeric(18,2)) as Rate, cast(round(pbd.mrp,2) as numeric(18,2)) as MRP\n"
                    + "	, cast(round(pbd.saleRate,2) as numeric(18,2)) as SaleRate , pbd.batchNo as BatchNo\n"
                    + "	, format(pbd.manufacturingDate ,'dd-MMM-yyyy') as ManufacturingDt\n"
                    + " , format( pbd.expiryDate,'dd-MMM-yyyy') as ExpiryDate, pbd.oh_Amt_Gross as GoodsValue, pbd.oh_Amt_Discount  as Discount\n"
                    + " , pbd.oh_Amt_Taxable as TaxableAmt\n"
                    + " , isNull(pbd.oh_at_CGST,0)+isNull(pbd.oh_at_SGST,0)+isNull(pbd.oh_at_IGST,0)+isNull(pbd.oh_at_UGST,0) as TaxRate \n"
                    + "	, pbd.oh_Amt_CGST as CGSTAmt, pbd.oh_Amt_SGST as SGSTAmt, pbd.oh_Amt_IGST as IGSTAmt\n"
                    + "	, pbd.oh_Amt_UGST as UGSTAmt, pbd.oh_Amt_Net as NetAmt, pbd.remark as Remark\n"
                    + "	, CASE WHEN pbd.isStatus = 1 THEN 'Cancel' WHEN pbd.isStatus = 2 THEN 'Completed' ELSE 'Open' END AS status ,pb.approvedBy as ApprovedBy\n"
                    + "	, cast(round(pbd.freeQty,2) as numeric(18,2)) as freeQty, umpu.unit as FreeQtyUnit\n"
                    + "	, cast(round(pbd.skuDifference,2) as numeric(18,2)) as SkuDifference \n"
                    + "	, cast(round(pbd.skuRate,2) as numeric(18,2)) as skuRate,  pbd.landedSkuRate , pbd.itemGSTNature  \n"
                    + "	, icm.name AS ColourName , ism.name AS SizeName , pbd.rejQty as RejQty , pbd.returnedQty as RetQty \n"
                    + "	, pbd.skuQtyForRate as SKUQtyForRate \n"
                    + "	, pbd.oh_at_CGST , pbd.oh_at_SGST , pbd.oh_at_IGST , pbd.oh_at_UGST \n"
                    + "		FROM pBill pb \n"
                    + "		LEFT JOIN voucher_Type vt ON vt.v_Type = pb.v_Type \n"
                    + "         LEFT JOIN subgroup sg ON sg.subCode = pb.subCode \n"
                    + "         LEFT JOIN pBillDetail pbd ON pbd.docId = pb.docId \n"
                    + "         LEFT JOIN pChallan pc ON pc.docId = pbd.pcDocId \n"
                    + "		LEFT JOIN itemMast im ON im.code = pbd.itemCode \n"
                    + "         LEFT JOIN hsnMast AS hs ON im.hsnCode = hs.code \n "
                    + "         LEFT JOIN itemmakemast imm ON imm.code = im.itemMakeCode \n"
                    + "         LEFT JOIN itemGroup ig ON im.itemGroup = ig.Code \n"
                    + "         LEFT JOIN itemCat ic ON im.itemCategoryCode = ic.Code \n"
                    + "         LEFT JOIN itemColourMast icm ON icm.code = pbd.colourCode\n"
                    + "         LEFT JOIN itemSizeMast ism ON ism.code = pbd.sizeCode \n"
                    + "		LEFT JOIN unitMast um ON um.code = pbd.unitCode \n"
                    + "		LEFT JOIN unitMast ums ON ums.code = im.sku  \n"
                    + "		LEFT JOIN unitMast umpu ON umpu.code = im.purchaseUnit   \n"
                    + "		left join city as c on c.cityCode = sg.cityCode  \n"
                    + "		left join itemBatch as ib on pbd.ibcode=ib.Code  \n"
                    + "		" + condition
                    + "		and pb.site_Code=:site_Code  AND ISNULL(pbd.isFooter, 0) = 0 \n"
                    + "		 		order by SupplierName, partyBillNo , PBillDt  ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.get("fromDate").getText()), true);
            psTemp.setDate("toDate", DmServer.convertDate(filter.get("toDate").getText()), true);

            if (!DmShared.xNull(filter.get("supplier").getText()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("supplier", DmShared.xNull(filter.get("supplier").getTag()));
            }
            if (!DmShared.xNull(filter.get("billType").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("billType", DmShared.xNull(filter.get("billType").getTag()));
            }
            if (!DmShared.xNull(filter.get("pOrder").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("pOrder", DmShared.xNull(filter.get("pOrder").getTag()));
            }
            if (!DmShared.xNull(filter.get("pBill").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("pBill", DmShared.xNull(filter.get("pBill").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemMedGroup").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemMedGroup", DmShared.xNull(filter.get("itemMedGroup").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemMedCat").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemMedCat", DmShared.xNull(filter.get("itemMedCat").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemMedCom").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemMedCom", DmShared.xNull(filter.get("itemMedCom").getTag()));
            }
            if (!DmShared.xNull(filter.get("itemMedicine").getTag()).trim().equalsIgnoreCase("")) {
                psTemp.setStringList("itemMedicineCode", DmShared.xNull(filter.get("itemMedicine").getTag()));
            }

            rsTemp = psTemp.executeQuery();

            String FILE_HEADER = "V Type,VoucherNo.,VoucherDate"
                    + ",PartyBillNo.,PartyBillDt."
                    + ",GRNNo.,GRNDate,P.ChallanNo."
                    //
                    + ",P.ChallanDt.,SupplierName,GSTIN"
                    + ",City,ItemName,HSN"
                    ///
                    + ",CompanyName,ItemGroup,ItemCategory,Unit,SKU,DocQty"
                    + ",Rec.Qty."
                    + ",Acc.Qty.,SKUQty"
                    //
                    + ",Rate,MRP"
                    + ",SaleRate,BatchNo."
                    + ",ManufacturingDt."
                    //
                    + ",ExpiryDate,GoodsValue,Discount"
                    + ",TaxableAmt."
                    + ",TaxRate"
                    + ",CGSTAmt.,SGSTAmt.,IGSTAmt."
                    + ",UGSTAmt.,NetAmt.,Remark"
                    //
                    + ",Status,ApprovedBy,freeQty"
                    + ",FreeQtyUnit"
                    + ",SkuDifference"
                    + ",skuRate,LandedSkuRate,ItemGSTNature"
                    //
                    + ",ColourName,SizeName,Rej.Qty.,Ret.Qty."
                    + ",SKUQtyForRate"
                    + ",CGST%,SGST%,IGST%,UGST%";

            fileWriter.append(FILE_HEADER.toString());
            fileWriter.append(NEW_LINE_SEPARATOR);

            while (rsTemp.next()) {

                String medName = rsTemp.getString("ItemName");
                String backSlash = "";
                if (medName.contains("\"")) {
                    backSlash = "\"";
                }

                lineData = "\"" + DmShared.xNull(rsTemp.getString("VType")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("VoucherNo")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("VoucherDate")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("partyBillNo")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("PBillDt")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("GRNNo")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("GRNDate")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("PChallanNo")) + "\"" + COMMA_DELIMITER;
                ///
                lineData += "\"" + DmShared.xNull(rsTemp.getString("PChallanDt")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("SupplierName")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("GSTIN")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("City")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ItemName")) + backSlash + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("HSN")) + "\"" + COMMA_DELIMITER;
                ////
                lineData += "\"" + DmShared.xNull(rsTemp.getString("CompanyName")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ItemGroup")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ItemCategory")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("Unit")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("SKU")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("DocQty")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("RecQty")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("AccQty")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("SKUQty")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                //
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("Rate")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("MRP")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("SaleRate")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("BatchNo")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ManufacturingDt")) + "\"" + COMMA_DELIMITER;
                //
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ExpiryDate")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("GoodsValue")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("Discount")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("TaxableAmt")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("TaxRate")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("CGSTAmt")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("SGSTAmt")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("IGSTAmt")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("UGSTAmt")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("NetAmt")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("Remark")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("status")) + "\"" + COMMA_DELIMITER;
                //
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ApprovedBy")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("freeQty")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("FreeQtyUnit")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("SkuDifference")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("skuRate")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("landedSkuRate")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemGSTNature")) + "\"" + COMMA_DELIMITER;
                //
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ColourName")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("SizeName")) + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("RejQty")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("RetQty")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("SKUQtyForRate")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_CGST")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_SGST")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_IGST")).toString(), "0.00") + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmServer.vNull(DmShared.vNull(rsTemp.getString("oh_at_UGST")).toString(), "0.00") + "\"" + COMMA_DELIMITER;

                fileWriter.append(lineData);
                fileWriter.append(NEW_LINE_SEPARATOR);

            }

            fileWriter.flush();
            fileWriter.close();

            psTemp.close();
            rsTemp.close();
            cnTemp.close();

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

    private void setBorder(WritableCellFormat cellFormat, boolean isTop, boolean isRight, boolean isBottom, boolean isLeft) {
        try {
            if (isBottom) {
                cellFormat.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            }
            if (isTop) {
                cellFormat.setBorder(Border.TOP, BorderLineStyle.THIN);
            }
            if (isLeft) {
                cellFormat.setBorder(Border.LEFT, BorderLineStyle.THIN);
            }
            if (isRight) {
                cellFormat.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

    }

    private int createXLHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp, LinkedHashMap<String, FieldUDT_Mdl> filter) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        String filterString = "";
        int colCount = 0;
        try {
            cellFormat = setFontGeneral(13, true, Alignment.CENTRE, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            colCount = rsTemp.getMetaData().getColumnCount() + 1;
            //===========================================<HOSPITAL SITE  Name> =========================================================
//            rowIndex = 0;
            workSheet.mergeCells(0, rowIndex, colCount, rowIndex);
            workSheet.getWorkSheet().setRowView(rowIndex, 300);

            //============================================< set column width>================================================================
            workSheet.getWorkSheet().setColumnView(0, 3);
            workSheet.getWorkSheet().setColumnView(1, 40);
//            workSheet.getWorkSheet().setColumnView(2, 11);
//            workSheet.getWorkSheet().setColumnView(3, 11);
//            workSheet.getWorkSheet().setColumnView(4, 11);

            lblHeader = new Label(0, rowIndex, DmServer.getSsnSiteDisplayName(this), cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            //===========================================<REPORT NAME>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex);

//            cellFormat = setFontGeneral(11, true, Alignment.LEFT, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            lblHeader = new Label(1, rowIndex, new lmAccounts().BILL_PAYMENT_MASTER.caption, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);
            //===========================================<Filters>=========================================================
            rowIndex++;
            workSheet.mergeCells(1, rowIndex, colCount, rowIndex);

            if (filter != null) {
                Set<String> keySet = filter.keySet();
                if (keySet != null) {
                    for (String key : keySet) {
                        FieldUDT_Mdl filter_Mdl = filter.get(key);
                        if (filter_Mdl != null) {
                            if (!DmShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
                                filterString += DmShared.xNull(filter_Mdl.getFieldCaption()) + ":" + DmShared.xNull(filter_Mdl.getText()) + "; ";
                            }
                        }
                    }
                }
            }

//            cellFormat = setFontGeneral(11, true, Alignment.LEFT, null, false, false, false, UnderlineStyle.NO_UNDERLINE);
            lblHeader = new Label(1, rowIndex, filterString, cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private int createColumnHeader(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        try {

            rowIndex++;
            for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                String colName = rsTemp.getMetaData().getColumnName(colIndex);
                int collen = colName.length();
                {
                    workSheet.getWorkSheet().setColumnView(colIndex, 6 + collen);
                    cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                }

                setBorder(cellFormat, false, false, true, false);

                lblHeader = new Label(colIndex++, rowIndex, DmShared.xNull(colName), cellFormat);
                workSheet.getWorkSheet().addCell(lblHeader);

            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private int createTotalRow(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Formula formula;
        String columnName = "", sumValue = "";
        Label lblHeader;
        int lastRowIndex = workSheet.getWorkSheet().getRows();
        try {

            rowIndex++;
            cellFormat = setFontGeneral(true, Alignment.RIGHT, null, false, true, false, UnderlineStyle.NO_UNDERLINE);

            setBorder(cellFormat, true, true, true, true);

            lblHeader = new Label(1, rowIndex, DmShared.xNull("Total"), cellFormat);
            workSheet.getWorkSheet().addCell(lblHeader);

            for (int colIndex = 2; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                cellFormat = setFontGeneral(true, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                cellFormat.setAlignment(Alignment.CENTRE);
                setBorder(cellFormat, true, true, true, true);
                columnName = CellReferenceHelper.getColumnReference(colIndex);
                sumValue = columnName + "7:" + columnName + DmShared.iNull(lastRowIndex).toString();
                formula = new Formula(colIndex, lastRowIndex, "SUM(" + sumValue + ")", cellFormat);
                workSheet.getWorkSheet().addCell(formula);
                colIndex++;
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    private int createDataRow(DmXlsSheet workSheet, int rowIndex, ResultSet rsTemp) {
        WritableCellFormat cellFormat = null;
        Label lblHeader = null;
        jxl.write.Number number;
        jxl.write.DateTime date;
        try {
            while (rsTemp.next()) {

                rowIndex++;
                for (int colIndex = 1; colIndex <= rsTemp.getMetaData().getColumnCount();) {
                    String coldata = rsTemp.getString(colIndex);//float//date//varchar
                    cellFormat = setFontGeneral(false, Alignment.CENTRE, null, false, true, false, UnderlineStyle.NO_UNDERLINE);
                    setBorder(cellFormat, false, false, false, false);

                    if (rsTemp.getMetaData().getColumnTypeName(colIndex).equalsIgnoreCase("float")) {

                        number = new jxl.write.Number(colIndex++, rowIndex, DmShared.vNull(coldata), cellFormat);
                        workSheet.getWorkSheet().addCell(number);
                    } else {
                        lblHeader = new Label(colIndex++, rowIndex, (DmShared.xNull(coldata)), cellFormat);
                        workSheet.getWorkSheet().addCell(lblHeader);
                    }
                }
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }

        return rowIndex;
    }

    public static WritableCellFormat setFontGeneral(boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), 10, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

    public static WritableCellFormat setFontGeneral(int fontSize, boolean isBold, Alignment anlgnmnt, Colour bgColor, boolean isvertical, boolean isWrap, boolean istalic, UnderlineStyle underline) {
        WritableCellFormat cellFormat = null;
        WritableFont cellFont = null;
        try {
            if (isBold) {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.BOLD, istalic, underline);
            } else {
                cellFont = new WritableFont(WritableFont.createFont("Cambria"), fontSize, WritableFont.NO_BOLD, istalic, underline);
            }

            cellFormat = new WritableCellFormat(cellFont);
            cellFormat.setAlignment(anlgnmnt);
            cellFormat.setVerticalAlignment(VerticalAlignment.CENTRE);

            if (bgColor != null) {
                cellFormat.setBackground(bgColor);
            }
            if (isvertical) {
                cellFormat.setOrientation(Orientation.PLUS_90);
            }
            if (isWrap) {
                cellFormat.setWrap(true);
            }

        } catch (Exception e) {
        }

        return cellFormat;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModule(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PU");
            listNode.setSharedKeyFieldName("Purchase");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("PH");
            listNode.setSharedKeyFieldName("Pharmacy");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSupplier(DmHelper pageing, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> lstTemp = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT"
                    + "   sg.subCode AS code"
                    + "     ,sg.name AS name,sg.manualCode,c.cityName as city"
                    + "     FROM subgroup sg"
                    + "     LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "     left join city as c on c.cityCode = sg.cityCode  "
                    + "     WHERE sgn.code IN ("
                    + "         SELECT acf.Nature"
                    + "         FROM AcFilteration acf"
                    + "         LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type"
                    + "         WHERE  vt.category IN ('" + pvc.PURCHASE_BILL + "', "
                    + "         '" + pvc.PURCHASE_BILL_UNREGISTERED + "', "
                    + "         '" + pvc.PURCHASE_BILL_DIRECT + "', "
                    + "         '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "', "
                    + "         '" + pvc.SERVICE_BILL + "', "
                    + "         '" + pvc.SERVICE_BILL_UNREGISTERED + "',"
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL + "', "
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', "
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "', "
                    + "         '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "')"
                    + " )";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("city")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
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
                    + "  vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category IN (" + V_CATEGORY + ")");
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
    public DmResultPageing<List<TxtHItemMedicine_Mdl>> getTxtHItemMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHItemMedicine_Mdl listNode;
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
                    + " LEFT JOIN unitmast um ON im.sku = um.code");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemMedicine_Mdl();
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemMedicineCat(DmHelper helper, boolean blnToEndOfFile) {
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
                    + "FROM itemCat ic");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemMedicineGroup(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemgroup ig");;
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
                    + " WHERE vt.category in('" + pvc.PHARMACY_PURCHASE_ORDER + "','" + pvc.SERVICE_ORDER + "','" + pvc.PURCHASE_ORDER + "','" + pvc.PURCHASE_ORDER_UNREGISTERED + "')"
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
                    + " WHERE vt.category IN (" + V_CATEGORY + ")"
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
    public DmResultPageing<List<TxtHItemMedCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemMedCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemMedCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHItemMedCompany_Mdl listNode;
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
                listNode = new TxtHItemMedCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
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
