package aarogya.pharmacy.materialCategoryMast.server;

import dataman.dmBase.self.shared.*;
import aarogya.pharmacy.materialCategoryMast.shared.*;
import aarogya.projectMenu.self.shared.pmInventory;
//import aarogya.selfToolkit.stImportData.server.MedicineCategoryMaster;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MaterialCategoryMast_SrvImpl extends DmRSSPrivate implements MaterialCategoryMast_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator(String module) {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);
//        String filterItemType;
        String condition="";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
//            if (DmShared.xNull(module).trim().equalsIgnoreCase(new pmInventory().moduleName)) {
//                filterItemType = DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp);
//            } else {
//                filterItemType = DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp);
//            }
            if (DmShared.xNull(module).trim().equalsIgnoreCase(new pmInventory().moduleName)) {
                condition+=" WHERE ic.isCommon in (0,2) ";
            } else {
                condition+=" WHERE ic.isCommon in (1,2) ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "SELECT CODE FROM materialCatMast "+condition+" ORDER BY NAME ");
//            psTemp.setString("filterItemType", DmShared.xNull(filterItemType));
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
    public DmResultPageing<List<String>> getSuggestionCategory(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();

        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlWithParam.setSQL("Select Name +'('+case isCommon when 0 then 'Inventory' when 1 then 'Pharmacy' else 'Both' end+')' as name From materialCatMast");
            helper.addSort("Name", DmEnum.DmSortType.ASC);
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
    public DmResult<MaterialCategoryMast_Mdl> getMoveRec(String strKey, String module) {
        DmResult<MaterialCategoryMast_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery,condition="";
        MaterialCategoryMast_Mdl mrTemp = new MaterialCategoryMast_Mdl();
        dmrTemp.setData(mrTemp);
//        String filterItemType = "";
        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));

//            if (DmShared.xNull(module).trim().equalsIgnoreCase(new pmInventory().moduleName)) {
//                filterItemType = DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp);
//            } else {
//                filterItemType = DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp);
//            }

        if (DmShared.xNull(module).trim().equalsIgnoreCase(new pmInventory().moduleName)) {
                condition+=" and ic.isCommon in (0,2) ";
            } else {
                condition+=" and ic.isCommon in (1,2) ";
            }
        
            sqlQuery = "SELECT ic.code, ic.name, "
                    + " ic.preparedBy, ic.isCommon, ic.u_Name"
                    + " FROM materialCatMast as ic "
                    + " WHERE ic.code = :code  "+condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("code", strKey);
//            psTemp.setString("filterItemType", DmShared.xNull(filterItemType));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_name")));
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));

                if (DmShared.xNull(rsTemp.getString("isCommon")).equalsIgnoreCase("1")) {
                    mrTemp.setIsCommonTag("1");
                    mrTemp.setIsCommonText("Pharmacy");
                } else if (DmShared.xNull(rsTemp.getString("isCommon")).equalsIgnoreCase("2")) {
                    mrTemp.setIsCommonTag("2");
                    mrTemp.setIsCommonText("Both");
                }else {
                    mrTemp.setIsCommonTag("0");
                    mrTemp.setIsCommonText("Inventory");
                }
            }
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

    @Override
    public DmResult<String> deleteRecord(String strKey) {
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

            sqlQuery = "Delete From materialCatMast Where code=:code";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
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
    public DmResult<String> saveRecord(MaterialCategoryMast_Mdl mrTemp, String module) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String sqlQuery, filterItemType = "";
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            MaterialCategoryMast_Vld.chkSave(mrTemp);

            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "materialCatMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            if (DmShared.xNull(module).trim().equalsIgnoreCase(new pmInventory().moduleName)) {
                filterItemType = DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp);
            } else {
                filterItemType = DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp);
            }

            sqlQuery = "  SELECT "
                    + "   name "
                    + " , code "
                    + " , filterItemType "
                    + " FROM materialCatMast "
                    + " WHERE LOWER(name) = :name "
                    + " AND code <> :code ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()).toLowerCase());
            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (!DmShared.xNull(rsTemp.getString("name")).equalsIgnoreCase("")) {
                    if (DmShared.xNull(rsTemp.getString("filterItemType")).equalsIgnoreCase(filterItemType)) {
                        throw new UnsupportedOperationException("Material Category already exists.");
                    } else {
                        mrTemp.setSharedKeyFieldCode(DmShared.iNull(rsTemp.getString("code")).toString());
                        mrTemp.setIsCommonTag("1");
                        blnAdd = false;
                    }
                }
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                sqlQuery = "Insert Into materialCatMast(code ,name, filterItemType, "
                        + " preparedBy, u_EntDt,isCommon) Values  "
                        + "(:code, :name, :filterItemType, :preparedBy, :uEntDt,:isCommon) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("isCommon", DmShared.xNull(mrTemp.getIsCommonTag()));
                psTemp.setString("filterItemType", DmShared.foriegnKey(filterItemType));
                psTemp.setString("preparedBy", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();

            } else {
                sqlQuery = "Update materialCatMast Set "
                        + " name = :name, "
                        + " U_Name = :u_Name,"
                        + " filterItemType = :filterItemType, "
                        + " isCommon = :isCommon, "
                        + " U_EntDt = :uEntDt "
                        + " Where code = :code ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("isCommon", DmShared.xNull(mrTemp.getIsCommonTag()));
                psTemp.setString("filterItemType", DmShared.foriegnKey(filterItemType));
                psTemp.setString("u_Name", DmServer.getSsnUserName(this));
                psTemp.setTimestamp("uEntDt", DmServer.getServerTimestamp());
                psTemp.executeUpdate();
            }

//            sqlQuery = "UPDATE itemMast "
//                    + " SET itemMast.name = vItem.itemName "
//                    + " FROM ( "
//                    + " 	SELECT im.code as itemCode "
//                    + " 	, im.displayName + '-' + ic.name + '-' + comp.shortName + ' (Packing: ' + ISNULL(im.itemPacking,'') + ')' AS itemName "
//                    + " 	FROM materialCatMast ic  "
//                    + " 	INNER JOIN itemMast im ON im.itemCategoryCode = ic.code "
//                    + "         INNER JOIN itemmakemast AS comp ON comp.code = im.itemMakeCode  "
//                    + " 	WHERE im.itemCategoryCode = :code "
//                    + " 	AND im.filterItemType = :filterItemType "
//                    + " 	) vItem "
//                    + " WHERE itemMast.code = vItem.itemCode ";
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psTemp.setString("code", DmShared.xNull(mrTemp.getSharedKeyFieldCode()));
//            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
//            psTemp.executeUpdate();
//            psTemp.close();
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
    public DmResultPageing<List<Find_Mdl>> getFind(String module, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<Find_Mdl>> rstTemp = new DmResultPageing<>();
        List<Find_Mdl> lstTemp = new ArrayList<>();
        Find_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String filterItemType;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        String condition="";
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            if (DmShared.xNull(module).trim().equalsIgnoreCase(new pmInventory().moduleName)) {
//                filterItemType = DmServer.getCodeViaManualCode("SM", "manualCode", "itemType", "code", cnTemp);
                condition+=" WHERE ic.isCommon in (0,2) ";
            } else {
//                filterItemType = DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp);
                condition+=" WHERE ic.isCommon in (1,2) ";
            }
            sqlWithParam.setSQL("SELECT ic.code, ic.name, ic.isCommon"
                    + " FROM materialCatMast as  ic  "
                    + condition );
//            sqlWithParam.setString("filterItemType", DmShared.xNull(filterItemType));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                if (DmShared.iNull(rsTemp.getString("isCommon")).intValue() == 0) {
                    listNode.setIsCommon("Inventory");
                }else if (DmShared.iNull(rsTemp.getString("isCommon")).intValue() == 1) {
                    listNode.setIsCommon("Pharmacy");
                }else {
                    listNode.setIsCommon("Both");
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

            sqlQuery = "Select code, name, hsn from hsnMast ";
            sqlWithParam.setSQL(sqlQuery);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new TxtHHSN_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setHsn(DmShared.xNull(rsTemp.getString("hsn")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHyesNo(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("Inventory");
            lstTemp.add(listNode);
            
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Pharmacy");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("2");
            listNode.setSharedKeyFieldName("Both");
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
    public DmResult<KeyFieldExtended> exportCSV(String sessionId) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        KeyFieldExtended keyField = new KeyFieldExtended();
        String sqlQuery, fileName = "", commaDelimiter = ",", lineData = "";
//        DmHandelFile handleFile = null;
//        char separator = File.separatorChar;
//        String csvHeader = ImportData_Server.getCSVFormats(new MaterialCategoryMast().getImportDataModel().getHmCSV()).get("header");
        dmrTemp.setData(keyField);
        try {
            fileName = "Medicine Category Master";
//            if (!DmSession.getSessions().containsKey(sessionId)) {
//                throw new DmException(0, "Invalid request.");
//            }

            if (DmShared.xNull(fileName).equalsIgnoreCase("")) {
                throw new DmException(0, "File Name not given.");
            }
//            String directoryPath = DmServer.Path.sessions + separator + sessionId;
            String serverFileName = fileName + ".csv";
//            String fullFilePath = directoryPath + separator + serverFileName;
//            new File(directoryPath).mkdir();
//            handleFile = new DmHandelFile(fullFilePath, true);

//            keyField.setSharedKeyFieldCode(directoryPath);
            keyField.setSharedKeyFieldName(serverFileName);

//            handleFile.writeLine(csvHeader);
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            sqlQuery = "SELECT ic.name  , "
                    + "  case ic.allowBifurcation when 1 then 'Yes' else 'No' end as allowBifurcation , "
                    + "  case ic.isCommon when 1 then 'Yes' else 'No' end as isCommon"
                    + " FROM materialCatMast as ic"
                    + " WHERE ic.filterItemType = :filterItemType  "
                    + " order by ic.name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode("PH", "manualCode", "itemType", "code", cnTemp)));
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                lineData = "\"" + DmShared.xNull(rsTemp.getString("name")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("allowBifurcation")) + "\"";
                lineData += commaDelimiter;
                lineData += "\"" + DmShared.xNull(rsTemp.getString("isCommon")) + "\"";
//                handleFile.writeLine(lineData);
            }
            psTemp.close();
            rsTemp.close();
//            handleFile.close();
            dmrTemp.setData(keyField);
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
//                if (handleFile != null) {
//                    handleFile.close();
//                }
            } catch (Exception e) {
            }
        }
        return dmrTemp;
    }

}
