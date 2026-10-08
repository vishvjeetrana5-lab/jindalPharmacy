package dataman.dmToolkit.dmFind.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmDynamicReportBuilder.shared.DmMasterReport;
import dataman.dmBase.dmDynamicReportBuilder.shared.DmMasterReportColumn;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmDateTimeFormat;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmOperand;
import dataman.dmBase.self.shared.DmEnum.DmOperator;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmPaging;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DmFind<T1, T2> extends DmFind_Dsg implements IsWidget, KeyDownHandler, DmRootEvents, AttachEvent.Handler, ClickHandler, KeyUpHandler {

    public interface FindCallBack<T1> {

        public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait);

        public void getSelectedRow(boolean blnStatus, T1 t1Row);
    }

    private DmHelper helper;
    private DmGrid<T1, T2> dmGrid;
    private FindCallBack findCallBack;
    private String searchPrvValue = "";
    private int gridHeight = 0;
    private int searchColumnIndex;
    private String searchDbFieldName;
    private DmEnum.DmDataType searchDbFieldDataType = DmEnum.DmDataType.STRING;

    private void gridEvents(Object t2PrpAcs) {
        dmGrid = new DmGrid<T1, T2>(t2PrpAcs);
        dmGrid.addInitializeGridCallBackHandler(new DmGrid.InitializeGridCallBack() {
            @Override
            public void onInitialized() {
                flwGrid.clear();
                dmGrid.getGrid().getElement().getStyle().setBorderStyle(Style.BorderStyle.NONE);
                flwGrid.add(dmGrid.getGrid());
                dmGrid.getGrid().addDomHandler(DmFind.this, KeyDownEvent.getType());

                dmGrid.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
                    @Override
                    public void onCellClick(CellClickEvent event) {
                        gridCellClick(event);
                    }
                });

                if (helper.getRowsPerPage() > 0) {
                    dmGrid.getGrid().setHeight(String.valueOf(gridHeight) + "px");
                    flwGridScroller.setHeight(gridHeight + "px");
                }

                setSearchColumn(0);

                dmGrid.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {

                    @Override
                    public void onHeaderClick(HeaderClickEvent event) {
                        setSearchColumn(event.getColumnIndex());
                    }
                });
            }
        });
    }

    private void setSearchColumn(int columnIndex) {
        if ((dmGrid.getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = dmGrid.getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) dmGrid.getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        helper.getFilter().get(0).setColumnName(searchDbFieldName);
        helper.getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearch.setText("");
        txtSearch.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : dmGrid.getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmGrid.getGrid().getView().refresh(true);
        txtSearch.setFocus(true);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootKeyDown(rootEvent);
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == dmGrid.getGrid()) {
            gridKeydown(event);
        } else if (event.getSource() == txtSearch) {
            txtKeyDown(event);
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (!searchPrvValue.equalsIgnoreCase(txtSearch.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearch.getText();
    }

    private void txtKeyEvents() {
        txtSearch.addDomHandler(this, KeyDownEvent.getType());

        txtSearch.addKeyUpHandler(this);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            if (dmGrid.getGrid().getStore().size() <= 0) {
                reQuery();
            }
            txtSearch.setFocus(true);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    private void btnEvents(Button[] btn) {
        for (Button btn1 : btn) {
            btn1.addClickHandler(this);
        }
    }

    public void addHelpGridCallBack(FindCallBack helpGridCallBack) {
        this.findCallBack = helpGridCallBack;
    }

    public void setData(List<T1> lstData, DmPaging pageingVariables) {
        if (pageingVariables.getResultFormat() == DmPaging.Result.XLS) {
            helper.getPaging().setResultFormat(DmPaging.Result.DEFAULT);
            startDownloading(pageingVariables.getFileName(), pageingVariables.getFilePath());
        } else {
            helper.setPaging(pageingVariables);
            dmGrid.getGrid().getStore().clear();
            dmGrid.getGrid().getSelectionModel().deselectAll();
            if (lstData.size() > 0) {
                dmGrid.getGrid().getStore().addAll(lstData);
            }
            setDisplay(pageingVariables);
        }
    }

    public DmGrid<T1, T2> getDmGrid() {
        return dmGrid;
    }

    public void setTitle(String title) {
        toolContainer.setTitle(title);
    }

    public String getTitle() {
        return toolContainer.getTitle();
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public DmTextBox getTextBox() {
        return txtSearch;
    }

    public DmHelper getHelper() {
        return helper;
    }

    public DmFind(Object t2PrpAcs, int rowsPerPage, boolean withHorizontalScroll) {
        super();
        startUp(t2PrpAcs, rowsPerPage, withHorizontalScroll);
    }

    public DmFind(Object t2PrpAcs, int rowsPerPage) {
        super();
        startUp(t2PrpAcs, rowsPerPage, false);
    }

    private void startUp(Object t2PrpAcs, int rowsPerPage, boolean withHorizontalScroll) {
        toolContainer.addWidget(txtSearch);
        toolContainer.addWidget(btnOk);
//        toolContainer.addWidget(btnFilter);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addWidget(btnDownload);
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();
        toolContainer.addWidget(lblRecords);
        toolContainer.setMode("Tabular");

        gridHeight = ((rowsPerPage + 1) * 22) + 2;
        if (withHorizontalScroll) {
            gridHeight += 18;
        }
        gridEvents(t2PrpAcs);
        vrtForm.addAttachHandler(this);
        txtKeyEvents();
        btnEvents(new Button[]{btnFilter, btnRefresh, btnFirst, btnPrevious, btnNext, btnLast, btnDownload, btnOk, btnCancel});
        helper = new <T1> DmHelper(0, rowsPerPage);
        helper.addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
    }

    @Override
    public Widget asWidget() {
        return vrtForm;

    }

    private void setDisplay(DmPaging pageingVariables) {
        if (pageingVariables.isBeginOfFile()) {
            btnFirst.setVisible(false);
            btnPrevious.setVisible(false);
        } else {
            btnFirst.setVisible(true);
            btnPrevious.setVisible(true);
        }

        if (pageingVariables.isEndOfFile()) {
            btnNext.setVisible(false);
            btnLast.setVisible(false);
        } else {
            btnNext.setVisible(true);
            btnLast.setVisible(true);
        }

        if (dmGrid.getGrid().getStore().size() > 0) {
            btnOk.setVisible(true);
            btnDownload.setVisible(true);
        } else {
            btnOk.setVisible(false);
            btnDownload.setVisible(false);
        }
    }

    private void txtKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            btnClick(btnOk);
        } else if (event.isControlKeyDown() == false && event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
            if (dmGrid.getGrid().getStore().size() > 0) {
                ((CellSelectionModel) dmGrid.getGrid().getSelectionModel()).selectCell(0, 0);
            }
        }
    }

    private void btnClick(Button aSource) {
        if (!aSource.isVisible()) {
            return;
        }
        if (!aSource.isEnabled()) {
            return;
        }
        if (!aSource.isAttached()) {
            return;
        }

        if (aSource.equals(btnOk)) {
            if (dmGrid.getGrid().getStore().size() > 0) {
                if (dmGrid.getGrid().getSelectionModel().getSelectedItem() == null) {
                    ((CellSelectionModel) dmGrid.getGrid().getSelectionModel()).selectCell(0, 1);
                }

                if (findCallBack != null) {
                    findCallBack.getSelectedRow(true, dmGrid.getGrid().getSelectionModel().getSelectedItem());
                }
            }
        } else if (aSource.equals(btnCancel)) {
            findCallBack.getSelectedRow(false, null);
        } else if (aSource.equals(btnRefresh)) {
            reQuery();
        } else if (aSource.equals(btnFirst)) {
            navigation(btnFirst);
        } else if (aSource.equals(btnPrevious)) {
            navigation(btnPrevious);
        } else if (aSource.equals(btnNext)) {
            navigation(btnNext);
        } else if (aSource.equals(btnLast)) {
            navigation(btnLast);
        } else if (aSource.equals(btnDownload)) {
            createPrint();
        }
    }

    private void createPrint() {
        try {
            DmMasterReport report = new DmMasterReport();
            report.setTitle(toolContainer.getTitle());
            helper.getPaging().setReport(report);
            helper.getPaging().setResultFormat(DmPaging.Result.XLS);
            ColumnModel model = this.getDmGrid().getGrid().getColumnModel();
            for (int i = 0; i < model.getColumnCount(); i++) {
                if (model.getColumn(i) instanceof DmColumnConfig) {
                    DmColumnConfig ocol = (DmColumnConfig) model.getColumn(i);
                    DmMasterReportColumn col = new DmMasterReportColumn(ocol.getHeader().asString(), ocol.getDbFieldName());
                    col.setColWidth(ocol.getWidth());
                    report.getReportColumns().put(col.getTitle(), col);
                }
            }

            if (this.findCallBack != null) {
                this.findCallBack.populateData(helper, false, true);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");

        } catch (Exception ex) {
        }
    }

    private void reQuery() {
        if (findCallBack != null) {
            String date = "";

            helper.setRowsStartFrom(0);
            if (helper.getFilter().get(0).getDataType() == DmEnum.DmDataType.DATE
                    && !DmShared.xNull(txtSearch.getText()).trim().equalsIgnoreCase("")) {
                date = (DmClient.returnDate(txtSearch.getText()));
                Date d = DmShared.convertDate(date);
                date = DmDateTimeFormat.getFormat("yyyy-MM-dd").format(d);
                helper.getFilter().get(0).setValue1(date);
            } else {
                helper.getFilter().get(0).setValue1(txtSearch.getText());
            }

            findCallBack.populateData(helper, false, false);
        }
    }

    private void navigation(Button btnPressed) {
        boolean blnToEndOfFile = false;

        if (findCallBack != null) {
            if (btnFirst == btnPressed) {
                helper.setRowsStartFrom(0);
            } else if (btnPrevious == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() - helper.getRowsPerPage());
            } else if (btnNext == btnPressed) {
                helper.setRowsStartFrom(helper.getRowsStartFrom() + helper.getRowsPerPage());
            } else if (btnLast == btnPressed) {
                blnToEndOfFile = true;
            }

            helper.getFilter().get(0).setValue1(txtSearch.getText());
            findCallBack.populateData(helper, blnToEndOfFile, false);
        }
    }

    private void rootKeyDown(DmRootEvent event) {
        if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
            event.stopPropagation();
            btnClick(btnRefresh);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_F) {
            event.stopPropagation();
            btnClick(btnFilter);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_UP) {
            event.stopPropagation();
            btnClick(btnFirst);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_LEFT) {
            event.stopPropagation();
            btnClick(btnPrevious);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_RIGHT) {
            event.stopPropagation();
            btnClick(btnNext);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_DOWN) {
            event.stopPropagation();
            btnClick(btnLast);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            event.stopPropagation();
            btnClick(btnDownload);
        } else if (event.getEvent().getNativeEvent().getCtrlKey() && event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            event.stopPropagation();
            btnClick(btnOk);
        } else if (event.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            event.stopPropagation();
            btnClick(btnCancel);
        }
    }

    private void gridKeydown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            btnClick(btnOk);
            event.preventDefault();
            event.stopPropagation();
        }
    }

    public void gridCellClick(CellClickEvent event) {
        btnClick(btnOk);
    }
}
