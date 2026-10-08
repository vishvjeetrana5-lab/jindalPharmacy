package dataman.dmToolkitR.dmGridHelp.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.KeyField;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

public class DmGridHelp<T extends KeyField> extends DmGrid_Dsg {

    public interface RowConfigCallBack<T extends KeyField> {

        public void rowConfig(DmGridRow<T> row, T tRow);
    }

    public interface ColConfigCallBack<T extends KeyField> {

        public void colConfig(DmGridCell<T> cell);
    }

    public interface GridCallBack<T extends KeyField> {

        public void getSelectedRow(T tRow);
    }

    private GridCallBack<T> gridCallBack;
    private RowConfigCallBack<T> rowConfig;
    private ColConfigCallBack<T> colConfig;
    private LinkedHashMap<String, DmGridCell<T>> columns = new LinkedHashMap<String, DmGridCell<T>>();
    private LinkedHashMap<Integer, DmGridRow<T>> rows = new LinkedHashMap<Integer, DmGridRow<T>>();

    public DmGridHelp() {
        super();
    }

    public void addHelpCallBack(GridCallBack<T> helpCallBack) {
        this.gridCallBack = helpCallBack;
    }

    public void addRowConfig(RowConfigCallBack<T> rowConfig) {
        this.rowConfig = rowConfig;
    }

    public void addColConfig(ColConfigCallBack<T> colConfig) {
        this.colConfig = colConfig;
    }

    public DmGridCell<T> addColumn(String dbField, String caption, String width) {
        DmGridCell<T> cell = new DmGridCell<T>();

        cell.setDBField(dbField.trim().toLowerCase());
        cell.setCaption(caption);
//        cell.setWidth(width + "px");
//        cell.setWidth(width + "%");
        cell.setWidth(width);
        columns.put(dbField, cell);

        return cell;
    }

    public int rowCount() {
        return rows.keySet().size();
    }

    public void clear() {
        flowPanel.clear();
        rows.clear();
    }

    public void setAllData(List<T> list) {

        flowPanel.clear();
        rows.clear();

        if (columns != null && list != null) {
            for (T t : list) {
                setData(t);
            }
        }
    }

    public void setData(T t) {
        Set<String> set;
        DmGridCell<T> cell;
        DmGridRow<T> row;

        set = columns.keySet();
        row = new DmGridRow<T>(t);
        for (String s : set) {
            cell = columns.get(s);
            cell = addCell(cell);
            row.add(cell);
        }

        row.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (event.getSource() instanceof DmGridRow) {
                    if (gridCallBack != null) {
                        gridCallBack.getSelectedRow(((DmGridRow<T>) event.getSource()).getRow());
                    }
                }
            }
        }, ClickEvent.getType());

        flowPanel.add(row);
        rows.put(rows.size(), row);

        if (rowConfig != null) {
            rowConfig.rowConfig(row, t);
        }

    }

    private DmGridCell<T> addCell(DmGridCell<T> sampleCell) {
        DmGridCell<T> cell = new DmGridCell<T>();

        cell.setDBField(sampleCell.getDBField());
        cell.setCaption(sampleCell.getCaption());
        cell.setWidth(sampleCell.getWidth());

        return cell;
    }

    @Override
    public Widget asWidget() {
        return flowPanel;
    }

}
