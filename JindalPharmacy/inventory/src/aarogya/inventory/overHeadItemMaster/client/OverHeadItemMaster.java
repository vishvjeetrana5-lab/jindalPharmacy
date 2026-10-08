package aarogya.inventory.overHeadItemMaster.client;

import aarogya.inventory.overHeadItemMaster.shared.Find_Mdl;
import aarogya.inventory.overHeadItemMaster.shared.OverHeadItemMaster_Srv;
import aarogya.inventory.overHeadItemMaster.shared.OverHeadItemMaster_SrvAsync;
import aarogya.inventory.overHeadItemMaster.shared.OverHeadItemMaster_Vld;
import aarogya.inventory.overHeadItemMaster.shared.OverHeadItemMaster_mdl;
import aarogya.inventory.overHeadItemMaster.shared.Sku_Mdl;
import aarogya.inventory.overHeadItemMaster.shared.TxtHCategory_Mdl;
import aarogya.inventory.overHeadItemMaster.shared.TxtHHSN_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
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
import java.util.List;

public class OverHeadItemMaster extends OverHeadItemMaster_Dsg implements BlurHandler, SelectionHandler<Item>, AttachEvent.Handler {

    ServiceInvoker<OverHeadItemMaster_SrvAsync> srvAsync;
    String previousSelectedSKU = "", isItemUsed = "";
    final ManageSite manageSite = new ManageSite();

    OverHeadItemMaster_mdl defaultValue = new OverHeadItemMaster_mdl();

    public OverHeadItemMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        srvAsync = new ServiceInvoker<>(GWT.create(OverHeadItemMaster_Srv.class), OverHeadItemMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        mnuManageSite.addSelectionHandler(this);
        manageDisplay(false);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OverHeadItemMaster.this);
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
                    txtItemGSTNature.setTag(DmShared.xNull(t1Row.getItemGSTNatureTag()));
                    txtItemGSTNature.setText(DmShared.xNull(t1Row.getItemGSTNatureText()));
                } else {
                    txtItemGSTNature.setTag("");
                    txtItemGSTNature.setText("");
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
        txtItemGSTNature.setEnabled(false);
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtManualCode.setEnabled(flag);
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

        txtHCategory.setTag(defaultValue.getCategoryTag());
        txtHCategory.setText(defaultValue.getCategoryText());
        txtHGroup.setTag(defaultValue.getGroupTag());
        txtHGroup.setText(defaultValue.getGroupText());
        txtHHSN.setTag(DmShared.xNull(defaultValue.getHsn()));
        txtHHSN.setText(DmShared.xNull(defaultValue.getHsnName()));
        txtItemGSTNature.setTag(DmShared.xNull(defaultValue.getGstNatureTag()));
        txtItemGSTNature.setText(DmShared.xNull(defaultValue.getGstNatureText()));
        txtHSKU.setTag(defaultValue.getSkuTag());
        txtHSKU.setText(defaultValue.getSkuText());

        txtItemName.setFocus(true);
    }

    private void topControlSave() {
        OverHeadItemMaster_mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            OverHeadItemMaster_Vld.chkSave(mrTemp);

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

    private OverHeadItemMaster_mdl topControlSaveInitialize() {
        OverHeadItemMaster_mdl mrTemp = new OverHeadItemMaster_mdl();
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
        mrTemp.setGstNatureTag(DmShared.xNull(txtItemGSTNature.getTag()));
        mrTemp.setHsn(DmShared.xNull(txtHHSN.getTag()));

        defaultValue.setCategoryText(DmShared.xNull(txtHCategory.getText()));
        defaultValue.setCategoryTag(DmShared.xNull(txtHCategory.getTag()));
        defaultValue.setGroupText(DmShared.xNull(txtHGroup.getText()));
        defaultValue.setGroupTag(DmShared.xNull(txtHGroup.getTag()));
        defaultValue.setGstNatureTag(DmShared.xNull(txtItemGSTNature.getTag()));
        defaultValue.setGstNatureText(DmShared.xNull(txtItemGSTNature.getText()));
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

        return mrTemp;
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<OverHeadItemMaster_mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<OverHeadItemMaster_mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<OverHeadItemMaster_mdl> result) {
        OverHeadItemMaster_mdl data;

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
            txtItemGSTNature.setTag(DmShared.xNull(data.getGstNatureTag()));
            txtItemGSTNature.setText(DmShared.xNull(data.getGstNatureText()));

            txtHSKU.setTag(data.getSkuTag());
            txtHSKU.setText(data.getSkuText());
            previousSelectedSKU = DmShared.xNull(data.getSkuTag());

            isItemUsed = DmShared.xNull(data.getIsItemUsed());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void onEdit() {
        manageDisplay(true);
        txtItemName.setFocus(true);
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

}
