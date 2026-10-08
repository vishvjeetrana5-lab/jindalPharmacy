package dataman.utility.voucherType.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import java.io.Serializable;

public class VoucherType_Vld extends KeyField implements Serializable, IsSerializable {

    public static void chkSave(VoucherType_Mdl mrTemp) {
        DmShared.isValidText(mrTemp.getSharedKeyFieldName(), "Voucher Type", 100);
        DmShared.isValidText(mrTemp.getShortName(), "Short Name", 200);
        DmShared.isValidText(mrTemp.getDescription(), "Description", 300);
        DmShared.isValidText(mrTemp.getModuleCode(), "Module Name", 400);
        DmShared.isValidText(mrTemp.getCategoryCode(), "Category", 500);
        DmShared.isValidText(mrTemp.getDefaultReportCode(), "Default Report", 600);
        DmShared.isValidText(mrTemp.getLandscapeCode(), "Landscape", 700);
        DmShared.isValidText(mrTemp.getActiveCode(), "Active", 900);
        DmShared.isValidText(mrTemp.getIsBankCashBookCode(), "Bank Cash Book", 1000);
    }
}
