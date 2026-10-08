package aarogya.pharmacy.purchaseBillDirect.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdImpurity_Mdl extends KeyField implements Serializable, IsSerializable {

    /*
    sharedKeyFieldCode = impurityCode
    sharedKeyFieldName = impurity
     */
    private String foundPer;
    private String acceptPer;
    private String netPer;
    private String netQty;
    private String supplierQty;

    public GrdImpurity_Mdl() {
    }

    public String getFoundPer() {
        return foundPer;
    }

    public void setFoundPer(String foundPer) {
        this.foundPer = foundPer;
    }

    public String getAcceptPer() {
        return acceptPer;
    }

    public void setAcceptPer(String acceptPer) {
        this.acceptPer = acceptPer;
    }

    public String getNetPer() {
        return netPer;
    }

    public void setNetPer(String netPer) {
        this.netPer = netPer;
    }

    public String getNetQty() {
        return netQty;
    }

    public void setNetQty(String netQty) {
        this.netQty = netQty;
    }

    public String getSupplierQty() {
        return supplierQty;
    }

    public void setSupplierQty(String supplierQty) {
        this.supplierQty = supplierQty;
    }

    public GrdImpurity_Mdl clone() {
        GrdImpurity_Mdl grn_Mdl = new GrdImpurity_Mdl();

        copy(grn_Mdl);

        return grn_Mdl;
    }

    private void copy(GrdImpurity_Mdl grn_Mdl) {
        grn_Mdl.setSharedKeyFieldCode(grn_Mdl.getSharedKeyFieldCode());
        grn_Mdl.setSharedKeyFieldName(grn_Mdl.getSharedKeyFieldName());
        grn_Mdl.setFoundPer(grn_Mdl.getFoundPer());
        grn_Mdl.setAcceptPer(grn_Mdl.getAcceptPer());
        grn_Mdl.setNetPer(grn_Mdl.getNetPer());
        grn_Mdl.setNetQty(grn_Mdl.getNetQty());
        grn_Mdl.setSupplierQty(grn_Mdl.getSupplierQty());
    }

}
