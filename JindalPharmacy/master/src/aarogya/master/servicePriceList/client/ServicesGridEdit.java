package aarogya.master.servicePriceList.client;

import aarogya.master.servicePriceList.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import java.util.List;

public class ServicesGridEdit<T1, T2> extends ServicesGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, BlurHandler {

    private final RoomCategoryGridEdit<GrdRoomCategory_Mdl, GrdRoomCate_PrpAcs> roomCategoryGridEdit = new RoomCategoryGridEdit(grdCategory);
    private final ServiceInvoker<ServicePriceList_SrvAsync> srvAsync;
    private CallBack callBack;
    private GrdServices_Mdl parentRow;
    private ServicePriceList clsParent;

    interface CallBack {

        public void refresh();
    }

    private void gridEvents() {
        roomCategoryGridEdit.addCallBack(new RoomCategoryGridEdit.CallBack() {

            @Override
            public void onOk(GrdRoomCategory_Mdl grdtest) {
                grdCategory.commitChanges();
            }

            @Override
            public void onDelete() {

            }
        });

        grdCategory.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridRoomCategoryEdit(event.getRowIndex());
            }
        });
        grdCategory.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridRoomCategoryEdit(grdCategory.getGrid().getStore().indexOf(grdCategory.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean newNeeded, boolean deleteNeeded, ServicePriceList master) {
        this.clsParent = master;
        super.showCenter(currentRowIndex, okNeeded, false, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtRate.setFocus(true);
            }
        });
    }

    @Override
    public void displayRow(T1 row) {
        GrdServices_Mdl ourRow = (GrdServices_Mdl) row;
        parentRow = new GrdServices_Mdl();

        txtCategory.setText(DmShared.xNull(ourRow.getCategoryText()));

        txtServices.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtServices.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtOrganizationService.setTag(DmShared.xNull(ourRow.getOrganizationServiceCode()));
        txtOrganizationService.setText(DmShared.xNull(ourRow.getOrganizationServiceName()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtDiscount.setText(DmClient.vNull(ourRow.getDiscountAmount(), "0.00"));
        txtDiscountPer.setText(DmClient.vNull(ourRow.getDiscountPercentage(), "0.00"));
        txtPSNo.setText(DmClient.vNull(ourRow.getPrintSNo(), "0"));
        getRoomCategoryRate();
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdServices_Mdl();
    }

    @Override
    public void onOk() {
        boolean flag = false;
        if (DmShared.iNull(txtPSNo.getText()).intValue() > 0) {
            flag = true;
        }
        if (flag) {
            if (DmShared.vNull(txtRate.getText()).doubleValue() <= 0) {
                txtRate.setFocus(true);
                new DmAlertMessageBox("Rate is mandatory.", txtRate).show();
                return;
            }
        }
        if (DmShared.vNull(txtDiscount.getText()).doubleValue() > 0 || DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            if (DmShared.vNull(txtRate.getText()).doubleValue() <= 0) {
                new DmAlertMessageBox("Rate is mandatory when a discount is gven.", txtRate).show();
                return;
            }
        }

        if (DmShared.vNull(txtRate.getText()).doubleValue() > 0) {
            if (DmShared.vNull(txtDiscount.getText()).doubleValue() > DmShared.vNull(txtRate.getText()).doubleValue()) {
                new DmAlertMessageBox("Discount should not be greater than rate.", txtDiscount).show();
                return;
            }
        }

        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 100) {
            new DmAlertMessageBox("Discount % should not be greater than 100%.", txtDiscountPer).show();
            return;
        }

        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        final GrdServices_Mdl ourRow = (GrdServices_Mdl) row;
        ourRow.setCategoryText(DmShared.xNull(txtCategory.getText()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtServices.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtServices.getText()));
        ourRow.setOrganizationServiceCode(DmShared.xNull(txtOrganizationService.getTag()));
        ourRow.setOrganizationServiceName(DmShared.xNull(txtOrganizationService.getText()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setDiscountAmount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setDiscountPercentage(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
        ourRow.setPrintSNo(DmClient.vNull(txtPSNo.getText(), "0"));
        ourRow.getLstRoomCategory().clear();
        ourRow.getLstRoomCategory().addAll(grdCategory.getGrid().getStore().getAll());
        new DmConfirmMessageBox("Are you sure? You want to save price.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    saveRecord(ourRow);
                }
            }
        }).show();

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
        new DmConfirmMessageBox("Are you sure? You want to save price.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    srvAsync.invokeRPC().deleteRecord(txtServices.getTag(), getParam(), new AsyncCallback<DmResult<String>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtServices).show();
                        }

                        @Override
                        public void onSuccess(DmResult<String> result) {
                            if (result.isStatus()) {
                                callBack.refresh();
                                hide();
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtServices).show();
                            }
                        }
                    });
                }
            }
        }).show();

    }

    private void getRoomCategoryRate() {
        SearchValue_Mdl param = getParam();
        srvAsync.invokeRPC().getRoomCategoryViaService(param, new AsyncCallback<DmResult<List<GrdRoomCategory_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtCategory).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdRoomCategory_Mdl>> result) {
                if (result.isStatus()) {
                    grdCategory.getGrid().getStore().clear();
                    grdCategory.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtCategory).show();
                }
            }
        });
    }

    private SearchValue_Mdl getParam() {
        SearchValue_Mdl param = new SearchValue_Mdl();
        param.setApplicableDate(clsParent.txtApplicableDate.getText());
        param.setOrganizationCode(clsParent.txtName.getTag());
        param.setServiceCode(txtServices.getTag());
        return param;
    }

    private void gridRoomCategoryEdit(Integer currentRowIndex) {
        roomCategoryGridEdit.show(currentRowIndex, true, false, false);
    }

    public ServicesGridEdit(DmGrid<T1, T2> grid, String widgetName) {
        super(grid, widgetName, "425px", "420px");
        srvAsync = new ServiceInvoker<>(GWT.create(ServicePriceList_Srv.class), ServicePriceList.class.getName());
        gridEvents();
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
            roomCategoryGridEdit.hide();
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

    private void saveRecord(GrdServices_Mdl mrTemp) {
        SearchValue_Mdl param = getParam();
        srvAsync.invokeRPC().saveRecord(mrTemp, param, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtRate).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    callBack.refresh();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtRate).show();
                }
            }
        });
    }
}
