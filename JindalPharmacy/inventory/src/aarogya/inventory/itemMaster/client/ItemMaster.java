package aarogya.inventory.itemMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import java.util.ArrayList;
import java.util.HashMap;
import aarogya.inventory.itemMaster.shared.DefaultValue_Mdl;
import aarogya.inventory.itemMaster.shared.Find_Mdl;
import aarogya.inventory.itemMaster.shared.GrdAlternativeItems_Mdl;
import aarogya.inventory.itemMaster.shared.GrdApprovedVendors_Mdl;
import aarogya.inventory.itemMaster.shared.GrdItemDepartmentInfo_Mdl;
import aarogya.inventory.itemMaster.shared.GrdQcParameter_Mdl;
import aarogya.inventory.itemMaster.shared.GrdItsRateSlab_Mdl;
import aarogya.inventory.itemMaster.shared.GrdRestrictedDept_Mdl;
import aarogya.inventory.itemMaster.shared.GrdSoundLookAlike_Mdl;
import aarogya.inventory.itemMaster.shared.ItemMaster_Mdl;
import aarogya.inventory.itemMaster.shared.ItemMaster_Srv;
import aarogya.inventory.itemMaster.shared.ItemMaster_SrvAsync;
import aarogya.inventory.itemMaster.shared.ItemMaster_Vld;
import aarogya.inventory.itemMaster.shared.Sku_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHCategory_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHHSN_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemPrperty_Mdl;
import aarogya.inventory.itemMaster.shared.TxtHItemType_Mdl;
import aarogya.selfToolkit.itemPropertyPopup.shared.ItemProperty_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.ItemStatus;
import aarogya.inventory.itemMaster.shared.*;

public final class ItemMaster extends ItemMaster_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, SelectionHandler<Item> {

    private final ServiceInvoker<ItemMaster_SrvAsync> srvAsync;
    private final ManageSite manageSite = new ManageSite();
    private final ItemDetailsGridEdit<GrdItemDepartmentInfo_Mdl, KeyFieldHashCode_T_PrpAcs> itemGridEdit = new ItemDetailsGridEdit(grdItemDetails);
    private final ApprovedVendorsGridEdit<GrdApprovedVendors_Mdl, KeyFieldHashCode_T_PrpAcs> ApprovedVendorsGrid = new ApprovedVendorsGridEdit(gridApprovedVendors);
    private final QcParameterGridEdit<GrdQcParameter_Mdl, KeyFieldHashCode_T_PrpAcs> QcParameterGrid = new QcParameterGridEdit(gridQcParameter);
    private final AlternativeItemsGridEdit<GrdAlternativeItems_Mdl, KeyFieldHashCode_T_PrpAcs> AlternativeItemsGrid = new AlternativeItemsGridEdit(gridAlternativeItems);
    private final RestrictedDepartmentGridEdit<GrdRestrictedDept_Mdl, KeyFieldHashCode_T_PrpAcs> RestrictedDeptGrid = new RestrictedDepartmentGridEdit(gridRestrictedDept);
    private final SoundLookAlikeGridEdit<GrdSoundLookAlike_Mdl, KeyFieldHashCode_T_PrpAcs> SoundLookAlikeGrid = new SoundLookAlikeGridEdit(gridSoundLookAlike);
    private final ItsRateSlabGridEdit<GrdItsRateSlab_Mdl, GridItsRateSlab_PrpAcs> itsRateSlabGridEdit = new ItsRateSlabGridEdit(grdItsRateSlab);
    String isItemUsed = "", previousSelectedSKU = "", prvSaleUnit = "", prvSkuTag = "", prvPurchaseUnit = "", prvConversionRatePurchase = "", prvConversionRateSale = "", wtaCode = "";
    boolean flagFillSkuValue = false, flag_Name_Editable = false;
    protected DefaultValue_Mdl defaultValue;
    private boolean isNegativeStock = false, isActive = false, isEditAccounts = false, isInventoryAccountingActive = false;
    WalkThrough walkThrough;
    private DefaultValue_Mdl dvm;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(true);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(statusChangedByBlog)) {
                isActive = true;
            }
        }

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                tabPanel.selectTab(0);
//                txtHItemType.setFocus(true);
                if (!flag_Name_Editable) {
                    txtHMaterialCategory.setFocus(true);
                } else {
                    txtName.setFocus(true);
                }

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHMaterialCategory.setFocus(true);
                txtManualCode.setEnabled(true);
                manageDisplay(true);

                grdItemDetails.getGrid().getStore().add(new GrdItemDepartmentInfo_Mdl());
                gridApprovedVendors.getGrid().getStore().add(new GrdApprovedVendors_Mdl());
                gridQcParameter.getGrid().getStore().add(new GrdQcParameter_Mdl());
                gridAlternativeItems.getGrid().getStore().add(new GrdAlternativeItems_Mdl());
                gridRestrictedDept.getGrid().getStore().add(new GrdRestrictedDept_Mdl());
                gridSoundLookAlike.getGrid().getStore().add(new GrdSoundLookAlike_Mdl());
                manageITSContainer();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemMaster.this);
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

        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
            }
        });

        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                if (event.getSelectedItem() == tabIndexAisala) {
                    ((CellSelectionModel) gridAlternativeItems.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        });

        txtName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                        }
                    }
                });
            }
        });

        txtHCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCategory_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCategory_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCategory_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCategory_Mdl tRow) {
                if (blnStatus && tRow != null) {
                } else {
                }
            }
        });

        txtHMaterialCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMaterialCategory_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMaterialCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMaterialCategory_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMaterialCategory_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMaterialCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMaterialCategory_Mdl tRow) {
                if (blnStatus && tRow != null) {
//                    txtName.setText(txtHMaterialCategory.getText().trim() + "-" + txtHMaterialType.getText().trim() + "-" + txtHItemMake.getText().trim()
//                            + "-" + txtModel.getText().trim() + "-" + txtStrength.getText().trim());
                    setNameText();
                }
            }
        });

        txtHMaterialType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMaterialType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMaterialType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMaterialType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMaterialType).show();
                        }
//                        txtName.setText(txtHMaterialCategory.getText().trim() + "-" + txtHMaterialType.getText().trim() + "-" + txtHItemMake.getText().trim()
//                                + "-" + txtModel.getText().trim() + "-" + txtStrength.getText().trim());
                        setNameText();
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                setNameText();
            }
        });

        txtHItemModel.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemModel(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemModel).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemModel.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemModel).show();
                        }
                        setNameText();
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                setNameText();
            }
        });
        txtHItemStrength.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemStrength(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemStrength).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemStrength.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemStrength).show();
                        }
                        setNameText();
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                setNameText();
            }
        });

        txtHHSN.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHHSN_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHHSN(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHHSN_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHSN).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHHSN_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHHSN.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHSN).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHHSN_Mdl tRow) {
                if (blnStatus && tRow != null) {

                }
            }
        });

        txtHCostCenter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCostCenter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCostCenter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCostCenter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHBudgetHead.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBudgerHead(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBudgetHead).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBudgetHead.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBudgetHead).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHItemGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHGroup_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHGroup_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHGroup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHGroup_Mdl t1Row) {
                manageTxtHBestBefore();
                txtHCategory.setText(t1Row.getCategoryName());
                txtHCategory.setTag(t1Row.getCategoryCode());
                txtHCategory.setEnabled(false);
                txtHInventoryAc.setTag(DmShared.xNull(t1Row.getInventoryAcTag()));
                txtHInventoryAc.setText(DmShared.xNull(t1Row.getInventoryAcText()));
                txtHPurchaseAc.setTag(DmShared.xNull(t1Row.getPurchaseAcTag()));
                txtHPurchaseReturnAc.setTag(DmShared.xNull(t1Row.getPurchaseReturnAcTag()));
                txtHPurchaseAc.setText(DmShared.xNull(t1Row.getPurchaseAcText()));
                txtHPurchaseReturnAc.setText(DmShared.xNull(t1Row.getPurchaseReturnAcText()));
                txtHSalesAc.setTag(DmShared.xNull(t1Row.getSalesAcTag()));
                txtHSalesReturnAc.setTag(DmShared.xNull(t1Row.getPurchaseReturnAcTag()));
                txtHSalesAc.setText(DmShared.xNull(t1Row.getSalesAcText()));
                txtHSalesReturnAc.setText(DmShared.xNull(t1Row.getSalesReturnAcText()));
                txtHGRIRAc.setTag(DmShared.xNull(t1Row.getGrIrAcTag()));
                txtHGRIRAc.setText(DmShared.xNull(t1Row.getGrIrAcText()));
                txtHConsumptionAc.setTag(DmShared.xNull(t1Row.getConsumptionAcTag()));
                txtHConsumptionAc.setText(DmShared.xNull(t1Row.getConsumptionAcText()));
                txtHCOGSAc.setTag(DmShared.xNull(t1Row.getCogsAcTag()));
                txtHCOGSAc.setText(DmShared.xNull(t1Row.getCogsAcText()));
                txtHPIDAc.setTag(DmShared.xNull(t1Row.getPidAcTag()));
                txtHPIDAc.setText(DmShared.xNull(t1Row.getPidAcText()));
                txtHInTransitAc.setTag(DmShared.xNull(t1Row.getInTransitAcTag()));
                txtHInTransitAc.setText(DmShared.xNull(t1Row.getInTransitAcText()));

                txtHCostCenter.setTag(DmShared.xNull(t1Row.getCostCenterTag()));
                txtHCostCenter.setText(DmShared.xNull(t1Row.getCostCenterText()));
                txtHBudgetHead.setTag(DmShared.xNull(t1Row.getBudgetHeadTag()));
                txtHBudgetHead.setText(DmShared.xNull(t1Row.getBudgetHeadText()));

                srvAsync.invokeRPC(true).getGroupRestrictedDepartment(t1Row.getSharedKeyFieldCode(), new AsyncCallback<DmResult<List<GrdRestrictedDept_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResult<List<GrdRestrictedDept_Mdl>> result) {
                        gridRestrictedDept.getGrid().getStore().clear();
                        gridRestrictedDept.getGrid().getStore().addAll(result.getData());
                    }
                });

            }
        });

        txtHSKU.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Sku_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSKUDetails(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Sku_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSKU).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Sku_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSKU.setData(result.getData(), result.getPaging());

                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSKU).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Sku_Mdl tRow) {
                if (blnStatus && tRow != null) {
//                    setUnitHeaderValue(tRow.getSharedKeyFieldName());
                    fillSkuHelps(tRow);
                    lblPurchaseConvertionRate.setText(tRow.getSharedKeyFieldName());
                    lblSaleConvertionRate.setText(tRow.getSharedKeyFieldName());
                    txtConversionRatePurchase.setTag2nd(tRow.getAllowedDecimalPlaces());
                    txtConversionRateSale.setTag2nd(tRow.getAllowedDecimalPlaces());
                    lblPackSize.setText(tRow.getSharedKeyFieldName());
                    txtPackSize.setTag2nd(DmShared.xNull(tRow.getAllowedDecimalPlaces()));

                    if (!DmShared.xNull(txtHPurchaseUnitSKU.getTag()).equalsIgnoreCase("")) {
                        if (DmShared.iNull(tRow.getSharedKeyFieldCode()).longValue() != DmShared.iNull(txtHPurchaseUnitSKU.getTag()).longValue()) {
                            txtConversionRatePurchase.setEnabled(true);
                            txtConversionRatePurchase.setReadOnly(false);
                        } else {
                            txtConversionRatePurchase.setText("1");
                            txtConversionRatePurchase.setEnabled(false);
                            txtConversionRatePurchase.setReadOnly(true);
                        }
                    }

                    if (!DmShared.xNull(txtHSaleUnitSKU.getTag()).equalsIgnoreCase("")) {
                        if (DmShared.iNull(tRow.getSharedKeyFieldCode()).longValue() != DmShared.iNull(txtHSaleUnitSKU.getTag()).longValue()) {
                            txtConversionRateSale.setEnabled(true);
                            txtConversionRateSale.setReadOnly(false);
                        } else {
                            txtConversionRateSale.setText("1");
                            txtConversionRateSale.setEnabled(false);
                            txtConversionRateSale.setReadOnly(true);
                        }
                    }
                    previousSelectedSKU = DmShared.xNull(tRow.getSharedKeyFieldCode());
                } else {
//                    setUnitHeaderValue("");
                    lblPurchaseConvertionRate.setText("");
                    lblSaleConvertionRate.setText("");
                }
            }
        });

        txtHItemType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemType_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemType_Mdl t1Row) {
            }
        });

        txtHBestBefore.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBestBefore).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBestBefore.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBestBefore).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHHighValue.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHighValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHighValue.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHighValue).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHFlowThrough.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFlowThrough).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFlowThrough.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFlowThrough).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHConsignmentItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsignmentItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsignmentItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsignmentItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHReturnable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReturnable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReturnable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReturnable).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHBilledItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBilledItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBilledItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBilledItem).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPurchaseUnitSKU.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (!DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getSKU("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHPurchaseUnitSKU).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                            if (result.isStatus()) {
                                txtHPurchaseUnitSKU.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseUnitSKU).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    if (tRow.getSharedKeyFieldCode().equalsIgnoreCase(DmShared.xNull(txtHSKU.getTag()))) {
                        txtConversionRatePurchase.setText("1");
                        txtConversionRatePurchase.setEnabled(false);
                        txtConversionRatePurchase.setReadOnly(true);
                    } else {
                        txtConversionRatePurchase.setText("");
                        txtConversionRatePurchase.setEnabled(true);
                        txtConversionRatePurchase.setReadOnly(false);
                    }
                }
            }
        });

        txtHSaleUnitSKU.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (!DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getSKU("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHSaleUnitSKU).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                            if (result.isStatus()) {
                                txtHSaleUnitSKU.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHSaleUnitSKU).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    if (tRow.getSharedKeyFieldCode().equalsIgnoreCase(DmShared.xNull(txtHSKU.getTag()))) {
                        txtConversionRateSale.setText("1");
                        txtConversionRateSale.setEnabled(false);
                        txtConversionRateSale.setReadOnly(true);
                    } else {
                        txtConversionRateSale.setText("");
                        txtConversionRateSale.setEnabled(true);
                        txtConversionRateSale.setReadOnly(false);
                    }
                }
            }
        });

        txtHProperties.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<TxtHItemPrperty_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHProperty(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemPrperty_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHProperties).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemPrperty_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHProperties.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHProperties).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
                manageTxtHBestBefore();
            }
        });
        txtHAllowNegetiveStock.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAllowNegetiveStock).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAllowNegetiveStock.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAllowNegetiveStock).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPoReq.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPoReq).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPoReq.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPoReq).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIndentReq.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndentReq).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndentReq.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndentReq).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHQuotationReq.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQuotationReq).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHQuotationReq.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQuotationReq).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHLabReq.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHLabRequired(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLabReq).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLabReq.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLabReq).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHItemGSTNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGSTNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemGSTNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemGSTNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemGSTNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHITSBasedOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHITSBasedOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSBasedOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSBasedOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSBasedOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHITSBasedOn.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHITSBasedOn.getTag()))) {
                    txtHITSBasedOn.setTag2nd(txtHITSBasedOn.getTag());
                    grdItsRateSlab.getGrid().getStore().clear();
                    txtHGstType.setText("");
                    txtHGstType.setTag("");
                    manageITSContainer();
                }
                if (blnStatus & t1Row != null) {
                }
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
                if (blnStatus & t1Row != null) {

                }
            }
        });

        txtHInventoryAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
        txtHPurchaseAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("PR", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("PR", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("SL", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("SL", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        txtHGRIRAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGRIRAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGRIRAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGRIRAc).show();
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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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

        txtHItemMake.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHItemMake_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemMake(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHItemMake_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemMake).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHItemMake_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHItemMake.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemMake).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHItemMake_Mdl t1Row) {
                if (blnStatus && t1Row != null) {

                    // hsnDetail = t1Row;
                    txtHItemMake.setTag2nd(t1Row.getSharedKeyFieldName());
//                    txtName.setText(txtHItemGroup.getText().trim() + " " + txtHItemMake.getText().trim() + " " + txtModel.getText().trim() + " " + txtStrength.getText().trim());
                    setNameText();
                }
            }

        });

        txtHMarketingCo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMarketingCo_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMarketingCo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMarketingCo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMarketingCo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMarketingCo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMarketingCo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMarketingCo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMarketingCo_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    // hsnDetail = t1Row;
                    txtHMarketingCo.setTag2nd(t1Row.getSharedKeyFieldName());

                }
            }

        });

        txtHItemSpeciality.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHItemSpeciality(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHItemSpeciality).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHItemSpeciality.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHItemSpeciality).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHManufacturer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHManufacturer_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHManufacturer(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHManufacturer_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHManufacturer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHManufacturer_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHManufacturer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHManufacturer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHManufacturer_Mdl t1Row) {
            }

        });

        txtHDivision.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHDivision_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDivision(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHDivision_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDivision).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHDivision_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHDivision.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDivision).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHDivision_Mdl t1Row) {
            }
        });
        txtHNarcotics.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNarcotics).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNarcotics.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNarcotics).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });

        txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, true, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

    }

    private void gridEvents() {

        itemGridEdit.addCallBack(new ItemDetailsGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemDepartmentInfo_Mdl gridItem_Mdl) {
                grdItemDetails.commitChanges();
            }

            @Override
            public void onDelete() {
                grdItemDetails.commitChanges();
            }

        });
        grdItemDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showItemGridEdit(event.getRowIndex());
            }
        });
        grdItemDetails.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemGridEdit(grdItemDetails.getGrid().getStore().indexOf(grdItemDetails.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        //=========================================================ApprovedVendorsGridEdit===============
        ApprovedVendorsGrid.addCallBack(new ApprovedVendorsGridEdit.CallBack() {

            @Override
            public void onOk(GrdApprovedVendors_Mdl gridVendor_Mdl) {
                gridApprovedVendors.commitChanges();
            }

            @Override
            public void onDelete() {
                gridApprovedVendors.commitChanges();
            }

        });

        gridApprovedVendors.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showApprovedVendorsGrid(event.getRowIndex());
            }
        });

        gridApprovedVendors.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showApprovedVendorsGrid(gridApprovedVendors.getGrid().getStore().indexOf(gridApprovedVendors.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        //================ QcParameterGridEdit ===============
        QcParameterGrid.addCallBack(new QcParameterGridEdit.CallBack() {

            @Override
            public void onOk(GrdQcParameter_Mdl grdQcParameterMdl) {
                gridQcParameter.commitChanges();
            }

            @Override
            public void onDelete() {
                gridQcParameter.commitChanges();
            }

        });

        gridQcParameter.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showQcParameterGrid(event.getRowIndex());
            }
        });

        gridQcParameter.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showQcParameterGrid(gridQcParameter.getGrid().getStore().indexOf(gridQcParameter.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        //=======================================AlternativeItemsGridEdit=================================
        AlternativeItemsGrid.addCallBack(new AlternativeItemsGridEdit.CallBack() {

            @Override
            public void onOk(GrdAlternativeItems_Mdl gridAlternativeItems_Mdl) {
                gridAlternativeItems.commitChanges();
            }

            @Override
            public void onDelete() {
                gridAlternativeItems.commitChanges();
            }

        });

        gridAlternativeItems.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showAlternativeItemsGrid(event.getRowIndex());
            }
        });

        gridAlternativeItems.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showAlternativeItemsGrid(gridAlternativeItems.getGrid().getStore().indexOf(gridAlternativeItems.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
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
        // =============================SoundLookAlikeGridEdit=================================================
        SoundLookAlikeGrid.addCallBack(new SoundLookAlikeGridEdit.CallBack() {

            @Override
            public void onOk(GrdSoundLookAlike_Mdl gridSoundLookAlike_Mdl) {
                gridSoundLookAlike.commitChanges();
            }

            @Override
            public void onDelete() {
                gridSoundLookAlike.commitChanges();
            }

        });

        gridSoundLookAlike.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showSoundLookAlikeGrid(event.getRowIndex());
            }
        });

        gridSoundLookAlike.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showSoundLookAlikeGrid(gridSoundLookAlike.getGrid().getStore().indexOf(gridSoundLookAlike.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        //========================================================ItsRateSlabGridEdit===========================
        itsRateSlabGridEdit.addCallBack(new ItsRateSlabGridEdit.CallBack() {
            @Override
            public void onOk(GrdItsRateSlab_Mdl gridRate_Mdl) {
                grdItsRateSlab.commitChanges();
            }

            @Override
            public void onDelete() {
                grdItsRateSlab.commitChanges();
            }
        });
        grdItsRateSlab.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showRateGrd(event.getRowIndex());
            }
        });
        grdItsRateSlab.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showRateGrd(grdItsRateSlab.getGrid().getStore().indexOf(grdItsRateSlab.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        //***********************************************************************************************

    }

    private void showItemGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            itemGridEdit.show(currentRowIndex, false, false, false);
        } else {
            itemGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    private void showApprovedVendorsGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            ApprovedVendorsGrid.show(currentRowIndex, false, false, false, this);
        } else {
            ApprovedVendorsGrid.show(currentRowIndex, true, true, true, this);
        }
    }

    private void showQcParameterGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            QcParameterGrid.show(currentRowIndex, false, false, false);
        } else {
            QcParameterGrid.show(currentRowIndex, true, true, true);
        }
    }

    private void showAlternativeItemsGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            AlternativeItemsGrid.show(currentRowIndex, false, false, false, this);
        } else {
            AlternativeItemsGrid.show(currentRowIndex, true, true, true, this);
        }
    }

    private void showSoundLookAlikeGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            SoundLookAlikeGrid.show(currentRowIndex, false, false, false, this);
        } else {
            SoundLookAlikeGrid.show(currentRowIndex, true, true, true, this);
        }
    }

    private void showRestrictedDeptGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            RestrictedDeptGrid.show(currentRowIndex, false, false, false);
        } else {
            RestrictedDeptGrid.show(currentRowIndex, true, true, true);
        }
    }

    private void showRateGrd(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            itsRateSlabGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            itsRateSlabGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {

        } else {

            manageSite.hide();
//            applicableBatchPopup.hide();
        }
    }

    public ItemMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(ItemMaster_Srv.class), ItemMaster.class.getName());
        walkThrough = new WalkThrough(srvAsync);
        this.asWidget().setTitle(widgetName);
        txtMaxLvl.addBlurHandler(this);
        mnuManageSite.addSelectionHandler(this);
        mnuExportCard.addSelectionHandler(this);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        iniDefaultValue(swi);
        manageDisplay(false);
        textHelpEvents();
        findEvents();
        gridEvents();
        handlerEvents();
        swi.setWidget(ItemMaster.this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ItemMaster_Mdl topControlSaveInitialize() {
        ItemMaster_Mdl mrTemp = new ItemMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        calculate();
        mrTemp.setManualCode(DmShared.xNull(txtManualCode.getText()));
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setWtaCode(DmShared.iNull(wtaCode).toString());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setCategoryText(DmShared.xNull(txtHCategory.getText()));
        mrTemp.setCategoryTag(DmShared.xNull(txtHCategory.getTag()));

        mrTemp.setMaterialCategoryTag(DmShared.xNull(txtHMaterialCategory.getTag()));
        mrTemp.setMaterialTypeTag(DmShared.xNull(txtHMaterialType.getTag()));

        mrTemp.setMedGroupText(DmShared.xNull(txtHItemGroup.getText()));
        mrTemp.setMedGroupTag(DmShared.xNull(txtHItemGroup.getTag()));
        mrTemp.setSkuTag(DmShared.xNull(txtHSKU.getTag()));
        mrTemp.setPrvSkuTag(DmShared.xNull(prvSkuTag));
        mrTemp.setItemTypeTag(DmShared.xNull(txtHItemType.getTag()));
        mrTemp.setItemTypeText(DmShared.xNull(txtHItemType.getText()));

        mrTemp.setHsnCode(DmShared.xNull(txtHHSN.getTag()));

        mrTemp.setPoReqTag(DmShared.xNull(txtHPoReq.getTag()));
        mrTemp.setIndentReqTag(DmShared.xNull(txtHIndentReq.getTag()));
        mrTemp.setQuotationReqTag(DmShared.xNull(txtHQuotationReq.getTag()));

        mrTemp.setBestBeforeTag(DmShared.xNull(txtHBestBefore.getTag()));
        mrTemp.setHighValueTag(DmShared.xNull(txtHHighValue.getTag()));
        mrTemp.setConsignmentTag(DmShared.xNull(txtHConsignmentItem.getTag()));
        mrTemp.setReturnableItemTag(DmShared.xNull(txtHReturnable.getTag()));
        mrTemp.setBilledItemTag(DmShared.xNull(txtHBilledItem.getTag()));
        mrTemp.setFlowThroughTag(DmShared.xNull(txtHFlowThrough.getTag()));
        mrTemp.setGstNatureTag(DmShared.xNull(txtHItemGSTNature.getTag()));
        mrTemp.setItsBasedOnTag(DmShared.xNull(txtHITSBasedOn.getTag()));

        mrTemp.setPropertyCode(DmShared.xNull(txtHProperties.getTag()));
        mrTemp.setPropertyName(DmShared.xNull(txtHProperties.getText()));

        mrTemp.setTolerancePlus(DmShared.xNull(txtTolerancePlus.getText()));
        mrTemp.setToleranceMinus(DmShared.xNull(txtToleranceMinus.getText()));

        mrTemp.setCostCenterTag(DmShared.xNull(txtHCostCenter.getTag()));
        mrTemp.setBudgetHeadTag(DmShared.xNull(txtHBudgetHead.getTag()));

        List<GrdItemDepartmentInfo_Mdl> OBJListItem = new ArrayList<>();
        OBJListItem.addAll(grdItemDetails.getGrid().getStore().getAll());
        mrTemp.getGrdItem_Mdl().addAll(OBJListItem);

        List<GrdApprovedVendors_Mdl> lstApprovedVendors = new ArrayList<>();
        lstApprovedVendors.addAll(gridApprovedVendors.getGrid().getStore().getAll());
        mrTemp.getGrdApprovedVendors_Mdls().addAll(lstApprovedVendors);

        List<GrdQcParameter_Mdl> lstQcParameters = new ArrayList<>();
        lstQcParameters.addAll(gridQcParameter.getGrid().getStore().getAll());
        mrTemp.getGrdQcParameter_Mdls().addAll(lstQcParameters);

        List<GrdAlternativeItems_Mdl> lstAlternativeItems = new ArrayList<>();
        lstAlternativeItems.addAll(gridAlternativeItems.getGrid().getStore().getAll());
        mrTemp.getGrdAlternativeItems_Mdls().addAll(lstAlternativeItems);

        List<GrdRestrictedDept_Mdl> lstGrdRestrictedDept = new ArrayList<>();
        lstGrdRestrictedDept.addAll(gridRestrictedDept.getGrid().getStore().getAll());
        mrTemp.getGrdRestrictedDept_Mdls().addAll(lstGrdRestrictedDept);

        List<GrdSoundLookAlike_Mdl> lstGrdSoundLookAlike = new ArrayList<>();
        lstGrdSoundLookAlike.addAll(gridSoundLookAlike.getGrid().getStore().getAll());
        mrTemp.getGrdSoundLookAlike_Mdls().addAll(lstGrdSoundLookAlike);

        List<GrdItsRateSlab_Mdl> lstGrdRateIts = new ArrayList<>();
        if (DmShared.xNull(txtHITSBasedOn.getText()).equalsIgnoreCase("Fixed")
                || DmShared.xNull(txtHITSBasedOn.getText()).equalsIgnoreCase("")) {
            GrdItsRateSlab_Mdl grdRateITS_Mdl = new GrdItsRateSlab_Mdl();
            grdRateITS_Mdl.setGstTypeCode(DmShared.xNull(txtHGstType.getTag()));
            lstGrdRateIts.add(grdRateITS_Mdl);
        } else {
            lstGrdRateIts.addAll(grdItsRateSlab.getGrid().getStore().getAll());
        }
        mrTemp.getGrdRateIts_Mdls().addAll(lstGrdRateIts);

        mrTemp.setPrvPurchaseUnitSkuCode(DmShared.xNull(prvPurchaseUnit));
        mrTemp.setPurchaseUnitSkuCode(DmShared.xNull(txtHPurchaseUnitSKU.getTag()));
        mrTemp.setPurchaseUnitSkuName(DmShared.xNull(txtHPurchaseUnitSKU.getText()));
        mrTemp.setPrvSaleUnitSkuCode(DmShared.xNull(prvSaleUnit));
        mrTemp.setSaleUnitSkuCode(DmShared.xNull(txtHSaleUnitSKU.getTag()));
        mrTemp.setSaleUnitSkuName(DmShared.xNull(txtHSaleUnitSKU.getText()));

        mrTemp.setConversionRatePurchase(DmShared.xNull(txtConversionRatePurchase.getText()));
        mrTemp.setConversionRateSale(DmShared.xNull(txtConversionRateSale.getText()));
        mrTemp.setPrvConversionRatePurchase(DmShared.xNull(prvConversionRatePurchase));
        mrTemp.setPrvConversionRateSale(DmShared.xNull(prvConversionRateSale));
        mrTemp.setPackSize(DmShared.xNull(txtPackSize.getText()));
        mrTemp.setAllowNegetiveStockCode(DmShared.xNull(txtHAllowNegetiveStock.getTag()));
        mrTemp.setAllowNegetiveStockName(DmShared.xNull(txtHAllowNegetiveStock.getText()));
        mrTemp.setTolerancePlus(DmShared.xNull(txtTolerancePlus.getText()));
        mrTemp.setToleranceMinus(DmShared.xNull(txtToleranceMinus.getText()));
        mrTemp.setLabRequiredTag(DmShared.xNull(txtHLabReq.getTag()));
        mrTemp.setLabRequiredText(DmShared.xNull(txtHLabReq.getText()));

        mrTemp.setInventoryAcTag(DmShared.xNull(txtHInventoryAc.getTag()));
        mrTemp.setInventoryAcText(DmShared.xNull(txtHInventoryAc.getText()));
        mrTemp.setPurchaseAcTag(DmShared.xNull(txtHPurchaseAc.getTag()));
        mrTemp.setPurchaseAcText(DmShared.xNull(txtHPurchaseAc.getText()));
        mrTemp.setSalesAcTag(DmShared.xNull(txtHSalesAc.getTag()));
        mrTemp.setSalesAcText(DmShared.xNull(txtHSalesAc.getText()));
        mrTemp.setPurchaseReturnAcTag(DmShared.xNull(txtHPurchaseReturnAc.getTag()));
        mrTemp.setSalesReturnAcTag(DmShared.xNull(txtHSalesReturnAc.getTag()));
        mrTemp.setGrIrAcTag(DmShared.xNull(txtHGRIRAc.getTag()));
        mrTemp.setConsumptionAcTag(DmShared.xNull(txtHConsumptionAc.getTag()));
        mrTemp.setCogsAcTag(DmShared.xNull(txtHCOGSAc.getTag()));
        mrTemp.setPidAcTag(DmShared.xNull(txtHPIDAc.getTag()));
        mrTemp.setInTransitAcTag(DmShared.xNull(txtHInTransitAc.getTag()));

        mrTemp.setItemSpecialityTag(DmShared.xNull(txtHItemSpeciality.getTag()));
        mrTemp.setManufacturerTag(DmShared.xNull(txtHManufacturer.getTag()));
        mrTemp.setDivisionTag(DmShared.xNull(txtHDivision.getTag()));
        mrTemp.setItemMakeTag(DmShared.xNull(txtHItemMake.getTag()));
        mrTemp.setModel(DmShared.xNull(txtHItemModel.getTag()));
        mrTemp.setStrength(DmShared.xNull(txtHItemStrength.getTag()));
        mrTemp.setMarketingCompanyCode(DmShared.xNull(txtHMarketingCo.getTag()));
        mrTemp.setNarcoticsTag(DmShared.xNull(txtHNarcotics.getTag()));

        return mrTemp;

    }

    private void topControlSave() {
        ItemMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ItemMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, isActive, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
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
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHItemType).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHItemGroup).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHCategory).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHSKU).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHConsignmentItem).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHBestBefore).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtHFlowThrough).show();
                break;
            case 2000:
                new DmAlertMessageBox(errorMessage, txtHHighValue).show();
                break;
            case 3000:
                new DmAlertMessageBox(errorMessage, txtHReturnable).show();
                break;
            case 4000:
                new DmAlertMessageBox(errorMessage, txtHBilledItem).show();
                break;
            case 6000:
                new DmAlertMessageBox(errorMessage, txtHSKU).show();
                break;
            case 7000:
                new DmAlertMessageBox(errorMessage, txtHPurchaseUnitSKU).show();
                break;
            case 8000:
                new DmAlertMessageBox(errorMessage, txtHSaleUnitSKU).show();
                break;
            case 9000:
                new DmAlertMessageBox(errorMessage, txtConversionRatePurchase).show();
                break;
            case 8500:
                new DmAlertMessageBox(errorMessage, txtConversionRatePurchase).show();
                break;
            case 9500:
                new DmAlertMessageBox(errorMessage, txtConversionRateSale).show();
                break;
            case 10000:
                new DmAlertMessageBox(errorMessage, txtConversionRateSale).show();
                break;
            case 11000:
                new DmAlertMessageBox(errorMessage, txtConversionRatePurchase).show();
                break;
            case 12000:
                new DmAlertMessageBox(errorMessage, txtConversionRatePurchase).show();
                break;
            case 13000:
                new DmAlertMessageBox(errorMessage, txtConversionRateSale).show();
                break;
            case 14000:
                new DmAlertMessageBox(errorMessage, txtPackSize).show();
                break;
            case 15000:
                new DmAlertMessageBox(errorMessage, txtHAllowNegetiveStock).show();
                break;
            case 16000:
                new DmAlertMessageBox(errorMessage, txtHPoReq).show();
                break;
            case 16500:
                new DmAlertMessageBox(errorMessage, txtHLabReq).show();
                break;
            case 17000:
                new DmAlertMessageBox(errorMessage, txtHIndentReq).show();
                break;
            case 18000:
                new DmAlertMessageBox(errorMessage, txtHQuotationReq).show();
                break;
            case 19000:
                new DmAlertMessageBox(errorMessage, txtHITSBasedOn).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtName).show();
                break;
        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ItemMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ItemMaster_Mdl> result) {
        ItemMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            userControl.setTextBlog(statusChangedByBlog, data.getStatusChangedBy());

            txtHProperties.setTag(data.getPropertyCode());
            txtHProperties.setText(data.getPropertyName());
            txtHAllowNegetiveStock.setText(DmShared.xNull(data.getAllowNegetiveStockName()));
            txtHAllowNegetiveStock.setTag(DmShared.xNull(data.getAllowNegetiveStockCode()));
            isNegativeStock = data.getIsNegativeStock();

            txtManualCode.setText(data.getManualCode());
            wtaCode = DmShared.xNull(data.getWtaCode());
            txtHActive.setTag(data.getActiveTag());
            txtHActive.setText(data.getActiveText());
            txtHCategory.setTag(data.getCategoryTag());
            txtHCategory.setText(data.getCategoryText());

            txtHMaterialCategory.setTag(data.getMaterialCategoryTag());
            txtHMaterialCategory.setText(data.getMaterialCategoryText());
            txtHMaterialType.setTag(data.getMaterialTypeTag());
            txtHMaterialType.setText(data.getMaterialTypeText());

            txtHItemGroup.setTag(data.getMedGroupTag());
            txtHItemGroup.setText(data.getMedGroupText());
            prvSkuTag = DmShared.xNull(data.getPrvSkuTag());
            previousSelectedSKU = DmShared.xNull(data.getSkuTag());
            txtHSKU.setTag(data.getSkuTag());
            txtHSKU.setText(data.getSkuText());
            txtName.setText(data.getSharedKeyFieldName());
            txtHItemType.setText(data.getItemTypeText());
            txtHItemType.setTag(data.getItemTypeTag());

            txtHPoReq.setText(data.getPoReqText());
            txtHPoReq.setTag(data.getPoReqTag());
            txtHIndentReq.setText(data.getIndentReqText());
            txtHIndentReq.setTag(data.getIndentReqTag());
            txtHQuotationReq.setText(data.getQuotationReqText());
            txtHQuotationReq.setTag(data.getQuotationReqTag());

            txtHBestBefore.setText(data.getBestBeforeText());
            txtHBestBefore.setTag(data.getBestBeforeTag());
            txtHHighValue.setText(data.getHighValueText());
            txtHHighValue.setTag(data.getHighValueTag());
            txtHFlowThrough.setText(data.getFlowThroughText());
            txtHFlowThrough.setTag(data.getFlowThroughTag());
            txtHConsignmentItem.setText(data.getConsignmentText());
            txtHConsignmentItem.setTag(data.getConsignmentTag());
            txtHReturnable.setText(data.getReturnableItemText());
            txtHReturnable.setTag(data.getReturnableItemTag());
            txtHBilledItem.setText(data.getBilledItemText());
            txtHBilledItem.setTag(data.getBilledItemTag());
            txtHITSBasedOn.setTag(DmShared.xNull(data.getItsBasedOnTag()));
            txtHITSBasedOn.setTag2nd(DmShared.xNull(data.getItsBasedOnTag()));
            txtHITSBasedOn.setText(DmShared.xNull(data.getItsBasedOn()));

            txtHLabReq.setTag(DmShared.xNull(data.getLabRequiredTag()));
            txtHLabReq.setText(DmShared.xNull(data.getLabRequiredText()));
//            setUnitHeaderValue(DmShared.xNull(txtHSKU.getText()));
            if (!DmShared.xNull(txtHSKU.getText()).equalsIgnoreCase("")) {
                lblPurchaseConvertionRate.setText(DmShared.xNull(txtHSKU.getText()));
                lblSaleConvertionRate.setText(DmShared.xNull(txtHSKU.getText()));
                lblPackSize.setText(DmShared.xNull(txtHSKU.getText()));
            } else {
                lblPurchaseConvertionRate.setText("");
                lblSaleConvertionRate.setText("");
                lblPackSize.setText("");
            }
            prvSaleUnit = DmShared.xNull(data.getPrvSaleUnitSkuCode());
            prvPurchaseUnit = DmShared.xNull(data.getPrvPurchaseUnitSkuCode());
            txtHPurchaseUnitSKU.setText(DmShared.xNull(data.getPurchaseUnitSkuName()));
            txtHPurchaseUnitSKU.setTag(DmShared.xNull(data.getPurchaseUnitSkuCode()));
            txtHSaleUnitSKU.setText(DmShared.xNull(data.getSaleUnitSkuName()));
            txtHSaleUnitSKU.setTag(DmShared.xNull(data.getSaleUnitSkuCode()));
            txtConversionRatePurchase.setText(DmShared.xNull(data.getConversionRatePurchase()));
            txtConversionRateSale.setText(DmShared.xNull(data.getConversionRateSale()));
            prvConversionRatePurchase = DmShared.xNull(data.getConversionRatePurchase());
            prvConversionRateSale = DmShared.xNull(data.getConversionRateSale());
            txtConversionRatePurchase.setTag2nd(DmShared.xNull(data.getAllowDecimalPlaces()));
            txtConversionRateSale.setTag2nd(DmShared.xNull(data.getAllowDecimalPlaces()));
            txtHHSN.setTag(DmShared.xNull(data.getHsnCode()));
            txtHHSN.setText(DmShared.xNull(data.getHsnName()));
            txtHItemGSTNature.setTag(DmShared.xNull(data.getGstNatureTag()));
            txtHItemGSTNature.setText(DmShared.xNull(data.getGstNatureText()));

            txtPackSize.setText(DmShared.xNull(data.getPackSize()));
            txtPackSize.setTag2nd(DmShared.xNull(data.getAllowDecimalPlaces()));

            txtTolerancePlus.setText(DmShared.xNull(data.getTolerancePlus()));
            txtToleranceMinus.setText(DmShared.xNull(data.getToleranceMinus()));

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

            txtHGRIRAc.setTag(DmShared.xNull(data.getGrIrAcTag()));
            txtHGRIRAc.setText(DmShared.xNull(data.getGrIrAcText()));
            txtHConsumptionAc.setTag(DmShared.xNull(data.getConsumptionAcTag()));
            txtHConsumptionAc.setText(DmShared.xNull(data.getConsumptionAcText()));
            txtHCOGSAc.setTag(DmShared.xNull(data.getCogsAcTag()));
            txtHCOGSAc.setText(DmShared.xNull(data.getCogsAcText()));
            txtHPIDAc.setTag(DmShared.xNull(data.getPidAcTag()));
            txtHPIDAc.setText(DmShared.xNull(data.getPidAcText()));
            txtHInTransitAc.setTag(DmShared.xNull(data.getInTransitAcTag()));
            txtHInTransitAc.setText(DmShared.xNull(data.getInTransitAcText()));

            txtHItemSpeciality.setText(data.getItemSpecialityText());
            txtHItemSpeciality.setTag(data.getItemSpecialityTag());
            txtHItemMake.setText(data.getItemMakeText());
            txtHItemMake.setTag(data.getItemMakeTag());
            txtHMarketingCo.setText(data.getMarketingCompany());
            txtHMarketingCo.setTag(data.getMarketingCompanyCode());
            txtHManufacturer.setText(data.getManufacturerText());
            txtHManufacturer.setTag(data.getManufacturerTag());
            txtHDivision.setText(data.getDivisionText());
            txtHDivision.setTag(data.getDivisionTag());
            txtHNarcotics.setText(data.getNarcoticsText());
            txtHNarcotics.setTag(data.getNarcoticsTag());
            txtHItemModel.setText(data.getModelName());
            txtHItemModel.setTag(data.getModel());
            txtHItemStrength.setText(data.getStrengthName());
            txtHItemStrength.setTag(data.getStrength());

            txtHCostCenter.setText(DmShared.xNull(data.getCostCenterText()));
            txtHCostCenter.setTag(DmShared.xNull(data.getCostCenterTag()));
            txtHBudgetHead.setText(DmShared.xNull(data.getBudgetHeadText()));
            txtHBudgetHead.setTag(DmShared.xNull(data.getBudgetHeadTag()));

            grdItemDetails.getGrid().getStore().clear();
            grdItemDetails.getGrid().getStore().addAll(data.getGrdItem_Mdl());

            gridAlternativeItems.getGrid().getStore().clear();
            gridAlternativeItems.getGrid().getStore().addAll(data.getGrdAlternativeItems_Mdls());

            gridSoundLookAlike.getGrid().getStore().clear();
            gridSoundLookAlike.getGrid().getStore().addAll(data.getGrdSoundLookAlike_Mdls());

            gridRestrictedDept.getGrid().getStore().clear();
            gridRestrictedDept.getGrid().getStore().addAll(data.getGrdRestrictedDept_Mdls());

            gridApprovedVendors.getGrid().getStore().clear();
            gridApprovedVendors.getGrid().getStore().addAll(data.getGrdApprovedVendors_Mdls());

            gridQcParameter.getGrid().getStore().clear();
            gridQcParameter.getGrid().getStore().addAll(data.getGrdQcParameter_Mdls());

            if (DmShared.xNull(data.getItsBasedOnTag()).equalsIgnoreCase("0")
                    || DmShared.xNull(data.getItsBasedOnTag()).equalsIgnoreCase("")) {
                for (GrdItsRateSlab_Mdl grdRateITS_Mdl : data.getGrdRateIts_Mdls()) {
                    txtHGstType.setTag(DmShared.xNull(grdRateITS_Mdl.getGstTypeCode()));
                    txtHGstType.setText(DmShared.xNull(grdRateITS_Mdl.getGstTypeName()));
                }
            } else {
                grdItsRateSlab.getGrid().getStore().clear();
                grdItsRateSlab.getGrid().getStore().addAll(data.getGrdRateIts_Mdls());
            }
            manageITSContainer();

            isItemUsed = DmShared.xNull(data.getIsItemUsed());

            manageDisplay(false);
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlCard();
                    clear();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        tabPanel.selectTab(0);
        txtHMaterialCategory.setFocus(true);
        txtHBestBefore.setTag("0");
        txtHBestBefore.setText("No");
        txtHHighValue.setTag("0");
        txtHHighValue.setText("No");
        txtHFlowThrough.setTag("0");
        txtHFlowThrough.setText("No");
        txtHConsignmentItem.setTag("0");
        txtHConsignmentItem.setText("No");
        txtHReturnable.setTag("1");
        txtHReturnable.setText("Yes");
        txtHBilledItem.setTag("0");
        txtHBilledItem.setText("No");
        txtHAllowNegetiveStock.setTag("0");
        txtHAllowNegetiveStock.setText("No");
        txtHPoReq.setTag("0");
        txtHPoReq.setText("No");
        txtHIndentReq.setTag("0");
        txtHIndentReq.setText("No");
        txtHQuotationReq.setTag("0");
        txtHQuotationReq.setText("No");
        txtHLabReq.setTag("0");
        txtHLabReq.setText("No");
        txtHNarcotics.setText("No");
        txtHNarcotics.setTag("0");
        txtHLabReq.setTag(String.valueOf(STEnum.LabRequired.NO.getId()));
        txtHLabReq.setText(STEnum.LabRequired.NO.getValue());

        txtHITSBasedOn.setTag("0");
        txtHITSBasedOn.setText("Fixed");
        txtHGstType.setText("");
        txtHGstType.setTag("");
        txtHActive.setTag("1");
        txtHActive.setText("Active");
        manageITSContainer();

//        gridSecondaryUnit.getGrid().getStore().add(new GrdUnit_Mdl());
        grdItemDetails.getGrid().getStore().add(new GrdItemDepartmentInfo_Mdl());

        gridApprovedVendors.getGrid().getStore().add(new GrdApprovedVendors_Mdl());

        gridQcParameter.getGrid().getStore().add(new GrdQcParameter_Mdl());

        gridAlternativeItems.getGrid().getStore().add(new GrdAlternativeItems_Mdl());

        gridRestrictedDept.getGrid().getStore().add(new GrdRestrictedDept_Mdl());

        gridSoundLookAlike.getGrid().getStore().add(new GrdSoundLookAlike_Mdl());

        txtManualCode.setEnabled(false);
//        txtApplicableBatch.setEnabled(false);
//        txtApplicableBatch.setText("batchNo, expiryDate, mrp");

    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    public void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(statusChangedByBlog, "");
        grdItemDetails.getGrid().getStore().clear();
        gridApprovedVendors.getGrid().getStore().clear();
        gridQcParameter.getGrid().getStore().clear();
        gridRestrictedDept.getGrid().getStore().clear();
        gridSoundLookAlike.getGrid().getStore().clear();
        gridAlternativeItems.getGrid().getStore().clear();
        grdItsRateSlab.getGrid().getStore().clear();
        isItemUsed = "";
        previousSelectedSKU = "";
        prvSkuTag = "";
        prvPurchaseUnit = "";
        prvSaleUnit = "";
        prvConversionRatePurchase = "";
        prvConversionRateSale = "";
        lblPurchaseConvertionRate.setText("");
        lblSaleConvertionRate.setText("");
        isNegativeStock = false;
        wtaCode = "";
        grdItsRateSlab.getGrid().getStore().clear();
        txtHGstType.setTag("");
        txtHGstType.setText("");
    }

    @Override
    public void onBlur(BlurEvent event) {

        if ((event.getSource() == txtConversionRatePurchase)) {
            if (!DmShared.xNull(txtConversionRatePurchase.getText()).equalsIgnoreCase("")) {
                calculate();
            }
        }
        if ((event.getSource() == txtConversionRateSale)) {
            if (!DmShared.xNull(txtConversionRateSale.getText()).equalsIgnoreCase("")) {
                calculate();
            }
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        btnClick((Button) event.getSource());
    }

    private void btnClick(Button button) {
        if (!button.isAttached()) {
            return;
        }

        if (!button.isVisible()) {
            return;
        }

        if (!button.isEnabled()) {
            return;
        }

//        if (button.equals(btnActiveBy)) {
//            if (DmShared.iNull(topControl.getDocId()).longValue() > 0) {
//
//                if (DmShared.iNull(txtItemStatus.getTag()).intValue() != ItemStatus.ACTIVE.getId()) {
//                    setActive(topControl.getDocId(), true);
//                } else {
//                    setActive(topControl.getDocId(), false);
//                }
//            }
//        }
    }

//    private void setActive(String docId, boolean isActive) {
//        try {
//            if (DmShared.iNull(wtaCode).longValue() != 0) {
//                walkThrough.show(wtaCode);
//            } else {
//                srvAsync.invokeRPC().setActive(DmClient.getLoginUserName(), isActive, docId, new AsyncCallback<DmResult<String>>() {
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResult<String> result) {
//                        if (result.isStatus()) {
//                            topControlMoveRec(result.getData());
//                        } else {
//                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
//                        }
//                    }
//                });
//            }
//        } catch (DmException e) {
//            topControlSaveError(e.getErrorNo(), e.getMessage());
//        }
//    }
    private void manageDisplay(boolean flag) {
        if (!flag_Name_Editable) {
            txtName.setEnabled(false);
        } else {
            txtName.setEnabled(true);
        }

        txtHBestBefore.setEnabled(false);
        txtHCategory.setEnabled(false);

        if (topControl.getMode().equals("Edit")) {
            if (isNegativeStock && DmShared.iNull(txtHAllowNegetiveStock.getTag()).intValue() == 1) {
                txtHAllowNegetiveStock.setEnabled(false);
            }
            if (!isItemUsed.equalsIgnoreCase("1")) {
                txtHPurchaseUnitSKU.setEnabled(true);
                txtHSaleUnitSKU.setEnabled(true);
                txtHSKU.setEnabled(true);
                txtPackSize.setEnabled(true);
                txtConversionRatePurchase.setEnabled(true);
                txtConversionRateSale.setEnabled(true);
                if (isInventoryAccountingActive) {
                    txtHInventoryAc.setEnabled(true);
                    txtHConsumptionAc.setEnabled(true);
                    txtHCOGSAc.setEnabled(true);
                    txtHPIDAc.setEnabled(true);
                    txtHGRIRAc.setEnabled(true);
                    txtHPurchaseAc.setEnabled(true);
                    txtHPurchaseReturnAc.setEnabled(true);
                    txtHSalesAc.setEnabled(true);
                    txtHSalesReturnAc.setEnabled(true);
                    txtHInTransitAc.setEnabled(true);
                    txtHItemGroup.setEnabled(true);
                }
                manageTxtHBestBefore();
            } else {
                txtHPurchaseUnitSKU.setEnabled(false);
                txtHSaleUnitSKU.setEnabled(false);
                txtHSKU.setEnabled(false);
                txtPackSize.setEnabled(false);
                txtConversionRatePurchase.setEnabled(false);
                txtConversionRateSale.setEnabled(false);
                if (isInventoryAccountingActive) {
                    txtHInventoryAc.setEnabled(false);
                    txtHConsumptionAc.setEnabled(false);
                    txtHCOGSAc.setEnabled(false);
                    txtHPIDAc.setEnabled(false);
                    txtHGRIRAc.setEnabled(false);
                    txtHPurchaseAc.setEnabled(false);
                    txtHPurchaseReturnAc.setEnabled(false);
                    txtHSalesAc.setEnabled(false);
                    txtHSalesReturnAc.setEnabled(false);
                    txtHInTransitAc.setEnabled(false);
                    txtHItemGroup.setEnabled(false);
                }

            }
            if (!DmShared.xNull(txtHPurchaseUnitSKU.getTag()).equalsIgnoreCase("") && !DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtHPurchaseUnitSKU.getTag()).equalsIgnoreCase(DmShared.xNull(txtHSKU.getTag()))) {
                    txtConversionRatePurchase.setEnabled(false);
                }
            }
            if (!DmShared.xNull(txtHSaleUnitSKU.getTag()).equalsIgnoreCase("") && !DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase("")) {
                if (DmShared.xNull(txtHSaleUnitSKU.getTag()).equalsIgnoreCase(DmShared.xNull(txtHSKU.getTag()))) {
                    txtConversionRateSale.setEnabled(false);
                }
            }
        }
        if (!isEditAccounts) {
            txtHInventoryAc.setEnabled(false);
            txtHConsumptionAc.setEnabled(false);
            txtHCOGSAc.setEnabled(false);
            txtHPIDAc.setEnabled(false);
            txtHGRIRAc.setEnabled(false);
            txtHPurchaseAc.setEnabled(false);
            txtHPurchaseReturnAc.setEnabled(false);
            txtHSalesAc.setEnabled(false);
            txtHSalesReturnAc.setEnabled(false);
            txtHInTransitAc.setEnabled(false);
        }
    }

    private void manageTxtHBestBefore() {
        boolean isExpiryDateApplicable = false;

        String[] property = DmShared.xNull(txtHProperties.getTag()).split(",");

        for (String x : property) {
            if (x.equalsIgnoreCase(ItemProperty_Mdl.Properties.EXPIRY_DATE.getPropertyManualCode())) {
                isExpiryDateApplicable = true;
                break;
            }
        }

        if (isExpiryDateApplicable) {
            if (!txtHBestBefore.isEnabled()) {
                txtHBestBefore.setEnabled(true);
            }
        } else {
            txtHBestBefore.setEnabled(false);
            txtHBestBefore.setTag("0");
            txtHBestBefore.setText("No");
        }

    }

    private void calculate() {
        if (!DmShared.xNull(txtConversionRatePurchase.getText()).equalsIgnoreCase("")) {
            txtConversionRatePurchase.setText(DmClient.vNull(txtConversionRatePurchase.getText(), DmShared.getNumberFormat(DmShared.iNull(txtConversionRatePurchase.getTag2nd()).intValue())));
        }
        if (!DmShared.xNull(txtConversionRateSale.getText()).equalsIgnoreCase("")) {
            txtConversionRateSale.setText(DmClient.vNull(txtConversionRateSale.getText(), DmShared.getNumberFormat(DmShared.iNull(txtConversionRateSale.getTag2nd()).intValue())));
        }
    }

    private void handlerEvents() {
        txtConversionRatePurchase.addBlurHandler(this);
        txtConversionRateSale.addBlurHandler(this);
        btnActiveBy.addClickHandler(this);
    }

    private void fillSkuHelps(Sku_Mdl tRow) {
        flagFillSkuValue = false;
        if (DmShared.iNull(previousSelectedSKU).longValue() == DmShared.iNull(txtHPurchaseUnitSKU.getTag()).longValue()) {
            if (DmShared.iNull(txtHPurchaseUnitSKU.getTag()).longValue() == DmShared.iNull(txtHSaleUnitSKU.getTag()).longValue()) {
                flagFillSkuValue = true;
            }
        }

        if (flagFillSkuValue) {
            txtHSKU.setText(DmShared.xNull(tRow.getSharedKeyFieldName()));
            txtHSKU.setTag(DmShared.xNull(tRow.getSharedKeyFieldCode()));
            txtHPurchaseUnitSKU.setText(DmShared.xNull(tRow.getSharedKeyFieldName()));
            txtHPurchaseUnitSKU.setTag(DmShared.xNull(tRow.getSharedKeyFieldCode()));
            txtHSaleUnitSKU.setText(DmShared.xNull(tRow.getSharedKeyFieldName()));
            txtHSaleUnitSKU.setTag(DmShared.xNull(tRow.getSharedKeyFieldCode()));
        }

    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuManageSite)) {
            if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                manageSite.show("itemSiteList", "code", topControl.getDocId(), "site_Code");
            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtName).show();
            }
        } else if (event.getSource().equals(mnuExportCard)) {
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
        srvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
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

    private void manageITSContainer() {
        flwMain.clear();
        if (DmShared.xNull(txtHITSBasedOn.getTag()).equalsIgnoreCase("1")) {  //rate
            vl.add(grdItsRateSlab.getGrid());
            flwMain.add(flwITSContainer);
            if (grdItsRateSlab.getGrid().getStore().size() == 0) {
                grdItsRateSlab.getGrid().getStore().add(new GrdItsRateSlab_Mdl());
            }
        } else if (DmShared.xNull(txtHITSBasedOn.getTag()).equalsIgnoreCase("0")) { //fixed
            flwMain.clear();
            vl.get().remove(grdItsRateSlab.getGrid());
            vl.add(txtHGstType);
            flwMain.add(flwGstType);
        }

    }

    private void setNameText() {
        String medicineName = "";

        if (!flag_Name_Editable) {
            if (!DmShared.xNull(txtHMaterialCategory.getTag()).equalsIgnoreCase("")) {
                medicineName += DmShared.xNull(txtHMaterialCategory.getText()).trim();
            }
            if (!DmShared.xNull(txtHMaterialType.getTag()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHMaterialType.getText()).trim();
            }
            if (!DmShared.xNull(txtHItemStrength.getText()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHItemStrength.getText()).trim();
            }
            if (!DmShared.xNull(txtHItemModel.getText()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHItemModel.getText()).trim();
            }
            if (dvm != null) {
                if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("U")) {
                    medicineName = medicineName.toUpperCase();
                } else if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("L")) {
                    medicineName = medicineName.toLowerCase();
                } else if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("S")) {
                    medicineName = DmShared.sentenceCase(medicineName);
                } else if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("P")) {
                    medicineName = DmShared.properCase(medicineName);
                }
            }
            txtName.setText(medicineName);
        } else {
            medicineName = txtName.getText();
        }
//        if (!flag_Name_Editable) {
//            if (!DmShared.xNull(txtHMaterialCategory.getTag()).equalsIgnoreCase("")) {
//                medicineName += DmShared.xNull(txtHMaterialCategory.getText()).trim();
//            }
//            if (!DmShared.xNull(txtHMaterialType.getTag()).equalsIgnoreCase("")) {
//                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHMaterialType.getText()).trim();
//            }
//            if (!DmShared.xNull(txtHItemStrength.getText()).equalsIgnoreCase("")) {
//                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHItemStrength.getText()).trim();
//            }
//            if (!DmShared.xNull(txtHItemModel.getText()).equalsIgnoreCase("")) {
//                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHItemModel.getText()).trim();
//            }
//        }


        if (dvm != null) {
            if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("U")) {
                medicineName = medicineName.toUpperCase();
            } else if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("L")) {
                medicineName = medicineName.toLowerCase();
            } else if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("S")) {
                medicineName = DmShared.sentenceCase(medicineName);
            } else if (DmShared.xNull(dvm.getMedicineNameAlphabetCase()).equalsIgnoreCase("P")) {
                medicineName = DmShared.properCase(medicineName);
            }
        }
        txtName.setText(medicineName);
    }

    private void iniDefaultValue(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    dvm = result.getData();
                    isEditAccounts = dvm.isAccountsEditable();
                    isInventoryAccountingActive = dvm.isInventoryAccountingActive();
                    flag_Name_Editable = dvm.isEdit_NameIn_Inventory_ItemMAst();
                    swi.setWidget(ItemMaster.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
