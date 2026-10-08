package aarogya.pharmacy.wholeSaleReports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.HashMap;

public class BillWiseOutstandingDebtors_Model {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("asOnDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "As On Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("billAfterOn").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Bill After On is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("reportOnChoice").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Report On Choice is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("balance").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Balance is mandatory");
        }

        if (DmShared.xNull(hashMapFilter.get("reportOn").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Report On is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("havingBalance").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Having Balance is mandatory");
        }

        if (DmShared.xNull(hashMapFilter.get("showAdjustmentOf").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Show Adjustment Of is mandatory");
        }
    }

    public static class TxtHAccountName extends KeyField implements Serializable, IsSerializable {

        private String GroupName;
        private String ManualCode;
        private String city;

        public TxtHAccountName() {
        }

        public String getManualCode() {
            return ManualCode;
        }

        public void setManualCode(String ManualCode) {
            this.ManualCode = ManualCode;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
