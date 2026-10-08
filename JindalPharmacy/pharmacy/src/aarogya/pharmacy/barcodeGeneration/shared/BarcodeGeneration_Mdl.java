package aarogya.pharmacy.barcodeGeneration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BarcodeGeneration_Mdl implements Serializable, IsSerializable {

    private String voucherPrefix;

    List<GrdBarcodeGeneration_Mdl> listBr = new ArrayList<>();

    public List<GrdBarcodeGeneration_Mdl> getListBr() {
        return listBr;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public void setListBr(List<GrdBarcodeGeneration_Mdl> listBr) {
        this.listBr = listBr;
    }

}
