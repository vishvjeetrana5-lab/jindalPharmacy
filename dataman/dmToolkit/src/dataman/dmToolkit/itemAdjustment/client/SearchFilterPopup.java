package dataman.dmToolkit.itemAdjustment.client;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.itemAdjustment.shared.*;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.List;


public class SearchFilterPopup extends SearchFilterPopup_Dsg implements IsWidget, ClickHandler, BlurHandler, AttachEvent.Handler, DmRootEvents {

    //<editor-fold defaultstate="collapsed" desc="Callback">
    public interface CallBack {

        public void select(Filter_Mdl filter);

        public void close();
    }

    private CallBack callBack;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }
    //</editor-fold>

    private ServiceInvoker<ItemAdjustment_SrvAsync> srvAsync;

    public SearchFilterPopup() {
        super();
        manageEvents();
    }

    public void show(SearchPopup searchPopup) {
        displayRow(searchPopup.filter);
        srvAsync = searchPopup.srvAsync;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean withCallback) {
        popupPanel.hide();
        if (withCallback) {
            callBack.close();
        }
    }

    private void displayRow(Filter_Mdl filter) {
        txtVoucherNo.setText(filter.getVoucherNo());
        txtVoucherDate.setText(filter.getVoucherDate());
        txtRefNo.setText(filter.getRefNo());
        txtHVoucherType.setTag(filter.getVoucherTypeCode());
        txtHVoucherType.setText(filter.getVoucherTypeName());
        txtHParty.setTag(filter.getPartyCode());
        txtHParty.setText(filter.getPartyName());

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtVoucherNo.setFocus(true);
            }
        });
    }

    private void onOk() {
        Filter_Mdl filter = new Filter_Mdl();

        filter.setVoucherNo(txtVoucherNo.getText());
        filter.setVoucherDate(txtVoucherDate.getText());
        filter.setRefNo(txtRefNo.getText());
        filter.setVoucherTypeCode(txtHVoucherType.getTag());
        filter.setVoucherTypeName(txtHVoucherType.getText());
        filter.setPartyCode(txtHParty.getTag());
        filter.setPartyName(txtHParty.getText());

        callBack.select(filter);
        hide();
    }

    private void manageEvents() {
        textHelpEvents();
        popupPanel.addAttachHandler(this);
        btnOk.addClickHandler(this);
        btnClose.addClickHandler(this);
        txtVoucherDate.addBlurHandler(this);
    }

    private void textHelpEvents() {
        txtHParty.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHLedger_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLedger(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHLedger_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHParty).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHLedger_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHParty.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHParty).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHVoucherType.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVoucherType).show();
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
            hide(true);
        } else if (event.getSource() == btnOk) {
            onOk();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
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
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
        } else if (rootEvent.getEvent().getNativeEvent().getAltKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_K) {
            onOk();
        }
    }

    @Override
    public Widget asWidget() {
        return popupPanel;
    }

}
