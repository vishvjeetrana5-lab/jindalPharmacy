package aarogya.pharmacy.enquiry.client;

import aarogya.pharmacy.enquiry.shared.Enquiry_SrvAsync;
import aarogya.pharmacy.enquiry.shared.GrdItemDetail_Mdl;
import aarogya.pharmacy.enquiry.shared.TxtHItem_Mdl;
import aarogya.pharmacy.enquiry.shared.Enquiry_Srv;
import aarogya.pharmacy.enquiry.shared.GrdIndentDetails_Mdl;
import aarogya.pharmacy.enquiry.shared.Unit_Mdl;
import aarogya.pharmacy.indentSearch.client.IndentSearch;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.stStatic.shared.STShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
import org.apache.commons.lang3.StringUtils;

public class ItemDetailsGridEdit<T1, T2> extends ItemDetailsGridEdit_Dsg<T1, T2> implements BlurHandler {

    final IndentSearch indentSearch = new IndentSearch();

    interface CallBack {

        public void onOk(GrdItemDetail_Mdl gridItem_Mdl);

        public void onDelete();
    }

    private ServiceInvoker<Enquiry_SrvAsync> SrvAsync;
    private CallBack callBack;
    private int hashCode = 0;
    GrdIndentDetails_Mdl indentDetailIndex;
    String previousItemCode = "";
    String skuQuantity = "", conversionRate = "", skuDecimalPlaces = "", unit = "";
    Enquiry enquiry;

    private void textHelpEvents() {
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItem_Mdl>>>() {

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
                }
                if (blnStatus && t1Row != null) {
                    txtUnit.setTag2nd(t1Row.getDecimalPlacesPurchaseUnit());
                    txtUnit.setTag(t1Row.getPurchaseUnitCode());
                    txtUnit.setText(t1Row.getPurchaseUnitName());
                    conversionRate = t1Row.getConversionRate();
                    skuDecimalPlaces = t1Row.getDecimalPlaces();
                    unit = DmShared.xNull(t1Row.getDisplayUnit());
                    calculate();
                }
            }
        });
    }

    private void resetControls() {
        txtUnit.setTag2nd("");
        txtUnit.setTag("");
        txtUnit.setText("");
        conversionRate = "";
        skuDecimalPlaces = "";
        skuQuantity = "";
        grdIndent.getGrid().getStore().clear();
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

    }

    @Override
    public void displayRow(T1 row) {
        GrdItemDetail_Mdl ourRow = (GrdItemDetail_Mdl) row;
        manageDisplay();

        if (ourRow.getGrdIndentDetail().size() != 0) {
            if (DmShared.iNull(ourRow.getGrdIndentDetail().get(0).getIndentVSNo()).intValue() > 0) {
                txtHItemName.setEnabled(false);
                txtQty.setFocus(true);
            } else {
                txtHItemName.setEnabled(true);
            }
        } else {
            txtHItemName.setEnabled(true);
        }

        txtHItemName.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHItemName.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtQty.setText(DmShared.xNull(ourRow.getQty()));
        conversionRate = DmShared.xNull(ourRow.getConversionRate());
        skuQuantity = DmShared.xNull(ourRow.getSkuQuantity());
        skuDecimalPlaces = DmShared.xNull(ourRow.getDecimalPlacesSku());
        txtUnit.setText(DmShared.xNull(ourRow.getPurchaseUnitName()));
        txtUnit.setTag(DmShared.xNull(ourRow.getPurchaseUnitCode()));
        txtUnit.setTag2nd(DmShared.xNull(ourRow.getDecimalPlacesPurchaseUnit()));
        txtDtReq.setText(DmShared.dNull(ourRow.getScheduleDt()));
        txtRemark.setTag(DmShared.xNull(ourRow.getvSno()));
        txtRemark.setText(DmShared.xNull(ourRow.getRemark()));
        hashCode = ourRow.getSharedKeyFieldHashCode();
        unit = ourRow.getDisplayUnit();
        previousItemCode = DmShared.xNull(ourRow.getSharedKeyFieldCode());

        grdIndent.getGrid().getStore().clear();
        grdIndent.getGrid().getStore().addAll(ourRow.getGrdIndentDetail());

        txtHItemName.setFocus(true);

    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdItemDetail_Mdl();
    }

    @Override
    public void onOk() {
        GrdIndentDetails_Mdl mrTemp;
        mrTemp = new GrdIndentDetails_Mdl();
        try {
            if (DmShared.xNull(txtHItemName.getTag()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Medicine Name is mandatory.", txtHItemName).show();
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
            if (!DmShared.dNull(enquiry.txtDtEnquiry.getText()).equalsIgnoreCase("")) {
                if (!DmShared.dNull(txtDtReq.getText()).equalsIgnoreCase("")) {
                    if (DmShared.convertDate(txtDtReq.getText()).getTime() < DmShared.convertDate(enquiry.txtDtEnquiry.getText()).getTime()) {
                        new DmAlertMessageBox("Req. Date can't be less than Enquiry Date ", txtDtReq).show();
                        return;
                    }
                }
            }
            for (GrdItemDetail_Mdl detail_Mdl : (List<GrdItemDetail_Mdl>) getDmGrid().getGrid().getStore().getAll()) {
                if (DmShared.xNull(detail_Mdl.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(txtHItemName.getTag()))) {
                    if (!DmClient.vNull(detail_Mdl.getSharedKeyFieldHashCode(), "0").equalsIgnoreCase(DmClient.vNull(hashCode, "0"))) {
                        new DmAlertMessageBox(DmShared.xNull(txtHItemName.getText()) + " already exists.", txtHItemName).show();
                        return;
                    }
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
        ourRow.setScheduleDt(DmShared.dNull(txtDtReq.getText()));
        ourRow.setRemark(DmShared.xNull(txtRemark.getText()));
        ourRow.setvSno(DmShared.xNull(txtRemark.getTag()));
        ourRow.setSkuQuantity(DmShared.xNull(skuQuantity));
        ourRow.setConversionRate(DmShared.xNull(conversionRate));
        ourRow.setDecimalPlacesSku(DmShared.xNull(skuDecimalPlaces));
        ourRow.setDecimalPlacesPurchaseUnit(DmShared.xNull(txtUnit.getTag2nd()));
        ourRow.setDisplayUnit(unit);

        ourRow.getGrdIndentDetail().clear();
        ourRow.getGrdIndentDetail().addAll(grdIndent.getGrid().getStore().getAll());

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
        super(grid, "Medicine Details", "500px", "330px");
        SrvAsync = new ServiceInvoker<Enquiry_SrvAsync>(GWT.create(Enquiry_Srv.class), Enquiry.class.getName());
        textHelpEvents();
        btnAddIndent();
        gridEvents();

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
            SrvAsync.invokeRPC().getIndentDetail(listKF, DmShared.xNull(txtHItemName.getTag()), new AsyncCallback<DmResult<List<GrdIndentDetails_Mdl>>>() {

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
                    indentSearch.show(DmShared.xNull(txtHItemName.getTag()), DmShared.dNull(enquiry.txtDtEnquiry.getText()), new ProjectVoucherCategory().PHARMACY_ENQUIRY);
                } else {
                    new DmAlertMessageBox("Medicine Name is mandatory.", txtHItemName).show();

                }
            }
        });
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

    private void manageDisplay() {
        txtUnit.setReadOnly(true);
        if (enquiry.topControl.getMode().equalsIgnoreCase("Browse")) {
            btnAddIndent.removeFromParent();
        } else {
            flwIndentBtn.clear();
            flwIndentBtn.add(btnAddIndent);
        }
    }
}
