package dataman.webJobs.notificationEntry.client;

import dataman.dmBase.self.shared.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.webJobs.notificationEntry.shared.Find_Mdl;
import dataman.webJobs.notificationEntry.shared.NotificationEntry_Mdl;
import dataman.webJobs.notificationEntry.shared.NotificationEntry_Srv;
import dataman.webJobs.notificationEntry.shared.NotificationEntry_SrvAsync;
import dataman.webJobs.notificationEntry.shared.NotificationEntry_Vld;

public class NotificationEntry extends NotificationEntry_Dsg implements SelectionHandler<Item>, AttachEvent.Handler, BlurHandler {

    private final ServiceInvoker<NotificationEntry_SrvAsync> srvAsync;
    private final ManageSite manageSite = new ManageSite();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtNotification.setFocus(true);
                iniDefault();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtNotification.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(NotificationEntry.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                // menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void iniDefault() {

        srvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtEntryDate).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtEntryDate.setText(result[0]);
                txtExpiryDate.setText(result[0]);
            }
        });

    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    public NotificationEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(NotificationEntry_Srv.class), NotificationEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
       
        findEvents();
        // mnuManageSite.addSelectionHandler(this);
        //  mnuExportCard.addSelectionHandler(this);
        txtEntryDate.addBlurHandler(this);
        txtExpiryDate.addBlurHandler(this);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private NotificationEntry_Mdl topControlSaveInitialize() {
        NotificationEntry_Mdl mrTemp = new NotificationEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtNotification.getText()));
        mrTemp.setEntryDate(DmShared.xNull(txtEntryDate.getText()));
        mrTemp.setExpiryDate(DmShared.xNull(txtExpiryDate.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        NotificationEntry_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            NotificationEntry_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtNotification).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
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
                new DmAlertMessageBox(errorMessage, txtNotification).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtEntryDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtExpiryDate).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtNotification).show();
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<NotificationEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<NotificationEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<NotificationEntry_Mdl> result) {
        NotificationEntry_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtNotification.setText(data.getSharedKeyFieldName());
            txtEntryDate.setText(data.getEntryDate());
            txtExpiryDate.setText(data.getExpiryDate());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
//        if (event.getSource().equals(mnuManageSite)) {
//            if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
//                manageSite.show("subGroupSiteList", "subCode", topControl.getDocId(), "site_Code");
//            } else {
//                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtDepartmentName).show();
//            }
//        } else if (event.getSource().equals(mnuExportCard)) {
//            new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {
//
//                @Override
//                public void onDialogHide(DialogHideEvent event) {
//                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
//                        exportCSV();
//                    }
//                }
//            }).show();
//        }
    }

    private void exportCSV() {
//        srvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<KeyFieldExtended> result) {
//                if (result.isStatus()) {
//                    KeyFieldExtended field = result.getData();
//                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
//                }
//            }
//        });
    }

    private void startDownloading(String fileName, String filePath) {
//        try {
//            HashMap<String, String> params = new HashMap<>();
//            params.put("u", DmRequestBuilder.getSessionId());
//            params.put("f", fileName);
//            params.put("p", filePath);
//            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
//        } catch (Exception ex) {
//        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            manageSite.hide();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtEntryDate)) {
            if (!DmShared.xNull(txtEntryDate.getText()).trim().equalsIgnoreCase("")) {
                txtEntryDate.setText(DmClient.returnDate(txtEntryDate.getText()));
            }
        }
        if (event.getSource().equals(txtExpiryDate)) {
            if (!DmShared.xNull(txtExpiryDate.getText()).trim().equalsIgnoreCase("")) {
                txtExpiryDate.setText(DmClient.returnDate(txtExpiryDate.getText()));
            }
        }
    }
}
