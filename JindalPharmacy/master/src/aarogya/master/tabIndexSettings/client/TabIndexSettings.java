package aarogya.master.tabIndexSettings.client;

import aarogya.master.tabIndexSettings.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.dmToolkit.self.shared.ViaListWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TabIndexSettings extends TabIndexSettings_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<TabIndexSettings_SrvAsync> SrvAsync;
    private final TabIndexSettingsGridEdit<GridTabIndexSettings_Mdl, GrdTabIndex_PrpAcs> gridEdit = new TabIndexSettingsGridEdit(grdTabIndex);

    public TabIndexSettings(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<>(GWT.create(TabIndexSettings_Srv.class), TabIndexSettings.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        textHelpEvents();
        handlerEvent();
        gridEvents();
        onLoad();
        findEvents();
        swi.setWidget(this);
        txtHModule.setFocus(true);
        this.getViaList();
    }

    private void textHelpEvents() {
        txtHModule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHModule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

                if (blnStatus && t1Row != null) {
                    txtHEntry.setTag("");
                    txtHEntry.setText("");
                    grdTabIndex.getGrid().getStore().clear();
                }

            }
        });
        txtHEntry.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEntry(txtHModule.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEntry).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEntry.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEntry).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtHEntry.setTag("");
                txtHEntry.setText("");
                if (blnStatus && t1Row != null) {
                    topControlMoveRec(txtHModule.getTag(), t1Row.getSharedKeyFieldName());//t1Row.getSharedKeyFieldCode());
                }
            }
        });

    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(TabIndexSettings.this);
                }
            }
        }).show();
    }

    private TabIndexSettings_Mdl topControlSaveInitialize() {
        TabIndexSettings_Mdl mrTemp = new TabIndexSettings_Mdl();
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHModule.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHModule.getText()));
        mrTemp.setMenuName(DmShared.xNull(txtHEntry.getTag()));
        grdTabIndex.commitChanges();
        mrTemp.getGrdTabIndexMdl().addAll(grdTabIndex.getGrid().getStore().getAll());
        return mrTemp;

    }

    private void topControlSave() {
        TabIndexSettings_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            TabIndexSettings_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {

                        //  topControlMoveRec(result.getData(), "");
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {

            case 100:
                new DmAlertMessageBox(errorMessage, txtHModule).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHEntry).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHModule).show();
        }

    }

    private void topControlMoveRec(final String module, final String entry) {
        SrvAsync.invokeRPC().getMoveRec(module, entry, new AsyncCallback<DmResult<TabIndexSettings_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<TabIndexSettings_Mdl> result) {
                topControlMoveRecSuccess(module, entry, result);
            }
        });
    }

    private void topControlMoveRecSuccess(String moduleName, String menuName, DmResult<TabIndexSettings_Mdl> result) {
        TabIndexSettings_Mdl data;
        if (result.isStatus()) {
            data = result.getData();

            if (!DmShared.xNull(data.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                txtHModule.setTag(moduleName);
                txtHModule.setText(moduleName);
            }
            txtHEntry.setTag(menuName);
            txtHEntry.setText(menuName);

            grdTabIndex.getGrid().getStore().clear();

            getExpectedWidget(moduleName, menuName, data.getGrdTabIndexMdl());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        txtHModule.setFocus(true);
    }

    private void getExpectedWidget(final String moduleName, final String menuName, final List<GridTabIndexSettings_Mdl> fetchedRecords) {
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(moduleName, menuName, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(IsWidget wgtObject) {
                if (wgtObject instanceof IsWidgetExtended) {
                    fillIndexedRecords(((IsWidgetExtended) wgtObject).getViaList(), fetchedRecords);
                }
            }
        });
    }

    private void fillIndexedRecords(DmClient.ViaList loadedWidgetViaList, List<GridTabIndexSettings_Mdl> fetchedRecords) {
        List<GridTabIndexSettings_Mdl> listGridModels = new ArrayList<>();

        for (int i = 0; i < loadedWidgetViaList.getViaListWidget().size(); i++) {
            ViaListWidget listWidget = loadedWidgetViaList.getViaListWidget().get(i);
            GridTabIndexSettings_Mdl gridModel = new GridTabIndexSettings_Mdl();
            gridModel.setSharedKeyFieldCode(listWidget.getName());
            gridModel.setSharedKeyFieldName(listWidget.getName());
            gridModel.setSerialNo(DmShared.iNull(i + 1).toString());
            for (int c = 0; c < fetchedRecords.size(); c++) {
                if (DmShared.xNull(listWidget.getName()).equalsIgnoreCase(fetchedRecords.get(c).getSharedKeyFieldCode())) {
                    gridModel.setSerialNo(fetchedRecords.get(c).getSerialNo());
                }
            }
            listGridModels.add(gridModel);
        }
        sortTabIndexings(listGridModels);
    }

    private void sortTabIndexings(final List<GridTabIndexSettings_Mdl> tabIndexRecords) {
        Collections.sort(tabIndexRecords, new Comparator<GridTabIndexSettings_Mdl>() {
            @Override
            public int compare(GridTabIndexSettings_Mdl o1, GridTabIndexSettings_Mdl o2) {
                int rValue = -1;

                String o1SerialString = DmShared.xNull(o1.getSerialNo()).trim();
                String o2SerialString = DmShared.xNull(o2.getSerialNo()).trim();

                if (o1SerialString.equals("")) {
                    o1SerialString = String.valueOf(tabIndexRecords.size() + 1);
                }

                if (o2SerialString.equals("")) {
                    o2SerialString = String.valueOf(tabIndexRecords.size() + 1);
                }

                int o1Serial = DmShared.iNull(o1SerialString).intValue();
                int o2Serial = DmShared.iNull(o2SerialString).intValue();

                if (o1Serial < o2Serial) {
                    rValue = -1;
                } else if (o1Serial == o2Serial) {
                    rValue = 0;
                } else {
                    rValue = 1;
                }
                return rValue;
            }
        });
        grdTabIndex.getGrid().getStore().clear();
        grdTabIndex.getGrid().getStore().addAll(tabIndexRecords);
        grdTabIndex.commitChanges();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void findEvents() {

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    private void gridEvents() {
        grdTabIndex.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGrid(event.getRowIndex());

            }
        });
        grdTabIndex.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    int row = 0;
                    row = grdTabIndex.getGrid().getStore().indexOf(grdTabIndex.getGrid().getSelectionModel().getSelectedItem());
                    showGrid(row);
                }
            }
        }, KeyDownEvent.getType());
        grdTabIndex.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGrid(event.getRowIndex());

            }
        });
        gridEdit.addCallBack(new TabIndexSettingsGridEdit.CallBack() {

            @Override
            public void onOk(GridTabIndexSettings_Mdl eduquali) {
                List<GridTabIndexSettings_Mdl> tempList = new ArrayList<>();
                tempList.addAll(grdTabIndex.getGrid().getStore().getAll());
                sortTabIndexings(tempList);
            }

            @Override
            public void onDelete() {
                grdTabIndex.commitChanges();
            }
        });
    }

    private void showGrid(int index) {
        gridEdit.show(index, true, false, false);
    }

    private void onLoad() {
        grdTabIndex.getGrid().getStore().clear();

    }

    @Override
    public void searchRecord(String key) {

    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource() == btnClose) {
                onClose();
            } else if (event.getSource() == btnSave) {
                topControlSave();
            } else if (event.getSource() == btnReset) {
                topControlDelete();
            } else if (event.getSource().equals(btnExport)) {
                new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportCSV();
                        }
                    }
                }).show();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        }
    }

    private void handlerEvent() {
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnReset.addClickHandler(this);
        btnExport.addClickHandler(this);
        flwForm.addAttachHandler(this);
    }

    private void topControlDelete() {
        SrvAsync.invokeRPC().deleteRecord(txtHModule.getTag(), txtHEntry.getTag(), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlMoveRec(txtHModule.getTag(), txtHEntry.getTag());
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void exportCSV() {
        SrvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
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

}
