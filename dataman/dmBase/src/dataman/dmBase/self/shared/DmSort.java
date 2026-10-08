package dataman.dmBase.self.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import dataman.dmBase.self.shared.DmEnum;
import java.io.Serializable;

public class DmSort implements Serializable,IsSerializable {

    private String columnCaption;
    private String columnName;
    private DmEnum.DmSortType sortType = DmEnum.DmSortType.ASC;

    public DmSort() {
    }

    public String getColumnCaption() {
        return columnCaption;
    }

    public void setColumnCaption(String columnCaption) {
        this.columnCaption = columnCaption;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public DmEnum.DmSortType getSortType() {
        return sortType;
    }

    public void setSortType(DmEnum.DmSortType sortType) {
        this.sortType = sortType;
    }
}
