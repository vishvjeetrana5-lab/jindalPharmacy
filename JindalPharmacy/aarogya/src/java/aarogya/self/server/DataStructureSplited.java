package aarogya.self.server;

import dataman.dmBase.dmRefreshDataStructure.server.DmConfigDataStructure;
import dataman.dmBase.dmRefreshDataStructure.server.DmRefreshDataStructure;
import dataman.dmBase.self.shared.DmEnum.SQLColumnType;
import dataman.dmBase.self.shared.DmEnum.SQLDataType;
import dataman.dmBase.self.shared.DmEnum.SQLIsNullable;

public class DataStructureSplited {

    DmRefreshDataStructure refreshDataStructure;

    public DataStructureSplited(DmRefreshDataStructure refreshDataStructure) {
        this.refreshDataStructure = refreshDataStructure;
    }

    protected void cancellBillService(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("psdDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("psdV_Sno", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("sbDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("grossAmount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("discount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("discountReason", SQLDataType.VARCHAR, "50", null, null, null);

        configDataStructure.setForeignKey("sbDocId", "docId", "saleBill");

    }

    protected void tblPatientHeader(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("id", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("transactionID", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("rcptNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("labNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("regNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("opdNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("type", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("ipdNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("title", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("Name", SQLDataType.VARCHAR, "150", null, null, null);
        configDataStructure.setColumnValue("Age", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("AgeUnit", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("sex", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("roomNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("registrationdate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("closed", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("closedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("insertTime", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("sourceHLM", SQLDataType.VARCHAR, "75", null, null, null);
        configDataStructure.setColumnValue("invoiceAccount", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("wareHouseCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("firstName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("middle", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("lastName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("city", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("mobileNo", SQLDataType.VARCHAR, "10", null, null, null);
    }

    protected void tblPatientLine(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("id", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("transactionID", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("srNo", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("testCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("testName", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("labNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("subSpeciality", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("closed", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("closedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("insertTime", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("sourceHLM", SQLDataType.VARCHAR, "75", null, null, null);
        configDataStructure.setColumnValue("invoiceAccount", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("wareHouseCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("city", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("mobileNo", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("diagSampleId", SQLDataType.INT, null, null, null, null);

        configDataStructure.setIndex("labNo");
    }

    protected void hisLPLInterfaceDetailLineStatus(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("id", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("transactionId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("testCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("lplLabNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("status", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("statusDateTime", SQLDataType.DATETIME, null, null, null, null);
    }

    //========================MRID/TreatmentID=====================
    protected void patientMRID(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Time", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("uhid", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("isStatus", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");//1=close 0= open

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
        configDataStructure.setForeignKey("regDocId", "regDocId", "patientRegistration");
        configDataStructure.setForeignKey("uhid", "docId", "patientUHID");

        configDataStructure.setIndex("regDocId");
        configDataStructure.setIndex("uhid");
    }

    protected void patientTreatment(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("nursingStationCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("roomCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("roomCode", "code", "roomMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
        configDataStructure.setForeignKey("regDocId", "regDocId", "patientRegistration");
        configDataStructure.setForeignKey("departmentCode", "subCode", "departmentMast");
        configDataStructure.setForeignKey("nursingStationCode", "code", "nursingStationMast");
    }

    protected void patientMRDAcknowledge(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setColumnValue("isFileSend", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("sendDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("sendTime", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("acknowledgeDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("acknowledgeBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("acknowledgeByTime", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "1000", null, null, null);
        configDataStructure.setColumnValue("AcknowledgeTime", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setForeignKey("regDocId", "regDocId", "patientRegistration");

    }

    protected void medicalRecordIssueRegister(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recid", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Time", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("fileRegDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "1000", null, null, null);
        configDataStructure.setColumnValue("applicantName", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("typeOfDocs", SQLDataType.VARCHAR, "150", null, null, null);
        configDataStructure.setColumnValue("dateOfRequest", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("timeOfRequest", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("reason", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("applicantMobile", SQLDataType.VARCHAR, "35", null, null, null);

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("fileRegDocId", "regDocId", "patientRegistration");
    }

    protected void saleBillReturn(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Time", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("itemNature", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("refNo", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("refDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("billFrom", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("billUpTo", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("consignee", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("placeOfSupply", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("partyType", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("lmCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("uhid", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("consultantCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("serviceCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("gstReasonCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("estimateDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("idTypeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("idNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("mobile", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("panNo", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("holderName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("relation", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Discount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Discount2", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Taxable", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_SGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_UGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_CGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_IGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_BAMT", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_RoundOff", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_LandedValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("CMN_Addition", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("CMN_Deduction", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("paymentSource", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("printCounter", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("u_EntDt_LatestLine", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("isSupplementary", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("balancingAc", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("returnDocId", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("uhid", "docId", "patientUHID");
        configDataStructure.setForeignKey("regDocId", "regDocId", "patientRegistration");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("consignee", "subCode", "subGroup");
        configDataStructure.setForeignKey("placeOfSupply", "code", "stateMast");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
        configDataStructure.setForeignKey("departmentCode", "subCode", "departmentMast");
        configDataStructure.setForeignKey("consultantCode", "subcode", "consultantMast");
        configDataStructure.setForeignKey("serviceCode", "code", "patientServiceMast");
        configDataStructure.setForeignKey("gstReasonCode", "code", "gstReasonMast");
        configDataStructure.setForeignKey("estimateDocId", "docId", "saleBill");
        configDataStructure.setForeignKey("idTypeCode", "code", "hl7Mast");
        configDataStructure.setForeignKey("lmCode", "code", "ledgerMast");
        configDataStructure.setForeignKey("returnDocId", "docID", "saleBill");

        configDataStructure.setIndex("regDocId");
        configDataStructure.setIndex("uhid");
        configDataStructure.setIndex("estimateDocId");
        configDataStructure.setIndex("lmCode");
        configDataStructure.setIndex("subCode");
        configDataStructure.setIndex("site_Code");
        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("departmentCode");
        configDataStructure.setIndex("balancingAc");
    }

    protected void saleBillReturnDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("hsnCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("itemGSTNature", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("saleUnitCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("ibCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("barcode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("batchNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manufacturingDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("expiryDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("skuRate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("mrp", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("skuMrp", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("quantity", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("freeQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("freeWithItemCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("skuQuantity", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("returnedQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("returnedFreeQty", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("skuQtyForRate", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("sbDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("sbV_Sno", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("requisitionDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("requisitionV_SNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Gross", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_at_Discount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Discount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_at_Discount2", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Discount2", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Taxable", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Ac_Sales", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("oh_At_SGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_SGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Ac_SGST", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("oh_At_UGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_UGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Ac_UGST", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("oh_At_CGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_CGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Ac_CGST", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("oh_At_IGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_IGST", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Ac_IGST", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Amt_Net", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("landedSkuValue", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isFooter", SQLDataType.TINYINT, null, null, null, "0");
        configDataStructure.setColumnValue("isReturn", SQLDataType.TINYINT, null, null, null, "0");
        configDataStructure.setColumnValue("discountReason", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("discountGivenBy", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("eodSbDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("oh_Ac_Item", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("isTaxInclusive", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setIndex("sbDocId");
        configDataStructure.setIndex("requisitionDocId");
        configDataStructure.setIndex("itemCode");
        configDataStructure.setIndex("freeWithItemCode");
        configDataStructure.setIndex("requisitionDocId,requisitionV_SNo");
        configDataStructure.setIndex("sbDocId,sbV_Sno");
        configDataStructure.setIndex("ibCode");
        configDataStructure.setIndex("saleUnitCode");
        configDataStructure.setIndex("hsnCode");

        configDataStructure.setForeignKey("docId", "docId", "saleBillReturn");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
        configDataStructure.setForeignKey("freeWithItemCode", "code", "itemMast");
        configDataStructure.setForeignKey("ibCode", "code", "itemBatch");
        configDataStructure.setForeignKey("requisitionDocId,requisitionV_SNo", "docId,v_SNo", "requisitionDetail");
        configDataStructure.setForeignKey("sbDocId,sbV_Sno", "docId,v_SNo", "saleBillDetail");
        configDataStructure.setForeignKey("oh_Ac_Sales", "subCode", "subGroup");
        configDataStructure.setForeignKey("oh_Ac_SGST", "subCode", "subGroup");
        configDataStructure.setForeignKey("oh_Ac_UGST", "subCode", "subGroup");
        configDataStructure.setForeignKey("oh_Ac_CGST", "subCode", "subGroup");
        configDataStructure.setForeignKey("oh_Ac_IGST", "subCode", "subGroup");
        configDataStructure.setForeignKey("discountGivenBy", "subcode", "consultantMast");
        configDataStructure.setForeignKey("hsnCode", "code", "hsnMast");
        configDataStructure.setForeignKey("saleUnitCode", "code", "unitMast");
        configDataStructure.setForeignKey("eodSbDocId", "docId", "saleBill");
        configDataStructure.setForeignKey("oh_Ac_Item", "subCode", "subGroup");
    }

    protected void itemModelMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    protected void itemStrengthMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
    }

    //===========================================TPA New Structure Start===================================
    public void ipdSecuritycardLedger(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("amtDr", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("amtCr", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("narration", SQLDataType.VARCHAR, "250", null, null, null);
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
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("cardRegDocId", "docId", "cardRegistration");
        configDataStructure.setForeignKey("cardTypeCode", "code", "cardTypeMast");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("currencyCode", "code", "currency");

        configDataStructure.setIndex("cardRegDocId");
    }

    public void ipdSecurityReceipt(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");

        configDataStructure.setColumnValue("eodDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("favourOf", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("tpaAllPaymentRefundDocid", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("v_Type", "v_Type", "voucher_type");
        configDataStructure.setForeignKey("regDocId", "regDocId", "patientRegistration");

        configDataStructure.setIndex("regDocId");
    }

    //===========================================TPA New Structure End=====================================
    //==========================================Barcode Generation System=================================
    public void barCodeGeneration(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Time", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);

    }

    public void barCodeGenerationDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("barcode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("barcodeDocid", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Time", SQLDataType.FLOAT, null, null, null, null);

    }

    //Payroll
    public void leaveMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("typeCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("natureCode", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("isActive", SQLDataType.TINYINT, null, null, null, null);

    }

    public void attendenceCodeDefinitionMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("days", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("byDefault", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("workingHours", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("site_code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);

    }

    protected void PY_AttendanceEntry(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("vSno", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("salaryMethod", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("departmentName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("employeeName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("day_1", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_2", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_3", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_4", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_5", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_6", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_7", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_8", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_9", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_10", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_11", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_12", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_13", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_14", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_15", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_16", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_17", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_18", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_19", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_20", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_21", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_22", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_23", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_24", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_25", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_26", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_27", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_28", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_29", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_30", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("day_31", SQLDataType.VARCHAR, "100", null, null, null);

    }

    //EMPLOYEE MASTER(Payroll)
    public void employeeLeave_Detail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("EmployeeCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("LeaveCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("leaves", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("LeaveCode", "code", "leaveMast");

    }

    public void employeeEarningDed_Detail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("EmployeeCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("SrNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("EarningCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("PFCode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("ESICode", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("PFPerc", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("Amount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("siteCode", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("EPType", SQLDataType.SMALLINT, null, null, null, null);

        configDataStructure.setForeignKey("EarningCode", "code", "earningDeductionMast");
    }

    public void employeeFamily_Detail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("employeeCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("srNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("memberName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("dob", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("relation", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("age", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("siteCode", SQLDataType.SMALLINT, null, null, null, null);
    }

    public void employeeSoftId_Detail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("employeeCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("srNo", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("softId", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("siteCode", SQLDataType.SMALLINT, null, null, null, null);
    }

    //TDS
    public void supplierTdsStatus(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("FromDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("ToDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("ITRStatus", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("vSNO", SQLDataType.TINYINT, null, null, null, null);

        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
    }

    //Procurement 
    public void procurementPlanner(String tableName) throws Exception {

        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docID", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("recID", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("indentDocID", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("indentDocID", "docID", "indent");
    }

    //TPA 
    public void tpaAllPaymentRefundDetail(String tableName) throws Exception {

        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docid", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Time", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("medPRFTotalAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("ipdSecurityTotalAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("medPRFAdjusted", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("ipdSecurityAdjusted", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("medPRFPrevRefund", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("ipdSecurityPrevRefund", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("medPRFCurrentRefund", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("ipdSecurityCurrentRefund", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("instrumentNo", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("instrumentDetail", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("instrumentDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("instrumentFavourOf", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("totalRefundAmount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("isCancelled", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, "0");
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("cardTypeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("cardTypeAccount", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setColumnValue("medPRFBalanceAmt", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("ipdSecurityBalanceAmt", SQLDataType.FLOAT, null, null, null, null);

        configDataStructure.setForeignKey("regDocId", "regDocId", "patientRegistration");
    }

    //hrEmployeeSalary 
    public void hrEmployeeSalary(String tableName) throws Exception {

        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);
        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("vSno", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("Employee", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("Father", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("BasicPay", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("CalcBasic", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("GrossSalary", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("NetSalary", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("TotalDays", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("OffDays", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("Holidays", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("WeekDays", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("salaryMethodCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("hrDepartmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("TotalEarn", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("TotalDed", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("leave", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("UEntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("Uname", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("EmployeeId", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("SiteCode", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("TotWDays", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("EPFPerc", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("EPFAmt", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("FPFPerc", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("FPFAmt", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("AdminAc21Perc", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("AdminAc21Amt", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("AdminAc22Perc", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("AdminAc22Amt", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("AdminAc2Perc", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("AdminAc2Amt", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("CompCode", SQLDataType.VARCHAR, "2", null, null, null);
        configDataStructure.setColumnValue("UAE", SQLDataType.VARCHAR, "1", null, null, null);
        configDataStructure.setColumnValue("CL", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("EL", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("PP", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("AA", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("HD", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("HRA", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("CA", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("SA", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("MA", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("MCA", SQLDataType.DECIMAL, null, null, null, null);
        configDataStructure.setColumnValue("CITY", SQLDataType.DECIMAL, null, null, null, null);

    }

    //Pharmacy 
    public void priceListMast(String tableName) throws Exception {

        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("priceListType", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("applicableOn", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        //Set Unique
        configDataStructure.setIndex("name");
    }

    public void priceList(String tableName) throws Exception {

        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("entryFrom", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("entryFromNature", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("site_code", SQLDataType.SMALLINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("priceListCode", SQLDataType.INT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("itemCode", SQLDataType.BIGINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("unitCode", SQLDataType.INT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("applicableFrom", SQLDataType.DATE, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("applicableTo", SQLDataType.DATE, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("nature", SQLDataType.TINYINT, null, null, SQLIsNullable.NOT_NULL, null);
        configDataStructure.setColumnValue("discount", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("discountPer", SQLDataType.DECIMAL, "18,7", null, null, null);
        configDataStructure.setColumnValue("rate", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("printSno", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        //Set Index
        configDataStructure.setIndex("site_Code,priceListCode,subCode,itemCode,unitCode,applicableFrom,applicableTo");

        //Set Foreign Key                    
        configDataStructure.setForeignKey("priceListCode", "code", "priceListMast");
        configDataStructure.setForeignKey("subCode", "subCode", "subGroup");
        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
        configDataStructure.setForeignKey("itemCode", "code", "itemMast");
        configDataStructure.setForeignKey("unitCode", "code", "unitMast");
    }

    //Consultation
    public void patientDisabilityDetail(String tableName) throws Exception {

        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("disabilityCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "9", null, null, null);
        configDataStructure.setColumnValue("disabilityPercent", SQLDataType.VARCHAR, "20", null, null, null);

        configDataStructure.setForeignKey("regDocId", "regDocId", "patientRegistration");
    }

    //disabilityMast
    protected void disabilityMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "200", null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "9", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "90", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);

    }

    //Asset
    protected void assetLocationMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("code", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("name", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

    }

    //Budget=======
    public void budgetHeadMast(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("headCode", SQLDataType.INT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("manualCode", SQLDataType.VARCHAR, "30", null, null, null);
        configDataStructure.setColumnValue("headName", SQLDataType.VARCHAR, "150", null, null, null);
        configDataStructure.setColumnValue("headUnder", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);

        configDataStructure.setForeignKey("headUnder", "headCode", "budgetHeadMast");
    }

    public void budgetHeadPath(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("headCode", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("sNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("headUnder", SQLDataType.BIGINT, null, null, null, null);

        configDataStructure.setForeignKey("headCode", "headCode", "budgetHeadMast");
        configDataStructure.setForeignKey("headUnder", "headCode", "budgetHeadMast");
    }

    public void budgetAllocation(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("PreparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("approvalDateTime", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("budgetDescription", SQLDataType.VARCHAR, "250", null, null, null);

        configDataStructure.setIndex("v_Type");
        configDataStructure.setIndex("v_Date");
        configDataStructure.setIndex("site_Code");

        configDataStructure.setForeignKey("site_Code", "code", "siteMast");
    }

    public void budgetAllocationDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, SQLColumnType.PRIMARY, null, null);
        configDataStructure.setColumnValue("costCenter", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("plusMinus", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("fromDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("toDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("headCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("budget", SQLDataType.DECIMAL, "18,4", null, null, null);

        configDataStructure.setIndex("costCenter");
        configDataStructure.setIndex("headCode");

        configDataStructure.setForeignKey("costCenter", "code", "costCenterMast");
        configDataStructure.setForeignKey("headCode", "headCode", "budgetHeadMast");
    }

    //End Budget
    protected void projectIndicator(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedDate", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("approvalDateTime", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("wtaCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt_LatestLine", SQLDataType.DATETIME, null, null, null, null);

    }
//    Blood Bank

    protected void bloodComponentIssue(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Type", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("v_No", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("recIdPrefix", SQLDataType.VARCHAR, "16", null, null, null);
        configDataStructure.setColumnValue("recId", SQLDataType.VARCHAR, "20", null, null, null);
        configDataStructure.setColumnValue("v_Prefix", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("v_Date", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("v_Time", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("subCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("departmentCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("requestedByCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("departmentToCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("priorityTypeCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("regDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "250", null, null, null);
        configDataStructure.setColumnValue("u_EntDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_Name", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("preparedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("site_Code", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("site_Code_To", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("transferDt", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("u_EntDt_LatestLine", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("approvedBy", SQLDataType.VARCHAR, "10", null, null, null);
        configDataStructure.setColumnValue("approvalDateTime", SQLDataType.DATETIME, null, null, null, null);
        configDataStructure.setColumnValue("wtaCode", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("requisitionDocId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("status", SQLDataType.TINYINT, null, null, null, null);
    }

    protected void bloodComponentIssueDetail(String tableName) throws Exception {
        DmConfigDataStructure configDataStructure = new DmConfigDataStructure(this.getClass().getName(), tableName, refreshDataStructure);

        configDataStructure.setColumnValue("docId", SQLDataType.BIGINT, null, null, null, null);
        configDataStructure.setColumnValue("v_SNo", SQLDataType.SMALLINT, null, null, null, null);
        configDataStructure.setColumnValue("bloodComponentCode", SQLDataType.INT, null, null, null, null);
        configDataStructure.setColumnValue("reqQty", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("issueQty", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("reqDate", SQLDataType.DATE, null, null, null, null);
        configDataStructure.setColumnValue("reqTime", SQLDataType.FLOAT, null, null, null, null);
        configDataStructure.setColumnValue("remark", SQLDataType.VARCHAR, "100", null, null, null);
        configDataStructure.setColumnValue("isStatus", SQLDataType.TINYINT, null, null, null, null);
        configDataStructure.setColumnValue("donnerName", SQLDataType.VARCHAR, "50", null, null, null);
        configDataStructure.setColumnValue("barCode", SQLDataType.VARCHAR, "50", null, null, null);

        configDataStructure.setForeignKey("docId", "docId", "bloodComponentIssue");

    }

}
