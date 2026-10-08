package aarogya.master.packageDetail.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.packageDetail.shared.*;
import aarogya.selfToolkit.stStatic.client.STPriceListCallback;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.ServicePriceList_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class TestGridEdit<T1, T2> extends TestGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdServiceMaster_Mdl node);

        public void onDelete();
    }

    ServiceInvoker<PackageDetail_SrvAsync> srvAsync;
    private STPriceListCallback priceListCallback;
    CallBack callBack;
    private int hashCode = 0;
    private PackageDetail packageDetail;
    private boolean isAdd = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PackageDetail packageDetail) {
        this.packageDetail = packageDetail;
        this.isAdd = newNeeded;
        int cellIndex = 1;
        cellIndex = getDmGrid().getSelectedCell().getCell();
        super.showRelativeTo(currentRowIndex, cellIndex, okNeeded, newNeeded, deleteNeeded);

        txtAmount.setEnabled(false);
        txtHInvestigation.setFocus(true);
    }

    private void textHelpEvents() {
        priceListCallback = new STPriceListCallback() {

            @Override
            public void refresh(DmResult<List<ServicePriceList_Mdl>> result) {
                txtRate.setText("");
                if (result.isStatus()) {
                    for (ServicePriceList_Mdl spl : result.getData()) {
                        if (DmShared.iNull(spl.getServiceCode()).longValue() == DmShared.iNull(txtHInvestigation.getTag()).longValue()) {
                            txtRate.setText(DmClient.vNull(spl.getRate(), "0.00"));
                        }
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHInvestigation).show();
                }
                calculate();
            }
        };

        txtHInvestigation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHService(true, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInvestigation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInvestigation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInvestigation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                getServicePriceBestFit();
            }
        });
    }

    private void getServicePriceBestFit() {
        ServicePriceList_Mdl servicePriceList_Mdl = new ServicePriceList_Mdl();
        servicePriceList_Mdl.setOrganizationCode(DmShared.xNull(packageDetail.txtOrganizationName.getTag()));
        servicePriceList_Mdl.setServiceCode(DmShared.xNull(txtHInvestigation.getTag()));
        servicePriceList_Mdl.setDate(DmClient.getLoginDate());
        servicePriceList_Mdl.setRoomCatCode(packageDetail.roomCategoryCode);
        servicePriceList_Mdl.setPriceListNature(STEnum.PriceListNature.None);

        srvAsync.invokeRPC().getServicePriceBestFit(servicePriceList_Mdl, new AsyncCallback<DmResult<List<ServicePriceList_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                priceListCallback.refresh(new DmResult<List<ServicePriceList_Mdl>>());
            }

            @Override
            public void onSuccess(DmResult<List<ServicePriceList_Mdl>> result) {
                priceListCallback.refresh(result);
            }
        });
    }

    @Override
    public void onBlur(BlurEvent event) {
        if ((event.getSource() == txtQuantity) || (event.getSource() == txtRate)) {
            calculate();
        }
    }

    @Override
    public void displayRow(T1 row) {
        GrdServiceMaster_Mdl ourRow = (GrdServiceMaster_Mdl) row;

        manageDisplay();
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHInvestigation.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHInvestigation.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtQuantity.setText(DmClient.vNull(ourRow.getQuantity(), "0.00"));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));

        if (txtHInvestigation.isEnabled()) {
            txtHInvestigation.setFocus(true);
        } else {
            txtQuantity.setFocus(true);
        }
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdServiceMaster_Mdl();
    }

    @Override
    public void onOk() {
        calculate();
        if (DmShared.xNull(txtHInvestigation.getTag()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Investigation is mandatory.", txtHInvestigation).show();
            return;
        }
        if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
            return;
        }

        for (GrdServiceMaster_Mdl consumable_Mdl : (List<GrdServiceMaster_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
            if ((DmShared.xNull(consumable_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHInvestigation.getTag())))) {
                if (!DmClient.vNull(consumable_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                    new DmAlertMessageBox(DmShared.xNull(txtHInvestigation.getText()) + "(" + DmShared.xNull(txtHInvestigation.getText()) + ")" + " already exists.", txtHInvestigation).show();
                    return;
                }
            }
        }
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        GrdServiceMaster_Mdl ourRow = (GrdServiceMaster_Mdl) row;

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHInvestigation.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHInvestigation.getText()));
        ourRow.setQuantity(DmClient.vNull(txtQuantity.getText(), "0.00"));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));

        callBack.onOk(ourRow);
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        proceedDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public TestGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Investigation Detail", "400px", "120px");
        srvAsync = new ServiceInvoker<PackageDetail_SrvAsync>(GWT.create(PackageDetail_Srv.class), PackageDetail.class.getName());
        textHelpEvents();
        txtRate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtHInvestigation.setEnabled(isAdd);
    }

    private void calculate() {
        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() * DmShared.vNull(txtRate.getText()).doubleValue(), "0.00"));
    }
}
