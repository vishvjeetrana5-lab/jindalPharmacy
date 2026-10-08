package aarogya.inventory.purchaseOrderDisplay.client;

import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Srv;
import aarogya.inventory.purchaseOrderDisplay.shared.ApprovedVendors_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.ArrayList;
import java.util.List;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_SrvAsync;

public class ApprovedVendors extends ApprovedVendors_Dsg implements IsWidget, ClickHandler, DmRootEvents {

    interface CallBack {

        public void ok(ApprovedVendors_Mdl model, int gridRowIndex);

        public void close();

    }

    private CallBack callBack;
    private ServiceInvoker<PurchaseOrderDisplay_SrvAsync> SrvAsync;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    private String strItemCode;
    private int intRowIndex;

    public ApprovedVendors() {
        super();
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());
        SrvAsync = new ServiceInvoker<PurchaseOrderDisplay_SrvAsync>(GWT.create(PurchaseOrderDisplay_Srv.class), PurchaseOrderDisplay.class.getName());
        gridEvents();
        manageEvents();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String itemCode, int intRowIndex) {
        strItemCode = itemCode;
        this.intRowIndex = intRowIndex;
        keyFields.clear();
        keyFields = new ArrayList<KeyFieldExtended>();
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        showVendors(strItemCode);

    }

    private void manageEvents() {
        btnCancel.addClickHandler(this);
    }

    private void showVendors(String strItemCode) {
        SrvAsync.invokeRPC().getApprovedVendors(strItemCode, new AsyncCallback<DmResult<List<ApprovedVendors_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<ApprovedVendors_Mdl>> result) {
                if (result.isStatus()) {
                    gridApprovedVendors.getGrid().getStore().add(new ApprovedVendors_Mdl());
                    gridApprovedVendors.getGrid().getStore().clear();
                    gridApprovedVendors.getGrid().getStore().addAll(result.getData());
                    gridApprovedVendors.commitChanges();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
       
    }

    public void hide() {
        this.popupPanel.hide();
    }

    private void gridEvents() {
        gridApprovedVendors.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                manageOk(gridApprovedVendors.getGrid().getStore().get(event.getRowIndex()));
            }
        });
    }

    private void manageOk(ApprovedVendors_Mdl model) {
        callBack.ok(model, intRowIndex);
        hide();
    }
}
