package aarogya.pharmacy.medicineRequirement.client;

import aarogya.pharmacy.medicineRequirement.shared.MedicineRequirement_Srv;
import aarogya.pharmacy.medicineRequirement.shared.MedicineRequirement_SrvAsync;
import aarogya.pharmacy.medicineRequirement.shared.SearchValue_Mdl;
import aarogya.pharmacy.medicineRequirement.shared.TxtHMedicine_Mdl;
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
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.Date;
import java.util.List;

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

    final ServiceInvoker<MedicineRequirement_SrvAsync> srvAsync;

    public SettingPopup() {
        super();
        srvAsync = new ServiceInvoker<MedicineRequirement_SrvAsync>(GWT.create(MedicineRequirement_Srv.class), MedicineRequirement.class.getName());

        toolContainer.setTitle("Setting");
        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        setDefaultValues();
        textHelpEvents();
        manageEvents();
    }

    private void setDefaultValues() {
        txtAsOnDate.setText(DmClient.getLoginDate());

        Date tempDate = DmShared.convertDate(DmShared.dNull(DmClient.getLoginDate()));
        CalendarUtil.addDaysToDate(tempDate, -3);
        txtMovementFrom.setText(DmShared.dNull(tempDate));

        txtHShowHold.setTag("0");
        txtHShowHold.setText("No");

        txtHGroupOn.setTag("Company");
        txtHGroupOn.setText("Company");
    }

    private void manageEvents() {
        popupPanel.addAttachHandler(this);
        txtAsOnDate.addBlurHandler(this);
        txtMovementFrom.addBlurHandler(this);
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    public void show(SearchValue_Mdl svm) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtMovementFrom.setFocus(true);
        txtAsOnDate.setEnabled(false);
        txtHShowHold.setTag(DmShared.xNull(svm.getShowHold()).trim().equals("1") ? "1" : "0");
        txtHShowHold.setText(DmShared.xNull(svm.getShowHold()).trim().equals("1") ? "Yes" : "No");
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

        txtHGroupOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getGroupName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroupOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroupOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroupOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

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

        txtHMedicine.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHMedicine_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicine_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicine_Mdl>> result) {
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

        txtHShowHold.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHShowHold(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShowHold).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHShowHold.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShowHold).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Object t1Row) {
            }
        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

            if (DmShared.dNull(txtAsOnDate.getText()).trim().equals("")) {
                new DmAlertMessageBox("As On Date is mandatory", txtAsOnDate).show();
                return;
            }

//            if (DmShared.dNull(txtMovementFrom.getText()).trim().equals("")) {
//                new DmAlertMessageBox("Movement From is mandatory", txtMovementFrom).show();
//                return;
//            }
            if (DmShared.xNull(txtHShowHold.getTag()).trim().equals("")) {
                new DmAlertMessageBox("Show Hold is mandatory", txtHShowHold).show();
                return;
            }

            manageOk();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtAsOnDate)) {
            txtAsOnDate.setText(DmClient.returnDate(txtAsOnDate.getText()));
        } else if (event.getSource().equals(txtMovementFrom)) {
            txtMovementFrom.setText(DmClient.returnDate(txtMovementFrom.getText()));
        }
    }

    private void manageOk() {
        SearchValue_Mdl svm = new SearchValue_Mdl();

        svm.setAsOnDate(DmShared.dNull(txtAsOnDate.getText()));
        svm.setMovementFrom(DmShared.dNull(txtMovementFrom.getText()));
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
        svm.setShowHold(DmShared.xNull(txtHShowHold.getTag()));
        svm.setGroupOnCode(DmShared.xNull(txtHGroupOn.getTag()));
        svm.setGroupOnName(DmShared.xNull(txtHGroupOn.getText()));

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
