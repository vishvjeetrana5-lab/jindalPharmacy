package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TrialDetailDrCrExcel_Model {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }

        if (DmShared.xNull(hashMapFilter.get("zeroBalance").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Zero Balance is mandatory");
        }

        if (DmShared.xNull(hashMapFilter.get("reportFor").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Report For is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("includeOpening").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Include Opening is mandatory");
        }
    }

    public static class TxtHAccountName extends KeyField implements Serializable, IsSerializable {

        private String City;
        private String GroupName;
        private String ManualCode;

        public TxtHAccountName() {
        }

        public String getManualCode() {
            return ManualCode;
        }

        public void setManualCode(String ManualCode) {
            this.ManualCode = ManualCode;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }
    }
}
