package aarogya.inventory.quotation.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import aarogya.inventory.indentSearch.client.IndentSearch;
import aarogya.inventory.quotation.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.quotation.shared.GrdItemDetail_Mdl;
import aarogya.inventory.quotation.shared.Quotation_Srv;
import aarogya.inventory.quotation.shared.Quotation_SrvAsync;
import aarogya.inventory.quotation.shared.Quotation_Vld;
import aarogya.inventory.quotation.shared.TxtHItemName_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class QuotationGridEdit<T1, T2> extends QuotationGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler, ClickHandler {

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtProperties) {
                flagFromlblClick = false;
                showPropertyPopUp();
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtProperties) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(Quotation_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), "0", quotationEntry.txtQuotationDate.getText(), propertyPopup_Mdl);
                } catch (Exception e) {
                    showPropertyPopUp();
                }
            }
        }
    }

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }

    private ServiceInvoker<Quotation_SrvAsync> srvAsync;
    private CallBack callBack;
    private Quotation quotationEntry;
    final IndentSearch indentSearch = new IndentSearch();
    private int hashCode = 0;
    final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl;
    String skuQuantity = "", conversionRate = "", skuDecimalPlaces = "";
    String enquiryDocid = "", enquiryV_SNo = "", unit = "", itsBasedOn = "", itemGSTNature = "";
    boolean flagFromlblClick = false;
    String prvItem = "";
    boolean isFirstShow = true;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtRemark.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    txtDiscountPer.setFocus(true);
                }
            }
        });
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
                txtQuantity.setFocus(true);
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                fillIndentDetails(listKF);
            }

            @Override
            public void close() {
                txtHItemName.setFocus(true);
            }
        });
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.xNull(quotationEntry.txtHQuotationType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemName_Mdl t1Row) {
                onItemSelection(blnStatus, t1Row);
            }
        });

        txtHITSType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGstType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHITSType.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHITSType.getTag()))) {
                    //   resetItemType();
                    txtHITSType.setTag2nd(DmShared.xNull(txtHITSType.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    //getGSTS
                    getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, quotationEntry.location, txtHITSType.getTag(), cbIsManualItsType.getValue());
                }
            }
        });

    }

    private void onItemSelection(boolean blnStatus, TxtHItemName_Mdl t1Row) {
        if (DmShared.iNull(prvItem).longValue() != DmShared.iNull(t1Row.getSharedKeyFieldCode()).longValue()) {
            resetItems();
            prvItem = DmShared.xNull(t1Row.getSharedKeyFieldCode());
        }
        if (blnStatus && t1Row != null) {
            propertyPopup_Mdl = new ItemProperty_Mdl();
            txtProperties.setText("");
            txtUnit.setTag2nd(t1Row.getDecimalPlacesPurchaseUnit());
            txtUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
            txtUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));

            txtHSN.setText(DmShared.xNull(t1Row.getHsnNo()));
            txtHSN.setTag(DmShared.xNull(t1Row.getHsnCode()));

            itsBasedOn = DmShared.iNull(t1Row.getItsBasedOn()).toString();

            itemGSTNature = DmShared.xNull(t1Row.getItemGstNature());

            unit = DmShared.xNull(t1Row.getDisplayUnit());
            conversionRate = t1Row.getConversionRate();
            skuDecimalPlaces = t1Row.getDecimalPlaces();
            //  formatQuantity();
            txtHItemName.setTag2nd(t1Row.getItemProperty());
            flagFromlblClick = false;
            txtHItemName.setTag2nd(t1Row.getItemProperty());
            showPropertyPopUp();
        }
        getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, quotationEntry.location, txtHITSType.getTag(), cbIsManualItsType.getValue());
    }

    private void getGSTS(String itemCode, String itemRate, String itsBasedOn, String location, String gstTypeCode, final boolean isManualGst) {
        srvAsync.invokeRPC(true).getGSTS(itemCode, itemRate, itsBasedOn, location, gstTypeCode, isManualGst, new AsyncCallback<DmResult<List<GSTS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GSTS_Mdl>> result) {
                if (result.isStatus() && result.getData() != null) {
                    if (!isManualGst) {
                        txtHITSType.setText(result.getValue2());
                        txtHITSType.setTag(result.getValue1());
                        txtHITSType.setTag2nd(result.getValue1());
                    }
                    setGSTS(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                }
            }
        });
    }

    private void setGSTS(List<GSTS_Mdl> lstITS) {
        String taxPer = "";
        resetTaxDetail();
        if (lstITS != null && lstITS.size() > 0) {
            for (GSTS_Mdl iTS_Mdl : lstITS) {
                taxPer = "";

                taxPer = DmClient.vNull(iTS_Mdl.getAtRate(), "0.00");
                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(quotationEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(DmShared.xNull(iTS_Mdl.getGstsHeadName()));
                    txtSgstUgstPer.setText(DmShared.xNull(taxPer));
                    txtSGSTPer.setText(DmShared.xNull(taxPer));

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(quotationEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(DmShared.xNull(iTS_Mdl.getGstsHeadName()));
                    txtSgstUgstPer.setText(DmShared.xNull(taxPer));
                    txtUGSTPer.setText(DmShared.xNull(taxPer));

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                    txtCGSTPer.setText(DmShared.xNull(taxPer));

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                    txtIGSTPer.setText(DmShared.xNull(taxPer));

                }
            }
        }
        calculate();
    }

    private void resetTaxDetail() {
        //SGST or UGST
        txtSgstUgstPer.setText("");
        txtSgstUgstPer.setTag("");
        txtSgstUgstPer.setTag2nd("");

        txtSgstUgst.setText("");
        txtSgstUgst.setTag("");
        txtSgstUgst.setTag2nd("");

        //SGST
        txtSGSTPer.setText("");
        txtSGSTPer.setTag("");
        txtSGSTPer.setTag2nd("");

        txtSGST.setText("");
        txtSGST.setTag("");
        txtSGST.setTag2nd("");

        //UGST
        txtUGSTPer.setText("");
        txtUGSTPer.setTag("");
        txtUGSTPer.setTag2nd("");

        txtUGST.setText("");
        txtUGST.setTag("");
        txtUGST.setTag2nd("");

        //CGST
        txtCGSTPer.setText("");
        txtCGSTPer.setTag("");
        txtCGSTPer.setTag2nd("");

        txtCGST.setText("");
        txtCGST.setTag("");
        txtCGST.setTag2nd("");

        //IGST
        txtIGSTPer.setText("");
        txtIGSTPer.setTag("");
        txtIGSTPer.setTag2nd("");

        txtIGST.setText("");
        txtIGST.setTag("");
        txtIGST.setTag2nd("");
    }
    //</editor-fold>

    private void resetItems() {
        txtUnit.setTag("");
        txtUnit.setText("");
        txtUnit.setTag2nd("");

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");
        propertyPopup_Mdl = new ItemProperty_Mdl();

        txtQuantity.setText("");
        txtQuantity.setTag("");
        txtQuantity.setTag2nd("");

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtDeliveryDate.setText("");
        txtDeliveryDate.setTag("");
        txtDeliveryDate.setTag2nd("");

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        cbIsManualItsType.setValue(false);
        cbIsManualTax.setValue(false);

        conversionRate = "";
        skuDecimalPlaces = "";
        skuQuantity = "";
        resetItemType();
    }

    private void resetItemType() {
        txtHITSType.setTag2nd("");
        txtHITSType.setText("");
        txtHITSType.setTag("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtItemValue.setText("");
        txtItemValue.setTag("");
        txtItemValue.setTag2nd("");

        resetTaxDetail();
    }

    private void showPropertyPopUp() {
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(Quotation_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag())).equalsIgnoreCase("")) {
                txtProperties.setEnabled(true);
                 propertyPopup.show(quotationEntry.topControl.getMode(), Quotation_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), "0", propertyPopup_Mdl, DmShared.dNull(quotationEntry.txtQuotationDate.getText()), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
            } else {
                txtProperties.setEnabled(false);
                if (flagFromlblClick) {
                    new DmAlertMessageBox("Properties are not applicable for this item.", txtQuantity).show();
                }
            }
        }
    }

    private void gridEvents() {
        gridIndentDetail.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (!DmShared.xNull(quotationEntry.topControl.getMode()).trim().equalsIgnoreCase("Browse")) {
                        if (event.getCellIndex() == 0) {
                            final int rowIndex = event.getRowIndex();
                            new DmConfirmMessageBox("Are you sure? You want to remove.", new DialogHideEvent.DialogHideHandler() {

                                @Override
                                public void onDialogHide(DialogHideEvent event1) {
                                    if (event1.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                        removeIndent(rowIndex);
                                    }
                                }
                            }).show();
                        }
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });
    }

    private void removeIndent(int rowIndex) {
        gridIndentDetail.getGrid().getStore().remove(rowIndex);
        gridIndentDetail.getGrid().getStore().commitChanges();
        //code
        calculateQty(true);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded) {
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        if (isFirstShow) {
            isFirstShow = false;

        }
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                txtHItemName.setFocus(true);
            }
        });

    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDeliveryDate) {
            txtDeliveryDate.setText(DmClient.returnDate(txtDeliveryDate.getText()));
        }
        if ((event.getSource() == txtQuantity) || (event.getSource() == txtRate)
                || (event.getSource() == txtDiscount) || (event.getSource() == txtDiscountPer)) {
            calculate();
        }
    }

    private void resetPopup() {

        cbIsManualItsType.setValue(false);
        cbIsManualTax.setValue(false);

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        prvItem = "";
        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");

        resetItems();

    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        propertyPopup_Mdl = new ItemProperty_Mdl();

        //clear
        resetPopup();

        manageDisplay();

        //ItemName
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag2nd(ourRow.getPropertyList());
        prvItem = DmShared.xNull(ourRow.getSharedKeyFieldCode());

        //ItemDetails
        itsBasedOn = ourRow.getItsBasedOn();
        itemGSTNature = ourRow.getItemGSTNature();
        cbIsManualTax.setValue(ourRow.isManualTax());
        cbIsManualItsType.setValue(ourRow.isManuaGstType());
        txtHITSType.setText(DmShared.xNull(ourRow.getGstTypeName()));
        txtHITSType.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHITSType.setTag2nd(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHSN.setText(DmShared.xNull(ourRow.getHsnNo()));
        txtHSN.setTag(DmShared.xNull(ourRow.getHsnCode()));

        txtUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlaces()));
        txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        conversionRate = DmShared.xNull(ourRow.getConversionRate());
        enquiryDocid = ourRow.getEnquiryDocId();
        enquiryV_SNo = ourRow.getEnquiryV_SNo();
        unit = ourRow.getDisplayUnit();
        skuQuantity = DmShared.xNull(ourRow.getSkuQuantity());
        skuDecimalPlaces = DmShared.xNull(ourRow.getSkuDecimalPlaces());
        txtDeliveryDate.setText(DmShared.dNull(ourRow.getDeliveryDate()));
        txtRemark.setTag(DmShared.xNull(ourRow.getvSNo()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));

        gridIndentDetail.getGrid().getStore().clear();
        gridIndentDetail.getGrid().getStore().addAll(ourRow.getListIndentDetail());

        hashCode = ourRow.getSharedKeyFieldHashCode();

        //Amount
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtDiscountPer.setText(ourRow.getDiscountPer());
        txtDiscount.setText(ourRow.getDiscount());
        txtTaxableAmt.setText(ourRow.getTaxableAmt());

        if (DmShared.vNull(ourRow.getSgstPer()) > 0) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if (DmShared.vNull(ourRow.getUgstPer()) > 0) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
        }

        txtSgstUgstPer.setText(ourRow.getSgstUgstPer());
        txtSgstUgst.setText(ourRow.getSgstUgst());
        txtSGSTPer.setText(ourRow.getSgstPer());
        txtSGST.setText(ourRow.getSgst());
        txtUGSTPer.setText(ourRow.getUgstPer());
        txtUGST.setText(ourRow.getUgst());
        txtCGSTPer.setText(ourRow.getCgstPer());
        txtCGST.setText(ourRow.getCgst());
        txtIGSTPer.setText(ourRow.getIgstPer());
        txtIGST.setText(ourRow.getIgst());
        txtItemValue.setText(ourRow.getItemValue());

        //set temp value for validate check
        txtSgstUgst.setTag2nd(ourRow.getSgstUgst());
        txtCGST.setTag2nd(ourRow.getCgst());
        txtIGST.setTag2nd(ourRow.getIgst());

        propertyPopup_Mdl.setColourCode(DmShared.xNull(ourRow.getColourCode()));
        propertyPopup_Mdl.setColourName(DmShared.xNull(ourRow.getColourName()));
        propertyPopup_Mdl.setSizeCode(DmShared.xNull(ourRow.getSizeCode()));
        propertyPopup_Mdl.setSizeName(DmShared.xNull(ourRow.getSizeName()));
        propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(ourRow.getItemMakeCode()));
        propertyPopup_Mdl.setItemMakeName(DmShared.xNull(ourRow.getItemMakeName()));

        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
        if (DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }
        if (!DmShared.xNull(ourRow.getEnquiryDocId()).equalsIgnoreCase("") || (ourRow.getListIndentDetail() != null && !ourRow.getListIndentDetail().isEmpty())) {
            txtHItemName.setEnabled(false);
            if (isPropertiesExists()) {
                txtProperties.setFocus(true);
            } else {
                txtQuantity.setFocus(true);
            }

        } else {
            txtHItemName.setEnabled(true);
            txtHItemName.setFocus(true);
        }

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        try {
            calculate();
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Item is mandatory.", txtHItemName).show();
                return;
            }

            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(Quotation_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), "0", quotationEntry.txtQuotationDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                    return;
                }
            }

            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.xNull(txtUnit.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Unit is mandatory.", txtUnit).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (!DmShared.dNull(txtDeliveryDate.getText()).equalsIgnoreCase("") && !DmShared.dNull(quotationEntry.txtQuotationDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(txtDeliveryDate.getText()).getTime() < (DmShared.convertDate(quotationEntry.txtQuotationDate.getText())).getTime())) {
                    new DmAlertMessageBox("Delivery Date should be greater than or equal to Quotation Date ", txtDeliveryDate).show();
                    return;
                }
            }
            if (cbIsManualTax.getValue()) {
                if (txtSgstUgst.isEnabled()) {
                    if (DmShared.compute("Math.abs(" + DmClient.vNull(DmShared.vNull(txtSgstUgst.getTag2nd()).doubleValue() - DmShared.vNull(txtSgstUgst.getText()).doubleValue(), "0.00") + ")") >= 1.0) {
                        new DmAlertMessageBox("" + DmShared.xNull(lblSgstUgst.getText()) + " is not valid", txtSgstUgst).show();
                        return;
                    }
                }
                if (txtCGST.isEnabled()) {
                    if (DmShared.compute("Math.abs(" + DmClient.vNull(DmShared.vNull(txtCGST.getTag2nd()).doubleValue() - DmShared.vNull(txtCGST.getText()).doubleValue(), "0.00") + ")") >= 1.0) {
                        new DmAlertMessageBox("CGST is not valid", txtCGST).show();
                        return;
                    }
                }
                if (txtIGST.isEnabled()) {
                    if (DmShared.compute("Math.abs(" + DmClient.vNull(DmShared.vNull(txtIGST.getTag2nd()).doubleValue() - DmShared.vNull(txtIGST.getText()).doubleValue(), "0.00") + ")") >= 1.0) {
                        new DmAlertMessageBox("IGST is not valid", txtIGST).show();
                        return;
                    }
                }

            }

            /*==============================<< Check Item dublicacy >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()))
                        && DmShared.xNull(propertyPopup_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getColourCode()))
                        && DmShared.xNull(propertyPopup_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSizeCode()))
                        && DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getItemMakeCode()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getItemMakeName()).trim().equals("")) ? "" : "<b>ItemMake:</b> " + DmShared.xNull(propertyPopup_Mdl.getItemMakeName()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHItemName).show();
                        return;
                    }
                }
            }

            /*==============================<< Check overheadLine conditions >>====================================*/
            //   OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());

            /*==============================<< Check delivery date condition >>====================================*/
            double indentOrderSkuQuantity = 0;
            String decimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue());

            if ((gridIndentDetail.getGrid().getStore().getAll().size() > 0)) {
                String reqDate = "", indentNo = "";
                for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
                    if (!DmShared.dNull(txtDeliveryDate.getText()).trim().equalsIgnoreCase("")) {

                        String tempDate = DmShared.dNull(detail_Mdl.getRequiredDate());
                        if (!DmShared.dNull(reqDate).equalsIgnoreCase("")) {
                            if ((DmShared.convertDate(reqDate).getTime() > (DmShared.convertDate(tempDate)).getTime())) {
                                reqDate = tempDate;
                                indentNo = DmShared.xNull(detail_Mdl.getSharedKeyFieldName());
                            }
                        } else {
                            reqDate = tempDate;
                            indentNo = DmShared.xNull(detail_Mdl.getSharedKeyFieldName());
                        }
                    }
                    indentOrderSkuQuantity += DmShared.vNull(detail_Mdl.getIndentQty()).doubleValue();
                }

                indentOrderSkuQuantity = indentOrderSkuQuantity / DmShared.vNull(conversionRate);
                indentOrderSkuQuantity = DmShared.vNull(DmClient.vNull(indentOrderSkuQuantity, decimalFormat));

                if (indentOrderSkuQuantity != 0) {
                    if (DmShared.vNull(txtQuantity.getText()).doubleValue() < DmShared.vNull(indentOrderSkuQuantity).doubleValue()) {
                        new DmAlertMessageBox("Quotation quantity should not be less than total indent sku quantity.", txtQuantity).show();
                        return;
                    }
                }
                if ((!DmShared.dNull(txtDeliveryDate.getText()).trim().equalsIgnoreCase("")) && !DmShared.dNull(reqDate).trim().equalsIgnoreCase("")) {
                    if ((DmShared.convertDate(txtDeliveryDate.getText()).getTime() > (DmShared.convertDate(reqDate)).getTime())) {
                        new DmConfirmMessageBox("Delivery Date should be less than or equal to Required Date for Indent No. " + indentNo + ".? You want to continue...", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                    proceedOk();
                                } else {
                                    txtDeliveryDate.setFocus(true);
                                    return;
                                }
                            }
                        }).show();
                    } else {
                        proceedOk();
                    }
                } else {
                    proceedOk();
                }
            } else {
                proceedOk();
            }

        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        // formatQuantity();

//        calculate();
        fillRowData(ourRow);

        callBack.onOk(ourRow);
    }

    protected void fillRowData(GrdItemDetail_Mdl ourRow) {
//item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));

        //itemProperty
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setProperties(DmShared.xNull(txtProperties.getText()));

        //Item Deatils
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setHsnCode(DmShared.xNull(txtHSN.getTag()));
        ourRow.setHsnNo(DmShared.xNull(txtHSN.getText()));
        ourRow.setManualTax(cbIsManualTax.getValue());
        ourRow.setManuaGstType(cbIsManualItsType.getValue());
        ourRow.setGstTypeCode(DmShared.xNull(txtHITSType.getTag()));
        ourRow.setGstTypeName(DmShared.xNull(txtHITSType.getText()));

        ourRow.setDeliveryDate(DmShared.dNull(txtDeliveryDate.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtUnit.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setvSNo(DmShared.xNull(txtRemark.getTag()));
        ourRow.setEnquiryDocId(enquiryDocid);
        ourRow.setEnquiryV_SNo(enquiryV_SNo);
        ourRow.setSkuQuantity(DmShared.xNull(skuQuantity));
        ourRow.setConversionRate(DmShared.xNull(conversionRate));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuDecimalPlaces));
        ourRow.setDecimalPlaces(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setDisplayUnit(DmShared.xNull(unit));

        //Rate
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));

        //Amount
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setDiscountPer(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
        ourRow.setDiscount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setTaxableAmt(DmClient.vNull(txtTaxableAmt.getText(), "0.00"));
        ourRow.setSgstUgstPer(DmClient.vNull(txtSgstUgstPer.getText(), "0.00"));
        ourRow.setSgstUgst(DmClient.vNull(txtSgstUgst.getText(), "0.00"));
        ourRow.setSgstPer(DmClient.vNull(txtSGSTPer.getText(), "0.00"));
        ourRow.setSgst(DmClient.vNull(txtSGST.getText(), "0.00"));
        ourRow.setUgstPer(DmClient.vNull(txtUGSTPer.getText(), "0.00"));
        ourRow.setUgst(DmClient.vNull(txtUGST.getText(), "0.00"));
        ourRow.setCgstPer(DmClient.vNull(txtCGSTPer.getText(), "0.00"));
        ourRow.setCgst(DmClient.vNull(txtCGST.getText(), "0.00"));
        ourRow.setIgstPer(DmClient.vNull(txtIGSTPer.getText(), "0.00"));
        ourRow.setIgst(DmClient.vNull(txtIGST.getText(), "0.00"));
        ourRow.setItemValue(DmClient.vNull(txtItemValue.getText(), "0.00"));

        ourRow.getListIndentDetail().clear();
        ourRow.getListIndentDetail().addAll(gridIndentDetail.getGrid().getStore().getAll());
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

    public QuotationGridEdit(DmGrid<T1, T2> grid, Quotation quotationEntry) {
        super(grid, "Item Detail", "750px", "310px");
        srvAsync = new ServiceInvoker<Quotation_SrvAsync>(GWT.create(Quotation_Srv.class), Quotation.class.getName());
        this.quotationEntry = quotationEntry;
        textHelpEvents();
        gridEvents();
        txtDeliveryDate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        manageEvents();
        showIndentSearch();
    }

    private void manageDisplay() {

        txtAmount.setEnabled(false);
        flwAddIndent.clear();
        txtProperties.setReadOnly(true);
        if (!DmShared.xNull(quotationEntry.topControl.getMode()).trim().equalsIgnoreCase("Browse")) {
            flwAddIndent.add(btnAddIndent);
        }
        //Footer Values
        txtTaxableAmt.setEnabled(false);
        txtSgstUgstPer.setEnabled(false);
        txtSGSTPer.setEnabled(false);
        txtSGST.setEnabled(false);
        txtUGSTPer.setEnabled(false);
        txtUGST.setEnabled(false);
        txtCGSTPer.setEnabled(false);
        txtIGSTPer.setEnabled(false);
        txtItemValue.setEnabled(false);
        manageTaxControls();
    }

    private void manageTaxControls() {
        txtHITSType.setEnabled(cbIsManualItsType.getValue());

        txtIGST.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtCGST.setEnabled(false);

        if (cbIsManualTax.getValue()) {
            if (DmShared.xNull(quotationEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.InterState.getId()))) {
                txtIGST.setEnabled(true);
            } else if (DmShared.xNull(quotationEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.IntraState.getId()))) {
                txtSgstUgst.setEnabled(true);
                txtCGST.setEnabled(true);
            }
        }
    }

    private void showIndentSearch() {
        btnAddIndent.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!DmShared.xNull(txtHItemName.getTag()).trim().equalsIgnoreCase("")) {
                    indentSearch.show(DmShared.xNull(quotationEntry.txtHQuotationType.getTag()), propertyPopup_Mdl, DmShared.dNull(quotationEntry.txtVendorQNDate.getText()).trim().equals("") ? DmShared.dNull(quotationEntry.txtQuotationDate.getText()) : DmShared.dNull(quotationEntry.txtVendorQNDate.getText()), DmShared.xNull(quotationEntry.voucherCategory), DmShared.xNull(quotationEntry.txtHSupplier.getTag()));
                } else {
                    new DmAlertMessageBox("Please select Item.", txtHItemName).show();
                }
            }
        });
    }

    private void fillIndentDetails(List<KeyFieldExtended> listKF) {
        srvAsync.invokeRPC().getIndentDetail(listKF, DmShared.xNull(txtHItemName.getTag()), new AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdIndentDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        gridIndentDetail.getGrid().getStore().clear();
                        gridIndentDetail.getGrid().getStore().addAll(result.getData());
                        calculateQty(false);
                        txtHItemName.setFocus(true);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                    }
                }
            }
        });
    }

    private void calculateQty(boolean isFromIndentDlt) {
        double purchaseUnitQty = 0, skuQty = 0;
        for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getIndentQtyInPurchaseUnit()).trim().equalsIgnoreCase("")) {
                purchaseUnitQty += DmShared.vNull(detail_Mdl.getIndentQtyInPurchaseUnit()).doubleValue();
                skuQty += DmShared.vNull(detail_Mdl.getIndentQty()).doubleValue();

            }
        }
        if (isFromIndentDlt) {
            if (purchaseUnitQty == skuQty) {
                if (DmShared.vNull(conversionRate) != 0) {
                    purchaseUnitQty = DmShared.vNull(purchaseUnitQty) / DmShared.vNull(conversionRate);
                }
            }
        }

        txtQuantity.setText(DmClient.vNull(purchaseUnitQty, DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));

    }

    public void calculate() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));
        txtQuantity.setTag(DmClient.vNull(DmShared.vNull(txtQuantity.getText()) * DmShared.vNull(txtRate.getText()), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() * DmShared.vNull(txtRate.getText()).doubleValue(), "0.00"));

        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            txtDiscount.setText(DmClient.vNull(DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue() * 0.01, "0.00"));
        }
        double taxableAmt = DmShared.vNull(txtAmount.getText()).doubleValue() - DmShared.vNull(txtDiscount.getText()).doubleValue();
        txtTaxableAmt.setText(DmClient.vNull(taxableAmt, "0.00"));
        txtSgstUgst.setTag2nd(DmClient.vNull(taxableAmt * DmShared.vNull(txtSgstUgstPer.getText()).doubleValue() * 0.01, "0.00"));
        if (!cbIsManualTax.getValue()) {
            txtSgstUgst.setText(txtSgstUgst.getTag2nd());
        }
        if (!(DmShared.iNull(quotationEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            txtSGSTPer.setText(txtSgstUgstPer.getText());
            txtSGST.setText(txtSgstUgst.getText());
        } else if ((DmShared.iNull(quotationEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
            txtUGSTPer.setText(txtSgstUgstPer.getText());
            txtUGST.setText(txtSgstUgst.getText());
        }
        txtCGST.setTag2nd(DmClient.vNull(taxableAmt * DmShared.vNull(txtCGSTPer.getText()).doubleValue() * 0.01, "0.00"));
        txtIGST.setTag2nd(DmClient.vNull(taxableAmt * DmShared.vNull(txtIGSTPer.getText()).doubleValue() * 0.01, "0.00"));
        if (!cbIsManualTax.getValue()) {
            txtCGST.setText(txtCGST.getTag2nd());
            txtIGST.setText(txtIGST.getTag2nd());
        }
        double total = 0;
        total = taxableAmt + DmShared.vNull(txtSgstUgst.getText()).doubleValue() + DmShared.vNull(txtCGST.getText()).doubleValue() + DmShared.vNull(txtIGST.getText()).doubleValue();
        txtItemValue.setText(DmClient.vNull(total, "0.00"));

        //Conversion
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));
        skuQuantity = DmClient.vNull(String.valueOf(DmShared.vNull(txtQuantity.getText()) * DmShared.vNull(conversionRate)), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()));

        manageDisplay();

    }

    private void manageEvents() {
        txtDiscount.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        cbIsManualItsType.addClickHandler(this);
        cbIsManualTax.addClickHandler(this);
        lblProperties.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                flagFromlblClick = true;
                showPropertyPopUp();
            }
        });
        lblProperties.addMouseOverHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                lblProperties.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            }
        });
        txtUnit.setReadOnly(true);
    }

    private boolean isPropertiesExists() {
        boolean rtnValue = false, flagApplicable = false;
        String propCode = "", property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            for (String prop : DmShared.xNull(property).split(",")) {
                flagApplicable = false;
                if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.ITEM_MAKE.getPropertyManualCode())) {
                    flagApplicable = true;

                }

                if (flagApplicable) {
                    if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                        propCode += ",";
                    }
                    propCode += prop;
                }
            }

            if (!DmShared.xNull(propCode).equalsIgnoreCase("")) {
                rtnValue = true;
            }
        }
        return rtnValue;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == cbIsManualItsType) {
            manageTaxControls();
            if (!cbIsManualItsType.getValue()) {
                //getGSTS
                getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, quotationEntry.location, txtHITSType.getTag(), cbIsManualItsType.getValue());
            }
        } else if (event.getSource() == cbIsManualTax) {
            manageTaxControls();
            calculate();
        } else {
            super.onClick(event);
        }
    }

}
