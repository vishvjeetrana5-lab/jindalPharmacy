package dataman.webJobs.smsManager.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.webJobs.smsManager.shared.*;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.*;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.self.client.JSNICaller;
import dataman.webJobs.emailManager.client.FilterDataPopup;
import dataman.webJobs.emailManager.shared.FilterDataPopup_Mdl;
import dataman.webJobs.emailManager.shared.Template_Mdl;

import java.util.ArrayList;
import java.util.List;

public final class SmsManager extends SmsManager_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, KeyUpHandler {

    ServiceInvoker<SmsManager_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private Template_Mdl templateMdl;
    private List<FilterDataPopup_Mdl> filterDataList = new ArrayList<>();
    private final FilterDataPopup filterPopupData = new FilterDataPopup();
    private final DraftPopup prepareDataPopup = new DraftPopup();
    private DmColumnConfig<DmDynamicGridRow, String> selectCheckBox = null;

    public SmsManager(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(SmsManager_Srv.class), SmsManager.class.getName());
        this.asWidget().setTitle(widgetName);
        textHelpEvents();
        handlerEvents();
        swi.setWidget(this);
    }

    private void showGridEdit(int currentRowIndex, SmsManagerGridEdit managerGridEdit) {
        if (DmShared.xNull(txtTemplateName.getTag()).trim().equalsIgnoreCase("")) {
            managerGridEdit.show(currentRowIndex, false);
        } else {
            managerGridEdit.show(currentRowIndex, true);
        }
    }

    private void handlerEvents() {
        btnClose.addClickHandler(this);
        btnSend.addClickHandler(this);
        btnFilter.addClickHandler(this);
        btnDraft.addClickHandler(this);
        btnClearFilter.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
    }

    private void clear() {
        templateMdl = null;
        flwGrdDynamic.clear();
        txtDataSourceName.setTag("");
        txtDataSourceName.setTag2nd("");
        txtDataSourceName.setText("");
        txtFunFile.setTag("");
        txtFunFile.setTag2nd("");
        txtFunFile.setText("");
        txtSearchValue.setText("");
        lblDraftedRecords.setText("0");
        lblFilteredRecords.setText("0");
        lblSelectedRecords.setText("0");
        lblSentRecords.setText("0");
        manageDisplay(false);
    }

    private void manageDisplay(boolean flag) {
        txtDataSourceName.setEnabled(false);
        txtFunFile.setEnabled(false);
        txtTemplateName.setEnabled(false);
        txtSearchValue.setEnabled(flag);
    }

    private void textHelpEvents() {
        filterPopupData.addCallBack(new FilterDataPopup.CallBack() {
            @Override
            public void onOk(List<FilterDataPopup_Mdl> listFilterData) {
                filterDataList = listFilterData;
                dmSmsGrd.reQuery();
                setHeaderValue(1);
                setCheckUncheck(false);
            }
        });

        prepareDataPopup.addCallBack(new DraftPopup.CallBack() {

            @Override
            public void onOk(Template_Mdl template_Mdl) {
                clear();
                txtTemplateName.setTag(template_Mdl.getSharedKeyFieldCode());
                txtTemplateName.setText(template_Mdl.getSharedKeyFieldName());
                txtDataSourceName.setText(DmShared.xNull(template_Mdl.getWebJobDataSource().getSourceName()));
                txtFunFile.setText(template_Mdl.getFuncOrFileName());
                SmsManager.this.templateMdl = template_Mdl;
                manageDisplay(true);
                getMetadata(template_Mdl);
            }

            @Override
            public void onClose() {

            }

        });

        new JSNICaller(new JSNICaller.Callback() {
            @Override
            public void handle(NativeEvent eventArgs, String elementId) {
                if (elementId.equalsIgnoreCase("unCheckAll")) {
                    manageCheckAll("unCheckAll");
                }
                if (elementId.equalsIgnoreCase("checkAll")) {
                    manageCheckAll("checkAll");
                }
            }
        });

        chkRecordsFilter.addCallBack(new DmCheckBox.CallBack() {

            @Override
            public void valueChanged() {
                dmSmsGrd.reQuery();
            }
        });

    }

    private void getMetadata(final Template_Mdl templateMdl) {
        SrvAsync.invokeRPC().getMetaData(templateMdl, new AsyncCallback<DmResult<DynamicGrid_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTemplateName).show();
            }

            @Override
            public void onSuccess(DmResult<DynamicGrid_Mdl> result) {
                if (result.isStatus()) {
                    processHeaders(result);
                    filterDataList.clear();
                    filterDataList = result.getData().getFilterData_Mdls();
                    filterDataPopupShow(result.getData().getFilterData_Mdls(), templateMdl);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtTemplateName).show();
                }
            }
        });
    }

    private void filterDataPopupShow(List<FilterDataPopup_Mdl> fDList, Template_Mdl templateInfo_Mdl) {
        filterPopupData.show(fDList, templateInfo_Mdl);
    }

    private void prepareDataPopupShow() {
        prepareDataPopup.show(this, templateMdl);
    }

    private void processHeaders(DmResult<DynamicGrid_Mdl> result) {
        if (result.isStatus()) {
            DynamicGrid_Mdl data = result.getData();
            dmSmsGrd = new DmGridPaging<DmDynamicGridRow, String, SearchValue_Mdl>(null, 16, true);
            SafeHtmlBuilder sb = new SafeHtmlBuilder();
            final SmsManagerGridEdit<DmDynamicGridRow, String> gridEdit = new SmsManagerGridEdit(this.dmSmsGrd.getDmGrid());

            selectCheckBox = dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("isChecked"), "", 25, "");
            dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_To_Name"), "Send_To_Name", 150, "Send_To_Name");
            dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_To_Address"), "Send_To_Address", 150, "Send_To_Address");
            dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_To_Mobile"), "Send_To_Mobile", 100, "Send_To_Mobile");

            dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("message"), "Message", 250, "Message");
            dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("size"), "Size", 50, "Size");
            dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("status"), "Status", 80, "Status");

            for (String colMdl : data.getColumnNameList()) {
                if (!(DmShared.xNull(colMdl).trim().equalsIgnoreCase("isChecked") || DmShared.xNull(colMdl).trim().equalsIgnoreCase("uniqueId")
                        || DmShared.xNull(colMdl).trim().equalsIgnoreCase("Subcode") || DmShared.xNull(colMdl).trim().equalsIgnoreCase("Send_To_Name")
                        || DmShared.xNull(colMdl).trim().equalsIgnoreCase("Send_To_Address") || DmShared.xNull(colMdl).trim().equalsIgnoreCase("Send_To_Mobile")
                        || DmShared.xNull(colMdl).trim().equalsIgnoreCase("Message") || DmShared.xNull(colMdl).trim().equalsIgnoreCase("Size")
                        || DmShared.xNull(colMdl).trim().equalsIgnoreCase("Status") || DmShared.xNull(colMdl).trim().equalsIgnoreCase("MessageTemplate"))) {
                    dmSmsGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider(DmShared.xNull(colMdl).trim()), DmShared.xNull(colMdl).trim(), 100, DmShared.xNull(colMdl).trim());
                }
            }

            dmSmsGrd.getDmGrid().initializeGrid(new DmDynamicModelKeyProvider("uniqueId"), 1262, dmSmsGrd.getGridHeight());

            dmSmsGrd.getHelper().addSort("uniqueId", DmSortType.ASC);

            dmSmsGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<DmDynamicGridRow>() {
                @Override
                public String getColStyle(DmDynamicGridRow model, ValueProvider<? super DmDynamicGridRow, ?> valueProvider, int rowIndex, int colIndex) {
                    String styleClass = "";

                    if (colIndex != 0) {
                        if (DmShared.xNull(model.getCells().get("status").toString()).equalsIgnoreCase("Send")) {
                            styleClass = "dm-GridCellDairyCancel";
                        }
                    }

                    return styleClass;
                }

                @Override
                public String getRowStyle(DmDynamicGridRow model, int rowIndex) {
                    return "";
                }
            });

            selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            selectCheckBox.setCellPadding(false);
            selectCheckBox.setCell(new AbstractCell<String>() {
                @Override
                public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                    int val = DmShared.iNull(dmSmsGrd.getDmGrid().getGrid().getStore().get(context.getIndex()).getCells().get("isChecked")).intValue();
                    if (val == 1) {
                        sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                    } else if (val == 0) {
                        sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                    }
                }
            });

            sb.appendHtmlConstant("<div id = 'unCheckAll' onclick = 'javascript:callJSNI(event);'> "
                    + "<span id = 'unCheckAll' class = 'fa fa-square-o' "
                    + "style = 'color:black;float:left;font-size:1.3em;'></span> "
                    + "</div>");

            selectCheckBox.setHeader(sb.toSafeHtml());

            flwGrdDynamic.clear();
            flwGrdDynamic.add(dmSmsGrd);

            dmSmsGrd.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
            for (ColumnConfig col1 : dmSmsGrd.getDmGrid().getGrid().getColumnModel().getColumns()) {
                col1.setColumnHeaderClassName("dm-Garbage");
            }

            getData(gridEdit);
            setCheckUncheck(true);
        } else {
            clear();
            new DmAlertMessageBox(result.getErrorMessage(), txtTemplateName).show();
        }
    }

    private void getData(final SmsManagerGridEdit managerGridEdit) {
        managerGridEdit.addCallBack(new SmsManagerGridEdit.CallBack() {

            @Override
            public void onOk(DmDynamicGridRow ddgr, int rowIndex) {
                dmSmsGrd.getDmGrid().getGrid().getStore().get(rowIndex).getCells().put("message", ddgr.getCells().get("message").toString());
                dmSmsGrd.getDmGrid().commitChanges();
                setCheckVal(false, false, ddgr);
            }
        });
        dmSmsGrd.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, SearchValue_Mdl param) {
                SrvAsync.invokeRPC().getData(chkRecordsFilter.getValue(), param, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<DmDynamicGridRow>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtTemplateName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<DmDynamicGridRow>> result) {
                        if (result.isStatus()) {
                            dmSmsGrd.getDmGrid().getGrid().getStore().clear();
                            dmSmsGrd.setData(result.getData(), result.getPaging());
                            getCountRecords();
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtTemplateName).show();
                        }
                    }
                });
            }

            @Override
            public SearchValue_Mdl getParam() {
                return valueIntialize();
            }
        });
        dmSmsGrd.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                if (event.getColumnIndex() != 0) {
                    setHeaderValue(event.getColumnIndex());
                }
            }
        });
        dmSmsGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                final DmDynamicGridRow ddgr = dmSmsGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                if (event.getCellIndex() == 4) {
                    showGridEdit(event.getRowIndex(), managerGridEdit);
                } else if (event.getCellIndex() == 0) {
                    final int rowIndex = event.getRowIndex();
                    final int val = DmShared.vNull(dmSmsGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getCells().get("isChecked")).intValue();
                    if (DmShared.xNull(dmSmsGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getCells().get("status").toString()).equalsIgnoreCase("Send")
                            && DmShared.iNull(val).toString().equalsIgnoreCase("0")) {
                        new DmConfirmMessageBox("Sms has already been send for this record. Are you sure? You want to resend.", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                    dmSmsGrd.getDmGrid().getGrid().getStore().get(rowIndex).getCells().put("isChecked", val == 1 ? "0" : "1");
                                    setCheckVal(false, true, ddgr);
                                    int checkcount = 0;
                                    for (int i = 0; i < dmSmsGrd.getDmGrid().getGrid().getStore().size(); i++) {
                                        int val1 = DmShared.vNull(dmSmsGrd.getDmGrid().getGrid().getStore().get(i).getCells().get("isChecked")).intValue();
                                        if (val1 == 0) {
                                            checkcount++;
                                        }
                                    }
                                    if (checkcount != 0) {
                                        setCheckUncheck(false);
                                    }

                                    dmSmsGrd.getDmGrid().commitChanges();
                                }
                            }
                        }).show();

                    } else {
                        dmSmsGrd.getDmGrid().getGrid().getStore().get(rowIndex).getCells().put("isChecked", val == 1 ? "0" : "1");
                        setCheckVal(false, true, ddgr);

                        int checkcount = 0;
                        for (int i = 0; i < dmSmsGrd.getDmGrid().getGrid().getStore().size(); i++) {
                            int val1 = DmShared.vNull(dmSmsGrd.getDmGrid().getGrid().getStore().get(i).getCells().get("isChecked")).intValue();
                            if (val1 == 0) {
                                checkcount++;
                            }
                        }
                        if (checkcount != 0) {
                            setCheckUncheck(false);
                        }

                        dmSmsGrd.getDmGrid().commitChanges();

                    }

                }
            }
        });
        dmSmsGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                        showGridEdit(dmSmsGrd.getDmGrid().getGrid().getStore().indexOf(dmSmsGrd.getDmGrid().getGrid().getSelectionModel().getSelectedItem()), managerGridEdit);
                    }
                } catch (Exception e) {
                }
            }
        }, KeyDownEvent.getType());
    }

    private void setCheckVal(boolean isCheckAll, boolean isCheckChange, DmDynamicGridRow ddgr) {
        SrvAsync.invokeRPC().updateRecord(templateMdl, ddgr, isCheckAll, isCheckChange, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTemplateName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    getCountRecords();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtTemplateName).show();
                }
            }
        });
    }

    private void setCheckUncheck(boolean flag) {
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        if (flag) {
            sb.appendHtmlConstant("<div id = 'checkAll' onclick = 'javascript:callJSNI(event);'> "
                    + "<span id = 'checkAll' class = 'fa fa-check-square-o' "
                    + "style = 'color:black;float:left;font-size:1.3em;'></span> "
                    + "</div>");
        } else {
            sb.appendHtmlConstant("<div id = 'unCheckAll' onclick = 'javascript:callJSNI(event);'> "
                    + "<span id = 'unCheckAll' class = 'fa fa-square-o' "
                    + "style = 'color:black;float:left;font-size:1.3em;'></span> "
                    + "</div>");
        }
        selectCheckBox.setHeader(sb.toSafeHtml());
        dmSmsGrd.getDmGrid().getGrid().getView().refresh(true);

    }

    private void manageCheckAll(String type) {

        try {
            for (int i = 0; i < dmSmsGrd.getDmGrid().getGrid().getStore().size(); i++) {
                if (DmShared.xNull(type).equalsIgnoreCase("unCheckAll")) {
                    if (!DmShared.xNull(dmSmsGrd.getDmGrid().getGrid().getStore().get(i).getCells().get("status").toString()).equalsIgnoreCase("Send")) {
                        dmSmsGrd.getDmGrid().getGrid().getStore().get(i).getCells().put("isChecked", "1");
                    }
                } else if (DmShared.xNull(type).equalsIgnoreCase("checkAll")) {
                    dmSmsGrd.getDmGrid().getGrid().getStore().get(i).getCells().put("isChecked", "0");
                }
            }

            if (DmShared.xNull(type).equalsIgnoreCase("checkAll")) {
                setCheckUncheck(false);
                setCheckVal(false, false, (DmDynamicGridRow) null);
            } else if (DmShared.xNull(type).equalsIgnoreCase("unCheckAll")) {
                setCheckUncheck(true);
                setCheckVal(true, false, (DmDynamicGridRow) null);
            }

            dmSmsGrd.getDmGrid().commitChanges();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
        }
    }

    private void reQuery() {
        dmSmsGrd.getHelper().setRowsStartFrom(0);
        dmSmsGrd.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
        dmSmsGrd.reQuery();

    }

    private void setHeaderValue(int columnIndex) {
        if ((dmSmsGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
            searchColumnIndex = 0;
        } else if (columnIndex < 0) {
            searchColumnIndex = dmSmsGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
        } else {
            searchColumnIndex = columnIndex;
        }

        DmColumnConfig col = ((DmColumnConfig) dmSmsGrd.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
        searchDbFieldName = col.getDbFieldName();
        searchDbFieldDataType = col.getDbFieldDataType();
        dmSmsGrd.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
        dmSmsGrd.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
        txtSearchValue.setText("");
        txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
        for (ColumnConfig col1 : dmSmsGrd.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }
        col.setColumnHeaderClassName("dm-ColActiveForFilter");
        dmSmsGrd.getDmGrid().getGrid().getView().refresh(true);
        txtSearchValue.setFocus(true);
    }

    private void SendSms() {
        try {
            new DmConfirmMessageBox("Are you sure? You want to send  SMS to selected records.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        SrvAsync.invokeRPC().sendSms(templateMdl, new AsyncCallback<DmResult<String>>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                new DmAlertMessageBox(caught.getMessage(), txtTemplateName).show();
                            }

                            @Override
                            public void onSuccess(DmResult<String> result) {
                                if (result.isStatus()) {
                                    reQuery();
                                } else {
                                    topControlSendError(result.getErrorNumber(), result.getErrorMessage());
                                }
                            }
                        });
                    }
                }
            }).show();
        } catch (DmException ex) {
            topControlSendError(ex.getErrorNo(), ex.getMessage());
        }

    }

    private void topControlSendError(int errorNo, String errorMessage) {
        switch (errorNo) {

            case 100:
                new DmAlertMessageBox(errorMessage, txtTemplateName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtDataSourceName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtFunFile).show();
                break;
            default:
                if (errorNo == 401) {
                    new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                } else {
                    new DmAlertMessageBox(errorMessage, txtTemplateName).show();
                }
        }

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SmsManager.this);
                }
            }
        }).show();
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl mdl = new SearchValue_Mdl();
        mdl.setFunFile(DmShared.xNull(txtFunFile.getText()));
        mdl.setTempID(DmShared.iNull(txtTemplateName.getTag()).toString());
        mdl.setMessage(DmShared.xNull(this.templateMdl.getMessageTemplate()));
        mdl.setTableName(DmShared.xNull(this.templateMdl.getSessionTable()));
        mdl.getFilterDataList().clear();
        mdl.getFilterDataList().addAll(filterDataList);
        return mdl;
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {

        if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
            reQuery();
        }
        searchPrvValue = txtSearchValue.getText();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSend)) {
                chkRecordsFilter.setValue(true);
                dmSmsGrd.reQuery();
//                new DmConfirmMessageBox("Are you sure? You want to send SMS to selected records.", new DialogHideEvent.DialogHideHandler() {
//
//                    @Override
//                    public void onDialogHide(DialogHideEvent event) {
//                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                SendSms();
//                        }
//                    }
//                }).show();
            } else if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnDraft)) {
                prepareDataPopupShow();
            } else if (event.getSource().equals(btnClearFilter)) {
                filterPopupData.refreshFilter();
                chkRecordsFilter.setValue(false);
                dmSmsGrd.reQuery();
                setCheckUncheck(false);
            } else if (event.getSource().equals(btnFilter)) {
                if (DmShared.iNull(txtTemplateName.getTag()).longValue() != 0) {
                    filterDataPopupShow(filterDataList, templateMdl);
                } else {
                    new DmAlertMessageBox("Please select template name to filter data.", btnDraft).show();
                    return;
                }
            }
        } catch (DmException e) {
            new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
        }

    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
            manageDisplay(false);
            btnDraft.setFocus(true);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
    }

    private void getCountRecords() {
        SrvAsync.invokeRPC().getCountRecords(templateMdl, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTemplateName).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    lblDraftedRecords.setText(result.getData().getSharedKeyFieldCode());
                    lblFilteredRecords.setText(result.getData().getSharedKeyFieldName());
                    lblSelectedRecords.setText(result.getData().getSharedPreparedBy());
                    lblSentRecords.setText(result.getData().getSharedModifiedBy());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtTemplateName).show();
                }
            }
        });

    }

}
