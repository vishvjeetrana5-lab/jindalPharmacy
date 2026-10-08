package dataman.webJobs.scheduler.server;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.webJobs.scheduler.shared.*;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.dmSession.server.DmRSSPrivate;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Scheduler_SrvImpl extends DmRSSPrivate implements Scheduler_Srv {

    @Override
    public DmResult<List<KeyFieldExtended>> getNavigator() {
        DmResult<List<KeyFieldExtended>> dmrTemp = new DmResult();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        List<KeyFieldExtended> lstNavigator = new ArrayList<>();
        KeyFieldExtended lstNavigatorTmp;
        dmrTemp.setData(lstNavigator);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            rsTemp = cnTemp.createStatement().executeQuery(" select docId from schedulerMast order by name ");

            while (rsTemp.next()) {
                lstNavigatorTmp = new KeyFieldExtended();
                lstNavigatorTmp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                lstNavigator.add(lstNavigatorTmp);
            }
            dmrTemp.setStatus(true);
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            dmrTemp.setErrorMessage(DmShared.xNull(e.getMessage()));
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
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
    public DmResultPageing<List<String>> getSuggestionName(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<String>> rstTemp = new DmResultPageing<>();
        List<String> data = new ArrayList<>();
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(data);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("select name from schedulerMast ");
            helper.addSort("name", DmEnum.DmSortType.ASC);
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                data.add(DmShared.xNull(rsTemp.getString("name")));
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
    public DmResult<Scheduler_Mdl> getMoveRec(String strKey) {
        DmResult<Scheduler_Mdl> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSQL;
        Scheduler_Mdl mrTemp = new Scheduler_Mdl();
        dmrTemp.setData(mrTemp);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            strSQL = "   select	sm.docId, sm.name as schedulerName, sm.PLTCode, plt.comp_Name as pltName, "
                    + "		com.centralData_Path AS dbLookUp, sm.selSiteCodes as siteCode, "
                    + "		sm.jobType, sm.WSTCode as templateCode, sm.isActive, sm.preparedBy, sm.u_Name, "
                    + "		FORMAT(sm.startdatetime,'dd-MMM-yyyy') stDate, FORMAT(sm.startdatetime,'HH.mm') AS stTime, "
                    + "		FORMAT(sm.expDateTime,'dd-MMM-yyyy') AS exDate, FORMAT(sm.expDateTime,'HH.mm') AS exTime, "
                    + "		sm.schType as taskScheduler, sm.recurQuantity, sm.recurUnit, sm.selDays as wmDays, "
                    + "		sm.dayOrMonth, sm.selMonths as months, sm.selDates as dates, sm.selWeeks as weeks, sm.responsibleUser "
                    + " from schedulerMast as sm "
                    + " left join productLicensedTo as plt on sm.PLTCode = plt.pltCode "
                    + "left join company  AS com ON plt.latestCompCode = com.comp_Code "
                    + "WHERE sm.docid = :docid";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                mrTemp.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                mrTemp.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("schedulerName")));
                mrTemp.setPltCode(DmShared.xNull(rsTemp.getString("PLTCode")));
                mrTemp.setPltName(DmShared.xNull(rsTemp.getString("pltName")));
                mrTemp.setDbLookUp(DmShared.xNull(rsTemp.getString("dbLookUp")));
                mrTemp.setSiteCode(DmShared.xNull(rsTemp.getString("siteCode")).replaceAll("\\|", ","));
                mrTemp.setJobTypeCode(DmShared.xNull(rsTemp.getString("jobType")));
                mrTemp.setJobTypeName(DmShared.xNull(rsTemp.getString("jobType")).equalsIgnoreCase("0") ? DmEnum.DmWebJobTypes.SMS.getWebJobName()
                        : DmShared.xNull(rsTemp.getString("jobType")).equalsIgnoreCase("1") ? DmEnum.DmWebJobTypes.EMAIL.getWebJobName()
                        : DmShared.xNull(rsTemp.getString("jobType")).equalsIgnoreCase("2") ? DmEnum.DmWebJobTypes.EVENT.getWebJobName() : "");
                mrTemp.setTemplateCode(DmShared.xNull(rsTemp.getString("templateCode")));
                mrTemp.setIsActiveCode(DmShared.xNull(rsTemp.getString("isActive")));
                mrTemp.setIsActiveName(DmShared.xNull(rsTemp.getString("isActive")).equalsIgnoreCase("1") ? "Yes" : "No");
                mrTemp.setSharedPreparedBy(DmShared.xNull(rsTemp.getString("preparedBy")));
                mrTemp.setSharedModifiedBy(DmShared.xNull(rsTemp.getString("u_Name")));
                mrTemp.setStartDate(DmShared.xNull(rsTemp.getString("stDate")));
                mrTemp.setStartTime(DmShared.xNull(rsTemp.getString("stTime")));
                mrTemp.setExpiryDate(DmShared.xNull(rsTemp.getString("exDate")));
                mrTemp.setExpiryTime(DmShared.xNull(rsTemp.getString("exTime")));
                mrTemp.setRbTaskScheduler(DmShared.xNull(rsTemp.getString("taskScheduler")));
                mrTemp.setRecurrenceQty(DmServer.vNull(rsTemp.getString("recurQuantity"), "0"));
                mrTemp.setRecurrenceUnit(DmServer.vNull(rsTemp.getString("recurUnit"), "0"));
                mrTemp.setRbDayOrMonth(DmShared.xNull(rsTemp.getString("dayOrMonth")));
                mrTemp.setMonthsCode(DmShared.xNull(rsTemp.getString("months")).replaceAll("\\|", ","));
                mrTemp.setDateCode(DmShared.xNull(rsTemp.getString("dates")).replaceAll("\\|", ","));
                mrTemp.setWeekCode(DmShared.xNull(rsTemp.getString("weeks")).replaceAll("\\|", ","));
                mrTemp.setResponsibleUser(DmShared.xNull(rsTemp.getString("responsibleUser")));

                if (DmShared.xNull(mrTemp.getRbTaskScheduler()).trim().equalsIgnoreCase("3")) {
                    mrTemp.setCbDays(DmShared.xNull(rsTemp.getString("wmDays")));
                } else {
                    mrTemp.setDaysCode(DmShared.xNull(rsTemp.getString("wmDays")));
                }
            }
            rsTemp.close();
            psTemp.close();

            if ((!DmShared.xNull(mrTemp.getSiteCode()).equalsIgnoreCase("-1")) && (!DmShared.xNull(mrTemp.getSiteCode()).equalsIgnoreCase(""))) {
                String siteName = "";
                strSQL = " SELECT name FROM siteMast WHERE code IN (" + mrTemp.getSiteCode() + ")  ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    siteName += DmShared.xNull(rsTemp.getString("name")) + ",";
                }
                rsTemp.close();
                psTemp.close();
                if (siteName.length() >= 1) {
                    siteName = siteName.substring(0, siteName.length() - 1);
                }
                mrTemp.setSiteName(DmShared.xNull(siteName));
            }

            if (!DmShared.xNull(mrTemp.getTemplateCode()).equalsIgnoreCase("")) {
                cnTemp.setCatalog(DmShared.xNull(mrTemp.getDbLookUp()));
                strSQL = " SELECT wtmp.code, wtmp.name FROM wJTemplateMast AS wtmp WHERE wtmp.code = :templateCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("templateCode", DmShared.xNull(mrTemp.getTemplateCode()));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    mrTemp.setTemplateCode(DmShared.xNull(rsTemp.getString("code")));
                    mrTemp.setTemplateName(DmShared.xNull(rsTemp.getString("name")));
                }
                rsTemp.close();
                psTemp.close();
            }
            cnTemp.setCatalog(DmServer.dbCompany);
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
        String strSQL;
        ResultSet rsTemp = null;
        dmrTemp.setData(strKey);

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);
            strSQL = "Delete From schedulerMast Where docId= :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(strKey).toString());
            psTemp.executeUpdate();
            psTemp.close();
            spBegin.commit();
            cnTemp.close();

            DmServer.getWebServiceSchedulars().scheduleRemove(strKey);
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
    public DmResult<String> saveRecord(Scheduler_Mdl mrTemp) {
        DmResult<String> dmrTemp = new DmResult();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        boolean blnAdd = false;
        String strSQL;
        ResultSet rsTemp = null;

        dmrTemp.setData("");

        try {
            Scheduler_Vld.chkSave(mrTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            spBegin = new DmSavePoint(cnTemp);

            cnTemp.setCatalog(DmShared.xNull(mrTemp.getDbLookUp()));

            mrTemp.setSharedKeyFieldName(DmShared.removeSpace(mrTemp.getSharedKeyFieldName()));
            if (DmShared.xNull(mrTemp.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                blnAdd = true;
                mrTemp.setSharedKeyFieldCode(DmServer.getSerialNoInteger(this, "schedulerMast", cnTemp));
            }

            if (mrTemp.getSharedKeyFieldCode().equalsIgnoreCase("")) {
                throw new UnsupportedOperationException("Invalid key.");
            }
            cnTemp.setCatalog(DmServer.dbCompany);

            strSQL = " Select COUNT(name) AS cnt  from schedulerMast where name=:name AND docId <> :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
            psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getLong("cnt") > 0) {
                throw new UnsupportedOperationException("Scheduler Name already exists.");
            }
            rsTemp.close();
            psTemp.close();

            if (blnAdd) {
                strSQL = "INSERT INTO schedulerMast "
                        + "(docId, PLTCode, name, WSTCode, startDateTime, expDateTime,"
                        + " isActive, jobType, selSiteCodes, recurQuantity, recurUnit, schType, selDays, "
                        + " dayOrMonth, selMonths, selDates, selWeeks, preparedBy, u_EntDate, responsibleUser) "
                        + " VALUES (:docId, :PLTCode, :name, :WSTCode, :startDateTime, :expDateTime, "
                        + " :isActive, :jobType, :selSiteCodes, :recurQuantity, :recurUnit, :schType, :selDays, "
                        + " :dayOrMonth, :selMonths, :selDates, :selWeeks, :u_Name, GETDATE(), :responsibleUser) ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("PLTCode", DmShared.foriegnKey(mrTemp.getPltCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("WSTCode", DmShared.foriegnKey(mrTemp.getTemplateCode()));
                psTemp.setString("startDateTime", DmShared.xNull(mrTemp.getStartDate()) + " " + DmShared.xNull(mrTemp.getStartTime()).replace(".", ":"));
                if (DmShared.dNull(mrTemp.getExpiryDate()).equalsIgnoreCase("") || DmShared.xNull(mrTemp.getExpiryTime()).equalsIgnoreCase("")) {
                    psTemp.setString("expDateTime", null);
                } else {
                    psTemp.setString("expDateTime", DmShared.xNull(mrTemp.getExpiryDate()) + " " + DmShared.xNull(mrTemp.getExpiryTime()).replace(".", ":"));
                }
                psTemp.setString("isActive", DmServer.vNull(mrTemp.getIsActiveCode(), "0"));
                psTemp.setString("jobType", DmServer.vNull(mrTemp.getJobTypeCode(), "0"));
                if (DmShared.xNull(mrTemp.getSiteCode()).trim().equalsIgnoreCase("")) {
                    psTemp.setString("selSiteCodes", DmShared.xNull("-1"));
                } else {
                    psTemp.setString("selSiteCodes", DmShared.xNull(mrTemp.getSiteCode()).replaceAll(",", "\\|"));
                }
                psTemp.setString("recurQuantity", DmServer.vNull(mrTemp.getRecurrenceQty(), "0"));
                psTemp.setString("recurUnit", DmServer.vNull(mrTemp.getRecurrenceUnit(), "0"));
                psTemp.setString("schType", DmShared.xNull(mrTemp.getRbTaskScheduler()));

                if (DmShared.iNull(mrTemp.getRbTaskScheduler()).intValue() == 3) {        //Checking for week
                    psTemp.setString("selDays", DmShared.xNull(mrTemp.getCbDays()));
                } else if (DmShared.iNull(mrTemp.getRbTaskScheduler()).intValue() == 4) {       //Checking for month
                    psTemp.setString("selDays", DmShared.xNull(mrTemp.getDaysCode()).replaceAll(",", "\\|"));
                } else {
                    psTemp.setString("selDays", DmShared.xNull(""));
                }
                psTemp.setString("dayOrMonth", DmShared.xNull(mrTemp.getRbDayOrMonth()));
                psTemp.setString("selMonths", DmShared.xNull(mrTemp.getMonthsCode()).replaceAll(",", "\\|"));
                psTemp.setString("selDates", DmShared.xNull(mrTemp.getDateCode()).replaceAll(",", "\\|"));
                psTemp.setString("selWeeks", DmShared.xNull(mrTemp.getWeekCode()).replaceAll(",", "\\|"));
                psTemp.setString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("responsibleUser", DmShared.xNull(mrTemp.getResponsibleUser()));
            } else {

                strSQL = "Update schedulerMast set "
                        + " PLTCode = :PLTCode , "
                        + " name = :name ,"
                        + " WSTCode = :WSTCode ,"
                        + " startDateTime = :startDateTime , "
                        + " expDateTime = :expDateTime , "
                        + " isActive = :isActive , "
                        + " jobType = :jobType , "
                        + " selSiteCodes = :selSiteCodes , "
                        + " recurQuantity = :recurQuantity , "
                        + " recurUnit = :recurUnit ,"
                        + " schType = :schType , "
                        + " selDays = :selDays ,"
                        + " dayOrMonth = :dayOrMonth , "
                        + " selMonths = :selMonths , "
                        + " selDates = :selDates , "
                        + " selWeeks = :selWeeks , "
                        + " responsibleUser = :responsibleUser , "
                        + " u_EntDate = GETDATE() , "
                        + " u_Name = :u_Name "
                        + " where docid = :docId ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
                psTemp.setString("docId", DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());
                psTemp.setString("PLTCode", DmShared.foriegnKey(mrTemp.getPltCode()));
                psTemp.setString("name", DmShared.xNull(mrTemp.getSharedKeyFieldName()));
                psTemp.setString("WSTCode", DmShared.foriegnKey(mrTemp.getTemplateCode()));
                psTemp.setString("startDateTime", DmShared.xNull(mrTemp.getStartDate()) + " " + DmShared.xNull(mrTemp.getStartTime()).replace(".", ":"));
                if (DmShared.dNull(mrTemp.getExpiryDate()).equalsIgnoreCase("") || DmShared.xNull(mrTemp.getExpiryTime()).equalsIgnoreCase("")) {
                    psTemp.setString("expDateTime", null);
                } else {
                    psTemp.setString("expDateTime", DmShared.xNull(mrTemp.getExpiryDate()) + " " + DmShared.xNull(mrTemp.getExpiryTime()).replace(".", ":"));
                }
                psTemp.setString("isActive", DmServer.vNull(mrTemp.getIsActiveCode(), "0"));
                psTemp.setString("jobType", DmServer.vNull(mrTemp.getJobTypeCode(), "0"));
                if (DmShared.xNull(mrTemp.getSiteCode()).trim().equalsIgnoreCase("")) {
                    psTemp.setString("selSiteCodes", DmShared.xNull("-1"));
                } else {
                    psTemp.setString("selSiteCodes", DmShared.xNull(mrTemp.getSiteCode()).replaceAll(",", "\\|"));
                }
                psTemp.setString("recurQuantity", DmServer.vNull(mrTemp.getRecurrenceQty(), "0"));
                psTemp.setString("recurUnit", DmServer.vNull(mrTemp.getRecurrenceUnit(), "0"));
                psTemp.setString("schType", DmShared.xNull(mrTemp.getRbTaskScheduler()));

                if (DmShared.iNull(mrTemp.getRbTaskScheduler()).intValue() == 3) {        //Checking for week
                    psTemp.setString("selDays", DmShared.xNull(mrTemp.getCbDays()));
                } else if (DmShared.iNull(mrTemp.getRbTaskScheduler()).intValue() == 4) {       //Checking for month
                    psTemp.setString("selDays", DmShared.xNull(mrTemp.getDaysCode()).replaceAll(",", "\\|"));
                } else {
                    psTemp.setString("selDays", DmShared.xNull(""));
                }
                psTemp.setString("dayOrMonth", DmShared.xNull(mrTemp.getRbDayOrMonth()));
                psTemp.setString("selMonths", DmShared.xNull(mrTemp.getMonthsCode()).replaceAll(",", "\\|"));
                psTemp.setString("selDates", DmShared.xNull(mrTemp.getDateCode()).replaceAll(",", "\\|"));
                psTemp.setString("selWeeks", DmShared.xNull(mrTemp.getWeekCode()).replaceAll(",", "\\|"));
                psTemp.setString("u_Name", DmShared.xNull(DmServer.getSsnUserName(this)));
                psTemp.setString("responsibleUser", DmShared.xNull(mrTemp.getResponsibleUser()));
            }
            psTemp.executeUpdate();
            psTemp.close();
            spBegin.commit();
            cnTemp.close();

            DmServer.addScheduler(DmShared.iNull(mrTemp.getSharedKeyFieldCode()).toString());

            dmrTemp.setData(mrTemp.getSharedKeyFieldCode());
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
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("  SELECT sm.docId, sm.Name AS schedulerName, plt.comp_Name AS pltname, sm.responsibleUser, "
                    + "  CASE 	WHEN sm.jobType = 0 THEN 'SMS'  "
                    + "	WHEN sm.jobType = 1 THEN 'E-Mail' END AS jobType, "
                    + "  CASE 	WHEN sm.schType = 1 THEN 'Ones'  "
                    + "	WHEN sm.schType = 2 THEN 'Daily'  "
                    + "	WHEN sm.schType = 3 THEN 'Weekly'  "
                    + "	WHEN sm.schType = 4 THEN 'Monthly' END AS taskScheduler, "
                    + "  CASE 	WHEN sm.isActive = 1 THEN 'Yes'  "
                    + "	WHEN sm.isActive = 0 THEN 'No' END AS active, "
                    + "  FORMAT(startdatetime,'dd-MMM-yyyy / HH:mm') AS startDateTime,  "
                    + "  FORMAT(expDateTime,'dd-MMM-yyyy / HH:mm') AS expireDateTime   "
                    + "FROM schedulerMast AS sm  "
                    + "  LEFT JOIN productlicensedto AS plt ON sm.pltCode = plt.pltCode  ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);

            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                listNode = new Find_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("docId")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("schedulerName")));
                listNode.setPltName(DmShared.xNull(rsTemp.getString("pltname")));
                listNode.setActive(DmShared.xNull(rsTemp.getString("active")));
                listNode.setJobType(DmShared.xNull(rsTemp.getString("jobType")));
                listNode.setTaskScheduler(DmShared.xNull(rsTemp.getString("taskScheduler")));
                listNode.setStartDateTime(DmShared.xNull(rsTemp.getString("startDateTime")));
                listNode.setExpireDateTime(DmShared.xNull(rsTemp.getString("expireDateTime")));
                listNode.setResponsibleUser(DmShared.xNull(rsTemp.getString("responsibleUser")));

                listNode.setIsAlive(DmShared.xNull(getSchedulerStatus(DmShared.iNull(listNode.getSharedKeyFieldCode()).toString())));

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
    public DmResultPageing<List<DataBase_Mdl>> getTxtHPLT(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<DataBase_Mdl>> rstTemp = new DmResultPageing<>();
        List<DataBase_Mdl> lstTemp = new ArrayList<>();
        DataBase_Mdl listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("SELECT "
                    + "plt.pltCode as code ,plt.comp_Name as name , com.centralData_Path as databaseName "
                    + "FROM productlicensedto  AS  plt "
                    + "left join company  AS com ON plt.latestCompCode = com.comp_Code "
                    + "where com.comp_Code is not null ");
            psTemp = DmServer.evaluateHelper(helper, sqlWithParam, cnTemp, blnToEndOfFile, this);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                listNode = new DataBase_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(rsTemp.getString("code")));
                listNode.setSharedKeyFieldName(DmShared.xNull(rsTemp.getString("name")));
                listNode.setDatabasename(DmShared.xNull(rsTemp.getString("databaseName")));
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHPLTSite(String pltcode, DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {
            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            sqlWithParam.setSQL("SELECT st.code  AS code , st.name  FROM sitemast AS st  WHERE st.pltCode=:pltcode");
            sqlWithParam.setString("pltcode", pltcode);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHJobType(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.DmWebJobTypes c : DmEnum.DmWebJobTypes.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(String.valueOf(c.getWebJobType()));
                listNode.setSharedKeyFieldName(c.getWebJobName());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHTemplate(String databBaseName, String schedulerType, DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);

            cnTemp = DmServer.getConnection(databBaseName);
            sqlWithParam.setSQL(" SELECT wtmp.code, wtmp.name FROM wJTemplateMast AS wtmp "
                    + " WHERE wtmp.templateType=:schedulerType ");
            sqlWithParam.setString("schedulerType", schedulerType);
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHActive(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("0");
            listNode.setSharedKeyFieldName("No");
            lstTemp.add(listNode);

            listNode = new KeyFieldExtended();
            listNode.setSharedKeyFieldCode("1");
            listNode.setSharedKeyFieldName("Yes");
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonthsName(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.DmMonths c : DmEnum.DmMonths.values()) {
                if (!String.valueOf(c.getMonthCalendarValue()).equalsIgnoreCase("-1")) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(String.valueOf(c.getMonthCalendarValue()));
                    listNode.setSharedKeyFieldName(c.getMonthName());
                    lstTemp.add(listNode);
                }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonthsNumber(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.DmDates c : DmEnum.DmDates.values()) {
                if (!(String.valueOf(c.getValue()).equalsIgnoreCase("0"))) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(String.valueOf(c.getValue()));
                    listNode.setSharedKeyFieldName(String.valueOf(c.getDisplayName()));
                    lstTemp.add(listNode);
                }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonthsWeekNumber(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {

            for (DmEnum.DmWeeks c : DmEnum.DmWeeks.values()) {
                listNode = new KeyFieldExtended();
                listNode.setSharedKeyFieldCode(String.valueOf(c.getWeekCalendarValue()));
                listNode.setSharedKeyFieldName(c.getWeekName());
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHMonthsWeekName(DmHelper helper, boolean blnToEndOfFile) {

        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;

        try {
            for (DmEnum.DmWeekdays c : DmEnum.DmWeekdays.values()) {
                if (!(String.valueOf(c.getWeekDay()).equalsIgnoreCase("-1"))) {
                    listNode = new KeyFieldExtended();
                    listNode.setSharedKeyFieldCode(String.valueOf(c.getWeekDay()));
                    listNode.setSharedKeyFieldName(c.getWeekDayName());
                    lstTemp.add(listNode);
                }
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
    public DmResultPageing<List<KeyFieldExtended>> getTxtHResponsibleUser(DmHelper helper, boolean blnToEndOfFile) {
        DmResultPageing<List<KeyFieldExtended>> rstTemp = new DmResultPageing<>();
        List<KeyFieldExtended> lstTemp = new ArrayList<>();
        KeyFieldExtended listNode;
        Connection cnTemp = null;
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        DmSQLWithParam sqlWithParam = new DmSQLWithParam();
        try {

            rstTemp.setData(lstTemp);
            cnTemp = DmServer.getConnection(DmServer.dbCompany);

            sqlWithParam.setSQL(" SELECT code, user_Name AS Name FROM usermast ");
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

    private String getSchedulerStatus(String docId) {
        String status = "Dead";
        try {
            List<SchedulerMast_Mdl> schedulerList = DmServer.getWebServiceSchedulars().getListedSchedulers();
            for (SchedulerMast_Mdl schedulerMdl : schedulerList) {
                if (DmShared.iNull(schedulerMdl.getDocId()).longValue() == DmShared.iNull(docId).longValue()) {
                    status = "Alive";
                    break;
                }
            }
        } catch (Exception e) {
        }
        return status;
    }

}
