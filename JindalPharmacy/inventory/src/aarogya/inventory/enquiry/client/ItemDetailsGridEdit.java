package aarogya.inventory.enquiry.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.CellClickEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.enquiry.shared.Enquiry_Srv;
import aarogya.inventory.enquiry.shared.Enquiry_SrvAsync;
import aarogya.inventory.enquiry.shared.Enquiry_Vld;
import aarogya.inventory.enquiry.shared.GrdIndentDetails_Mdl;
import aarogya.inventory.enquiry.shared.GrdItemDetail_Mdl;
import aarogya.inventory.enquiry.shared.TxtHItem_Mdl;
import aarogya.inventory.indentSearch.client.IndentSearch;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;

public class ItemDetailsGridEdit<T1, T2> extends ItemDetailsGridEdit_Dsg<T1, T2> implements BlurHandler, KeyUpHandler, KeyDownHandler {

    final IndentSearch indentSearch = new IndentSearch();

    @Override
    public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtProperties) {
                flagFromlblClick = true;
                showPropertyPopUp();
            }
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource() == txtProperties) {

                try {
                    ItemPropertyPopup_Vld.validateProperties(Enquiry_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), "0", enquiry.txtDtEnquiry.getText(), propertyPopup_Mdl);
                } catch (Exception e) {
                    showPropertyPopUp();
                }
            }
        }
    }

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl gridItem_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<Enquiry_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    Enquiry enquiry;
    GrdIndentDetails_Mdl indentDetailIndex;
    final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl;
    String previousItemCode = "", previousProperties = "";
    String skuQuantity = "", conversionRate = "", skuDecimalPlaces = "", Unit = "";
    boolean flagFromlblClick = false;

    private void textHelpEvents() {
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                if (!previousProperties.equalsIgnoreCase(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()))) {
                    grdIndent.getGrid().getStore().clear();
                    previousProperties = DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv());
                }
                txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
                txtQty.setFocus(true);
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                getIndentDetail(listKF);
            }

            @Override
            public void close() {
                txtHItemName.setFocus(true);
            }
        });

        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItem_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItem(DmShared.xNull(enquiry.txtHEnquiryType.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItem_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItem_Mdl t1Row) {
                if (DmShared.iNull(previousItemCode).longValue() != DmShared.iNull(txtHItemName.getTag()).longValue()) {
                    resetControls();
                    previousItemCode = DmShared.xNull(txtHItemName.getTag());
                } else {
                    previousProperties = propertyPopup_Mdl.getPropertiesCsv();
                }
                if (blnStatus && t1Row != null) {
                    Unit = DmShared.xNull(t1Row.getDisplayUnit());
                    txtUnit.setTag2nd(t1Row.getDecimalPlacesPurchaseUnit());
                    txtUnit.setTag(t1Row.getPurchaseUnitCode());
                    txtUnit.setText(t1Row.getPurchaseUnitName());
                    txtHItemName.setTag2nd(t1Row.getItemProperty());
                    conversionRate = t1Row.getConversionRate();
                    skuDecimalPlaces = t1Row.getDecimalPlaces();
                    flagFromlblClick = false;
                    showPropertyPopUp();
                    calculate();
                }
            }
        });
    }

    private void resetControls() {
        txtUnit.setTag2nd("");
        txtUnit.setTag("");
        txtUnit.setText("");
        txtProperties.setText("");
        conversionRate = "";
        skuDecimalPlaces = "";
        skuQuantity = "";
        grdIndent.getGrid().getStore().clear();
        propertyPopup_Mdl = new ItemProperty_Mdl();
    }

    private void showPropertyPopUp() {
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(Enquiry_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag())).equalsIgnoreCase("")) {
                txtProperties.setEnabled(true);
               propertyPopup.show(enquiry.topControl.getMode(), Enquiry_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), "0", propertyPopup_Mdl, DmShared.dNull(enquiry.txtDtEnquiry.getText()), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
            } else {
                txtProperties.setEnabled(false);
                if (flagFromlblClick) {
                    new DmAlertMessageBox("Properties are not applicable for this item.", txtQty).show();
                }
            }
        }
    }

    private void gridEvents() {
        grdIndent.getGrid().addCellClickHandler(new CellClickEvent.CellClickHandler() {
            @Override
            public void onCellClick(CellClickEvent event) {
                try {
                    if (event.getCellIndex() == 0) {

                        indentDetailIndex = grdIndent.getGrid().getStore().get(event.getRowIndex());
                        new DmConfirmMessageBox("Are you sure? You want to delete.", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event1) {
                                if (event1.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                    grdIndent.getGrid().getStore().remove(indentDetailIndex);
                                    addQty(true);
                                }
                            }
                        }).show();
                    }
                } catch (Exception e) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null);
                }
            }
        });
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;

    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, Enquiry enquiry) {
        this.enquiry = enquiry;
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        txtDtReq.addBlurHandler(this);
        txtQty.addBlurHandler(this);
        manageDisplay();
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        propertyPopup_Mdl = new ItemProperty_Mdl();
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtQty.setText(DmShared.xNull(ourRow.getQty()));
        txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        conversionRate = DmShared.xNull(ourRow.getConversionRate());
        skuQuantity = DmShared.xNull(ourRow.getSkuQuantity());
        skuDecimalPlaces = DmShared.xNull(ourRow.getDecimalPlaces());
        txtUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlacesPurchaseUnit()));
        txtDtReq.setText(DmShared.dNull(ourRow.getScheduleDt()));
        txtRemark.setTag(DmShared.xNull(ourRow.getvSno()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHItemName.setTag2nd(ourRow.getPropertyList());
        previousItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());

        propertyPopup_Mdl.setColourCode(DmShared.xNull(ourRow.getColourCode()));
        propertyPopup_Mdl.setColourName(DmShared.xNull(ourRow.getColourName()));
        propertyPopup_Mdl.setSizeCode(DmShared.xNull(ourRow.getSizeCode()));
        propertyPopup_Mdl.setSizeName(DmShared.xNull(ourRow.getSizeName()));

        propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(ourRow.getItemMakeCode()));
        propertyPopup_Mdl.setItemMakeName(DmShared.xNull(ourRow.getItemMakeName()));

        grdIndent.getGrid().getStore().clear();
        grdIndent.getGrid().getStore().addAll(ourRow.getGrdIndentDetail());
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());
        previousProperties = DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv());

        Unit = DmShared.xNull(ourRow.getDisplayUnit());
        if (DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }
        if (ourRow.getGrdIndentDetail().size() != 0) {
            if (DmShared.iNull(ourRow.getGrdIndentDetail().get(0).getIndentVSNo()).intValue() > 0) {
                txtHItemName.setEnabled(false);
                if (isPropertiesExists()) {
                    txtProperties.setFocus(true);
                } else {
                    txtQty.setFocus(true);
                }
            } else {
                txtHItemName.setEnabled(true);
                txtHItemName.setFocus(true);
            }
        } else {
            txtHItemName.setEnabled(true);
            txtHItemName.setFocus(true);
        }

        manageDisplay();
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
                new DmAlertMessageBox("Item Name is mandatory.", txtHItemName).show();
                return;
            }
            if (DmClient.vNull(txtQty.getText().trim(), "0.00").equalsIgnoreCase("0.00")) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQty).show();
                return;
            }
            if (DmShared.xNull(txtUnit.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Unit is mandatory.", txtUnit).show();
                return;
            }
            if (!DmShared.xNull(enquiry.txtDtEnquiry.getText()).equalsIgnoreCase("")) {
                if (!DmShared.dNull(txtDtReq.getText()).equalsIgnoreCase("")) {
                    if (DmShared.convertDate(txtDtReq.getText()).getTime() < DmShared.convertDate(enquiry.txtDtEnquiry.getText()).getTime()) {
                        new DmAlertMessageBox("Req. Date can't be less than Enquiry Date ", txtDtReq).show();
                        return;
                    }
                }
            }
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()))
                        && DmShared.xNull(propertyPopup_Mdl.getColourCode()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getColourCode()))
                        && DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getItemMakeCode()))
                        && DmShared.xNull(propertyPopup_Mdl.getSizeCode()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSizeCode()))) {
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

            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(Enquiry_Vld.getApplicableProperties(txtHItemName.getTag2nd(), txtHItemName.getTag()), "0", enquiry.txtDtEnquiry.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                    return;
                }
            }

            double indentOrderSkuQuantity = 0;
            String decimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue());

            if ((grdIndent.getGrid().getStore().getAll().size() > 0)) {
                String reqDate = "", indNo = "";
                for (GrdIndentDetails_Mdl detail_Mdl : grdIndent.getGrid().getStore().getAll()) {

                    for (GrdItemDetail_Mdl itemDetail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                        if (!DmClient.vNull(itemDetail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                            for (GrdIndentDetails_Mdl grdIndentDetail : itemDetail_Mdl.getGrdIndentDetail()) {
                                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(grdIndentDetail.getSharedKeyFieldCode()))
                                        && DmShared.xNull(detail_Mdl.getIndentVSNo()).equalsIgnoreCase(DmShared.xNull(grdIndentDetail.getIndentVSNo()))) {
                                    new DmAlertMessageBox("Indent No.: " + DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " already exists.", btnAddIndent).show();
                                    return;
                                }
                            }
                        }
                    }

                    if (!DmShared.dNull(txtDtReq.getText()).trim().equalsIgnoreCase("")) {
                        String tempDate = DmShared.dNull(detail_Mdl.getReqDate());
                        if (!DmShared.dNull(reqDate).equalsIgnoreCase("")) {
                            if ((DmShared.convertDate(reqDate).getTime() > (DmShared.convertDate(tempDate)).getTime())) {
                                reqDate = tempDate;
                                indNo = detail_Mdl.getSharedKeyFieldName();
                            }
                        } else {
                            reqDate = tempDate;
                            indNo = detail_Mdl.getSharedKeyFieldName();
                        }
                    }

                    indentOrderSkuQuantity += DmShared.vNull(detail_Mdl.getIndentQty()).doubleValue();
                }

                indentOrderSkuQuantity = indentOrderSkuQuantity / DmShared.vNull(conversionRate);
                indentOrderSkuQuantity = DmShared.vNull(DmClient.vNull(indentOrderSkuQuantity, decimalFormat));

                if (indentOrderSkuQuantity != 0) {
                    if (DmShared.vNull(txtQty.getText()).doubleValue() < DmShared.vNull(indentOrderSkuQuantity).doubleValue()) {
                        new DmAlertMessageBox("Enquiry quantity should not be less than total indent sku quantity.", txtQty).show();
                        return;
                    }
                }

                if ((!DmShared.dNull(txtDtReq.getText()).trim().equalsIgnoreCase("")) && !DmShared.dNull(reqDate).trim().equalsIgnoreCase("")) {
                    if ((DmShared.convertDate(txtDtReq.getText()).getTime() > (DmShared.convertDate(reqDate)).getTime())) {
                        new DmConfirmMessageBox("Enquiry Req. Date is greater than Indent Req. Date for Indent No. " + indNo + ". Do You want to continue?", new DialogHideEvent.DialogHideHandler() {

                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                                    proceedOk();
                                } else {
                                    txtDtReq.setFocus(true);
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

        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setQty(DmShared.xNull(txtQty.getText()));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtUnit.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setDecimalPlacesPurchaseUnit(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setScheduleDt(DmShared.dNull(txtDtReq.getText()));
        ourRow.setvSno(DmShared.xNull(txtRemark.getTag()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setPropertyList(DmShared.xNull(txtHItemName.getTag2nd()));

        ourRow.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColourName(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSizeName(DmShared.xNull(propertyPopup_Mdl.getSizeName()));

        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));

        ourRow.getGrdIndentDetail().clear();
        ourRow.getGrdIndentDetail().addAll(grdIndent.getGrid().getStore().getAll());
        ourRow.setSkuQuantity(DmShared.xNull(skuQuantity));
        ourRow.setConversionRate(DmShared.xNull(conversionRate));
        ourRow.setDecimalPlaces(DmShared.xNull(skuDecimalPlaces));
        ourRow.setProperties(DmShared.xNull(txtProperties.getText()));
        ourRow.setDisplayUnit(DmShared.xNull(Unit));

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

    public ItemDetailsGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Item Details", "500px", "350px");
        SrvAsync = new ServiceInvoker<Enquiry_SrvAsync>(GWT.create(Enquiry_Srv.class), Enquiry.class.getName());
        textHelpEvents();
        btnAddIndent();
        gridEvents();
        handlerEvents();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtDtReq)) {
            if (!DmShared.xNull(txtDtReq.getText()).equalsIgnoreCase("")) {
                txtDtReq.setText(DmClient.returnDate(txtDtReq.getText()));
            }
        } else if (event.getSource().equals(txtQty)) {
            calculate();
        }
    }

    private void getIndentDetail(List<KeyFieldExtended> listKF) {
        try {
//            ItemProperty_Mdl ippm = new ItemProperty_Mdl();
//            ippm.setItemCode(DmShared.xNull(txtHItemName.getTag()));
//            ippm.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
//            ippm.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
            if (DmShared.iNull(propertyPopup_Mdl.getItemCode()).longValue() == 0) {
                propertyPopup_Mdl.setItemCode(DmShared.xNull(txtHItemName.getTag()));
            }

            SrvAsync.invokeRPC().getIndentDetail(listKF, propertyPopup_Mdl, new AsyncCallback<DmResult<List<GrdIndentDetails_Mdl>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
                }

                @Override
                public void onSuccess(DmResult<List<GrdIndentDetails_Mdl>> result) {

                    if (result.isStatus()) {
                        grdIndent.getGrid().getStore().clear();
                        grdIndent.getGrid().getStore().addAll(result.getData());
                        addQty(false);
                        txtHItemName.setFocus(true);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), btnAddIndent).show();

                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void btnAddIndent() {
        btnAddIndent.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                    if (isPropertiesExists()) {
                        if (DmShared.xNull(txtProperties.getText()).equalsIgnoreCase("")) {
                            new DmAlertMessageBox("Properties are mandatory.", txtProperties).show();
                            return;
                        }
                    }

                    if (DmShared.iNull(propertyPopup_Mdl.getItemCode()).longValue() == 0) {
                        propertyPopup_Mdl.setItemCode(DmShared.xNull(txtHItemName.getTag()));
                    }
                    indentSearch.show(DmShared.xNull(enquiry.txtHEnquiryType.getTag()), propertyPopup_Mdl, DmShared.dNull(enquiry.txtDtEnquiry.getText()), new ProjectVoucherCategory().INVENTORY_ENQUIRY);
                } else {
                    new DmAlertMessageBox("Item Name is mandatory.", txtHItemName).show();

                }
            }
        }
        );
    }

    private void addQty(boolean isFromIndentDlt) {
        double purchaseUnitQty = 0, skuQty = 0;

        for (GrdIndentDetails_Mdl detail_Mdl : grdIndent.getGrid().getStore().getAll()) {
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
        txtQty.setText(DmClient.vNull(purchaseUnitQty, DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));

    }

    private void calculate() {
        txtQty.setText(DmClient.vNull(txtQty.getText(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));

        skuQuantity = DmClient.vNull(String.valueOf(DmShared.vNull(txtQty.getText()) * DmShared.vNull(conversionRate)), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()));
    }

    private void handlerEvents() {
        txtProperties.addKeyUpHandler(this);
        txtProperties.addKeyDownHandler(this);
        lblProperties.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                flagFromlblClick = true;
                showPropertyPopUp();
            }
        });
    }

    private void manageDisplay() {
        txtUnit.setReadOnly(true);
        txtProperties.setReadOnly(true);

        if (enquiry.topControl.getMode().equalsIgnoreCase("Browse")) {
            btnAddIndent.removeFromParent();
        } else {
            flwIndentBtn.clear();
            flwIndentBtn.add(btnAddIndent);
        }
    }

    private boolean isPropertiesExists() {
        boolean rtnValue = false, flagApplicable = false;
        String propCode = "", property = DmShared.xNull(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            txtProperties.setEnabled(true);
            for (String prop : DmShared.xNull(property).split(",")) {
                flagApplicable = false;
                if (DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.COLOUR.getPropertyManualCode())
                        || DmShared.xNull(prop).equalsIgnoreCase(ItemProperty_Mdl.Properties.SIZE.getPropertyManualCode())) {
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
}
