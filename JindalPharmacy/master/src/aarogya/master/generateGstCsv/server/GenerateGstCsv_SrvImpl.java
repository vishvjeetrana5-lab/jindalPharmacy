package aarogya.master.generateGstCsv.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import aarogya.master.generateGstCsv.shared.Filter_Mdl;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import aarogya.master.generateGstCsv.shared.GenerateGstCsv_Srv;
import dataman.dmBase.self.shared.DmEnum;

public class GenerateGstCsv_SrvImpl extends DmRSSPrivate implements GenerateGstCsv_Srv {

    boolean isItemRequiredInCsv = false;

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("S");
            listNode.setSharedKeyFieldName("Sales");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("P");
            listNode.setSharedKeyFieldName("Purchase");
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
    public DmResult<String[]> exportGstDataToCsv(Filter_Mdl filter) {
        DmResult<String[]> dmrTemp = new DmResult<>();
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", tempValue = "", financialPeriod, siteManualCode, directoryPath, fullFilePath, FILE_HEADER;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = "\n", lineData = "";
        String result[] = {"", ""};
        String itemName = "";
        FileWriter fileWriter = null;
        File tempFile = null;
        dmrTemp.setData(result);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================< Validate Filter >==============================*/
            if (DmShared.xNull(filter.getSessionId()).trim().equals("")) {
                throw new UnsupportedOperationException("Invalid Session ID...");
            }
            if (DmShared.dNull(filter.getFromDate()).trim().equals("")) {
                throw new UnsupportedOperationException("From Date is mandatory...");
            }
            if (DmShared.dNull(filter.getToDate()).trim().equals("")) {
                throw new UnsupportedOperationException("To Date is mandatory...");
            }
            if (DmShared.xNull(filter.getType()).trim().equals("")) {
                throw new UnsupportedOperationException("Type is mandatory...");
            }

            /*==============================< Set financial period >==============================*/
            SimpleDateFormat formatter = new SimpleDateFormat("MMM-yyyy");
            financialPeriod = formatter.format(DmShared.convertDate(filter.getFromDate()));

            /*==============================< Set siteManualcode >==============================*/
            siteManualCode = DmServer.executeScalar("SELECT sm.manualCode FROM siteMast sm WHERE sm.code = " + DmShared.xNull(DmServer.getSsnSiteCode(this)), cnTemp);

            /*===========================< Set Voucher Category Condition >===========================*/
            if (DmShared.xNull(filter.getType()).trim().equalsIgnoreCase("P")) {
                tempValue = "GST_Purchase_" + DmShared.xNull(financialPeriod).trim() + "_" + DmShared.xNull(siteManualCode).trim() + ".csv";
//                condition = " AND vt.category IN ("
//                        + "   '" + pvc.PURCHASE_BILL + "'"
//                        + " , '" + pvc.PURCHASE_BILL_UNREGISTERED + "'"
//                        + " , '" + pvc.PURCHASE_BILL_DIRECT + "'"
//                        + " , '" + pvc.PURCHASE_BILL_DIRECT_UNREGISTERED + "'"
//                        + " , '" + pvc.PURCHASE_RETURN + "'"
//                        + " , '" + pvc.PURCHASE_RETURN_UNREGISTERED + "'"
//                        + " , '" + pvc.PHARMACY_PURCHASE_BILL + "'"
//                        + " , '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "'"
//                        + " , '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "'"
//                        + " , '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "'"
//                        + " , '" + pvc.PHARMACY_PURCHASE_RETURN + "'"
//                        + " , '" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "')";
                condition = " AND gst.gstType = " + String.valueOf(DmEnum.DmGSTType.INPUT.toInt()) + " ";
            } else if (DmShared.xNull(filter.getType()).trim().equalsIgnoreCase("S")) {
                tempValue = "GST_Sales_" + DmShared.xNull(financialPeriod).trim() + "_" + DmShared.xNull(siteManualCode).trim() + ".csv";
//                condition = " AND vt.category IN ("
//                        + "   '" + pvc.PHARMACY_BILL + "'"
//                        + " , '" + pvc.PHARMACY_BILL_RETURN + "')";
                condition = " AND gst.gstType = " + String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt()) + " ";
            } else {
                throw new UnsupportedOperationException("Invalid Type...");
            }

            /*=================================< Set file path >=================================*/
            directoryPath = DmServer.Path.sessions + File.separatorChar + filter.getSessionId() + File.separatorChar + "GST_Export";
            fullFilePath = directoryPath + "/" + tempValue;

            /*===========================< Make directory if not exits >===========================*/
            tempFile = new File(directoryPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            tempFile = null;

            /*===========================< Delete if file already exist >===========================*/
            tempFile = new File(fullFilePath);
            if (tempFile.exists()) {
                tempFile.delete();
            }
            tempFile = null;

            FILE_HEADER = "ourGSTIN, ourSiteCode, financialPeriod, customerGSTIN, customerGstNature, eCommerceGSTIN, documentType"
                    + " , throughECommerce, docId, v_Sno, documentNature, voucherNoPrefix, voucherNoSerial, voucherNo, voucherDate"
                    + " , placeOfSupply, reverseCharge, invoiceValue, hsn_sac_code, itemName, itemGSTNature, itemStore, itemQuantity"
                    + " , itemUnit, itemGrossAmount, itemTaxableAmount, itemIGSTRate, itemIGSTAmount, itemSGSTRate, itemSGSTAmount"
                    + " , itemCGSTRate, itemCGSTAmount, itemUGSTRate, itemUGSTAmount, cessRate, cessAmount, itemNetAmount"
                    + " , shippingBillNo, shippingBillDate, shippingPortCode, referenceDocId, referenceVoucherNo, referenceVoucherDate"
                    + " , referenceDocumentType, referenceInvoiceValue, remark_reason, preGST, ammendedDocId";
            if (DmShared.xNull(filter.getType()).trim().equalsIgnoreCase("P")) {
                FILE_HEADER += ", purchaseBillNo, purchaseBillDate, referencePurchaseBillNo, referencePurchaseBillDate";
            }
            FILE_HEADER += ", customerName";
            fileWriter = new FileWriter(fullFilePath, true);
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);

            sqlQuery = "SELECT"
                    + "  sm.gstin AS ourGSTIN"
                    + " , sm.manualCode AS ourSiteCode"
                    + " , format(gst.v_Date, 'MMM-yyyy') AS financialPeriod"
                    + " , sgc.gstin AS customerGSTIN"
                    + " , gst.consigneeType AS customerGstNature"
                    + " , '' AS eCommerceGSTIN"
                    + " , vt.documentTypeCode AS documentType"
                    + " , 'OE' AS throughECommerce"
                    + " , gst.docId"
                    + " , gst.v_Sno"
                    + " , vt.documentNatureCode AS documentNature"
                    + " , gst.recIdPrefix AS voucherNoPrefix"
                    + " , gst.recIdSerial AS voucherNoSerial"
                    + " , gst.recId AS voucherNo"
                    + " , format(gst.v_Date,'dd-MMM-yyyy') AS voucherDate"
                    + " , stm.numericCode AS placeOfSupply"
                    + " , CASE WHEN ISNULL(gst.rcmApplicable, 0) = 1 THEN 'Y' ELSE 'N' END AS reverseCharge"
                    + " , gst.invoiceValue"
                    + " , hsn.hsn AS hsn_sac_code"
                    + " , SUBSTRING(im.name, 1, 30) AS itemName"
                    + " , gst.itemGSTNature"
                    + " , SUBSTRING(dm.nature, 1, 20) AS itemStore"
                    + " , gst.qty AS itemQuantity"
                    + " , gum.manualCode AS itemUnit"
                    + " , gst.itemGrossAmount"
                    + " , gst.itemTaxableAmount"
                    + " , gst.itemIgstRate"
                    + " , gst.itemIgstAmount"
                    + " , gst.itemSgstRate"
                    + " , gst.itemSgstAmount"
                    + " , gst.itemCgstRate"
                    + " , gst.itemCgstAmount"
                    + " , gst.itemUgstRate"
                    + " , gst.itemUgstAmount"
                    + " , gst.itemCessRate"
                    + " , gst.itemCessAmount"
                    + " , gst.itemNetAmount"
                    + " , gst.shippingBillNo"
                    + " , format(gst.shippingBillDt,'dd-MMM-yyyy') AS shippingBillDate"
                    + " , gst.shippingPortCode"
                    + " , gst.refDocId"
                    + " , gst.refRecId"
                    + " , format(gst.refV_Date,'dd-MMM-yyyy') AS refV_Date"
                    + " , gst.refPartyBillNo"
                    + " , format(gst.refPartyBillDt,'dd-MMM-yyyy') AS refPartyBillDt"
                    + " , vtr.documentTypeCode AS refDocType"
                    + " , gst.refDocValue"
                    + " , grm.name AS reason"
                    + " , '' AS preGst"
                    + " , '' AS ammendedDocId"
                    + " , gst.partyBillNO"
                    + " , format(gst.partyBillDt,'dd-MMM-yyyy') AS partyBillDt "
                    + " , sgc.name AS customerName"
                    + " FROM gstTrans gst"
                    + " LEFT JOIN siteMast sm ON sm.code = gst.site_Code"
                    + " LEFT JOIN voucher_Type vt ON vt.v_Type = gst.v_Type"
                    + " LEFT JOIN stateMast stm ON stm.code = gst.placeOfSupply"
                    + " LEFT JOIN hsnMast hsn ON hsn.code = gst.hsnCode"
                    + " LEFT JOIN itemMast im ON im.code = gst.itemCode"
                    + " LEFT JOIN departmentMast dm ON dm.subCode = gst.departmentCode"
                    + " LEFT JOIN gstUnitMast gum ON gum.code = gst.gstUnitCode"
                    + " LEFT JOIN gstReasonMast grm ON grm.code = gst.gstReasonCode"
                    + " LEFT JOIN subGroup sgc ON sgc.subCode = gst.consignee"
                    + " LEFT JOIN voucher_Type vtr ON vtr.v_Type = gst.refV_Type"
                    + " WHERE gst.site_Code = :site_Code"
                    + " AND gst.v_Date BETWEEN :fromDate AND :toDate" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setDate("fromDate", DmServer.convertDate(filter.getFromDate()));
            psTemp.setDate("toDate", DmServer.convertDate(filter.getToDate()));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("ourGSTIN")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ourSiteCode")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("financialPeriod")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("customerGSTIN")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("customerGstNature")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("eCommerceGSTIN")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("documentType")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("throughECommerce")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("docId")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("v_Sno")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("documentNature")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("voucherNoPrefix")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("voucherNoSerial")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("voucherNo")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("voucherDate")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("placeOfSupply")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("reverseCharge")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("invoiceValue")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("hsn_sac_code")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                if (isItemRequiredInCsv) {
                    itemName = DmShared.xNull(rsTemp.getString("itemName"));
                } else {
                    itemName = "";
                }
                lineData += "\"" + DmShared.xNull(itemName).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemGSTNature")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemStore")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemQuantity")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemUnit")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemGrossAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemTaxableAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemIgstRate")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemIgstAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemSgstRate")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemSgstAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemCgstRate")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemCgstAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemUgstRate")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemUgstAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemCessRate")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemCessAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("itemNetAmount")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("shippingBillNo")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("shippingBillDate")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("shippingPortCode")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("refDocId")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("refRecId")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("refV_Date")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("refDocType")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("refDocValue")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("reason")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("preGst")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("ammendedDocId")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                if (DmShared.xNull(filter.getType()).trim().equalsIgnoreCase("P")) {
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("partyBillNO")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("partyBillDt")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("refPartyBillNo")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                    lineData += "\"" + DmShared.xNull(rsTemp.getString("refPartyBillDt")).trim().replace('"', '\'') + "\"" + COMMA_DELIMITER;
                }
                lineData += "\"" + DmShared.xNull(rsTemp.getString("customerName")).trim().replace('"', '\'') + "\"";
                fileWriter.append(lineData);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            rsTemp.close();
            psTemp.close();

            fileWriter.flush();
            fileWriter.close();

            result[0] = directoryPath;
            result[1] = tempValue;

            dmrTemp.setStatus(true);
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
                if (fileWriter != null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
