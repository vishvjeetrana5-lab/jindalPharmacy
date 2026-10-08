package aarogya.pharmacy.self.server;

import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.dmPrepareStatement.server.DmPreparedStatement;
import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.sql.Connection;
import java.sql.ResultSet;

public class PharmacyDataStructure {

    private DmRefreshDataStructure refreshDataStructure;

    public PharmacyDataStructure(DmRefreshDataStructure refreshDataStructure) throws Exception {
        this.refreshDataStructure = refreshDataStructure;

//        itemProperty("itemProperty");
        medicineCompoMast("medicineCompoMast");
        medicineComposition("medicineComposition");
        medicineTemplateMast("medicineTemplateMast");
        medicineTemplateDetail("medicineTemplateDetail");

//        medicineType("medicineType");
//        medicineCat("medicineCat");
//        medicineGroup("medicineGroup");
//        medicineIndent("medicineIndent");
//        medicineIndentDetail("medicineIndentDetail");
//        medicineMakeMast("medicineMakeMast");
//        medicineMast("medicineMast");
//        medicineSiteList("medicineSiteList");
//        medicineDepartmentInfo("medicineDepartmentInfo");
//        medicineRequisition("medicineRequisition");
//        medicineRequisitionDetail("medicineRequisitionDetail");
//        medicineBill("medicineBill");
//        medicineBillDetail("medicineBillDetail");
//        medicineBillBatchDetail("medicineBillBatchDetail");
//        itemBatch("itemBatch");
//        medicineBatch("medicineBatch");
//        medicineBatchDetail("medicineBatchDetail");
//        medicineStockHead("medicineStockHead");
//        medicineStock("medicineStock");
//
//        medicinePOrder("medicinePOrder");
//        medicinePOrderDetail("medicinePOrderDetail");
//        medicinePOrderIndentDetail("medicinePOrderIndentDetail");
//
//        medicinePChallan("medicinePChallan");
//        medicinePChallanDetail("medicinePChallanDetail");
//
//        medicinePBill("medicinePBill");
//        medicinePBillDetail("medicinePBillDetail");
    }

    private void medicineTemplateMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    private void medicineTemplateDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("quantity", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setForeignKey("code", "code", "medicineTemplateMast");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");

    }
//    public void itemProperty(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, SQLIsNullable.NOT_NULL, null);
//        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "5", null, SQLIsNullable.NOT_NULL, null);
//        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "1");
//
//        //Set Unique Key
//        configDataStructure.setUnique("name");
//        configDataStructure.setUnique("manualCode");
//    }
//    public void medicinePChallan(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
//        configDataStructure.setColumnValue("pChallanNo", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("pChallanDate", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Roff", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("CMN_Addition", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("CMN_Deduction", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("wtaCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("approvalDateTime", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("transfered", SQLDataType.VARCHAR, "1", null, null, null);
//
//        configDataStructure.setForeignKey("departmentCode", "subCode", "departmentMast");
//        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
//        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
//        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
//    }
//
//    public void medicinePChallanDetail(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("poDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("medicineCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("mbCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("quantity", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("freeQty", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("billedQty", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
//
//        configDataStructure.setForeignKey("medicineCode", "code", "medicineMast");
//        configDataStructure.setForeignKey("mbCode", "code", "medicineBatch");
//        configDataStructure.setForeignKey("poDocId", "docId", "medicinePOrder");
//    }
//
//    public void medicinePBill(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("partyBillNo", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("partyBillDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("CMN_Addition", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("CMN_Deduction", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("wtaCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "100", null, null, null);
//        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("approvalDateTime", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("transfered", SQLDataType.VARCHAR, "1", null, null, null);
//
//        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
//        configDataStructure.setForeignKey("departmentCode", "subCode", "departmentMast");
//        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
//        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
//    }
//
//    public void medicinePBillDetail(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("pcDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("poDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("mbCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("pbDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("medicineCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("quantity", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("freeQty", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "150", null, null, null);
//
//        configDataStructure.setForeignKey("medicineCode", "code", "medicineMast");
//        configDataStructure.setForeignKey("pcDocId", "docId", "medicinePChallan");
//        configDataStructure.setForeignKey("poDocId", "docId", "medicinePOrder");
//        configDataStructure.setForeignKey("pbDocId", "docId", "medicinePBill");
//        configDataStructure.setForeignKey("mbCode", "code", "medicineBatch");
//    }
//
//    public void medicinePOrder(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("refNo", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("refDate", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("currencyCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("CMN_Addition", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("CMN_Deduction", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
//
//        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
//        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
//        configDataStructure.setForeignKey("currencyCode", "code", "currency");
//        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
//    }
//
//    public void medicinePOrderDetail(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("medicineCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("requiredDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("unitCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("quantity", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("challanedQty", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "150", null, null, null);
//
//        configDataStructure.setForeignKey("unitCode", "code", "unitMast");
//        configDataStructure.setForeignKey("medicineCode", "code", "medicineMast");
//    }
//
//    public void medicinePOrderIndentDetail(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("medicineCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("indentDocId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("orderedQty", SQLDataType.FLOAT, null, null, null, null);
//
//        configDataStructure.setForeignKey("indentDocId", "docId", "medicineIndent");
//        configDataStructure.setForeignKey("medicineCode", "code", "medicineMast");
//    }

    private void medicineCompoMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("restrictGroupCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("restrictGroupCode", "code", "restrictedGrpMast");
    }

    private void medicineComposition(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("compositionCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("power", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("unitCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("isBase", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setForeignKey("code", "code", "itemMast");
        configDataStructure.setForeignKey("compositionCode", "code", "medicineCompoMast");
        configDataStructure.setForeignKey("unitCode", "code", "unitMast");

    }
//    public void medicineBill(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_No", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
//        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
//        configDataStructure.setColumnValue("v_Prefix", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("patientInitial", SQLDataType.VARCHAR, "5", null, null, null);
//        configDataStructure.setColumnValue("patientName", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("consultantCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
//        configDataStructure.setColumnValue("cmn_addition", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("cmn_deduction", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_BAMT", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_ROFF", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_CGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_SGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_IGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Taxable", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Discount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_UGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, null, "0");
//        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
//        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("serviceCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("u_EntDt_LatestLine", SQLDataType.DATETIME, null, null, null, null);
//        configDataStructure.setColumnValue("gstReasonCode", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Total", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_RoundOff", SQLDataType.FLOAT, null, null, null, null);
//
//        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
//        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
//        configDataStructure.setForeignKey("departmentCode", "subCode", "departmentMast");
//        configDataStructure.setForeignKey("consultantCode", "subcode", "consultantMast");
//        configDataStructure.setForeignKey("regDocId", "docId", "patientRegistrationUHID");
//    }
//    public void medicineBillDetail(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("quantity", SQLDataType.DECIMAL, null, null, null, null);
//        configDataStructure.setColumnValue("skuQuantity", SQLDataType.DECIMAL, null, null, null, null);
//        configDataStructure.setColumnValue("itemGSTNature", SQLDataType.VARCHAR, "1", null, null, null);
//        configDataStructure.setColumnValue("mrp", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("saleUnitCode", SQLDataType.INT, null, null, null, null);
//        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("skuQtyForRate", SQLDataType.DECIMAL, null, null, null, null);
//        configDataStructure.setColumnValue("mBillDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("mbillV_Sno", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("requisitionDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("requisitionV_SNo", SQLDataType.SMALLINT, null, null, null, null);
//        configDataStructure.setColumnValue("grossAmount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("discountPer", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("discount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Ac_Sales", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Rate_CGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_CGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Ac_CGST", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_SGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Rate_SGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Ac_SGST", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_Taxable", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_UGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Rate_UGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Ac_UGST", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Rate_IGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Amt_IGST", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("oh_Ac_IGST", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("lmDocId", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("isFooter", SQLDataType.TINYINT, null, null, null, "0");
//        configDataStructure.setColumnValue("isReturn", SQLDataType.TINYINT, null, null, null, "0");
//        configDataStructure.setColumnValue("ibCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("saleRate", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("vatPer", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("vatAmount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("satPer", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("satAmount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("netAmount", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("reqDocId", SQLDataType.BIGINT, null, null, null, null);
//
//        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
//        configDataStructure.setForeignKey("ibCode", "code", "itemBatch");
//        configDataStructure.setForeignKey("requisitionDocId,requisitionV_SNo", "docId,v_SNo", "requisitionDetail");
////        configDataStructure.setForeignKey("reqDocId", "docId", "Requisition");
//        configDataStructure.setForeignKey("mBillDocId,mbillV_Sno", "docId,v_SNo", "medicineBillDetail");
////        configDataStructure.setForeignKey("mBillDocId", "docId", "medicineBill");
//        configDataStructure.setForeignKey("oh_Ac_CGST", "subCode", "subGroup");
//        configDataStructure.setForeignKey("oh_Ac_SGST", "subCode", "subGroup");
//        configDataStructure.setForeignKey("oh_Ac_IGST", "subCode", "subGroup");
//        configDataStructure.setForeignKey("oh_Ac_Sales", "subCode", "subGroup");
//        configDataStructure.setForeignKey("oh_Ac_UGST", "subCode", "subGroup");
//        configDataStructure.setForeignKey("lmDocId", "docId", "ledgerM");
//    }
//
//    public void medicineBillBatchDetail(String tableName) throws Exception {
//        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
//        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
//        configDataStructure.setColumnValue("ibCode", SQLDataType.BIGINT, null, null, null, null);
//        configDataStructure.setColumnValue("batchNo", SQLDataType.VARCHAR, "50", null, null, null);
//        configDataStructure.setColumnValue("manufacturingDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("expiryDate", SQLDataType.DATE, null, null, null, null);
//        configDataStructure.setColumnValue("quantity", SQLDataType.DECIMAL, null, null, null, null);
//        configDataStructure.setColumnValue("skuQuantity", SQLDataType.DECIMAL, null, null, null, null);
//        configDataStructure.setColumnValue("saleRate", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("mrp", SQLDataType.FLOAT, null, null, null, null);
//        configDataStructure.setColumnValue("returnedQty", SQLDataType.DECIMAL, null, null, null, null);
//
//        configDataStructure.setForeignKey("ibCode", "code", "itemBatch");
//    }

    public void initilizer() throws Exception {
        Connection cnTemp = null;
        DmPreparedStatement psTemp = null;
        ResultSet rsTemp = null;
        String sqlQuery = "", codeValue = "";
        ProjectVoucherCategory pvc = new ProjectVoucherCategory();
        try {
            cnTemp = DmServer.getConnection(refreshDataStructure.getDatabaseName());

//<editor-fold defaultstate="collapsed" desc="Voucher Type [Pharmacy]">
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_MEDICINE_MAST + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_MEDICINE_MAST + "', '" + codeValue + "', 'Medicine Master', '" + pvc.PHARMACY_MEDICINE_MAST + "', 1, 'SA', 'SA', getdate(), 'Medicine Master','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }
            
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.MEDICINE_TRANSFER_WHOLE_SALE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule,ParentVoucher) "
                        + "VALUES ('Pharmacy', '" + pvc.MEDICINE_TRANSFER_WHOLE_SALE + "', '" + codeValue + "', 'Medicine Transfer (Whole Sale)', '" + pvc.MEDICINE_TRANSFER_WHOLE_SALE + "', 1, 'SA', 'SA', getdate(), 'Medicine Transfer (Whole Sale)','Pharmacy','Stock Transfer') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }
            
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "', '" + codeValue + "', 'Sale Bill (Whole Sale)', '" + pvc.PHARMACY_BILL_WHOLESALE_CASH + "', 1, 'SA', 'SA', getdate(), 'Sale Bill (Whole Sale)','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_LEDGER_MASTER + "'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
//                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_LEDGER_MASTER + "', '" + codeValue + "', 'Pharmacy Ledger Master', '" + pvc.PHARMACY_LEDGER_MASTER + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Ledger Master','Pharmacy') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHREQ'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'PHREQ', '" + codeValue + "', 'Pharmacy Requisition', 'PHREQ', 1, 'SA', 'SA', getdate(), 'Pharmacy Requisition','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHIND'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'PHIND', '" + codeValue + "', 'Pharmacy Indent', 'PHIND', 1, 'SA', 'SA', getdate(), 'Pharmacy Indent','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_ENQUIRY + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_ENQUIRY + "', '" + codeValue + "', 'Pharmacy Enquiry', '" + pvc.PHARMACY_ENQUIRY + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Enquiry','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_QUOTATION + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_QUOTATION + "', '" + codeValue + "', 'Pharmacy Quotation', '" + pvc.PHARMACY_QUOTATION + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Quotation','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHPO'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'PHPO', '" + codeValue + "', 'Pharmacy Purchase Order', 'PHPO', 1, 'SA', 'SA', getdate(), 'Pharmacy Purchase Order','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_BILL + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_BILL + "', '" + codeValue + "', 'Pharmacy Bill', '" + pvc.PHARMACY_BILL + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Bill','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_BILL_RETURN + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_BILL_RETURN + "', '" + codeValue + "', 'Pharmacy Bill Return', '" + pvc.PHARMACY_BILL_RETURN + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Bill Return','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "', '" + codeValue + "', 'Pharmacy Bill Final Discount', '" + pvc.PHARMACY_BILL_FINAL_DISCOUNT + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Bill Final Discount','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_CHANGE_PAYMENT_SOURCE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_CHANGE_PAYMENT_SOURCE + "', '" + codeValue + "', 'Pharmacy Change Payment Source', '" + pvc.PHARMACY_CHANGE_PAYMENT_SOURCE + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Change Payment Source','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHRTN'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
//                        + "VALUES ('Pharmacy', 'PHRTN', '" + codeValue + "', 'Pharmacy Return', 'PHRTN', 1, 'SA', 'SA', getdate(), 'Pharmacy Return','Pharmacy') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHRCT'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
//                        + "VALUES ('Pharmacy', 'PHRCT', '" + codeValue + "', 'Pharmacy Receipt', 'PHRCT', 1, 'SA', 'SA', getdate(), 'Pharmacy Receipt','Pharmacy') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'MEDIS'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'MEDIS', '" + codeValue + "', 'Medicine Issue', 'MEDIS', 1, 'SA', 'SA', getdate(), 'Medicine Issue','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'MEDRC'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'MEDRC', '" + codeValue + "', 'Medicine Receive', 'MEDRC', 1, 'SA', 'SA', getdate(), 'Medicine Receive','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'MEDTR'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'MEDTR', '" + codeValue + "', 'Medicine Transfer', 'MEDTR', 1, 'SA', 'SA', getdate(), 'Medicine Transfer','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'MEDOP'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'MEDOP', '" + codeValue + "', 'Medicine Opening', 'MEDOP', 1, 'SA', 'SA', getdate(), 'Medicine Opening','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PHYSICAL_STOCK + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PHYSICAL_STOCK + "', '" + codeValue + "', 'Pharmacy Physical Stock Entry', '" + pvc.PHARMACY_PHYSICAL_STOCK + "', 1, 'SA', 'SA', getdate(), 'Physical Stock Entry','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "', '" + codeValue + "', 'Pharmacy Physical Stock Posting', '" + pvc.PHARMACY_PHYSICAL_STOCK_POSTING + "', 1, 'SA', 'SA', getdate(), 'Physical Stock Posting','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PHYSICAL_STOCK_CLEARANCE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PHYSICAL_STOCK_CLEARANCE + "', '" + codeValue + "', 'Pharmacy Physical Stock Clearance', '" + pvc.PHARMACY_PHYSICAL_STOCK_CLEARANCE + "', 1, 'SA', 'SA', getdate(), 'Physical Stock Clearance','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'ESTBL'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
//                        + "VALUES ('Pharmacy', 'ESTBL', '" + codeValue + "', 'Estimate Bill', 'ESTBL', 1, 'SA', 'SA', getdate(), 'Estimate Bill','Pharmacy') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
//
//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'ESTMT'", cnTemp)) <= 0) {
//                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
//                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
//                        + "VALUES ('Pharmacy', 'ESTMT', '" + codeValue + "', 'Pharmacy Estimate', 'ESTMT', 1, 'SA', 'SA', getdate(), 'Pharmacy Estimate','Pharmacy') ";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_ESTIMATE_BILL + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_ESTIMATE_BILL + "', '" + codeValue + "', 'Pharmacy Estimate Bill', '" + pvc.PHARMACY_ESTIMATE_BILL + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Estimate Bill','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_ESTIMATE + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_ESTIMATE + "', '" + codeValue + "', 'Pharmacy Estimate', '" + pvc.PHARMACY_ESTIMATE + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Estimate','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_BILL_RECEIPT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_BILL_RECEIPT + "', '" + codeValue + "', 'Pharmacy Bill Receipt', '" + pvc.PHARMACY_BILL_RECEIPT + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Bill Receipt','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "', '" + codeValue + "', 'Pharmacy Bill Return Receipt', '" + pvc.PHARMACY_BILL_RETURN_RECEIPT + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Bill Receipt','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_RECEIPT + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_RECEIPT + "', '" + codeValue + "', 'Pharmacy Receipt', '" + pvc.PHARMACY_RECEIPT + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Receipt','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_REFUND + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_REFUND + "', '" + codeValue + "', 'Pharmacy Refund', '" + pvc.PHARMACY_REFUND + "', 1, 'SA', 'SA', getdate(), 'Pharmacy Refund','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHPBL'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'PHPBL', '" + codeValue + "', 'Pharmacy Purchase Bill', 'PHPBL', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', '" + codeValue + "', 'Pharmacy Purchase Bill (U)', '" + pvc.PHARMACY_PURCHASE_BILL_UNREGISTERED + "', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHPBD'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'PHPBD', '" + codeValue + "', 'Pharmacy Purchase Bill Direct', 'PHPBD', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "', '" + codeValue + "', 'Pharmacy Purchase Bill Direct (U)', '" + pvc.PHARMACY_PURCHASE_BILL_DIRECT_UNREGISTERED + "', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = 'PHCHL'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', 'PHCHL', '" + codeValue + "', 'Pharmacy Purchase Challan', 'PHCHL', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "', '" + codeValue + "', 'Pharmacy Purchase Challan (U)', '" + pvc.PHARMACY_PURCHASE_CHALLAN_UNREGISTERED + "', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PURCHASE_RETURN + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PURCHASE_RETURN + "', '" + codeValue + "', 'Pharmacy Purchase Return', '" + pvc.PHARMACY_PURCHASE_RETURN + "', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "', '" + codeValue + "', 'Pharmacy Purchase Return (U)', '" + pvc.PHARMACY_PURCHASE_RETURN_UNREGISTERED + "', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }

            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From Voucher_Type VT Where VT.Short_Name = '" + pvc.MEDICINE_RETURN_REQUEST  + "'", cnTemp)) <= 0) {
                codeValue = DmServer.vNull(DmServer.executeScalar("Select isNull(max(v_type),0) + 1 As Cnt From Voucher_Type VT ", cnTemp), "0");
                sqlQuery = "INSERT INTO Voucher_Type (NCat, Category, V_Type, Description, Short_Name, isSystemDefine, u_Name, PreparedBy, U_EntDt, MnuText,MnuAttachedInModule) "
                        + "VALUES ('Pharmacy', '" + pvc.MEDICINE_RETURN_REQUEST  + "', '" + codeValue + "', 'Medicine Return Request', '" + pvc.MEDICINE_RETURN_REQUEST  + "', 1, 'SA', 'SA', getdate(), '','Pharmacy') ";
                DmServer.executeSQL(sqlQuery, cnTemp);
            }
//</editor-fold>

//            if (DmShared.vNull(DmServer.executeScalar("Select Count(*) As Cnt From medicineType ", cnTemp)) <= 0) {
//                sqlQuery = "INSERT INTO medicineType (Code, ManualCode, Name)"
//                        + "VALUES ('001','MD','Medicine')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//
//                sqlQuery = "INSERT INTO medicineType (Code, ManualCode, Name)"
//                        + "VALUES ('002','PH','Pharmacy')";
//                DmServer.executeSQL(sqlQuery, cnTemp);
//            }
            if (DmShared.vNull(DmServer.executeScalar("select count(*) as cnt from sysObjects where Name = 'secondryQty' and xType = 'FN'", cnTemp)) <= 0) {
                sqlQuery = "create Function [dbo].[secondryQty] (@skuQty decimal, @convertionRate decimal) Returns varchar(100) "
                        + "As  "
                        + "begin "
                        + "declare @secondPart varchar(50) "
                        + "declare @skuPart varchar(50) "
                        + "set @skuQty = round(@skuQty,7) "
                        + "set @convertionRate = round(@convertionRate,7) "
                        + "if @convertionRate = 0 	begin "
                        + "set @secondPart = '0' "
                        + "set @skuPart = RTrim(cast(@skuQty as bigint)) "
                        + "end else if @skuQty <> 0 begin  "
                        + "set @secondPart = RTrim(cast((@skuQty / @convertionRate) as bigint)) "
                        + "set @skuPart = RTrim(@skuQty % @convertionRate) "
                        + "end else begin "
                        + "set @secondPart = '0' "
                        + "set @skuPart = '0' "
                        + "end "
                        + "Return concat(@secondPart , ' : ' , @skuPart) "
                        + "end; ";
                psTemp = DmPreparedStatement.prepareStatement(cnTemp, sqlQuery);
                psTemp.executeUpdate();
                psTemp.close();
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
