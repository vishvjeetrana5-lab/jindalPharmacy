package aarogya.pharmacy.barcodeGeneration.server;

import aarogya.pharmacy.barcodeGeneration.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BarcodeGeneration_SrvImpl extends DmRSSPrivate implements BarcodeGeneration_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    String catFilter = "'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','"
            + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "','"
            + pvc.PHARMACY_PURCHASE_CHALLAN + "','"
            + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "','"
            + pvc.MEDICINE_OPENING + "'";

    @Override
    public DmResult<String> saveRecord(BarcodeGeneration_Mdl mrTemp, Setting_Mdl setting_Mdl) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, barCodeVtype, entryPointVType, barCodeDocid, entryPointDocid, vDate, vTime, userName;
        ResultSet rsTemp = null;
        RecId barRecid;
        RecId entryPonitRecid = new RecId();
        boolean flag = false;
        int noOfBarCodeGenerated = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);
            vDate = DmServer.getServerDateTime()[0];
            vTime = DmServer.getServerDateTime()[1];
            userName = DmServer.getSsnUserName(this);
            barCodeVtype = DmServer.getCodeViaManualCode(pvc.BAR_CODE_COUNTER, "category", "voucher_Type", "v_type", cnTemp);
            entryPointVType = DmServer.getCodeViaManualCode(pvc.BAR_CODE_GENERATION, "category", "voucher_Type", "v_type", cnTemp);

            entryPointDocid = DmServer.getDocId(DmShared.iNull(entryPointVType).toString(), mrTemp.getVoucherPrefix(), cnTemp, this);
            entryPonitRecid = DmServer.getRecId("barCodeGeneration", "docId", DmShared.xNull(entryPointDocid), "recId", entryPonitRecid, vDate, DmShared.iNull(entryPointVType).toString(), mrTemp.getVoucherPrefix(), cnTemp, this, true);

            sqlQuery = "Insert into barCodeGeneration (docId, v_No, recId, recIdPrefix, v_Type, v_Date, v_Time, v_Prefix, preparedBy, u_EntDt)"
                    + " values (:docId, :v_No, :recId, :recIdPrefix, :v_Type, :v_Date, :v_Time, :v_Prefix, :preparedBy, :u_EntDt)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", DmShared.iNull(entryPointDocid).toString());
            psTemp.setString("v_No", DmShared.iNull(entryPonitRecid.getCounter()).toString());
            psTemp.setString("recId", DmShared.xNull(entryPonitRecid.getRecIdValue()));
            psTemp.setString("recIdPrefix", DmShared.xNull(entryPonitRecid.getPrefix()));
            psTemp.setString("v_Type", DmShared.foriegnKey(entryPointVType));
            psTemp.setString("v_Prefix", mrTemp.getVoucherPrefix());
            psTemp.setDate("v_Date", DmServer.convertDate(vDate));
            psTemp.setString("v_Time", DmServer.vNull(vTime, "0.00"));
            psTemp.setString("preparedBy", userName);
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            for (GrdBarcodeGeneration_Mdl grdEntry_Mdl : mrTemp.getListBr()) {
                if (!DmShared.xNull(grdEntry_Mdl.getBarcode()).equalsIgnoreCase("")) {
                    continue;
                }
                noOfBarCodeGenerated++;
//                if (flag) {
//
//                    break;
//                }
//                flag = true;
                barCodeDocid = DmServer.getDocId(DmShared.iNull(barCodeVtype).toString(), mrTemp.getVoucherPrefix(), cnTemp, this);
                barRecid = new RecId();
                barRecid = DmServer.getRecId("barCodeGenerationDetail", "barCodeDocId", DmShared.xNull(barCodeDocid), "barCode", barRecid, vDate, DmShared.iNull(barCodeVtype).toString(), mrTemp.getVoucherPrefix(), cnTemp, this, true);

                sqlQuery = "Update  itemBatch set  "
                        + "         barcode = :barcode  "
                        + "         where code = :code and itemCode = :itemCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("barcode", DmShared.xNull(barRecid.getRecIdValue()));
                psTemp.setString("code", DmShared.iNull(grdEntry_Mdl.getSharedKeyFieldCode()).toString());
                psTemp.setString("itemCode", DmShared.iNull(grdEntry_Mdl.getItemCode()).toString());
                psTemp.executeUpdate();
                psTemp.close();

                sqlQuery = "Insert into barCodeGenerationDetail (docId, barCodeDocid,  v_No, barCode, recIdPrefix, v_Type, v_Date, v_Time, v_Prefix)"
                        + " values (:docId, :barCodeDocid, :v_No, :barCode, :recIdPrefix, :v_Type, :v_Date, :v_Time, :v_Prefix)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("docId", DmShared.iNull(entryPointDocid).toString());
                psTemp.setString("barCodeDocid", DmShared.iNull(barCodeDocid).toString());
                psTemp.setString("v_No", DmShared.iNull(barRecid.getCounter()).toString());
                psTemp.setString("barCode", DmShared.xNull(barRecid.getRecIdValue()));
                psTemp.setString("recIdPrefix", DmShared.xNull(barRecid.getPrefix()));
                psTemp.setString("v_Type", DmShared.foriegnKey(barCodeVtype));
                psTemp.setString("v_Prefix", mrTemp.getVoucherPrefix());
                psTemp.setDate("v_Date", DmServer.convertDate(vDate));
                psTemp.setString("v_Time", DmServer.vNull(vTime, "0.00"));
                psTemp.executeUpdate();
                psTemp.close();

            }
            if (noOfBarCodeGenerated == 0) {
                spBegin.rollBack();
            } else {
                spBegin.commit();
            }
            dmrTemp.setData(entryPointDocid);
            cnTemp.close();
            dmrTemp.setStatus(true);
            dmrTemp.setValue1(entryPointVType);
        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
                if (e instanceof DmException) {
                    dmrTemp.setErrorNumber(((DmException) e).getErrorNo());
                    dmrTemp.setValue1(((DmException) e).getErrorSource());
                    dmrTemp.setValue2(((DmException) e).getErrorSourceCode());
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
    public DmResult<BarcodeGeneration_Mdl> getFillRecords(Setting_Mdl setting_Mdl) {
        DmResult<BarcodeGeneration_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            dmrTemp.setData(getRecords(setting_Mdl, cnTemp));
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
    public DmResult<BarcodeGeneration_Mdl> getMoveRec(String docid) {
        DmResult<BarcodeGeneration_Mdl> dmrTemp = new DmResult<>();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        BarcodeGeneration_Mdl mrTemp = new BarcodeGeneration_Mdl();
        List<GrdBarcodeGeneration_Mdl> lstTemp = new ArrayList<>();
        Connection cnTemp = null;
        try {
            Calendar cal = Calendar.getInstance();
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = " Select imb.code, imb.itemcode, max(im.name) itemName, max(icm.name) color, "
                    + " max(imb.batchNo) batchNo, max(imb.expiryDate)expiryDate, max(ism.name) size , "
                    + " max(sg.displayName) department, max(imb.mrp) MRP, max(imb.barcode)barcode, "
                    + " max(im.purchaseUnitConversionRate) pUnit, sum(st.recQty) rcvdQnty                    \n      "
                    + " from barCodeGenerationDetail bcg                                    \n      "
                    + " left join itembatch imb on imb.barcode =  bcg.barcode               \n      "
                    + " left join Stock st on imb.code = st.ibcode  and imb.itemcode= st.itemCode      \n"
                    + " left join itemMast im on im.code = st.itemCode                      \n     "
                    + " left join itemColourMast icm on icm.code = imb.colourCode           \n     "
                    + " left join itemSizeMast ism on ism.code = imb.sizeCode               \n     "
                    + " left join voucher_Type vt on vt.v_type = st.v_type                  \n     "
                    + " left join subGroup sg on sg.subCode = st.departmentCode "
                    + " where 1=1 and isNull(imb.barcode,'')<>'' and vt.category in (" + catFilter + ")      "
                    + "   and isNUll(st.recQty,0) > 0  and bcg.docid=:docid"
                    + " group by imb.code, imb.itemcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docid", DmShared.iNull(docid).toString());

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdBarcodeGeneration_Mdl node = new GrdBarcodeGeneration_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                node.setColor(DmShared.xNull(rsTemp.getString("color")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                node.setSize(DmShared.xNull(rsTemp.getString("size")));
                node.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemcode")));
                node.setItemMRP(DmShared.xNull(rsTemp.getString("MRP")));
                node.setIsBarcode(DmShared.xNull("1"));
                node.setSkuQty(DmShared.vNull(rsTemp.getString("rcvdQnty")).toString());
                double d = DmShared.vNull(rsTemp.getString("rcvdQnty")) / DmShared.vNull(rsTemp.getString("pUnit"));
                node.setNoOFStricker(DmShared.iNull(Math.ceil(d)).toString());
                lstTemp.add(node);
            }
            mrTemp.getListBr().addAll(lstTemp);
            dmrTemp.setData(mrTemp);
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
            dmrTemp.setStatus(true);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
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

    private BarcodeGeneration_Mdl getRecords(Setting_Mdl setting_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        BarcodeGeneration_Mdl mrTemp = new BarcodeGeneration_Mdl();
        List<GrdBarcodeGeneration_Mdl> lstTemp = new ArrayList<>();

        try {

            if (!DmShared.xNull(setting_Mdl.getFromDate()).equals("")) {
                condition += " and st.v_Date >= :FromDate ";

            }
            if (!DmShared.xNull(setting_Mdl.getToDate()).equals("")) {
                condition += " and st.v_Date <= :toDate  ";

            }
            if (!DmShared.xNull(setting_Mdl.getDepartment()).equals("")) {
                condition += " and st.departmentCode = :departmentCode  ";
            }

            sqlQuery = " Select imb.code, imb.itemcode, max(im.name) itemName, max(icm.name) color, "
                    + " max(imb.batchNo) batchNo, "
                    + " max(imb.expiryDate)expiryDate, max(ism.name) size , max(sg.displayName) department, max(imb.mrp) MRP, "
                    + " max(im.purchaseUnitConversionRate) pUnit, sum(st.recQty) rcvdQnty, max(imb.barcode)barcode     \n"
                    + " from stock st       \n"
                    + " left join itembatch imb on imb.code = st.ibcode  and imb.itemcode= st.itemCode      \n"
                    + " left join itemMast im on im.code = st.itemCode \n"
                    + " left join itemColourMast icm on icm.code = imb.colourCode \n"
                    + " left join itemSizeMast ism on ism.code = imb.sizeCode       \n"
                    + " left join subGroup sg on sg.subCode = st.departmentCode "
                    + " left join voucher_Type vt on vt.v_type = st.v_type "
                    + " where 1=1 and isNull(imb.barcode,'')='' and vt.category in (" + catFilter + ")"
                    + " and im.filterItemType = :filterItemType and isNUll(st.recQty,0) > 0 " + condition
                    + " group by imb.code, imb.itemcode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            if (!DmShared.xNull(setting_Mdl.getFromDate()).equals("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(setting_Mdl.getFromDate()));
            }
            if (!DmShared.xNull(setting_Mdl.getToDate()).equals("")) {
                psTemp.setDate("toDate", DmServer.convertDate(setting_Mdl.getToDate()));
            }
            if (!DmShared.xNull(setting_Mdl.getDepartment()).equals("")) {
                psTemp.setString("departmentCode", DmShared.xNull(setting_Mdl.getDepartment()));
            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdBarcodeGeneration_Mdl node = new GrdBarcodeGeneration_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                node.setColor(DmShared.xNull(rsTemp.getString("color")));
                node.setExpiryDate(DmShared.dNull(rsTemp.getDate("expiryDate")));
                node.setItemName(DmShared.xNull(rsTemp.getString("itemName")));
                node.setBatchNo(DmShared.xNull(rsTemp.getString("batchNo")));
                node.setSize(DmShared.xNull(rsTemp.getString("size")));
                node.setDepartment(DmShared.xNull(rsTemp.getString("department")));
                node.setItemCode(DmShared.xNull(rsTemp.getString("itemcode")));
                node.setItemMRP(DmShared.xNull(rsTemp.getString("MRP")));
                node.setBarcode(DmShared.xNull(rsTemp.getString("barcode")));
                node.setSkuQty(DmShared.vNull(rsTemp.getString("rcvdQnty")).toString());
                double d = DmShared.vNull(rsTemp.getString("rcvdQnty")) / DmShared.vNull(rsTemp.getString("pUnit"));
                node.setNoOFStricker(DmShared.iNull(Math.ceil(d)).toString());
                lstTemp.add(node);
            }
            mrTemp.getListBr().addAll(lstTemp);
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

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

        return mrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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
                    + "	LEFT JOIN subGroupSiteList sgl ON sg.subcode = sgl.subCode "
                    + "	WHERE sgn.manualCode IN ('CM','PH') AND sg.isActive = 1 and sgl.site_Code=:site_Code " + condition;
            sqlWithParam.setSQL(sqlQuery);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                sqlWithParam.setStringList("department", STServer.getSsnDepartmentCodeList(this));
            }
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBarCodeHeader(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "select docid, recid, v_time, v_date from barCodeGeneration ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
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

}
