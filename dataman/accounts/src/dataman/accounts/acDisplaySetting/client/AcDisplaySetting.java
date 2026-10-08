package dataman.accounts.acDisplaySetting.client;

import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.accounts.acDisplaySetting.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmBase.self.shared.*;
import java.util.List;

public class AcDisplaySetting extends AcDisplaySetting_Dsg implements IsWidget, ClickHandler, BlurHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<AcDisplaySetting_SrvAsync> srvAsync;
    private CallBack callBack;
    public AcDisplaySetting_Mdl acDisplaySetting = null;

    public interface CallBackIniDefault {

        public void onSuccess(AcDisplaySetting_Mdl acDisplaySetting);
    }

    public interface CallBack {

        public void onOk(AcDisplaySetting_Mdl acDisplaySetting);

        public void onClose();
    }

    public AcDisplaySetting() {
        super();
        srvAsync = new ServiceInvoker<AcDisplaySetting_SrvAsync>(GWT.create(AcDisplaySetting_Srv.class), AcDisplaySetting.class.getName());
        handlerEvents();
        textHelpEvents();
        fillValue();
    }

    public void fillDefaultValue(final CallBackIniDefault callBackIniDefault) {
        acDisplaySetting = new AcDisplaySetting_Mdl();
        acDisplaySetting.setDateFrom(DmClient.getStartDate());
        acDisplaySetting.setDateTo(DmClient.getEndDate());
        srvAsync.invokeRPC().fillData(new AsyncCallback<DmResult<AcDisplaySetting_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnOk).show();
            }

            @Override
            public void onSuccess(DmResult<AcDisplaySetting_Mdl> result) {
                if (result.isStatus()) {
                    acDisplaySetting = result.getData();
                    txtDateFrom.setText(DmClient.getStartDate());
                    txtDateTo.setText(DmClient.getEndDate());
                    txtHDetail.setTag("0");
                    txtHDetail.setText("No");
                    txtHNarration.setTag("1");
                    txtHNarration.setText("Yes");
                    txtHOpening.setTag("1");
                    txtHOpening.setText("Yes");
                    txtHZeroValue.setTag("1");
                    txtHZeroValue.setText("Yes");
                    txtHSite.setTag(acDisplaySetting.getSiteCodeList());
                    txtHSite.setText(acDisplaySetting.getSiteNameList());
                    acDisplaySetting.setDateFrom(DmShared.dNull(txtDateFrom.getText()));
                    acDisplaySetting.setDateTo(DmShared.dNull(txtDateTo.getText()));
                    acDisplaySetting.setClosingStock(DmShared.vNull(txtClosing.getText()).toString());
                    acDisplaySetting.setIncludeOpening(DmShared.iNull(txtHOpening.getTag()).toString());
                    acDisplaySetting.setLedgerAcCodeList(DmShared.xNull(txtHLedger.getTag()));
                    acDisplaySetting.setSiteCodeList(DmShared.xNull(txtHSite.getTag()));
                    acDisplaySetting.setSiteNameList(DmShared.xNull(txtHSite.getText()));
                    acDisplaySetting.setShowDetail(DmShared.iNull(txtHDetail.getTag()).toString());
                    acDisplaySetting.setShowNarration(DmShared.iNull(txtHNarration.getTag()).toString());
                    acDisplaySetting.setShowZeroValueAc(DmShared.iNull(txtHZeroValue.getTag()).toString());

                    if (callBackIniDefault != null) {
                        callBackIniDefault.onSuccess(acDisplaySetting);
                    }
                } else {
                }
            }
        });
    }

    private void fillValue() {
        if (acDisplaySetting != null) {
            txtDateFrom.setText(acDisplaySetting.getDateFrom());
            txtDateTo.setText(acDisplaySetting.getDateTo());
            txtHDetail.setTag(acDisplaySetting.getShowDetail());
            txtHDetail.setText(DmShared.iNull(acDisplaySetting.getShowDetail()).intValue() == 1 ? "Yes" : "No");
            txtHNarration.setTag(acDisplaySetting.getShowNarration());
            txtHNarration.setText(DmShared.iNull(acDisplaySetting.getShowNarration()).intValue() == 1 ? "Yes" : "No");
            txtHOpening.setTag(acDisplaySetting.getIncludeOpening());
            txtHOpening.setText(DmShared.iNull(acDisplaySetting.getIncludeOpening()).intValue() == 1 ? "Yes" : "No");
            txtHZeroValue.setTag(acDisplaySetting.getShowZeroValueAc());
            txtHZeroValue.setText(DmShared.iNull(acDisplaySetting.getShowZeroValueAc()).intValue() == 1 ? "Yes" : "No");
            txtHSite.setTag(acDisplaySetting.getSiteCodeList());
            txtHSite.setText(acDisplaySetting.getSiteNameList());
            txtClosing.setText(acDisplaySetting.getClosingStock());
        }
    }

    private void textHelpEvents() {

        txtHDetail.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDetail).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDetail.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDetail).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHLedger.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<AcntLedger_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getLedgerAcount(helper, blnToEndOfFile, DmShared.xNull(txtHSite.getTag()), new AsyncCallback<DmResultPageing<List<AcntLedger_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLedger).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<AcntLedger_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHLedger.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLedger).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHNarration.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNarration).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNarration.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNarration).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHOpening.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOpening).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOpening.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOpening).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHZeroValue.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHZeroValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHZeroValue.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHZeroValue).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
    }

    private void handlerEvents() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        pnlPopUp.addAttachHandler(this);
        txtDateFrom.addBlurHandler(this);
        txtDateTo.addBlurHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    public void show(Widget widget, AcDisplaySetting_Mdl acDisplaySetting) {
        this.acDisplaySetting = acDisplaySetting;
        pnlPopUp.setGlassEnabled(true);
        pnlPopUp.showRelativeTo(widget);
        fillValue();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                txtDateFrom.setFocus(true);
            }
        });
    }

    protected void hide() {
        this.pnlPopUp.hide();
    }

    @Override
    public Widget asWidget() {
        return pnlPopUp;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            if (DmShared.xNull(txtHSite.getTag()).trim().equals("") && acDisplaySetting != null) {
                txtHSite.setTag(acDisplaySetting.getSiteCodeList());
                txtHSite.setText(acDisplaySetting.getSiteNameList());
            }
            this.hide();
        } else if (event.getSource().equals(btnOk)) {
            if (DmShared.xNull(txtHSite.getTag()).trim().equals("")) {
                new DmAlertMessageBox("Site Name is mandatory.", txtHSite).show();
                return;
            }
            if (!DmShared.dNull(txtDateFrom.getText()).trim().equalsIgnoreCase("") && !DmShared.dNull(txtDateTo.getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtDateFrom.getText()).getTime() > DmShared.convertDate(txtDateTo.getText()).getTime()) {
                    new DmAlertMessageBox("From date should be less than To Date", txtDateFrom).show();
                    return;
                }
            }
            acDisplaySetting = new AcDisplaySetting_Mdl();
            acDisplaySetting.setDateFrom(DmShared.dNull(txtDateFrom.getText()));
            acDisplaySetting.setDateTo(DmShared.dNull(txtDateTo.getText()));
            acDisplaySetting.setClosingStock(DmShared.vNull(txtClosing.getText()).toString());
            acDisplaySetting.setIncludeOpening(DmShared.iNull(txtHOpening.getTag()).toString());
            acDisplaySetting.setLedgerAcNameList(DmShared.xNull(txtHLedger.getText()));
            acDisplaySetting.setLedgerAcCodeList(DmShared.xNull(txtHLedger.getTag()));
            acDisplaySetting.setSiteCodeList(DmShared.xNull(txtHSite.getTag()));
            acDisplaySetting.setSiteNameList(DmShared.xNull(txtHSite.getText()));
            acDisplaySetting.setShowDetail(DmShared.iNull(txtHDetail.getTag()).toString());
            acDisplaySetting.setShowNarration(DmShared.iNull(txtHNarration.getTag()).toString());
            acDisplaySetting.setShowZeroValueAc(DmShared.iNull(txtHZeroValue.getTag()).toString());
            this.callBack.onOk(acDisplaySetting);
            this.hide();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtDateFrom)) {
            if (!DmShared.xNull(txtDateFrom.getText()).equalsIgnoreCase("")) {
                txtDateFrom.setText(DmClient.returnDate(txtDateFrom.getText()));

            }
        } else if (event.getSource().equals(txtDateTo)) {
            if (!DmShared.xNull(txtDateTo.getText()).equalsIgnoreCase("")) {
                txtDateTo.setText(DmClient.returnDate(txtDateTo.getText()));
            }
        }
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
            this.hide();
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

}
