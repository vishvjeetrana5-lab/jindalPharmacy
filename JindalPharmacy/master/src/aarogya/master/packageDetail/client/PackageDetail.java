package aarogya.master.packageDetail.client;

import aarogya.master.packageDetail.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import java.util.List;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;

public class PackageDetail extends PackageDetail_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler {

    public interface CallBack {

        public void onClose();
    }

    private CallBack callBack;
    private ServiceInvoker<PackageDetail_SrvAsync> srvAsync;
    private final ServiceGridEdit<GrdServiceMaster_Mdl, GridService_PrpAcs> gridServiceEdit = new ServiceGridEdit(grdService);
    private final TestGridEdit<GrdServiceMaster_Mdl, GridInvestigation_PrpAcs> gridTestEdit = new TestGridEdit(grdInvestigation);
    private final ItemGridEdit<GrdServiceMaster_Mdl, GridItem_PrpAcs> gridItemEdit = new ItemGridEdit(grdItem);
    private RoomCategoryDetail roomCategoryDetail = null;
    private RoomCategoryDetail.CallBack roomCallBack;
    private PackageDetail_Mdl packageDetail_Mdl;
    protected String code, packageCode, organizationCode, roomCategoryCode, parentPackageCode, fromDate = "", toDate = "";
    private boolean isEditable = false, isAddDelete = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvents() {
        roomCallBack = new RoomCategoryDetail.CallBack() {
            @Override
            public void onClose() {
                roomCategoryDetail = null;
                ((CellSelectionModel) grdService.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        };
        txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHSchedule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSchedule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSchedule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSchedule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSchedule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHPackage.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<TxtHPackage_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPackage(false, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHPackage_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHPackage_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHPackage.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHPackage_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    copyPackageDetail(DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                } else {
                    copyPackageDetail("");
                }
            }
        });
    }

    private void gridEvents() {
        gridTestEdit.addCallBack(new TestGridEdit.CallBack() {

            @Override
            public void onOk(GrdServiceMaster_Mdl node) {
                grdInvestigation.commitChanges();
                calculateInvestigationAmt();
            }

            @Override
            public void onDelete() {
                grdInvestigation.commitChanges();
                calculateInvestigationAmt();
            }
        });
        grdInvestigation.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showTestGridEdit(event.getRowIndex());
            }
        });
        grdInvestigation.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showTestGridEdit(grdInvestigation.getGrid().getStore().indexOf(grdInvestigation.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        gridItemEdit.addCallBack(new ItemGridEdit.CallBack() {

            @Override
            public void onOk(GrdServiceMaster_Mdl node) {
                grdItem.commitChanges();
                calculateItemAmt();
            }

            @Override
            public void onDelete() {
                grdItem.commitChanges();
                calculateItemAmt();
            }
        });
        grdItem.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showItemGridEdit(event.getRowIndex());
            }
        });
        grdItem.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemGridEdit(grdItem.getGrid().getStore().indexOf(grdItem.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        gridServiceEdit.addCallBack(new ServiceGridEdit.CallBack() {

            @Override
            public void onOk(GrdServiceMaster_Mdl node) {
                grdService.commitChanges();
                calculateServiceAmt();
            }

            @Override
            public void onDelete() {
                grdService.commitChanges();
                calculateServiceAmt();
            }
        });
        grdService.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showServiceGridEdit(event.getRowIndex());
            }
        });
        grdService.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {

                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showServiceGridEdit(grdService.getGrid().getStore().indexOf(grdService.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showServiceGridEdit(int currentRowIndex) {
        if (isEditable) {
            gridServiceEdit.show(currentRowIndex, true, isAddDelete, isAddDelete, this);
        } else {
            gridServiceEdit.show(currentRowIndex, false, false, false, this);
        }
    }

    private void showTestGridEdit(int currentRowIndex) {
        if (isEditable) {
            gridTestEdit.show(currentRowIndex, true, isAddDelete, isAddDelete, this);
        } else {
            gridTestEdit.show(currentRowIndex, false, false, false, this);
        }
    }

    private void showItemGridEdit(int currentRowIndex) {
        if (isEditable) {
            gridItemEdit.show(currentRowIndex, true, isAddDelete, isAddDelete);
        } else {
            gridItemEdit.show(currentRowIndex, false, false, false);
        }
    }

    public void show(String code, String packageCode, String organizationCode, String roomCategoryCode, String parentPackageCode, boolean isEditable, boolean isAddDelete) {
        this.code = code;
        this.packageCode = packageCode;
        this.organizationCode = organizationCode;
        this.roomCategoryCode = roomCategoryCode;
        this.parentPackageCode = parentPackageCode;
        this.isEditable = isEditable;
        this.isAddDelete = isAddDelete;
        this.fromDate = "";
        this.toDate = "";

        popupPanel.show();
        popupPanel.center();
        popupPanel.setGlassEnabled(true);

        flwDelete.clear();
        flwSave.clear();
        flwRoomCategory.clear();
        flwPackageCode.clear();
        flwRoomCategoryName.clear();
        flwCopyPackage.clear();
        if (isEditable) {
            flwSave.add(vl.add(btnSave));
            flwDelete.add(btnDelete);
        }
        if (DmShared.iNull(roomCategoryCode).longValue() == 0) {
            flwCopyPackage.add(btnCopyPackage);
            flwRoomCategory.add(btnRoomCategory);
        }
        if (isAddDelete) {
            txtOrgCode.setEnabled(true);
            txtFromDate.setEnabled(true);
            txtToDate.setEnabled(true);
            txtHSchedule.setEnabled(true);
            txtHActive.setEnabled(true);
        }
        clear();

        if (DmShared.iNull(code).longValue() == 0 && DmShared.iNull(parentPackageCode).longValue() != 0) {
            getPackageDetail(parentPackageCode);
        } else {
            getPackageDetail(code);
        }

        tabPanel.selectTab(0);
    }

    public void setApplicableDate(String fromDate, String toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        txtFromDate.setText(DmShared.dNull(fromDate));
        txtToDate.setText(DmShared.dNull(toDate));
    }

    public void hide() {
        this.popupPanel.hide();
        callBack.onClose();
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtFromDate) {
            txtFromDate.setText(DmClient.returnDate(txtFromDate.getText()));
        }
        if (event.getSource() == txtToDate) {
            txtToDate.setText(DmClient.returnDate(txtToDate.getText()));
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnCancel) {
            hide();
        } else if (event.getSource() == btnSave) {
            saveRecord();
        } else if (event.getSource() == btnDelete) {
            new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        deleteRecord(DmShared.iNull(packageDetail_Mdl.getSharedKeyFieldCode()).toString());
                    }
                }
            }).show();
        } else if (event.getSource() == btnCopyPackage) {
            txtHPackage.show(btnCopyPackage, "");
            txtHPackage.reQuery("");
        } else if (event.getSource() == btnRoomCategory) {
            if (DmShared.iNull(code).longValue() != 0) {
                roomCategoryDetail = new RoomCategoryDetail();
                roomCategoryDetail.addCallBack(roomCallBack);
                roomCategoryDetail.show(code, packageCode, organizationCode, DmShared.dNull(txtFromDate.getText()), DmShared.dNull(txtToDate.getText()));
            } else {
                new DmAlertMessageBox("Please save this record before open Room Category Wise Detail.", txtHActive).show();
            }
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            gridServiceEdit.hide();
            gridItemEdit.hide();
            gridTestEdit.hide();
            hide();
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            if (isEditable) {
                saveRecord();
            }
        } else if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_D && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            if (isEditable) {
                deleteRecord(DmShared.iNull(packageDetail_Mdl.getSharedKeyFieldCode()).toString());
            }
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

    public PackageDetail() {
        super();
        srvAsync = new ServiceInvoker<PackageDetail_SrvAsync>(GWT.create(PackageDetail_Srv.class), PackageDetail.class.getName());
        manageEvent();
        manageDisplay();
        textHelpEvents();
        gridEvents();
    }

    private void manageEvent() {
        btnCancel.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnDelete.addClickHandler(this);
        btnCopyPackage.addClickHandler(this);
        btnRoomCategory.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        txtFromDate.addBlurHandler(this);
        txtToDate.addBlurHandler(this);
    }

    private void manageDisplay() {
        txtPackageName.setEnabled(false);
        txtPackageShortName.setEnabled(false);
        txtPackageNature.setEnabled(false);
        txtPackageAmt.setEnabled(false);
        txtOrganizationName.setEnabled(false);
        txtOrgCode.setEnabled(false);
        txtFromDate.setEnabled(false);
        txtToDate.setEnabled(false);
        txtHSchedule.setEnabled(false);
        txtHActive.setEnabled(false);
        txtRateListAmt.setEnabled(false);
    }

    private void getPackageDetail(String code) {
        srvAsync.invokeRPC().getPackageDetail(code, packageCode, organizationCode, roomCategoryCode, new AsyncCallback<DmResult<PackageDetail_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PackageDetail_Mdl> result) {
                if (result.isStatus()) {
                    fillRecord(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillRecord(PackageDetail_Mdl mrTemp) {
        clear();
        tabPanel.selectTab(0);
        packageDetail_Mdl = null;
        if (mrTemp != null) {
            if (DmShared.iNull(code).longValue() == 0 && DmShared.iNull(parentPackageCode).longValue() != 0) {
                code = "";
                roomCategoryCode = DmShared.xNull(mrTemp.getDefaultRoomCategoryCode());
                mrTemp.setSharedKeyFieldCode(code);
                mrTemp.setRoomCategoryCode(mrTemp.getDefaultRoomCategoryCode());
                mrTemp.setRoomCategoryName(mrTemp.getDefaultRoomCategoryName());
            } else {
                code = DmShared.xNull(mrTemp.getSharedKeyFieldCode());
                roomCategoryCode = DmShared.xNull(mrTemp.getRoomCategoryCode());
            }

            packageDetail_Mdl = mrTemp;
            packageCode = DmShared.xNull(mrTemp.getPackageCode());
            organizationCode = DmShared.xNull(mrTemp.getOrganizationCode());

            txtPackageName.setTag(DmShared.xNull(mrTemp.getPackageCode()));
            txtPackageName.setText(DmShared.xNull(mrTemp.getPackageName()));
            txtPackageShortName.setText(DmShared.xNull(mrTemp.getPackageShortName()));
            txtPackageNature.setTag(DmShared.xNull(mrTemp.getPackageNatureCode()));
            txtPackageNature.setText(DmShared.xNull(mrTemp.getPackageNatureName()));
            txtOrganizationName.setTag(DmShared.xNull(mrTemp.getOrganizationCode()));
            txtOrganizationName.setText(DmShared.xNull(mrTemp.getOrganizationName()));
            txtOrgCode.setText(DmShared.xNull(mrTemp.getOrgCode()));
            txtFromDate.setText(DmShared.dNull(mrTemp.getFromDate()));
            txtToDate.setText(DmShared.dNull(mrTemp.getToDate()));
            txtHSchedule.setTag(DmShared.xNull(mrTemp.getScheduleCode()));
            txtHSchedule.setText(DmShared.xNull(mrTemp.getScheduleName()));
            txtHActive.setTag(DmShared.xNull(mrTemp.getActiveCode()));
            txtHActive.setText(DmShared.xNull(mrTemp.getActiveName()));
            txtRateListAmt.setText(DmClient.vNull(mrTemp.getRateListPrice(), "0.00"));
            txtRateListAmt.setTag(DmShared.xNull(mrTemp.getRateListCode()));
            fillDefault();

            if (!DmShared.xNull(mrTemp.getRoomCategoryName()).trim().equalsIgnoreCase("")) {
                lblRoomCategoryName.setText(mrTemp.getRoomCategoryName());
                flwRoomCategoryName.add(lblRoomCategoryName);
            } else {
                lblRoomCategoryName.setText("");
                flwRoomCategoryName.clear();
            }

            if (DmShared.iNull(code).longValue() != 0) {
                lblPackageCode.setText(code);
                flwPackageCode.add(lblPackageCode);
            } else {
                lblPackageCode.setText("");
                flwPackageCode.clear();
            }

            grdService.getGrid().getStore().addAll(mrTemp.getListService());
            grdInvestigation.getGrid().getStore().addAll(mrTemp.getListInvestigation());
            grdItem.getGrid().getStore().addAll(mrTemp.getListMedicineItem());

            calculateInvestigationAmt();
            calculateItemAmt();
            calculateServiceAmt();

            if (isAddDelete) {
                grdItem.getGrid().getStore().add(new GrdServiceMaster_Mdl());
                grdInvestigation.getGrid().getStore().add(new GrdServiceMaster_Mdl());
                grdService.getGrid().getStore().add(new GrdServiceMaster_Mdl());
                txtOrgCode.setFocus(true);
            } else {
                ((CellSelectionModel) grdService.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        }
    }

    private PackageDetail_Mdl getValue() {
        PackageDetail_Mdl mrTemp = new PackageDetail_Mdl();

        calculateInvestigationAmt();
        calculateItemAmt();
        calculateServiceAmt();
        mrTemp = packageDetail_Mdl;
        mrTemp.setRoomCategoryCode(DmShared.xNull(roomCategoryCode));
        mrTemp.setOrgCode(DmShared.xNull(txtOrgCode.getText()).trim());
        mrTemp.setFromDate(DmShared.dNull(txtFromDate.getText()));
        mrTemp.setToDate(DmShared.dNull(txtToDate.getText()));
        mrTemp.setScheduleCode(DmShared.xNull(txtHSchedule.getTag()));
        mrTemp.setActiveCode(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setActualValue(DmClient.vNull(lblActualValue.getText(), "0.00"));
        mrTemp.setPackageAmount(DmClient.vNull(lblPackageValue.getText(), "0.00"));

        mrTemp.getListService().clear();
        mrTemp.getListService().addAll(grdService.getGrid().getStore().getAll());
        mrTemp.getListInvestigation().clear();
        mrTemp.getListInvestigation().addAll(grdInvestigation.getGrid().getStore().getAll());
        mrTemp.getListMedicineItem().clear();
        mrTemp.getListMedicineItem().addAll(grdItem.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void saveRecord() {
        PackageDetail_Mdl mrTemp;

        try {
            mrTemp = getValue();
            PackageDetail_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        if (DmShared.iNull(roomCategoryCode).longValue() == 0) {
                            code = DmShared.xNull(result.getData());
                            getPackageDetail(code);
                        } else {
                            hide();
                        }
                    } else {
                        saveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            saveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void saveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtFromDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtToDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHActive).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHActive).show();
        }
    }

    private void deleteRecord(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    hide();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    private void copyPackageDetail(String code) {
        srvAsync.invokeRPC().getCopyPackage(code, new AsyncCallback<DmResult<PackageDetail_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PackageDetail_Mdl> result) {
                if (result.isStatus()) {
                    grdService.getGrid().getStore().clear();
                    grdService.getGrid().getStore().addAll(result.getData().getListService());

                    grdInvestigation.getGrid().getStore().clear();
                    grdInvestigation.getGrid().getStore().addAll(result.getData().getListInvestigation());

                    grdItem.getGrid().getStore().clear();
                    grdItem.getGrid().getStore().addAll(result.getData().getListMedicineItem());

                    calculateInvestigationAmt();
                    calculateItemAmt();
                    calculateServiceAmt();
                    if (isAddDelete) {
                        grdItem.getGrid().getStore().add(new GrdServiceMaster_Mdl());
                        grdInvestigation.getGrid().getStore().add(new GrdServiceMaster_Mdl());
                        grdService.getGrid().getStore().add(new GrdServiceMaster_Mdl());
                    }
                    ((CellSelectionModel) grdService.getGrid().getSelectionModel()).selectCell(0, 1);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void calculateServiceAmt() {
        double netAmount = 0, netPkgAmount = 0;

        for (GrdServiceMaster_Mdl node : grdService.getGrid().getStore().getAll()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                netAmount += DmShared.vNull(node.getAmount()).doubleValue();
                netPkgAmount += DmShared.vNull(node.getPkgAmount()).doubleValue();
            }
        }
        lblActualValue.setText(DmClient.vNull(netAmount, "0.00"));
        lblPackageValue.setText(DmClient.vNull(netPkgAmount, "0.00"));
        lblDifference.setText(DmClient.vNull(netAmount - netPkgAmount, "0.00"));
        txtPackageAmt.setText(DmClient.vNull(netPkgAmount, "0.00"));
    }

    private void calculateInvestigationAmt() {
        double netAmount = 0;

        for (GrdServiceMaster_Mdl node : grdInvestigation.getGrid().getStore().getAll()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                netAmount += DmShared.vNull(node.getAmount()).doubleValue();
            }
        }
        lblInvestigationAmt.setText(DmClient.vNull(netAmount, "0.00"));
    }

    private void calculateItemAmt() {
        double netAmount = 0;

        for (GrdServiceMaster_Mdl node : grdItem.getGrid().getStore().getAll()) {
            if (DmShared.iNull(node.getSharedKeyFieldCode()).longValue() != 0) {
                netAmount += DmShared.vNull(node.getAmount()).doubleValue();
            }
        }
        lblMedicineItemAmt.setText(DmClient.vNull(netAmount, "0.00"));
    }

    private void fillDefault() {
        if (DmShared.dNull(txtFromDate.getText()).equalsIgnoreCase("")) {
            if (DmShared.dNull(fromDate).equalsIgnoreCase("")) {
                txtFromDate.setText(DmClient.getStartDate());
            } else {
                txtFromDate.setText(fromDate);
            }
            if (DmShared.dNull(toDate).equalsIgnoreCase("")) {
                txtToDate.setText("31-Dec-9999");
            } else {
                txtToDate.setText(toDate);
            }
        }
        if (DmShared.xNull(txtHActive.getTag()).equalsIgnoreCase("")) {
            txtHActive.setTag("1");
            txtHActive.setText("Yes");
        }
    }

    private void clear() {
        txtPackageName.setTag("");
        txtPackageName.setText("");
        txtPackageShortName.setText("");
        txtPackageNature.setText("");
        txtOrganizationName.setTag("");
        txtOrganizationName.setText("");
        txtHSchedule.setTag("");
        txtHSchedule.setText("");
        txtHActive.setTag("");
        txtHActive.setText("");
        txtFromDate.setText("");
        txtToDate.setText("");
        grdService.getGrid().getStore().clear();
        grdItem.getGrid().getStore().clear();
        grdInvestigation.getGrid().getStore().clear();
    }
}
