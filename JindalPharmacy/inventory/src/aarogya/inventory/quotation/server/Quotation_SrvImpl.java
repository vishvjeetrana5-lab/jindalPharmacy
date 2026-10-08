package aarogya.inventory.quotation.server;

import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmFileManager.server.DmFileManager_Server;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import java.util.UUID;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.quotation.shared.DefaultValue_Mdl;
import aarogya.inventory.quotation.shared.Find_Mdl;
import aarogya.inventory.quotation.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.quotation.shared.GrdItemDetail_Mdl;
import aarogya.inventory.quotation.shared.Quotation_Mdl;
import aarogya.inventory.quotation.shared.Quotation_Srv;
import aarogya.inventory.quotation.shared.Quotation_Vld;
import aarogya.inventory.quotation.shared.TxtHEnquiry_Mdl;
import aarogya.inventory.quotation.shared.TxtHItemName_Mdl;
import aarogya.inventory.quotation.shared.TxtHSupplier_Mdl;
import aarogya.inventory.quotation.shared.TxtHVoucherType_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.serviceGrid.shared.ServiceDetail_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.stStatic.shared.ItemPriceList_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STShared;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.HashMap;

public class Quotation_SrvImpl extends DmRSSPrivate implements Quotation_Srv {

    private String vCat = "'" + new ProjectVoucherCategory().QUOTATION + "','" + new ProjectVoucherCategory().QUOTATION_UNREGISTERED + "'";
    ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String voucherTypeFieldName = "q.V_Type", voucherDateFieldName = "q.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition = "";
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select q.docId "
                    + "         from quotation as q "
                    + "         left join voucher_Type as vt on q.v_Type = vt.v_Type  "
                    + "         where vt.category in (" + vCat + ") and q.site_Code = :site_Code and q.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += "         order by q.v_Date desc , vt.short_Name, q.v_No desc, q.recId  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            rsTemp.close();
            psTemp.close();

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
    public DmResult<Quotation_Mdl> getMoveRec(String strKey) {
        DmResult<Quotation_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        Quotation_Mdl mrTemp = new Quotation_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        ServiceDetail_Mdl serviceDetail_Mdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = " select	q.docId, q.recId, q.v_No, q.recIdPrefix, q.v_Date, q.vendorRefNo, q.vendorRefDate, \n"
                    + " q.enquiryRefNo, q.enquiryRefDate, q.u_Name, q.preparedBy, \n"
                    + " q.guiConfigCode, q.v_Type, vt.description,q.subCode as vendorCode, q.validUpto, \n"
                    + " sgv.name as vendorName, en.recId as enquiryNo,en.v_Date as enquiryDate, q.oh_Amt_Gross \n"
                    + " , q.oh_Amt_GrossGoods\n"
                    + " , q.oh_Amt_GrossService\n"
                    + " , q.oh_Amt_Gross\n"
                    + " , q.oh_Amt_Discount\n"
                    + " , q.oh_Amt_TaxableGoods\n"
                    + " , q.oh_Amt_TaxableService\n"
                    + " , q.oh_Amt_Taxable\n"
                    + " , q.oh_Amt_SGST\n"
                    + " , q.oh_Amt_UGST\n"
                    + " , q.oh_Amt_CGST\n"
                    + " , q.oh_Amt_IGST\n"
                    + " , q.oh_Amt_SubTotal\n"
                    + " , q.oh_Amt_Total\n"
                    + " , q.oh_Amt_RoundOff\n"
                    + " , q.oh_Amt_Net\n"
                    + " , q.locationCode\n"
                    + " , q.partyType \n"
                    + " , sg.partyType AS supplierPartyType \n"
                    + " , q.rcmApplicable \n"
                    + " , q.placeOfSupply \n"
                    + " ,q.gstin AS supplierGstin \n"
                    + " ,q.gstinDate AS supplierGstinDate \n"
                    + " , vt.category \n"
                    + " from quotation as q \n"
                    + " left join voucher_Type as vt on q.v_Type = vt.v_Type \n"
                    + " left join subGroup as sgv on q.subCode = sgv.subCode \n"
                    + " LEFT JOIN subGroup sg ON q.subCode = sg.subCode \n"
                    + "         LEFT JOIN ("
                    + "             select docid, enquiryDocid from quotationDetail where enquiryDocId is NOT NULL \n"
                    + "             group by docid,enquiryDocid \n"
                    + "         )vd ON vd.docId = q.docId \n"
                    + "         left join enquiry as en on vd.enquiryDocId = en.docId 	\n"
                    + "		where q.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setQuotationDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                mrTemp.setVendorTag(DmShared.xNull(rsTemp.getString("vendorCode")));
                mrTemp.setVendorText(DmShared.xNull(rsTemp.getString("vendorName")));
                mrTemp.setVendorRefDate(DmShared.dNull(rsTemp.getDate("vendorRefDate")));
                mrTemp.setVendorRefNo(DmShared.xNull(rsTemp.getString("vendorRefNo")));
                mrTemp.setEnquiryText(DmShared.xNull(rsTemp.getString("enquiryNo")));
                mrTemp.setEnquiryRefNo(DmShared.xNull(rsTemp.getString("enquiryRefNo")));
                mrTemp.setEnquiryRefDate(DmShared.dNull(rsTemp.getDate("enquiryRefDate")));
                mrTemp.setValidUptoDate(DmShared.dNull(rsTemp.getDate("validUpto")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setEnquiryDate(DmShared.dNull(rsTemp.getString("enquiryDate")));
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                mrTemp.setPartyType(DmShared.xNull(rsTemp.getString("supplierPartyType")));
                mrTemp.setPartyTypeForEntry(DmShared.xNull(rsTemp.getString("partyType")));
                mrTemp.setRcmApplicable(DmShared.iNull(rsTemp.getString("rcmApplicable")).toString());
                mrTemp.setPlaceOfSupply(DmShared.xNull(rsTemp.getString("placeOfSupply")));
                mrTemp.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
                mrTemp.setSupplierGstin(DmShared.xNull(rsTemp.getString("supplierGstin")));
                mrTemp.setSupplierGstinDate(DmShared.dNull(rsTemp.getDate("supplierGstinDate")));
                //Amount And Tax
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                mrTemp.setGoodsGrossAmt(DmServer.vNull(rsTemp.getString("oh_Amt_GrossGoods"), "0.00"));
                mrTemp.setServiceGrossAmt(DmServer.vNull(rsTemp.getString("oh_Amt_GrossService"), "0.00"));
                mrTemp.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                mrTemp.setGoodsTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_TaxableGoods"), "0.00"));
                mrTemp.setServiceTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_TaxableService"), "0.00"));
                mrTemp.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                if (DmShared.vNull(rsTemp.getString("oh_Amt_SGST")).doubleValue() > 0) {
                    mrTemp.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                } else if (DmShared.vNull(rsTemp.getString("oh_Amt_UGST")).doubleValue() > 0) {
                    mrTemp.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                }
                mrTemp.setSgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                mrTemp.setUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                mrTemp.setCgst(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                mrTemp.setIgst(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                mrTemp.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                mrTemp.setTotalAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Total"), "0.00"));
                mrTemp.setRoundOffAmount(DmServer.vNull(rsTemp.getString("oh_Amt_RoundOff"), "0.00"));
                mrTemp.setNetAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "select  qd.itemCode,qd.v_SNo, im.name as itemName, qd.quantity, qd.unitCode, \n"
                    + "um.unit as unitName, qd.deliveryDate, qd.remark, \n"
                    + "	qd.rate, qd.oh_Amt_Gross as amount, qd.v_SNo,im.propertyList, \n"
                    + "qd.colourCode,icm.name as colourName, \n"
                    + " qd.sizeCode as sizeCode,ism.name sizeName, \n"
                    + "qd.itemMakeCode,imm.CompanyName as itemMakeName, \n"
                    + "um.decimalPlaces,qd.enquiryDocid,qd.enquiryV_SNo, \n"
                    + " im.sku ,ums.unit as skuName,ums.decimalPlaces as allowedSkuDecimalPlaces, \n"
                    + " qd.skuQuantity,cast( (qd.skuQuantity /  qd.quantity) as decimal(18,7)) as conversionRate, \n"
                    + "  qd.itemGSTNature \n"
                    + " , qd.hsnCode \n"
                    + " , hsn.hsn as hsnNo \n"
                    + " , im.itsBasedOn \n"
                    + " , qd.isManualTax\n"
                    + " , qd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , qd.gstTypeCode\n"
                    + " , qd.oh_Amt_Gross\n"
                    + " , qd.oh_Amt_Discount\n"
                    + " , qd.oh_at_Discount\n"
                    + " , qd.oh_Amt_Taxable\n"
                    + " , qd.oh_at_SGST\n"
                    + " , qd.oh_Amt_SGST\n"
                    + " , qd.oh_at_UGST\n"
                    + " , qd.oh_Amt_UGST\n"
                    + " , qd.oh_at_CGST\n"
                    + " , qd.oh_Amt_CGST\n"
                    + " , qd.oh_at_IGST\n"
                    + " , qd.oh_Amt_IGST\n"
                    + " , qd.oh_Amt_SubTotal\n"
                    + " , qd.oh_Amt_Net\n"
                    + "	from quotationDetail as qd \n"
                    + "	left join itemMast as im on qd.itemCode = im.code \n"
                    + " LEFT JOIN itemColourMast icm ON icm.code = qd.colourCode \n"
                    + " LEFT JOIN itemSizeMast ism ON ism.code = qd.sizeCode \n"
                    + " LEFT JOIN itemMakeMast imm ON imm.code = qd.itemMakeCode \n"
                    + "	left join unitMast as um on qd.unitCode = um.code \n"
                    + " LEFT JOIN unitMast ums ON ums.code = im.sku \n"
                    + " LEFT JOIN gstType it ON it.code = qd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn on hsn.code = qd.hsnCode \n"
                    + "	where qd.docId = :docId "
                    + " AND ISNULL(qd.isFooter, 0) = 0 \n"
                    + " order by qd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                node.setDeliveryDate(DmShared.dNull(rsTemp.getDate("deliveryDate")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setEnquiryDocId(DmShared.xNull(rsTemp.getString("enquiryDocid")));
                node.setEnquiryV_SNo(DmShared.xNull(rsTemp.getString("enquiryV_SNo")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                ipm.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setProperties(DmShared.xNull(ipm.getPropertiesCsv()));
                node.setSkuQuantity(DmServer.vNull(rsTemp.getString("skuQuantity")));
                node.setConversionRate(DmServer.vNull(DmServer.vNull(rsTemp.getString("conversionRate"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue()))));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setDisplayUnit(DmShared.xNull(rsTemp.getString("unitName")));
                node.setSkuDecimalPlaces(DmServer.vNull(rsTemp.getString("allowedSkuDecimalPlaces")));
                if (DmShared.vNull(rsTemp.getString("unitCode")).longValue() != DmShared.vNull(rsTemp.getString("sku")).longValue()) {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")) + " " + "=" + " " + DmServer.vNull(node.getConversionRate()) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                } else {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                }
                //Item Details
                node.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                node.setManuaGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                node.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                node.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsnNo(DmShared.xNull(rsTemp.getString("hsnNo")));

                //Amount
                node.setDiscountPer(DmServer.vNull(rsTemp.getString("oh_at_Discount"), "0.00"));
                node.setDiscount(DmServer.vNull(rsTemp.getString("oh_Amt_Discount"), "0.00"));
                node.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                    node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    node.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                    node.setSgstUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    node.setSgstUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                }
                node.setSgstPer(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                node.setSgst(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                node.setUgstPer(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                node.setUgst(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                node.setCgstPer(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                node.setCgst(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                node.setIgstPer(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                node.setIgst(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                node.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                node.setItemValue(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));

                /*==============================<< Get Indent detail >>===============================*/
                DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(true, DmShared.xNull(strKey), DmShared.xNull(rsTemp.getString("v_SNo")), null, ipm, cnTemp);
                if (resultIndentDetail.isStatus()) {
                    node.getListIndentDetail().addAll(resultIndentDetail.getData());
                } else {
                    throw new DmException(0, "No record found.");
                }

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            mrTemp.getListItemDetail().addAll(lstNode);

            /*==============================<< Get Service detail >>===============================*/
            sqlQuery = "SELECT\n"
                    + "  pcd.v_SNo\n"
                    + " , pcd.itemCode\n"
                    + " , pcd.hsnCode\n"
                    + " , pcd.itemGSTNature\n"
                    + " , im.name AS itemName\n"
                    + " , im.smfCode\n"
                    + " , im.itsBasedOn\n"
                    + " , pcd.unitCode \n"
                    + " , pcd.quantity\n"
                    + " , pcd.rate \n"
                    + " , pcd.isManualTax\n"
                    + " , pcd.isManualGstType\n"
                    + " , it.name AS gstTypeName\n"
                    + " , pcd.gstTypeCode\n"
                    + " , pcd.oh_Amt_Gross\n"
                    + " , pcd.oh_Amt_Taxable\n"
                    + " , pcd.oh_at_SGST\n"
                    + " , pcd.oh_Amt_SGST\n"
                    + " , pcd.oh_at_UGST\n"
                    + " , pcd.oh_Amt_UGST\n"
                    + " , pcd.oh_at_CGST\n"
                    + " , pcd.oh_Amt_CGST\n"
                    + " , pcd.oh_at_IGST\n"
                    + " , pcd.oh_Amt_IGST\n"
                    + " , pcd.oh_Amt_SubTotal\n"
                    + " , pcd.oh_Amt_Net \n"
                    + " , um.gstUnitCode\n"
                    + " , hsn.hsn \n"
                    + " , pcd.serviceEvalTypeCode \n"
                    + " , pcd.serviceEvalRate \n"
                    + " FROM quotationDetail pcd\n"
                    + " LEFT JOIN itemMast im ON pcd.itemCode = im.code\n"
                    + " LEFT JOIN gstType it ON it.code = pcd.gstTypeCode \n"
                    + " LEFT JOIN hsnMast hsn ON hsn.code = pcd.hsnCode \n"
                    + " LEFT JOIN unitMast um ON pcd.unitCode = um.code\n"
                    + " WHERE pcd.docId = :docId \n"
                    + " AND ISNULL(pcd.isFooter, 0) = 2\n"
                    + " ORDER BY pcd.v_SNo\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                serviceDetail_Mdl = new ServiceDetail_Mdl();
                serviceDetail_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                serviceDetail_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                serviceDetail_Mdl.setSmfCode(DmShared.xNull(rsTemp.getString("smfCode")));
                serviceDetail_Mdl.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                serviceDetail_Mdl.setV_Sno(DmShared.xNull(rsTemp.getString("v_SNo")));
                serviceDetail_Mdl.setAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                serviceDetail_Mdl.setTaxableAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Taxable"), "0.00"));
                serviceDetail_Mdl.setSubTotal(DmServer.vNull(rsTemp.getString("oh_Amt_SubTotal"), "0.00"));
                serviceDetail_Mdl.setNetAmt(DmServer.vNull(rsTemp.getString("oh_Amt_Net"), "0.00"));
                serviceDetail_Mdl.setRate(DmServer.vNull(rsTemp.getString("rate"), "0.00"));
                serviceDetail_Mdl.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                serviceDetail_Mdl.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                serviceDetail_Mdl.setGstUnitCode(DmShared.xNull(rsTemp.getString("gstUnitCode")));

                serviceDetail_Mdl.setManualTax(DmShared.iNull(rsTemp.getString("isManualTax")).intValue() == 1);
                serviceDetail_Mdl.setManualGstType(DmShared.iNull(rsTemp.getString("isManualGstType")).intValue() == 1);
                serviceDetail_Mdl.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                serviceDetail_Mdl.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                serviceDetail_Mdl.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                serviceDetail_Mdl.setItemGSTNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                serviceDetail_Mdl.setHsn(DmShared.xNull(rsTemp.getString("hsn")));

                if (!DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")).equalsIgnoreCase("")) {
                    serviceDetail_Mdl.setServiceEvalTypeCode(DmShared.xNull(rsTemp.getString("serviceEvalTypeCode")));
                    serviceDetail_Mdl.setServiceEvalTypeName(DmShared.xNull(DmEnum.ServiceEvalType.getViaCode(DmShared.iNull(rsTemp.getString("serviceEvalTypeCode")).intValue()).getName()));
                    serviceDetail_Mdl.setServiceEvalRate(DmServer.vNull(rsTemp.getString("serviceEvalRate"), "0.00"));
                }

                //ITS
                if (DmShared.vNull(rsTemp.getString("oh_at_SGST")).doubleValue() > 0) {
                    serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                    serviceDetail_Mdl.setSgstOrUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                } else if (DmShared.vNull(rsTemp.getString("oh_at_UGST")).doubleValue() > 0) {
                    serviceDetail_Mdl.setSgstOrUgstRate(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                    serviceDetail_Mdl.setSgstOrUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                }
                serviceDetail_Mdl.setSgstRate(DmServer.vNull(rsTemp.getString("oh_at_SGST"), "0.00"));
                serviceDetail_Mdl.setSgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_SGST"), "0.00"));
                serviceDetail_Mdl.setUgstRate(DmServer.vNull(rsTemp.getString("oh_at_UGST"), "0.00"));
                serviceDetail_Mdl.setUgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_UGST"), "0.00"));
                serviceDetail_Mdl.setCgstRate(DmServer.vNull(rsTemp.getString("oh_at_CGST"), "0.00"));
                serviceDetail_Mdl.setCgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_CGST"), "0.00"));
                serviceDetail_Mdl.setIgstRate(DmServer.vNull(rsTemp.getString("oh_at_IGST"), "0.00"));
                serviceDetail_Mdl.setIgstAmt(DmServer.vNull(rsTemp.getString("oh_Amt_IGST"), "0.00"));
                serviceDetail_Mdl.setGstRate(DmShared.vNull(DmShared.vNull(serviceDetail_Mdl.getCgstRate())
                        + DmShared.vNull(serviceDetail_Mdl.getSgstOrUgstRate())
                        + DmShared.vNull(serviceDetail_Mdl.getIgstRate())).toString());

                mrTemp.getGrdServiceDetail().add(serviceDetail_Mdl);
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get GUI >>===============================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), strKey, DmServer.getSsnSiteCode(this), cnTemp));
            dmrTemp.setData(mrTemp);
            cnTemp.close();
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
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String[]> getServerDateTime() {
        DmResult<String[]> dmrTemp = new DmResult<String[]>();
        try {
            dmrTemp.setData(DmServer.getServerDateTime());
            dmrTemp.setStatus(true);
        } catch (Exception ex) {
            dmrTemp.setErrorMessage(ex.getMessage());
        }
        return dmrTemp;
    }

    @Override
    public DmResult<String> deleteRecord(String keyCode, String guiConfigCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Check If docuemnt already used >>============================*/
            STServer.chkIfDocumentUsed("quotationDocId", "pOrderDetail", keyCode, cnTemp);

            /*==============================<< Delete record from Overhead & OverheadLine >>============================*/
            OverHeadLine_Server.delete(keyCode, cnTemp);
            OverHead_Server.delete(keyCode, cnTemp);
            ModifyReason_Server.delete(keyCode, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, keyCode, DmServer.getSsnSiteCode(this), cnTemp);

            /*==============================<< Delete record from quotationIndentDetail >>===============================*/
            sqlQuery = " Delete From quotationIndentDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from quotationDetail >>===============================*/
            sqlQuery = " Delete From quotationDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from quotation >>===============================*/
            sqlQuery = " Delete From quotation Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from mongoDb >>===============================*/
            //<editor-fold defaultstate="collapsed" desc="delete attachments">
            sqlQuery = "SELECT lfc.sNo, lfc.ruleCode FROM linkFileCollection lfc WHERE docid = :docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", DmShared.iNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            DmFileManagerMongo dmFileManagerMongo = DmServer.getConnectionMongo(DmServer.getSsnFileDBName(this));
            while (rsTemp.next()) {
                dmFileManagerMongo.deleteFiles(keyCode, DmShared.iNull(rsTemp.getString("sNo")).toString(), DmShared.xNull(rsTemp.getString("ruleCode")), cnTemp);
            }
            rsTemp.close();
            psTemp.close();
            //</editor-fold>    

            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    public DmResult<String> saveRecord(Quotation_Mdl mrTemp, String voucherPrefix, boolean IsCommonPriceList, boolean IsUpdatePriceListFromQuotation) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode, roundOffItemCode = "", roundOffItemHsnCode = "", roundOffItemGSTNature = "", roundOffGstTypeCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            Quotation_Vld.chkSave(mrTemp);
            //OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            //OverHeadLine_Shared.chkGrid(mrTemp.getListItemDetail());
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            DmServer.checkForwordVoucherDateTime(mrTemp.getQuotationDate(), "");

            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/

                STServer.chkIfDocumentUsed("quotationDocId", "pOrderDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("quotation", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getQuotationDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
                /*----- Save mongoDB files -----*/
                DmFileManager_Server.manageLinkFileCollection(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmShared.xNull(mrTemp.getTempKeyCode()), this, cnTemp);
            } else {
                DmServer.getRecId("quotation", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getQuotationDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            /*==============================<< Check items for ENQUIRY item change >>====================================*/
            if (!DmShared.xNull(mrTemp.getEnqItemChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM enquiryDetail enq " + DmShared.xNull(mrTemp.getEnqItemChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new DmException(0, "Enquiry has been changed, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            /*==============================<< Check items for ENQUIRY approval change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM enquiry enq where enq.docid='" + DmShared.xNull(mrTemp.getEnquiryTag()) + "' and ISNULL(enq.approvedBy,'')='' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Enquiry has been UnApproved, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check items for ENQUIRY date change >>====================================*/
            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM enquiry enq where enq.docid='" + DmShared.xNull(mrTemp.getEnquiryTag()) + "' and enq.v_Date > :quotationDate";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setDate("quotationDate", DmServer.convertDate(mrTemp.getQuotationDate()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Enquiry Date is greater than quotation date, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Check items for INDENT Approval change >>====================================*/
            if (!DmShared.xNull(mrTemp.getIndApprovalChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM indent ind " + DmShared.xNull(mrTemp.getIndApprovalChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new DmException(0, "Indent has been UnApproved, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            /*==============================<< Check items for INDENT Date change >>====================================*/
            if (!DmShared.xNull(mrTemp.getIndentV_DateChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT MAX(ind.v_Date) AS maxIndentDate FROM indent ind " + DmShared.xNull(mrTemp.getIndentV_DateChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.convertDate(DmShared.dNull(rsTemp.getDate("maxIndentDate"))).getTime() > DmShared.convertDate(DmShared.dNull(mrTemp.getQuotationDate())).getTime()) {
                        throw new DmException(0, "Indent Date is changed, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

            /*==============================<< Check items for INDENT Item change >>====================================*/
            if (!DmShared.xNull(mrTemp.getIndItemChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM indentDetail ind " + DmShared.xNull(mrTemp.getIndItemChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                        throw new DmException(0, "Indent has been changed, can't save record, refresh record then save");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            /*==============================<< Check for Round Off Item in Enviro >>====================================*/
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
                sqlQuery = "SELECT im.code, im.hsnCode, im.itemGstNature, iid.gstTypeCode\n"
                        + "FROM itemMast im\n"
                        + "LEFT JOIN itemItsDetail iid ON iid.code = im.code\n"
                        + "WHERE im.uniqueId = 'RO'";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    roundOffItemCode = DmShared.xNull(rsTemp.getString("itemCode"));
                    roundOffItemHsnCode = DmShared.xNull(rsTemp.getString("hsnCode"));
                    roundOffItemGSTNature = DmShared.xNull(rsTemp.getString("itemGstNature"));
                    roundOffGstTypeCode = DmShared.xNull(rsTemp.getString("gstTypeCode"));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.xNull(roundOffItemCode).trim().equals("")) {
                    throw new DmException(0, "Round Off Item not defined in Overhead Master.");
                }

            }
            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert record into quotation >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into quotation (docId, v_Type, v_No, v_Date, v_Prefix, recId, \n"
                        + " subCode, vendorRefNo, vendorRefDate, \n"
                        + " enquiryDocId, enquiryRefNo, enquiryRefDate, \n"
                        + " site_Code, transferDt, preparedBy, u_EntDt,recIdPrefix, guiConfigCode, validUpto, \n"
                        + " consignee, partyType, gsTin, gstinDate,rcmApplicable, placeOfSupply, \n"
                        + " oh_Amt_GrossGoods, oh_Amt_GrossService, oh_Amt_Gross, oh_Amt_Discount, oh_Amt_TaxableGoods, oh_Amt_TaxableService, \n"
                        + " oh_Amt_Taxable, oh_Amt_SGST, oh_Amt_UGST, oh_Amt_CGST, oh_Amt_IGST, \n"
                        + " oh_Amt_SubTotal, oh_Amt_Total, oh_Amt_RoundOff, oh_Amt_Net, locationCode) \n"
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId, \n"
                        + " :subCode, :vendorRefNo, :vendorRefDate, \n"
                        + " :enquiryDocId, :enquiryRefNo, :enquiryRefDate, \n"
                        + " :site_Code, :transferDt, :preparedBy, :uEntDt, :recIdPrefix, :guiConfigCode, :validUpto, \n"
                        + " :consignee, :partyType, :gsTin, :gstinDate, :rcmApplicable, :placeOfSupply, \n"
                        + " :oh_Amt_GrossGoods, :oh_Amt_GrossService, :oh_Amt_Gross, :oh_Amt_Discount, :oh_Amt_TaxableGoods, :oh_Amt_TaxableService, \n"
                        + " :oh_Amt_Taxable, :oh_Amt_SGST, :oh_Amt_UGST, :oh_Amt_CGST, :oh_Amt_IGST, \n"
                        + " :oh_Amt_SubTotal, :oh_Amt_Total, :oh_Amt_RoundOff, :oh_Amt_Net,  :locationCode \n"
                        + " )";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getQuotationDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getVendorTag()));
                psTemp.setString("vendorRefNo", DmShared.xNull(mrTemp.getVendorRefNo()));
                psTemp.setDate("vendorRefDate", DmServer.convertDate(mrTemp.getVendorRefDate()));
                psTemp.setString("enquiryDocId", DmShared.foriegnKey(mrTemp.getEnquiryTag()));
                psTemp.setString("enquiryRefNo", DmShared.foriegnKey(mrTemp.getEnquiryRefNo()));
                psTemp.setDate("enquiryRefDate", DmServer.convertDate(mrTemp.getEnquiryRefDate()));
                psTemp.setDate("validUpto", DmServer.convertDate(mrTemp.getValidUptoDate()));
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("transferDt", null);
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
                psTemp.setString("oh_Amt_GrossGoods", DmServer.vNull(mrTemp.getGoodsGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_GrossService", DmServer.vNull(mrTemp.getServiceGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getGrossAmount(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTemp.getDiscount(), "0.00"));
                psTemp.setString("oh_Amt_TaxableGoods", DmServer.vNull(mrTemp.getGoodsTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_TaxableService", DmServer.vNull(mrTemp.getServiceTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTemp.getSgst(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTemp.getUgst(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTemp.getCgst(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTemp.getIgst(), "0.00"));
                psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTemp.getSubTotal(), "0.00"));
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTemp.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("preparedBy", userName);
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
            } else {
                sqlQuery = " update quotation Set \n"
                        + " subCode = :subCode, \n"
                        + " vendorRefNo = :vendorRefNo, \n"
                        + " vendorRefDate = :vendorRefDate, \n"
                        + " validUpto = :validUpto, \n"
                        + " enquiryDocId = :enquiryDocId, \n"
                        + " enquiryRefNo = :enquiryRefNo, \n"
                        + " enquiryRefDate = :enquiryRefDate, \n"
                        + " transferDt = :transferDt \n"
                        + " , consignee = :consignee \n"
                        + " , partyType = :partyType \n"
                        + " , gsTin = :gsTin \n"
                        + " , gstinDate = :gstinDate \n"
                        + " , rcmApplicable = :rcmApplicable \n"
                        + " , placeOfSupply = :placeOfSupply \n"
                        + " , oh_Amt_GrossGoods = :oh_Amt_GrossGoods\n"
                        + " , oh_Amt_GrossService = :oh_Amt_GrossService\n"
                        + " , oh_Amt_Gross = :oh_Amt_Gross\n"
                        + " , oh_Amt_Discount = :oh_Amt_Discount\n"
                        + " , oh_Amt_TaxableGoods = :oh_Amt_TaxableGoods\n"
                        + " , oh_Amt_TaxableService = :oh_Amt_TaxableService\n"
                        + " , oh_Amt_Taxable = :oh_Amt_Taxable\n"
                        + " , oh_Amt_SGST = :oh_Amt_SGST\n"
                        + " , oh_Amt_UGST = :oh_Amt_UGST\n"
                        + " , oh_Amt_CGST = :oh_Amt_CGST\n"
                        + " , oh_Amt_IGST = :oh_Amt_IGST\n"
                        + " , oh_Amt_SubTotal = :oh_Amt_SubTotal\n"
                        + " , oh_Amt_Total = :oh_Amt_Total\n"
                        + " , oh_Amt_RoundOff = :oh_Amt_RoundOff\n"
                        + " , oh_Amt_Net = :oh_Amt_Net\n"
                        + " , locationCode = :locationCode\n"
                        + " , u_Name = :u_Name \n"
                        + " , u_EntDt = :uEntDt \n"
                        + " Where docId = :docId \n";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getVendorTag()));
                psTemp.setString("vendorRefNo", DmShared.xNull(mrTemp.getVendorRefNo()));
                psTemp.setDate("vendorRefDate", DmServer.convertDate(mrTemp.getVendorRefDate()));
                psTemp.setDate("validUpto", DmServer.convertDate(mrTemp.getValidUptoDate()));
                psTemp.setString("enquiryDocId", DmShared.foriegnKey(mrTemp.getEnquiryTag()));
                psTemp.setString("enquiryRefNo", DmShared.foriegnKey(mrTemp.getEnquiryRefNo()));
                psTemp.setDate("enquiryRefDate", DmServer.convertDate(mrTemp.getEnquiryRefDate()));
                psTemp.setString("transferDt", null);
                psTemp.setString("u_Name", userName);
                psTemp.setString("oh_Amt_GrossGoods", DmServer.vNull(mrTemp.getGoodsGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_GrossService", DmServer.vNull(mrTemp.getServiceGrossAmt(), "0.00"));
                psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getGrossAmount(), "0.00"));
                psTemp.setString("oh_Amt_Discount", DmServer.vNull(mrTemp.getDiscount(), "0.00"));
                psTemp.setString("oh_Amt_TaxableGoods", DmServer.vNull(mrTemp.getGoodsTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_TaxableService", DmServer.vNull(mrTemp.getServiceTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getTaxableAmt(), "0.00"));
                psTemp.setString("oh_Amt_SGST", DmServer.vNull(mrTemp.getSgst(), "0.00"));
                psTemp.setString("oh_Amt_UGST", DmServer.vNull(mrTemp.getUgst(), "0.00"));
                psTemp.setString("oh_Amt_CGST", DmServer.vNull(mrTemp.getCgst(), "0.00"));
                psTemp.setString("oh_Amt_IGST", DmServer.vNull(mrTemp.getIgst(), "0.00"));
                psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTemp.getSubTotal(), "0.00"));
                psTemp.setString("oh_Amt_Total", DmServer.vNull(mrTemp.getTotalAmt(), "0.00"));
                psTemp.setString("oh_Amt_RoundOff", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getNetAmount(), "0.00"));
                psTemp.setString("locationCode", DmShared.foriegnKey(mrTemp.getLocationCode()));
                psTemp.setString("consignee", DmShared.foriegnKey(mrTemp.getSupplierTag()));
                psTemp.setString("partyType", DmShared.xNull(mrTemp.getPartyTypeForEntry()));
                psTemp.setString("gsTin", DmShared.xNull(mrTemp.getSupplierGstin()));
                psTemp.setDate("gstinDate", DmServer.convertDate(mrTemp.getSupplierGstinDate()));
                psTemp.setString("rcmApplicable", DmShared.iNull(mrTemp.getRcmApplicable()).toString());
                psTemp.setString("placeOfSupply", DmShared.foriegnKey(mrTemp.getPlaceOfSupply()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from quotationIndentDetail  >>====================================*/
            sqlQuery = "DELETE qid FROM quotationIndentDetail  qid "
                    + " Left Join quotationDetail as qd ON qid.docId = qd.docId AND qid.v_SNo=qd.v_SNo "
                    + " WHERE qid.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from quotationDetail >>====================================*/
            sqlQuery = "Delete From quotationDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into quotationDetail >>====================================*/
            int vSNo = 0;
            List<GrdItemDetail_Mdl> listDetail = mrTemp.getListItemDetail();
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    if (!detail_Mdl.isServiceRecord()) {
                        detail_Mdl.setvSNo(String.valueOf(++vSNo));
                        sqlQuery = " insert into quotationDetail(docId, v_SNo, itemCode, quantity, rate, \n"
                                + " unitCode, deliveryDate, remark, colourCode, sizeCode, itemMakeCode, enquiryDocid, \n"
                                + " enquiryV_SNo,skuQuantity , hsnCode, itemGSTNature \n"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net \n"
                                + "     , gstTypeCode, isManualGstType, isManualTax) \n"
                                + " values (:docId, :v_SNo, :itemCode, :quantity, :rate, :unitCode, \n"
                                + " :deliveryDate, :remark, :colourCode, :sizeCode, :itemMakeCode, \n"
                                + " :enquiryDocid,:enquiryV_SNo, :skuQuantity, :hsnCode, :itemGSTNature \n"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                + "     , :gstTypeCode, :isManualGstType, :isManualTax \n"
                                + " )";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                        psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).toString());
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                        psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                        psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                        psTemp.setDate("deliveryDate", DmServer.convertDate(detail_Mdl.getDeliveryDate()));
                        psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                        psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColourCode()));
                        psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
                        psTemp.setString("itemMakeCode", DmShared.foriegnKey(detail_Mdl.getItemMakeCode()));
                        psTemp.setString("enquiryDocid", DmShared.foriegnKey(detail_Mdl.getEnquiryDocId()));
                        psTemp.setString("enquiryV_SNo", DmShared.foriegnKey(detail_Mdl.getEnquiryV_SNo()));
                        psTemp.setString("skuQuantity", DmServer.vNull(detail_Mdl.getSkuQuantity()));
                        psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                        psTemp.setString("oh_at_Discount", DmServer.vNull(detail_Mdl.getDiscountPer(), "0.00"));
                        psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getDiscount(), "0.00"));
                        psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));
                        psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getSgst(), "0.00"));
                        psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getUgst(), "0.00"));
                        psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getCgst(), "0.00"));
                        psTemp.setString("oh_at_IGST", DmServer.vNull(detail_Mdl.getIgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_IGST", DmServer.vNull(detail_Mdl.getIgst(), "0.00"));
                        psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(detail_Mdl.getSubTotal(), "0.00"));
                        psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getItemValue(), "0.00"));
                        psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail_Mdl.getGstTypeCode()));
                        psTemp.setString("isManualGstType", detail_Mdl.isManuaGstType() ? "1" : "0");
                        psTemp.setString("isManualTax", detail_Mdl.isManualTax() ? "1" : "0");
                        psTemp.executeUpdate();
                        psTemp.close();

                        if (IsUpdatePriceListFromQuotation) {
                            sqlQuery = "update itemMAst set remark= :remark "
                                    + " WHERE code = :itemCode ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }

                    }
                }
            }


            /*==============================<< Insert records into quotationIndentDetail  >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    for (GrdIndentDetail_Mdl indentDetail_Mdl : detail_Mdl.getListIndentDetail()) {
                        if (!DmShared.xNull(indentDetail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                            sqlQuery = " insert into quotationIndentDetail(docId, v_SNo, indentDocId, itemCode,indentV_SNo) \n"
                                    + " values (:docId, :v_SNo, :indentDocId, :itemCode,:indentV_SNo) \n";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                            psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getvSNo()).toString()); //Doubt (v_SNo different in Pharmacy)
                            psTemp.setString("indentDocId", DmShared.foriegnKey(indentDetail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("indentV_SNo", DmShared.foriegnKey(indentDetail_Mdl.getIndentVSNo()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }

            /*=======================================<< check multiple quotation for same indent and supplier >>=============================================*/
            sqlQuery = " SELECT max(vq1.v_SNo) as v_SNo, max(vq1.name) as itemName, max(vq1.colourName) as colourName, max(vq1.sizeName) as sizeName"
                    + " , max(vq1.itemMakeName) as itemMakeName, vq1.indentDocId, max(vq1.recId) as indentRecid, vq1.indentV_SNo "
                    + " FROM ("
                    + "     select  qd.itemCode,im.name, qd.v_SNo, qid.docId, q.subCode, qid.indentDocId,i.recId,qid.indentV_SNo,"
                    + "     qd.colourCode,icm.name as colourName,qd.sizeCode as sizeCode,ism.name sizeName,qd.itemMakeCode,imm.CompanyName as itemMakeName"
                    + "     from quotation q"
                    + "     left join quotationDetail qd on qd.docId=q.docId"
                    + "     left join quotationIndentDetail qid on qid.docId=q.docId AND qid.v_SNo = qd.v_SNO"
                    + "     left join itemMast im on im.code=qd.itemCode"
                    + "     LEFT JOIN itemColourMast icm ON icm.code = qd.colourCode "
                    + "     LEFT JOIN itemSizeMast ism ON ism.code = qd.sizeCode "
                    + "     LEFT JOIN itemMakeMast imm ON imm.code = qd.itemMakeCode "
                    + "     left join indent i on i.docId=qid.indentDocId"
                    + "     where ISNULL(qid.docId,0) = :docId"
                    + "     ) vq1 "
                    + " LEFT JOIN ("
                    + "     SELECT q2.subCode, qid2.indentDocId, qid2.indentV_SNo "
                    + "     FROM quotation q2 "
                    + "     LEFT JOIN quotationIndentDetail  qid2 ON qid2.docId = q2.docId "
                    + "     where ISNULL(qid2.docId,0) <> :docId"
                    + "     ) vq2 ON vq1.subCode = vq2.subCode AND vq1.indentDocId = vq2.indentDocId AND vq1.indentV_SNo = vq2.indentV_SNo"
                    + " WHERE vq2.subCode IS NOT NULL"
                    + " GROUP BY  vq1.indentDocId, vq1.indentV_SNo "
                    + " ORDER BY max(vq1.v_SNo), max(vq1.recId), vq1.indentV_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            String errorMessage = "";
            while (rsTemp.next()) {
                errorMessage += (DmShared.xNull(rsTemp.getString("indentRecid")).equalsIgnoreCase("")) ? "" : "<b>Indent No:</b> " + (DmShared.xNull(rsTemp.getString("indentRecid"))) + "&nbsp,&nbsp";
                errorMessage += (DmShared.xNull(rsTemp.getString("itemName")).equalsIgnoreCase("")) ? "" : "<b>Item:</b> " + (DmShared.xNull(rsTemp.getString("itemName"))) + "&nbsp,&nbsp";
                errorMessage += (DmShared.xNull(rsTemp.getString("colourName")).equalsIgnoreCase("")) ? "" : "<b>Colour:</b> " + (DmShared.xNull(rsTemp.getString("colourName"))) + "&nbsp,&nbsp";
                errorMessage += (DmShared.xNull(rsTemp.getString("sizeName")).equalsIgnoreCase("")) ? "" : "<b>Size:</b> " + (DmShared.xNull(rsTemp.getString("sizeName"))) + "&nbsp,&nbsp";
                errorMessage += (DmShared.xNull(rsTemp.getString("itemMakeName")).equalsIgnoreCase("")) ? "" : "<b>ItemMake:</b> " + (DmShared.xNull(rsTemp.getString("itemMakeName"))) + "<br>";
            }
            rsTemp.close();
            psTemp.close();
            if (!DmShared.xNull(errorMessage).trim().equalsIgnoreCase("")) {
                errorMessage = "<div style='float:right; margin-right: 5px;'> Quotation already exist with same indent no and supplier - <br><br>" + errorMessage + "</div>";
                throw new DmException(600, errorMessage);
            }
            //========================================(Service Grid Modal)==========================================================
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {

                    if (detail_Mdl.isServiceRecord()) {
                        ++vSNo;
                        detail_Mdl.setvSNo(String.valueOf(vSNo));
                        sqlQuery = "INSERT INTO quotationDetail (\n"
                                + "     docId, v_SNo, itemCode, hsnCode, quantity,  rate, itemGSTNature ,unitCode ,isFooter\n"
                                + "     , oh_Amt_Gross, oh_at_Discount, oh_Amt_Discount, oh_Amt_Taxable\n"
                                + "     , oh_at_SGST, oh_Amt_SGST, oh_at_UGST, oh_Amt_UGST, oh_at_CGST, oh_Amt_CGST\n"
                                + "     , oh_at_IGST, oh_Amt_IGST, oh_Amt_SubTotal, oh_Amt_Net\n"
                                + "     , serviceEvalTypeCode, serviceEvalRate, gstTypeCode, isManualGstType, isManualTax)\n"
                                + " Values (\n"
                                + "     :docId, :v_SNo, :itemCode, :hsnCode, :quantity,  :rate, :itemGSTNature ,:unitCode, :isFooter \n"
                                + "     , :oh_Amt_Gross, :oh_at_Discount, :oh_Amt_Discount, :oh_Amt_Taxable\n"
                                + "     , :oh_at_SGST, :oh_Amt_SGST, :oh_at_UGST, :oh_Amt_UGST, :oh_at_CGST, :oh_Amt_CGST\n"
                                + "     , :oh_at_IGST, :oh_Amt_IGST, :oh_Amt_SubTotal, :oh_Amt_Net\n"
                                + "     , :serviceEvalTypeCode, :serviceEvalRate, :gstTypeCode, :isManualGstType, :isManualTax )\n";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setString("v_SNo", DmShared.xNull(detail_Mdl.getvSNo()));
                        psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                        psTemp.setString("hsnCode", DmShared.foriegnKey(detail_Mdl.getHsnCode()));
                        psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                        psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                        psTemp.setString("itemGSTNature", DmShared.xNull(detail_Mdl.getItemGSTNature()));
                        psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                        psTemp.setString("isFooter", "2");
                        psTemp.setString("oh_Amt_Gross", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                        psTemp.setString("oh_at_Discount", DmServer.vNull(detail_Mdl.getDiscountPer(), "0.00"));
                        psTemp.setString("oh_Amt_Discount", DmServer.vNull(detail_Mdl.getDiscount(), "0.00"));
                        psTemp.setString("oh_Amt_Taxable", DmServer.vNull(detail_Mdl.getTaxableAmt(), "0.00"));
                        psTemp.setString("oh_at_SGST", DmServer.vNull(detail_Mdl.getSgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_SGST", DmServer.vNull(detail_Mdl.getSgst(), "0.00"));
                        psTemp.setString("oh_at_UGST", DmServer.vNull(detail_Mdl.getUgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_UGST", DmServer.vNull(detail_Mdl.getUgst(), "0.00"));
                        psTemp.setString("oh_at_CGST", DmServer.vNull(detail_Mdl.getCgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_CGST", DmServer.vNull(detail_Mdl.getCgst(), "0.00"));
                        psTemp.setString("oh_at_IGST", DmServer.vNull(detail_Mdl.getIgstPer(), "0.00"));
                        psTemp.setString("oh_Amt_IGST", DmServer.vNull(detail_Mdl.getIgst(), "0.00"));
                        psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(detail_Mdl.getSubTotal(), "0.00"));
                        psTemp.setString("oh_Amt_Net", DmServer.vNull(detail_Mdl.getItemValue(), "0.00"));
                        psTemp.setString("serviceEvalTypeCode", DmShared.foriegnKey(detail_Mdl.getServiceEvalTypeCode()));
                        psTemp.setString("serviceEvalRate", DmServer.vNull(detail_Mdl.getServiceEvalRate(), "0.00"));
                        psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail_Mdl.getGstTypeCode()));
                        psTemp.setString("isManualGstType", detail_Mdl.isManuaGstType() ? "1" : "0");
                        psTemp.setString("isManualTax", detail_Mdl.isManualTax() ? "1" : "0");

                        psTemp.executeUpdate();
                        psTemp.close();

                    }
                }
            }

            /*==============================<< Insert Round Off Line >>====================================*/
            if (STShared.MANANGE_ROUNDOFF_AS_SERVICE) {
                if (DmShared.vNull(mrTemp.getRoundOffAmount()) != 0) {
                    sqlQuery = "INSERT INTO quotationDetail ("
                            + "     docId, v_SNo, itemCode, hsnCode, quantity, rate, oh_Amt_Gross, oh_Amt_Net, oh_Amt_Taxable, isFooter"
                            + "     , oh_Amt_SubTotal, itemGSTNature, gstTypeCode)"
                            + " Values ("
                            + "     :docId, :v_SNo, :itemCode, :hsnCode, :quantity, :rate, :oh_Amt_Gross, :oh_Amt_Net, :oh_Amt_Taxable, :isFooter"
                            + "     , :oh_Amt_SubTotal, :itemGSTNature, :gstTypeCode)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", String.valueOf(++vSNo));
                    psTemp.setString("itemCode", DmShared.foriegnKey(roundOffItemCode));
                    psTemp.setString("hsnCode", DmShared.foriegnKey(roundOffItemHsnCode));
                    psTemp.setString("itemGSTNature", DmShared.foriegnKey(roundOffItemGSTNature));
                    psTemp.setString("gstTypeCode", DmShared.foriegnKey(roundOffGstTypeCode));
                    if (DmShared.vNull(mrTemp.getRoundOffAmount()) < 0) {
                        psTemp.setString("quantity", "-1");
                    } else {
                        psTemp.setString("quantity", "1");
                    }
                    psTemp.setString("rate", DmServer.vNull(Math.abs(DmShared.vNull(mrTemp.getRoundOffAmount())), "0.00"));
                    psTemp.setString("oh_Amt_Gross", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_SubTotal", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Net", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("oh_Amt_Taxable", DmServer.vNull(mrTemp.getRoundOffAmount(), "0.00"));
                    psTemp.setString("isFooter", "1");
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            /*==============================<< Insert records of Overhead and OverheadLine  >>====================================*/
            // OverHeadLine_Server.save(listDetail, "docId", mrTemp.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            // OverHead_Server.save(mrTemp.getOverHeadList(), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getObjRecId(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getQuotationDate(), "", "", "", "", "", true, false, cnTemp);

            /*==============================<< Manage Modify Reasion >>====================================*/
            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }
            /*==============================<< Save GUI >>====================================*/
            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), siteCode, cnTemp);

            /*==============================<< Save Item Price List >>====================================*/
            if (IsUpdatePriceListFromQuotation) {
                savePriceList(cnTemp, mrTemp, IsCommonPriceList);
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getVoucherTypeCode(), this);
            }
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, sqlCondition;

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	q.docId, q.recId, q.v_Date, q.vendorRefNo, q.vendorRefDate, q.enquiryRefNo,q.v_No, q.validUpto,  "
                    + "		q.enquiryRefDate, sgv.displayName as vendorName, en.recId as enquiryNo, vt.short_Name "
                    + "		from quotation as q "
                    + "		left join voucher_Type as vt on q.v_Type = vt.v_Type "
                    + "		left join subGroup as sgv on q.subCode = sgv.subCode "
                    + "         LEFT JOIN ("
                    + "             select docid, enquiryDocid "
                    + "             from quotationDetail "
                    + "             where enquiryDocid is NOT NULL "
                    + "             group by docid,enquiryDocid "
                    + "         )vd ON vd.docId = q.docId"
                    + "         left join enquiry as en on vd.enquiryDocId = en.docId 	"
                    + "		where vt.category in (" + vCat + ") and q.site_Code = :site_Code and q.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setQuotationDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setEnquiryDate(DmShared.dNull(rsTemp.getDate("enquiryRefDate")));
                listNode.setEnquiryNo(DmShared.xNull(rsTemp.getString("enquiryNo")));
                listNode.setQuotationType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setEnquiryRefNo(DmShared.xNull(rsTemp.getString("enquiryRefNo")));
                listNode.setVendorName(DmShared.xNull(rsTemp.getString("vendorName")));
                listNode.setVendorQotDate(DmShared.dNull(rsTemp.getDate("vendorRefDate")));
                listNode.setVendorQotNo(DmShared.xNull(rsTemp.getString("vendorRefNo")));
                listNode.setValidUptoDate(DmShared.dNull(rsTemp.getDate("validUpto")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucher(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT "
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.guiTemplateCode"
                    + " , vt.category"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category in (" + vCat + ")"
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("category")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setSiteCode(DmServer.getSsnSiteCode(this));
            defaultValue.setGstApplicable(!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase(""));

            /*==============================<< Get default voucher_Type >>===============================*/
            defaultValue.setVoucherType(getVoucherType("", cnTemp));

            /*==========================<< Get Site GST Details >>==========================*/
            sqlQuery = "SELECT "
                    + "   si.gstin AS siteGstin"
                    + " , si.gstinDate AS siteGstinDate"
                    + " , si.registrationType AS siteRegistrationType"
                    + " , st.code AS siteStateCode"
                    + " , st.isUT AS isSiteStateUT"
                    + " FROM siteMast si"
                    + " LEFT JOIN stateMast st ON si.stateNumericCode = st.numericCode"
                    + " WHERE si.code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setSiteGstinDate(DmShared.dNull(rsTemp.getDate("siteGstinDate")));
                defaultValue.setSiteRegistrationType(DmShared.xNull(rsTemp.getString("siteRegistrationType")));
                defaultValue.setSiteStateCode(DmShared.xNull(rsTemp.getString("siteStateCode")));
                defaultValue.setIsSiteStateUT(DmShared.xNull(rsTemp.getString("isSiteStateUT")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.isIncludeTaxInCost,e.IsCommonPricelistForSite,e.updatePriceListFromQuotation \n"
                    + "FROM enviro e\n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setIncludeTaxInCost(DmShared.iNull(rsTemp.getString("isIncludeTaxInCost")).intValue() == 1);
                defaultValue.setIsCommonPriceList(DmShared.iNull(rsTemp.getString("IsCommonPricelistForSite")).intValue() == 1);
                defaultValue.setUpdatePriceListFromQuotation(DmShared.iNull(rsTemp.getString("updatePriceListFromQuotation")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Enviro Company Data >>====================================*/
            sqlQuery = "SELECT ec.isInventoryAccountingActive \n"
                    + "FROM enviroCompany ec\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getString("isInventoryAccountingActive") == null) {
                    throw new DmException(0, "Inventory Accounting Active is not defined in Environment Setting (Company).");
                }
                defaultValue.setInventoryAccountingActive(DmShared.iNull(rsTemp.getString("isInventoryAccountingActive")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();
            /*==============================<< ************************* >>====================================*/

            dmrTemp.setData(defaultValue);
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

    private TxtHVoucherType_Mdl getVoucherType(String partyType, Connection cnTemp) throws Exception {
        TxtHVoucherType_Mdl voucherType_Mdl = new TxtHVoucherType_Mdl();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            /*==============================<< Get default voucher_Type >>===============================*/
            sqlQuery = "SELECT TOP 1"
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.guiTemplateCode"
                    + " , vt.category"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = :category "
                    + " AND isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(DmServer.getSsnSiteGSTIN(this)).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(partyType).trim().equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                psTemp.setString("category", pvc.QUOTATION);
            } else {
                psTemp.setString("category", pvc.QUOTATION_UNREGISTERED);
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                voucherType_Mdl.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                voucherType_Mdl.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                voucherType_Mdl.setVoucherCategory(DmShared.xNull(rsTemp.getString("category")));
                voucherType_Mdl.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw e;
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

        return voucherType_Mdl;
    }

    @Override
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "", itemCatFilteration = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition = " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            sqlWithParam.setSQL(" select im.code, im.name, im.manualCode, im.sku, um.unit as skuName, "
                    + " im.propertyList, IsNull(um.decimalPlaces,0) as decimalPlaces,IsNull(ump.decimalPlaces,0) as decimalPlacesPurchaseUnit, "
                    + " ump.unit as purchaseUnitName,im.purchaseUnit as purchaseUnitCode,im.purchaseUnitConversionRate,"
                    + "  im.isIndentMandatory, hsn.hsn as hsnNo , im.itsBasedOn, im.itemGSTNature, im.hsnCode"
                    + " from itemMast as im  "
                    + " left join itemSiteList sl on sl.code = im.code"
                    + " left join unitmast as um on im.sku=um.code  "
                    + " left join unitmast as ump on im.purchaseUnit=ump.code"
                    + "  left join hsnMast as hsn on hsn.code = im.hsnCode "
                    + " where im.IsActive = 1 "
                    + " and (im.filterItemType = :filterItemType OR im.isCommon = 1) "
                    + " and sl.site_Code = :site_Code" + condition);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSku(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setDisplayUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setDecimalPlacesPurchaseUnit(DmShared.xNull(rsTemp.getString("decimalPlacesPurchaseUnit")));
                listNode.setIsIndentMandatory(DmShared.xNull(rsTemp.getString("isIndentMandatory")));
                listNode.setConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                if (DmShared.vNull(rsTemp.getString("purchaseUnitCode")).longValue() != DmShared.vNull(rsTemp.getString("sku")).longValue()) {
                    listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " " + "=" + " " + listNode.getConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                } else {
                    listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                }
                listNode.setHsnNo(DmShared.xNull(rsTemp.getString("hsnNo")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setItemGstNature(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                listNode.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));

                lstTemp.add(listNode);
            }
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rsTemp.close();
            psTemp.close();

            rstTemp.setStatus(true);
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
    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(List<KeyFieldExtended> keyFields, String itemCode) {
        DmResult<List<GrdIndentDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Indent detail (via list) >>===============================*/
            ItemProperty_Mdl ippm = new ItemProperty_Mdl();
            ippm.setItemCode(DmShared.xNull(itemCode));
            DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(false, "", "", keyFields, ippm, cnTemp);
            if (resultIndentDetail.isStatus()) {
                dmrTemp.setData(resultIndentDetail.getData());
            } else {
                throw new DmException(0, "No record found.");
            }

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    @Override
    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String v_Type, String enquiryDocId, String location, String isStateOrUt, List<KeyFieldExtended> keyFields) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", condition1 = "", itemCatFilteration = "";
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        List<GSTS_Mdl> lstIts = new ArrayList<>();
        GrdItemDetail_Mdl node;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition1 += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            if (DmShared.vNull(enquiryDocId).doubleValue() != 0) {
                /*==============================<< Get Item detail (via enquiryDocId) >>===============================*/
                sqlQuery = " select  qd.itemCode, im.name as itemName, qd.skuQuantity as quantity, qd.unitCode as purchaseUnitCode, um.unit as purchaseUnitName, "
                        + " qd.v_SNo,(im.propertyList) propertyList,"
                        + "(qd.colourCode) colourCode,"
                        + "(icm.name) as colourName,  "
                        + "(qd.sizeCode) as sizeCode,(ism.name)  sizeName ,"
                        + "(qd.itemMakeCode) as itemMakeCode,(imm.CompanyName) as itemMakeName ,"
                        + " qd.skuQuantity ,"
                        + " ump.decimalPlaces as allowedDecimalPlaces, "
                        + " um.decimalPlaces as allowedSkuDecimalPlaces,ums.unit as unitName ,"
                        + " ums.code as unitCode,qd.docid as enquiryDocId,qd.V_SNo as enquiryV_SNo, "
                        + " im.purchaseUnitConversionRate, "
                        + " hsn.hsn as hsnNo, "
                        + " im.hsnCode as hsnCode, "
                        + "  im.itsBasedOn "
                        + " from enquiryDetail as qd            "
                        + " left join enquiry as e on e.docid = qd.docid    "
                        + " left join itemMast as im on qd.itemCode = im.code    "
                        + " LEFT JOIN itemColourMast as icm ON icm.code = qd.colourCode  "
                        + " LEFT JOIN itemSizeMast as ism ON ism.code = qd.sizeCode  "
                        + " LEFT JOIN itemMakeMast  as imm ON imm.code = qd.itemMakeCode "
                        + " left join unitMast as um on qd.unitCode = um.code   "
                        + " left join unitMast as ums on im.sku = ums.code       "
                        + " left join unitMast as ump on im.purchaseUnit = ump.code "
                        + " left join hsnMast as hsn on hsn.code = im.hsnCode "
                        + " where qd.docId = :docId "
                        + condition1
                        + " order by im.name ";
            } else {
                /*==============================<< Get Item detail (via list) >>===============================*/
                for (KeyFieldExtended field : keyFields) {
                    if (DmShared.vNull(field.getSharedKeyFieldCode()).doubleValue() != 0) {
                        if (!condition.trim().equalsIgnoreCase("")) {
                            condition += " or ";
                        }
                        condition += "  (id.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                                + " and  id.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "') ";
                    }
                }

                sqlQuery = "select  id.itemCode, max(im.name) as itemName,max(id.docId) as indDocid,max(id.v_SNo) as indv_SNo, sum(id.aprQty) as quantity, max(im.sku) as unitCode,"
                        + " max(um.unit) as unitName,max(im.propertyList) propertyList,max(id.colourCode) colourCode,max(icm.name) as colourName,"
                        + " max(id.sizeCode) as sizeCode,max(ism.name) as sizeName, "
                        + " max(id.itemMakeCode) as itemMakeCode,max(imm.CompanyName) as itemMakeName, "
                        + " max(im.purchaseUnit) as purchaseUnitCode,max(ump.unit) as purchaseUnitName,"
                        + " max(ump.decimalPlaces) as allowedDecimalPlaces,"
                        + " max(um.decimalPlaces) as allowedSkuDecimalPlaces,"
                        + " max(im.purchaseUnitConversionRate) as purchaseUnitConversionRate, "
                        + " max(hsn.hsn) as hsnNo, "
                        + " max(im.hsnCode) as hsnCode, "
                        + " max(im.itsBasedOn) as itsBasedOn "
                        + "         from indent as i "
                        + "         left join indentDetail as id on i.docId = id.docId "
                        //                        + "         left join quotationIndentDetail qid on qid.indentDocId = id.docId AND qid.indentV_SNo = id.v_Sno "  //TBC :Satyam
                        + "         left join itemMast as im on id.itemCode = im.code "
                        + " LEFT JOIN itemColourMast as icm ON icm.code = id.colourCode "
                        + " LEFT JOIN itemSizeMast as ism ON ism.code = id.sizeCode "
                        + " LEFT JOIN itemMakeMast as imm ON imm.code = id.itemMakeCode "
                        + " left join unitMast as um on im.sku = um.code "
                        + " left join unitMast as ump on im.purchaseUnit = ump.code   "
                        + " left join hsnMast as hsn on hsn.code = im.hsnCode "
                        + " where id.isStatus = 0 and isNull(i.approvedBy,'') <> '' "
                        + " and (" + condition + ")" + condition1
                        + "         group by id.itemCode,id.colourCode,id.sizeCode"
                        + "         order by itemName ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.vNull(enquiryDocId).doubleValue() != 0) {
                psTemp.setString("docId", DmShared.iNull(enquiryDocId).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setDisplayUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setDecimalPlaces(DmShared.xNull(rsTemp.getString("allowedDecimalPlaces")));
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                ipm.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setProperties(ipm.getPropertiesCsv());

                node.setHsnCode(DmShared.xNull(rsTemp.getString("hsnCode")));
                node.setHsnNo(DmShared.xNull(rsTemp.getString("hsnNo")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));

                node.setSkuQuantity(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue())));
                if (DmShared.vNull(enquiryDocId).doubleValue() != 0) {
                    node.setEnquiryDocId(DmShared.xNull(rsTemp.getString("enquiryDocid")));
                    node.setEnquiryV_SNo(DmShared.xNull(rsTemp.getString("enquiryV_SNo")));
                }

                if (!DmShared.xNull(rsTemp.getString("unitCode")).equalsIgnoreCase(node.getPurchaseUnitCode())) {
                    if (DmShared.vNull(node.getQuantity()) > 0 && DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate")) > 0) {
                        double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                        node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedDecimalPlaces")).intValue())));
                    }
                }

                node.setConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                if (DmShared.vNull(rsTemp.getString("purchaseUnitCode")).longValue() != DmShared.vNull(rsTemp.getString("unitCode")).longValue()) {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " " + "=" + " " + node.getConversionRate() + " " + DmShared.xNull(rsTemp.getString("unitName")));
                } else {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                }

                DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail;
                ItemProperty_Mdl ippm = new ItemProperty_Mdl();
                ippm.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                ippm.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                ippm.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                ippm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));

                if (DmShared.vNull(enquiryDocId).doubleValue() == 0) {
                    /*==============================<< Get Indent detail (via list & ItemCode ) >>===============================*/
                    resultIndentDetail = getIndentDetail(false, "", "", keyFields, ippm, cnTemp);
                } else {
                    /*==============================<< Get Indent detail (via enquiryDocId & v_SNo) >>===============================*/
                    resultIndentDetail = getIndentDetail(false, DmShared.xNull(enquiryDocId), DmShared.iNull(rsTemp.getString("v_SNo")).toString(), null, ippm, cnTemp);
                }
                if (resultIndentDetail.isStatus()) {
                    node.getListIndentDetail().addAll(resultIndentDetail.getData());
                } else {
                    throw new DmException(0, "No record found.");
                }

                DmResult<List<GSTS_Mdl>> dmrIts = getGSTS(node.getSharedKeyFieldCode(), "", node.getItsBasedOn(), DmShared.xNull(location), "", false, cnTemp);
                if (dmrIts != null) {
                    for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                        String taxPer = "";

                        taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                        if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isStateOrUt).intValue() == 1)) {
                            node.setSgstUgstPer(DmShared.xNull(taxPer));
                            node.setSgstPer(DmShared.xNull(taxPer));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isStateOrUt).intValue() == 1)) {
                            node.setSgstUgstPer(DmShared.xNull(taxPer));
                            node.setUgstPer(DmShared.xNull(taxPer));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                            node.setCgstPer(DmShared.xNull(taxPer));

                        } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                            node.setIgstPer(DmShared.xNull(taxPer));

                        }
                    }
                    node.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                    node.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                }

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
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
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(boolean isMoveRec, String docId, String v_SNo, List<KeyFieldExtended> keyFields, ItemProperty_Mdl ippm, Connection cnTemp) {
        DmResult<List<GrdIndentDetail_Mdl>> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<GrdIndentDetail_Mdl> lstNode = new ArrayList<>();
        GrdIndentDetail_Mdl node;
        dmrTemp.setData(lstNode);
        String condition = "", propertyCondition2 = "";

        try {

            if (!DmShared.xNull(ippm.getColourCode()).equalsIgnoreCase("")) {
                propertyCondition2 += " and id.colourCode = " + DmShared.xNull(ippm.getColourCode()) + "";

            }
            if (!DmShared.xNull(ippm.getSizeCode()).equalsIgnoreCase("")) {
                propertyCondition2 += " and id.sizeCode = " + DmShared.xNull(ippm.getSizeCode()) + "";
            }
            if (DmShared.vNull(docId).doubleValue() != 0) {
                /*==============================<< Get Indent detail (via docId & v_SNo ) >>===============================*/
                String tableName = "";
                if (isMoveRec) {
                    tableName = " quotationIndentDetail ";
                } else {
                    tableName = " enquiryIndentDetail ";
                }
                sqlQuery = "select  qid.indentDocId, id.v_SNo, i.recId as indentNo, i.v_Date as indentDate, "
                        + "         id.aprQty as quantity, id.reqDate, im.sku, um.unit, id.remark, "
                        + "         im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName,"
                        + "         ump.decimalPlaces as allowedDecimalPlaces,"
                        + "         um.decimalPlaces as allowedSkuDecimalPlaces,"
                        + "         im.purchaseUnitConversionRate as purchaseUnitConversionRate "
                        + "         from " + tableName + " as qid "
                        + "         left join indent as i on qid.indentDocId = i.docId "
                        + "         left join indentDetail as id on qid.indentDocId = id.docId and qid.indentV_SNo = id.v_SNo "
                        + "         left join itemMast as im on qid.itemCode = im.code   "
                        + "         left join unitMast as um on im.sku = um.code   "
                        + "         left join unitMast as ump on im.purchaseUnit = ump.code   "
                        + "         where qid.docId = :docId and qid.v_SNo = :v_SNo "
                        + "        order by id.v_SNo ";
            } else {
                /*==============================<< Get Indent detail (via list & ItemCode ) >>===============================*/
                for (KeyFieldExtended field : keyFields) {
                    if (DmShared.vNull(field.getSharedKeyFieldCode()).doubleValue() != 0) {
                        if (!condition.trim().equalsIgnoreCase("")) {
                            condition += " or ";
                        }
                        condition += "  (id.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                                + " and  id.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "' "
                                + " and id.itemCode = :itemCode" + propertyCondition2
                                + " and IsNull(id.aprQty,0) > 0"
                                + ") ";
                    }
                }
                sqlQuery = "select  id.docId as indentDocId, id.v_SNo, i.recId as indentNo, i.v_Date as indentDate,  "
                        + "         id.aprQty as quantity, id.reqDate, im.sku, um.unit, id.remark, "
                        + "         im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName,"
                        + "         ump.decimalPlaces as allowedDecimalPlaces,"
                        + "         um.decimalPlaces as allowedSkuDecimalPlaces,"
                        + "         im.purchaseUnitConversionRate as purchaseUnitConversionRate "
                        + "         from indent as i "
                        + "         left join indentDetail as id on i.docId = id.docId "
                        + "         inner join voucher_Type as vt on vt.v_Type = i.V_type "
                        + "         left join itemMast as im on id.itemCode = im.code "
                        + "         left join unitMast as um on im.sku = um.code "
                        + "         left join unitMast as ump on im.purchaseUnit = ump.code   "
                        + "         where id.isStatus = 0 and i.site_Code = :site_Code "
                        + "         and isNull(i.approvedBy,'') <> '' "
                        + "         and vt.category = '" + new ProjectVoucherCategory().INVENTORY_INDENT + "' "
                        + "         and  " + condition
                        + "         order by id.v_SNo ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            if (DmShared.vNull(docId).doubleValue() != 0) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("v_SNo", DmShared.iNull(v_SNo).toString());
            } else {
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("itemCode", DmShared.iNull(ippm.getItemCode()).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdIndentDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("indentDocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indentNo")));
                node.setIndentVSNo(DmServer.vNull(rsTemp.getString("v_SNo")));
                node.setIndentDate(DmShared.dNull(rsTemp.getDate("indentDate")));
                node.setRequiredDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setIndentSKU(DmShared.xNull(rsTemp.getString("unit")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setIndentQty(DmServer.vNull(rsTemp.getString("quantity")));
                node.setIndentQtyInPurchaseUnit(DmServer.vNull(rsTemp.getString("quantity")));

                if (!isMoveRec) {
                    if (!DmShared.xNull(rsTemp.getString("sku")).equalsIgnoreCase(rsTemp.getString("purchaseUnitCode"))) {
                        if (DmShared.vNull(node.getIndentQtyInPurchaseUnit()) > 0 && DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate")) > 0) {
                            double qty = DmShared.vNull(node.getIndentQtyInPurchaseUnit()) / DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                            node.setIndentQtyInPurchaseUnit(DmServer.vNull(qty));
                        }
                    }
                }
                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

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
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl mrTemp = new TxtHSupplier_Mdl();
        Connection cnTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            lstTemp = getSupplierDetail("", voucherCategory, flagFilterByVoucherCategory, v_Type, helper, blnToEndOfFile, cnTemp);
            rstTemp.setData(lstTemp);
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            cnTemp.close();

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private List<TxtHSupplier_Mdl> getSupplierDetail(String vendorCode, String voucherCategory, boolean flagFilterByVoucherCategory, String v_Type, DmHelper pageing, boolean blnToEndOfFile, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        String condition = "", filtration = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            if (DmShared.iNull(vendorCode).longValue() > 0) {
                condition += " AND  sg.subCode = :vendorCode ";
            }

            if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.QUOTATION_UNREGISTERED)) {
                condition += " AND sg.partyType = '" + DmEnum.DmPartyType.Unregistered.getId() + "'";
            } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(pvc.QUOTATION)) {
                condition += " AND sg.partyType != '" + DmEnum.DmPartyType.Registered.getId() + "'";
            }

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + pvc.QUOTATION + "','" + pvc.QUOTATION_UNREGISTERED + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            sqlQuery = "SELECT sg.subCode AS code, sg.name, sg.manualCode, c.cityName, "
                    + " c.stateCode, sg.partyType, sg.gstin, sg.gstinDate "
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(vendorCode).longValue() > 0) {
                sqlWithParam.setString("vendorCode", vendorCode);
            }
            if (!flagFilterByVoucherCategory) {
                sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setPartyType(DmShared.xNull(rsTemp.getString("partyType")));
                listNode.setGstin(DmShared.xNull(rsTemp.getString("gstin")));
                listNode.setGstinDate(DmShared.dNull(rsTemp.getDate("gstinDate")));
                lstTemp.add(listNode);
            }

            rsTemp.close();
            psTemp.close();
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
        return lstTemp;
    }

    @Override
    public DmResult<TxtHSupplier_Mdl> getSupplierDetail(String vendorCode, String voucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResult<TxtHSupplier_Mdl> rstTemp = new DmResult();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        TxtHSupplier_Mdl mrTemp = new TxtHSupplier_Mdl();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getSupplierDetail(vendorCode, voucherCategory, false, v_Type, helper, blnToEndOfFile, cnTemp);

            if (lstTemp != null) {
                if (lstTemp.size() > 0 && lstTemp.get(0) != null) {
                    mrTemp = lstTemp.get(0);
                }
            }
            rstTemp.setData(mrTemp);
            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<TxtHEnquiry_Mdl>> getTxtHEnquiryNo(String vendorCode, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHEnquiry_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHEnquiry_Mdl> lstTemp = new ArrayList<TxtHEnquiry_Mdl>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getEnquiryDetail("", vendorCode, v_Date, quotationDocId, helper, blnToEndOfFile, cnTemp);
            rstTemp.setData(lstTemp);

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    private List<TxtHEnquiry_Mdl> getEnquiryDetail(String enquiryDocId, String vendorCode, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        List<TxtHEnquiry_Mdl> lstTemp = new ArrayList<>();
        TxtHEnquiry_Mdl listNode;
        String condition = "", filtration = "", conditionEdit = "";;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            if (!DmShared.dNull(v_Date).trim().equals("")) {
                condition += " AND e.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }
            if (DmShared.iNull(enquiryDocId).longValue() > 0) {
                condition += " AND e.docId = :enquiryDocId";
            }
            if (DmShared.iNull(quotationDocId).longValue() != 0) {
                conditionEdit += " WHERE q.docId != :quotationDocId";
            }

            sqlQuery = "select	e.docId as code, e.recId as name, e.v_Date as enquiryDate, e.referenceNo as refNo "
                    + "		from enquiry as e "
                    + "		left join enquiryVendorDetail as evd on e.docId = evd.docId "
                    + "		left join voucher_Type as vt on e.v_Type=vt.v_Type "
                    + " left join ("
                    + "             SELECT q.subCode, qd.enquiryDocId "
                    + "             FROM quotation q "
                    + "             INNER JOIN quotationDetail qd ON qd.docId = q.docid "
                    + conditionEdit
                    + "             GROUP BY q.subCode, qd.enquiryDocId "
                    + "         ) vEd on vEd.enquiryDocId = e.docid AND vEd.subCode = evd.subCode "
                    + "		where evd.isStatus = 0 and isNull(e.approvedBy,'') <> '' and evd.subCode = :subCode "
                    + "         and vEd.enquiryDocId is null "
                    + "         and vt.category='" + new ProjectVoucherCategory().INVENTORY_ENQUIRY + "'" + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("subCode", DmShared.iNull(vendorCode).toString());

            if (!DmShared.dNull(v_Date).trim().equals("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }

            if (DmShared.iNull(quotationDocId).longValue() != 0) {
                sqlWithParam.setString("quotationDocId", DmShared.iNull(quotationDocId).toString());
            }
            if (DmShared.iNull(enquiryDocId).longValue() > 0) {
                sqlWithParam.setString("enquiryDocId", enquiryDocId);
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHEnquiry_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setEnquiryDate(DmShared.dNull(rsTemp.getDate("enquiryDate")));
                listNode.setEnquiryRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                lstTemp.add(listNode);
            }

            rsTemp.close();
            psTemp.close();

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
        return lstTemp;
    }

    @Override
    public DmResult<List<TxtHEnquiry_Mdl>> getEnquiryDetail(KeyFieldExtended keyField, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResult<List<TxtHEnquiry_Mdl>> rstTemp = new DmResult();
        List<TxtHEnquiry_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            lstTemp = getEnquiryDetail(keyField.getSharedKeyFieldCode(), keyField.getSharedKeyFieldName(), v_Date, quotationDocId, helper, blnToEndOfFile, cnTemp);
            rstTemp.setData(lstTemp);

            cnTemp.close();
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public String getTempKeyCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHRCMApplicable(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGstType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT it.code, it.name, it.printSrNo\n"
                    + "FROM gstType it\n"
                    + "WHERE it.isActive = 1\n";
            sqlWithParam.setSQL(sqlQuery);
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
    public DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst) {
        DmResult<List<GSTS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp = getGSTS(itemCode, itemRate, itsBasedOn, locationCode, gstTypeCode, isManualGst, cnTemp);
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private DmResult<List<GSTS_Mdl>> getGSTS(String itemCode, String itemRate, String itsBasedOn, String locationCode, String gstTypeCode, boolean isManualGst, Connection cnTemp) throws Exception {
        DmResult<List<GSTS_Mdl>> dmrTemp = new DmResult();
        try {

            dmrTemp = STServer.getGSTS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, locationCode, gstTypeCode, isManualGst, cnTemp);

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            throw e;
        }

        return dmrTemp;
    }

    @Override
    public DmResult<Quotation_Mdl> getListFromCSV(String serverDirectory, String serverFileName, String location, String isStateOrUt) {
        DmResult<Quotation_Mdl> dmrTemp = new DmResult<Quotation_Mdl>();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        File sessionDir;
        String fieldValue, sqlQuery;
        String directoryPath, lineNoDisplay = "";
        DmCsvReader csvReader = null;
        CSV_Mdl csvMdl = null;
        DmHandelFile handelFileLog = null;
        File tempFile = null;
        boolean flag = false;
        int index = 0, noOfBug = 0, count = 0;
        Quotation_Mdl mrTemp = new Quotation_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        try {
            directoryPath = serverDirectory + File.separatorChar + "Quotation_Log";

            /*===========================< Make directory if not exits >===========================*/
            tempFile = new File(directoryPath);
            tempFile.delete();
            tempFile.mkdirs();
            tempFile = null;

            handelFileLog = new DmHandelFile(directoryPath + File.separatorChar + "Quotation.log", true);

            sessionDir = new File(serverDirectory + File.separatorChar + serverFileName);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (sessionDir.exists()) {
                csvReader = new DmCsvReader(sessionDir);
                csvReader.readLines();

                DmImportData_Server.checkHeader(new String[]{"itemName", "rate", "discountPer", "mrp", "remark"}, csvReader, handelFileLog);
                index = 0;

                for (DmDynamicGridRow ddgr : csvReader.getRows()) {
                    csvMdl = new CSV_Mdl();
                    index++;
                    lineNoDisplay = "line No. " + DmShared.iNull(index).toString() + " :";
                    for (String columnName : csvReader.getHeaders()) {
                        fieldValue = ddgr.getCells().get(columnName.toLowerCase()) + "";
                        if (columnName.equalsIgnoreCase("itemName")) {
                            char[] ch = fieldValue.toCharArray();
                            int countDoubleQutoes = 0;
                            for (int i = 0; i < ch.length; i++) {
                                if (ch[i] == '\"') {
                                    countDoubleQutoes++;
                                }
                            }
                            if (countDoubleQutoes > 1) {
                                fieldValue = fieldValue.substring(0, ch.length - 1);
                            }
                            flag = validateField(columnName, fieldValue, true, false, false, lineNoDisplay, handelFileLog);
                            if (DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                                flag = false;
                                handelFileLog.writeLine(lineNoDisplay + " not inserted. Item Name(" + DmShared.xNull(fieldValue).trim() + ") not found.");
                            } else {
                                sqlQuery = "SELECT im.code as itemCode, im.purchaseUnit AS unitCode, um.unit AS unitName,um.decimalPlaces\n"
                                        + " ,im.sku ,ums.unit as skuName,ums.decimalPlaces as allowedSkuDecimalPlaces\n"
                                        + " ,im.purchaseUnitConversionRate as conversionRate\n"
                                        + " ,1 quantity,1*(im.purchaseUnitConversionRate) as skuQty "
                                        + " ,im.isIndentMandatory, hsn.hsn as hsnNo , im.itsBasedOn, im.itemGSTNature, im.hsnCode "
                                        + " FROM itemMast im \n"
                                        + " LEFT JOIN unitMast um ON um.code = im.purchaseUnit\n"
                                        + " left join unitMast as ums on im.purchaseUnit = ums.code\n "
                                        + " left join hsnMast as hsn on hsn.code = im.hsnCode\n "
                                        + " where im.name = :name  and (im.filterItemType = :filterItemType OR im.isCommon = 1) \n";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("name", fieldValue);
                                psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
                                rsTemp = psTemp.executeQuery();
                                if (rsTemp.next()) {
                                    if (DmShared.iNull(rsTemp.getString("itemCode")).longValue() == 0) {
//                                        throw new DmException(0, lineNoDisplay + "Item name not matched. [" + csvMdl.getItemName().trim() + "]");
                                        csvMdl.setItemName(DmShared.xNull(fieldValue));
                                    } else {
                                        csvMdl.setItemName(DmShared.xNull(fieldValue));
                                        csvMdl.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                                        csvMdl.setUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                                        csvMdl.setUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                                        csvMdl.setSku(DmShared.xNull(rsTemp.getString("sku")));
                                        csvMdl.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                                        csvMdl.setDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                                        csvMdl.setAllowedSkuDecimalPlaces(DmShared.xNull(rsTemp.getString("allowedSkuDecimalPlaces")));
                                        csvMdl.setConversionRate(DmShared.xNull(rsTemp.getString("conversionRate")));
                                        csvMdl.setQuantity(DmShared.xNull(rsTemp.getString("quantity")));
                                        csvMdl.setSkuQty(DmShared.xNull(rsTemp.getString("skuQty")));
                                    }
                                } else {
                                    csvMdl.setItemName(DmShared.xNull(fieldValue));
                                }
                                rsTemp.close();
                                psTemp.close();
                            }
                            if (!flag) {
                                break;
                            }
                        } else if (columnName.equalsIgnoreCase("rate")) {
                            flag = validateField(columnName, fieldValue, true, true, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setRate(DmShared.xNull(fieldValue));
                        } else if (columnName.equalsIgnoreCase("discountPer")) {
                            flag = validateField(columnName, fieldValue, false, true, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setDiscountPercentage(DmShared.xNull(fieldValue));
                        } else if (columnName.equalsIgnoreCase("remark")) {
                            flag = validateField(columnName, fieldValue, false, false, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setRemark(DmShared.xNull(fieldValue));
                        }
                    }

                    if (DmShared.vNull(csvMdl.getDiscountPercentage()).doubleValue() > 0) {
                        if (DmShared.vNull(csvMdl.getRate()).doubleValue() <= 0) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getItemName() + " not inserted. Rate is mandatory when discount given.");
                        }
                        if (DmShared.vNull(csvMdl.getDiscountPercentage()).doubleValue() > 100) {
                            flag = false;
                            handelFileLog.writeLine(lineNoDisplay + " " + csvMdl.getItemName() + " not inserted. Discount percentage cannot be greater than 100.");
                        }
                    }
                    if (flag) {
                        node = new GrdItemDetail_Mdl();
                        node.setSharedKeyFieldCode(DmShared.xNull(csvMdl.getItemCode()));
                        node.setSharedKeyFieldName(DmShared.xNull(csvMdl.getItemName()));
                        node.setQuantity(DmServer.vNull(csvMdl.getQuantity()));
                        node.setRate(DmServer.vNull(csvMdl.getRate()));
                        node.setRemark(DmShared.xNull(csvMdl.getRemark()));
                        node.setSkuQuantity(DmServer.vNull(csvMdl.getSkuQty()));
//                        node.setDecimalPlacesPurchaseUnit(DmShared.xNull(csvMdl.getDecimalPlaces()));
                        node.setConversionRate(DmServer.vNull(node.getConversionRate()));
                        node.setSkuDecimalPlaces(DmServer.vNull(csvMdl.getAllowedSkuDecimalPlaces()));
                        node.setDisplayUnit(DmShared.xNull(csvMdl.getUnitName()));
                        node.setPurchaseUnitCode(DmShared.xNull(csvMdl.getUnitCode()));
                        if (DmShared.vNull(csvMdl.getUnitCode()).longValue() != DmShared.vNull(csvMdl.getSku()).longValue()) {
                            node.setPurchaseUnitName(DmShared.xNull(csvMdl.getUnitName()) + " " + "=" + " " + DmServer.vNull(node.getConversionRate()) + " " + DmShared.xNull(csvMdl.getSkuName()));
                        } else {
                            node.setPurchaseUnitName(DmShared.xNull(csvMdl.getUnitName()));
                        }
                        /*==============================<< Get GST detail >>===============================*/
                        DmResult<List<GSTS_Mdl>> dmrIts = getGSTS(node.getSharedKeyFieldCode(), "", node.getItsBasedOn(), DmShared.xNull(location), "", false, cnTemp);
                        if (dmrIts != null) {
                            for (GSTS_Mdl iTS_Mdl : dmrIts.getData()) {
                                String taxPer = "";

                                taxPer = DmServer.vNull(iTS_Mdl.getAtRate(), "0.00");
                                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(isStateOrUt).intValue() == 1)) {
                                    node.setSgstUgstPer(DmShared.xNull(taxPer));
                                    node.setSgstPer(DmShared.xNull(taxPer));

                                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(isStateOrUt).intValue() == 1)) {
                                    node.setSgstUgstPer(DmShared.xNull(taxPer));
                                    node.setUgstPer(DmShared.xNull(taxPer));

                                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                                    node.setCgstPer(DmShared.xNull(taxPer));

                                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                                    node.setIgstPer(DmShared.xNull(taxPer));

                                }
                            }
                            node.setGstTypeCode(DmShared.xNull(dmrIts.getValue1()));
                            node.setGstTypeName(DmShared.xNull(dmrIts.getValue2()));
                        }
                        lstNode.add(node);
                        count++;
                    } else {
                        noOfBug++;
                    }
                }
                mrTemp.getListItemDetail().addAll(lstNode);
            }
            spBegin.commit();
            dmrTemp.setData(mrTemp);
            dmrTemp.setStatus(true);
            dmrTemp.setErrorNumber(noOfBug);
            cnTemp.close();
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                handelFileLog.writeLine(e.getMessage());
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            } catch (Exception ex) {
                dmrTemp.setStatus(false);
                dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            }
        } finally {
            try {
                if (rsTemp1 != null) {
                    rsTemp1.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
                if (handelFileLog != null) {
                    handelFileLog.close();
                }
            } catch (Exception ex) {

            }
        }
        return dmrTemp;
    }

    private static boolean validateField(String fieldName, String fieldValue, boolean isMandatory,
            boolean isNumeric, Boolean isNegative, String lineNoDisplay, DmHandelFile handelFile) {
        boolean flag = true;

        try {
            if (isMandatory) {
                if (isNumeric) {
                    if (DmShared.vNull(fieldValue).doubleValue() == 0) {
                        flag = false;
                        handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is mandatory.");
                    }
                } else if (DmShared.xNull(fieldValue).trim().equalsIgnoreCase("")) {
                    flag = false;
                    handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is mandatory.");
                }
            }

            if (isNumeric) {
                if (isNegative != null) {
                    if (isNegative) {
                        if (DmShared.vNull(fieldValue).doubleValue() > 0) {
                            flag = false;
                            handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " positive not allowed.");
                        }
                    } else {
                        if (DmShared.vNull(fieldValue).doubleValue() < 0) {
                            flag = false;
                            handelFile.writeLine(lineNoDisplay + " not inserted." + fieldName + " is negative not allowed.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            try {
                flag = false;
                handelFile.writeLine(e.getMessage());
            } catch (Exception ex) {
            }
        }

        return flag;
    }

    class CSV_Mdl {

        private String itemName, itemCode, unitCode, unitName, decimalPlaces, sku, skuName,
                allowedSkuDecimalPlaces, conversionRate, quantity, skuQty, rate, discountPercentage, remark;

        public CSV_Mdl() {
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String itemCode) {
            this.itemCode = itemCode;
        }

        public String getUnitCode() {
            return unitCode;
        }

        public void setUnitCode(String unitCode) {
            this.unitCode = unitCode;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getDecimalPlaces() {
            return decimalPlaces;
        }

        public void setDecimalPlaces(String decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public String getAllowedSkuDecimalPlaces() {
            return allowedSkuDecimalPlaces;
        }

        public void setAllowedSkuDecimalPlaces(String allowedSkuDecimalPlaces) {
            this.allowedSkuDecimalPlaces = allowedSkuDecimalPlaces;
        }

        public String getConversionRate() {
            return conversionRate;
        }

        public void setConversionRate(String conversionRate) {
            this.conversionRate = conversionRate;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getSkuQty() {
            return skuQty;
        }

        public void setSkuQty(String skuQty) {
            this.skuQty = skuQty;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDiscountPercentage() {
            return discountPercentage;
        }

        public void setDiscountPercentage(String discountPercentage) {
            this.discountPercentage = discountPercentage;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    private void savePriceList(Connection cnTemp, Quotation_Mdl mrTemp, boolean IsCommonPriceList) {
        DmPreparedStatement psTemp = null, psTemp1 = null, psTempPriceList = null;
        ResultSet rsTemp = null, rsTemp1 = null, rsTempPriceList = null;
        List<ItemPriceList_Mdl> lstItemPrice = new ArrayList<ItemPriceList_Mdl>();
        String sqlQuery, priceListCode = "", maxApplicableDate = "", applicableTo = "";
        try {
            sqlQuery = "select code,name from priceListMast where name=:name";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", vCat);
            rsTemp = psTemp.executeQuery();
            if (!rsTemp.next()) {
                priceListCode = DmServer.getSerialNoInteger(this, "priceListMast", cnTemp);
                sqlQuery = "insert into priceListMast (code,name,priceListType,preparedBy,preparedDt) \n"
                        + "values(:code,:name,1,:preparedBy,:preparedDt)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", priceListCode);
                psTemp.setString("name", vCat);
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
            } else {
                priceListCode = DmShared.xNull(rsTemp.getString("code"));
            }
            psTemp.close();
            rsTemp.close();

            if (IsCommonPriceList) {
                sqlQuery = "select code,name from siteMast";
            } else {
                sqlQuery = "select code,name from siteMast where code= " + DmServer.getSsnSiteCode(this);
            }
            psTempPriceList = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTempPriceList = psTempPriceList.executeQuery();

            while (rsTempPriceList.next()) {
                lstItemPrice = new ArrayList<ItemPriceList_Mdl>();

                List<GrdItemDetail_Mdl> listDetail = mrTemp.getListItemDetail();
                for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                    ItemPriceList_Mdl priceList;
                    priceList = new ItemPriceList_Mdl();
                    priceList.setPriceListCode("Price List Code");
                    priceList.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    priceList.setItemName(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()));
                    priceList.setUnitCode(DmShared.xNull(detail_Mdl.getPurchaseUnitCode()));
                    priceList.setUnitName(DmShared.xNull(detail_Mdl.getPurchaseUnitName()));
                    priceList.setRate(DmShared.xNull(detail_Mdl.getRate()));
                    priceList.setDiscountPercentage("0");
                    priceList.setDiscountAmount("0");
                    priceList.setDate(DmShared.dNull(mrTemp.getQuotationDate()));
                    priceList.setPrintSNo("0");
                    priceList.setEntryFrom((STEnum.PriceListEntryFrom.PRICELIST_MASTER));
                    priceList.setEntryFromNature(DmShared.iNull(STEnum.PriceListEntryFrom.NONE.toInt()).toString());
                    lstItemPrice.add(priceList);

                    sqlQuery = "select Max(applicableFrom) as applicableFrom,Min(applicableFrom) as applicableFromMin  "
                            + "from priceList "
                            + "where entryFrom = :entryFrom "
                            + "and site_Code = :site_Code "
                            + "and itemCode = :itemCode "
                            + "and priceListCode = :priceListCode";

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setInt("entryFrom", STEnum.PriceListEntryFrom.PRICELIST_MASTER.toInt());
                    psTemp.setString("site_Code", rsTempPriceList.getString("code"));
                    psTemp.setString("priceListCode", priceListCode);
                    psTemp.setString("itemCode", priceList.getItemCode());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
                    }
                    rsTemp.close();
                    psTemp.close();

                    if (!maxApplicableDate.equalsIgnoreCase("")) {
                        if (DmServer.convertDate(mrTemp.getQuotationDate()).getTime() < DmServer.convertDate(maxApplicableDate).getTime()) {
                            sqlQuery = "select applicableFrom,applicableTo "
                                    + "from priceList "
                                    + "where entryFrom = :entryFrom "
                                    + "and site_Code = :site_Code "
                                    + "and priceListCode = :priceListCode "
                                    + "and applicableFrom < :applicableFrom"
                                    + "group by applicableFrom,applicableTo "
                                    + "order by applicableFrom desc "
                                    + "offset 0 rows fetch next 1 rows only ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getQuotationDate()));
                            psTemp.setInt("entryFrom", STEnum.PriceListEntryFrom.PRICELIST_MASTER.toInt());
                            psTemp.setString("site_Code", rsTempPriceList.getString("code"));
                            psTemp.setString("priceListCode", priceListCode);
                            rsTemp = psTemp.executeQuery();
                            if (rsTemp.next()) {
                                applicableTo = DmShared.dNull(rsTemp.getDate("applicableTo"));
                                sqlQuery = "update priceList set "
                                        + "applicableTo = :applicableTo "
                                        + "where applicableFrom = :applicableFrom "
                                        + "and entryFrom = :entryFrom "
                                        + "and site_Code = :site_Code "
                                        + "and priceListCode = :priceListCode ";

                                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp1.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getQuotationDate()))));
                                psTemp1.setDate("applicableFrom", rsTemp.getDate("ApplicableFrom"));
                                psTemp1.setInt("entryFrom", STEnum.PriceListEntryFrom.PRICELIST_MASTER.toInt());
                                psTemp1.setString("site_Code", rsTempPriceList.getString("code"));
                                psTemp1.setString("priceListCode", priceListCode);
                                psTemp1.executeUpdate();
                                psTemp1.close();

                            } else {
                                sqlQuery = "select applicableFrom,applicableTo "
                                        + "FROM priceList "
                                        + "where applicableFrom > :applicableFrom "
                                        + "and entryFrom = :entryFrom "
                                        + "and site_Code = :site_Code "
                                        + "and priceListCode = :priceListCode"
                                        + "group by applicableFrom,applicableTo "
                                        + "order by applicableFrom asc offset 0 rows fetch next 1 rows only ";
                                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp1.setDate("applicableFrom", DmServer.convertDate(mrTemp.getQuotationDate()));
                                psTemp1.setInt("entryFrom", STEnum.PriceListEntryFrom.PRICELIST_MASTER.toInt());
                                psTemp1.setString("site_Code", rsTempPriceList.getString("code"));
                                psTemp1.setString("priceListCode", priceListCode);

                                rsTemp1 = psTemp1.executeQuery();
                                if (rsTemp1.next()) {
                                    applicableTo = DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(rsTemp1.getDate("applicableFrom")));
                                }
                                psTemp1.close();
                                rsTemp1.close();
                            }
                            psTemp.close();
                            rsTemp.close();
                        } else {
                            sqlQuery = "update priceList set "
                                    + "applicableTo = :applicableTo "
                                    + "where applicableFrom = :applicableFrom "
                                    + "and entryFrom = :entryFrom "
                                    + "and site_Code = :site_Code "
                                    + "and itemCode = :itemCode "
                                    + "and priceListCode = :priceListCode";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setDate("applicableFrom", DmServer.convertDate(maxApplicableDate));
                            psTemp.setDate("applicableTo", DmServer.convertDate(DmServer.addDate(Calendar.DATE, -1, DmShared.dNull(mrTemp.getQuotationDate()))));
                            psTemp.setInt("entryFrom", STEnum.PriceListEntryFrom.PRICELIST_MASTER.toInt());
                            psTemp.setString("site_Code", rsTempPriceList.getString("code"));
                            psTemp.setString("priceListCode", priceListCode);
                            psTemp.setString("itemCode", priceList.getItemCode());
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }

                    sqlQuery = "delete from priceList "
                            + "where applicableFrom = :applicableFrom "
                            + "and entryFrom = :entryFrom "
                            + "and site_Code = :site_Code "
                            + "and priceListCode = :priceListCode "
                            + "and itemCode = :itemCode "
                            + "and unitCode = :unitCode ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setDate("applicableFrom", DmServer.convertDate(mrTemp.getQuotationDate()));
                    psTemp.setString("entryFrom", String.valueOf(STEnum.PriceListEntryFrom.PRICELIST_MASTER.toInt()));
                    psTemp.setString("site_Code", rsTempPriceList.getString("code"));
                    psTemp.setString("priceListCode", priceListCode);
                    psTemp.setString("itemCode", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("unitCode", DmShared.xNull(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                }
                if (lstItemPrice.size() > 0) {
                    STServer.savePriceListItem(mrTemp.getQuotationDate(), STEnum.PriceListEntryFrom.PRICELIST_MASTER, lstItemPrice.get(0).getSharedKeyFieldCode(), priceListCode, rsTempPriceList.getString("code"), lstItemPrice, cnTemp);
                }
            }
            psTempPriceList.close();
            rsTempPriceList.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    }

    @Override
    public DmResult<String> exportCSV(String docid) {
        DmResult<String> dmrTemp = new DmResult<String>();
        String downloadFileName = "", downloadDirectoryPath = "", fileFullPath = "", sqlQuery = "", sessionId;
        String COMMA_DELIMITER = ",", NEW_LINE_SEPARATOR = System.lineSeparator(), lineData = "", fileHeader = "";
        FileWriter fileWriter = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        try {

            downloadFileName = "PriceList.csv";
            sessionId = ((DmRequestWrapper) this.getLocalRequest()).getSession().getId();
            downloadDirectoryPath = DmServer.Path.sessions + File.separatorChar + sessionId + File.separatorChar + "downloads";

            new File(downloadDirectoryPath).mkdirs();
            fileFullPath = downloadDirectoryPath + File.separatorChar + downloadFileName;

            fileWriter = new FileWriter(fileFullPath);

            fileHeader = "itemName,rate,discountPer,remark";
            fileWriter.append(fileHeader);
            fileWriter.append(NEW_LINE_SEPARATOR);

            if (DmShared.iNull(docid).toString().equalsIgnoreCase("")) {
                sqlQuery = "SELECT top 1 name as itemName,10 as rate,0.00 as discountPer,'xyz' as remark from itemMast";

            } else {
                sqlQuery = "select   im.name as itemName, qd.remark,  	qd.oh_at_Discount,	\n"
                        + "qd.rate\n"
                        + "from quotationDetail as qd 		\n"
                        + "left join itemMast as im on qd.itemCode = im.code 		\n"
                        + "left join unitMast as um on qd.unitCode = um.code          \n"
                        + "LEFT JOIN unitMast ums ON ums.code = im.sku 		\n"
                        + "where qd.docId =:docId       \n"
                        + "order by qd.v_SNo ";
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docid).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                //                String medName = rsTemp.getString("itemName");
//                if (medName.contains("\"")) {
//                    medName = medName.replace("\"", "");
//                }
                String medName = rsTemp.getString("itemName");
                String backSlash = "";
                if (medName.contains("\"")) {
                    backSlash = "\"";
                }

                // lineData = "\"" + DmShared.xNull(rsTemp.getString("itemName")) + backSlash + "\"" + COMMA_DELIMITER;
                lineData = "\"" + DmShared.xNull(rsTemp.getString("itemName")) + backSlash + "\"" + COMMA_DELIMITER;

                lineData += "\"" + DmShared.xNull(rsTemp.getString("rate")).trim() + "\"" + COMMA_DELIMITER;
                //  lineData += "\"" + DmShared.xNull(rsTemp.getString("mrp")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("oh_at_Discount")).trim() + "\"" + COMMA_DELIMITER;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("remark")).trim() + "\"";
                fileWriter.append(lineData);
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            rsTemp.close();
            psTemp.close();

            fileWriter.flush();
            fileWriter.close();
            fileWriter = null;

            dmrTemp.setData(downloadFileName);
            dmrTemp.setValue1(downloadDirectoryPath);
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
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }
}
