package aarogya.dashBoard.dataModificationLog.server;

import aarogya.dashBoard.dataModificationLog.client.FlatMapUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import dataman.dmBase.self.shared.*;
import aarogya.dashBoard.dataModificationLog.shared.*;
import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.modifyReason.server.ModifyReason_Server;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class DataModificationLog_SrvImpl extends DmRSSPrivate implements DataModificationLog_Srv {

    @Override
    public DmResult<DefaultValue_Mdl> getDefaultValue() {
        DmResultPageing<DefaultValue_Mdl> dmrTemp = new DmResultPageing();
        DefaultValue_Mdl mrTemp = new DefaultValue_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            mrTemp.setCurrentSiteCode(DmServer.getSsnSiteCode(this));
            mrTemp.setCurrentSiteName(DmServer.getSsnSiteSName(this));
            mrTemp.setFromDate(DmServer.getServerDateTime()[0]);
            mrTemp.setToDate(DmServer.getServerDateTime()[0]);

            dmrTemp.setStatus(true);
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }

        return dmrTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHSite(DmHelper pageing, boolean blnToEndOfFile) {
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
            sqlWithParam.setSQL("SELECT  sm.code , sm.name"
                    + " FROM siteMast sm"
                    + " where sm.code in(" + DmServer.getParamList(":code", DmServer.getSsnSiteList(this)) + ")");
            sqlWithParam.setStringList("code", DmServer.getSsnSiteList(this));
            psTemp = DmServer.evaluateHelper(pageing, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            pageing.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(pageing.getPaging());
            rstTemp.setStatus(true);
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            cnTemp = null;
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
                    cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<GrdDataModificationLog_Mdl>> getFillRecords(SearchValue_Mdl searchValueMdl, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<GrdDataModificationLog_Mdl>> rstTemp = new DmResultPageing<>();
        List<GrdDataModificationLog_Mdl> lstTemp = new ArrayList<GrdDataModificationLog_Mdl>();
        GrdDataModificationLog_Mdl listNode;
        DBCursor dBCursor = null;

        BasicDBObject condition = new BasicDBObject();

        List<String> objSite = new ArrayList<String>();
        BasicDBObject inQuerySite = new BasicDBObject();

        List<String> objUserName = new ArrayList<String>();
        BasicDBObject inQueryUserName = new BasicDBObject();

        List<String> objFy = new ArrayList<String>();
        BasicDBObject inQueryFy = new BasicDBObject();

        List<String> objMode = new ArrayList<String>();
        BasicDBObject inQueryMode = new BasicDBObject();

        List<String> objModule = new ArrayList<String>();
        BasicDBObject inQueryModule = new BasicDBObject();

        List<String> objMenu = new ArrayList<String>();
        BasicDBObject inQueryMenu = new BasicDBObject();

        List<String> objRecord_1 = new ArrayList<String>();
        BasicDBObject inQueryRecord_1 = new BasicDBObject();

        List<String> objRecord_2 = new ArrayList<String>();
        BasicDBObject inQueryRecord_2 = new BasicDBObject();

        List<String> objVrShortName = new ArrayList<String>();
        BasicDBObject inQueryVrShortName = new BasicDBObject();

        DBObject dBObject = null;

        try {

            DataModificationLog_Vld.validateInput(searchValueMdl);

            SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat logDateFormat1 = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            String logdate = "";

            Date fromDate = DmShared.convertDate(searchValueMdl.getFromDate() == null ? DmServer.getServerDateTime()[0] : searchValueMdl.getFromDate());
            fromDate.setHours(0);
            fromDate.setMinutes(0);
            fromDate.setSeconds(0);
            Date toDate = DmShared.convertDate(searchValueMdl.getToDate() == null ? DmServer.getServerDateTime()[0] : searchValueMdl.getToDate());
            toDate.setHours(23);
            toDate.setMinutes(59);
            toDate.setSeconds(59);
            condition.put("logdate", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());

            if (!DmShared.xNull(searchValueMdl.getUserNameCode()).equalsIgnoreCase("")) {
                String[] userStr = searchValueMdl.getUserName().trim().split(",");
                for (int i = 0; i < userStr.length; i++) {
                    objUserName.add(DmShared.xNull(userStr[i]).trim());
                }

                inQueryUserName.put("$in", objUserName);
                condition.put("username", inQueryUserName);
            }

            if (!DmShared.xNull(searchValueMdl.getFinancialyearCode()).equalsIgnoreCase("")) {
                String[] fyStr = searchValueMdl.getFinancialyear().trim().split(",");
                for (int i = 0; i < fyStr.length; i++) {
                    objFy.add(DmShared.xNull(fyStr[i]).trim());
                }

                inQueryFy.put("$in", objFy);
                condition.put("financialyear", inQueryFy);
            }
            if (!DmShared.xNull(searchValueMdl.getModeCode()).equalsIgnoreCase("")) {
                String[] modeStr = searchValueMdl.getMode().trim().split(",");
                for (int i = 0; i < modeStr.length; i++) {
                    objMode.add(DmShared.xNull(modeStr[i]).trim());
                }

                inQueryMode.put("$in", objMode);
                condition.put("mode", inQueryMode);
            }
            if (!DmShared.xNull(searchValueMdl.getVrTypeShortNameCode()).equalsIgnoreCase("")) {
                String[] vrShortStr = searchValueMdl.getVrTypeShortName().trim().split(",");
                for (int i = 0; i < vrShortStr.length; i++) {
                    objVrShortName.add(DmShared.xNull(vrShortStr[i]).trim());
                }

                inQueryVrShortName.put("$in", objVrShortName);
                condition.put("v_type_s_name", inQueryVrShortName);
            }

            if (!DmShared.xNull(searchValueMdl.getModuleCode()).equalsIgnoreCase("")) {
                String[] moduleStr = searchValueMdl.getModuleCode().split(",");
                for (int i = 0; i < moduleStr.length; i++) {
                    objModule.add(DmShared.xNull(moduleStr[i]).trim());
                }

                inQueryModule.put("$in", objModule);
                condition.put("modulename", inQueryModule);
            }

            if (!DmShared.xNull(searchValueMdl.getMenuCode()).equalsIgnoreCase("")) {
                String[] menuStr = searchValueMdl.getMenu().split(",");
                for (int i = 0; i < menuStr.length; i++) {
                    objMenu.add(DmShared.xNull(menuStr[i]).trim());
                }

                inQueryMenu.put("$in", objMenu);
                condition.put("menuname", inQueryMenu);
            }

            if (!DmShared.xNull(searchValueMdl.getRecordName1()).equalsIgnoreCase("")) {
                objRecord_1.add(DmShared.xNull(searchValueMdl.getRecordName1()).trim());
                inQueryRecord_1.put("$in", objRecord_1);
                condition.put("keyname_1", inQueryRecord_1);
            }

            if (!DmShared.xNull(searchValueMdl.getRecordName2()).equalsIgnoreCase("")) {
                objRecord_2.add(DmShared.xNull(searchValueMdl.getRecordName2()).trim());
                inQueryRecord_2.put("$in", objRecord_2);
                condition.put("keyname_2", inQueryRecord_2);
            }

            if (!DmShared.xNull(searchValueMdl.getSiteNameList()).equalsIgnoreCase("")) {
                String[] siteStr = searchValueMdl.getSiteNameList().split(",");
                for (int i = 0; i < siteStr.length; i++) {
                    objSite.add(DmShared.xNull(siteStr[i]).trim());
                }

                inQuerySite.put("$in", objSite);
                condition.put("sitemcode", inQuerySite);
            }

            Date parseLogDate;

            DBCollection dBCollection = DmServer.getMdbCollection(this, ModifyReason_Server.MDB_COLLECTION_NAME_NEW, true);

            dBCursor = DmServer.evaluateHelperDB(helper, dBCollection, condition, blnToEndOfFile, this);
            while (dBCursor.hasNext()) {
                dBObject = dBCursor.next();

                listNode = new GrdDataModificationLog_Mdl();
                parseLogDate = logDateFormat.parse(logDateFormat.format(dBObject.get("logdate")));
                logdate = logDateFormat1.format(parseLogDate);

                listNode.setLogDateTime(DmShared.xNull(logdate));
                listNode.setUserName(String.valueOf(dBObject.get("username")));
                listNode.setModule(String.valueOf(dBObject.get("modulename")));
                listNode.setMenu(String.valueOf(dBObject.get("menuname")));
                listNode.setRecordName1(String.valueOf(dBObject.get("keyname_1")));
                listNode.setRecordDesc1(String.valueOf(dBObject.get("keydescription_1")));
                listNode.setRecordName2(String.valueOf(dBObject.get("keyname_2")));
                listNode.setRecordDesc2(String.valueOf(dBObject.get("keydescription_2")));
                listNode.setVrTypeShortName(String.valueOf(dBObject.get("v_type_s_name")));
                listNode.setMode(String.valueOf(dBObject.get("mode")));
                listNode.setModifyReason(String.valueOf(dBObject.get("modifyreason")));
                listNode.setRemark(String.valueOf(dBObject.get("remark")));
                listNode.setShortSiteName(String.valueOf(dBObject.get("sitemcode")));
                listNode.setFinancialyear(String.valueOf(dBObject.get("financialyear")));

                listNode.setOldData(String.valueOf(dBObject.get("olddata")));
                listNode.setNewData(String.valueOf(dBObject.get("newdata")));

                lstTemp.add(listNode);
            }
            dBCursor.close();
            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
            rstTemp.setData(lstTemp);

        } catch (Exception e) {
            e.printStackTrace();
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (dBCursor != null) {
                    dBCursor.close();
                }

            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHUserName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select user_Name as code, user_Name as name, description "
                    + "                 from userMast "
                    + "                 Where coalesce(user_Role,'') <>'' and lower(user_Name) not in ('sa','super') and isActive = 1 ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("description")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            cnTemp = null;

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
                    cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMode(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> listNode = new ArrayList<>();
        KeyFieldExtended node;

        try {

            node = new KeyFieldExtended();
            node.setSharedKeyFieldCode("0");
            node.setSharedKeyFieldName("Edit");
            listNode.add(node);

            node = new KeyFieldExtended();
            node.setSharedKeyFieldCode("1");
            node.setSharedKeyFieldName("Delete");
            listNode.add(node);

            listNode = DmShared.filterKeyFieldList(helper, listNode);
            rstTemp.setData(listNode);

            helper.positioning(listNode.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> getTxtData(String newData, String oldData) {
        DmResult<String> rstTemp = new DmResult<>();

        try {

            Gson g = new Gson();
            Type mapType = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> firstMap = g.fromJson(newData, mapType);
            Map<String, Object> secondMap = g.fromJson(oldData, mapType);

            if (firstMap != null) {
                String differences = Maps.difference(firstMap, secondMap).toString();
                MapDifference<String, Object> diff = Maps.difference(firstMap, secondMap);

                Map<String, Object> leftFlatMap = FlatMapUtil.flatten(firstMap);
                Map<String, Object> rightFlatMap = FlatMapUtil.flatten(secondMap);

                MapDifference<String, Object> difference = Maps.difference(leftFlatMap, rightFlatMap);

                rstTemp.setData((difference.entriesDiffering().toString()).replaceAll("\\),", "\\),\n"));

                System.out.println("\n\nEntries differing\n--------------------------");
                difference.entriesDiffering().forEach((key, value) -> System.out.println(key + ": " + value));

            } else {
                rstTemp.setData(formatJSON(oldData));
            }
            rstTemp.setStatus(true);

        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        }
        return rstTemp;
    }

    public String formatJSON(String input) {
        int tabCount = 0;

        StringBuilder inputBuilder = new StringBuilder();
        char[] inputChar = input.toCharArray();

        for (int i = 0; i < inputChar.length; i++) {
            String charI = String.valueOf(inputChar[i]);
            if (charI.equals("}") || charI.equals("]")) {
                tabCount--;
                if (!String.valueOf(inputChar[i - 1]).equals("[") && !String.valueOf(inputChar[i - 1]).equals("{")) {
                    inputBuilder.append(newLine(tabCount));
                }
            }
            inputBuilder.append(charI);

            if (charI.equals("{") || charI.equals("[")) {
                tabCount++;
                if (String.valueOf(inputChar[i + 1]).equals("]") || String.valueOf(inputChar[i + 1]).equals("}")) {
                    continue;
                }

                inputBuilder.append(newLine(tabCount));
            }

            if (charI.equals(",")) {
                inputBuilder.append(newLine(tabCount));
            }
        }

        return inputBuilder.toString();
    }

    private String newLine(int tabCount) {
        StringBuilder builder = new StringBuilder();

        builder.append("\n");
        for (int j = 0; j < tabCount; j++) {
            builder.append("  ");
        }

        return builder.toString();
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHFy(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select comp_Code as code,cyear as name from company ");
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
            cnTemp = null;

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
                    cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHVoucherType(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("Select v_type as code, v_type, description, short_name from voucher_type ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("short_name")));
                listNode.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("description")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            cnTemp = null;

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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
                    cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModules(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("select distinct mnumodule as name from user_perdashBoardsion_web ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("name")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            cnTemp = null;

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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
                    cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMenus(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        rstTemp.setData(lstTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL("Select CONCAT(MnuText, 'þ' , MnuName, 'þ' ,MnuModule )As Code, MnuText as name , MnuName "
                    + "                 From User_PerdashBoardsion_Web "
                    + "			Where lower(UserName)=lower('SA')");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("MnuName")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                lstTemp.add(listNode);
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
            cnTemp = null;

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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
                    cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResultPageing<List<KeyFieldExtended>> getTxtHModifyReason(DmHelper helper, boolean blnToEndOfFile) {
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

            sqlWithParam.setSQL("Select code, name from modifyReasonMast ");
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
            cnTemp = null;

            helper.positioning(lstTemp.size(), blnToEndOfFile);
            rstTemp.setPaging(helper.getPaging());
            rstTemp.setStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
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
                    cnTemp = null;
                }
            } catch (Exception e) {
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<String> getRecordPrint(String widgetName, SearchValue_Mdl searchValueMdl, DmReportProperty_Mdl reportProperty) {
        DmResult<String> rstTemp = new DmResult<>();
        KeyFieldExtended keyField = new KeyFieldExtended();
        List<GrdDataModificationLog_Mdl> lstTemp = new ArrayList<GrdDataModificationLog_Mdl>();
        GrdDataModificationLog_Mdl listNode;
        DBCursor dBCursor = null;
        String fileName, sessionId;
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        DBObject dBObject = null;
        BasicDBObject condition = new BasicDBObject();

        List<String> objSite = new ArrayList<String>();
        BasicDBObject inQuerySite = new BasicDBObject();

        List<String> objUserName = new ArrayList<String>();
        BasicDBObject inQueryUserName = new BasicDBObject();

        List<String> objFy = new ArrayList<String>();
        BasicDBObject inQueryFy = new BasicDBObject();

        List<String> objMode = new ArrayList<String>();
        BasicDBObject inQueryMode = new BasicDBObject();

        List<String> objModule = new ArrayList<String>();
        BasicDBObject inQueryModule = new BasicDBObject();

        List<String> objMenu = new ArrayList<String>();
        BasicDBObject inQueryMenu = new BasicDBObject();

        List<String> objRecord_1 = new ArrayList<String>();
        BasicDBObject inQueryRecord_1 = new BasicDBObject();

        List<String> objRecord_2 = new ArrayList<String>();
        BasicDBObject inQueryRecord_2 = new BasicDBObject();

        List<String> objVrShortName = new ArrayList<String>();
        BasicDBObject inQueryVrShortName = new BasicDBObject();

        SimpleDateFormat logDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat logDateFormat1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat logDateFormat2 = new SimpleDateFormat("dd-MMM-yyyy");
        String logdate = "";
        String logdate1 = "";

        try {
            DataModificationLog_Vld.validateInput(searchValueMdl);
            sessionId = DmServer.getSsnId(this);

            DBCollection dBCollection = DmServer.getMdbCollection(this, ModifyReason_Server.MDB_COLLECTION_NAME_NEW, false);
            java.util.Date fromDate = DmShared.convertDate(searchValueMdl.getFromDate());
            fromDate.setHours(0);
            fromDate.setMinutes(0);
            fromDate.setSeconds(0);
            java.util.Date toDate = DmShared.convertDate(searchValueMdl.getToDate());
            toDate.setHours(23);
            toDate.setMinutes(59);
            toDate.setSeconds(59);
            condition.put("logdate", BasicDBObjectBuilder.start("$gte", fromDate).add("$lte", toDate).get());

            if (!DmShared.xNull(searchValueMdl.getUserNameCode()).equalsIgnoreCase("")) {
                String[] userStr = searchValueMdl.getUserName().trim().split(",");
                for (int i = 0; i < userStr.length; i++) {
                    objUserName.add(DmShared.xNull(userStr[i]).trim());
                }

                inQueryUserName.put("$in", objUserName);
                condition.put("username", inQueryUserName);
            }

            if (!DmShared.xNull(searchValueMdl.getFinancialyearCode()).equalsIgnoreCase("")) {
                String[] fyStr = searchValueMdl.getFinancialyear().trim().split(",");
                for (int i = 0; i < fyStr.length; i++) {
                    objFy.add(DmShared.xNull(fyStr[i]).trim());
                }

                inQueryFy.put("$in", objFy);
                condition.put("financialyear", inQueryFy);
            }
            if (!DmShared.xNull(searchValueMdl.getModeCode()).equalsIgnoreCase("")) {
                String[] modeStr = searchValueMdl.getMode().trim().split(",");
                for (int i = 0; i < modeStr.length; i++) {
                    objMode.add(DmShared.xNull(modeStr[i]).trim());
                }

                inQueryMode.put("$in", objMode);
                condition.put("mode", inQueryMode);
            }

            if (!DmShared.xNull(searchValueMdl.getVrTypeShortNameCode()).equalsIgnoreCase("")) {
                String[] vrShortStr = searchValueMdl.getVrTypeShortName().trim().split(",");
                for (int i = 0; i < vrShortStr.length; i++) {
                    objVrShortName.add(DmShared.xNull(vrShortStr[i]).trim());
                }

                inQueryVrShortName.put("$in", objVrShortName);
                condition.put("v_type_s_name", inQueryVrShortName);
            }

            if (!DmShared.xNull(searchValueMdl.getModuleCode()).equalsIgnoreCase("")) {
                String[] moduleStr = searchValueMdl.getModuleCode().split(",");
                for (int i = 0; i < moduleStr.length; i++) {
                    objModule.add(DmShared.xNull(moduleStr[i]).trim());
                }

                inQueryModule.put("$in", objModule);
                condition.put("modulename", inQueryModule);
            }

            if (!DmShared.xNull(searchValueMdl.getMenuCode()).equalsIgnoreCase("")) {
                String[] menuStr = searchValueMdl.getMenu().split(",");
                for (int i = 0; i < menuStr.length; i++) {
                    objMenu.add(DmShared.xNull(menuStr[i]).trim());
                }

                inQueryMenu.put("$in", objMenu);
                condition.put("menuname", inQueryMenu);
            }

            if (!DmShared.xNull(searchValueMdl.getRecordName1()).equalsIgnoreCase("")) {
                objRecord_1.add(DmShared.xNull(searchValueMdl.getRecordName1()).trim());
                inQueryRecord_1.put("$in", objRecord_1);
                condition.put("keyname_1", inQueryRecord_1);
            }

            if (!DmShared.xNull(searchValueMdl.getRecordName2()).equalsIgnoreCase("")) {
                objRecord_2.add(DmShared.xNull(searchValueMdl.getRecordName2()).trim());
                inQueryRecord_2.put("$in", objRecord_2);
                condition.put("keyname_2", inQueryRecord_2);
            }

            if (!DmShared.xNull(searchValueMdl.getSiteNameList()).equalsIgnoreCase("")) {
                String[] siteStr = searchValueMdl.getSiteNameList().split(",");
                for (int i = 0; i < siteStr.length; i++) {
                    objSite.add(DmShared.xNull(siteStr[i]).trim());
                }

                inQuerySite.put("$in", objSite);
                condition.put("sitemcode", inQuerySite);
            }

            Date parseLogDate;
            DBObject sortFields = new BasicDBObject("logdate", -1).append("modulename", 1).append("sessionId".toLowerCase(), 1);
            dBCursor = dBCollection.find(condition).sort(sortFields);
            while (dBCursor.hasNext()) {
                dBObject = dBCursor.next();
                listNode = new GrdDataModificationLog_Mdl();
                parseLogDate = logDateFormat.parse(logDateFormat.format(dBObject.get("logdate")));
                logdate = logDateFormat1.format(parseLogDate);
                logdate1 = logDateFormat2.format(parseLogDate);
                listNode.setLogDateTime(String.valueOf(logdate));
                listNode.setLogDate(String.valueOf(logdate1));
                listNode.setUserName(String.valueOf(dBObject.get("username")));
                listNode.setModule(String.valueOf(dBObject.get("modulename")));
                listNode.setMenu(String.valueOf(dBObject.get("menuname")));
                listNode.setRecordName1(String.valueOf(dBObject.get("keyname_1")));
                listNode.setRecordDesc1(String.valueOf(dBObject.get("keydescription_1")));
                listNode.setRecordName2(String.valueOf(dBObject.get("keyname_2")));
                listNode.setRecordDesc2(String.valueOf(dBObject.get("keydescription_2")));
                listNode.setVrTypeShortName(String.valueOf(dBObject.get("v_type_s_name")));
                listNode.setMode(String.valueOf(dBObject.get("mode")));
                listNode.setModifyReason(String.valueOf(dBObject.get("modifyreason")));
                listNode.setRemark(String.valueOf(dBObject.get("remark")));
                listNode.setShortSiteName(String.valueOf(dBObject.get("sitemcode")));
                listNode.setFinancialyear(String.valueOf(dBObject.get("financialyear")));

                listNode.setOldData(String.valueOf(dBObject.get("olddata")));
                listNode.setNewData(String.valueOf(dBObject.get("newdata")));

                lstTemp.add(listNode);
            }

            jasperReport = DmServer.compileJasper("dashBoard_DataModificationLog");

            HashMap hm = new HashMap();
            jasperPrint = DmServer.fillJasper(this, new pmDashBoard().DATA_MODIFICATION_LOG.caption, jasperReport, hm, null, lstTemp);
            rstTemp.setData(DmServer.showJasper(this, jasperPrint, "", reportProperty));
            rstTemp.setStatus(true);
        } catch (Exception e) {
            rstTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            if (dBCursor != null) {
                dBCursor.close();
            }
        }
        return rstTemp;
    }

    @Override
    public DmResult<KeyFieldExtended> getDownloadExcel(String widgetName, SearchValue_Mdl searchValueMdl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
