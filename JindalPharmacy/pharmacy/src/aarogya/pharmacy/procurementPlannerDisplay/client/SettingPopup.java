package aarogya.pharmacy.procurementPlannerDisplay.client;

import aarogya.pharmacy.procurementPlannerDisplay.client.*;
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_Srv;
import aarogya.pharmacy.procurementPlannerDisplay.shared.ProcurementPlannerDisplay_SrvAsync;
import aarogya.pharmacy.procurementPlannerDisplay.shared.SearchValue_Mdl;
import aarogya.pharmacy.procurementPlannerDisplay.shared.TxtHMedicine_Mdl;
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

    private boolean isPlanExist = false;

    @Override
    public void onBlur(BlurEvent event) {
        
    }

    //<editor-fold defaultstate="collapsed" desc="Callback">
    interface CallBack {

        public void onOk(SearchValue_Mdl svm);
    }

    CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    final ServiceInvoker<ProcurementPlannerDisplay_SrvAsync> srvAsync;

    public SettingPopup() {
        super();
        srvAsync = new ServiceInvoker<ProcurementPlannerDisplay_SrvAsync>(GWT.create(ProcurementPlannerDisplay_Srv.class), ProcurementPlannerDisplay.class.getName());

        toolContainer.setTitle("Setting");
        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        setDefaultValues();
        textHelpEvents();
        manageEvents();
    }

    private void setDefaultValues() {
        Date tempDate = DmShared.convertDate(DmShared.dNull(DmClient.getLoginDate()));
        CalendarUtil.addDaysToDate(tempDate, -3);
        txtConsumptionDays.setText("90");
        txtProjectionDays.setText("30");
        txtHShortForDept.setText("Yes");
        txtHShortForDept.setTag("1");
        txtHShortForSite.setText("Yes");
        txtHShortForSite.setTag("1");
        txtstockRunDaysDept.setText("0");
        txtstockRunDaysSite.setText("0");
    }

    private void manageEvents() {
        popupPanel.addAttachHandler(this);
        txtConsumptionDays.addBlurHandler(this);
        txtProjectionDays.addBlurHandler(this);
        txtstockRunDaysDept.addBlurHandler(this);
        txtstockRunDaysSite.addBlurHandler(this);
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    public void show(SearchValue_Mdl svm, boolean isPlanExist) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        txtHDepartment.setFocus(true);
        this.isPlanExist = isPlanExist;
//        lblCompanyName.setText("XYZ");
        if (!DmShared.xNull(svm.getDepartmentCode()).trim().equalsIgnoreCase("")) {
            txtHDepartment.setText(DmShared.xNull(svm.getDepartmentName()));
            txtHDepartment.setTag(DmShared.xNull(svm.getDepartmentCode()));

            txtHMedicine.setText(DmShared.xNull(svm.getMedicineName()));
            txtHMedicine.setTag(DmShared.xNull(svm.getMedicineCode()));

            txtHCatgeory.setText(DmShared.xNull(svm.getCategoryName()));
            txtHCatgeory.setTag(DmShared.xNull(svm.getCategoryCode()));

            txtHCompany.setText(DmShared.xNull(svm.getCompanyName()));
            txtHCompany.setTag(DmShared.xNull(svm.getCompanyCode()));

            txtHGroup.setText(DmShared.xNull(svm.getGroupName()));
            txtHGroup.setTag(DmShared.xNull(svm.getGroupCode()));

            txtProjectionDays.setText(DmShared.xNull(svm.getProjectionDays()));
            txtConsumptionDays.setText(DmShared.xNull(svm.getConsumptionDays()));
//        txtShortForDept.setText(DmShared.xNull(svm.get()));
//        txtShortForSite.setText(DmShared.xNull(svm.getGroupName()));
            txtstockRunDaysDept.setText(DmShared.xNull(svm.getStockRunDaysDept()));
            txtstockRunDaysSite.setText(DmShared.xNull(svm.getStockRunDaysSite()));
        } else {
            setDefaultValues();
        }
        setControlsEnable(isPlanExist ? false : true);
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
        
        txtHShortForDept.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShortForDept).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHShortForDept.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShortForDept).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Object t1Row) {
            }
        });
        
        txtHShortForSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHShortForSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHShortForSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHShortForSite).show();
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
            if (isPlanExist) {
                popupPanel.hide();
            } else {
                if (DmShared.xNull(txtConsumptionDays.getText()).trim().equals("") || DmShared.iNull(txtConsumptionDays.getText()).longValue() == (0)) {
                    new DmAlertMessageBox("Consumption days is mandatory", txtConsumptionDays).show();
                    return;
                }
                if (DmShared.xNull(txtProjectionDays.getText()).trim().equals("") || DmShared.iNull(txtProjectionDays.getText()).longValue() == (0)) {
                    new DmAlertMessageBox("Consumption days is mandatory", txtProjectionDays).show();
                    return;
                }
                manageOk();
            }
        }
    }

    private void manageOk() {
        if (DmShared.xNull(txtHDepartment.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Department is mandatory.", txtHDepartment).show();
            return;
        }
        SearchValue_Mdl svm = new SearchValue_Mdl();

        svm.setProjectionDays(DmShared.iNull(txtProjectionDays.getText()).toString());
        svm.setConsumptionDays(DmShared.iNull(txtConsumptionDays.getText()).toString());
        svm.setStockRunDaysDept(DmShared.iNull(txtstockRunDaysDept.getText()).toString());
        svm.setStockRunDaysSite(DmShared.iNull(txtstockRunDaysSite.getText()).toString());
        svm.setOnlyShortForDept(DmShared.iNull(txtHShortForDept.getTag()).intValue() == 1);
        svm.setOnlyShortForSite(DmShared.iNull(txtHShortForSite.getTag()).intValue() == 1);
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

    private void setControlsEnable(boolean activeSetting) {
        txtHDepartment.setEnabled(activeSetting);
        txtHCatgeory.setEnabled(activeSetting);
        txtHGroup.setEnabled(activeSetting);
        txtHCompany.setEnabled(activeSetting);
        txtHMedicine.setEnabled(activeSetting);
        txtConsumptionDays.setEnabled(activeSetting);
        txtProjectionDays.setEnabled(activeSetting);
        txtHShortForDept.setEnabled(activeSetting);
        txtHShortForSite.setEnabled(activeSetting);
        txtstockRunDaysDept.setEnabled(activeSetting);
        txtstockRunDaysSite.setEnabled(activeSetting);
    }
}
