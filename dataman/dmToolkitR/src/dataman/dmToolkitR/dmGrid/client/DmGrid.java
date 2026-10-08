package dataman.dmToolkitR.dmGrid.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkitR.dmAlertBox.client.DmAlertBox;
import dataman.dmToolkitR.dmStatic.shared.DmShared;
import static dataman.dmToolkitR.dmGrid.client.DmGridCell.Separator.CELL_FIELD_VALUE_SEPARATOR;
import static dataman.dmToolkitR.dmGrid.client.DmGridCell.Separator.CELL_SEPARATOR;
import dataman.dmToolkitR.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;

public class DmGrid<T> implements IsWidget, ClickHandler, KeyDownHandler, BlurHandler {
    
    private T t;
    private final List<T> data = new ArrayList<T>();
    private final FlowPanel flwContainer = new FlowPanel();
    private final FlexTable flxGrid = new FlexTable();
    private final FocusPanel fcsPnl = new FocusPanel(flxGrid);
    private int selectedDataRowIndex = -1;
    private int selectedRowIndex = -1;
    private int selectedCellIndex = -1;
    private int selectedHeaderCellIndex = -1;
    private boolean isRowNoColumnRequired = false;
    private final List<String> columnsToRemove = new ArrayList<String>();
//    private List<DmGridCell> rowConfig = new ArrayList<>();

    //<editor-fold defaultstate="collapsed" desc="RowClickHandler">
    public interface RowClickHandler {

        void onRowClick(int rowIndex);
    }

    RowClickHandler rowClickHandler;

    public void addRowClickHandler(RowClickHandler rowClickHandler) {
        this.rowClickHandler = rowClickHandler;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CellClickHandler">
    public interface CellClickHandler {

        void onCellClick(int cellIndex, int rowIndex);
    }

    CellClickHandler cellClickHandler;

    public void addCellClickHandler(CellClickHandler cellClickHandler) {
        this.cellClickHandler = cellClickHandler;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="HeaderClickHandler">
    public interface HeaderClickHandler {

        void onHeaderClick(String dbFieldName);
    }

    HeaderClickHandler headerClickHandler;

    public void addHeaderClickHandler(HeaderClickHandler headerClickHandler) {
        this.headerClickHandler = headerClickHandler;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CellBeforeRenderCallBack">
    public interface CellBeforeRenderCallBack {

        public String onCellBeforeRender(String css, int cellIndex, int dataRowIndex, String cellValue);
    }

    private CellBeforeRenderCallBack cellBeforeRenderCallBack;

    public void addCellBeforeRender(CellBeforeRenderCallBack cellBeforeRenderCallBack) {
        this.cellBeforeRenderCallBack = cellBeforeRenderCallBack;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="RowConfigCallBack">

    public interface RowConfigCallBack<T> {

        public String rowConfig(T tRow);
    }

    private RowConfigCallBack<T> rowConfigCallBack;

    public void addRowConfig(RowConfigCallBack<T> rowConfigCalBack) {
        this.rowConfigCallBack = rowConfigCalBack;
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CellValueBeforeRenderCallBack">

    public interface CellValueBeforeRenderCallBack {

        public String setCellValueBeforeRender(int cellIndex, int dataRowIndex, String cellValue);
    }

    private CellValueBeforeRenderCallBack cellValueBeforeRenderCallBack;

    public void addCellValue(CellValueBeforeRenderCallBack cellValueBeforeRenderCallBack) {
        this.cellValueBeforeRenderCallBack = cellValueBeforeRenderCallBack;
    }
//</editor-fold>

//    public List<DmGridCell> getRowConfig() {
//        return rowConfig;
//    }
//
//    public void setRowConfig(List<DmGridCell> rowConfig) {
//        this.rowConfig = rowConfig;
//    }
    public int getSelectedRowIndex() {
        return selectedRowIndex;
    }

    public int getSelectedCellIndex() {
        return selectedCellIndex;
    }

    public int getSelectedHeaderCellIndex() {
        return selectedHeaderCellIndex;
    }

    public int getSelectedDataRowIndex() {
        return selectedDataRowIndex;
    }

    public void setSelectedDataRowIndex(int selectedDataRowIndex) {
        this.selectedDataRowIndex = selectedDataRowIndex;
    }

    public DmGrid(T t, boolean isRowNoColumnRequired) {
        this(t, isRowNoColumnRequired, "100%", "120px");
    }

    public DmGrid(T t, boolean isRowNoColumnRequired, String width, String height) {
        this.isRowNoColumnRequired = isRowNoColumnRequired;
        this.t=t;
        flwContainer.setStyleName("responsiveGridContainer");
        flwContainer.getElement().setAttribute("style", "width:" + width + "; height:" + height + "; overflow:auto;");
        flxGrid.setStyleName("responsiveGrid");
        fcsPnl.addClickHandler(this);
        fcsPnl.addKeyDownHandler(this);
        fcsPnl.addBlurHandler(this);
        flwContainer.add(fcsPnl);
        commitChanges(t);
        clear();
    }

    public List<T> getData() {
        return data;
    }

//    private FlowPanel getDetailTable(List<DmGridCell> lstData) {
//        FlowPanel flwTable, flwTr, flwTd;
//        flwTable = new FlowPanel();
//        flwTable.setStyleName("responsiveGridDetail");
//
//        for (DmGridCell dgcm : lstData) {
//
//            flwTr = new FlowPanel();
//            flwTr.setStyleName("tr");
//
//            flwTd = new FlowPanel();
//            flwTd.setStyleName("td");
//            flwTd.add(new Label(dgcm.getName()));
//            flwTr.add(flwTd);
//
//            flwTd = new FlowPanel();
//            flwTd.setStyleName("td");
//            flwTd.add(new Label(" : " + dgcm.getValue()));
//            flwTr.add(flwTd);
//
//            flwTable.add(flwTr);
//        }
//        return flwTable;
//    }
    public final void commitChanges() {
//        if (data.isEmpty()) {
//            clear();
//            return;
//        }

        int rowIndex = 0, cellIndex = 0, dataIndex = 0;
        String style = "";
        List<DmGridCell> dataRow;
        try {
            flxGrid.removeAllRows();

            if (rowConfigCallBack != null) {
                cellIndex = 0;
                if (isRowNoColumnRequired) {
                    addCell(rowIndex, cellIndex++, "", "header", "width:21px;");
                }
                for (DmGridCell firstDataRow : getRowFromString(rowConfigCallBack.rowConfig(t))) {
                    style = "";
                    if (selectedHeaderCellIndex == cellIndex) {
                        style += "color: red; ";
                    }
                    addCell(rowIndex, cellIndex++, firstDataRow.getName(), "header " + firstDataRow.getCssClasses(), style, firstDataRow.getAlias());
                }
                
                    for (T t : data) {
                        dataRow = getRowFromString(rowConfigCallBack.rowConfig(t));

                        cellIndex = 0;
                        if (isRowNoColumnRequired) {
                            addCell(++rowIndex, cellIndex++, String.valueOf(dataIndex + 1), "", "width:21px;");
                        }

                        boolean firstCellFlag = false;
                        for (DmGridCell dataCell : dataRow) {
                            if (!firstCellFlag) {
                                if (!isRowNoColumnRequired) {
                                    ++rowIndex;
                                }
                                firstCellFlag = true;
                            }

                            if (cellBeforeRenderCallBack != null) {
                                dataCell.setStyle(cellBeforeRenderCallBack.onCellBeforeRender(dataCell.getStyle(), cellIndex, rowIndex - 1, dataCell.getValue()));
                                cellBeforeRenderCallBack.onCellBeforeRender(dataCell.getStyle(), cellIndex, rowIndex - 1, dataCell.getValue());
                            }
                            if (cellValueBeforeRenderCallBack != null) {
                                dataCell.setValue(cellValueBeforeRenderCallBack.setCellValueBeforeRender(cellIndex, rowIndex - 1, dataCell.getValue()));
                                // cellValueBeforeRenderCallBack.onCellBeforeRender(dataCell.getStyle(), cellIndex, rowIndex - 1, dataCell.getValue());
                            }
                            addCell(rowIndex, cellIndex++, dataCell.getValue(), dataCell.getCssClasses(), dataCell.getStyle(), dataCell.getAlias(), dataCell.getDataType(), String.valueOf(dataIndex));
                        }
                        dataIndex++;
                    }
                

                selectLastRow();
            }
        } catch (Exception ex) {
            new DmAlertBox("Error in grid creation...", (DmTextBox) null).show();
        }
    }

    public final void commitChanges(T t) {
        data.add(t);
        commitChanges();
    }

    private void addCell(int row, int cell, String html, String cssClasses, String style) {
        addCell(row, cell, html, cssClasses, style, "", "", "");
    }

    private void addCell(int row, int cell, String html, String cssClasses, String style, String alias) {
        addCell(row, cell, html, cssClasses, style, alias, "", "");
    }

    private void addCell(int row, int cell, String html, String cssClasses, String style, String alias, String dataType, String dataIndex) {
        flxGrid.setHTML(row, cell, DmShared.xNull(html));
        flxGrid.getFlexCellFormatter().setStyleName(row, cell, cssClasses);
        flxGrid.getFlexCellFormatter().getElement(row, cell).setAttribute("data-alias", alias);
        flxGrid.getFlexCellFormatter().getElement(row, cell).setAttribute("data-type", dataType);
        flxGrid.getFlexCellFormatter().getElement(row, cell).setAttribute("data-index", dataIndex);
        flxGrid.getFlexCellFormatter().getElement(row, cell).setAttribute("style", style);
    }

    private final void removeColumns(String... args) {
        columnsToRemove.clear();
        for (String arg : args) {
            columnsToRemove.add(DmShared.xNull(arg));
        }
    }

    private final void clear() {
        for (int rowIndex = (flxGrid.getRowCount() - 1); rowIndex > 0; rowIndex--) {
            flxGrid.removeRow(rowIndex);
        }
    }

    public static String getStringFromRow(List<DmGridCell> dataRow) {
        String strDataRow = "";

        for (DmGridCell dataCell : dataRow) {
            strDataRow += DmGridCell.Field.NAME + DmShared.xNull(dataCell.getName()).trim() + CELL_FIELD_VALUE_SEPARATOR;
            strDataRow += DmGridCell.Field.VALUE + DmShared.xNull(dataCell.getValue()).trim() + CELL_FIELD_VALUE_SEPARATOR;
            strDataRow += DmGridCell.Field.CSSCLASSES + DmShared.xNull(dataCell.getCssClasses()).trim() + CELL_FIELD_VALUE_SEPARATOR;
            strDataRow += DmGridCell.Field.ALIAS + DmShared.xNull(dataCell.getAlias()).trim() + CELL_FIELD_VALUE_SEPARATOR;
            strDataRow += DmGridCell.Field.DATATYPE + DmShared.xNull(dataCell.getDataType()).trim() + CELL_FIELD_VALUE_SEPARATOR;
            strDataRow += DmGridCell.Field.STYLE + DmShared.xNull(dataCell.getStyle()).trim() + CELL_FIELD_VALUE_SEPARATOR;
            strDataRow += CELL_SEPARATOR;
        }

        return strDataRow.substring(0, strDataRow.lastIndexOf(CELL_SEPARATOR));
    }

    private List<DmGridCell> getRowFromString(String strDataRow) {

        List<DmGridCell> dataRow = new ArrayList<>();
        DmGridCell dataCell = null;

        String[] cells = strDataRow.split("[" + CELL_SEPARATOR + "]");
        for (String cell : cells) {
            dataCell = new DmGridCell();
            for (String str : cell.split("[" + CELL_FIELD_VALUE_SEPARATOR + "]")) {
                if (str.contains(DmGridCell.Field.NAME)) {
                    dataCell.setName(DmShared.xNull(str.replace(DmGridCell.Field.NAME, "")));
                } else if (str.contains(DmGridCell.Field.VALUE)) {
                    dataCell.setValue(DmShared.xNull(str.replace(DmGridCell.Field.VALUE, "")));
                } else if (str.contains(DmGridCell.Field.CSSCLASSES)) {
                    dataCell.setCssClasses(DmShared.xNull(str.replace(DmGridCell.Field.CSSCLASSES, "")));
                } else if (str.contains(DmGridCell.Field.ALIAS)) {
                    dataCell.setAlias(DmShared.xNull(str.replace(DmGridCell.Field.ALIAS, "")));
                } else if (str.contains(DmGridCell.Field.DATATYPE)) {
                    dataCell.setDataType(DmShared.xNull(str.replace(DmGridCell.Field.DATATYPE, "")));
                } else if (str.contains(DmGridCell.Field.STYLE)) {
                    dataCell.setStyle(DmShared.xNull(str.replace(DmGridCell.Field.STYLE, "")));
                }
            }
            if (!columnsToRemove.contains(DmShared.xNull(dataCell.getName()))) {
                dataRow.add(dataCell);
            }
        }

        return dataRow;
    }

    public int getOffsetHeight() {
        return flwContainer.getOffsetHeight();
    }

    public void selectCell(int rowIndex, int cellIndex) {
        if (flxGrid.isCellPresent(rowIndex, cellIndex)) {
            if (rowIndex > 0 && cellIndex > 0) {
                if (flxGrid.isCellPresent(selectedRowIndex, selectedCellIndex)) {
                    if (selectedRowIndex > 0 && selectedCellIndex > 0) {
                        flxGrid.getFlexCellFormatter().removeStyleName(selectedRowIndex, selectedCellIndex, "selected-cell");
                    }
                }
                flxGrid.getFlexCellFormatter().addStyleName(rowIndex, cellIndex, "selected-cell");
                this.selectedRowIndex = rowIndex;
                this.selectedCellIndex = cellIndex;
                this.selectedDataRowIndex = Integer.valueOf(flxGrid.getFlexCellFormatter().getElement(rowIndex, cellIndex).getAttribute("data-index"));
                fcsPnl.setFocus(true);
            }
        }
    }

    private void selectLastRow() {
        boolean flag = true;
        if (rowConfigCallBack != null) {
            if (data.size() > 0) {
                for (DmGridCell cell : getRowFromString(rowConfigCallBack.rowConfig(data.get(data.size() - 1)))) {
                    if (!DmShared.xNull(cell.getValue()).equalsIgnoreCase("")) {
                        flag = false;
                    }
                }
            }
            if (data.size() > 1 && flag) {
                fcsPnl.setFocus(true);
                selectCell(flxGrid.getRowCount() - 1, isRowNoColumnRequired ? 1 : 0);
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        int rowIndex = flxGrid.getCellForEvent(event).getRowIndex();
        int cellIndex = flxGrid.getCellForEvent(event).getCellIndex();
        if (rowIndex != 0) {
            onSelect(rowIndex, cellIndex);
        } else {
            if (headerClickHandler != null) {
                headerClickHandler.onHeaderClick(getSelectedDbFieldName(cellIndex));
            }
        }
    }

    private String getSelectedDbFieldName(int cellIndex) {
        if (flxGrid.isCellPresent(0, cellIndex)) {
            if (flxGrid.isCellPresent(0, selectedHeaderCellIndex)) {
                flxGrid.getFlexCellFormatter().getElement(0, selectedHeaderCellIndex).getStyle().setColor("inherit");
            }
            flxGrid.getFlexCellFormatter().getElement(0, cellIndex).getStyle().setColor("red");
            selectedHeaderCellIndex = cellIndex;
            return flxGrid.getFlexCellFormatter().getElement(0, cellIndex).getAttribute("data-alias");
        } else {
            return "";
        }
    }

//    private String getDefaultDbFieldName() {
//        if (isRowNoColumnRequired) {
//            return getSelectedDbFieldName(1);
//        } else {
//            return getSelectedDbFieldName(0);
//        }
//    }
    private void onSelect(int rowIndex, int cellIndex) {
        int dataRowIndex = Integer.valueOf(flxGrid.getFlexCellFormatter().getElement(rowIndex, cellIndex).getAttribute("data-index"));
        selectCell(rowIndex, cellIndex);
        if (rowClickHandler != null) {

            rowClickHandler.onRowClick(dataRowIndex);
        }
        if (cellClickHandler != null) {
            cellClickHandler.onCellClick(cellIndex, dataRowIndex);
        }
    }

    @Override
    public Widget asWidget() {
        return flwContainer;
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        switch (event.getNativeKeyCode()) {
            case KeyCodes.KEY_DOWN:
                selectCell(selectedRowIndex + 1, selectedCellIndex);
                break;
            case KeyCodes.KEY_UP:
                selectCell(selectedRowIndex - 1, selectedCellIndex);
                break;
            case KeyCodes.KEY_LEFT:
                selectCell(selectedRowIndex, selectedCellIndex - 1);
                break;
            case KeyCodes.KEY_RIGHT:
                selectCell(selectedRowIndex, selectedCellIndex + 1);
                break;
            case KeyCodes.KEY_ENTER:
                onSelect(selectedRowIndex, selectedCellIndex);
                break;
            default:
                break;
        }
    }

    public void setFocus(boolean focused) {
        fcsPnl.setFocus(focused);
        selectCell(1, isRowNoColumnRequired ? 1 : 0);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(fcsPnl)) {
            if (flxGrid.isCellPresent(selectedRowIndex, selectedCellIndex)) {
                if (selectedRowIndex > 0 && selectedCellIndex > 0) {
                    flxGrid.getFlexCellFormatter().removeStyleName(selectedRowIndex, selectedCellIndex, "selected-cell");
                }
            }
        }
    }

}
