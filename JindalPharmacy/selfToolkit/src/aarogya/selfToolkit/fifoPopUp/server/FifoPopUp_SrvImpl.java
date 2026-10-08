package aarogya.selfToolkit.fifoPopUp.server;

import aarogya.selfToolkit.fifoPopUp.shared.ClsVar;
import aarogya.selfToolkit.fifoPopUp.shared.FifoSetting_Mdl;
import aarogya.selfToolkit.fifoPopUp.shared.FifoPopUp_Srv;
import aarogya.selfToolkit.itemBatch.server.ItemBatch_Server;
import aarogya.selfToolkit.stStatic.server.STServer;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmSQLWithParam;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.stock.server.Stock_Server;
import dataman.dmToolkit.stock.shared.LogFile_Mdl;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FifoPopUp_SrvImpl extends DmRSSPrivate implements FifoPopUp_Srv {

    String fullLogFilePath = "";

    @Override

    public DmResultPageing<List<KeyFieldExtended>> getTxtHItemName(String moduleType, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT   "
                    + "  im.Code,im.name,im.manualCode  "
                    + " FROM itemMast im "
                    + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1) ");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode(moduleType, "manualCode", "itemType", "code", cnTemp)));
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("Code")));
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

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHCat(String moduleType, DmHelper helper, boolean blnToEndOfFile) {
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
                    + "FROM itemCat ic"
                    + " WHERE (ic.filterItemType = :filterItemType or ic.isCommon=1)");
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode(moduleType, "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHGroup(String moduleType, DmHelper helper, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT ig.code, ig.name FROM itemgroup ig"
                    + "   WHERE (ig.filterItemType = :filterItemType or isCommon=1) ");;
            sqlWithParam.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode(moduleType, "manualCode", "itemType", "code", cnTemp)));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHDepartment(String moduleType, DmHelper helper, boolean blnToEndOfFile) {
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
                    + "	WHERE sgn.manualCode IN ('CM','" + moduleType + "') AND sg.isActive = 1 " + condition;
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModuleType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(ClsVar.moduleTypeInventory_Code);
            listNode.setSharedKeyFieldName(ClsVar.moduleTypeInventory_Name);
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode(ClsVar.moduleTypePharmacy_Code);
            listNode.setSharedKeyFieldName(ClsVar.moduleTypePharmacy_Name);
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
    public DmResult<String> startFifo(FifoSetting_Mdl mdl) {
        Connection cnTemp = null;
        DmResult<String> dmrTemp = new DmResult();
        dmrTemp.setData("");
        DmSavePoint spBegin = null;

        try {
            cnTemp = DmServer.getConnection(DmServer.getSsnDBName(this));
            spBegin = new DmSavePoint(cnTemp);

            runFIFO(mdl.getItemCode(), mdl.getDepartmentCode(), mdl.getSiteCode(), mdl.getCategoryCode(), mdl.getGroupCode(), mdl.getModuleType(), cnTemp);

            dmrTemp.setValue1(fullLogFilePath);

            dmrTemp.setStatus(true);

            spBegin.commit();
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
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                }
            } catch (Exception e) {
            }
        }

        return dmrTemp;
    }

    private void runFIFO(String itemCode, String departmentCode, String siteCode, String categoryCode, String groupCode, String moduleType, Connection cnTemp) throws Exception {

        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", condition = "";
        LogFile_Mdl logFile_Mdl = new LogFile_Mdl();
        try {
            if (DmShared.xNull(itemCode).trim().equalsIgnoreCase("")) {
                if ((!DmShared.xNull(categoryCode).trim().equalsIgnoreCase("")) || (!DmShared.xNull(groupCode).trim().equalsIgnoreCase(""))) {
                    if (!DmShared.xNull(categoryCode).trim().equalsIgnoreCase("")) {
                        condition += " and  im.itemCategoryCode in (" + DmServer.getParamList(":catCode", categoryCode) + " )";
                    }
                    if (!DmShared.xNull(groupCode).trim().equalsIgnoreCase("")) {
                        condition += " and  im.itemGroup in (" + DmServer.getParamList(":grpCode", groupCode) + " )";
                    }
                    sqlQuery = " SELECT code as itemCode"
                            + " FROM itemMast im "
                            + " WHERE (im.filterItemType = :filterItemType or im.isCommon=1) " + condition;

                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.setString("filterItemType", DmShared.xNull(DmServer.getCodeViaManualCode(moduleType, "manualCode", "itemType", "code", cnTemp)));
                    if (!DmShared.xNull(categoryCode).trim().equalsIgnoreCase("")) {
                        psTemp.setStringList("catCode", DmShared.xNull(categoryCode));
                    }
                    if (!DmShared.xNull(groupCode).trim().equalsIgnoreCase("")) {
                        psTemp.setStringList("grpCode", DmShared.xNull(groupCode));
                    }
                    rsTemp = psTemp.executeQuery();
                    while (rsTemp.next()) {
                        itemCode += "," + DmShared.xNull(rsTemp.getString("itemCode"));
                    }
                    if (!DmShared.xNull(itemCode).equalsIgnoreCase("")) {
                        itemCode = itemCode.replaceFirst(",", "");
                    }
                }
            }

            if (DmShared.xNull(siteCode).trim().equalsIgnoreCase("")) {
                siteCode = DmServer.getSsnSiteCode(this);
            }

            List<String> strItemList = Arrays.asList(DmShared.xNull(itemCode).split(","));
            List<String> strDepartmentList = Arrays.asList(DmShared.xNull(departmentCode).split(","));
            List<String> strSiteList = Arrays.asList(DmShared.xNull(siteCode).split(","));

            logFile_Mdl = Stock_Server.getFile(this);
            fullLogFilePath = DmShared.xNull(logFile_Mdl.getFilePath());
            for (String steCode : strSiteList) {
                if (!DmShared.xNull(departmentCode).trim().equalsIgnoreCase("")) {
                    for (String departCode : strDepartmentList) {
                        if (!DmShared.xNull(itemCode).trim().equalsIgnoreCase("")) {
                            for (String itmCode : strItemList) {
                                Stock_Server.runStockFIFO_Process(DmShared.xNull(moduleType), DmShared.xNull(itmCode), "", DmShared.xNull(departCode), DmShared.xNull(steCode), cnTemp, false, fullLogFilePath);
                            }
                        } else {
                            Stock_Server.runStockFIFO_Process(DmShared.xNull(moduleType), "", "", DmShared.xNull(departCode), DmShared.xNull(steCode), cnTemp, false, fullLogFilePath);
                        }
                    }
                } else {
                    if (!DmShared.xNull(itemCode).trim().equalsIgnoreCase("")) {
                        for (String itmCode : strItemList) {
                            Stock_Server.runStockFIFO_Process(DmShared.xNull(moduleType), DmShared.xNull(itmCode), "", "", DmShared.xNull(steCode), cnTemp, false, fullLogFilePath);
                        }
                    } else {
                        Stock_Server.runStockFIFO_Process(DmShared.xNull(moduleType), "", "", "", DmShared.xNull(steCode), cnTemp, false, fullLogFilePath);
                    }
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
    }

    @Override
    public DmResult<KeyFieldExtended> readLogFile(String filePath) {
        DmResult<KeyFieldExtended> dmrTemp = new DmResult<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        ResultSet rsTemp = null;
        String log = "";

        try {

            File fr = new File(filePath);
            if (fr.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(filePath));
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                log = sb.toString();
                br.close();

            }
            KeyFieldExtended field = new KeyFieldExtended();
            field.setSharedKeyFieldName(log);
            dmrTemp.setData(field);
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

}
