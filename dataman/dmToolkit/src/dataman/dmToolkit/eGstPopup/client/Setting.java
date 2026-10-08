package dataman.dmToolkit.eGstPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.eGstPopup.shared.EGstPopup_Srv;
import dataman.dmToolkit.eGstPopup.shared.EGstPopup_SrvAsync;
import dataman.dmToolkit.eGstPopup.shared.TxtHIrpInfo_Mdl;
import dataman.dmToolkit.gst.shared.EGstParam_Mdl;
import java.util.List;

public class Setting<T1, T2> extends Setting_Dsg implements IsWidget, AttachEvent.Handler, DmRootEvents, ClickHandler {

    private EGstParam_Mdl eGstParam_Mdl = new EGstParam_Mdl();
    private String ewayIrpUrl = "", einvIrpUrl = "";

    private void onOk() {
        if (validateRecord()) {
            fillRecords();
            callBack.onOk(eGstParam_Mdl);
            hide();
        }
    }

    private boolean validateRecord() {
        if (DmShared.xNull(txtHEwayIRP.getText()).trim().equalsIgnoreCase("")
                || DmShared.xNull(ewayIrpUrl).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("E-Way IRP is mandatory", txtHEwayIRP).show();
            return false;
        }
        if (DmShared.xNull(txtHEinvIRP.getText()).trim().equalsIgnoreCase("")
                || DmShared.xNull(einvIrpUrl).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("E-Invoice IRP is mandatory", txtHEinvIRP).show();
            return false;
        }
        return true;
    }

    interface CallBack {

        public void onOk(EGstParam_Mdl eGstParam_Mdl);
    }

    private CallBack callBack;
    private final ServiceInvoker<EGstPopup_SrvAsync> srvAsync;

    public void addCallBack(CallBack setting) {
        callBack = setting;
    }

    public void show(EGstParam_Mdl mrTemp) {
        clear();
        eGstParam_Mdl = mrTemp;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        displayData();
    }

    private void clear() {
        ewayIrpUrl = "";
        einvIrpUrl = "";
        txtHEinvIRP.setTag("");
        txtHEinvIRP.setTag2nd("");
        txtHEinvIRP.setText("");
        txtHEwayIRP.setTag("");
        txtHEwayIRP.setTag2nd("");
        txtHEwayIRP.setText("");
    }

    private void displayData() {
        if (eGstParam_Mdl != null) {

            txtHEwayIRP.setTag(DmShared.xNull(eGstParam_Mdl.getEway_IrpCode()));
            txtHEwayIRP.setTag2nd(DmShared.xNull(eGstParam_Mdl.getEway_IrpCode()));
            txtHEwayIRP.setText(DmShared.xNull(eGstParam_Mdl.getEway_IrpName()));
            ewayIrpUrl = DmShared.xNull(eGstParam_Mdl.getEway_BaseUrl());

            txtHEinvIRP.setTag(DmShared.xNull(eGstParam_Mdl.getEinv_IrpCode()));
            txtHEinvIRP.setTag2nd(DmShared.xNull(eGstParam_Mdl.getEinv_IrpCode()));
            txtHEinvIRP.setText(DmShared.xNull(eGstParam_Mdl.getEinv_IrpName()));
            einvIrpUrl = DmShared.xNull(eGstParam_Mdl.getEinv_BaseUrl());
        }
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void textHelpEvents() {

        txtHEwayIRP.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHIrpInfo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIrp(DmEnum.IRP_Type.E_WAY.getCode(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHIrpInfo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEwayIRP).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHIrpInfo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEwayIRP.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEwayIRP).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHIrpInfo_Mdl tRow) {
                if (!DmShared.xNull(txtHEwayIRP.getTag()).equalsIgnoreCase(DmShared.xNull(txtHEwayIRP.getTag2nd()))) {
                    ewayIrpUrl = "";
                    txtHEwayIRP.setTag2nd(DmShared.xNull(txtHEwayIRP.getTag()));
                }
                if (blnStatus && tRow != null) {
                    ewayIrpUrl = DmShared.xNull(tRow.getUrl());
                }
            }

        });

        txtHEinvIRP.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHIrpInfo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIrp(DmEnum.IRP_Type.E_INVOICE.getCode(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHIrpInfo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEinvIRP).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHIrpInfo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEinvIRP.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEinvIRP).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHIrpInfo_Mdl tRow) {
                if (!DmShared.xNull(txtHEinvIRP.getTag()).equalsIgnoreCase(DmShared.xNull(txtHEinvIRP.getTag2nd()))) {
                    einvIrpUrl = "";
                    txtHEinvIRP.setTag2nd(DmShared.xNull(txtHEinvIRP.getTag()));
                }
                if (blnStatus && tRow != null) {
                    einvIrpUrl = DmShared.xNull(tRow.getUrl());
                }
            }

        });

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnOk) {
            onOk();
        }
    }

    private void fillRecords() {
        eGstParam_Mdl.setEway_IrpCode(DmShared.xNull(txtHEwayIRP.getTag()));
        eGstParam_Mdl.setEway_IrpName(DmShared.xNull(txtHEwayIRP.getText()));
        eGstParam_Mdl.setEway_BaseUrl(DmShared.xNull(ewayIrpUrl));
        eGstParam_Mdl.setEinv_IrpCode(DmShared.xNull(txtHEinvIRP.getTag()));
        eGstParam_Mdl.setEinv_IrpName(DmShared.xNull(txtHEinvIRP.getText()));
        eGstParam_Mdl.setEinv_BaseUrl(DmShared.xNull(einvIrpUrl));
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
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

    public Setting() {
        super();
        srvAsync = new ServiceInvoker<EGstPopup_SrvAsync>(GWT.create(EGstPopup_Srv.class), EGstPopup.class.getName());
        textHelpEvents();
        popupPanel.addAttachHandler(this);
        toolContainer.setTitle("Setting");
        toolContainer.addWidget(flwOk);
        toolContainer.addWidget(flwCancel);
        toolContainer.addGap();
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

}
