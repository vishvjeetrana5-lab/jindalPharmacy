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

public class OutstandingCreditorsFIFO_Model {

    public static class TxtHAccountName extends KeyField implements Serializable, IsSerializable {

        private String manualCode;
        private String city;
        private String groupName;
        private String Zone;

        public TxtHAccountName() {
        }

        public String getZone() {
            return Zone;
        }

        public void setZone(String Zone) {
            this.Zone = Zone;
        }

        public String getManualCode() {
            return manualCode;
        }

        public void setManualCode(String manualCode) {
            this.manualCode = manualCode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

    }

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("asOnDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "As On Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("interval").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Interval is mandatory.");
        }
    }
}
