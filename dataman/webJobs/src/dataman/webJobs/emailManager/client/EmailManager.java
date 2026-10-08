package dataman.webJobs.emailManager.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.webJobs.emailManager.shared.*;
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
import dataman.dmBase.dmFileManager.shared.DmFile_Mdl;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmGrid.client.DmColumnConfig;
import dataman.dmToolkit.dmGrid.client.DmDynamicModelKeyProvider;
import dataman.dmToolkit.dmGrid.client.DmDynamicValueProvider;
import dataman.dmBase.self.shared.DmDynamicGridRow;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.self.client.JSNICaller;
import java.util.ArrayList;
import java.util.List;

public final class EmailManager extends EmailManager_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, KeyUpHandler {

    ServiceInvoker<EmailManager_SrvAsync> SrvAsync;
    private int searchColumnIndex;
    private String searchDbFieldName, searchPrvValue = "";
    private DmDataType searchDbFieldDataType = DmDataType.STRING;
    private Template_Mdl templateMdl;
    private DmFile_Mdl file;
    private List<FilterDataPopup_Mdl> filterDataList = new ArrayList<>();
    private final FilterDataPopup filterPopupData = new FilterDataPopup();
    private final DraftPopup prepareDataPopup = new DraftPopup();
    private DmColumnConfig<DmDynamicGridRow, String> selectCheckBox = null;

    public EmailManager(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(EmailManager_Srv.class), EmailManager.class.getName());
        this.asWidget().setTitle(widgetName);
        textHelpEvents();
        handlerEvents();
        swi.setWidget(this);

    }

    private void handlerEvents() {
        btnClose.addClickHandler(this);
        btnSend.addClickHandler(this);
        btnFilter.addClickHandler(this);
        btnDraft.addClickHandler(this);
        btnClearFilter.addClickHandler(this);
        btnClearAttachment.addClickHandler(this);
        flwForm.addAttachHandler(this);
        txtSearchValue.addKeyUpHandler(this);
    }

    private void manageDisplay(boolean flag) {
        txtDataSourceName.setEnabled(false);
        txtFunFile.setEnabled(false);
        txtTemplateName.setEnabled(false);
        txtUploadFile.setEnabled(false);
        txtSearchValue.setEnabled(flag);
    }

    private void clear() {
        templateMdl = null;
        file = null;
        txtDataSourceName.setTag("");
        txtDataSourceName.setTag2nd("");
        txtDataSourceName.setText("");
        txtFunFile.setTag("");
        txtFunFile.setTag2nd("");
        txtFunFile.setText("");
        txtSearchValue.setText("");
        txtSearchValue.setTag("");
        txtSearchValue.setTag2nd("");
        txtUploadFile.setTag("");
        txtUploadFile.setTag2nd("");
        txtUploadFile.setText("");
        flwGrdDynamic.clear();
        lblDraftedRecords.setText("0");
        lblFilteredRecords.setText("0");
        lblSelectedRecords.setText("0");
        lblSentRecords.setText("0");

        manageDisplay(false);
    }

    private void textHelpEvents() {
        filterPopupData.addCallBack(new FilterDataPopup.CallBack() {
            @Override
            public void onOk(List<FilterDataPopup_Mdl> listFilterData) {
                filterDataList = listFilterData;
                dmEmailGrd.reQuery();
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
                EmailManager.this.templateMdl = template_Mdl;
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
                dmEmailGrd.reQuery();
            }
        });

    }

    private void getMetadata(final Template_Mdl templateInfo_Mdl) {
        SrvAsync.invokeRPC().getMetaData(templateInfo_Mdl, new AsyncCallback<DmResult<DynamicGrid_Mdl>>() {
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
                    filterDataPopupShow(result.getData().getFilterData_Mdls(), templateInfo_Mdl);

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
        String templateCode = "", templateName = "";
        if (templateMdl != null) {
            templateCode = DmShared.xNull(templateMdl.getSharedKeyFieldCode());
            templateName = DmShared.xNull(templateMdl.getSharedKeyFieldName());
        }
        prepareDataPopup.show(this, templateCode, templateName);
    }

    private void processHeaders(DmResult<DynamicGrid_Mdl> result) {
        DynamicGrid_Mdl data = result.getData();
        dmEmailGrd = new DmGridPaging<>(null, 16, true);
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        final MessageGridEdit<DmDynamicGridRow, String> gridEdit = new MessageGridEdit(dmEmailGrd.getDmGrid());

        selectCheckBox = dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("isChecked"), "", 25, "");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_To_Name"), "Send_To_Name", 100, "send_To_Name");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_To_Address"), "Send_To_Address", 100, "send_To_Address");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_To_Email"), "Send_To_Email", 100, "send_To_Email");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_BCc_Email"), "Send_BCc_EMail", 100, "send_BCc_Email");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("send_Cc_Email"), "Send_Cc_EMail", 100, "send_Cc_EMail");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("subject"), "Subject", 100, "subject");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("message"), "Message", 200, "message");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("size"), "Size", 50, "size");
        dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider("status"), "Status", 100, "status");

        for (String colMdl : data.getColumnNameList()) {
            if (!(colMdl.trim().equalsIgnoreCase("isChecked") || colMdl.trim().equalsIgnoreCase("uniqueId")
                    || colMdl.trim().equalsIgnoreCase("Subcode") || colMdl.trim().equalsIgnoreCase("send_To_Name")
                    || colMdl.trim().equalsIgnoreCase("send_To_Address") || colMdl.trim().equalsIgnoreCase("send_To_Email")
                    || colMdl.trim().equalsIgnoreCase("send_BCc_Email") || colMdl.trim().equalsIgnoreCase("Send_Cc_EMail")
                    || colMdl.trim().equalsIgnoreCase("subject") || colMdl.trim().equalsIgnoreCase("Message")
                    || colMdl.trim().equalsIgnoreCase("Size") || colMdl.trim().equalsIgnoreCase("formatsub")
                    || colMdl.trim().equalsIgnoreCase("formatMsg") || colMdl.trim().equalsIgnoreCase("Status"))) {
                dmEmailGrd.getDmGrid().addTextColumn(new DmDynamicValueProvider(colMdl.trim()), colMdl.trim(), 100, colMdl.trim());
            }
        }
        dmEmailGrd.getDmGrid().initializeGrid(new DmDynamicModelKeyProvider("uniqueId"), 1262, 372);

        dmEmailGrd.getHelper().addSort("uniqueId", DmSortType.ASC);
        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);
        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                try {
                    int val = DmShared.vNull(dmEmailGrd.getDmGrid().getGrid().getStore().get(context.getIndex()).getCells().get("isChecked")).intValue();
                    if (val == 1) {
                        sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                    } else if (val == 0) {
                        sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), txtTemplateName).show();
                }
            }
        });

        dmEmailGrd.getDmGrid().getGrid().getView().setViewConfig(new GridViewConfig<DmDynamicGridRow>() {
            @Override
            public String getColStyle(DmDynamicGridRow model, ValueProvider<? super DmDynamicGridRow, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = "";

                if (DmShared.xNull(model.getCells().get("status").toString()).equalsIgnoreCase("Send")) {
                    styleClass = ("dm-GridCellDairyCancel");
                }

                return styleClass;
            }

            @Override
            public String getRowStyle(DmDynamicGridRow model, int rowIndex) {
                return "";
            }
        });

        sb.appendHtmlConstant("<div id = 'unCheckAll' onclick = 'javascript:callJSNI(event);'> "
                + "<span id = 'unCheckAll' class = 'fa fa-square-o' "
                + "style = 'color:black;float:left;font-size:1.3em;'></span> "
                + "</div>");

        selectCheckBox.setHeader(sb.toSafeHtml());

        flwGrdDynamic.clear();
        flwGrdDynamic.add(dmEmailGrd);

        dmEmailGrd.getHelper().addFilter(searchDbFieldName, searchDbFieldDataType, DmOperator.BEGINSWITH, "", null, DmOperand.AND);
        for (ColumnConfig col1 : dmEmailGrd.getDmGrid().getGrid().getColumnModel().getColumns()) {
            col1.setColumnHeaderClassName("dm-Garbage");
        }

        getData(gridEdit);
        setCheckUncheck(true);
    }

    private void getData(final MessageGridEdit gridEdit) {
        gridEdit.addCallBack(new MessageGridEdit.CallBack() {

            @Override
            public void onOk(DmDynamicGridRow ddgr, int rowIndex) {
                dmEmailGrd.getDmGrid().getGrid().getStore().get(rowIndex).getCells().put("message", ddgr.getCells().get("message").toString());
                dmEmailGrd.getDmGrid().getGrid().getStore().get(rowIndex).getCells().put("subject", ddgr.getCells().get("subject").toString());
                dmEmailGrd.getDmGrid().commitChanges();
                setCheckVal(false, false, ddgr);
            }
        });
        dmEmailGrd.addCallBack(new DmGridPaging.CallBack<SearchValue_Mdl>() {
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
                            dmEmailGrd.getDmGrid().getGrid().getStore().clear();
                            dmEmailGrd.setData(result.getData(), result.getPaging());
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
        dmEmailGrd.getDmGrid().getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                try {
                    if (event.getColumnIndex() != 0) {
                        setHeaderValue(event.getColumnIndex());
                    }

                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
                }

            }
        });
        dmEmailGrd.getDmGrid().getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    final DmDynamicGridRow ddgr = dmEmailGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex());
                    if (event.getCellIndex() == 6 || event.getCellIndex() == 7) {
                        gridEdit.show(event.getRowIndex(), true, event.getCellIndex());
                    } else if (event.getCellIndex() == 0) {
                        final int rowIndex = event.getRowIndex();
                        final int val = DmShared.vNull(dmEmailGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getCells().get("isChecked")).intValue();
                        if (DmShared.xNull(dmEmailGrd.getDmGrid().getGrid().getStore().get(event.getRowIndex()).getCells().get("status").toString()).equalsIgnoreCase("Send")
                                && DmShared.iNull(val).toString().equalsIgnoreCase("0")) {
                            new DmConfirmMessageBox("Email has already been send for this record. Are you sure? You want to resend.", new DialogHideEvent.DialogHideHandler() {

                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                        dmEmailGrd.getDmGrid().getGrid().getStore().get(rowIndex).getCells().put("isChecked", val == 1 ? "0" : "1");
                                        setCheckVal(false, true, ddgr);
                                        int checkcount = 0;
                                        for (int i = 0; i < dmEmailGrd.getDmGrid().getGrid().getStore().size(); i++) {
                                            int val1 = DmShared.vNull(dmEmailGrd.getDmGrid().getGrid().getStore().get(i).getCells().get("isChecked")).intValue();
                                            if (val1 == 0) {
                                                checkcount++;
                                            }
                                        }
                                        if (checkcount != 0) {
                                            setCheckUncheck(false);
                                        }
                                        dmEmailGrd.getDmGrid().commitChanges();
                                    }
                                }
                            }).show();

                        } else {
                            dmEmailGrd.getDmGrid().getGrid().getStore().get(rowIndex).getCells().put("isChecked", val == 1 ? "0" : "1");
                            setCheckVal(false, true, ddgr);
                            int checkcount = 0;
                            for (int i = 0; i < dmEmailGrd.getDmGrid().getGrid().getStore().size(); i++) {
                                int val1 = DmShared.vNull(dmEmailGrd.getDmGrid().getGrid().getStore().get(i).getCells().get("isChecked")).intValue();
                                if (val1 == 0) {
                                    checkcount++;
                                }
                            }
                            if (checkcount != 0) {
                                setCheckUncheck(false);
                            }
                            dmEmailGrd.getDmGrid().commitChanges();
                        }

                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
                }
            }
        });
        dmEmailGrd.getDmGrid().getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                try {
                    if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
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
        dmEmailGrd.getDmGrid().getGrid().getView().refresh(true);

    }

    private void manageCheckAll(String type) {

        try {
            for (int i = 0; i < dmEmailGrd.getDmGrid().getGrid().getStore().size(); i++) {
                if (DmShared.xNull(type).equalsIgnoreCase("unCheckAll")) {
                    if (!DmShared.xNull(dmEmailGrd.getDmGrid().getGrid().getStore().get(i).getCells().get("status").toString()).equalsIgnoreCase("Send")) {
                        dmEmailGrd.getDmGrid().getGrid().getStore().get(i).getCells().put("isChecked", "1");
                    }
                } else if (DmShared.xNull(type).equalsIgnoreCase("checkAll")) {
                    dmEmailGrd.getDmGrid().getGrid().getStore().get(i).getCells().put("isChecked", "0");
                }
            }

            if (DmShared.xNull(type).equalsIgnoreCase("checkAll")) {
                setCheckUncheck(false);
                setCheckVal(false, false, (DmDynamicGridRow) null);
            } else if (DmShared.xNull(type).equalsIgnoreCase("unCheckAll")) {
                setCheckUncheck(true);
                setCheckVal(true, false, (DmDynamicGridRow) null);
            }

            dmEmailGrd.getDmGrid().commitChanges();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
        }
    }

    private void reQuery() {
        try {
            dmEmailGrd.getHelper().setRowsStartFrom(0);
            dmEmailGrd.getHelper().getFilter().get(0).setValue1(txtSearchValue.getText());
            dmEmailGrd.reQuery();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
        }
    }

    private void sendEmail() {
        if (file == null) {
            file = new DmFile_Mdl();
        }
        SrvAsync.invokeRPC().sendEmail(templateMdl, file, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtTemplateName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    reQuery();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtTemplateName).show();
                }
            }
        });
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EmailManager.this);
                }
            }
        }).show();
    }

    private SearchValue_Mdl valueIntialize() {
        SearchValue_Mdl mdl = new SearchValue_Mdl();
        mdl.setFunOrFileName(DmShared.xNull(txtFunFile.getText()));
        mdl.setTemplateId(DmShared.iNull(txtTemplateName.getTag()).toString());
        mdl.setMessageTemplate(DmShared.xNull(templateMdl.getMessageTemplate()));
        mdl.setSubjectTemplate(DmShared.xNull(templateMdl.getSubjectTemplate()));
        mdl.setSessionTable(DmShared.xNull(templateMdl.getSessionTable()));
        mdl.getFilterDataList().clear();
        mdl.getFilterDataList().addAll(filterDataList);
        return mdl;
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getSource().equals(txtSearchValue)) {
            try {
                if (!searchPrvValue.equalsIgnoreCase(txtSearchValue.getText())) {
                    reQuery();
                }
                searchPrvValue = txtSearchValue.getText();
            } catch (Exception e) {
                new DmAlertMessageBox(e.getMessage(), txtSearchValue).show();
            }
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void setHeaderValue(int columnIndex) {
        try {
            if ((dmEmailGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1) < columnIndex) {
                searchColumnIndex = 0;
            } else if (columnIndex < 0) {
                searchColumnIndex = dmEmailGrd.getDmGrid().getGrid().getColumnModel().getColumnCount() - 1;
            } else {
                searchColumnIndex = columnIndex;
            }

            DmColumnConfig col = ((DmColumnConfig) dmEmailGrd.getDmGrid().getGrid().getColumnModel().getColumn(searchColumnIndex));
            searchDbFieldName = col.getDbFieldName();
            searchDbFieldDataType = col.getDbFieldDataType();
            dmEmailGrd.getHelper().getFilter().get(0).setColumnName(searchDbFieldName);
            dmEmailGrd.getHelper().getFilter().get(0).setDataType(searchDbFieldDataType);
            txtSearchValue.setText("");
            txtSearchValue.getElement().setPropertyString("placeholder", col.getHeader().asString());
            for (ColumnConfig col1 : dmEmailGrd.getDmGrid().getGrid().getColumnModel().getColumns()) {
                col1.setColumnHeaderClassName("dm-Garbage");
            }
            col.setColumnHeaderClassName("dm-ColActiveForFilter");
            dmEmailGrd.getDmGrid().getGrid().getView().refresh(true);
            txtSearchValue.setFocus(true);
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSend)) {
                chkRecordsFilter.setValue(true);
                dmEmailGrd.reQuery();
                new DmConfirmMessageBox("Are you sure? You want to send Email to selected records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            sendEmail();
                        }
                    }
                }).show();
            } else if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(btnDraft)) {
                prepareDataPopupShow();
            } else if (event.getSource().equals(btnClearFilter)) {
                filterPopupData.refreshFilter();
                chkRecordsFilter.setValue(false);
                dmEmailGrd.reQuery();
                setCheckUncheck(false);
            } else if (event.getSource().equals(btnFilter)) {
                if (DmShared.iNull(txtTemplateName.getTag()).longValue() != 0) {
                    filterDataPopupShow(filterDataList, templateMdl);
                } else {
                    new DmAlertMessageBox("Please select template name to filter data.", btnDraft).show();
                    return;
                }
            } else if (event.getSource().equals(btnClearAttachment)) {
                new DmConfirmMessageBox("Are you sure? You want to clear the Attachment.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            txtUploadFile.setTag("");
                            txtUploadFile.setText("");
                            txtUploadFile.setTag2nd("");
                            EmailManager.this.file = null;
                        }
                    }
                }).show();

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

    @Override
    public void notifyForUpload(String notifier) {
        try {

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtTemplateName).show();
        }
    }

    @Override
    public void processAfterUploaded(DmIFile_Mdl file) {
        try {
            txtUploadFile.setTag(file.getServerFileName());
            txtUploadFile.setText(file.getFileName());
            txtUploadFile.setTag2nd(file.getServerDirectory());
            EmailManager.this.file = (DmFile_Mdl) file;
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), txtTemplateName).show();
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
