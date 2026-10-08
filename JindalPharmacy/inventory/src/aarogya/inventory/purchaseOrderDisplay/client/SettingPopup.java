package aarogya.inventory.purchaseOrderDisplay.client;

import aarogya.inventory.purchaseOrderDisplay.shared.SearchValue_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.TxtHItem_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.TxtHIndent_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import java.util.Date;
import java.util.List;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Srv;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_SrvAsync;

public class SettingPopup<T1, T2> extends SettingPopup_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    interface CallBack {

        public void onOk(SearchValue_Mdl svm);
    }

    CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    final ServiceInvoker<PurchaseOrderDisplay_SrvAsync> srvAsync;

    public SettingPopup() {
        super();
        srvAsync = new ServiceInvoker<PurchaseOrderDisplay_SrvAsync>(GWT.create(PurchaseOrderDisplay_Srv.class), PurchaseOrderDisplay.class.getName());

        toolContainer.setTitle("Setting");
        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        setDefaultValues();
        textHelpEvents();
        manageEvents();
    }

    private void setDefaultValues() {
        txtToDate.setText(DmClient.getLoginDate());
//        Date tempDate = DmShared.convertDate(DmShared.dNull(DmClient.getLoginDate()));
//        CalendarUtil.addDaysToDate(tempDate, -3);
        txtFromDate.setText(DmClient.getStartDate());
    }

    private void manageEvents() {
        popupPanel.addAttachHandler(this);
        txtToDate.addBlurHandler(this);
        txtFromDate.addBlurHandler(this);
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    public void show(SearchValue_Mdl svm) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtFromDate.setFocus(true);
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {
        txtHDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHCompany.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHGroup.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

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
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHCatgeory.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCatgeory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCatgeory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCatgeory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHMedicine.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItem_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicine.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicine).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHIndent.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHIndent_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIndent(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHIndent_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndent).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHIndent_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHIndent.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndent).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

            if (DmShared.dNull(txtToDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("As On Date is mandatory", txtToDate).show();
                return;
            }

//            if (DmShared.xNull(txtHShowHold.getTag()).trim().equals("")) {
//                new DmAlertMessageBox("Show Hold is mandatory", txtHShowHold).show();
//                return;
//            }

            manageOk();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtToDate)) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        } else if (event.getSource().equals(txtFromDate)) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
    }

    private void manageOk() {
        SearchValue_Mdl svm = new SearchValue_Mdl();

        svm.setAsOnDate(DmShared.dNull(txtToDate.getText()));
        svm.setMovementFrom(DmShared.dNull(txtFromDate.getText()));
        svm.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        svm.setDepartmentName(DmShared.xNull(txtHDepartment.getText()));
        svm.setCompanyCode(DmShared.xNull(txtHCompany.getTag()));
        svm.setCompanyName(DmShared.xNull(txtHCompany.getText()));
        svm.setGroupCode(DmShared.xNull(txtHGroup.getTag()));
        svm.setGroupName(DmShared.xNull(txtHGroup.getText()));
        svm.setCategoryCode(DmShared.xNull(txtHCatgeory.getTag()));
        svm.setCategoryName(DmShared.xNull(txtHCatgeory.getText()));
        svm.setMedicineCode(DmShared.xNull(txtHMedicine.getTag()));
        svm.setMedicineName(DmShared.xNull(txtHMedicine.getText()));
        svm.setIndent(DmShared.xNull(txtHIndent.getTag()));

        callBack.onOk(svm);
        popupPanel.hide();
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
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

}
