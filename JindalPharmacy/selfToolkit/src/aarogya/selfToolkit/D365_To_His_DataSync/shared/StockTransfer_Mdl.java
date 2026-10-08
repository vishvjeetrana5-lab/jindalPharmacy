package aarogya.selfToolkit.D365_To_His_DataSync.shared;

import java.util.ArrayList;
import java.util.List;

public class StockTransfer_Mdl {

    private String docId;
    private String entryType;         // vt.parentVoucher
    private String entryNumber;       // CONCAT(sh.docId, sm.BusinessUnit)
    private String entryDate;           // sh.v_Date
    private String documentType;      // vt.description
    private String documentNumber;    // sh.recId
    private String businessUnit;      // sm.businessUnit
    private String legalEntity;       // sm.LegalEntity

    // ---- Line items ----
    private List<StockTransferLine_Mdl> lineDetails = new ArrayList<>();                  // unt.unit

    public StockTransfer_Mdl() {
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(String entryNumber) {
        this.entryNumber = entryNumber;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public String getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(String legalEntity) {
        this.legalEntity = legalEntity;
    }

    public List<StockTransferLine_Mdl> getLineDetails() {
        return lineDetails;
    }

    public void setLineDetails(List<StockTransferLine_Mdl> lineDetails) {
        this.lineDetails = lineDetails;
    }

}
