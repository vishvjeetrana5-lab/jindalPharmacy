package dataman.dmToolkit.singleLineMaster.client;

import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import dataman.dmToolkit.singleLineMaster.shared.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.singleLineMaster.shared.SingleLineMaster_SrvAsync;
import java.util.List;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.HashMap;

public class SingleLineMaster extends SingleLineMaster_Dsg implements SelectionHandler<Item> {

    private final ServiceInvoker<SingleLineMaster_SrvAsync> srvAsync;
    private SingleLineMaster_Mdl singleLineMaster;
    private boolean isPopup = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);
        topControl.setDisplay(true);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                userControl.setTextBlog(preparedByBlog, "");
                userControl.setTextBlog(modifiedByBlog, "");
                txtField.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtField.setFocus(true);
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
                if (isPopup) {
                    SingleLineMaster.this.pnlForm.hide();
                } else {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SingleLineMaster.this);
                }
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });
    }

    private void textHelpEvents() {

        txtField.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, singleLineMaster, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtField).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtField.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtField).show();
                        }
                    }
                });
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, singleLineMaster, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    public SingleLineMaster(String pageTitle, String tableName, String fieldTitle, String fieldCode, String fieldText, int maxLength, boolean haveSiteList, boolean haveSiteCode, SQLDataType fieldCodeDataType, UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<SingleLineMaster_SrvAsync>(GWT.create(SingleLineMaster_Srv.class), SingleLineMaster.class.getName());
        startUp(pageTitle, tableName, fieldTitle, fieldCode, fieldText, maxLength, haveSiteList, haveSiteCode, fieldCodeDataType, menuPermission, null, swi);
    }

    public SingleLineMaster(String pageTitle, String tableName, String fieldTitle, String fieldCode, String fieldText, int maxLength, boolean haveSiteList, boolean haveSiteCode, SQLDataType fieldCodeDataType, UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<SingleLineMaster_SrvAsync>(GWT.create(SingleLineMaster_Srv.class), SingleLineMaster.class.getName());
        startUp(pageTitle, tableName, fieldTitle, fieldCode, fieldText, maxLength, haveSiteList, haveSiteCode, fieldCodeDataType, menuPermission, paramHashMap, swi);
    }

    private void startUp(String pageTitle, String tableName, String fieldTitle, String fieldCode, String fieldText, int maxLength, boolean haveSiteList, boolean haveSiteCode, SQLDataType fieldCodeDataType, UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        if (paramHashMap != null) {
            if (paramHashMap.containsKey("asPopup")) {
                isPopup = ((Boolean) paramHashMap.get("asPopup"));
            }
        }
        this.asWidget().setTitle(pageTitle);
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        topControlEvents(menuPermission);
        mnuExportCard.addSelectionHandler(this);
        textHelpEvents();
        findEvents();
        lblField.setText(fieldTitle);
        txtField.setMaxLength(maxLength);

        if (isPopup) {
            this.pnlForm.clear();
            pnlForm.add(flwForm);
            pnlForm.setGlassEnabled(true);
            pnlForm.center();
        }

        SingleLineMaster_Mdl singleLineMaster = new SingleLineMaster_Mdl();
        singleLineMaster.setFieldTitle(fieldTitle);
        singleLineMaster.setTableName(tableName);
        singleLineMaster.setFieldCode(fieldCode);
        singleLineMaster.setFieldText(fieldText);
        singleLineMaster.setHaveSiteList(haveSiteList);
        singleLineMaster.setHaveSiteCode(haveSiteCode);
        singleLineMaster.setFieldCodeDataType(fieldCodeDataType);
        singleLineMaster.setFileName(pageTitle.trim());
        this.singleLineMaster = singleLineMaster;
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        if (isPopup) {
            return pnlForm;
        } else {
            return flwForm;
        }
    }

    private KeyFieldExtended topControlSaveInitialize() {
        KeyFieldExtended mrTemp = new KeyFieldExtended();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtField.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        KeyFieldExtended keyField;

        try {
            keyField = topControlSaveInitialize();
            SingleLineMaster_Vld.chkSave(keyField, singleLineMaster);

            srvAsync.invokeRPC().saveRecord(singleLineMaster, keyField, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtField).show();
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
                        new DmAlertMessageBox(result.getErrorMessage(), txtField).show();
                    }
                }
            });
        } catch (DmException e) {
            new DmAlertMessageBox(e.getMessage(), txtField).show();
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(singleLineMaster, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtField).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    topControl.setNavigator(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtField).show();
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(singleLineMaster, strKey, new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtField).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<KeyFieldExtended> result) {
        KeyFieldExtended data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtField.setText(data.getSharedKeyFieldName());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), txtField).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(singleLineMaster, strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtField).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtField).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuExportCard)) {
            new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        exportCSV();
                    }
                }
            }).show();
        }
    }

    private void exportCSV() {
        srvAsync.invokeRPC().exportCSV(singleLineMaster, DmRequestBuilder.getSessionId(), this.asWidget().getTitle(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
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
