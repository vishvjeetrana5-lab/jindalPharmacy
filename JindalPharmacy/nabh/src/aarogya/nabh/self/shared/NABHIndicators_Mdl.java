package aarogya.nabh.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NABHIndicators_Mdl implements IsSerializable, Serializable {

    private String groupName;
    private List<Map<String, String>> rows;

    public NABHIndicators_Mdl() {
        rows = new ArrayList<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String name) {
        this.groupName = name;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }
}
