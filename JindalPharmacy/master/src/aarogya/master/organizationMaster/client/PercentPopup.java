package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.*;
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
import com.sencha.gxt.widget.core.client.event.HeaderClickEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmInputBox.client.DmInputBox;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;

public class PercentPopup extends PercentPopup_Dsg implements IsWidget, ClickHandler {

    interface CallBack {

        public void onOk(DiscountPercent_Mdl mrTemp);

        public void onClose();
    }
    private CallBack callBack;
    private final DmInputBox inputBox = new DmInputBox();
    private final CategoryWiseDiscountGridEdit<GrdCategoryDiscount_Mdl, GrdCategoryWiseDiscount_PrpAcs> categoryDiscountGridEdit = new CategoryWiseDiscountGridEdit(grdCategoryWiseDiscount, "Category Wise Discount");
    private final ServiceInvoker<OrganizationMaster_SrvAsync> srvAsync;
    private String mode;

    public PercentPopup() {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(OrganizationMaster_Srv.class), OrganizationMaster.class.getName());
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

    protected void show(DiscountPercent_Mdl mrTemp, String mode) {
        this.mode = mode;
        if (this.mode.equalsIgnoreCase("Browse")) {
            flwOk.remove(btnOk);
            flwCancel.add(btnCancel);
        } else {
            flwOk.add(vl.add(btnOk));
            flwCancel.add(btnCancel);
        }
        pnlForm.show();
        pnlForm.center();
        pnlForm.setGlassEnabled(true);

        clear();
        setData(mrTemp);
        txtDiscountPharmacyPer.setFocus(true);
    }

    private void setData(DiscountPercent_Mdl data) {
        if (data != null) {
            txtHPharmacyDiscount.setTag(data.isPharmacyDiscountAllow() ? "1" : "0");
            txtHPharmacyDiscount.setText(data.isPharmacyDiscountAllow() ? "Yes" : "No");
            txtHMedicineSaleOn.setTag(data.getMedicineSaleOn() == null ? DmShared.iNull(MedicineSaleOn.SALE_RATE.getId()).toString() : DmShared.iNull(data.getMedicineSaleOn().getId()).toString());
            txtHMedicineSaleOn.setText(data.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE.getValue() : data.getMedicineSaleOn().getValue());
            txtDiscountPharmacyPer.setText(data.getPharmacyDiscountPercentage());
            grdCategoryWiseDiscount.getGrid().getStore().clear();
            grdCategoryWiseDiscount.getGrid().getStore().addAll(data.getLstCategoryDiscount());
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnCancel)) {
            this.hide(true);
        } else if (event.getSource().equals(btnOk)) {
            try {
                DiscountPercent_Mdl mrTemp = new DiscountPercent_Mdl();
                mrTemp.setPharmacyDiscountAllow(DmShared.iNull(txtHPharmacyDiscount.getTag()).intValue() == 1);
                mrTemp.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(txtHMedicineSaleOn.getTag()).intValue()));
                mrTemp.setPharmacyDiscountPercentage(DmClient.vNull(txtDiscountPharmacyPer.getText(), "0.00"));
                mrTemp.getLstCategoryDiscount().clear();
                mrTemp.getLstCategoryDiscount().addAll(grdCategoryWiseDiscount.getGrid().getStore().getAll());
                callBack.onOk(mrTemp);
                this.hide(true);
            } catch (Exception e) {
            }
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
                        hide(true);
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
        if (mode.equalsIgnoreCase("Browse")) {
            categoryDiscountGridEdit.show(currentRowIndex, false, false, false);
        } else {
            categoryDiscountGridEdit.show(currentRowIndex, true, false, false);
        }
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

    private void clear() {

        grdCategoryWiseDiscount.getGrid().getStore().clear();
        txtDiscountPharmacyPer.setTag("");
        txtDiscountPharmacyPer.setTag2nd("");
        txtDiscountPharmacyPer.setText("");
        txtHMedicineSaleOn.setTag("");
        txtHMedicineSaleOn.setTag2nd("");
        txtHMedicineSaleOn.setText("");
        txtHPharmacyDiscount.setTag("");
        txtHPharmacyDiscount.setTag2nd("");
        txtHPharmacyDiscount.setText("");
    }
}
