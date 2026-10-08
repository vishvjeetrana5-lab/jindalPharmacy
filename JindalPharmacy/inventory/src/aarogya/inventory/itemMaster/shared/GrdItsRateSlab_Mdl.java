package aarogya.inventory.itemMaster.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class GrdItsRateSlab_Mdl extends KeyField implements Serializable, IsSerializable {

    private String rateFrom;
    private String rateUpto;
    private String gstTypeCode;
    private String gstTypeName;

    public GrdItsRateSlab_Mdl() {
    }

    public String getGstTypeName() {
        return gstTypeName;
    }

    public void setGstTypeName(String gstTypeName) {
        this.gstTypeName = gstTypeName;
    }

    public String getGstTypeCode() {
        return gstTypeCode;
    }

    public void setGstTypeCode(String gstTypeCode) {
        this.gstTypeCode = gstTypeCode;
    }

    public String getRateFrom() {
        return rateFrom;
    }

    public void setRateFrom(String rateFrom) {
        this.rateFrom = rateFrom;
    }

    public String getRateUpto() {
        return rateUpto;
    }

    public void setRateUpto(String rateUpto) {
        this.rateUpto = rateUpto;
    }

}
