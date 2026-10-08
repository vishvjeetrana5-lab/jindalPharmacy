package aarogya.pharmacy.quotation.client;

import aarogya.pharmacy.quotation.shared.*;
import aarogya.pharmacy.indentSearch.client.IndentSearch;
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
import dataman.dmToolkit.overHeadLine.client.OverHeadLine;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Shared;
import aarogya.pharmacy.quotation.shared.Quotation_Srv;
import aarogya.pharmacy.quotation.shared.Quotation_SrvAsync;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.client.STClient;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;

public class QuotationGridEdit<T1, T2> extends QuotationGridEdit_Dsg<T1, T2> implements BlurHandler {

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl opdService);

        public void onDelete();
    }

    private ServiceInvoker<Quotation_SrvAsync> srvAsync;
    private CallBack callBack;
    private Quotation quotationEntry;
    private OverHeadLine overHeadLine;
    final IndentSearch indentSearch = new IndentSearch();
    private int hashCode = 0;
    String skuQuantity = "", conversionRate = "", skuDecimalPlaces = "";
    String enquiryDocid = "", enquiryV_SNo = "", unit = "", prvItem = "";
    boolean isFirstShow = true;
    private String itsBasedOn = "";

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        txtRemark.addKeyDownHandler(new DmRemarkTextBox.TxtRemarkKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    ((CellSelectionModel) overHeadLine.getDmGrid().getGrid().getSelectionModel()).selectCell(STClient.getOHLFocusIndex(overHeadLine.getOverHeadList()), 0);
                }
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemName(DmShared.xNull(quotationEntry.location), DmShared.xNull(quotationEntry.txtQuotationDate.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemName_Mdl>>>() {

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
                if (DmShared.iNull(prvItem).longValue() != DmShared.iNull(t1Row.getSharedKeyFieldCode()).longValue()) {
                    resetControls();
                    prvItem = DmShared.xNull(t1Row.getSharedKeyFieldCode());
                }
                if (blnStatus && t1Row != null) {
                    txtUnit.setTag2nd(t1Row.getDecimalPlacesPurchaseUnit());
                    txtUnit.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    txtUnit.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    conversionRate = t1Row.getConversionRate();
                    skuDecimalPlaces = t1Row.getDecimalPlaces();
                    unit = DmShared.xNull(t1Row.getDisplayUnit());
                    itsBasedOn = DmShared.iNull(t1Row.getItsBasedOn()).toString();
                    formatQuantity();
                    getCDConfiguration(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                }
            }
        });

    }

    private void resetControls() {
        txtUnit.setTag("");
        txtUnit.setText("");
        conversionRate = "";
        skuDecimalPlaces = "";
        skuQuantity = "";
        itsBasedOn = "";
        gridIndentDetail.getGrid().getStore().clear();
    }

    private void formatQuantity() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));
        skuQuantity = DmClient.vNull(String.valueOf(DmShared.vNull(txtQuantity.getText()) * DmShared.vNull(conversionRate)), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()));
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

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, Quotation quotationEntry) {
        this.quotationEntry = quotationEntry;
        overHeadLine = this.quotationEntry.ohlHandler.getWidget();
        super.showCenter(currentRowIndex, okNeeded, newNeeded, deleteNeeded);
        flwOHL.clear();
        overHeadLine.setBannerVisible(false);
        flwOHL.add(overHeadLine.asWidget());
        if (isFirstShow) {
            isFirstShow = false;
            vl.add(overHeadLine);
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
        if ((event.getSource() == txtQuantity) || (event.getSource() == txtRate)) {
            setIts(txtHItemName.getTag(), txtRate.getText(), quotationEntry.location, itsBasedOn, quotationEntry.txtQuotationDate.getText());
            calculate();
            formatQuantity();
        }
    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;

        manageDisplay();

        if (!DmShared.xNull(ourRow.getEnquiryDocId()).equalsIgnoreCase("") || (ourRow.getListIndentDetail() != null && !ourRow.getListIndentDetail().isEmpty())) {
            txtHItemName.setEnabled(false);
            txtQuantity.setFocus(true);
        } else {
            txtHItemName.setEnabled(true);
        }
        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        itsBasedOn = DmShared.xNull(ourRow.getItsBasedOn());
        txtUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlacesPurchaseUnit()));
        txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        conversionRate = DmShared.xNull(ourRow.getConversionRate());
        enquiryDocid = DmShared.xNull(ourRow.getEnquiryDocId());
        enquiryV_SNo = DmShared.xNull(ourRow.getEnquiryV_SNo());
        unit = DmShared.xNull(ourRow.getDisplayUnit());
        skuQuantity = DmShared.xNull(ourRow.getSkuQuantity());
        skuDecimalPlaces = DmShared.xNull(ourRow.getSkuDecimalPlaces());
        txtDeliveryDate.setText(DmShared.dNull(ourRow.getDeliveryDate()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        txtRemark.setTag(DmShared.xNull(ourRow.getvSNo()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtAmount.setText(DmClient.vNull(ourRow.getAmount(), "0.00"));
        txtMrp.setText(DmClient.vNull(ourRow.getMrp(), "0.00"));
        txtQuantity.setText(DmShared.xNull(ourRow.getQuantity()));
        prvItem = DmShared.xNull(ourRow.getSharedKeyFieldCode());

        gridIndentDetail.getGrid().getStore().clear();
        gridIndentDetail.getGrid().getStore().addAll(ourRow.getListIndentDetail());

        overHeadLine.initialize(this.getCurrentRowIndex());
        hashCode = ourRow.getSharedKeyFieldHashCode();

        txtHItemName.setFocus(true);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        try {
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine is mandatory.", txtHItemName).show();
                return;
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

            /*==============================<< Check Item dublicacy >>====================================*/
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(detail_Mdl.getSharedKeyFieldName()) + " already exists.", txtHItemName).show();
                        return;
                    }
                }
            }

            /*==============================<< Check overheadLine conditions >>====================================*/
            OverHeadLine_Shared.chkGridSingleOH(overHeadLine.getOverHeadList());

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
        formatQuantity();
        calculate();
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHItemName.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHItemName.getText()));
        ourRow.setItsBasedOn(itsBasedOn);
        ourRow.setDeliveryDate(DmShared.dNull(txtDeliveryDate.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setvSNo(DmShared.xNull(txtRemark.getTag()));
        ourRow.setPurchaseUnitCode(DmShared.xNull(txtUnit.getTag()));
        ourRow.setPurchaseUnitName(DmShared.xNull(txtUnit.getText()));
        ourRow.setQuantity(DmShared.xNull(txtQuantity.getText()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setMrp(DmClient.vNull(txtMrp.getText(), "0.00"));
        ourRow.setAmount(DmClient.vNull(txtAmount.getText(), "0.00"));
        ourRow.setEnquiryDocId(enquiryDocid);
        ourRow.setEnquiryV_SNo(enquiryV_SNo);
        ourRow.setSkuQuantity(DmShared.xNull(skuQuantity));
        ourRow.setConversionRate(DmShared.xNull(conversionRate));
        ourRow.setSkuDecimalPlaces(DmShared.xNull(skuDecimalPlaces));
        ourRow.setDecimalPlacesPurchaseUnit(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setDisplayUnit(DmShared.xNull(unit));

        ourRow.getListIndentDetail().clear();
        ourRow.getListIndentDetail().addAll(gridIndentDetail.getGrid().getStore().getAll());

        ourRow.setOHLList(overHeadLine.getOverHeadList());

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

    public QuotationGridEdit(DmGrid<T1, T2> grid) {
        super(grid, "Medicine Detail", "750px", "310px");
        srvAsync = new ServiceInvoker<Quotation_SrvAsync>(GWT.create(Quotation_Srv.class), Quotation.class.getName());
        textHelpEvents();
        gridEvents();
        txtDeliveryDate.addBlurHandler(this);
        txtQuantity.addBlurHandler(this);
        txtRate.addBlurHandler(this);
        txtUnit.setReadOnly(true);
        showIndentSearch();
    }

    private void manageDisplay() {
        txtAmount.setEnabled(false);
        flwAddIndent.clear();
        if (!DmShared.xNull(quotationEntry.topControl.getMode()).trim().equalsIgnoreCase("Browse")) {
            flwAddIndent.add(btnAddIndent);
        }
    }

    private void showIndentSearch() {
        btnAddIndent.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!DmShared.xNull(txtHItemName.getTag()).trim().equalsIgnoreCase("")) {
                    if (DmShared.dNull(quotationEntry.txtVendorQNDate.getText()).equalsIgnoreCase("")) {
                        indentSearch.show(txtHItemName.getTag(), quotationEntry.txtQuotationDate.getText(), new ProjectVoucherCategory().PHARMACY_QUOTATION);  //Voucher Category To Be Manage
                    } else {
                        indentSearch.show(txtHItemName.getTag(), quotationEntry.txtVendorQNDate.getText(), new ProjectVoucherCategory().PHARMACY_QUOTATION);
                    }
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
                        if (gridIndentDetail.getGrid().getStore().size() == 1) {
                            txtMrp.setText(DmShared.xNull(gridIndentDetail.getGrid().getStore().get(0).getIndentMrp()));
                        }
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

    private void calculate() {
        txtQuantity.setText(DmClient.vNull(txtQuantity.getText(), DmShared.getNumberFormat(DmShared.iNull(txtUnit.getTag2nd()).intValue())));
        txtAmount.setText(DmClient.vNull(DmShared.vNull(txtQuantity.getText()).doubleValue() * DmShared.vNull(txtRate.getText()).doubleValue(), "0.00"));
        overHeadLine.getDmGrid().commitChanges();
        overHeadLine.calculate(txtAmount.getText());
    }

    private void setIts(final String itemCode, final String itemRate, final String location, final String itsBasedOn, final String billDate) {
        srvAsync.invokeRPC(true).getITS(itemCode, itemRate, location, itsBasedOn, billDate, new AsyncCallback<DmResult<List<OverHeadITS_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHItemName).show();
            }

            @Override
            public void onSuccess(DmResult<List<OverHeadITS_Mdl>> result) {
                if (result.isStatus()) {
                    overHeadLine.setITS(result.getData());
                    calculate();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHItemName).show();
                }
            }
        });
    }

    private void getCDConfiguration(String medicineCode) {

        srvAsync.invokeRPC().getCDConfiguration(DmShared.xNull(quotationEntry.txtHSupplier.getTag()), DmShared.xNull(medicineCode), new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    OverHeadLine_Mdl ohlMdl = OverHeadLine_Shared.getOHByManualCode("DISC", overHeadLine.getOverHeadList(true));
                    ohlMdl.setAtRate(DmClient.vNull(result.getData(), "0.00"));

                    if (DmShared.vNull(result.getData()) > 0) {
                        ohlMdl.setAtRateEditable("0");
                        ohlMdl.setAmountEditable("0");
                    } else {
                        ohlMdl.setAtRateEditable("1");
                        ohlMdl.setAmountEditable("1");
                    }

                    overHeadLine.calculate(txtAmount.getText());
                    overHeadLine.getDmGrid().commitChanges();

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

}
