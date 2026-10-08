package aarogya.master.frequencyMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import aarogya.master.frequencyMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.HashMap;

public final class FrequencyMaster extends FrequencyMaster_Dsg implements KeyPressHandler, BlurHandler, SelectionHandler<Item> {

    private ServiceInvoker<FrequencyMaster_SrvAsync> SrvAsync;
    private boolean flagOpenAsPopup = false;
    private final String moduleName = new pmMaster().moduleName;
    private String siteCode, siteName;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                txtFrequencyName.setFocus(true);
                userControl.setTextBlog(modifiedByBlog, "");
                userControl.setTextBlog(preparedByBlog, "");
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtFrequencyName.setFocus(true);
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
                print(false);
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
                if (flagOpenAsPopup) {
                    ppForm.hide();
                } else {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(FrequencyMaster.this);
                }
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<KeyFieldExtended>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    siteCode = result.getData().getSharedKeyFieldCode();
                    siteName = result.getData().getSharedKeyFieldName();

                    swi.setWidget(FrequencyMaster.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        txtFrequencyName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionForFrequencyName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtFrequencyName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtFrequencyName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtFrequencyName).show();
                        }
                    }
                });
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    public FrequencyMaster(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        flagOpenAsPopup = (Boolean) paramHashMap.get("flagOpenAsPopup");
        //menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(FrequencyMaster_Srv.class), FrequencyMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        manageEvent();
        if (flagOpenAsPopup) {
            ppForm.clear();
            ppForm.add(flwForm);
            ppForm.setGlassEnabled(true);
            ppForm.center();
        }
        iniDefaultValues(swi);
    }

    private void manageEvent() {
        txtTime.addKeyPressHandler(this);
        txtPresentation.addKeyPressHandler(this);
        txtTime.addBlurHandler(this);
        txtPresentation.addBlurHandler(this);
        mnuExportCard.addSelectionHandler(this);
    }

    @Override
    public Widget asWidget() {
        if (flagOpenAsPopup) {
            return ppForm;
        } else {
            return flwForm;
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void action(String nature, Object object) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        switch (DmShared.xNull(nature).trim()) {
            case "NEW":
                topControl.btnClick("New");
                break;
        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource() == txtTime) {
            int charCode = event.getCharCode();
            if ((charCode > 31) && (charCode < 48 || charCode > 57) && (charCode != 44) && (charCode != 46)) {
                txtTime.cancelKey();
            }
        } else if (event.getSource() == txtPresentation) {
            int charCode = event.getCharCode();
            if ((charCode > 31) && (charCode < 48 || charCode > 49) && (charCode != 43)) {
                txtPresentation.cancelKey();
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtTime) {
            if (!DmShared.xNull(txtTime.getText()).trim().equalsIgnoreCase("")) {
                txtTime.setText(txtTime.getText());
            }
        } else if (event.getSource() == txtPresentation) {
            if (!DmShared.xNull(txtPresentation.getText()).trim().equalsIgnoreCase("")) {
                if (!DmShared.xNull(txtPresentation.getText()).contains("+")) {
                    if (DmShared.iNull(txtPresentation.getText()).intValue() > 0) {
                        txtPresentation.setText("1");
                    } else {
                        txtPresentation.setText("0");
                    }
                }
            }

        }
    }

    private FrequencyMaster_Mdl topControlSaveInitialize() {
        FrequencyMaster_Mdl mrTemp = new FrequencyMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtFrequencyName.getText()).trim());
        mrTemp.setRegionalLanguageName(DmShared.xNull(txtRegionalName.getText()).trim());
        mrTemp.setFrequencyTime(DmShared.xNull(txtTime.getText()).trim());
        mrTemp.setPresentation(DmShared.xNull(txtPresentation.getText()).trim());
        mrTemp.setFrequencyValue(DmShared.vNull(txtValue.getText()).toString());

        return mrTemp;
    }

    private void topControlSave() {
        FrequencyMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            FrequencyMaster_Vld.chkSave(mrTemp, true);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtFrequencyName).show();
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
                new DmAlertMessageBox(errorMessage, txtFrequencyName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtPresentation).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtFrequencyName).show();
                break;
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<FrequencyMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<FrequencyMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<FrequencyMaster_Mdl> result) {
        FrequencyMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtFrequencyName.setText(data.getSharedKeyFieldName());
            txtRegionalName.setText(data.getRegionalLanguageName());
            txtTime.setText(data.getFrequencyTime());
            txtPresentation.setText(data.getPresentation());
            txtValue.setText(data.getFrequencyValue());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    userControl.setTextBlog(modifiedByBlog, "");
                    userControl.setTextBlog(preparedByBlog, "");
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void print(boolean printToPrinter) {

        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("siteCode", new FieldUDT_Mdl("site Code", siteName, siteCode));
        new OpenSourceReport("", "Master_FrequencyMaster", "", widgetName, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
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
