package dataman.dmToolkit.dmStatic.server;

import dataman.dmBase.dmBaseStatic.server.DmBaseServer;
import dataman.dmBase.dmSession.server.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.UserPermission;
import dataman.dmBase.self.shared.VoucherPermission;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.dmToolkit.dmJasper.server.DmPLSqlExecutorFactory;
import dataman.dmToolkit.self.server.DMJasperVariables;
import dataman.dmToolkit.self.server.ManageUser;
import dataman.dmToolkit.self.server.ManageUser_Abstract;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.CloseReason;
import javax.websocket.Session;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import java.util.Timer;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class DmServer extends DmBaseServer {

    public class DocumentPrintParam {

        String reportName, procedureName, prefix;
    }
//    private static Logger logger = LoggerFactory.getLogger(DmServer.class);

    private static Timer gcTimer = new Timer(false);
    private static ManageUser mu = null;
    private static DMJasperVariables dmjv = null;

    public static void getInitializeINI(ServletContextEvent sce, String iniName) {
        String sqlHostName, sqlPort;

        try {
//            System.setProperty("java.awt.headless", "true");
            System.out.println("java.awt.headless:" + System.getProperty("java.awt.headless"));
            System.out.println("getInitializeINI(ServletContextEvent sce, String iniName)");
//            InitialContext icTemp = new InitialContext();
//            Properties prpTemp = (Properties) icTemp.lookup(iniName);
//            System.out.println("(Properties) icTemp.lookup(iniName)");
//            String configPath = prpTemp.getProperty("configPath").trim();

            String configPath = DmShared.xNull(System.getProperty(iniName, ""));
            System.out.println("repository path: " + DmShared.xNull(configPath));
            System.out.println("Context Path: " + sce.getServletContext().getContextPath());
            if (!configPath.trim().equalsIgnoreCase("")) {
                configPath = configPath + DmShared.xNull(sce.getServletContext().getContextPath());
            }
            System.out.println("repository final path: " + DmShared.xNull(configPath));
            Properties prpTemp = DmBaseServer.getInitializeBaseINI(sce, configPath, DmPLSqlExecutorFactory.class.getCanonicalName());
            sqlHostName = prpTemp.getProperty("sqlHostName").trim();
            System.out.println("sqlHostName: " + sqlHostName);
            sqlPort = prpTemp.getProperty("sqlPort").trim();
            System.out.println("sqlPort");
            sqlPassword = prpTemp.getProperty("sqlPassword");
            System.out.println("sqlPassword");
            sqlUser = prpTemp.getProperty("sqlUser");
            System.out.println("sqlUser");
            dbCompany = prpTemp.getProperty("dbCompany").trim();
            System.out.println("dbCompany");
//            Path.repository = prpTemp.getProperty("physicalPath").trim();
//            System.out.println("physicalPath");
            mongoDBServer = prpTemp.getProperty("mongoServer").trim();
            System.out.println("mongoServer");
            mongoDBPort = prpTemp.getProperty("mongoPort").trim();
            System.out.println("mongoPort");
            pacPortWeb = prpTemp.getProperty("pacPortWeb").trim();
            System.out.println("pacPortWeb");

            localeCountry = prpTemp.getProperty("localeCountry").trim();
            System.out.println("localeCountry" + localeCountry);
//            pasServer = prpTemp.getProperty("pasServer").trim();
//            System.out.println("pasServer");
//            pasPortTCP = prpTemp.getProperty("pasPortTCP").trim();
//            System.out.println("pasPortTCP");
//            pasPortWeb = prpTemp.getProperty("pasPortWeb").trim();
//            System.out.println("pasPortWeb");
            sessionTimeOut = Long.parseLong(prpTemp.getProperty("sessionTimeOut"));
            System.out.println("sessionTimeOut");
            gcTimeInterval = Long.parseLong(prpTemp.getProperty("gcTimeInterval"));
            System.out.println("gcTimeInterval");
            sessionLockTimeOut = Long.parseLong(prpTemp.getProperty("sessionLockTimeOut"));
            System.out.println("sessionLockTimeOut");
            if (sessionTimeOut <= 0) {
                sessionTimeOut = (30 * 60); //30 Minutes
            }

            if (gcTimeInterval <= 0) {
                gcTimeInterval = (6 * 60 * 60); //6 Hours
            }

            if (sessionLockTimeOut <= 0) {
                sessionLockTimeOut = (1 * 60); //1 Minutes
            }

            createRepositoryExtended();
            System.out.println("createRepositoryExtended");

            gcTimer.schedule(new DmGarbageCleaner(), new java.util.Date(), gcTimeInterval * 1000);

            iniBoneCP(sqlHostName, sqlPort);

            iniMongo();

            addScheduler("");
            System.out.println("addScheduler");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    private static void createRepositoryExtended() {
        File file;

        try {
//            Path.repository = Path.repository + "/repository";
//            Path.sessions = Path.repository + "/sessions";
//            Path.logs = Path.repository + "/logs";

//            Path.resources = Path.repository + "/resources";
//            Path.reports = Path.resources + "/reports";
//            Path.images = Path.resources + "/images";
            Path.webJobs = Path.resources + "/webJobs";
            Path.csvTemplates_SMS_eMail = Path.webJobs + "/csvTemplates_SMS_eMail";

//            file = new File(Path.repository);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            file = new File(Path.sessions);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            file = new File(Path.logs);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            file = new File(Path.resources);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            file = new File(Path.reports);
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            file = new File(Path.images);
//            if (!file.exists()) {
//                file.mkdir();
//            }
            file = new File(Path.webJobs);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.csvTemplates_SMS_eMail);
            if (!file.exists()) {
                file.mkdir();
            }

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static boolean setNotificationSession(String sessionId, Session session) {
        boolean bool = false;
        try {
            if (DmSession.getSessions().containsKey(sessionId)) {
                DmSession.getSessions().get(sessionId).setReceiverSession(session);
            } else {
                session.close(new CloseReason(CloseReason.CloseCodes.RESERVED, "Invalid User. Notifications Blocked."));
            }
        } catch (Exception ex) {
            bool = false;
            ex.printStackTrace();
        }
        return bool;
    }

    public static boolean validateToken(HttpServletRequest request, String userName, String tokenKey) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        boolean blnRtn = false;

        try {

            cnTemp = getConnection(dbCompany);
            sqlQuery = "Select count(*) as cnt,max(UM.User_Name) as User_Name,isNull(max(UM.Description),'') As Description,"
                    + "isNull(max(userName_Linked),'') as userName_Linked,max(tokenKey) as tokenKey "
                    + "From UserMast UM "
                    + "Where isActive = 1 "
                    + "and User_Name = :user_Name ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("user_Name", userName.toLowerCase());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                    if (DmShared.xNull(tokenKey).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("tokenKey")))) {
                        blnRtn = true;
                        setSsnData(request, "UserName", DmShared.xNull(rsTemp.getString("User_Name")).trim());
                        setSsnData(request, "UserDescription", DmShared.xNull(rsTemp.getString("Description")).trim().equalsIgnoreCase("") ? DmShared.xNull(rsTemp.getString("User_Name")).trim() : DmShared.xNull(rsTemp.getString("Description")).trim());
                        setSsnData(request, "LinkedUser", DmShared.xNull(rsTemp.getString("userName_Linked")).trim());
                    }
                }
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return blnRtn;
    }

    public static boolean validateToken(DmRSS rssTemp, String userName, String tokenKey) {
        return validateToken(rssTemp.getLocalRequest(), userName, tokenKey);
    }

    public static boolean validateUser(DmRSS rssTemp, DmEnum.DmUserValidationMode userValidationMode, String userName, String password) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        boolean blnRtn = false;

        try {
            System.out.println("validateUser: " + DmShared.xNull(userName.toLowerCase()));
            cnTemp = getConnection(dbCompany);
            System.out.println("validateUser company Connection: " + DmShared.xNull(userName.toLowerCase()));
            sqlQuery = "Select count(*) as cnt,max(UM.User_Name) as User_Name,isNull(max(UM.Description),'') As Description,"
                    + "isNull(max(userName_Linked),'') as userName_Linked,max(un.manualCode) as natureCode,  "
                    + "max(um.PassWd) as PassWd, convert(varchar(32),HashBytes('MD5',lower(max(UM.User_Name)) + :passWd),2) myPassWd,"
                    + "max(um.UserLockTimeOut) as UserLockTimeOut "
                    + "From UserMast UM "
                    + "left join userNature un on um.nature = un.code "
                    + "Where isActive = 1 ";
//                    + "PassWd = convert(varchar(32),HashBytes('MD5',:passWd),2) and  "
            if (userValidationMode == DmEnum.DmUserValidationMode.EMAIL) {
                sqlQuery += "and eMail = :user_Name ";
            } else if (userValidationMode == DmEnum.DmUserValidationMode.MOBILE) {
                sqlQuery += "and mobile = :user_Name ";
            } else {
                sqlQuery += "and User_Name = :user_Name ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("passWd", password);
            psTemp.setString("user_Name", userName.toLowerCase());
            rsTemp = psTemp.executeQuery();
            System.out.println("validateUser execute Query: " + DmShared.xNull(userName.toLowerCase()));
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() == 1) {
                    if (DmShared.xNull(rsTemp.getString("PassWd")).equalsIgnoreCase(DmShared.xNull(rsTemp.getString("myPassWd")))) {
                        blnRtn = true;
                        System.out.println("validateUser session inisialize: " + DmShared.xNull(userName.toLowerCase()));
                        setSsnData(rssTemp, "UserName", DmShared.xNull(rsTemp.getString("User_Name")).trim());
//                        setSsnData(rssTemp, "UserLockTimeOut", DmShared.iNull(rsTemp.getString("UserLockTimeOut")).intValue());
                        DmSession.getSessions().get(rssTemp.getLocalRequest().getSession().getId()).setLockTimeOut(DmShared.iNull(rsTemp.getString("UserLockTimeOut")).intValue());
                        setSsnData(rssTemp, "UserDescription", DmShared.xNull(rsTemp.getString("Description")).trim().equalsIgnoreCase("") ? DmShared.xNull(rsTemp.getString("User_Name")).trim() : DmShared.xNull(rsTemp.getString("Description")).trim());
                        setSsnData(rssTemp, "LinkedUser", DmShared.xNull(rsTemp.getString("userName_Linked")).trim());
                        setSsnData(rssTemp, "UserNatureCode", DmShared.xNull(rsTemp.getString("natureCode")).trim());
                        System.out.println("validateUser session inisialize complete: " + DmShared.xNull(userName.toLowerCase()));
                    }
                }
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            System.out.println("validateUser finally: " + DmShared.xNull(userName.toLowerCase()));
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
        return blnRtn;
    }

    public static boolean validateSite(DmRSS rssTemp, String siteCode) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;
        boolean blnRtn = false;

        try {
            cnTemp = getConnection(getSsnDBName(rssTemp));
            sqlQuery = "Select SM.Code,SM.Name,SM.manualCode,sm.stateNumericCode "
                    + "From SiteMast SM "
                    + "Where sm.Code = :siteCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                blnRtn = true;
                setSsnData(rssTemp, "SiteCode", DmShared.xNull(rsTemp.getString("Code")).trim());
                setSsnData(rssTemp, "SiteSName", DmShared.xNull(rsTemp.getString("manualCode")).trim());
                setSsnData(rssTemp, "SiteStateNumericCode", DmShared.iNull(rsTemp.getString("stateNumericCode")).toString().trim());
                break;
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return blnRtn;
    }

    public static PCSData getPLTs(DmRSS rssTemp, String pltCode) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, userName;
        PCSData pcsTemp = new PCSData();
        PLT pltTemp = null;

        try {
            cnTemp = getConnection(dbCompany);
            userName = getSsnUserName(rssTemp);
            if (userName.trim().equalsIgnoreCase("SA") || userName.trim().equalsIgnoreCase("SUPER")) {
                sqlQuery = "Select PLT.PLTCode,Max(PLT.Comp_Name) As Comp_Name,null as subCode "
                        + "From ProductLicensedTo PLT "
                        + "Left Join Company C On C.PLTCode = PLT.PLTCode "
                        + "Left Join UserSite US On US.CompCode = C.Comp_Code "
                        + "Group By PLT.PLTCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            } else {
                sqlQuery = "Select PLT.PLTCode,Max(PLT.Comp_Name) As Comp_Name,max(ula.subCode) as subCode "
                        + "From ProductLicensedTo PLT "
                        + "Left Join Company C On C.PLTCode = PLT.PLTCode "
                        + "Left Join UserSite US On US.CompCode = C.Comp_Code "
                        + "Left Join userLinkAccount ULA On ULA.pltCode = PLT.PLTCode and ULA.user_Name = :user_Name "
                        + "Where US.User_Name = :user_Name "
                        + "Group By PLT.PLTCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("user_Name", userName);
            }
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                pltTemp = new PLT();
//                pcsTemp.setLoginDate(DmShared.dNull(rsTemp.getDate("loginDate")));
                pcsTemp.setLoginDate(getServerDateTime()[0]);
                pltTemp.setPLTCode(DmShared.xNull(rsTemp.getString("PLTCode")).trim());
                pltTemp.setPLTName(DmShared.xNull(rsTemp.getString("Comp_Name")).trim());
                pltTemp.setUserLinkedAccountCode(DmShared.xNull(rsTemp.getString("subCode")).trim());
                pcsTemp.lstPLTs.add(pltTemp);

                if (DmShared.xNull(pltCode).equalsIgnoreCase(pltTemp.getPLTCode())) {
                    pcsTemp.pltSelected = pltTemp;
                }

            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            if (pcsTemp.lstPLTs.size() == 1) {
                pcsTemp.pltSelected = pcsTemp.lstPLTs.get(0);
            }

            if (pcsTemp.pltSelected != null) {
                setSsnData(rssTemp, "PLTCode", pcsTemp.pltSelected.getPLTCode());
                setSsnData(rssTemp, "UserLinkedAccountCode", pcsTemp.pltSelected.getUserLinkedAccountCode());
            }

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return pcsTemp;
    }

    public static PCSData getCompanys(DmRSS rssTemp, String companyCode) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, userName, dbName = "", fileDBName = "", pltCode = "";
        PCSData pcsTemp = new PCSData();
        Company cTemp = null;

        try {
            cnTemp = getConnection(dbCompany);
            userName = getSsnUserName(rssTemp);
            pltCode = getSsnPLTCode(rssTemp);

            if (userName.trim().equalsIgnoreCase("SA") || userName.trim().equalsIgnoreCase("SUPER")) {
                sqlQuery = "Select C.Comp_Code,Max(p.Comp_Name) as Comp_Name, Max(C.V_Prefix) As V_Prefix,Max(CentralData_Path) As CentralData_Path, "
                        + "Max(centralFileData_Path) as centralFileData_Path,Max(C.Start_Dt) As Start_Dt,Max(C.End_Dt) As End_Dt,Max(C.CYear) As CYear "
                        + "From Company C "
                        + "left join " + dbCompany.trim() + ".dbo.productLicensedTo p on c.pltCode = p.pltCode "
                        + "Left Join UserSite US On US.CompCode = C.Comp_Code "
                        + "Where C.PLTCode = :pltCode "
                        + "Group By C.Comp_Code,C.Start_Dt  "
                        + "Order By C.Start_Dt Desc; ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("pltCode", pltCode);

            } else {
                sqlQuery = "Select C.Comp_Code,Max(p.Comp_Name) as Comp_Name, Max(C.V_Prefix) As V_Prefix,Max(CentralData_Path) As CentralData_Path, "
                        + "Max(centralFileData_Path) as centralFileData_Path, Max(C.Start_Dt) As Start_Dt,Max(C.End_Dt) As End_Dt,Max(C.CYear) As CYear "
                        + "From Company C "
                        + "left join " + dbCompany.trim() + ".dbo.productLicensedTo p on c.pltCode = p.pltCode "
                        + "Left Join UserSite US On US.CompCode = C.Comp_Code "
                        + "Where US.User_Name = :user_Name And C.PLTCode = :pltCode "
                        + "Group By C.Comp_Code,C.Start_Dt  "
                        + "Order By C.Start_Dt Desc; ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setString("user_Name", userName);
                psTemp.setString("pltCode", pltCode);
            }
            rsTemp = psTemp.executeQuery();

            while (rsTemp.next()) {
                cTemp = new Company();

                cTemp.setCompanyCode(DmShared.xNull(rsTemp.getString("Comp_Code")).trim());
                cTemp.setCompanyName(DmShared.xNull(rsTemp.getString("Comp_Name")).trim());
                cTemp.setCurrentYear(DmShared.xNull(rsTemp.getString("CYear")).trim());
                cTemp.setVPrefix(DmShared.xNull(rsTemp.getString("V_Prefix")).trim());
//                cTemp.setStartDate(dNull(rsTemp.getDate("Start_Dt")).trim());
//                cTemp.setEndDate(dNull(rsTemp.getDate("End_Dt")).trim());
                cTemp.setStartDate(DmShared.dNull(rsTemp.getDate("Start_Dt")));
                cTemp.setEndDate(DmShared.dNull(rsTemp.getDate("End_Dt")));
                pcsTemp.lstCompanys.add(cTemp);

                if (DmShared.xNull(companyCode).equalsIgnoreCase(cTemp.getCompanyCode())) {
                    pcsTemp.cSelected = cTemp;
                    dbName = DmShared.xNull(rsTemp.getString("CentralData_Path")).trim();
                    fileDBName = DmShared.xNull(rsTemp.getString("centralFileData_Path")).trim();
                }

                if (DmShared.xNull(companyCode).trim().equalsIgnoreCase("")) {
                    dbName = DmShared.xNull(rsTemp.getString("CentralData_Path")).trim();
                    fileDBName = DmShared.xNull(rsTemp.getString("centralFileData_Path")).trim();
                }
            }
            psTemp.close();
            rsTemp.close();
            cnTemp.close();

            if (pcsTemp.lstCompanys.size() == 1) {
                pcsTemp.cSelected = pcsTemp.lstCompanys.get(0);
            }

            if (pcsTemp.cSelected != null) {
                setSsnData(rssTemp, "CompanyCode", pcsTemp.cSelected.getCompanyCode());
                setSsnData(rssTemp, "CompanyName", pcsTemp.cSelected.getCompanyName());
                setSsnData(rssTemp, "CompanyCYear", pcsTemp.cSelected.getCurrentYear());
                setSsnDBName(rssTemp, DmShared.xNull(dbName).trim());
                setSsnData(rssTemp, "FileDBName", DmShared.xNull(fileDBName).trim());
                setSsnData(rssTemp, "VoucherPrefix", pcsTemp.cSelected.getVPrefix());
            }

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
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
        return pcsTemp;
    }

    public static PCSData getSites(DmRSS rssTemp, String strSiteCode) {
        Connection cnDB = null, cnCompany = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery, userName, compCode, pltCode, userSiteList = "", costCenterList = "", siteSName = "", siteStateNumericCode = "",
                siteName = "", siteDisplayName = "", siteAddress1 = "", siteAddress2 = "", siteCityName = "", sitePIN = "", sitePhone = "", siteEMail = "",
                siteDrugLicenseNo = "", siteGSTIN = "", siteGSTINDate = "", siteSerialKey = "", siteClientCode = "";
        PCSData pcsTemp = new PCSData();
        Site sSite = null;
        boolean siteFound = false;

        try {
            try {
                cnDB = getConnection(getSsnDBName(rssTemp));
            } catch (Exception e) {
                throw new DmException(1, e.getMessage());
            }

            cnCompany = getConnection(dbCompany);
            userName = getSsnUserName(rssTemp);
            compCode = getSsnCompanyCode(rssTemp);
            pltCode = getSsnPLTCode(rssTemp);
            if (userName.trim().equalsIgnoreCase("SA") || userName.trim().equalsIgnoreCase("SUPER")) {
                sqlQuery = "Select SM.Code From SiteMast SM ";
                psTemp = DmPreparedStatement.prepareStatement(cnDB, sqlQuery);
                rsTemp = psTemp.executeQuery();

                while (rsTemp.next()) {
                    if (!userSiteList.trim().equalsIgnoreCase("")) {
                        userSiteList += ",";
                    }
                    userSiteList += "'" + DmShared.xNull(rsTemp.getString("Code")).trim() + "'";
                }
                psTemp.close();
                rsTemp.close();
            } else {
                sqlQuery = "Select US.SiteList,US.CostCenterList "
                        + "From UserSite US "
                        + "Where US.User_Name = :user_Name And US.CompCode = :compCode ";
                psTemp = DmPreparedStatement.prepareStatement(cnCompany, sqlQuery);
                psTemp.setString("user_Name", userName);
                psTemp.setString("compCode", compCode);
                rsTemp = psTemp.executeQuery();
                while (rsTemp.next()) {
                    userSiteList = DmShared.xNull(rsTemp.getString("SiteList")).trim().replace("|", "'");
                    costCenterList = DmShared.xNull(rsTemp.getString("CostCenterList")).trim().replace("|", "'");
                    break;
                }
                psTemp.close();
                rsTemp.close();
            }
            if (userSiteList.trim().equalsIgnoreCase("")) {
                userSiteList = "''";
            }

            sqlQuery = "Select sm.Code,sm.Name,sm.regionalLanguage,sm.manualCode,sm.stateNumericCode,bm.name as branchName,sm.serialKey,sm.clientCode,"
                    + "sm.Address1,sm.Address2,sm.City,sm.Pin,sm.phone,sm.eMail,sm.drugLicenseNo,sm.gstin,sm.gstinDate,sm.siteDisplayName "
                    + "From SiteMast sm "
                    + "left join " + dbCompany.trim() + ".dbo.branchMast bm on bm.code = sm.branchCode "
                    + "Where sm.Code In (" + userSiteList + ") and sm.pltCode = '" + pltCode + "' and bm.code is not null ";
            psTemp = DmPreparedStatement.prepareStatement(cnDB, sqlQuery);
            rsTemp = psTemp.executeQuery();
            userSiteList = "";
            while (rsTemp.next()) {
                sSite = new Site();
                sSite.setSiteCode(DmShared.xNull(rsTemp.getString("Code")).trim());
                sSite.setSiteName(DmShared.xNull(rsTemp.getString("Name")).trim());
                sSite.setBranchName(DmShared.xNull(rsTemp.getString("branchName")).trim());
                pcsTemp.lstSites.add(sSite);
                pcsTemp.setLocaleCountry(DmShared.xNull(DmBaseServer.baseDmLocaleCountry.getDmCode()));
                if (DmShared.xNull(strSiteCode).equalsIgnoreCase(sSite.getSiteCode())) {
                    pcsTemp.setRegionalLanguage(DmShared.xNull(rsTemp.getString("regionalLanguage")).trim());
                    siteSName = DmShared.xNull(rsTemp.getString("manualCode")).trim();

                    siteDisplayName = DmShared.xNull(rsTemp.getString("siteDisplayName")).trim();
                    siteName = DmShared.xNull(rsTemp.getString("Name")).trim();
                    siteAddress1 = DmShared.xNull(rsTemp.getString("Address1")).trim();
                    siteAddress2 = DmShared.xNull(rsTemp.getString("Address2")).trim();
                    siteCityName = DmShared.xNull(rsTemp.getString("City")).trim();
                    sitePIN = DmShared.xNull(rsTemp.getString("Pin")).trim();
                    sitePhone = DmShared.xNull(rsTemp.getString("phone")).trim();
                    siteEMail = DmShared.xNull(rsTemp.getString("eMail")).trim();
                    siteDrugLicenseNo = DmShared.xNull(rsTemp.getString("drugLicenseNo")).trim();
                    siteGSTIN = DmShared.xNull(rsTemp.getString("gstin")).trim();
                    siteGSTINDate = DmShared.xNull(rsTemp.getString("gstinDate")).trim();
                    siteSerialKey = DmShared.xNull(rsTemp.getString("serialKey")).trim();
                    siteClientCode = DmShared.xNull(rsTemp.getString("clientCode")).trim();

                    siteStateNumericCode = DmShared.iNull(rsTemp.getString("stateNumericCode")).toString().trim();
                    pcsTemp.sSelected = sSite;
                } else if (pcsTemp.lstSites.size() == 1) {
                    pcsTemp.setRegionalLanguage(DmShared.xNull(rsTemp.getString("regionalLanguage")).trim());
                    siteSName = DmShared.xNull(rsTemp.getString("manualCode")).trim();
                    siteStateNumericCode = DmShared.iNull(rsTemp.getString("stateNumericCode")).toString().trim();
                    siteDisplayName = DmShared.xNull(rsTemp.getString("siteDisplayName")).trim();
                    siteName = DmShared.xNull(rsTemp.getString("Name")).trim();
                    siteAddress1 = DmShared.xNull(rsTemp.getString("Address1")).trim();
                    siteAddress2 = DmShared.xNull(rsTemp.getString("Address2")).trim();
                    siteCityName = DmShared.xNull(rsTemp.getString("City")).trim();
                    sitePIN = DmShared.xNull(rsTemp.getString("Pin")).trim();
                    sitePhone = DmShared.xNull(rsTemp.getString("phone")).trim();
                    siteEMail = DmShared.xNull(rsTemp.getString("eMail")).trim();
                    siteDrugLicenseNo = DmShared.xNull(rsTemp.getString("drugLicenseNo")).trim();
                    siteGSTIN = DmShared.xNull(rsTemp.getString("gstin")).trim();
                    siteGSTINDate = DmShared.xNull(rsTemp.getString("gstinDate")).trim();
                    siteSerialKey = DmShared.xNull(rsTemp.getString("serialKey")).trim();
                    siteClientCode = DmShared.xNull(rsTemp.getString("clientCode")).trim();
                }

                if (!userSiteList.trim().equalsIgnoreCase("")) {
                    userSiteList += ",";
                }
                userSiteList += "'" + DmShared.xNull(rsTemp.getString("Code")).trim() + "'";

                siteFound = true;
            }

            if (!siteFound) {
                throw new DmException(0, "Permission denied.");
            }
            setSsnData(rssTemp, "SiteList", userSiteList);
            setSsnData(rssTemp, "CostCenterList", costCenterList);
            psTemp.close();
            rsTemp.close();
            cnDB.close();
            cnCompany.close();
            if (pcsTemp.lstSites.size() == 1) {
                pcsTemp.sSelected = pcsTemp.lstSites.get(0);
            }

            if (pcsTemp.sSelected != null) {
                setSsnData(rssTemp, "SiteCode", pcsTemp.sSelected.getSiteCode());
                setSsnData(rssTemp, "SiteSName", DmShared.xNull(siteSName).trim());
                setSsnData(rssTemp, "SiteName", DmShared.xNull(siteName).trim());
                setSsnData(rssTemp, "SiteDisplayName", DmShared.xNull(siteDisplayName).trim());
                setSsnData(rssTemp, "SiteAddress1", DmShared.xNull(siteAddress1).trim());
                setSsnData(rssTemp, "SiteAddress2", DmShared.xNull(siteAddress2).trim());
                setSsnData(rssTemp, "SiteCityName", DmShared.xNull(siteCityName).trim());
                setSsnData(rssTemp, "SitePIN", DmShared.xNull(sitePIN).trim());
                setSsnData(rssTemp, "SitePhone", DmShared.xNull(sitePhone).trim());
                setSsnData(rssTemp, "SiteEMail", DmShared.xNull(siteEMail).trim());
                setSsnData(rssTemp, "SiteDrugLicenseNo", DmShared.xNull(siteDrugLicenseNo).trim());
                setSsnData(rssTemp, "SiteGSTIN", DmShared.xNull(siteGSTIN).trim());
                setSsnData(rssTemp, "SiteGSTINDate", DmShared.xNull(siteGSTINDate).trim());
                setSsnData(rssTemp, "SiteStateNumericCode", DmShared.iNull(siteStateNumericCode).toString().trim());
                setSsnData(rssTemp, "SiteSerialKey", DmShared.xNull(siteSerialKey).trim());
                setSsnData(rssTemp, "SiteClientCode", DmShared.xNull(siteClientCode).trim());
                cnDB = getConnection(getSsnDBName(rssTemp));
                sqlQuery = "select displayName from subgroup where subcode = " + DmShared.iNull(getSsnUserLinkedAccountCode(rssTemp)).toString() + " ";
                psTemp = DmPreparedStatement.prepareStatement(cnDB, sqlQuery);
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    setSsnData(rssTemp, "UserLinkedAccountName", DmShared.xNull(rsTemp.getString("displayName")));
                } else {
                    setSsnData(rssTemp, "UserLinkedAccountCode", "");
                }
                rsTemp.close();
                psTemp.close();

                setSsnData(rssTemp, "eGstParam", getEGstParam(DmShared.xNull(siteGSTIN).trim(), cnDB));
                cnDB.close();

                setCountryWiseSetting();
            }
        } catch (Exception e) {
            if (e instanceof DmException) {
                throw new DmException(((DmException) e).getErrorNo(), e.getMessage());
            } else {
                throw new UnsupportedOperationException(e.getMessage());
            }
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
                if (cnDB != null) {
                    cnDB.close();
                }
                if (cnCompany != null) {
                    cnCompany.close();
                }
            } catch (Exception e) {
            }
        }
        return pcsTemp;
    }

    public static EGstParam_Mdl getEGstParam(String keyCode, Connection cnTemp) throws Exception {

        EGstParam_Mdl eGSTParam_Mdl = new EGstParam_Mdl();
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";
        try {

            sqlQuery = " SELECT egp.userGstin \n"
                    //<editor-fold defaultstate="collapsed" desc="e-way">                    
                    + "      ,egp.eway_UserName \n"
                    + "      ,egp.eway_UserPassword \n"
                    + "      ,egp.eway_IrpCode \n"
                    + "      ,irw.name as eway_IrpName \n"
                    + "      ,irw.url as eway_BaseUrl \n"
                    + "      ,egp.eway_GSPName \n"
                    + "      ,egp.eway_ASPID \n"
                    + "      ,egp.eway_ASPID_Password \n"
                    + "      ,egp.eway_UrlFormat \n"
                    + "      ,egp.eway_AuthFormat \n"
                    + "      ,egp.eway_PrintFormat \n"
                    + "      ,egp.eway_AuthFolderName \n"
                    + "      ,egp.eway_BillFolderName \n"
                    + "      ,egp.eway_AuthApiName \n"
                    + "      ,egp.eway_BillApiName \n"
                    + "      ,egp.eway_PrintApiName \n"
                    + "      ,egp.eway_AuthApiVersion \n"
                    + "      ,egp.eway_BillApiVersion \n"
                    + "      ,egp.eway_PrintApiVersion \n"
                    + "      ,egp.eway_AuthAction \n"
                    + "      ,egp.eway_GenerateAction \n"
                    + "      ,egp.eway_CancelAction \n"
                    + "      ,egp.eway_PrintAction \n"
                    + "      ,egp.eway_DetailedPrintAction \n"
                    + "      ,egp.eway_getDetailAction \n"
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="E-inv">

                    + "      ,egp.einv_UserName \n"
                    + "      ,egp.einv_UserPassword \n"
                    + "      ,egp.einv_IrpCode \n"
                    + "      ,iri.name as einv_IrpName \n"
                    + "      ,iri.url as einv_BaseUrl \n"
                    + "      ,egp.einv_GSPName \n"
                    + "      ,egp.einv_ASPID \n"
                    + "      ,egp.einv_ASPID_Password \n"
                    + "      ,egp.einv_UrlFormat \n"
                    + "      ,egp.einv_AuthFormat \n"
                    + "      ,egp.einv_PrintFormat \n"
                    + "      ,egp.einv_AuthFolderName \n"
                    + "      ,egp.einv_ApiFolderName \n"
                    + "      ,egp.einv_AuthApiName \n"
                    + "      ,egp.einv_BillApiName \n"
                    + "      ,egp.einv_PrintApiName \n"
                    + "      ,egp.einv_AuthApiVersion \n"
                    + "      ,egp.einv_BillApiVersion \n"
                    + "      ,egp.einv_PrintApiVersion \n"
                    + "      ,egp.einv_AuthAction \n"
                    + "      ,egp.einv_GenerateAction \n"
                    + "      ,egp.einv_CancelFormat \n"
                    + "      ,egp.einv_CancelAction \n"
                    + "      ,egp.einv_PrintAction \n"
                    + "      ,egp.einv_DetailedPrintAction \n"
                    + "      ,egp.einv_getDetailAction \n"
                    + "      ,egp.einv_qrCodeSize \n"
                    + "      ,egp.einv_parseIrnResp \n"
                    + "      ,egp.einv_eWayViaIrnUrlFormat \n"
                    + "      ,egp.einv_eWayViaIrnApiName\n"
                    + "      ,egp.einv_eWayViaIrnApiFolderName\n"
                    + "      ,egp.einv_eWayViaIrnApiVersion \n"
                    //</editor-fold>
                    //<editor-fold defaultstate="collapsed" desc="Modified By name and DateTime">
                    + "     ,egp.u_Name \n"
                    + "     ,egp.u_EntDt \n"
                    //</editor-fold>
                    + " FROM eGstParam egp \n"
                    + " LEFT JOIN irpMast irw ON irw.code = egp.eway_IrpCode \n"
                    + " LEFT JOIN irpMast iri ON iri.code = egp.einv_IrpCode \n"
                    + " WHERE egp.userGstin =:userGstin ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userGstin", DmShared.xNull(keyCode).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                eGSTParam_Mdl.setUserGstin(DmShared.xNull(rsTemp.getString("userGstin")));
                //<editor-fold defaultstate="collapsed" desc="e-way">
                eGSTParam_Mdl.setEway_UserName(DmShared.xNull(rsTemp.getString("eway_UserName")));
                eGSTParam_Mdl.setEway_UserPassword(DmShared.xNull(rsTemp.getString("eway_UserPassword")));
                eGSTParam_Mdl.setEway_IrpCode(DmShared.xNull(rsTemp.getString("eway_IrpCode")));
                eGSTParam_Mdl.setEway_IrpName(DmShared.xNull(rsTemp.getString("eway_IrpName")));
                eGSTParam_Mdl.setEway_BaseUrl(DmShared.xNull(rsTemp.getString("eway_BaseUrl")));
                eGSTParam_Mdl.setEway_GSPName(DmShared.xNull(rsTemp.getString("eway_GSPName")));
                eGSTParam_Mdl.setEway_AspUserId(DmShared.xNull(rsTemp.getString("eway_ASPID")));
                eGSTParam_Mdl.setEway_AspPassword(DmShared.xNull(rsTemp.getString("eway_ASPID_Password")));
                //<editor-fold defaultstate="collapsed" desc="e-way Format">
                eGSTParam_Mdl.setEway_UrlFormat(DmShared.xNull(rsTemp.getString("eway_UrlFormat")));
                eGSTParam_Mdl.setEway_AuthFormat(DmShared.xNull(rsTemp.getString("eway_AuthFormat")));
                eGSTParam_Mdl.setEway_PrintFormat(DmShared.xNull(rsTemp.getString("eway_PrintFormat")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-way Folder">
                eGSTParam_Mdl.setEway_AuthFolderName(DmShared.xNull(rsTemp.getString("eway_AuthFolderName")));
                eGSTParam_Mdl.setEway_BillFolderName(DmShared.xNull(rsTemp.getString("eway_BillFolderName")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-way Api Name">
                eGSTParam_Mdl.setEway_AuthApiName(DmShared.xNull(rsTemp.getString("eway_AuthApiName")));
                eGSTParam_Mdl.setEway_BillApiName(DmShared.xNull(rsTemp.getString("eway_BillApiName")));
                eGSTParam_Mdl.setEway_PrintApiName(DmShared.xNull(rsTemp.getString("eway_PrintApiName")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-way Action">
                eGSTParam_Mdl.setEway_AuthAction(DmShared.xNull(rsTemp.getString("eway_AuthAction")));
                eGSTParam_Mdl.setEway_GenerateAction(DmShared.xNull(rsTemp.getString("eway_GenerateAction")));
                eGSTParam_Mdl.setEway_CancelAction(DmShared.xNull(rsTemp.getString("eway_CancelAction")));
                eGSTParam_Mdl.setEway_PrintAction(DmShared.xNull(rsTemp.getString("eway_PrintAction")));
                eGSTParam_Mdl.setEway_DetailePrintAction(DmShared.xNull(rsTemp.getString("eway_DetailedPrintAction")));
                eGSTParam_Mdl.setEway_getDetailAction(DmShared.xNull(rsTemp.getString("eway_getDetailAction")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-way Version">
                eGSTParam_Mdl.setEway_AuthApiVersion(DmShared.xNull(rsTemp.getString("eway_AuthApiVersion")));
                eGSTParam_Mdl.setEway_BillApiVersion(DmShared.xNull(rsTemp.getString("eway_BillApiVersion")));
                eGSTParam_Mdl.setEway_PrintApiVersion(DmShared.xNull(rsTemp.getString("eway_PrintApiVersion")));
                //</editor-fold>
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-Inv">
                eGSTParam_Mdl.setEinv_UserName(DmShared.xNull(rsTemp.getString("einv_UserName")));
                eGSTParam_Mdl.setEinv_UserPassword(DmShared.xNull(rsTemp.getString("einv_UserPassword")));
                eGSTParam_Mdl.setEinv_IrpCode(DmShared.xNull(rsTemp.getString("einv_IrpCode")));
                eGSTParam_Mdl.setEinv_IrpName(DmShared.xNull(rsTemp.getString("einv_IrpName")));
                eGSTParam_Mdl.setEinv_BaseUrl(DmShared.xNull(rsTemp.getString("einv_BaseUrl")));
                eGSTParam_Mdl.setEinv_GSPName(DmShared.xNull(rsTemp.getString("einv_GSPName")));
                eGSTParam_Mdl.setEinv_ASPID(DmShared.xNull(rsTemp.getString("einv_ASPID")));
                eGSTParam_Mdl.setEinv_ASPID_Password(DmShared.xNull(rsTemp.getString("einv_ASPID_Password")));
                //<editor-fold defaultstate="collapsed" desc="e-Inv Format">
                eGSTParam_Mdl.setEinv_UrlFormat(DmShared.xNull(rsTemp.getString("einv_UrlFormat")));
                eGSTParam_Mdl.setEinv_AuthFormat(DmShared.xNull(rsTemp.getString("einv_AuthFormat")));
                eGSTParam_Mdl.setEinv_PrintFormat(DmShared.xNull(rsTemp.getString("einv_PrintFormat")));
                eGSTParam_Mdl.setEinv_CancelFormat(DmShared.xNull(rsTemp.getString("einv_CancelFormat")));
                eGSTParam_Mdl.setEinv_eWayViaIrnUrlFormat(DmShared.xNull(rsTemp.getString("einv_eWayViaIrnUrlFormat")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-Inv Folder">
                eGSTParam_Mdl.setEinv_AuthFolderName(DmShared.xNull(rsTemp.getString("einv_AuthFolderName")));
                eGSTParam_Mdl.setEinv_ApiFolderName(DmShared.xNull(rsTemp.getString("einv_ApiFolderName")));
                eGSTParam_Mdl.setEinv_eWayViaIrnApiFolderName(DmShared.xNull(rsTemp.getString("einv_eWayViaIrnApiFolderName")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-Inv Api Name">
                eGSTParam_Mdl.setEinv_AuthApiName(DmShared.xNull(rsTemp.getString("einv_AuthApiName")));
                eGSTParam_Mdl.setEinv_BillApiName(DmShared.xNull(rsTemp.getString("einv_BillApiName")));
                eGSTParam_Mdl.setEinv_PrintApiName(DmShared.xNull(rsTemp.getString("einv_PrintApiName")));
                eGSTParam_Mdl.setEinv_eWayViaIrnApiName(DmShared.xNull(rsTemp.getString("einv_eWayViaIrnApiName")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-Inv Action">
                eGSTParam_Mdl.setEinv_AuthAction(DmShared.xNull(rsTemp.getString("einv_AuthAction")));
                eGSTParam_Mdl.setEinv_GenerateAction(DmShared.xNull(rsTemp.getString("einv_GenerateAction")));
                eGSTParam_Mdl.setEinv_CancelAction(DmShared.xNull(rsTemp.getString("einv_CancelAction")));
                eGSTParam_Mdl.setEinv_PrintAction(DmShared.xNull(rsTemp.getString("einv_PrintAction")));
                eGSTParam_Mdl.setEinv_DetailedPrintAction(DmShared.xNull(rsTemp.getString("einv_DetailedPrintAction")));
                eGSTParam_Mdl.setEinv_getDetailAction(DmShared.xNull(rsTemp.getString("einv_getDetailAction")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="e-Inv Version">
                eGSTParam_Mdl.setEinv_AuthApiVersion(DmShared.xNull(rsTemp.getString("einv_AuthApiVersion")));
                eGSTParam_Mdl.setEinv_BillApiVersion(DmShared.xNull(rsTemp.getString("einv_BillApiVersion")));
                eGSTParam_Mdl.setEinv_PrintApiVersion(DmShared.xNull(rsTemp.getString("einv_PrintApiVersion")));
                eGSTParam_Mdl.setEinv_eWayViaIrnApiVersion(DmShared.xNull(rsTemp.getString("einv_eWayViaIrnApiVersion")));
                //</editor-fold>
                eGSTParam_Mdl.setEinv_qrCodeSize(DmShared.xNull(rsTemp.getString("einv_qrCodeSize")));
                eGSTParam_Mdl.setEinv_parseIrnResp(DmShared.xNull(rsTemp.getString("einv_parseIrnResp")));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Modified By name and DateTime">
                if (rsTemp.getTimestamp("u_EntDt") != null) {
                    eGSTParam_Mdl.setUserDateTime(getUserWithDateTime(DmShared.xNull(rsTemp.getString("u_Name")), rsTemp.getTimestamp("u_EntDt")));
                }
                //</editor-fold>
            }
            rsTemp.close();
            psTemp.close();

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
        return eGSTParam_Mdl;

    }

//    public static DmPreparedStatement evaluateHelper(DmHelper helper, DmSQLWithParam sqlWithParam, Connection cnTemp, boolean blnEOF, DmRSS servletInstance) throws Exception {
//        ResultSet rsTemp = null;
//        DmPreparedStatement psTemp = null;
//        String finalQuery, orderBy;
//        int intTemp;
//
//        sqlWithParam = helper.prepareQuery(sqlWithParam);
//        finalQuery = sqlWithParam.getSQL();
//
//        if (helper.getPaging().getResultFormat() == DmPaging.Result.PRINT) {
//            orderBy = "";
//            if (DmShared.vNull(helper.getRowsPerPage()) > 0) {
//                if (DmShared.vNull(helper.getRowsStartFrom()) >= 0) {
//                    for (DmSort sort : helper.getSort()) {
//                        if (sort != null) {
//                            if (!DmShared.xNull(sort.getColumnName()).trim().equalsIgnoreCase("")) {
//                                if (!orderBy.trim().equalsIgnoreCase("")) {
//                                    orderBy += ",";
//                                }
//                                orderBy += DmShared.xNull(sort.getColumnName()).trim() + " " + sort.getSortType().name() + " ";
//                            }
//                        }
//                    }
//
//                    finalQuery += "order by " + orderBy;
//                }
//            }
//            sqlWithParam.setSQL(finalQuery);
//            DmPreparedStatement tmpStmt = loadParam(cnTemp, sqlWithParam);
//            RptFind tmpPrint = new RptFind();
//            tmpPrint.setMasterReport(helper.getPaging().getReport());
//            tmpPrint.setPreparedStatement(tmpStmt);
//            JasperPrint jprint = tmpPrint.printReport(servletInstance);
//            String fileName = DmServer.showJasper(servletInstance, jprint, "", helper.getPaging().getReport().getReportProperty());
//            helper.getPaging().getReport().setResultFileName(fileName);
//
//            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select  tmp.column1 from (select 0 as column1) as tmp where tmp.column1 = 1");
//
//        } else {
//            if (blnEOF) {
//                sqlWithParam.setSQL("Select Count(*) As Cnt From ( " + finalQuery + ") As T1 ");
//                psTemp = loadParam(cnTemp, sqlWithParam);
//                rsTemp = psTemp.executeQuery();
//                rsTemp.next();
//                helper.setRowCount(rsTemp.getInt(1));
//                rsTemp.close();
//                psTemp.close();
//                intTemp = Integer.valueOf(String.valueOf(helper.getRowCount() / helper.getRowsPerPage()));
//                intTemp = intTemp * helper.getRowsPerPage();
//                if ((helper.getRowCount() % helper.getRowsPerPage()) == 0) {
//                    intTemp = intTemp - helper.getRowsPerPage();
//                }
//                helper.setRowsStartFrom(intTemp);
//            }
//
//            orderBy = "";
//            if (DmShared.vNull(helper.getRowsPerPage()) > 0) {
//                if (DmShared.vNull(helper.getRowsStartFrom()) >= 0) {
//                    for (DmSort sort : helper.getSort()) {
//                        if (sort != null) {
//                            if (!DmShared.xNull(sort.getColumnName()).trim().equalsIgnoreCase("")) {
//                                if (!orderBy.trim().equalsIgnoreCase("")) {
//                                    orderBy += ",";
//                                }
//                                orderBy += DmShared.xNull(sort.getColumnName()).trim() + " " + sort.getSortType().name() + " ";
//                            }
//                        }
//                    }
//
//                    finalQuery += "order by " + orderBy;
//                    finalQuery += "offset " + String.valueOf(helper.getRowsStartFrom()) + " rows fetch next " + String.valueOf(helper.getRowsPerPage()) + " rows only ";
//                }
//            }
//
//            sqlWithParam.setSQL(finalQuery);
//            psTemp = loadParam(cnTemp, sqlWithParam);
//        }
//
//        return psTemp;
//    }
//    private static DmPreparedStatement loadParam(Connection cnTemp, DmSQLWithParam sqlWithParam) throws Exception {
//        DmPreparedStatement psTemp = null;
//        List<DmSQLParam> sqlparam;
//
//        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlWithParam.getSQL());
//
//        sqlparam = sqlWithParam.getSqlParam();
//        for (int i = 0; i < sqlparam.size(); i++) {
//            if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.DOUBLE) {
//                psTemp.setDouble(sqlparam.get(i).getParamName(), sqlparam.get(i).getDouble());
//            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.INTEGER) {
//                psTemp.setInt(sqlparam.get(i).getParamName(), sqlparam.get(i).getInteger());
//            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.DATETIME) {
//                if (sqlparam.get(i).getDate() != null) {
//                    psTemp.setDate(sqlparam.get(i).getParamName(), new Date(sqlparam.get(i).getDate().getTime()));
//                } else {
//                    psTemp.setDate(sqlparam.get(i).getParamName(), null);
//                }
//            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.STRINGLIST) {
//                psTemp.setStringList(sqlparam.get(i).getParamName(), sqlparam.get(i).getString());
//            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.TIMESTAMP) {
//                if (sqlparam.get(i).getTimestamp() != null) {
//                    psTemp.setTimestamp(sqlparam.get(i).getParamName(), sqlparam.get(i).getTimestamp());
//                } else {
//                    psTemp.setTimestamp(sqlparam.get(i).getParamName(), null);
//                }
//            } else {
//                psTemp.setString(sqlparam.get(i).getParamName(), sqlparam.get(i).getString());
//            }
//        }
//
//        return psTemp;
//    }
    public static JasperPrint fillJasper(DmRSS rssTemp, String reportTitle, JasperReport jasperReport, HashMap parameter, LinkedHashMap<String, FieldUDT_Mdl> filter, ResultSet resultSet) throws Exception {
//        JasperPrint jasperPrint;
//        JRResultSetDataSource jrResultSetDataSource;
//
//        jrResultSetDataSource = new JRResultSetDataSource(resultSet);
//        jasperPrint = JasperFillManager.fillReport(jasperReport, setJasperVariables(rssTemp, reportTitle, jasperReport, filter, parameter), jrResultSetDataSource);
        parameter = setJasperVariables(rssTemp, reportTitle, jasperReport, filter, parameter);
        return DmBaseServer.fillJasper(reportTitle, jasperReport, parameter, filter, resultSet);
//        return jasperPrint;
    }

    public static <T> JasperPrint fillJasper(DmRSS rssTemp, String reportTitle, JasperReport jasperReport, HashMap parameter, LinkedHashMap<String, FieldUDT_Mdl> filter, List<T> listOfT) throws Exception {
//        JasperPrint jasperPrint;
//        JRBeanCollectionDataSource jrBeanCollectionDataSource;
//
//        jrBeanCollectionDataSource = new JRBeanCollectionDataSource(listOfT);
//        jasperPrint = JasperFillManager.fillReport(jasperReport, setJasperVariables(rssTemp, reportTitle, jasperReport, filter, parameter), jrBeanCollectionDataSource);
//        return jasperPrint;
        parameter = setJasperVariables(rssTemp, reportTitle, jasperReport, filter, parameter);
        return DmBaseServer.fillJasper(reportTitle, jasperReport, parameter, filter, listOfT);
    }

//    public static String showJasper(DmRSS rssTemp, JasperPrint jasperPrint, String suffix, DmReportProperty_Mdl reportProperty) throws Exception {
//        String filePath, fileName;
//        filePath = DmBaseServer.Path.sessions + "/" + rssTemp.getLocalRequest().getSession().getId().trim();
//        if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
//            fileName = String.valueOf(new java.util.Date().getTime()).trim() + DmBaseShared.xNull(suffix).trim() + ".xls";
//
//        } else if (reportProperty.getExportType() == DmEnum.DmExportType.WORD) {
//            fileName = String.valueOf(new java.util.Date().getTime()).trim() + DmBaseShared.xNull(suffix).trim() + ".docx";
//
//        } else {
//            fileName = String.valueOf(new java.util.Date().getTime()).trim() + DmBaseShared.xNull(suffix).trim() + ".pdf";
//        }
//
//        return DmBaseServer.showJasper(filePath, fileName, jasperPrint, suffix, reportProperty);
//    }
    public static HashMap setJasperVariables(DmRSS rssTemp, String reportTitle, JasperReport jasperReport, LinkedHashMap<String, FieldUDT_Mdl> filter, HashMap parameter) throws Exception {
        JRParameter jrParameter[];
        String tempString, siteCode;//,sqlQuery
        File file;

        try {
            parameter = DmBaseServer.setJasperVariables(reportTitle, jasperReport, filter, parameter);

            siteCode = DmServer.getSsnSiteCode(rssTemp);
//            cnTemp = getConnection(getSsnDBName(rssTemp));
//            sqlQuery = "Select  SM.Name,SM.Address1,SM.Address2,SM.City,SM.Pin,sm.phone,sm.eMail,sm.drugLicenseNo,sm.gstin,sm.gstinDate "
//                    + "From SiteMast SM "
//                    + "Where SM.Code = :siteCode;";
//            psSite = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psSite.setString("siteCode", siteCode);
//            rsSite = psSite.executeQuery();
//            rsSite.next();

//            sqlQuery = "Select  C.Comp_Name,C.CYear "
//                    + "From " + dbCompany.trim() + ".dbo.Company C "
//                    + "Where C.Comp_Code = :compCode;";
//            psCompany = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
//            psCompany.setString("compCode", DmServer.getSsnCompanyCode(rssTemp));
//            rsCompany = psCompany.executeQuery();
//            rsCompany.next();
            jrParameter = jasperReport.getParameters();
            for (int i = 0; i < jrParameter.length; i++) {
                if (jrParameter[i].getName().equalsIgnoreCase("dmPrintBy")) {
                    parameter.put(jrParameter[i].getName(), getSsnUserName(rssTemp));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmCompanyName")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnCompanyName(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmFinancialYear")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnCompanyCYear(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteName")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSiteName(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteDisplayName")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSiteDisplayName(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteAddress")) {
                    tempString = DmShared.xNull(getSsnSiteAddress1(rssTemp));
                    if (!DmShared.xNull(getSsnSiteAddress2(rssTemp)).trim().equalsIgnoreCase("")) {
                        if (!tempString.trim().equalsIgnoreCase("")) {
                            tempString = tempString + " ";
                        }
                        tempString = tempString + DmShared.xNull(getSsnSiteAddress2(rssTemp));
                    }
                    parameter.put(jrParameter[i].getName(), tempString);
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmTitle")) {
                    parameter.put(jrParameter[i].getName(), reportTitle);
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteCity")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSiteCityName(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSitePin")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSitePIN(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSitePhone")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSitePhone(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteEMail")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSiteEMail(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteDrugLicenseNo")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSiteDrugLicenseNo(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteGSTIN")) {
                    parameter.put(jrParameter[i].getName(), DmShared.xNull(getSsnSiteGSTIN(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmSiteGSTINDate")) {
                    parameter.put(jrParameter[i].getName(), DmShared.dNull(getSsnSiteGSTINDate(rssTemp)));
                } else if (jrParameter[i].getName().equalsIgnoreCase("dmCompanyLogo")) {
                    file = new File(Path.images + "/" + siteCode + "/companyLogo");
                    if (file.exists()) {
                        parameter.put(jrParameter[i].getName(), Path.images + "/" + siteCode + "/companyLogo");
                    }
                }
            }

            if (dmjv != null) {
                parameter = dmjv.setJasperVariables(rssTemp, reportTitle, jasperReport, filter, parameter);
            }
        } finally {
        }
        return parameter;
    }

    public static void deleteMenu() {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany); //"J2EE_Company"
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "Delete from user_control_permission_web where userName in ('SA','SUPER') or isNull(Comp_Code,-1) = -1; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();

            sqlQuery = "Delete from User_permission_web where userName in ('SA','SUPER') or isNull(Comp_Code,-1) = -1; ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();

            spBegin.commit();
        } catch (Exception ex) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception e) {
            }
            throw new DmException(0, ex.getMessage());
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
    }

    public static void repairMenu(String moduleName, List<UserMenu> menuList) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery;
        Integer sno = 0;

        try {

            cnTemp = DmServer.getConnection(DmServer.dbCompany); //"J2EE_Company"
            spBegin = new DmSavePoint(cnTemp);

            sqlQuery = "select isNull(max(sno),0) sno from User_permission_web where userName in ('SA'); ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                sno = rsTemp.getInt("sno");
            }
            rsTemp.close();
            psTemp.close();
            repairMenu(moduleName, null, menuList, sno + 1, 0, cnTemp, psTemp);
            spBegin.commit();

            cnTemp.close();
        } catch (Exception ex) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception e) {
            }
            throw new DmException(0, ex.getMessage());
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
    }

    private static int repairMenu(String moduleName, String parentMenu, List<UserMenu> menuList, Integer sno, int level, Connection cnTemp, DmPreparedStatement psTemp) throws Exception {
        String sqlQuery;
        List<String> userControlList;

        for (int i = 0; i < menuList.size(); i++) {
            sqlQuery = "Insert Into User_permission_web(userName,mnuModule,comp_Code, "
                    + "mnuName,mnuText,sno,mnuLevel,parent, "
                    + "permission,reportFor,isActive) Values  "
                    + "(:userName,:mnuModule,:comp_Code,:mnuName,:mnuText,:sno,:mnuLevel,:parent,:permission,:reportFor,:isActive); ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", "SA");
            psTemp.setString("mnuModule", moduleName);
            psTemp.setString("comp_Code", "-1");
            psTemp.setString("mnuName", menuList.get(i).getMenuName().replace(" ", ""));
            psTemp.setString("mnuText", DmShared.removeSpace(menuList.get(i).getMenuName()));
            psTemp.setString("sno", String.valueOf(sno));
            psTemp.setString("mnuLevel", String.valueOf(level));
            psTemp.setString("parent", DmShared.xNull(parentMenu).replace(" ", ""));
            psTemp.setString("permission", "AEDP");
            psTemp.setString("reportFor", moduleName);
            psTemp.setString("isActive", "1");
            psTemp.executeUpdate();

            sqlQuery = "Insert Into User_permission_web(userName,mnuModule,comp_Code, "
                    + "mnuName,mnuText,sno,mnuLevel,parent, "
                    + "permission,reportFor,isActive) Values  "
                    + "(:userName,:mnuModule,:comp_Code,:mnuName,:mnuText,:sno,:mnuLevel,:parent,:permission,:reportFor,:isActive); ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("userName", "SUPER");
            psTemp.setString("mnuModule", moduleName);
            psTemp.setString("comp_Code", "-1");
            psTemp.setString("mnuName", menuList.get(i).getMenuName().replace(" ", ""));
            psTemp.setString("mnuText", DmShared.removeSpace(menuList.get(i).getMenuName()));
            psTemp.setString("sno", String.valueOf(sno));
            psTemp.setString("mnuLevel", String.valueOf(level));
            psTemp.setString("parent", DmShared.xNull(parentMenu).replace(" ", ""));
            psTemp.setString("permission", "AEDP");
            psTemp.setString("reportFor", moduleName);
            psTemp.setString("isActive", "1");
            psTemp.executeUpdate();

            sno++;

            userControlList = menuList.get(i).getControlPermission();
            if (userControlList != null) {
                for (int k = 0; k < userControlList.size(); k++) {
                    if (!DmShared.xNull(userControlList.get(k)).equalsIgnoreCase("")) {
                        sqlQuery = "Insert Into user_control_permission_web(userName,mnuModule,comp_Code, "
                                + "mnuName,mnuText,groupText,groupName) Values "
                                + "(:userName,:mnuModule,:comp_Code,:mnuName,:mnuText,:groupText,:groupName); ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("userName", "SA");
                        psTemp.setString("mnuModule", moduleName);
                        psTemp.setString("comp_Code", "-1");
                        psTemp.setString("mnuName", menuList.get(i).getMenuName().replace(" ", ""));
                        psTemp.setString("mnuText", DmShared.removeSpace(menuList.get(i).getMenuName()));
                        psTemp.setString("groupText", userControlList.get(k));
                        psTemp.setString("groupName", userControlList.get(k).replace(" ", ""));
                        psTemp.executeUpdate();

                        sqlQuery = "Insert Into user_control_permission_web(userName,mnuModule,comp_Code, "
                                + "mnuName,mnuText,groupText,groupName) Values "
                                + "(:userName,:mnuModule,:comp_Code,:mnuName,:mnuText,:groupText,:groupName); ";
                        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                        psTemp.setString("userName", "SUPER");
                        psTemp.setString("mnuModule", moduleName);
                        psTemp.setString("comp_Code", "-1");
                        psTemp.setString("mnuName", menuList.get(i).getMenuName().replace(" ", ""));
                        psTemp.setString("mnuText", DmShared.removeSpace(menuList.get(i).getMenuName()));
                        psTemp.setString("groupText", userControlList.get(k));
                        psTemp.setString("groupName", userControlList.get(k).replace(" ", ""));
                        psTemp.executeUpdate();
                    }
                }
            }

            if (menuList.get(i).getUserMenu().size() > 0) {
                sno = repairMenu(moduleName, menuList.get(i).getMenuName(), menuList.get(i).getUserMenu(), sno, level + 1, cnTemp, psTemp);
            }
        }

        return sno;
    }

    public static List<UserMenu> getUserMenu(String moduleName, DmRSS rssTemp) {
        List<UserMenu> menuList = new ArrayList<>();
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        String userName, compCode;
        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany); //"J2EE_Company"
            userName = DmServer.getSsnUserName(rssTemp);
            compCode = DmServer.getSsnCompanyCode(rssTemp);

            getUserMenu(userName, compCode, moduleName, menuList, null, 0, cnTemp);

        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
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
        return menuList;
    }

    public static UserPermission getUserPermission(DmRSS rssTemp, String moduleAllowed) {
        LinkedHashMap<String, List<UserMenu>> userPermission = new LinkedHashMap<>();
        List<VoucherPermission> voucherPermission = new ArrayList<>();
        List<UserMenu> menuList;
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String userName, compCode, dbName;
        String sqlQuery, currentDate = "", condition = "";

        try {
            cnTemp = DmServer.getConnection(DmServer.dbCompany);
            userName = DmServer.getSsnUserName(rssTemp);
            compCode = DmServer.getSsnCompanyCode(rssTemp);
            dbName = DmServer.getSsnDBName(rssTemp);

            if (DmShared.iNull(compCode).longValue() == 0) {
                throw new DmException(0, "Invalid company.");
            }

            if (!DmShared.xNull(moduleAllowed).trim().equalsIgnoreCase("")) {
                condition = " and upw.mnuModule in (" + moduleAllowed + ") ";
            }

            sqlQuery = "select upw.mnuModule,min(upw.sno) sno "
                    + "from user_permission_web upw "
                    + "where upw.isActive = 1 "
                    + "and upw.userName = '" + userName + "' "
                    + "and isNull(upw.Comp_Code,-1) In ('" + compCode + "','-1') "
                    + condition
                    + "group by upw.mnuModule "
                    + "order by min(upw.sno) ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                menuList = new ArrayList<>();
                getUserMenu(userName, compCode, DmShared.xNull(rsTemp.getString("mnuModule")), menuList, null, 0, cnTemp);
                userPermission.put(DmShared.xNull(rsTemp.getString("mnuModule")).toLowerCase(), menuList);
            }
            rsTemp.close();
            psTemp.close();

            currentDate = getServerDateTime()[0];
            sqlQuery = "Select	uvp.Category,vt.V_Type, "
                    + "case when isNull(uvp.VP_Days,0) = 0 then null else DATEADD(day,-(uvp.VP_Days - 1), '" + currentDate + "') end as VP_Date, "
                    + "case when isNull(uvp.DP_Days,0) = 0 then null else DATEADD(day,-(uvp.DP_Days - 1), '" + currentDate + "') end as DP_Date, "
                    + "case when isNull(uvp.SP_days,0) = 0 then null else DATEADD(day,-(uvp.SP_days - 1), '" + currentDate + "') end as SP_Date, "
                    + "uvp.AllowValueUpto,uvp.isAllowAudit As AllowAudit "
                    + "From User_Voucher_Permission uvp  "
                    + "left join " + dbName + ".dbo.voucher_Type vt on vt.short_Name = uvp.vTypeSName "
                    + "Where uvp.User_Name = '" + userName + "' And uvp.Comp_Code = '" + compCode + "'  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                voucherPermission.add(new VoucherPermission(
                        DmShared.xNull(rsTemp.getString("Category")),
                        rsTemp.getString("V_Type"), DmShared.dNull(rsTemp.getDate("VP_Date")),
                        DmShared.dNull(rsTemp.getDate("DP_Date")), DmShared.dNull(rsTemp.getDate("SP_Date")), rsTemp.getDouble("AllowValueUpto")));
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
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

        return new UserPermission(userPermission, voucherPermission);
    }

    private static void getUserMenu(String userName, String compCode, String moduleName, List<UserMenu> menuList, String parentMenu, int level, Connection cnTemp) {
        DmPreparedStatement psTemp = null, psTemp1 = null;
        ResultSet rsTemp = null, rsTemp1 = null;
        String sqlQuery, currentDate = "";

        try {
            currentDate = getServerDateTime()[0];
            sqlQuery = "Select  UPW.SNo,UPW_S.parent,ucpw.mnuName as controlMnuName, "
                    + "         UPW.MnuModule,UPW.MnuName,UPW.MnuText,UPW.MnuLevel,UPW.ReportFor, "
                    + "         UPW.ReportFor As DisplayModuleName,UPW.permission,    "
                    + "         case when isNull(UPW.VP_Days,0) = 0 then null else DATEADD(day,-(UPW.VP_Days - 1), '" + currentDate + "') end as VP_Date, "
                    + "         case when isNull(UPW.DP_Days,0) = 0 then null else DATEADD(day,-(UPW.DP_Days - 1), '" + currentDate + "') end as DP_Date, "
                    + "         case when isNull(UPW.SP_days,0) = 0 then null else DATEADD(day,-(UPW.SP_days - 1), '" + currentDate + "') end as SP_Date, "
                    + "         UPW.AllowValueUpto,mc.mnuShortHand "
                    + "         From User_permission_web UPW "
                    + "         left join ( "
                    + "             select  UPW_S.mnuModule,UPW_S.parent "
                    + "                     from User_permission_web UPW_S  "
                    + "                     Where UPW_S.isActive = 1 And "
                    + "                     UPW_S.UserName = '" + userName + "' And "
                    + "                     isNull(UPW_S.Comp_Code,-1) In ('" + compCode + "','-1')  "
                    + "                     group by mnuModule,parent "
                    + "         ) as UPW_S on UPW_S.parent = UPW.MnuName and UPW_S.mnuModule = UPW.mnuModule "
                    + "         left join ( "
                    + "             Select  ucpw.mnuModule,ucpw.mnuName "
                    + "                     from user_control_permission_web ucpw "
                    + "                     where ucpw.userName = 'SA' and "
                    + "                     isNull(ucpw.Comp_Code,-1) In ('" + compCode + "','-1') "
                    + "                     group by ucpw.mnuModule,ucpw.mnuName "
                    + "         ) as ucpw on ucpw.mnuName = UPW.MnuName and ucpw.mnuModule = UPW.mnuModule "
                    + "         left join menuCodification mc on mc.mnuModule = UPW.mnuModule and mc.mnuName = UPW.mnuName "
                    + "         Where UPW.isActive = 1 And "
                    + "         UPW.UserName='" + userName + "' And  UPW.mnuModule = '" + moduleName + "' And "
                    + "         isNull(UPW.Comp_Code,-1) In ('" + compCode + "','-1') and UPW.mnuLevel = " + level + " and "
                    + "         isNull(UPW.parent,'') = '" + DmShared.xNull(parentMenu) + "' "
                    + "         order by UPW.sno ";

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                menuList.add(new UserMenu(DmShared.xNull(rsTemp.getString("MnuText")), DmShared.xNull(rsTemp.getString("permission"))));
                menuList.get(menuList.size() - 1).setToolTip(DmShared.xNull(rsTemp.getString("mnuShortHand")));
                menuList.get(menuList.size() - 1).setViewDate(DmShared.dNull(rsTemp.getDate("VP_Date")));
                menuList.get(menuList.size() - 1).setSaveDate(DmShared.dNull(rsTemp.getDate("SP_Date")));
                menuList.get(menuList.size() - 1).setDeleteDate(DmShared.dNull(rsTemp.getDate("DP_Date")));
                menuList.get(menuList.size() - 1).setAllowValueUpto(DmShared.vNull(DmServer.vNull(rsTemp.getString("AllowValueUpto"), "#.#######")));

                if (!DmShared.xNull(rsTemp.getString("controlMnuName")).trim().equalsIgnoreCase("")) {
                    sqlQuery = "Select	ucpw.GroupText "
                            + "From user_control_permission_web ucpw  "
                            + "Where ucpw.UserName = '" + userName + "' And ucpw.Comp_Code In ('" + compCode + "','-1') And "
                            + "ucpw.mnuName = '" + DmShared.xNull(rsTemp.getString("mnuName")) + "' And  ucpw.mnuModule = '" + moduleName + "';";
                    psTemp1 = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    rsTemp1 = psTemp1.executeQuery();
                    while (rsTemp1.next()) {
                        menuList.get(menuList.size() - 1).addControlPermission(DmShared.xNull(rsTemp1.getString("GroupText")));
                    }
                    rsTemp1.close();
                    psTemp1.close();
                }

                if (!DmShared.xNull(rsTemp.getString("parent")).trim().equalsIgnoreCase("")) {
                    getUserMenu(userName, compCode, moduleName, menuList.get(menuList.size() - 1).getUserMenu(), rsTemp.getString("parent"), level + 1, cnTemp);
                }
            }
            rsTemp.close();
            psTemp.close();
        } catch (Exception ex) {
            throw new DmException(0, ex.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    rsTemp.close();
                }
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static String getUserViewCondition(String voucherDateFieldName, String voucherTypefieldName, UserPermission userPermission) {
        StringBuilder conditionSB = new StringBuilder();
        String condition = "";
        List<VoucherPermission> voucherPermissionList;;
        List<UserMenu> userMenuList;;

        if (DmShared.xNull(voucherDateFieldName).trim().equalsIgnoreCase("")) {
            return condition;
        }

        if (DmShared.xNull(voucherTypefieldName).trim().equalsIgnoreCase("")) {
            return condition;
        }
        if (userPermission != null) {
            if (userPermission.getUserMenu() != null) {
                userMenuList = userPermission.getUserMenu().get("");
                if (userMenuList != null) {
                    for (UserMenu userMenu : userMenuList) {
                        if (userMenu != null) {
                            if (!DmShared.dNull(userMenu.getViewDate()).trim().equalsIgnoreCase("")) {
                                if (conditionSB.length() > 0) {
                                    conditionSB.append("or ");
                                }
                                conditionSB.append("(" + voucherDateFieldName + " >= '" + DmDateTimeFormat.getFormat("yyyy-MM-dd").format(DmShared.convertDate(userMenu.getViewDate())) + "') ");
                            }
                        }
                    }
                }
                condition = conditionSB.toString();

                if (!condition.trim().equalsIgnoreCase("")) {
                    condition = " (" + condition + ") ";
                }
            }

            if (DmShared.xNull(condition).trim().equalsIgnoreCase("")) {
                voucherPermissionList = userPermission.getVoucherPermission();
                if (voucherPermissionList != null) {
                    for (int i = 0; i < voucherPermissionList.size(); i++) {
                        if (!DmShared.xNull(voucherPermissionList.get(i).getViewDate()).trim().equalsIgnoreCase("")) {
                            if (conditionSB.length() > 0) {
                                conditionSB.append("or ");
                            }
                            conditionSB.append("(" + voucherTypefieldName + " = '" + DmShared.xNull(String.valueOf(voucherPermissionList.get(i).getVoucherType())) + "' And "
                                    + voucherDateFieldName + " >= '" + DmDateTimeFormat.getFormat("yyyy-MM-dd").format(DmShared.convertDate(voucherPermissionList.get(i).getViewDate())) + "') ");
                        }
                    }

                    condition = conditionSB.toString();

                    if (!condition.trim().equalsIgnoreCase("")) {
                        condition = " (" + condition + ") ";
                    }
                }
            }
        }

        return condition;
    }

    public static Object eval(String formula) throws Exception {
        Object returnValue;
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("js");
            returnValue = engine.eval(formula);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return returnValue;
    }

    public static ManageUser getManageUser() {
        return mu;
    }

    public static void setManageUser(ManageUser_Abstract mua) {
        mu = new ManageUser(mua);
    }

    public static void setDMJasperVariables(DMJasperVariables dmjv) {
        DmServer.dmjv = dmjv;
    }

    public static DMJasperVariables getDMJasperVariables() {
        return dmjv;
    }

    public static String getUserVoucherTypes(UserPermission userPermission) {
        String rtnValue = "";
        if (userPermission != null) {
            if (DmShared.xNull(rtnValue).trim().equalsIgnoreCase("")) {
                if (userPermission.getVoucherPermission() != null) {
                    for (VoucherPermission voucherPermission : userPermission.getVoucherPermission()) {
                        if (!DmShared.xNull(voucherPermission.getVoucherType()).equalsIgnoreCase("")) {
                            if (DmShared.xNull(rtnValue).equalsIgnoreCase("")) {
                                rtnValue = "'" + voucherPermission.getVoucherType() + "'";
                            } else {
                                rtnValue += ", '" + voucherPermission.getVoucherType() + "'";
                            }
                        }
                    }
                }
            }
        }

        return rtnValue;
    }

    public static String getItemCatFilteration(String v_type, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", itemCatCSV = "";
        try {
            sqlQuery = " SELECT icf.itemCategoryCode "
                    + " FROM itemCatFilteration icf "
                    + " WHERE icf.v_Type = :v_type ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("v_type", DmShared.iNull(v_type).toString());
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                if (DmShared.xNull(itemCatCSV).equalsIgnoreCase("")) {
                    itemCatCSV += "'" + DmShared.xNull(rsTemp.getString("itemCategoryCode")) + "'";
                } else {
                    itemCatCSV += ",'" + DmShared.xNull(rsTemp.getString("itemCategoryCode")) + "'";
                }
            }
            rsTemp.close();
            psTemp.close();
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
        return itemCatCSV;
    }

    public static void checkForwordVoucherDateTime(String voucherDate, String voucherTime) throws Exception {
        String[] dateTime;

        try {
            if (DmShared.dNull(voucherDate).trim().equals("")) {
                throw new Exception("Please Input the Date");
            }

            dateTime = DmServer.getServerDateTime();

            if (DmServer.convertDate(dateTime[0]).getTime() < DmServer.convertDate(voucherDate).getTime()) {
                throw new Exception("Forward date entry not allowed.");
            }

            if (!DmShared.xNull(voucherTime).equalsIgnoreCase("") && DmServer.convertDate(dateTime[0]).getTime() == DmServer.convertDate(DmShared.dNull(voucherDate)).getTime()) {
                if (DmShared.vNull(dateTime[1]).doubleValue() < DmShared.vNull(voucherTime).doubleValue()) {
                    throw new Exception("Forward time entry not allowed.");
                }
            }
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {

            } catch (Exception ex) {
            }
        }
    }

    public static void checkBackwordVoucherDateTime(String tableName, String dateField, String timeField, String v_Date, String v_Time, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", lastTransactionDate = "", lastTransactionTime = "";

        try {
            if (DmShared.xNull(tableName).trim().equals("")) {
                throw new Exception("Please Input the Table Name");
            }
            if (DmShared.xNull(dateField).trim().equals("")) {
                throw new Exception("Please Input the Date Field");
            }
            if (DmShared.dNull(v_Date).trim().equals("")) {
                throw new Exception("Please Input the Voucher Date");
            }

            sqlQuery = "SELECT MAX(" + dateField + ") as lastTransactionDate FROM " + tableName + " ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                lastTransactionDate = DmShared.dNull(rsTemp.getDate("lastTransactionDate"));
            }

            if (DmShared.dNull(lastTransactionDate).equalsIgnoreCase("")) {
                return;
            }

            if (DmServer.convertDate(lastTransactionDate).getTime() > DmServer.convertDate(v_Date).getTime()) {
                throw new Exception("Backward date entry not allowed.");
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(timeField).trim().equalsIgnoreCase("") && !DmShared.xNull(v_Time).trim().equalsIgnoreCase("")
                    && DmServer.convertDate(lastTransactionDate).getTime() == DmServer.convertDate(DmShared.dNull(v_Date)).getTime()) {
                sqlQuery = "SELECT MAX(" + timeField + ") as lastTransactionTime "
                        + " FROM " + tableName + " "
                        + " WHERE " + dateField + " = :v_Date ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.setDate(":v_Date", DmServer.convertDate(v_Date));
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    lastTransactionTime = DmServer.vNull(rsTemp.getString("lastTransactionTime"));
                }
                if (DmShared.vNull(lastTransactionTime).doubleValue() > DmShared.vNull(v_Time).doubleValue()) {
                    throw new Exception("Backword time entry not allowed.");
                }
                rsTemp.close();
                psTemp.close();
            }

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    //===============================================Log Management=====================================
    public static String getLocalIPAddresses(DmRSS rssTemp) {
        String returnIPAddresses, localIPAddress, reqIPAddress;
        String[] localIPAddresses;
        boolean isLocal_Request_IPSame = false;

        localIPAddress = DmShared.xNull(getSsnLocalIPAddress(rssTemp));
        reqIPAddress = DmShared.xNull(getClientIpAddress(rssTemp));

        localIPAddresses = localIPAddress.split(",");
        for (String localIPAdd : localIPAddresses) {
            if (DmShared.xNull(localIPAdd).trim().equalsIgnoreCase(DmShared.xNull(reqIPAddress).trim())) {
                isLocal_Request_IPSame = true;
                break;
            }
        }

        returnIPAddresses = localIPAddress;
        if (!isLocal_Request_IPSame) {
            if (!DmShared.xNull(returnIPAddresses).trim().equalsIgnoreCase("")) {
                returnIPAddresses += ", ";
            }
            returnIPAddresses += reqIPAddress;
        }

        return returnIPAddresses;
    }

    public static String getClientIpAddress(DmRSS rssTemp) {
        String xForwardedForHeader = rssTemp.getLocalRequest().getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return rssTemp.getLocalRequest().getRemoteAddr();
        } else {
            // As of https://en.wikipedia.org/wiki/X-Forwarded-For
            // The general format of the field is: X-Forwarded-For: client, proxy1, proxy2 ...
            // we only want the client
            return new StringTokenizer(xForwardedForHeader, ",").nextToken().trim();
        }
    }

    public static void setSsnOldData(String keyName, Object keyValue, DmRSS rssTemp) throws Exception {
        Hashtable<String, Object> htOldData;

        try {
            if (keyName == null || keyName.isEmpty()) {
                throw new DmException(0, "Key Name is mandatory.");
            }
            htOldData = (Hashtable) DmSession.getSessions().get(DmServer.getSsnId(rssTemp)).getAttribute("OldData");
            if (htOldData == null) {
                htOldData = new Hashtable<>();
                rssTemp.getLocalRequest().getSession().setAttribute("OldData", htOldData);
            }

            htOldData.put(keyName, keyValue);

        } catch (Exception e) {
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        }
    }

    public static Object getSsnOldData(String keyName, DmRSS rssTemp) throws Exception {
        Object returnValue = null;
        try {
            if (keyName == null || keyName.isEmpty()) {
                throw new DmException(0, "Key Name is mandatory.");
            }
            Hashtable<String, Object> htOldData = (Hashtable) DmSession.getSessions().get(DmServer.getSsnId(rssTemp)).getAttribute("OldData");
            if (htOldData != null) {
                returnValue = htOldData.get(keyName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DmException(0, e.getMessage());
        }
        return returnValue;
    }

    public static void checkLockDate(String siteCode, String voucherPrefix, Connection cnTemp) {

        String sqlQuery;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String[] lockVoucherPrefix = new String[50];
        try {

            //=========================
            sqlQuery = " select lockVoucherPrefix from enviro where site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                lockVoucherPrefix = DmShared.xNull(rsTemp.getString("lockVoucherPrefix")).split(",");

            }
            rsTemp.close();
            psTemp.close();

            //==========================
            sqlQuery = " select lockDate from enviro where site_Code = :siteCode";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {

                if (rsTemp.getDate("lockDate") != null && !lockVoucherPrefix[0].equalsIgnoreCase("")) {

                    if (DmShared.convertDate(DmShared.dNull(DmServer.getServerDateTime()[0])).getTime() >= DmShared.convertDate(DmShared.dNull(rsTemp.getDate("lockDate"))).getTime()) {

                        for (int i = 0; i < lockVoucherPrefix.length; i++) {
                            if (DmShared.xNull(lockVoucherPrefix[i]).equalsIgnoreCase(voucherPrefix)) {
                                throw new DmException(0, "Transactions are Locked.");
                            }
                        }

                    }
                }
            }
            rsTemp.close();
            psTemp.close();

        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
                if (rsTemp != null) {
                    rsTemp.close();
                }

            } catch (Exception e) {
            }
        }

    }

    public static String getUserWithDateTime(String userName, Timestamp entryDateTime) {
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

    //<editor-fold defaultstate="collapsed" desc="validateAPB_State">
    public static boolean validateAPB_State(String subCode, String sno, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String strSql = "", condition = "";
        boolean returnValue = false;
        try {
            if (DmShared.xNull(subCode).trim().equalsIgnoreCase("")) {
                throw new DmException(0, "Party is required to validate additional places business.");
            }

            condition += "AND apb.subCode = :subCode \n";

            if (!DmShared.xNull(sno).trim().equalsIgnoreCase("")) {
                condition += "AND apb.sno = :sno \n";
            }

            strSql = "SELECT count(*) AS cnt \n"
                    + "FROM additionalPlaceOfBusiness apb \n"
                    + "LEFT JOIN city c ON c.cityCode = apb.cityCode \n"
                    + "LEFT JOIN subGroup sg ON sg.subCode = apb.subCode \n"
                    + "LEFT JOIN city ct ON ct.cityCode = sg.cityCode \n"
                    + "WHERE c.stateCode <> ct.stateCode \n" + condition;
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSql);
            psTemp.setString("subCode", DmShared.xNull(subCode));
            if (!DmShared.xNull(sno).trim().equalsIgnoreCase("")) {
                psTemp.setString("sno", DmShared.xNull(sno));
            }

            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.iNull(rsTemp.getString("cnt")).intValue() > 0) {
                    throw new DmException(0, "State defined for additional place of business is mis-matching with party primary state.");
                }
            }
            rsTemp.close();
            psTemp.close();

            returnValue = true;
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
        return returnValue;
    }
//</editor-fold>

    public static void checkCurrentSiteRecord(String tableName, String siteCodeFieldName, String docId, String docIdFieldName, Connection cnTemp, DmRSS rssTemp, String action) throws Exception {
        try {
            checkCurrentSiteRecord(tableName, siteCodeFieldName, docId, docIdFieldName, cnTemp, getSsnSiteCode(rssTemp), action);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static void checkCurrentSiteRecord(String tableName, String siteCodeFieldName, String docId, String docIdFieldName, Connection cnTemp, String sessionSiteCode, String action) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, siteCode = "";

        try {
            if (DmShared.xNull(action).trim().equalsIgnoreCase("")) {
                action = "edit / delete";
            }

            sqlQuery = "select " + siteCodeFieldName + " "
                    + "from " + tableName + " "
                    + "where " + docIdFieldName + " = :docId ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("docId", docId);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                siteCode = DmShared.xNull(rsTemp.getString(1));
            }
            rsTemp.close();
            psTemp.close();

            if (!DmShared.xNull(siteCode).equalsIgnoreCase(DmShared.xNull(sessionSiteCode))) {
                throw new Exception("Record belongs to other site.<br>This cannot be " + action + " in current site.");
            }
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
    }

    public static void chkIfDocumentUsed(String fieldName, String tableName, String docId, Connection cnTemp) throws Exception {
        try {
            chkIfDocumentUsed(fieldName, tableName, docId, "", cnTemp);
        } catch (Exception e) {
            throw new DmException(0, e.getMessage());
        }
    }

    public static void chkIfDocumentUsed(String fieldName, String tableName, String docId, String action, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        String strSQL = "";
        ResultSet rsTemp = null;

        try {
            if (DmShared.xNull(action).trim().equalsIgnoreCase("")) {
                action = "modify / delete";
            }

            strSQL = " Select count(tb." + fieldName.trim() + ") as cnt from " + tableName.trim() + " as tb  "
                    + " where tb." + fieldName.trim() + " = :docId  ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, strSQL);
            psTemp.setString("docId", DmShared.iNull(docId).toString());
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                if (DmShared.vNull(rsTemp.getString("cnt")).doubleValue() > 0) {
                    throw new DmException(0, "Document has been used. You can't " + action + " this record.");
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
    }

}
