package aarogya.inventory.purchaseOrderDisplay.client;

import static aarogya.inventory.purchaseOrderDisplay.client.ApprovedVendors_Dsg.widgetName;
import aarogya.inventory.purchaseOrderDisplay.shared.ApprovedVendors_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_Srv;
import aarogya.inventory.purchaseOrderDisplay.shared.GrdGrnPurchaseHistory_Mdl;
import aarogya.inventory.purchaseOrderDisplay.shared.TxtHSupplier_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;
import aarogya.inventory.purchaseOrderDisplay.shared.PurchaseOrderDisplay_SrvAsync;

public class ItemDetail<T1, T2> extends ItemDetail_Dsg<T1, T2> implements ClickHandler, DmRootEvents {

    @Override
    public T1 getNewRow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    interface CallBack {

        public void ok(ApprovedVendors_Mdl model, int gridRowIndex);

        public void close();

    }

    private ItemDetail.CallBack callBack;
    private ServiceInvoker<PurchaseOrderDisplay_SrvAsync> SrvAsync;
    private List<KeyFieldExtended> keyFields = new ArrayList<KeyFieldExtended>();
    private String strItemCode;
    private int intRowIndex;

    public ItemDetail(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);

        SrvAsync = new ServiceInvoker<PurchaseOrderDisplay_SrvAsync>(GWT.create(PurchaseOrderDisplay_Srv.class), PurchaseOrderDisplay.class.getName());
        grdPrevGrnPurchase.getHelper().addFilter("supplierName", DmEnum.DmDataType.STRING, DmEnum.DmOperator.BEGINSWITH, "", null, DmEnum.DmOperand.AND);
        gridEvents();
        textHelpEvents();
    }

    public void addCallBack(ItemDetail.CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String itemCode, int intRowIndex) {
        super.showCenter(1, true, true, true);
        strItemCode = itemCode;
        this.intRowIndex = intRowIndex;
        keyFields.clear();
        keyFields = new ArrayList<KeyFieldExtended>();
        showVendors(strItemCode);
        grdPrevGrnPurchase.reQuery();
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
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void showItemHistory(String strItemCode) {
        SrvAsync.invokeRPC().getPrevGrn(strItemCode, grdPrevGrnPurchase.getHelper(), true, strItemCode, new AsyncCallback<DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> result) {
                if (result.isStatus() && result.getData() != null) {
                    grdPrevGrnPurchase.setData(result.getData(), result.getPaging());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onOk() {
        proceedOk();
    }

    @Override
    public void onNew() {
        
    }

    @Override
    public void onDelete() {
        
    }

    @Override
    public void onClose() {
        this.hide();
    }

    @Override
    public void manageOk(T1 row) {
        ApprovedVendors_Mdl model=new ApprovedVendors_Mdl();
        model.setSharedKeyFieldCode(DmShared.xNull(txtHSupplier.getTag()));
        model.setSharedKeyFieldName(DmShared.xNull(txtHSupplier.getText()));
        model.setRate(DmClient.vNull(txtHSupplier.getTag2nd(),"0.00"));
        this.hide();
        callBack.ok(model, intRowIndex);
    }

    @Override
    public void manageDelete() {
            }

    @Override
    public void displayRow(Object row) {
            }

    private void gridEvents() {
        gridApprovedVendors.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                txtHSupplier.setText(gridApprovedVendors.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldName());
                txtHSupplier.setTag(gridApprovedVendors.getGrid().getStore().get(event.getRowIndex()).getSharedKeyFieldCode());
                txtHSupplier.setTag2nd(gridApprovedVendors.getGrid().getStore().get(event.getRowIndex()).getRate());
//                manageOk(gridApprovedVendors.getGrid().getStore().get(event.getRowIndex()));
            }
        });

       grdPrevGrnPurchase.getDmGrid().getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
             @Override
            public void onRowClick(RowClickEvent event) {
                    txtHSupplier.setText(grdPrevGrnPurchase.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSupplierName());
                    txtHSupplier.setTag(grdPrevGrnPurchase.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getSupplierCode());
                    txtHSupplier.setTag2nd(grdPrevGrnPurchase.getDmGrid().getGrid().getSelectionModel().getSelectedItem().getRate());
            }
        });
        
        grdPrevGrnPurchase.addCallBack(new DmGridPaging.CallBack<String>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String param) {
                SrvAsync.invokeRPC(blnToEndOfFile).getPrevGrn(strItemCode, helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>> result) {
                        if (result.isStatus() && result.getData() != null) {
                            grdPrevGrnPurchase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public String getParam() {
                return strItemCode;
            }

        });

    }
    
    private void textHelpEvents(){
        txtHSupplier.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSupplier_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSupplierPT(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHSupplier_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSupplier).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSupplier_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSupplier.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSupplier).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSupplier_Mdl tRow) {
                
            }
        });
    }
}
