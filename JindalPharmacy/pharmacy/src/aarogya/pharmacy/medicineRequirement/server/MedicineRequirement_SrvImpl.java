package aarogya.pharmacy.medicineRequirement.server;

import aarogya.pharmacy.medicineRequirement.shared.GrdMedicine_Mdl;
import aarogya.pharmacy.medicineRequirement.shared.MedicineRequirement_Srv;
import aarogya.pharmacy.medicineRequirement.shared.SearchValue_Mdl;
import aarogya.pharmacy.medicineRequirement.shared.TxtHMedicine_Mdl;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineRequirement_SrvImpl extends DmRSSPrivate implements MedicineRequirement_Srv {

    @Override
    public DmResultPageing<List<GrdMedicine_Mdl>> getMedicines(SearchValue_Mdl svm, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdMedicine_Mdl> lstTemp = new ArrayList<GrdMedicine_Mdl>();
        GrdMedicine_Mdl lstNode;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String mainCondition = "", stockCondition = "", movementCondition = "", deptInfoCodition = "", groupName = "", groupCode = "", groupOn = "";
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

            mainCondition += " AND im.isActive = 1 ";
            mainCondition += " AND (im.filterItemType = :filterItemType OR im.isCommon = 1)";
            mainCondition += " AND ISNULL(vIdi.maxStock,0) > 0 ";
            mainCondition += " AND ISNULL(vStk.balanceQty,0) <= ISNULL(vIdi.reOrder,0) ";

            if (!DmShared.xNull(svm.getAsOnDate()).trim().equals("")) {
                stockCondition += " AND  sk.v_Date <= :asOnDate ";
            }
            if (!DmShared.xNull(svm.getMovementFrom()).trim().equals("")) {
//                if (!DmShared.xNull(svm.getAsOnDate()).trim().equals("")) {
//                    movementCondition += " AND  sk.v_Date <= :asOnDate ";
//                }
                movementCondition += " AND  sk.v_Date >= :movementFrom ";
                mainCondition += " AND vMi.itemCode IS NOT NULL ";
            }
            if (!DmShared.xNull(svm.getDepartmentCode()).trim().equals("")) {
                mainCondition += " AND  vIdi.departmentCode IN (" + DmServer.getParamList(":department", svm.getDepartmentCode()) + " ) ";
            } else {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equals("")) {
                    mainCondition += " AND  vIdi.departmentCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + " ) ";
                }
            }
            if (!DmShared.xNull(svm.getCompanyCode()).trim().equals("")) {
                mainCondition += " AND im.itemmakeCode IN (" + DmServer.getParamList(":medMake", svm.getCompanyCode()) + " )";
            }
            if (!DmShared.xNull(svm.getGroupCode()).trim().equals("")) {
                mainCondition += " AND im.itemGroup IN (" + DmServer.getParamList(":medGroup", svm.getGroupCode()) + " )";
            }
            if (!DmShared.xNull(svm.getCategoryCode()).trim().equals("")) {
                mainCondition += " AND im.itemCategoryCode IN (" + DmServer.getParamList(":medCat", svm.getCategoryCode()) + " )";
            }
            if (!DmShared.xNull(svm.getMedicineCode()).trim().equals("")) {
                mainCondition += " AND im.code IN (" + DmServer.getParamList(":medicineCode", svm.getMedicineCode()) + " )";
            }
            if (!DmShared.xNull(svm.getShowHold()).trim().equals("")) {
                deptInfoCodition += " AND ISNULL(idi.isHoldRequirement, 0) = :isHoldRequirement";
            }

            groupOn = "'" + DmShared.xNull(svm.getGroupOnCode()) + "'";
            if (DmShared.xNull(svm.getGroupOnCode()).trim().equalsIgnoreCase("Category")) {
                groupCode = " im.itemCategoryCode ";
                groupName = " icm.name ";
            } else if (DmShared.xNull(svm.getGroupOnCode()).trim().equalsIgnoreCase("Company")) {
                groupCode = " im.itemMakeCode ";
                groupName = " imm.companyName ";
            } else if (DmShared.xNull(svm.getGroupOnCode()).trim().equalsIgnoreCase("Group")) {
                groupCode = " im.itemGroup ";
                groupName = " ig.name ";
            } else {
                groupCode = " '' ";
                groupName = " '' ";
                groupOn = " '' ";
            }

            sqlWithParam.setSQL(getMedicineRequirementSQL(deptInfoCodition, stockCondition, movementCondition, mainCondition, groupCode, groupName, groupOn));
            sqlWithParam.setString("filterItemType", DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp));
            sqlWithParam.setString("site_Code", DmServer.getSsnSiteCode(this));

//            if (!DmShared.xNull(svm.getAsOnDate()).trim().equals("")) {
//                sqlWithParam.setDate("asOnDate", DmServer.convertDate(svm.getAsOnDate()));
//            }
            if (!DmShared.dNull(svm.getMovementFrom()).trim().equals("")) {
                sqlWithParam.setDate("movementFrom", DmServer.convertDate(svm.getMovementFrom()));
            }
            if (!DmShared.xNull(svm.getDepartmentCode()).trim().equals("")) {
                sqlWithParam.setStringList("department", DmShared.xNull(svm.getDepartmentCode()));
            } else {
                if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equals("")) {
                    sqlWithParam.setStringList("department", DmShared.xNull(STServer.getSsnDepartmentCodeList(this)));
                }
            }
            if (!DmShared.xNull(svm.getCompanyCode()).trim().equals("")) {
                sqlWithParam.setStringList("medMake", DmShared.xNull(svm.getCompanyCode()));
            }
            if (!DmShared.xNull(svm.getCategoryCode()).trim().equals("")) {
                sqlWithParam.setStringList("medCat", DmShared.xNull(svm.getCategoryCode()));
            }
            if (!DmShared.xNull(svm.getGroupCode()).trim().equals("")) {
                sqlWithParam.setStringList("medGroup", DmShared.xNull(svm.getGroupCode()));
            }
            if (!DmShared.xNull(svm.getMedicineCode()).trim().equals("")) {
                sqlWithParam.setStringList("medicineCode", DmShared.xNull(svm.getMedicineCode()));
            }
            if (!DmShared.xNull(svm.getShowHold()).trim().equals("")) {
                sqlWithParam.setString("isHoldRequirement", svm.getShowHold());
            }

            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lstNode = new GrdMedicine_Mdl();
                lstNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("medicineCode")));
                lstNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("medicineName")));
                lstNode.setDepartmentCode(DmShared.xNull(rsTemp.getString("departmentCode")));
                lstNode.setDepartmentName(DmShared.xNull(rsTemp.getString("departmentName")));
                lstNode.setSkuName(DmShared.xNull(rsTemp.getString("skuName")));
                lstNode.setMaxLevel(DmServer.vNull(rsTemp.getString("maxLevel")));
                lstNode.setMinLevel(DmServer.vNull(rsTemp.getString("minLevel")));
                lstNode.setReorderLevel(DmServer.vNull(rsTemp.getString("reorderLevel")));
                lstNode.setStockQty(DmServer.vNull(rsTemp.getString("stockQty")));
                lstNode.setRequiredQty(DmServer.vNull(rsTemp.getString("requiredQty")));
                lstNode.setGroupName(DmShared.xNull(rsTemp.getString("groupbyName")));
                lstNode.setGroupCode(DmShared.xNull(rsTemp.getString("groupByCode")));
                lstNode.setIsChecked("0");
                lstTemp.add(lstNode);
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

    public static String getMedicineRequirementSQL(String deptInfoCodition, String stockCondition, String movementCondition, String mainCondition, String groupCode, String groupName, String groupOn) {
        String sqlQuery = "SELECT im.code AS medicineCode"
                + " , im.name AS medicineName"
                + " , im.displayName AS itemDisplayName"
                + " , " + groupCode + " as groupByCode "
                + " , " + groupName + " as groupbyName "
                + " , " + groupOn + " as groupOn "
                + " , im.itemPacking"
                + " , sgd.name as departmentName"
                + " , vIdi.departmentCode "
                + " , vIdi.maxStock as maxLevel "
                + " , vIdi.minStock as minLevel "
                + " , vIdi.reOrder as reorderLevel "
                + " , ig.name AS itemGroupName "
                + " , icm.name AS itemCategoryName "
                + " , imm.companyName"
                + " , vStk.balanceQty as stockQty "
                + " , ISNULL(vIdi.maxStock,0) - ISNULL(vStk.balanceQty,0) AS requiredQty"
                + " , um.unit as skuName "
                + " FROM itemMast as im  "
                + " LEFT JOIN ("
                + "		SELECT   idi.departmentCode"
                + "                , idi.itemCode"
                + "                , idi.reOrder"
                + "                , idi.maxStock"
                + "                , idi.minStock"
                + "		FROM itemDepartmentInfo idi"
                + "		WHERE idi.site_Code = :site_Code " + deptInfoCodition
                + "		) AS vIdi ON vIdi.itemCode = im.code "
                + " LEFT JOIN ( "
                //                + "             SELECT sk.itemCode,  sk.departmentCode"
                //                + "     	, isNull(sum(sk.recQty),0) - isNull(sum(sk.isuQty),0) balanceQty"
                //                + "     	FROM stock as sk 		"
                //                + "     	WHERE sk.site_Code = :site_Code " + stockCondition
                //                + "     	GROUP BY sk.itemCode, sk.departmentCode "
                + "      SELECT ibs.itemCode, ibs.departmentCode, sum(isnull(ibs.balanceQty,0)) balanceQty"
                + "     	FROM itemBatchStock ibs "
                + "     	WHERE ibs.site_Code = :site_Code " //+ stockCondition
                + "     	GROUP BY ibs.itemCode, ibs.departmentCode "
                + "             ) as vStk ON vIdi.itemCode = vStk.itemCode AND vIdi.departmentCode = vStk.departmentCode ";
        if (!DmShared.xNull(movementCondition).trim().equals("")) {
            sqlQuery += " LEFT JOIN ( "
                    + "         SELECT sk.itemCode,  sk.departmentCode"
                    + "     	FROM stock as sk 		"
                    + "     	WHERE sk.site_Code = :site_Code " + movementCondition
                    + "     	GROUP BY sk.itemCode, sk.departmentCode "
                    + "         ) as vMi ON vIdi.itemCode = vMi.itemCode AND vIdi.departmentCode = vMi.departmentCode ";
        }

        sqlQuery += " LEFT JOIN subGroup sgd ON sgd.subCode = vIdi.departmentCode "
                + " LEFT JOIN itemCat icm ON im.itemcategoryCode = icm.code"
                + " LEFT JOIN itemGroup ig ON im.itemGroup = ig.code "
                + " LEFT JOIN itemMakeMast imm ON im.itemmakeCode = imm.code"
                + " LEFT JOIN unitMast um ON im.sku = um.code"
                + " WHERE 1=1 " + mainCondition;

        return sqlQuery;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(DmHelper helper, boolean blnToEndOfFile) {
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

            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equalsIgnoreCase("")) {
                condition = " AND dm.subCode IN (" + DmServer.getParamList(":department", STServer.getSsnDepartmentCodeList(this)) + ")";
            }

            sqlWithParam.setSQL("SELECT	dm.subCode AS code, sg.name"
                    + "	FROM departmentMast dm"
                    + "	LEFT JOIN subGroup sg ON dm.subCode = sg.subCode"
                    + " LEFT JOIN subGroupNature sgn ON sgn.code = sg.nature"
                    + " WHERE sgn.manualCode IN ('CM','PH')"
                    + " AND sg.isActive = 1" + condition);
            if (!DmShared.xNull(STServer.getSsnDepartmentCodeList(this)).equals("")) {
                sqlWithParam.setStringList("department", DmShared.xNull(STServer.getSsnDepartmentCodeList(this)));
            }
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("Name")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCompany(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL(" SELECT imm.code, imm.companyName AS name"
                    + " FROM itemMakeMast imm"
                    + " WHERE imm.filterItemType = :filterItemType");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name"
                    + " FROM itemgroup ig"
                    + " WHERE (ig.filterItemType = :filterItemType OR ig.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCategory(DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ic.code, ic.name "
                    + " FROM itemcat ic "
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<TxtHMedicine_Mdl>> getTxtHMedicine(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<TxtHMedicine_Mdl>> rstTemp = new DmResultPageing<>();
        List<TxtHMedicine_Mdl> lstTemp = new ArrayList<>();
        TxtHMedicine_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("SELECT im.code, im.name, im.manualCode "
                    + " FROM itemMast im"
                    + " LEFT JOIN unitmast um ON im.sku = um.code "
                    + " WHERE (im.filterItemType = :filterItemType OR im.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setManualCode(DmShared.xNull(rsTemp.getString("manualCode")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHShowHold(DmHelper helper, boolean blnToEndOfFile) {
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
    public DmResult<String> saveRecord(List<KeyFieldExtended> keyFields, String showHold) {
        DmResult<String> dmr = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery, condition = "", conditionInner = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            if (keyFields != null && !keyFields.isEmpty()) {
                for (KeyFieldExtended keyField : keyFields) {
                    if (!conditionInner.trim().equals("")) {
                        conditionInner += " OR ";
                    }
                    conditionInner += " (itemCode = '" + DmShared.iNull(keyField.getSharedKeyFieldCode()).toString() + "'";
                    conditionInner += " AND departmentCode = '" + DmShared.iNull(keyField.getSharedKeyFieldName()).toString() + "')";
                }

                if (!DmShared.xNull(conditionInner).trim().equalsIgnoreCase("")) {
                    condition += " AND (" + conditionInner + ")";
                } else {
                    condition += " AND 1 = 2";
                }

                sqlQuery = "UPDATE itemDepartmentInfo SET isHoldRequirement = :isHoldRequirement WHERE site_Code = :site_Code " + condition;
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("site_Code", DmServer.getSsnSiteCode(this));
                if (DmShared.xNull(showHold).trim().equals("1")) {
                    psTemp.setString("isHoldRequirement", "0");
                } else {
                    psTemp.setString("isHoldRequirement", "1");
                }
                psTemp.executeUpdate();
            }

            spBegin.commit();
            dmr.setData("");
            dmr.setStatus(true);

        } catch (Exception e) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception ex) {
            }
            dmr.setErrorMessage(e.getMessage());
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

        return dmr;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getGroupName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        try {

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Category");
            listNode.setSharedKeyFieldName("Category");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Company");
            listNode.setSharedKeyFieldName("Company");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("Group");
            listNode.setSharedKeyFieldName("Group");
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
}
