package dataman.utility.voucherType.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.utility.voucherType.shared.*;
import java.util.List;

public class VoucherType extends VoucherType_Dsg implements AttachEvent.Handler, BlurHandler {

    private final ServiceInvoker<VoucherType_SrvAsync> SrvAsync;
    private final FilterationGridEdit<GrdACFilteration_Mdl, Grd_Entry> grdACFilteration = new FilterationGridEdit(gridACFilteration);
    private final FormAppliedGridEdit<GrdFormApplied_Mdl, Grid_FormApplied> grdFormApplied = new FormAppliedGridEdit(gridFormApplied);
    private final WebJobsGridEdit<GrdWebJobs_Mdl, Grd_PrpAcs> grdWebJobs = new WebJobsGridEdit(gridWebJobs);
    private final ReportConfigGridEdit<GrdReportConfig_Mdl, GrdReport_PrpAcs> grdReport = new ReportConfigGridEdit(gridReportConfig);
    private final ReceiptTypeGridEdit<GrdReceiptType_Mdl, VoucherType_Dsg.GrdReceiptType_Mdl_PrpAcs> grdReceiptType = new ReceiptTypeGridEdit(gridPaymentMode);
    private final ItemCategoryGridEdit<KeyFieldExtended, GrdItemCat_Entry> grdItemCat = new ItemCategoryGridEdit(gridItemCat);
    private final ExportData exportData = new ExportData();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                txtShortName.setFocus(true);
                txtVoucherType.setText("");
                txtVoucherType.setTag("");
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtVoucherType.setFocus(true);
                txtMenuName.setEnabled(false);
                txtMenuUsedInModule.setEnabled(false);
                txtVoucherType.setEnabled(false);
                txtHModule.setEnabled(false);
                txtHCategory.setEnabled(false);
                gridACFilteration.getGrid().getStore().add(new GrdACFilteration_Mdl());
                gridItemCat.getGrid().getStore().add(new KeyFieldExtended());
                gridFormApplied.getGrid().getStore().add(new GrdFormApplied_Mdl());
                gridWebJobs.getGrid().getStore().add(new GrdWebJobs_Mdl());
                gridPaymentMode.getGrid().getStore().add(new GrdReceiptType_Mdl());
                gridReportConfig.getGrid().getStore().add(new GrdReportConfig_Mdl());
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(VoucherType.this);
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
        txtShortName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionShortName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtShortName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtShortName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtShortName).show();
                        }
                    }
                });
            }
        });
        txtDescription.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionDescription(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtDescription).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtDescription.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtDescription).show();
                        }
                    }
                });
            }
        });
        txtHActiveYN.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHYesNO(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHActiveYN).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHActiveYN.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHActiveYN).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHCategory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCategory(helper, blnToEndOfFile, DmShared.xNull(txtHModule.getTag()), new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCategory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCategory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHCategory).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHDefaultAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHDefaultAC_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDefaultAC(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHDefaultAC_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefaultAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHDefaultAC_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHDefaultAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHDefaultAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHDefaultAC_Mdl tRow) {
            }
        });
        txtHBalancingAC.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHDefaultAC_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDefaultAC(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHDefaultAC_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBalancingAC).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHDefaultAC_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBalancingAC.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHBalancingAC).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHDefaultAC_Mdl tRow) {
            }
        });
        txtHDefaultReport.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDefaultReport(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefaultReport).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefaultReport.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHDefaultReport).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHMenuText.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMenuText_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHMenuText(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMenuText_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMenuText).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMenuText_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMenuText.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHMenuText).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMenuText_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        txtMenuName.setText(DmShared.xNull(tRow.getMenuName()));
                        txtMenuUsedInModule.setText(DmShared.xNull(tRow.getMenuModuleName()));
                    }
                }
            }
        });
        txtHModule.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHModule(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHModule).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHModule.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHModule).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHlandescape.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHYesNO(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHlandescape).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHlandescape.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHlandescape).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHParentVoucher.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHParentVoucher(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHParentVoucher).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHParentVoucher.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHParentVoucher).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
        txtHGUITemplate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGUITemplate(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGUITemplate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGUITemplate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGUITemplate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHDocumentType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDocumentType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDocumentType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDocumentType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHDocumentType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHDocumentNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDocumentNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDocumentNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDocumentNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHDocumentNature).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHBankCashBook.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHYesNO(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBankCashBook).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBankCashBook.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHBankCashBook).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });

        //
        txtHTemplateName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHTemplate_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTemplateName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHTemplate_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTemplateName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHTemplate_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTemplateName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHTemplateName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHTemplate_Mdl tRow) {

            }
        });
        //
     txtDocumentMailUser.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtDocumentMailUser(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtDocumentMailUser).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtDocumentMailUser.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtDocumentMailUser).show();
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

        grdACFilteration.addCallBack(new FilterationGridEdit.CallBack() {
            @Override
            public void onOk(GrdACFilteration_Mdl grdVoucherEntry) {
                gridACFilteration.commitChanges();
            }

            @Override
            public void onDelete() {
                gridACFilteration.commitChanges();
            }
        });

        gridACFilteration.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showFilterationGridEdit(event.getRowIndex());
            }
        });

        gridACFilteration.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showFilterationGridEdit(gridACFilteration.getGrid().getStore().indexOf(gridACFilteration.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdFormApplied.addCallBack(new FormAppliedGridEdit.CallBack() {
            @Override
            public void onOk(GrdFormApplied_Mdl grdFrmApld) {
                gridFormApplied.commitChanges();
            }

            @Override
            public void onDelete() {
                gridFormApplied.commitChanges();
            }
        });

        gridFormApplied.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showFormAppliedGridEdit(event.getRowIndex());
            }
        });

        gridFormApplied.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showFormAppliedGridEdit(gridFormApplied.getGrid().getStore().indexOf(gridFormApplied.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdWebJobs.addCallBack(new WebJobsGridEdit.CallBack() {
            @Override
            public void onOk(GrdWebJobs_Mdl webJobs_Mdl) {
                gridWebJobs.commitChanges();
            }

            @Override
            public void onDelete() {
                gridWebJobs.commitChanges();
            }
        });

        gridWebJobs.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showWebJobsGridEdit(event.getRowIndex());
            }
        });

        gridWebJobs.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showWebJobsGridEdit(gridWebJobs.getGrid().getStore().indexOf(gridWebJobs.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
        grdReport.addCallBack(new ReportConfigGridEdit.CallBack() {
            @Override
            public void onOk(GrdReportConfig_Mdl grdReportMdl) {
                gridReportConfig.commitChanges();
            }

            @Override
            public void onDelete() {
                gridReportConfig.commitChanges();
            }
        });

        gridReportConfig.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showReportConfigGridEdit(event.getRowIndex());
            }
        });

        gridReportConfig.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showReportConfigGridEdit(gridReportConfig.getGrid().getStore().indexOf(gridReportConfig.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdReceiptType.addCallBack(new ReceiptTypeGridEdit.CallBack() {
            @Override
            public void onOk(GrdReceiptType_Mdl grdReceiptTypeMdl) {
                gridPaymentMode.commitChanges();
            }

            @Override
            public void onDelete() {
                gridPaymentMode.commitChanges();
            }
        });

        gridPaymentMode.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showReceiptTypeGridEdit(event.getRowIndex());
            }
        });

        gridPaymentMode.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showReceiptTypeGridEdit(gridPaymentMode.getGrid().getStore().indexOf(gridPaymentMode.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        grdItemCat.addCallBack(new ItemCategoryGridEdit.CallBack() {
            @Override
            public void onOk(KeyFieldExtended grdItemCategoryMdl) {
                gridItemCat.commitChanges();
            }

            @Override
            public void onDelete() {
                gridItemCat.commitChanges();
            }
        });

        gridItemCat.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showItemCategoryGridEdit(event.getRowIndex());
            }
        });

        gridItemCat.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showItemCategoryGridEdit(gridItemCat.getGrid().getStore().indexOf(gridItemCat.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showFilterationGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdACFilteration.show(currentRowIndex, false, false, false);
        } else {
            grdACFilteration.show(currentRowIndex, true, true, true);
        }
    }

    private void showItemCategoryGridEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdItemCat.show(currentRowIndex, false, false, false);
        } else {
            grdItemCat.show(currentRowIndex, true, true, true);
        }
    }

    private void showFormAppliedGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdFormApplied.show(currentRowIndex, false, false, false);
        } else {
            grdFormApplied.show(currentRowIndex, true, true, true);
        }
    }

    private void showWebJobsGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdWebJobs.show(currentRowIndex, false, false, false);
        } else {
            grdWebJobs.show(currentRowIndex, true, true, true);
        }
    }

    private void showReportConfigGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdReport.show(currentRowIndex, false, false, false);
        } else {
            grdReport.show(currentRowIndex, true, true, true);
        }
    }

    private void showReceiptTypeGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            grdReceiptType.show(currentRowIndex, false, false, false);
        } else {
            grdReceiptType.show(currentRowIndex, true, true, true);
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
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), dmFind.getTextBox()).show();
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

    public VoucherType(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(VoucherType_Srv.class), VoucherType.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        txtAcceptEntryAfter.addBlurHandler(this);
        flwForm.addAttachHandler(this);
        swi.setWidget(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private VoucherType_Mdl topControlSaveInitialize() {
        VoucherType_Mdl mrTemp = new VoucherType_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmClient.vNull(txtVoucherType.getText(), "0"));
        mrTemp.setDescription(DmShared.xNull(txtDescription.getText()));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));
        mrTemp.setModuleCode(DmShared.xNull(txtHModule.getTag()));
        mrTemp.setModuleName(DmShared.xNull(txtHModule.getText()));
        mrTemp.setCategoryCode(DmShared.xNull(txtHCategory.getTag()));
        mrTemp.setCategoryName(DmShared.xNull(txtHCategory.getText()));
        mrTemp.setMenutextCode(DmShared.xNull(txtHMenuText.getTag()));
        mrTemp.setMenutextName(DmShared.xNull(txtHMenuText.getText()));
        mrTemp.setMenuname(DmShared.xNull(txtMenuName.getText()));
        mrTemp.setMenuUsedInModule(DmShared.xNull(txtMenuUsedInModule.getText()));
        mrTemp.setSysGroup(DmShared.xNull("0"));

        mrTemp.setActiveCode(DmShared.xNull(txtHActiveYN.getTag()));
        mrTemp.setActiveName(DmShared.xNull(txtHActiveYN.getText()));

        mrTemp.setDefaultReportCode(DmShared.xNull(txtHDefaultReport.getTag()));
        mrTemp.setDefaultReportName(DmShared.xNull(txtHDefaultReport.getText()));
        mrTemp.setPaperSize(DmShared.xNull(txtPaperSize.getText()));
        mrTemp.setLandscapeCode(DmShared.xNull(txtHlandescape.getTag()));
        mrTemp.setLandscapeName(DmShared.xNull(txtHlandescape.getText()));
        mrTemp.setDefaultAcCode(DmShared.xNull(txtHDefaultAC.getTag()));
        mrTemp.setDefaultAcName(DmShared.xNull(txtHDefaultAC.getText()));

        mrTemp.setBalancingAcCode(DmShared.xNull(txtHBalancingAC.getTag()));
        mrTemp.setBalancingAcName(DmShared.xNull(txtHBalancingAC.getText()));

        mrTemp.setSerialNo(DmClient.vNull(txtSerialNo.getText(), "0"));
        mrTemp.setParentVoucherCode(DmShared.xNull(txtHParentVoucher.getText()));
        mrTemp.setParentVoucherName(DmShared.xNull(txtHParentVoucher.getText()));
        mrTemp.setAcceptEntryAfter(DmShared.xNull(txtAcceptEntryAfter.getText()));
        mrTemp.setGuiTemplateCode(DmShared.xNull(txtHGUITemplate.getTag()));
        mrTemp.setGuiTemplateName(DmShared.xNull(txtHGUITemplate.getText()));
        mrTemp.setIsBankCashBookCode(DmShared.xNull(txtHBankCashBook.getTag()));
        mrTemp.setDocumentTypeCode(DmShared.xNull(txtHDocumentType.getTag()));
        mrTemp.setDocumentTypeName(DmShared.xNull(txtHDocumentType.getText()));
        mrTemp.setDocumentNatureCode(DmShared.xNull(txtHDocumentNature.getTag()));
        mrTemp.setDocumentNatureName(DmShared.xNull(txtHDocumentNature.getText()));

        mrTemp.setDocumentMailTemplateCode(DmShared.xNull(txtHTemplateName.getTag()));
        mrTemp.setDocumentMailUserName(DmShared.xNull(txtDocumentMailUser.getText()));
        mrTemp.setDocumentMailReportName(DmShared.xNull(txtReportName.getText()));

        mrTemp.getGrdFilteration().addAll(gridACFilteration.getGrid().getStore().getAll());

        mrTemp.getGrdAppliedForm().addAll(gridFormApplied.getGrid().getStore().getAll());

        mrTemp.getGrdWebJobs().addAll(gridWebJobs.getGrid().getStore().getAll());

        mrTemp.getGrdReceiptType().addAll(gridPaymentMode.getGrid().getStore().getAll());

        mrTemp.getGrdReportConfig().addAll(gridReportConfig.getGrid().getStore().getAll());

        mrTemp.getGrdItemCategory().addAll(gridItemCat.getGrid().getStore().getAll());

        return mrTemp;
    }

    private void topControlSave() {
        VoucherType_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            VoucherType_Vld.chkSave(mrTemp);

            SrvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtVoucherType).show();
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
                        topControlSaveError(result.getErrorNumber(), DmShared.xNull(result.getErrorMessage()));
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
                new DmAlertMessageBox(errorMessage, txtVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtShortName).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtDescription).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHModule).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHCategory).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, txtHDefaultReport).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHlandescape).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHGUITemplate).show();
                break;
            case 900:
                new DmAlertMessageBox(errorMessage, txtHActiveYN).show();
                break;
            case 1000:
                new DmAlertMessageBox(errorMessage, txtHBankCashBook).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtVoucherType).show();
                break;
        }

    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtVoucherType).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtVoucherType).show();
                    }
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<VoucherType_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtVoucherType).show();
            }

            @Override
            public void onSuccess(DmResult<VoucherType_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<VoucherType_Mdl> result) {
        VoucherType_Mdl data;

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());
            txtVoucherType.setText(DmClient.vNull(data.getSharedKeyFieldName(), "0"));

            userControl.setTextBlog(preparedByBlog, DmShared.xNull(data.getSharedPreparedBy()));
            userControl.setTextBlog(modifiedByBlog, DmShared.xNull(data.getSharedModifiedBy()));

            txtDescription.setText(DmShared.xNull(data.getDescription()));
            txtShortName.setText(DmShared.xNull(data.getShortName()));
            txtHModule.setTag(DmShared.xNull(data.getModuleCode()));
            txtHModule.setText(DmShared.xNull(data.getModuleName()));
            txtHCategory.setTag(DmShared.xNull(data.getCategoryCode()));
            txtHCategory.setText(DmShared.xNull(data.getCategoryName()));
            txtHMenuText.setTag(DmShared.xNull(data.getMenutextCode()));
            txtHMenuText.setText(DmShared.xNull(data.getMenutextName()));
            txtMenuName.setText(DmShared.xNull(data.getMenuname()));
            txtMenuUsedInModule.setText(DmShared.xNull(data.getMenuUsedInModule()));
            lblSysGroup.setText(DmShared.xNull(data.getSysGroup()));
            txtHActiveYN.setTag(DmShared.xNull(data.getActiveCode()));
            txtHActiveYN.setText(DmShared.xNull(data.getActiveName()));

            txtHDefaultReport.setTag(DmShared.xNull(data.getDefaultReportCode()));
            txtHDefaultReport.setText(DmShared.xNull(data.getDefaultReportName()));
            txtPaperSize.setText(DmShared.xNull(data.getPaperSize()));
            txtHlandescape.setTag(DmShared.xNull(data.getLandscapeCode()));
            txtHlandescape.setText(DmShared.xNull(data.getLandscapeName()));
            txtHDefaultAC.setTag(DmShared.xNull(data.getDefaultAcCode()));
            txtHDefaultAC.setText(DmShared.xNull(data.getDefaultAcName()));

            txtHBalancingAC.setTag(DmShared.xNull(data.getBalancingAcCode()));
            txtHBalancingAC.setText(DmShared.xNull(data.getBalancingAcName()));

            txtHGUITemplate.setTag(DmShared.xNull(data.getGuiTemplateCode()));
            txtHGUITemplate.setText(DmShared.xNull(data.getGuiTemplateName()));
            txtSerialNo.setText(DmClient.vNull(data.getSerialNo(), "0"));
            txtHParentVoucher.setTag(DmShared.xNull(data.getParentVoucherName()));
            txtHParentVoucher.setText(DmShared.xNull(data.getParentVoucherName()));
            txtAcceptEntryAfter.setText(DmShared.xNull(data.getAcceptEntryAfter()));
            txtHBankCashBook.setTag(DmShared.xNull(data.getIsBankCashBookCode()));
            txtHBankCashBook.setText(DmShared.xNull(data.getIsBankCashBookName()));

            txtHDocumentNature.setText(DmShared.xNull(data.getDocumentNatureName()));
            txtHDocumentNature.setTag(DmShared.xNull(data.getDocumentNatureCode()));
            txtHDocumentType.setTag(DmShared.xNull(data.getDocumentTypeCode()));
            txtHDocumentType.setText(DmShared.xNull(data.getDocumentTypeName()));

            txtDocumentMailUser.setText(DmShared.xNull(data.getDocumentMailUserName()));
            txtDocumentMailUser.setTag(DmShared.xNull(data.getDocumentMailUserName()));
            txtReportName.setText(DmShared.xNull(data.getDocumentMailReportName()));
            txtHTemplateName.setTag(DmShared.xNull(data.getDocumentMailTemplateCode()));
            txtHTemplateName.setText(DmShared.xNull(data.getDocumentMailTemplateCodeName()));

            gridACFilteration.getGrid().getStore().clear();
            gridACFilteration.getGrid().getStore().addAll(data.getGrdFilteration());

            gridFormApplied.getGrid().getStore().clear();
            gridFormApplied.getGrid().getStore().addAll(data.getGrdAppliedForm());

            gridWebJobs.getGrid().getStore().clear();
            gridWebJobs.getGrid().getStore().addAll(data.getGrdWebJobs());

            gridReportConfig.getGrid().getStore().clear();
            gridReportConfig.getGrid().getStore().addAll(data.getGrdReportConfig());

            gridPaymentMode.getGrid().getStore().clear();
            gridPaymentMode.getGrid().getStore().addAll(data.getGrdReceiptType());

            gridItemCat.getGrid().getStore().clear();
            gridItemCat.getGrid().getStore().addAll(data.getGrdItemCategory());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), txtVoucherType).show();
        }
        topControl.setDisplay(true);
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtVoucherType).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    clear();
                    topControlCard();
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtVoucherType).show();
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
        if (!event.isAttached()) {
            grdACFilteration.hide();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtAcceptEntryAfter) {
            txtAcceptEntryAfter.setText(DmClient.returnDate(txtAcceptEntryAfter.getText()));
        }
    }

    private void clear() {
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtHActiveYN.setText("Yes");
        txtHActiveYN.setTag("1");
        txtHDefaultReport.setTag("W");
        txtHDefaultReport.setText("Windows");
        lblSysGroup.setText("User Define");
        txtHlandescape.setTag("0");
        txtHlandescape.setText("No");
        txtHBankCashBook.setTag("0");
        txtHBankCashBook.setText("No");

        gridACFilteration.getGrid().getStore().clear();
        gridACFilteration.getGrid().getStore().add(new GrdACFilteration_Mdl());

        gridFormApplied.getGrid().getStore().clear();
        gridFormApplied.getGrid().getStore().add(new GrdFormApplied_Mdl());

        gridWebJobs.getGrid().getStore().clear();
        gridWebJobs.getGrid().getStore().add(new GrdWebJobs_Mdl());

        gridReportConfig.getGrid().getStore().clear();
        gridReportConfig.getGrid().getStore().add(new GrdReportConfig_Mdl());

        gridPaymentMode.getGrid().getStore().clear();
        gridPaymentMode.getGrid().getStore().add(new GrdReceiptType_Mdl());

        gridItemCat.getGrid().getStore().clear();
        gridItemCat.getGrid().getStore().add(new KeyFieldExtended());

        txtMenuName.setEnabled(false);
        txtMenuUsedInModule.setEnabled(false);
        tabPanel.selectTab(0);
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuExportData.getText())) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                exportData.show();
            }
        }
    }

}
