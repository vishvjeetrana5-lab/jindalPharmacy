package aarogya.initializer.server;

import aarogya.initializer.shared.GrdInitializer_Mdl;
import aarogya.pharmacy.medicineMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import com.google.gson.Gson;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.self.server.DmHandelFile;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmImportData.shared.DmImportStatus;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

public class ImportMedicine implements Initializer_Interface {

    private DmRSS rssTemp;
    private DmHandelFile refreshDataStructure;
    private String vCat = new ProjectVoucherCategory().PHARMACY_MEDICINE_MAST;
    private final String ALTERNATIVE_ITEM_NATURE = "AI";
    private final String SOUND_LOOK_ALIKE_NATURE = "SL";

    public ImportMedicine() {
    }

    @Override
    public DmImportStatus start(DmRSS rssTemp, GrdInitializer_Mdl model) {
        File sessionDir = null;
        BufferedReader br = null;
        String fileName;
        int status = 0, rtnStatus = 0;
        DmImportStatus importStatus = null;
        try {
            this.rssTemp = rssTemp;
            this.refreshDataStructure = new DmHandelFile(model.getLogFilePath(), true);
            Gson gson = new Gson();
            sessionDir = new File(model.getDataPath());
            if (!sessionDir.exists()) {
                sessionDir.mkdir();
            }

            if (sessionDir.list() != null) {
                for (int i = 0; i < sessionDir.list().length; i++) {
                    fileName = sessionDir.list()[i];
                    String tempReadPath = model.getDataPath() + File.separatorChar + fileName;
                    br = new BufferedReader(new FileReader(tempReadPath));
                    MedicineMaster_Mdl mrTemp = gson.fromJson(br, MedicineMaster_Mdl.class);
                    status = initializeModel(mrTemp);
                    if (status != DmImportStatus.ERROR.getId()) {
                        status = save(mrTemp);
                    }
                    if (status > rtnStatus) {
                        rtnStatus = status;
                    }
                    br.close();
                    br = null;
                }
            }
        } catch (Exception e) {
            try {
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                throw new Exception(e);
            } catch (Exception ex) {
            }
        } finally {
            try {
                importStatus = DmImportStatus.getImportStatus(rtnStatus);
                if (refreshDataStructure != null) {
                    refreshDataStructure.close();
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                if (sessionDir != null) {
                    sessionDir = null;
                }
            } catch (Exception e) {
            }
        }
        return importStatus;
    }

    private int initializeModel(MedicineMaster_Mdl mrTemp) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        int status = 0, rtnStatus = 0;
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this.rssTemp));

            mrTemp.setSharedKeyFieldCode("");
            // mrTemp.setManualCode("");
            mrTemp.setManufacturerCode("");
            mrTemp.setManufacturerName("");
            mrTemp.setDivisionTag("");
            mrTemp.setDivisionText("");
            mrTemp.setSchemeFreeQty("");
            mrTemp.setSchemeOnQty("");
            mrTemp.setPurchaseDisPer("");
            mrTemp.setSaleDisPer("");
            mrTemp.setAllowNegetiveStockCode("0");
            mrTemp.setAllowNegetiveStockName("No");
            mrTemp.setPurchaseUnitSkuCode("");
            mrTemp.setSaleUnitSkuCode("");
            mrTemp.setMrp("");

            mrTemp.setMedGroupTag("");
            mrTemp.setMedGroupTag(DmServer.getCodeViaManualCode(mrTemp.getMedGroupText(), "name", "itemGroup", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getMedGroupTag()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getMedGroupText()) + "  Medicine Group does not exist");
            }
            mrTemp.setSkuTag("");
            mrTemp.setSkuTag(DmServer.getCodeViaManualCode(mrTemp.getSkuText(), "unit", "unitMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getSkuTag()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getSkuText()) + " SKU does not exist");

            }

            mrTemp.setPurchaseUnitSkuCode("");
            mrTemp.setPurchaseUnitSkuCode(DmServer.getCodeViaManualCode(mrTemp.getPurchaseUnitSkuName(), "unit", "unitMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getPurchaseUnitSkuCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getPurchaseUnitSkuName()) + " Purchase Unit does not exist");
            }

            mrTemp.setSaleUnitSkuCode("");
            mrTemp.setSaleUnitSkuCode(DmServer.getCodeViaManualCode(mrTemp.getSaleUnitSkuName(), "unit", "unitMast", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getSaleUnitSkuCode()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getSaleUnitSkuName()) + " Sale Unit does not exist");

            }
            mrTemp.setCompanyTag("");
            mrTemp.setCompanyShortName("");
            if (!DmShared.xNull(mrTemp.getCompanyText()).equalsIgnoreCase("")) {
                sqlQuery = "select code,shortName  "
                        + " from itemMakeMast imm "
                        + " where companyName = :companyName ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("companyName", DmShared.xNull(mrTemp.getCompanyText()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setCompanyTag(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setCompanyShortName(DmShared.xNull(rsTemp.getString("shortName")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getCompanyTag()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getCompanyText()) + " Medicine company does not exist");
                }

            }

            mrTemp.setCategoryTag("");
            mrTemp.setCategoryTag(DmServer.getCodeViaManualCode(mrTemp.getCategoryText(), "name", "itemCat", "code", cnTemp));
            if (DmShared.iNull(mrTemp.getCategoryTag()).longValue() == 0) {
                status = DmImportStatus.ERROR.getId();
                throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getCategoryText()) + " Medicine Category does not exist");
            }

            // ************************************** 
            mrTemp.setHsnCode("");
            mrTemp.setGstNatureTag("");
            if (!DmShared.xNull(mrTemp.getHsnName()).equalsIgnoreCase("")) {
                sqlQuery = "select code,itemGSTNature  "
                        + " from hsnMast hsn "
                        + " where name = :name ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("name", DmShared.xNull(mrTemp.getHsnName()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setHsnCode(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setGstNatureTag(DmShared.xNull(rsTemp.getString("itemGSTNature")));
                }
                rsTemp.close();
                psTemp.close();

                if (DmShared.iNull(mrTemp.getHsnCode()).longValue() == 0) {
                    status = DmImportStatus.ERROR.getId();
                    throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getHsnName()) + " HSN not exists");
                }
            }

            if (mrTemp.getGrdComposition_Mdl() != null) {
                for (GrdComposition_Mdl grdComposition : mrTemp.getGrdComposition_Mdl()) {
                    grdComposition.setSharedKeyFieldCode("");
                    grdComposition.setUnitTag("");

                    if (!DmShared.xNull(grdComposition.getSharedKeyFieldName()).trim().equals("")) {
                        grdComposition.setSharedKeyFieldCode(DmServer.getCodeViaManualCode(grdComposition.getSharedKeyFieldName(), "name", "medicinecompomast", "code", cnTemp));
                        if (DmShared.iNull(grdComposition.getSharedKeyFieldCode()).longValue() == 0) {
                            status = DmImportStatus.ERROR.getId();
                            throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(grdComposition.getSharedKeyFieldName()) + "  Medicine composition does not exist.");
                        }
                    }
                    if (!DmShared.xNull(grdComposition.getUnitText()).trim().equals("")) {
                        grdComposition.setUnitTag(DmServer.getCodeViaManualCode(grdComposition.getUnitText(), "unit", "unitMast", "code", cnTemp));
                        if (DmShared.iNull(grdComposition.getUnitTag()).longValue() == 0) {
                            status = DmImportStatus.ERROR.getId();
                            throw new DmException(0, DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(grdComposition.getUnitText()) + "Composition unit does not exist.");
                        }
                    }
                }
            }

            cnTemp.close();

            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                refreshDataStructure.writeLine(e.getMessage());
            } catch (Exception ex) {
            }

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

        return rtnStatus;
    }

    private int save(MedicineMaster_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null, psTemp1 = null;
        DmSavePoint spBegin = null;
        boolean isSiteListExists = false;
        CallableStatement csTemp = null;
        String sqlQuery, vType = "", sqlQuery1 = "";
        ResultSet rsTemp = null;
        int status = 0, rtnStatus = 0;

        dmrTemp.setData("");

        try {
            MedicineMaster_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(rssTemp));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {

                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(rssTemp, "itemMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                status = DmImportStatus.ERROR.getId();
                throw new UnsupportedOperationException("Invalid key.");
            }

            sqlQuery = " Select Count(Name) As Cnt From itemMast mm Where mm.Name = :Name And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Name", mrTemp.getSharedKeyFieldName());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getSharedKeyFieldCode()) + "Medicine Name already exists.");
                }
            }
            rsTemp.close();
            psTemp.close();

            sqlQuery = "SELECT vt.v_Type  FROM voucher_type vt WHERE  vt.category = '" + vCat + " ' ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                vType = DmShared.xNull(rsTemp.getString("v_Type"));
            }
            rsTemp.close();
            psTemp.close();

            // ************************************** GET MANUAL CODE *********************************************
            if (DmShared.xNull(mrTemp.getManualCode()).trim().equalsIgnoreCase("")) {
                HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
                fieldUDTHashMap.put("itemName", new FieldUDT_Mdl("itemName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
                fieldUDTHashMap.put("itemGroup", new FieldUDT_Mdl("itemGroup", DmShared.xNull(mrTemp.getMedGroupText()).toString(), DmShared.xNull(mrTemp.getMedGroupText()).trim()));
                fieldUDTHashMap.put("itemCat", new FieldUDT_Mdl("itemCat", DmShared.xNull(mrTemp.getCategoryText()).toString(), DmShared.xNull(mrTemp.getCategoryText()).trim()));
                csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getItemManualCode(?, ?)}");
                if (csTemp instanceof SQLServerCallableStatement) {
                    ((SQLServerCallableStatement) csTemp).setString(1, DmShared.foriegnKey(vType));
                    ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
                }
                rsTemp = csTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)).toUpperCase());
                }
                rsTemp.close();
                csTemp.close();
            }
            if (DmShared.xNull(mrTemp.getManualCode()).trim().equals("")) {
                status = DmImportStatus.WARNING.getId();
                throw new UnsupportedOperationException(DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getSharedKeyFieldCode()) + "Invalid Manual Code.");
            }

            sqlQuery = " Select Count(ManualCode) As Cnt From itemMast mm Where mm.ManualCode = :ManualCode And Code <> :Code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("Cnt") > 0) {
                    status = DmImportStatus.WARNING.getId();
                    throw new UnsupportedOperationException(DmShared.xNull(mrTemp.getManualCode()) + ":" + DmShared.xNull(mrTemp.getSharedKeyFieldCode()) + "Manual Code already exists.");
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
//<editor-fold defaultstate="collapsed" desc="comment">
//            if (mrTemp.getGrdComposition_Mdl() != null) {
//                String compCsv = "";
//                for (GrdComposition_Mdl grdComposition : mrTemp.getGrdComposition_Mdl()) {
//                    if (DmShared.iNull(grdComposition.getSharedKeyFieldCode()).longValue() > 0) {
//                        if (DmShared.iNull(grdComposition.getRestrictGroupCode()).longValue() > 0) {
//                            sqlQuery = " SELECT "
//                                    + " count(*) as cnt "
//                                    + " FROM medicineCompoMast mcm "
//                                    + " WHERE mcm.code = :Code "
//                                    + " AND mcm.restrictGroupCode=:restrictGroupCode ";
//                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//                            psTemp.setString("Code", grdComposition.getSharedKeyFieldCode());
//                            psTemp.setString("restrictGroupCode", grdComposition.getRestrictGroupCode());
//                            rsTemp = psTemp.executeQuery();
//                            if (rsTemp.next()) {
//                                if (rsTemp.getLong("cnt") > 0) {
//                                    compCsv += grdComposition.getSharedKeyFieldName() + " , ";
//                                }
//                            }
//                            rsTemp.close();
//                            psTemp.close();
//                        }
//                    }
//                }
//                if (!DmShared.xNull(compCsv).trim().equalsIgnoreCase("")) {
//                    throw new UnsupportedOperationException("Restricted Group changed for following compositions : " + compCsv);
//                }
//            }
//</editor-fold>
            sqlQuery = "INSERT INTO itemmast (CODE, NAME, itemGroup, itemType,filterItemType, sku, "
                    + " preparedBY, u_EntDt, transferDt, itemMakeCode, isActive, itemCategoryCode,"
                    + " manualCode, hsnCode,isBestBefore,isLifeSaving,isHighValue,isFlowThrough,  "
                    + " isNarcotics,isConsignment,isReturnableItem,isBilledItem,propertyList,itemGSTNature,purchaseUnit,saleUnit,"
                    + " purchaseUnitConversionRate,saleUnitConversionRate,allowNegativeStock,displayName,isSaleRateEditable,isIncludeFreeQtyInSaleRate,"
                    + " isDiscountAllow,isCommon,instruction,regionalInstruction,itempacking,manufacturerCode,divisionCode"
                    + " , mrp "
                    + " , saleRate "
                    + " , purchaseRate "
                    + " , schemeOnQty "
                    + " , schemeFreeQty "
                    + " , isPOMandatory "
                    + " , isIndentMandatory "
                    + " , isQuotationMandatory "
                    + " , isBarcodeActive "
                    + " , purchaseDiscountPer "
                    + " , saleDiscountPer "
                    + " , priority "
                    + " , preparedDate "
                    + " )  "
                    + " VALUES (:code, :name, :itemGroup, :itemType,:filterItemType,:sku, "
                    + " :preparedBY, :uEntDt, NULL, :itemMakeCode, :isActive,  :itemCategoryCode,"
                    + " :manualCode,:hsnCode,:isBestBefore,:isLifeSaving,:isHighValue,:isFlowThrough, "
                    + " :isNarcotics,:isConsignment,:isReturnableItem,:isBilledItem,:propertyList,:itemGSTNature,:purchaseUnit,:saleUnit,"
                    + " :purchaseUnitConversionRate,:saleUnitConversionRate,:allowNegativeStock,:displayName,:isSaleRateEditable,"
                    + " :isIncludeFreeQtyInSaleRate,:isDiscountAllow,:isCommon,"
                    + " :instruction,:regionalInstruction,:itempacking,:manufacturerCode,:divisionCode"
                    + " , :mrp "
                    + " , :saleRate "
                    + " , :purchaseRate "
                    + " , :schemeOnQty "
                    + " , :schemeFreeQty "
                    + " , :isPOMandatory "
                    + " , :isIndentMandatory "
                    + " , :isQuotationMandatory "
                    + " , :isBarcodeActive "
                    + " , :purchaseDiscountPer "
                    + " , :saleDiscountPer "
                    + " , :priority "
                    + " , :uEntDt "
                    + " )";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getMedGroupTag()));
            psTemp.setString("manualCode", mrTemp.getManualCode());
            psTemp.setString("itemType", DmShared.foriegnKey(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            psTemp.setString("sku", DmShared.foriegnKey(mrTemp.getSkuTag()));
            psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getCompanyTag()));
            psTemp.setString("isActive", DmShared.xNull(mrTemp.getStatusTag()));
            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryTag()));
            psTemp.setString("hsnCode", DmShared.foriegnKey(mrTemp.getHsnCode()));
            psTemp.setString("isBestBefore", DmShared.xNull(mrTemp.getBestBeforeTag()));
            psTemp.setString("isLifeSaving", DmShared.xNull(mrTemp.getLifeSavingTag()));
            psTemp.setString("isHighValue", DmShared.xNull(mrTemp.getHighValueTag()));
            psTemp.setString("isFlowThrough", DmShared.xNull(mrTemp.getFlowThroughTag()));
            psTemp.setString("isNarcotics", DmShared.xNull(mrTemp.getNarcoticsTag()));
            psTemp.setString("isPOMandatory", DmShared.xNull(mrTemp.getPoReqTag()));
            psTemp.setString("isIndentMandatory", DmShared.xNull(mrTemp.getIndentReqTag()));
            psTemp.setString("isQuotationMandatory", DmShared.xNull(mrTemp.getQuotationReqTag()));
            psTemp.setString("isBarcodeActive", DmShared.xNull(mrTemp.getBarcodeActiveTag()));
            psTemp.setString("isConsignment", DmShared.xNull(mrTemp.getConsignmentTag()));
            psTemp.setString("isReturnableItem", DmShared.xNull(mrTemp.getReturnableItemTag()));
            psTemp.setString("isBilledItem", DmShared.xNull(mrTemp.getBilledItemTag()));
            psTemp.setString("propertyList", MedicineMaster_Mdl.MedicineProperties.PROPERTIES.getProperties());
            psTemp.setString("preparedBY", DmServer.getSsnUserName(rssTemp));
            psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
            psTemp.setString("itemGSTNature", DmShared.xNull(mrTemp.getGstNatureTag()));
            psTemp.setString("isDiscountAllow", DmShared.xNull(mrTemp.getIsDiscountAllowTag()));
            psTemp.setString("isCommon", DmShared.xNull(mrTemp.getIsCommonItemTag()));
            psTemp.setString("purchaseUnit", DmShared.xNull(mrTemp.getPurchaseUnitSkuCode()));
            psTemp.setString("saleUnit", DmShared.xNull(mrTemp.getSaleUnitSkuCode()));
            psTemp.setString("purchaseUnitConversionRate", DmServer.vNull(mrTemp.getConversionRatePurchase()));
            psTemp.setString("saleUnitConversionRate", DmServer.vNull(mrTemp.getConversionRateSale()));
            psTemp.setString("allowNegativeStock", DmShared.xNull(mrTemp.getAllowNegetiveStockCode()));
            psTemp.setString("isSaleRateEditable", DmShared.xNull(mrTemp.getIsSaleRateEditableCode()));
            psTemp.setString("isIncludeFreeQtyInSaleRate", DmShared.xNull(mrTemp.getIsIncludeFreeQtyInSaleRateCode()));
            psTemp.setString("displayName", DmShared.xNull(mrTemp.getDisplayName()));
            psTemp.setString("instruction", DmShared.xNull(mrTemp.getInstruction()));
            psTemp.setString("regionalInstruction", DmShared.xNull(mrTemp.getRegionalInstruction()));
            psTemp.setString("itempacking", DmShared.xNull(mrTemp.getPackSize()));
            psTemp.setString("manufacturerCode", DmShared.foriegnKey(mrTemp.getManufacturerCode()));
            psTemp.setString("divisionCode", DmShared.foriegnKey(mrTemp.getDivisionTag()));
            psTemp.setString("mrp", DmServer.vNull(mrTemp.getMrp(), "0.00"));
            psTemp.setString("saleRate", DmServer.vNull(mrTemp.getSaleRate(), "0.00"));
            psTemp.setString("purchaseRate", DmServer.vNull(mrTemp.getPurchaseRate(), "0.00"));
            psTemp.setString("schemeFreeQty", DmServer.vNull(mrTemp.getSchemeFreeQty()));
            psTemp.setString("schemeOnQty", DmServer.vNull(mrTemp.getSchemeOnQty()));
            psTemp.setString("purchaseDiscountPer", DmServer.vNull(mrTemp.getPurchaseDisPer(), "0.00"));
            psTemp.setString("saleDiscountPer", DmServer.vNull(mrTemp.getSaleDisPer(), "0.00"));
            psTemp.setString("priority", DmShared.iNull(mrTemp.getPriority()).toString());

            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="INSERT IN ITEM_SITE_LIST">
            if (!isSiteListExists) {
                sqlQuery1 = "INSERT INTO itemSiteList (CODE, site_Code)  "
                        + " VALUES (:code, :site_Code)";
                psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery1);
                psTemp1.setString("code", DmShared.foriegnKey(mrTemp.getSharedKeyFieldCode()));
                psTemp1.setString("site_Code", DmShared.foriegnKey(DmServer.getSsnSiteCode(rssTemp)));

                psTemp1.executeUpdate();
                psTemp1.close();
            }
            //</editor-fold>

            /*===========================< Update record of itemMast >===============================*/
            sqlQuery = "UPDATE itemMast "
                    + " SET itemMast.name = vItem.itemName "
                    + " FROM ( "
                    + " 	SELECT im.code as itemCode "
                    + " 	, im.displayName + '-' + ic.name + '-' + comp.shortName + ' (Packing : ' + im.itemPacking + ')' AS itemName "
                    + " 	FROM itemMast im  "
                    + " 	INNER JOIN itemCat ic ON im.itemCategoryCode = ic.code "
                    + "         INNER JOIN itemmakemast AS comp ON im.itemMakeCode = comp.code  "
                    + " 	WHERE im.code = :code "
                    + " 	) vItem "
                    + " WHERE itemMast.code = vItem.itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            sqlQuery = "Update  im set "
                    + "         im.itemGSTNature = hm.itemGSTNature,"
                    + "         im.itsBasedOn = hm.itsBasedOn "
                    + "         from itemmast as im "
                    + "         left join hsnMast as hm on im.hsnCode = hm.code "
                    + "         where im.code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of itemITS >===============================*/
            sqlQuery = " delete from itemITSDetail where code = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            if (DmShared.iNull(mrTemp.getHsnCode()).intValue() > 0) {
                sqlQuery = "insert into itemITSDetail (code ,v_SNo,rateFrom,rateUpto,its_Code_Purchase,its_Code_PurchaseRet,"
                        + "                  its_Code_Sales,its_Code_SalesRet,purchaseAc,purchaseReturnAc,salesAc,salesReturnAc) "
                        + "         		(select :code ,hid.v_SNo,hid.rateFrom,hid.rateUpto,hid.its_Code_Purchase,hid.its_Code_PurchaseRet,"
                        + "                 hid.its_Code_Sales,hid.its_Code_SalesRet,hid.purchaseAc,hid.purchaseReturnAc,hid.salesAc,hid.salesReturnAc "
                        + "         		from hsnITSDetail as hid where hid.code = :hsnCode) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
                psTemp.setString("hsnCode", mrTemp.getHsnCode());
                psTemp.executeUpdate();
                psTemp.close();

            }

            //============================= Item Details Grid Entry =================================
            sqlQuery = " delete from itemDepartmentInfo where itemCode = :itemCode  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //=============================Alternative Items Grid Entry =================================
            sqlQuery = " delete from alternativeItem where code = :code  and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", DmShared.xNull(ALTERNATIVE_ITEM_NATURE));
            psTemp.executeUpdate();
            psTemp.close();

            //=============================Composition Grid Entry =================================
            sqlQuery = " delete from medicineComposition where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            int sNo2 = 0;
            if (mrTemp.getGrdComposition_Mdl() != null) {
                for (GrdComposition_Mdl grdComposition : mrTemp.getGrdComposition_Mdl()) {

                    if (DmShared.iNull(grdComposition.getSharedKeyFieldCode()).longValue() > 0) {
                        sqlQuery = "INSERT INTO medicineComposition (code, sNo, compositionCode, power, isBase, unitCode) "
                                + "VALUES (:code, :sNo, :compositionCode, :power,:isBase,:unitCode);";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                        psTemp.setInt("sNo", ++sNo2);
                        psTemp.setString("compositionCode", DmShared.foriegnKey(grdComposition.getSharedKeyFieldCode()));
                        psTemp.setString("power", DmServer.vNull(grdComposition.getPower()));
                        psTemp.setString("isBase", grdComposition.getBaseTag());
                        psTemp.setString("unitCode", DmShared.foriegnKey(grdComposition.getUnitTag()));
                        psTemp.executeUpdate();
                        psTemp.close();

                        if (DmShared.iNull(grdComposition.getBaseTag()).longValue() == 1) {
                            sqlQuery = "UPDATE itemMast "
                                    + " set itemMast.compositionCode=:compositionCode "
                                    + " where itemMast.code=:code";
                            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                            psTemp.setString("compositionCode", DmShared.foriegnKey(grdComposition.getSharedKeyFieldCode()));
                            psTemp.executeUpdate();
                            psTemp.close();

                        }
                    }
                }
            }
            //============================= sound and look alike Item  Grid Entry =================================
            sqlQuery = " delete from alternativeItem where code = :code  and nature = :nature ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("nature", DmShared.xNull(SOUND_LOOK_ALIKE_NATURE));
            psTemp.executeUpdate();
            psTemp.close();

            //============================= Restricted department Entry =================================
            sqlQuery = " delete from itemRestrictedToDepartment where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //============================= Approved vendors Grid Entry =================================
            sqlQuery = " delete from approvedVendors where code = :code  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="SAVE IN ITEM SALE SCHEME DETAILS">
            sqlQuery = "Delete From itemSaleSchemeDetail Where code= :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();

            //<editor-fold defaultstate="collapsed" desc="Update Item Mast">
            sqlQuery = " UPDATE "
                    + " itemMast "
                    + " SET "
                    + " itemMast.restrictGroup = vMcm.restrictGroupCode "
                    + " FROM ( "
                    + "     SELECT "
                    + "       im.code AS itemCode "
                    + "     , MAX(mcm.restrictGroupCode) AS restrictGroupCode "
                    + "     FROM itemMast im "
                    + "     LEFT JOIN medicineComposition mc ON mc.code=im.code "
                    + "     LEFT JOIN medicineCompoMast mcm ON mcm.code=mc.compositionCode "
                    + "     LEFT JOIN restrictedGrpMast rgm ON rgm.code=mcm.restrictGroupCode "
                    + "     WHERE im.code=:code "
                    + "     GROUP BY im.code "
                    //                    + "     HAVING SUM(CASE WHEN ISNULL(mcm.restrictGroupCode,ISNULL(im.restrictGroup,0)) = ISNULL(im.restrictGroup,0) THEN 1 ELSE 0 END)  = COUNT(im.code) "
                    + "	) vMcm "
                    + " WHERE itemMast.code = vMcm.itemCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            psTemp.executeUpdate();
            psTemp.close();
//</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="Chk of wrong restrict group update">
            sqlQuery = " SELECT "
                    + " COUNT(*) cnt "
                    + " , im.code "
                    + " , Max(im.name) "
                    + " FROM itemMast im "
                    + " LEFT JOIN medicineComposition mc ON mc.code=im.code "
                    + " LEFT JOIN medicineCompoMast mcm ON mcm.code=mc.compositionCode "
                    + " WHERE im.code = :code "
                    + " GROUP BY im.code "
                    + " HAVING SUM(CASE WHEN ISNULL(mcm.restrictGroupCode,ISNULL(im.restrictGroup,0)) = ISNULL(im.restrictGroup,0) THEN 1 ELSE 0 END)  <> COUNT(im.code)";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("Code", mrTemp.getSharedKeyFieldCode());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (rsTemp.getLong("cnt") > 0) {
                    throw new UnsupportedOperationException("Restricted Group changed for compositions in composition Master");
                }
            }
            rsTemp.close();
            psTemp.close();
//</editor-fold>
            //========================== END====================================================

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
            spBegin.commit();
            cnTemp.close();
            dmrTemp.setStatus(true);
            rtnStatus = DmImportStatus.SUCCESS.getId();
        } catch (Exception e) {
            try {
                if (status == DmImportStatus.NONE.getId()) {
                    status = DmImportStatus.ERROR.getId();
                }
                if (status > rtnStatus) {
                    rtnStatus = status;
                }
                refreshDataStructure.writeLine(e.getMessage());
                if (spBegin != null) {
                    spBegin.rollBack();
                }

                // dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
            } catch (Exception ex) {
            }

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

        return rtnStatus;
    }
}
