package dataman.accounts.tdsCategoryMaster.client;

import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.accounts.tdsCategoryMaster.shared.*;
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
import java.util.List;

public final class TdsCategoryMaster extends TdsCategoryMaster_Dsg implements IsWidget, AttachEvent.Handler, BlurHandler, ClickHandler {

    ServiceInvoker<TdsCategoryMaster_SrvAsync> SrvAsync;
    final TdsCategoryGridEdit<TdsCategoryMaster_Mdl, Grd_PrpAcs> tdsCategoryGridEdit = new TdsCategoryGridEdit(dmGrd);
    private HandlerRegistration clickRegistration;
    private ApplicableFrom applicableFrom = new ApplicableFrom();
    protected String applicableDtFrom = "", applicableDtTo = "";

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (event.isAttached()) {
            this.clickRegistration = btnShowAppPupup.addClickHandler(this);
        } else {

            this.clickRegistration.removeHandler();
            tdsCategoryGridEdit.hide();
        }
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());

        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.setDisplay(true);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                // Add a new Row in Grid
                clear();
                txtCategoryName.setFocus(true);
                manageDisplay();
                manageFlwApplicable(true);

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                // Add a new Row in Grid

                txtCategoryName.setFocus(true);
                manageDisplay();
                manageFlwApplicable(false);
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(TdsCategoryMaster.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {

        txtHTdsFor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTdsFor(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTdsFor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                    txtHTdsFor.setText(tRow.getSharedKeyFieldName());
                    txtHTdsFor.setTag(tRow.getSharedKeyFieldCode());
                }
                txtHTdsFor.setFocus(true);
            }

        });
        txtCategoryName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtCategoryName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtCategoryName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtCategoryName).show();
                        }
                    }
                });
            }
        });

        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
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
                if (!DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHType.getTag2nd()))) {
                    txtHType.setTag2nd(DmShared.xNull(txtHType.getTag()));
                    clear();
                }
                if (blnStatus && t1Row != null) {
                    txtHType.setText(DmShared.xNull(t1Row.getSharedKeyFieldName()));
                    txtHType.setTag(DmShared.xNull(t1Row.getSharedKeyFieldCode()));

                }

            }
        });
        
        txtHTdsOnPreviousTransactions.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTdsOnPreviousTransactions(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTdsOnPreviousTransactions.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                    txtHTdsOnPreviousTransactions.setText(tRow.getSharedKeyFieldName());
                    txtHTdsOnPreviousTransactions.setTag(tRow.getSharedKeyFieldCode());
                }
                txtHTdsFor.setFocus(true);
            }

        });

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
            public void onCopyTDS(String copyITS, String copyDtApplicable, String newDtApplicable) {
                if (!DmShared.dNull(newDtApplicable).equalsIgnoreCase("")) {
                    txtApplicableDate.setText(DmShared.dNull(newDtApplicable));
                }
                if (!DmShared.xNull(copyITS).trim().equals("") && !DmShared.dNull(copyDtApplicable).equalsIgnoreCase("")) {
                    fillGrid(copyITS, copyDtApplicable);
                }
            }
        });

        tdsCategoryGridEdit.addCallBack(new TdsCategoryGridEdit.CallBack() {
            @Override
            public void onOk(GrdTdsCategory_Mdl grdVoucherEntry) {
                dmGrd.commitChanges();
            }

            @Override
            public void onDelete() {
                dmGrd.commitChanges();
            }
        });

        dmGrd.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                if (DmShared.vNull(txtInitialTurnover.getText().trim()) == 0) {
                    new DmAlertMessageBox("Initial Turnover Amount is mandatory.", txtInitialTurnover).show();
                } else {
                    showVoucherEntryGridEdit(event.getRowIndex());
                }
            }
        });

        dmGrd.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    if (DmShared.vNull(txtInitialTurnover.getText().trim()) == 0) {
                        new DmAlertMessageBox("Initial Turnover Amount is mandatory.", txtInitialTurnover).show();
                    } else {
                        showVoucherEntryGridEdit(dmGrd.getGrid().getStore().indexOf(dmGrd.getGrid().getSelectionModel().getSelectedItem()));
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    protected void fillGrid(String itsCode, String applicableDate) {

        SrvAsync.invokeRPC().fillGrid(DmShared.xNull(itsCode), DmShared.dNull(applicableDate), new AsyncCallback<DmResult<List<GrdTdsCategory_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdTdsCategory_Mdl>> result) {
                if (result.isStatus()) {
                    dmGrd.getGrid().getStore().clear();
                    dmGrd.getGrid().getStore().addAll(result.getData());
                }
            }
        });
    }

    private void showVoucherEntryGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            tdsCategoryGridEdit.show(currentRowIndex, false, false, false, txtInitialTurnover.getText());
        } else {
            tdsCategoryGridEdit.show(currentRowIndex, true, true, true, txtInitialTurnover.getText());
        }
    }

    public TdsCategoryMaster(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(TdsCategoryMaster_Srv.class), TdsCategoryMaster.class.getName());
        this.asWidget().setTitle(widgetName);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        manageEvents();
        textHelpEvents();
        gridEvents();
        btnShowAppPupup.addClickHandler(this);
        swi.setWidget(this);
    }

    private void manageEvents() {

    }

    @Override
    public Widget asWidget() {
        return flwForm;
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

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<TdsCategoryMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<TdsCategoryMaster_Mdl> result) {
                manageFlwApplicable(true);
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<TdsCategoryMaster_Mdl> result) {
        TdsCategoryMaster_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtCategoryName.setText(data.getSharedKeyFieldName());
            txtApplicableDate.setText(data.getApplicableFrom());
            txtHType.setText(data.getType());
            txtHType.setTag(data.getTypeCode());
            txtHType.setTag2nd(data.getTypeCode());
            applicableDtFrom = data.getApplicableFrom();
            txtHTdsFor.setTag(DmShared.xNull(data.getTdsForTag()));
            txtHTdsFor.setText(DmShared.xNull(data.getTdsForText()));
            txtSection.setText(data.getSection());
            txtInitialTurnover.setText(DmClient.vNull(data.getGrdData().get(0).getInitialTurnoverAmount(),"0.00"));
            txtHTdsOnPreviousTransactions.setTag(DmShared.xNull(data.getTdsOnPreviousTransactionsTag()));
            txtHTdsOnPreviousTransactions.setText(DmShared.xNull(data.getTdsOnPreviousTransactionsText()));
            dmGrd.getGrid().getStore().clear();
            dmGrd.getGrid().getStore().addAll(data.getGrdData());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlSave() {
        TdsCategoryMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            TdsCategoryMaster_Vld.chkSave(mrTemp);
            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {

                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtCategoryName).show();
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
                new DmAlertMessageBox(errorMessage, txtCategoryName).show();
                break;

            case 200:
            case 300:
                ((CellSelectionModel) dmGrd.getGrid().getSelectionModel()).selectCell(0, 1);
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;

            default:
                new DmAlertMessageBox(errorMessage, txtCategoryName).show();
                break;
        }

    }

    private TdsCategoryMaster_Mdl topControlSaveInitialize() {
        TdsCategoryMaster_Mdl mrTemp = new TdsCategoryMaster_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.setApplicableFrom(txtApplicableDate.getText());
        }
        if (DmShared.dNull(txtApplicableDate.getText()).equalsIgnoreCase("")) {
            mrTemp.setApplicableFrom(DmClient.getStartDate());
        }
        mrTemp.setType(txtHType.getText());
        mrTemp.setTypeCode(txtHType.getTag());
        mrTemp.setSection(DmShared.xNull(txtSection.getText()));
        mrTemp.setTdsForTag(DmShared.xNull(txtHTdsFor.getTag()));
        mrTemp.setTdsForText(DmShared.xNull(txtHTdsFor.getText()));
        mrTemp.setInitialTurnoverAmount(DmShared.xNull(txtInitialTurnover.getText()));
        dmGrd.getGrid().getStore().commitChanges();

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtCategoryName.getText()));
        mrTemp.setTdsOnPreviousTransactionsTag(DmShared.xNull(txtHTdsOnPreviousTransactions.getTag()));

        mrTemp.getGrdData().addAll(dmGrd.getGrid().getStore().getAll());
        return mrTemp;

    }

    private void clear() {

        txtApplicableDate.setText("");
        txtApplicableDate.setTag("");

        txtHType.setText("");
        txtHType.setTag("");
        txtHType.setTag2nd("");
        dmGrd.getGrid().getStore().clear();
        dmGrd.getGrid().getStore().add(new GrdTdsCategory_Mdl());

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(btnShowAppPupup)) {
            try {
                if (topControl.getMode().equalsIgnoreCase("New")) {
                    applicableFrom.show(btnShowAppPupup, true, false, "", true);
                } else {
                    if (!DmShared.xNull(txtCategoryName.getText()).equalsIgnoreCase("")) {
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

    private void manageFlwApplicable(boolean flag) {
        flwApplicableFrom.clear();
        if (flag) {
            flwApplicableFrom.add(btnShowAppPupup);
        }

    }

    @Override
    public void onBlur(com.google.gwt.event.dom.client.BlurEvent event) {
        if (event.getSource() == txtApplicableDate) {
            txtApplicableDate.setText(DmClient.returnDate(txtApplicableDate.getText()));

        }
    }

    private void manageDisplay() {
        txtApplicableDate.setEnabled(false);
    }

}
