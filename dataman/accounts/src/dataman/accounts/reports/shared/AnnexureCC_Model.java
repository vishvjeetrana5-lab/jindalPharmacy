package dataman.accounts.reports.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.HashMap;

public class AnnexureCC_Model {

    public static class TxtHAccountGroup extends KeyField implements Serializable, IsSerializable {

        private String mainGroup;

        public TxtHAccountGroup() {
        }

        public String getMainGroup() {
            return mainGroup;
        }

        public void setMainGroup(String mainGroup) {
            this.mainGroup = mainGroup;
        }

    }

    public static void validateInput(HashMap<String, FieldUDT_Mdl> hashMapFilter) {
        if (DmShared.xNull(hashMapFilter.get("upToDate").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Up To Date is mandatory.");
        }
        if (DmShared.xNull(hashMapFilter.get("reportType").getText()).equalsIgnoreCase("")) {
            throw new DmException(100, "Report Type is mandatory.");
        }
    }
}
