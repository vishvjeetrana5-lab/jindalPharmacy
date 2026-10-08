package aarogya.master.serviceMaster.client;

import aarogya.master.serviceMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.STEnum.ServiceNature;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import java.util.HashMap;
import java.util.List;

public final class ServiceMaster extends ServiceMaster_Dsg implements ClickHandler, AttachEvent.Handler, SelectionHandler<Item> {

    private final ServiceInvoker<ServiceMaster_SrvAsync> srvAsync;
    private DefaultValue_Mdl defaultValue;
    private final ManageSite manageSite = new ManageSite();
    boolean serviceFlag = false;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {

            @Override
            public void editClicked() {
                manageDisplay(true);
                if (DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("CF")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("VC")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("RG")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("ADJ")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("PA")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("DGN")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("PRM")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("ACDC")) {

                    txtName.setEnabled(false);
                    txtShortName.setEnabled(false);
                    txtPrntSerial.setFocus(true);
                } else {
                    txtName.setFocus(true);
                    txtHServiceRoutine.setEnabled(ServiceNature.getValueOf(txtHServiceType.getText()) == ServiceNature.SERVICE);
                    if (ServiceNature.getValueOf(txtHServiceType.getText()) == ServiceNature.SERVICE) {
                        txtHServiceRoutine.setText("As Applicable");
                        txtHServiceRoutine.setTag("0");
                    }
                }
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("CF")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("VC")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("RG")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("ADJ")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("PA")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("DGN")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("PRM")
                        || DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("ACDC")) {

                } else {
                    new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                topControlDelete(topControl.getDocId());
                            }
                        }
                    }).show();
                }
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
        topControl.addOtherHandeller(new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ServiceMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                        }
                    }
                });
            }
        });
        txtHCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCategory_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCategory_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCategory_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCategory_Mdl tRow) {

            }
        });
        txtHGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });
        txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHNightShift.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNightShift).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNightShift.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNightShift).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    if (DmShared.iNull(txtHNightShift.getTag()).longValue() == 1) {
                        txtAmount.setEnabled(true);
                        txtHType.setEnabled(true);
                        txtHType.setTag("0");
                        txtHType.setText("%");

                    } else {
                        txtAmount.setEnabled(false);
                        txtHType.setEnabled(false);
                        txtAmount.setText("");
                        txtHType.setText("");

                    }
                }
            }
        });
        txtHDiscount.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                txtHDiscount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

                    @Override
                    public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                        srvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                new DmAlertMessageBox(caught.getMessage(), txtHDiscount).show();
                            }

                            @Override
                            public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                                if (result.isStatus()) {
                                    txtHDiscount.setData(result.getData(), result.getPaging());
                                } else {
                                    new DmAlertMessageBox(result.getErrorMessage(), txtHDiscount).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                    }
                });
            }
        });
        txtHServiceRoutine.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHServiceRoutine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceRoutine).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceRoutine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceRoutine).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });
        txtHRateEditable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateEditable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRateEditable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateEditable).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHLinkDept.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLinkDept(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkDept).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLinkDept.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLinkDept).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHServiceType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHServiceType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHServiceRoutine.setEnabled(ServiceNature.getValueOf(t1Row.getSharedKeyFieldName()) == ServiceNature.SERVICE);

                    if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).trim().equalsIgnoreCase("2")) {
                        txtHCategory.setText(defaultValue.getDiagnosticCatName());
                        txtHCategory.setTag(defaultValue.getDiagnosticCatCode());
                        txtHCategory.setEnabled(false);
                    } else {
                        txtHCategory.setEnabled(true);
                    }
                    if (ServiceNature.getValueOf(t1Row.getSharedKeyFieldName()) == ServiceNature.SERVICE) {
                        txtHServiceRoutine.setText("As Applicable");
                        txtHServiceRoutine.setTag("0");
                    }

                }
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

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    public ServiceMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(ServiceMaster_Srv.class), ServiceMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        btnApplicable.addClickHandler(this);
        flwForm.addAttachHandler(this);
        manageSiteMenu.addSelectionHandler(this);
        mnuExportCard.addSelectionHandler(this);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        iniDefaultValue(swi);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnApplicable) {
            showOrganizationMaster();
        }
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    swi.setWidget(ServiceMaster.this);
                    manageDisplay(false);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private ServiceMaster_Mdl topControlSaveInitialize() {
        ServiceMaster_Mdl mrTemp = new ServiceMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setCategoryCode(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setCategoryName(DmShared.xNull(txtHCategory.getText()));
        mrTemp.setGroupCode(DmShared.xNull(txtHGroup.getTag()));
        mrTemp.setGroupName(DmShared.xNull(txtHGroup.getText()));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setActiveText(DmShared.xNull(txtHActive.getText()));
        mrTemp.setIsDiscountAllowTag(DmShared.xNull(txtHDiscount.getTag()));
        mrTemp.setRoutineTag(DmShared.xNull(txtHServiceRoutine.getTag()));
        mrTemp.setRoutineText(DmShared.xNull(txtHServiceRoutine.getText()));
        mrTemp.setPrnSrNo(DmClient.vNull(txtPrntSerial.getText(), "0"));
        mrTemp.setOrganizationCode(DmShared.xNull(defaultValue.getOrganizationCode()));
        mrTemp.setRateEditableTag(DmShared.xNull(txtHRateEditable.getTag()));
        mrTemp.setRateEditableText(DmShared.xNull(txtHRateEditable.getText()));
        mrTemp.setNightShiftTag(DmShared.xNull(txtHNightShift.getTag()));
        mrTemp.setNightShiftAmount(DmShared.xNull(txtAmount.getText()));
        mrTemp.setLinkDepartmentCode(DmShared.xNull(txtHLinkDept.getTag()));
        mrTemp.setTypeCode(DmShared.xNull(txtHType.getTag()));
        mrTemp.setTypeName(DmShared.xNull(txtHType.getText()));

        mrTemp.setServiceNatureCode(DmShared.xNull(txtHServiceType.getTag()));
        mrTemp.setServiceNatureName(DmShared.xNull(txtHServiceType.getText()));
        return mrTemp;
    }

    private void topControlSave() {
        ServiceMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ServiceMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
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
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtShortName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDiscount).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHCategory).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHServiceRoutine).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHGroup).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHNightShift).show();
                break;
            case 750:
                new DmAlertMessageBox(errorMessage, txtAmount).show();
                break;
            case 780:
                new DmAlertMessageBox(errorMessage, txtHType).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHRateEditable).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHActive).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtHServiceType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ServiceMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(DmResult<ServiceMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ServiceMaster_Mdl> result) {
        ServiceMaster_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            if (DmShared.iNull(result.getValue1()).intValue() == 1) {
                serviceFlag = false;
            } else {
                serviceFlag = true;
            }
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtName.setText(data.getSharedKeyFieldName());
            txtShortName.setText(data.getShortName());
            manageDisplay(false);
            txtHActive.setTag(data.getActiveTag());
            txtHActive.setText(data.getActiveText());
            txtHRateEditable.setTag(data.getRateEditableTag());
            txtHRateEditable.setText(data.getRateEditableText());
            txtHDiscount.setTag(data.getIsDiscountAllowTag());
            txtHDiscount.setText(data.getIsDiscountAllowText());
            txtHServiceRoutine.setTag(data.getRoutineTag());
            txtHServiceRoutine.setText(data.getRoutineText());
            txtHNightShift.setTag(data.getNightShiftTag());
            txtHNightShift.setText(data.getNightShiftText());
            txtAmount.setText(data.getNightShiftAmount());
            txtHCategory.setTag(data.getCategoryCode());
            txtHCategory.setText(data.getCategoryName());
            txtHGroup.setTag(data.getGroupCode());
            txtHGroup.setText(data.getGroupName());
            txtHType.setTag(data.getTypeCode());
            txtHType.setText(data.getTypeName());

            txtHLinkDept.setTag(data.getLinkDepartmentCode());
            txtHLinkDept.setText(data.getLinkDepartmentName());
            txtHServiceType.setTag(data.getServiceNatureCode());
            txtHServiceType.setText(data.getServiceNatureName());
            txtPrntSerial.setText(DmClient.vNull(data.getPrnSrNo(), "0").equals("0") ? "" : DmClient.vNull(data.getPrnSrNo(), "0"));
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(DmShared.xNull(defaultValue.getOrganizationCode()), DmShared.xNull(defaultValue.getSiteCode()), strKey, new AsyncCallback<DmResult<String>>() {

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
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtName.setFocus(true);
        txtAmount.setEnabled(false);
        txtHType.setEnabled(false);

        txtHActive.setTag("1");
        txtHActive.setText("Yes");
        txtHRateEditable.setTag("1");
        txtHRateEditable.setText("Yes");
        txtHNightShift.setTag("0");
        txtHNightShift.setText("No");
        txtHDiscount.setTag("0");
        txtHDiscount.setText("No");

        txtHServiceRoutine.setTag("0");
        txtHServiceRoutine.setText("As Applicable");
        txtAmount.setText("");
        txtHServiceType.setTag("0");
        txtHServiceType.setText("Service");
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    private void manageDisplay(boolean flag) {
        txtHActive.setEnabled(flag);
        txtHDiscount.setEnabled(flag);
        txtHServiceType.setEnabled(flag);
        txtAmount.setEnabled((DmShared.iNull(txtHNightShift.getTag()).intValue() == 1));
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHServiceType.setEnabled(serviceFlag);
        }
    }

    private void showOrganizationMaster() {
        if (DmShared.iNull(defaultValue.getOrganizationCode()).longValue() > 0 && !topControl.getMode().equalsIgnoreCase("New")) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmMaster().moduleName, new pmMaster().SERVICE_PRICELIST_MASTER.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    final HashMap<String, String> hmOrganization = new HashMap<String, String>();
                    hmOrganization.put("organizationCode", defaultValue.getOrganizationCode());
                    hmOrganization.put("serviceCode", DmShared.xNull(txtName.getText()));
                    hmOrganization.put("serviceNature", DmShared.iNull(ServiceNature.SERVICE.getId()).toString());
                    ((IsWidgetExtended) wgtObject).action("OPEN_ORGANIZATION", hmOrganization, new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    ((IsWidgetExtended) wgtObject).action("ADD_INVESTIGATION_SERVICE", hmOrganization, null);
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            manageSite.hide();
        }
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(manageSiteMenu)) {
            if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).intValue() > 0) {
                manageSite.show("patientServiceSiteList", "code", topControl.getDocId(), "site_code");
            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", (DmTextBox) null).show();
            }
        } else if (event.getSource().equals(mnuExportCard)) {
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
        srvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
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
