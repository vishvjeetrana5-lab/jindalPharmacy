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

public class GTAReg_Model {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("includeOpening").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Include Opening is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("reportOnChoice").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Report On Choice is mandatory");
        }
    }

    public static class TxtHPartyName extends KeyField implements Serializable, IsSerializable {

        private String ManualCode;

        public TxtHPartyName() {
        }

        public String getManualCode() {
            return ManualCode;
        }

        public void setManualCode(String ManualCode) {
            this.ManualCode = ManualCode;
        }
    }

    public static class TxtHConsignor extends KeyField implements Serializable, IsSerializable {

        private String ManualCode;

        public TxtHConsignor() {
        }

        public String getManualCode() {
            return ManualCode;
        }

        public void setManualCode(String ManualCode) {
            this.ManualCode = ManualCode;
        }
    }

    public static class TxtHConsignee extends KeyField implements Serializable, IsSerializable {

        private String ManualCode;

        public TxtHConsignee() {
        }

        public String getManualCode() {
            return ManualCode;
        }

        public void setManualCode(String ManualCode) {
            this.ManualCode = ManualCode;
        }
    }

    public static class TxtHDrAccount extends KeyField implements Serializable, IsSerializable {

        private String ManualCode;

        public TxtHDrAccount() {
        }

        public String getManualCode() {
            return ManualCode;
        }

        public void setManualCode(String ManualCode) {
            this.ManualCode = ManualCode;
        }
    }

    public static class TxtHCrAccount extends KeyField implements Serializable, IsSerializable {

        private String ManualCode;

        public TxtHCrAccount() {
        }

        public String getManualCode() {
            return ManualCode;
        }

        public void setManualCode(String ManualCode) {
            this.ManualCode = ManualCode;
        }
    }

}
