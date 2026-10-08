package aarogya.dashBoard.totalRefundForDate.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.dashBoard.totalRefundForDate.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;
import java.util.List;

public final class TotalRefundForDate extends TotalRefundForDate_Dsg implements DmRootEvents, AttachEvent.Handler, BlurHandler, KeyUpHandler, KeyDownHandler, ValueChangeHandler<Boolean> {

    private final ServiceInvoker<TotalRefundForDate_SrvAsync> SrvAsync;
    private String dbField = "description", searchPrvValue = "";
    private int searchColumnIndex;
    private DmDataType dataType = DmDataType.STRING;
    private KeyFieldExtended defaultValue;

    public TotalRefundForDate(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        SrvAsync = new ServiceInvoker<TotalRefundForDate_SrvAsync>(GWT.create(TotalRefundForDate_Srv.class), TotalRefundForDate.class.getName());
        grdDataDisplay.getHelper().addFilter(dbField, dataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        gridEvents();
        handlerEvents();
        manageDisplay();
        getDefaultValue(swi);

    }

    private void getDefaultValue(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    setDefaultValue();
                    swi.setWidget(TotalRefundForDate.this);
                    reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setDefaultValue() {
        if (defaultValue != null) {
            txtDaysUpto.setText("90");
        }
    }

    private void handlerEvents() {
        flwForm.addAttachHandler(this);
        btnRefresh.addClickHandler(this);
        btnCancel.addClickHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addBlurHandler(this);
        txtSearchValue.addDomHandler(this, KeyDownEvent.getType());
        txtDaysUpto.addBlurHandler(this);

        btnNext.addClickHandler(this);

        btnExportData.addClickHandler(this);
    }

    private SearchValue_Mdl setParam(String dbField, DmTextBox txtBox) {
        SearchValue_Mdl svm = new SearchValue_Mdl();
        if (!DmShared.xNull(dbField).trim().equalsIgnoreCase("") && txtBox != null) {
            svm.setFieldName(dbField);
            svm.setFieldNameValue(DmShared.xNull(txtBox.getText()));
            svm.setDate(DmShared.dNull(txtDaysUpto.getText()));
        }
        return svm;
    }

    private void exportCSV() {
        SrvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), DmShared.iNull(txtDaysUpto.getText()).toString(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                }
            }
        });
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

    private void gridEvents() {
        setHeaderValue(0, searchColumnIndex, grdDataDisplay, dbField, dataType, txtSearchValue);

        grdDataDisplay.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex(), searchColumnIndex, grdDataDisplay, dbField, dataType, txtSearchValue);
                DmColumnConfig col = ((DmColumnConfig) grdDataDisplay.getDmGrid().getGrid().getColumnModel().getColumn(event.getColumnIndex()));
                dbField = col.getDbFieldName();
                dataType = col.getDbFieldDataType();
            }
        });
        grdDataDisplay.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                SrvAsync.invokeRPC(blnToEndOfFile).getData(DmShared.iNull(txtDaysUpto.getText()).toString(), helper, blnToEndOfFile, searchValue_Mdl, new AsyncCallback<DmResultPageing<List<TotalRefundForDate_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TotalRefundForDate_Mdl>> result) {
                        if (result.isStatus()) {
                            grdDataDisplay.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return setParam(dbField, txtSearchValue);
            }
        });

    }

    private void setHeaderValue(int columnIndex, int searchColumn, DmGridPaging grdPaging, String dbField, DmDataType dataType, DmTextBox txtBox) {
        if ((grdPaging.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumn = 0;
        } else if (columnIndex < 0) {
            searchColumn = grdPaging.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumn = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdPaging.getDmGrid().getGrid().getColumnModel().getColumn(searchColumn));
        dbField = col.getDbFieldName();
        dataType = col.getDbFieldDataType();
        grdPaging.getHelper().getFilter().get(0).setColumnName(dbField);
        grdPaging.getHelper().getFilter().get(0).setDataType(dataType);
        txtBox.setText("");
        txtBox.getElement().setPropertyString("placeholder", col.getHeader().asString());

        for (ColumnConfig col1 : ((List<ColumnConfig>) grdPaging.getDmGrid().getGrid().getColumnModel().getColumns())) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdPaging.getDmGrid().getGrid().getView().refresh(true);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtSearchValue.setFocus(true);
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    @Override
    public void onClick(ClickEvent event) {

        if (event.getSource() == btnCancel) {
            onClose();
        }

        if (event.getSource() == btnExportData) {
            exportCSV();
        }

        if (event.getSource() == btnRefresh) {

            reQuery();
        }

        if (event.getSource() == btnNext) {
            try {
                if (!DmShared.xNull(txtDaysUpto.getText()).equalsIgnoreCase("")) {
                    int days = DmShared.iNull(txtDaysUpto.getText()).intValue() + 1;
                    txtDaysUpto.setText(DmShared.iNull(days).toString());
                    reQuery();

                } else {
                    new DmAlertMessageBox("Days is mandatory.", txtDaysUpto).show();
                }
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), btnNext).show();
            }
        }

    }

    public void getCount() {
        SrvAsync.invokeRPC(false).getCardDetail(DmShared.iNull(txtDaysUpto.getText()).toString(), new AsyncCallback<DmResult<CardDetail_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (Button) null).show();
            }

            @Override
            public void onSuccess(DmResult<CardDetail_Mdl> result) {
                if (result.isStatus()) {

                    lbl2.setText("Total : " + result.getData().getCount());
//                    setValue(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                }
            }
        });
    }

    private void manageDisplay() {

        txtDaysUpto.setText("15");
    }

    private void reQuery() {
        getCount();
        grdDataDisplay.getHelper().setRowsStartFrom(0);
        grdDataDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDataDisplay.getHelper().getFilter().get(0).setColumnName(dbField);
        grdDataDisplay.getHelper().getFilter().get(0).setDataType(dataType);
        grdDataDisplay.reQuery();
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
                reQuery();
            }
            searchPrvValue = txtSearchValue.getText();
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(TotalRefundForDate.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            txtSearchValue.setText(DmShared.xNull(txtSearchValue.getText()));
        } else if (event.getSource().equals(txtDaysUpto)) {

        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource() == txtSearchValue) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                ((CellSelectionModel) grdDataDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                int rowIndex = grdDataDisplay.getDmGrid().getGrid().getStore().indexOf(grdDataDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem());

            } else if (event.isControlKeyDown() == false && event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdDataDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdDataDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            }
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "REFRESH":
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<Boolean> event) {
        try {
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

}
