package dataman.utility.itemTaxStructure.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.utility.itemTaxStructure.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.event.shared.HandlerRegistration;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.HashMap;
import java.util.List;

public final class ItemTaxStructure extends ItemTaxStructure_Dsg implements AttachEvent.Handler, ClickHandler, BlurHandler, SelectionHandler<Item> {

    private final ServiceInvoker<ItemTaxStructure_SrvAsync> SrvAsync;
    private final ItemTaxStructureGridEdit<GrdItemTaxStructure_Mdl, GrdItemTaxStructure_PrpAcs> grdEdit = new ItemTaxStructureGridEdit(grdITS);
    private final ApplicableFrom applicableFrom = new ApplicableFrom();
    private HandlerRegistration clickRegistration;
    protected String applicableDtFrom = "", applicableDtTo = "";
    private final ExportData exportData = new ExportData();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtITSName.setFocus(true);
                manageDisplay();
                manageFlwApplicable(true);
                grdITS.getGrid().getStore().clear();
                grdITS.getGrid().getStore().add(new GrdItemTaxStructure_Mdl());
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtITSName.setFocus(true);
                manageDisplay();
                manageFlwApplicable(false);
                grdITS.getGrid().getStore().add(new GrdItemTaxStructure_Mdl());
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ItemTaxStructure.this);
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
        applicableFrom.addCallBack(new ApplicableFrom.CallBack() {

            @Override
            public void onOk(String newDtApplicable, String oldDtApplicable) {
                try {

                    txtApplicableDate.setTag(DmShared.dNull(oldDtApplicable));
                    txtApplicableDate.setText(DmShared.dNull(newDtApplicable));
                    if (!DmShared.dNull(oldDtApplicable).equalsIgnoreCase("")) {
                        fillGrid(topControl.getDocId(), txtApplicableDate.getTag());
                    }
                } catch (Exception ex) {
                    new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
                }
            }

            @Override
            public void onDelete(String applicableFrom) {
                topControlMoveRec(DmShared.xNull(topControl.getDocId()));
            }

            @Override
            public void onCopyITS(String copyITS, String copyDtApplicable, String newDtApplicable) {
                if (!DmShared.dNull(newDtApplicable).equalsIgnoreCase("")) {
                    txtApplicableDate.setText(DmShared.dNull(newDtApplicable));
                }
                if (!DmShared.xNull(copyITS).trim().equals("") && !DmShared.dNull(copyDtApplicable).equalsIgnoreCase("")) {
                    fillGrid(copyITS, copyDtApplicable);
                }
            }
        });

        grdEdit.addCallBack(new ItemTaxStructureGridEdit.CallBack() {

            @Override
            public void onOk(GrdItemTaxStructure_Mdl grdItemTaxStructure_Mdl) {
                grdITS.commitChanges();
            }

            @Override
            public void onDelete() {
                grdITS.commitChanges();
            }
        });

        grdITS.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {

                gridEdit(event.getRowIndex());

            }

        });
        grdITS.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridEdit(grdITS.getGrid().getStore().indexOf(grdITS.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    protected void fillGrid(String itsCode, String applicableDate) {
//        SrvAsync.invokeRPC().fillGrid(DmShared.xNull(topControl.getDocId()), DmShared.dNull(txtApplicableDate.getTag()), new AsyncCallback<DmResult<List<GrdItemTaxStructure_Mdl>>>() {
        SrvAsync.invokeRPC().fillGrid(DmShared.xNull(itsCode), DmShared.dNull(applicableDate), new AsyncCallback<DmResult<List<GrdItemTaxStructure_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdItemTaxStructure_Mdl>> result) {
                if (result.isStatus()) {
                    grdITS.getGrid().getStore().clear();
                    grdITS.getGrid().getStore().addAll(result.getData());
                }
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnShowAppPupup)) {
            try {
                if (topControl.getMode().equalsIgnoreCase("New")) {
                    applicableFrom.show(btnShowAppPupup, true, false, "", true);
                } else {
                    if (!DmShared.xNull(txtITSName.getText()).equalsIgnoreCase("")) {
                        applicableFrom.show(btnShowAppPupup, true, true, topControl.getDocId(), false);
                    } else {
                        new DmAlertMessageBox("Select Record To View/Add ApplicableFrom", (DmTextBox) null).show();
                        return;
                    }
                }
            } catch (DmException e) {
                new DmAlertMessageBox(DmShared.xNull(DmShared.xNull(e.toString())), btnShowAppPupup).show();
                return;
            }
        }

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

    public ItemTaxStructure(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(ItemTaxStructure_Srv.class), ItemTaxStructure.class.getName());
        this.asWidget().setTitle(widgetName);
        txtApplicableDate.addBlurHandler(this);
        topControlEvents(menuPermission);
        gridEvents();
        findEvents();
        textHelpEvent();
        btnShowAppPupup.addClickHandler(this);
        mnuExport.addSelectionHandler(this);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private ItemTaxStructure_Mdl topControlSaveInitialize() {
        ItemTaxStructure_Mdl mrTemp = new ItemTaxStructure_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setApplicableFrom(txtApplicableDate.getText());
        }
        if (DmShared.dNull(txtApplicableDate.getText()).equalsIgnoreCase("")) {
            mrTemp.setApplicableFrom(DmClient.getStartDate());
        }

        mrTemp.setSharedKeyFieldCode(txtITSName.getTag());
        mrTemp.setSharedKeyFieldName(txtITSName.getText());
        mrTemp.setNature(txtHNature.getTag());
        mrTemp.setTotalTaxPercent(txtTotalTaxPercent.getText());

        mrTemp.getGrdItemTaxStructure().addAll(grdITS.getGrid().getStore().getAll());
        return mrTemp;
    }

    private void topControlSave() {
        ItemTaxStructure_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ItemTaxStructure_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
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
                new DmAlertMessageBox(errorMessage, txtITSName).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (txtITSName)).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ItemTaxStructure_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ItemTaxStructure_Mdl> result) {
                manageFlwApplicable(true);
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void textHelpEvent() {
        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<ItemTaxStructure_Mdl> result) {
        ItemTaxStructure_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtITSName.setTag(data.getSharedKeyFieldCode());
            txtITSName.setText(data.getSharedKeyFieldName());
            txtApplicableDate.setText(data.getApplicableFrom());
            txtTotalTaxPercent.setText(data.getTotalTaxPercent());
            txtHNature.setText(data.getNature());
            txtHNature.setTag(data.getNature());

            applicableDtFrom = data.getApplicableFrom();

            grdITS.getGrid().getStore().clear();
            grdITS.getGrid().getStore().addAll(data.getGrdItemTaxStructure());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void manageFlwApplicable(boolean flag) {
        flwApplicableFrom.clear();
        if (flag) {
            flwApplicableFrom.add(btnShowAppPupup);
        }

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
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
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

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (event.isAttached()) {
            this.clickRegistration = btnShowAppPupup.addClickHandler(this);
        } else {
            grdEdit.hide();
            this.clickRegistration.removeHandler();
        }
    }

    private void gridEdit(Integer currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdEdit.show(currentRowIndex, false, false, false, this);
        } else {
            grdEdit.show(currentRowIndex, true, true, true, this);
        }
    }

    private void manageDisplay() {
        txtApplicableDate.setEnabled(false);
    }

    private void clear() {
        txtApplicableDate.setText("");
        txtApplicableDate.setTag("");
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtApplicableDate) {
            txtApplicableDate.setText(DmClient.returnDate(txtApplicableDate.getText()));

        }
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuExport)) {
            exportData.show();
        }
    }

}
