package dataman.utility.gstStructure.client;

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
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyField;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.server.DmServer;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.utility.gstStructure.shared.GstStructure_Mdl;
import dataman.utility.gstStructure.shared.GstStructure_Vld;

import java.util.List;
import dataman.utility.gstStructure.shared.GstStructure_Srv;
import dataman.utility.gstStructure.shared.GstStructure_SrvAsync;

public class GstStructureGridEdit extends GstStructureGridEdit_Dsg implements AttachEvent.Handler, DmRootEvents, BlurHandler, ClickHandler {

    private GstStructure itemTaxStructure;
    private CallBack callBack;
    private GstStructure_Mdl parentRow;
    private ServiceInvoker<GstStructure_SrvAsync> srvAsync;

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnOk) {
            onOk();
        } else if (event.getSource() == btnClose) {
            hide();
        }
    }

    public void onOk() {

        manage();
    }

    private void manageEvent() {
        btnOk.addClickHandler(this);
        btnClose.addClickHandler(this);

    }

    private void manage() {
        GstStructure_Mdl ourRow = new GstStructure_Mdl();
        ourRow.setLocationCode(DmShared.xNull(txtLocationCode.getTag()));
        ourRow.setLocationName(DmShared.xNull(txtLocationCode.getText()));
        ourRow.setGstTypeCode(DmShared.xNull(txtGstTypeCode.getTag()));
        ourRow.setGstTypeName(DmShared.xNull(txtGstTypeCode.getText()));
        ourRow.setGstHeadCode(DmShared.xNull(txtGstHeadCode.getTag()));
        ourRow.setGstHeadName(DmShared.xNull(txtGstHeadCode.getText()));
        ourRow.setAmount(DmShared.xNull(txtAtRate.getText()));
        ourRow.setPurchaseSalesAc(DmShared.iNull(txtHPurchaseSalesAc.getTag()).toString());
        ourRow.setPurchaseSalesAcName(DmShared.xNull(txtHPurchaseSalesAc.getText()));
        ourRow.setInputTaxAc(DmShared.xNull(txtHInputTaxAc.getTag()));
        ourRow.setInputTaxAcName(DmShared.xNull(txtHInputTaxAc.getText()));
        ourRow.setOutputTaxAc(DmShared.xNull(txtHOutputTaxAc.getTag()));
        ourRow.setOutputTaxAcName(DmShared.xNull(txtHOutputTaxAc.getText()));
        ourRow.setRcmAc(DmShared.xNull(txtHRcmAc.getTag()));
        ourRow.setRcmAcName(DmShared.xNull(txtHRcmAc.getText()));

        saveRecord(ourRow);
    }

    private void saveRecord(final GstStructure_Mdl grdItemTaxStructure) {
        try {
            GstStructure_Vld.chkSave(grdItemTaxStructure);
            srvAsync.invokeRPC().saveRecord(grdItemTaxStructure, new AsyncCallback<DmResult<String>>() {
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        callBack.onOk(grdItemTaxStructure);
                    }

                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

    }

    @Override
    public void onBlur(BlurEvent event) {
//        if (event.getSource() == txtNewDeliverydate) {
//            txtNewDeliverydate.setText(DmClient.returnDate(txtNewDeliverydate.getText()));
//            onOk();
//        }

//            if (event.getSource() == txtAtRate) {
//                onOk();
//            }
    }

    interface CallBack {

        public void onOk(GstStructure_Mdl itemTaxStructure_Mdl);

        public void onDelete();

    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void displayRow(GstStructure_Mdl row) {

        GstStructure_Mdl ourRow = (GstStructure_Mdl) row;
        parentRow = ourRow;
//        txtOrderNo.setTag2nd(DmShared.xNull(ourRow.getvSNo()));
        txtLocationCode.setTag(DmShared.xNull(ourRow.getLocationCode()));
        txtLocationCode.setText(DmShared.xNull(ourRow.getLocationName()));
        txtGstTypeCode.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtGstTypeCode.setText(DmShared.xNull(ourRow.getGstTypeName()));
        txtGstHeadCode.setTag(DmShared.xNull(ourRow.getGstHeadCode()));
        txtGstHeadCode.setText(DmShared.xNull(ourRow.getGstHeadName()));
        txtAtRate.setText(DmShared.xNull(ourRow.getAmount()));
        txtHPurchaseSalesAc.setTag(DmShared.xNull(ourRow.getPurchaseSalesAc()));
        txtHPurchaseSalesAc.setText(DmShared.xNull(ourRow.getPurchaseSalesAcName()));
        txtHInputTaxAc.setTag(DmShared.xNull(ourRow.getInputTaxAc()));
        txtHInputTaxAc.setText(DmShared.xNull(ourRow.getInputTaxAcName()));
        txtHOutputTaxAc.setTag(DmShared.xNull(ourRow.getOutputTaxAc()));
        txtHOutputTaxAc.setText(DmShared.xNull(ourRow.getOutputTaxAcName()));
        txtHRcmAc.setTag(DmShared.xNull(ourRow.getRcmAc()));
        txtHRcmAc.setText(DmShared.xNull(ourRow.getRcmAcName()));
        manageDisplay();

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (DmShared.xNull(parentRow.getIsTaxHead()).equalsIgnoreCase("1")) {
                    txtAtRate.setFocus(true);
                } else if (DmShared.xNull(parentRow.getIsTaxHead()).equalsIgnoreCase("2")) {
                    txtHPurchaseSalesAc.setFocus(true);
                }
            }
        });
    }

    public void show(GstStructure invc, GstStructure_Mdl itemTaxStructure_Mdl) {

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtLocationCode.setFocus(true);

            }
        });

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        displayRow(itemTaxStructure_Mdl);

    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            this.hide();
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public GstStructureGridEdit() {
        super("GST Detail ", "328px", "100px");
        srvAsync = new ServiceInvoker<GstStructure_SrvAsync>(GWT.create(GstStructure_Srv.class), GstStructure.class.getName());
        popupPanel.addAttachHandler(this);
        manageEvent();
        textHelpEvents();
    }

    interface GridEditCallBack {

        public void onOk(GstStructure_Mdl itemTaxStructure_Mdl);

        public void close();
    }

    private void manageDisplay() {
        if (DmShared.xNull(parentRow.getIsTaxHead()).equalsIgnoreCase("1")) {
            txtHPurchaseSalesAc.setEnabled(false);
            txtHInputTaxAc.setEnabled(true);
            txtHOutputTaxAc.setEnabled(true);
            txtHRcmAc.setEnabled(true);
            txtAtRate.setEnabled(true);
        } else if (DmShared.xNull(parentRow.getIsTaxHead()).equalsIgnoreCase("2")) {
            txtHInputTaxAc.setEnabled(false);
            txtHOutputTaxAc.setEnabled(false);
            txtHRcmAc.setEnabled(false);
            txtAtRate.setEnabled(false);
            txtHPurchaseSalesAc.setEnabled(true);

        } else {
            txtHInputTaxAc.setEnabled(true);
            txtHOutputTaxAc.setEnabled(true);
            txtHRcmAc.setEnabled(true);
            txtHPurchaseSalesAc.setEnabled(true);
        }
        txtLocationCode.setEnabled(false);
        txtGstTypeCode.setEnabled(false);
        txtGstHeadCode.setEnabled(false);
    }

    private void textHelpEvents() {

        txtHPurchaseSalesAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseSalesAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseSalesAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseSalesAc).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }

        });

        txtHInputTaxAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInputTaxAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInputTaxAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInputTaxAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHOutputTaxAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInputTaxAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOutputTaxAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOutputTaxAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHRcmAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccount(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInputTaxAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRcmAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRcmAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

    }

}
