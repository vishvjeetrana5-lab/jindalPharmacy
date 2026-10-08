package dataman.webJobs.emailManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import java.io.*;
import java.util.*;

public class DynamicGrid_Mdl implements Serializable, IsSerializable {

    private List<DmDynamicGridRow> dynamicGridRowList = new ArrayList<DmDynamicGridRow>();
    private List<String> columnNameList = new ArrayList<String>();
    private List<FilterDataPopup_Mdl> filterData_Mdls = new ArrayList<>();

    public DynamicGrid_Mdl() {
    }

    public List<DmDynamicGridRow> getDynamicGridRowList() {
        return dynamicGridRowList;
    }

    public List<String> getColumnNameList() {
        return columnNameList;
    }

    public List<FilterDataPopup_Mdl> getFilterData_Mdls() {
        return filterData_Mdls;
    }

}
