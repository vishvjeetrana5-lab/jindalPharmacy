package dataman.dmBase.self.shared;

import java.util.List;

public abstract class VoucherPermissionHandler {

    private List<VoucherPermission> voucherPermission;

    public abstract String getVoucherType();

    public abstract String getVoucherDate();

    public abstract String getVoucherValue();

    public VoucherPermissionHandler(List<VoucherPermission> voucherPermission) {
        this.voucherPermission = voucherPermission;
    }

    public List<VoucherPermission> getVoucherPermission() {
        return voucherPermission;
    }

}
