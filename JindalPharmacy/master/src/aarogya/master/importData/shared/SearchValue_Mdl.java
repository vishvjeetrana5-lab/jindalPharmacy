package aarogya.master.importData.shared;

import dataman.dmToolkit.dmImportData.shared.ImportData_Mdl;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchValue_Mdl implements Serializable, IsSerializable {

    private String fieldName;
    private List<ImportData_Mdl> lstImport = new ArrayList<>();
    private boolean searching = false;

    public SearchValue_Mdl() {
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public List<ImportData_Mdl> getLstImport() {
        return lstImport;
    }

    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }
}
