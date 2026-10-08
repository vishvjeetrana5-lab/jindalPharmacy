package dataman.dmBase.dmBaseStatic.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ibm.icu.util.ULocale;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import dataman.dmBase.dmBaseStatic.shared.DmBaseShared;
import dataman.dmBase.dmDynamicReportBuilder.server.RptFind;
import dataman.dmBase.dmFileManagerMongo.server.DmFileManagerMongo;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmScheduler.server.DmSchedulerFactory;
import dataman.dmBase.dmScheduler.shared.SchedulerMast_Mdl;
import dataman.dmBase.dmSession.server.DmRSS;
import dataman.dmBase.dmSession.server.DmRequestWrapper;
import dataman.dmBase.dmSession.server.DmSession;
import dataman.dmBase.dmSession.server.IAppServiceResponseHandler;
import dataman.dmBase.self.server.*;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.xls.server.DmPrintFindExcel;
import dataman.dmBase.self.xls.server.DmXlsSheet;
import dmBuild.DmBuild;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.PageOrientation;
import jxl.format.PaperSize;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.type.PdfVersionEnum;

public class DmBaseServer {

    public static abstract class Path {

        public static String repository, sessions, logs, resources, reports, images;
        public static String pacSetup, fonts, installer, pac;
        public static String webJobs, csvTemplates_SMS_eMail;
    }

    public static String dbCompany, mongoDBServer, mongoDBPort, pacPortWeb, localeCountry, majorCurrency, minorCurrency;
    protected static String sqlPassword, sqlUser;
    public static long sessionTimeOut, gcTimeInterval, sessionLockTimeOut;
    public static final String uriPathExtensionCORS = ".cors";
    private static String codifyAlgorithm = "AES";
    private static String codifyKey1 = "~!≡s5*.d", codifyKey2 = "T←mÄn╤sY";
    private static Gson gson = null;
    private static MongoClient mongoClient;
    private static BoneCP boneCP;
    public static ULocale baseULocale;
    public static DmEnum.DmLocaleCountry baseDmLocaleCountry;
//    public static String baseLocaleCountry, baseLocaleLanguage = "en";
    private static DmSchedulerFactory webServiceSchedulars = new DmSchedulerFactory();
    private static Hashtable<String, IAppServiceResponseHandler> applicationServiceExtensions = new Hashtable<>();

    public static void registerApplicationServiceExtension(String applicationServiceExtension, IAppServiceResponseHandler serviceResponseHandler) throws Exception {
        String serviceExtension = DmBaseShared.xNull(applicationServiceExtension).trim();
        if (serviceExtension.equals("")) {
            throw new Exception("Application Service Extension can't be left empty or null.");
        }
        if (serviceResponseHandler == null) {
            throw new Exception("Application Service response handler can't be left emptry or null.");
        }

        if (!applicationServiceExtensions.containsKey(serviceExtension.toLowerCase())) {
            applicationServiceExtensions.put(serviceExtension.toLowerCase(), serviceResponseHandler);
        }

    }

    public static IAppServiceResponseHandler getApplicationServiceHandler(String applicationServiceExtension) {
        IAppServiceResponseHandler responseHandler = null;
        try {
            String serviceExtension = DmBaseShared.xNull(applicationServiceExtension).trim();
            if (applicationServiceExtensions.containsKey(applicationServiceExtension.toLowerCase())) {
                responseHandler = applicationServiceExtensions.get(serviceExtension.toLowerCase());
            }
        } catch (Exception ex) {
            responseHandler = null;
            ex.printStackTrace();
        }
        return responseHandler;
    }

    public static boolean compareDBPassword(String password) {
        if (password.equalsIgnoreCase(sqlPassword)) {
            return true;
        } else {
            return false;
        }
    }

    public static void iniBoneCP(String sqlHostName, String sqlPort) throws Exception {
        BoneCPConfig bcConfig;
        SQLServerDriver dvrTemp = new SQLServerDriver();
        DriverManager.registerDriver(dvrTemp);
        if (!sqlPort.trim().equals("")) {
            sqlPort = ":" + sqlPort;
        } else {
            sqlPort = "";
        }

        bcConfig = new BoneCPConfig();
        bcConfig.setJdbcUrl("jdbc:sqlserver://" + sqlHostName + sqlPort + ";sendStringParametersAsUnicode=false");
        bcConfig.setUsername(sqlUser);
        bcConfig.setPassword(sqlPassword);

        bcConfig.setCloseOpenStatements(true);
//        bcConfig.setMaxConnectionsPerPartition(300);
        bcConfig.setMaxConnectionsPerPartition(100);
        bcConfig.setPartitionCount(1);
        //Only for debug mode - open
        bcConfig.setCloseConnectionWatch(true);
        bcConfig.setCloseConnectionWatchTimeoutInMs(60000);
        //Only for debug mode - close
        boneCP = new BoneCP(bcConfig);
        System.out.println("new BoneCP(bcConfig)");
    }

    public static void iniMongo() throws Exception {
        mongoClient = new MongoClient(mongoDBServer + ":" + mongoDBPort);
        System.out.println("new MongoClient");
    }

    public static DmSchedulerFactory getWebServiceSchedulars() {
        return DmBaseServer.webServiceSchedulars;
    }

    public static void addScheduler(String schedulerId) {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery;

        try {
            cnTemp = DmBaseServer.getConnection(DmBaseServer.dbCompany);

//            sqlQuery = "SELECT	TEMP.*,US.EMAILID AS US$EMAILID,US.password AS US$PASSWD,US.BCC AS US$BCC,US.CC AS US$CC "
//                    + "         FROM ( "
//                    + "                 SELECT	SCHMAST.*, "
//                    + "                         PLT.COMP_NAME AS PLT$COMP_NAME, PLT.SNAME AS PLT$SNAME, PLT.LATESTCOMPCODE AS PLT$LATESTCOMPCODE, "
//                    + "                         C.COMP_NAME AS COMP$COMP_NAME, C.CENTRALDATA_PATH AS COMP$CENTRALDATA_PATH, C.centralFileData_Path AS COMP$CENTRALFILEDATA_PATH, "
//                    + "                         C.COMP_CODE AS COMP$COMP_CODE, C.V_PREFIX AS COMP$VOUCHERPREFIX  "
//                    + "                         FROM DBO.SCHEDULERMAST SCHMAST,DBO.PRODUCTLICENSEDTO PLT,DBO.COMPANY C "
//                    + "                         WHERE (SCHMAST.PLTCODE=PLT.PLTCODE) AND (PLT.LATESTCOMPCODE = C.COMP_CODE) "
//                    + "         ) AS TEMP  "
//                    + "         LEFT JOIN DBO.userPLTConfig US ON (TEMP.responsibleUser=US.USERNAME) AND (TEMP.PLTCODE=US.pltCode)  "
//                    //                + "WHERE (TEMP.isActive=:isActive) ";
//                    + "         WHERE (1 = 1) ";
            sqlQuery = "SELECT	TEMP.*,US.EMAILID AS US$EMAILID,US.password AS US$PASSWD,US.BCC AS US$BCC,US.CC AS US$CC "
                    + "         FROM ( "
                    + "                 SELECT	SCHMAST.*, "
                    + "                         PLT.COMP_NAME AS PLT$COMP_NAME, PLT.SNAME AS PLT$SNAME, PLT.LATESTCOMPCODE AS PLT$LATESTCOMPCODE, "
                    + "                         C.CENTRALDATA_PATH AS COMP$CENTRALDATA_PATH, C.centralFileData_Path AS COMP$CENTRALFILEDATA_PATH, "
                    + "                         C.COMP_CODE AS COMP$COMP_CODE, C.V_PREFIX AS COMP$VOUCHERPREFIX  "
                    + "                         FROM DBO.SCHEDULERMAST SCHMAST,DBO.PRODUCTLICENSEDTO PLT,DBO.COMPANY C "
                    + "                         WHERE (SCHMAST.PLTCODE=PLT.PLTCODE) AND (PLT.LATESTCOMPCODE = C.COMP_CODE) "
                    + "         ) AS TEMP  "
                    + "         LEFT JOIN DBO.userPLTConfig US ON (TEMP.responsibleUser=US.USERNAME) AND (TEMP.PLTCODE=US.pltCode)  "
                    //                + "WHERE (TEMP.isActive=:isActive) ";
                    + "         WHERE (1 = 1) ";

            if (DmBaseShared.iNull(schedulerId).longValue() != 0) {
                sqlQuery += "AND (TEMP.DOCID=:docId) ";
            }
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            if (DmBaseShared.iNull(schedulerId).longValue() != 0) {
                psTemp.setString("docId", schedulerId);
            }
//        psTemp.setInt("isActive", DmEnum.DmYesNo.YES.toInt());
            rsTemp = psTemp.executeQuery();
            addScheduler(rsTemp);
            rsTemp.close();
            psTemp.close();
            DmBaseServer.getWebServiceSchedulars().startWatching();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp = null;
                }
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

    public static void setCountryWiseSetting() {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "";

        try {
            cnTemp = DmBaseServer.getConnection(DmBaseServer.dbCompany);

            sqlQuery = "select top(1) majorCurrency,minorCurrency from company";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                majorCurrency = DmBaseShared.xNull(rsTemp.getString("majorCurrency")).toString();
                minorCurrency = DmBaseShared.xNull(rsTemp.getString("minorCurrency")).toString();
            }
            rsTemp.close();
            psTemp.close();
            cnTemp.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsTemp != null) {
                    rsTemp.close();
                    rsTemp = null;
                }
                if (psTemp != null) {
                    psTemp.close();
                    psTemp = null;
                }
                if (cnTemp != null && !cnTemp.isClosed()) {
                    cnTemp.close();
                    cnTemp = null;
                }
            } catch (Exception ex) {
            }
        }
    }

    private static void addScheduler(ResultSet rsTemp) throws Exception {
        java.util.Date dtNow = new java.util.Date();
        SchedulerMast_Mdl schedularModel = null;

        while (rsTemp.next()) {
            schedularModel = new SchedulerMast_Mdl();
            schedularModel.setDocId(DmBaseShared.iNull(rsTemp.getLong("docId")).longValue());
            schedularModel.setActive(DmBaseShared.iNull(rsTemp.getString("isActive")).intValue() == 1);
            schedularModel.setStartDateTime(rsTemp.getTimestamp("startDateTime"));
            schedularModel.setExpUsed(rsTemp.getTimestamp("expDateTime") == null ? false : true);
            schedularModel.setExpDateTime(rsTemp.getTimestamp("expDateTime"));
            schedularModel.setName(DmBaseShared.xNull(rsTemp.getString("name")));
            schedularModel.setActiveDB(DmBaseShared.xNull(rsTemp.getString("COMP$CENTRALDATA_PATH")));
            schedularModel.setActiveFileDbName(DmBaseShared.xNull(rsTemp.getString("COMP$CENTRALFILEDATA_PATH")));
            schedularModel.setCompanyCode(DmBaseShared.xNull(rsTemp.getString("COMP$COMP_CODE")));
            schedularModel.setVoucherPrefix(DmBaseShared.xNull(rsTemp.getString("COMP$VOUCHERPREFIX")));
            PLT plt = new PLT();
            plt.setPLTCode(DmBaseShared.xNull(rsTemp.getString("PLTCode")));
            plt.setPLTName(DmBaseShared.xNull(rsTemp.getString("PLT$SNAME")));
            schedularModel.setPlt(plt);
//            schedularModel.setScheduleExecutionType(DmEnum.DmScheduleExecutionType.getValueFrom(DmShared.iNull(rsTemp.getInt("nature")).intValue()));
            schedularModel.setWebJobTemplateId(DmBaseShared.iNull(rsTemp.getLong("WSTCode")).longValue());
            schedularModel.setUserEntryDate(rsTemp.getTimestamp("u_EntDate"));
            schedularModel.setUserName(DmBaseShared.xNull(rsTemp.getString("u_Name")));
            schedularModel.setEmailSenderUserName(DmBaseShared.xNull(rsTemp.getString("responsibleUser")));
            schedularModel.setEmailSenderPassword(DmBaseShared.xNull(rsTemp.getString("US$PASSWD")));
            schedularModel.setEmailId(DmBaseShared.xNull(rsTemp.getString("US$EMAILID")));
            schedularModel.setEmailBcc(DmBaseShared.xNull(rsTemp.getString("US$BCC")));
            schedularModel.setEmailCc(DmBaseShared.xNull(rsTemp.getString("US$CC")));

            schedularModel.setSchType(DmEnum.DmSchedularType.getValueFrom(DmBaseShared.iNull(rsTemp.getInt("schType")).intValue()));
            schedularModel.setWebJobType(DmEnum.DmWebJobTypes.getValueFrom(DmBaseShared.iNull(rsTemp.getInt("jobType")).intValue()));
//            schedularModel.setLastExecutionTime(rsTemp.getTimestamp("lastExecTime"));

            schedularModel.setSelSites(DmBaseShared.xNull(rsTemp.getString("selSiteCodes")));

            if (schedularModel.getSchType() != DmEnum.DmSchedularType.ONCE) {
                schedularModel.setRecurQuan(DmBaseShared.iNull(rsTemp.getInt("recurQuantity")).intValue());
                schedularModel.setRecurUnit(DmEnum.DmRecurrenceUnit.getValueFrom(DmBaseShared.iNull(rsTemp.getInt("recurUnit")).intValue()));

                if (schedularModel.getSchType() == DmEnum.DmSchedularType.WEEKLY) {
                    schedularModel.setSelDays(DmBaseShared.xNull(rsTemp.getString("selDays")));

                } else if (schedularModel.getSchType() == DmEnum.DmSchedularType.MONTHLY) {
                    schedularModel.setDayOrMonth(DmBaseShared.xNull(rsTemp.getString("dayOrMonth")));
                    schedularModel.setSelMonths(DmBaseShared.xNull(rsTemp.getString("selMonths")));

                    if (schedularModel.getDayOrMonth().equalsIgnoreCase("d")) {
                        schedularModel.setSelDates(DmBaseShared.xNull(rsTemp.getString("selDates")));

                    } else if (schedularModel.getDayOrMonth().equalsIgnoreCase("m")) {
                        schedularModel.setSelDays(DmBaseShared.xNull(rsTemp.getString("selDays")));
                        schedularModel.setSelWeeks(DmBaseShared.xNull(rsTemp.getString("selWeeks")));

                    }
                }
            }

            DmBaseServer.getWebServiceSchedulars().schedule(schedularModel, dtNow);
        }
    }

    public static Connection getConnection(String databaseName) {
        Connection cnDB;

        try {
            System.err.println(
                    " getTotalCreatedConnections: " + boneCP.getTotalCreatedConnections()
                    + ", getTotalFree: " + boneCP.getTotalFree()
                    + ", getTotalLeased: " + boneCP.getTotalLeased()
                    + ", getStatementsCached: " + boneCP.getStatistics().getStatementsCached());
            cnDB = boneCP.getConnection();
            cnDB.setCatalog(databaseName);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return cnDB;
    }

    public static DmFileManagerMongo getConnectionMongo(String databaseName, boolean createIfNotExists) throws Exception {
        DmFileManagerMongo fileMongo = null;

        try {
            if (mongoClient == null) {
                throw new Exception("mongo client is not defined.");
            }

            if (createIfNotExists) {
                fileMongo = new DmFileManagerMongo(mongoClient.getDB(databaseName));
            } else if (!createIfNotExists) {
                List<String> dbs = mongoClient.getDatabaseNames();
                for (int i = 0; i < dbs.size(); i++) {
                    if (dbs.get(i).equalsIgnoreCase(databaseName)) {
                        fileMongo = new DmFileManagerMongo(mongoClient.getDB(databaseName));
                    }
                }
            }

        } catch (Exception ex) {
            throw ex;
        }
        return fileMongo;
    }

    public static DmFileManagerMongo getConnectionMongo(String databaseName) throws Exception {
        return getConnectionMongo(databaseName, false);
    }

    private static void createFontsInitalization(String fontXmlFilePath, File directoryToScan) throws Exception {
//        System.out.println("Directory to scan fonts : " + directoryToScan.getAbsolutePath() + ", fontXMLFile Path : " + fontXmlFilePath);
        System.out.println(fontXmlFilePath);
        List<File> lstFontFiles = scanFonts(directoryToScan);
//        System.out.println("List of Scanned fonts : " + lstFontFiles);
        Map<String, List<DmFontFace>> fontFamilies = visitFontFamilies(lstFontFiles);
//        System.out.println("Font families discovered...: " + fontFamilies);
        createFontXml(fontXmlFilePath, fontFamilies);
        fontFamilies.clear();
        lstFontFiles.clear();
        fontFamilies = null;
        lstFontFiles = null;
//        System.out.println("Font xml file has been created...");
    }

    private static Map<String, List<DmFontFace>> visitFontFamilies(List<File> lstFontFiles) throws Exception {
        Map<String, List<DmFontFace>> fontFamilies = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < lstFontFiles.size(); i++) {
            DmFontFace fontFace = new DmFontFace(lstFontFiles.get(i));
            if (fontFamilies.containsKey(fontFace.getFontFamilyName())) {
                if (!fontFamilies.get(fontFace.getFontFamilyName()).contains(fontFace)) {
                    fontFamilies.get(fontFace.getFontFamilyName()).add(fontFace);
                }
            } else if (!fontFamilies.containsKey(fontFace.getFontFamilyName())) {
                List<DmFontFace> lstFonts = new ArrayList<>();
                lstFonts.add(fontFace);
                fontFamilies.put(fontFace.getFontFamilyName(), lstFonts);
            }
        }
        return fontFamilies;
    }

    private static List<File> scanFonts(File parentDirectory) throws Exception {
        List<File> fontFiles = new ArrayList<>();
        String[] lstFiles = parentDirectory.list();
        for (int i = 0; i < lstFiles.length; i++) {
            String fileName = lstFiles[i].trim();
            File tempFile = new File(parentDirectory, fileName);
            if (tempFile.isDirectory()) {
                List<File> tempFilesCollection = scanFonts(tempFile);
                fontFiles.addAll(tempFilesCollection);
            } else if (fileName.toLowerCase().endsWith(".ttf")) {
                fontFiles.add(tempFile);

            }
        }
        return fontFiles;
    }

    private static void createFontXml(String fontXmlFilePath, Map<String, List<DmFontFace>> fontFamilies) throws Exception {
        File tempFile = new File(fontXmlFilePath);
        StringBuilder buffer = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        buffer.append("<fontFamilies>\n\n");
        Iterator<String> families = fontFamilies.keySet().iterator();
        while (families.hasNext()) {
            String familyName = families.next();
            List<DmFontFace> lstFonts = fontFamilies.get(familyName);
            if ((lstFonts != null) && (lstFonts.size() > 0)) {
                buffer.append("   <fontFamily name=\"" + familyName.toLowerCase() + "\">\n");
                for (int i = 0; i < lstFonts.size(); i++) {
                    DmFontFace fontFace = lstFonts.get(i);
                    if (fontFace.isBold()) {
                        buffer.append("       <bold><![CDATA[" + fontFace.getFontFile().getAbsolutePath() + "]]></bold>\n");
                    } else if (fontFace.isItalic()) {
                        buffer.append("       <italic><![CDATA[" + fontFace.getFontFile().getAbsolutePath() + "]]></italic>\n");
                    } else {
                        buffer.append("       <normal><![CDATA[" + fontFace.getFontFile().getAbsolutePath() + "]]></normal>\n");
                    }
                }
                buffer.append("       <pdfEmbedded><![CDATA[false]]></pdfEmbedded>\n");
                buffer.append("   </fontFamily>\n\n\n");
            }
        }
        buffer.append("</fontFamilies>");

        FileWriter writer = new FileWriter(tempFile);
        writer.write(buffer.toString());
        writer.close();
        writer = null;
    }

    public static JasperReport compileJasper(String reportName, DmJasperStyle... styleList) throws Exception {
        return compileJasper(reportName, "", false, styleList).getReport();
    }

    public static DmIJasperCompiled compileJasper(String reportName, String subDirectory, boolean withHierarchy, DmJasperStyle... styleList) throws Exception {
        String reportPath = "";
        DmJasperCompiler jasperReport = null;
        File file = null;

        if (DmBaseShared.xNull(subDirectory).equalsIgnoreCase("")) {
            subDirectory = "system";
        }

        if (subDirectory.equalsIgnoreCase("system")) {
            reportPath = Path.reports + "/" + "systemCustomized" + "/" + reportName.trim() + ".jrxml";
            file = new File(reportPath);
            if (!file.exists()) {
                reportPath = Path.reports + "/" + subDirectory + "/" + reportName.trim() + ".jrxml";
                file = new File(reportPath);
            }
        } else {
            reportPath = Path.reports + "/" + subDirectory + "/" + reportName.trim() + ".jrxml";
            file = new File(reportPath);
        }

        jasperReport = new DmJasperCompiler(file, withHierarchy, styleList);

        jasperReport.compileReport();

        return jasperReport;
    }

    public static JasperPrint setDmCustomJasperProperties(JasperReport jasperReport, JasperPrint jasperPrint) throws Exception {
        String[] propertyNames = jasperReport.getPropertiesMap().getPropertyNames();
        for (int i = 0; i < propertyNames.length; i++) {
            String propertyName = DmBaseShared.xNull(propertyNames[i]).trim();
            if (propertyName.startsWith("dm")) {
                String propertyValue = DmBaseShared.xNull(jasperReport.getPropertiesMap().getProperty(propertyName)).trim();
                jasperPrint.getPropertiesMap().setProperty(propertyName, propertyValue);
            }
        }
        return jasperPrint;
    }

    public static JasperPrint fillJasper(String reportTitle, JasperReport jasperReport, HashMap parameter, LinkedHashMap<String, FieldUDT_Mdl> filter, ResultSet resultSet) throws Exception {
        JasperPrint jasperPrint = null;
        JRResultSetDataSource jrResultSetDataSource;

        if (resultSet.isBeforeFirst()) {
            parameter = setJasperVariables(reportTitle, jasperReport, filter, parameter);
            jrResultSetDataSource = new JRResultSetDataSource(resultSet);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, jrResultSetDataSource);
            setDmCustomJasperProperties(jasperReport, jasperPrint);
        } else {
            throw new UnsupportedOperationException("No record found!");
        }

        return jasperPrint;
    }

    public static <T> JasperPrint fillJasper(String reportTitle, JasperReport jasperReport, HashMap parameter, LinkedHashMap<String, FieldUDT_Mdl> filter, List<T> listOfT) throws Exception {
        JasperPrint jasperPrint = null;
        JRBeanCollectionDataSource jrBeanCollectionDataSource;

        if (listOfT != null
                && listOfT.size() > 0
                && (!listOfT.isEmpty())) {
            parameter = setJasperVariables(reportTitle, jasperReport, filter, parameter);
            jrBeanCollectionDataSource = new JRBeanCollectionDataSource(listOfT, false);
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, jrBeanCollectionDataSource);
            setDmCustomJasperProperties(jasperReport, jasperPrint);
        } else {
            throw new UnsupportedOperationException("No record found!");
        }

        return jasperPrint;
    }

    public static HashMap setJasperVariables(String reportTitle, JasperReport jasperReport, LinkedHashMap<String, FieldUDT_Mdl> filter, HashMap parameter) throws Exception {
        JRParameter jrParameter[];
        String tempString;
        Object tempObject;

        if (parameter == null) {
            parameter = new HashMap();
        }

        jrParameter = jasperReport.getParameters();
        for (int i = 0; i < jrParameter.length; i++) {
            if (jrParameter[i].getName().equalsIgnoreCase("dmCurrentDateTime")) {
                parameter.put(jrParameter[i].getName(), DmDateTimeFormat.getFormat("dd-MMM-yyyy HH:mm").format(new java.util.Date()));
            } else if (jrParameter[i].getName().equalsIgnoreCase("SUBREPORT_DIR")) {
                tempObject = parameter.get("SUBREPORT_DIR");
                tempString = "";
                if (tempObject != null) {
                    tempString = tempObject.toString();
                }

                if (tempString.trim().equalsIgnoreCase("osr")) {
                    parameter.put(jrParameter[i].getName(), Path.reports + "/osr/");

                } else {
                    parameter.put(jrParameter[i].getName(), Path.reports + "/system/");

                }
            } else if (jrParameter[i].getName().equalsIgnoreCase("dmFrmRptFormulaFieldDisplay")) {
                tempString = "";
//                if (filter != null) {
//                    Set<String> keySet = filter.keySet();
//                    if (keySet != null) {
//                        for (String key : keySet) {
//                            Filter_Mdl filter_Mdl = filter.get(key);
//                            if (filter_Mdl != null) {
//                                if (!DmBaseShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
//                                    if (!tempString.equalsIgnoreCase("")) {
//                                        tempString += "\n";
//                                    }
//                                    tempString += DmBaseShared.xNull(filter_Mdl.getFieldCaption()) + ": " + DmBaseShared.xNull(filter_Mdl.getText());
//                                }
//                            }
//                        }
//                    }
//
//                }

                if (filter != null) {
                    Set<String> keySet = filter.keySet();
                    if (keySet != null) {
                        for (String key : keySet) {
                            FieldUDT_Mdl filter_Mdl = filter.get(key);
                            if (filter_Mdl != null) {
                                if (!DmBaseShared.xNull(filter_Mdl.getFieldCaption()).equalsIgnoreCase("")) {
//                                    if (!tempString.equalsIgnoreCase("")) {
//                                        tempString += "\n";
//                                    }
                                    tempString += "<b><u>" + DmBaseShared.xNull(filter_Mdl.getFieldCaption()) + ":</b></u> " + DmBaseShared.xNull(filter_Mdl.getText()) + "; ";
//                                    tempString += DmBaseShared.xNull(filter_Mdl.getFieldCaption()) + ":<b> " + DmBaseShared.xNull(filter_Mdl.getText()) + ";</b> ";
                                }
                            }
                        }
                    }

                }
                parameter.put(jrParameter[i].getName(), tempString);
            }
        }

        return parameter;
    }

    public static String showJasper(String filePath, String fileName, JasperPrint jasperPrint, String suffix, DmReportProperty_Mdl reportProperty) throws Exception {

        try {
            filePath = filePath + "/" + fileName;
            if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
                JRXlsExporter xlsExporter = new JRXlsExporter();
                xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
                xlsExporter.exportReport();

            } else if (reportProperty.getExportType() == DmEnum.DmExportType.WORD) {
                JRDocxExporter docXExporter = new JRDocxExporter();
                docXExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                docXExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
                docXExporter.exportReport();

            } else {
                JRPdfExporter pdfExporter = new JRPdfExporter();
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(filePath));
                SimplePdfExporterConfiguration pdfConfig = new SimplePdfExporterConfiguration();
                pdfConfig.setCompressed(Boolean.TRUE);
                pdfConfig.setPdfVersion(PdfVersionEnum.VERSION_1_7);
                pdfConfig.setDisplayMetadataTitle(Boolean.TRUE);
                pdfConfig.setMetadataTitle(fileName);
                pdfExporter.setConfiguration(pdfConfig);
                pdfExporter.exportReport();
            }
        } catch (Exception e) {
            fileName = "";
            throw new UnsupportedOperationException(e.getMessage());
        }
        return fileName;
    }

    public static SQLServerDataTable getFieldUDT(HashMap<String, FieldUDT_Mdl> fieldUDTMap) throws Exception {
        SQLServerDataTable table = null;
        Set<String> set;
        FieldUDT_Mdl fieldUDT;

        table = new SQLServerDataTable();
        table.addColumnMetadata("fieldCode", java.sql.Types.VARCHAR);
        table.addColumnMetadata("fieldName", java.sql.Types.VARCHAR);
        table.addColumnMetadata("fieldText", java.sql.Types.VARCHAR);
        table.addColumnMetadata("fieldTag", java.sql.Types.VARCHAR);

        if (fieldUDTMap != null) {
            set = fieldUDTMap.keySet();
            for (String s : set) {
                if (!DmBaseShared.xNull(s).trim().equalsIgnoreCase("")) {
                    fieldUDT = fieldUDTMap.get(s);
                    table.addRow(s, fieldUDT.getFieldCaption(), fieldUDT.getText(), fieldUDT.getTag());
                }
            }
        }

        return table;
    }

    public static double mntsToHrs(int value) {
        int hours;
        double minutes, rtnHours;

        hours = DmBaseShared.iNull(value / 60).intValue();
        minutes = DmBaseShared.vNull(vNull((value % 60), "0.00")).doubleValue() / 100;

        rtnHours = hours + minutes;

        return rtnHours;
    }

    public static int hrsToMnts(double value) {
        int minutes;

        minutes = DmBaseShared.iNull((60 * DmBaseShared.iNull(value).intValue()) + ((DmBaseShared.vNull(vNull(value - DmBaseShared.iNull(value).intValue(), "0.00"))) * 100)).intValue();

        return minutes;
    }

    public static <T> String objectToBase64(T model) throws Exception {
        ObjectOutputStream oos = null;
        String rValue = "";
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(model);
            rValue = new String(Base64.getEncoder().encode(bos.toByteArray()), "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DmException(0, ex.getMessage());
        } finally {
            if (bos != null) {
                bos.close();
                bos = null;
            }
            if (oos != null) {
                oos.close();
                oos = null;
            }
        }
        return rValue;
    }

    public static <T> T base64ToObject(String msg) throws Exception {
        T model = null;
        ObjectInputStream ois = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(msg);
            ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            model = (T) ois.readObject();
        } finally {
            if (ois != null) {
                ois.close();
                ois = null;
            }
        }
        return model;
    }

    public static <T> String convertToJson(T objectToConvert, TypeToken<T> typeToken) throws Exception {
        initializeGson();
        return gson.toJson(objectToConvert, typeToken.getType());
    }

    public static <T> T convertFromJson(String jsonToConvert, TypeToken<T> typeToken) throws Exception {
        initializeGson();
        return gson.fromJson(jsonToConvert, typeToken.getType());
    }

    private static void initializeGson() throws Exception {
        if (gson == null) {
            gson = new GsonBuilder().enableComplexMapKeySerialization().serializeNulls().create();
        }
    }

    private static byte[] convertStringToBytes(String str) {
        char[] charArray = str.toCharArray();
        byte[] bytes = new byte[str.toCharArray().length];
        for (int i = 0; i < charArray.length; i++) {
            bytes[i] = (byte) charArray[i];
        }
        return bytes;
    }

    public static String codify(String stringToCodify) throws Exception {
        byte[] keyBytes = convertStringToBytes(codifyKey1 + codifyKey2);
        Key key = new SecretKeySpec(keyBytes, codifyAlgorithm);
        Cipher cipher = Cipher.getInstance(codifyAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(stringToCodify.getBytes("utf-8"));
        String encryptedBytes64 = new String(java.util.Base64.getEncoder().encode(encryptedBytes));
        return encryptedBytes64;
    }

    public static String dCodify(String stringToDCodify) throws Exception {
        byte[] keyBytes = convertStringToBytes(codifyKey1 + codifyKey2);
        Key key = new SecretKeySpec(keyBytes, codifyAlgorithm);
        Cipher cipher = Cipher.getInstance(codifyAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = java.util.Base64.getDecoder().decode(stringToDCodify);
        byte[] decryptedValue = cipher.doFinal(decryptedValue64);
        String decryptedString = new String(decryptedValue, "utf-8");
        return decryptedString;
    }

    protected static Properties getInitializeBaseINI(ServletContextEvent sce, String repositoryPhysicalPath, String jasperPlSqlfactoryPath) {
        Properties prpTemp = new Properties();
        try {
//            System.setProperty("java.awt.headless", "true");
            System.out.println("getInitializeBaseINI(ServletContextEvent sce, String repositoryPhysicalPath)");
            DmBuild.verifyVersion(sce.getServletContext().getRealPath("META-INF"), sce.getServletContext().getRealPath("WEB-INF"));
//            Path.repository = prpTemp.getProperty("physicalPath").trim();
//            System.out.println("physicalPath");
//            System.out.println("Context Path: " + sce.getServletContext().getContextPath());
//            System.out.println("Servlet Context Name: " + sce.getServletContext().getServletContextName());

            DmBaseServer.registerApplicationServiceExtension(DmBaseServer.uriPathExtensionCORS, new IAppServiceResponseHandler() {
                @Override
                public void handleResponse(HttpServletResponseWrapper httpResponse) {
                    httpResponse.addHeader("Access-Control-Allow-Origin", "*");
                    httpResponse.addHeader("Access-Control-Allow-Methods", "POST, GET, UPDATE, OPTIONS");
                    httpResponse.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, X-Auth-Token,DTMN_SERVICE,DTMN_SESSIONID");
                    httpResponse.addHeader("Acccess-Control-Expose-Headers", "DTMN_SESSION_EXPIRED,DTMN_SERVICE,DTMN_SESSIONID");
                }
            });
            Path.repository = DmBaseShared.xNull(repositoryPhysicalPath).trim();
            createRepository();
            System.out.println("createRepository");

            prpTemp.load(new FileReader(DmBaseServer.Path.repository + "/config.properties"));
            System.out.println("config.properties has been read.");
            String localeDmCode = DmBaseShared.xNull(prpTemp.getProperty("localeCountry")).trim().toUpperCase();
            System.out.println("localeCountry: " + localeDmCode);
            if (localeDmCode.equalsIgnoreCase("")) {
                localeDmCode = "IND";
            }
            baseDmLocaleCountry = DmEnum.DmLocaleCountry.getLocaleViaDmCode(localeDmCode);
            baseULocale = new ULocale("en", baseDmLocaleCountry.getISOCode3());
            System.out.println("baseLocaleCreated: ");
            DmBaseShared.mobileDigit = baseDmLocaleCountry.getMobileDigit();
//            System.out.println("mobileDigit: " + DmBaseShared.mobileDigit.toString());
//            JRProperties.setProperty(QueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", "dataman.dmToolkit.dmJasper.server.DmPLSqlExecutorFactory");
            JRProperties.setProperty(QueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX + "plsql", jasperPlSqlfactoryPath);
            System.out.println("JRProperties.setProperty");
            Properties fontsProperties = new Properties();
            InputStream fis = null;
            try {
                String jasperExtensionProperties = sce.getServletContext().getRealPath("WEB-INF/classes");
                File propertisFile = new File(new File(jasperExtensionProperties), "jasperreports_extension.properties");
                changeJasperExtensionProperties(propertisFile, Path.pacSetup);
                fis = new FileInputStream(propertisFile);
                fontsProperties.load(fis);
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (fis != null) {
                    fis.close();
                    fis = null;
                }
            }
            System.out.println("jasperExtensionProperties");

            if (fontsProperties.containsKey("net.sf.jasperreports.extension.simple.font.families.ireportfamily")) {
                System.out.println("scanFonts: " + Path.fonts);
                createFontsInitalization(DmBaseShared.xNull(fontsProperties.getProperty("net.sf.jasperreports.extension.simple.font.families.ireportfamily", "")), new File(Path.fonts));
            }

            System.out.println("fontsProperties");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnsupportedOperationException(e.getMessage());
        }

        return prpTemp;
    }

    private static void changeJasperExtensionProperties(File propertiesFilePath, String fontsXmlPath) throws Exception {
        FileWriter writer = null;
        try {
            writer = new FileWriter(propertiesFilePath);
            writer.write("net.sf.jasperreports.extension.registry.factory.fonts=net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory\n"
                    + "net.sf.jasperreports.extension.simple.font.families.ireportfamily=" + fontsXmlPath + "/fontsfamily.xml");
        } finally {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        }
    }

    private static void createRepository() {
        File file;

        try {
            Path.repository = Path.repository + "/repository";
            Path.sessions = Path.repository + "/sessions";
            Path.logs = Path.repository + "/logs";

            Path.resources = Path.repository + "/resources";
            Path.reports = Path.resources + "/reports";
            Path.images = Path.resources + "/images";

            Path.pacSetup = Path.resources + "/pacSetup";
            Path.installer = Path.pacSetup + "/installer";
            Path.fonts = Path.pacSetup + "/fonts";
            Path.pac = Path.pacSetup + "/pac";

            file = new File(Path.repository);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.sessions);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.logs);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.resources);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.reports);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.images);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.pacSetup);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.installer);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.fonts);
            if (!file.exists()) {
                file.mkdir();
            }

            file = new File(Path.pac);
            if (!file.exists()) {
                file.mkdir();
            }

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static void createRepositorySsn(String sessionId) {
        File file;

        try {

            file = new File(Path.sessions + "/" + sessionId.trim());
            if (file.exists()) {
                file.delete();
            }

            file.mkdir();
            file.deleteOnExit();

        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static synchronized void deleteFilesAndDirectories(String path) {
        File file;
        String[] files;

        try {
            file = new File(path);
            if (file.isDirectory()) {
                files = file.list();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        deleteFilesAndDirectories(path + "/" + files[i].trim());
                    }
                }
                file.delete();
            } else {
                file.delete();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getSpace(int noOfSpace) {
        String rtnValue = "";

        for (int i = 0; i < noOfSpace; i++) {
            rtnValue = rtnValue + " ";
        }

        return rtnValue;
    }

    public static Date convertDate(String value) {
        Date date = null;

        try {
            if (value != null) {
                if (!value.equalsIgnoreCase("")) {
                    date = new Date(DmBaseShared.convertDate(value).getTime());
                }
            }
        } catch (Exception e) {
        }

        return date;
    }

    public static String addDate(int addNature, int addValue, String date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DmBaseShared.convertDate(date));
        cal.add(addNature, addValue);
        return DmBaseShared.dNull(new Date(cal.getTime().getTime()));
    }

    public static String vNull(Object value) {
        NumberFormat formatter = new DecimalFormat("0.#######");

        return formatter.format(DmBaseShared.vNull(value));
    }

    public static String vNull(Object value, String pattern) {
        NumberFormat formatter = new DecimalFormat(pattern);

        return formatter.format(DmBaseShared.vNull(value));
    }

    public static String[] getServerDateTime() {
        String currentTime = "";
        String[] arrSrvDatetime;

        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        currentTime = dateFormat.format(cal.getTime());
        arrSrvDatetime = currentTime.split(" ");

        arrSrvDatetime[0] = DmBaseShared.dNull(arrSrvDatetime[0]);
        arrSrvDatetime[1] = arrSrvDatetime[1].replace(':', '.');

        return arrSrvDatetime;
    }

    public static Timestamp getServerTimestamp() {
//        String currentTime = "";
//        String[] arrSrvDatetime;
//
//        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
//        Calendar cal = Calendar.getInstance();
//        currentTime = dateFormat.format(cal.getTime());
//        arrSrvDatetime = currentTime.split(" ");
//
//        arrSrvDatetime[0] = DmBaseShared.dNull(arrSrvDatetime[0]);
//        arrSrvDatetime[1] = arrSrvDatetime[1].replace(':', '.');

        return new Timestamp(new java.util.Date().getTime());
    }

    public static String executeScalar(String sqlQuery, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String rtnValue = null;

        try {
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            rsTemp = psTemp.executeQuery();
            if (rsTemp.next()) {
                rtnValue = rsTemp.getString(1);
            }
            psTemp.close();
            rsTemp.close();
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
            } catch (Exception e) {
            }
        }

        return rtnValue;
    }

    public static void executeSQL(String sqlQuery, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp = null;

        try {
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.executeUpdate();
            psTemp.close();
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        } finally {
            try {
                if (psTemp != null) {
                    psTemp.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static StringBuffer getRequestBuffer(HttpServletRequest request) throws Exception {
        StringBuffer sb = new StringBuffer();
        BufferedReader inputStream = request.getReader();
        String readLine = inputStream.readLine();
        while (readLine != null) {
            sb.append(readLine);
            readLine = inputStream.readLine();
        }
        inputStream.close();

        return sb;
    }

    public static String getSessionId(HttpServletRequest request) {
        String sessionId = "";
        try {
            if (request instanceof DmRequestWrapper) {
                sessionId = request.getSession().getId();
            } else {
                sessionId = request.getParameter("u");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sessionId;
    }

    public static <T> T getSsnData(HttpSession session, String attributeName) {
        T tRtn = null;
        try {
            tRtn = (T) session.getAttribute(attributeName);
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }
        return tRtn;
    }

    public static <T> T getSsnData(String sessionId, String attributeName) {
        T tRtn = null;
        try {
            tRtn = (T) getSsnData(DmSession.getSessions().get(sessionId), attributeName);
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }
        return tRtn;
    }

    public static <T> T getSsnData(DmRSS rssTemp, String strVarName) {
        T tRtn = null;

        try {
            tRtn = (T) getSsnData(rssTemp.getLocalRequest(), strVarName);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
        return tRtn;
    }

    public static <T> T getSsnData(HttpServletRequest request, String attributeName) {
        T rtnValue = null;

        try {
            rtnValue = (T) getSsnData(getSessionId(request), attributeName);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return rtnValue;
    }

    public static String getSsnUserName(HttpSession session) {
        String strRtn = null;
        try {
            strRtn = (String) session.getAttribute("UserName");
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }
        return strRtn;
    }

    public static String getSsnUserName(String sessionId) {
        String strRtn = null;

        try {
            strRtn = (String) getSsnUserName(DmSession.getSessions().get(sessionId));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) getSsnUserName(rssTemp.getLocalRequest());
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) getSsnUserName(getSessionId(request));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static int getSsnUserLockTimeOut(HttpSession session) {
        int strRtn = 0;
        try {
            strRtn = (int) session.getAttribute("UserLockTimeOut");
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }
        return strRtn;
    }

    public static int getSsnUserLockTimeOut(String sessionId) {
        int strRtn = 0;

        try {
            strRtn = (int) getSsnUserLockTimeOut(DmSession.getSessions().get(sessionId));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static int getSsnUserLockTimeOut(DmRSS rssTemp) {
        int strRtn = 0;

        try {
            strRtn = (int) getSsnUserLockTimeOut(rssTemp.getLocalRequest());
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static int getSsnUserLockTimeOut(HttpServletRequest request) {
        int strRtn = 0;

        try {
            strRtn = (int) getSsnUserLockTimeOut(getSessionId(request));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnLinkedUser(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("LinkedUser"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnLinkedUser(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("LinkedUser");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserLinkedAccountName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("UserLinkedAccountName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserLinkedAccountName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("UserLinkedAccountName");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserLinkedAccountCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("UserLinkedAccountCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserLinkedAccountCode(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("UserLinkedAccountCode");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserDescription(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("UserDescription"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserDescription(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("UserDescription");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserNatureCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("UserNatureCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnUserNatureCode(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("UserNatureCode");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnFileDBName(DmRSS rssTemp, String FileDBName) {
        try {
            ((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().setAttribute("FileDBName", FileDBName);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getSsnFileDBName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("FileDBName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnFileDBName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("FileDBName");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnDBName(DmRSS rssTemp, String dbName) {
        try {
            setSsnDBName(rssTemp.getLocalRequest(), dbName);
//            ((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().setAttribute("DBName", dbName);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static void setSsnDBName(HttpServletRequest request, String dbName) {
        try {
            if (request instanceof DmRequestWrapper) {
                ((DmRequestWrapper) request).getDatamanSession().setAttribute("DBName", dbName);
            } else {
                DmSession.getSessions().get(request.getParameter("u")).setAttribute("DBName", dbName);
            }
//            ((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().setAttribute("DBName", dbName);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getSsnDBName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("DBName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnDBName(HttpServletRequest request) {
        String strRtn = null;

        try {
            if (request instanceof DmRequestWrapper) {
                strRtn = (String) ((DmRequestWrapper) request).getDatamanSession().getValue("DBName");
            } else {
                strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("DBName");
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnPLTName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("PLTName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnPLTCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("PLTCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnPLTCode(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("PLTCode");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCompanyCYear(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("CompanyCYear"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCompanyCYear(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("CompanyCYear");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCompanyName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("CompanyName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCompanyName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("CompanyName");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCompanyCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("CompanyCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCompanyCode(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("CompanyCode");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnVoucherPrefix(DmRSS rssTemp, String voucherPrefix) {
        try {
            ((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().setAttribute("VoucherPrefix", voucherPrefix);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getSsnVoucherPrefix(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("VoucherPrefix"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnVoucherPrefix(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("VoucherPrefix");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteStateNumericCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteStateNumericCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteStateNumericCode(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteStateNumericCode");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnSiteCode(DmRSS rssTemp, String siteCode) {
        try {
            ((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().setAttribute("SiteCode", siteCode);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getSsnSiteCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteCode(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteCode");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteDisplayName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteDisplayName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteDisplayName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteDisplayName");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteName");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteSName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteSName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteSName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteSName");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteAddress1(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteAddress1"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteAddress1(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteAddress1");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteAddress2(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteAddress2"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteAddress2(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteAddress2");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteCityName(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteCityName"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteCityName(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteCityName");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSitePIN(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SitePIN"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSitePIN(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SitePIN");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSitePhone(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SitePhone"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSitePhone(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SitePhone");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteEMail(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteEMail"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteEMail(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteEMail");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteDrugLicenseNo(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteDrugLicenseNo"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteDrugLicenseNo(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteDrugLicenseNo");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteGSTIN(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteGSTIN"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteGSTIN(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteGSTIN");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteGSTINDate(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteGSTINDate"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteGSTINDate(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteGSTINDate");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteList(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteList"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteList(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("SiteList");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteSerialKey(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteSerialKey"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnSiteClientCode(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("SiteClientCode"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCostCenterList(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("CostCenterList"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnCostCenterList(HttpServletRequest request) {
        String strRtn = null;

        try {
            strRtn = (String) DmSession.getSessions().get(request.getParameter("u")).getValue("CostCenterList");
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnNoOfUser(DmRSS rssTemp, String noOfMachine) {
        try {
            setSsnData(rssTemp, "NoOfUser", DmBaseShared.iNull(noOfMachine).toString());
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getSsnNoOfUser(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("NoOfUser"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static void setSsnData(String sessionId, String attributeName, Object objValue) {
        try {
            setSsnData(DmSession.getSessions().get(sessionId), attributeName, objValue);
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }
    }

    public static void setSsnData(HttpSession session, String attributeName, Object objValue) {
        try {
            session.setAttribute(attributeName, objValue);
        } catch (Exception ex) {
            throw new UnsupportedOperationException(ex.getMessage());
        }
    }

    public static void setSsnData(DmRSS rssTemp, String attributeName, Object tVarValue) {
        try {
            setSsnData(rssTemp.getLocalRequest(), attributeName, tVarValue);
//            ((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().setAttribute(strVarName, tVarValue);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static void setSsnData(HttpServletRequest request, String attributeName, Object tVarValue) {
        try {
            setSsnData(getSessionId(request), attributeName, tVarValue);
//            DmSession.getSessions().get(request.getParameter("u")).setAttribute(strVarName, tVarValue);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }
    }

    public static String getParamList(String parameter, String x) {
        String rtnValue = "";
        String[] xList;

        x = x.replace("'", "");
        xList = x.split(",");

        for (int j = 0; j < xList.length; j++) {
            if (!rtnValue.equalsIgnoreCase("")) {
                rtnValue += ",";
            }
            rtnValue += parameter.trim() + String.valueOf(j + 1);
        }

        return rtnValue;
    }

    public static String getSerialNoInteger(DmRSS rssTemp, String tableName, Connection cnDB) {
        return getSerialNoInteger(getSsnSiteCode(rssTemp), tableName, cnDB);
    }

    public static String getSerialNoInteger(String siteCode, String tableName, Connection cnDB) {
        String rtnValue = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            psTemp = DmPreparedStatement.prepareStatement(cnDB, "exec getCode :tableName,:siteCode,0 ");
            psTemp.setString("tableName", tableName);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            rtnValue = "1" + vNull(siteCode, "0000") + vNull(rsTemp.getInt(1), "0000");
            psTemp.close();
            rsTemp.close();
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
            } catch (Exception e) {
            }
        }

        return rtnValue;
    }

    public static String getSerialNoBigInt(DmRSS rssTemp, String tableName, Connection cnDB) {
        return getSerialNoBigInt(getSsnSiteCode(rssTemp), tableName, cnDB);
    }

    public static String getSerialNoBigInt(String siteCode, String tableName, Connection cnDB) {
        String rtnValue = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            psTemp = DmPreparedStatement.prepareStatement(cnDB, "exec getCode :tableName,:siteCode,0 ");
            psTemp.setString("tableName", tableName);
            psTemp.setString("siteCode", siteCode);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            rtnValue = "1" + vNull(siteCode, "0000") + vNull(rsTemp.getInt(1), "00000000000000");
            psTemp.close();
            rsTemp.close();
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
            } catch (Exception e) {
            }
        }

        return rtnValue;
    }

    public static String getSerialNoBigInt(String siteCode, String vPrefix, String tableName, Connection cnDB) {
        String rtnValue = "";
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;

        try {
            if (DmBaseShared.iNull(siteCode).longValue() == 0) {
                throw new UnsupportedOperationException("Sitecode is mandatory.");
            }

            if (DmBaseShared.iNull(vPrefix).longValue() == 0) {
                throw new UnsupportedOperationException("Voucher Prefix is mandatory.");
            }

            psTemp = DmPreparedStatement.prepareStatement(cnDB, "exec getCode :tableName,:siteCode,:vPrefix ");
            psTemp.setString("tableName", tableName);
            psTemp.setString("siteCode", siteCode);
            psTemp.setString("vPrefix", vPrefix);
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            rtnValue = "1" + vNull(siteCode, "0000") + vNull(vPrefix, "00") + vNull(rsTemp.getInt(1), "00000000000");
            psTemp.close();
            rsTemp.close();
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
            } catch (Exception e) {
            }
        }

        return rtnValue;
    }

    public static String getDocId(String voucherType, String voucherPrefix, String siteCode, Connection cnTemp) throws Exception {
        DmPreparedStatement psTemp;
        ResultSet rsTemp;
        String docId = "", voucherNo = "";

        psTemp = DmPreparedStatement.prepareStatement(cnTemp, "exec getDocId :voucherType,:voucherPrefix,:siteCode ");
        psTemp.setString("voucherType", voucherType);
        psTemp.setString("voucherPrefix", voucherPrefix);
        psTemp.setString("siteCode", siteCode);
        rsTemp = psTemp.executeQuery();
        if (rsTemp.next()) {
            voucherNo = String.valueOf(DmBaseShared.vNull(rsTemp.getString(1)).intValue());
        }
        rsTemp.close();
        psTemp.close();

        if (voucherNo.equalsIgnoreCase("") || Double.valueOf(voucherNo) <= 0) {
            throw new UnsupportedOperationException("Unable to generate docId.");
        }

        docId = "1" + vNull(DmBaseShared.vNull(siteCode), "0000") + vNull(DmBaseShared.vNull(voucherType), "000") + vNull(DmBaseShared.vNull(voucherPrefix), "00") + vNull(DmBaseShared.vNull(voucherNo), "00000000");

        return docId;
    }

    public static String getDocId(String voucherType, String voucherPrefix, Connection cnTemp, DmRSS rssTemp) throws Exception {
        return getDocId(voucherType, voucherPrefix, getSsnSiteCode(rssTemp), cnTemp);
    }

    public static RecId getRecId(String tableName, String keyFieldName, String keyValue, String recIdFieldName,
            RecId recId, String dateValue, String voucherType, String voucherPrefix,
            Connection cnTemp, DmRSS rssTemp, Boolean isCheckDate) throws Exception {

        return getRecId(tableName, keyFieldName, keyValue, recIdFieldName, recId, dateValue, voucherType, voucherPrefix, cnTemp, rssTemp, isCheckDate, null);
    }

    public static RecId getRecId(String tableName, String keyFieldName, String keyValue, String recIdFieldName,
            RecId recId, String dateValue, String voucherType, String voucherPrefix,
            Connection cnTemp, DmRSS rssTemp, Boolean isCheckDate, HashMap<String, String> paramFormat) throws Exception {

        return getRecId(tableName, keyFieldName, keyValue, recIdFieldName, recId, dateValue, "", voucherType, voucherPrefix,
                getSsnSiteCode(rssTemp), getSsnSiteSName(rssTemp), getSsnCompanyCode(rssTemp), cnTemp, isCheckDate, paramFormat);
    }

    public static RecId getRecId(String tableName, String keyFieldName, String keyValue, String recIdFieldName,
            RecId recId, String dateValue, String voucherTypeFieldName, String voucherType, String voucherPrefix,
            String siteCode, String siteShortName, String companyCode,
            Connection cnTemp, Boolean isCheckDate, HashMap<String, String> paramFormat) throws Exception {
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        int day = 0, month = 0, year = 0, yearFull = 0;
        String sqlQuery = "", nsIncludeV_Type = "", nsFormat = "", nsCounterFormat = "0", companySName = "", voucherSName = "";
        boolean isYearWise = true, isSiteWise = true;
        Calendar calender = Calendar.getInstance();

        if (recId == null) {
            throw new UnsupportedOperationException("Please mention recId.");
        }
//        recId.setRecIdValue(DmBaseShared.xNull(recId.getPrefix()) + DmBaseShared.xNull(recId.getRecIdValue()) + DmBaseShared.xNull(recId.getSuffix()));
        recId.setRecIdValue(DmBaseShared.xNull(recId.getRecIdValue()));

        keyValue = DmBaseShared.xNull(keyValue);
        //====================================================================================
        //================================ Start V_Type Check ================================
        //====================================================================================
        if (DmBaseShared.xNull(DmBaseShared.foriegnKey(voucherType)).trim().equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Please mention voucher type.");
        }

        if (dateValue.trim().equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Please mention date.");
        }
        //====================================================================================
        //================================ End V_Type Check ==================================
        //====================================================================================
        //=============================== Start Get voucher Category=================================

        boolean isOPDEntry = false; // As per Boss told on 23-Mar-2023 to By pass Date Range for All OPD Entries. 
        String sql = "select category from voucher_Type where v_type= :vtype and category in ('OPD','OPDEM','PAYCL','OPRRC','PTRID')  ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sql);
        psTemp.setString("vtype", DmBaseShared.iNull(voucherType).toString());
        rsTemp = psTemp.executeQuery();
        if (rsTemp.next()) {
            isOPDEntry = true;
        }
        rsTemp.close();
        psTemp.close();
        //===============================End Get voucher Category=================================
        //====================================================================================
        //============================== Start Ini Voucher Type ==============================
        //====================================================================================
        sqlQuery = "select  top 1 nsf.nsFormat,nsf.counterFormat,nsf.isYearWise,nsf.isSiteWise,ns.includeV_Type "
                + "from numberSystem ns,numberSystemFormat nsf "
                + "where ns.nsGroup = nsf.nsGroup and  "
                + "ns.v_Type = :voucherType and "
                + "isNull(nsf.site_Code,'" + siteCode + "') = '" + siteCode + "' "
                + "order by nsf.site_Code desc;";

        sqlQuery += "Select isNull(acceptEntryAfter,DATEADD(day,-1,:dateValue)) As acceptEntryAfter,short_name From Voucher_Type Where V_Type = :voucherType; ";
        sqlQuery += "Select c.Start_Dt, c.End_Dt, p.sName "
                + "From " + dbCompany.trim() + ".dbo.Company c "
                + "left join " + dbCompany.trim() + ".dbo.productLicensedTo p on c.pltCode = p.pltCode "
                + "Where c.comp_Code = '" + companyCode + "' And c.v_Prefix = :voucherPrefix; ";
        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
        psTemp.setString("voucherType", voucherType);
        psTemp.setDate("dateValue", DmBaseServer.convertDate(dateValue));
        psTemp.setString("voucherPrefix", voucherPrefix);

        if (psTemp.execute()) {
            rsTemp = psTemp.getResultSet();
            if (rsTemp.next()) {
                nsIncludeV_Type = DmBaseShared.xNull(rsTemp.getString("includeV_Type"));
                nsFormat = DmBaseShared.xNull(rsTemp.getString("nsFormat"));
                nsCounterFormat = DmBaseShared.xNull(rsTemp.getString("counterFormat"));
                if (DmBaseShared.iNull(rsTemp.getString("isYearWise")).intValue() == 0) {
                    isYearWise = false;
                }
                if (DmBaseShared.iNull(rsTemp.getString("isSiteWise")).intValue() == 0) {
                    isSiteWise = false;
                }
            } else {
                rsTemp.close();
                psTemp.close();
                throw new UnsupportedOperationException("Please define number system.");
            }
            rsTemp.close();

            psTemp.getMoreResults();
            rsTemp = psTemp.getResultSet();
            if (rsTemp.next()) {
                voucherSName = DmBaseShared.xNull(rsTemp.getString("short_name"));
                if (DmBaseShared.convertDate(DmBaseShared.dNull(rsTemp.getDate("acceptEntryAfter"))).getTime()
                        >= DmBaseShared.convertDate(dateValue).getTime()) {
                    rsTemp.close();
                    psTemp.close();

                    throw new UnsupportedOperationException("Date in which you are adding an entry is being locked by your administrator.");

                }
            }
            rsTemp.close();

            //====================================================================================
            //================================ Start Date Check ==================================
            //====================================================================================
            psTemp.getMoreResults();
            rsTemp = psTemp.getResultSet();
            if (rsTemp.next()) {
                companySName = DmBaseShared.xNull(rsTemp.getString("sName"));
                if (isCheckDate) {
                    if (DmBaseShared.convertDate(DmBaseShared.dNull(rsTemp.getDate("Start_Dt"))).getTime() > DmBaseShared.convertDate(dateValue).getTime()
                            || DmBaseShared.convertDate(DmBaseShared.dNull(rsTemp.getDate("End_Dt"))).getTime() < DmBaseShared.convertDate(dateValue).getTime()) {
                        rsTemp.close();
                        psTemp.close();
                        if (!isOPDEntry) {
                            throw new UnsupportedOperationException("You can do entry in current financial year only.");
                        }
                    }
                }
            }
            rsTemp.close();

            //====================================================================================
            //================================ End Date Check ====================================
            //====================================================================================
        }
        psTemp.close();

        if (nsFormat.trim().equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Please define number format.");
        }
        if (nsCounterFormat.trim().equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Please define counter format.");
        }
        //====================================================================================
        //============================== End Ini Voucher Type ================================
        //====================================================================================

        calender.setTime(DmBaseShared.convertDate(dateValue));
        day = calender.get(Calendar.DAY_OF_MONTH);
        month = calender.get(Calendar.MONTH) + 1;
        yearFull = Integer.valueOf(String.valueOf(calender.get(Calendar.YEAR)));
        year = Integer.valueOf(DmBaseShared.iNull(yearFull).toString().substring(2, 4));

        if (recId.getRecIdValue().trim().equalsIgnoreCase("")) {

//            if (nsFormat.contains("{sitesname}")) {
//                nsFormat = nsFormat.replaceAll("\\{sitesname\\}", siteShortName);
//            }
            nsFormat = nsFormat.replaceAll("\\{sitesname\\}", siteShortName);
            nsFormat = nsFormat.replaceAll("\\{site\\}", new DecimalFormat("000").format(DmBaseShared.iNull(siteCode).intValue()));
            nsFormat = nsFormat.replaceAll("\\{voucherprefix\\}", new DecimalFormat("00").format(DmBaseShared.iNull(voucherPrefix).intValue()));
            nsFormat = nsFormat.replaceAll("\\{company\\}", companySName);
            nsFormat = nsFormat.replaceAll("\\{year\\}", new DecimalFormat("00").format(year));
            nsFormat = nsFormat.replaceAll("\\{yearfull\\}", new DecimalFormat("0000").format(yearFull));
            nsFormat = nsFormat.replaceAll("\\{month\\}", new DecimalFormat("00").format(month));
            nsFormat = nsFormat.replaceAll("\\{day\\}", new DecimalFormat("00").format(day));
            nsFormat = nsFormat.replaceAll("\\{voucher\\}", new DecimalFormat("000").format(DmBaseShared.iNull(voucherType).intValue()));
            nsFormat = nsFormat.replaceAll("\\{vouchersname\\}", voucherSName);

            if (paramFormat != null) {
                for (String keySet : paramFormat.keySet()) {
                    nsFormat = nsFormat.replaceAll("\\{" + keySet + "\\}", DmBaseShared.xNull(paramFormat.get(keySet)).trim());
                }
            }

            recId.setPrefix(nsFormat.replaceAll("\\{counter\\}", ""));

            if (DmBaseShared.iNull(recId.getCounter()).longValue() == 0) {
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, "exec getRecId :nsGroup,:voucherPrefix,:siteCode,:nsFormatValue ");
                psTemp.setString("nsGroup", voucherType);
                psTemp.setString("voucherPrefix", isYearWise ? voucherPrefix : null);
                psTemp.setString("siteCode", isSiteWise ? siteCode : null);
//                psTemp.setString("nsFormatValue", nsFormat.replaceAll("\\{counter\\}", ""));
                psTemp.setString("nsFormatValue", recId.getPrefix());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    recId.setCounter(DmBaseShared.iNull(rsTemp.getString(1)).longValue());
                }
                rsTemp.close();
                psTemp.close();
            } else {
                psTemp = DmPreparedStatement.prepareStatement(cnTemp,
                        "select	[counter] as cntr "
                        + "from numberSystemCounter  "
                        + "where nsGroup = :nsGroup and "
                        + "isNull(v_Prefix,0) = isNull(:voucherPrefix,0) and  "
                        + "isNull(site_Code,0) = isNull(:siteCode,0) and  "
                        + "isNull(nsFormatValue,'') = isNull(:nsFormatValue,'') and "
                        + "isNull([counter],0) > :counter; ");
                psTemp.setString("nsGroup", voucherType);
                psTemp.setString("voucherPrefix", voucherPrefix);
                psTemp.setString("siteCode", siteCode);
//                psTemp.setString("nsFormatValue", nsFormat.replaceAll("\\{counter\\}", ""));
                psTemp.setString("nsFormatValue", recId.getPrefix());
                psTemp.setString("counter", DmBaseShared.iNull(recId.getCounter()).toString());
                rsTemp = psTemp.executeQuery();
                if (rsTemp.next()) {
                    rsTemp.close();
                    psTemp.close();
                    throw new UnsupportedOperationException("Given counter can not be greater than existing counter.");
                }
                rsTemp.close();
                psTemp.close();
            }
            nsFormat = nsFormat.replaceAll("\\{counter\\}", new DecimalFormat(nsCounterFormat).format(DmBaseShared.iNull(recId.getCounter()).longValue()));

            recId.setRecIdValue(nsFormat);
        }

        //====================================================================================
        //============================== Start Format Check ==================================
        //====================================================================================
        if (!recId.getRecIdValue().trim().equalsIgnoreCase("")) {
            if (DmBaseShared.xNull(voucherTypeFieldName).trim().equalsIgnoreCase("")) {
                voucherTypeFieldName = "v_Type";
            }
            sqlQuery = "Select Count(" + recIdFieldName + ") As Cnt "
                    + "From " + tableName + "  "
                    + "Where " + recIdFieldName + " = :recId and "
                    + "" + keyFieldName + " <> :keyValue and "
                    + voucherTypeFieldName + " in (" + getParamList(":v_Type", nsIncludeV_Type) + ") ";
//                    + "v_Type in (" + getParamList(":v_Type", nsIncludeV_Type) + ") ";

            if (isYearWise) {
                sqlQuery += "and v_Prefix = :voucherPrefix ";
            }
            if (isSiteWise) {
                sqlQuery += "and site_Code = '" + siteCode + "' ";
            }

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("recId", recId.getRecIdValue());
            psTemp.setString("keyValue", keyValue);
            psTemp.setStringList("v_Type", nsIncludeV_Type);
            if (isYearWise) {
                psTemp.setString("voucherPrefix", voucherPrefix);
            }
            rsTemp = psTemp.executeQuery();
            rsTemp.next();
            if (rsTemp.getInt("Cnt") > 0) {
                rsTemp.close();
                psTemp.close();
                throw new UnsupportedOperationException("Duplicate voucher no.");
            }
            rsTemp.close();
            psTemp.close();
        }
        //====================================================================================
        //============================== End Format Check ====================================
        //====================================================================================

        if (recId.getRecIdValue().trim().equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Voucher no. is mandatory.");
        }

        return recId;
    }

    public static String getVType(String manualCode, Connection cnTemp) {
        return getCodeViaManualCode(manualCode, "short_Name", "voucher_Type", "v_Type", cnTemp);
    }

    public static String getCodeViaManualCode(String manualCode, String manualCodeFieldName, String tableName, String codeFieldName, Connection cnTemp) {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String sqlQuery, rtnValue = "";

        try {
            sqlQuery = "select " + codeFieldName + " "
                    + "from " + tableName + " "
                    + "where " + manualCodeFieldName + " = :manualCode ";
            psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
            psTemp.setString("manualCode", manualCode);
            rsTemp = psTemp.executeQuery();
            while (rsTemp.next()) {
                rtnValue = DmBaseShared.xNull(rsTemp.getString(1));
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
        return rtnValue;
    }

    public static String prepareContraText(String contraName, double dmount, String drOrCr) {
        int nameMaxLen = 35, nameCutOffLen, amtMaxLen = 18, spaceNeeded = 2;
        String contraTextVar;

        //StrContraTextVar = Mid(Trim(StrContraName), 1, IntNameMaxLen) & Space((IntNameMaxLen + IntSpaceNeeded) - Len(Mid(Trim(StrContraName), 1, IntNameMaxLen))) & Space(IntAmtMaxLen - Len(Format(Val(DblAmount), "##,##,##,##,##0.00"))) & Format(Val(DblAmount), "##,##,##,##,##0.00") & " " & Trim(StrDrCr)
        if (contraName.trim().length() < nameMaxLen) {
            nameCutOffLen = contraName.trim().length();
        } else {
            nameCutOffLen = nameMaxLen;
        }

        contraTextVar = contraName.trim().substring(0, nameCutOffLen) + getSpace((nameMaxLen + spaceNeeded) - contraName.trim().substring(0, nameCutOffLen).length()) + getSpace(amtMaxLen - new DecimalFormat("##,##,##,##,##0.00").format(dmount).length()) + new DecimalFormat("##,##,##,##,##0.00").format(dmount) + " " + drOrCr.trim();

        return contraTextVar;

    }

    public static DmPreparedStatement evaluateHelper(DmHelper helper, DmSQLWithParam sqlWithParam, Connection cnTemp, boolean blnEOF, DmRSS servletInstance) throws Exception {
        ResultSet rsTemp = null;
        DmPreparedStatement psTemp = null;
        String finalQuery, orderBy;
        int intTemp;

        sqlWithParam = helper.prepareQuery(sqlWithParam);
        finalQuery = sqlWithParam.getSQL();
        if (helper.getPaging().getResultFormat() == DmPaging.Result.XLS) {
            orderBy = "";
            if (DmBaseShared.vNull(helper.getRowsPerPage()) > 0) {
                if (DmBaseShared.vNull(helper.getRowsStartFrom()) >= 0) {
                    for (DmSort sort : helper.getSort()) {
                        if (sort != null) {
                            if (!DmBaseShared.xNull(sort.getColumnName()).trim().equalsIgnoreCase("")) {
                                if (!orderBy.trim().equalsIgnoreCase("")) {
                                    orderBy += ",";
                                }
                                orderBy += DmBaseShared.xNull(sort.getColumnName()).trim() + " " + sort.getSortType().name() + " ";
                            }
                        }
                    }

                    finalQuery += "order by " + orderBy;
                }
            }
            sqlWithParam.setSQL(finalQuery);
            DmPreparedStatement tmpStmt = loadParam(cnTemp, sqlWithParam);

            RptFind tmpDownload = new RptFind();
            tmpDownload.setMasterReport(helper.getPaging().getReport());
            tmpDownload.setPreparedStatement(tmpStmt);
            KeyField kf = tmpDownload.printFind(servletInstance);
            helper.getPaging().setFilePath(kf.getSharedKeyFieldCode());
            helper.getPaging().setFileName(kf.getSharedKeyFieldName());

            psTemp = DmPreparedStatement.prepareStatement(cnTemp, "select  tmp.column1 from (select 0 as column1) as tmp where tmp.column1 = 1");

        } else {
            if (blnEOF) {
                sqlWithParam.setSQL("Select Count(*) As Cnt From ( " + finalQuery + ") As T1 ");
                psTemp = loadParam(cnTemp, sqlWithParam);
                rsTemp = psTemp.executeQuery();
                rsTemp.next();
                helper.setRowCount(rsTemp.getInt(1));
                rsTemp.close();
                psTemp.close();
                intTemp = Integer.valueOf(String.valueOf(helper.getRowCount() / helper.getRowsPerPage()));
                intTemp = intTemp * helper.getRowsPerPage();
                if ((helper.getRowCount() % helper.getRowsPerPage()) == 0) {
                    intTemp = intTemp - helper.getRowsPerPage();
                }
                helper.setRowsStartFrom(intTemp);
            }

            orderBy = "";
            if (DmBaseShared.vNull(helper.getRowsPerPage()) > 0) {
                if (DmBaseShared.vNull(helper.getRowsStartFrom()) >= 0) {
                    for (DmSort sort : helper.getSort()) {
                        if (sort != null) {
                            if (!DmBaseShared.xNull(sort.getColumnName()).trim().equalsIgnoreCase("")) {
                                if (!orderBy.trim().equalsIgnoreCase("")) {
                                    orderBy += ",";
                                }
                                orderBy += DmBaseShared.xNull(sort.getColumnName()).trim() + " " + sort.getSortType().name() + " ";
                            }
                        }
                    }

                    finalQuery += "order by " + orderBy;
                    finalQuery += "offset " + String.valueOf(helper.getRowsStartFrom()) + " rows fetch next " + String.valueOf(helper.getRowsPerPage()) + " rows only ";
                }
            }

            sqlWithParam.setSQL(finalQuery);
            psTemp = loadParam(cnTemp, sqlWithParam);
        }

        return psTemp;
    }

    private static DmPreparedStatement loadParam(Connection cnTemp, DmSQLWithParam sqlWithParam) throws Exception {
        DmPreparedStatement psTemp = null;
        List<DmSQLParam> sqlparam;

        psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlWithParam.getSQL());

        sqlparam = sqlWithParam.getSqlParam();
        for (int i = 0; i < sqlparam.size(); i++) {
            if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.DOUBLE) {
                psTemp.setDouble(sqlparam.get(i).getParamName(), sqlparam.get(i).getDouble());
            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.INTEGER) {
                psTemp.setInt(sqlparam.get(i).getParamName(), sqlparam.get(i).getInteger());
            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.DATETIME) {
                if (sqlparam.get(i).getDate() != null) {
                    psTemp.setDate(sqlparam.get(i).getParamName(), new Date(sqlparam.get(i).getDate().getTime()));
                } else {
                    psTemp.setDate(sqlparam.get(i).getParamName(), null);
                }
            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.STRINGLIST) {
                psTemp.setStringList(sqlparam.get(i).getParamName(), sqlparam.get(i).getString());
            } else if (sqlparam.get(i).getDataType() == DmEnum.DmDataType.TIMESTAMP) {
                if (sqlparam.get(i).getTimestamp() != null) {
                    psTemp.setTimestamp(sqlparam.get(i).getParamName(), sqlparam.get(i).getTimestamp());
                } else {
                    psTemp.setTimestamp(sqlparam.get(i).getParamName(), null);
                }
            } else {
                psTemp.setString(sqlparam.get(i).getParamName(), sqlparam.get(i).getString());
            }
        }

        return psTemp;
    }

    public static String showJasper(DmRSS rssTemp, JasperPrint jasperPrint, String suffix, DmReportProperty_Mdl reportProperty) throws Exception {
        String filePath, fileName;
        filePath = DmBaseServer.Path.sessions + "/" + rssTemp.getLocalRequest().getSession().getId().trim();
        if (reportProperty.getExportType() == DmEnum.DmExportType.EXCEL) {
            fileName = String.valueOf(new java.util.Date().getTime()).trim() + DmBaseShared.xNull(suffix).trim() + ".xls";

        } else if (reportProperty.getExportType() == DmEnum.DmExportType.WORD) {
            fileName = String.valueOf(new java.util.Date().getTime()).trim() + DmBaseShared.xNull(suffix).trim() + ".docx";

        } else {
            fileName = String.valueOf(new java.util.Date().getTime()).trim() + DmBaseShared.xNull(suffix).trim() + ".pdf";
        }

        return DmBaseServer.showJasper(filePath, fileName, jasperPrint, suffix, reportProperty);
    }

    public static DmXlsSheet setXLSFileHeader(String fileName, DmPrintFindExcel xlsRsTemp, String widgetName, PageOrientation pageOrientation, PaperSize paperSize, HashMap<String, FieldUDT_Mdl> filter, DmRSS rssTemp) {
        DmXlsSheet xlsSheet = null;
        Label lbl;
        WritableFont cellFontBold;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        WritableCellFormat cellFormat;
        int dividedColumn = 0;
        try {
            xlsSheet = xlsRsTemp.createWorkBook(fileName);
            xlsSheet.getWorkSheet().setPageSetup(pageOrientation, paperSize, 0.25, 0.278);

            cellFontBold = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD);

            dividedColumn = xlsRsTemp.getResultSetColumnCount() / 2;

            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 0, dividedColumn, 0);
            lbl = new Label(0, 0, widgetName, cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 1, dividedColumn, 1);
            lbl = new Label(0, 1, getSsnCompanyName(rssTemp) + "-" + getSsnSiteSName(rssTemp), cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.LEFT);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.NONE);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            xlsSheet.mergeCells(0, 2, dividedColumn, 2);
            lbl = new Label(0, 2, sdf.format(new java.sql.Date(getServerTimestamp().getTime())) + " - " + getSsnUserName(rssTemp), cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);

            xlsSheet.mergeCells(0, 3, xlsRsTemp.getResultSetColumnCount() - 1, 3);
            lbl = new Label(0, 3, "", cellFormat);
            xlsSheet.getWorkSheet().addCell(lbl);
        } catch (Exception e) {
        }
        return xlsSheet;
    }

    public static void setXLSFileFooter(DmXlsSheet workSheet, DmRSS rssTemp) {
        Label lbl;
        WritableFont cellFontBold;
        WritableCellFormat cellFormat;
        int lastRowIndex = 0;
        try {
            lastRowIndex = workSheet.getWorkSheet().getRows();
            cellFontBold = new WritableFont(WritableFont.TAHOMA, 10, WritableFont.NO_BOLD);
            cellFormat = new WritableCellFormat(cellFontBold);
            cellFormat.setAlignment(Alignment.CENTRE);
            cellFormat.setBorder(Border.NONE, BorderLineStyle.THIN);
            cellFormat.setVerticalAlignment(VerticalAlignment.TOP);
            workSheet.getWorkSheet().mergeCells(0, lastRowIndex, workSheet.getWorkSheet().getColumns() - 1, lastRowIndex);
            lbl = new Label(0, lastRowIndex, "**End Of File**", cellFormat);
            workSheet.getWorkSheet().addCell(lbl);

        } catch (Exception e) {
        }
    }

    public static String getMdbTmpCollectionName(DmRSS rssTemp, String collectionName) throws Exception {
        String finalCollectionName = "";
        try {
            DmSession session = (DmSession) rssTemp.getLocalRequest().getSession();
            String sessionId = session.getId().replace("-", "_");
            finalCollectionName = "aw_" + sessionId + "_" + collectionName;
        } finally {

        }
        return finalCollectionName;
    }

    public static DBCollection getMdbCollection(DmRSS rssTemp, String collectionName, boolean createIfNotExists) throws Exception {
        DBCollection mdbCollection = null;
        try {
            if (collectionName.contains("$")) {
                throw new Exception("Collection Name of mongo cann't contain '$' sign...");
            }
            String fileDbName = getSsnFileDBName(rssTemp);
            DB mdb = getMongoDB(fileDbName, createIfNotExists);

            mdbCollection = mdb.getCollection(collectionName);
        } finally {

        }
        return mdbCollection;
    }

    public static DB getMongoDB(String dbName, boolean createIfNotExists) throws Exception {
        DB mongoDb = null;
        try {
            if (mongoClient == null) {
                throw new Exception("Mongo Server is not connected.. Unable to create Collection");
            }
            if (createIfNotExists) {
                mongoDb = mongoClient.getDB(dbName);
            } else if (!createIfNotExists) {
                List<String> dbs = mongoClient.getDatabaseNames();
                for (int i = 0; i < dbs.size(); i++) {
                    if (dbs.get(i).equalsIgnoreCase(dbName)) {
                        mongoDb = mongoClient.getDB(dbName);
                        break;
                    }
                }
            }
        } finally {
            System.out.print("Requesting getMongoDB() - Pankaj ");
        }
        return mongoDb;
    }

    public static DBCursor evaluateMDBHelper(DmHelper helper, DBCollection dBCollection, BasicDBObject filters, boolean blnEOF, DmRSS servletInstance) throws Exception {
        DBCursor dBCursor = null;
        BasicDBObject orderBydBo = new BasicDBObject();
        int intTemp;

        if (filters == null) {
            filters = new BasicDBObject();
        }

        if (helper.getFilter() != null) {
            for (int i = 0; i < helper.getFilter().size(); i++) {
                switch (helper.getFilter().get(i).getOperand()) {
                    case AND:
                        switch (helper.getFilter().get(i).getOperator()) {
                            case EQUAL:
                                filters.put(helper.getFilter().get(i).getColumnName(), DmBaseShared.xNull(helper.getFilter().get(i).getValue1()));
                                break;
                            case CONTAINS:
                                filters.put(helper.getFilter().get(i).getColumnName(), new BasicDBObject("$regex", DmBaseShared.xNull(helper.getFilter().get(i).getValue1()) + ".*").append("$options", "i"));
                                break;
                            case BEGINSWITH:
                                filters.put(helper.getFilter().get(i).getColumnName(), new BasicDBObject("$regex", "^" + DmBaseShared.xNull(helper.getFilter().get(i).getValue1())).append("$options", "i"));
                                break;
                        }
                        break;
                }
            }
        }

        if (helper.getSort() != null) {
            for (int i = 0; i < helper.getSort().size(); i++) {
                if (helper.getSort().get(i).getSortType() == DmEnum.DmSortType.DESC) {
                    orderBydBo.append(helper.getSort().get(i).getColumnName().trim(), -1);
                } else {
                    orderBydBo.append(helper.getSort().get(i).getColumnName().trim(), 1);
                }
            }
        }

//        if (helper.getPaging().getResultFormat() == DmPaging.Result.PRINT) {
        if (helper.getPaging().getResultFormat() == DmPaging.Result.XLS) {

        } else {
            if (filters != null) {
                dBCursor = dBCollection.find(filters);
            } else {
                dBCursor = dBCollection.find();
            }

            if (orderBydBo != null) {
                dBCursor = dBCursor.sort(orderBydBo);
            }

            if (blnEOF) {
                helper.setRowCount(dBCursor.count());
                intTemp = Integer.valueOf(String.valueOf(helper.getRowCount() / helper.getRowsPerPage()));
                intTemp = intTemp * helper.getRowsPerPage();
                if ((helper.getRowCount() % helper.getRowsPerPage()) == 0) {
                    intTemp = intTemp - helper.getRowsPerPage();
                }
                helper.setRowsStartFrom(intTemp);
            }

            dBCursor = dBCursor.skip(helper.getRowsStartFrom()).limit(helper.getRowsPerPage());
        }

        return dBCursor;
    }

    public static DBCursor mdbCursor(DBCollection dBCollection, BasicDBObject filters, DmRSS servletInstance) throws Exception {
        DBCursor dBCursor = null;
        if (filters == null) {
            filters = new BasicDBObject();
        }

        if (filters != null) {
            dBCursor = dBCollection.find(filters);
        } else {
            dBCursor = dBCollection.find();
        }

        return dBCursor;
    }

    public static String getSsnLocationCodeList(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("AllowedLocationCodeList"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    //==========================================Log Management=================================
    public static String getSsnId(DmRSS rssTemp) {
        String strRtn = "";

        try {
            strRtn = DmBaseShared.xNull(((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getId());
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String getSsnLocalIPAddress(DmRSS rssTemp) {
        String strRtn = null;

        try {
            strRtn = (String) (((DmRequestWrapper) rssTemp.getLocalRequest()).getDatamanSession().getAttribute("LocalIPAddress"));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return strRtn;
    }

    public static String formatTimestamp(Timestamp dateTime) {
        if (dateTime != null) {
            return new SimpleDateFormat("dd-MMM-yyyy HH.mm").format(new Date(dateTime.getTime()));
        }
        return null;
    }

    public static String removeAllCharacterExceptAlphaNumeric(String stringToReplace) throws Exception {
        return replaceAllCharactersExceptAlphaNumeric(stringToReplace, "");
    }

    public static String replaceAllCharactersExceptAlphaNumeric(String stringToReplace, String stringWithReplace) throws Exception {
        String retVal = "";
        try {
            String regEx = "[^a-zA-Z0-9]";
            retVal = DmBaseShared.xNull(stringToReplace).replaceAll(regEx, DmBaseShared.xNull(stringWithReplace).trim());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ex);
        }
        return retVal;
    }

    public static DBCursor evaluateHelperDB(DmHelper helper, DBCollection dBCollection, BasicDBObject filters, boolean blnEOF, DmRSS servletInstance) throws Exception {
        DBCursor dBCursor = null;
        BasicDBObject orderBydBo = new BasicDBObject();
        int intTemp;

        if (filters == null) {
            filters = new BasicDBObject();
        }

        if (helper.getFilter() != null) {
            for (int i = 0; i < helper.getFilter().size(); i++) {
                switch (helper.getFilter().get(i).getOperand()) {
                    case AND:
                        switch (helper.getFilter().get(i).getOperator()) {
                            case EQUAL:
                                filters.put(helper.getFilter().get(i).getColumnName(), DmBaseShared.xNull(helper.getFilter().get(i).getValue1()));
                                break;
                            case CONTAINS:
                                filters.put(helper.getFilter().get(i).getColumnName(), new BasicDBObject("$regex", DmBaseShared.xNull(helper.getFilter().get(i).getValue1()) + ".*").append("$options", "i"));
                                break;
                            case BEGINSWITH:
                                filters.put(helper.getFilter().get(i).getColumnName(), new BasicDBObject("$regex", "^" + DmBaseShared.xNull(helper.getFilter().get(i).getValue1())).append("$options", "i"));
                                break;
                        }
                        break;
                }
            }
        }

        if (helper.getSort() != null) {
            for (int i = 0; i < helper.getSort().size(); i++) {
                if (helper.getSort().get(i).getSortType() == DmEnum.DmSortType.DESC) {
                    orderBydBo.append(helper.getSort().get(i).getColumnName().trim(), -1);
                } else {
                    orderBydBo.append(helper.getSort().get(i).getColumnName().trim(), 1);
                }
            }
        }

        if (filters != null) {
            dBCursor = dBCollection.find(filters);
        } else {
            dBCursor = dBCollection.find();
        }

        if (orderBydBo != null) {
            dBCursor = dBCursor.sort(orderBydBo);
        }

        if (helper.getPaging().getResultFormat() == DmPaging.Result.XLS) {

        } else {
            if (blnEOF) {
                helper.setRowCount(dBCursor.count());
                intTemp = Integer.valueOf(String.valueOf(helper.getRowCount() / helper.getRowsPerPage()));
                intTemp = intTemp * helper.getRowsPerPage();
                if ((helper.getRowCount() % helper.getRowsPerPage()) == 0) {
                    intTemp = intTemp - helper.getRowsPerPage();
                }
                helper.setRowsStartFrom(intTemp);
            }

            dBCursor = dBCursor.skip(helper.getRowsStartFrom()).limit(helper.getRowsPerPage());
        }

        return dBCursor;
    }

}
