package aarogya.pharmacy.duplicateBarcodeGeneration.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DuplicateBarcodeGeneration_Mdl implements Serializable, IsSerializable {

    private String voucherPrefix;

    List<GrdDuplicateBarcodeGeneration_Mdl> listBr = new ArrayList<>();

    public List<GrdDuplicateBarcodeGeneration_Mdl> getListBr() {
        return listBr;
    }

    public String getVoucherPrefix() {
        return voucherPrefix;
    }

    public void setVoucherPrefix(String voucherPrefix) {
        this.voucherPrefix = voucherPrefix;
    }

    public void setListBr(List<GrdDuplicateBarcodeGeneration_Mdl> listBr) {
        this.listBr = listBr;
    }

}
