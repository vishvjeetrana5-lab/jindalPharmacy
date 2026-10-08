package aarogya.dashBoard.dataModificationLog.client;

import aarogya.dashBoard.dataModificationLog.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.DmEnum.DmDataType;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmReportLayout.shared.DmReportLayout_SrvAsync;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;
import java.util.List;

public final class DataModificationLog extends DataModificationLog_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, KeyUpHandler, KeyDownHandler {

    private ServiceInvoker<DataModificationLog_SrvAsync> srvAsync;
    private final Filter filter = new Filter();
    private final EyeDataPopUp eyeDataPopUp = new EyeDataPopUp();
    private final DataPopUp dataPopUp = new DataPopUp();

    public DmReportProperty_Mdl reportProperty = new DmReportProperty_Mdl();
    private int searchColumnIndex;
    private String searchDbFieldName = "sessionid", searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;

    private SearchValue_Mdl searchMdl = new SearchValue_Mdl();
    private DefaultValue_Mdl dvm;

    public DataModificationLog(UserMenu menuPerdashBoardsion, SetWidgetInvoker swi) {
        super();
        try {
            srvAsync = new ServiceInvoker<DataModificationLog_SrvAsync>(GWT.create(DataModificationLog_Srv.class), DataModificationLog.class.getName());
            this.asWidget().setTitle(widgetName);
            grdDisplay.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
            handlerEvents();
            gridEvents();
            textEvents();
            defaultValues(swi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void defaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    searchMdl.setFromDate(dvm.getFromDate());
                    searchMdl.setToDate(dvm.getToDate());
                    searchMdl.setSiteCodeList(DmShared.xNull(dvm.getCurrentSiteCode()));
                    searchMdl.setSiteNameList(DmShared.xNull(dvm.getCurrentSiteName()));
                    fillData(result);
                    swi.setWidget(DataModificationLog.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void handlerEvents() {
        btnPrint.addClickHandler(this);
        btnExcel.addClickHandler(this);
        btnRefresh.addClickHandler(this);
        btnClose.addClickHandler(this);
        btnSetting.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
        txtSearchValue.addKeyDownHandler(this);
    }

    private void textEvents() {
        filter.addCallBack(new Filter.CallBack() {
            @Override
            public void onOk(SearchValue_Mdl searchValue) {
                searchMdl = searchValue;
                lblDate.setText(searchMdl.getFromDate() + " To " + searchMdl.getToDate());
                checkSearchValue();
                reQuery();
            }
        });
    }

    private void gridEvents() {
        setHeaderValue(3);
        grdDisplay.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                setHeaderValue(event.getColumnIndex());
            }
        });

        grdDisplay.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl searchValue_Mdl) {
                srvAsync.invokeRPC(blnToEndOfFile).getFillRecords(searchMdl, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<GrdDataModificationLog_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdDataModificationLog_Mdl>> result) {
                        if (result.isStatus()) {
                            grdDisplay.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return searchMdl;
            }
        });
        grdDisplay.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (DmShared.iNull(event.getColumnIndex()).intValue() == 11) {
                    eyeDataPopUp.show(DmShared.xNull(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getOldData()), DmShared.xNull(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getNewData()),
                            (grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getModule()), DmShared.xNull(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getMenu()),
                            (grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getRecordName1()));
                } else {
                    dataPopUp.show(DmShared.xNull(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getNewData()), DmShared.xNull(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getOldData()),
                            (grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getModule()), DmShared.xNull(grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getMenu()),
                            (grdDisplay.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getRecordName1()));
                }
            }
        });
    }

    private void reQuery() {
        grdDisplay.getHelper().setRowsStartFrom(0);
        grdDisplay.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        grdDisplay.reQuery();
    }

    private void fillData(DmResult<DefaultValue_Mdl> result) {
        dvm = result.getData();
        lblDate.setText(dvm.getFromDate() + " To " + dvm.getToDate());
        checkSearchValue();
        grdDisplay.reQuery();

        ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
    }

    private void setHeaderValue(int columnIndex) {
        if ((grdDisplay.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = grdDisplay.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) grdDisplay.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        grdDisplay.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        grdDisplay.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : grdDisplay.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        grdDisplay.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
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
        if (event.getSource().equals(btnSetting)) {
            filter.show(searchMdl, dvm);
        } else if (event.getSource() == btnPrint) {
            reportProperty.setExportType(DmEnum.DmExportType.PDF);
            prnRecord();
        } else if (event.getSource() == btnExcel) {
            downloadExcel();
        } else if (event.getSource().equals(btnRefresh)) {
            checkSearchValue();
            reQuery();
        } else if (event.getSource().equals(btnClose)) {
            onClose();
        }
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
        try {
            if (event.isAttached()) {
                DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
                checkSearchValue();
                reQuery();
            } else {
                DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            checkSearchValue();
            reQuery();
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_UP || event.getNativeKeyCode() == KeyCodes.KEY_DOWN) {
                if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                    ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 0);
                }
            } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                if (grdDisplay.getDmGrid().getGrid().getStore().size() > 0) {
                    if (grdDisplay.getDmGrid().getGrid().getSelectionModel().getSelectedItem() == null) {
                        ((CellSelectionModel) grdDisplay.getDmGrid().getGrid().getSelectionModel()).selectCell(0, 1);
                    }
                }
            }
        }
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(DataModificationLog.this);
                }
            }
        }).show();
    }

    private void checkSearchValue() {
        if (DmShared.xNull(txtSearchValue.getText()).trim().equals("")) {
            grdDisplay.getHelper().getFilter().get(0).setColumnName("sessionid");
            grdDisplay.getHelper().getFilter().get(0).setDataType(DmDataType.STRING);
            grdDisplay.getHelper().getFilter().get(0).setValue1("");
        } else if (!searchDbFieldName.trim().equalsIgnoreCase("sessionid")) {
            grdDisplay.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
            grdDisplay.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        }
    }

    private void downloadExcel() {
        try {
            srvAsync.invokeRPC(true).getDownloadExcel(widgetName, searchMdl, new AsyncCallback<DmResult<KeyFieldExtended>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<KeyFieldExtended> result) {
                    if (result.isStatus()) {
                        startDownloading(result.getData().getSharedPreparedBy(), result.getData().getSharedKeyFieldName(), result.getData().getSharedKeyFieldCode());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void prnRecord() {
        try {
            srvAsync.invokeRPC(true).getRecordPrint(widgetName, searchMdl, reportProperty, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        getReport(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    public String getReport(String fileName) {
        String reportUrl = null;
        if (!DmShared.xNull(fileName).trim().equalsIgnoreCase("")) {
            DmBaseURL baseURL = DmClient.getHostPageBaseURL();
            UrlBuilder urlBuilder = new UrlBuilder();
            urlBuilder.setProtocol(baseURL.getProtocol());
            urlBuilder.setPath(baseURL.getUrl() + DmShared.classPathToPtah(DmReportLayout_SrvAsync.class.getName()));
            urlBuilder.setParameter("u", DmRequestBuilder.getSessionId());
            urlBuilder.setParameter("f", fileName);
            reportUrl = urlBuilder.buildString().replace("///", "//");
            Window.open(reportUrl, "_blank", "");
        }
        return reportUrl;
    }

    private void startDownloading(String sessionId, String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", sessionId);
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }
}
