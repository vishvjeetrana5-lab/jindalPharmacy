package aarogya.selfToolkit.stockDetailPopup.client;

import aarogya.selfToolkit.stockDetailPopup.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.toolTip.shared.ToolTip_Mdl;

public class StockDetailPopup extends StockDetailPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void onClose();
    }

    private CallBack callBack;
    private final ServiceInvoker<StockDetailPopup_SrvAsync> SrvAsync;
//    private String itemCode = "", ibCode = "";
//    private ItemProperty_Mdl propertyPopup_Mdl;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String itemCode, ItemProperty_Mdl propertyPopup_Mdl) {
//        this.itemCode = itemCode;
//        this.ibCode = ibCode;
//        this.propertyPopup_Mdl = propertyPopup_Mdl;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        getGridDetail(itemCode, propertyPopup_Mdl);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.onClose();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            hide(true);
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
    public Widget asWidget() {
        return this.popupPanel;
    }

    public StockDetailPopup() {
        super();
        SrvAsync = new ServiceInvoker<StockDetailPopup_SrvAsync>(GWT.create(StockDetailPopup_Srv.class), StockDetailPopup.class.getName());
        btnCancel.addClickHandler(this);
        popupPanel.addAttachHandler(this);

    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            hide(true);
        }
    }

    private void getGridDetail(String itemCode, final ItemProperty_Mdl propertyPopup_Mdl) {
        grdStockDetail.getGrid().getStore().clear();
        txtItem.setText("");
        SrvAsync.invokeRPC().getShowStock(itemCode, propertyPopup_Mdl, new AsyncCallback<DmResult<List<StockDetailGrdEdit_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<StockDetailGrdEdit_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.getData().size() > 0) {
                        txtItem.setText(DmShared.xNull(result.getData().get(0).getSharedKeyFieldName()));
                        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
                    }
                    grdStockDetail.getGrid().getStore().clear();
                    grdStockDetail.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        }
        );
    }
}
