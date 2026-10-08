package aarogya.inventory.wipToStockConversion.server;

import aarogya.inventory.wipToStockConversion.server.*;
import aarogya.inventory.wipToStockConversion.shared.WipToStockConversion_Srv;
import aarogya.inventory.wipToStockConversion.shared.GrdItemDetail_Mdl;
import aarogya.inventory.wipToStockConversion.shared.WipToStockConversion_Mdl;
import aarogya.inventory.wipToStockConversion.shared.TxtHItemName_Mdl;
import aarogya.inventory.wipToStockConversion.shared.WipToStockConversion_Vld;
import aarogya.inventory.wipToStockConversion.shared.Find_Mdl;
import aarogya.inventory.wipToStockConversion.shared.DefaultValue_Mdl;
import aarogya.inventory.wipToStockConversion.shared.TxtHProject_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.itemBatch.shared.CompareProperties_Mdl;
import aarogya.selfToolkit.itemBatch.shared.ItemBatch_Mdl;
import dataman.dmToolkit.stock.shared.StockFifo_Mdl;
import dataman.dmToolkit.stock.shared.Stock_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.modifyReason.server.ModifyReason_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.accountPosting.server.AccountPosting_Server;
import dataman.dmToolkit.accountPosting.shared.AccountPosting_Mdl;
import dataman.dmToolkit.stock.server.Stock_Server;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WipToStockConversion_SrvImpl extends DmRSSPrivate implements WipToStockConversion_Srv {

    private final String vCat = new ProjectVoucherCategory().WIP_TO_STOCK_CONVERSION;
    private final String voucherTypefieldName = "sh.V_Type", voucherDateFieldName = "sh.V_Date";

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

            sqlQuery = " select sh.docId "
                    + "         from stockHead as sh "
                    + "         left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "         where vt.category = '" + vCat + "' and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
            if (!sqlCondition.equalsIgnoreCase("")) {
                sqlQuery += "and " + sqlCondition;
            }

            sqlQuery += "    order by sh.v_Date desc, vt.short_Name, sh.v_No desc, sh.recId  ";

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
    public DmResult<WipToStockConversion_Mdl> getMoveRec(String strKey) {
        DmResult<WipToStockConversion_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        WipToStockConversion_Mdl mrTemp = new WipToStockConversion_Mdl();
        dmrTemp.setData(mrTemp);
        List<GrdItemDetail_Mdl> lstNode = new ArrayList<GrdItemDetail_Mdl>();
        GrdItemDetail_Mdl node;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*==============================<< Get header detail >>===============================*/
            sqlQuery = "select	sh.docId, sh.recId, sh.v_No, sh.recIdPrefix, sh.v_Date, sh.v_Type, vt.description, sh.balancingAc, \n"
                    + "         sh.preparedBY, sh. preparedDate, sh. u_EntDt, sh.u_Name,st.departmentCode, sgd.name as departmentName, \n"
                    + "		st.subCode as receivedByCode,sh.refDate,sh.refNo,sh.remark,sgrb.name receivedByName, \n"
                    + "		sh.projectCode,sh.projectAcCode,pm.name as projectName,sgpac.name as projectAcName,st.oldTotalStkValue \n"
                    + "		from stockHead as sh \n"
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type \n"
                    + "		left join projectMast pm on sh.projectCode = pm.code \n"
                    + "		left join subGroup as sgpac on sh.projectAcCode = sgpac.subCode \n"
                    + "		left join ( \n"
                    + "                     select s.docId, max(s.departmentCode) as departmentCode, \n"
                    + "                     max(subCode) as subCode,isnull(sum(s.skuValue),0) as oldTotalStkValue \n"
                    + "                     from stockHeadDetail s \n"
                    + "                     where s.docId = :docId \n"
                    + "                     group by s.docId \n"
                    + "		)   as st on sh.docId = st.docId \n"
                    + "		left join subGroup as sgd on st.departmentCode = sgd.subCode \n"
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode \n"
                    + "		where sh.docId = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("docId")).toString());
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemReceive().setRecIdValue(DmShared.xNull(rsTemp.getString("recId")));
                mrTemp.getRecIdItemReceive().setCounter(DmShared.iNull(rsTemp.getString("v_No")).longValue());
                mrTemp.getRecIdItemReceive().setPrefix(DmShared.xNull(rsTemp.getString("recIdPrefix")));
                mrTemp.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                mrTemp.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("v_Type")));
                mrTemp.setVoucherTypeName(DmShared.xNull(rsTemp.getString("description")));
                mrTemp.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
                mrTemp.setDepartmentTag(DmShared.xNull(rsTemp.getString("departmentCode")));
                mrTemp.setDepartmentText(DmShared.xNull(rsTemp.getString("departmentName")));
                mrTemp.setProjectTag(DmShared.xNull(rsTemp.getString("projectCode")));
                mrTemp.setProjectText(DmShared.xNull(rsTemp.getString("projectName")));
                mrTemp.setProjectAcTag(DmShared.xNull(rsTemp.getString("projectAcCode")));
                mrTemp.setProjectAcText(DmShared.xNull(rsTemp.getString("projectAcName")));
                mrTemp.setRemarkHeader(DmShared.xNull(rsTemp.getString("remark")));
                mrTemp.setOldTotalStkValue(DmShared.xNull(rsTemp.getString("oldTotalStkValue")));
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_Name")), rsTemp.getTimestamp("u_EntDt")));

            }
            rsTemp.close();
            psTemp.close();

            /*==============================<< Get Item grid detail >>===============================*/
            sqlQuery = " Select	shd.itemCode, im.name as itemName, um.unit as skuName, shd.skuValue,shd.quantity , "
                    + " shd.remark, shd.ibCode, shd.batchNo, shd.lotNo, shd.expiryDate, shd.manufacturingDate, shd.mrp, im.propertyList ,  "
                    + " DATEDIFF(day, shd.manufacturingDate, shd.expiryDate) AS bestBeforeDays,  shd.colourCode, shd.sizeCode,shd.itemMakeCode , "
                    + " icm.name AS colourName, ism.name AS sizeName,  um.decimalPlaces,im.isBestBefore, shd.v_SNo,shd.skuMrp  "
                    + ",shd.skuRate "
                    + ",shd.rate "
                    + ",shd.skuValue "
                    + ",shd.skuQtyForRate "
                    + ",shd.unitCode as purchaseUnitCode "
                    + ",shd.unitQuantity "
                    + ",im.sku as skuCode "
                    + " ,imm.companyName as itemMakeName"
                    + ",ump.unit as purchaseUnitName "
                    + ",ump.decimalPlaces as purchaseUnitDecimalPlaces "
                    + ",shd.oh_Ac_Item "
                    + " FROM stockHeadDetail  shd  "
                    + " left join itemMast as im on shd.itemCode = im.code  "
                    + " left join unitMast as um on im.sku = um.code  "
                    + " left join unitMast as ump on shd.unitCode = ump.code  "
                    + " LEFT JOIN itemColourMast icm ON shd.colourCode = icm.code  "
                    + " LEFT JOIN itemSizeMast ism ON shd.sizeCode = ism.code "
                    + " LEFT JOIN itemMakeMast imm ON imm.code = shd.itemMakeCode "
                    + " where shd.docId = :docId "
                    + " order by shd.v_SNo ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdItemDetail_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemCode")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setV_SNo(DmShared.xNull(rsTemp.getString("v_SNo")));
                node.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                node.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                node.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                node.setRemark(DmShared.xNull(rsTemp.getString("remark")));
                node.setQuantity(DmServer.vNull(rsTemp.getString("quantity")));
                node.setPropertyList(DmShared.xNull(rsTemp.getString("propertyList")));
                node.setIbCode(DmShared.xNull(rsTemp.getString("ibCode")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                node.setBestBeforeDays(DmShared.xNull(rsTemp.getString("bestBeforeDays")));
                node.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setColourCode(DmShared.xNull(rsTemp.getString("colourCode")));
                node.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                node.setSizeCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                node.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                node.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                node.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                node.setIsBestBefore(DmShared.xNull(rsTemp.getString("isBestBefore")));
                node.setMrp(DmServer.vNull(rsTemp.getString("mrp"), "0.00"));
                node.setSkuMrp(DmServer.vNull(rsTemp.getString("skuMrp"), DmShared.getNumberFormat(7)));
                node.setSkuQtyForRate(DmServer.vNull(rsTemp.getString("skuQtyForRate")));

                node.setAmount(DmServer.vNull(rsTemp.getString("skuValue")));
                node.setRate(DmServer.vNull(rsTemp.getString("rate")));
                node.setSkuRate(DmServer.vNull(rsTemp.getString("skuRate")));
                node.setTotalpacks(DmServer.vNull(rsTemp.getString("unitQuantity")));

                node.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                node.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                node.setPurchaseUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());

                //IAP
                node.setIsubCode(DmShared.xNull(rsTemp.getString("oh_Ac_Item")));

                //======================< Set Item Properties Display >======================
                ItemProperty_Mdl ipm = new ItemProperty_Mdl();
                ipm.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                ipm.setLotNo(DmShared.xNull(rsTemp.getString("lotNo")));
                ipm.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                ipm.setColourName(DmShared.xNull(rsTemp.getString("colourName")));
                ipm.setSizeName(DmShared.xNull(rsTemp.getString("sizeName")));
                ipm.setItemMakeCode(DmShared.xNull(rsTemp.getString("itemMakeCode")));
                ipm.setItemMakeName(DmShared.xNull(rsTemp.getString("itemMakeName")));
                ipm.setMrp(DmShared.xNull(rsTemp.getString("mrp")));
                ipm.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                ipm.setManufacturingDate(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                node.setPropertyDisplay(ipm.getPropertiesCsv());
                //============================================================================

                lstNode.add(node);
            }
            rsTemp.close();
            psTemp.close();

            mrTemp.getListItemDetail().addAll(lstNode);

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

            /*==============================<< Delete Ledger Posting >>====================================*/
            AccountPosting_Server.deleteAccountPosting(keyCode, cnTemp);

            /*==============================<< Delete record from stock >>===============================*/
            Stock_Server.deleteStock(keyCode, cnTemp);

            /*==============================<< Delete record from stockHeadDetail >>===============================*/
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
    public DmResult<String> saveRecord(WipToStockConversion_Mdl mrTemp, WipToStockConversion_Mdl mrTempPrevious, String voucherPrefix) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, userName, siteCode;
        dmrTemp.setData("");

        try {
            WipToStockConversion_Vld.chkSave(mrTemp);
            userName = DmServer.getSsnUserName(this);
            siteCode = DmServer.getSsnSiteCode(this);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            DmServer.checkForwordVoucherDateTime(mrTemp.getVoucherDate(), "");
            if (mrTemp.isInventoryAccountingActive()) {
                DmServer.checkBackwordVoucherDateTime("stock", "v_Date", "", mrTemp.getVoucherDate(), "", cnTemp);
            }

            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                /*==============================<< Generate docId for new record >>====================================*/
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getDocId(DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this));
                mrTemp.setRecIdItemReceive(DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdItemReceive(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(mrTemp.getRecIdItemReceive().getRecIdValue()));
            } else {
                DmServer.getRecId("stockHead", "docId", DmShared.xNull(mrTemp.getSharedKeyFieldCode()), "recId", mrTemp.getRecIdItemReceive(), mrTemp.getVoucherDate(), DmShared.xNull(mrTemp.getVoucherTypeCode()), voucherPrefix, cnTemp, this, true);
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*==============================<< Check items for restricted department >>====================================*/
            STServer.checkItemsRestrictedDepartment(mrTemp.getLineItemCodesCsv(), mrTemp.getDepartmentTag(), mrTemp.getDepartmentText(), cnTemp);


            /*==============================<< Insert / Update record into stockHead >>====================================*/
            if (blnAdd) {
                sqlQuery = "Insert Into stockHead (docId, v_Type, v_No, v_Date, v_Prefix, recId,projectCode, projectAcCode,remark,"
                        + " balancingAc, site_Code, preparedBy,recIdPrefix,preparedDate) "
                        + " values (:docId, :v_Type, :v_No, :v_Date,  :v_Prefix, :recId,:projectCode, :projectAcCode, :remark,"
                        + " :balancingAc, :site_Code, :preparedBy,:recIdPrefix, :preparedDate) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("recId", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("v_Type", DmShared.foriegnKey(mrTemp.getVoucherTypeCode()));
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("v_Prefix", voucherPrefix);
                psTemp.setString("v_No", DmShared.iNull(mrTemp.getRecIdItemReceive().getCounter()).toString());
                psTemp.setString("recIdPrefix", DmShared.xNull(mrTemp.getRecIdItemReceive().getPrefix()));
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAc()));
                psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));
                psTemp.setString("preparedBy", userName);
                psTemp.setString("projectCode", DmShared.foriegnKey(mrTemp.getProjectTag()));
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemarkHeader()));
                psTemp.setString("projectAcCode", DmShared.foriegnKey(mrTemp.getProjectAcTag()));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
            } else {
                sqlQuery = " update stockHead Set "
                        + " v_Date = :v_Date, "
                        + " projectCode = :projectCode, "
                        + " projectAcCode = :projectAcCode, "
                        + " balancingAc = :balancingAc, "
                        + " transferDt = :transferDt, "
                        + " u_Name = :u_Name, "
                        + " remark = :remark, "
                        + " u_EntDt = :uEntDt "
                        + " Where docId = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setDate("v_Date", DmServer.convertDate(mrTemp.getVoucherDate()));
                psTemp.setString("projectCode", DmShared.foriegnKey(mrTemp.getProjectTag()));
                psTemp.setString("projectAcCode", DmShared.foriegnKey(mrTemp.getProjectAcTag()));
                psTemp.setString("balancingAc", DmShared.foriegnKey(mrTemp.getBalancingAc()));
                psTemp.setString("transferDt", null);
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("u_Name", userName);
                psTemp.setString("remark", DmShared.xNull(mrTemp.getRemarkHeader()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Validate Removed Received Items >>====================================*/
            if (!blnAdd) {
                ItemBatch_Server.validateRemovedReceivedItems(iniItemBatchProperties(mrTemp, mrTempPrevious), DmEnum.StockEntryType.OTHER, cnTemp, this);
            }

            /*==============================<< Delete records from stock >>====================================*/
            Stock_Server.deleteStock(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString(), cnTemp, true);

            /*==============================<< Delete records from stockHeadDetail >>====================================*/
            sqlQuery = "Delete From stockHeadDetail Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            /*==============================<< Insert records into stockHeadDetail & stockHeadBatchDetail >>====================================*/
            int v_SNo = 0;
            List<GrdItemDetail_Mdl> listDetail = mrTemp.getListItemDetail();
            for (GrdItemDetail_Mdl detail_Mdl : listDetail) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")) {
                    detail_Mdl.setV_SNo(String.valueOf(++v_SNo));
                    sqlQuery = " insert into stockHeadDetail(docId, v_SNo, departmentCode, "
                            + " itemCode, quantity, remark,site_Code ,"
                            + " ibCode, batchNo, lotNo, manufacturingDate,expiryDate, colourCode, sizeCode,itemMakeCode,  mrp,skuMrp"
                            + ",skuRate"
                            + ",rate"
                            + ",skuValue"
                            + ",skuQtyForRate"
                            + ",unitCode"
                            + ",unitQuantity"
                            + ",oh_Ac_Item"
                            + ")"
                            + " values (:docId, :v_SNo, :departmentCode,"
                            + " :itemCode, :quantity, :remark,:site_Code ,"
                            + " :ibCode,:batchNo, :lotNo, :manufacturingDate,:expiryDate, :colourCode, :sizeCode,:itemMakeCode, :mrp,:skuMrp"
                            + ",:skuRate"
                            + ",:rate"
                            + ",:skuValue"
                            + ",:skuQtyForRate"
                            + ",:unitCode"
                            + ",:unitQuantity"
                            + ",:oh_Ac_Item"
                            + ") ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                    psTemp.setString("v_SNo", DmShared.iNull(detail_Mdl.getV_SNo()).toString());
                    psTemp.setString("departmentCode", DmShared.foriegnKey(mrTemp.getDepartmentTag()));

                    psTemp.setString("itemCode", DmShared.foriegnKey(detail_Mdl.getSharedKeyFieldCode()));
                    psTemp.setString("quantity", DmServer.vNull(detail_Mdl.getQuantity()));
                    psTemp.setString("remark", DmShared.xNull(detail_Mdl.getRemark()));
                    psTemp.setString("site_Code", DmShared.foriegnKey(siteCode));

                    psTemp.setString("rate", DmServer.vNull(detail_Mdl.getRate(), "0.00"));
                    psTemp.setString("skuRate", DmServer.vNull(detail_Mdl.getSkuRate()));
                    psTemp.setString("skuValue", DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    psTemp.setString("skuQtyForRate", DmServer.vNull(detail_Mdl.getSkuQtyForRate()));
                    psTemp.setString("unitCode", DmShared.foriegnKey(detail_Mdl.getPurchaseUnitCode()));
                    psTemp.setString("unitQuantity", DmServer.vNull(detail_Mdl.getTotalpacks()));

                    psTemp.setString("ibCode", DmShared.foriegnKey(detail_Mdl.getIbCode()));
                    psTemp.setString("batchNo", DmShared.foriegnKey(detail_Mdl.getBatchNo()));
                    psTemp.setString("lotNo", DmShared.foriegnKey(detail_Mdl.getLotNo()));
                    psTemp.setDate("manufacturingDate", DmServer.convertDate(detail_Mdl.getManufacturingDate()));
                    psTemp.setDate("expiryDate", DmServer.convertDate(detail_Mdl.getExpiryDate()));
                    psTemp.setString("colourCode", DmShared.foriegnKey(detail_Mdl.getColourCode()));
                    psTemp.setString("sizeCode", DmShared.foriegnKey(detail_Mdl.getSizeCode()));
                    psTemp.setString("itemMakeCode", DmShared.foriegnKey(detail_Mdl.getItemMakeCode()));
                    psTemp.setString("mrp", DmServer.vNull(detail_Mdl.getMrp()));
                    psTemp.setString("skuMrp", DmServer.vNull(detail_Mdl.getSkuMrp()));
                    psTemp.setString("oh_Ac_Item", DmShared.foriegnKey(detail_Mdl.getIsubCode()));
                    psTemp.executeUpdate();
                    psTemp.close();

                    /*----------------------------------------------------------Stock Saving--------------------------------------------------------------*/
                    Stock_Mdl stock_Mdl = new Stock_Mdl();
                    stock_Mdl.setSharedKeyFieldCode(DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    stock_Mdl.setSharedKeyFieldName(detail_Mdl.getV_SNo());
                    stock_Mdl.setSite_Code(DmShared.xNull(siteCode));
                    stock_Mdl.setV_Type(DmShared.xNull(mrTemp.getVoucherTypeCode()));
                    stock_Mdl.setV_No(DmShared.iNull(mrTemp.getRecIdItemReceive().getCounter()).toString());
                    stock_Mdl.setRecId(DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                    stock_Mdl.setRecIdPrefix(DmShared.xNull(mrTemp.getRecIdItemReceive().getPrefix()));
                    stock_Mdl.setV_Date(DmShared.dNull(mrTemp.getVoucherDate()));
                    stock_Mdl.setDepartmentCode(DmShared.xNull(mrTemp.getDepartmentTag()));
                    stock_Mdl.setStockContraSubCode(DmShared.xNull(mrTemp.getProjectAcTag()));
                    stock_Mdl.setRemark(DmShared.xNull(mrTemp.getRemarkHeader()) + "\n" + DmShared.xNull(detail_Mdl.getRemark()));
                    stock_Mdl.setItemCode(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()));
                    stock_Mdl.setIbCode(detail_Mdl.getIbCode());
                    stock_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));
                    stock_Mdl.setSkuValue(DmServer.vNull(detail_Mdl.getAmount(), "0.00"));
                    stock_Mdl.setLandedSkuValue(DmServer.vNull(detail_Mdl.getAmount(), "0.00"));

                    StockFifo_Mdl stockFifo_Mdl = new StockFifo_Mdl();
                    stockFifo_Mdl.setSharedKeyFieldCode(DmShared.xNull(stock_Mdl.getSharedKeyFieldCode()));
                    stockFifo_Mdl.setSharedKeyFieldName(DmShared.xNull(stock_Mdl.getSharedKeyFieldName()));
                    stockFifo_Mdl.setRecQty(DmServer.vNull(detail_Mdl.getQuantity()));
                    stockFifo_Mdl.setStockValueDate(DmShared.dNull(stock_Mdl.getV_Date()));

                    stock_Mdl.setStockFifo_Mdl(stockFifo_Mdl);

                    Stock_Server.saveStock(stock_Mdl, DmEnum.StockEntryType.ITEM_RECEIVE, cnTemp);

                }
            }

            /*==============================<< Account Posting >>====================================*/
            if (mrTemp.isInventoryAccountingActive()) {
                AccountPosting_Server.saveAccountPosting(iniAcPostList(mrTemp.getSharedKeyFieldCode(), cnTemp), "docId", mrTemp.getSharedKeyFieldCode(), mrTemp.getRecIdItemReceive(), mrTemp.getVoucherTypeCode(), voucherPrefix, siteCode, mrTemp.getVoucherDate(), "", "", "", "", "", true,false, cnTemp);
            }
            /*==============================<< *************** >>====================================*/

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
        String sqlQuery, sqlCondition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " select	sh.docId, sh.recId, sh.v_Date, sgd.name as departmentName, sgrb.name receivedByName, vt.short_Name,sh.v_No "
                    + "		from stockHead as sh  "
                    + "		left join voucher_Type as vt on sh.v_Type = vt.v_Type  "
                    + "		left join ( "
                    + "                     select docId, max(departmentCode) as departmentCode, max(subCOde) as subCOde "
                    + "                     from stockHeadDetail  "
                    + "                     group by docId "
                    + "		) as st on sh.docId = st.docId "
                    + "		left join subGroup as sgd on st.departmentCode = sgd.subCode  "
                    + "		left join subGroup as sgrb on st.subCode = sgrb.subCode "
                    + "		where vt.category = '" + vCat + "' and sh.site_Code = :site_Code and sh.v_prefix = :vPrefix";

            sqlCondition = DmServer.getUserViewCondition(voucherDateFieldName, voucherTypefieldName, userPermission);
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
                listNode.setVoucherType(DmShared.xNull(rsTemp.getString("short_Name")));
                listNode.setVoucherDate(DmShared.dNull(rsTemp.getDate("v_Date")));
                listNode.setReceivedBy(DmShared.xNull(rsTemp.getString("receivedByName")));
                listNode.setDepartment(DmShared.xNull(rsTemp.getString("departmentName")));
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
            sqlWithParam.setSQL(" select vt.v_Type as code, vt.description as name, vt.balancingAc \n"
                    + "from voucher_Type vt\n"
                    + "where vt.category = '" + vCat + "'\n"
                    + "and vt.isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("balancingAc")));
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

            /*==============================<< Get default voucher_Type >>===============================*/
            sqlQuery = " select vt.v_Type as code, vt.description as name, vt.balancingAc \n"
                    + "from voucher_Type vt \n"
                    + "where vt.category = '" + vCat + "' \n"
                    + "and vt.isActive = 1 \n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setVoucherTypeCode(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setVoucherTypeName(DmShared.xNull(rsTemp.getString("name")));
                defaultValue.setBalancingAc(DmShared.xNull(rsTemp.getString("balancingAc")));
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "select  dm.subCode as code, sg.displayName as name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "  where sg.IsActive = 1 and dm.subCode=:depCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("depCode", DmShared.xNull(STServer.getSsnDepartmentCode(this)));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                defaultValue.setDepartmentTag(DmShared.xNull(rsTemp.getString("code")));
                defaultValue.setDepartmentText(DmShared.xNull(rsTemp.getString("name")));

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
            /*==============================<< *********************** >>====================================*/

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
    public DmResultPageing<List<TxtHItemName_Mdl>> getTxtHItemName(String v_Type, String departmentCode, DmHelper helper, boolean blnToEndOfFile) {
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

            if (DmShared.xNull(departmentCode).equalsIgnoreCase("")) {
                condition = " AND 1 = 2 ";
            }

            itemCatFilteration = DmServer.getItemCatFilteration(v_Type, cnTemp);
            
            if (!DmShared.xNull(itemCatFilteration).equalsIgnoreCase("")) {
                condition += " AND im.itemCategoryCode IN (" + itemCatFilteration + ")";
            }
            
            sqlQuery = "SELECT  im.code, im.name, im.manualCode, um.unit AS skuName, vis.currentStock "
                    + ", im.propertyList, im.isBestBefore, um.decimalPlaces, im.purchaseUnitConversionRate  "
                    + ", im.purchaseUnit as purchaseUnitCode,ump.unit as purchaseUnitName, im.sku as skuCode"
                    + ", ump.decimalPlaces as purchaseUnitDecimalPlaces, im.subCode AS iSubCode  "
                    + "		FROM itemMast im  "
                    + "         LEFT JOIN itemSiteList sl ON sl.code = im.code"
                    + "		LEFT JOIN unitmast um ON im.sku = um.code  "
                    + "		LEFT JOIN unitmast ump ON im.purchaseUnit = ump.code  "
                    + "		LEFT JOIN ( "
                    + "                     SELECT iStk.itemCode, SUM(iStk.balanceQty) AS currentStock  "
                    + "                     FROM itemStock iStk "
                    + "                     WHERE iStk.site_Code = :site_Code AND iStk.departmentCode = :departmentCode  "
                    + "                     GROUP BY iStk.itemCode "
                    + "		) AS vis ON im.code = vis.itemCode  "
                    + " 	LEFT JOIN ( "
                    + "                 SELECT irtd.code "
                    + "                 FROM itemRestrictedToDepartment irtd "
                    + "                 WHERE irtd.departmentCode = :departmentCode "
                    + "		) virtd ON virtd.code = im.code "
                    + "		WHERE im.isActive = 1 "
                    + "		AND (im.filterItemType = :filterItemType OR im.isCommon = 1)"
                    + "         AND virtd.code IS NULL "
                    + "         AND sl.site_Code = :site_Code " + condition;
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
            sqlWithParam.setString("departmentCode", DmShared.iNull(departmentCode).toString());
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHItemName_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                listNode.setSkuCode(DmShared.xNull(rsTemp.getString("skuCode")));
                listNode.setSkuDecimalPlaces(DmShared.iNull(rsTemp.getString("decimalPlaces")).toString());
                listNode.setPurchaseUnitConversionRate(DmServer.vNull(rsTemp.getString("purchaseUnitConversionRate")));
                listNode.setItemProperty(DmShared.xNull(rsTemp.getString("propertyList")));
                listNode.setStock(DmServer.vNull(rsTemp.getString("currentStock")));
                listNode.setIsBestBefore(DmShared.iNull(rsTemp.getString("isBestBefore")).toString());
                listNode.setPurchaseUnitCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                listNode.setPurchaseUnitName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                listNode.setPurchaseUnitDecimalPlaces(DmShared.iNull(rsTemp.getString("purchaseUnitDecimalPlaces")).toString());
                listNode.setIsubCode(DmShared.xNull(rsTemp.getString("iSubCode")));
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
    public DmResultPageing<List<TxtHProject_Mdl>> getTxtHProject(String v_Type, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHProject_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHProject_Mdl> lstTemp = new ArrayList<>();
        TxtHProject_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "SELECT pm.code,pm.name,pm.projectAc,pm.manualCode,sg.name as projectAcName \n"
                    + "	from projectMast pm \n"
                    + "	left join subgroup sg on pm.projectAc=sg.subCode \n"
                    + "	where pm.active=1 ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHProject_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setProjectAc(DmShared.xNull(rsTemp.getString("projectAc")));
                listNode.setProjectAcName(DmShared.xNull(rsTemp.getString("projectAcName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition = "";

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " and dm.subCode in (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlQuery = "select	dm.subCode as code, sg.name  "
                    + "		from departmentMast as dm "
                    + "		left join subGroup as sg on dm.subCode = sg.subCode "
                    + "         left join subGroupNature sgn on sgn.code = sg.nature"
                    + "		Left join subGroupSiteList as sgsl on sg.subCode=sgsl.subCode "
                    + "         where sgn.manualCode IN ('CM','PR')"
                    + "         and  sgsl.site_Code = :siteCode "
                    + "         and sg.isActive = 1 " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("siteCode", DmServer.getSsnSiteCode(this));
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

    private CompareProperties_Mdl iniItemBatchProperties(WipToStockConversion_Mdl mrTempCurrent, WipToStockConversion_Mdl mrTempPrevious) {
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
                batch_Mdl.setStockDepartmentCode(DmShared.xNull(mrTempCurrent.getDepartmentTag()));
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
                batch_MdlPrev.setStockDepartmentCode(DmShared.xNull(mrTempPrevious.getDepartmentTag()));
                lstItemBatch_MdlPrev.add(batch_MdlPrev);
            }
        }

        compareProperties_Mdl.setDocId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldCode()));
        compareProperties_Mdl.setRecId(DmShared.xNull(mrTempCurrent.getSharedKeyFieldName()));
        compareProperties_Mdl.setRecIdPrefix(DmShared.xNull(mrTempCurrent.getRecIdItemReceive().getPrefix()));
        compareProperties_Mdl.setV_No(DmShared.xNull(mrTempCurrent.getRecIdItemReceive().getCounter().toString()));
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
    public DmResult<String> getSkuLandedRate(String itemCode, String ibCode, String departmentCode, String v_Date) {
        DmResult<String> dmrtemp = new DmResult<String>();
        String skuLandedRate = "";
        Connection cnTemp = null;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            skuLandedRate = DmServer.vNull(STServer.getSkuLandedRate(itemCode, ibCode, departmentCode, DmServer.getSsnSiteCode(this), v_Date, cnTemp), "0.00");
            dmrtemp.setData(skuLandedRate);
            dmrtemp.setStatus(true);
            cnTemp.close();
        } catch (Exception ex) {
            dmrtemp.setErrorMessage(ex.getMessage());
        } finally {
            try {
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }
        return dmrtemp;
    }

    //<editor-fold defaultstate="collapsed" desc="Account Posting">
    private List<AccountPosting_Mdl> iniAcPostList(String docId, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<AccountPosting_Mdl> lstApm = new ArrayList<>();
        AccountPosting_Mdl apm;
        String sqlQuery = "", inventorySql = "", balancingAcSql = "", baseSql = "";
        String topInventoryAc = "";
        boolean flag = false;
        int sr = 0;

        try {
            //<editor-fold defaultstate="collapsed" desc="Prepare Account Posting SQL">
            sqlQuery = " SELECT \n"
                    + "		TOP 1 shd.oh_Ac_Item AS subCode\n"
                    + "	FROM stockHeadDetail shd\n"
                    + "	WHERE shd.docId = :docId\n"
                    + "	AND shd.oh_Ac_Item IS NOT NULL\n"
                    + " ORDER BY shd.v_SNo\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                topInventoryAc = DmShared.xNull(rsTemp.getString("subCode"));
            }
            rsTemp.close();
            psTemp.close();

            ++sr;
            inventorySql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Inventory Posting\n"
                    + "	SELECT \n"
                    + "		shd.oh_Ac_Item AS subCode\n"
                    + "		, sh.balancingAc AS contraSub\n"
                    + "		, shd.skuValue AS amount\n"
                    + "	FROM stockHead sh\n"
                    + "	LEFT JOIN stockHeadDetail shd ON shd.docId = sh.docId \n"
                    + "	WHERE sh.docId = :docId\n"
                    + "	AND shd.oh_Ac_Item IS NOT NULL\n"
                    + "	AND ISNULL(shd.skuValue, 0) <> 0\n"
                    + "\n"
                    + "	) t\n"
                    + "GROUP BY t.subCode\n";
            ++sr;
            balancingAcSql = "SELECT t.subCode, MAX(t.contraSub) AS contraSub , SUM(t.amount) AS amount, " + sr + " AS sr\n"
                    + "FROM (\n"
                    + "	-- Balancing A/c Posting\n"
                    + "	SELECT \n"
                    + "		sh.balancingAc AS subCode\n"
                    + "		, :topInventoryAc AS contraSub\n"
                    + "		, -ISNULL(shd.skuValue, 0) AS amount\n"
                    + "	FROM stockHead sh\n"
                    + "	LEFT JOIN stockHeadDetail shd ON shd.docId = sh.docId \n"
                    + "	WHERE sh.docId = :docId\n"
                    + "	AND sh.balancingAc IS NOT NULL\n"
                    + "	AND ISNULL(shd.skuValue, 0) <> 0\n"
                    + "	) t\n"
                    + "GROUP BY t.subCode\n";

            baseSql = inventorySql
                    + "UNION ALL\n"
                    + "\n"
                    + balancingAcSql;

            sqlQuery = "SELECT CAST(SUM(t.amount) AS DECIMAL(18,2)) AS amount\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("topInventoryAc", DmShared.iNull(topInventoryAc).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() != 0) {
                    throw new DmException(0, "Error in ledger posting.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT t.subCode, t.contraSub, t.amount, sgc.name as contraSubName\n"
                    + "FROM (\n"
                    + baseSql
                    + "	) t\n"
                    + "LEFT JOIN subGroup sgc ON sgc.subCode = t.contraSub\n"
                    + "ORDER BY sr";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.xNull(docId));
            psTemp.setString("topInventoryAc", DmShared.iNull(topInventoryAc).toString());
            rsTemp = psTemp.executeQuery();
            //</editor-fold>

            flag = false;
            while (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("subCode")).longValue() > 0) {
                    if (!flag) {
                        flag = true;
                    }
                    apm = new AccountPosting_Mdl();
                    apm.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                    apm.setContraCode(DmShared.xNull(rsTemp.getString("contraSub")));
                    apm.setContraText(DmShared.xNull(rsTemp.getString("contraSubName")));
                    if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() > 0) {
                        apm.setAmtDr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtCr("");
                    } else if (DmShared.vNull(rsTemp.getString("amount")).doubleValue() < 0) {
                        apm.setAmtCr(DmServer.vNull(rsTemp.getString("amount"), "0.00"));
                        apm.setAmtDr("");
                    }
                    lstApm.add(apm);
                }
            }
            rsTemp.close();
            psTemp.close();

            if (!flag) {
                throw new DmException(0, "Error in ledger posting.");
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

        return lstApm;
    }
    //</editor-fold>

public DmResult<Double> getGetLedgerBalance(String search) {
        DmResult<Double> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        double data = 0;
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select (isNull(Sum(LG.AmtDr),0) - isNull(Sum(LG.AmtCr),0)) As Balance "
                    + " From Ledger LG"
                    + " Where LG.SubCode = :SubCode and LG.Site_Code = :Site_Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("SubCode", DmShared.iNull(search).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                data = DmShared.vNull(rsTemp.getString("Balance"));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(data);
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
    }}
