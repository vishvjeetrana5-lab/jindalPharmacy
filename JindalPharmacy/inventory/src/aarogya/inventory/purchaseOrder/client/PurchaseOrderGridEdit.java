package aarogya.inventory.purchaseOrder.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.gsts.shared.GSTS_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.inventory.indentSearch.client.IndentSearch;
import aarogya.inventory.purchaseOrder.shared.GrdGrnPurchaseHistory_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdIndentDetail_Mdl;
import aarogya.inventory.purchaseOrder.shared.GrdItemDetail_Mdl;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Srv;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_SrvAsync;
import aarogya.inventory.purchaseOrder.shared.PurchaseOrder_Vld;
import aarogya.inventory.purchaseOrder.shared.Tolerance_Mdl;
import aarogya.inventory.purchaseOrder.shared.TxtHItemName_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.client.ItemPropertyPopup;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemPropertyPopup_Vld;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmToolkit.dmStatic.server.DmServer;

public class PurchaseOrderGridEdit<T1, T2> extends PurchaseOrderGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl grdItemDetail);

        public void onDelete();
    }

    private ServiceInvoker<PurchaseOrder_SrvAsync> srvAsync;
    private CallBack callBack;
    private PurchaseOrder orderEntry;
//    private OverHeadLine overHeadLine; <AG> TBR
    GrdItemDetail_Mdl parentRow;
    String itsBasedOn = "", mode = "", isBestBefore = "0", purchaseUnitName = "",
            itemGSTNature = "", purchaseUnitConversionRate = "", skuName = "",
            skuDecimalPlaces = "", selectedItemCode = "";
    final IndentSearch indentSearch = new IndentSearch();
    final IndentGridEdit indentGridEdit = new IndentGridEdit(gridIndentDetail);
    private int hashCode = 0;
    final TolerancePopup tolerancePopup = new TolerancePopup();
    private Tolerance_Mdl toleranceMdl = null;
    final ItemPropertyPopup propertyPopup = new ItemPropertyPopup();
    private ItemProperty_Mdl propertyPopup_Mdl = new ItemProperty_Mdl();
    boolean isFirstShow = true, approvedRateYN = false;
    private String purchaseRate = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        indentSearch.addCallBack(new IndentSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                List<KeyFieldExtended> kf = listKF;
                if (gridIndentDetail.getGrid().getStore().size() != 0) {
                    for (int i = 0; i < gridIndentDetail.getGrid().getStore().size(); i++) {
                        GrdIndentDetail_Mdl detail_Mdl = gridIndentDetail.getGrid().getStore().get(i);
                        for (KeyFieldExtended keyField : kf) {
                            if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(keyField.getSharedKeyFieldCode())) {
                                new DmAlertMessageBox("Indent already exist.", txtHItemName).show();
                                return;
                            }
                        }
                    }
                }
                fillIndentDetails(listKF);
            }

            @Override
            public void close() {
                if (txtHItemName.isEnabled()) {
                    txtHItemName.setFocus(true);
                } else {
                    txtQuantity.setFocus(true);
                }
            }
        });
        propertyPopup.addCallBack(new ItemPropertyPopup.CallBack() {
            @Override
            public void onOk(ItemProperty_Mdl property_Mdl) {
                propertyPopup_Mdl = property_Mdl;
                txtProperty.setTag(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
                txtProperty.setText(DmShared.xNull(propertyPopup_Mdl.getColourName()));
                txtProperty.setTag(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
                txtProperty.setText(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
                txtProperty.setTag(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
                txtProperty.setText(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
                txtProperties.setText(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));
                txtProperties.setFocus(true);
                reQuery();
            }

            @Override
            public void onClose() {
                txtProperties.setFocus(true);
            }
        });
        txtRemark.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    txtDiscountPer.setFocus(true);
                }
            }
        });

        txtProperties.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    showPropertyPopUp();
                }
            }
        });
        aProperties.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showPropertyPopUp();
            }
        });
        txtProperties.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                        try {
                            ItemPropertyPopup_Vld.validateProperties(PurchaseOrder_Vld.getPOApplicableProperties(txtHItemName.getTag2nd()), isBestBefore, orderEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                        } catch (DmException e) {
                            showPropertyPopUp();
                        }
                    }
                }
            }
        });

        tolerancePopup.addCallBack(new TolerancePopup.CallBack() {
            @Override
            public void onOk(Tolerance_Mdl tMdl) {
                toleranceMdl = tMdl;
                txtTolerance.setText(DmShared.xNull(getToleranceString()));
                txtTolerance.setFocus(true);
            }

            @Override
            public void onClose() {
                txtTolerance.setFocus(true);
            }
        });
        aTolerance.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                showTolerancePopup();
            }
        });
        txtTolerance.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                    showTolerancePopup();
                }
            }
        });
        txtHItemName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItem(DmShared.xNull(orderEntry.txtHVoucherType.getTag()), DmShared.xNull(orderEntry.voucherCategory), orderEntry.txtVoucherDate.getText(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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
//                fetchLastRate(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
            }
        });
        txtHGstType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGstType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGstType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHGstType.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHGstType.getTag()))) {
                    //   resetItemType();
                    txtHGstType.setTag2nd(DmShared.xNull(txtHGstType.getTag()));
                }
                if (blnStatus && t1Row != null) {
                    //getGSTS
                    getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, orderEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
                }
            }
        });
    }

    private void onItemSelection(boolean blnStatus, TxtHItemName_Mdl t1Row) {
        if (!DmShared.xNull(selectedItemCode).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
            resetItem();
            selectedItemCode = DmShared.xNull(txtHItemName.getTag());
        }
        if (blnStatus && t1Row != null) {
            txtPurchaseUnit.setTag2nd(t1Row.getPurchaseUnitDecimalPlaces());
            txtPurchaseUnit.setTag(t1Row.getPurchaseUnitCode());
            txtPurchaseUnit.setText(t1Row.getPurchaseUnitDisplayName());
            purchaseUnitName = t1Row.getPurchaseUnitName();
            purchaseRate = DmShared.xNull(t1Row.getPurchaseRate());
            itsBasedOn = DmShared.iNull(t1Row.getItsBasedOn()).toString();

            itemGSTNature = DmShared.xNull(t1Row.getItemGSTNature());

            isBestBefore = DmShared.iNull(t1Row.getIsBestBefore()).toString();
            purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
            skuDecimalPlaces = DmShared.xNull(t1Row.getSkuDecimalPlaces());
            skuName = DmShared.xNull(t1Row.getSkuName());
            txtRate.setText(DmShared.xNull(t1Row.getPurchaseRate()));
            approvedRateYN = (DmShared.xNull(t1Row.getApprovedRateYN()).trim().equalsIgnoreCase("1") ? true : false);
            txtRate.setTag(DmShared.xNull(t1Row.getPurchaseRate()));

            txtHSN.setTag(DmShared.xNull(t1Row.getHsnCode()));
            txtHSN.setText(DmShared.xNull(t1Row.getHsn()));

            toleranceMdl = new Tolerance_Mdl();
            toleranceMdl.setOrderQuantity(txtQuantity.getText());
            toleranceMdl.setOrderQuantityWithExcess(t1Row.getToleranceMdl().getOrderQuantityWithExcess());
            toleranceMdl.setOrderQuantityWithShortage(t1Row.getToleranceMdl().getOrderQuantityWithShortage());
            toleranceMdl.setTl_ExcessPer(t1Row.getToleranceMdl().getTl_ExcessPer());
            toleranceMdl.setTl_ExcessQty(t1Row.getToleranceMdl().getTl_ExcessQty());
            toleranceMdl.setTl_ShortagePer(t1Row.getToleranceMdl().getTl_ShortagePer());
            toleranceMdl.setTl_ShortageQty(t1Row.getToleranceMdl().getTl_ShortageQty());
            txtTolerance.setText(DmShared.xNull(getToleranceString()));

            txtHItemName.setTag2nd(t1Row.getItemProperty());
            txtRemark.setText(DmShared.xNull(t1Row.getRemark()));
            showPropertyPopUp();
            getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, orderEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
        }
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
                        txtHGstType.setText(result.getValue2());
                        txtHGstType.setTag(result.getValue1());
                        txtHGstType.setTag2nd(result.getValue1());
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
                if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getShortName()) && !(DmShared.iNull(orderEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtSGSTPer.setText(taxPer);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getShortName()) && (DmShared.iNull(orderEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
                    lblSgstUgst.setText(iTS_Mdl.getGstsHeadName());
                    txtSgstUgstPer.setText(taxPer);
                    txtUGSTPer.setText(taxPer);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.CGST.getShortName())) {
                    txtCGSTPer.setText(taxPer);

                } else if (DmShared.xNull(iTS_Mdl.getGstsHeadShortName()).equalsIgnoreCase(DmEnum.GSTS_Head.IGST.getShortName())) {
                    txtIGSTPer.setText(taxPer);

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

    private void gridEvents() {
        indentGridEdit.addCallBack(new IndentGridEdit.CallBack() {

            @Override
            public void onOk(GrdIndentDetail_Mdl grdIndentEntry) {
                gridIndentDetail.commitChanges();
                calculateOdrQty();
            }

            @Override
            public void onDelete() {
                gridIndentDetail.commitChanges();
                calculateOdrQty();
            }
        });

        gridIndentDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showPurchaseOrderEntryGridEdit(event.getRowIndex());
            }
        });

        gridIndentDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showPurchaseOrderEntryGridEdit(gridIndentDetail.getGrid().getStore().indexOf(gridIndentDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdPrevGrnPurchase.addCallBack(new DmGridPaging.CallBack<String>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, String param) {
                srvAsync.invokeRPC(blnToEndOfFile).getPrevGrn(propertyPopup_Mdl, txtHItemName.getTag(),
                        cbCurrentSupplier.getValue() || cbOtherSuppliers.getValue() ? orderEntry.txtHSupplier.getTag() : "",
                        cbCurrentSupplier.getValue(),
                        helper, blnToEndOfFile, param, new AsyncCallback<DmResultPageing<List<GrdGrnPurchaseHistory_Mdl>>>() {
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
                return "";
            }
        });

    }

    private void showPurchaseOrderEntryGridEdit(int currentRowIndex) {
        indentGridEdit.show(currentRowIndex, true, true, true, mode);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, String isMode) {
        resetPopup();
        mode = isMode;
//        overHeadLine = this.orderEntry.ohlHandler.getWidget(); //<Ag> TBR
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
//        flwOHL.clear(); //<Ag> TBR
//        overHeadLine.setBannerVisible(false);//<Ag> TBR
//        flwOHL.add(overHeadLine.asWidget()); //<Ag> TBR
        if (isFirstShow) {
            isFirstShow = false;
//            vl.add(overHeadLine); //<Ag> TBR
        }
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                if (txtHItemName.isEnabled()) {
                    txtHItemName.setFocus(true);
                } else {
                    txtQuantity.setFocus(true);
                }
                reQuery();
            }
        });
    }

    private void showPropertyPopUp() {
        String poApplicableProperties = PurchaseOrder_Vld.getPOApplicableProperties(txtHItemName.getTag2nd());
        if (!DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
            if (!DmShared.xNull(poApplicableProperties).equalsIgnoreCase("")) {
                txtProperties.setEnabled(true);
                propertyPopup_Mdl.setSkuName(skuName);
                propertyPopup_Mdl.setSkuQtyForRate(purchaseUnitConversionRate);
                 propertyPopup.show(mode, poApplicableProperties, isBestBefore, propertyPopup_Mdl, orderEntry.txtVoucherDate.getText(), false,false,STEnum.ItemBatchType.NON_EXPIRED_BATCH);
            } else {
                txtProperties.setEnabled(false);
                reQuery();
            }
        }
    }

    private void showTolerancePopup() {
        if (DmShared.vNull(txtQuantity.getText()) > 0) {
            tolerancePopup.show(PurchaseOrderGridEdit.this, toleranceMdl);
        } else {
            new DmAlertMessageBox("Please fill Order Qty.", txtQuantity).show();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtRequiredDate) {
            txtRequiredDate.setText(DmClient.returnDate(txtRequiredDate.getText()));
        } else if ((event.getSource() == txtRate)) {
            if (DmShared.iNull(itsBasedOn).intValue() == DmEnum.ItsBasedOn.Rate.toInt()) {
                getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, orderEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
            } else {
                calculate();
            }
        } else if ((event.getSource() == txtQuantity)
                || (event.getSource() == txtDiscount)
                || (event.getSource() == txtDiscountPer)) {
            calculate();
        } else if (event.getSource() == txtRemark) {
            txtDiscountPer.setFocus(true);
        }

    }

    private String getToleranceString() {
        String tolerance = "";
        if (toleranceMdl != null) {
            if (DmShared.vNull(toleranceMdl.getTl_ExcessQty()) != 0 || DmShared.vNull(toleranceMdl.getTl_ExcessQty()) != 0) {
                tolerance = "+" + DmShared.xNull(toleranceMdl.getTl_ExcessQty()) + "; -" + DmShared.xNull(toleranceMdl.getTl_ShortageQty()) + ";";
            }
        }
        return tolerance;
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        parentRow = new GrdItemDetail_Mdl();

        parentRow = ourRow;

        //Clear
        resetPopup();

        //Item
        selectedItemCode = ourRow.getSharedKeyFieldCode();
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtHItemName.setTag2nd(DmShared.xNull(ourRow.getItemProperty()));

        //ItemDetails
        isBestBefore = DmShared.iNull(ourRow.getIsBestBefore()).toString();
        itsBasedOn = ourRow.getItsBasedOn();
        itemGSTNature = ourRow.getItemGSTNature();
        cbIsManualTax.setValue(ourRow.isManualTax());
        cbIsManualGstType.setValue(ourRow.isManualGstType());
        txtHGstType.setText(DmShared.xNull(ourRow.getGstTypeName()));
        txtHGstType.setTag(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHGstType.setTag2nd(DmShared.xNull(ourRow.getGstTypeCode()));
        txtHSN.setText(DmShared.xNull(ourRow.getHsnNo()));
        txtHSN.setTag(DmShared.xNull(ourRow.getHsnCode()));

        //Sku
        skuDecimalPlaces = DmShared.xNull(ourRow.getSkuDecimalPlaces());
        skuName = DmShared.xNull(ourRow.getSkuName());

        //Purchase Unit
        txtPurchaseUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtPurchaseUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitDisplayName()));
        txtPurchaseUnit.setTag2nd(DmShared.xNull(ourRow.getPurchaseUnitDecimalPlaces()));
        purchaseUnitName = DmShared.xNull(ourRow.getPurchaseUnitName());
        purchaseRate = DmShared.xNull(ourRow.getPurchaseRate());
        purchaseUnitConversionRate = DmShared.xNull(ourRow.getPurchaseUnitConversionRate());

        //
        txtRequiredDate.setText(DmShared.dNull(ourRow.getReqDate()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));

        //Rate
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtRate.setTag(DmShared.xNull(ourRow.getLastRateComparison()));
        approvedRateYN = (DmShared.xNull(ourRow.getApprovedRateYN()).trim().equalsIgnoreCase("1") ? true : false);

        //Amount
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtDiscountPer.setText(ourRow.getDiscountPer());
        txtDiscount.setText(ourRow.getDiscount());
        txtTaxableAmt.setText(ourRow.getTaxableAmt());
//        if (!(DmShared.iNull(orderEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
//            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
//        } else if ((DmShared.iNull(orderEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
//            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
//        }
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

        //set lables
        if (DmShared.iNull(ourRow.getSgstPer()).intValue() > 0) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.SGST.getName());
        } else if (DmShared.iNull(ourRow.getUgstPer()).intValue() > 0) {
            lblSgstUgst.setText(DmEnum.GSTS_Head.UGST.getName());
        }

        //set temp value for validate check
        txtSgstUgst.setTag2nd(ourRow.getSgstUgst());
        txtCGST.setTag2nd(ourRow.getCgst());
        txtIGST.setTag2nd(ourRow.getIgst());

        //Quantity
        txtQuantity.setText(DmShared.xNull(ourRow.getOrderQuantity()));
        txtQuantity.setTag(DmShared.xNull(ourRow.getSkuQty()));

        //Tolerance
        toleranceMdl = new Tolerance_Mdl();
        toleranceMdl.setOrderQuantity(txtQuantity.getText());
        toleranceMdl.setOrderQuantityWithExcess(ourRow.getToleranceMdl().getOrderQuantityWithExcess());
        toleranceMdl.setOrderQuantityWithShortage(ourRow.getToleranceMdl().getOrderQuantityWithShortage());
        toleranceMdl.setTl_ExcessPer(ourRow.getToleranceMdl().getTl_ExcessPer());
        toleranceMdl.setTl_ExcessQty(ourRow.getToleranceMdl().getTl_ExcessQty());
        toleranceMdl.setTl_ShortagePer(ourRow.getToleranceMdl().getTl_ShortagePer());
        toleranceMdl.setTl_ShortageQty(ourRow.getToleranceMdl().getTl_ShortageQty());
        txtTolerance.setText(DmShared.xNull(getToleranceString()));

        gridIndentDetail.getGrid().getStore().clear();
        gridIndentDetail.getGrid().getStore().addAll(ourRow.getListIndentDetail());
        hashCode = ourRow.getSharedKeyFieldHashCode();

        //property
        propertyPopup_Mdl.setColourCode(DmShared.xNull(ourRow.getColorCode()));
        propertyPopup_Mdl.setColourName(DmShared.xNull(ourRow.getColor()));
        propertyPopup_Mdl.setSizeCode(DmShared.xNull(ourRow.getSizeCode()));
        propertyPopup_Mdl.setSizeName(DmShared.xNull(ourRow.getSize()));
        propertyPopup_Mdl.setItemMakeCode(DmShared.xNull(ourRow.getItemMakeCode()));
        propertyPopup_Mdl.setItemMakeName(DmShared.xNull(ourRow.getItemMakeName()));

        txtProperty.setText(DmShared.xNull(ourRow.getColorCode()));
        txtProperty.setTag(DmShared.xNull(ourRow.getSizeCode()));
        txtProperties.setText(propertyPopup_Mdl.getPropertiesCsv());

        manageDisplay();
        if (txtHItemName.isEnabled()) {
            txtHItemName.setFocus(true);
        } else {
            txtQuantity.setFocus(true);
        }
//        if (!DmShared.xNull(ourRow.getSharedKeyFieldCode()).trim().equalsIgnoreCase("")){
//            fetchLastRate(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
//        }
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
            if (DmShared.xNull(txtPurchaseUnit.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Unit is mandatory.", txtPurchaseUnit).show();
                return;
            }
            if (!DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
                try {
                    ItemPropertyPopup_Vld.validateProperties(PurchaseOrder_Vld.getPOApplicableProperties(txtHItemName.getTag2nd()), isBestBefore, orderEntry.txtVoucherDate.getText(), propertyPopup_Mdl);
                } catch (DmException e) {
                    new DmAlertMessageBox("Properties : " + e.getMessage(), txtProperties).show();
                    return;
                }
            }
            if (DmShared.vNull(txtQuantity.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Quantity is mandatory.", txtQuantity).show();
                return;
            }
            if (DmShared.vNull(txtRate.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
            if (!DmShared.dNull(txtRequiredDate.getText()).equalsIgnoreCase("")) {
                if ((DmShared.convertDate(orderEntry.txtVoucherDate.getText()).getTime() > (DmShared.convertDate(DmShared.dNull(txtRequiredDate.getText())).getTime()))) {
                    new DmAlertMessageBox("Required Date should be greater than or equal to PO Date.", txtRequiredDate).show();
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

            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()))
                        && DmShared.xNull(txtProperty.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getSizeCode()))
                        && DmShared.xNull(txtProperty.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getColorCode()))
                        && DmShared.xNull(txtProperty.getTag()).equalsIgnoreCase(DmShared.xNull(detail_Mdl.getItemMakeCode()))
                        && DmShared.dNull(txtRequiredDate.getText()).equalsIgnoreCase(DmShared.dNull(detail_Mdl.getReqDate()))
                        && DmShared.vNull(txtRate.getText()).doubleValue() == DmShared.vNull(detail_Mdl.getRate()).doubleValue()) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {

                        String message = "<div style='float:right; margin-right: 5px;'>";
                        message += "Record already exist with following details - <br>";
                        message += (DmShared.xNull(txtHItemName.getText()).trim().equals("")) ? "" : "<b>Item:</b> " + DmShared.xNull(txtHItemName.getText()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getColourName()).trim().equals("")) ? "" : "<b>Colour:</b> " + DmShared.xNull(propertyPopup_Mdl.getColourName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getSizeName()).trim().equals("")) ? "" : "<b>Size:</b> " + DmShared.xNull(propertyPopup_Mdl.getSizeName()) + "<br>";
                        message += (DmShared.xNull(propertyPopup_Mdl.getItemMakeName()).trim().equals("")) ? "" : "<b>Item Make:</b> " + DmShared.xNull(propertyPopup_Mdl.getItemMakeName()) + "<br>";
                        message += (DmShared.vNull(txtRate.getText()) == 0) ? "" : "<b>Rate:</b> " + DmClient.vNull(txtRate.getText(), "0.00") + "<br>";
                        message += (DmShared.dNull(txtRequiredDate.getText()).trim().equals("")) ? "" : "<b>Req. Date:</b> " + DmShared.dNull(txtRequiredDate.getText()) + "<br>";
                        message += "</div>";
                        new DmAlertMessageBox(message, txtHItemName).show();
                        return;
                    }
                }
            }
            double indentOrderSkuQuantity = 0;
            String decimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());
            for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
                if (!DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase("")) {
                    indentOrderSkuQuantity += DmShared.vNull(detail_Mdl.getOrderQty()).doubleValue();
                }
            }
            indentOrderSkuQuantity = indentOrderSkuQuantity / DmShared.vNull(purchaseUnitConversionRate);
            indentOrderSkuQuantity = DmShared.vNull(DmClient.vNull(indentOrderSkuQuantity, decimalFormat));
            if (indentOrderSkuQuantity != 0) {
                if (DmShared.vNull(txtQuantity.getText()).doubleValue() < DmShared.vNull(indentOrderSkuQuantity).doubleValue()) {
                    new DmAlertMessageBox("Order quantity should not be less than indent's total order sku quantity.", txtQuantity).show();
                    return;
                }
            }
//            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList()); //<Ag> TBR
            proceedOk();
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), txtHItemName).show();
        }
    }

    @Override
    public void manageOk(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        //  calculate();
        fillRowData(ourRow);

//        ourRow.setOHLList(overHeadLine.getOverHeadList()); //<Ag> TBR
        callBack.onOk(ourRow);
    }

    protected void fillRowData(GrdItemDetail_Mdl ourRow) {
        //Item
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));

        //ItemProperty
        ourRow.setItemProperty(DmShared.xNull(txtHItemName.getTag2nd()));
        ourRow.setColorCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        ourRow.setColor(DmShared.xNull(propertyPopup_Mdl.getColourName()));
        ourRow.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));
        ourRow.setSize(DmShared.xNull(propertyPopup_Mdl.getSizeName()));
        ourRow.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));
        ourRow.setItemMakeName(DmShared.xNull(propertyPopup_Mdl.getItemMakeName()));
        ourRow.setProperties(DmShared.xNull(propertyPopup_Mdl.getPropertiesCsv()));

        //Item Deatils
        ourRow.setIsBestBefore(DmShared.iNull(isBestBefore).toString());
        ourRow.setItsBasedOn(DmShared.xNull(itsBasedOn));
        ourRow.setItemGSTNature(DmShared.xNull(itemGSTNature));
        ourRow.setHsnCode(DmShared.xNull(txtHSN.getTag()));
        ourRow.setHsnNo(DmShared.xNull(txtHSN.getText()));
        ourRow.setManualTax(cbIsManualTax.getValue());
        ourRow.setManualGstType(cbIsManualGstType.getValue());
        ourRow.setGstTypeCode(DmShared.xNull(txtHGstType.getTag()));
        ourRow.setGstTypeName(DmShared.xNull(txtHGstType.getText()));

        //Purchase Unit
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtPurchaseUnit.getTag()));
        ourRow.setPurchaseUnitDisplayName(DmShared.xNull(txtPurchaseUnit.getText()));
        ourRow.setPurchaseUnitDecimalPlaces(DmShared.xNull(txtPurchaseUnit.getTag2nd()));
        ourRow.setPurchaseUnitName(DmShared.xNull(purchaseUnitName));
        ourRow.setPurchaseRate(DmShared.xNull(purchaseRate));
        ourRow.setPurchaseUnitConversionRate(DmShared.xNull(purchaseUnitConversionRate));

        //Sku
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuDecimalPlaces));
        ourRow.setSkuName(DmShared.xNull(skuName));

        //quantitiy
        ourRow.setOrderQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setSkuQty(DmShared.xNull(txtQuantity.getTag()));

        //Tollerence
        ourRow.getToleranceMdl().setOrderQuantity(toleranceMdl.getOrderQuantity());
        ourRow.getToleranceMdl().setOrderQuantityWithExcess(toleranceMdl.getOrderQuantityWithExcess());
        ourRow.getToleranceMdl().setOrderQuantityWithShortage(toleranceMdl.getOrderQuantityWithShortage());
        ourRow.getToleranceMdl().setTl_ExcessPer(toleranceMdl.getTl_ExcessPer());
        ourRow.getToleranceMdl().setTl_ExcessQty(toleranceMdl.getTl_ExcessQty());
        ourRow.getToleranceMdl().setTl_ShortagePer(toleranceMdl.getTl_ShortagePer());
        ourRow.getToleranceMdl().setTl_ShortageQty(toleranceMdl.getTl_ShortageQty());

        //Rate
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setApprovedRateYN(approvedRateYN ? "1" : "0");

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

        //Others
        ourRow.setReqDate(DmShared.dNull(txtRequiredDate.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));

        ourRow.getListIndentDetail().clear();
        ourRow.getListIndentDetail().addAll(gridIndentDetail.getGrid().getStore().getAll());

        if (DmShared.vNull(txtRate.getTag()) > 0 && DmShared.vNull(txtRate.getTag()).doubleValue() > DmShared.vNull(txtRate.getText()).doubleValue()) {
            ourRow.setLastRateComparison("3");// Low Rate from Previous
            ourRow.setLastRateComparisonName("LR");// Low Rate from Previous
        } else if (DmShared.vNull(txtRate.getTag()) > 0 && DmShared.vNull(txtRate.getTag()).doubleValue() < DmShared.vNull(txtRate.getText()).doubleValue()) {
            ourRow.setLastRateComparison("2");// Heigher Rate from Previous
            ourRow.setLastRateComparisonName("HR");// Heigher Rate from Previous
        } else if (DmShared.vNull(txtRate.getTag()) > 0 && (DmShared.vNull(txtRate.getTag()).doubleValue() == DmShared.vNull(txtRate.getText()).doubleValue())) {
            ourRow.setLastRateComparison("1");// Same Rate from Previous
            ourRow.setLastRateComparisonName("SR");// Same Rate from Previous
        } else if (DmShared.vNull(txtRate.getTag()) == 0) {
            ourRow.setLastRateComparison("0");// New Purchase
            ourRow.setLastRateComparisonName("NP");// New Purchase
        }
    }

    @Override
    public void onNew() {
        proceedNew();
    }

    @Override
    public void onDelete() {
        if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("1")) {
            new DmAlertMessageBox("Record has been canceled can't delete record.", txtHItemName).show();
            return;
        } else if (DmShared.xNull(parentRow.getIsStatus()).equalsIgnoreCase("2")) {
            new DmAlertMessageBox("Record has been completed, can't change record.", txtHItemName).show();
            return;
        } else if (!DmShared.xNull(parentRow.getChallanQyt()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Item is used, can't change record.", txtHItemName).show();
            return;
        } else {
            proceedDelete();
        }
    }

    @Override
    public void onClose() {
        hide();
    }

    @Override
    public void manageDelete() {
        callBack.onDelete();
    }

    public PurchaseOrderGridEdit(DmGrid<T1, T2> grid, PurchaseOrder orderEntry) {
        super(grid, "Item Detail", "750px", "499px");
        srvAsync = new ServiceInvoker<PurchaseOrder_SrvAsync>(GWT.create(PurchaseOrder_Srv.class), PurchaseOrder.class.getName());
        this.orderEntry = orderEntry;
        textHelpEvents();
        gridEvents();
        manageEvents();
        showIndentSearch();
    }

    private void manageEvents() {
        txtRequiredDate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtDiscount.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        cbCurrentSupplier.addClickHandler(this);
        cbOtherSuppliers.addClickHandler(this);
        cbIsManualGstType.addClickHandler(this);
        cbIsManualTax.addClickHandler(this);
    }

    private void manageDisplay() {
        txtAmount.setEnabled(false);
        flwAddIndent.clear();
        if (!DmShared.xNull(mode).equalsIgnoreCase("Browse")) {
            flwAddIndent.add(btnAddIndent);
        }
        if (DmShared.xNull(txtHItemName.getTag2nd()).trim().equalsIgnoreCase("")) {
            txtProperties.setEnabled(false);
        } else {
            txtProperties.setEnabled(true);
        }
        if (parentRow != null && !DmShared.xNull(parentRow.getQuotationDocId()).equalsIgnoreCase("")) {
            txtHItemName.setEnabled(false);
            txtRate.setEnabled(false);
        } else {
            if (approvedRateYN) {
                txtRate.setEnabled(false);
            } else {
                txtRate.setEnabled(true);
            }

            if (gridIndentDetail.getGrid().getStore().getAll().size() > 0) {
                txtHItemName.setEnabled(false);
            } else {
                txtHItemName.setEnabled(true);
            }
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
        txtHGstType.setEnabled(cbIsManualGstType.getValue());

        txtIGST.setEnabled(false);
        txtSgstUgst.setEnabled(false);
        txtCGST.setEnabled(false);

        if (cbIsManualTax.getValue()) {
            if (DmShared.xNull(orderEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.InterState.getId()))) {
                txtIGST.setEnabled(true);
            } else if (DmShared.xNull(orderEntry.location).equalsIgnoreCase(String.valueOf(DmEnum.DmLocation.IntraState.getId()))) {
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
                    if (isPropertiesExists()) {
                        if (DmShared.xNull(txtProperties.getText()).equalsIgnoreCase("")) {
                            new DmAlertMessageBox("Properties are mandatory.", txtProperties).show();
                            return;
                        }
                    }

                    if (DmShared.iNull(propertyPopup_Mdl.getItemCode()).longValue() == 0) {
                        propertyPopup_Mdl.setItemCode(DmShared.xNull(txtHItemName.getTag()));
                    }

                    indentSearch.show(DmShared.xNull(orderEntry.txtHVoucherType.getTag()), propertyPopup_Mdl, !DmShared.dNull(orderEntry.txtRefDate.getText()).equalsIgnoreCase("") ? orderEntry.txtRefDate.getText() : orderEntry.txtVoucherDate.getText(), new ProjectVoucherCategory().PURCHASE_ORDER, "");  ////PURCHASE_ORDER_UNREGISTERED //Manage Also UnRegistered PO                
                } else {
                    new DmAlertMessageBox("Please select Item.", txtHItemName).show();
                }
            }
        });
    }

    private void fillIndentDetails(List<KeyFieldExtended> listKF) {
        ItemProperty_Mdl iipm_Mdl = new ItemProperty_Mdl();
        iipm_Mdl.setItemCode(DmShared.xNull(txtHItemName.getTag()));
        iipm_Mdl.setColourCode(DmShared.xNull(propertyPopup_Mdl.getColourCode()));
        iipm_Mdl.setSizeCode(DmShared.xNull(propertyPopup_Mdl.getSizeCode()));

        iipm_Mdl.setItemMakeCode(DmShared.xNull(propertyPopup_Mdl.getItemMakeCode()));

        srvAsync.invokeRPC().getIndentDetail(listKF, iipm_Mdl, new AsyncCallback<DmResult<List<GrdIndentDetail_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdIndentDetail_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        gridIndentDetail.getGrid().getStore().addAll(result.getData());
                        calculateQty();
                        if (txtHItemName.isEnabled()) {
                            txtHItemName.setFocus(true);
                        } else {
                            txtQuantity.setFocus(true);
                        }
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                    }
                }
            }
        });
    }

    protected void calculate() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue())));
        txtQuantity.setTag(DmClient.vNull(DmShared.vNull(txtQuantity.getText()) * DmShared.vNull(purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

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
//        if (!(DmShared.iNull(orderEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
//            txtSGSTPer.setText(txtSgstUgstPer.getText());
//            txtSGST.setText(txtSgstUgst.getText());
//        } else if ((DmShared.iNull(orderEntry.defaultValue.getIsSiteStateUT()).intValue() == 1)) {
//            txtUGSTPer.setText(txtSgstUgstPer.getText());
//            txtUGST.setText(txtSgstUgst.getText());
//        }

        if (DmShared.xNull(lblSgstUgst.getText()).equalsIgnoreCase(DmEnum.GSTS_Head.SGST.getName())) {
            txtSGSTPer.setText(txtSgstUgstPer.getText());
            txtSGST.setText(txtSgstUgst.getText());
        } else if (DmShared.xNull(lblSgstUgst.getText()).equalsIgnoreCase(DmEnum.GSTS_Head.UGST.getName())) {
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

        manageDisplay();

        calculateTolerance();
    }

    private void calculateQty() {
        double quantity = 0;
        for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getIndentQty()).trim().equalsIgnoreCase("")) {
                quantity += DmShared.vNull(detail_Mdl.getIndentQty()).doubleValue();
            }
        }

        txtQuantity.setText(DmShared.vNull(quantity / DmShared.vNull(purchaseUnitConversionRate)).toString());
        calculate();
    }

    private void calculateOdrQty() {
        double quantity = 0;
        for (GrdIndentDetail_Mdl detail_Mdl : gridIndentDetail.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(detail_Mdl.getOrderQty()).trim().equalsIgnoreCase("")) {
                quantity += DmShared.vNull(detail_Mdl.getOrderQty()).doubleValue();
            }
        }
        txtQuantity.setText(DmShared.vNull(quantity / DmShared.vNull(purchaseUnitConversionRate)).toString());
        calculate();
    }

    private void calculateTolerance() {
        if (toleranceMdl != null) {
            toleranceMdl.setOrderQuantity(txtQuantity.getText());

            double orderQty = DmShared.vNull(toleranceMdl.getOrderQuantity());
            double tl_ExcessPer = DmShared.vNull(toleranceMdl.getTl_ExcessPer());
            double tl_ShortagePer = DmShared.vNull(toleranceMdl.getTl_ShortagePer());
            String purchaseUnitDecimalFormat = DmShared.getNumberFormat(DmShared.iNull(txtPurchaseUnit.getTag2nd()).intValue());

            if (tl_ExcessPer > 0) {
                toleranceMdl.setTl_ExcessQty(DmShared.vNull(orderQty * (tl_ExcessPer * 0.01)).toString());
            }
            toleranceMdl.setTl_ExcessQty(DmClient.vNull(toleranceMdl.getTl_ExcessQty(), purchaseUnitDecimalFormat));

            if (tl_ShortagePer > 0) {
                toleranceMdl.setTl_ShortageQty(DmShared.vNull(orderQty * (tl_ShortagePer * 0.01)).toString());
            }
            toleranceMdl.setTl_ShortageQty(DmClient.vNull(toleranceMdl.getTl_ShortageQty(), purchaseUnitDecimalFormat));

            double tl_ExcessQty = DmShared.vNull(toleranceMdl.getTl_ExcessQty());
            double tl_ShortageQty = DmShared.vNull(toleranceMdl.getTl_ShortageQty());

            if (tl_ExcessQty > 0 || tl_ShortageQty > 0) {
                toleranceMdl.setOrderQuantityWithExcess(DmClient.vNull(orderQty + tl_ExcessQty, purchaseUnitDecimalFormat));
                toleranceMdl.setOrderQuantityWithShortage(DmClient.vNull(orderQty - tl_ShortageQty, purchaseUnitDecimalFormat));
            }

            txtTolerance.setText(getToleranceString());
        }
        manageDisplay();
    }

    private void resetPopup() {
        mode = "";

        cbIsManualGstType.setValue(false);
        cbIsManualTax.setValue(false);

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        selectedItemCode = "";
        txtHItemName.setText("");
        txtHItemName.setTag("");
        txtHItemName.setTag2nd("");

        resetItem();

    }

    private void resetItem() {
        isBestBefore = "";
        purchaseUnitName = "";
        purchaseRate = "";
        purchaseUnitConversionRate = "";
        skuDecimalPlaces = "";
        skuName = "";
        itsBasedOn = "";

        txtProperties.setText("");
        txtProperties.setTag("");
        txtProperties.setTag2nd("");
        propertyPopup_Mdl = new ItemProperty_Mdl();

        txtPurchaseUnit.setText("");
        txtPurchaseUnit.setTag("");
        txtPurchaseUnit.setTag2nd("");

        txtQuantity.setText("");
        txtQuantity.setTag("");
        txtQuantity.setTag2nd("");

        txtTolerance.setText("");
        txtTolerance.setTag("");
        txtTolerance.setTag2nd("");
        toleranceMdl = new Tolerance_Mdl();

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtRequiredDate.setText("");
        txtRequiredDate.setTag("");
        txtRequiredDate.setTag2nd("");

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        cbIsManualGstType.setValue(false);
        cbIsManualTax.setValue(false);

        gridIndentDetail.getGrid().getStore().clear();
        grdPrevGrnPurchase.getDmGrid().getGrid().getStore().clear();
        cbCurrentSupplier.setValue(false);
        cbOtherSuppliers.setValue(false);

        resetItemType();

    }

    private void resetItemType() {
        txtHGstType.setTag2nd("");

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtItemValue.setText("");
        txtItemValue.setTag("");
        txtItemValue.setTag2nd("");

        resetTaxDetail();
    }

    private void reQuery() {
        grdPrevGrnPurchase.getHelper().setRowsStartFrom(0);
        grdPrevGrnPurchase.reQuery();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == cbCurrentSupplier) {
            if (cbCurrentSupplier.getValue()) {
                cbOtherSuppliers.setValue(false);
            }
            reQuery();
        } else if (event.getSource() == cbOtherSuppliers) {
            if (cbOtherSuppliers.getValue()) {
                cbCurrentSupplier.setValue(false);
            }
            reQuery();
        } else if (event.getSource() == cbIsManualGstType) {
            manageTaxControls();
            if (!cbIsManualGstType.getValue()) {
                //getGSTS
                getGSTS(txtHItemName.getTag(), txtRate.getText(), itsBasedOn, orderEntry.location, txtHGstType.getTag(), cbIsManualGstType.getValue());
            }
        } else if (event.getSource() == cbIsManualTax) {
            manageTaxControls();
            calculate();
        } else {
            super.onClick(event);
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

//    private void fetchLastRate(String itemCode) {
//        srvAsync.invokeRPC(true).getLastRate(itemCode, new AsyncCallback<DmResult<String>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
//            }
//            @Override
//            public void onSuccess(DmResult<String> result) {
//                if (result.isStatus()) {
//                    txtRate.setTag(result.getData());
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
//                }
//            }
//        });
//    }
}
