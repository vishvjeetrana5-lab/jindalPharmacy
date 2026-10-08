package aarogya.master.priceListMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.master.priceListMaster.shared.PriceListMaster_Srv;
import aarogya.master.priceListMaster.shared.PriceListMaster_SrvAsync;
import aarogya.master.priceListMaster.shared.PriceList_Mdl;
import aarogya.master.self.shared.Master_Model;

public class CreatePriceListPopup extends CreatePriceListPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    interface CallBack {

        public void onOk(PriceList_Mdl mrTemp);

        public void onClose();
    }
    private CallBack callBack;
    private final ServiceInvoker<PriceListMaster_SrvAsync> srvAsync;
    private PriceListMaster clsParent;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public CreatePriceListPopup() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(PriceListMaster_Srv.class), PriceListMaster.class.getName());
        toolContainer.setTitle(widgetName);
        manageEvent();
        txtHelpEvents();
    }

    public void show(PriceListMaster organizationMaster) {
        popupPanel.show();
        popupPanel.showRelativeTo(organizationMaster.lblPriceList);
        popupPanel.setGlassEnabled(true);
        clsParent = organizationMaster;
        clear();
        txtName.setFocus(true);
    }

    private void clear() {
        txtHRateType.setText("");
        txtHRateType.setTag("");
        txtHRateType.setTag2nd("");
        txtName.setText("");
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
    }

    private void txtHelpEvents() {
        txtHRateType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPriceListType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRateType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHApplicableOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHApplicableOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHApplicableOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHApplicableOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHApplicableOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    // applicableOn();
//                    grdPriceListDetail.commitChanges();
                }

            }
        });
    }

    private PriceList_Mdl saveInitialize() {
        PriceList_Mdl mrTemp = new PriceList_Mdl();
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setPriceListTypeTag(DmShared.xNull(txtHRateType.getTag()));
        mrTemp.setPriceListTypeName(DmShared.xNull(txtHRateType.getText()));
        mrTemp.setApplicableOn(DmShared.xNull(txtHApplicableOn.getTag()));
        mrTemp.setApplicableOnName(DmShared.xNull(txtHApplicableOn.getText()));
        return mrTemp;
    }

    private PriceList_Mdl savePriceList() {

        final PriceList_Mdl mrTemp = saveInitialize();
        try {
            srvAsync.invokeRPC().savePriceList(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        mrTemp.setSharedKeyFieldCode(result.getData());
                        mrTemp.setApplicableOn(result.getValue1());
                        hide(false, mrTemp);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                    }
                }
            });
        } catch (DmException ex) {
            new DmAlertMessageBox(ex.getMessage(), txtName).show();
        }
        return mrTemp;
    }

    private void hide(boolean isCallEvent, PriceList_Mdl mrTemp) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
        }
        if (mrTemp != null) {
            this.callBack.onOk(mrTemp);
        }
    }

    public void hide() {
        hide(false, null);
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnClose) {
            hide(true, null);
        } else if (event.getSource() == btnOk) {
            try {
                DmShared.isValidText(DmShared.xNull(txtName.getText()), "Name", 100);
                DmShared.isValidText(DmShared.xNull(txtHRateType.getText()), "Price List Type", 200);
//                DmShared.isValidText(DmShared.xNull(txtHApplicableOn.getText()), "List For", 300);
                savePriceList();
            } catch (DmException e) {
                switch (e.getErrorNo()) {
                    case 100:
                        new DmAlertMessageBox(e.getMessage(), txtName).show();
                        break;
                    case 200:
                        new DmAlertMessageBox(e.getMessage(), txtHRateType).show();
                        break;
                    case 300:
                        new DmAlertMessageBox(e.getMessage(), txtHApplicableOn).show();
                        break;
                    default:
                        new DmAlertMessageBox(e.getMessage(), txtName).show();
                        break;
                }
            }

        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true, null);
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
