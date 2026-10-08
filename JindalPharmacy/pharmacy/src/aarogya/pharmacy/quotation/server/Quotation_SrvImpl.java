package aarogya.pharmacy.quotation.server;

import aarogya.pharmacy.quotation.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmToolkit.overHead.server.OverHead_Server;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.server.OverHeadLine_Server;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import aarogya.pharmacy.quotation.shared.Quotation_Srv;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.server.DmCsvReader;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.dmImportData.server.DmImportData_Server;
import java.io.File;

import aarogya.selfToolkit.stStatic.shared.ItemPriceList_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.PriceListEntryFrom;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import java.io.FileWriter;
import java.util.Calendar;

public class Quotation_SrvImpl extends DmRSSPrivate implements Quotation_Srv {

    private String vCat = "'" + new ProjectVoucherCategory().PHARMACY_QUOTATION + "'";
    private final String voucherTypeFieldName = "q.V_Type", voucherDateFieldName = "q.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery = "", sqlCondition = "";
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

            sqlQuery += " order by q.v_Date desc , vt.short_Name, q.v_No desc, q.recId  ";
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

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = " select	q.docId, q.recId, q.v_No, q.recIdPrefix, q.v_Date, q.vendorRefNo, q.vendorRefDate, q.enquiryRefNo, q.enquiryRefDate, q.u_Name, q.preparedBy, "
                    + " q.locationCode,q.guiConfigCode, q.v_Type, vt.description, q.subCode as vendorCode, sgv.displayName as vendorName, en.recId as enquiryNo, q.oh_Amt_Gross"
                    + " from quotation as q "
                    + " left join voucher_Type as vt on q.v_Type = vt.v_Type "
                    + " left join subGroup as sgv on q.subCode = sgv.subCode "
                    + "         LEFT JOIN ("
                    + "             select docid, enquiryDocid from quotationDetail where enquiryDocId is NOT NULL "
                    + "             group by docid,enquiryDocid "
                    + "         )vd ON vd.docId = q.docId"
                    + "         left join enquiry as en on vd.enquiryDocId = en.docId 	"
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
                mrTemp.setVendorTag(DmShared.xNull(rsTemp.getString("vendorCode")));
                mrTemp.setVendorText(DmShared.xNull(rsTemp.getString("vendorName")));
                mrTemp.setVendorRefDate(DmShared.dNull(rsTemp.getDate("vendorRefDate")));
                mrTemp.setVendorRefNo(DmShared.xNull(rsTemp.getString("vendorRefNo")));
                mrTemp.setLocationCode(DmShared.xNull(rsTemp.getString("locationCode")));
                mrTemp.setEnquiryText(DmShared.xNull(rsTemp.getString("enquiryNo")));
                mrTemp.setEnquiryRefNo(DmShared.xNull(rsTemp.getString("enquiryRefNo")));
                mrTemp.setEnquiryRefDate(DmShared.dNull(rsTemp.getDate("enquiryRefDate")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setGrossAmount(DmServer.vNull(rsTemp.getString("oh_Amt_Gross"), "0.00"));
                mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getObjRecId().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getObjRecId().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = "SELECT  qd.itemCode, im.name as itemName, im.itsBasedOn, qd.quantity, qd.unitCode, um.unit as unitName, qd.deliveryDate, qd.remark,  "
                    + "		qd.rate, qd.oh_Amt_Gross as amount, qd.v_SNo ,um.decimalPlaces,qd.enquiryDocid,qd.enquiryV_SNo,qd.mrp, "
                    + "         im.sku ,ums.unit as skuName,ums.decimalPlaces as allowedSkuDecimalPlaces,qd.skuQuantity,cast( (qd.skuQuantity /  qd.quantity) as decimal(18,7)) as conversionRate "
                    + "		FROM quotationDetail AS qd "
                    + "		LEFT JOIN itemMast AS im on qd.itemCode = im.code "
                    + "		LEFT JOIN unitMast AS um on qd.unitCode = um.code "
                    + "         LEFT JOIN unitMast ums ON ums.code = im.sku "
                    + "		WHERE qd.docId = :docId "
                    + "         ORDER BY qd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp")));
                node.setAmount(DmServer.vNull(rsTemp.getString("amount")));
                node.setDeliveryDate(DmShared.dNull(rsTemp.getDate("deliveryDate")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setvSNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setEnquiryDocId(DmShared.xNull(rsTemp.getString("enquiryDocid")));
                node.setEnquiryV_SNo(DmShared.xNull(rsTemp.getString("enquiryV_SNo")));
                node.setSkuQuantity(DmServer.vNull(rsTemp.getString("skuQuantity")));
                node.setDecimalPlacesPurchaseUnit(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                node.setConversionRate((DmServer.vNull(rsTemp.getString("conversionRate"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue()))));
                node.setConversionRate(DmServer.vNull(node.getConversionRate()));
                node.setSkuDecimalPlaces(DmServer.vNull(rsTemp.getString("allowedSkuDecimalPlaces")));
                node.setDisplayUnit(DmShared.xNull(rsTemp.getString("unitName")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                if (DmShared.vNull(rsTemp.getString("unitCode")).longValue() != DmShared.vNull(rsTemp.getString("sku")).longValue()) {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")) + " " + "=" + " " + DmServer.vNull(node.getConversionRate()) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                } else {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unitName")));
                }
                /*==============================<< Get OverheadLine detail >>===============================*/
                node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), DmShared.iNull(strKey).toString(), DmShared.xNull(rsTemp.getString("v_SNo")), cnTemp));

                /*==============================<< Get Indent detail >>===============================*/
                DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(true, DmShared.xNull(strKey), DmShared.xNull(rsTemp.getString("v_SNo")), null, "", cnTemp);
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

            /*==============================<< Get Overhead detail >>===============================*/
            mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), strKey, cnTemp));

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
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, keyCode, DmServer.getSsnSiteCode(this), cnTemp);

            /*==============================<< Delete record from quotationIndentDetail >>===============================*/
            sqlQuery = " Delete From quotationIndentDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from quotationDetail >>===============================*/
            sqlQuery = " Delete From quotationDetail Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from quotation >>===============================*/
            sqlQuery = " Delete From quotation Where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

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
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            Quotation_Vld.chkSave(mrTemp);
            OverHead_Shared.chkGrid(mrTemp.getOverHeadList());
            OverHeadLine_Shared.chkGrid(mrTemp.getListItemDetail());
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                STServer.chkIfDocumentUsed("quotationDocId", "pOrderDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setObjRecId(DmServer.getRecId("quotation", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getObjRecId(), mrTemp.getQuotationDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getObjRecId().getRecIdValue()));
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

            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), siteCode, cnTemp);

            /*==============================<< Insert record into quotation >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into quotation (docId, v_Type, v_No, v_Date, v_Prefix, recId, "
                        + " subCode, vendorRefNo, vendorRefDate, enquiryRefNo, enquiryRefDate, "
                        + " locationCode ,site_Code, transferDt, preparedBy, u_EntDt,recIdPrefix, guiConfigCode) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId, "
                        + " :subCode, :vendorRefNo, :vendorRefDate, :enquiryRefNo, :enquiryRefDate, "
                        + " :locationCode ,:site_Code, :transferDt, :preparedBy, :uEntDt,:recIdPrefix, :guiConfigCode) ";
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
                psTemp.setString("enquiryRefNo", DmShared.foriegnKey(mrTemp.getEnquiryRefNo()));
                psTemp.setDate("enquiryRefDate", DmServer.convertDate(mrTemp.getEnquiryRefDate()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getObjRecId().getPrefix()));
                psTemp.setString("locationCode", DmShared.xNull(mrTemp.getLocationCode()));
                psTemp.setString("site_Code", siteCode);
                psTemp.setString("transferDt", null);
                psTemp.setString("preparedBy", userName);
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
            } else {
                sqlQuery = " update quotation Set "
                        + " subCode = :subCode, "
                        + " vendorRefNo = :vendorRefNo, "
                        + " vendorRefDate = :vendorRefDate, "
                        + " enquiryRefNo = :enquiryRefNo, "
                        + " enquiryRefDate = :enquiryRefDate, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " u_EntDt = :uEntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getVendorTag()));
                psTemp.setString("vendorRefNo", DmShared.xNull(mrTemp.getVendorRefNo()));
                psTemp.setDate("vendorRefDate", DmServer.convertDate(mrTemp.getVendorRefDate()));
                psTemp.setString("enquiryRefNo", DmShared.foriegnKey(mrTemp.getEnquiryRefNo()));
                psTemp.setDate("enquiryRefDate", DmServer.convertDate(mrTemp.getEnquiryRefDate()));
                psTemp.setString("transferDt", null);
                psTemp.setString("u_Name", userName);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete records from quotationDetail >>====================================*/
            sqlQuery = "Delete From quotationDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into quotationDetail >>====================================*/
            int v_SNo = 0;
            List<GrdItemDetail_Mdl> listDetail = mrTemp.getListItemDetail();
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    sqlQuery = " insert into quotationDetail(docId, v_SNo, itemCode, quantity, rate, unitCode, deliveryDate, remark,enquiryDocid,enquiryV_SNo,skuQuantity,mrp)"
                            + " values (:docId, :v_SNo, :itemCode, :quantity, :rate, :unitCode, :deliveryDate, :remark,:enquiryDocid,:enquiryV_SNo,:skuQuantity,:mrp) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(++v_SNo).toString());
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.setDate("deliveryDate", DmServer.convertDate(detail_Mdl.getDeliveryDate()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("enquiryDocid", DmShared.foriegnKey(detail_Mdl.getEnquiryDocId()));
                    psTemp.setString("enquiryV_SNo", DmShared.foriegnKey(detail_Mdl.getEnquiryV_SNo()));
                    psTemp.setString("skuQuantity", DmShared.xNull(detail_Mdl.getSkuQuantity()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp(), "0.00"));
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

            /*==============================<< Delete records from quotationIndentDetail  >>====================================*/
            sqlQuery = "Delete From quotationIndentDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into quotationIndentDetail  >>====================================*/
            int v_No = 0;
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                v_No += 1;
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    for (GrdIndentDetail_Mdl indentDetail_Mdl : detail_Mdl.getListIndentDetail()) {
                        if (!DmShared.xNull(indentDetail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                            sqlQuery = " insert into quotationIndentDetail(docId, v_SNo, indentDocId, itemCode,indentV_SNo)"
                                    + " values (:docId, :v_SNo, :indentDocId, :itemCode,:indentV_SNo) ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                            psTemp.setString("v_SNo", DmShared.iNull(v_No).toString());
                            psTemp.setString("indentDocId", DmShared.foriegnKey(indentDetail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("indentV_SNo", DmShared.foriegnKey(indentDetail_Mdl.getIndentVSNo()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }

            /*==============================<< Insert records of Overhead and OverheadLine  >>====================================*/
            OverHeadLine_Server.save(listDetail, "docId", mrTemp.getSharedKeyFieldCode(), "v_SNo", siteCode, cnTemp);
            OverHead_Server.save(mrTemp.getOverHeadList(), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getObjRecId(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getQuotationDate(), "", "", "", "", "", true, false, cnTemp);

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
        String sqlQuery = "", sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	q.docId, q.recId, q.v_Date, q.vendorRefNo, q.vendorRefDate, q.enquiryRefNo,q.v_No,  "
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
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = " + vCat
                    + " AND vt.isActive = 1");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
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
    public DmResult<DefaultValue_Mdl> getDefaultValues() {
        DmResult<DefaultValue_Mdl> dmrTemp = new DmResult();
        DefaultValue_Mdl defaultValue = new DefaultValue_Mdl();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get default voucher+_Type >>===============================*/
            sqlQuery = " SELECT TOP 1 "
                    + "   vt.v_Type AS code"
                    + " , vt.description AS name"
                    + " , vt.guiTemplateCode"
                    + " FROM voucher_Type vt"
                    + " WHERE vt.category = " + vCat
                    + " AND vt.isActive = 1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setGuiTemplateCode(DmShared.xNull(rsTemp.getString("guiTemplateCode")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT e.IsCommonPricelistForSite,e.updatePriceListFromQuotation \n"
                    + "FROM enviro e\n"
                    + "WHERE e.site_Code = :site_Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setIsCommonPriceList(DmShared.iNull(rsTemp.getString("IsCommonPricelistForSite")).intValue() == 1);
                defaultValue.setUpdatePriceListFromQuotation(DmShared.iNull(rsTemp.getString("updatePriceListFromQuotation")).intValue() == 1);
            }
            rsTemp.close();
            psTemp.close();

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
                defaultValue.setSiteGstin(DmShared.xNull(rsTemp.getString("siteGstin")));
                defaultValue.setSiteGstinDate(DmShared.dNull(rsTemp.getDate("siteGstinDate")));
                defaultValue.setSiteRegistrationType(DmShared.xNull(rsTemp.getString("siteRegistrationType")));
                defaultValue.setSiteStateCode(DmShared.xNull(rsTemp.getString("siteStateCode")));
                defaultValue.setIsSiteStateUT(DmShared.xNull(rsTemp.getString("isSiteStateUT")));
            }
            rsTemp.close();
            psTemp.close();

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);

            dmrTemp.setData(defaultValue);
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
    public DmResultPageing<List<TxtHEnquiry_Mdl>> getTxtHEnquiryNo(String vendorCode, String v_Date, String quotationDocId, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHEnquiry_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHEnquiry_Mdl> lstTemp = new ArrayList<TxtHEnquiry_Mdl>();
        TxtHEnquiry_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String sqlQuery, condition = "", conditionEdit = "";

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.dNull(v_Date).trim().equals("")) {
                condition += " AND e.v_Date <= :v_Date";
            } else {
                condition += " AND 1 = 2";
            }
            if (DmShared.iNull(quotationDocId).longValue() != 0) {
                conditionEdit += " WHERE q.docId != :quotationDocId";
            }
            sqlQuery = "select	e.docId as code, e.recId as name, e.v_Date as enquiryDate, e.referenceNo as refNo "
                    + "		from enquiry as e "
                    + "		left join enquiryVendorDetail as evd on e.docId = evd.docId "
                    + "		left join voucher_Type as vt on e.v_Type=vt.v_Type"
                    + " left join ("
                    + "             SELECT q.subCode, qd.enquiryDocId "
                    + "             FROM quotation q "
                    + "             INNER JOIN quotationDetail qd ON qd.docId = q.docid "
                    + conditionEdit
                    + "             GROUP BY q.subCode, qd.enquiryDocId "
                    + "         ) vEd on vEd.enquiryDocId = e.docid AND vEd.subCode = evd.subCode "
                    + "		where evd.isStatus = 0 and isNull(e.approvedBy,'') <> '' and evd.subCode = :subCode "
                    + "         and vEd.enquiryDocId is null and vt.category='" + new ProjectVoucherCategory().PHARMACY_ENQUIRY + "'"
                    + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("subCode", DmShared.iNull(vendorCode).toString());
            if (!DmShared.dNull(v_Date).trim().equals("")) {
                sqlWithParam.setDate("v_Date", DmServer.convertDate(v_Date));
            }
            if (DmShared.iNull(quotationDocId).longValue() != 0) {
                sqlWithParam.setString("quotationDocId", DmShared.iNull(quotationDocId).toString());
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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String location, String billDate, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItemName_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItemName_Mdl> lstTemp = new ArrayList<TxtHItemName_Mdl>();
        TxtHItemName_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" select im.code, im.name, im.manualCode, im.itsBasedOn , im.sku, um.unit as skuName, "
                    + " IsNull(um.decimalPlaces,0) as decimalPlaces,IsNull(ump.decimalPlaces,0) as decimalPlacesPurchaseUnit, "
                    + " ump.unit as purchaseUnitName,im.purchaseUnit as purchaseUnitCode,im.purchaseUnitConversionRate,im.isIndentMandatory "
                    + " from itemMast as im"
                    + " left join itemSiteList as sl on sl.code = im.code"
                    + " left join unitmast as um on im.sku = um.code"
                    + " left join unitmast as ump on im.purchaseUnit = ump.code"
                    + " where im.IsActive = 1"
                    + " and (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " and sl.site_Code = :site_Code");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                listNode.setSku(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setUnit(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setDisplayUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setDecimalPlacesPurchaseUnit(DmShared.xNull(rsTemp.getString("decimalPlacesPurchaseUnit")));
                listNode.setDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                listNode.setIsIndentMandatory(DmShared.xNull(rsTemp.getString("isIndentMandatory")));
                listNode.setConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                if (DmShared.vNull(rsTemp.getString("purchaseUnitCode")).longValue() != DmShared.vNull(rsTemp.getString("sku")).longValue()) {
                    listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " " + "=" + " " + listNode.getConversionRate() + " " + DmShared.xNull(rsTemp.getString("skuName")));
                } else {
                    listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                }

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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT"
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , c.cityName"
                    + " , c.stateCode"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
                listNode.setStateCode(DmShared.xNull(rsTemp.getString("stateCode")));
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
    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(List<KeyFieldExtended> keyFields, String itemCode) {
        DmResult<List<GrdIndentDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        dmrTemp.setData(null);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get Indent detail (via list) >>===============================*/
            DmResult<List<GrdIndentDetail_Mdl>> resultIndentDetail = getIndentDetail(false, "", "", keyFields, DmShared.xNull(itemCode), cnTemp);
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
    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String enquiryDocId, List<KeyFieldExtended> keyFields) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.vNull(enquiryDocId).doubleValue() != 0) {
                /*==============================<< Get Item detail (via enquiryDocId) >>===============================*/
                sqlQuery = "SELECT  qd.itemCode, im.name as itemName,im.itsBasedOn \n"
                        + " ,qd.skuQuantity as quantity\n"
                        + " ,qd.unitCode as purchaseUnitCode\n"
                        + " ,um.unit as purchaseUnitName\n"
                        + " ,qd.v_SNo\n"
                        + " ,qd.skuQuantity \n"
                        + " ,ump.decimalPlaces as allowedDecimalPlaces \n"
                        + " ,um.decimalPlaces as allowedSkuDecimalPlaces,ums.unit as unitName \n"
                        + " ,ums.code as unitCode,qd.docid as enquiryDocId,qd.V_SNo as enquiryV_SNo\n"
                        + " ,im.purchaseUnitConversionRate\n"
                        + " FROM enquiryDetail AS qd \n"
                        + " LEFT JOIN itemMast AS im on qd.itemCode = im.code \n"
                        + " LEFT JOIN itemColourMast icm ON icm.code = qd.colourCode \n"
                        + " LEFT JOIN itemSizeMast ism ON ism.code = qd.sizeCode   \n"
                        + " LEFT JOIN unitMast as um on qd.unitCode = um.code \n"
                        + " LEFT JOIN unitMast as ums on im.sku = ums.code \n"
                        + " LEFT JOIN unitMast as ump on im.purchaseUnit = ump.code\n"
                        + " WHERE qd.docId = :docId \n"
                        + " ORDER BY im.name ";
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
                sqlQuery = " SELECT  id.itemCode, MAX(im.name) AS itemName ,MAX(im.itsBasedOn) AS itsBasedOn ,MAX(id.docId) AS indDocid,max(id.v_SNo) as indv_SNo \n"
                        + " , SUM(id.aprQty) AS quantity, max(im.sku) as unitCode\n"
                        + " , MAX(um.unit) AS unitName \n"
                        + " , MAX(im.purchaseUnit) AS purchaseUnitCode,max(ump.unit) AS purchaseUnitName \n"
                        + " , MAX(ump.decimalPlaces) AS allowedDecimalPlaces \n"
                        + " , MAX(um.decimalPlaces) AS allowedSkuDecimalPlaces \n"
                        + " , MAX(im.purchaseUnitConversionRate) AS purchaseUnitConversionRate \n"
                        + "         FROM indent AS i  \n"
                        + "         LEFT JOIN indentDetail AS id on i.docId = id.docId  \n"
                        + "         LEFT JOIN itemMast AS im on id.itemCode = im.code \n"
                        + "         LEFT JOIN unitMast AS um on im.sku = um.code  \n"
                        + "         LEFT JOIN unitMast AS ump on im.purchaseUnit = ump.code  \n"
                        + "         WHERE id.isStatus = 0 and isNull(i.approvedBy,'') <> ''  \n"
                        + " AND " + condition
                        + "         GROUP BY id.itemCode,id.colourCode,id.sizeCode"
                        + "         ORDER BY itemName ";
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
                node.setItsBasedOn(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setDisplayUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setDecimalPlacesPurchaseUnit(DmShared.xNull(rsTemp.getString("allowedDecimalPlaces")));
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
                if (DmShared.vNull(enquiryDocId).doubleValue() == 0) {
                    /*==============================<< Get Indent detail (via list & ItemCode ) >>===============================*/
                    resultIndentDetail = getIndentDetail(false, "", "", keyFields, DmShared.xNull(rsTemp.getString("itemCode")), cnTemp);
                } else {
                    /*==============================<< Get Indent detail (via enquiryDocId & v_SNo) >>===============================*/
                    resultIndentDetail = getIndentDetail(false, DmShared.xNull(enquiryDocId), DmShared.iNull(rsTemp.getString("v_SNo")).toString(), null, DmShared.xNull(rsTemp.getString("itemCode")), cnTemp);
                }
                if (resultIndentDetail.isStatus()) {
                    node.getListIndentDetail().addAll(resultIndentDetail.getData());
                } else {
                    throw new DmException(0, "No record found.");
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

    public DmResult<List<GrdIndentDetail_Mdl>> getIndentDetail(boolean isMoveRec, String docId, String v_SNo, List<KeyFieldExtended> keyFields, String itemCode, Connection cnTemp) {
        DmResult<List<GrdIndentDetail_Mdl>> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<GrdIndentDetail_Mdl> lstNode = new ArrayList<>();
        GrdIndentDetail_Mdl node;
        dmrTemp.setData(lstNode);
        String condition = "";

        try {

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
                        + "         order by qid.v_SNo ";
            } else {
                /*==============================<< Get Indent detail (via list & ItemCode ) >>===============================*/
                for (KeyFieldExtended field : keyFields) {
                    if (DmShared.vNull(field.getSharedKeyFieldCode()).doubleValue() != 0) {
                        if (!condition.trim().equalsIgnoreCase("")) {
                            condition += " or ";
                        }
                        condition += "  (id.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                                + " and  id.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "' "
                                + " and id.itemCode = :itemCode) ";
                    }
                }
                sqlQuery = "select  id.docId as indentDocId, id.v_SNo, i.recId as indentNo, i.v_Date as indentDate,  "
                        + "         id.aprQty as quantity, id.reqDate, im.sku, um.unit, id.remark ,"
                        + "         im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName,"
                        + "         ump.decimalPlaces as allowedDecimalPlaces,"
                        + "         um.decimalPlaces as allowedSkuDecimalPlaces,"
                        + "         im.purchaseUnitConversionRate as purchaseUnitConversionRate,id.mrp "
                        + "         from indent as i "
                        + "         left join indentDetail as id on i.docId = id.docId "
                        + "         left join itemMast as im on id.itemCode = im.code "
                        + "         left join unitMast as um on im.sku = um.code "
                        + "         left join unitMast as ump on im.purchaseUnit = ump.code   "
                        + "         inner join voucher_Type as vt on vt.v_Type = i.V_type "
                        + "         where id.isStatus = 0 and isNull(i.approvedBy,'') <> ''"
                        + "         and vt.category = '" + new ProjectVoucherCategory().PHARMACY_INDENT + "' "
                        + " and  " + condition
                        + "         order by id.v_SNo ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.vNull(docId).doubleValue() != 0) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("v_SNo", DmShared.iNull(v_SNo).toString());
            } else {
                psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdIndentDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("indentDocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indentNo")));
                node.setIndentVSNo(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
                node.setIndentDate(DmShared.dNull(rsTemp.getDate("indentDate")));
                node.setRequiredDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setIndentSKU(DmShared.xNull(rsTemp.getString("unit")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setIndentQty(DmServer.vNull(rsTemp.getString("quantity")));
                node.setIndentQtyInPurchaseUnit(DmServer.vNull(rsTemp.getString("quantity")));
                if (DmShared.vNull(docId).doubleValue() == 0) {
                    node.setIndentMrp(DmServer.vNull(rsTemp.getString("mrp")));
                }
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
    public DmResult<Quotation_Mdl> getListFromCSV(String serverDirectory, String serverFileName, String location) {
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
                                        + " FROM itemMast im \n"
                                        + " LEFT JOIN unitMast um ON um.code = im.purchaseUnit\n"
                                        + " left join unitMast as ums on im.purchaseUnit = ums.code\n "
                                        + " where name = :name  and (im.filterItemType = :filterItemType OR im.isCommon = 1) \n";
                                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                                psTemp.setString("name", fieldValue);
                                psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
                                    csvMdl.setItemName(DmShared.xNull(fieldValue));;
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
                        } else if (columnName.equalsIgnoreCase("mrp")) {
                            flag = validateField(columnName, fieldValue, false, true, false, lineNoDisplay, handelFileLog);
                            if (!flag) {
                                break;
                            }
                            csvMdl.setMrp(DmShared.xNull(fieldValue));
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
                        node.setMrp(DmServer.vNull(csvMdl.getMrp()));
                        node.getOverHeadITSList().addAll(getItsForFile(node.getSharedKeyFieldCode(), node.getRate(), location, cnTemp));
                        node.setDiscountper(DmServer.vNull(csvMdl.getDiscountPercentage()));
                        node.setRemark(DmShared.xNull(csvMdl.getRemark()));
                        node.setSkuQuantity(DmServer.vNull(csvMdl.getSkuQty()));
                        node.setDecimalPlacesPurchaseUnit(DmShared.xNull(csvMdl.getDecimalPlaces()));
//                        node.setConversionRate((DmServer.vNull(csvMdl.getConversionRate(), DmShared.getNumberFormat(DmShared.iNull(csvMdl.getAllowedSkuDecimalPlaces()).intValue()))));
                        node.setConversionRate(DmServer.vNull(node.getConversionRate()));
                        node.setSkuDecimalPlaces(DmServer.vNull(csvMdl.getAllowedSkuDecimalPlaces()));
                        node.setDisplayUnit(DmShared.xNull(csvMdl.getUnitName()));
                        node.setPurchaseUnitCode(DmShared.xNull(csvMdl.getUnitCode()));
                        if (DmShared.vNull(csvMdl.getUnitCode()).longValue() != DmShared.vNull(csvMdl.getSku()).longValue()) {
                            node.setPurchaseUnitName(DmShared.xNull(csvMdl.getUnitName()) + " " + "=" + " " + DmServer.vNull(node.getConversionRate()) + " " + DmShared.xNull(csvMdl.getSkuName()));
                        } else {
                            node.setPurchaseUnitName(DmShared.xNull(csvMdl.getUnitName()));
                        }
                        /*==============================<< Get OverheadLine detail >>===============================*/
                        node.setOHLList(OverHeadLine_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), "", "", cnTemp));
                        lstNode.add(node);
                        count++;
                    } else {
                        noOfBug++;
                    }
                }
                mrTemp.getListItemDetail().addAll(lstNode);

                /*==============================<< Get Overhead detail >>===============================*/
                mrTemp.getOverHeadList().addAll(OverHead_Server.getOverHead("", "", DmServer.getSsnSiteCode(this), "", cnTemp));
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
                allowedSkuDecimalPlaces, conversionRate, quantity, skuQty, rate, discountPercentage, mrp, remark;

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

        public String getMrp() {
            return mrp;
        }

        public void setMrp(String mrp) {
            this.mrp = mrp;
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
                    priceList.setMrp(DmShared.xNull(detail_Mdl.getMrp()));
                    priceList.setDiscountPercentage("0");
                    priceList.setDiscountAmount("0");
                    priceList.setDate(DmShared.dNull(mrTemp.getQuotationDate()));
                    priceList.setPrintSNo("0");
                    priceList.setEntryFrom((PriceListEntryFrom.PRICELIST_MASTER));
                    priceList.setEntryFromNature(DmShared.iNull(PriceListEntryFrom.NONE.toInt()).toString());
                    lstItemPrice.add(priceList);

                    /**
                     * ******************************Item wise Appliacable
                     * Date********************************
                     */
//                    lstItemPrice = new ArrayList<ItemPriceList_Mdl>();
                    sqlQuery = "select Max(applicableFrom) as applicableFrom,Min(applicableFrom) as applicableFromMin  "
                            + "from priceList "
                            + "where entryFrom = :entryFrom "
                            + "and site_Code = :site_Code "
                            + "and itemCode = :itemCode "
                            + "and priceListCode = :priceListCode";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
                    psTemp.setString("site_Code", rsTempPriceList.getString("code"));
                    psTemp.setString("priceListCode", priceListCode);
                    psTemp.setString("itemCode", priceList.getItemCode());
                    rsTemp = psTemp.executeQuery();
                    if (rsTemp.next()) {
                        maxApplicableDate = DmShared.dNull(rsTemp.getDate("applicableFrom"));
                    }
                    rsTemp.close();
                    psTemp.close();
                    /**
                     * ******************************Item wise Appliacabl
                     * Date********************************
                     */

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
                            psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
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
                                psTemp1.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
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
                                psTemp1.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
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
                            psTemp.setInt("entryFrom", PriceListEntryFrom.PRICELIST_MASTER.toInt());
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
                    psTemp.setString("entryFrom", String.valueOf(PriceListEntryFrom.PRICELIST_MASTER.toInt()));
                    psTemp.setString("site_Code", rsTempPriceList.getString("code"));
                    psTemp.setString("priceListCode", priceListCode);
                    psTemp.setString("itemCode", DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("unitCode", DmShared.xNull(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                }
                if (lstItemPrice.size() > 0) {
                    STServer.savePriceListItem(mrTemp.getQuotationDate(), PriceListEntryFrom.PRICELIST_MASTER, lstItemPrice.get(0).getItemCode(), priceListCode, rsTempPriceList.getString("code"), lstItemPrice, cnTemp);
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

            fileHeader = "itemName,rate,mrp,discountPer,remark";
            fileWriter.append(fileHeader);
            fileWriter.append(NEW_LINE_SEPARATOR);

            if (DmShared.iNull(docid).toString().equalsIgnoreCase("")) {
                sqlQuery = "SELECT top 1 name as itemName,10 as rate,0.00 as discountPer,'xyz' as remark from itemMast";

            } else {

                sqlQuery = "select   im.name as itemName, qd.remark,  	qd.oh_at_Discount,	\n"
                        + "qd.rate, qd.mrp\n"
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
                lineData += "\"" + DmShared.xNull(rsTemp.getString("mrp")).trim() + "\"" + COMMA_DELIMITER;
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

    @Override
    public DmResult<String> getCDConfiguration(String SupplierCode, String medicineCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        String mrTemp = "";
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT TOP 1  cdc.discPer \n"
                    + "FROM CDConfiguration cdc "
                    + "LEFT JOIN itemMast im ON im.itemMakeCode=cdc.companyCode\n"
                    + "WHERE \n"
                    + "cdc.supplierCode=:supplierCode  \n"
                    + "AND im.code=:medicineCode \n"
                    + "AND  cast(GETDATE() as date) BETWEEN cdc.appFrom AND cdc.appTo";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("supplierCode", DmShared.iNull(SupplierCode).toString());
            psTemp.setString("medicineCode", DmShared.iNull(medicineCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                mrTemp = (DmServer.vNull(rsTemp.getString("discPer"), "0.00"));

            }
            rsTemp.close();
            psTemp.close();

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

    public List<OverHeadITS_Mdl> getItsForFile(String itemCode, String itemRate, String location, Connection cnTemp) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();

        try {

            dmrTemp = STServer.getITS(itemCode, DmShared.vNull(itemRate).doubleValue(), "", STEnum.ITSType.PURCHASE, DmServer.getSsnSiteCode(this), location, DmServer.getServerDateTime()[0], cnTemp);

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {

            } catch (Exception e) {
            }
        }

        return dmrTemp.getData();

    }

    @Override
    public DmResult<List<OverHeadITS_Mdl>> getITS(String itemCode, String itemRate, String location, String itsBasedOn, String billDate) {
        DmResult<List<OverHeadITS_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp = STServer.getITS(itemCode, DmShared.vNull(itemRate).doubleValue(), itsBasedOn, STEnum.ITSType.PURCHASE, DmServer.getSsnSiteCode(this), location, billDate, cnTemp);
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

}
