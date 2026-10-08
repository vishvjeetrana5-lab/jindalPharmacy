package aarogya.pharmacy.duplicateBarcodeGeneration.server;

import aarogya.pharmacy.duplicateBarcodeGeneration.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DuplicateBarcodeGeneration_SrvImpl extends DmRSSPrivate implements DuplicateBarcodeGeneration_Srv {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    String catFilter = "'" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','"
            + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "','"
            + pvc.PHARMACY_PURCHASE_CHALLAN + "','"
            + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "','"
            + pvc.MEDICINE_OPENING + "'";

    @Override
    public DmResult<DuplicateBarcodeGeneration_Mdl> getFillRecords(Setting_Mdl setting_Mdl) {
        DmResult<DuplicateBarcodeGeneration_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        String barCodeGeneration;
        try {

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            barCodeGeneration = DmServer.getCodeViaManualCode(pvc.BAR_CODE_GENERATION, "category", "voucher_Type", "v_type", cnTemp);
            dmrTemp.setData(getRecords(setting_Mdl, cnTemp));
            dmrTemp.setStatus(true);
            dmrTemp.setValue1(barCodeGeneration);
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

    private DuplicateBarcodeGeneration_Mdl getRecords(Setting_Mdl setting_Mdl, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        DuplicateBarcodeGeneration_Mdl mrTemp = new DuplicateBarcodeGeneration_Mdl();
        List<GrdDuplicateBarcodeGeneration_Mdl> lstTemp = new ArrayList<>();

        try {

            if (!DmShared.xNull(setting_Mdl.getFromDate()).equals("")) {
                condition += " and st.v_Date >= :FromDate ";
            }
            if (!DmShared.xNull(setting_Mdl.getToDate()).equals("")) {
                condition += " and st.v_Date <= :toDate  ";
            }

            if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                condition += " and st.departmentCode IN(" + DmServer.getParamList(":departmentCode", setting_Mdl.getDepartmentCode()) + " )";
            }

            if (!DmShared.xNull(setting_Mdl.getBarCodeRecid()).equalsIgnoreCase("")) {
                condition += " and imb.barcode IN(" + DmServer.getParamList(":barcode", setting_Mdl.getBarCodeRecid()) + " )";
            }

            if (!DmShared.xNull(setting_Mdl.getBatch()).equalsIgnoreCase("")) {
                condition += " and imb.batchNo IN(" + DmServer.getParamList(":batchNo", setting_Mdl.getBatch()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getColorCode()).equalsIgnoreCase("")) {
                condition += " and icm.code IN(" + DmServer.getParamList(":colorCode", setting_Mdl.getColorCode()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getExpiryDate()).equalsIgnoreCase("")) {
                condition += " and imb.expiryDate = :expiryDate  ";
            }
            if (!DmShared.xNull(setting_Mdl.getManufacturingDate()).equalsIgnoreCase("")) {
                condition += " and imb.manufacturingDate = :manufacturingDate  ";
            }
            if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                condition += " and imb.itemcode IN(" + DmServer.getParamList(":itemcode", setting_Mdl.getItemCode()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getMrp()).equalsIgnoreCase("")) {
                condition += " and imb.mrp IN(" + DmServer.getParamList(":mrp", setting_Mdl.getMrp()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getSaleRate()).equalsIgnoreCase("")) {
                condition += " and imb.saleRate IN(" + DmServer.getParamList(":saleRate", setting_Mdl.getSaleRate()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getSizeCode()).equalsIgnoreCase("")) {
                condition += " and imb.sizeCode IN(" + DmServer.getParamList(":sizeCode", setting_Mdl.getSizeCode()) + " )";
            }
            if (!DmShared.xNull(setting_Mdl.getpBillNoCode()).equalsIgnoreCase("")) {
                condition += " and st.docid IN(" + DmServer.getParamList(":pbDocid", setting_Mdl.getpBillNoCode()) + " )";
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
                    + " left join barCodeGenerationDetail barcgn on barcgn.barCode = imb.barcode       \n"
                    + " left join subGroup sg on sg.subCode = st.departmentCode "
                    + " left join voucher_Type vt on vt.v_type = st.v_type "
                    + " where 1=1 and isNull(imb.barcode,'')<>'' and vt.category in (" + catFilter + ")"
                    + " and im.filterItemType = :filterItemType and isNUll(st.recQty,0) > 0 " + condition
                    + " group by imb.code, imb.itemcode "
                    + " order by barcode desc";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            if (!DmShared.xNull(setting_Mdl.getFromDate()).equalsIgnoreCase("")) {
                psTemp.setDate("fromDate", DmServer.convertDate(setting_Mdl.getFromDate()));
            }
            if (!DmShared.xNull(setting_Mdl.getToDate()).equalsIgnoreCase("")) {
                psTemp.setDate("toDate", DmServer.convertDate(setting_Mdl.getToDate()));
            }
            if (!DmShared.xNull(setting_Mdl.getDepartmentCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("departmentCode", DmShared.xNull(setting_Mdl.getDepartmentCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getBarCodeRecid()).equalsIgnoreCase("")) {
                psTemp.setStringList("barcode", DmShared.xNull(setting_Mdl.getBarCodeRecid()));
            }
            if (!DmShared.xNull(setting_Mdl.getBatch()).equalsIgnoreCase("")) {
                psTemp.setStringList("batchNo", DmShared.xNull(setting_Mdl.getBatch()));
            }
            if (!DmShared.xNull(setting_Mdl.getColorCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("colorCode", DmShared.xNull(setting_Mdl.getColorCode()));
            }
            if (!DmShared.xNull(setting_Mdl.getExpiryDate()).equalsIgnoreCase("")) {
                psTemp.setDate("expiryDate", DmServer.convertDate(setting_Mdl.getExpiryDate()));
            }
            if (!DmShared.xNull(setting_Mdl.getManufacturingDate()).equalsIgnoreCase("")) {
                psTemp.setDate("manufacturingDate", DmServer.convertDate(setting_Mdl.getManufacturingDate()));
            }

            if (!DmShared.xNull(setting_Mdl.getItemCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("itemcode", DmShared.xNull(setting_Mdl.getItemCode()));
            }

            if (!DmShared.xNull(setting_Mdl.getMrp()).equalsIgnoreCase("")) {
                psTemp.setStringList("mrp", DmShared.xNull(setting_Mdl.getMrp()));
            }

            if (!DmShared.xNull(setting_Mdl.getSaleRate()).equalsIgnoreCase("")) {
                psTemp.setStringList("saleRate", DmShared.xNull(setting_Mdl.getSaleRate()));
            }
            if (!DmShared.xNull(setting_Mdl.getSizeCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("sizeCode", DmShared.xNull(setting_Mdl.getSizeCode()));

            }
            if (!DmShared.xNull(setting_Mdl.getpBillNoCode()).equalsIgnoreCase("")) {
                psTemp.setStringList("pbDocid", DmShared.xNull(setting_Mdl.getpBillNoCode()));

            }
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                GrdDuplicateBarcodeGeneration_Mdl node = new GrdDuplicateBarcodeGeneration_Mdl();
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
                node.setIsBarcode("1");
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

            sqlQuery = "    select docid, barCode, v_time, v_date from barCodeGenerationDetail  ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("barCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("barCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHItem(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "select ib.itemcode,  im.Name from itemBatch ib          \n"
                    + "     left join itemMast im on im.code = ib.itemCode  where  ISNULL(ib.Barcode,'')<>''    \n"
                    + "     group by ib.itemcode, im.name ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("itemcode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHBatchNo(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "select ib.batchNo from itemBatch ib where isNUll(ib.batchNo,'')<>'' and ISNULL(ib.Barcode,'')<>'' group by ib.batchNo  ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("batchNo")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("batchNo")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHColor(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "    select ib.colourCode,  im.Name from itemBatch ib \n"
                    + "     left join itemColourMast im on im.code = ib.itemCode where ISNULL(ib.colourCode,'')<>'' "
                    + "         and ISNULL(ib.Barcode,'')<>'' \n"
                    + "     group by ib.colourCode, im.name ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("colourCode")).toString());
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSize(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "   select ib.sizeCode,  im.Name from itemBatch ib   \n"
                    + "     left join itemSizeMast im on im.code = ib.itemCode where ISNULL(ib.sizeCode,'')<>'' and ISNULL(ib.Barcode,'')<>'' \n"
                    + "     group by ib.sizeCode, im.name   ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("sizeCode")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHManufacturingDate(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "   select manufacturingDate from itemBatch ib where  ISNULL(ib.manufacturingDate,'')<>'' and ISNULL(ib.Barcode,'')<>'' group by manufacturingDate ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("manufacturingDate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHExpiryDate(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "   select expiryDate from itemBatch ib where  ISNULL(ib.expiryDate,'')<>'' and ISNULL(ib.Barcode,'')<>'' group by expiryDate ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.dNull(rsTemp.getDate("expiryDate")));
                listNode.setSharedKeyFieldName(DmShared.dNull(rsTemp.getDate("expiryDate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSaleRate(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "   select skuSaleRate from itemBatch ib where  ISNULL(ib.skuSaleRate,'')<>''  and ISNULL(ib.Barcode,'')<>'' group by skuSaleRate";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("skuSaleRate")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("skuSaleRate")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMRP(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlQuery = "   select mrp from itemBatch ib where  ISNULL(ib.mrp,'')<>'' and ISNULL(ib.Barcode,'')<>'' group by mrp  ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("mrp")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("mrp")));
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
    public DmResultPageing<List<PBillNoH_Mdl>> getTxtHPBillNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<PBillNoH_Mdl>> rstTemp = new DmResultPageing<>();
        List<PBillNoH_Mdl> lstTemp = new ArrayList<>();
        PBillNoH_Mdl node;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "    select pb.docid, pb.recid, pb.v_date, vt.description,                       "
                    + "     sg.name as supplierName, pb.partyBillNo, pb.partyBillDate    \n"
                    + "   from pbill pb                                                                 \n"
                    + "   left join voucher_type vt on vt.v_Type = pb.v_Type                            \n"
                    + "   left join subgroup sg on pb.subcode = sg.subCode                              \n"
                    + "   where vt.category in ('" + pvc.PHARMACY_PURCHASE_BILL_DIRECT + "','" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "') ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new PBillNoH_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docid")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("recid")));
                node.setDescription(DmShared.xNull(rsTemp.getString("description")));
                node.setPartyBillDate(DmShared.dNull(rsTemp.getDate("partyBillDate")));
                node.setPartyBillNO(DmShared.xNull(rsTemp.getString("partyBillNo")));
                node.setRecid(DmShared.xNull(rsTemp.getString("recid")));
                node.setSupplierName(DmShared.xNull(rsTemp.getString("supplierName")));
                node.setPdate(DmShared.dNull(rsTemp.getDate("v_date")));
                lstTemp.add(node);
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
