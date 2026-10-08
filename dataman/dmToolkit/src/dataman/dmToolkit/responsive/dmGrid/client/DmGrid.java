package dataman.dmToolkit.responsive.dmGrid.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.List;
import org.gwtbootstrap3.client.ui.gwt.CellTable;

public class DmGrid<T> {

    private CellTable<T> dataGrid = new CellTable<T>();
    private ListDataProvider<T> dataProvider = new ListDataProvider<T>();
    final SingleSelectionModel<T> selectionModel = new SingleSelectionModel<T>();
    private List<T> dataList;

    public CellTable<T> getGrid() {
        return dataGrid;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        //dataGrid.setRowData(0, dataList);
        this.dataList = dataList;
        List<T> list = dataProvider.getList();
        list.addAll(this.dataList);
        if (!dataProvider.getDataDisplays().isEmpty()) {
            dataProvider.removeDataDisplay(dataGrid);
        }
        dataProvider.addDataDisplay(dataGrid);
        dataProvider.refresh();
        dataGrid.setSelectionModel(selectionModel);
    }

    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public SingleSelectionModel<T> getSelectionModel() {
        return selectionModel;
    }

    public void clear() {
        if (dataList != null) {
            dataProvider.getList().removeAll(dataList);
            dataProvider.refresh();
            this.dataList.removeAll(dataList);
        }
    }

    public void makeMoveAble() {
        dataGrid.addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                CellTable<T> selectedcell = (CellTable<T>) event.getSource();
                if (KeyCodes.KEY_UP == event.getNativeEvent().getKeyCode()) {
                    if (selectedcell.getKeyboardSelectedRow() != 0) {
                        selectCell(selectedcell.getKeyboardSelectedRow() - 1);
                    } else {
                        selectCell(selectedcell.getKeyboardSelectedRow());
                    }
                } else if (KeyCodes.KEY_DOWN == event.getNativeEvent().getKeyCode()) {
                    if ((getDataProvider().getList().size() - 1) > selectedcell.getKeyboardSelectedRow()) {
                        selectCell(selectedcell.getKeyboardSelectedRow() + 1);
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    public void selectCell(int rowIndex) {
        if (getDataProvider().getList() != null) {
            dataGrid.getSelectionModel().setSelected(getDataProvider().getList().get(rowIndex), true);
        }
    }
}
