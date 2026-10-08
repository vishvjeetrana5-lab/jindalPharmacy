package dataman.payroll.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.HashMap;

public class AccountGroupMergeLedger_Model {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("contraAccount").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Contra A/C Needed is mandatory.");
        }
    }

    public static class TxtHVoucherType extends KeyField implements Serializable, IsSerializable {

        public TxtHVoucherType() {
        }

        private String Type;

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }
    }

    public static class TxtHAccountGroup extends KeyField implements Serializable, IsSerializable {

        private String GroupUnder;

        public TxtHAccountGroup() {
        }

        public String getGroupUnder() {
            return GroupUnder;
        }

        public void setGroupUnder(String GroupUnder) {
            this.GroupUnder = GroupUnder;
        }
    }
}
