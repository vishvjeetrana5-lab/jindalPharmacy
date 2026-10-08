package dataman.dmToolkitR.dmGridPaging.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmToolkitR.dmGridHelp.client.DmGridCell;
import dataman.dmToolkitR.dmGridHelp.client.DmGridCellCaption;
import dataman.dmToolkitR.dmGridHelp.client.DmGridCellValue;
import dataman.dmToolkitR.dmGridHelp.client.DmGridHelp;
import dataman.dmToolkitR.dmGridHelp.client.DmGridRow;
import java.util.HashMap;
import java.util.List;
import org.gwtbootstrap3.client.ui.Icon;

public class DmGridPaging<T extends KeyField> extends DmGridPaging_Dsg implements ClickHandler, KeyUpHandler {

    public interface RowConfigCallBack<T extends KeyField> {

        public void rowConfig(DmGridRow<T> row, T tRow);
    }

    public interface GridPagingCallBack<T extends KeyField> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelectedRow(T tRow, DmGridRow<T> row);
    }

    public interface CellConfigCallBack<T> {

        public void getSelectedCell(String value, String dBField);
    }

    private CellConfigCallBack<T> cellConfigCallBack;
    private GridPagingCallBack<T> gridPagingCallBackCallBack;
    private RowConfigCallBack<T> rowConfig;
    private DmHelper helper;
    private String searchDbFieldName;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;
    private boolean isFirstPage = false, isPrvPage = false, isNextPage = false, isLastPage = false;
    private HashMap hmCellCallback = new HashMap();

    public DmGridPaging(String dbField, int rowsPerPage) {
        super();
        this.searchDbFieldName = dbField.trim().toLowerCase();
        helper = new DmHelper(0, rowsPerPage);
        helper.addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        addEvents();
    }

    public void addGridPagingCallBack(GridPagingCallBack<T> gridPagingCallBackCallBack) {
        this.gridPagingCallBackCallBack = gridPagingCallBackCallBack;
    }

    public void addRowConfig(RowConfigCallBack<T> rowConfig) {
        this.rowConfig = rowConfig;
    }

    public void addCellConfig(CellConfigCallBack<T> cellConfigCallBack) {
        this.cellConfigCallBack = cellConfigCallBack;
    }

    private void addEvents() {
        txtSearch.addKeyUpHandler(this);
        firstPageIcn.addClickHandler(this);
        prvPageIcn.addClickHandler(this);
        nextPageIcn.addClickHandler(this);
        lastPageIcn.addClickHandler(this);

        grid.addColConfig(new DmGridHelp.ColConfigCallBack<T>() {

            @Override
            public void colConfig(DmGridCell<T> cell) {
                if (searchDbFieldName.trim().equalsIgnoreCase(cell.getDBField())) {
                    txtSearch.setPlaceholder(cell.getCaption());
                }
            }
        });

        grid.addRowConfig(new DmGridHelp.RowConfigCallBack<T>() {
            @Override
            public void rowConfig(DmGridRow<T> row, T tRow) {
                row.addDomHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        if (event.getSource() instanceof DmGridRow) {
                            if (gridPagingCallBackCallBack != null) {
                                gridPagingCallBackCallBack.getSelectedRow(((DmGridRow<T>) event.getSource()).getRow(),(DmGridRow<T>) event.getSource());
                              
                            }
                        }
                    }
                }, ClickEvent.getType());

                for (String s : row.getCellNames()) {
                    row.getCell(s).getValueCell().addDomHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            DmGridCell<T> cell = ((DmGridCellValue) event.getSource()).getCell();
                            if (hmCellCallback.containsKey(cell.getDBField().trim().toLowerCase())) {
                                if (cellConfigCallBack != null) {
                                    cellConfigCallBack.getSelectedCell(cell.getValue(), cell.getDBField());
                                }
                            }
                        }

                    }, ClickEvent.getType());

                    row.getCell(s).getCaptionCell().addDomHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            if (!hmCellCallback.containsKey(searchDbFieldName.trim().toLowerCase())) {
                                DmGridCell<T> cell = ((DmGridCellCaption) event.getSource()).getCell();
                                searchDbFieldName = cell.getDBField();
                                helper.getFilter().get(0).setColumnName(searchDbFieldName);
                                txtSearch.setPlaceholder(cell.getCaption());
                                txtSearch.setText("");

                                event.stopPropagation();
                                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                    @Override
                                    public void execute() {
                                      //  txtSearch.setFocus(true);
                                    }
                                });
                            }
                        }
                    }, ClickEvent.getType());
                }

                if (rowConfig != null) {
                    rowConfig.rowConfig(row, tRow);

                }
            }
        });
    }

    public DmGridHelp<T> getGrid() {
        return grid;
    }

    public DmGridCell<T> addColumn(String dbField, String caption, String width, boolean isCellCallbackRequired) {
        hmCellCallback.put(dbField.trim().toLowerCase(), isCellCallbackRequired);
        return grid.addColumn(dbField, caption, width);
    }


    public DmGridCell<T> addColumn(String dbField, String caption, String width) {
        return grid.addColumn(dbField, caption, width);
    }

    public void setAllData(List<T> list, DmPaging pageingVariables) {
        helper.setPaging(pageingVariables);

        grid.setAllData(list);

        setDisplay(pageingVariables);
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(lastPageIcn)) {
            navigation(lastPageIcn);
        } else if (event.getSource().equals(nextPageIcn)) {
            navigation(nextPageIcn);
        } else if (event.getSource().equals(prvPageIcn)) {
            navigation(prvPageIcn);
        } else if (event.getSource().equals(firstPageIcn)) {
            navigation(firstPageIcn);
        }
    }

    private void navigation(Icon btnPressed) {
        boolean blnToEndOfFile = false;
        boolean isPopulate = false;

        if (gridPagingCallBackCallBack != null) {
            if (firstPageIcn == btnPressed) {
                if (isFirstPage) {
                    helper.setRowsStartFrom(0);
                    isPopulate = true;
                }
            } else if (prvPageIcn == btnPressed) {
                if (isPrvPage) {
                    helper.setRowsStartFrom(helper.getRowsStartFrom() - helper.getRowsPerPage());
                    isPopulate = true;
                }
            } else if (nextPageIcn == btnPressed) {
                if (isNextPage) {
                    helper.setRowsStartFrom(helper.getRowsStartFrom() + helper.getRowsPerPage());
                    isPopulate = true;
                }
            } else if (lastPageIcn == btnPressed) {
                if (isLastPage) {
                    blnToEndOfFile = true;
                    isPopulate = true;
                }
            }
            if (isPopulate) {
                helper.getFilter().get(0).setValue1(txtSearch.getText());
                gridPagingCallBackCallBack.populateData(helper, blnToEndOfFile, false);
            }
        }

    }

    public void reQuery(String searchText) {
        if (gridPagingCallBackCallBack != null) {
            grid.clear();
            txtSearch.setText(searchText);
            helper.setRowsStartFrom(0);
            helper.getFilter().get(0).setValue1(txtSearch.getText());
            gridPagingCallBackCallBack.populateData(helper, false, false);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        reQuery(txtSearch.getText());
    }

    private void setDisplay(DmPaging pageingVariables) {
        firstPageIcn.removeStyleName("disable");
        prvPageIcn.removeStyleName("disable");
        nextPageIcn.removeStyleName("disable");
        lastPageIcn.removeStyleName("disable");

        if (pageingVariables == null) {
            firstPageIcn.addStyleName("disable");
            isFirstPage = false;

            prvPageIcn.addStyleName("disable");
            isPrvPage = false;

            nextPageIcn.addStyleName("disable");
            isNextPage = false;

            lastPageIcn.addStyleName("disable");
            isLastPage = false;

        } else {
            if (pageingVariables.isBeginOfFile()) {
                firstPageIcn.addStyleName("disable");
                isFirstPage = false;

                prvPageIcn.addStyleName("disable");
                isPrvPage = false;
            } else {
                firstPageIcn.setVisible(true);
                isFirstPage = true;

                prvPageIcn.setVisible(true);
                isPrvPage = true;
            }

            if (pageingVariables.isEndOfFile()) {
                nextPageIcn.addStyleName("disable");
                isNextPage = false;

                lastPageIcn.addStyleName("disable");
                isLastPage = false;
            } else {
                nextPageIcn.setVisible(true);
                isNextPage = true;

                lastPageIcn.setVisible(true);
                isLastPage = true;
            }
        }
    }

    public DmHelper getHelper() {
        return helper;
    }

    @Override
    public Widget asWidget() {
        return flowPanel;
    }

    public void setFocus() {
       // txtSearch.setFocus(true);
    }
}
