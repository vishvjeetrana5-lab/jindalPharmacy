package dataman.utility.self.server;

import dataman.dmBase.self.shared.*;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;

import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;

public class UtilityDataStructure {

    private DmRefreshDataStructure refreshDataStructure;

    public UtilityDataStructure(DmRefreshDataStructure refreshDataStructure) throws Exception {
        this.refreshDataStructure = refreshDataStructure;

        voucherType("voucher_Type");
        siteWiseReportConfig("siteWiseReportConfig");
        voucherTypeFormList("voucherTypeFormList");
        acFilteration("acFilteration");
        itemCatFilteration("itemCatFilteration");

        numberSystem("numberSystem");
        numberSystemFormat("numberSystemFormat");
        numberSystemCounter("numberSystemCounter");

        currency("currency");
        siteMast("siteMast");
        docIdMaster("docIdMaster");
        codeMaster("codeMaster");

        gsts("gsts");
        its("its");
        itsType("itsType");
        itsDetail("itsDetail");
        taxFormMast("taxFormMast");
        taxFormSiteList("taxFormSiteList");
        overHeadMast("overHeadMast");

        overHeadConfigName("overHeadConfigName");
        overHeadConfigSite("overHeadConfigSite");
        overHeadConfig("overHeadConfig");

        overHeadLineConfigName("overHeadLineConfigName");
        overHeadLineConfigSite("overHeadLineConfigSite");
        overHeadLineConfig("overHeadLineConfig");
        overHead("overHead");
        overHeadLine("overHeadLine");
        overHeadMA("overHeadMA");

        guiTemplateMast("guiTemplateMast");
        guiConfig("guiConfig");
        guiConfigDetail("guiConfigDetail");
        guiConfigApplicableTo("guiConfigApplicableTo");
        guiConfigDetailHelp("guiConfigDetailHelp");

        linkFileCollection("linkFileCollection");
        guiValueTemplateMast("guiValueTemplateMast");
        modifyReasonMast("modifyReasonMast");
        modifyReason("modifyReason");

        cardTypeMast("cardTypeMast");
        cardStock("cardStock");
        cardRegistration("cardRegistration");
        cardLedger("cardLedger");
    }

    private void cardTypeMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("shortName", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("cardType", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("isPhysicallyRequired", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isSMSAlert", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isEMailAlert", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAccountEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("currencyCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("onAmount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("points", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("valuePerPoint", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("validDays", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("currencyCode", "code", "currency");
    }

    private void cardLedger(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("amtDr", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("amtCr", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("billAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("narration", SQLDataType.VARCHAR, "250", null, null, null);
//        configDataStructure.setColumnValue("cardList", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("cardTypeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("cardRegDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("physicalId", SQLDataType.NVARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("cardNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("cardHolderName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("cardAuthorization", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("cardDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("conversionRate", SQLDataType.DECIMAL, "18,13", null, null, null);
        configDataStructure.setColumnValue("currencyCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("currencyAmount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("cType", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("cardNo_OutSide", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("cardName_OutSide", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("cardRegDocId", "docId", "cardRegistration");
        configDataStructure.setForeignKey("cardTypeCode", "code", "cardTypeMast");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
//        configDataStructure.setForeignKey("cardNo", "cardNo", "cardStock");
        configDataStructure.setForeignKey("currencyCode", "code", "currency");

        configDataStructure.setIndex("cardRegDocId");
    }

    private void cardRegistration(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("physicalId", SQLDataType.NVARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("uhid", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("cardNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("cardTypeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("expiryDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("initialPrefix", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("personName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("address", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("cityCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("mobile", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("eMail", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("dob", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("doa", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("cardTypeCode", "code", "cardTypeMast");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("cityCode", "cityCode", "city");
//        configDataStructure.setForeignKey("uhid", "docId", "patientUHID");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");

        configDataStructure.setIndex("cardNo");
    }

    private void cardStock(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("cardNo", SQLDataType.VARCHAR, "20", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("recDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("cardRegDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("cardRegDocId", "docId", "cardRegistration");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");

        configDataStructure.setIndex("cardRegDocId");
    }

    private void modifyReasonMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    private void modifyReason(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Sno", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("modifyReasonCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("modifyReason", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("preChangeValue", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("postChangeValue", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("preU_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preU_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("mode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("uniqueKey", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("fieldName", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("entryPointName", SQLDataType.VARCHAR, "50", null, null, null);

        configDataStructure.setForeignKey("modifyReasonCode", "code", "modifyReasonMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
    }

    private void guiTemplateMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    private void guiValueTemplateMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("guiConfigCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("guiConfigCode", "code", "guiConfig");
    }

    private void linkFileCollection(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("fileLink", SQLDataType.VARCHAR, "50", SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("ruleCode", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("fileType", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("fileName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("fileSize", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("remarks", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("tempDocId", SQLDataType.VARCHAR, "36", null, null, null);
        configDataStructure.setColumnValue("sessionId", SQLDataType.VARCHAR, "36", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    private void guiConfig(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("reportNamePrefix", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("tableName", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("tableKeyField", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("sqlQuery", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    private void guiConfigDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("guiRowNo", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("saveInfieldName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("fieldName ", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("fieldCaption", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("fieldDataType", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("customFieldName", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("isMandatory", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isCopy", SQLDataType.TINYINT, null, null, null, "1");
        configDataStructure.setColumnValue("maxLength", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("captionStyle", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("captionWidth", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("controlStyle", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("controlWidth", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isAllowNegative", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("allowDecimalPlaces", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("defaultText", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("defaultCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("viewFieldCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("viewFieldText", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("helpSqlQuery", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("helpWidth", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("helpRowPerPage", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    private void guiConfigApplicableTo(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("guiTemplateCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("site_code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);

        configDataStructure.setForeignKey("guiTemplateCode", "code", "guiTemplateMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
    }

    private void guiConfigDetailHelp(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("guiRowNo", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sno", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("fieldCaption", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("fieldWidth", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("fieldAlignment", SQLDataType.TINYINT, null, null, null, null);

    }

    private void taxFormMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("L_C", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("trnType", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("formTrnType", SQLDataType.VARCHAR, "1", null, null, null);

        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    private void taxFormSiteList(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("taxCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);

        configDataStructure.setForeignKey("taxCode", "code", "taxFormMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
    }

    private void overHeadMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("isSystemGenerated", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setUnique("manualCode");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
    }

    private void overHeadConfigName(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("Name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("v_type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("v_type", "v_Type", "voucher_Type");
    }

    private void overHeadConfigSite(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);

        configDataStructure.setUnique("code,site_Code");
        configDataStructure.setForeignKey("site_Code", "code", "SiteMast");
    }

    private void overHeadConfig(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("applicableFrom", SQLDataType.DATE, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("applicableTo", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("overHeadCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sno", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("atRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("atRateAmount", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("contraOhMCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("formula", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("drCr", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("atRateSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("amountSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("tableName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isAtRateEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isOverHeadAliasEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAcEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isShowFormulaValue", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("roundOff", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("ohSetUp", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("UniqueId", SQLDataType.VARCHAR, "4", null, null, null);
        configDataStructure.setColumnValue("isAtRateMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isPrintable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("calLValOn", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setColumnValue("isPartialPosting", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("overHeadCode", "code", "overHeadMast");
        configDataStructure.setForeignKey("subCode", "subcode", "subGroup");
    }

    private void overHeadLineConfigName(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("Name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("v_type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("v_type", "v_Type", "voucher_Type");
    }

    private void overHeadLineConfigSite(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);

        configDataStructure.setUnique("code,site_Code");
        configDataStructure.setForeignKey("site_Code", "code", "SiteMast");
    }

    private void overHeadLineConfig(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("applicableFrom", SQLDataType.DATE, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("applicableTo", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("overHeadCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sno", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("atRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("atRateAmount", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("formula", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("atRateSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("amountSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("overHeadSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("acSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("tableName", SQLDataType.VARCHAR, "50", null, null, null);

        configDataStructure.setColumnValue("isAtRateEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
//        configDataStructure.setColumnValue("isoverHeadEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isOverHeadAliasEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAcEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isShowFormulaValue", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("roundOff", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("UniqueId", SQLDataType.VARCHAR, "4", null, null, null);
        configDataStructure.setColumnValue("isAtRateMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isPrintable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");

        configDataStructure.setForeignKey("overHeadCode", "code", "overHeadMast");
        configDataStructure.setForeignKey("subCode", "subcode", "subGroup");
    }

    private void overHead(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sno", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("overHeadCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("overHeadMCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("atRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("contraOhMCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("formula", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("actualFormula", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("excludeFormula", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("drCr", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("atRateSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("amountSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("tableName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isAtRateEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isOverHeadAliasEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAcEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isShowFormulaValue", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("roundOff", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("ohSetUp", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("UniqueId", SQLDataType.VARCHAR, "4", null, null, null);
        configDataStructure.setColumnValue("isAtRateMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isPrintable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("overheadAlias", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("calLValOn", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setColumnValue("isPartialPosting", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("overHeadCode", "code", "overHeadMast");
        configDataStructure.setForeignKey("subCode", "subcode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "SiteMast");
    }

    private void overHeadMA(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("vSNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("subCode", "subcode", "subGroup");
    }

    private void overHeadLine(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("vSNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("overHeadCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("overHeadMCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("atRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("formula", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("atRateSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("amountSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("overHeadSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("acSaveIn", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("tableName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isAtRateEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isOverHeadAliasEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAcEditable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isShowFormulaValue", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("roundOff", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("UniqueId", SQLDataType.VARCHAR, "4", null, null, null);
        configDataStructure.setColumnValue("isAtRateMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isAmountMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isPrintable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("overheadAlias", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("overHeadCode", "code", "overHeadMast");
        configDataStructure.setForeignKey("subCode", "subcode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "SiteMast");
    }

    private void gsts(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("locationCode", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("gstTypeCode", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("gstHeadCode", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("atRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseSalesAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("inputTaxAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("outputTaxAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("rcmAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setIndex("purchaseSalesAc");
        configDataStructure.setIndex("inputTaxAc");
        configDataStructure.setIndex("outputTaxAc");
        configDataStructure.setIndex("rcmAc");

        configDataStructure.setForeignKey("gstTypeCode", "code", "gstType");
        configDataStructure.setForeignKey("gstHeadCode", "code", "gstHead");
        configDataStructure.setForeignKey("purchaseSalesAc", "subcode", "subGroup");
        configDataStructure.setForeignKey("inputTaxAc", "subcode", "subGroup");
        configDataStructure.setForeignKey("outputTaxAc", "subcode", "subGroup");
        configDataStructure.setForeignKey("rcmAc", "subcode", "subGroup");
    }

    private void its(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("totalTaxPercent", SQLDataType.FLOAT, null, null, null, null);
    }

    private void itsType(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
    }

    private void itsDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("applicableFrom", SQLDataType.DATE, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("applicableTo", SQLDataType.DATE, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("overHeadCode", SQLDataType.INT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("locationCode", SQLDataType.SMALLINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("atRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("overHeadCode", "code", "overHeadMast");
//        configDataStructure.setForeignKey("taxFormCode", "code", "taxFormMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");

        configDataStructure.setUnique("code,applicableFrom,applicableTo,overHeadCode,locationCode,site_Code");
    }

    private void voucherType(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("description", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("category", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("parentVoucher", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("isBankCashBook", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("nCat", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("short_Name", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("isSystemDefine", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
//        configDataStructure.setColumnValue("reportName", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("procedureName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("mnuName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("mnuText", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("mnuAttachedInModule", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("acceptEntryAfter", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("defaultReport", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("paperSizeName", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("isLandScape", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("defaultAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("balancingAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("serialNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("guiTemplateCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("attachWJTemplates", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("attachPaymentMode", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("documentTypeCode", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("documentNatureCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("documentMailTemplateCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("documentMailUser", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("documentMailReportName", SQLDataType.VARCHAR, "50", null, null, null);

        //Unique Key
        configDataStructure.setUnique("short_Name");

        //Index Key
        configDataStructure.setIndex("defaultAc");
        configDataStructure.setIndex("balancingAc");

        //Foreign Key
        configDataStructure.setForeignKey("guiTemplateCode", "code", "guiTemplateMast");
        configDataStructure.setForeignKey("defaultAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("balancingAc", "subCode", "subGroup");

    }

    private void siteWiseReportConfig(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("v_type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("reportName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("pageSize", SQLDataType.VARCHAR, "20", null, null, null);

        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");

        configDataStructure.setUnique("v_Type,site_Code");
    }

    private void voucherTypeFormList(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("taxCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);

        configDataStructure.setForeignKey("taxCode", "code", "taxFormMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
    }

    private void acFilteration(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("nature", "code", "subGroupNature");
    }

    public void itemCatFilteration(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("itemCategoryCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("U_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("itemCategoryCode", "code", "itemCat");

    }

    private void numberSystem(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("nsGroup", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("includeV_Type", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("nsGroup", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
    }

    private void numberSystemFormat(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("nsGroup", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("nsFormat", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("counterFormat", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("isYearWise", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("isSiteWise", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");

        configDataStructure.setUnique("nsGroup,site_Code");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("nsGroup", "v_Type", "voucher_Type");

        configDataStructure.setIndex("nsGroup,site_Code");
    }

    private void numberSystemCounter(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("nsGroup", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("nsFormatValue", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("counter", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setUnique("nsGroup,v_prefix,site_Code,nsFormatValue");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("nsGroup", "v_Type", "voucher_Type");

        configDataStructure.setIndex("nsFormatValue,nsGroup,v_Prefix,site_Code");
    }

    private void currency(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("countryLocale", SQLDataType.VARCHAR, "7", null, null, null);
//        configDataStructure.setColumnValue("discription", SQLDataType.VARCHAR, "15", null, null, null);
//        configDataStructure.setColumnValue("smallCurrency", SQLDataType.VARCHAR, "8", null, null, null);
//        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("modifiedByRate", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
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
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("address1", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("address2", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("city", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("currencyCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("pin", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("regionalLanguage", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("eccCode", SQLDataType.VARCHAR, "50", null, null, null);
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
        configDataStructure.setColumnValue("eMail", SQLDataType.VARCHAR, "50", null, null, null);
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

        configDataStructure.setForeignKey("subCode", "subcode", "subGroup");
    }

    private void docIdMaster(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("serialNo", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");

        configDataStructure.setIndex("v_Type,v_Prefix,site_Code");
    }

    private void codeMaster(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("tablename", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("serialNo", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setUnique("tablename,site_Code,v_Prefix");

        configDataStructure.setIndex("tablename,v_Prefix,site_Code");
    }

    public void initilizer() throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", codeValue = "", codeValue1 = "";
        LibraryVoucherCategory lvc = new LibraryVoucherCategory();

        try {
            cnTemp = DmServer.getConnection(refreshDataStructure.getDatabaseName());

            if (DmShared.vNull(DmServer.executeScalar("select count(name) as cnt from sysTypes where name = 'fieldUDT'", cnTemp)) <= 0) {
                sqlQuery = "CREATE TYPE dbo.fieldUDT AS TABLE "
                        + "( "
                        + "	fieldCode varchar(30) primary key, "
                        + "	fieldName varchar(30) NULL, "
                        + "	fieldText varchar(max) NULL, "
                        + "	fieldTag varchar(max) NULL "
                        + ")";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("select count(name) as cnt from sysTypes where name = 'graphUDT'", cnTemp)) <= 0) {
                sqlQuery = "CREATE TYPE dbo.graphUDT AS TABLE (  "
                        + "	groupCode varchar(30) not null,  "
                        + "	groupName varchar(30) null, "
                        + "	labelCode varchar(30) not null,  "
                        + "	labelName varchar(30) null,  "
                        + "     dataValue float, "
                        + "     PRIMARY KEY (groupCode,labelCode) "
                        + ")";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.USER_MASTER + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Utility', '" + lvc.USER_MASTER + "', '" + codeValue + "', 'User Mast', '" + lvc.USER_MASTER + "', 1, 'SA', 'SA', getdate(), 'User Master','Utility') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.GUI_VALUE_TEMPLATE_MASTER + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Utility', '" + lvc.GUI_VALUE_TEMPLATE_MASTER + "', '" + codeValue + "', 'GUI Value', '" + lvc.GUI_VALUE_TEMPLATE_MASTER + "', 1, 'SA', 'SA', getdate(), 'GUI Templeate Value Master','Utility') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.CARD_REGISTRATION + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Master', '" + lvc.CARD_REGISTRATION + "', '" + codeValue + "', 'Card Registration', '" + lvc.CARD_REGISTRATION + "', 1, 'SA', 'SA', getdate(), 'Card Allotment','Master') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.CARD_DEACTIVE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Master', '" + lvc.CARD_DEACTIVE + "', '" + codeValue + "', 'Card Deactive', '" + lvc.CARD_DEACTIVE + "', 1, 'SA', 'SA', getdate(), 'Card Deactive','Master') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.CARD_DISCHARGE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Master', '" + lvc.CARD_DISCHARGE + "', '" + codeValue + "', 'Card Discharge', '" + lvc.CARD_DISCHARGE + "', 1, 'SA', 'SA', getdate(), 'Card Discharge','Master') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.CARD_RECHARGE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Master', '" + lvc.CARD_RECHARGE + "', '" + codeValue + "', 'Card Recharge', '" + lvc.CARD_RECHARGE + "', 1, 'SA', 'SA', getdate(), 'Card Recharge','Master') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.OTP + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Utility', '" + lvc.OTP + "', '" + codeValue + "', 'OTP', '" + lvc.OTP + "', 1, 'SA', 'SA', getdate(), '','') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From siteMast", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO siteMast (code, name, manualCode, isHo,pltCode,branchCode, regionalLanguage) "
                        + "VALUES ('0001', 'Head office [DDP]', 'HO', '1','1','1', '" + RegionalLanguage.HINDI.toString() + "')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//            if (DmShared.vNull(DmServer.executeScalar("Select count(*) as cnt From INFORMATION_SCHEMA.ROUTINES r where r.ROUTINE_SCHEMA = '" + refreshDataStructure.getDatabaseName() + "' and r.ROUTINE_TYPE In ('PROCEDURE','FUNCTION') and r.ROUTINE_NAME = 'getDocId'", cnTemp)) <= 0) {
            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getDocId' and xType = 'P'", cnTemp)) <= 0) {
                sqlQuery = "CREATE PROCEDURE [dbo].[getDocId]  (@vVoucherType smallint,  @vVoucherPrefix smallint,  @vSiteCode smallint) "
                        + "AS BEGIN "
                        + "SET NOCOUNT ON "
                        + "DECLARE  @vVoucherNo bigint "
                        + "Set @vVoucherNo = 0  "
                        + "UPDATE docidmaster SET @vVoucherNo = serialNo = ISNULL(serialNo, 0) + 1 "
                        + "WHERE  "
                        + "v_Type = @vVoucherType AND  "
                        + "v_Prefix = @vVoucherPrefix AND  "
                        + "site_Code = @vSiteCode "
                        + "IF ISNULL(@vVoucherNo, 0) <= 0 "
                        + "BEGIN "
                        + "SET @vVoucherNo = 1 "
                        + "INSERT docidmaster(v_Type, v_Prefix, site_Code, serialNo) "
                        + "VALUES (@vVoucherType, @vVoucherPrefix, @vSiteCode, @vVoucherNo) "
                        + "END "
                        + "Select @vVoucherNo as v_No "
                        + "END ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

//            if (DmShared.vNull(DmServer.executeScalar("Select count(*) as cnt From INFORMATION_SCHEMA.ROUTINES r where r.ROUTINE_SCHEMA = '" + refreshDataStructure.getDatabaseName() + "' and r.ROUTINE_TYPE In ('PROCEDURE','FUNCTION') and r.ROUTINE_NAME = 'getCode'", cnTemp)) <= 0) {
            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getCode' and xType = 'P'", cnTemp)) <= 0) {
                sqlQuery = "CREATE PROCEDURE [dbo].[getCode] (@vTableName varchar(50), @vSiteCode smallInt, @vPrefix tinyint) "
                        + "AS BEGIN "
                        + "SET NOCOUNT ON "
                        + "DECLARE @vSerialNo bigint "
                        + "Set @vSerialNo = 0  "
                        + "UPDATE codemaster SET "
                        + "@vSerialNo = serialNo = ISNULL(serialNo, 0) + 1 "
                        + "WHERE tablename = @vTableName and "
                        + "site_Code = @vSiteCode and "
                        + "isNull(v_Prefix,0) = isNull(@vPrefix,0) "
                        + "IF ISNULL(@vSerialNo, 0) <= 0 "
                        + "BEGIN "
                        + "SET @vSerialNo = 1 "
                        + "INSERT codemaster(tablename, site_Code, v_Prefix, serialNo) "
                        + "VALUES (@vTableName, @vSiteCode, isNull(@vPrefix,0), @vSerialNo) "
                        + "END "
                        + "Select @vSerialNo as v_No "
                        + "END ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getRecId' and xType = 'P'", cnTemp)) <= 0) {
                sqlQuery = "CREATE PROCEDURE [dbo].[getRecId]  (@nsGroup smallint,  @voucherPrefix tinyInt,  @siteCode smallint, @nsFormatValue as varchar(20)) "
                        + "AS "
                        + "BEGIN "
                        + "SET NOCOUNT ON "
                        + "DECLARE  @voucherNo bigint "
                        + "Set @voucherNo = 0  "
                        + "UPDATE	numberSystemCounter SET  "
                        + "@voucherNo = [counter] = isNull([counter], 0) + 1 "
                        + "WHERE  nsGroup = @nsGroup and "
                        + "isNull(v_Prefix,0) = isNull(@voucherPrefix,0) and "
                        + "isNull(site_Code,0) = isNull(@siteCode,0) and "
                        + "isNull(nsFormatValue,'') = isNull(@nsFormatValue,'') "
                        + "IF isNull(@voucherNo, 0) <= 0  "
                        + "BEGIN  "
                        + "SET @voucherNo = 1 "
                        + "INSERT numberSystemCounter(nsGroup, v_Prefix, site_Code, nsFormatValue, [counter]) VALUES  "
                        + "(@nsGroup, @voucherPrefix, @siteCode, isNull(@nsFormatValue,''),@voucherNo) "
                        + "END "
                        + "Select @voucherNo as v_No "
                        + "END ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From AcFilteration", cnTemp)) <= 0) {
                codeValue1 = DmServer.vNull(DmServer.executeScalar("Select code From subGroupNature sgn where sgn.name = 'Cash' ", cnTemp), "0");
                codeValue = DmServer.vNull(DmServer.executeScalar("Select v_type From Voucher_Type VT where short_Name = 'CPV' ", cnTemp), "0");
                if (Integer.valueOf(codeValue) > 0) {
                    sqlQuery = "INSERT INTO AcFilteration (V_Type, Nature, U_Name, U_EntDt) "
                            + "VALUES ('" + codeValue + "', '" + codeValue1 + "', 'SA', getdate())";
                    DmServer.executeSQL(sqlQuery, cnTemp);
                }

                codeValue = DmServer.vNull(DmServer.executeScalar("Select v_type From Voucher_Type VT where short_Name = 'CRV' ", cnTemp), "0");
                if (Integer.valueOf(codeValue) > 0) {
                    sqlQuery = "INSERT INTO AcFilteration (V_Type, Nature, U_Name, U_EntDt)"
                            + "VALUES ('" + codeValue + "', '" + codeValue1 + "', 'SA', getdate())";
                    DmServer.executeSQL(sqlQuery, cnTemp);
                }

                codeValue1 = DmServer.vNull(DmServer.executeScalar("Select code From subGroupNature sgn where sgn.name = 'Bank' ", cnTemp), "0");
                codeValue = DmServer.vNull(DmServer.executeScalar("Select v_type From Voucher_Type VT where short_Name = 'PMT' ", cnTemp), "0");
                if (Integer.valueOf(codeValue) > 0) {
                    sqlQuery = "INSERT INTO AcFilteration (V_Type, Nature, U_Name, U_EntDt)"
                            + "VALUES ('" + codeValue + "', '" + codeValue1 + "', 'SA', getdate())";
                    DmServer.executeSQL(sqlQuery, cnTemp);
                }

                codeValue = DmServer.vNull(DmServer.executeScalar("Select v_type From Voucher_Type VT where short_Name = 'RCT' ", cnTemp), "0");
                if (Integer.valueOf(codeValue) > 0) {
                    sqlQuery = "INSERT INTO AcFilteration (V_Type, Nature, U_Name, U_EntDt)"
                            + "VALUES ('" + codeValue + "', '" + codeValue1 + "', 'SA', getdate())";
                    DmServer.executeSQL(sqlQuery, cnTemp);
                }
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From overHeadMast", cnTemp)) <= 0) {
                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Gross Amount','G',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Discount','DISC',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Item Value','IV',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Taxable Value','TV',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'SGST','SGST',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'UGST','UGST',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'CGST','CGST',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'IGST','IGST',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'SGST-RCM','SGST-RCM',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'UGST-RCM','UGST-RCM',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'CGST-RCM','CGST-RCM',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'IGST-RCM','IGST-RCM',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Bill Amount','BAMT',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Round Off','ROFF',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Net Amount','NAMT',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmServer.getSerialNoInteger("1", "overhead", cnTemp);
                sqlQuery = "INSERT INTO overHeadMast(Code,name,manualCode,isSystemGenerated,U_Name,U_EntDt) "
                        + "VALUES (" + codeValue + ",'Landed Value','LV',1,'SA',GETDATE()) ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//<editor-fold defaultstate="collapsed" desc="File Type">
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From " + DmServer.dbCompany + ".dbo.fileTypeMast ftm Where ftm.ruleCode = 'GUI'", cnTemp)) <= 0) {
//                sqlQuery = "insert into " + DmServer.dbCompany + ".dbo.fileTypeMast(ruleCode,fileType,maxSize,isDefault,extFilters,u_Name,u_EntDt) "
//                        + "values ('GUI','Images',5120,1,'jpg,bmp,gif,jpeg','SA',getdate()) ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//</editor-fold>
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
