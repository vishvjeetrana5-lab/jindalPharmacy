package aarogya.master.servicePriceList.client;

import aarogya.master.servicePriceList.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmInputBox.client.DmInputBox;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PercentPopup extends PercentPopup_Dsg implements IsWidget, ClickHandler {

    interface CallBack {

        public void onSave(ServicePriceMaster_Mdl mrTemp);

        public void onClose();
    }
    private CallBack callBack;
    private final DmInputBox inputBox = new DmInputBox();
    private final CategoryWiseDiscountGridEdit<GrdCategoryDiscount_Mdl, GrdCategoryWiseDiscount_PrpAcs> categoryDiscountGridEdit = new CategoryWiseDiscountGridEdit(grdCategoryWiseDiscount, "Category Wise Discount");
    private final ServiceInvoker<ServicePriceList_SrvAsync> srvAsync;
    private String organizationCode = "";

    public PercentPopup() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(ServicePriceList_Srv.class), ServicePriceList.class.getName());
        handlerEvent();
        gridEvents();
        textHelpEvents();
    }

    @Override
    public Widget asWidget() {
        return pnlForm;
    }

    private void handlerEvent() {
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
    }

    protected void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    protected void show(ServicePriceMaster_Mdl mrTemp) {
        pnlForm.show();
        pnlForm.center();
        pnlForm.setGlassEnabled(true);
        clear();
        setData(mrTemp);
        txtDiscountServicePer.setFocus(true);
    }

    private void setData(ServicePriceMaster_Mdl data) {
        if (data != null) {
            organizationCode = data.getOrganizationCode();
            txtHPharmacyDiscount.setTag(data.isAllowPharmacyDiscount() ? "1" : "0");
            txtHPharmacyDiscount.setText(data.isAllowPharmacyDiscount() ? "Yes" : "No");
            txtHMedicineSaleOn.setTag(data.getMedicineSaleOn() == null ? DmShared.iNull(MedicineSaleOn.SALE_RATE.getId()).toString() : DmShared.iNull(data.getMedicineSaleOn().getId()).toString());
            txtHMedicineSaleOn.setText(data.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE.getValue() : data.getMedicineSaleOn().getValue());
            txtDiscountPharmacyPer.setText(data.getPharmacyDiscountPer());
            txtDiscountFeesPer.setText(data.getFeesDiscountPer());
            txtDiscountServicePer.setText(data.getServiceDiscountPer());
            txtDiscountTestPer.setText(data.getTestDiscountPer());
            grdCategoryWiseDiscount.getGrid().getStore().clear();
            grdCategoryWiseDiscount.getGrid().getStore().addAll(data.getLstServiceCategory());
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            this.hide(true);
        } else if (event.getSource().equals(btnOk)) {
            new DmConfirmMessageBox("Do you want to save this record?", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        saveRecord();
                    }
                }
            }).show();
        }
    }

    private void gridEvents() {
        grdCategoryWiseDiscount.getGrid().addHeaderClickHandler(new HeaderClickEvent.HeaderClickHandler() {
            @Override
            public void onHeaderClick(HeaderClickEvent event) {
                if (event.getColumnIndex() == colDiscount) {
                    inputBox.show(txtDiscountPharmacyPer, "Discount %", "", 6, "Discount %", "200px");
                }
            }
        });
        categoryDiscountGridEdit.addCallBack(new CategoryWiseDiscountGridEdit.CallBack() {
            @Override
            public void onOk(GrdCategoryDiscount_Mdl grdtest) {
                grdCategoryWiseDiscount.commitChanges();
            }

            @Override
            public void onDelete() {
                grdCategoryWiseDiscount.commitChanges();
            }
        });
        grdCategoryWiseDiscount.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                gridCategoryWiseDiscountEdit(event.getRowIndex());
            }
        });
        grdCategoryWiseDiscount.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridCategoryWiseDiscountEdit(grdCategoryWiseDiscount.getGrid().getStore().indexOf(grdCategoryWiseDiscount.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void textHelpEvents() {
        inputBox.addCallBack(new DmInputBox.CallBack() {
            @Override
            public void onOk(String inputValue, String parentName) {
                if (DmShared.isNumeric(inputValue)) {
                    if (DmShared.vNull(inputValue).doubleValue() > 0) {
                        for (int i = 0; i < grdCategoryWiseDiscount.getGrid().getStore().getAll().size(); i++) {
                            if (DmShared.vNull(grdCategoryWiseDiscount.getGrid().getStore().get(i).getDiscountPercentage()).doubleValue() == 0) {
                                grdCategoryWiseDiscount.getGrid().getStore().get(i).setDiscountPercentage(DmClient.vNull(inputValue, "0.00"));
                                grdCategoryWiseDiscount.getGrid().getStore().get(i).setColorChange(true);
                                grdCategoryWiseDiscount.commitChanges();
                            }
                        }
                    }
                } else {
                    new DmAlertMessageBox("Please Input numeric value.", (DmTextBox) null).show();
                }
            }
        });
        txtHMedicineSaleOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineSaleOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineSaleOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineSaleOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineSaleOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPharmacyDiscount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPharmacyDiscount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPharmacyDiscount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPharmacyDiscount).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

    private void gridCategoryWiseDiscountEdit(Integer currentRowIndex) {
        categoryDiscountGridEdit.show(currentRowIndex, true, false, false);
    }

    public void hide() {
        hide(false);
    }

    private void hide(boolean isCallEvent) {
        this.pnlForm.hide();
        if (isCallEvent) {
            callBack.onClose();
        }
    }

    private ServicePriceMaster_Mdl saveInitialize() {
        ServicePriceMaster_Mdl mrTemp = new ServicePriceMaster_Mdl();
        mrTemp.setOrganizationCode(organizationCode);
        mrTemp.setAllowPharmacyDiscount(DmShared.iNull(txtHPharmacyDiscount.getTag()).intValue() == 1);
        mrTemp.setFeesDiscountPer(DmClient.vNull(txtDiscountFeesPer.getText(), "0.00"));
        mrTemp.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(txtHMedicineSaleOn.getTag()).intValue()));
        mrTemp.setPharmacyDiscountPer(DmClient.vNull(txtDiscountPharmacyPer.getText(), "0.00"));
        mrTemp.setServiceDiscountPer(DmClient.vNull(txtDiscountServicePer.getText(), "0.00"));
        mrTemp.setTestDiscountPer(DmClient.vNull(txtDiscountTestPer.getText(), "0.00"));
        mrTemp.getLstServiceCategory().clear();
        mrTemp.getLstServiceCategory().addAll(grdCategoryWiseDiscount.getGrid().getStore().getAll());
        return mrTemp;
    }

    private void saveRecord() {
        final ServicePriceMaster_Mdl mrTemp = saveInitialize();
        try {
            ServicePriceList_Vld.chkSavePercent(mrTemp);
            srvAsync.invokeRPC().savePopup(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), btnOk).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        callBack.onSave(mrTemp);
                        clear();
                        hide(false);
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), btnOk).show();
                    }
                }
            });
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), btnOk).show();
        }
    }

    private void clear() {
        organizationCode = "";
        grdCategoryWiseDiscount.getGrid().getStore().clear();
        txtDiscountPharmacyPer.setTag("");
        txtDiscountPharmacyPer.setTag2nd("");
        txtDiscountPharmacyPer.setText("");
        txtDiscountFeesPer.setTag("");
        txtDiscountFeesPer.setTag2nd("");
        txtDiscountFeesPer.setText("");
        txtDiscountServicePer.setTag("");
        txtDiscountServicePer.setTag2nd("");
        txtDiscountServicePer.setText("");
        txtDiscountTestPer.setTag("");
        txtDiscountTestPer.setTag2nd("");
        txtDiscountTestPer.setText("");
        txtHMedicineSaleOn.setTag("");
        txtHMedicineSaleOn.setTag2nd("");
        txtHMedicineSaleOn.setText("");
        txtHPharmacyDiscount.setTag("");
        txtHPharmacyDiscount.setTag2nd("");
        txtHPharmacyDiscount.setText("");
    }
}
