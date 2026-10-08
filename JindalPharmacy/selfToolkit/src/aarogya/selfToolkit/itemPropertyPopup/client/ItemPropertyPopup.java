package aarogya.selfToolkit.itemPropertyPopup.client;

import aarogya.selfToolkit.itemPropertyPopup.shared.*;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemBatchType;
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
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemPropertyPopup extends ItemPropertyPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void onOk(ItemProperty_Mdl property_Mdl);

        public void onClose();
    }
    private CallBack callBack;
    private ServiceInvoker<ItemPropertyPopup_SrvAsync> SrvAsync;
    private ItemProperty_Mdl fillData;
    private ArrayList<String> arrPrpty;
    private String properties;
    private boolean bestBefore;
    private boolean isIbCodeRequired = false, flagBatchHelpAdded = false, isOpening = false, isCheckExpiryDate = false;
    private String ibCode, vDate, skuMrp = "";
    ItemBatchType batchType;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(String mode, String properties, String isBestBefore, ItemProperty_Mdl property_Mdl, String vDate, boolean isOpening,boolean isCheckExpiryDate, ItemBatchType batchType) {
        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);
        flwOk.clear();
        flwOk.add(btnOk);
        flwOk.setVisible(true);
        clear();
        this.vDate = vDate;
        initializeViaList();
        this.bestBefore = DmShared.iNull(isBestBefore).intValue() == 1;
        this.properties = properties;
        this.isOpening = isOpening;
        this.isCheckExpiryDate = isCheckExpiryDate;
        fillData = property_Mdl;
        setFlwPanels();
        displayRow(property_Mdl);
        this.batchType = batchType;

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (flwMain.getWidgetCount() > 0) {
                    if (flwMain.getWidget(0).equals(flwColour)) {
                        txtHColour.setFocus(true);
                    } else if (flwMain.getWidget(0).equals(flwSize)) {
                        txtHSize.setFocus(true);
                    } else if (flwMain.getWidget(0).equals(flwMRP)) {
                        txtMRP.setFocus(true);
                    } else if (flwMain.getWidget(0).equals(flwBatchNo)) {
                        txtHBatchNo.setFocus(true);
                    } else if (flwMain.getWidget(0).equals(flwMfgDate)) {
                        txtMfgDate.setFocus(true);
                    } else if (flwMain.getWidget(0).equals(flwBestBefore)) {
                        txtBestBefore.setFocus(true);
                    } else if (flwMain.getWidget(0).equals(flwExpiryDate)) {
                        txtExpiryDate.setFocus(true);
                    }
                }
            }
        });
        if (DmShared.xNull(mode).equalsIgnoreCase("Browse")) {
            flwOk.clear();
            flwOk.setVisible(false);
        }
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.popupPanel.hide();
        if (isCallEvent) {
            this.callBack.onClose();
        }
    }

    private void textHelp() {
        txtHColour.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHColour(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHColour).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHColour.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHColour).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSize.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSize(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSize).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSize.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSize).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHBatchNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<BatchNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getBatchNo(DmShared.xNull(fillData.getItemCode()), isIbCodeRequired, isOpening, batchType, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<BatchNo_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBatchNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<BatchNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBatchNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBatchNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, BatchNo_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHColour.setText(DmShared.xNull(t1Row.getColour()));
                    txtHColour.setTag(DmShared.xNull(t1Row.getColourCode()));
                    txtHSize.setText(DmShared.xNull(t1Row.getSize()));
                    txtHSize.setTag(DmShared.xNull(t1Row.getSizeCode()));

                    txtMRP.setText(DmClient.vNull(t1Row.getMrp(), "0.00"));

                    txtMfgDate.setText(DmShared.dNull(t1Row.getManufacturingDate()));
                    txtExpiryDate.setText(DmShared.dNull(t1Row.getExpiryDate()));

                    Date mfgDate = DmShared.convertDate(txtMfgDate.getText());
                    Date expDate = DmShared.convertDate(txtExpiryDate.getText());
                    if (mfgDate != null && expDate != null) {
                        txtBestBefore.setText(String.valueOf(CalendarUtil.getDaysBetween(mfgDate, expDate)));
                    }

                    if (isIbCodeRequired) {
                        skuMrp = DmClient.vNull(t1Row.getSkuMrp(), DmShared.getNumberFormat(7));
                    } else {
                        calculate();
                    }
                }
            }
        });

    }

    private void initializeViaList() {
        vl.get().clear();
        vl.add(btnOk);
        vl.add(btnCancel);
    }

    private void setFlwPanels() {
        flwMain.clear();
        String[] propertyList = properties.split(",");
        arrPrpty = new ArrayList<>();
        for (String list : propertyList) {
            arrPrpty.add(list);
        }
        if (arrPrpty.contains(ItemProperty_Mdl.Properties.BATCH_NO.getPropertyManualCode())) {
            flwMain.add(flwBatchNo);
            flagBatchHelpAdded = true;
        } else {
            flagBatchHelpAdded = false;
        }
        manageBatchableControls();
        if (arrPrpty.contains(ItemProperty_Mdl.Properties.EXPIRY_DATE.getPropertyManualCode())) {
            if (bestBefore) {
                flwMain.add(flwMfgDate);
                flwMain.add(flwBestBefore);
                txtExpiryDate.setEnabled(false);
            } else {
                txtExpiryDate.setEnabled(true);
            }
            flwMain.add(flwExpiryDate);
        }
        if (arrPrpty.contains(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())) {
            flwMain.add(flwColour);
        }
        if (arrPrpty.contains(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())) {
            flwMain.add(flwSize);
        }
        if (arrPrpty.contains(ItemProperty_Mdl.Properties.MRP.getPropertyManualCode())) {
            flwMain.add(flwMRP);
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {
            onOk();
        }
    }

    private void displayRow(ItemProperty_Mdl ourRow) {
        txtHColour.setTag(DmShared.xNull(ourRow.getColourCode()));
        txtHSize.setTag(DmShared.xNull(ourRow.getSizeCode()));
        txtHColour.setText(DmShared.xNull(ourRow.getColourName()));
        txtHSize.setText(DmShared.xNull(ourRow.getSizeName()));
        txtHBatchNo.setText(DmShared.xNull(ourRow.getBatchNo()));
        txtExpiryDate.setText(DmShared.xNull(ourRow.getExpiryDate()));
        txtMRP.setText(DmShared.xNull(ourRow.getMrp()));
        txtMfgDate.setText(DmShared.dNull(ourRow.getManufacturingDate()));
        if (DmShared.vNull(ourRow.getSkuQtyForRate()) != 0) {
            txtRateForSKUQty.setTag2nd(DmShared.xNull(ourRow.getSkuName()));
            txtRateForSKUQty.setTag(DmShared.xNull(ourRow.getSkuQtyForRate()));
            txtRateForSKUQty.setText(" / " + " " + DmShared.xNull(ourRow.getSkuQtyForRate()) + " " + DmShared.xNull(ourRow.getSkuName()));
        }

        Date mfgDate = DmShared.convertDate(txtMfgDate.getText());
        Date expDate = DmShared.convertDate(txtExpiryDate.getText());
        if (mfgDate != null && expDate != null) {
            txtBestBefore.setText(String.valueOf(CalendarUtil.getDaysBetween(mfgDate, expDate)));
        }

    }

    private ItemProperty_Mdl initializeValues() {
        ItemProperty_Mdl ourRow = new ItemProperty_Mdl();
        calculate();
        ourRow.setItemCode(DmShared.xNull(fillData.getItemCode()));
        ourRow.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        ourRow.setManufacturingDate(DmShared.xNull(txtMfgDate.getText()));
        ourRow.setBestBeforeDays(DmShared.xNull(txtBestBefore.getText()));
        ourRow.setExpiryDate(DmShared.xNull(txtExpiryDate.getText()));
        ourRow.setColourCode(DmShared.xNull(txtHColour.getTag()));
        ourRow.setColourName(DmShared.xNull(txtHColour.getText()));
        ourRow.setSizeCode(DmShared.xNull(txtHSize.getTag()));
        ourRow.setSizeName(DmShared.xNull(txtHSize.getText()));
        ourRow.setMrp(DmShared.xNull(txtMRP.getText()));
        ourRow.setFromOpening(isOpening);
        ourRow.setIsCheckExpiryDate(isCheckExpiryDate);
        ourRow.setSkuQtyForRate(DmShared.xNull(txtRateForSKUQty.getTag()));
        ourRow.setSkuName(DmShared.xNull(txtRateForSKUQty.getTag2nd()));
        ourRow.setSkuMrp(DmShared.xNull(skuMrp));
        return ourRow;
    }

    private void onOk() {
        try {
            final ItemProperty_Mdl ourRow = initializeValues();
            ItemPropertyPopup_Vld.validateProperties(properties, bestBefore ? "1" : "0", vDate, ourRow);
            if (isIbCodeRequired && flagBatchHelpAdded == false) {
                SrvAsync.invokeRPC(true).getIbCode(ourRow, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            ibCode = DmShared.xNull(result.getData());
                            if (!ibCode.trim().equals("")) {
                                ourRow.setIbCode(ibCode);
                                callBack.onOk(ourRow);
                                hide(false);
                            } else {
                                new DmAlertMessageBox("Item is not valid with selected properties!", txtHBatchNo).show();
                            }

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            } else {
                if (isIbCodeRequired) {
                    ibCode = DmShared.xNull(txtHBatchNo.getTag());
                    ourRow.setIbCode(ibCode);
                }
                callBack.onOk(ourRow);
                hide(false);
            }

        } catch (DmException e) {
            ibCode = "";
            showError(e.getErrorNo(), e.getMessage());
        }
    }

    private void showError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHBatchNo).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtMfgDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtBestBefore).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtExpiryDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHColour).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHSize).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtMRP).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtMRP).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, btnOk).show();
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
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    public ItemPropertyPopup() {
        this(false);
    }

    public ItemPropertyPopup(boolean isIbCodeRequired) {
        super(!isIbCodeRequired);
        this.isIbCodeRequired = isIbCodeRequired;
        SrvAsync = new ServiceInvoker<ItemPropertyPopup_SrvAsync>(GWT.create(ItemPropertyPopup_Srv.class), ItemPropertyPopup.class.getName());
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        txtMfgDate.addBlurHandler(this);
        txtBestBefore.addBlurHandler(this);
        txtExpiryDate.addBlurHandler(this);
        popupPanel.addAttachHandler(this);
        textHelp();
        manageDisplay();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtMfgDate) {
            if (!DmShared.xNull(txtMfgDate.getText()).equalsIgnoreCase("")) {
                txtMfgDate.setText(STClient.getDateFromMonthYear(txtMfgDate.getText(), false));
                setExpiryDate();
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtBestBefore.setFocus(true);
                    }
                });
            } else {
                setExpiryDate();
            }
        } else if (event.getSource() == txtBestBefore) {
            setExpiryDate();
        } else if (event.getSource() == txtExpiryDate) {
            txtExpiryDate.setText(STClient.getDateFromMonthYear(txtExpiryDate.getText(), true));
        }
    }

    private void setExpiryDate() {
        if (!DmShared.xNull(txtBestBefore.getText()).equalsIgnoreCase("") && !DmShared.xNull(txtMfgDate.getText()).equalsIgnoreCase("")) {
            Date tempDate = DmShared.convertDate(DmShared.dNull(txtMfgDate.getText()));
            CalendarUtil.addDaysToDate(tempDate, DmShared.iNull(txtBestBefore.getText()).intValue());
            txtExpiryDate.setText(DmShared.dNull(tempDate));
        } else {
            txtExpiryDate.setText("");
        }
    }

    private void clear() {
        txtHColour.setTag("");
        txtHColour.setText("");
        txtHSize.setTag("");
        txtHSize.setText("");
        txtHBatchNo.setText("");
        txtBestBefore.setText("");
        txtExpiryDate.setText("");
        txtMRP.setText("");
        txtMfgDate.setText("");
        vDate = "";
    }

    private void manageDisplay() {
        txtRateForSKUQty.setReadOnly(true);
    }

    private void manageBatchableControls() {
        boolean flag = true;
        if (isIbCodeRequired && flagBatchHelpAdded) {
            flag = false;
        }
        txtHColour.setEnabled(flag);
        txtHSize.setEnabled(flag);
        txtMRP.setEnabled(flag);
        txtMfgDate.setEnabled(flag);
        txtExpiryDate.setEnabled(flag);
        txtBestBefore.setEnabled(flag);
    }

    private void calculate() {
        if (!isIbCodeRequired || (isIbCodeRequired && flagBatchHelpAdded == false)) {
            skuMrp = "0";
            if (arrPrpty.contains(ItemProperty_Mdl.Properties.MRP.getPropertyManualCode())) {
                skuMrp = DmClient.vNull(DmShared.vNull(txtMRP.getText()).doubleValue() / DmShared.vNull(txtRateForSKUQty.getTag()).doubleValue(), DmShared.getNumberFormat(7));
            }
        }
    }
}
