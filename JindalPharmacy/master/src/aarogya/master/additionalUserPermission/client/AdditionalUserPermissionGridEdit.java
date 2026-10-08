package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.*;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.libraryMenu.self.shared.lmUtility;
import java.util.HashMap;
import java.util.List;

public class AdditionalUserPermissionGridEdit<T1, T2> extends AdditionalUserPermissionGridEdit_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, BlurHandler, ClickHandler {

    private ServiceInvoker<AdditionalUserPermission_SrvAsync> srvAsync;
    private AdditionalUserPermission userDepartmentMapping;
    private final ServiceGridEdit<GrdService_Mdl, GrdService_PrpAcs> gridServiceEdit = new ServiceGridEdit(grdService);
    private final PaymentModeGridEdit<GridPaymentMode_Mdl, GetPaymentMode_PrpAcs> grdPayment = new PaymentModeGridEdit(grdPaymentMode);
    private CallBack callBack;

    interface CallBack {

        public void onOk();

        public void onClose();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(AdditionalUserPermission usermapping, GrdAdditionalUserPermission_Mdl additionalUserPermission_Mdl) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        userDepartmentMapping = usermapping;
        txtUserName.setEnabled(false);
        flwAlterUserParent.clear();
        flwUserPermissionParent.clear();
        flwSaveParent.clear();
        if (usermapping.isAdditionalPermissionBlog) {
            flwSaveParent.add(flwSaveChild);
        }
        if (usermapping.isCreateUserBlog) {
            flwAlterUserParent.add(flwAlterUserChild);
        }
        if (usermapping.isUserPermissionBlog) {
            flwUserPermissionParent.add(flwUserPermissionChild);
        }
        fillData(additionalUserPermission_Mdl);

    }

    private void textHelpEvents() {
        txtHLinkedAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLinkedAc(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLinkedAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLinkedAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLinkedAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        txtHNursingStation.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNursingStation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNursingStation).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNursingStation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNursingStation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHOtherDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHOtherDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOtherDepartment).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOtherDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOtherDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHLocationAllowed.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLocationAllowed(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLocationAllowed).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLocationAllowed.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLocationAllowed).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHDiscForNonDisSer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDiscForNDS(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDiscForNonDisSer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDiscForNonDisSer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDiscForNonDisSer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

    }

    private void gridEvents() {
        gridServiceEdit.addCallBack(new ServiceGridEdit.CallBack() {

            @Override
            public void onOk(GrdService_Mdl master_Mdl) {
                grdService.commitChanges();
            }

            @Override
            public void onDelete() {
                grdService.commitChanges();
            }

        });
        
        grdPayment.addCallBack(new PaymentModeGridEdit.CallBack() {
            @Override
            public void onOk(GridPaymentMode_Mdl paymentGrid) {
                grdPaymentMode.commitChanges();
//                List<KeyFieldExtended> lstTemp = new ArrayList<>();
//                lstTemp.addAll(grdPaymentMode.getGrid().getStore().getAll());
//                savePayModePermission(lstTemp, paymentGrid.getSharedModifiedBy(), paymentGrid.getSharedKeyFieldCode());

            }

            @Override
            public void onDelete() {
                grdPaymentMode.commitChanges();
            }
        });

        grdService.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showService(event.getRowIndex());
            }
        });
        
        grdService.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showService(grdService.getGrid().getStore().indexOf(grdService.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        
        grdPaymentMode.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showGrdPaymentMode(event.getRowIndex());
            }
        });
        grdPaymentMode.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGrdPaymentMode(grdPaymentMode.getGrid().getStore().indexOf(grdPaymentMode.getGrid().getSelectionModel().getSelectedItem()));

                }
            }
        }, KeyDownEvent.getType());
    }

    private void showService(int currentRowIndex) {
        gridServiceEdit.show(currentRowIndex, true, true, true);
    }
    
    private void showGrdPaymentMode(int currentRowIndex) {
        grdPayment.show(currentRowIndex, true, true, true,grdPaymentMode.getGrid().getStore().get(currentRowIndex).getSharedKeyFieldCode(),DmShared.xNull(txtUserName.getText()));
    }

    private GrdAdditionalUserPermission_Mdl saveInitialize() {
        GrdAdditionalUserPermission_Mdl ourRow = new GrdAdditionalUserPermission_Mdl();

        ourRow.setSharedKeyFieldName(DmShared.xNull(txtUserName.getText()));
        ourRow.setAllowDiscountPercent(DmClient.vNull(txtAllowDiscountPercent.getText(), "0.00"));
        ourRow.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        ourRow.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));

        ourRow.setLinkedAcCode(DmShared.xNull(txtHLinkedAc.getTag()));
        ourRow.setLinkedAcName(DmShared.xNull(txtHLinkedAc.getText()));

        ourRow.setNursingStationCode(DmShared.xNull(txtHNursingStation.getTag()));
        ourRow.setNursingStationName(DmShared.xNull(txtHNursingStation.getText()));

        ourRow.setAllowDepartmentName(DmShared.xNull(txtHOtherDepartment.getText()));
        ourRow.setAllowDepartmentCode(DmShared.xNull(txtHOtherDepartment.getTag()));

        ourRow.setAllowLocationName(DmShared.xNull(txtHLocationAllowed.getText()));
        ourRow.setAllowLocationCode(DmShared.xNull(txtHLocationAllowed.getTag()));

        ourRow.setAllowDiscForNDSCode(DmShared.xNull(txtHDiscForNonDisSer.getTag()));
        ourRow.setAllowDiscForNDSName(DmShared.xNull(txtHDiscForNonDisSer.getText()));

        ourRow.getListServiceDetail().clear();
        ourRow.getListServiceDetail().addAll(grdService.getGrid().getStore().getAll());
        
        ourRow.getListServiceDetail().clear();
        ourRow.getListPaymentMode().addAll(grdPaymentMode.getGrid().getStore().getAll());

        String serviceName = "", serviceCatName = "";
        for (GrdService_Mdl grdService_Mdl : ourRow.getListServiceDetail()) {
            serviceName += DmShared.xNull(grdService_Mdl.getServiceName()).trim() + ",";
            serviceCatName += DmShared.xNull(grdService_Mdl.getSharedKeyFieldName()).trim() + ",";
        }
        if (!DmShared.xNull(serviceName).trim().equalsIgnoreCase("")) {
            serviceName = serviceName.substring(0, serviceName.length() - 1);
        }
        if (!DmShared.xNull(serviceCatName).trim().equalsIgnoreCase("")) {
            serviceCatName = serviceCatName.substring(0, serviceCatName.length() - 1);
        }
        ourRow.setServiceName(serviceName);
        ourRow.setServiceCatName(serviceCatName);

        return ourRow;
    }

    public void saveRecord() {
        GrdAdditionalUserPermission_Mdl mrTemp;
        try {
            mrTemp = saveInitialize();
            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHLinkedAc).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        hide();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtHLinkedAc).show();
                    }
                }
            });
        } catch (DmException e) {

        }
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onOk();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnSave) {
            saveRecord();
        } else if (event.getSource() == btnAlterUser) {
            openUserMast();
            hide();
        }
        if (event.getSource() == btnUserPermission) {
            openUserPermission();
            hide();
        }
        if (event.getSource() == btnClose) {
            hide();
        }
    }

    private void openUserPermission() {
        final HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userId", txtUserName.getTag());
        hashMap.put("userName", txtUserName.getText());
        if (DmShared.iNull(txtUserName.getTag()).intValue() > 0) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmUtility().moduleName, new lmUtility().USER_PERMISSION.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    ((IsWidgetExtended) wgtObject).action("ADD_USER_PERMISSION", hashMap, new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);

                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void openUserMast() {
        if (DmShared.iNull(txtUserName.getTag()).intValue() > 0) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmUtility().moduleName, new lmUtility().USER_MASTER.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    ((IsWidgetExtended) wgtObject).action("OPEN_USER", DmShared.iNull(txtUserName.getTag()).toString(), new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            callBack.onOk();
            this.hide();
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

    public AdditionalUserPermissionGridEdit() {
        super();
        srvAsync = new ServiceInvoker<AdditionalUserPermission_SrvAsync>(GWT.create(AdditionalUserPermission_Srv.class), AdditionalUserPermission.class.getName());
        popupPanel.addAttachHandler(this);
        gridEvents();
        textHelpEvents();
        manageEvents();

    }

    private void fillData(GrdAdditionalUserPermission_Mdl data) {
        txtUserName.setText(DmShared.xNull(data.getSharedKeyFieldName()));
        txtUserName.setTag(DmShared.xNull(data.getSharedKeyFieldCode()));

        txtHDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
        txtHDepartment.setText(DmShared.xNull(data.getDepartmentName()));

        txtHLinkedAc.setTag(DmShared.xNull(data.getLinkedAcCode()));
        txtHLinkedAc.setText(DmShared.xNull(data.getLinkedAcName()));

        txtHNursingStation.setTag(DmShared.xNull(data.getNursingStationCode()));
        txtHNursingStation.setText(DmShared.xNull(data.getNursingStationName()));

        txtHOtherDepartment.setText(DmShared.xNull(data.getAllowDepartmentName()));
        txtHOtherDepartment.setTag(DmShared.xNull(data.getAllowDepartmentCode()));
        txtAllowDiscountPercent.setText(DmClient.vNull(data.getAllowDiscountPercent(), "0.00"));

        txtHDiscForNonDisSer.setTag(DmShared.xNull(data.getAllowDiscForNDSCode()));
        txtHDiscForNonDisSer.setText(DmShared.xNull(data.getAllowDiscForNDSName()));

        txtHLocationAllowed.setText(DmShared.xNull(data.getAllowLocationName()));
        txtHLocationAllowed.setTag(DmShared.xNull(data.getAllowLocationCode()));

        grdService.getGrid().getStore().clear();
        grdService.getGrid().getStore().addAll(data.getListServiceDetail());
        grdService.getGrid().getStore().add(new GrdService_Mdl());
        
        grdPaymentMode.getGrid().getStore().clear();
        grdPaymentMode.getGrid().getStore().addAll(data.getListPaymentMode());
        grdPaymentMode.getGrid().getStore().add(new GridPaymentMode_Mdl());

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHLinkedAc.setFocus(true);
            }
        });
    }

    private void manageEvents() {
        btnSave.addClickHandler(this);
        btnAlterUser.addClickHandler(this);
        btnUserPermission.addClickHandler(this);
        btnClose.addClickHandler(this);
        txtAllowDiscountPercent.addBlurHandler(this);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAllowDiscountPercent) {
            if (DmShared.vNull(DmClient.vNull(txtAllowDiscountPercent.getText(), "0.00")).doubleValue() > 100) {
                new DmAlertMessageBox("Discount % can't exceed 100%. ", txtAllowDiscountPercent).show();
                return;
            }

        }
    }
}
