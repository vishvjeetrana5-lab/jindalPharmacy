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

public class AcDisplaySettingComparison extends AcDisplaySettingComparison_Dsg implements IsWidget, ClickHandler, BlurHandler, DmRootEvents, AttachEvent.Handler {

    private final ServiceInvoker<AcDisplaySetting_SrvAsync> srvAsync;
    private CallBack callBack;
    public AcDisplaySetting_Mdl acDisplaySettingComparison = null;

    public interface CallBackIniDefault {

        public void onSuccess(AcDisplaySetting_Mdl acDisplaySettingComparison);
    }

    public interface CallBack {

        public void onOk(AcDisplaySetting_Mdl acDisplaySettingComparison);

        public void onClose();
    }

    public AcDisplaySettingComparison() {
        super();
        srvAsync = new ServiceInvoker<AcDisplaySetting_SrvAsync>(GWT.create(AcDisplaySetting_Srv.class), AcDisplaySetting.class.getName());
        handlerEvents();
        textHelpEvents();
        fillValue();
    }

    public void fillDefaultValue(final CallBackIniDefault callBackIniDefault) {
        acDisplaySettingComparison = new AcDisplaySetting_Mdl();
        acDisplaySettingComparison.setDateFrom(DmClient.getStartDate());
        acDisplaySettingComparison.setDateTo(DmClient.getEndDate());
        acDisplaySettingComparison.setLstDateFrom(DmClient.getStartDate());
        acDisplaySettingComparison.setLstDateTo(DmClient.getEndDate());
        
        acDisplaySettingComparison.setYearPrefix(DmClient.getVoucherPrefix());
        acDisplaySettingComparison.setYearPrefix2(DmClient.getVoucherPrefix());
        
        srvAsync.invokeRPC().fillData(new AsyncCallback<DmResult<AcDisplaySetting_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), btnOk).show();
            }

            @Override
            public void onSuccess(DmResult<AcDisplaySetting_Mdl> result) {
                if (result.isStatus()) {
                    acDisplaySettingComparison = result.getData();
                    txtDateFrom.setText(DmClient.getStartDate());
                    txtDateTo.setText(DmClient.getEndDate());
                    txtLstDateFrom.setText(DmClient.getStartDate());
                    txtLstDateTo.setText(DmClient.getEndDate());
                    txtHDetail.setTag("0");
                    txtHDetail.setText("No");
                    txtHNarration.setTag("1");
                    txtHNarration.setText("Yes");
                    txtHOpening.setTag("1");
                    txtHOpening.setText("Yes");
                    txtHZeroValue.setTag("1");
                    txtHZeroValue.setText("Yes");
                    txtHSite.setTag(acDisplaySettingComparison.getSiteCodeList());
                    txtHSite.setText(acDisplaySettingComparison.getSiteNameList());
                    acDisplaySettingComparison.setDateFrom(DmShared.dNull(txtDateFrom.getText()));
                    acDisplaySettingComparison.setDateTo(DmShared.dNull(txtDateTo.getText()));
                    acDisplaySettingComparison.setLstDateFrom(DmShared.dNull(txtLstDateFrom.getText()));
                    acDisplaySettingComparison.setLstDateTo(DmShared.dNull(txtLstDateTo.getText()));
                    acDisplaySettingComparison.setClosingStock(DmShared.vNull(txtClosing.getText()).toString());
                    acDisplaySettingComparison.setIncludeOpening(DmShared.iNull(txtHOpening.getTag()).toString());
                    acDisplaySettingComparison.setLedgerAcCodeList(DmShared.xNull(txtHLedger.getTag()));
                    acDisplaySettingComparison.setSiteCodeList(DmShared.xNull(txtHSite.getTag()));
                    acDisplaySettingComparison.setSiteNameList(DmShared.xNull(txtHSite.getText()));
                    acDisplaySettingComparison.setShowDetail(DmShared.iNull(txtHDetail.getTag()).toString());
                    acDisplaySettingComparison.setShowNarration(DmShared.iNull(txtHNarration.getTag()).toString());
                    acDisplaySettingComparison.setShowZeroValueAc(DmShared.iNull(txtHZeroValue.getTag()).toString());
                    acDisplaySettingComparison.setYearPrefix(DmClient.getVoucherPrefix());
                    acDisplaySettingComparison.setYearPrefix2(DmClient.getVoucherPrefix());

                    if (callBackIniDefault != null) {
                        callBackIniDefault.onSuccess(acDisplaySettingComparison);
                    }
                } else {
                }
            }
        });
    }

    private void fillValue() {
        if (acDisplaySettingComparison != null) {
            txtDateFrom.setText(acDisplaySettingComparison.getDateFrom());
            txtDateTo.setText(acDisplaySettingComparison.getDateTo());
            txtLstDateFrom.setText(acDisplaySettingComparison.getLstDateFrom());
            txtLstDateTo.setText(acDisplaySettingComparison.getLstDateTo());
            txtHDetail.setTag(acDisplaySettingComparison.getShowDetail());
            txtHDetail.setText(DmShared.iNull(acDisplaySettingComparison.getShowDetail()).intValue() == 1 ? "Yes" : "No");
            txtHNarration.setTag(acDisplaySettingComparison.getShowNarration());
            txtHNarration.setText(DmShared.iNull(acDisplaySettingComparison.getShowNarration()).intValue() == 1 ? "Yes" : "No");
            txtHOpening.setTag(acDisplaySettingComparison.getIncludeOpening());
            txtHOpening.setText(DmShared.iNull(acDisplaySettingComparison.getIncludeOpening()).intValue() == 1 ? "Yes" : "No");
            txtHZeroValue.setTag(acDisplaySettingComparison.getShowZeroValueAc());
            txtHZeroValue.setText(DmShared.iNull(acDisplaySettingComparison.getShowZeroValueAc()).intValue() == 1 ? "Yes" : "No");
            txtHSite.setTag(acDisplaySettingComparison.getSiteCodeList());
            txtHSite.setText(acDisplaySettingComparison.getSiteNameList());
            txtClosing.setText(acDisplaySettingComparison.getClosingStock());
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
        txtLstDateFrom.addBlurHandler(this);
        txtLstDateTo.addBlurHandler(this);
    }

    public void addCallBack(CallBack callBack) {
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    public void show(Widget widget, AcDisplaySetting_Mdl acDisplaySetting) {
        this.acDisplaySettingComparison = acDisplaySetting;
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
            if (DmShared.xNull(txtHSite.getTag()).trim().equals("") && acDisplaySettingComparison != null) {
                txtHSite.setTag(acDisplaySettingComparison.getSiteCodeList());
                txtHSite.setText(acDisplaySettingComparison.getSiteNameList());
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

            if (!DmShared.dNull(txtLstDateFrom.getText()).trim().equalsIgnoreCase("") && !DmShared.dNull(txtLstDateTo.getText()).trim().equalsIgnoreCase("")) {
                if (DmShared.convertDate(txtLstDateFrom.getText()).getTime() > DmShared.convertDate(txtLstDateTo.getText()).getTime()) {
                    new DmAlertMessageBox("Lst From date should be less than Lst To Date", txtLstDateFrom).show();
                    return;
                }
            }

            acDisplaySettingComparison = new AcDisplaySetting_Mdl();
            acDisplaySettingComparison.setDateFrom(DmShared.dNull(txtDateFrom.getText()));
            acDisplaySettingComparison.setDateTo(DmShared.dNull(txtDateTo.getText()));
            acDisplaySettingComparison.setLstDateFrom(DmShared.dNull(txtLstDateFrom.getText()));
            acDisplaySettingComparison.setLstDateTo(DmShared.dNull(txtLstDateTo.getText()));
            acDisplaySettingComparison.setClosingStock(DmShared.vNull(txtClosing.getText()).toString());
            acDisplaySettingComparison.setIncludeOpening(DmShared.iNull(txtHOpening.getTag()).toString());
            acDisplaySettingComparison.setLedgerAcNameList(DmShared.xNull(txtHLedger.getText()));
            acDisplaySettingComparison.setLedgerAcCodeList(DmShared.xNull(txtHLedger.getTag()));
            acDisplaySettingComparison.setSiteCodeList(DmShared.xNull(txtHSite.getTag()));
            acDisplaySettingComparison.setSiteNameList(DmShared.xNull(txtHSite.getText()));
            acDisplaySettingComparison.setShowDetail(DmShared.iNull(txtHDetail.getTag()).toString());
            acDisplaySettingComparison.setShowNarration(DmShared.iNull(txtHNarration.getTag()).toString());
            acDisplaySettingComparison.setShowZeroValueAc(DmShared.iNull(txtHZeroValue.getTag()).toString());

            srvAsync.invokeRPC().getVoucherPrefix(acDisplaySettingComparison, new AsyncCallback<DmResult<AcDisplaySetting_Mdl>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtDateFrom).show();
                }

                @Override
                public void onSuccess(DmResult<AcDisplaySetting_Mdl> result) {
                    if (result.isStatus()) {
                        AcDisplaySettingComparison.this.callBack.onOk(result.getData());
                        AcDisplaySettingComparison.this.hide();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtDateFrom).show();
                    }
                }
            });

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
        if (event.getSource().equals(txtLstDateFrom)) {
            if (!DmShared.xNull(txtLstDateFrom.getText()).equalsIgnoreCase("")) {
                txtLstDateFrom.setText(DmClient.returnDate(txtLstDateFrom.getText()));

            }
        } else if (event.getSource().equals(txtLstDateTo)) {
            if (!DmShared.xNull(txtLstDateTo.getText()).equalsIgnoreCase("")) {
                txtLstDateTo.setText(DmClient.returnDate(txtLstDateTo.getText()));
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
