package aarogya.inventory.gstServiceMaster.server;

import aarogya.inventory.gstServiceMaster.shared.Find_Mdl;
import aarogya.inventory.gstServiceMaster.shared.GstServiceMaster_Vld;
import aarogya.inventory.gstServiceMaster.shared.GstServiceMaster_mdl;
import aarogya.inventory.gstServiceMaster.shared.Sku_Mdl;
import aarogya.inventory.gstServiceMaster.shared.TxtHCategory_Mdl;
import aarogya.inventory.gstServiceMaster.shared.TxtHHSN_Mdl;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.GSTNature;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.ServiceEvalType;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import aarogya.inventory.gstServiceMaster.shared.GrdItsRateSlab_Mdl;
import aarogya.inventory.gstServiceMaster.shared.GstServiceMaster_Srv;

public class GstServiceMaster_SrvImpl extends DmRSSPrivate implements GstServiceMaster_Srv {

    private String vCat = new LibraryVoucherCategory().SERVICE_MAST;

    public GstServiceMaster_SrvImpl() {
    }

    @Override
    public DmResultPageing<List<String>> getSuggestionItem(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT NAME FROM itemMast ");
            helper.addSort("NAME", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("Name")));
            }
            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResultPageing<List<Sku_Mdl>> getSKU(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Sku_Mdl>> rstTemp = new DmResultPageing<>();
        List<Sku_Mdl> lstTemp = new ArrayList<>();
        Sku_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select code, unit, decimalPlaces from unitmast ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Sku_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("unit")));
                listNode.setAllowedDecimalPlaces(DmShared.xNull(rsTemp.getString("decimalPlaces")));
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
    public DmResultPageing<List<TxtHCategory_Mdl>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCategory_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCategory_Mdl> lstTemp = new ArrayList<>();
        TxtHCategory_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL(" Select imc.code as categoryCode, imc.name as categoryName "
                    + " from itemcat imc ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCategory_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("categoryCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("categoryName")));

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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" SELECT ig.code, ig.name FROM itemGroup ig");
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
    public DmResultPageing<List<TxtHHSN_Mdl>> getTxtHHSN(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHHSN_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHHSN_Mdl> lstTemp = new ArrayList<>();
        TxtHHSN_Mdl listNode;
        String sqlQuery;
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select hm.code, hm.name, hm.hsn ,hm.itemGSTNature from hsnMast hm";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHHSN_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
                listNode.setItemGSTNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                if (!DmShared.xNull(rsTemp.getString("itemGSTNature")).equalsIgnoreCase("")) {
                    listNode.setItemGSTNatureText(GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))).getValue());
                }
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
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
    public DmResult<GstServiceMaster_mdl> getMoveRec(String strKey) {

        DmResult<GstServiceMaster_mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        GstServiceMaster_mdl mrTemp = new GstServiceMaster_mdl();

        List<GrdItsRateSlab_Mdl> listGrdRateMdl = new ArrayList<>();
        GrdItsRateSlab_Mdl grdRateMdl;

        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " SELECT "
                    + "   im.code"
                    + " , im.name"
                    + " , im.itemGroup AS groupCode"
                    + " , ig.name AS groupName"
                    + " , im.itemType AS typeCode"
                    + " , it.name AS typeName"
                    + " , it.manualCode as itemTypeManualCode"
                    + " , im.sku AS skuCode"
                    + " , um.unit AS sku"
                    + " , im.isActive"
                    + " , im.itemCategoryCode"
                    + " , ic.name AS categoryName"
                    + " , im.preparedBY "
                    + " , im.u_name "
                    + " , im.preparedDate "
                    + " , im.u_EntDt "
                    + " , im.ManualCode"
                    + " , hs.name as hsnName"
                    + " , im.hsnCode"
                    + " , im.itemGSTNature"
                    + " , im.purchaseUnit as purchaseUnitCode"
                    + " , im.saleUnit as saleUnitCode"
                    + " , ump.unit as purchaseUnitName"
                    + " , ums.unit as saleUnitName"
                    + " , um.decimalPlaces"
                    + " , im.allowNegativeStock"
                    + " , im.uniqueId"
                    + " , smf.name as serviceMappingField"
                    + " , im.smfCode "
                    + " , im.serviceEvalTypecode "
                    + " , im.serviceEvalRate "
                    + " , im.printSrNo "
                    + " , im.itsBasedOn "
                    + " , im.payableAc as payableAcTag "
                    + " , sgpAc.name as payableAcName"
                    + " , im.purchaseAc,im.purchaseReturnAc , im.salesAc, im.salesReturnAc, im.subCode As inventoryAc\n "
                    + " , sgp.name as purchaseAcName,sgpr.name as purchaseReturnAcName,sgs.name as salesAcName,sgsr.name as salesReturnAcName,sgi.name as inventoryAcName\n"
                    + "  FROM itemMast AS im  "
                    + "  LEFT JOIN itemCat AS ic ON im.itemCategoryCode = ic.code  "
                    + "  LEFT JOIN itemgroup AS ig ON im.itemGroup = ig.code  "
                    + "  LEFT JOIN itemtype AS it ON im.itemType = it.code  "
                    + "  LEFT JOIN unitmast AS um ON im.sku = um.code  "
                    + "	 LEFT JOIN unitmast AS ump ON im.purchaseUnit = ump.code "
                    + "	 LEFT JOIN unitmast AS ums ON im.saleUnit = ums.code "
                    + "  LEFT JOIN hsnMast AS hs ON im.hsnCode = hs.code  "
                    + "  LEFT JOIN serviceMappingField AS smf on im.smfCode = smf.code "
                    + "  left join subgroup AS sgp ON im.purchaseAc = sgp.subCode"
                    + "  left join subgroup AS sgpr ON im.purchaseReturnAc = sgpr.subCode"
                    + "  left join subgroup AS sgs ON im.salesAc = sgs.subCode"
                    + "  left join subgroup AS sgsr ON im.salesReturnAc = sgsr.subCode"
                    + "  left join subgroup AS sgi ON im.subCode = sgi.subCode"
                    + "  left join subgroup AS sgpAc on im.payableAc = sgpAc.subCode"
                    + "  WHERE im.code = :code ;";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
            rsTemp = psTemp.executeQuery();

            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setActiveTag(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setActiveText(DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 0 ? "InActive"
                        : DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 1 ? "Active"
                        : DmShared.vNull(rsTemp.getString("isActive")).doubleValue() == 2 ? "Hold" : "");

                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString("ManualCode")));
                mrTemp.setCategoryTag(DmShared.xNull(rsTemp.getString("itemCategoryCode")));
                mrTemp.setCategoryText(DmShared.xNull(rsTemp.getString("categoryName")));

                mrTemp.setGroupTag(DmShared.xNull(rsTemp.getString("groupCode")));
                mrTemp.setGroupText(DmShared.xNull(rsTemp.getString("groupName")));

                mrTemp.setInventoryAcTag(DmShared.xNull(rsTemp.getString("inventoryAc")));
                mrTemp.setInventoryAcText(DmShared.xNull(rsTemp.getString("inventoryAcName")));
                mrTemp.setPurchaseAcTag(DmShared.xNull(rsTemp.getString("purchaseAc")));
                mrTemp.setPurchaseAcText(DmShared.xNull(rsTemp.getString("purchaseAcName")));
                mrTemp.setSalesAcTag(DmShared.xNull(rsTemp.getString("salesAc")));
                mrTemp.setSalesAcText(DmShared.xNull(rsTemp.getString("salesAcName")));

                mrTemp.setPurchaseReturnAcTag(DmShared.xNull(rsTemp.getString("purchaseReturnAc")));
                mrTemp.setPurchaseReturnAcText(DmShared.xNull(rsTemp.getString("purchaseReturnAcName")));
                mrTemp.setSalesReturnAcTag(DmShared.xNull(rsTemp.getString("salesReturnAc")));
                mrTemp.setSalesReturnAcText(DmShared.xNull(rsTemp.getString("salesReturnAcName")));

                mrTemp.setPrvSkuTag(DmShared.xNull(rsTemp.getString("skuCode")));
                mrTemp.setSkuTag(DmShared.xNull(rsTemp.getString("skuCode")));
                mrTemp.setSkuText(DmShared.xNull(rsTemp.getString("sku")));
                mrTemp.setSharedPreparedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("preparedBY")), rsTemp.getTimestamp("preparedDate")));
                mrTemp.setSharedModifiedBy(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_name")), rsTemp.getTimestamp("u_EntDt")));
                mrTemp.setHsn(DmShared.xNull(rsTemp.getString("hsnCode")));
                mrTemp.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));

                mrTemp.setGstNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                if (GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))) != null) {
                    mrTemp.setGstNatureText(GSTNature.getValueOf(DmShared.xNull(rsTemp.getString("itemGSTNature"))).getValue());
                }

                mrTemp.setPurchaseUnitSkuCode(DmShared.xNull(rsTemp.getString("purchaseUnitCode")));
                mrTemp.setPurchaseUnitSkuName(DmShared.xNull(rsTemp.getString("purchaseUnitName")));
                mrTemp.setSaleUnitSkuCode(DmShared.xNull(rsTemp.getString("saleUnitCode")));
                mrTemp.setSaleUnitSkuName(DmShared.xNull(rsTemp.getString("saleUnitName")));

                mrTemp.setPayableAcTag(DmShared.xNull(rsTemp.getString("payableAcTag")));
                mrTemp.setPayableAcText(DmShared.xNull(rsTemp.getString("payableAcName")));

                mrTemp.setTypeManualCode(DmShared.xNull(rsTemp.getString("itemTypeManualCode")));
                mrTemp.setTypeName(DmShared.xNull(rsTemp.getString("typeName")));
                mrTemp.setUniqueId(DmShared.xNull(rsTemp.getString("uniqueId")));
                mrTemp.setMapWithFieldTag(DmShared.xNull(rsTemp.getString("smfCode")));
                mrTemp.setMapWithField(DmShared.xNull(rsTemp.getString("serviceMappingField")));
                mrTemp.setValue(DmServer.vNull(rsTemp.getString("serviceEvalRate"), "0.00"));
                if (!DmShared.xNull(rsTemp.getString("serviceEvalTypecode")).equalsIgnoreCase("")) {
                    mrTemp.setServiceEvalType(DmShared.xNull(ServiceEvalType.getViaCode(DmShared.iNull(rsTemp.getString("serviceEvalTypecode")).intValue()).getName()));
                }
                mrTemp.setServiceEvalTypeCode(DmShared.xNull(rsTemp.getString("serviceEvalTypecode")));
                mrTemp.setPrintSrNo(DmShared.xNull(rsTemp.getString("printSrNo")));
                mrTemp.setTaxBasedOnTag(DmShared.xNull(rsTemp.getString("itsBasedOn")));
                if (DmShared.xNull(rsTemp.getString("itsBasedOn")).equalsIgnoreCase("0")) {
                    mrTemp.setTaxBasedOn("Fixed");//fixed
                } else if (DmShared.xNull(rsTemp.getString("itsBasedOn")).equalsIgnoreCase("1")) {
                    mrTemp.setTaxBasedOn("Rate");//rate
                }
            }
            psTemp.close();
            rsTemp.close();

            //==================== GST Rate Slab Grid =====================================
            sqlQuery = "select iid.rateFrom, iid.rateUpto, \n"
                    + " iid.gstTypeCode, it.name as gstTypeName \n"
                    + " from itemItsDetail iid\n"
                    + " left join gstType it on it.code = iid.gstTypeCode\n"
                    + " where iid.code = :itemCode \n"
                    + " ORDER BY iid.rateFrom ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", strKey);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                grdRateMdl = new GrdItsRateSlab_Mdl();
                grdRateMdl.setRateFrom(DmServer.vNull(rsTemp.getString("rateFrom")));
                grdRateMdl.setRateUpto(DmServer.vNull(rsTemp.getString("rateUpto")));
                grdRateMdl.setGstTypeCode(DmShared.xNull(rsTemp.getString("gstTypeCode")));
                grdRateMdl.setGstTypeName(DmShared.xNull(rsTemp.getString("gstTypeName")));
                listGrdRateMdl.add(grdRateMdl);
            }
            mrTemp.getGrdRateIts_Mdls().addAll(listGrdRateMdl);
            rsTemp.close();
            psTemp.close();

            mrTemp.setIsItemUsed(checkItem(mrTemp, cnTemp));

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
    public DmResult<String> saveRecord(GstServiceMaster_mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false, isSiteListExists = false;
        CallableStatement csTemp = null;
        String sqlQuery, vType = "", sqlQuery1 = "";
        ResultSet rsTemp = null;
        dmrTemp.setData("");

        try {
            GstServiceMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "itemMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            if (DmShared.xNull(checkItem(mrTemp, cnTemp)).equalsIgnoreCase("1")) {
                if (DmShared.iNull(mrTemp.getSkuTag()).intValue() != DmShared.iNull(mrTemp.getPrvSkuTag()).intValue()) {
                    dmrTemp.setErrorNumber(6000);
                    throw new UnsupportedOperationException("Cannot change sku as item is used.");
                }
            }

            sqlQuery = " Select Count(Name) As Cnt From itemMast mm Where mm.Name = :Name And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Name", mrTemp.getSharedKeyFieldName());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Service Name already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            //check duplicate unique ID
            if (!DmShared.xNull(mrTemp.getUniqueId()).trim().equalsIgnoreCase("")) {
                sqlQuery = " Select Count(uniqueId) As Cnt From itemMast mm Where mm.uniqueId = :uniqueId And Code <> :Code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("uniqueId", mrTemp.getUniqueId());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    if (rsTemp.getLong("Cnt") > 0) {
                        throw new UnsupportedOperationException("unique Id already exists.");
                    }
                }
                rsTemp.close();
                psTemp.close();
            }
            //
            //check duplicate services
            sqlQuery = " select count(smfCode) as cnt from itemMast where smfCode = :smfCode  And Code <> :Code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("smfCode", mrTemp.getMapWithFieldTag());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Service already assigned");
                }
            }
            rsTemp.close();
            psTemp.close();
            //

            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();
            if (blnAdd) {
                // ************************************** GET MANUAL CODE *********************************************
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("itemName", new FieldUDT_Mdl("itemName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("itemGroup", new FieldUDT_Mdl("itemGroup", DmShared.xNull(mrTemp.getGroupText()).toString(), DmShared.xNull(mrTemp.getGroupText()).trim()));
                fieldUDTHashMap.put("itemCat", new FieldUDT_Mdl("itemCat", DmShared.xNull(mrTemp.getCategoryText()).toString(), DmShared.xNull(mrTemp.getCategoryText()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getServiceManualCode(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.foriegnKey(vType));
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }
                rsTemp = csTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)).toString().toUpperCase());
                }
                rsTemp.close();
                csTemp.close();

            }

            if (DmShared.xNull(mrTemp.getManualCode()).trim().equals("")) {
                throw new UnsupportedOperationException("Invalid Manual Code.");
            }

            sqlQuery = " Select Count(ManualCode) As Cnt From itemMast mm Where mm.ManualCode = :ManualCode And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    throw new UnsupportedOperationException("Manual Code already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="CHECK OF SITE LIST">
            sqlQuery = " Select Count(code) As Cnt From itemSiteList isl Where isl.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    isSiteListExists = true;
                }
            }
            rsTemp.close();
            psTemp.close();

//</editor-fold>
            if (blnAdd) {
                sqlQuery = "INSERT INTO itemmast "
                        + "("
                        + "   CODE "
                        + " , NAME "
                        + " , itemGroup "
                        + " , itemType "
                        + " , filterItemType "
                        + " , sku "
                        + " , preparedBY "
                        + " , preparedDate "
                        + " , transferDt "
                        + " , isActive "
                        + " , itemCategoryCode"
                        + " , manualCode"
                        + " , hsnCode"
                        + " , itemGSTNature"
                        + " , purchaseUnit"
                        + " , saleUnit"
                        + " , purchaseUnitConversionRate"
                        + " , saleUnitConversionRate"
                        + " , allowNegativeStock"
                        + " , displayName"
                        + " , uniqueId"
                        + " , smfCode"
                        + " , serviceEvalTypeCode"
                        + " , serviceEvalRate"
                        + " , printSrNo"
                        + " , itsBasedOn"
                        + " , purchaseAc,purchaseReturnAc, salesAc, salesReturnAc, subCode"
                        + " , payableAc "
                        + ")"
                        + " VALUES "
                        + "("
                        + "   :code"
                        + " , :name"
                        + " , :itemGroup"
                        + " , :itemType"
                        + " , :filterItemType"
                        + " , :sku"
                        + " , :preparedBY"
                        + " , :preparedDate"
                        + " , NULL"
                        + " , :isActive"
                        + " , :itemCategoryCode"
                        + " , :manualCode"
                        + " , :hsnCode "
                        + " , :itemGSTNature"
                        + " , :purchaseUnit"
                        + " , :saleUnit "
                        + " , :purchaseUnitConversionRate"
                        + " , :saleUnitConversionRate"
                        + " , :allowNegativeStock"
                        + " , :displayName"
                        + " , :uniqueId"
                        + " , :smfCode"
                        + " , :serviceEvalTypeCode"
                        + " , :serviceEvalRate"
                        + " , :printSrNo"
                        + " , :itsBasedOn"
                        + " , :purchaseAc, :purchaseReturnAc, :salesAc, :salesReturnAc ,:subCode"
                        + " , :payableAc"
                        + ")";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getGroupTag()));
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("itemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode(mrTemp.getTypeManualCode(), "manualCode", "itemType", "code", cnTemp)));
//                psTemp.setString("itemType", DmShared.foriegnKey(mrTemp.getTypeManualCode()));
                psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("OI", "manualCode", "itemType", "code", cnTemp));
                psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSkuTag()));
                psTemp.setString("isActive", DmShared.xNull(mrTemp.getActiveTag()));
                psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsn()));
                psTemp.setString("preparedBY", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("preparedDate", DmServer.getServerTimestamp());
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));

                psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()));
                psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitSkuCode()));
                psTemp.setString("purchaseUnitConversionRate", DmServer.vNull("1"));
                psTemp.setString("saleUnitConversionRate", DmServer.vNull("1"));
                psTemp.setString("allowNegativeStock", DmShared.xNull("1"));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("uniqueId", DmShared.foriegnKey(DmShared.removeSpace(mrTemp.getUniqueId())));
                psTemp.setString("smfCode", DmShared.foriegnKey(mrTemp.getMapWithFieldTag()));
                psTemp.setString("serviceEvalTypeCode", DmShared.foriegnKey(mrTemp.getServiceEvalTypeCode()));
                psTemp.setString("serviceEvalRate", DmShared.xNull(mrTemp.getValue()));
                psTemp.setString("printSrNo", DmShared.xNull(mrTemp.getPrintSrNo()));
                psTemp.setString("itsBasedOn", DmShared.xNull(mrTemp.getTaxBasedOnTag()));
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("payableAc", DmShared.foriegnKey(mrTemp.getPayableAcTag()));

            } else {
                sqlQuery = "UPDATE itemmast SET "
                        + "    name = :name "
                        + "  , displayName = :displayName "
                        + "  , itemGroup = :itemGroup "
                        + "  , sku = :sku "
                        + "  , u_Name = :userName "
                        + "  , u_EntDt = :uEntDt "
                        + "  , transferDt = NULL "
                        + "  , itemCategoryCode = :itemCategoryCode  "
                        + "  , hsnCode = :hsnCode  "
                        + "  , manualCode = :manualCode  "
                        + "  , itemGSTNature=:itemGSTNature "
                        + "  , purchaseUnit=:purchaseUnit "
                        + "  , saleUnit=:saleUnit "
                        + "  , itemType= :itemType "
                        + "  , uniqueId = :uniqueId "
                        + "  , smfCode = :smfCode"
                        + "  , serviceEvalTypeCode = :serviceEvalTypeCode"
                        + "  , serviceEvalRate = :serviceEvalRate"
                        + "  , printSrNo = :printSrNo"
                        + "  , itsBasedOn = :itsBasedOn"
                        + "  , purchaseAc = :purchaseAc "
                        + "  ,purchaseReturnAc = :purchaseReturnAc "
                        + "  , salesAc = :salesAc "
                        + "  ,salesReturnAc = :salesReturnAc "
                        + "  , subCode = :subCode "
                        + "  , payableAc = :payableAc"
                        + " WHERE CODE = :CODE ;";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("CODE", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getGroupTag()));
                psTemp.setString("manualCode", mrTemp.getManualCode());
                psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSkuTag()));
                psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
                psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsn()));
                psTemp.setString("userName", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
                psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()));
                psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitSkuCode()));
                psTemp.setString("itemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode(mrTemp.getTypeManualCode(), "manualCode", "itemType", "code", cnTemp)));
                psTemp.setString("uniqueId", DmShared.foriegnKey(DmShared.removeSpace(mrTemp.getUniqueId())));
                psTemp.setString("smfCode", DmShared.foriegnKey(mrTemp.getMapWithFieldTag()));
                psTemp.setString("serviceEvalTypeCode", DmShared.foriegnKey(mrTemp.getServiceEvalTypeCode()));
                psTemp.setString("serviceEvalRate", DmShared.xNull(mrTemp.getValue()));
                psTemp.setString("printSrNo", DmShared.xNull(mrTemp.getPrintSrNo()));
                psTemp.setString("itsBasedOn", DmShared.xNull(mrTemp.getTaxBasedOnTag()));
                psTemp.setString("purchaseAc", DmShared.foriegnKey(mrTemp.getPurchaseAcTag()));
                psTemp.setString("salesAc", DmShared.foriegnKey(mrTemp.getSalesAcTag()));
                psTemp.setString("purchaseReturnAc", DmShared.foriegnKey(mrTemp.getPurchaseReturnAcTag()));
                psTemp.setString("salesReturnAc", DmShared.foriegnKey(mrTemp.getSalesReturnAcTag()));
                psTemp.setString("subCode", DmShared.foriegnKey(mrTemp.getInventoryAcTag()));
                psTemp.setString("payableAc", DmShared.foriegnKey(mrTemp.getPayableAcTag()));
            }
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="INSERT IN ITEM_SITE_LIST">
            if (!isSiteListExists) {
                sqlQuery1 = "INSERT INTO itemSiteList (CODE, site_Code)  "
                        + " VALUES (:code, :site_Code)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery1);
                psTemp1.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp1.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(this)));

                psTemp1.executeUpdate();
                psTemp1.close();
            }
            //</editor-fold>


            /*===========================< itemITSDetail >===============================*/
            sqlQuery = " delete from itemITSDetail where code = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int sNo2 = 0;
            for (int i = 0; i < mrTemp.getGrdRateIts_Mdls().size(); i++) {
                GrdItsRateSlab_Mdl detail = mrTemp.getGrdRateIts_Mdls().get(i);
                sNo2++;
                if (!DmShared.xNull(detail.getGstTypeCode()).equalsIgnoreCase("")) {
                    sqlQuery = "insert into itemITSDetail (code ,v_SNo,rateFrom,rateUpto,gstTypeCode)"
                            + " values(:code,:v_SNo,:rateFrom,:rateUpto,:gstTypeCode) ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                    psTemp.setString("v_SNo", DmShared.iNull(sNo2).toString());
                    psTemp.setString("rateFrom", DmServer.vNull(detail.getRateFrom(), "0.00"));
                    psTemp.setString("rateUpto", DmServer.vNull(detail.getRateUpto(), "0.00"));
                    psTemp.setString("gstTypeCode", DmShared.foriegnKey(detail.getGstTypeCode()));

                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        DmPreparedStatement psTemp = null;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT CODE FROM itemMast WHERE filterItemType = :filterItemType ORDER BY NAME");
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("OI", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
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
    public DmResult<String> deleteRecord(String strKey) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            strSQL = "Delete From itemSiteList Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = " delete from itemITSDetail where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
            psTemp.executeUpdate();
            psTemp.close();

            strSQL = "Delete From itemmast Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("code", strKey);
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
    public DmResultPageing<List<Find_Mdl>> getFind(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;

        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL(" SELECT "
                    + "   im.code"
                    + " , im.name "
                    + " , mg.name AS groupName"
                    + " , um.unit AS sku  "
                    + " , im.isActive"
                    + " , mc.name AS categoryName"
                    + " , it.code as typeCode"
                    + " , it.name as typename"
                    + " , im.manualCode "
                    + " , hs.name as hsnName "
                    + " , case im.isActive when 0  then 'InActive' when 1 then 'Active' when 2 then 'Hold' end  as activeText "
                    + "  FROM itemmast AS im "
                    + "  LEFT JOIN itemCat AS mc ON im.itemCategoryCode = mc.code "
                    + "  LEFT JOIN itemGroup AS mg ON im.itemGroup = mg.code "
                    + "  LEFT JOIN itemType AS it ON im.itemType = it.code  "
                    + "  LEFT JOIN unitmast AS um ON im.sku = um.code "
                    + "  LEFT JOIN hsnMast AS hs ON im.hsnCode = hs.code  "
                    + "  WHERE  im.filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("OI", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                listNode.setActive(DmShared.xNull(rsTemp.getString("activeText")));

                listNode.setCategory(DmShared.xNull(rsTemp.getString("categoryName")));
                listNode.setGroup(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.setSkuName(DmShared.xNull(rsTemp.getString("sku")));
                listNode.setItemTypeTag(DmShared.xNull(rsTemp.getString("typeCode")));
                listNode.setItemTypeText(DmShared.xNull(rsTemp.getString("typename")));
                listNode.setHsnName(DmShared.xNull(rsTemp.getString("hsnName")));

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
    public DmResultPageing<List<KeyFieldExtended>> getYesNo(DmHelper helper, boolean blnToEndOfFile, boolean isActiveCall) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            if (isActiveCall) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("Inactive");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Active");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("2");
                listNode.setSharedKeyFieldName("Hold");
                lstTemp.add(listNode);
            } else {

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("0");
                listNode.setSharedKeyFieldName("No");
                lstTemp.add(listNode);

                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode("1");
                listNode.setSharedKeyFieldName("Yes");
                lstTemp.add(listNode);
            }
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

    private String checkItem(GstServiceMaster_mdl mrTemp, Connection cnTemp) {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String isItemUsed = "";
        String strSQL = "";
        try {
            strSQL = " SELECT COUNT(s.itemCode) AS cnt FROM stock s "
                    + " WHERE s.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(ib.itemCode) AS cnt FROM itemBatch ib "
                    + " WHERE ib.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(req.itemCode) AS cnt FROM requisitionDetail req "
                    + " WHERE req.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(ind.itemCode) AS cnt FROM indentDetail ind "
                    + " WHERE ind.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(ed.itemCode) AS cnt FROM enquiryDetail ed "
                    + " WHERE ed.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(qd.itemCode) AS cnt FROM quotationDetail qd "
                    + " WHERE qd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(pcd.itemCode) AS cnt FROM pChallanDetail pcd "
                    + " WHERE pcd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

            strSQL = " SELECT COUNT(pbd.itemCode) AS cnt FROM pBillDetail pbd "
                    + " WHERE pbd.itemCode = :itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("itemCode", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    isItemUsed = "1";
                    return isItemUsed;
                }
            }
            rsTemp.close();
            psTemp.close();

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
        return isItemUsed;
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
    public DmResultPageing<List<KeyFieldExtended>> getHType(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL(" SELECT it.manualCode as code, it.name FROM itemType it"
                    + "  where it.manualCode IN('OI','SV')");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMapWithField(DmHelper helper, boolean blnToEndOfFile, String itemCode) {
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

            sqlWithParam.setSQL("SELECT smf.code as code, smf.name "
                    + "FROM serviceMappingField smf\n"
                    + "left join (\n"
                    + "     SELECT DISTINCT im.smfCode \n"
                    + "     FROM itemMast im \n"
                    + "     WHERE im.smfCode IS NOT NULL \n"
                    + "     AND im.code <> :itemCode \n"
                    + "     ) AS im on im.smfCode = smf.code \n"
                    + "where smf.isActive = '1' \n"
                    + "and im.smfCode is null ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTaxBasedOn(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Fixed");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Rate");
            lstTemp.add(listNode);

            rstTemp.setData(DmShared.filterKeyFieldList(helper, lstTemp));
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGSTNature(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (GSTNature nature : GSTNature.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(nature.getId());
                listNode.setSharedKeyFieldName(nature.getValue());
                lstTemp.add(listNode);
            }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHValuationType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (ServiceEvalType nature : ServiceEvalType.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(nature.getCode()).toString());
                listNode.setSharedKeyFieldName(nature.getName());
                lstTemp.add(listNode);
            }
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
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT code, name, printSrNo FROM gstType\n"
                    + "Where isActive = '1' ");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSubCode(String subgroupNature, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String Condition = "";
        rstTemp.setData(lstTemp);
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                Condition = " and sg.nature = :nature ";
            }

            sqlWithParam.setSQL(" SELECT sg.subCode, sg.name, sg.manualCode "
                    + " FROM subGroup sg where sg.isActive = 1 "
                    + Condition);
            if (!DmShared.xNull(subgroupNature).trim().equalsIgnoreCase("")) {
                sqlWithParam.setString("nature", DmShared.xNull(DmServer.getCodeViaManualCode(subgroupNature, "manualCode", "subGroupNature", "code", cnTemp)));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("subCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("manualCode")));
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
