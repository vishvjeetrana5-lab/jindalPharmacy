package dataman.dmBase.dmScheduler.server;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;

public class DmMessageParam implements Serializable, IsSerializable {

    private String columnName;
    private String formattedColName;

    public DmMessageParam() {
    }

    public DmMessageParam(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getFormattedColName() {
        return "{" + formattedColName + "}";
    }

}
