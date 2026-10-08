package aarogya.inventory.gstServiceMaster.client;

import aarogya.inventory.gstServiceMaster.shared.Find_Mdl;
import aarogya.inventory.gstServiceMaster.shared.GstServiceMaster_Vld;
import aarogya.inventory.gstServiceMaster.shared.GstServiceMaster_mdl;
import aarogya.inventory.gstServiceMaster.shared.Sku_Mdl;
import aarogya.inventory.gstServiceMaster.shared.TxtHCategory_Mdl;
import aarogya.inventory.gstServiceMaster.shared.TxtHHSN_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import java.util.ArrayList;
import java.util.List;
import aarogya.inventory.gstServiceMaster.shared.GrdItsRateSlab_Mdl;
import aarogya.inventory.gstServiceMaster.shared.GstServiceMaster_Srv;
import aarogya.inventory.gstServiceMaster.shared.GstServiceMaster_SrvAsync;

public class GstServiceMaster extends GstServiceMaster_Dsg implements BlurHandler, SelectionHandler<Item>, AttachEvent.Handler {

    ServiceInvoker<GstServiceMaster_SrvAsync> srvAsync;
    String previousSelectedSKU = "", isItemUsed = "";
    final ManageSite manageSite = new ManageSite();
    private final ItsRateSlabGridEdit<GrdItsRateSlab_Mdl, GridItsRateSlab_PrpAcs> itsRateSlabGridEdit = new ItsRateSlabGridEdit(grdItsRateSlab);
    GstServiceMaster_mdl defaultValue = new GstServiceMaster_mdl();

    public GstServiceMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(GstServiceMaster_Srv.class), GstServiceMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        mnuManageSite.addSelectionHandler(this);
        manageDisplay(false);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(true);
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                onEdit();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GstServiceMaster.this);
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

        txtItemName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionItem(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtItemName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtItemName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtItemName).show();
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
                }
            }
        });
        txtHGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGroup(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGroup).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {

                }
            }
        });
        txtHSKU.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<Sku_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSKU(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Sku_Mdl>>>() {

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
                }
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
                    txtHGSTNature.setTag(DmShared.xNull(t1Row.getItemGSTNatureTag()));
                    txtHGSTNature.setText(DmShared.xNull(t1Row.getItemGSTNatureText()));
                } else {
                    txtHGSTNature.setTag("");
                    txtHGSTNature.setText("");
                }
            }
        });

        txtHActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
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
        });

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHMapWithField.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMapWithField(helper, blnToEndOfFile, DmShared.xNull(topControl.getDocId()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMapWithField).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMapWithField.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMapWithField).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHTaxBasedOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTaxBasedOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTaxBasedOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTaxBasedOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTaxBasedOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHTaxBasedOn.getTag2nd()).equalsIgnoreCase(DmShared.xNull(txtHTaxBasedOn.getTag()))) {
                    txtHTaxBasedOn.setTag2nd(txtHTaxBasedOn.getTag());
                    grdItsRateSlab.getGrid().getStore().clear();
                    txtHGstType.setText("");
                    txtHGstType.setTag("");
                    manageITSContainer();
                }

            }
        });
        txtHGSTNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHGSTNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGSTNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGSTNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGSTNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHValuationType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHValuationType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHValuationType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHValuationType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHValuationType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

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

        txtHSubCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSubCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSubCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSubCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                // manageTxtHBestBefore();
            }
        });
        txtHPurchase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("PR", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                //manageTxtHBestBefore();
            }

        });

        txtHPurchaseReturn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("PR", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseReturn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseReturn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseReturn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }

        });
        txtHSales.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("SL", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSales).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSales.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSales).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                //manageTxtHBestBefore();
            }
        });

        txtHSalesReturn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("SL", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSalesReturn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSalesReturn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSalesReturn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                //manageTxtHBestBefore();
            }
        });
        txtHPayableAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSubCode("", helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPayableAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPayableAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPayableAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

    }

    private void manageITSContainer() {
        flwMain.clear();

        if (DmShared.xNull(txtHTaxBasedOn.getTag()).equalsIgnoreCase("1")) {  //rate
            vl.add(grdItsRateSlab.getGrid());
            flwMain.add(flwGstsContainer);
            if (grdItsRateSlab.getGrid().getStore().size() == 0) {
                grdItsRateSlab.getGrid().getStore().add(new GrdItsRateSlab_Mdl());
            }
        } else if (DmShared.xNull(txtHTaxBasedOn.getTag()).equalsIgnoreCase("0")) { //fixed
            flwMain.clear();
            vl.get().remove(grdItsRateSlab.getGrid());
            vl.add(txtHGstType);
            flwMain.add(flwGstType);
        }

    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        previousSelectedSKU = "";
        isItemUsed = "";
    }

    private void manageDisplay(boolean flag) {
        txtHActive.setEnabled(flag);
        txtManualCode.setEnabled(false);
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtManualCode.setEnabled(flag);
            if (!DmShared.xNull(txtUniqueId.getText()).equalsIgnoreCase("")) {
                txtUniqueId.setEnabled(false);
            } else {
                txtUniqueId.setEnabled(true);
            }
            if (!DmShared.xNull(txtHMapWithField.getTag()).equalsIgnoreCase("")) {
                txtHMapWithField.setEnabled(false);
            } else {
                txtHType.setEnabled(true);
            }
            if (isItemUsed.equalsIgnoreCase("1")) {
                txtHSKU.setEnabled(false);
            } else {
                txtHSKU.setEnabled(true);
            }
        }
    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        txtHActive.setTag("1");
        txtHActive.setText("Active");

        txtHType.setTag("SV");
        txtHType.setText("Service");

        txtHCategory.setTag(defaultValue.getCategoryTag());
        txtHCategory.setText(defaultValue.getCategoryText());
        txtHGroup.setTag(defaultValue.getGroupTag());
        txtHGroup.setText(defaultValue.getGroupText());
        txtHHSN.setTag(DmShared.xNull(defaultValue.getHsn()));
        txtHHSN.setText(DmShared.xNull(defaultValue.getHsnName()));
        txtHGSTNature.setTag(DmShared.xNull(defaultValue.getGstNatureTag()));
        txtHGSTNature.setText(DmShared.xNull(defaultValue.getGstNatureText()));
        txtHSKU.setTag(defaultValue.getSkuTag());
        txtHSKU.setText(defaultValue.getSkuText());

        txtHTaxBasedOn.setTag("0");
        txtHTaxBasedOn.setText("Fixed");
        grdItsRateSlab.getGrid().getStore().clear();
        txtHGstType.setTag("");
        txtHGstType.setText("");
        manageITSContainer();

        txtItemName.setFocus(true);

    }

    private void topControlSave() {
        GstServiceMaster_mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            GstServiceMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtItemName).show();
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

    private GstServiceMaster_mdl topControlSaveInitialize() {
        GstServiceMaster_mdl mrTemp = new GstServiceMaster_mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setManualCode(DmShared.removeSpace(txtManualCode.getText()).trim());
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtItemName.getText()));
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));

        mrTemp.setCategoryText(DmShared.xNull(txtHCategory.getText()));
        mrTemp.setCategoryTag(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setGroupText(DmShared.xNull(txtHGroup.getText()));
        mrTemp.setGroupTag(DmShared.xNull(txtHGroup.getTag()));
        mrTemp.setGstNatureTag(DmShared.xNull(txtHGSTNature.getTag()));
        mrTemp.setHsn(DmShared.xNull(txtHHSN.getTag()));

        defaultValue.setCategoryText(DmShared.xNull(txtHCategory.getText()));
        defaultValue.setCategoryTag(DmShared.xNull(txtHCategory.getTag()));
        defaultValue.setGroupText(DmShared.xNull(txtHGroup.getText()));
        defaultValue.setGroupTag(DmShared.xNull(txtHGroup.getTag()));
        defaultValue.setGstNatureTag(DmShared.xNull(txtHGSTNature.getTag()));
        defaultValue.setGstNatureText(DmShared.xNull(txtHGSTNature.getText()));
        defaultValue.setHsn(DmShared.xNull(txtHHSN.getTag()));
        defaultValue.setHsnName(DmShared.xNull(txtHHSN.getText()));
        defaultValue.setSkuTag(DmShared.xNull(txtHSKU.getTag()));
        defaultValue.setSkuText(DmShared.xNull(txtHSKU.getText()));

        mrTemp.setSkuTag(DmShared.xNull(txtHSKU.getTag()));
        mrTemp.setPrvSkuTag(DmShared.xNull(previousSelectedSKU));

        mrTemp.setPurchaseUnitSkuCode(DmShared.xNull(txtHSKU.getTag()));
        mrTemp.setPurchaseUnitSkuName(DmShared.xNull(txtHSKU.getText()));
        mrTemp.setSaleUnitSkuCode(DmShared.xNull(txtHSKU.getTag()));
        mrTemp.setSaleUnitSkuName(DmShared.xNull(txtHSKU.getText()));

        mrTemp.setTypeName(DmShared.xNull(txtHType.getText()));
        mrTemp.setTypeManualCode(DmShared.xNull(txtHType.getTag()));
        mrTemp.setUniqueId(DmShared.xNull(txtUniqueId.getText()));
        mrTemp.setMapWithFieldTag(DmShared.xNull(txtHMapWithField.getTag()));
        mrTemp.setMapWithField(DmShared.xNull(txtHMapWithField.getText()));
        mrTemp.setServiceEvalTypeCode(DmShared.xNull(txtHValuationType.getTag()));
        mrTemp.setServiceEvalType(DmShared.xNull(txtHValuationType.getText()));
        mrTemp.setValue(DmShared.xNull(txtValue.getText()));
        mrTemp.setPrintSrNo(DmShared.xNull(txtPrintSrNo.getText()));
        mrTemp.setInventoryAcTag(DmShared.xNull(txtHSubCode.getTag()));
        mrTemp.setInventoryAcText(DmShared.xNull(txtHSubCode.getText()));
        mrTemp.setPurchaseAcTag(DmShared.xNull(txtHPurchase.getTag()));
        mrTemp.setPurchaseAcText(DmShared.xNull(txtHPurchase.getText()));
        mrTemp.setSalesAcTag(DmShared.xNull(txtHSales.getTag()));
        mrTemp.setSalesAcText(DmShared.xNull(txtHSales.getText()));

        mrTemp.setPurchaseReturnAcTag(DmShared.xNull(txtHPurchaseReturn.getTag()));
        mrTemp.setPurchaseReturnAcText(DmShared.xNull(txtHPurchaseReturn.getText()));
        mrTemp.setSalesReturnAcTag(DmShared.xNull(txtHSalesReturn.getTag()));
        mrTemp.setSalesReturnAcText(DmShared.xNull(txtHSalesReturn.getText()));

        mrTemp.setTypeTag(DmShared.xNull(txtHType.getTag()));
        mrTemp.setTypeName(DmShared.xNull(txtHType.getText()));
        mrTemp.setTaxBasedOnTag(DmShared.xNull(txtHTaxBasedOn.getTag()));
        mrTemp.setPayableAcTag(DmShared.xNull(txtHPayableAc.getTag()));
        mrTemp.setPayableAcText(DmShared.xNull(txtHPayableAc.getText()));

        List<GrdItsRateSlab_Mdl> lstGrdRateIts = new ArrayList<>();
        if (DmShared.xNull(txtHTaxBasedOn.getText()).equalsIgnoreCase("Fixed")
                || DmShared.xNull(txtHTaxBasedOn.getText()).equalsIgnoreCase("")) {
            GrdItsRateSlab_Mdl grdRateITS_Mdl = new GrdItsRateSlab_Mdl();
            grdRateITS_Mdl.setGstTypeCode(DmShared.xNull(txtHGstType.getTag()));
            lstGrdRateIts.add(grdRateITS_Mdl);
        } else {
            lstGrdRateIts.addAll(grdItsRateSlab.getGrid().getStore().getAll());
        }
        mrTemp.getGrdRateIts_Mdls().addAll(lstGrdRateIts);

        return mrTemp;
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<GstServiceMaster_mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GstServiceMaster_mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<GstServiceMaster_mdl> result) {
        GstServiceMaster_mdl data;

        if (result.isStatus()) {
            clear();
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            manageDisplay(false);

            txtItemName.setText(data.getSharedKeyFieldName());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            txtManualCode.setText(data.getManualCode());

            txtHActive.setTag(data.getActiveTag());
            txtHActive.setText(data.getActiveText());

            txtHCategory.setTag(data.getCategoryTag());
            txtHCategory.setText(data.getCategoryText());
            txtHGroup.setTag(data.getGroupTag());
            txtHGroup.setText(data.getGroupText());
            txtHHSN.setTag(DmShared.xNull(data.getHsn()));
            txtHHSN.setText(DmShared.xNull(data.getHsnName()));
            txtHGSTNature.setTag(DmShared.xNull(data.getGstNatureTag()));
            txtHGSTNature.setText(DmShared.xNull(data.getGstNatureText()));

            txtHSKU.setTag(data.getSkuTag());
            txtHSKU.setText(data.getSkuText());
            previousSelectedSKU = DmShared.xNull(data.getSkuTag());

            isItemUsed = DmShared.xNull(data.getIsItemUsed());

            txtHType.setText(data.getTypeName());
//            txtHType.setTag(data.getTypeTag());
            txtHType.setTag(data.getTypeManualCode());
            txtUniqueId.setText(DmShared.xNull(data.getUniqueId()));
            txtHMapWithField.setTag(data.getMapWithFieldTag());
            txtHMapWithField.setText(DmShared.xNull(data.getMapWithField()));
            txtHValuationType.setTag(DmShared.xNull(data.getServiceEvalTypeCode()));
            txtHValuationType.setText(DmShared.xNull(data.getServiceEvalType()));
            txtValue.setText(DmShared.xNull(data.getValue()));
            txtPrintSrNo.setText(DmShared.xNull(data.getPrintSrNo()));
            txtHSubCode.setTag(DmShared.xNull(data.getInventoryAcTag()));
            txtHSubCode.setText(DmShared.xNull(data.getInventoryAcText()));
            txtHPurchase.setTag(DmShared.xNull(data.getPurchaseAcTag()));
            txtHPurchase.setText(DmShared.xNull(data.getPurchaseAcText()));
            txtHSales.setTag(DmShared.xNull(data.getSalesAcTag()));
            txtHSales.setText(DmShared.xNull(data.getSalesAcText()));

            txtHPurchaseReturn.setTag(DmShared.xNull(data.getPurchaseReturnAcTag()));
            txtHPurchaseReturn.setText(DmShared.xNull(data.getPurchaseReturnAcText()));
            txtHSalesReturn.setTag(DmShared.xNull(data.getSalesReturnAcTag()));
            txtHSalesReturn.setText(DmShared.xNull(data.getSalesReturnAcText()));

            txtHTaxBasedOn.setTag(DmShared.xNull(data.getTaxBasedOnTag()));
            txtHTaxBasedOn.setTag2nd(DmShared.xNull(data.getTaxBasedOnTag()));
            txtHTaxBasedOn.setText(DmShared.xNull(data.getTaxBasedOn()));

            txtHPayableAc.setTag(DmShared.xNull(data.getPayableAcTag()));
            txtHPayableAc.setText(DmShared.xNull(data.getPayableAcText()));

            if (DmShared.xNull(data.getTaxBasedOnTag()).equalsIgnoreCase("0")
                    || DmShared.xNull(data.getTaxBasedOnTag()).equalsIgnoreCase("")) {
                for (GrdItsRateSlab_Mdl grdRateITS_Mdl : data.getGrdRateIts_Mdls()) {
                    txtHGstType.setTag(DmShared.xNull(grdRateITS_Mdl.getGstTypeCode()));
                    txtHGstType.setText(DmShared.xNull(grdRateITS_Mdl.getGstTypeName()));
                }
            } else {
                grdItsRateSlab.getGrid().getStore().clear();
                grdItsRateSlab.getGrid().getStore().addAll(data.getGrdRateIts_Mdls());
            }
            manageITSContainer();

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void onEdit() {
        manageDisplay(true);
        txtItemName.setFocus(true);
        manageITSContainer();
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

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 200:
                new DmAlertMessageBox(errorMessage, txtHCategory).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtItemName).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHGroup).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHSKU).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHActive).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHGstType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtItemName).show();
                break;
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {

        } else {
            manageSite.hide();
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
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuManageSite)) {
            if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                manageSite.show("itemSiteList", "code", topControl.getDocId(), "site_Code");
            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtItemName).show();
            }
        }
    }

    private void gridEvents() {
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

    private void showRateGrd(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            itsRateSlabGridEdit.show(currentRowIndex, false, false, false, this);
        } else {
            itsRateSlabGridEdit.show(currentRowIndex, true, true, true, this);
        }
    }

}
