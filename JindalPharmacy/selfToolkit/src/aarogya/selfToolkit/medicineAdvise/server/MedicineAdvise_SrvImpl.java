package aarogya.selfToolkit.medicineAdvise.server;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.medicineAdvise.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.CustomWidgetNature;
import com.jolbox.bonecp.ConnectionHandle;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerConnection;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicineAdvise_SrvImpl extends DmRSSPrivate implements MedicineAdvise_Srv {

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedicineCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> listNode = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        String sqlQuery;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	code, name from itemCat where filterItemType = :filterItemType ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile, String stockSite) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHMedicine_Mdl> listNode = new ArrayList<TxtHMedicine_Mdl>();
        TxtHMedicine_Mdl node;
        String[] depatSiteCodeArray1, depatSiteCodeArray2 = null;
        String sqlQuery, join = "", condition = "", column = ", 0 as stockQty ";
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (!DmShared.xNull(stockSite).equalsIgnoreCase("")) {
                depatSiteCodeArray1 = stockSite.split(",");
                if (depatSiteCodeArray1 != null) {
                    for (String s : depatSiteCodeArray1) {
                        if (!condition.trim().equalsIgnoreCase("")) {
                            condition += " or ";
                        }
                        depatSiteCodeArray2 = s.split("\\|");
                        condition += " (ist.departmentCode = " + depatSiteCodeArray2[0] + " and  ist.site_Code = " + depatSiteCodeArray2[1] + ") ";
                    }

                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition = " and ( " + condition + ") ";
                    }
                }

                column = ", ist.stockQty ";
                join = " left join ( "
                        + "	select  ist.itemCode, sum(ist.balanceQty) as stockQty "
                        + "		from itemStock ist "
                        + "             left join itemMast as im on ist.itemCode = im.code "
                        + "		where im.filterItemType = :filterItemType and im.isActive in (1,2) " + condition
                        + "		group by ist.itemCode, ist.departmentCode "
                        + " ) ist on ist.itemCode = im.code ";
            }

            sqlQuery = "Select	im.code, im.name, im.manualCode, im.compositionCode, mcm.name as compositionName, "
                    + "		im.itemCategoryCode, ic.name as itemCategoryName, ums.unit, ums.decimalPlaces AS skuDecimalPlaces " + column
                    + "		from itemMast as im "
                    + "		left join medicineCompoMast as mcm on im.compositionCode = mcm.code "
                    + "		left join itemCat as ic on im.itemCategoryCode = ic.code "
                    + "         left join unitmast ums ON im.sku = ums.code " + join
                    + "		where im.filterItemType = :filterItemType and im.isActive in (1,2) ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHMedicine_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
                node.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                node.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                node.setCategoryCode(DmShared.xNull(rsTemp.getString("itemCategoryCode")));
                node.setCategoryName(DmShared.xNull(rsTemp.getString("itemCategoryName")));
                node.setStock(DmServer.vNull(DmServer.vNull(rsTemp.getString("stockQty"), DmShared.getNumberFormat(DmShared.iNull(rsTemp.getString("skuDecimalPlaces")).intValue()))));
                node.setUnit(DmShared.xNull(rsTemp.getString("unit")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
    public DmResultPageing<List<TxtHFrequency_Mdl>> getTxtHFrequency(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHFrequency_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHFrequency_Mdl> listNode = new ArrayList<TxtHFrequency_Mdl>();
        TxtHFrequency_Mdl node;
        String sqlQuery;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select code, frequencyName as name, presentation, frequencyTime from frequencyMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHFrequency_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setTime(DmShared.xNull(rsTemp.getString("frequencyTime")));
                node.setPresentation(DmShared.xNull(rsTemp.getString("presentation")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDuration(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Day");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Day (Next)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Week");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("3");
            listNode.setSharedKeyFieldName("Week (Next)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("4");
            listNode.setSharedKeyFieldName("Month");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("5");
            listNode.setSharedKeyFieldName("Month (Next)");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("6");
            listNode.setSharedKeyFieldName("Dose");
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
    public DmResultPageing<List<GrdFavorite_Mdl>> getFavoriteDetail(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<GrdFavorite_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<GrdFavorite_Mdl> listNode = new ArrayList<GrdFavorite_Mdl>();
        GrdFavorite_Mdl node;
        String sqlQuery;
        rstTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	pmf.itemCode as code, im.name, im.compositionCode, mcm.name as compositionName, "
                    + "		pmf.doseText, pmf.doseDisplay, pmf.frequencyCode, fm.frequencyName, fm.presentation,"
                    + "         pmf.instructionCode, ins.name as instruction "
                    + "		from prescriptionMedFavourite as pmf "
                    + "		left join itemMast as im on pmf.itemCode = im.code "
                    + "		left join medicineCompoMast as mcm on im.compositionCode = mcm.code "
                    + "		left join frequencyMast as fm on pmf.frequencyCode = fm.code "
                    + "         left join prescriptionDetailMast ins on ins.code = pmf.instructionCode "
                    + "		where pmf.consultantCode = :consultantCode ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new GrdFavorite_Mdl();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setCompositionCode(DmShared.xNull(rsTemp.getString("compositionCode")));
                node.setCompositionName(DmShared.xNull(rsTemp.getString("compositionName")));
                node.setFrequencyCode(DmShared.xNull(rsTemp.getString("frequencyCode")));
                node.setFrequencyName(DmShared.xNull(rsTemp.getString("frequencyName")));
                node.setDosePresentation(DmShared.xNull(rsTemp.getString("presentation")));
                node.setDoseValue(DmShared.xNull(rsTemp.getString("doseText")));
                node.setDoseText(DmShared.xNull(rsTemp.getString("doseText")));
                node.setDoseDisplayText(DmShared.xNull(rsTemp.getString("doseDisplay")));
                node.setInstructionCode(DmShared.xNull(rsTemp.getString("instructionCode")));
                node.setInstructionName(DmShared.xNull(rsTemp.getString("instruction")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
    public DmResult<String> addFavorite(GrdMedicineDetail_Mdl mrTemp, String consultantCode) {
        DmResult<String> dmrTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, code = "";
        boolean isAdd = false;
        dmrTemp.setData("");

        try {
            if (DmShared.iNull(consultantCode).longValue() == 0) {
                throw new DmException(0, "Consultant is mandatory.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*=====================< Check weather favorite already exist or not >======================*/
            sqlQuery = "Select  consultantCode "
                    + "         from prescriptionMedFavourite "
                    + "         where consultantCode = :consultantCode and itemCode = :itemCode "
                    + "         and doseText = :doseText and frequencyCode = :frequencyCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("doseText", DmShared.xNull(mrTemp.getDoseText()).trim());
            psTemp.setString("frequencyCode", DmShared.iNull(mrTemp.getFrequencyCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                isAdd = false;
            } else {
                isAdd = true;
                code = DmServer.getSerialNoInteger(this, "prescriptionMedFavourite", cnTemp);
            }
            rsTemp.close();
            psTemp.close();

            if (isAdd) {
                /*=====================< Insert into prescriptionMedFavourite >======================*/
                sqlQuery = "Insert into prescriptionMedFavourite (code, itemCode, doseText, doseDisplay, frequencyCode, consultantCode, instructionCode) "
                        + " values (:code, :itemCode, :doseText, :doseDisplay, :frequencyCode, :consultantCode, :instructionCode)";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.iNull(code).toString());
                psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
                psTemp.setString("itemCode", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("doseText", DmShared.xNull(mrTemp.getDoseText()).trim());
                psTemp.setNString("doseDisplay", DmShared.xNull(mrTemp.getDoseDisplayText()).trim());
                psTemp.setString("frequencyCode", DmShared.foriegnKey(mrTemp.getFrequencyCode()));
                psTemp.setString("InstructionCode", DmShared.foriegnKey(mrTemp.getInstructionCode()));
                psTemp.executeUpdate();
                psTemp.close();
            } else {
                throw new DmException(0, "Medicine already exist in favorite.");
            }

            spBegin.commit();
            dmrTemp.setData("");
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResult<String> removeFavorite(String consultantCode, String itemCode, String doseText, String frequencyCode) {
        DmResult<String> dmrTemp = new DmResult<String>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        dmrTemp.setData("");

        try {
            if (DmShared.iNull(consultantCode).longValue() == 0) {
                throw new DmException(0, "Consultant is mandatory.");
            }

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            /*==============================<< Delete from prescriptionMedFavourite >>===============================*/
            sqlQuery = "Delete from prescriptionMedFavourite where consultantCode = :consultantCode and itemCode = :itemCode "
                    + " and doseText = :doseText and frequencyCode = :frequencyCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("consultantCode", DmShared.iNull(consultantCode).toString());
            psTemp.setString("itemCode", DmShared.iNull(itemCode).toString());
            psTemp.setString("doseText", DmShared.xNull(doseText).trim());
            psTemp.setString("frequencyCode", DmShared.iNull(frequencyCode).toString());
            psTemp.executeUpdate();
            psTemp.close();

            spBegin.commit();
            dmrTemp.setData("");
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResultPageing<List<String>> getSuggestionMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(data);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("Select displayName from itemMast ");
            helper.addSort("displayName", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("displayName")));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(data.size(), blnToEndOfFile);
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
    public DmResult<AddNewRecord_Mdl> saveNewRecord(AddNewRecord_Mdl mrTemp) {
        DmResult<AddNewRecord_Mdl> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        CallableStatement csTemp = null;
        String sqlQuery;
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoBigInt(this, "itemMast", cnTemp));

            /*=====================< Get manual code >======================*/
            HashMap<String, FieldUDT_Mdl> fieldUDTHashMap = new HashMap<String, FieldUDT_Mdl>();
            fieldUDTHashMap.put("itemName", new FieldUDT_Mdl("itemName", DmShared.xNull(mrTemp.getSharedKeyFieldName()), DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim()));
            fieldUDTHashMap.put("itemGroup", new FieldUDT_Mdl("itemGroup", DmShared.xNull(mrTemp.getGroupName()).toString(), DmShared.xNull(mrTemp.getGroupName()).trim()));
            fieldUDTHashMap.put("itemCat", new FieldUDT_Mdl("itemCat", DmShared.xNull(mrTemp.getCategoryName()).toString(), DmShared.xNull(mrTemp.getCategoryName()).trim()));
            csTemp = ((SQLServerConnection) ((ConnectionHandle) cnTemp).getInternalConnection()).prepareCall("{call getItemManualCode(?, ?)}");
            if (csTemp instanceof SQLServerCallableStatement) {
                ((SQLServerCallableStatement) csTemp).setString(1, DmShared.foriegnKey(DmServer.getVType(new ProjectVoucherCategory().PHARMACY_MEDICINE_MAST, cnTemp)));
                ((SQLServerCallableStatement) csTemp).setStructured(2, "dbo.fieldUDT", (SQLServerDataTable) DmServer.getFieldUDT(fieldUDTHashMap));
            }
            rsTemp = csTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setManualCode(DmShared.xNull(rsTemp.getString(1)).toString().toUpperCase());
            }
            rsTemp.close();
            csTemp.close();

            if (DmShared.iNull(mrTemp.getSharedKeyFieldCode()).longValue() == 0) {
                throw new UnsupportedOperationException("Invalid key.");
            }

            /*=====================< Check weather Name already exist or not >======================*/
            sqlQuery = "Select code from itemMast where name = :name and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            /*=====================< Check weather Manual Code already exist or not >======================*/
            sqlQuery = "Select code from itemMast where manualCode = :manualCode and code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()).trim());
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                throw new UnsupportedOperationException("Manual Code already exists.");
            }
            rsTemp.close();
            psTemp.close();

            /*=====================< Insert into itemMast >======================*/
            sqlQuery = "Insert into itemMast(code, name, displayName, manualCode, itemType, filterItemType, itemCategoryCode, itemMakeCode, "
                    + " itemGroup, isActive, preparedBy, u_EntDt) "
                    + " values (:code, :name, :displayName, :manualCode, :itemType, :filterItemType, :itemCategoryCode, :itemMakeCode, "
                    + " :itemGroup, :isActive, :preparedBy, :u_EntDt) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("displayName", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("manualCode", DmShared.xNull(mrTemp.getManualCode()));
            psTemp.setString("itemType", "1");
            psTemp.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            psTemp.setString("itemCategoryCode", DmShared.foriegnKey(mrTemp.getCategoryCode()));
            psTemp.setString("itemMakeCode", DmShared.foriegnKey(mrTemp.getCompanyCode()));
            psTemp.setString("itemGroup", DmShared.foriegnKey(mrTemp.getGroupCode()));
            psTemp.setString("isActive", "2");
            psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
            psTemp.setTimestamp("u_EntDt", DmServer.getServerTimestamp());
            psTemp.executeUpdate();
            psTemp.close();

            /*=====================< Insert into itemSiteList >======================*/
            sqlQuery = "Insert into itemSiteList(code, site_Code) "
                    + " values (:code, :site_Code) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
            psTemp.executeUpdate();
            psTemp.close();

            /*===========================< Update record of itemMast >===============================*/
            sqlQuery = "UPDATE  itemMast "
                    + "         SET itemMast.name = vItem.itemName "
                    + "         FROM ( "
                    + "             SELECT  im.code as itemCode, "
                    + "                     im.displayName + '-' + isNull(ic.name,'') + '-' + isNull(comp.shortName,'') + ' (Packing : ' + isNull(im.itemPacking,'-') + ')' AS itemName "
                    + "             FROM itemMast im  "
                    + "             LEFT JOIN itemCat ic ON im.itemCategoryCode = ic.code "
                    + "             LEFT JOIN itemMakeMast AS comp ON im.itemMakeCode = comp.code "
                    + "             WHERE im.code = :code "
                    + "         ) vItem "
                    + "         WHERE itemMast.code = vItem.itemCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            psTemp.executeUpdate();
            psTemp.close();

            /*=====================< get name & displayName >======================*/
            sqlQuery = "Select name, displayName from itemMast where code = :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setDisplayName(DmShared.xNull(rsTemp.getString("displayName")));
            }
            rsTemp.close();
            psTemp.close();

            dmrTemp.setData(mrTemp);
            spBegin.commit();
            dmrTemp.setStatus(true);
            cnTemp.close();
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
    public DmResultPageing<List<TxtHInstruction_Mdl>> getTxtHInstruction(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHInstruction_Mdl>> rstTemp = new DmResultPageing<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<TxtHInstruction_Mdl> listNode = new ArrayList<TxtHInstruction_Mdl>();
        TxtHInstruction_Mdl node;
        rstTemp.setData(listNode);
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("select  pdm.code, pdm.name, pdm.shortName, pdm.groupCode, pdgm.name as groupName "
                    + "         from prescriptionDetailMast as pdm "
                    + "         left join prescriptionDetailGroupMast as pdgm on pdm.groupCode = pdgm.code "
                    + "         where pdm.nature = :nature ");
            sqlWithParam.setString("nature", DmShared.xNull(CustomWidgetNature.MEDICINE_INSTRUCTION.getId()).trim());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new TxtHInstruction_Mdl();
                node.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                node.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                node.setGroupCode(DmShared.xNull(rsTemp.getString("groupCode")));
                node.setGroupName(DmShared.xNull(rsTemp.getString("groupName")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMedGroup(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT ig.code, ig.name "
                    + " FROM itemGroup ig "
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon = 1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResultPageing<List<TxtHCompany_Mdl>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHCompany_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHCompany_Mdl> lstTemp = new ArrayList<>();
        TxtHCompany_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlWithParam.setSQL("SELECT CODE, companyName, shortName, purchaseDiscountPer FROM itemmakemast"
                    + "   WHERE filterItemType = :filterItemType ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHCompany_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("CODE")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("companyName")));
                listNode.setShortName(DmShared.xNull(rsTemp.getString("shortName")));
                listNode.setPurchaseDisPer(DmServer.vNull(rsTemp.getString("purchaseDiscountPer"), "0.00"));
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
    public DmResultPageing<List<KeyFieldExtended>> getTemplateFavorite(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl svm) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<List<KeyFieldExtended>>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        List<KeyFieldExtended> listNode = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended node;
        String sqlQuery = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            sqlQuery = "Select	vtm.docId, max(vtm.name) as name "
                    + "		from guiValueTemplateMast as vtm "
                    + "         left join patientPrescriptionMedAdv as ppma on vtm.docId = ppma.docId "
                    + "		where vtm.consultantCode = :consultantCode and ppma.docId is not null "
                    + "         group by vtm.docId ";
            sqlWithParam.setSQL(sqlQuery);
            sqlWithParam.setString("consultantCode", DmShared.iNull(svm.getConsultantCode()).toString());
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                node = new KeyFieldExtended();
                node.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                node.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.add(node);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

            helper.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(listNode);
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
    public DmResult<List<GrdMedicineDetail_Mdl>> getMedicineViaTemplate(String docId) {
        DmResult<List<GrdMedicineDetail_Mdl>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        List<GrdMedicineDetail_Mdl> listNode = new ArrayList<GrdMedicineDetail_Mdl>();
        dmrTemp.setData(listNode);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            /*====================================< Get Medicine Advise Data >====================================*/
            listNode = MedicineAdvise_Server.getMedicineDetail(DmShared.iNull(docId).toString(), cnTemp);

            cnTemp.close();
            dmrTemp.setData(listNode);
            dmrTemp.setStatus(true);
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

}
