package dataman.dmToolkit.dmGrid.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.self.shared.DmEnum;

public class DmColumnConfig<M, N> extends ColumnConfig<M, N> {

    private String dbFieldName;
    private DmEnum.DmDataType dbFieldDataType = DmEnum.DmDataType.STRING;

    public DmColumnConfig(ValueProvider<? super M, N> valueProvider) {
        super(valueProvider);
    }

    public DmColumnConfig(ValueProvider<? super M, N> valueProvider, int width) {
        super(valueProvider, width);
    }

    public DmColumnConfig(ValueProvider<? super M, N> valueProvider, int width, SafeHtml header) {
        super(valueProvider, width, header);
    }

    public DmColumnConfig(ValueProvider<? super M, N> valueProvider, int width, String header) {
        super(valueProvider, width, header);
    }

    public String getDbFieldName() {
        return dbFieldName;
    }

    public void setDbFieldName(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    public DmEnum.DmDataType getDbFieldDataType() {
        return dbFieldDataType;
    }

    public void setDbFieldDataType(DmEnum.DmDataType dbFieldDataType) {
        this.dbFieldDataType = dbFieldDataType;
    }
}
