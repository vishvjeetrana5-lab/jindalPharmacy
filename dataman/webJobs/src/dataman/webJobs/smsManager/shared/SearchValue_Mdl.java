package dataman.webJobs.smsManager.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.webJobs.emailManager.shared.FilterDataPopup_Mdl;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchValue_Mdl implements Serializable,IsSerializable{

    private String funFile;
    private String tempID;
    private String message;
    private String tableName;
    private List<FilterDataPopup_Mdl> filterDataList = new ArrayList<>();

    public SearchValue_Mdl() {
    }

    public String getFunFile() {
        return funFile;
    }

    public void setFunFile(String funFile) {
        this.funFile = funFile;
    }

    public String getTempID() {
        return tempID;
    }

    public void setTempID(String tempID) {
        this.tempID = tempID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<FilterDataPopup_Mdl> getFilterDataList() {
        return filterDataList;
    }
}
