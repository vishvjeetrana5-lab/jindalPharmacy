package aarogya.inventory.itemTransfer.server;

import aarogya.inventory.itemTransfer.shared.TxtHItem_Mdl;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_Srv;
import aarogya.inventory.itemTransfer.shared.TxtHRequisitionNo_Mdl;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_Vld;
import aarogya.inventory.itemTransfer.shared.ItemIssueWithRequisition_Mdl;
import aarogya.inventory.itemTransfer.shared.TxtHTransferTo_Mdl;
import aarogya.inventory.itemTransfer.shared.GrdItemDetail_Mdl;
import aarogya.inventory.itemTransfer.shared.Find_Mdl;
import aarogya.inventory.itemTransfer.shared.ItemTransfer_Mdl;
import aarogya.inventory.itemTransfer.shared.DefaultValue_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmToolkit.stock.shared.StockFifo_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STApprovals;
import aarogya.selfToolkit.stStatic.server.STServer;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmJasper.server.DmJasper;
import dataman.dmToolkit.stock.server.Stock_Server;
import dataman.dmToolkit.walkThrough.server.WalkThrough_Server;
import dataman.dmToolkit.walkThrough.shared.IWalkThrough;
import dataman.dmToolkit.walkThrough.shared.WalkThroughDefault_Mdl;
import dataman.dmToolkit.walkThrough.shared.WalkThroughEnum;
import dataman.dmToolkit.walkThrough.shared.WalkThrough_Mdl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ItemTransfer_SrvImpl extends DmRSSPrivate implements ItemTransfer_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final String vCat = "'" + pvc.ITEM_TRANSFER + "','" + pvc.SERVICE_TRANSFER + "'";
    private final String voucherTypefieldName = "sh.V_Type", voucherDateFieldName = "sh.V_Date";

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(UserPermission userPermission) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
        String sqlQuery, sqlCondition;
        DmPreparedStatement psTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select sh.docId "
                    + "         from stockHead as sh "
                    + "         left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "         where vt.category in (" + vCat + ") and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlQuery += " order by sh.v_Date desc, vt.short_Name, sh.v_No desc, sh.recId ";
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
    public DmResult<ItemTransfer_Mdl> getMoveRec(String strKey) {
        DmResult<ItemTransfer_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        ItemTransfer_Mdl mrTemp = new ItemTransfer_Mdl();
        dmrTemp.setData(mrTemp);
        GrdItemDetail_Mdl node;
        ItemProperty_Mdl propertyMdl;
//        TxtHItem_Mdl itemMdl;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	sh.docId, sh.recId, sh.v_Date, sh.u_Name, sh.preparedBy, sh.v_Type, vt.description,sh.remark"
                    + "         , sh.recIdPrefix, sh.v_No, sh.refNo, sh.refDate, st.subCode as transferToCode,sh.wtaCode, "
                    + "		sgrb.name as transferToName ,sh.preparedDate ,sh. u_EntDt,sh.approvedBy,sh.approvalDateTime"
                    + "		from stockHead as sh "
                    + "		left join ( "
                    + "                     select s.docId,"
                    + "                     max(subCode) as subCode "
                    + "                     from stockHeadDetail s "
                    + "                     where s.docId = :docId "
                    + "                     group by s.docId "
                    + "		)   as st on sh.docId = st.docId "
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type "
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode "
                    + "		where sh.docId = :docId";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdTransfer().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdTransfer().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdTransfer().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                mrTemp.setWtaCode(DmShared.xNull(rsTemp.getString("wtaCode")));
                mrTemp.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
                mrTemp.setTransferToTag(DmShared.xNull(rsTemp.getString("transferToCode")));
                mrTemp.setTransferToText(DmShared.xNull(rsTemp.getString("transferToName")));
                mrTemp.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setApprovedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("approvedBy")), rsTemp.getTimestamp("approvalDateTime")));
            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get header detail (Ref No, Ref Date, Department To and Department From) >>===============================*/
            sqlQuery = "select shd.departmentCode, depFr.name departmentFrom, shd.departmentToCode, depTo.name departmentTo, "
                    + "		shd.itemCode, im.name item, im.purchaseUnitConversionRate As skuQtyForRate, shd.quantity, shd.site_Code, shd.site_Code_To, "
                    + "		shd.ibCode, shd.batchNo, shd.manufacturingDate, shd.expiryDate, im.propertyList, "
                    + "		shd.sizeCode, shd.itemMakeCode,  imm.companyName as itemMakeName, ism.name size, shd.colourCode, icm.name color, um.decimalPlaces, "
                    + "         um.unit skuName, shd.remark, shd.v_SNo ,shd.requisitionDocId,shd.requisitionV_SNo , mrq.recId as reqNo, shd.mrp  "
                    + "	from stockHeadDetail shd "
                    + "	left join subGroup depTo on shd.departmentToCode = depTo.subCode "
                    + "	left join subGroup depFr on shd.departmentCode = depFr.subCode "
                    + "	left join itemMast im on im.code = shd.itemCode "
                    + "	left join unitMast um on um.code = im.sku "
                    + "	left join itemColourMast icm on icm.code = shd.colourCode "
                    + "	left join itemSizeMast ism on ism.code = shd.sizeCode "
                    + " LEFT JOIN itemMakeMast imm ON imm.code = shd.itemMakeCode "
                    + " left join requisition mrq ON mrq.docId = shd.requisitionDocId "
                    + "	where shd.docId = :docId "
                    + "	order by shd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                node = new GrdItemDetail_Mdl();

                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("item")));
                node.setBestBefore(!DmShared.dNull(rsTemp.getDate("manufacturingDate")).trim().equalsIgnoreCase(""));
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList"))); //??????
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                node.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                mrTemp.setDepartmentFromSite(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setDepartmentFromTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentFromText(DmShared.xNull(rsTemp.getString("departmentFrom")));
                mrTemp.setDepartmentToSite(DmShared.xNull(rsTemp.getString("site_Code")));
                mrTemp.setDepartmentToTag(DmShared.xNull(rsTemp.getString("departmentToCode")));
                mrTemp.setDepartmentToText(DmShared.xNull(rsTemp.getString("departmentTo")));
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setQuantity(DmServer.vNull(Math.abs(DmShared.vNull(rsTemp.getString("quantity")))));
                if (DmShared.iNull(rsTemp.getString("requisitionDocId")).longValue() != 0) {
                    mrTemp.setRequisitionDocId(DmShared.foriegnKey(rsTemp.getString("requisitionDocId")));
                    mrTemp.setRequisitionNo(DmShared.foriegnKey(rsTemp.getString("reqNo")));
                }
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("requisitionDocId")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("reqNo")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("requisitionV_SNo")));

                //======================< Set Item Properties >======================
                propertyMdl = new ItemProperty_Mdl();
                propertyMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                propertyMdl.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                propertyMdl.setColourName(DmShared.xNull(rsTemp.getString("color")));
                propertyMdl.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                propertyMdl.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                propertyMdl.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                propertyMdl.setItemName(DmShared.xNull(rsTemp.getString("item")));
                propertyMdl.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                propertyMdl.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                propertyMdl.setSizeName(DmShared.xNull(rsTemp.getString("size")));
                propertyMdl.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                propertyMdl.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                propertyMdl.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));
                propertyMdl.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                node.setPropertyMdl(propertyMdl);
                //======================< Set Item Properties Display >======================
                node.setPropertyDisplay(propertyMdl.getPropertiesCsv());
                //============================================================================

                mrTemp.getListItemDetail().add(node);
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

    private String getUserWithDateTime(String userName, Timestamp entryDateTime) {
        String rtnValue = "";
        Calendar cal;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy - HH:mm");
        try {
            if (!DmShared.xNull(userName).equalsIgnoreCase("") && entryDateTime != null) {
                cal = Calendar.getInstance();
                cal.setTime(new Date(entryDateTime.getTime()));
                rtnValue = userName + " - " + sdf.format(cal.getTime());
            }
        } catch (Exception e) {
        }
        return rtnValue;
    }

    @Override
    public DmResult<String> deleteRecord(String keyCode) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData(keyCode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            /*==============================<< Manage Batch Detail >>====================================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            /*==============================<< Manage Requisition Status >>====================================*/
            manageRequisitionStatus(keyCode, true, cnTemp);

            /*==============================<< Delete record from stock >>===============================*/
            sqlQuery = " Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(keyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Delete record from stockHead >>===============================*/
            sqlQuery = " Delete From stockHead Where docId= :docId ";
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
    public DmResult<String> saveRecord(UserPermission userPermission, ItemTransfer_Mdl mrTemp, ItemTransfer_Mdl prevMdl, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        ResultSet rsTemp = null;
        ItemProperty_Mdl propertyMdl;
        dmrTemp.setData("");

        try {
            ItemTransfer_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            DmServer.checkLockDate(DmServer.getSsnSiteCode(this), DmServer.getSsnVoucherPrefix(this), cnTemp);
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdTransfer(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdTransfer(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdTransfer().getRecIdValue()));
                mrTemp.setWtaCode(WalkThrough_Server.applyWalkThrough(getWalkThroughFor(mrTemp), DmServer.getSsnUserName(this), DmShared.xNull(mrTemp.getVoucherTypeCode()), mrTemp.getSharedKeyFieldCode(), cnTemp, this));
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdTransfer(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentFromTag(), mrTemp.getDepartmentFromText(), cnTemp);

            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentToTag(), mrTemp.getDepartmentToText(), cnTemp);

            sqlQuery = "SELECT COUNT(*) AS totalInvalidRecords FROM requisitionDetail rd" + DmShared.xNull(mrTemp.getRequisitionItemChangeCondition());
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("totalInvalidRecords")).intValue() > 0) {
                    throw new DmException(0, "Requisition has been changed, can't save record, refresh record then save");
                }
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "insert into stockHead (docId, v_Type, v_No, recId, v_Prefix, v_Date, refNo, refDate,\n"
                        + " preparedDate, site_Code, preparedBy, recIdPrefix,wtaCode,remark) "
                        + " values (:docId, :vType, :vNo, :recId, :vPrefix, :vDate, :refNo, :refDate,\n"
                        + " :preparedDate, :siteCode, :userName, :recIdPrefix,:wtaCode,:remark)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdTransfer().getPrefix()));
                psTemp.setString("vType", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("wtaCode", DmShared.xNull(mrTemp.getWtaCode()));
                psTemp.setString("vPrefix", voucherPrefix);
                psTemp.setLong("vNo", DmShared.iNull(mrTemp.getRecIdTransfer().getCounter()).longValue());
                psTemp.setString("siteCode", siteCode);
                psTemp.setString("userName", userName);
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());

            } else {
                sqlQuery = " update stockHead Set "
                        + " v_Date = :vDate, "
                        + " transferDt = :transferDt, "
                        + " remark = :remark, "
                        + " u_Name = :u_Name, "
                        + " refNo = :refNo, "
                        + " refDate = :refDate, "
                        + " u_EntDt = :uEntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setDate("vDate", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("refNo", DmShared.xNull(mrTemp.getRefNo()));
                psTemp.setDate("refDate", DmServer.convertDate(mrTemp.getRefDate()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemark()));
                psTemp.setString("transferDt", null);
                psTemp.setString("u_Name", userName);
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Manage Requisition Status >>====================================*/
            manageRequisitionStatus(mrTemp.getSharedKeyFieldCode(), true, cnTemp);

            /*==============================<< Validate Removed Received Items >>====================================*/
            if (!blnAdd) {
                ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, prevMdl), DmEnum.StockEntryType.STOCK_TRANSFER_RECEIVE, cnTemp, this);
            }

            Stock_Server.deleteStock(mrTemp.getSharedKeyFieldCode(), cnTemp, true);

            sqlQuery = " Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int v_SNo = 0;
            for (GrdItemDetail_Mdl detail_Mdl : mrTemp.getListItemDetail()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    propertyMdl = detail_Mdl.getPropertyMdl();
                    sqlQuery = " insert into stockHeadDetail (docId, v_SNo, departmentCode, departmentToCode, itemCode, quantity, site_Code, "
                            + " site_Code_To, remark, isSystemGenerated, ibCode, batchNo, manufacturingDate, expiryDate, "
                            + " colourCode, sizeCode,itemMakeCode,subCode , mrp, requisitionDocId, requisitionV_SNo ) "
                            + " values (:docId, :vSno, :departmentCode, :departmentToCode, :itemCode, :quantity, :siteCode, :siteToCode, "
                            + " :remark, 0, :ibCode, :batchNo, :mfgDate, :expDate, :colourCode, :sizeCode,:itemMakeCode,:subCode , :mrp,"
                            + " :requisitionDocId, :requisitionV_SNo)";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("vSno", detail_Mdl.getV_SNo());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentFromTag()));
                    psTemp.setString("departmentToCode", DmShared.foriegnKey(mrTemp.getDepartmentToTag()));
                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("siteCode", DmShared.foriegnKey(mrTemp.getDepartmentFromSite()));
                    psTemp.setString("siteToCode", DmShared.foriegnKey(mrTemp.getDepartmentToSite()));
                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("batchNo", DmShared.xNull(propertyMdl.getBatchNo()));
                    psTemp.setDate("mfgDate", DmServer.convertDate(propertyMdl.getManufacturingDate()));
                    psTemp.setDate("expDate", DmServer.convertDate(propertyMdl.getExpiryDate()));
                    psTemp.setString("colourCode", DmShared.foriegnKey(propertyMdl.getColourCode()));
                    psTemp.setString("sizeCode", DmShared.foriegnKey(propertyMdl.getSizeCode()));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(propertyMdl.getItemMakeCode()));
                    psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getTransferToTag()));
                    psTemp.setString("mrp", DmShared.xNull(propertyMdl.getMrp()));
                    psTemp.setString("requisitionDocId", DmShared.foriegnKey(detail_Mdl.getRequisitionDocId()));
                    psTemp.setString("requisitionV_SNo", DmShared.foriegnKey(detail_Mdl.getRequisitionV_SNo()));
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            //============< Save Issue Stock >===================//
            v_SNo = saveStockRecord(mrTemp, cnTemp, 0, false);
            //============< Save Receive Stock >===================//
            saveStockRecord(mrTemp, cnTemp, v_SNo, true);
            //============< **************** >===================//

            /*==============================<< Manage Requisition Status >>====================================*/
            manageRequisitionStatus(mrTemp.getSharedKeyFieldCode(), false, cnTemp);

            if (mrTemp.getModifyReasonMdl() != null && !blnAdd) {
                ModifyReason_Server.save(mrTemp.getModifyReasonMdl(), mrTemp.getSharedKeyFieldCode(), DmServer.getSsnUserName(this), cnTemp);
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

    private int saveStockRecord(ItemTransfer_Mdl mrTemp, Connection cnTemp, int maxVSno, boolean flagSystemGenerated) {
        int returnValue = 0, vSNo = 0;
        try {
            if (flagSystemGenerated) {
                vSNo = maxVSno;
            }
            for (GrdItemDetail_Mdl detail_Mdl : mrTemp.getListItemDetail()) {
                if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdTransfer().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdTransfer().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    stock_Mdl.setRefChallanNo(DmShared.xNull(mrTemp.getRefNo()));
                    stock_Mdl.setRefChallanDate(DmShared.dNull(mrTemp.getRefDate()));
                    stock_Mdl.setStockContraSubCode(DmShared.xNull(mrTemp.getTransferToTag()));
                    stock_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    stock_Mdl.setIbCode(detail_Mdl.getIbCode());

                    if (flagSystemGenerated == false) {
                        stock_Mdl.setSharedKeyFieldName(detail_Mdl.getV_SNo());
                        stock_Mdl.setSite_Code(DmShared.xNull(mrTemp.getDepartmentFromSite()));
                        stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentFromTag()));
                        stock_Mdl.setIsSystemGenerated("0");
                        stock_Mdl.setIsuQty(DmServer.vNull(detail_Mdl.getQuantity()));
                    } else {
                        stock_Mdl.setRefV_SNo(detail_Mdl.getV_SNo());
                        stock_Mdl.setSharedKeyFieldName(String.valueOf(++vSNo));
                        stock_Mdl.setSite_Code(DmShared.xNull(mrTemp.getDepartmentToSite()));
                        stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentToTag()));
                        stock_Mdl.setIsSystemGenerated("1");
                        stock_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));

                        StockFifo_Mdl stockFifo_Mdl = new StockFifo_Mdl();
                        stockFifo_Mdl.setSharedKeyFieldCode(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()));
                        stockFifo_Mdl.setSharedKeyFieldName(DmShared.xNull(stock_Mdl.getSharedKeyFieldName()));
                        stockFifo_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));
                        stockFifo_Mdl.setStockValueDate(DmShared.dNull(stock_Mdl.getV_Date()));

                        stock_Mdl.setStockFifo_Mdl(stockFifo_Mdl);

                    }

                    if (returnValue < DmShared.iNull(detail_Mdl.getV_SNo()).intValue()) {
                        returnValue = DmShared.iNull(detail_Mdl.getV_SNo()).intValue();
                    }

                    Stock_Server.saveStock(stock_Mdl, flagSystemGenerated ? DmEnum.StockEntryType.STOCK_TRANSFER_RECEIVE : DmEnum.StockEntryType.OTHER, cnTemp);

                }
            }
            return returnValue;
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
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

            sqlQuery = "select sh.docId, sh.recId, sh.v_No, vt.short_Name, sh.v_Date, depTo.name departmentTo, depFr.name departmentFrom, "
                    + "         sh.refNo, sh.refDate "
                    + "	from stockHead sh "
                    + "	left join voucher_Type vt on vt.v_Type = sh.v_Type "
                    + "	left join ( "
                    + "             select shd.docId, max(shd.departmentCode) departmentCode, max(shd.departmentToCode) departmentToCode "
                    + "                 from stockHeadDetail shd "
                    + "			group by shd.docId "
                    + "         ) shd on shd.docId = sh.docId "
                    + "	left join subGroup depTo on shd.departmentToCode = depTo.subCode "
                    + "	left join subGroup depFr on shd.departmentCode = depFr.subCode "
                    + "	where sh.site_Code = :siteCode and vt.category in (" + vCat + ")  and sh.v_prefix = :vPrefix ";
            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("vPrefix", DmServer.getSsnVoucherPrefix(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setDepartmentFrom(DmShared.xNull(rsTemp.getString("departmentFrom")));
                listNode.setDepartmentTo(DmShared.xNull(rsTemp.getString("departmentTo")));
                listNode.setRefNo(DmShared.xNull(rsTemp.getString("refNo")));
                listNode.setRefDate(DmShared.dNull(rsTemp.getDate("refDate")));
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
            sqlWithParam.setSQL(" select v_Type as code, description as name from voucher_Type where category in (" + vCat + ") and isActive = 1 ");
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
            sqlQuery = " select v_Type, description from voucher_Type where category in (" + vCat + ") and isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
            }
            rsTemp.close();
            psTemp.close();

            //Department
            sqlQuery = "select dm.subCode, sg.name"
                    + "     , :siteCode AS linkSite"
                    + "     from departmentMast dm "
                    + "     left join subGroup sg on sg.subCode = dm.subCode "
                    + "     where dm.subCode = :subCode and sg.isActive = 1 ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("subCode", STServer.getSsnDepartmentCode(this));
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentTag(DmShared.xNull(rsTemp.getString("subCode")));
                defaultValue.setDepartmentText(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("linkSite")));
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
            /*==============================<< Get Enviro Data >>====================================*/
            sqlQuery = "SELECT isNull(en.ph_daysToRestrictItemTransferWithoutApproval,0) as daysToRestrictItemTransferWithoutApproval, \n"
                    + " en.requisitionMandatoryInTransfer "
                    + "FROM enviro en\n"
                    + "  Where en.site_Code = :site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("site_Code", DmShared.xNull(DmServer.getSsnSiteCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDaysToRestrictItemTransferWithoutApproval(DmShared.xNull(rsTemp.getString("daysToRestrictItemTransferWithoutApproval")));
                defaultValue.setRequisitionMandatoryInTransfer(DmShared.xNull(rsTemp.getString("requisitionMandatoryInTransfer")));
            }
            rsTemp.close();
            psTemp.close();
            /*==============================<< **************************** >>====================================*/

            defaultValue.setCurrentDate(DmServer.getServerDateTime()[0]);
            defaultValue.setSiteCode(DmServer.getSsnSiteCode(this));

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
    public DmResultPageing<List<TxtHItem_Mdl>> getTxtHItemName(String departmentFrom, String departmentTo, String requisitionMandatoryInTransfer, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHItem_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHItem_Mdl> lstTemp = new ArrayList<TxtHItem_Mdl>();
        TxtHItem_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, departmentCSV = "", condition = "", itemCatFilteration = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            String voucherCategory = DmServer.getCodeViaManualCode(v_Type, "v_Type", "voucher_Type", "category", cnTemp);

            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);

            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition = " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }

            if (DmShared.iNull(departmentFrom).longValue() > 0) {
                departmentCSV = departmentFrom;
            }
            if (DmShared.iNull(departmentTo).longValue() > 0) {
                if (!departmentCSV.trim().equals("")) {
                    departmentCSV += ",";
                }
                departmentCSV += departmentTo;
            }

            if (DmShared.xNull(requisitionMandatoryInTransfer).trim().equalsIgnoreCase("1")
                    && DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.ITEM_TRANSFER)) {
                condition += " and 1=2 ";
            }

            sqlQuery = "SELECT  im.code, im.name, im.manualCode, um.unit skuName, vis.currentStock ,visi.toCurrentStock"
                    + ", im.propertyList, im.isBestBefore, um.decimalPlaces, im.purchaseUnitConversionRate  "
                    + "		FROM itemMast im  "
                    + "         LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + "		LEFT JOIN unitmast um ON im.sku = um.code  "
                    + "		LEFT JOIN ( "
                    + "                     SELECT iStk.itemCode, SUM(iStk.balanceQty) AS currentStock  "
                    + "                     FROM itemStock iStk "
                    + "                     WHERE iStk.site_Code = :site_Code AND iStk.departmentCode = :departmentFrom  "
                    + "                     GROUP BY iStk.itemCode "
                    + "		) AS vis ON im.code = vis.itemCode  "
                    + " 	LEFT JOIN ( "
                    + "                     SELECT iStk.itemCode, SUM(iStk.balanceQty) AS toCurrentStock  "
                    + "                     FROM itemStock iStk "
                    + "                     WHERE iStk.site_Code = :site_Code AND iStk.departmentCode = :departmentTo  "
                    + "                     GROUP BY iStk.itemCode "
                    + "		) AS visi ON im.code = visi.itemCode  "
                    + " 	LEFT JOIN ( "
                    + "                 SELECT irtd.code "
                    + "                 FROM itemRestrictedToDepartment irtd "
                    + "                 WHERE irtd.departmentCode in (" + DmServer.getParamList(":departments", DmShared.xNull(departmentCSV)) + ") "
                    + "		) virtd ON virtd.code = im.code "
                    + "		WHERE im.isActive = 1 "
                    + condition
                    + "		AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + "         AND virtd.code IS NULL "
                    + "         AND sl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentFrom", DmShared.iNull(departmentFrom).toString());
            sqlWithParam.setString("departmentTo", DmShared.iNull(departmentTo).toString());
            sqlWithParam.setStringList("departments", DmShared.xNull(departmentCSV));
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItem_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue());
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setStock(DmServer.vNull(rsTemp.getString("currentStock")));
                listNode.setToCurrentStock(DmServer.vNull(rsTemp.getString("toCurrentStock")));
                listNode.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, int allDepartment, boolean blnToEndOfFile, String department) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (allDepartment == 0) {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    condition += " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
                }
            }

            sqlWithParam.setSQL("select	dm.subCode as code, sg.name"
                    + "         , sgsl.site_Code AS linkSite "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "		left join subGroupNature as sgn on sgn.code = sg.nature "
                    + "		Left join subGroupSiteList as sgsl on sg.subCode=sgsl.subCode "
                    + "		where sg.isActive = 1 "
                    + "         and sgn.manualCode in ('CM','PR')"
                    + "         and  sgsl.site_Code = :siteCode "
                    + "         and dm.subCode <> :subCode " + condition);
            if (allDepartment == 0) {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                    sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
                }
            }
            sqlWithParam.setString("subCode", DmShared.iNull(department).toString());
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("linkSite")));
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
    public DmResultPageing<List<TxtHTransferTo_Mdl>> getTxtHTransferTo(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHTransferTo_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHTransferTo_Mdl> lstTemp = new ArrayList<>();
        TxtHTransferTo_Mdl listNode;
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
                    + " FROM subGroup sg"
                    + " LEFT JOIN subGroupSiteList sgsl ON sgsl.subCode = sg.subCode"
                    + " LEFT JOIN city c ON c.cityCode = sg.cityCode"
                    + "	LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.code IN (SELECT acf.Nature FROM AcFilteration acf WHERE acf.v_Type = :v_Type)"
                    + " AND sg.isActive = 1"
                    + " AND sgsl.site_Code = :site_Code";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("v_Type", DmShared.iNull(v_Type).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHTransferTo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setCityName(DmShared.xNull(rsTemp.getString("cityName")));
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

    private CompareProperties_Mdl iniItemBatchProperties(ItemTransfer_Mdl mrTempCurrent, ItemTransfer_Mdl mrTempPrevious) {
        CompareProperties_Mdl compareProperties_Mdl = new CompareProperties_Mdl();
        List<ItemBatch_Mdl> lstItemBatch_MdlCurnt = new ArrayList<>();
        List<ItemBatch_Mdl> lstItemBatch_MdlPrev = new ArrayList<>();

        lstItemBatch_MdlCurnt.clear();
        for (GrdItemDetail_Mdl detail_MdlCurrent : mrTempCurrent.getListItemDetail()) {
            ItemBatch_Mdl batch_Mdl = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlCurrent.getSharedKeyFieldCode()).longValue() != 0) {
                batch_Mdl.getIpm().setItemCode(DmShared.xNull(detail_MdlCurrent.getSharedKeyFieldCode()));
                batch_Mdl.setCode(DmShared.xNull(detail_MdlCurrent.getIbCode()));
                batch_Mdl.setReceiveQty(DmServer.vNull(DmShared.vNull(detail_MdlCurrent.getQuantity())));
                batch_Mdl.setReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentToTag()));
                lstItemBatch_MdlCurnt.add(batch_Mdl);
            }
        }

        lstItemBatch_MdlPrev.clear();
        for (GrdItemDetail_Mdl detail_MdlPrev : mrTempPrevious.getListItemDetail()) {
            ItemBatch_Mdl batch_MdlPrev = new ItemBatch_Mdl();
            if (DmShared.iNull(detail_MdlPrev.getSharedKeyFieldCode()).longValue() != 0) {
                batch_MdlPrev.getIpm().setItemCode(DmShared.xNull(detail_MdlPrev.getSharedKeyFieldCode()));
                batch_MdlPrev.setCode(DmShared.xNull(detail_MdlPrev.getIbCode()));
                batch_MdlPrev.setReceiveQty(DmServer.vNull(DmShared.vNull(detail_MdlPrev.getQuantity())));
                batch_MdlPrev.setReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));
                batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempPrevious.getDepartmentToTag()));
                lstItemBatch_MdlPrev.add(batch_MdlPrev);
            }
        }

        compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
        compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
        compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdTransfer().getPrefix()));
        compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdTransfer().getCounter().toString()));
        compareProperties_Mdl.setV_Type(DmShared.xNull(mrTempCurrent.getVoucherTypeCode()));
        compareProperties_Mdl.setCurrentReceiveDate(DmShared.dNull(mrTempCurrent.getVoucherDate()));
//            compareProperties_Mdl.setContraSubCode(DmShared.xNull(mrTempCurrent.getSupplierTag()));
        compareProperties_Mdl.setSite_Code(DmServer.getSsnSiteCode(this));
        compareProperties_Mdl.getCurrentIbm().addAll(lstItemBatch_MdlCurnt);
        compareProperties_Mdl.getPreviousIbm().addAll(lstItemBatch_MdlPrev);
        compareProperties_Mdl.setPreviousReceiveDate(DmShared.dNull(mrTempPrevious.getVoucherDate()));

        return compareProperties_Mdl;
    }

    @Override
    public DmResult<String> getIbCode(ItemProperty_Mdl mdl) {
        DmResult<String> dmrTemp = new DmResult();
        String ibCode = "";
        Connection cnTemp = null;
        ItemBatch_Mdl ibm;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            ibm = new ItemBatch_Mdl();
            ibm.setCode("");
            ibm.setIpm(mdl);
            ibCode = ItemBatch_Server.createItemBatch(ibm, cnTemp, this);

            cnTemp.close();
            dmrTemp.setData(ibCode);
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

    @Override
    public DmResult<List<GrdItemDetail_Mdl>> getItemDetail(String v_Date, String reqDocId, String itemIssueDocId, String fromDeptCode) {
        DmResult<List<GrdItemDetail_Mdl>> dmrTemp = new DmResult();

        try {
            dmrTemp.setData(getItemDetails(v_Date, reqDocId, itemIssueDocId, fromDeptCode).getGrdItemDetailList());
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    private ItemIssueWithRequisition_Mdl getItemDetails(String v_Date, String reqDocId, String itemIssueDocId, String fromDeptCode) throws Exception {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", recQty = "";
        ItemIssueWithRequisition_Mdl itemIssueWithRequisition = new ItemIssueWithRequisition_Mdl();
        GrdItemDetail_Mdl node;
        List<GrdItemDetail_Mdl> listDetail = new ArrayList<>();
        List<GrdItemDetail_Mdl> listNode;
        ItemProperty_Mdl propertyMdl;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(itemIssueDocId).equalsIgnoreCase("")) {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) AS reqQty, ";
                condition += " AND (CASE WHEN (mrqd.isStatus = 0) THEN 1 WHEN (mrqd.isStatus = 2 And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) + ISNULL(sd.quantity,0) > 0) THEN 1 ELSE 0 END) = 1 ";
            } else {
                recQty = " ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) AS reqQty, ";
                condition += " And ISNULL(mrqd.reqQty,0) - ISNULL(mrqd.issueQty,0) > 0 ";
                condition += " and mrqd.isStatus=0 ";
            }

            /*==============================<< Get Item detail (via list) >>===============================*/
            sqlQuery = " select mrq.docid,mrq.recid, mrqd.v_SNo, " + recQty
                    + "         mrqd.itemCode,im.name as itemName, "
                    + "		im.isBestBefore, "
                    + "         im.propertyList,um.unit as skuName, um.decimalPlaces AS skuDecimalPlaces, "
                    + "		im.sku as skuCode,im.purchaseUnitConversionRate,mrqd.colourCode,mrqd.sizeCode,\n"
                    + "         im.itemMakeCode, icm.name as colourName,ism.name as sizeName,imm.companyName as itemMakeName ,"
                    + "          vis.currentStock "
                    + "	from requisition mrq "
                    + "	left Join requisitionDetail as mrqd on mrqd.docId = mrq.docid  "
                    + "		LEFT JOIN ( "
                    + "                     SELECT iStk.itemCode, SUM(iStk.balanceQty) AS currentStock  "
                    + "                     FROM itemStock iStk "
                    + "                     WHERE iStk.site_Code = :siteCode AND iStk.departmentCode = :departmentFrom  "
                    + "                     GROUP BY iStk.itemCode "
                    + "		) AS vis ON mrqd.itemCode = vis.itemCode  "
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join itemMast im on im.code = mrqd.itemCode "
                    + " left join itemGroup ig on ig.code = im.itemGroup "
                    + "	left join unitMast um ON um.code = im.sku "
                    + "	left join itemColourMast icm ON icm.code = mrqd.colourCode "
                    + "	left join itemSizeMast ism ON ism.code = mrqd.sizeCode"
                    + " left join itemMakeMast imm ON imm.code=im.itemMakeCode"
                    + "	left join hsnMast hm on hm.code = im.hsnCode ";

            if (!DmShared.xNull(itemIssueDocId).equalsIgnoreCase("")) {
                sqlQuery += "LEFT JOIN ( "
                        + " SELECT shd.* "
                        + " from stockHeadDetail shd "
                        + " where shd.docid=:itemIssueDocId "
                        + " )as sd ON sd.requisitionDocId=mrqd.docId and sd.requisitionV_SNo=mrqd.V_SNo ";

            }
            sqlQuery += "	where mrq.docId = :docId  and ISNULL(mrq.approvedBy,'') <> '' and mrq.site_Code_To= :siteCode "
                    + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmServer.getSsnSiteCode(this));
            psTemp.setString("docId", reqDocId);
            psTemp.setString("departmentFrom", fromDeptCode);
            if (!DmShared.xNull(itemIssueDocId).equalsIgnoreCase("")) {
                psTemp.setString("itemIssueDocId", itemIssueDocId);
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {

                itemIssueWithRequisition.setRequisitionCode(DmShared.xNull(rsTemp.getString("docId")));
                itemIssueWithRequisition.setRequisitionName(DmShared.xNull(rsTemp.getString("recid")));

                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setRequisitionV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("reqQty")));
                node.setBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).intValue() == 1 ? true : false);
                node.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList"))); //??????
                node.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).toString());
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setRequisitionDocId(DmShared.xNull(rsTemp.getString("docid")));
                node.setRequisitionNo(DmShared.xNull(rsTemp.getString("recid")));
                node.setFromDeptStk(DmServer.vNull(rsTemp.getString("currentStock")));

                //======================< Set Item Properties >======================                
                propertyMdl = new ItemProperty_Mdl();
                propertyMdl.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                propertyMdl.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                propertyMdl.setItemCode(DmShared.xNull(rsTemp.getString("itemCode")));
                propertyMdl.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                propertyMdl.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                propertyMdl.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
//                propertyMdl.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
//                propertyMdl.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                propertyMdl.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                node.setPropertyMdl(propertyMdl);
                //======================< Set Item Properties Display >======================
                node.setPropertyDisplay(propertyMdl.getPropertiesCsv());
                if (DmShared.xNull(rsTemp.getString("propertyList")).trim().equals("") && DmShared.xNull(node.getPropertyDisplay()).trim().equals("")) {
                    ItemBatch_Mdl ibm = new ItemBatch_Mdl();
                    ibm.setCode("");
                    ibm.setIpm(propertyMdl);
                    node.setIbCode(ItemBatch_Server.getItemBatchForIssue(ibm, cnTemp));
                }
                //============================================================================
                itemIssueWithRequisition.getGrdItemDetailList().add(node);
                listDetail.add(node);
            }
            rsTemp.close();
            psTemp.close();

            listNode = fillBatchFIFO(listDetail, v_Date, fromDeptCode, false, cnTemp);
            itemIssueWithRequisition.setGrdItemDetailList(listNode);
            cnTemp.close();

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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return itemIssueWithRequisition;
    }

    private List<GrdItemDetail_Mdl> fillBatchFIFO(List<GrdItemDetail_Mdl> lstNode, String v_Date, String departmentCode, boolean isEstimateDetails, Connection cnTemp) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery;
        List<GrdItemDetail_Mdl> listNode = new ArrayList<>();
        GrdItemDetail_Mdl node;
        ItemIssueWithRequisition_Mdl itemIssueWithRequisition = new ItemIssueWithRequisition_Mdl();
        ItemProperty_Mdl propertyMdl;
        double balanceQty = 0.0;
        String requiredQty = "";
        double batchStockQty = 0.0;
        String selectedIbCodeSQL = "";
        boolean fillBatch = false;
        try {

            selectedIbCodeSQL = " SELECT 0 AS ibCode, 0 AS qty ";
//
            for (GrdItemDetail_Mdl node1 : lstNode) {
                balanceQty = DmShared.vNull(node1.getQuantity()).doubleValue();
                requiredQty = DmShared.vNull(node1.getQuantity()).toString();

                sqlQuery = " SELECT "
                        + "     vib.runningTotal "
                        + "   , vib.stockQuantity "
                        + "   , vib.ibCode "
                        + "   , vib.batchNo"
                        + "   , vib.manufacturingDate "
                        + "   , vib.expiryDate "
                        + "   , vib.saleRate "
                        + "   , vib.staffSaleRate "
                        + "   , vib.mrp "
                        + "   , vib.skuSaleRate "
                        + "   , vib.skuStaffSaleRate "
                        + "   , vib.skuMrp "
                        + " FROM ("
                        + "		SELECT "
                        + "              (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) as stockQuantity  "
                        + "		 ,SUM(ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) OVER (ORDER BY ib.expiryDate , (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) DESC, ib.code)  as runningTotal \n"
                        + "		 ,ib.code as ibCode ,ib.batchNo  "
                        + "		 ,ib.manufacturingDate  "
                        + "		 ,ib.expiryDate  "
                        + "		 ,ib.saleRate  "
                        + "		 ,ib.staffSaleRate  "
                        + "		 ,ib.mrp  "
                        + "		 ,ib.skuStaffSaleRate  "
                        + "		 ,ib.skuSaleRate  "
                        + "		 ,ib.skuMrp  "
                        + "		 FROM itembatch ib  "
                        + "		 LEFT JOIN itemBatchStock ibs ON ibs.ibCode=ib.code  "
                        + "		 LEFT JOIN ( "
                        + "			      SELECT ibTemp.ibCode, SUM(ibTemp.qty) AS qty "
                        + "			      FROM (" + selectedIbCodeSQL + ") AS ibTemp "
                        + "			      GROUP BY ibTemp.ibCode              "
                        + "			   ) AS ibt ON ibt.ibCode = ib.code "
                        + "              WHERE ib.itemCode=:itemCode "
                        + "              AND ISNULL(ibs.departmentCode,:departmentCode) = :departmentCode "
                        + "              AND ISNULL(ibs.site_Code,:site_Code) = :site_Code "
                        + "              AND (ISNULL(ibs.balanceQty,0) - ISNULL(ibt.qty,0)) > 0 "
                        //                        + "              AND ib.expiryDate > :v_Date "
                        + "	) as vib"
                        + " WHERE ISNULL(vib.runningTotal,0) < ISNULL(vib.stockQuantity,0) + :requiredQty ";

//                if (DmShared.iNull(node1.getIbCode()).longValue() > 0) {
//                    sqlQuery += " AND vib.ibCode = :ibCode ";
//                }
                sqlQuery += " ORDER BY vib.expiryDate , vib.stockQuantity DESC ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                psTemp.setDate("v_Date", DmServer.convertDate(v_Date));
                psTemp.setString("itemCode", DmShared.xNull(node1.getSharedKeyFieldCode()));
                psTemp.setString("requiredQty", DmShared.xNull(requiredQty));
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                psTemp.setString("departmentCode", DmShared.iNull(departmentCode).toString());
                if (DmShared.iNull(node1.getIbCode()).longValue() > 0) {
//                    psTemp.setString("ibCode", DmShared.iNull(node1.getIbCode()).toString());
                }
                rsTemp = psTemp.executeQuery();

                while (rsTemp.next()) {
                    if (balanceQty != 0) {
                        fillBatch = true;
                        node = new GrdItemDetail_Mdl();
                        itemIssueWithRequisition.setRequisitionCode(DmShared.xNull(itemIssueWithRequisition.getRequisitionCode()));
                        itemIssueWithRequisition.setRequisitionName(DmShared.xNull(itemIssueWithRequisition.getRequisitionName()));

                        node = new GrdItemDetail_Mdl();
                        node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));
                        node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));
                        node.setRequisitionV_SNo(DmShared.xNull(node1.getRequisitionV_SNo()));
                        node.setQuantity(DmServer.vNull(node1.getQuantity()));
//                        node.setBestBefore(DmShared.iNull(node1.getB).intValue() == 1 ? true : false);
                        node.setItemProperty(DmShared.xNull(node1.getItemProperty())); //??????
                        node.setPurchaseUnitConversionRate(DmServer.vNull(node1.getPurchaseUnitConversionRate()));
                        node.setSkuDecimalPlaces(DmShared.iNull(node1.getSkuDecimalPlaces()).toString());
                        node.setSkuName(DmShared.xNull(node1.getSkuName()));
                        node.setRequisitionDocId(DmShared.xNull(node1.getRequisitionDocId()));
                        node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                        node.setFromDeptStk(DmServer.vNull(node1.getFromDeptStk()));

                        //======================< Set Item Properties >======================                
                        propertyMdl = new ItemProperty_Mdl();
                        propertyMdl.setColourCode(DmShared.xNull(node1.getPropertyMdl().getColourCode()));
                        propertyMdl.setColourName(DmShared.xNull(node1.getPropertyMdl().getColourName()));
                        propertyMdl.setItemCode(DmShared.xNull(node1.getPropertyMdl().getItemCode()));
                        propertyMdl.setItemName(DmShared.xNull(node1.getPropertyMdl().getItemName()));
                        propertyMdl.setSizeCode(DmShared.xNull(node1.getPropertyMdl().getSizeCode()));
                        propertyMdl.setSizeName(DmShared.xNull(node1.getPropertyMdl().getSizeName()));
//                      propertyMdl.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
//                      propertyMdl.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                        propertyMdl.setSkuQtyForRate(DmServer.vNull(node1.getPropertyMdl().getSkuQtyForRate()));
                        node.setPropertyMdl(propertyMdl);
//                        //======================< Set Item Properties Display >======================
//                        node.setPropertyDisplay(propertyMdl.getPropertiesCsv());
//                        if (DmShared.xNull(node1.getItemProperty()).trim().equals("") && DmShared.xNull(node.getPropertyDisplay()).trim().equals("")) {
//                            ItemBatch_Mdl ibm = new ItemBatch_Mdl();
//                            ibm.setCode("");
//                            ibm.setIpm(propertyMdl);
//                            node.setIbCode(ItemBatch_Server.getItemBatchForIssue(ibm, cnTemp, this));
//                        }
                        //============================================================================

//                        if (isEstimateDetails && node1.getMedicineMdl().isAllowNegativeStock()) {
//                            fillBatch = false;
//                        }
                        if (fillBatch) {

                            if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() < balanceQty) {
                                node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                                node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity"), DmShared.getNumberFormat(DmShared.iNull(node1.getSkuDecimalPlaces()).intValue())));
                                balanceQty = balanceQty - DmShared.vNull(node.getQuantity()).doubleValue();
                                batchStockQty = 0;
                            } else if (DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() == balanceQty) {
                                node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity")));
                                node.setQuantity(DmServer.vNull(rsTemp.getString("stockQuantity"), DmShared.getNumberFormat(DmShared.iNull(node1.getSkuDecimalPlaces()).intValue())));
                                balanceQty = 0;
                                batchStockQty = 0;
                            } else {
                                node.setQuantity(DmServer.vNull(balanceQty));
                                node.setQuantity(DmServer.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(node1.getSkuDecimalPlaces()).intValue())));
                                batchStockQty = DmShared.vNull(rsTemp.getString("stockQuantity")).doubleValue() - balanceQty;
                                balanceQty = 0;
                            }
//                            node.setQuantity(DmServer.vNull(node.getQuantity()));
//                            double stockQty = DmShared.vNull(rsTemp.getString("stockQuantity")) / DmShared.vNull(node1.getPurchaseUnitConversionRate());
//                            node.setQuantity(DmServer.vNull(stockQty));
                            //<editor-fold defaultstate="collapsed" desc="BATCH Detail">

                            node.setIbCode(DmShared.iNull(rsTemp.getString("ibCode")).toString());
                            propertyMdl.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                            propertyMdl.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                            propertyMdl.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                            propertyMdl.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                            propertyMdl.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp")));
                            node.setPropertyMdl(propertyMdl);
//                            if (!isEstimateDetails) {
//                                setSaleRate(node, DmShared.xNull(rsTemp.getString("saleRate")), DmShared.xNull(rsTemp.getString("skuSaleRate")), saleOn, mrpDiscountAllowFromVoucher, allowMrpDiscountFromOrganization, isIgnoreMrpDiscount, DmShared.xNull(rsTemp.getString("staffSaleRate")), DmShared.xNull(rsTemp.getString("skuStaffSaleRate")), saleOnAsPerVType);
//                            } else {
//                                propertyMdl.setRate(DmServer.vNull(node1.getRate(), "0.00"));
//                                propertyMdl.setSkuRate(DmServer.vNull(node1.getSkuRate()));
////
//                            }
//</editor-fold>
                            selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantity()) + " AS qty ";

                            //<editor-fold defaultstate="collapsed" desc="Quantity in saleUnit">
                            if (DmShared.vNull(node.getQuantity()) > 0) {
                                double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(node.getPurchaseUnitConversionRate());
                                node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(node.getPropertyMdl().getAllowDecimalPlaces()).intValue())));

                            }

//</editor-fold>
                        } else {

//                            lstIbCode.add(DmShared.iNull(medicineMdl.getIbCode()).longValue());
                            node.setQuantity(DmServer.vNull(node1.getQuantity()));
//                            node.setQuantitySKU(DmServer.vNull(DmShared.vNull(node1.getQuantitySKU()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
//                            node.setTotalQuantitySKU(DmServer.vNull(DmShared.vNull(node1.getTotalQuantitySKU()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                            balanceQty = 0;
                            batchStockQty = 0;

                            selectedIbCodeSQL += " UNION ALL SELECT " + node1.getIbCode() + " AS ibCode, " + DmServer.vNull(node1.getQuantity()) + " AS qty ";

                            //<editor-fold defaultstate="collapsed" desc="BATCH Detail">
                            node.setIbCode(DmShared.iNull(node1.getIbCode()).toString());
                            propertyMdl.setBatchNo(DmShared.xNull(node1.getPropertyMdl().getBatchNo()));
                            propertyMdl.setManufacturingDate(DmShared.dNull(node1.getPropertyMdl().getManufacturingDate()));
                            propertyMdl.setExpiryDate(DmShared.dNull(node1.getPropertyMdl().getExpiryDate()));

//                            node.setDiscountPer(DmServer.vNull(node1.getDiscountPer()));
//                            node.setDiscountPer2(DmServer.vNull(node1.getDiscountPer2()));
                            propertyMdl.setMrp(DmServer.vNull(node1.getPropertyMdl().getMrp(), "0.00"));
                            propertyMdl.setSkuMrp(DmServer.vNull(node1.getPropertyMdl().getSkuMrp()));

                            //</editor-fold>
                            //<editor-fold defaultstate="collapsed" desc="Quantity in saleUnit">
                            if (DmShared.vNull(node.getQuantity()) > 0) {
                                double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(node.getPurchaseUnitConversionRate());
                                node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(node.getPropertyMdl().getAllowDecimalPlaces()).intValue())));
                            }
                        }

                        listNode.add(node);

                    }
                }
                rsTemp.close();
                psTemp.close();

                if (balanceQty != 0) {
                    node = new GrdItemDetail_Mdl();
                    itemIssueWithRequisition.setRequisitionCode(DmShared.xNull(itemIssueWithRequisition.getRequisitionCode()));
                    itemIssueWithRequisition.setRequisitionName(DmShared.xNull(itemIssueWithRequisition.getRequisitionName()));

                    node = new GrdItemDetail_Mdl();
                    node.setSharedKeyFieldCode(DmShared.xNull(node1.getSharedKeyFieldCode()));
                    node.setSharedKeyFieldName(DmShared.xNull(node1.getSharedKeyFieldName()));
                    node.setRequisitionV_SNo(DmShared.xNull(node1.getRequisitionV_SNo()));
                    node.setQuantity(DmServer.vNull(node1.getQuantity()));
//                        node.setBestBefore(DmShared.iNull(node1.getB).intValue() == 1 ? true : false);
                    node.setItemProperty(DmShared.xNull(node1.getItemProperty())); //??????
                    node.setPurchaseUnitConversionRate(DmServer.vNull(node1.getPurchaseUnitConversionRate()));
                    node.setSkuDecimalPlaces(DmShared.iNull(node1.getSkuDecimalPlaces()).toString());
                    node.setSkuName(DmShared.xNull(node1.getSkuName()));
                    node.setRequisitionDocId(DmShared.xNull(node1.getRequisitionDocId()));
                    node.setRequisitionNo(DmShared.xNull(node1.getRequisitionNo()));
                    node.setFromDeptStk(DmServer.vNull(node1.getFromDeptStk()));

                    //======================< Set Item Properties >======================                
                    propertyMdl = new ItemProperty_Mdl();
                    propertyMdl.setColourCode(DmShared.xNull(node1.getPropertyMdl().getColourCode()));
                    propertyMdl.setColourName(DmShared.xNull(node1.getPropertyMdl().getColourName()));
                    propertyMdl.setItemCode(DmShared.xNull(node1.getPropertyMdl().getItemCode()));
                    propertyMdl.setItemName(DmShared.xNull(node1.getPropertyMdl().getItemName()));
                    propertyMdl.setSizeCode(DmShared.xNull(node1.getPropertyMdl().getSizeCode()));
                    propertyMdl.setSizeName(DmShared.xNull(node1.getPropertyMdl().getSizeName()));
//                      propertyMdl.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
//                      propertyMdl.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                    propertyMdl.setSkuQtyForRate(DmServer.vNull(node1.getPropertyMdl().getSkuQtyForRate()));
                    node.setPropertyMdl(propertyMdl);
                    //======================< Set Item Properties Display >======================
                    node.setPropertyDisplay(propertyMdl.getPropertiesCsv());
                    if (DmShared.xNull(node1.getItemProperty()).trim().equals("") && DmShared.xNull(node.getPropertyDisplay()).trim().equals("")) {
                        ItemBatch_Mdl ibm = new ItemBatch_Mdl();
                        ibm.setCode("");
                        ibm.setIpm(propertyMdl);
                        node.setIbCode(ItemBatch_Server.getItemBatchForIssue(ibm, cnTemp));
                    }
//
                    node.setQuantity(DmServer.vNull(balanceQty));
//                    node.setQuantitySKU(DmServer.vNull(balanceQty, DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
//                    node.setTotalQuantitySKU(DmServer.vNull(node.getQuantitySKU(), DmShared.getNumberFormat(DmShared.iNull(medicineMdl.getAllowSkuDecimalPlaces()).intValue())));
                    balanceQty = 0;
//                    //<editor-fold defaultstate="collapsed" desc="BATCH Detail">
//                    //</editor-fold>
                    selectedIbCodeSQL += " UNION ALL SELECT " + node.getIbCode() + " AS ibCode, " + DmServer.vNull(node.getQuantity()) + " AS qty ";
//                    //<editor-fold defaultstate="collapsed" desc="Quantity in sale unit">
//                    if (DmShared.iNull(medicineMdl.getSkuCode()).longValue() != DmShared.iNull(medicineMdl.getSaleUnitCode()).longValue()) {
                    if (DmShared.vNull(node.getQuantity()) > 0) {
                        double qty = DmShared.vNull(node.getQuantity()) / DmShared.vNull(node.getPurchaseUnitConversionRate());
                        node.setQuantity(DmServer.vNull(qty, DmShared.getNumberFormat(DmShared.iNull(node.getPropertyMdl().getAllowDecimalPlaces()).intValue())));
                    }

                    listNode.add(node);
//
                }
            }

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
        return listNode;
    }

    @Override
    public DmResultPageing<List<TxtHRequisitionNo_Mdl>> getTxtHRequisitionNo(String departmentToCode, String departmentFromCode, String voucherDate, String issueDocId, String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHRequisitionNo_Mdl>> rstTemp = new DmResultPageing<List<TxtHRequisitionNo_Mdl>>();
        List<TxtHRequisitionNo_Mdl> lstTemp = new ArrayList<TxtHRequisitionNo_Mdl>();
        TxtHRequisitionNo_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, condition = "", joinItemIssue = "", conditionItemIssue = "", voucherCategory = "";
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            voucherCategory = DmServer.getCodeViaManualCode(v_Type, "v_Type", "voucher_Type", "category", cnTemp);

            if (DmShared.xNull(voucherCategory).trim().equalsIgnoreCase(pvc.SERVICE_TRANSFER)) {
                condition += " and 1=2 ";
            }
            if (DmShared.dNull(voucherDate).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Voucher date is mandatory.");
            }
            if (DmShared.iNull(departmentToCode).longValue() > 0) {
                condition += " and mrq.departmentToCode = :departmentToCode ";
            }
            if (DmShared.iNull(departmentFromCode).longValue() > 0) {
                condition += " and mrq.departmentCode = :departmentFromCode ";
            }

            if (DmShared.iNull(issueDocId).longValue() != 0) {
                joinItemIssue = " LEFT JOIN stockHeadDetail sd ON sd.requisitionDocId = rd.docId AND sd.requisitionV_SNo = rd.v_SNo";
                conditionItemIssue += " WHERE CASE WHEN rd.isStatus = 0 AND ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 THEN 1 ELSE CASE WHEN rd.isStatus = 2 AND ISNULL(sd.docid,0) = :issueDocId  THEN 1 ELSE 0 END  END = 1 ";
            } else {
                conditionItemIssue += " WHERE rd.isStatus = 0 AND ISNULL(rd.reqQty,0) - ISNULL(rd.issueQty,0) > 0 ";
            }

            sqlQuery = " select mrq.docid, mrq.recid, mrq.departmentCode, dm.name as department, rbc.name as requestedByName,mrq.requestedByCode, vt.short_Name,"
                    + "         mrq.v_No,  mrq.v_Date, mrq.departmentToCode, dm1.name departmentTo "
                    + "	FROM requisition mrq "
                    + "	INNER JOIN ("
                    + "     SELECT rd.docId"
                    + "     FROM requisitionDetail rd" + joinItemIssue + conditionItemIssue
                    + "     GROUP BY rd.docId"
                    + "	) mrqd on mrqd.docId = mrq.docid"
                    + "	left join voucher_Type vt on vt.v_Type = mrq.v_Type "
                    + "	left join subGroup dm on dm.subCode = mrq.departmentCode "
                    + "	left join subGroup dm1 on dm1.subCode = mrq.departmentToCode "
                    + "	left join subGroup rbc on rbc.subCode =	mrq.requestedByCode "
                    + "	where vt.category = " + getVoucherCategory(new ProjectVoucherCategory().INVENTORY_REQUISITION) + " "
                    + " and ISNULL(mrq.approvedBy,'') <> '' "
                    + " and mrq.site_Code_To = :siteCode  "
                    + " and mrq.v_Date <= :voucherDate " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
            sqlWithParam.setDate("voucherDate", DmServer.convertDate(voucherDate));

            if (DmShared.iNull(departmentToCode).longValue() > 0) {
                sqlWithParam.setString("departmentToCode", DmShared.iNull(departmentToCode).toString());
            }
            if (DmShared.iNull(departmentFromCode).longValue() > 0) {
                sqlWithParam.setString("departmentFromCode", DmShared.iNull(departmentFromCode).toString());
            }
            if (DmShared.iNull(issueDocId).longValue() != 0) {
                sqlWithParam.setString("issueDocId", DmShared.iNull(issueDocId).toString());
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHRequisitionNo_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                listNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                listNode.setDepartmentName(DmShared.xNull(rsTemp.getString("department")));
                listNode.setDepartmentToCode(DmShared.xNull(rsTemp.getString("departmentToCode")));
                listNode.setDepartmentToName(DmShared.xNull(rsTemp.getString("departmentTo")));
                listNode.setRequisitionBy(DmShared.xNull(rsTemp.getString("requestedByName")));
                listNode.setRequisitionByCode(DmShared.xNull(rsTemp.getString("requestedByCode")));
                listNode.setRequisitionDate(DmShared.dNull(rsTemp.getDate("v_Date")));
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

    private String getVoucherCategory(String voucherCategory) {
        String returnValue = "'" + voucherCategory + "'";
        return returnValue;
    }

    private void manageRequisitionStatus(String docId, boolean isDelete, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null;
        String sqlQuery, rqDocIds = "";
        double reqQty, issueQty, billQty;
        Integer isStatus;

        try {
            sqlQuery = "select	req.docId as rqDocId, reqd.v_SNo AS rqV_SNo, mbd.Qty as billQty, reqd.reqQty as reqQty, reqd.issueQty, reqd.isStatus, req.approvedBy,  "
                    + "        	req.recId as reqNo, vt.short_name as reqType"
                    + "        	from requisitionDetail reqd  "
                    + "        	left join requisition req on req.docId = reqd.docId  "
                    + "        	left join voucher_type vt on vt.v_type = req.v_Type  "
                    + "        	inner join (  "
                    + "        			select	mbd.requisitionDocId, mbd.requisitionV_SNo, sum(mbd.quantity) as Qty  "
                    + "        			from	stockHeadDetail mbd  "
                    + "        			where mbd.docId = :docId  "
                    + "        			group by mbd.requisitionDocId, mbd.requisitionV_SNo  "
                    + "        	) mbd on reqd.docId = mbd.requisitionDocId and reqd.v_SNo = mbd.requisitionV_SNo  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                isStatus = DmShared.iNull(rsTemp.getString("isStatus")).intValue();
                if (DmShared.xNull(rsTemp.getString("approvedBy")).trim().equalsIgnoreCase("")) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition is not Approved");
                }
                if (isStatus.intValue() == 1) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition is Cancelled");
                }
                reqQty = DmShared.vNull(rsTemp.getString("reqQty")).doubleValue();
                issueQty = DmShared.vNull(rsTemp.getString("issueQty")).doubleValue();
                billQty = DmShared.vNull(rsTemp.getString("billQty")).doubleValue();

                if (isDelete) {
                    issueQty -= billQty;
                } else {
                    issueQty += billQty;
                }

                if (issueQty > reqQty) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition overflows by " + DmServer.vNull(issueQty - reqQty));
                } else if (issueQty < 0) {
                    throw new DmException(500, DmShared.xNull(rsTemp.getString("rqDocId")), DmShared.xNull(rsTemp.getString("rqV_SNo")), "Requisition overflows by " + DmServer.vNull(issueQty));
                } else if (issueQty == reqQty) {
                    isStatus = 2;
                } else {
                    isStatus = 0;
                }

                if (!rqDocIds.equalsIgnoreCase("")) {
                    rqDocIds += ",";
                }
                rqDocIds += DmShared.iNull(rsTemp.getString("rqDocId")).toString();

                sqlQuery = "update requisitionDetail set "
                        + " isStatus = :isStatus,"
                        + " issueQty = :issueQty "
                        + " where docId = :docId and v_SNo = :v_SNo";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp1.setString("docId", DmShared.iNull(rsTemp.getString("rqDocId")).toString());
                psTemp1.setString("v_SNo", DmShared.iNull(rsTemp.getString("rqV_SNo")).toString());
                psTemp1.setString("isStatus", DmShared.iNull(isStatus).toString());
                psTemp1.setString("issueQty", DmServer.vNull(issueQty));
                psTemp1.execute();
                psTemp1.close();
            }
            rsTemp.close();
            psTemp.close();

            if (!rqDocIds.equalsIgnoreCase("")) {
                sqlQuery = " update requisition set u_EntDt_LatestLine = :uEntDt where docId in (" + rqDocIds + ") ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
                psTemp.close();
            }

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp1 != null) {
                    psTemp1.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public DmResult<List<ToolTip_Mdl>> getToolTipShowStock(String itemCode, String ibCode, String fromDepartment, String toDepartment) {
        DmResult<List<ToolTip_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, condition = "";
        ToolTip_Mdl node;
        List<ToolTip_Mdl> lstTemp = new ArrayList<>();
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (DmShared.iNull(ibCode).longValue() != 0) {
                condition += " AND iStk.ibCode = :ibCode ";
            }
            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "SELECT sg.Name AS departmentName "
                    + ",  vIStk.itemName "
                    + ",  vIStk.currentStock "
                    + ",  vIStk.skuName "
                    + ",  vIStk.decimalPlaces "
                    + " FROM subGroup sg   "
                    + " LEFT JOIN ("
                    + "     SELECT iStk.departmentCode"
                    + "     , MAX(im.Name) AS itemName "
                    + "     , SUM(iStk.balanceQty) AS currentStock "
                    + "     , MAX(um.unit) as skuName "
                    + "     , MAX(um.decimalPlaces) as decimalPlaces "
                    + "     FROM itemStock iStk "
                    + "     LEFT JOIN itemMast im ON im.code = iStk.itemCode "
                    + "     LEFT JOIN unitMast um ON um.code = im.sku "
                    + "     WHERE iStk.itemCode = :itemCode "
                    + "     AND iStk.site_Code = :site_Code "
                    + "     AND iStk.departmentCode IN (:fromDepartment, :toDepartment)  " + condition
                    + "     GROUP BY iStk.itemCode, iStk.departmentCode"
                    + "     ) vIStk ON sg.subCode = vIStk.departmentCode   "
                    + " WHERE sg.subCode IN (:fromDepartment, :toDepartment)  "
                    + " ORDER BY CASE WHEN sg.subCode = :fromDepartment THEN 1 WHEN sg.subCode = :toDepartment THEN 2 ELSE 99999 END ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            psTemp.setString("fromDepartment", DmShared.iNull(fromDepartment).toString());
            psTemp.setString("toDepartment", DmShared.iNull(toDepartment).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            if (DmShared.iNull(ibCode).longValue() != 0) {
                psTemp.setString("ibCode", DmShared.iNull(ibCode).toString());
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new ToolTip_Mdl();
                node.setCaption(DmShared.xNull(rsTemp.getString("departmentName")));
                int decimalPlaces = DmShared.iNull(rsTemp.getString("decimalPlaces")).intValue();
                node.setValue(DmJasper.formatAmount(BigDecimal.valueOf(DmShared.vNull(rsTemp.getString("currentStock")).doubleValue()), decimalPlaces, false) + " " + DmShared.xNull(rsTemp.getString("skuName")));
                lstTemp.add(node);
            }

            rsTemp.close();
            psTemp.close();
            dmrTemp.setData(lstTemp);
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
    public DmResult<String> setApproval(String approvedBy, boolean isApprove, String docId) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmSavePoint spBegin = null;
        dmrTemp.setData("");
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            spBegin = new DmSavePoint(cnTemp);

            if (isApprove) {
                STApprovals.itemTransferApproval(WalkThroughEnum.ACCEPT, docId, approvedBy, cnTemp);
            } else {
                STApprovals.itemTransferApproval(WalkThroughEnum.DECLINE, docId, approvedBy, cnTemp);
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

    @Override
    public DmResult<String> walkThrough(WalkThrough_Mdl walkThrough) {
        final DmResult<String> dmrTemp = new DmResult();

        try {
            WalkThrough_Server.walkThrough(walkThrough, new IWalkThrough() {
                @Override
                public void changeStatus(WalkThroughEnum walkThroughEnum, String keyCode, String userName, Connection cnTemp) throws Exception {
                    STApprovals.itemTransferApproval(walkThroughEnum, keyCode, userName, cnTemp);
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

    private WalkThroughDefault_Mdl getWalkThroughFor(ItemTransfer_Mdl mrTemp) {
        WalkThroughDefault_Mdl walkThroughDefault = new WalkThroughDefault_Mdl();

        walkThroughDefault.setDepartmentCode(mrTemp.getDepartmentFromTag());
        walkThroughDefault.setSiteCode(DmServer.getSsnSiteCode(this));
        walkThroughDefault.setUserName(DmServer.getSsnUserName(this));
        walkThroughDefault.setValueUpto("");
        walkThroughDefault.setvType(mrTemp.getVoucherTypeCode());

        return walkThroughDefault;
    }

    public DmResult<Boolean> getIsExisPendingTransferForApproval(String vType, String vDate, String departmentCode, String daysToRestrictItemTransferWithoutApproval) {
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, sqlCondition;
        Boolean isPendingRequest = false;
        DmResult<Boolean> dmrTemp = new DmResult<>();

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlCondition = "";
            if (DmShared.iNull(daysToRestrictItemTransferWithoutApproval).intValue() != 0) {
                sqlCondition = " and sh.v_Date <=DATEADD(d,-" + DmShared.iNull(daysToRestrictItemTransferWithoutApproval).intValue() + ",:vDate) ";
            } else {
                sqlCondition = " and 1=2 ";
            }
            sqlQuery = " select count(sh.docID) as pendingForApproval  \n "
                    + "		from stockHead sh \n "
                    + "         left Join stockHeadDetail shd on sh.docid=shd.docid \n"
                    + "		where isNull(sh.approvedBy,'')='' \n"
                    + "         and sh.site_Code=:siteCode and shd.departmentToCode=:departmentCodeTo and sh.v_Type=:vType \n"
                    + sqlCondition;

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", DmShared.iNull(DmServer.getSsnSiteCode(this)).toString());
            psTemp.setString("departmentCodeTo", DmShared.xNull(departmentCode));
            psTemp.setString("vType", DmShared.xNull(vType));
            if (DmShared.iNull(daysToRestrictItemTransferWithoutApproval).intValue() != 0) {
                psTemp.setDate("vDate", DmServer.convertDate(vDate));
            }

            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("pendingForApproval")).trim().equalsIgnoreCase("0")) {
                    isPendingRequest = true;
                }
            }

            if (DmShared.iNull(daysToRestrictItemTransferWithoutApproval).intValue() == 0) {
                isPendingRequest = false;
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(isPendingRequest);
            dmrTemp.setStatus(isPendingRequest);
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(e.getMessage());
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
