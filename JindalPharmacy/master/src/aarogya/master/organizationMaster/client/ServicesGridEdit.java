package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.*;
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
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.ArrayList;
import java.util.List;

public class ServicesGridEdit<T1, T2> extends ServicesGridEdit_Dsg<T1, T2> implements AttachEvent.Handler, BlurHandler {

    private final RoomCategoryGridEdit<GrdRoomCategory_Mdl, GrdRoomCate_PrpAcs> roomCategoryGridEdit = new RoomCategoryGridEdit(grdRoomCategory);
    private final ServiceInvoker<OrganizationMaster_SrvAsync> srvAsync;
    private CallBack callBack;
    private GrdServices_Mdl parentRow;
    private OrganizationMaster orgMst;
    private boolean flag;

    interface CallBack {

        public void onOk(GrdServices_Mdl mrTemp);
    }

    public ServicesGridEdit(DmGrid<T1, T2> grid, String widgetName) {
        super(grid, widgetName, "425px", "420px");
        srvAsync = new ServiceInvoker<>(GWT.create(OrganizationMaster_Srv.class), OrganizationMaster.class.getName());
        gridEvents();
        txtHelpEvents();
        txtDiscount.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtRate.addBlurHandler(this);
    }

    public void show(int currentRowIndex, boolean okNeeded, boolean deleteNeeded, OrganizationMaster master) {
        this.orgMst = master;
        super.showCenter(currentRowIndex, okNeeded, false, deleteNeeded);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHServiceCat.setFocus(true);
            }
        });
    }

    private void gridEvents() {
        roomCategoryGridEdit.addCallBack(new RoomCategoryGridEdit.CallBack() {

            @Override
            public void onOk(GrdRoomCategory_Mdl grdtest) {
                grdRoomCategory.commitChanges();
            }

            @Override
            public void onDelete() {

            }
        });

        grdRoomCategory.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridRoomCategoryEdit(event.getRowIndex());
            }
        });
        grdRoomCategory.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridRoomCategoryEdit(grdRoomCategory.getGrid().getStore().indexOf(grdRoomCategory.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void displayRow(T1 row) {
        GrdServices_Mdl ourRow = (GrdServices_Mdl) row;
        List<GrdRoomCategory_Mdl> lstCategory = new ArrayList<GrdRoomCategory_Mdl>();
        flag = false;
        parentRow = new GrdServices_Mdl();
        // hashCode = ourRow.getSharedKeyFieldHashCode();
        txtHServiceCat.setText(DmShared.xNull(ourRow.getCategoryText()));
        txtHServiceCat.setTag(DmShared.xNull(ourRow.getCategoryCode()));
        txtHService.setTag(DmShared.xNull(ourRow.getSharedKeyFieldCode()));
        txtHService.setText(DmShared.xNull(ourRow.getSharedKeyFieldName()));
        txtRate.setText(DmClient.vNull(ourRow.getRate(), "0.00"));
        txtDiscount.setText(DmClient.vNull(ourRow.getDiscountAmount(), "0.00"));
        txtDiscountPer.setText(DmClient.vNull(ourRow.getDiscountPercentage(), "0.00"));
        txtPSNo.setText(DmClient.vNull(ourRow.getPrintSNo(), "0"));
        grdRoomCategory.getGrid().getStore().clear();
        for (int i = 0; i < orgMst.roomCategory.size(); i++) {
            GrdRoomCategory_Mdl nodeCategory = new GrdRoomCategory_Mdl();
            GrdRoomCategory_Mdl category = orgMst.roomCategory.get(i);
            flag = false;
            for (int j = 0; j < ourRow.getLstRoomCategory().size(); j++) {
                GrdRoomCategory_Mdl roomCategory = ourRow.getLstRoomCategory().get(j);
                if (DmShared.xNull(category.getSharedKeyFieldCode()).equalsIgnoreCase(DmShared.xNull(roomCategory.getSharedKeyFieldCode()))) {
                    nodeCategory = roomCategory.clone();
                    lstCategory.add(nodeCategory);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                nodeCategory = category.clone();
                if (DmShared.xNull(txtHService.getTag()).equalsIgnoreCase("")) {
                    nodeCategory.setRate("0.00");
                }
                lstCategory.add(nodeCategory);
            }

        }
        grdRoomCategory.getGrid().getStore().addAll(lstCategory);
    }

    @Override
    public T1 getNewRow() {
        return (T1) new GrdServices_Mdl();
    }

    @Override
    public void onOk() {
        boolean flag = false;
        if (DmShared.iNull(txtHServiceCat.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Service Category is mandatory.", txtHServiceCat).show();
            return;
        }
        if (DmShared.iNull(txtHService.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Service is mandatory", txtHService).show();
            return;
        }
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

//        new DmConfirmMessageBox("Are you sure? You want to save price.", new DialogHideEvent.DialogHideHandler() {
//
//            @Override
//            public void onDialogHide(DialogHideEvent event) {
//                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
//                    saveRecord(saveInitialize(new GrdServices_Mdl()));
//                }
//            }
//        }).show();
        proceedOk();
    }

    @Override
    public void manageOk(T1 row) {
        flag = false;
        final GrdServices_Mdl ourRow = (GrdServices_Mdl) row;
        callBack.onOk(saveInitialize(ourRow));
    }

    private GrdServices_Mdl saveInitialize(GrdServices_Mdl ourRow) {
        ourRow.setCategoryText(DmShared.xNull(txtHServiceCat.getText()));
        ourRow.setCategoryCode(DmShared.xNull(txtHServiceCat.getTag()));
        ourRow.setSharedKeyFieldCode(DmShared.xNull(txtHService.getTag()));
        ourRow.setSharedKeyFieldName(DmShared.xNull(txtHService.getText()));
        ourRow.setServiceCode(DmShared.xNull(txtHService.getTag()));
        ourRow.setRate(DmClient.vNull(txtRate.getText(), "0.00"));
        ourRow.setDiscountAmount(DmClient.vNull(txtDiscount.getText(), "0.00"));
        ourRow.setDiscountPercentage(DmClient.vNull(txtDiscountPer.getText(), "0.00"));
        ourRow.setPrintSNo(DmClient.vNull(txtPSNo.getText(), "0"));
        ourRow.getLstRoomCategory().clear();
//        ourRow.getLstRoomCategory().addAll(grdRoomCategory.getGrid().getStore().getAll());
        for (GrdRoomCategory_Mdl category : grdRoomCategory.getGrid().getStore().getAll()) {
            if (!DmShared.xNull(category.getSharedKeyFieldName()).equalsIgnoreCase("")) {
                if (DmShared.vNull(category.getRate()).doubleValue() > 0) {
                    ourRow.getLstRoomCategory().add(category.clone());
                }
            }
        }
        return ourRow;
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
    }

//    private void getRoomCategoryRate() {
//        SearchValue_Mdl param = getParam();
//        srvAsync.invokeRPC().getRoomCategoryViaService(param, new AsyncCallback<DmResult<List<GrdRoomCategory_Mdl>>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtHServiceCat).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<List<GrdRoomCategory_Mdl>> result) {
//                if (result.isStatus()) {
//                    grdRoomCategory.getGrid().getStore().clear();
//                    grdRoomCategory.getGrid().getStore().addAll(result.getData());
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtHServiceCat).show();
//                }
//            }
//        });
//    }
    private SearchValue_Mdl getParam() {
        SearchValue_Mdl param = new SearchValue_Mdl();
        if (DmShared.dNull(orgMst.txtServiceAppcblDate.getText()).equalsIgnoreCase("")) {
            param.setApplicableDate(DmShared.dNull(DmClient.getStartDate()));
        } else {
            param.setApplicableDate(DmShared.dNull(orgMst.txtServiceAppcblDate.getText()));
        }
        param.setRateListCode(orgMst.txtHRateList.getTag());
        param.setServiceCode(txtHService.getTag());
        return param;
    }

    private void gridRoomCategoryEdit(Integer currentRowIndex) {
        roomCategoryGridEdit.show(currentRowIndex, true, false, false);
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

//    private void saveRecord(GrdServices_Mdl mrTemp) {
//        SearchValue_Mdl param = getParam();
//        srvAsync.invokeRPC().saveServiceRecord(mrTemp, param, new AsyncCallback<DmResult<String>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtRate).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<String> result) {
//                if (result.isStatus()) {
//                    proceedOk();
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtRate).show();
//                }
//            }
//        });
//    }
    private void txtHelpEvents() {
        txtHService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHService(helper, txtHServiceCat.getTag(), blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHServiceCat.setText(DmShared.xNull(t1Row.getSharedPreparedBy()));
                    txtHServiceCat.setTag(DmShared.xNull(t1Row.getSharedModifiedBy()));
                }
            }
        });
        txtHServiceCat.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHServiceCat(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceCat).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceCat.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceCat).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }
}
