package aarogya.pharmacy.addBatchSingle.client;

import aarogya.pharmacy.addBatchSingle.shared.*;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.datepicker.client.CalendarUtil;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.Date;
import java.util.List;

public class AddBatchSingle extends AddBatchSingle_Dsg implements IsWidget, BlurHandler, ClickHandler, DmRootEvents, AttachEvent.Handler {

    public interface CallBack {

        public void ok(TxtHBatch_Mdl batch_Mdl);

        public void close();

    }

    private CallBack callBack;
    private final ServiceInvoker<AddBatchSingle_SrvAsync> SrvAsync;
    private String itemCode = "", departmentCode = "";
    private String skuQtyForRate = "0";
    private boolean isBestBefore = false, isBarcodeActive = false;
    private String entryDate = DmClient.getLoginDate();

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void grdEvents() {
        txtHBatchNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBatch_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHBatchNo(DmShared.xNull(itemCode), DmShared.xNull(departmentCode), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBatch_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBatchNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBatch_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBatchNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBatchNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBatch_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHBatchNo.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()));
                    txtMfgDate.setText(DmShared.xNull(t1Row.getMfgDate()));
                    txtExpDate.setText(DmShared.xNull(t1Row.getExpDate()));
                    txtBestBefore.setText(DmShared.xNull(t1Row.getBestBeforeDays()));
                    txtBarcode.setText(DmShared.xNull(t1Row.getBarcode()));
                    txtMRP.setText(DmClient.vNull(t1Row.getMrp(), "0.00"));
                    txtSaleRate.setText(DmClient.vNull(t1Row.getSaleRate(), "0.00"));
                    if (isBarcodeActive) {
                        if (!txtBarcode.isEnabled()) {
                            txtBarcode.setEnabled(true);
                        }

                    } else {
                        txtBarcode.setEnabled(false);
                    }
                } else {
                    txtMfgDate.setText("");
                    txtExpDate.setText("");
                    txtBestBefore.setText("");
                    txtBarcode.setText("");
                    txtMRP.setText("");
                    txtSaleRate.setText("");
                }

            }
        });
    }

    public void show(String itemCode, String departmentCode, String entryDate, boolean isBestBefore, boolean isBarcodeActive, String skuQtyForRate, String mode, String skuName, String purchaseUnitName) {
        clear();
        if (DmShared.xNull(mode).equalsIgnoreCase("Browse")) {
            btnOk.setVisible(false);
        } else if (DmShared.xNull(mode).equalsIgnoreCase("New") || DmShared.xNull(mode).equalsIgnoreCase("Edit")) {
            btnOk.setVisible(true);
        }
        this.itemCode = itemCode;
        this.departmentCode = departmentCode;
        this.entryDate = DmShared.dNull(entryDate);
        this.isBestBefore = isBestBefore;
        this.isBarcodeActive = isBarcodeActive;
        this.skuQtyForRate = skuQtyForRate;
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        manageFlwBestBefore();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHBatchNo.setFocus(true);
            }
        });
        txtConversionSaleRate.setText("");
        if (!DmShared.xNull(skuQtyForRate).equalsIgnoreCase("") && !DmShared.xNull(skuName).equalsIgnoreCase("") && !DmShared.xNull(purchaseUnitName).equalsIgnoreCase("")) {
            if (DmShared.xNull(purchaseUnitName).trim().equalsIgnoreCase(DmShared.xNull(skuName))) {
                txtConversionSaleRate.setText(purchaseUnitName);
            } else if (!DmShared.xNull(purchaseUnitName).trim().equalsIgnoreCase(DmShared.xNull(skuName))) {
                txtConversionSaleRate.setText(purchaseUnitName + " = " + skuQtyForRate + " " + skuName);
            }
        }
    }

    private void clear() {
        txtBestBefore.setText("");
        txtBarcode.setText("");
        txtExpDate.setText("");
        txtHBatchNo.setText("");
        txtHBatchNo.setTag("");
        txtMRP.setText("");
        txtMfgDate.setText("");
        txtPackSize.setText("");
        txtConversionSaleRate.setText("");
        txtPackSize.setEnabled(false);
        txtSaleRate.setText("");
    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            callBack.close();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            saveBatch();
        }
    }

    private void saveBatch() {
        Batch_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();

            AddBatchSingle_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveNewBatch(mrTemp, new AsyncCallback<DmResult<TxtHBatch_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<TxtHBatch_Mdl> result) {
                    if (result.isStatus()) {
                        callBack.ok(result.getData());
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
            hide();
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }

    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHBatchNo).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtMRP).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtSaleRate).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtMfgDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtExpDate).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHBatchNo).show();
                break;
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
            this.hide();
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public AddBatchSingle() {
        super();
        SrvAsync = new ServiceInvoker<AddBatchSingle_SrvAsync>(GWT.create(AddBatchSingle_Srv.class), AddBatchSingle.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        grdEvents();
        txtMfgDate.addBlurHandler(this);
        txtExpDate.addBlurHandler(this);
        txtBestBefore.addBlurHandler(this);
        txtMRP.addBlurHandler(this);
        txtSaleRate.addBlurHandler(this);
    }

    private void manageFlwBestBefore() {
//        flwTdBestBefore.clear();
        if (this.isBestBefore) {
            txtMfgDate.setText("");
            txtExpDate.setText("");
            txtBestBefore.setText("");
            txtExpDate.setEnabled(false);
            txtMfgDate.setText("");
            txtMfgDate.setEnabled(true);
            txtBestBefore.setText("");
            txtBestBefore.setEnabled(true);
//            flwTdBestBefore.add(flwWithBstBefr);
        } else {
            txtExpDate.setText("");
            txtExpDate.setEnabled(true);
            txtMfgDate.setText("");
            txtMfgDate.setEnabled(false);
            txtBestBefore.setText("");
            txtBestBefore.setEnabled(false);
        }
    }

    private void calculate() {
        if (DmShared.vNull(skuQtyForRate).doubleValue() > 0) {
            if (DmShared.vNull(txtMRP.getText()).doubleValue() > 0) {
                txtMRP.setTag(DmClient.vNull((DmShared.vNull(txtMRP.getText()).doubleValue() / DmShared.vNull(skuQtyForRate).doubleValue()), DmShared.getNumberFormat(7)));
            } else {
                txtMRP.setTag("");
            }
            if (DmShared.vNull(txtSaleRate.getText()).doubleValue() > 0) {
                txtSaleRate.setTag(DmClient.vNull((DmShared.vNull(txtSaleRate.getText()).doubleValue() / DmShared.vNull(skuQtyForRate).doubleValue()), DmShared.getNumberFormat(7)));
            } else {
                txtSaleRate.setTag("");
            }
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtMfgDate) {
            if (!DmShared.xNull(txtMfgDate.getText()).equalsIgnoreCase("")) {
                txtMfgDate.setText(DmShared.dNull(STClient.getDateFromMonthYear(DmShared.xNull(txtMfgDate.getText()), false)));
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                    @Override
                    public void execute() {
                        txtBestBefore.setFocus(true);
                    }
                });
            }
        } else if (event.getSource() == txtBestBefore) {
            if (!DmShared.xNull(txtBestBefore.getText()).equalsIgnoreCase("")) {
                Date tempDate = DmShared.convertDate(DmShared.dNull(txtMfgDate.getText()));
                CalendarUtil.addDaysToDate(tempDate, DmShared.iNull(txtBestBefore.getText()).intValue());
                txtExpDate.setText(DmShared.dNull(tempDate));
            } else {
                txtExpDate.setText("");
            }
        } else if (event.getSource() == txtExpDate) {
            txtExpDate.setText(STClient.getDateFromMonthYear(txtExpDate.getText(), true));
        } else if (event.getSource().equals(txtMRP) || event.getSource().equals(txtSaleRate)) {
            calculate();
        }
    }

    private Batch_Mdl topControlSaveInitialize() {
        Batch_Mdl mrTemp = new Batch_Mdl();
        ItemProperty_Mdl propertyMdl = new ItemProperty_Mdl();

        calculate();
        propertyMdl.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        propertyMdl.setExpiryDate(DmShared.dNull(txtExpDate.getText()));
        propertyMdl.setItemCode(DmShared.iNull(itemCode).toString());
        propertyMdl.setManufacturingDate(DmShared.dNull(txtMfgDate.getText()));
        propertyMdl.setMrp(DmShared.vNull(txtMRP.getText()).toString());
        propertyMdl.setSkuMrp(DmShared.vNull(txtMRP.getTag()).toString());
        mrTemp.setIpm(propertyMdl);
        mrTemp.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        mrTemp.setSkuSaleRate(txtSaleRate.getTag());
        mrTemp.setIsBestBefore(isBestBefore);
        mrTemp.setIsBarcodeActive(isBarcodeActive);
        mrTemp.setEntryDate(entryDate);
        mrTemp.setBarcode(txtBarcode.getText());

        return mrTemp;
    }
}
