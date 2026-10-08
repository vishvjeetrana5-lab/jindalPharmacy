package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class VoucherPermission implements Serializable,IsSerializable{

    private String saveDate, viewDate, deleteDate;
    private double allowValueUpto;
    private String voucherType;
    private String category;

    public VoucherPermission() {

    }

    public VoucherPermission(String category, String voucherType, String viewDate, String deleteDate, String saveDate, double allowValueUpto) {
        this.category = category;
        this.voucherType = voucherType;
        this.saveDate = saveDate;
        this.viewDate = viewDate;
        this.deleteDate = deleteDate;
        this.allowValueUpto = allowValueUpto;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public String getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(String deleteDate) {
        this.deleteDate = deleteDate;
    }

    public double getAllowValueUpto() {
        return allowValueUpto;
    }

    public void setAllowValueUpto(double allowValueUpto) {
        this.allowValueUpto = allowValueUpto;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
