package aarogya.pharmacy.enquiry.server;

import aarogya.pharmacy.enquiry.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGUI.server.DmGUI_Server;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Enquiry_SrvImpl extends DmRSSPrivate implements Enquiry_Srv {

    private String vCat = new ProjectVoucherCategory().PHARMACY_ENQUIRY;
    private final String voucherTypeFieldName = "enq.V_Type", voucherDateFieldName = "enq.V_Date";

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
            sqlQuery = "SELECT enq.docid "
                    + "From enquiry enq "
                    + "Inner Join voucher_Type vt on vt.v_type = enq.v_type "
                    + "where enq.site_Code = " + DmServer.getSsnSiteCode(this) + " "
                    + "and vt.category = '" + vCat + "' and enq.site_Code = :site_Code and enq.v_prefix = :vPrefix ";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypeFieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " Order By enq.v_Date Desc,vt.short_Name, enq.v_No Desc, enq.recid ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();

            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResult<Enquiry_Mdl> getMoveRec(String strKey) {
        DmResult<Enquiry_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        RecId recId = new RecId();
        String sqlQuery;
        Enquiry_Mdl mrTemp = new Enquiry_Mdl();
        dmrTemp.setData(mrTemp);
        GrdItemDetail_Mdl node;
        GrdVendor_Mdl lstVendor;
        dmrTemp.setData(mrTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            //====================== 1st & 2nd Row Detail========================  
            sqlQuery = "SELECT enq.docId, enq.v_Type, vt.description, enq.recId, enq.recIdPrefix, enq.v_No, enq.v_Date,vt.guiTemplateCode,"
                    + " enq.preparedBy, enq.remark,enq.u_name,enq.u_EntDt ,enq.referenceNo,enq.u_EntDt_LatestLine,"
                    + " enq.approvedBy,enq.wtaCode, enq.guiConfigCode "
                    + " FROM enquiry enq"
                    + " INNER JOIN voucher_type vt ON enq.v_Type = vt.v_Type"
                    + " WHERE enq.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", strKey);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setEnquiryTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setEnquiryTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setEnquiryDt(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setReferenceNo(DmShared.xNull(rsTemp.getString("referenceNo")));
                mrTemp.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setGuiConfigCode(DmShared.xNull(rsTemp.getString("guiConfigCode")));
                recId.setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                recId.setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                recId.setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.setRecIdEnquiry(recId);
                if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                    mrTemp.setUserEntDtLatestLine(String.valueOf(rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()));
                }
            }
            psTemp.close();
            rsTemp.close();

            sqlQuery = "SELECT ed.docId, ed.v_SNo, IsNull(ed.quantity,0) as quantity,ed.scheduleDate,ed.remark,"
                    + " ed.itemCode itemCode,im.name itemName,um.unit AS unit, ed.unitCode,ed.skuQuantity, "
                    + " im.sku ,ums.unit as skuName,ums.decimalPlaces as allowedSkuDecimalPlaces,"
                    + " ed.skuQuantity,cast( (ed.skuQuantity /  ed.quantity) as decimal(18,7)) as conversionRate,ump.decimalPlaces as purchaseUnitDecimalPlaces"
                    + " FROM enquiryDetail ed  "
                    + " LEFT JOIN itemMast im ON im.code = ed.itemCode  "
                    + " LEFT JOIN unitMast um ON um.code = ed.unitCode   "
                    + " LEFT JOIN unitMast ums ON ums.code = im.sku "
                    + " LEFT JOIN unitMast ump ON ump.code = im.purchaseUnit "
                    + " where ed.docid=:docid "
                    + " order by ed.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setScheduleDt(DmShared.dNull(rsTemp.getDate("scheduleDate")));
                node.setQty(DmServer.vNull(rsTemp.getString("quantity")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("unitCode")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setvSno(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setSkuQuantity(DmServer.vNull(rsTemp.getString("skuQuantity")));
                node.setDecimalPlacesSku(DmServer.vNull(rsTemp.getString("allowedSkuDecimalPlaces")));
                node.setDecimalPlacesPurchaseUnit(DmServer.vNull(rsTemp.getString("purchaseUnitDecimalPlaces")));
                node.setConversionRate(DmServer.vNull(rsTemp.getString("conversionRate"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue())));
                node.setConversionRate(DmServer.vNull(node.getConversionRate()));
                node.setDisplayUnit(DmShared.xNull(rsTemp.getString("unit")));
                if (DmShared.vNull(rsTemp.getString("unitCode")).longValue() != DmShared.vNull(rsTemp.getString("sku")).longValue()) {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unit")) + " " + "=" + " " + DmServer.vNull(rsTemp.getString("conversionRate")) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                } else {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("unit")));
                }

                DmResult<List<GrdIndentDetails_Mdl>> resultIndentDetail = getIndentDetail(true, DmShared.xNull(strKey), DmShared.xNull(rsTemp.getString("v_SNo")), null, "", cnTemp);
                if (resultIndentDetail.isStatus()) {
                    node.getGrdIndentDetail().addAll(resultIndentDetail.getData());
                } else {
                    throw new DmException(0, "No record found.");
                }
                mrTemp.getGrdItem_Mdl().add(node);
            }
            rsTemp.close();
            psTemp.close();

            //==================== Vendor Grid =====================================
            sqlQuery = "SELECT evd.subCode,sb.name as name,evd.isStatus "
                    + " FROM enquiryVendorDetail evd  "
                    + " LEFT JOIN subgroup sb ON evd.subCode = sb.subCode "
                    + " where evd.docid=:docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstVendor = new GrdVendor_Mdl();

                lstVendor.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                lstVendor.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstVendor.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));
                mrTemp.getGrdVen_Mdl().add(lstVendor);
            }
            rsTemp.close();
            psTemp.close();
            /*====================================< Get GUI detail >====================================*/
            mrTemp.setGuiMdl(DmGUI_Server.getGUIViaConfigCode(mrTemp.getGuiConfigCode(), DmShared.xNull(strKey), DmServer.getSsnSiteCode(this), cnTemp));

            //====================== END ==============================================
            psTemp.close();
            rsTemp.close();
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

    private WalkThroughDefault_Mdl getWalkThroughFor(Enquiry_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode("");
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getEnquiryTypeCode());

        return walkThroughDefault;
    }

    @Override
    public DmResult<String> saveRecord(Enquiry_Mdl mrTemp, String voucherPrefix, boolean isApprove) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery;

        dmrTemp.setData("");

        try {
            Enquiry_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (!DmShared.xNull(mrTemp.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {

                STServer.chkIfDocumentUsed("enquiryDocId", "quotationDetail", mrTemp.getSharedKeyFieldCode(), cnTemp);

                sqlQuery = "select count(*) as cnt from  enquiry where docid = :docId and ISNULL(approvedBy,'') <> '' ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                        throw new DmException(0, "Record is approved. You can not save this record.");
                    }
                }
                rsTemp.close();
                psTemp.close();

                sqlQuery = "SELECT u_EntDt_LatestLine FROM enquiry WHERE docId = :docId";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (rsTemp.getTimestamp("u_EntDt_LatestLine") != null) {
                        if (DmShared.iNull(mrTemp.getUserEntDtLatestLine()).longValue() != rsTemp.getTimestamp("u_EntDt_LatestLine").getTime()) {
                            throw new DmException(0, "Record has been changed, can't update record, refresh record then update");
                        }
                    }
                }
                rsTemp.close();
                psTemp.close();
            }

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
            /*==============================<< Check items for INDENT date change >>====================================*/
            if (!DmShared.xNull(mrTemp.getIndentV_DateChangeCondition()).equalsIgnoreCase("")) {
                sqlQuery = "SELECT MAX(ind.v_Date) AS maxIndentDate FROM indent ind " + DmShared.xNull(mrTemp.getIndentV_DateChangeCondition());
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (DmShared.convertDate(DmShared.dNull(rsTemp.getDate("maxIndentDate"))).getTime() > DmShared.convertDate(DmShared.dNull(mrTemp.getEnquiryDt())).getTime()) {
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

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getEnquiryTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdEnquiry(DmServer.getRecId("enquiry", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdEnquiry(), mrTemp.getEnquiryDt(), DmShared.xNull(mrTemp.getEnquiryTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdEnquiry().getRecIdValue()));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), mrTemp.getEnquiryTypeCode(), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("enquiry", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdEnquiry(), mrTemp.getEnquiryDt(), DmShared.xNull(mrTemp.getEnquiryTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            /*=======================================<< Delete GUI >>=============================================*/
            DmGUI_Server.deleteViaConfigCode(mrTemp.getGuiConfigCode(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnSiteCode(this), cnTemp);

            if (blnAdd) {
                sqlQuery = "INSERT INTO enquiry (docId, v_Type, recId, recIdPrefix, v_No, v_Date, v_Prefix,site_code,remark, preparedBy ,"
                        + "  referenceNo, u_EntDt,wtaCode, guiConfigCode,u_EntDt_LatestLine) "
                        + "VALUES (:docId, :v_Type,  :recId,:recIdPrefix,:v_No, :v_Date, :v_Prefix,:site_code, :remark,:preparedBy,"
                        + "   :referenceNo, :uEntDt,:wtaCode, :guiConfigCode,:uEntDt ) ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("v_Type", DmShared.xNull(mrTemp.getEnquiryTypeCode()));
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdEnquiry().getCounter()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getEnquiryDt()));
                psTemp.setString("remark", mrTemp.getRemark());
                psTemp.setString("site_code", DmServer.getSsnSiteCode(this));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdEnquiry().getPrefix()));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setString("referenceNo", DmShared.xNull(mrTemp.getReferenceNo()));
                psTemp.setString("wtaCode", DmShared.xNull(mrTemp.getWtaCode()));
                psTemp.setString("guiConfigCode", DmShared.foriegnKey(mrTemp.getGuiConfigCode()));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            } else {
                sqlQuery = "UPDATE enquiry SET"
                        + "  v_Date = :v_Date ,"
                        + "  remark = :remark ,"
                        + "  referenceNo = :referenceNo ,"
                        + "  u_EntDt = :uEntDt ,"
                        + "  u_EntDt_LatestLine = :uEntDt ,"
                        + "  u_name = :u_name, "
                        + "  site_code = :site_code "
                        + " WHERE docId = :docId ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getEnquiryDt()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("referenceNo", DmShared.xNull(mrTemp.getReferenceNo()));
                psTemp.setString("u_name", DmServer.getSsnUserName(this));
                psTemp.setString("site_code", DmServer.getSsnSiteCode(this));
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            }
            psTemp.executeUpdate();
            psTemp.close();

            //============================= Item Detail Grid Entry ===============================
            sqlQuery = " delete from enquiryDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            List<GrdItemDetail_Mdl> listDetail = mrTemp.getGrdItem_Mdl();
            int vSNo = 0;
            for (GrdItemDetail_Mdl item_Mdl : listDetail) {
                if (!DmShared.xNull(item_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    item_Mdl.setvSno(String.valueOf(++vSNo));
                    sqlQuery = "insert into enquiryDetail(docId,v_SNo, itemCode, quantity, unitCode, scheduleDate,remark,skuQuantity)  "
                            + " VALUES (:docId,:v_SNo, :itemCode, :quantity, :unitCode, :scheduleDate,:remark,:skuQuantity) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(item_Mdl.getvSno()).toString());
                    psTemp.setString("itemCode", DmShared.foriegnKey(item_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", item_Mdl.getQty());
                    psTemp.setString("unitCode", DmShared.foriegnKey(item_Mdl.getPurchaseUnitCode()));
                    psTemp.setDate("scheduleDate", DmServer.convertDate(item_Mdl.getScheduleDt()));
                    psTemp.setString("remark", DmShared.xNull(item_Mdl.getRemark()));
                    psTemp.setString("skuQuantity", DmServer.vNull(item_Mdl.getSkuQuantity()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            //========================== END===================================================
            //============================= Indent Detail Grid Entry ==========================
            sqlQuery = " delete from enquiryIndentDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdItemDetail_Mdl itemDetail_Mdl : listDetail) {

                if (!DmShared.xNull(itemDetail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    for (GrdIndentDetails_Mdl grdIndentDetail : itemDetail_Mdl.getGrdIndentDetail()) {
                        if (!DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                            sqlQuery = "insert into enquiryIndentDetail(docId, v_SNo, itemCode,indentDocId,indentV_SNo)  "
                                    + " VALUES (:docId, :v_SNo, :itemCode, :indentDocId,:indentV_SNo) ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setString("indentV_SNo", DmShared.foriegnKey(grdIndentDetail.getIndentVSNo()));
                            psTemp.setString("v_SNo", DmShared.iNull(itemDetail_Mdl.getvSno()).toString());
                            psTemp.setString("itemCode", DmShared.foriegnKey(itemDetail_Mdl.getSharedKeyFieldCode()));
                            psTemp.setString("indentDocId", DmShared.foriegnKey(grdIndentDetail.getSharedKeyFieldCode()));
                            psTemp.executeUpdate();
                            psTemp.close();
                        }
                    }
                }
            }
            //========================== END===================================================

            //============================= Vendor Grid Entry =================================
            sqlQuery = " delete from enquiryVendorDetail Where docId= :docId and isStatus not in ('1')  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();
            List<GrdVendor_Mdl> listVendor = mrTemp.getGrdVen_Mdl();
            for (GrdVendor_Mdl vendor_Mdl : listVendor) {
                if ((!DmShared.xNull(vendor_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase(""))
                        && !DmShared.xNull(vendor_Mdl.getIsStatus()).equalsIgnoreCase("1")) {
                    sqlQuery = "insert into enquiryVendorDetail(docId, subCode,isStatus)  "
                            + " VALUES (:docId, :subCode,0) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("subCode", DmShared.foriegnKey(vendor_Mdl.getSharedKeyFieldCode()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }
            if (DmShared.iNull(mrTemp.getWtaCode()).longValue() == 0 && isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }
            //========================== END====================================================
            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
            }
            /*====================================< Save GUI >====================================*/
            DmGUI_Server.saveViaConfigCode(mrTemp.getGuiMdl().getGuiRowList(), DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), DmServer.getSsnSiteCode(this), cnTemp);
            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());

            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();

            /*====================================< Send mail & SMS >====================================*/
            if (blnAdd) {
                STServer.sendMail(mrTemp.getSharedKeyFieldCode(), mrTemp.getEnquiryTypeCode(), this);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHEnquiryType(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT vt.V_type, vt.Description "
                    + " FROM Voucher_Type vt  "
                    + " WHERE vt.isActive = 1 "
                    + " and vt.Category = '" + vCat + "'");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("V_type")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Description")));
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
    public DmResultPageing<List<TxtHSupplier_Mdl>> getTxtHSupplier(boolean flagFilterByVoucherCategory, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHSupplier_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHSupplier_Mdl> lstTemp = new ArrayList<>();
        TxtHSupplier_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", filtration = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (flagFilterByVoucherCategory) {
                filtration = " SELECT acf.Nature "
                        + " FROM AcFilteration acf "
                        + " LEFT JOIN voucher_Type vt ON vt.v_type = acf.v_Type "
                        + " WHERE vt.category IN ('" + vCat + "')";
            } else {
                filtration = " SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type ";
            }

            sqlQuery = "SELECT "
                    + "   sg.subCode AS code"
                    + " , sg.name"
                    + " , sg.manualCode"
                    + " , c.cityName"
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (" + filtration + ")"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (!flagFilterByVoucherCategory) {
                sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHSupplier_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
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
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT im.code, im.name, im.manualCode, im.sku, um.unit as skuName,"
                    + " im.propertyList, IsNull(um.decimalPlaces,0) as decimalPlaces,"
                    + " IsNull(ump.decimalPlaces,0) as decimalPlacesPurchaseUnit,"
                    + " ump.unit as purchaseUnitName,im.purchaseUnit as purchaseUnitCode,"
                    + " im.purchaseUnitConversionRate"
                    + " FROM itemMast im"
                    + " LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + " LEFT JOIN unitmast um ON im.sku = um.code"
                    + " LEFT JOIN unitmast ump ON im.purchaseUnit = ump.code"
                    + " WHERE im.IsActive = 1"
                    + " AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + " AND sl.site_Code = :site_Code");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSku(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setDisplayUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setDecimalPlacesPurchaseUnit(DmShared.xNull(rsTemp.getString("decimalPlacesPurchaseUnit")));
                listNode.setConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                if (DmShared.vNull(rsTemp.getString("purchaseUnitCode")).longValue() != DmShared.vNull(rsTemp.getString("sku")).longValue()) {
                    listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " " + "=" + " " + listNode.getConversionRate() + " " + listNode.getSkuName());
                } else {
                    listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                }

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
    public DmResult<String> deleteRecord(String strKey, String guiConfigCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, wtaCode = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            sqlQuery = "select approvedBy, wtaCode  FROM enquiry WHERE  docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                wtaCode = DmShared.xNull(rsTemp.getString("wtaCode"));
                if (!DmShared.xNull(rsTemp.getString("approvedBy")).equalsIgnoreCase("")) {
                    throw new DmException(0, "Record is approved. You can't delete this record.");
                }
            }
            rsTemp.close();
            psTemp.close();

            STServer.chkIfDocumentUsed("enquiryDocId", "quotationDetail", strKey, cnTemp);

            WalkThrough_Server.deleteWalkThrough(wtaCode, cnTemp);
            ModifyReason_Server.delete(strKey, cnTemp);
            DmGUI_Server.deleteViaConfigCode(guiConfigCode, strKey, DmServer.getSsnSiteCode(this), cnTemp);

            sqlQuery = "delete from enquiryIndentDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from enquiryVendorDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from enquiryDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "delete from enquiry Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
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
    public DmResultPageing<List<Find_Mdl>> getFind(UserPermission userPermission, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "", sqlCondition = "";

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select enq.docid,vt.short_Name,enq.recId,vt.v_Type,enq.remark,enq.v_Date,enq.v_No,"
                    + " enq.referenceNo,enq.approvedBy,format(enq.approvalDateTime,'dd-MMM-yyyy HH.mm') as approvalDateTime "
                    + " from enquiry as enq "
                    + " inner join voucher_Type as vt on enq.v_Type=vt.v_Type "
                    + " where enq.site_Code = :site_Code and vt.category = '" + vCat + "' and enq.v_prefix = :vPrefix ";
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

                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docid")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setEnquiryTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                listNode.setEnquiryTypeName(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setEnqiryDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setRemarks(DmShared.xNull(rsTemp.getString("remark")));
                listNode.setReferenceNo(DmShared.xNull(rsTemp.getString("referenceNo")));
                listNode.setApproval(DmShared.xNull(rsTemp.getString("approvedBy")) + " " + DmShared.xNull(rsTemp.getString("approvalDateTime")));
                listNode.setApprovedBy(DmShared.xNull(rsTemp.getString("approvedBy")));
//                listNode.setIsStatus(DmShared.xNull(rsTemp.getString("isStatus")));

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
    public DmResult<List<GrdIndentDetails_Mdl>> getIndentDetail(List<KeyFieldExtended> listKF, String itemCode) {
        DmResult<List<GrdIndentDetails_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            DmResult<List<GrdIndentDetails_Mdl>> resultIndentDetail = getIndentDetail(false, "", "", listKF, DmShared.xNull(itemCode), cnTemp);
            if (resultIndentDetail.isStatus()) {
                dmrTemp.setData(resultIndentDetail.getData());
            } else {
                throw new DmException(0, "No record found.");
            }

            dmrTemp.setStatus(true);
            cnTemp.close();
        } catch (SQLException | DmException e) {
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
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        Connection cnTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "select top 1 description,V_Type, guiTemplateCode "
                    + " from voucher_Type "
                    + " where category='" + vCat + "' and isActive=1";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);

            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setEnquiryTypeCode(DmShared.xNull(rsTemp.getString("V_Type")));
                mrTemp.setEnquiryTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setGuiTemplate(DmShared.xNull(rsTemp.getString("guiTemplateCode")));

            }
            psTemp.close();
            rsTemp.close();
            mrTemp.setCurrentDate(DmServer.getServerDateTime()[0]);

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

    private DmResult<List<GrdIndentDetails_Mdl>> getIndentDetail(boolean isMoveRec, String docId, String v_SNo, List<KeyFieldExtended> listKF, String itemCode, Connection cnTemp) {
        DmResult<List<GrdIndentDetails_Mdl>> dmrTemp = new DmResult();
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = null;
        List<GrdIndentDetails_Mdl> lstNode = new ArrayList<>();
        GrdIndentDetails_Mdl node;
        String condition = "";
        dmrTemp.setData(lstNode);
        try {
            if (DmShared.iNull(docId).longValue() != 0) {
                if (isMoveRec) {
                    sqlQuery = "select  eind.indentDocId, id.v_SNo, i.recId as indentNo, i.v_Date as indentDate, "
                            + " IsNull(id.aprQty,0) as aprQty, id.reqDate, im.sku, um.unit, id.remark, "
                            + " im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName,"
                            + " ump.decimalPlaces as allowedDecimalPlaces,"
                            + " um.decimalPlaces as allowedSkuDecimalPlaces,"
                            + " im.purchaseUnitConversionRate as purchaseUnitConversionRate "
                            + " from enquiryIndentDetail as eind "
                            + " left join indent as i on eind.indentDocId = i.docId "
                            + " left join indentDetail as id on eind.indentDocId = id.docId and eind.itemCode = id.itemCode "
                            + " left join itemMast as im on eind.itemCode = im.code   "
                            + " left join unitMast as um on im.sku = um.code   "
                            + " left join unitMast as ump on im.purchaseUnit = ump.code   "
                            + " where eind.docId = :docId and eind.v_SNo = :v_SNo "
                            + " order by id.v_SNo ";
                }
            } else {
                for (KeyFieldExtended field : listKF) {
                    if (DmShared.iNull(field.getSharedKeyFieldCode()).longValue() != 0) {
                        if (!condition.trim().equalsIgnoreCase("")) {
                            condition += " or ";
                        }
                        condition += " (id.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                                + "         and id.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "' "
                                + "         and id.itemCode = :itemCode "
                                + "         and IsNull(id.aprQty,0) > 0 "
                                + " ) ";
                    }
                }

                sqlQuery = "select  i.docId as indentDocId, id.v_SNo, i.recId as indentNo, i.v_Date as indentDate, "
                        + " IsNull(id.aprQty,0) as aprQty, id.reqDate, im.sku, um.unit, "
                        + " im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName,"
                        + " ump.decimalPlaces as allowedDecimalPlaces,"
                        + " um.decimalPlaces as allowedSkuDecimalPlaces,"
                        + " im.purchaseUnitConversionRate as purchaseUnitConversionRate "
                        + " from indent as i "
                        + " inner join indentDetail as id on i.docId = id.docId "
                        + " inner join voucher_Type as vt on vt.v_Type = i.V_type "
                        + " left join subGroup as sgd on i.departmentCode = sgd.subCode "
                        + " left join itemMast as im on id.itemCode = im.code "
                        + " left join unitMast as um on im.sku = um.code "
                        + " left join unitMast as ump on im.purchaseUnit = ump.code   "
                        + " left join ("
                        + "             SELECT eid.indentDocId, eid.indentV_SNo "
                        + "             FROM enquiryIndentDetail eid "
                        + "             group by eid.indentDocId, eid.indentV_SNo"
                        + "         ) vEid on vEid.indentDocId = i.docid AND vEid.indentV_SNo = id.v_SNo"
                        + " where i.site_Code = :site_Code "
                        + " and isNull(i.approvedBy,'') <> '' "
                        + " and id.isStatus = 0 "
                        + " AND vEid.indentDocId is null "
                        + " and vt.category = '" + new ProjectVoucherCategory().PHARMACY_INDENT + "' "
                        + " and (" + condition + ")"
                        + " order by id.v_SNo";

            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmShared.vNull(docId).doubleValue() != 0) {
                psTemp.setString("docId", DmShared.iNull(docId).toString());
                psTemp.setString("v_SNo", DmShared.iNull(v_SNo).toString());
            } else {
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdIndentDetails_Mdl();

                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("indentDocId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indentNo")));
                node.setIndentVSNo(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
                node.setReqDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                node.setIndentDate(DmShared.dNull(rsTemp.getDate("indentDate")));
                node.setIndentQty(DmServer.vNull(rsTemp.getString("aprQty")));
                node.setIndentSkuTag(DmShared.xNull(rsTemp.getString("sku")));
                node.setIndentSkuText(DmShared.xNull(rsTemp.getString("unit")));
                node.setIndentQtyInPurchaseUnit(DmServer.vNull(rsTemp.getString("aprQty")));

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
        return dmrTemp;
    }

    @Override
    public DmResult<String> setApproval(String approvedBy, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        int approved;
        String sqlQuery = "";
        ResultSet rsTemp = null;
        dmrTemp.setData(docId);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(approvedBy).trim().equalsIgnoreCase("")) {
                sqlQuery = "UPDATE enquiry SET  "
                        + " approvedBy = :userName "
                        + " WHERE docid = :docId "
                        + " AND  ISNULL(approvedBy,'') = '';";
            } else {
                sqlQuery = "UPDATE enquiry SET  "
                        + " approvedBy = NULL "
                        + " WHERE docid = :docId "
                        + " AND  :userName IN (ISNULL(approvedBy,''),'SA','SUPER');";

            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            psTemp.setString("userName", DmServer.getSsnUserName(this));
            approved = psTemp.executeUpdate();

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
    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(List<KeyFieldExtended> listKF) {
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

            /*==============================<< Get Item detail (via list) >>===============================*/
            for (KeyFieldExtended field : listKF) {
                if (DmShared.vNull(field.getSharedKeyFieldCode()).doubleValue() != 0) {
                    condition += " or (id.docId = '" + DmShared.iNull(field.getSharedKeyFieldCode()).toString() + "' "
                            + "         and id.v_SNo = '" + DmShared.iNull(field.getSharedKeyFieldName()).toString() + "'"
                            + "         and IsNull(id.aprQty,0) > 0 "
                            + " ) ";
                }
            }
            sqlQuery = "select  id.itemCode, max(im.name) as itemName, IsNull(sum(id.aprQty),0) as quantity, max(im.sku) as unitCode,"
                    + " max(um.unit) as unitName,min(id.reqDate) as reqdate, "
                    + " max(im.purchaseUnit) as purchaseUnitCode,max(ump.unit) as purchaseUnitName,"
                    + " max(ump.decimalPlaces) as allowedDecimalPlacesPurchaseUnit,"
                    + " max(um.decimalPlaces) as allowedSkuDecimalPlaces,"
                    + " max(im.purchaseUnitConversionRate) as purchaseUnitConversionRate "
                    + " from indent as i "
                    + " inner join indentDetail as id on i.docId = id.docId "
                    + " inner join voucher_Type as vt on vt.v_Type = i.V_type "
                    + " left join itemMast as im on id.itemCode = im.code "
                    + " left join unitMast as ump on im.purchaseUnit = ump.code   "
                    + " left join unitMast as um on im.sku = um.code "
                    + " left join ("
                    + "             SELECT eid.indentDocId, eid.indentV_SNo "
                    + "             FROM enquiryIndentDetail eid "
                    + "             group by eid.indentDocId, eid.indentV_SNo"
                    + "         ) vEid on vEid.indentDocId = i.docid AND vEid.indentV_SNo = id.v_SNo "
                    + " where i.site_Code = :site_Code "
                    + " and isNull(i.approvedBy,'') <> ''"
                    + " and id.isStatus = 0 "
                    + " AND vEid.indentDocId is null "
                    + " and vt.category = '" + new ProjectVoucherCategory().PHARMACY_INDENT + "' "
                    + " and (1 <> 1 " + condition + ")"
                    + " group by id.itemCode"
                    + " order by itemName ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setQty(DmServer.vNull(rsTemp.getString("quantity")));
                node.setScheduleDt(DmShared.dNull(rsTemp.getDate("reqdate")));
                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setDisplayUnit(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setDecimalPlacesPurchaseUnit(DmShared.xNull(rsTemp.getString("allowedDecimalPlacesPurchaseUnit")));
                node.setDecimalPlacesSku(DmShared.xNull(rsTemp.getString("allowedSkuDecimalPlaces")));
                node.setSkuQuantity(DmServer.vNull(rsTemp.getString("quantity"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedSkuDecimalPlaces")).intValue())));
                if (!DmShared.xNull(rsTemp.getString("unitCode")).equalsIgnoreCase(node.getPurchaseUnitCode())) {
                    if (DmShared.vNull(node.getQty()) > 0 && DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate")) > 0) {
                        double qty = DmShared.vNull(node.getQty()) / DmShared.vNull(rsTemp.getString("purchaseUnitConversionRate"));
                        node.setQty(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("allowedDecimalPlacesPurchaseUnit")).intValue())));
                    }
                }

                node.setConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                if (DmShared.vNull(rsTemp.getString("purchaseUnitCode")).longValue() != DmShared.vNull(rsTemp.getString("unitCode")).longValue()) {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")) + " " + "=" + " " + node.getConversionRate() + " " + DmShared.xNull(rsTemp.getString("unitName")));
                } else {
                    node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                }
                /*==============================<< Get Indent detail (via list & ItemCode ) >>===============================*/
                DmResult<List<GrdIndentDetails_Mdl>> resultIndentDetail;
                resultIndentDetail = getIndentDetail(false, "", "", listKF, DmShared.xNull(rsTemp.getString("itemCode")), cnTemp);

                if (resultIndentDetail.isStatus()) {
                    node.getGrdIndentDetail().addAll(resultIndentDetail.getData());
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

    @Override
    public DmResult<String> cancelVendor(String strKey, String subCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "update enquiryVendorDetail set isStatus=1 Where docId= :docId AND subCode=:subCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
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
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                documentApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                documentApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
            }
            dmrTemp.setData(docId);

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

    private void documentApproval(WalkThroughEnum walkThroughMove, String keyCode, String userName, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery = "";

        try {

            STServer.chkIfDocumentUsed("enquiryDocId", "quotationDetail", keyCode, cnTemp);

            sqlQuery = "select approvedBy,approvalDateTime,"
                    + "(case when :userName IN (ISNULL(approvedBy,''),'SA','SUPER') then 1 else 0 end) canApprove "
                    + "from enquiry where docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.setString("userName", userName);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (walkThroughMove == WalkThroughEnum.ACCEPT) {
                    if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        rsTemp.updateString("approvedBy", DmShared.xNull(userName).trim());
                        rsTemp.updateTimestamp("approvalDateTime", new Timestamp(new java.util.Date().getTime()));
                        rsTemp.updateRow();
                        rsTemp.close();
                        psTemp.close();

                        sqlQuery = " Update enquiry set "
                                + " u_EntDt_LatestLine=:uEntDt "
                                + " where docId=:docId ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                        psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                        psTemp.executeUpdate();
                        psTemp.close();
                    } else {
                        throw new Exception("Document already approved.");
                    }
                } else {
                    if (!DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                        if (DmShared.iNull(rsTemp.getString("canApprove")).longValue() == 1) {

                            rsTemp.updateString("approvedBy", null);
                            rsTemp.updateDate("approvalDateTime", null);
                            rsTemp.updateRow();
                            rsTemp.close();
                            psTemp.close();

                            sqlQuery = " Update enquiry set "
                                    + " u_EntDt_LatestLine=:uEntDt "
                                    + " where docId=:docId ";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
                            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                            psTemp.executeUpdate();
                            psTemp.close();
                        } else {
                            throw new Exception("Unauthorized user to unapprove.");
                        }
                    } else {
                        throw new Exception("Document already un-approved.");
                    }
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    documentApproval(walkThroughEnum, keyCode, userName, cnTemp);
                }
            }, this);

            dmrTemp.setData("");
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
            dmrTemp.setStatus(false);
        }
        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<GrdIndentDetailsSearch_Mdl>> getIndentSearchDetail(String itemCode, IndentSearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdIndentDetailsSearch_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String condition = "", sqlQuery;
        List<GrdIndentDetailsSearch_Mdl> lstTemp = new ArrayList<GrdIndentDetailsSearch_Mdl>();
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "and id.isStatus = 0 and IsNull(id.aprQty,0) > 0 and isNull(i.approvedBy,'') <> '' ";


            /*=================================  Set Conditions ===============================================*/
            if (!DmShared.xNull(itemCode).equalsIgnoreCase("")) {
                condition += " and id.itemCode =:itemCode ";
            }
            if (!DmShared.dNull(searchValueMdl.getFromDate()).equalsIgnoreCase("")) {
                condition += " and i.v_Date >= :fromDate ";
            }
            if (!DmShared.dNull(searchValueMdl.getToDate()).equalsIgnoreCase("")) {
                condition += " and i.v_Date <= :toDate ";
            }
            if (!DmShared.xNull(searchValueMdl.getDepartmentTag()).equalsIgnoreCase("")) {
                condition += " and i.departmentCode in (" + DmServer.getParamList(":departmentCode", searchValueMdl.getDepartmentTag()) + " )";
            }

            sqlQuery = "select  i.docId as indentDocId, id.v_SNo, i.recId as indentNo, i.v_Date as indentDate, i.departmentCode, sgd.name as departmentName, "
                    + "		id.itemCode, im.name as itemName, id.aprQty as quantity, id.reqDate, im.sku, um.unit, id.remark, id.isStatus, i.approvedBy "
                    + "		from indent as i "
                    + "		inner join indentDetail as id on i.docId = id.docId "
                    + "         inner join voucher_Type as vt on vt.v_Type = i.V_type "
                    + "		left join subGroup as sgd on i.departmentCode = sgd.subCode "
                    + "		left join itemMast as im on id.itemCode = im.code "
                    + "		left join unitMast as um on im.sku = um.code "
                    + "         left join ("
                    + "             SELECT eid.indentDocId "
                    + "             FROM enquiryIndentDetail eid "
                    + "             group by eid.indentDocId"
                    + "         ) vEid on vEid.indentDocId = i.docid "
                    + "         where i.site_Code = :site_Code "
                    + "         and vt.category = '" + new ProjectVoucherCategory().PHARMACY_INDENT + "' "
                    + "         and vEid.indentDocId is null " + condition;
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            if (!DmShared.xNull(itemCode).equalsIgnoreCase("")) {
                psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            }
            if (!DmShared.dNull(searchValueMdl.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(searchValueMdl.getFromDate()));
            }
            if (!DmShared.dNull(searchValueMdl.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(searchValueMdl.getToDate()));
            }
            if (!DmShared.xNull(searchValueMdl.getDepartmentTag()).equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(searchValueMdl.getDepartmentTag()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdIndentDetailsSearch_Mdl objListTemp = new GrdIndentDetailsSearch_Mdl();
                objListTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("indentDocId")));
                objListTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("indentNo")));
                objListTemp.setVoucherSNo(DmServer.vNull(rsTemp.getString("v_SNo"), "0"));
                objListTemp.setIndentDate(DmShared.dNull(rsTemp.getDate("indentDate")));
                objListTemp.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                objListTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                objListTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                objListTemp.setSku(DmShared.xNull(rsTemp.getString("unit")));
                objListTemp.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                objListTemp.setRequiredDate(DmShared.dNull(rsTemp.getDate("reqDate")));
                objListTemp.setIsChecked("0");
                lstTemp.add(objListTemp);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartmentIndentSearch(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }
            sqlQuery = "SELECT dm.subCode AS code, sg.name "
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON sg.subCode = dm.subCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + "	WHERE sgn.manualCode IN ('CM','PR') AND sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
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
    public DmResult<List<GrdVendor_Mdl>> getApprovedVendors(String itemCsv, String vendorCsv) {
        DmResult<List<GrdVendor_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "";
        List<GrdVendor_Mdl> lstNode = new ArrayList<>();
        GrdVendor_Mdl node;
        dmrTemp.setData(lstNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            condition = "WHERE sg.isActive = 1 ";
            condition += "AND ssl.site_Code = :site_Code ";
            if (!DmShared.xNull(itemCsv).equalsIgnoreCase("")) {
                condition += " AND av.code IN(" + DmServer.getParamList(":itemCode", itemCsv) + ") ";
            }

            if (!DmShared.xNull(vendorCsv).equalsIgnoreCase("")) {
                condition += " AND av.approvedVendorCode NOT IN(" + DmServer.getParamList(":vendor", vendorCsv) + " ) ";
            }

            sqlQuery = "SELECT av.approvedVendorCode AS subCode, MAX(Sg.name) AS name, MAX(sg.manualCode) AS manualCode, MAX(c.cityName) AS cityName ";
            sqlQuery += "FROM approvedVendors av  ";
            sqlQuery += "INNER JOIN subgroup sg ON sg.subCode = av.approvedVendorCode ";
            sqlQuery += "INNER JOIN subGroupSiteList ssl ON ssl.subCode = sg.subCode ";
            sqlQuery += "LEFT JOIN city c ON c.cityCode = sg.cityCode ";
            sqlQuery += condition;
            sqlQuery += "GROUP BY av.approvedVendorCode ";
            sqlQuery += "ORDER BY MAX(sg.name) ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (!DmShared.xNull(itemCsv).equalsIgnoreCase("")) {
                psTemp.setStringList("itemCode", itemCsv);
            }
            if (!DmShared.xNull(vendorCsv).equalsIgnoreCase("")) {
                psTemp.setStringList("vendor", vendorCsv);
            }
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdVendor_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                node.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
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

}
