package aarogya.pharmacy.barcodeGeneration.client;

import aarogya.pharmacy.barcodeGeneration.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class Setting<T1, T2> extends Setting_Dsg implements ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    @Override
    public void searchRecord(String key) {

    }

    interface CallBack {

        public void onOk(Setting_Mdl setting_Mdl);

        public void onClose();

    }

    CallBack callBack;
    GrdBarcodeGeneration_Mdl parentRow;
    ServiceInvoker<BarcodeGeneration_SrvAsync> SrvAsync;
    BarcodeGeneration bankReconciliationEntry;

    public void addCallBack(CallBack reconciliationEntryCallBack) {
        this.callBack = reconciliationEntryCallBack;
    }

    public void show(GrdBarcodeGeneration_Mdl parentRow, boolean editable) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        this.parentRow = parentRow;
        flwOk.clear();
        flwOk.setVisible(false);
        if (editable) {
            flwOk.add(btnOk);
            flwOk.setVisible(true);
        }
        if (DmShared.xNull(txtFromDate.getText()).equalsIgnoreCase("")) {
            txtFromDate.setText(DmClient.getLoginDate());
        }
        if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
            txtToDate.setText(DmClient.getLoginDate());
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtFromDate.setFocus(true);
            }
        });
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onClose();
    }

    @Override
    public void onClick(ClickEvent event) {
        Setting_Mdl mrTemp = new Setting_Mdl();

        if (event.getSource() == btnCancel) {
            popupPanel.hide();
        } else if (event.getSource() == btnOk) {

            if (DmShared.xNull(txtToDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("To Date is mandatory.", txtToDate).show();
                return;
            }

            if (!txtFromDate.getText().trim().equalsIgnoreCase("") && !txtToDate.getText().trim().equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtToDate.getText()).getTime() < (DmShared.convertDate(txtFromDate.getText())).getTime())) {
                    new DmAlertMessageBox("From Date should be less than or equal to To Date.", txtToDate).show();
                    return;
                }
            }
            mrTemp.setFromDate(DmShared.xNull(txtFromDate.getText()));
            mrTemp.setToDate(DmShared.xNull(txtToDate.getText()));
            mrTemp.setDepartment(DmShared.xNull(txtHDepartment.getTag()));
            mrTemp.setBarCodeDocid(DmShared.iNull(txtHBarCode.getTag()).toString());
            mrTemp.setBarCodeRecid(DmShared.xNull(txtHBarCode.getText()));
            popupPanel.hide();
            callBack.onOk(mrTemp);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide();
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

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
        if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    private void textHelpEvents() {
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHBarCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBarCodeHeader(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBarCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBarCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBarCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    public Setting() {
        super();
        SrvAsync = new ServiceInvoker<BarcodeGeneration_SrvAsync>(GWT.create(BarcodeGeneration_Srv.class),BarcodeGeneration.class.getName());
        textHelpEvents();
        btnOk.addClickHandler(this);
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
    }

}
