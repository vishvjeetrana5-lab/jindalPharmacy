package aarogya.pharmacy.procurementPlannerDisplay.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.KeyField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Mdl_Main extends KeyField implements Serializable, IsSerializable {

    private String voucherDate;
    private String indentDocID;
    private String indentRecID;
    private SearchValue_Mdl filterMdl = new SearchValue_Mdl();
    private List<ProcurementPlannerDisplay_Mdl> grdItemDetails = new ArrayList<ProcurementPlannerDisplay_Mdl>();

    public SearchValue_Mdl getFilterMdl() {
        return filterMdl;
    }

    public void setFilterMdl(SearchValue_Mdl filterMdl) {
        this.filterMdl = filterMdl;
    }

    public List<ProcurementPlannerDisplay_Mdl> getGrdItemDetails() {
        return grdItemDetails;
    }

    public void setGrdItemDetails(List<ProcurementPlannerDisplay_Mdl> grdItemDetails) {
        this.grdItemDetails = grdItemDetails;
    }

    public String getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(String voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getIndentDocID() {
        return indentDocID;
    }

    public void setIndentDocID(String indentDocID) {
        this.indentDocID = indentDocID;
    }

    public String getIndentRecID() {
        return indentRecID;
    }

    public void setIndentRecID(String indentRecID) {
        this.indentRecID = indentRecID;
    }
}
