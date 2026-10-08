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

public class Ledger_Model {

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("fromDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "From Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("toDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "To Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("indexneeded").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Index Needed is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("contraAccount").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Contra A/c Needed is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("reportFor").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Report For is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("includeOpening").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Include Opening is mandatory");
        }
        if (DmShared.xNull(hashMapFilter.get("havingBalance").getTag()).equalsIgnoreCase("")) {
            throw new UnsupportedOperationException("Having Balance is mandatory");
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

    public static class TxtHVoucherType extends KeyField implements Serializable, IsSerializable {

        private String Type;

        public TxtHVoucherType() {
        }

        public String getType() {
            return Type;
        }

        public void setType(String Type) {
            this.Type = Type;
        }
    }

    public static class TxtHHavingAccountName extends KeyField implements Serializable, IsSerializable {

        private String City;
        private String GroupName;
        private String ManualCode;

        public TxtHHavingAccountName() {
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
