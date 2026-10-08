package aarogya.inventory.itemGroupMaster.client;

import aarogya.inventory.itemGroupMaster.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

import aarogya.inventory.itemGroupMaster.shared.GrdRestrictedDept_Mdl;
import aarogya.inventory.itemGroupMaster.shared.GrdDepartmentConsumptionAc_Mdl;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public final class ItemGroupMaster extends ItemGroupMaster_Dsg implements SelectionHandler<Item> {

    ServiceInvoker<ItemGroupMaster_SrvAsync> SrvAsync;
    private final RestrictedDepartmentGridEdit<GrdRestrictedDept_Mdl, KeyFieldHashCode_T_PrpAcs> RestrictedDeptGrid = new RestrictedDepartmentGridEdit(gridRestrictedDept);
    private final DepartmentConcumptionAcGridEdit<GrdDepartmentConsumptionAc_Mdl, GridDepartmentConsumptionAc_PrpAcs> DeptConsumptionAcGrid = new DepartmentConcumptionAcGridEdit(gridDeptConsumptionAc);
    

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtitemGrp.setFocus(true);
                gridRestrictedDept.getGrid().getStore().add(new GrdRestrictedDept_Mdl());
                gridDeptConsumptionAc.getGrid().getStore().add(new GrdDepartmentConsumptionAc_Mdl());
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtitemGrp.setFocus(true);
                gridRestrictedDept.getGrid().getStore().add(new GrdRestrictedDept_Mdl());
                gridDeptConsumptionAc.getGrid().getStore().add(new GrdDepartmentConsumptionAc_Mdl());
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            topControlDelete(topControl.getDocId());
                        }
                    }
                }).show();
            }
        });

        topControl.addCardHandeller(new DmTopControl.eCard() {
            @Override
            public void cardClicked() {
                topControlCard();
            }
        });

        topControl.addFindHandeller(new DmTopControl.eFind() {
            @Override
            public void findClicked() {
                flwForm.remove(vrtInnerForm);
                flwForm.add(dmFind);
            }
        });

        topControl.addMoveRecHandeller(new DmTopControl.iMoveRec() {
            @Override
            public void moveRec(String strFetchCode) {
                topControlMoveRec(strFetchCode);
            }
        });

        topControl.addPrintHandeller(new DmTopControl.ePrint() {

            @Override
            public void printClicked() {

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemGroupMaster.this);
            }
        });

        topControl.addOtherHandeller(new DmTopControl.eOther() {

            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtitemGrp.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtitemGrp).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtitemGrp.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtitemGrp).show();
                        }
                    }
                });
            }
        });

        txtHGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHGroup_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGroup(helper, blnToEndOfFile, "", new AsyncCallback<DmResultPageing<List<TxtHGroup_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHGroup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHGroup_Mdl tRow) {
                txtHCategory.setText(tRow.getCategoryName());
                txtHCategory.setTag(tRow.getCategoryCode());
                txtHCategory.setEnabled(false);
            }
        });

        txtHCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHPurchaseAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("PR", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }

        });
        txtHPurchaseReturnAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("PR", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseReturnAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseReturnAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseReturnAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }

        });
        txtHSalesAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("SL", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalesAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSalesAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalesAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSalesReturnAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("SL", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalesReturnAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSalesReturnAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalesReturnAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHInventoryAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInventoryAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInventoryAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInventoryAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        //==========================================================================================================================
        txtHGrIrAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGrIrAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGrIrAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGrIrAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHConsumptionAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsumptionAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsumptionAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsumptionAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHCOGSAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCOGSAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCOGSAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCOGSAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPIDAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPIDAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPIDAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPIDAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHInTransitAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInTransitAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInTransitAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInTransitAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        //==========================================================================================================================
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Find_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);

                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    public ItemGroupMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ItemGroupMaster_Srv.class), ItemGroupMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        mnuExportCard.addSelectionHandler(this);
        textHelpEvents();
        gridEvents();
        findEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ItemGroupMaster_Mdl topControlSaveInitialize() {
        ItemGroupMaster_Mdl mrTemp = new ItemGroupMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtitemGrp.getText()));
        mrTemp.setGroupUnder(DmShared.xNull(txtHGroup.getTag()));
        mrTemp.setUnderCateCode(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setInventoryAcTag(DmShared.xNull(txtHInventoryAc.getTag()));
        mrTemp.setPurchaseAcTag(DmShared.xNull(txtHPurchaseAc.getTag()));
        mrTemp.setSalesAcTag(DmShared.xNull(txtHSalesAc.getTag()));
        mrTemp.setPurchaseReturnAcTag(DmShared.xNull(txtHPurchaseReturnAc.getTag()));
        mrTemp.setSalesReturnAcTag(DmShared.xNull(txtHSalesReturnAc.getTag()));

        mrTemp.setGrIrAcTag(DmShared.xNull(txtHGrIrAc.getTag()));
        mrTemp.setConsumptionAcTag(DmShared.xNull(txtHConsumptionAc.getTag()));
        mrTemp.setCogsAcTag(DmShared.xNull(txtHCOGSAc.getTag()));
        mrTemp.setPidAcTag(DmShared.xNull(txtHPIDAc.getTag()));
        mrTemp.setInTransitAcTag(DmShared.xNull(txtHInTransitAc.getTag()));

        List<GrdRestrictedDept_Mdl> lstGrdRestrictedDept = new ArrayList<>();
        lstGrdRestrictedDept.addAll(gridRestrictedDept.getGrid().getStore().getAll());
        mrTemp.getGrdRestrictedDept_Mdls().addAll(lstGrdRestrictedDept);
        
        List<GrdDepartmentConsumptionAc_Mdl> lstGrdDepartmentConsumptionAc_Mdl = new ArrayList<>();
        lstGrdDepartmentConsumptionAc_Mdl.addAll(gridDeptConsumptionAc.getGrid().getStore().getAll());
        mrTemp.getGrdDepartmentConsumptionAc().addAll(lstGrdDepartmentConsumptionAc_Mdl);
        
        return mrTemp;
    }

    private void topControlSave() {
        ItemGroupMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ItemGroupMaster_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtitemGrp).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());

                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {

        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtitemGrp).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHCategory).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtitemGrp).show();
                break;
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ItemGroupMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemGroupMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ItemGroupMaster_Mdl> result) {
        ItemGroupMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtHCategory.setTag(data.getUnderCateCode());
            txtHCategory.setText(data.getUnderCateName());

            txtHGroup.setTag(data.getGroupUnder());
            txtHGroup.setText(data.getGroupUnderName());

            txtHInventoryAc.setTag(DmShared.xNull(data.getInventoryAcTag()));
            txtHInventoryAc.setText(DmShared.xNull(data.getInventoryAcText()));
            txtHPurchaseAc.setTag(DmShared.xNull(data.getPurchaseAcTag()));
            txtHPurchaseReturnAc.setTag(DmShared.xNull(data.getPurchaseReturnAcTag()));
            txtHPurchaseAc.setText(DmShared.xNull(data.getPurchaseAcText()));
            txtHPurchaseReturnAc.setText(DmShared.xNull(data.getPurchaseReturnAcText()));
            txtHSalesAc.setTag(DmShared.xNull(data.getSalesAcTag()));
            txtHSalesReturnAc.setTag(DmShared.xNull(data.getPurchaseReturnAcTag()));
            txtHSalesAc.setText(DmShared.xNull(data.getSalesAcText()));
            txtHSalesReturnAc.setText(DmShared.xNull(data.getSalesReturnAcText()));

            txtHGrIrAc.setText(DmShared.xNull(data.getGrIrAcText()));
            txtHGrIrAc.setTag(DmShared.xNull(data.getGrIrAcTag()));
            txtHConsumptionAc.setText(DmShared.xNull(data.getConsumptionAcText()));
            txtHConsumptionAc.setTag(DmShared.xNull(data.getConsumptionAcTag()));
            txtHCOGSAc.setText(DmShared.xNull(data.getCogsAcText()));
            txtHCOGSAc.setTag(DmShared.xNull(data.getCogsAcTag()));
            txtHPIDAc.setText(DmShared.xNull(data.getPidAcText()));
            txtHPIDAc.setTag(DmShared.xNull(data.getPidAcTag()));
            txtHInTransitAc.setText(DmShared.xNull(data.getInTransitAcText()));
            txtHInTransitAc.setTag(DmShared.xNull(data.getInTransitAcTag()));

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            
            txtitemGrp.setText(data.getSharedKeyFieldName());
            
            gridRestrictedDept.getGrid().getStore().clear();
            gridRestrictedDept.getGrid().getStore().addAll(data.getGrdRestrictedDept_Mdls());
            
            gridDeptConsumptionAc.getGrid().getStore().clear();
            gridDeptConsumptionAc.getGrid().getStore().addAll(data.getGrdDepartmentConsumptionAc());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        
        gridRestrictedDept.getGrid().getStore().clear();
        gridDeptConsumptionAc.getGrid().getStore().clear();
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuExportCard)) {
            new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        exportCSV();
                    }
                }
            }).show();
        }
    }

    private void exportCSV() {
        SrvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                }
            }
        });
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void gridEvents() {
        //================RestrictedDepartmentGridEdit======================================================
        RestrictedDeptGrid.addCallBack(new RestrictedDepartmentGridEdit.CallBack() {
            @Override
            public void onOk(GrdRestrictedDept_Mdl gridRestrictedDept_Mdl) {
                gridRestrictedDept.commitChanges();
            }

            @Override
            public void onDelete() {
                gridRestrictedDept.commitChanges();
            }
        });

        gridRestrictedDept.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showRestrictedDeptGrid(event.getRowIndex());
            }
        });

        gridRestrictedDept.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showRestrictedDeptGrid(gridRestrictedDept.getGrid().getStore().indexOf(gridRestrictedDept.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        
        //================DepartmentWiseConsumptionAc======================================================
        DeptConsumptionAcGrid.addCallBack(new DepartmentConcumptionAcGridEdit.CallBack() {
            @Override
            public void onOk(GrdDepartmentConsumptionAc_Mdl grdDepartmentConsumptionAc_Mdl) {
                gridDeptConsumptionAc.commitChanges();
            }

            @Override
            public void onDelete() {
                gridDeptConsumptionAc.commitChanges();
            }
        });

        gridDeptConsumptionAc.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showDepartmentConsumptionAcGrid(event.getRowIndex());
            }
        });

        gridDeptConsumptionAc.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showDepartmentConsumptionAcGrid(gridDeptConsumptionAc.getGrid().getStore().indexOf(gridDeptConsumptionAc.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showRestrictedDeptGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            RestrictedDeptGrid.show(currentRowIndex, false, false, false);
        } else {
            RestrictedDeptGrid.show(currentRowIndex, true, true, true);
        }
    }
    
    private void showDepartmentConsumptionAcGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            DeptConsumptionAcGrid.show(currentRowIndex, false, false, false);
        } else {
            DeptConsumptionAcGrid.show(currentRowIndex, true, true, true);
        }
    }

}
