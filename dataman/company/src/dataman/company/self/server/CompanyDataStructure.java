package dataman.company.self.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Calendar;

public class CompanyDataStructure {

    private DmRefreshDataStructure refreshDataStructure;

    public CompanyDataStructure(DmRefreshDataStructure refreshDataStructure) throws Exception {
        this.refreshDataStructure = refreshDataStructure;

        alignmentMaster("alignmentMaster");
        productLicensedTo("productLicensedTo");
        company("company");
        branchMast("branchMast");
        siteMast("siteMast");
        moduleMaster("moduleMaster");
        report_Filter_DataType("report_Filter_DataType");
        report_For("report_For");
        report_Help_DataType("report_Help_DataType");
        report_HGColFormat("report_HGColFormat");
        report_Initialize("report_Initialize");
        report_Module_Menu("report_Module_Menu");
//        user_Control_Permission("user_Control_Permission");
        user_Control_Permission_Web("user_Control_Permission_Web");
//        user_Favorite("user_Favorite");
        user_Favorite_Web("user_Favorite_Web");
//        user_Permission("user_Permission");
        user_Permission_Web("user_Permission_Web");
        user_Restrict_System("user_Restrict_System");
        user_Voucher_Permission("user_Voucher_Permission");
        userNature("userNature");
        userMast("userMast");
        userSite("userSite");
        userLinkAccount("userLinkAccount");
        userPLTConfig("userPLTConfig");
        fileTypeMast("fileTypeMast");
        schedulerMast("schedulerMast");
        linkVerification("linkVerification");
        menuCodification("menuCodification");
        backupInfo("backupInfo");
//        searchColumnFormatting("searchColumnFormatting");
//        searchColumnAdvance("searchColumnAdvance");
//        formGridColumnFormatting("formGridColumnFormatting");
//        userLoginLog("userLoginLog");
//        dmScheduler("dmScheduler");
    }

    private void backupInfo(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("elementType", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("elementName", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("elementLocation", SQLDataType.VARCHAR, "500", null, null, null);
        configDataStructure.setColumnValue("backupLocation", SQLDataType.VARCHAR, "500", null, null, null);
    }

    private void menuCodification(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "250", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "250", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuShortHand", SQLDataType.VARCHAR, "6", null, null, null);

    }

    private void linkVerification(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("keyField", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("mobile", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("eMail", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("pltCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("compCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("siteCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("expiryDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("OTP", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("regDocID", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("tempRefundAmt", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setForeignKey("pltCode", "pltCode", "productLicensedTo");
        configDataStructure.setForeignKey("compCode", "comp_Code", "company");
        configDataStructure.setForeignKey("siteCode", "code", "siteMast");
    }

    private void schedulerMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("PLTCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("Name", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("exeType", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setColumnValue("WSTCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("startDateTime", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("useExpiry", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("expDateTime", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("isAct", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("jobType", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("selSiteCodes", SQLDataType.VARCHAR, "100", null, null, null);

//        configDataStructure.setColumnValue("recurQuan", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("recurQuantity", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("recurUnit", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("schType", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setColumnValue("selDays", SQLDataType.VARCHAR, "60", null, null, null);
        configDataStructure.setColumnValue("dayOrMonth", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("selMonths", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("selDates", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("selWeeks", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("lastExecTime", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("isExecuted", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setColumnValue("responsibleUser", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDate", SQLDataType.DATETIME, null, null, null, null);

    }

    private void alignmentMaster(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
    }

    private void productLicensedTo(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("pltCode", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("comp_Name", SQLDataType.VARCHAR, "75", null, null, null);
        configDataStructure.setColumnValue("sName", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("address1", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("address2", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("city", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("regsitartionType", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("stateName", SQLDataType.VARCHAR, "30", null, null, null);
//        configDataStructure.setColumnValue("stateNumericCode", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("country", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("pin", SQLDataType.VARCHAR, "6", null, null, null);
//        configDataStructure.setColumnValue("phone", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("fax", SQLDataType.VARCHAR, "25", null, null, null);
//        configDataStructure.setColumnValue("lstNo", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("lstDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("cstNo", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("cstDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("drugLicense", SQLDataType.VARCHAR, "50", null, null, null);
////        configDataStructure.setColumnValue("serialKeyNo", SQLDataType.VARCHAR, "25", null, null, null);
//        configDataStructure.setColumnValue("eMAil", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("gram", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("desc1", SQLDataType.VARCHAR, "60", null, null, null);
//        configDataStructure.setColumnValue("desc2", SQLDataType.VARCHAR, "60", null, null, null);
//        configDataStructure.setColumnValue("desc3", SQLDataType.VARCHAR, "60", null, null, null);
//        configDataStructure.setColumnValue("eccCode", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("exDivision", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("exRange", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("exRegNo", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("excoll", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("panNo", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("notificationNo", SQLDataType.VARCHAR, "25", null, null, null);
//        //configDataStructure.setColumnValue("site_Code", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("plaNo", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("cenvatNo", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("tinNo", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("workAddress1", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("workAddress2", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("workCity", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("workCountry", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("workPin", SQLDataType.VARCHAR, "6", null, null, null);
//        configDataStructure.setColumnValue("workPhone", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("workFax", SQLDataType.VARCHAR, "25", null, null, null);
//        configDataStructure.setColumnValue("ieCode", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("rbiCode", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("tinDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("cin", SQLDataType.VARCHAR, "15", null, null, null);
//        configDataStructure.setColumnValue("gstin", SQLDataType.VARCHAR, "15", null, null, null);
//        configDataStructure.setColumnValue("gstinDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("tallyIntegrationDBName", SQLDataType.VARCHAR, "200", null, null, null);
//        configDataStructure.setColumnValue("tallyCompanyName", SQLDataType.VARCHAR, "200", null, null, null);
//        configDataStructure.setColumnValue("dashboard_URL", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("latestCompCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("latestCompCode", "comp_Code", "company");
    }

    private void company(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("comp_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("pltCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("centralData_Path", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("centralFileData_Path", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("start_Dt", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("end_Dt", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("cYear", SQLDataType.VARCHAR, "9", null, null, null);
        configDataStructure.setColumnValue("pYear", SQLDataType.VARCHAR, "9", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setColumnValue("majorCurrency", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("minorCurrency", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setForeignKey("pltCode", "pltCode", "productLicensedTo");
    }

    private void branchMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "75", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("city", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
    }

    private void siteMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("pltCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("branchCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("siteDisplayName", SQLDataType.VARCHAR, "75", null, null, null);
        configDataStructure.setColumnValue("siteDesc", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("isHO", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "4", null, null, null);
        configDataStructure.setColumnValue("address1", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("address2", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("city", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("currencyCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("pin", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("regionalLanguage", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("eCCCode", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("exRange", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("exRegNo", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("exDivision", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("exColl", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("panNo", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("lstNo", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("lstDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("cstNo", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("cstDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("tinNo", SQLDataType.VARCHAR, "12", null, null, null);
        configDataStructure.setColumnValue("tinDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("cin", SQLDataType.VARCHAR, "15", null, null, null);
        configDataStructure.setColumnValue("gstin", SQLDataType.VARCHAR, "15", null, null, null);
        configDataStructure.setColumnValue("gstinDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("notificationNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("phone", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("fax", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("email", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("gram", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("registrationType", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("stateName", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("stateNumericCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("country", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("drugLicenseNo", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("clientCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("serialKey", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("ieCode", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("rbiCode", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("pltCode", "pltCode", "productLicensedTo");
        configDataStructure.setForeignKey("branchCode", "code", "branchMast");
    }

    private void moduleMaster(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "20", SQLColumnType.PRIMARY, null, null);
    }

    private void fileTypeMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("ruleCode", SQLDataType.VARCHAR, "5", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("description", SQLDataType.VARCHAR, "500", null, null, null);
        configDataStructure.setColumnValue("fileType", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("maxSize", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("isDefault", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("extFilters", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
    }

    private void report_Filter_DataType(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "10", null, null, null);
    }

    private void report_For(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
    }

    private void report_Help_DataType(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "20", null, null, null);
    }

    private void report_HGColFormat(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "20", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("fieldCode", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("colIndex", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("headerText", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("colWidth", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("colAlignment", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("isColDisplay", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("colAlignment", "code", "alignmentMaster");
    }

    private void report_Initialize(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "20", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("rowIndex", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("fieldCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("fieldName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("fieldDataType", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("fieldFilterDataType", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("defaultValue", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("isDisplay", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("dMHGQuery", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("dMHGHeight", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("dMHGWidth", SQLDataType.SMALLINT, null, null, null, null);

        configDataStructure.setForeignKey("fieldDataType", "code", "report_Help_DataType");
        configDataStructure.setForeignKey("fieldFilterDataType", "code", "report_Filter_DataType");
    }

    private void report_Module_Menu(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "20", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("mnuLevel", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("parent", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("reportFor", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("noOfSubReport", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
    }

//    public void user_Control_Permission(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("groupText", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("groupName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("comp_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//
//        configDataStructure.setForeignKey("userName", "user_Name", "userMast");
//
//    }
    private void user_Control_Permission_Web(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("groupText", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("groupName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("comp_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);

        configDataStructure.setForeignKey("userName", "user_Name", "userMast");
    }

    private void user_Favorite_Web(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("moduleSNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("menuSNo", SQLDataType.SMALLINT, null, null, null, null);

        configDataStructure.setForeignKey("userName", "user_Name", "userMast");

    }

//    public void user_Favorite(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("shortcut", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//
//        configDataStructure.setForeignKey("userName", "user_Name", "userMast");
//    }
//    public void user_Permission(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("mnuLevel", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("parent", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("permission", SQLDataType.VARCHAR, "4", null, null, null);
//        configDataStructure.setColumnValue("reportFor", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
//        configDataStructure.setColumnValue("comp_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//
//        configDataStructure.setForeignKey("userName", "user_Name", "userMast");
//
//    }
    private void user_Permission_Web(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("comp_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuModule", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("mnuLevel", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("parent", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("permission", SQLDataType.VARCHAR, "4", null, null, null);
        configDataStructure.setColumnValue("reportFor", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("vp_Days", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("sp_days", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("dp_Days", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("allowValueUpto", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");

        configDataStructure.setForeignKey("userName", "user_Name", "userMast");

        configDataStructure.setIndex("userName,mnuModule,mnuName");
    }

    private void user_Restrict_System(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("user_Name", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("ipAddress", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("computerName", SQLDataType.VARCHAR, "50", null, null, null);

        configDataStructure.setForeignKey("user_Name", "user_Name", "userMast");
    }

    private void user_Voucher_Permission(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("comp_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("user_Name", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("vTypeSName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("category", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("vp_Days", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("sp_days", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("dp_Days", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("allowValueUpto", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isAllowAudit", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("comp_Code", "comp_Code", "company");
        configDataStructure.setForeignKey("user_Name", "user_Name", "userMast");

    }

    private void userNature(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setUnique("manualCode");
    }

    private void userMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("user_Name", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("eMail", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("mobile", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("passWd", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("description", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("tokenKey", SQLDataType.VARCHAR, "36", null, null, null);
        configDataStructure.setColumnValue("isAdmin", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("userName_Linked", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("user_Role", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("UserLockTimeOut", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("nature", "code", "userNature");
    }

    private void userSite(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("user_Name", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("compCode", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sitelist", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("costCenterList", SQLDataType.VARCHARMAX, null, null, null, null);
//        configDataStructure.setColumnValue("linkedURL", SQLDataType.VARCHAR, "250", null, null, null);

        configDataStructure.setForeignKey("compCode", "comp_Code", "company");
        configDataStructure.setForeignKey("user_Name", "user_Name", "userMast");
    }

    private void userLinkAccount(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("user_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("pltCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("nursingStationCodeList", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("allowedDepartmentCodeList", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("allowedDiscountPercentUpto", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("allowDiscForNonDiscServ", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("allowedLocationCodeList", SQLDataType.VARCHARMAX, null, null, null, null);

        configDataStructure.setForeignKey("pltCode", "pltCode", "productLicensedTo");
        configDataStructure.setForeignKey("user_Name", "user_Name", "userMast");
//        configDataStructure.setUnique("pltCode,subCode");

    }

    private void userPLTConfig(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("pltCode", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("emailId", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("cc", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("bcc", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("password", SQLDataType.VARCHAR, "50", null, null, null);

        configDataStructure.setForeignKey("pltCode", "pltCode", "productLicensedTo");
        configDataStructure.setForeignKey("userName", "user_Name", "userMast");
    }

//    public void searchColumnFormatting(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("searchName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("colIndex", SQLDataType.SMALLINT_UNSIGNED, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("colWidth", SQLDataType.SMALLINT_UNSIGNED, null, null, null, null);
//        configDataStructure.setColumnValue("colDisplayIndex", SQLDataType.SMALLINT_UNSIGNED, null, null, null, null);
//        configDataStructure.setColumnValue("colWrap", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("colHide", SQLDataType.VARCHAR, "1", null, null, null);
//
//        configDataStructure.setForeignKey("user_Name", "user_Name", "userMast");
//    }
//
//    public void searchColumnAdvance(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("searchName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("scType", SQLDataType.VARCHAR, "2", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("sNo", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("columnName", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("columnDataType", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("scCondition", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("scToDo", SQLDataType.SMALLINT_UNSIGNED, null, null, null, null);
//        configDataStructure.setColumnValue("scValue1", SQLDataType.VARCHAR, "250", null, null, null);
//        configDataStructure.setColumnValue("scValue2", SQLDataType.VARCHAR, "250", null, null, null);
//
//        configDataStructure.setForeignKey("user_Name", "user_Name", "userMast");
//    }
//
//    public void formGridColumnFormatting(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("baseName", SQLDataType.VARCHAR, "100", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("gridName", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("colIndex", SQLDataType.SMALLINT_UNSIGNED, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("colWidth", SQLDataType.SMALLINT_UNSIGNED, null, null, null, null);
//        configDataStructure.setColumnValue("colDisplayIndex", SQLDataType.SMALLINT_UNSIGNED, null, null, null, null);
//        configDataStructure.setColumnValue("colHide", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("columnName", SQLDataType.VARCHAR, "50", null, null, null);
//    }
//    public void userLoginLog(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("userSessionId", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("login_Time", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("logoff_Time", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("localIPAddress", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("externalIPAddress", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("systemName", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("logOffType", SQLDataType.VARCHAR, "1", null, null, null);
//    }
//    public void dmScheduler(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("code", SQLDataType.VARCHAR, "6", SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("wstCode", SQLDataType.VARCHAR, "6", null, null, null);
//        configDataStructure.setColumnValue("uniqueValueFilter", SQLDataType.VARCHAR, "250", null, null, null);
//        configDataStructure.setColumnValue("pLTCode", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("dmsAction", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("active", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("mode", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
//    }
    public void initilizer(String centralData_Path, String centralFileData_Path) throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", yearFull = "", yearShort = "";
        Calendar cal = null;
        try {
            cnTemp = DmServer.getConnection(refreshDataStructure.getDatabaseName());
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From UserMast;", cnTemp)) <= 0) {
                sqlQuery = "Insert Into UserMast(Code,User_Name,PassWd,Description,isAdmin)Values('1','SA',convert(varchar(32),HashBytes('MD5','sasa'),2),'System Administrator','1'); ";
                DmServer.executeSQL(sqlQuery, cnTemp);
                sqlQuery = "Insert Into UserMast(Code,User_Name,PassWd,Description,isAdmin)Values('2','SUPER',convert(varchar(32),HashBytes('MD5','supersuper'),2),'Super Admin','1'); ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From productLicensedTo;", cnTemp)) <= 0) {
                sqlQuery = "Insert Into productLicensedTo(pltCode,comp_Name,sName)Values('1','Dataman Demonstration Package','DDP'); ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From company;", cnTemp)) <= 0) {
                cal = Calendar.getInstance();
                if (cal.get(Calendar.MONTH) == Calendar.JANUARY || cal.get(Calendar.MONTH) == Calendar.FEBRUARY || cal.get(Calendar.MONTH) == Calendar.MARCH) {
                    yearFull = String.valueOf(Calendar.getInstance().get(Calendar.YEAR) - 1);
                } else {
                    yearFull = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                }
                yearShort = yearFull.substring(2);

                sqlQuery = "insert into company (comp_Code, pltCode, centralData_Path, centralFileData_Path, start_Dt, end_Dt, cYear, pYear, "
                        + "                     v_Prefix, preparedBy, u_EntDt) values (1, 1, "
                        //                        + " concat('DDP_','" + yearShort + "','" + DmServer.vNull(DmShared.vNull(yearShort) + 1, "00") + "'), 'DDP', "
                        + "'" + centralData_Path + "', '" + centralFileData_Path + "', "
                        + "'" + DmServer.convertDate("01-Apr-" + yearFull) + "',"
                        + "'" + DmServer.convertDate("31-Mar-" + DmServer.vNull(DmShared.vNull(yearFull) + 1, "0000")) + "',"
                        + "'" + yearFull + "-" + DmServer.vNull(DmShared.vNull(yearShort) + 1, "00") + "',"
                        + "'" + DmServer.vNull(DmShared.vNull(yearFull) - 1, "0000") + "-" + DmServer.vNull(DmShared.vNull(yearShort), "00") + "',"
                        + "'" + yearShort + "', 'SA', GETDATE())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "update productLicensedTo set latestCompCode = 1 where pltCode = '1'";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From BranchMast;", cnTemp)) <= 0) {
                sqlQuery = "Insert Into BranchMast(code,manualCode,name,city,u_Name,u_entdt)Values('1','HO','Head office','Kanpur','SA',getdate()); ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From siteMast", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO siteMast (code, name, manualCode, isHo,pltCode,branchCode, regionalLanguage,"
                        + "siteDisplayName, stateName, stateNumericCode, city, registrationType, gstin, gstinDate, panNo) "
                        + "VALUES ('0001', 'Head office [DDP]', 'HO', '1','1','1', '" + RegionalLanguage.HINDI.toString() + "',"
                        + "'Dataman Demonstration Package', 'Uttar Pradesh', '9', 'Kanpur', 'R', '09AAACD5213H1ZY', '" + DmServer.convertDate("01-Apr-" + yearFull) + "', 'AAACD5213H')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From AlignmentMaster;", cnTemp)) <= 0) {

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (0, 'NotSet');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (1, 'TopLeft');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (2, 'TopCenter');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (4, 'TopRight');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (16, 'MiddleLeft');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (32, 'MiddleCenter');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (64, 'MiddleRight');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (256, 'BottomLeft');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (512, 'BottomCenter');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO AlignmentMaster (Code, Name)VALUES (1024, 'BottomRight');";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From ModuleMaster;", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO ModuleMaster (MnuModule)VALUES ('Report');";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Report_For;", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Accounts');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Machine');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Master');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Production');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Purchase');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Report');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Sales');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_For (Name)VALUES ('Stock');";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Report_Filter_DataType;", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO Report_Filter_DataType (Code, Name)VALUES (0, 'None');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Filter_DataType (Code, Name)VALUES (1, 'Numeric');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Filter_DataType (Code, Name)VALUES (2, 'String');";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Report_Help_DataType;", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO Report_Help_DataType (Code, Name)VALUES (0, 'Date');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Help_DataType (Code, Name)VALUES (1, 'Numeric');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Help_DataType (Code, Name)VALUES (2, 'Float');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Help_DataType (Code, Name)VALUES (3, 'String');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Help_DataType (Code, Name)VALUES (4, 'None');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Help_DataType (Code, Name)VALUES (5, 'Single Selection');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO Report_Help_DataType (Code, Name)VALUES (6, 'Multi Selection');";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From userNature;", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO userNature (Code, manualcode, Name) VALUES ('001','CT','Customer');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO userNature (Code, manualcode, Name) VALUES ('002','EM','Employee');";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }
        } catch (Exception ex) {
            refreshDataStructure.addResultLog("initilizer", "", this.getClass().getName(), sqlQuery, ex.getMessage());
            throw new DmException(0, ex.getMessage());
        } finally {
            if (rsTemp != null) {
                rsTemp.close();
            }
            if (psTemp != null) {
                psTemp.close();
            }
            if (cnTemp != null && !cnTemp.isClosed()) {
                cnTemp.close();
            }
        }
    }
}
