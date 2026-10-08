package aarogya.master.priceListMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import aarogya.master.priceListMaster.shared.GrdItemDetail_Mdl;
import aarogya.master.priceListMaster.shared.PriceListMaster_Srv;
import aarogya.master.priceListMaster.shared.PriceListMaster_SrvAsync;
import aarogya.master.priceListMaster.shared.SearchValue_Mdl;

public class ItemGridEdit<T1, T2> extends ItemGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, BlurHandler {

    private final ServiceInvoker<PriceListMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private GrdItemDetail_Mdl parentRow;
    private PriceListMaster clsParent;
    private String recordType;
    private int rateGridRow = -1;

    interface CallBack {

        public void refresh();
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, PriceListMaster master) {
        this.clsParent = master;
        super.showCenter(currentRowIndex, okNeeded, false, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRate.setFocus(true);
            }
        });
        manageDisplay();
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        parentRow = new GrdItemDetail_Mdl();
        txtItemCategory.setText(DmShared.xNull(ourRow.getItemCatName()));
        txtItemCategory.setTag(DmShared.xNull(ourRow.getItemCatCode()));
        txtItemName.setTag(DmShared.xNull(ourRow.getItemCode()));
        txtItemName.setText(DmShared.xNull(ourRow.getItemName()));
        txtUnit.setTag(DmShared.xNull(ourRow.getUnitCode()));
        txtUnit.setText(DmShared.xNull(ourRow.getUnitName()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtDiscount.setText(DmClient.vNull(ourRow.getDiscountAmount(), "0.00"));
        txtDiscountPer.setText(DmShared.xNull(ourRow.getDiscountPercentage()));
        txtPSNo.setText(DmClient.vNull(ourRow.getPrintSNo(), "0"));

        recordType = DmShared.xNull(ourRow.getRecordType());
    }

    private void manageDisplay() {
        txtItemName.setEnabled(false);
        txtItemCategory.setEnabled(false);
        txtUnit.setEnabled(false);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        if (checkVld()) {
            new DmConfirmMessageBox("Are you sure? You want to save price.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                        saveRecord(saveInitialize(new GrdItemDetail_Mdl()));
                    }
                }
            }).show();
        }
    }

    private boolean checkVld() {
        if (DmShared.vNull(txtRate.getText()).doubleValue() <= 0) {
            new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
            return false;
        }

        if (DmShared.vNull(txtDiscount.getText()).doubleValue() > DmShared.vNull(txtRate.getText()).doubleValue()) {
            new DmAlertMessageBox("Discount cannot be greater than rate.", txtDiscount).show();
            return false;
        }

        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0  &&  DmShared.vNull(txtDiscount.getText()).doubleValue() > 0) {
            new DmAlertMessageBox("Discount percentage and discount amount both cannot be greater than 0.", txtDiscount).show();
            return false;
        }
        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 100) {
            new DmAlertMessageBox("Discount percentage cannot be greater than 100.", txtDiscountPer).show();
            return false;
        }
        return true;
    }

    @Override
    public void manageOk(T1 row) {
        final GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        saveInitialize(ourRow);
        callBack.refresh();
    }

    private GrdItemDetail_Mdl saveInitialize(GrdItemDetail_Mdl ourRow) {
        ourRow.setItemCatName(DmShared.xNull(txtItemCategory.getText()));
        ourRow.setItemCode(DmShared.xNull(txtItemName.getTag()));
        ourRow.setItemName(DmShared.xNull(txtItemName.getText()));
        ourRow.setUnitCode(DmShared.xNull(txtUnit.getTag()));
        ourRow.setUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setDiscountAmount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setDiscountPercentage(DmShared.xNull(txtDiscountPer.getText()));
        ourRow.setPrintSNo(DmClient.vNull(txtPSNo.getText(), "0"));

        ourRow.setApplicableOn(DmShared.xNull(clsParent.txtApplicableOn.getTag()));
        ourRow.setRecordType(DmShared.xNull(recordType));
        return ourRow;
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        manageDelete();
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        new DmConfirmMessageBox("Are you sure? You want to Delete price.", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    srvAsync.invokeRPC().deleteRecord(txtItemName.getTag(), getParam(), new AsyncCallback<DmResult<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtItemName).show();
                        }

                        @Override
                        public void onSuccess(DmResult<String> result) {
                            if (result.isStatus()) {
                                callBack.refresh();
                                hide();
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtItemName).show();
                            }
                        }
                    });
                }
            }
        }).show();
    }

    private SearchValue_Mdl getParam() {
        SearchValue_Mdl param = new SearchValue_Mdl();
        if (DmShared.dNull(clsParent.txtApplicableDate.getText()).equalsIgnoreCase("")) {
            param.setApplicableDate(DmShared.dNull(DmClient.getStartDate()));
        } else {
            param.setApplicableDate(DmShared.dNull(clsParent.txtApplicableDate.getText()));
        }
        param.setRateListCode(clsParent.txtHPriceList.getTag());
        param.setItemcode(txtItemName.getTag());

        return param;
    }

    public ItemGridEdit(DmGrid<T1, T2> grid, String widgetName) {
        super(grid, widgetName, "425px", "196px");//<Ak>
        srvAsync = new ServiceInvoker<>(GWT.create(PriceListMaster_Srv.class), PriceListMaster.class.getName());
        txtDiscount.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtRate.addBlurHandler(this);
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
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
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDiscountPer || event.getSource() == txtDiscount) {
            calculate();
        }
    }

    private void calculate() {
        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            txtDiscount.setText("0.00");
        }
        if (DmShared.vNull(txtDiscount.getText()).doubleValue() > 0) {
            txtDiscountPer.setText("0.00");
        }
    }

    private void saveRecord(GrdItemDetail_Mdl mrTemp) {
        SearchValue_Mdl param = getParam();
        srvAsync.invokeRPC().saveRecord(mrTemp, param, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtRate).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    proceedOk();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtRate).show();
                }
            }
        });
    }
}
