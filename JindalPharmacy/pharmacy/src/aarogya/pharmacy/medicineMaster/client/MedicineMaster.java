package aarogya.pharmacy.medicineMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import java.util.List;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import aarogya.pharmacy.medicineMaster.shared.*;
import aarogya.projectMenu.self.shared.ProjectRuleCode;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
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
import dataman.dmToolkit.dmFileManager.client.DmFileManager;
import dataman.dmToolkit.manageSite.client.ManageSite;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.HashMap;

public final class MedicineMaster extends MedicineMaster_Dsg implements BlurHandler, SelectionHandler<Item>, AttachEvent.Handler {

    ServiceInvoker<MedicineMaster_SrvAsync> srvAsync;
    final ManageSite manageSite = new ManageSite();
    final MedicineDetailsGridEdit<GrdMedicineDepartmentInfo_Mdl, KeyFieldHashCode_T_PrpAcs> medGridEdit = new MedicineDetailsGridEdit(grdMedDetails);
    private final ApprovedVendorsGridEdit<GrdApprovedVendors_Mdl, KeyFieldHashCode_T_PrpAcs> ApprovedVendorsGrid = new ApprovedVendorsGridEdit(gridApprovedVendors);
    private final AlternativeItemsGridEdit<GrdAlternativeItems_Mdl, KeyFieldHashCode_T_PrpAcs> AlternativeItemsGrid = new AlternativeItemsGridEdit(gridAlternativeItems);
    private final CompositionGrid<GrdComposition_Mdl, KeyFieldHashCode_T_PrpAcs> compositionGrid = new CompositionGrid(grdCompositionDetails);
    private final RestrictedDepartmentGridEdit<GrdRestrictedDept_Mdl, KeyFieldHashCode_T_PrpAcs> RestrictedDeptGrid = new RestrictedDepartmentGridEdit(gridRestrictedDept);
    private final SoundLookAlikeGridEdit<GrdSoundLookAlike_Mdl, KeyFieldHashCode_T_PrpAcs> SoundLookAlikeGrid = new SoundLookAlikeGridEdit(gridSoundLookAlike);
    private final SchmeGridEdit<GrdSchemeDetails_Mdl, Grd_PrpAcs> schemeDetailsGrid = new SchmeGridEdit(grdSchemeDetail);
    String purchaseDecimalPlaces = "", isItemUsed = "", previousSelectedSKU = "", prvSkuTag = "", prvSaleUnit = "", prvPurchaseUnit = "", prvConversionRatePurchase = "", prvConversionRateSale = "";
    boolean flagFillSkuValue = false, flagOpenAsPopup = false;
    protected DefaultValue_Mdl defaultValue;
    private boolean isNegativeStock = false, flag_Name_Editable = false;
    final DmFileManager fileManager = new DmFileManager(ProjectRuleCode.ASSETS.getRuleCode());
    boolean isAttachFiles = false, isDeleteFiles = false, isInventoryAccountingActive = false;
    String nature = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(true);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(attachFileBlog)) {
                isAttachFiles = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(deleteFileBlog)) {
                isDeleteFiles = true;
            }
        }

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
                tabPanel.selectTab(0);
                txtDisplayName.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHSKU.setEnabled(true);
                manageDisplay(true);
                txtDisplayName.setFocus(true);
                txtManualCode.setEnabled(true);
                if (DmShared.xNull(txtHSKU.getTag2nd()).equalsIgnoreCase("No")) {
                    txtHSKU.setEnabled(false);
                }
                grdMedDetails.getGrid().getStore().add(new GrdMedicineDepartmentInfo_Mdl());
                gridApprovedVendors.getGrid().getStore().add(new GrdApprovedVendors_Mdl());
                gridAlternativeItems.getGrid().getStore().add(new GrdAlternativeItems_Mdl());
                gridRestrictedDept.getGrid().getStore().add(new GrdRestrictedDept_Mdl());
                gridSoundLookAlike.getGrid().getStore().add(new GrdSoundLookAlike_Mdl());
                grdCompositionDetails.getGrid().getStore().add(new GrdComposition_Mdl());
                grdSchemeDetail.getGrid().getStore().add(new GrdSchemeDetails_Mdl());
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
                //==============================< Check For Different SKU units >=======================================
                if (DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase(DmShared.xNull(txtHPurchaseUnitSKU.getTag()))
                        && DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase(DmShared.xNull(DmShared.xNull(txtHSaleUnitSKU.getTag())))) {
                    topControlSave();
                } else {
                    new DmConfirmMessageBox("SKU Units are different.Are you sure? You want to save.", new DialogHideEvent.DialogHideHandler() {
                        @Override
                        public void onDialogHide(DialogHideEvent event) {
                            if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                topControlSave();
                            }
                        }
                    }).show();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                if (flagOpenAsPopup) {
                    ppForm.hide();
                } else {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(MedicineMaster.this);
                }
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

    private void gridEvents() {

        medGridEdit.addCallBack(new MedicineDetailsGridEdit.CallBack() {

            @Override
            public void onOk(GrdMedicineDepartmentInfo_Mdl gridItem_Mdl) {
                grdMedDetails.commitChanges();
            }

            @Override
            public void onDelete() {
                grdMedDetails.commitChanges();
            }
        });

        grdMedDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showItemGridEdit(event.getRowIndex());
            }
        });

        grdMedDetails.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemGridEdit(grdMedDetails.getGrid().getStore().indexOf(grdMedDetails.getGrid().getSelectionModel().getSelectedItem()));
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
        //=======================================CompositionGrid=================================
        compositionGrid.addCallBack(new CompositionGrid.CallBack() {

            @Override
            public void onOk(GrdComposition_Mdl grdComposition_Mdl) {
                grdCompositionDetails.commitChanges();
            }

            @Override
            public void onDelete() {
                grdCompositionDetails.commitChanges();
            }

        });

        grdCompositionDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showCompositionDetailGrid(event.getRowIndex());
            }
        });

        grdCompositionDetails.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showCompositionDetailGrid(grdCompositionDetails.getGrid().getStore().indexOf(grdCompositionDetails.getGrid().getSelectionModel().getSelectedItem()));
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

        // =============================Grid Scheme Details =================================================
        schemeDetailsGrid.addCallBack(new SchmeGridEdit.CallBack() {

            @Override
            public void onOk(GrdSchemeDetails_Mdl grdDetails) {
                grdSchemeDetail.commitChanges();
            }

            @Override
            public void onDelete() {
                grdSchemeDetail.commitChanges();
            }

        });

        grdSchemeDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                showSchemeDetailsGrid(event.getRowIndex());
            }
        });

        grdSchemeDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showSchemeDetailsGrid(grdSchemeDetail.getGrid().getStore().indexOf(grdSchemeDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showItemGridEdit(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            medGridEdit.show(currentRowIndex, false, false, false);
        } else {
            medGridEdit.show(currentRowIndex, true, true, true);
        }
    }

    private void showSchemeDetailsGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            schemeDetailsGrid.show(currentRowIndex, false, false, false);
        } else {
            schemeDetailsGrid.show(currentRowIndex, true, true, true);
        }
    }

    private void showApprovedVendorsGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            ApprovedVendorsGrid.show(currentRowIndex, false, false, false);
        } else {
            ApprovedVendorsGrid.show(currentRowIndex, true, true, true);
        }
    }

    private void showAlternativeItemsGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            AlternativeItemsGrid.show(currentRowIndex, false, false, false, this);
        } else {
            AlternativeItemsGrid.show(currentRowIndex, true, true, true, this);
        }
    }

    private void showCompositionDetailGrid(int currentRowIndex) {
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            compositionGrid.show(currentRowIndex, false, false, false, this);
        } else {
            compositionGrid.show(currentRowIndex, true, true, true, this);
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

    private void textHelpEvents() {
        tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                if (event.getSelectedItem() == tabIndexAisala) {
                    ((CellSelectionModel) gridAlternativeItems.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        });

        txtDisplayName.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                setNameText();
            }
        });

        txtDisplayName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtDisplayName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtDisplayName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtDisplayName).show();
                        }
                    }
                });
            }
        });
        txtHStatus.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                txtHStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

                    @Override
                    public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {

                        srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, true, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                new DmAlertMessageBox(caught.getMessage(), txtHStatus).show();
                            }

                            @Override
                            public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                                if (result.isStatus()) {
                                    txtHStatus.setData(result.getData(), result.getPaging());
                                } else {
                                    new DmAlertMessageBox(result.getErrorMessage(), txtHStatus).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
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
                        setNameText();
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCategory_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    setNameText();
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
                    setNameText();
                }
            }
        });

        txtHSubCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCategory(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSubCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSubCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSubCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

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

        txtHCompany.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCompany_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCompany(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCompany_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCompany).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCompany_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCompany.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCompany).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCompany_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtHCompany.setTag2nd(t1Row.getShortName());
                    if (DmShared.vNull(txtPurchaseDisPer.getText()).doubleValue() == 0) {
                        txtPurchaseDisPer.setText(DmClient.vNull(t1Row.getPurchaseDisPer(), "0.00"));
                    }
                    setNameText();

                }
            }
        });

        txtHMedGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedGrp_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedGrp_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedGrp_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedGrp_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    if (DmShared.vNull(txtSaleDisPer.getText()).doubleValue() == 0) {
                        txtSaleDisPer.setText(DmClient.vNull(t1Row.getSaleDisPer(), "0.00"));

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
                    }
                }
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
                    fillSkuHelps(tRow);
                    lblPurchaseConvertionRate.setText(tRow.getSharedKeyFieldName());
                    lblSaleConvertionRate.setText(tRow.getSharedKeyFieldName());
                    txtConversionRatePurchase.setTag2nd(tRow.getAllowedDecimalPlaces());
                    txtConversionRateSale.setTag2nd(tRow.getAllowedDecimalPlaces());

                    if (!DmShared.xNull(txtHPurchaseUnitSKU.getTag()).equalsIgnoreCase("")) {
                        lblPurchaseUnitName.setText(txtHPurchaseUnitSKU.getText());
                        lblMRPUnitName.setText(txtHPurchaseUnitSKU.getText());
                        lblPurchaseRateUnitName.setText(txtHPurchaseUnitSKU.getText());
                        lblSaleRateUnitName.setText(txtHPurchaseUnitSKU.getText());
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
                    lblPurchaseConvertionRate.setText("");
                    lblSaleConvertionRate.setText("");
                }
                setNameText();
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

        txtHLifeSaving.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLifeSaving).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLifeSaving.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLifeSaving).show();
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

        txtHPurchaseUnitSKU.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Sku_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (!DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getSKU("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Sku_Mdl>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHPurchaseUnitSKU).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<Sku_Mdl>> result) {
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
            public void getSelectedRow(boolean blnStatus, Sku_Mdl tRow) {
                if (blnStatus && tRow != null) {
                    lblPurchaseUnitName.setText(DmShared.xNull(tRow.getSharedKeyFieldName()));
                    lblMRPUnitName.setText(txtHPurchaseUnitSKU.getText());
                    lblPurchaseRateUnitName.setText(txtHPurchaseUnitSKU.getText());
                    lblSaleRateUnitName.setText(txtHPurchaseUnitSKU.getText());
                    purchaseDecimalPlaces = tRow.getAllowedDecimalPlaces();
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

        txtHSaleUnitSKU.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Sku_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (!DmShared.xNull(txtHSKU.getTag()).equalsIgnoreCase("")) {
                    srvAsync.invokeRPC(blnCallBackWait).getSKU("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Sku_Mdl>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHSaleUnitSKU).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<Sku_Mdl>> result) {
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
            public void getSelectedRow(boolean blnStatus, Sku_Mdl tRow) {
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

        txtHIsDiscountAllow.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsDiscountAllow).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsDiscountAllow.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsDiscountAllow).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsSaleRateEditable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsSaleRateEditable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsSaleRateEditable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsSaleRateEditable).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsIncludeFreeQtyInSaleRate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsIncludeFreeQtyInSaleRate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsIncludeFreeQtyInSaleRate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsIncludeFreeQtyInSaleRate).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsCommonItem.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsCommonItem).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsCommonItem.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsCommonItem).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
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
            public void getSelectedRow(boolean blnStatus, TxtHHSN_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    txtItemGSTNature.setTag(DmShared.xNull(t1Row.getItemGSTNatureTag()));
                    txtItemGSTNature.setText(DmShared.xNull(t1Row.getItemGSTNatureText()));
                    srvAsync.invokeRPC().getHSNDetail(DmShared.xNull(t1Row.getSharedKeyFieldCode()), new AsyncCallback<DmResult<HSNDetail_Mdl>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHHSN).show();
                        }

                        @Override
                        public void onSuccess(DmResult<HSNDetail_Mdl> result) {
                            if (result.isStatus()) {
                                fillHSNDetail(result);
                            }
                        }
                    });
                } else {
                    txtItemGSTNature.setTag("");
                    txtItemGSTNature.setText("");
                }
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

        txtHIsBarcodeActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsBarcodeActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsBarcodeActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsBarcodeActive).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHITSOnPurchase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                nature = "Purchase";
                srvAsync.invokeRPC(blnCallBackWait).getTxtHITS(nature, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnPurchase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnPurchase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnPurchase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtITSOnPurchaseReturn.setTag(txtHITSOnPurchase.getTag());
                if (!DmShared.vNull(t1Row.getSharedPreparedBy()).toString().equalsIgnoreCase("")) {
                    txtTaxPerc.setText(DmShared.vNull(t1Row.getSharedPreparedBy()).toString());
                    txtTaxPerc.setTag(DmShared.vNull(t1Row.getSharedPreparedBy()).toString());
                    txtHITSOnPurchase.setTag2nd(DmShared.vNull(t1Row.getSharedPreparedBy()).toString());
                }
            }
        });
        txtHITSOnSale.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                nature = "Sales";
                srvAsync.invokeRPC(blnCallBackWait).getTxtHITS(nature, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHITSOnSale).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHITSOnSale.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHITSOnSale).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                txtITSOnSaleReturn.setTag(txtHITSOnSale.getTag());
                if (!DmShared.vNull(t1Row.getSharedPreparedBy()).toString().equalsIgnoreCase("")) {
                    txtTaxPerc.setText(DmShared.vNull(t1Row.getSharedPreparedBy()).toString());
                    txtTaxPerc.setTag(DmShared.vNull(t1Row.getSharedPreparedBy()).toString());
                    txtHITSOnSale.setTag2nd(DmShared.vNull(t1Row.getSharedPreparedBy()).toString());
                }
            }
        });

        txtHPurchaseAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSaleAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaleAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHSaleReturnAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleReturnAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSaleReturnAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleReturnAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPurchaseReturnAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseReturnAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseReturnAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseReturnAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHMarginRateFix.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMarginRateFix(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMarginRateFix).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMarginRateFix.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMarginRateFix).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

    private void manageDisplay(boolean flag) {
        txtTaxPerc.setEnabled(false);
        txtResGroup.setEnabled(false);
        txtHStatus.setEnabled(flag);
        txtHIsCommonItem.setEnabled(flag);
        txtItemGSTNature.setEnabled(false);
        txtName.setEnabled(false);
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            if (isNegativeStock && DmShared.iNull(txtHAllowNegetiveStock.getTag()).intValue() == 1) {
                txtHAllowNegetiveStock.setEnabled(false);
            }
            if (isItemUsed.equalsIgnoreCase("1")) {
                txtHBestBefore.setEnabled(false);
                txtHPurchaseUnitSKU.setEnabled(false);
                txtHSaleUnitSKU.setEnabled(false);
                txtHSKU.setEnabled(false);
                txtConversionRatePurchase.setEnabled(false);
                txtConversionRateSale.setEnabled(false);
                if (isInventoryAccountingActive) {
                    txtHMedGroup.setEnabled(false);
                    txtHPurchaseAC.setEnabled(false);
                    txtHPurchaseReturnAC.setEnabled(false);
                    txtHSaleAC.setEnabled(false);
                    txtHSaleReturnAC.setEnabled(false);
                }
            } else {
                txtHPurchaseUnitSKU.setEnabled(true);
                txtHSaleUnitSKU.setEnabled(true);
                txtHSKU.setEnabled(true);
                txtHMedGroup.setEnabled(true);
                txtConversionRatePurchase.setEnabled(true);
                txtConversionRateSale.setEnabled(true);
                txtHMedGroup.setEnabled(true);
                txtHPurchaseAC.setEnabled(true);
                txtHPurchaseReturnAC.setEnabled(true);
                txtHSaleAC.setEnabled(true);
                txtHSaleReturnAC.setEnabled(true);
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
        }
    }

    public MedicineMaster(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super();
        flagOpenAsPopup = (Boolean) paramHashMap.get("flagOpenAsPopup");
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(MedicineMaster_Srv.class), MedicineMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        mnuManageSite.addSelectionHandler(this);
        mnuExportCard.addSelectionHandler(this);
        mnuManageFile.addSelectionHandler(this);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        handlerEvents();

        manageDisplay(false);
        if (flagOpenAsPopup) {
            ppForm.clear();
            ppForm.add(flwForm);
            ppForm.setGlassEnabled(true);
            ppForm.center();
        }
        iniDefault(swi);
    }

    @Override
    public Widget asWidget() {
        if (flagOpenAsPopup) {
            return ppForm;
        } else {
            return flwForm;
        }
    }

    private MedicineMaster_Mdl topControlSaveInitialize() {
        MedicineMaster_Mdl mrTemp = new MedicineMaster_Mdl();
        calculate();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        lbldisplay.setText("");
        setNameText();
        mrTemp.setManualCode(DmShared.removeSpace(txtManualCode.getText()).trim());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setStatusTag(DmShared.xNull(txtHStatus.getTag()));
        mrTemp.setCategoryText(DmShared.xNull(txtHCategory.getText()));
        mrTemp.setCategoryTag(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setMaterialCategoryText(DmShared.xNull(txtHMaterialCategory.getText()));
        mrTemp.setMaterialCategoryTag(DmShared.xNull(txtHMaterialCategory.getTag()));
        mrTemp.setModel(DmShared.xNull(txtHItemModel.getTag()));
        mrTemp.setStrength(DmShared.xNull(txtHItemStrength.getTag()));
        mrTemp.setCompanyTag(DmShared.xNull(txtHCompany.getTag()));
        mrTemp.setMedGroupText(DmShared.xNull(txtHMedGroup.getText()));
        mrTemp.setMedGroupTag(DmShared.xNull(txtHMedGroup.getTag()));
        mrTemp.setPriority(DmShared.iNull(txtPriority.getText()).toString());
        mrTemp.setMarginRateTag(DmShared.xNull(txtHMarginRateFix.getTag()));
        mrTemp.setMarginRateText(DmShared.xNull(txtHMarginRateFix.getText()));
        mrTemp.setDivisionTag(DmShared.xNull(txtHDivision.getTag()));
        mrTemp.setDivisionText(DmShared.xNull(txtHDivision.getText()));

        mrTemp.setMrp(DmClient.vNull(txtMRP.getText(), "0.00"));
        mrTemp.setSaleRate(DmClient.vNull(txtSaleRate.getText(), "0.00"));
        mrTemp.setPurchaseRate(DmClient.vNull(txtPurchaseRate.getText(), "0.00"));
        mrTemp.setPurchaseDisPer(DmClient.vNull(txtPurchaseDisPer.getText(), "0.00"));
        mrTemp.setSaleDisPer(DmClient.vNull(txtSaleDisPer.getText(), "0.00"));
        mrTemp.setTaxPerc(DmShared.xNull(txtTaxPerc.getText()));
        mrTemp.setSchemeFreeQty(DmClient.vNull(txtSchemeFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(purchaseDecimalPlaces).intValue())));
        mrTemp.setSchemeOnQty(DmClient.vNull(txtSchemeOnQty.getText(), DmShared.getNumberFormat(DmShared.iNull(purchaseDecimalPlaces).intValue())));

        mrTemp.setManufacturerCode(DmShared.xNull(txtHManufacturer.getTag()));
        mrTemp.setManufacturerName(DmShared.xNull(txtHManufacturer.getText()));

        mrTemp.setRestrictGroupTag(DmShared.xNull(txtResGroup.getTag()));
        mrTemp.setSkuTag(DmShared.xNull(txtHSKU.getTag()));
        mrTemp.setPrvSkuTag(DmShared.xNull(prvSkuTag));
        mrTemp.setPackSize(DmShared.xNull(txtPackSize.getText()));

        mrTemp.setIsDiscountAllowTag(DmShared.xNull(txtHIsDiscountAllow.getTag()));
        mrTemp.setIsDiscountAllowText(DmShared.xNull(txtHIsDiscountAllow.getText()));
        mrTemp.setIsCommonItemTag(DmShared.xNull(txtHIsCommonItem.getTag()));
        mrTemp.setIsCommonItemText(DmShared.xNull(txtHIsCommonItem.getText()));

        mrTemp.setBestBeforeTag(DmShared.xNull(txtHBestBefore.getTag()));
        mrTemp.setLifeSavingTag(DmShared.xNull(txtHLifeSaving.getTag()));
        mrTemp.setHighValueTag(DmShared.xNull(txtHHighValue.getTag()));
        mrTemp.setNarcoticsTag(DmShared.xNull(txtHNarcotics.getTag()));

        mrTemp.setPoReqTag(DmShared.xNull(txtHPoReq.getTag()));
        mrTemp.setIndentReqTag(DmShared.xNull(txtHIndentReq.getTag()));
        
        mrTemp.setQuotationReqTag(DmShared.xNull(txtHQuotationReq.getTag()));

        mrTemp.setConsignmentTag(DmShared.xNull(txtHConsignmentItem.getTag()));
        mrTemp.setReturnableItemTag(DmShared.xNull(txtHReturnable.getTag()));
        mrTemp.setBilledItemTag(DmShared.xNull(txtHBilledItem.getTag()));
        mrTemp.setFlowThroughTag(DmShared.xNull(txtHFlowThrough.getTag()));
        mrTemp.setGstNatureTag(DmShared.xNull(txtItemGSTNature.getTag()));
        //=====================<HSN Detail>===============================//
        mrTemp.setHsnCode(DmShared.xNull(txtHHSN.getTag()));
        mrTemp.setIts_Code_Purchase(DmShared.xNull(txtHITSOnPurchase.getTag()));
        mrTemp.setIts_Tax_Purchase(DmShared.xNull(txtHITSOnPurchase.getTag2nd()));
        mrTemp.setIts_Code_PurchaseRet(DmShared.xNull(txtITSOnPurchaseReturn.getTag()));
        mrTemp.setIts_Code_Sales(DmShared.xNull(txtHITSOnSale.getTag()));
        mrTemp.setIts_Tax_Sales(DmShared.xNull(txtHITSOnSale.getTag2nd()));
        mrTemp.setIts_Code_SalesRet(DmShared.xNull(txtITSOnSaleReturn.getTag()));
        mrTemp.setItsPurAcCode(DmShared.xNull(txtHPurchaseAC.getTag()));
        mrTemp.setItsPurRetAcCode(DmShared.xNull(txtHPurchaseReturnAC.getTag()));
        mrTemp.setItsSaleAcCode(DmShared.xNull(txtHSaleAC.getTag()));
        mrTemp.setItsSaleReturnAcCode(DmShared.xNull(txtHSaleReturnAC.getTag()));

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

        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        mrTemp.setInstruction(DmShared.xNull(txtInstructions.getText()));
        mrTemp.setRegionalInstruction(DmShared.xNull(txtRegionalInstructions.getText()));

        List<GrdMedicineDepartmentInfo_Mdl> OBJListMed = new ArrayList<>();
        OBJListMed.addAll(grdMedDetails.getGrid().getStore().getAll());
        mrTemp.getGrdMed_Mdl().addAll(OBJListMed);

        List<GrdApprovedVendors_Mdl> lstApprovedVendors = new ArrayList<>();
        lstApprovedVendors.addAll(gridApprovedVendors.getGrid().getStore().getAll());
        mrTemp.getGrdApprovedVendors_Mdls().addAll(lstApprovedVendors);

        List<GrdAlternativeItems_Mdl> lstAlternativeItems = new ArrayList<>();
        lstAlternativeItems.addAll(gridAlternativeItems.getGrid().getStore().getAll());
        mrTemp.getGrdAlternativeItems_Mdls().addAll(lstAlternativeItems);

        List<GrdComposition_Mdl> lstCompositionDetails = new ArrayList<>();
        lstCompositionDetails.addAll(grdCompositionDetails.getGrid().getStore().getAll());
        mrTemp.getGrdComposition_Mdl().addAll(lstCompositionDetails);

        List<GrdRestrictedDept_Mdl> lstGrdRestrictedDept = new ArrayList<>();
        lstGrdRestrictedDept.addAll(gridRestrictedDept.getGrid().getStore().getAll());
        mrTemp.getGrdRestrictedDept_Mdls().addAll(lstGrdRestrictedDept);

        List<GrdSoundLookAlike_Mdl> lstGrdSoundLookAlike = new ArrayList<>();
        lstGrdSoundLookAlike.addAll(gridSoundLookAlike.getGrid().getStore().getAll());
        mrTemp.getGrdSoundLookAlike_Mdls().addAll(lstGrdSoundLookAlike);

        List<GrdSchemeDetails_Mdl> lstSchemeDetails = new ArrayList<>();
        lstSchemeDetails.addAll(grdSchemeDetail.getGrid().getStore().getAll());
        mrTemp.getLstSchemeDetails().addAll(lstSchemeDetails);

        mrTemp.setPrvPurchaseUnitSkuCode(DmShared.xNull(prvPurchaseUnit));
        mrTemp.setPurchaseUnitSkuCode(DmShared.xNull(txtHPurchaseUnitSKU.getTag()));
        mrTemp.setPurchaseUnitSkuName(DmShared.xNull(txtHPurchaseUnitSKU.getText()));
        mrTemp.setPrvSaleUnitSkuCode(DmShared.xNull(prvSaleUnit));
        mrTemp.setSaleUnitSkuCode(DmShared.xNull(txtHSaleUnitSKU.getTag()));
        mrTemp.setSaleUnitSkuName(DmShared.xNull(txtHSaleUnitSKU.getText()));

        mrTemp.setIsSaleRateEditableCode(DmShared.xNull(txtHIsSaleRateEditable.getTag()));
        mrTemp.setIsSaleRateEditableName(DmShared.xNull(txtHIsSaleRateEditable.getText()));
        mrTemp.setIsIncludeFreeQtyInSaleRateCode(DmShared.xNull(txtHIsIncludeFreeQtyInSaleRate.getTag()));
        mrTemp.setIsIncludeFreeQtyInSaleRateName(DmShared.xNull(txtHIsIncludeFreeQtyInSaleRate.getText()));

        mrTemp.setConversionRatePurchase(DmShared.xNull(txtConversionRatePurchase.getText()));
        mrTemp.setConversionRateSale(DmShared.xNull(txtConversionRateSale.getText()));
        mrTemp.setPrvConversionRatePurchase(DmShared.xNull(prvConversionRatePurchase));
        mrTemp.setPrvConversionRateSale(DmShared.xNull(prvConversionRateSale));
        mrTemp.setAllowNegetiveStockCode(DmShared.xNull(txtHAllowNegetiveStock.getTag()));
        mrTemp.setAllowNegetiveStockName(DmShared.xNull(txtHAllowNegetiveStock.getText()));
        mrTemp.setBarcodeActiveTag(DmShared.xNull(txtHIsBarcodeActive.getTag()));
        mrTemp.setSubCategoryCode(DmShared.xNull(txtHSubCategory.getTag()));

        return mrTemp;
    }

    private void iniDefault(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultVal(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtDisplayName).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    isInventoryAccountingActive = defaultValue.isInventoryAccountingActive();
                    flag_Name_Editable = defaultValue.isEdit_NameIn_Inventory_ItemMAst();
                    manageDisplay(false);
                    swi.setWidget(MedicineMaster.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtDisplayName).show();
                }
            }
        });
    }

    private void topControlSave() {
        MedicineMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            MedicineMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtDisplayName).show();
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
                new DmAlertMessageBox(errorMessage, txtHCategory).show();
                break;
            case 220:
                new DmAlertMessageBox(errorMessage, txtHMaterialCategory).show();
                break;
            case 230:
                new DmAlertMessageBox(errorMessage, txtHItemModel).show();
                break;
            case 240:
                new DmAlertMessageBox(errorMessage, txtHItemStrength).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHCompany).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHMedGroup).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHConsignmentItem).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHBestBefore).show();
                break;
            case 710:
                new DmAlertMessageBox(errorMessage, txtHIsCommonItem).show();
                break;
            case 720:
                new DmAlertMessageBox(errorMessage, txtHIsDiscountAllow).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHLifeSaving).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHNarcotics).show();
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
            case 5000:
                new DmAlertMessageBox(errorMessage, txtHStatus).show();
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
            case 8500:
                new DmAlertMessageBox(errorMessage, txtConversionRatePurchase).show();
                break;
            case 9500:
                new DmAlertMessageBox(errorMessage, txtConversionRateSale).show();
                break;
            case 9501:
                new DmAlertMessageBox(errorMessage, txtHHSN).show();
                break;
            case 9502:
                new DmAlertMessageBox(errorMessage, txtHITSOnPurchase).show();
                break;
            case 9503:
                new DmAlertMessageBox(errorMessage, txtHITSOnSale).show();
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
            case 15000:
                new DmAlertMessageBox(errorMessage, txtHAllowNegetiveStock).show();
                break;
            case 16000:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;
            case 17000:
                new DmAlertMessageBox(errorMessage, txtPackSize).show();
                break;
            case 17100:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
                break;
            case 17200:
                new DmAlertMessageBox(errorMessage, txtSaleRate).show();
                break;
            case 17300:
                new DmAlertMessageBox(errorMessage, txtPurchaseRate).show();
                break;
            case 17400:
                new DmAlertMessageBox(errorMessage, txtSchemeOnQty).show();
                break;
            case 17500:
                new DmAlertMessageBox(errorMessage, txtSchemeFreeQty).show();
                break;
            case 17600:
                new DmAlertMessageBox(errorMessage, txtPurchaseDisPer).show();
                break;
            case 17700:
                new DmAlertMessageBox(errorMessage, txtSaleDisPer).show();
                break;
            case 17800:
                new DmAlertMessageBox(errorMessage, txtHPoReq).show();
                break;
            case 17900:
                new DmAlertMessageBox(errorMessage, txtHIndentReq).show();
                break;
            case 17920:
                new DmAlertMessageBox(errorMessage, txtHMarginRateFix).show();
                break;
            case 18000:
                new DmAlertMessageBox(errorMessage, txtHQuotationReq).show();
                break;
            case 19000:
                new DmAlertMessageBox(errorMessage, txtTaxPerc).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtDisplayName).show();
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
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<MedicineMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MedicineMaster_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<MedicineMaster_Mdl> result) {
        MedicineMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtName.setText(data.getSharedKeyFieldName());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            txtManualCode.setText(data.getManualCode());

            txtHStatus.setTag(data.getStatusTag());
            txtHStatus.setText(data.getStatusText());
            txtHCategory.setTag(data.getCategoryTag());
            txtHCategory.setText(data.getCategoryText());
            txtHCompany.setTag(data.getCompanyTag());
            txtHCompany.setText(data.getCompanyText());
            txtHCompany.setTag2nd(data.getCompanyShortName());
            txtHMarginRateFix.setTag(data.getMarginRateTag());
            txtHMarginRateFix.setText(data.getMarginRateText());
            txtHMaterialCategory.setText(data.getMaterialCategoryText());
            txtHMaterialCategory.setTag(data.getMaterialCategoryTag());

            txtHItemModel.setText(data.getModelName());
            txtHItemModel.setTag(data.getModel());
            txtHItemStrength.setText(data.getStrengthName());
            txtHItemStrength.setTag(data.getStrength());

            txtPriority.setText(DmShared.iNull(data.getPriority()).toString());

            txtHManufacturer.setTag(data.getManufacturerCode());
            txtHManufacturer.setText(data.getManufacturerName());

            txtHDivision.setTag(data.getDivisionTag());
            txtHDivision.setText(data.getDivisionText());

            purchaseDecimalPlaces = data.getPurchaseDecimalPlaces();

            txtMRP.setText(DmClient.vNull(data.getMrp(), "0.00"));
            txtSaleRate.setText(DmClient.vNull(data.getSaleRate(), "0.00"));
            txtPurchaseRate.setText(DmClient.vNull(data.getPurchaseRate(), "0.00"));
            txtSchemeFreeQty.setText(DmClient.vNull(data.getSchemeFreeQty(), DmShared.getNumberFormat(DmShared.iNull(purchaseDecimalPlaces).intValue())));
            txtSchemeOnQty.setText(DmClient.vNull(data.getSchemeOnQty(), DmShared.getNumberFormat(DmShared.iNull(purchaseDecimalPlaces).intValue())));
            txtPurchaseDisPer.setText(DmClient.vNull(data.getPurchaseDisPer(), "0.00"));
            txtSaleDisPer.setText(DmClient.vNull(data.getSaleDisPer(), "0.00"));
            txtTaxPerc.setText(DmShared.xNull(data.getTaxPerc()));

            txtHMedGroup.setTag(data.getMedGroupTag());
            txtHMedGroup.setText(data.getMedGroupText());
            txtResGroup.setTag(data.getRestrictGroupTag());
            txtResGroup.setText(data.getRestrictGroupText());
            txtHSKU.setTag(data.getSkuTag());
            txtHSKU.setText(data.getSkuText());
            txtDisplayName.setText(DmShared.xNull(data.getDisplayName()));
            previousSelectedSKU = DmShared.xNull(data.getSkuTag());
            prvSkuTag = DmShared.xNull(data.getSkuTag());
            prvSaleUnit = DmShared.xNull(data.getSaleUnitSkuCode());
            prvPurchaseUnit = DmShared.xNull(data.getPurchaseUnitSkuCode());

            txtHSKU.setTag2nd(data.getIsSkuEdit());
            txtPackSize.setText(data.getPackSize());

            txtHBestBefore.setText(data.getBestBeforeText());
            txtHBestBefore.setTag(data.getBestBeforeTag());
            txtHLifeSaving.setText(data.getLifeSavingText());
            txtHLifeSaving.setTag(data.getLifeSavingTag());
            txtHHighValue.setText(data.getHighValueText());
            txtHHighValue.setTag(data.getHighValueTag());
            txtHFlowThrough.setText(data.getFlowThroughText());
            txtHFlowThrough.setTag(data.getFlowThroughTag());
            txtHNarcotics.setText(data.getNarcoticsText());
            txtHNarcotics.setTag(data.getNarcoticsTag());

            txtHPoReq.setText(data.getPoReqText());
            txtHPoReq.setTag(data.getPoReqTag());
            txtHIndentReq.setText(data.getIndentReqText());
            txtHIndentReq.setTag(data.getIndentReqTag());
          
            txtHQuotationReq.setText(data.getQuotationReqText());
            txtHQuotationReq.setTag(data.getQuotationReqTag());

            txtHConsignmentItem.setText(data.getConsignmentText());
            txtHConsignmentItem.setTag(data.getConsignmentTag());
            txtHReturnable.setText(data.getReturnableItemText());
            txtHReturnable.setTag(data.getReturnableItemTag());
            txtHBilledItem.setText(data.getBilledItemText());
            txtHBilledItem.setTag(data.getBilledItemTag());
            txtHIsSaleRateEditable.setTag(DmShared.xNull(data.getIsSaleRateEditableCode()));
            txtHIsSaleRateEditable.setText(DmShared.xNull(data.getIsSaleRateEditableName()));
            txtHIsIncludeFreeQtyInSaleRate.setTag(DmShared.xNull(data.getIsIncludeFreeQtyInSaleRateCode()));
            txtHIsIncludeFreeQtyInSaleRate.setText(DmShared.xNull(data.getIsIncludeFreeQtyInSaleRateName()));
            txtHIsDiscountAllow.setTag(DmShared.xNull(data.getIsDiscountAllowTag()));
            txtHIsDiscountAllow.setText(DmShared.xNull(data.getIsDiscountAllowText()));
            txtHIsCommonItem.setTag(DmShared.xNull(data.getIsCommonItemTag()));
            txtHIsCommonItem.setText(DmShared.xNull(data.getIsCommonItemText()));
            txtInstructions.setText(DmShared.xNull(data.getInstruction()));
            txtRegionalInstructions.setText(DmShared.xNull(data.getRegionalInstruction()));
            txtHIsBarcodeActive.setText(data.getBarcodeActiveText());
            txtHIsBarcodeActive.setTag(data.getBarcodeActiveTag());

            if (!DmShared.xNull(txtHSKU.getText()).equalsIgnoreCase("")) {
                lblPurchaseConvertionRate.setText(DmShared.xNull(txtHSKU.getText()));
                lblSaleConvertionRate.setText(DmShared.xNull(txtHSKU.getText()));
            } else {
                lblPurchaseConvertionRate.setText("");
                lblSaleConvertionRate.setText("");
            }
            prvConversionRatePurchase = DmShared.xNull(data.getConversionRatePurchase());
            prvConversionRateSale = DmShared.xNull(data.getConversionRateSale());
            txtHPurchaseUnitSKU.setText(DmShared.xNull(data.getPurchaseUnitSkuName()));
            lblPurchaseUnitName.setText(DmShared.xNull(data.getPurchaseUnitSkuName()));
            lblMRPUnitName.setText(DmShared.xNull(data.getPurchaseUnitSkuName()));
            lblPurchaseRateUnitName.setText(DmShared.xNull(data.getPurchaseUnitSkuName()));
            lblSaleRateUnitName.setText(DmShared.xNull(data.getPurchaseUnitSkuName()));
            txtHPurchaseUnitSKU.setTag(DmShared.xNull(data.getPurchaseUnitSkuCode()));
            txtHSaleUnitSKU.setText(DmShared.xNull(data.getSaleUnitSkuName()));
            txtHSaleUnitSKU.setTag(DmShared.xNull(data.getSaleUnitSkuCode()));
            txtConversionRatePurchase.setText(DmShared.xNull(data.getConversionRatePurchase()));
            txtConversionRateSale.setText(DmShared.xNull(data.getConversionRateSale()));

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

            grdMedDetails.getGrid().getStore().clear();
            grdMedDetails.getGrid().getStore().addAll(data.getGrdMed_Mdl());

            gridAlternativeItems.getGrid().getStore().clear();
            gridAlternativeItems.getGrid().getStore().addAll(data.getGrdAlternativeItems_Mdls());

            gridSoundLookAlike.getGrid().getStore().clear();
            gridSoundLookAlike.getGrid().getStore().addAll(data.getGrdSoundLookAlike_Mdls());

            gridRestrictedDept.getGrid().getStore().clear();
            gridRestrictedDept.getGrid().getStore().addAll(data.getGrdRestrictedDept_Mdls());

            gridApprovedVendors.getGrid().getStore().clear();
            gridApprovedVendors.getGrid().getStore().addAll(data.getGrdApprovedVendors_Mdls());

            grdCompositionDetails.getGrid().getStore().clear();
            grdCompositionDetails.getGrid().getStore().addAll(data.getGrdComposition_Mdl());

            grdSchemeDetail.getGrid().getStore().clear();
            grdSchemeDetail.getGrid().getStore().addAll(data.getLstSchemeDetails());

            txtHHSN.setTag(DmShared.xNull(data.getHsnCode()));
            txtHHSN.setText(DmShared.xNull(data.getHsnName()));
            txtItemGSTNature.setTag(DmShared.xNull(data.getGstNatureTag()));
            txtItemGSTNature.setText(DmShared.xNull(data.getGstNatureText()));

            txtHITSOnPurchase.setTag(DmShared.xNull(data.getIts_Code_Purchase()));
            txtHITSOnPurchase.setText(DmShared.xNull(data.getItsPurName()));
            txtHITSOnPurchase.setTag2nd(DmShared.vNull(data.getIts_Tax_Purchase()).toString());
            txtITSOnPurchaseReturn.setTag(DmShared.xNull(data.getIts_Code_PurchaseRet()));
            txtITSOnPurchaseReturn.setText(DmShared.xNull(data.getItsPurRetName()));
            txtHITSOnSale.setTag(DmShared.xNull(data.getIts_Code_Sales()));
            txtHITSOnSale.setText(DmShared.xNull(data.getItsSaleName()));
            txtHITSOnSale.setTag2nd(DmShared.vNull(data.getIts_Tax_Sales()).toString());
            txtITSOnSaleReturn.setTag(DmShared.xNull(data.getIts_Code_SalesRet()));
            txtITSOnSaleReturn.setText(DmShared.xNull(data.getItsSaleReturnName()));
            txtHPurchaseAC.setTag(DmShared.xNull(data.getItsPurAcCode()));
            txtHPurchaseAC.setText(DmShared.xNull(data.getItsPurAcName()));
            txtHPurchaseReturnAC.setTag(DmShared.xNull(data.getItsPurRetAcCode()));
            txtHPurchaseReturnAC.setText(DmShared.xNull(data.getItsPurRetAcName()));
            txtHSaleAC.setTag(DmShared.xNull(data.getItsSaleAcCode()));
            txtHSaleAC.setText(DmShared.xNull(data.getItsSaleAcName()));
            txtHSaleReturnAC.setTag(DmShared.xNull(data.getItsSaleReturnAcCode()));
            txtHSaleReturnAC.setText(DmShared.xNull(data.getItsSaleReturnAcName()));
            txtHSubCategory.setText(DmShared.xNull(data.getSubCategoryName()));
            txtHSubCategory.setTag(DmShared.xNull(data.getSubCategoryCode()));

            isItemUsed = DmShared.xNull(data.getIsItemUsed());
            txtConversionRatePurchase.setTag2nd(DmShared.xNull(data.getAllowDecimalPlaces()));
            txtConversionRateSale.setTag2nd(DmShared.xNull(data.getAllowDecimalPlaces()));
            txtHAllowNegetiveStock.setText(DmShared.xNull(data.getAllowNegetiveStockName()));
            txtHAllowNegetiveStock.setTag(DmShared.xNull(data.getAllowNegetiveStockCode()));
            isNegativeStock = data.getIsNegativeStock();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        manageDisplay(false);
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

    @Override
    public void onBlur(BlurEvent event) {
        if ((event.getSource() == txtConversionRatePurchase)) {
            if (!DmShared.xNull(txtConversionRatePurchase.getText()).equalsIgnoreCase("")) {
                calculate();
            }
        } else if ((event.getSource() == txtConversionRateSale)) {
            if (!DmShared.xNull(txtConversionRateSale.getText()).equalsIgnoreCase("")) {
                calculate();
            }
        } else if ((event.getSource() == txtDisplayName)) {
            if (!DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                setNameText();
            }
        } else if ((event.getSource() == txtPackSize)) {
            if (!DmShared.xNull(txtPackSize.getText()).equalsIgnoreCase("")) {
                setNameText();
            }
        } else if ((event.getSource() == txtMRP)) {
            if (!DmShared.xNull(txtMRP.getText()).equalsIgnoreCase("")) {
                txtMRP.setText(DmClient.vNull(txtMRP.getText(), "0.00"));
            }
        } else if ((event.getSource() == txtSaleRate)) {
            if (!DmShared.xNull(txtSaleRate.getText()).equalsIgnoreCase("")) {
                txtSaleRate.setText(DmClient.vNull(txtSaleRate.getText(), "0.00"));
            }
        } else if ((event.getSource() == txtPurchaseRate)) {
            if (!DmShared.xNull(txtPurchaseRate.getText()).equalsIgnoreCase("")) {
                txtPurchaseRate.setText(DmClient.vNull(txtPurchaseRate.getText(), "0.00"));
            }
        } else if ((event.getSource() == txtSchemeFreeQty)) {
            if (!DmShared.xNull(txtSchemeFreeQty.getText()).equalsIgnoreCase("")) {
                txtSchemeFreeQty.setText(DmClient.vNull(txtSchemeFreeQty.getText(), DmShared.getNumberFormat(DmShared.iNull(purchaseDecimalPlaces).intValue())));
            }
        } else if ((event.getSource() == txtSchemeOnQty)) {
            if (!DmShared.xNull(txtSchemeOnQty.getText()).equalsIgnoreCase("")) {
                txtSchemeOnQty.setText(DmClient.vNull(txtSchemeOnQty.getText(), DmShared.getNumberFormat(DmShared.iNull(purchaseDecimalPlaces).intValue())));
            }
        } else if ((event.getSource() == txtSchemeOnQty)) {
            if (!DmShared.xNull(txtSchemeOnQty.getText()).equalsIgnoreCase("")) {
                txtSchemeOnQty.setText(DmClient.vNull(txtSchemeOnQty.getText(), DmShared.getNumberFormat(DmShared.iNull(purchaseDecimalPlaces).intValue())));
            }
        } else if ((event.getSource() == txtPurchaseDisPer)) {
            if (!DmShared.xNull(txtPurchaseDisPer.getText()).equalsIgnoreCase("")) {
                txtPurchaseDisPer.setText(DmClient.vNull(txtPurchaseDisPer.getText(), "0.00"));
            }
        } else if ((event.getSource() == txtSaleDisPer)) {
            if (!DmShared.xNull(txtSaleDisPer.getText()).equalsIgnoreCase("")) {
                txtSaleDisPer.setText(DmClient.vNull(txtSaleDisPer.getText(), "0.00"));
            }
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtHStatus.setTag("1");
        txtHStatus.setText("Active");
        txtHBestBefore.setTag("0");
        txtHBestBefore.setText("No");
        txtHLifeSaving.setTag("0");
        txtHLifeSaving.setText("No");
        txtHHighValue.setTag("0");
        txtHHighValue.setText("No");
        txtHFlowThrough.setTag("0");
        txtHFlowThrough.setText("No");
        txtHNarcotics.setTag("0");
        txtHNarcotics.setText("No");
        txtHConsignmentItem.setTag("0");
        txtHConsignmentItem.setText("No");
        txtHReturnable.setTag("1");
        txtHReturnable.setText("Yes");
        txtHBilledItem.setTag("0");
        txtHBilledItem.setText("No");
        txtHIsSaleRateEditable.setTag("0");
        txtHIsSaleRateEditable.setText("No");
        txtHIsIncludeFreeQtyInSaleRate.setTag("1");
        txtHIsIncludeFreeQtyInSaleRate.setText("Yes");
        txtHAllowNegetiveStock.setTag("0");
        txtHAllowNegetiveStock.setText("No");
        txtHIsDiscountAllow.setTag("1");
        txtHIsDiscountAllow.setText("Yes");
        txtHIsCommonItem.setTag("0");
        txtHIsCommonItem.setText("No");
        txtManualCode.setEnabled(false);
        txtResGroup.setText("");
        txtResGroup.setTag("");
        txtResGroup.setTag2nd("");
        txtHPoReq.setTag("0");
        txtHPoReq.setText("No");
        txtHIndentReq.setTag("0");
        txtHIndentReq.setText("No");
      
        txtHQuotationReq.setTag("0");
        txtHQuotationReq.setText("No");
        txtHIsBarcodeActive.setTag("0");
        txtHIsBarcodeActive.setText("No");

        grdMedDetails.getGrid().getStore().add(new GrdMedicineDepartmentInfo_Mdl());

        gridApprovedVendors.getGrid().getStore().add(new GrdApprovedVendors_Mdl());

        gridAlternativeItems.getGrid().getStore().add(new GrdAlternativeItems_Mdl());

        gridRestrictedDept.getGrid().getStore().add(new GrdRestrictedDept_Mdl());

        gridSoundLookAlike.getGrid().getStore().add(new GrdSoundLookAlike_Mdl());

        grdSchemeDetail.getGrid().getStore().add(new GrdSchemeDetails_Mdl());

        grdCompositionDetails.getGrid().getStore().add(new GrdComposition_Mdl());
    }

    public void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        lbldisplay.setText("");
        grdMedDetails.getGrid().getStore().clear();
        gridApprovedVendors.getGrid().getStore().clear();
        gridAlternativeItems.getGrid().getStore().clear();
        gridRestrictedDept.getGrid().getStore().clear();
        gridSoundLookAlike.getGrid().getStore().clear();
        grdCompositionDetails.getGrid().getStore().clear();
        grdSchemeDetail.getGrid().getStore().clear();
        isItemUsed = "";
        previousSelectedSKU = "";
        purchaseDecimalPlaces = "";
        prvSkuTag = "";
        prvPurchaseUnit = "";
        prvSaleUnit = "";
        prvConversionRatePurchase = "";
        prvConversionRateSale = "";
        lblPurchaseConvertionRate.setText("");
        lblSaleConvertionRate.setText("");
        lblPurchaseUnitName.setText("");
        lblMRPUnitName.setText("");
        lblPurchaseRateUnitName.setText("");
        lblSaleRateUnitName.setText("");
        isNegativeStock = false;
    }

    private void calculate() {
        if (!DmShared.xNull(txtConversionRatePurchase.getText()).equalsIgnoreCase("")) {
            txtConversionRatePurchase.setText(DmClient.vNull(txtConversionRatePurchase.getText(), DmShared.getNumberFormat(DmShared.iNull(txtConversionRatePurchase.getTag2nd()).intValue())));
            setNameText();
        }
        if (!DmShared.xNull(txtConversionRateSale.getText()).equalsIgnoreCase("")) {
            txtConversionRateSale.setText(DmClient.vNull(txtConversionRateSale.getText(), DmShared.getNumberFormat(DmShared.iNull(txtConversionRateSale.getTag2nd()).intValue())));
        }
    }

    private void handlerEvents() {
        txtConversionRatePurchase.addBlurHandler(this);
        txtConversionRateSale.addBlurHandler(this);
        txtDisplayName.addBlurHandler(this);
        txtPackSize.addBlurHandler(this);
        txtMRP.addBlurHandler(this);
        txtSaleRate.addBlurHandler(this);
        txtPurchaseRate.addBlurHandler(this);
        txtSchemeFreeQty.addBlurHandler(this);
        txtSchemeOnQty.addBlurHandler(this);
        txtPurchaseDisPer.addBlurHandler(this);
        txtSaleDisPer.addBlurHandler(this);
    }

    private void setNameText() {
        String medicineName = DmShared.xNull(txtDisplayName.getText());

        if (defaultValue != null) {
            if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("U")) {
                medicineName = medicineName.toUpperCase();
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("L")) {
                medicineName = medicineName.toLowerCase();
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("S")) {
                medicineName = DmShared.sentenceCase(medicineName);
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("P")) {
                medicineName = DmShared.properCase(medicineName);
            }
        }
        txtDisplayName.setText(medicineName);
        medicineName = "";
        if (!flag_Name_Editable) {
            if (!DmShared.xNull(txtHMaterialCategory.getTag()).equalsIgnoreCase("")) {
                medicineName += DmShared.xNull(txtHMaterialCategory.getText()).trim();
            }
            if (!DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtDisplayName.getText()).trim();
            }
            if (!DmShared.xNull(txtHItemStrength.getText()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHItemStrength.getText()).trim();
            }
            if (!DmShared.xNull(txtHItemModel.getText()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHItemModel.getText()).trim();
            }
        } else {
            if (!DmShared.xNull(txtDisplayName.getText()).equalsIgnoreCase("")) {
                medicineName += DmShared.xNull(txtDisplayName.getText()).trim();
            }
            if (!DmShared.xNull(txtHCategory.getText()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHCategory.getText()).trim();
            }
            if (!DmShared.xNull(txtHCompany.getText()).equalsIgnoreCase("")) {
                medicineName += (DmShared.xNull(medicineName).trim() != "" ? "-" : "") + DmShared.xNull(txtHCompany.getText()).trim();
            }
        }

        if (defaultValue != null) {
            if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("U")) {
                medicineName = medicineName.toUpperCase();
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("L")) {
                medicineName = medicineName.toLowerCase();
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("S")) {
                medicineName = DmShared.sentenceCase(medicineName);
            } else if (DmShared.xNull(defaultValue.getMedicineNameAlphabetCase()).equalsIgnoreCase("P")) {
                medicineName = DmShared.properCase(medicineName);
            }
        }

        txtName.setText(medicineName);
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
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtDisplayName).show();
            }

        } else if (event.getSource().equals(mnuExportCard)) {
            new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        exportJson();
                    }
                }
            }).show();
        } else if (event.getSource().equals(mnuManageFile)) {
            fileManager.show(topControl.getMode().trim().equalsIgnoreCase("New") ? "" : topControl.getDocId(), "1", isAttachFiles, isDeleteFiles);
        }
    }

    private void exportJson() {
        srvAsync.invokeRPC().exportJson(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
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

    private void fillHSNDetail(DmResult<HSNDetail_Mdl> result) {
        HSNDetail_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            txtHITSOnPurchase.setText(DmShared.xNull(data.getItsPurName()));
            txtHITSOnPurchase.setTag(DmShared.xNull(data.getIts_Code_Purchase()));
            txtHITSOnPurchase.setTag2nd(DmShared.vNull(data.getIts_Tax_Purchase()).toString());
            txtITSOnPurchaseReturn.setText(DmShared.xNull(data.getItsPurRetName()));
            txtITSOnPurchaseReturn.setTag(DmShared.xNull(data.getIts_Code_PurchaseRet()));
            txtHITSOnSale.setText(DmShared.xNull(data.getItsSaleName()));
            txtHITSOnSale.setTag(DmShared.xNull(data.getIts_Code_Sales()));
            txtHITSOnSale.setTag2nd(DmShared.vNull(data.getIts_Tax_Sales()).toString());
            txtTaxPerc.setText(DmShared.vNull(data.getIts_Tax_Sales()).toString());
            txtTaxPerc.setTag(DmShared.vNull(data.getIts_Tax_Sales()).toString());
            txtITSOnSaleReturn.setText(DmShared.xNull(data.getItsSaleReturnName()));
            txtITSOnSaleReturn.setTag(DmShared.xNull(data.getIts_Code_SalesRet()));
            txtHPurchaseAC.setText(DmShared.xNull(data.getItsPurAcName()));
            txtHPurchaseAC.setTag(DmShared.xNull(data.getItsPurAcCode()));
            txtHSaleAC.setText(DmShared.xNull(data.getItsSaleAcName()));
            txtHSaleAC.setTag(DmShared.xNull(data.getItsSaleAcCode()));
            txtHPurchaseReturnAC.setText(DmShared.xNull(data.getItsPurRetAcName()));
            txtHPurchaseReturnAC.setTag(DmShared.xNull(data.getItsPurRetAcCode()));
            txtHSaleReturnAC.setText(DmShared.xNull(data.getItsSaleReturnAcName()));
            txtHSaleReturnAC.setTag(DmShared.xNull(data.getItsSaleReturnAcCode()));
        }
    }
}
