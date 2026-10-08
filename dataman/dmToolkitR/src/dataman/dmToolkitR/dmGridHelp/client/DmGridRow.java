package dataman.dmToolkitR.dmGridHelp.client;

import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.KeyField;
import java.util.LinkedHashMap;
import java.util.Set;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

public class DmGridRow<T extends KeyField> extends FlowPanel {

    private T tRow;
    private LinkedHashMap<String, DmGridCell<T>> cells = new LinkedHashMap<String, DmGridCell<T>>();

    public DmGridRow(T tRow) {
        super();
        this.tRow = tRow;
        this.setStyleName("dmRow");
    }

    public T getRow() {
        return tRow;
    }

    @Override
    public void add(Widget cell) {
        super.add(cell);

        DmGridCell<T> c = ((DmGridCell<T>) cell);
        c.setRow(this);
        cells.put(c.getDBField(), c);
    }

    public DmGridCell<T> getCell(String dbField) {
        return cells.get(dbField.trim().toLowerCase());
    }

    public Set<String> getCellNames() {
        return cells.keySet();
    }
}
