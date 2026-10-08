package dataman.accounts.self.server;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.dmPrepareStatement.server.DmSavePoint;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.sql.Connection;
import java.sql.ResultSet;

public final class AccountsDataStructure {

    DmRefreshDataStructure refreshDataStructure;

    public AccountsDataStructure(DmRefreshDataStructure refreshDataStructure) throws Exception {
        this.refreshDataStructure = refreshDataStructure;

        hl7Mast("hl7Mast");
        //Item Related
        initialMast("initialMast");
        unitMast("unitMast");
        gstUnitMast("gstUnitMast");
        hsnMast("hsnMast");
        gstHead("gstHead");
        gstType("gstType");
        serviceMappingField("serviceMappingField");

        hsnITSDetail("hsnITSDetail");
        itemITSDetail("itemITSDetail");
        itemMakeMast("itemMakeMast");
        itemCat("itemCat");
        itemGroup("itemGroup");
        itemMast("itemMast");
        itemDepartmentInfo("itemDepartmentInfo");
        itemSiteList("itemSiteList");
        itemType("itemType");

        //Stock Base
        departmentMast("departmentMast");
        itemBatch("itemBatch");
        itemBatchStock("itemBatchStock");
        stock("stock");
        itemId("itemId");
        stockFifo("stockFifo");
        stockItemId("stockItemId");

        //General
        designationMast("designationMast");
        subGroupNature("subGroupNature");
        subGroup("subGroup");
        subGroup2("subGroup2");
        subGroupMember("subGroupMember");
        subGroupSiteList("subGroupSiteList");
        addressbook("addressBook");

        country("country");
        stateMast("stateMast");
        city("city");
        acGroup("acGroup");
        acGroupPositioning("acGroupPositioning");
        tdsCat_Description("tdsCat_Description");
        tdsCat("tdsCat");
        tdsCat_Det("tdsCat_Det");
        costCenterMast("costCenterMast");
        costCenterPath("costCenterPath");
        acGroupPath("acGroupPath");

        ledger("ledger");
        ledgerM("ledgerM");
        ledgerAdj("ledgerAdj");
        ledgerCostCenter("ledgerCostCenter");
        ledgerGst("ledgerGst");

        narrationMast("narrationMast");
        ledgerGroup("ledgerGroup");
        zoneMast("zoneMast");
//        bankReco("bankReco");
        dataAudit("dataAudit");
        enviro("enviro");

        walkThroughMast("walkThroughMast");
        walkThroughDetail("walkThroughDetail");
        walkThroughDefault("walkThroughDefault");
        walkThroughApplicable("walkThroughApplicable");
        walkThroughFootPrint("walkThroughFootPrint");

        nationalityMast("nationalityMast");
        chequePrintingSetup("chequePrintingSetup");

        bankDetail("bankDetail");
        tdsTrans("tdsTrans");

        gstReasonMast("gstReasonMast");
        advanceReqEntry("advanceReqEntry");
        advanceEntry("advanceEntry");
        finPayReqEnt("finPayReqEnt");
        finPayEnt("finPayEnt");
        finPayEntDetail("finPayEntDetail");
        finPayEnt_OthDetail("finPayEnt_OthDetail");
    }

    public void gstReasonMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "5", null, null, null);
        configDataStructure.setColumnValue("description", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    public void gstUnitMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("description", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    public void bankDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("accountNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("bankName", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("branch", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("branchAddress", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("swiftCode", SQLDataType.VARCHAR, "15", null, null, null);
        configDataStructure.setColumnValue("ifscCode", SQLDataType.VARCHAR, "11", null, null, null);
        configDataStructure.setColumnValue("isDefault", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");

    }

    public void chequePrintingSetup(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("bankCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("chequeDateTop", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("chequeDateLeft", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("favourOfTop", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("favourOfLeft", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("AmtInWordsTop", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("AmtInWordsLeft", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("AmtTop", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("AmtLeft", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("authSignatory", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("authSignatoryTop", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("authSignatoryLeft", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("companyName", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("companyNameTop", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("companyNameLeft", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("crossChequeTop", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("crossChequeLeft", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("paperWidth", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("paperHeight", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("bankCode", "subCode", "subGroup");
    }

    public void hl7Mast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("tableName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("shortName", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("priorityIndex", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("colorName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
    }

    public void nationalityMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("countrylocale", SQLDataType.VARCHAR, "7", null, null, null);
    }

    public void walkThroughMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    public void walkThroughDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("authority", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("linkedUser", SQLDataType.VARCHAR, "250", null, null, null);
    }

    public void walkThroughDefault(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("userName", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("valueUpto", SQLDataType.FLOAT, null, null, SQLIsNullable.NOT_NULL, null);

        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("departmentCode", "subCode", "subGroup");
        configDataStructure.setUnique("code,userName,v_Type,site_Code,departmentCode,valueUpto");
    }

    public void walkThroughApplicable(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("wtaCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("authority", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("linkedUser", SQLDataType.VARCHAR, "250", null, null, null);
    }

    public void walkThroughFootPrint(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("wtaCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("wtfTimeStamp", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("linkV_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("linkDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("authority", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("linkedUser", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("isStatus", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("linkV_Type", "v_Type", "voucher_Type");
    }

//    public void walkThroughDocument(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("V_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("RefNo", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("RefDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("subcode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("CostCenter", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("ItemCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("WTDValue", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("Remark", SQLDataType.VARCHAR, "250", null, null, null);
//        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
//
//        configDataStructure.setForeignKey("subcode", "subcode", "subGroup");
//        configDataStructure.setForeignKey("CostCenter", "code", "costCenterMast");
//        configDataStructure.setForeignKey("ItemCode", "code", "itemMast");
//        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
//    }
//
//    public void walkThroughLedgerAdj(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("Vr_DocId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("Vr_V_SNo", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("Adj_DocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("Adj_V_SNo", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("Adjustment", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("Remark", SQLDataType.VARCHAR, "100", null, null, null);
//
//        configDataStructure.setForeignKey("Vr_DocId,Vr_V_SNo", "DocId,V_SNo", "Ledger");
//        configDataStructure.setForeignKey("Adj_DocId,Adj_V_SNo", "DocId,V_SNo", "WalkThroughDocument");
//    }
    public void enviro(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("ac_RoundOffTDS", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("ac_RoundOffGST", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("stockAdjustmentVType", SQLDataType.VARCHAR, "250", null, null, null);
//        configDataStructure.setColumnValue("ac_RoundOffAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
//        configDataStructure.setForeignKey("ac_RoundOffAc", "subCode", "subgroup");
    }

    public void dataAudit(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
    }

    public void addressbook(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("groupName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("designation", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("remarks", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("add1", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("add2", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("cityCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("countryCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("pin", SQLDataType.VARCHAR, "6", null, null, null);
        configDataStructure.setColumnValue("phone", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("mobile", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("fax", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("zone", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("eMail", SQLDataType.VARCHAR, "40", null, null, null);
        configDataStructure.setColumnValue("initial", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("contactPerson", SQLDataType.VARCHAR, "35", null, null, null);

        configDataStructure.setColumnValue("isDefault", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("zone", "code", "zoneMast");
        configDataStructure.setForeignKey("cityCode", "cityCode", "city");
//        configDataStructure.setForeignKey("countryCode", "code", "country");
        configDataStructure.setForeignKey("subCode", "subCode", "subgroup");
    }

    public void zoneMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    public void city(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("cityCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("cityName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("stateCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("countryCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("stdCode", SQLDataType.VARCHAR, "15", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setForeignKey("stateCode", "code", "stateMast");
        configDataStructure.setForeignKey("countryCode", "code", "country");
    }

    public void stateMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("numericCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("textCode", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("isUT", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
    }

    public void country(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("currencyCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setForeignKey("currencyCode", "code", "currency");
    }

    public void subGroup(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("initial", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("displayName", SQLDataType.VARCHAR, "200", null, null, null);

        configDataStructure.setColumnValue("groupCode", SQLDataType.BIGINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("ledgerGroup", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("distributor", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("filterNature", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("add1", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("add2", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("cityCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("countryCode", SQLDataType.INT, null, null, null, null);

        configDataStructure.setColumnValue("pin", SQLDataType.VARCHAR, "6", null, null, null);
        configDataStructure.setColumnValue("phone", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("mobile", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("fax", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("zone", SQLDataType.INT, null, null, null, null);

        configDataStructure.setColumnValue("gsTin", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("aadhaarNo", SQLDataType.VARCHAR, "12", null, null, null);
        configDataStructure.setColumnValue("gstinDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("guiConfigCode", SQLDataType.INT, null, null, null, null);

        configDataStructure.setColumnValue("isDuplicateTIN", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("tinNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("cstNo", SQLDataType.VARCHAR, "40", null, null, null);
        configDataStructure.setColumnValue("stNo", SQLDataType.VARCHAR, "40", null, null, null);
        configDataStructure.setColumnValue("eMail", SQLDataType.VARCHAR, "250", null, null, null);

        configDataStructure.setColumnValue("pan", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("tan", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("location", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("creditLimit", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("dueDays", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("tds_Catg", SQLDataType.INT, null, null, null, null);

        configDataStructure.setColumnValue("policyNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("ifscCode", SQLDataType.VARCHAR, "15", null, null, null);
        configDataStructure.setColumnValue("iecCode", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("eccCode", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("excise", SQLDataType.VARCHAR, "35", null, null, null);

        configDataStructure.setColumnValue("range", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("division", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("partyType", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("partyCat", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("fbtOnPer", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setColumnValue("fbtPer", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("creditLimit_Temp_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("creditLimit_Temp", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("contactPerson", SQLDataType.VARCHAR, "35", null, null, null);

        configDataStructure.setColumnValue("drugLicense", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("registrationNo", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("expiryTerms", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("invoiceDiscount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("minimum", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("maximum", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("reorderLevel", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isNonMoving", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isMonopoly", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setColumnValue("remark", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("costCenter", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("linkedUser", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setForeignKey("filterNature", "code", "subGroupNature");
        configDataStructure.setForeignKey("nature", "code", "subGroupNature");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("groupCode", "groupCode", "acGroup");
        configDataStructure.setForeignKey("ledgerGroup", "code", "ledgerGroup");
        configDataStructure.setForeignKey("distributor", "subCode", "subGroup");
        configDataStructure.setForeignKey("zone", "code", "zoneMast");
        configDataStructure.setForeignKey("cityCode", "cityCode", "city");
//        configDataStructure.setForeignKey("countryCode", "code", "country");
        configDataStructure.setForeignKey("costCenter", "code", "costCenterMast");
        configDataStructure.setForeignKey("tds_Catg", "code", "tdsCat");
        configDataStructure.setForeignKey("guiConfigCode", "code", "guiConfig");
    }

    public void subGroup2(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("dob", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("dor", SQLDataType.DATE, null, null, null, null);

        configDataStructure.setColumnValue("genderCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("maritalStatusCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("religionCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("bloodGroupCode", SQLDataType.INT, null, null, null, null);

        configDataStructure.setColumnValue("fathername", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("designationCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("bankCode", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setColumnValue("bankAcNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("pfAcNo", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("doj", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("doa", SQLDataType.DATE, null, null, null, null);

        configDataStructure.setColumnValue("tADD1", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("tADD2", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("tCityCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("tCountryCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("tPIN", SQLDataType.VARCHAR, "6", null, null, null);

        configDataStructure.setColumnValue("licenseNo", SQLDataType.VARCHAR, "25", null, null, null);
        configDataStructure.setColumnValue("licenseExpiry", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("qualification", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("passportNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("pIssueDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("pExpDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("pIssuePlace", SQLDataType.VARCHAR, "20", null, null, null);

        configDataStructure.setColumnValue("hrDepartmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("offDay", SQLDataType.SMALLINT, null, null, null, null);

        configDataStructure.setForeignKey("genderCode", "code", "hl7Mast");
        configDataStructure.setForeignKey("maritalStatusCode", "code", "hl7Mast");
        configDataStructure.setForeignKey("religionCode", "code", "hl7Mast");
        configDataStructure.setForeignKey("bloodGroupCode", "code", "hl7Mast");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("tCityCode", "cityCode", "city");
        configDataStructure.setForeignKey("tCountryCode", "code", "country");
        configDataStructure.setForeignKey("designationCode", "code", "designationMast");
        configDataStructure.setForeignKey("departmentCode", "subCode", "departmentMast");
        configDataStructure.setForeignKey("bankCode", "subCode", "subgroup");
    }

    public void subGroupMember(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("memberName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("dob", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("address", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("relationShip", SQLDataType.VARCHAR, "20", null, null, null);

        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
    }

    public void subGroupSiteList(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("isOpdConsultant", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("allowForSite", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isIpdConsultant", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("drugLicenseNo", SQLDataType.VARCHAR, "35", null, null, null);

        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
    }

    public void designationMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("level_No", SQLDataType.TINYINT, null, null, null, null);
    }

    public void departmentMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("drugLicenceNo", SQLDataType.VARCHAR, "35", null, null, null);

        //Set Foreign Key
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
    }

    public void itemType(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setUnique("manualCode");
    }

    public void initialMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

//    public void tariffMast(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "30", null, null, null);
//        configDataStructure.setColumnValue("tType", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
//    }
//
//    public void vatCodeMast(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "35", null, null, null);
//        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "30", null, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
//    }
    public void unitMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("unit", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("decimalPlaces", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("gstUnitCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        //Set Foreign Key
        configDataStructure.setForeignKey("gstUnitCode", "code", "gstUnitMast");
    }

    public void itemMakeMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("companyName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("shortName", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("lclPerson", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("lclMobile", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("lclFax", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("lclRPhone", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("lclOPhone", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("lclEmail", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("lclAddress1", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("lclAddress2", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("regPerson", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("regMobile", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("regFax", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("regRPhone", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("regOPhone", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("regEmail", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("regAddress1", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("regAddress2", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("natPerson", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("natMobile", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("natFax", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("natRPhone", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("natOPhone", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("natEmail", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("natAddress1", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("natAddress2", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("filterItemType", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("filterItemType", "code", "itemType");
    }

    public void hsnMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "1500", null, null, null);
        configDataStructure.setColumnValue("hsn", SQLDataType.BIGINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("itsBasedOn", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("itemGSTNature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    public void hsnITSDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("rateFrom", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("rateUpto", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_Purchase", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_PurchaseRet", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_Sales", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_SalesRet", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseReturnAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesReturnAc", SQLDataType.BIGINT, null, null, null, null);

        //Set Foreign Key
        configDataStructure.setForeignKey("code", "code", "hsnMast");
        configDataStructure.setForeignKey("purchaseAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("purchaseReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("its_Code_Sales", "code", "its");
        configDataStructure.setForeignKey("its_Code_SalesRet", "code", "its");
        configDataStructure.setForeignKey("its_Code_Purchase", "code", "its");
        configDataStructure.setForeignKey("its_Code_PurchaseRet", "code", "its");
    }

    public void gstType(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("printSrNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setUnique("name");
    }

    public void gstHead(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("shortName", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("isTaxHead", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);  // 1 == Yes, 2 == No
        configDataStructure.setColumnValue("printSrNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setUnique("shortName");
    }

    public void itemITSDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("rateFrom", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("rateUpto", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("gstTypeCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_Purchase", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_PurchaseRet", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_Sales", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("its_Code_SalesRet", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseReturnAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesReturnAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("itsTypeCode", SQLDataType.INT, null, null, null, null);

        //Set Foreign Key
        configDataStructure.setForeignKey("code", "code", "itemMast");
        configDataStructure.setForeignKey("gstTypeCode", "code", "gstType");

        configDataStructure.setForeignKey("purchaseAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("purchaseReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("its_Code_Sales", "code", "its");
        configDataStructure.setForeignKey("its_Code_SalesRet", "code", "its");
        configDataStructure.setForeignKey("its_Code_Purchase", "code", "its");
        configDataStructure.setForeignKey("its_Code_PurchaseRet", "code", "its");

    }

    public void itemCat(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("printSrNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("filterItemType", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
//        configDataStructure.setColumnValue("hsnCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("allowNegativeStock", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isCommon", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
//        configDataStructure.setColumnValue("propertyList", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("saleRateProffitMargine", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setForeignKey("filterItemType", "code", "itemType");
//        configDataStructure.setForeignKey("hsnCode", "code", "hsnMast");

    }

    public void itemGroup(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("filterItemType", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("printSrNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("isCommon", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseReturnAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesReturnAc", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setColumnValue("grIrAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("consumptionAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("cogsAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("pidAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("inTransitAc", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("filterItemType", "code", "itemType");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("purchaseAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("purchaseReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("grIrAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("consumptionAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("cogsAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("pidAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("inTransitAc", "subCode", "subGroup");
    }

    public void serviceMappingField(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setUnique("name");
    }

    public void itemMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("displayName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "21", null, null, null);
        configDataStructure.setColumnValue("printSrNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("itemDesc", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("itemGroup", SQLDataType.INT, null, null, null, null);

        configDataStructure.setColumnValue("itemMakeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("itemCategoryCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("binName", SQLDataType.VARCHAR, "20", null, null, null);

        configDataStructure.setColumnValue("itsBasedOn", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("allowNegativeStock", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("purchaseUnit", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("saleUnit", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseUnitConversionRate", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("saleUnitConversionRate", SQLDataType.DECIMAL, "18,7", null, null, null);
//        configDataStructure.setColumnValue("itemPacking", SQLDataType.DECIMAL, "18,7", null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("itemGSTNature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("propertyList", SQLDataType.VARCHAR, "250", null, null, null);

        configDataStructure.setColumnValue("hsnCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("its_Code", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("its_Code_Sale", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("purchaseReturnAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("salesReturnAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("grIrAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("consumptionAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("cogsAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("pidAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("inTransitAc", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setColumnValue("filterItemType", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);

        configDataStructure.setColumnValue("itemType", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("sku", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("secondryUnit", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("conversionRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("itemPacking", SQLDataType.VARCHAR, "15", null, null, null);
        configDataStructure.setColumnValue("isPOMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isIndentMandatory", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
//        configDataStructure.setColumnValue("minStock", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("maxStock", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("reOrder", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("uniqueId", SQLDataType.VARCHAR, "4", null, null, null);
        configDataStructure.setColumnValue("smfCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("serviceEvalTypeCode", SQLDataType.TINYINT, null, null, null, null);  // 1 = Free value, 2 = Percentage of goods taxable value
        configDataStructure.setColumnValue("serviceEvalRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isCommon", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("applicableBatchFields", SQLDataType.VARCHAR, "250", null, null, null);

//Index Key
        configDataStructure.setIndex("subCode");
        configDataStructure.setIndex("purchaseAc");
        configDataStructure.setIndex("salesAc");
        configDataStructure.setIndex("purchaseReturnAc");
        configDataStructure.setIndex("salesReturnAc");

        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("purchaseAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("purchaseReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("salesReturnAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("grIrAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("consumptionAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("cogsAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("pidAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("inTransitAc", "subCode", "subGroup");
        configDataStructure.setForeignKey("hsnCode", "code", "hsnMast");
        configDataStructure.setForeignKey("sku", "code", "unitMast");
        configDataStructure.setForeignKey("itemGroup", "code", "itemGroup");
        configDataStructure.setForeignKey("itemCategoryCode", "code", "itemCat");
        configDataStructure.setForeignKey("itemMakeCode", "code", "itemMakeMast");
        configDataStructure.setForeignKey("itemType", "code", "itemType");
        configDataStructure.setForeignKey("filterItemType", "code", "itemType");
        configDataStructure.setForeignKey("purchaseUnit", "code", "unitMast");
        configDataStructure.setForeignKey("saleUnit", "code", "unitMast");
        configDataStructure.setForeignKey("smfCode", "code", "serviceMappingField");

    }

    public void itemDepartmentInfo(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("minStock", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("maxStock", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("reOrder", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("binName", SQLDataType.VARCHAR, "20", null, null, null);

        configDataStructure.setForeignKey("departmentCode", "subCode", "departmentMast");
        configDataStructure.setForeignKey("ItemCode", "code", "itemMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
    }

    public void itemSiteList(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("approvedPurchaseRate", SQLDataType.DECIMAL, "18,5", null, null, null);

        configDataStructure.setForeignKey("code", "code", "itemMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
    }

    public void itemBatch(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("saleRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("skuSaleRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("mrp", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("skuMrp", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("batchNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manufacturingDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("expiryDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("colourCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("sizeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("itemMakeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("barcode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("lotNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("isBatchable", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setIndex("barcode");

        configDataStructure.setForeignKey("colourCode", "code", "itemColourMast");
        configDataStructure.setForeignKey("sizeCode", "code", "itemSizeMast");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
        configDataStructure.setForeignKey("itemMakeCode", "code", "itemMakeMast");

        configDataStructure.setIndex("batchNo");
        configDataStructure.setIndex("itemCode");
    }

    public void itemBatchStock(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
        configDataStructure.setColumnValue("ibCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("barCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("balanceQty", SQLDataType.DECIMAL, "18,7", null, null, null);

        //Set Unique Key
//        configDataStructure.setUnique("ibCode,itemCode,departmentCode,site_Code,barCode");
        //Set Foreign Key
        configDataStructure.setForeignKey("ibCode", "code", "itemBatch");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
        configDataStructure.setForeignKey("departmentCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");

    }

    public void stock(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("stockContraSubCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("acContraSubCode", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setColumnValue("refDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("refV_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("refV_SNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("refChallanNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("refChallanDate", SQLDataType.DATE, null, null, null, null);

        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("ibCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("recQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("isuQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("skuValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("landedSkuValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("skuSaleValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("landedSkuSaleValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("otherAdjustment", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("itemMakeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("grossPurchaseRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("staffSkuValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("totalTaxPerc", SQLDataType.FLOAT, null, null, null, null);

//        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("tempDisplayRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isSystemGenerated", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);

        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("departmentCode");
        configDataStructure.setIndex("site_Code");
        configDataStructure.setIndex("itemCode");
        configDataStructure.setIndex("ibCode");

        configDataStructure.setForeignKey("departmentCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("itemMakeCode", "code", "itemMakeMast");
        configDataStructure.setForeignKey("stockContraSubCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("acContraSubCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("refV_Type", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("ibCode", "code", "itemBatch");
    }

    public void stockFifo(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sno", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("recQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("isuQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("stockValueDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("avgSKURate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("avgLandedSKURate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isItemStockRecord", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setIndex("stockValueDate");

        //Set Foreign Key
        configDataStructure.setForeignKey("docId,v_SNo", "docid,v_SNo", "stock");
//        configDataStructure.setForeignKey("docId", "docid", "stock");
//        configDataStructure.setForeignKey("v_SNo", "v_SNo", "stock");
    }

    private void itemId(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("prefix", SQLDataType.VARCHAR, "12", null, null, null);
        configDataStructure.setColumnValue("counter", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("description", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("ibCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("stockStatus", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "2"); //2 = No Stock / 1=Stock  Exists / 3=Reserved / 4=Loaded For Dispatch
        configDataStructure.setColumnValue("barcode", SQLDataType.VARCHAR, "50", null, null, null);  //always hold current barcode of item ID
//        configDataStructure.setColumnValue("prodDocid", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("prodV_Sno", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("sysQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("quantity", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("currentDeptCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("currentSiteCode", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("currentSoDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("currentSoV_Sno", SQLDataType.SMALLINT, null, null, null, null);

        //Set Index
        configDataStructure.setIndex("ibCode");
        configDataStructure.setIndex("itemCode");
//        configDataStructure.setIndex("prodDocid,prodV_Sno");
//        configDataStructure.setIndex("currentSoDocId,currentSoV_Sno");
        configDataStructure.setIndex("currentDeptCode");

        //Set Foreign Key			 
        configDataStructure.setForeignKey("ibCode", "code", "itemBatch");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
//        configDataStructure.setForeignKey("prodDocid,prodV_Sno", "docId,v_SNo", "productionDetail");
//        configDataStructure.setForeignKey("currentSoDocId,currentSoV_Sno", "docId,v_SNo", "sOrderDetail");
        configDataStructure.setForeignKey("currentDeptCode", "subCode", "departmentMast");
        configDataStructure.setForeignKey("currentSiteCode", "code", "siteMast");
    }

    private void stockItemId(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Sno", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sno", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("itemId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("recPcs", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isuPcs", SQLDataType.TINYINT, null, null, null, null);

        //Set Index
        configDataStructure.setIndex("itemId");

        //Set Foreign Key
        configDataStructure.setForeignKey("docId,v_SNo ", "docId,v_SNo", "stock");
        configDataStructure.setForeignKey("itemId", "code", "itemId");
    }

    public void costCenterMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("underCostCenter", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("address", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
        configDataStructure.setColumnValue("cType", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);

        configDataStructure.setForeignKey("underCostCenter", "code", "costCenterMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
    }

    public void costCenterPath(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("underCostCenter", SQLDataType.INT, null, null, null, null);

        configDataStructure.setForeignKey("code", "code", "costCenterMast");
        configDataStructure.setForeignKey("underCostCenter", "code", "costCenterMast");
    }

    public void ledgerGroup(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("groupFilter", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("groupFilter", "code", "subgroupNature");
    }

    public void narrationMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    public void ledgerCostCenter(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("costCenter", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("amtDr", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("amtCr", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setIndex("costCenter");

        configDataStructure.setForeignKey("docId,v_Sno", "docId,v_Sno", "ledger");
        configDataStructure.setForeignKey("costCenter", "code", "costCenterMast");
    }

    public void ledgerAdj(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("vr_DocId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("vr_V_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("vr_SubCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("vr_V_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("adj_DocId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("adj_V_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("adj_SubCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("adj_V_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("adjustment", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setIndex("vr_SubCode");
        configDataStructure.setIndex("adj_DocId,adj_V_SNo");
        configDataStructure.setIndex("adj_SubCode");

        configDataStructure.setForeignKey("vr_DocId,vr_V_SNo", "docId,v_SNo", "ledger");
        configDataStructure.setForeignKey("vr_SubCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("adj_DocId,adj_V_SNo", "docId,v_SNo", "ledger");
        configDataStructure.setForeignKey("adj_SubCode", "subCode", "subGroup");
    }

    public void ledgerGst(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("hsnCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("itemGSTNature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("qty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("grossAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("taxableValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("cgstPer", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("cgstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("cgstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("cgstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("sgstPer", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("sgstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("sgstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("sgstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("ugstPer", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("ugstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("ugstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("ugstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("igstPer", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("igstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("igstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("igstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("rcmCgstPer", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmCgstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmCgstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmCgstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("rcmSgstPer", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmSgstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmSgstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmSgstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("rcmUgstPer", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmUgstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmUgstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmUgstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("rcmIgstPer", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmIgstAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmIgstAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("rcmIgstDrCr", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("taxableAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("taxableDrCr", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("roundOffGST", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("roundOff", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("roundOffAc", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("roundOffDrCr", SQLDataType.VARCHAR, "2", null, null, null);
//        configDataStructure.setColumnValue("isTaxInclusive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isRcm", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("netAmt", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("consignee", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("consigneeType", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("placeOfSupply", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("gstType", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setColumnValue("cgstSubCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("sgstSubCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("igstSubCode", SQLDataType.BIGINT, null, null, null, null);

//        configDataStructure.setIndex("itemCode");
        configDataStructure.setIndex("hsnCode");

//        configDataStructure.setForeignKey("docId,v_SNo", "docId,v_SNo", "ledger");
//        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
        configDataStructure.setForeignKey("hsnCode", "code", "hsnMast");

        configDataStructure.setForeignKey("cgstSubCode", "subCode", "subgroup");
        configDataStructure.setForeignKey("sgstSubCode", "subCode", "subgroup");
        configDataStructure.setForeignKey("igstSubCode", "subCode", "subgroup");

//        configDataStructure.setForeignKey("cgstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("sgstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("ugstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("igstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("rcmCgstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("rcmSgstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("rcmUgstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("rcmIgstAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("taxableAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("roundOffAc", "subCode", "subGroup");
//        configDataStructure.setForeignKey("consignee", "subCode", "subGroup");
//        configDataStructure.setForeignKey("placeOfSupply", "code", "stateMast");
    }

    public void subGroupNature(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.TINYINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setUnique("manualCode");
    }

    public void acGroup(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("groupCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("groupName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("groupUnder", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("isSysGroup", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("groupNature", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("contraGroupName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("nature", "code", "subGroupNature");
        configDataStructure.setForeignKey("groupUnder", "groupCode", "acGroup");
    }

    public void acGroupPath(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("groupCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("groupUnder", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("groupCode", "groupCode", "acGroup");
        configDataStructure.setForeignKey("groupUnder", "groupCode", "acGroup");
    }

    public void acGroupPositioning(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("groupCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("level", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isExpand", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    public void ledgerM(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("subcode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("narration", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("refDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("refV_SNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("gstReasonCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("consignee", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("consigneeType", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("partyType", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("gsTin", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("gstinDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("placeOfSupply", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("PreparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("postedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("to_Site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("lgDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("wtaCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("postedByDateTime", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("tpaControlAc", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("site_Code");
        configDataStructure.setIndex("to_Site_Code");

        configDataStructure.setForeignKey("lgDocId", "docId", "ledgerM");
        configDataStructure.setForeignKey("subcode", "subcode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("to_Site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("gstReasonCode", "code", "gstReasonMast");
        configDataStructure.setForeignKey("consignee", "subCode", "subGroup");
        configDataStructure.setForeignKey("placeOfSupply", "code", "stateMast");
        configDataStructure.setColumnValue("refDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("refV_SNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setForeignKey("refDocId,refV_SNo", "docId,v_SNo", "ledger");

        configDataStructure.setForeignKey("tpaControlAc", "subcode", "subGroup");
    }

    public void ledger(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("refNo", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("refDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("subcode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("contraSub", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("contraText", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("amtDr", SQLDataType.DECIMAL, "18,4", null, null, null);
        configDataStructure.setColumnValue("amtCr", SQLDataType.DECIMAL, "18,4", null, null, null);
        configDataStructure.setColumnValue("instrumentCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("chq_No", SQLDataType.VARCHAR, "40", null, null, null);
        configDataStructure.setColumnValue("chq_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("clg_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("clg_Remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("favourOf", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("accountNo", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("pan", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("tan", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("bankName", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("branchAddress", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("ifscCode", SQLDataType.VARCHAR, "11", null, null, null);
        configDataStructure.setColumnValue("swiftCode", SQLDataType.VARCHAR, "15", null, null, null);
        configDataStructure.setColumnValue("branch", SQLDataType.VARCHAR, "35", null, null, null);
        configDataStructure.setColumnValue("paymentMode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isCrossCheque", SQLDataType.TINYINT, null, null, null, "0");
        configDataStructure.setColumnValue("tdsFor", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("tdsCategory", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("tdsDeductFrom", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("tdsDesc", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("orignalAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("tdsOnAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("tdsPer", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("tds_Of_V_Sno", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("gst_Of_V_Sno", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("formulaString", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("roundOffTDS", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("isSystem_Generated", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("isBankCashBook", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("narration", SQLDataType.VARCHAR, "2000", null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("isPosted", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setColumnValue("isPartialPosting", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("tdsDeductFrom");
        configDataStructure.setIndex("subcode");
        configDataStructure.setIndex("contraSub");
        configDataStructure.setIndex("site_Code");
        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("tdsDesc");
        configDataStructure.setIndex("tdsCategory");

        configDataStructure.setForeignKey("tdsDeductFrom", "subcode", "subGroup");
        configDataStructure.setForeignKey("subcode", "subcode", "subGroup");
        configDataStructure.setForeignKey("contraSub", "subcode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("tdsDesc", "code", "tdsCat_Description");
        configDataStructure.setForeignKey("tdsCategory", "code", "tdsCat");
    }

    public void tdsTrans(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("subcode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("contraSub", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("amtDr", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("amtCr", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("pan", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("tan", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("tdsFor", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("tdsCategory", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("tdsDeductFrom", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("tdsDesc", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("tdsOnAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("tdsPer", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("tds_Of_V_Sno", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("formulaString", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("roundOffTDS", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setIndex("subcode");
        configDataStructure.setIndex("contraSub");

        configDataStructure.setForeignKey("tdsDeductFrom", "subcode", "subGroup");
        configDataStructure.setForeignKey("subcode", "subcode", "subGroup");
        configDataStructure.setForeignKey("contraSub", "subcode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");
        configDataStructure.setForeignKey("tdsDesc", "code", "tdsCat_Description");
        configDataStructure.setForeignKey("tdsCategory", "code", "tdsCat");
    }

    public void tdsCat(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("section", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("tdsFor", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setColumnValue("tdsOnPreviousTransactions", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("type", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");

    }

    public void tdsCat_Det(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("srNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("acCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("percentage", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("formulaString", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("tdsDesc", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setColumnValue("applicableFrom", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("applicableTo", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("fromAmt", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("toAmt", SQLDataType.DECIMAL, "20,7", null, null, null);
        configDataStructure.setColumnValue("penalityPercentage", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("initialTurnoverAmt", SQLDataType.DECIMAL, "18,7", null, null, null);

        configDataStructure.setForeignKey("acCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("tdsDesc", "code", "tDSCat_Description");
    }

    public void tdsCat_Description(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    public void advanceReqEntry(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isStatus", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("poDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("module", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("advanceReqAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("dueDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("discardRemark", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("site_Code");

        configDataStructure.setForeignKey("poDocId", "docid", "pOrder");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");

    }

    public void advanceEntry(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("reqDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("finPayReqDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("paidAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("originalAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("tdsDeduction", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("approvedDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("site_Code");

        configDataStructure.setForeignKey("reqDocId", "docid", "advanceReqEntry");
        configDataStructure.setForeignKey("finPayReqDocId", "docid", "finPayReqEnt");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");

    }

    public void finPayReqEnt(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("partPay", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("pbDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("module", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("supplierCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("finDuePay", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("paymDt", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setColumnValue("advanceSettled", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("site_Code");

        configDataStructure.setForeignKey("pbDocId", "docid", "pBill");
        configDataStructure.setForeignKey("supplierCode", "subcode", "subgroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");

    }

    public void finPayEnt(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("module", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("supplierCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("amtPaid", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("originalAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("tdsDeduction", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHARMAX, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("approvedDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("site_Code");

        configDataStructure.setForeignKey("supplierCode", "subcode", "subgroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");

    }

    public void finPayEntDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("payReqDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("finReqAmt", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setIndex("v_Date");

        configDataStructure.setForeignKey("payReqDocId", "docId", "finPayReqEnt");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_Type");

    }

    public void finPayEnt_OthDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);

        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("otherAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHARMAX, null, null, null, null);

        configDataStructure.setForeignKey("otherAc", "subcode", "subgroup");

    }
//    public void bankReco(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("bank_V_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("chq_No", SQLDataType.VARCHAR, "40", null, null, null);
//        configDataStructure.setColumnValue("chq_Date", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("clg_Date", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("amtDr", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("amtCr", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("favourof", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("isSystem_Generated", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
//        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "100", null, null, null);
//
//        configDataStructure.setForeignKey("docID,bank_V_SNo", "docID,v_SNo", "ledger");
//    }

    public void initilizer() throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        DmSavePoint spBegin = null;
        String sqlQuery = "", codeValue = "";
        LibraryVoucherCategory lvc = new LibraryVoucherCategory();

        try {
            cnTemp = DmServer.getConnection(refreshDataStructure.getDatabaseName());
            spBegin = new DmSavePoint(cnTemp);

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.FUND_TRANSFER + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.FUND_TRANSFER + "', '" + codeValue + "', 'Fund Transfer', '" + lvc.FUND_TRANSFER + "', 1, 'SA', 'SA', getdate(), 'Fund Transfer Entry','Accounts') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.FUND_IN_TRANSIT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.FUND_IN_TRANSIT + "', '" + codeValue + "', 'Fund In Transit', '" + lvc.FUND_IN_TRANSIT + "', 1, 'SA', 'SA',getdate(),'Fund In Transfer Entry','Accounts') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='STAX'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'STAX', '" + codeValue + "', 'Inward GTA', 'STAX', 1, 'SA', 'SA', getdate(),'Service Tax Entry','Accounts') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='STAXR'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'STAX','" + codeValue + "', 'Inward GTA Reverse', 'STAXR', 1, 'SA', 'SA', getdate(), 'Service Tax Entry','Accounts') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='STXOW'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'STAX', '" + codeValue + "', 'Outward GTA', 'STXOW', 1, 'SA', 'SA',getdate() ,'Service Tax Entry','Accounts') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='STXOR'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'STAX', '" + codeValue + "', 'Outward GTA Reverse', 'STXOR', 1, 'SA', 'SA', getdate(),'Service Tax Entry','Accounts') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='STXNG'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'STAX', '" + codeValue + "', 'Non GTA', 'STXNG', 1, 'SA', 'SA', getdate(),'Service Tax Entry','Accounts') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='STXNR'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'STAX', '" + codeValue + "', 'Non GTA Reverse', 'STXNR', 1, 'SA', 'SA',getdate(),'Service Tax Entry','Accounts') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='BDGT'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'BDGT', '" + codeValue + "', 'Budget Schedule', 'BDGT', 1, 'sa', 'sa', getdate(),'','')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.CREDIT_NOTE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.JOURNAL + "', '" + codeValue + "', 'Credit Note', '" + lvc.CREDIT_NOTE + "', 1, 'SA', 'SA', getdate(),'Voucher Entry','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.CASH_PAYMENT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule,isBankCashBook) "
                        + "VALUES ('FA', '" + lvc.BANK_PAYMENT + "',  '" + codeValue + "', 'Cash Payment', '" + lvc.CASH_PAYMENT + "', 1, 'SA', 'SA', getdate(),'Voucher Entry','Accounts',1)";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.CASH_RECEIPT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule,isBankCashBook) "
                        + "VALUES ('FA', '" + lvc.BANK_RECEIPT + "','" + codeValue + "', 'Cash Receipt', '" + lvc.CASH_RECEIPT + "', 1, 'SA', 'SA', getdate(),'Voucher Entry','Accounts',1)";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.DEBIT_NOTE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.JOURNAL + "', '" + codeValue + "', 'Debit Note', '" + lvc.DEBIT_NOTE + "', 1, 'SA', 'SA', getdate(),'Voucher Entry','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.JOURNAL + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.JOURNAL + "','" + codeValue + "', 'Journal', '" + lvc.JOURNAL + "', 1, 'SA', 'SA', getdate(), 'Voucher Entry','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.CREDIT_NOTE_GST + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.CREDIT_NOTE_GST + "','" + codeValue + "', 'Credit Note (GST)', '" + lvc.CREDIT_NOTE_GST + "', 1, 'SA', 'SA', getdate(), 'GST Voucher','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.DEBIT_NOTE_GST + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.DEBIT_NOTE_GST + "','" + codeValue + "', 'Debit Note (GST)', '" + lvc.DEBIT_NOTE_GST + "', 1, 'SA', 'SA', getdate(), 'GST Voucher','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.CREDIT_INVOICE_GST + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.CREDIT_INVOICE_GST + "','" + codeValue + "', 'Credit Invoice (GST)', '" + lvc.CREDIT_INVOICE_GST + "', 1, 'SA', 'SA', getdate(), 'GST Voucher','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.DEBIT_INVOICE_GST + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.DEBIT_INVOICE_GST + "','" + codeValue + "', 'Debit Invoice (GST)', '" + lvc.DEBIT_INVOICE_GST + "', 1, 'SA', 'SA', getdate(), 'GST Voucher','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.OPENING_BALANCE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.OPENING_BALANCE + "', '" + codeValue + "', 'Opening Balance', '" + lvc.OPENING_BALANCE + "', 1, 'sa', 'sa', getdate(),'Voucher Entry','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.CONTRA + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.CONTRA + "','" + codeValue + "', 'Contra', '" + lvc.CONTRA + "', 1, 'sa', 'sa', getdate(),'Voucher Entry','Accounts')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.BANK_PAYMENT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule,isBankCashBook) "
                        + "VALUES ('FA', '" + lvc.BANK_PAYMENT + "','" + codeValue + "', 'Bank Payment', '" + lvc.BANK_PAYMENT + "', 1, 'SA', 'SA', getdate(),'Voucher Entry','Accounts',1)";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.BANK_RECEIPT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule,isBankCashBook) "
                        + "VALUES ('FA', '" + lvc.BANK_RECEIPT + "', '" + codeValue + "', 'Bank Receipt', '" + lvc.BANK_RECEIPT + "', 1, 'SA', 'SA', getdate(),'Voucher Entry','Accounts',1) ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='BREC'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
//                        + "VALUES ('FA', 'BREC','" + codeValue + "', 'Bank Reconcilation', 'BREC', 1, 'SA', 'SA', getdate(),'','') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.ACCOUNT_MASTER + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.ACCOUNT_MASTER + "', '" + codeValue + "', 'Account Master', '" + lvc.ACCOUNT_MASTER + "', 1, 'SA', 'SA', getdate(),'','') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.SUPPLIER_MASTER + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.SUPPLIER_MASTER + "', '" + codeValue + "', 'Supplier Master', '" + lvc.SUPPLIER_MASTER + "', 1, 'SA', 'SA', getdate(),'','') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.CUSTOMER_MASTER + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.CUSTOMER_MASTER + "', '" + codeValue + "', 'Customer Master', '" + lvc.CUSTOMER_MASTER + "', 1, 'SA', 'SA', getdate(),'','') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name='" + lvc.EMPLOYEE_MASTER + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt,MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.EMPLOYEE_MASTER + "', '" + codeValue + "', 'Employee Master', '" + lvc.EMPLOYEE_MASTER + "', 1, 'SA', 'SA', getdate(),'','') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            //New Added
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.ADVANCE_REQUEST_ENTRY + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.ADVANCE_REQUEST_ENTRY + "', '" + codeValue + "', 'Advance Request Entry', '" + lvc.ADVANCE_REQUEST_ENTRY + "', 1, 'SA', 'SA', getdate(), 'Advance Request Entry','Account') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.ADVANCE_ENTRY + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.ADVANCE_ENTRY + "', '" + codeValue + "', 'Advance Entry', '" + lvc.ADVANCE_ENTRY + "', 1, 'SA', 'SA', getdate(), 'Advance Entry','Account') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.FINAL_PAYMENT_REQUEST_ENTRY + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.FINAL_PAYMENT_REQUEST_ENTRY + "', '" + codeValue + "', 'Final Payment Request Entry', '" + lvc.FINAL_PAYMENT_REQUEST_ENTRY + "', 1, 'SA', 'SA', getdate(), 'Final Payment Request Entry','Account') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.FINAL_PAYMENT_ENTRY + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.FINAL_PAYMENT_ENTRY + "', '" + codeValue + "', 'Final Payment Entry', '" + lvc.FINAL_PAYMENT_ENTRY + "', 1, 'SA', 'SA', getdate(), 'Final Payment Entry','Account') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            //===
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.INVENTORY_ITEM_MAST + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Inventory', '" + lvc.INVENTORY_ITEM_MAST + "', '" + codeValue + "', 'Item Mast', '" + lvc.INVENTORY_ITEM_MAST + "', 1, 'SA', 'SA', getdate(), 'Item Mast','Inventory') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + lvc.BILL_HANDLE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('FA', '" + lvc.BILL_HANDLE + "', '" + codeValue + "', 'Bill Handle', '" + lvc.BILL_HANDLE + "', 1, 'SA', 'SA', getdate(),'" + "Bill Handle" + "','FA') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From subGroupNature;", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('001','LG','Ledger');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('002','BK','Bank');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('003','CS','Cash');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('004','AG','Agent');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('005','CT','Customer');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('006','EM','Employee');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('007','OT','Others');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('008','SU','Supplier');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('009','TP','Transporter');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('010','BR','Broker');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('011','DP','Department');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('012','PR','Purchase');";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO subGroupNature (Code, manualcode, Name) VALUES ('013','SL','Sales');";
                DmServer.executeSQL(sqlQuery, cnTemp);

            }

            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////      
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From AcGroup ", cnTemp)) <= 0) {
                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0001', NULL, 'Capital Account', 'Capital Account', NULL, '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0002', NULL, 'Loan (Liability)', 'Loan (Liability)', NULL, '0', " + codeValue + ", '1', 'SA', getdate() )";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0003', NULL, 'Current Liabilities', 'Current Liabilities', NULL, '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0004', NULL, 'Fixed Assets', 'Fixed Assets', NULL, '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0005', NULL, 'Investments', 'Investments', NULL, '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0006', NULL, 'Current Assets', 'Current Assets', NULL, '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0007', NULL, 'Branch / Divisions', 'Branch / Divisions', NULL, '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0008', NULL, 'Misc. Expences (Asset)', 'Misc. Expences (Asset)', NULL, '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0009', NULL, 'Suspense A/c', 'Suspense A/c', NULL, '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0010', NULL, 'Reserves & Surplus', 'Reserves & Surplus', '0001', '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0010',1,'0001') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'BK'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0011', NULL,'Bank OD A/c', 'Bank OD A/c', '0002', '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0011',1,'0002') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0012', NULL, 'Secured Loans', 'Secured Loans', NULL, '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0013', NULL, 'Unsecured Loans', 'Unsecured Loans', '0002', '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0013',1,'0002') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0014', NULL, 'Duties & Taxes', 'Duties & Taxes', '0003', '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0014',1,'0003') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0015', NULL, 'Provisions', 'Provisions', '0003', '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0015',1,'0003')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0016', NULL, 'Sundry Creditors', 'Sundry Creditors', '0003', '0', " + codeValue + ", '1', 'SA',getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0016',1,'0003') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0017', NULL, 'Opening Stock', 'Opening Stock', NULL, '2', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0018', NULL, 'Deposits (Asset)', 'Deposits (Asset)', '0006', '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0018',1,'0006') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0019', NULL, 'Loans & Advances (Asset)', 'Loans & Advances (Asset)', '0006', '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0019',1,'0006') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0020', NULL, 'Sundry Debtors', 'Sundry Debtors', '0006', '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0020',1,'0006') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'CS'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0021', NULL, 'Cash-in-Hand', 'Cash-In-Hand', '0006', '1', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0021',1,'0006') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'BK'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0022', NULL, 'Bank Accounts', 'Bank Accounts', '0006', '1', " + codeValue + ", '1', 'sa', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
                        + "VALUES ('0022',1,'0006') ";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'SL'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0023', NULL, 'Sales Accounts', 'Sales Accounts', NULL, '4', " + codeValue + ", '1', 'SA',getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'PR'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0024', NULL, 'Purchase Accounts', 'Purchase Accounts', NULL, '2', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0025', NULL, 'Direct Incomes', 'Direct Incomes', NULL, '4', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0026', NULL, 'Direct Expenses', 'Direct Expenses', NULL, '2', " + codeValue + ", '1', 'SA',getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0027', NULL, 'Indirect Incomes', 'Indirect Incomes', NULL, '5', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0028', NULL, 'Indirect Expenses', 'Indirect Expenses', NULL, '3', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0029', NULL, 'Profit & Loss A/c', 'Profit & Loss A/c', NULL, '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'LG'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0030', NULL, 'Closing Stock', 'Closing Stock', NULL, '4', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

                codeValue = DmShared.iNull(DmServer.executeScalar("Select code From subGroupNature sgn Where sgn.manualcode = 'EM'", cnTemp)).toString();
                sqlQuery = "INSERT INTO AcGroup (GroupCode, SNo, GroupName, ContraGroupName, GroupUnder, GroupNature, Nature, isSysGroup, U_Name, U_EntDt)"
                        + "VALUES ('0031', NULL, 'Employee', 'Employee', '0016', '0', " + codeValue + ", '1', 'SA', getdate())";
                DmServer.executeSQL(sqlQuery, cnTemp);

//                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
//                        + "VALUES ('0031',1,'0003') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                sqlQuery = "Insert Into AcGroupPath(GroupCode,SNo,GroupUnder) "
//                        + "VALUES ('0031',2,'0016') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From InitialMast ", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO InitialMast (Code, Name)"
                        + "VALUES ('01', 'Mr.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO InitialMast (Code, Name)"
                        + "VALUES ('02', 'Mrs.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO InitialMast (Code, Name)"
                        + "VALUES ('03', 'Ms.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO InitialMast (Code, Name)"
                        + "VALUES ('04', 'Shri')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO InitialMast (Code, Name)"
                        + "VALUES ('05', 'Dr.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO InitialMast (Code, Name)"
                        + "VALUES ('06', 'M/s')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From ItemType ", cnTemp)) <= 0) {
                sqlQuery = "INSERT INTO ItemType (Code, ManualCode, Name)"
                        + "VALUES ('001','FM','Finished Mtrl.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('002','OT', 'Others')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('003','PM', 'Packing Mtrl.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('004','RM', 'Raw Mtrl.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('005','SM', 'Store Mtrl.')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('006','FL', 'Fuel')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('007','CL', 'Coal')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('008','CM', 'Chemical')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('009','SF', 'Semi Finished')";
                DmServer.executeSQL(sqlQuery, cnTemp);

                sqlQuery = "INSERT INTO ItemType (Code, manualcode, Name)"
                        + "VALUES ('0010','MC', 'Machine')";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//            iniHL7(cnTemp);
            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getAccountId' and xType = 'P'", cnTemp)) <= 0) {
                sqlQuery = "CREATE PROCEDURE [dbo].[getAccountId]  (@vType smallint,  @fieldUDT dbo.fieldUDT READONLY) "
                        + "AS BEGIN "
                        + "SET NOCOUNT ON  "
                        + "DECLARE  @serialNo bigint, @manualCode varchar(20), @manualCodeExsits INT; "
                        + "DECLARE @accountName VARCHAR(100),@acGroup VARCHAR(100),@nature VARCHAR(50), "
                        + "@tempAcName varchar(100),@tempAcGroup VARCHAR(100),@tempNature VARCHAR(50),@accountPrefix varchar(10); "
                        + "set @serialNo = 0  "
                        + "set @manualCode = ''  "
                        + "set @manualCodeExsits = 0 "
                        + "SET @accountName = '' "
                        + "SET @acGroup = '' "
                        + "SET @nature = '' "
                        + "SET @tempAcName='' "
                        + "SET @tempAcGroup='' "
                        + "SET @tempNature='' "
                        + "SET @accountPrefix=''; "
                        + "SELECT @accountName = fieldText FROM @fieldUDT WHERE fieldCode = 'accountName' "
                        + "SELECT @acGroup = fieldText FROM @fieldUDT WHERE fieldCode = 'acGroup' "
                        + "SELECT @nature = fieldText FROM @fieldUDT WHERE fieldCode = 'nature' "
                        + "SET @tempAcName=SUBSTRING(@accountName, 1, 2) "
                        + "SET @tempAcGroup=SUBSTRING(@acGroup, 1, 2) "
                        + "SET @tempNature=SUBSTRING(@nature, 1, 2) "
                        + "SET @accountPrefix=@tempAcName+@tempAcGroup+@tempNature; "
                        + "while 1 = 1 "
                        + "begin "
                        + "UPDATE	numberSystemCounter SET  @serialNo = [counter] = isNull([counter], 0) + 1  "
                        + "WHERE  nsGroup = @vType and isNull(nsFormatValue,'') = isNull(@accountPrefix,'')  "
                        + "IF isNull(@serialNo, 0) <= 0  "
                        + "BEGIN   "
                        + "SET @serialNo = 1  "
                        + "INSERT numberSystemCounter(nsGroup, v_Prefix, site_Code, nsFormatValue, [counter])  "
                        + "VALUES  (@vType, null, null, isNull(@accountPrefix,''),@serialNo)  "
                        + "END  "
                        + "Set @manualCode = concat(@accountPrefix, FORMAT(@serialNo, '0')) "
                        + "set @manualCodeExsits = (select count(*) as cnt from subGroup where manualCode = @manualCode) "
                        + "if(isNull(@manualCodeExsits,0) = 0) "
                        + "begin "
                        + "break; "
                        + "end "
                        + "end "
                        + "select @manualCode as manualCode "
                        + "END ";

                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getCustomerId' and xType = 'P'", cnTemp)) <= 0) {
                sqlQuery = "CREATE PROCEDURE [dbo].[getCustomerId]  (@vType smallint, @fieldUDT dbo.fieldUDT READONLY) "
                        + "AS "
                        + "BEGIN "
                        + "SET NOCOUNT ON  "
                        + "DECLARE  @serialNo bigint, @manualCode varchar(20), @manualCodeExsits int "
                        + "set @serialNo = 0  set @manualCode = ''  "
                        + "set @manualCodeExsits = 0; "
                        + "while 1 = 1 "
                        + "begin "
                        + "UPDATE	numberSystemCounter SET  "
                        + "@serialNo = [counter] = isNull([counter], 0) + 1  "
                        + "WHERE  nsGroup = @vType and isNull(nsFormatValue,'') = isNull('CT','')  "
                        + "IF isNull(@serialNo, 0) <= 0  "
                        + "BEGIN   "
                        + "SET @serialNo = 1  "
                        + "INSERT numberSystemCounter(nsGroup, v_Prefix, site_Code, nsFormatValue, [counter])  VALUES  "
                        + "(@vType, null, null, isNull('CT',''),@serialNo)  "
                        + "END  "
                        + "Set @manualCode = concat('CT', FORMAT(@serialNo, '0')) "
                        + "set @manualCodeExsits = (select count(*) as cnt from subGroup where manualCode = @manualCode) "
                        + "if(isNull(@manualCodeExsits,0) = 0) "
                        + "begin "
                        + "break; "
                        + "end "
                        + "end "
                        + "select @manualCode as manualCode "
                        + "END ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getSupplierId' and xType = 'P'", cnTemp)) <= 0) {

                sqlQuery = "CREATE PROCEDURE [dbo].[getSupplierId]  (@vType smallint,  @fieldUDT dbo.fieldUDT READONLY)  "
                        + "AS BEGIN  "
                        + "SET NOCOUNT ON  "
                        + "DECLARE  @serialNo bigint, @manualCode varchar(20), @manualCodeExsits INT; "
                        + "DECLARE @supplierName VARCHAR(100),@acGroup VARCHAR(100),@nature VARCHAR(50),@partyCategory VARCHAR(15), "
                        + "@tempSupName varchar(100),@tempAcGroup VARCHAR(100),@tempNature VARCHAR(50),@tempPartyCategory VARCHAR(15), "
                        + "@supplierPrefix varchar(10); "
                        + "set @serialNo = 0  "
                        + "set @manualCode = ''  "
                        + "set @manualCodeExsits = 0 "
                        + "SET @supplierName = '' "
                        + "SET @acGroup = '' "
                        + "SET @nature = '' "
                        + "SET @partyCategory = '' "
                        + "SET @tempSupName='' "
                        + "SET @tempAcGroup='' "
                        + "SET @tempNature='' "
                        + "SET @tempPartyCategory='' "
                        + "SET @supplierPrefix=''; "
                        + "SELECT @supplierName = fieldText FROM @fieldUDT WHERE fieldCode = 'supplierName' "
                        + "SELECT @acGroup = fieldText FROM @fieldUDT WHERE fieldCode = 'acGroup' "
                        + "SELECT @nature = fieldText FROM @fieldUDT WHERE fieldCode = 'nature' "
                        + "SELECT @partyCategory = fieldText FROM @fieldUDT WHERE fieldCode = 'partyCategory' "
                        + "SET @tempSupName=SUBSTRING(@supplierName, 1, 2) "
                        + "SET @tempAcGroup=SUBSTRING(@acGroup, 1, 2) "
                        + "SET @tempNature=SUBSTRING(@nature, 1, 2) "
                        + "SET @tempPartyCategory=SUBSTRING(@partyCategory, 1, 2) "
                        + "SET @supplierPrefix=@tempSupName+@tempAcGroup+@tempNature+@tempPartyCategory; "
                        + "while 1 = 1 "
                        + "begin "
                        + "UPDATE	numberSystemCounter SET  @serialNo = [counter] = isNull([counter], 0) + 1  "
                        + "WHERE  nsGroup = @vType and isNull(nsFormatValue,'') = isNull(@supplierPrefix,'')  "
                        + "IF isNull(@serialNo, 0) <= 0  "
                        + "BEGIN   "
                        + "SET @serialNo = 1  "
                        + "INSERT numberSystemCounter(nsGroup, v_Prefix, site_Code, nsFormatValue, [counter])  "
                        + "VALUES  (@vType, null, null, isNull(@supplierPrefix,''),@serialNo)  "
                        + "END  "
                        + "Set @manualCode = concat(@supplierPrefix, FORMAT(@serialNo, '0')) "
                        + "set @manualCodeExsits = (select count(*) as cnt from subGroup where manualCode = @manualCode) "
                        + "if(isNull(@manualCodeExsits,0) = 0) "
                        + "begin "
                        + "break; "
                        + "end "
                        + "end "
                        + "select @manualCode as manualCode "
                        + "END ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getEmployeeId' and xType = 'P'", cnTemp)) <= 0) {

                sqlQuery = "create PROCEDURE [dbo].[getEmployeeId]  (@vType smallint,  @fieldUDT dbo.fieldUDT READONLY)  "
                        + "AS BEGIN  "
                        + "SET NOCOUNT ON  "
                        + "DECLARE  @serialNo bigint, @manualCode varchar(20), @manualCodeExsits INT; "
                        + "DECLARE @empName VARCHAR(100),@acGroup VARCHAR(100),@nature VARCHAR(50), "
                        + "@tempEmpName varchar(100),@tempAcGroup VARCHAR(100),@tempNature VARCHAR(50),@empPrefix varchar(10); "
                        + "set @serialNo = 0  "
                        + "set @manualCode = ''  "
                        + "set @manualCodeExsits = 0 "
                        + "SET @empName = '' "
                        + "SET @acGroup = '' "
                        + "SET @nature = '' "
                        + "SET @tempEmpName='' "
                        + "SET @tempAcGroup='' "
                        + "SET @tempNature='' "
                        + "SET @empPrefix=''; "
                        + "SELECT @empName = fieldText FROM @fieldUDT WHERE fieldCode = 'empName' "
                        + "SELECT @acGroup = fieldText FROM @fieldUDT WHERE fieldCode = 'acGroup' "
                        + "SELECT @nature = fieldText FROM @fieldUDT WHERE fieldCode = 'nature' "
                        + "SET @tempEmpName=SUBSTRING(@empName, 1, 2) "
                        + "SET @tempAcGroup=SUBSTRING(@acGroup, 1, 2) "
                        + "SET @tempNature=SUBSTRING(@nature, 1, 2) "
                        + "SET @empPrefix=@tempEmpName+@tempAcGroup+@tempNature; "
                        + "while 1 = 1 "
                        + "begin "
                        + "UPDATE	numberSystemCounter SET  @serialNo = [counter] = isNull([counter], 0) + 1  "
                        + "WHERE  nsGroup = @vType and isNull(nsFormatValue,'') = isNull(@empPrefix,'')  "
                        + "IF isNull(@serialNo, 0) <= 0  "
                        + "BEGIN   "
                        + "SET @serialNo = 1  "
                        + "INSERT numberSystemCounter(nsGroup, v_Prefix, site_Code, nsFormatValue, [counter])  "
                        + "VALUES  (@vType, null, null, isNull(@empPrefix,''),@serialNo)  "
                        + "END  "
                        + "Set @manualCode = concat(@empPrefix, FORMAT(@serialNo, '0')) "
                        + "set @manualCodeExsits = (select count(*) as cnt from subGroup where manualCode = @manualCode) "
                        + "if(isNull(@manualCodeExsits,0) = 0) "
                        + "begin "
                        + "break; "
                        + "end "
                        + "end "
                        + "select @manualCode as manualCode "
                        + "END";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
            }

            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getItemManualCode' and xType = 'P'", cnTemp)) <= 0) {
                sqlQuery = "Create PROCEDURE [dbo].[getItemManualCode] "
                        + " (@vType smallint,@fieldUDT dbo.fieldUDT READONLY)  AS BEGIN   "
                        + " SET NOCOUNT ON   "
                        + " DECLARE  @serialNo bigint,  "
                        + " 		@manualCode varchar(20),  "
                        + " 		@manualCodeExsits INT;  "
                        + " DECLARE @itemName VARCHAR(100), "
                        + " 		@itemGroup VARCHAR(100), "
                        + " 		@itemCat VARCHAR(50),  "
                        + " 		@tempItemName varchar(100), "
                        + " 		@tempItemGroup VARCHAR(100), "
                        + " 		@tempItemCat VARCHAR(50), "
                        + " 		@itemPrefix varchar(10);  "
                        + " 		set @serialNo = 0   "
                        + " 		set @manualCode = ''   "
                        + " 		set @manualCodeExsits = 0  "
                        + " 		SET @itemName = ''  "
                        + " 		SET @itemGroup = ''  "
                        + " 		SET @itemCat = ''  "
                        + " 		SET @tempItemName=''  "
                        + " 		SET @tempItemGroup=''  "
                        + " 		SET @tempItemCat=''  "
                        + " 		SET @itemPrefix='';  "
                        + " 		SELECT @itemName = fieldText  "
                        + " 				FROM @fieldUDT  "
                        + " 				WHERE fieldCode = 'itemName'  "
                        + " 		SELECT @itemGroup = fieldText  "
                        + " 				FROM @fieldUDT "
                        + " 				WHERE fieldCode = 'itemGroup'  "
                        + " 		SELECT @itemCat = fieldText  "
                        + " 				FROM @fieldUDT  "
                        + " 				WHERE fieldCode = 'itemCat'  "
                        + " 		SET @tempItemName=SUBSTRING(@itemName, 1, 2)  "
                        + " 		SET @tempItemGroup=SUBSTRING(@itemGroup, 1, 2)  "
                        + " 		SET @tempItemCat=SUBSTRING(@itemCat, 1, 2)  "
                        + " 		SET @itemPrefix=@tempItemName+@tempItemGroup+@tempItemCat;  "
                        + " 		while 1 = 1 begin  "
                        + " 		UPDATE	numberSystemCounter  "
                        + " 			SET  @serialNo = [counter] = isNull([counter], 0) + 1   "
                        + " 			WHERE  nsGroup = @vType and isNull(nsFormatValue,'') = isNull(@itemPrefix,'')   "
                        + " 		IF isNull(@serialNo, 0) <= 0   "
                        + " 			BEGIN    "
                        + " 			SET @serialNo = 1   "
                        + " 			INSERT numberSystemCounter(nsGroup, v_Prefix, site_Code, nsFormatValue, [counter])   "
                        + " 			VALUES  (@vType, null, null, isNull(@itemPrefix,''),@serialNo)   "
                        + " 			END   "
                        + " 		Set @manualCode = concat(@itemPrefix,  FORMAT(@serialNo, '0'))  "
                        + " 		set @manualCodeExsits = (select count(*) as cnt from itemMast where manualCode = @manualCode)  "
                        + " 		if(isNull(@manualCodeExsits,0) = 0) begin break;  "
                        + " 				end  "
                        + " 				end  "
                        + " 		select @manualCode as manualCode END                 "
                        + "             ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();

                if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'getServiceManualCode' and xType = 'P'", cnTemp)) <= 0) {
                    sqlQuery = "Create PROCEDURE [dbo].[getServiceManualCode] "
                            + " (@vType smallint,@fieldUDT dbo.fieldUDT READONLY)  AS BEGIN   "
                            + " SET NOCOUNT ON   "
                            + " DECLARE  @serialNo bigint,  "
                            + " 		@manualCode varchar(20),  "
                            + " 		@manualCodeExsits INT;  "
                            + " DECLARE @itemName VARCHAR(100), "
                            + " 		@itemGroup VARCHAR(100), "
                            + " 		@itemCat VARCHAR(50),  "
                            + " 		@tempItemName varchar(100), "
                            + " 		@tempItemGroup VARCHAR(100), "
                            + " 		@tempItemCat VARCHAR(50), "
                            + " 		@itemPrefix varchar(10);  "
                            + " 		set @serialNo = 0   "
                            + " 		set @manualCode = ''   "
                            + " 		set @manualCodeExsits = 0  "
                            + " 		SET @itemName = ''  "
                            + " 		SET @itemGroup = ''  "
                            + " 		SET @itemCat = ''  "
                            + " 		SET @tempItemName=''  "
                            + " 		SET @tempItemGroup=''  "
                            + " 		SET @tempItemCat=''  "
                            + " 		SET @itemPrefix='';  "
                            + " 		SELECT @itemName = fieldText  "
                            + " 				FROM @fieldUDT  "
                            + " 				WHERE fieldCode = 'itemName'  "
                            + " 		SELECT @itemGroup = fieldText  "
                            + " 				FROM @fieldUDT "
                            + " 				WHERE fieldCode = 'itemGroup'  "
                            + " 		SELECT @itemCat = fieldText  "
                            + " 				FROM @fieldUDT  "
                            + " 				WHERE fieldCode = 'itemCat'  "
                            + " 		SET @tempItemName=SUBSTRING(@itemName, 1, 2)  "
                            + " 		SET @tempItemGroup=SUBSTRING(@itemGroup, 1, 2)  "
                            + " 		SET @tempItemCat=SUBSTRING(@itemCat, 1, 2)  "
                            + " 		SET @itemPrefix=@tempItemName+@tempItemGroup+@tempItemCat;  "
                            + " 		while 1 = 1 begin  "
                            + " 		UPDATE	numberSystemCounter  "
                            + " 			SET  @serialNo = [counter] = isNull([counter], 0) + 1   "
                            + " 			WHERE  nsGroup = @vType and isNull(nsFormatValue,'') = isNull(@itemPrefix,'')   "
                            + " 		IF isNull(@serialNo, 0) <= 0   "
                            + " 			BEGIN    "
                            + " 			SET @serialNo = 1   "
                            + " 			INSERT numberSystemCounter(nsGroup, v_Prefix, site_Code, nsFormatValue, [counter])   "
                            + " 			VALUES  (@vType, null, null, isNull(@itemPrefix,''),@serialNo)   "
                            + " 			END   "
                            + " 		Set @manualCode = concat(@itemPrefix,  FORMAT(@serialNo, '0'))  "
                            + " 		set @manualCodeExsits = (select count(*) as cnt from itemMast where manualCode = @manualCode)  "
                            + " 		if(isNull(@manualCodeExsits,0) = 0) begin break;  "
                            + " 				end  "
                            + " 				end  "
                            + " 		select @manualCode as manualCode END                 "
                            + "             ";
                    psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                    psTemp.executeUpdate();
                    psTemp.close();
                }
            }

            spBegin.commit();
        } catch (Exception ex) {
            try {
                if (spBegin != null) {
                    spBegin.rollBack();
                }
            } catch (Exception e) {
            }
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

//    private void iniHL7(Connection cnTemp) throws Exception {
//        DmPreparedStatement psTemp = null;
//        ResultSet rsTemp = null;
//        String sqlQuery = "", codeValue = "";
//
//        try {
//
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From hl7Mast;", cnTemp)) <= 0) {
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'administrativeSex', 'Ambiguous', 'A', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'administrativeSex', 'Female', 'F', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'administrativeSex', 'Male', 'M', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'administrativeSex', 'Not applicable', 'N', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'administrativeSex', 'Other', 'O', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'administrativeSex', 'Unknown', 'U', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Separated', 'A', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Unmarried', 'B', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Common law', 'C', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Divorced', 'D', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Legally Separated', 'E', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Living together', 'G', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Interlocutory', 'I', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Married', 'M', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Annulled', 'N', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Other', 'O', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Domestic partner', 'P', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Registered domestic partner', 'R', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Single', 'S', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Unreported', 'T', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Unknown', 'U', NULL, 0)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'maritalStatus', 'Widowed', 'W', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'race', 'American Indian or Alaska Native', '1002-5', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'race', 'Asian', '2028-9', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'race', 'Black or African American', '2054-5', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'race', 'Native Hawaiian or Other Pacific Islander', '2076-8', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'race', 'White', '2106-3', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'race', 'Other Race', '2131-1', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'religion', 'Buddhist', 'BUD', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'religion', 'Christian', 'CHR', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'religion', 'Hindu', 'HIN', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'religion', 'Jain', 'JAI', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'religion', 'Muslim', 'MOS', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'religion', 'Sikh', 'SIK', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'vipType', 'VIP', 'V', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'vipType', 'VVIP', 'VV', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'vipType', 'Democrat', 'D', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'identifiertype', 'Aadhaar', NULL, NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'identifiertype', 'Voter Id', NULL, NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'identifiertype', 'BPL No.', NULL, NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'identifiertype', 'Ration Card', NULL, NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'A+', 'A+', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'A-', 'A-', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'B+', 'B+', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'B-', 'B-', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'AB+', 'AB+', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'AB-', 'AB-', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'O+', 'O+', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'bloodGroup', 'O-', 'O-', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive,priorityIndex,colorName)"
//                        + " VALUES ('" + codeValue + "', 'priorityType', 'As soon as possible (a priority lower than stat)', 'A', NULL, 1,2,'#f8cbad')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive,priorityIndex,colorName)"
//                        + " VALUES ('" + codeValue + "', 'priorityType', 'Preoperative (to be done prior to surgery)', 'P', NULL, 1,1,'#9bc2e6')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive,priorityIndex,colorName)"
//                        + " VALUES ('" + codeValue + "', 'priorityType', 'Routine', 'R', NULL, 1,0,'')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive,priorityIndex,colorName)"
//                        + " VALUES ('" + codeValue + "', 'priorityType', 'Stat (do immediately)', 'S', NULL, 1,4,'#ff5b5b')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive,priorityIndex,colorName)"
//                        + " VALUES ('" + codeValue + "', 'priorityType', 'Timing critical (do as near as possible to requested time)', 'T', NULL, 1,3,'#92d050')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'castName', 'OBC', 'OBC', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'castName', 'General', 'GEN', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'castName', 'SC', 'SC', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                codeValue = DmServer.getSerialNoInteger("1", "hl7Mast", cnTemp);
//                sqlQuery = "INSERT INTO hl7Mast (code, tableName, name, shortName, remark, isActive)"
//                        + " VALUES ('" + codeValue + "', 'castName', 'ST', 'ST', NULL, 1)";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//
//        } catch (Exception ex) {
//            refreshDataStructure.addResultLog("iniHL7", "", this.getClass().getName(), sqlQuery, ex.getMessage());
//            throw new DmException(0, ex.getMessage());
//        } finally {
//            if (rsTemp != null) {
//                rsTemp.close();
//            }
//            if (psTemp != null) {
//                psTemp.close();
//            }
//        }
//    }
}
