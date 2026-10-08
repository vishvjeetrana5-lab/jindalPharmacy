package dataman.accounts.voucherEntry.client;

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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import static dataman.accounts.voucherEntry.client.VoucherEntry_Dsg.approvedByBlog;
import static dataman.accounts.voucherEntry.client.VoucherEntry_Dsg.preparedByBlog;
import dataman.accounts.voucherEntry.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.accountAdjustment.shared.GrdAdjustment_Mdl;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.itemAdjustment.shared.GrdItemAdjustment_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class VoucherEntry extends VoucherEntry_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    ServiceInvoker<VoucherEntry_SrvAsync> SrvAsync;
    List<GrdVoucherEntry_Mdl> grdList = new ArrayList<>();
    final VoucherEntryGridEdit<GrdVoucherEntry_Mdl, Grd_PrpAcs> voucherEntryGridEdit = new VoucherEntryGridEdit(grdAccountDetail);
    String dateEnterByUser = DmClient.getLoginDate();
    private String tempV_TypeCode = "", tempV_TypeName = "", tempV_Cat = "", tempV_Date = "", tempAcName = "", tempAcCode = "", tempAcNature = "";
    WalkThrough walkThrough;
    VoucherPrintPopup voucherPrintPopup = new VoucherPrintPopup();
    VoucherPrint_Mdl voucherPrint_Mdl = new VoucherPrint_Mdl();
    String posted = "A";
    ReportPrint reportPrint;
    private SetWidgetInvoker swi;
    LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    GrdVoucherEntry_Mdl headerAcRow = new GrdVoucherEntry_Mdl();
    String headerAcManualCode = "";

    //private double headerAcTotal = 0;
    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{lvc.JOURNAL, lvc.BANK_PAYMENT, lvc.BANK_RECEIPT, lvc.CONTRA})) {

            @Override
            public String getVoucherType() {
                return txtHVoucherType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtVoucherDate.getText();
            }

            @Override
            public String getVoucherValue() {
                return "";
            }
        });
        topControl.setDisplay(true);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                // onEdit();

                if (!DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is approved. You can't modify this record.", (DmTextBox) null).show();
                } else {
                    onEdit();
                }
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

                voucherPrintPopup.show(DmShared.xNull(txtVoucherDate.getText()), DmShared.xNull(txtVoucherDate.getText()),
                        DmShared.xNull(txtHVoucherType.getTag2nd()), DmShared.xNull(txtHVoucherType.getTag()), DmShared.xNull(txtHVoucherType.getText()),
                        DmShared.xNull(txtVoucherNo.getText()), topControl.getDocId(), DmShared.xNull(txtHAccountName.getText()), DmShared.iNull(txtHAccountName.getTag()).toString(), currentWidgetName, swi, DmShared.xNull(userControl.getTextBlog(approvedByBlog)));

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                checkAndSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(VoucherEntry.this);
            }
        });
    }

    private void onAdd() {
        try {
            clear();
            txtVoucherNo.setEnabled(false);
            txtHVoucherType.setFocus(true);

            userControl.setTextBlog(preparedByBlog, "");
            userControl.setTextBlog(modifiedByBlog, "");
            userControl.setTextBlog(approvedByBlog, "");
            lblLedgerBalance.setText("Dr. 0.00");

            if (!DmShared.xNull(tempV_TypeCode).trim().equalsIgnoreCase("")) {
                setDisplay(tempV_Cat);
                txtHVoucherType.setText(tempV_TypeName);
                txtHVoucherType.setTag(tempV_TypeCode);
                txtVoucherDate.setText(tempV_Date);
                if (!DmShared.xNull(tempAcCode).trim().equalsIgnoreCase("")) {
                    txtHAccountName.setText(tempAcName);
                    txtHAccountName.setTag(tempAcCode);
                    txtHAccountName.setTag2nd(tempAcNature);
                    getLedgerBalance(DmShared.xNull(txtHAccountName.getTag()));
                }
            } else {
                txtVoucherDate.setText(dateEnterByUser);
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
            e.printStackTrace();
        }
    }

    private void onEdit() {
        try {
            setDisplay(txtHVoucherType.getTag2nd());
            txtHVoucherType.setEnabled(false);
            txtVoucherNo.setEnabled(false);
            txtVoucherDate.setFocus(true);
            grdAccountDetail.getGrid().getStore().add(new GrdVoucherEntry_Mdl());
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
            e.printStackTrace();
        }
    }

    private void VoucherPrint() {
        voucherPrintPopup.addCallBack(new VoucherPrintPopup.CallBack() {

            @Override
            public void ok(VoucherPrint_Mdl mrTemp) {
                voucherPrint_Mdl = mrTemp;
                if (currentWidgetName.equalsIgnoreCase(new lmAccounts().PAYMENT_VOUCHER.caption)) {
                    if (voucherPrint_Mdl.isPaymentVoucherChecked()) {
                        voucherPrint_Mdl.setPrintTitle("Payment");
                        print(true);
                    }
                    if (voucherPrint_Mdl.isBankLetterChecked()) {
                        voucherPrint_Mdl.setPrintTitle("BankLetter");
                        print(true);
                    }
                    if (voucherPrint_Mdl.isChqPrintChecked()) {
                        voucherPrint_Mdl.setPrintTitle("Cheque");
                        print(true);
                    }
                } else {
                    print(false);
                }
            }
        });
    }

    private void print(final boolean printToPrinter) {
        if (!DmShared.xNull(voucherPrint_Mdl.getPrintTitle()).equalsIgnoreCase("Cheque")) {
            String title = "";
            if (DmShared.xNull(voucherPrint_Mdl.getPrintTitle()).equalsIgnoreCase("BankLetter")) {
                title = "Bank Letter";
            } else {
                title = this.currentWidgetName;
            }
            HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
            hashMapFilter.put("fromDate", new FieldUDT_Mdl("From Date", voucherPrint_Mdl.getFromDate(), voucherPrint_Mdl.getFromDate()));
            hashMapFilter.put("toDate", new FieldUDT_Mdl("To Date", voucherPrint_Mdl.getToDate(), voucherPrint_Mdl.getToDate()));
            if (!DmShared.xNull(voucherPrint_Mdl.getPrintTitle()).equalsIgnoreCase("BankLetter")) {
                hashMapFilter.put("category", new FieldUDT_Mdl("Category", voucherPrint_Mdl.getVoucherCategory(), voucherPrint_Mdl.getVoucherCategory()));
                voucherPrint_Mdl.setPrintTitle("");
            }

            hashMapFilter.put("voucherType", new FieldUDT_Mdl("Voucher Type", DmShared.xNull(voucherPrint_Mdl.getVoucherTypeText()), DmShared.xNull(voucherPrint_Mdl.getVoucherTypeTag())));
            hashMapFilter.put("voucherNo", new FieldUDT_Mdl("Voucher No.", voucherPrint_Mdl.getVoucherNo(), voucherPrint_Mdl.getVoucherNoCode()));
            hashMapFilter.put("contraSub", new FieldUDT_Mdl(DmShared.xNull(lblAccName.getText()), voucherPrint_Mdl.getHeaderAccountName(), voucherPrint_Mdl.getHeaderAccountCode()));
            new OpenSourceReport(txtHVoucherType.getTag(), voucherPrint_Mdl.getPrintTitle(), title, this.currentWidgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
                @Override
                public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                    DmClient.localSettingPrintMap(reportProperty, "Accounts", currentWidgetName, "Print");
                    return reportProperty;
                }
            }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
        } else if (DmShared.xNull(voucherPrint_Mdl.getPrintTitle()).equalsIgnoreCase("Cheque")) {
            SrvAsync.invokeRPC().printRecords(voucherPrint_Mdl, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        new ReportPrint(result.getData(), printToPrinter, new ReportPrint.CallBack() {
                            @Override
                            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                                DmClient.localSettingPrintMap(reportProperty, "Accounts", currentWidgetName, "Print");
                                return reportProperty;
                            }
                        }, swi);

                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (Button) null).show();
                    }
                }
            });
        }
    }

    private void textHelpEvents() {
        walkThrough.addCallBack(new WalkThrough.CallBack() {
            @Override
            public void onClose() {
                topControlMoveRec(topControl.getDocId());
                topControlCard();
            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(voucherCategory, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVoucherType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVoucherType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVoucherType_Mdl tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        clear();
                        setDisplay(tRow.getCategory());
                        txtHVoucherType.setText(tRow.getSharedKeyFieldName());
                        txtHVoucherType.setTag(tRow.getSharedKeyFieldCode());
                        txtHAccountName.setText("");
                        txtHAccountName.setTag("");
                        txtHAccountName.setTag2nd("");
                        if (!String.valueOf(tRow.getDefaultAc()).equalsIgnoreCase("")) {
                            txtHAccountName.setText(tRow.getDAName());
                            txtHAccountName.setTag(tRow.getDefaultAc());
                            txtHAccountName.setTag2nd(tRow.getNature());
                            getLedgerBalance(DmShared.xNull(txtHAccountName.getTag()));
                        }
                    }
                }
                txtHVoucherType.setFocus(true);
            }

        });

        txtHAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, DmShared.xNull(txtHVoucherType.getTag()), new AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHAccountName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHAccountName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHAccountName_Mdl tRow) {
                if (blnStatus) {
                    lblLedgerBalance.setText("");
                    headerAcManualCode = "";
                    if (tRow != null) {
                        txtHAccountName.setTag2nd(tRow.getNatureManualCode());
                        headerAcManualCode = DmShared.xNull(tRow.getManualCode());
                        getLedgerBalance(DmShared.xNull(txtHAccountName.getTag()));
                    }
                }
                txtHAccountName.setFocus(true);
            }
        });

        txtHCostCenter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCostCenter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCostCenter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus) {
                    if (tRow != null) {
                        txtHCostCenter.setText(tRow.getSharedKeyFieldName());
                        txtHCostCenter.setTag(tRow.getSharedKeyFieldCode());
                    }
                }
                txtHAccountName.setFocus(true);
            }

        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), voucherCategory, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
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

    private void getLedgerBalance(String StrSubCode) {
        SrvAsync.invokeRPC(true).getGetLedgerBalance(StrSubCode, new AsyncCallback<DmResult<Double[]>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Double[]> result) {
                if (result.isStatus()) {
                    if (DmShared.vNull(result.getData()[0]).doubleValue() > 0) {
                        lblLedgerBalance.setText("Dr. " + DmClient.vNull(result.getData()[0], "0.00"));
                    } else if (DmShared.vNull(result.getData()[0]).doubleValue() < 0) {
                        lblLedgerBalance.setText("Cr. " + DmClient.vNull((0 - result.getData()[0]), "0.00"));
                    } else {
                        lblLedgerBalance.setText("");
                    }
                    setHeaderRowRecord();

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void setHeaderRowRecord() {
        headerAcRow = new GrdVoucherEntry_Mdl();
        headerAcRow.setAccMCode(headerAcManualCode);
        headerAcRow.setAccName(txtHAccountName.getText());
        headerAcRow.setAccCode(txtHAccountName.getTag());
        headerAcRow.setAccNature(txtHAccountName.getTag2nd());
        headerAcRow.setNarration("");
        headerAcRow.setPan("");
        headerAcRow.setTan("");
        headerAcRow.setDebit(null);
        headerAcRow.setCredit(null);

        headerAcRow.getCostCenterGrdData().add(new CostCenterGrid_Mdl());
        headerAcRow.getLstAdjustments().add(new GrdAdjustment_Mdl());
        headerAcRow.getLstItemAdjustments().add(new GrdItemAdjustment_Mdl());
        headerAcRow.setChequeDetail_Mdl(new ChequeDetail_Mdl());
        headerAcRow.setTds_Mdl(new TDS_Mdl());
        headerAcRow.setHeaderAcRecord(true);
    }

    private void gridEvents() {
        voucherEntryGridEdit.addCallBack(new VoucherEntryGridEdit.CallBack() {

            @Override
            public void onOk(GrdVoucherEntry_Mdl grdVoucherEntry_Mdl) {
                grdAccountDetail.commitChanges();
                manageTdsRow(grdVoucherEntry_Mdl, "");
            }

            @Override
            public void onDelete(String parentAcCode) {
                grdAccountDetail.commitChanges();
                manageTdsRow(null, parentAcCode);
            }
        });

        grdAccountDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showVoucherEntryGridEdit(event.getRowIndex());
            }
        });

        grdAccountDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event
            ) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showVoucherEntryGridEdit(grdAccountDetail.getGrid().getStore().indexOf(grdAccountDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showVoucherEntryGridEdit(int currentRowIndex) {
        if (DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase("") || DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_PAYMENT)
                || DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
            if (DmShared.iNull(txtHAccountName.getTag()).longValue() == 0) {
                String acDrCrType = DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_PAYMENT) ? "Credit" : "Debit";
                new DmAlertMessageBox(acDrCrType + " A/c Name is mandatory.", txtHAccountName).show();
                return;
            }
        }
        if (topControl.getMode().equalsIgnoreCase("Browse") || grdAccountDetail.getGrid().getStore().get(currentRowIndex).isTdsRecord()
                || grdAccountDetail.getGrid().getStore().get(currentRowIndex).isHeaderAcRecord()) {
            voucherEntryGridEdit.show(currentRowIndex, this, false, false, false);
        } else {
            voucherEntryGridEdit.show(currentRowIndex, this, true, true, true);
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), posted, voucherCategory, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), strKey, new AsyncCallback<DmResult<String>>() {

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

    private VoucherEntry_Mdl topControlSaveInitialize() {
        VoucherEntry_Mdl mrTemp = new VoucherEntry_Mdl();
        double totalDr = 0, totalCr = 0;

        calculate();
        if (topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setVoucherPrefix(DmClient.getVoucherPrefix());
        } else {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.setVoucherPrefix(DmShared.xNull(txtVoucherNo.getTag()));
            mrTemp.getObjRecId().setCounter(DmShared.iNull(txtVoucherDate.getTag()).longValue());
            mrTemp.getObjRecId().setPrefix(DmShared.xNull(txtVoucherNo.getTag2nd()));
            mrTemp.getObjRecId().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
        }

        dateEnterByUser = txtVoucherDate.getText();
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherCategory(DmShared.xNull(txtHVoucherType.getTag2nd()));

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setCosetCenterName(DmShared.xNull(txtHCostCenter.getText()));
        mrTemp.setCosetCenterCode(DmShared.xNull(txtHCostCenter.getTag()));

        mrTemp.setReferenceNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setReferenceDate(DmShared.dNull(txtRefDate.getText()));

        mrTemp.setAccountName(DmShared.xNull(txtHAccountName.getText()));

        mrTemp.setAccountNameCode(DmShared.xNull(txtHAccountName.getTag()));
        mrTemp.setAccountNameNature(DmShared.xNull(txtHAccountName.getTag2nd()));

        for (int i = 0; i < grdAccountDetail.getGrid().getStore().getAll().size(); i++) {
            if (!DmShared.xNull(grdAccountDetail.getGrid().getStore().getAll().get(i).getAccCode()).equalsIgnoreCase("") && grdAccountDetail.getGrid().getStore().getAll().get(i).isHeaderAcRecord()) {
                totalDr += DmShared.vNull(DmClient.vNull(grdAccountDetail.getGrid().getStore().getAll().get(i).getDebit(), "0.00")).doubleValue();
                totalCr += DmShared.vNull(DmClient.vNull(grdAccountDetail.getGrid().getStore().getAll().get(i).getCredit(), "0.00")).doubleValue();
            }
        }

        mrTemp.setTotalDebit(DmClient.vNull(totalDr, "0.00"));
        mrTemp.setTotalCredit(DmClient.vNull(totalCr, "0.00"));

        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));

        if (txtHAccountName.isEnabled()) {
            mrTemp.setIsAccountNameEnable("Y");
        }

        mrTemp.setgrdData(mrTemp.cloneGridData(grdAccountDetail.getGrid().getStore().getAll()));

        return mrTemp;
    }

    private void topControlSave(VoucherEntry_Mdl mrTemp) {
        try {
            tempV_TypeCode = "";
            tempV_TypeName = "";
            tempV_Cat = "";
            tempV_Date = "";
            tempAcName = "";
            tempAcCode = "";
            tempAcNature = "";

            SrvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, new AsyncCallback<DmResult<SaveRecord_Mdl>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<SaveRecord_Mdl> result) {
                    if (result.isStatus()) {
                        final SaveRecord_Mdl srmTemp = result.getData();
                        topControl.setDocId(srmTemp.getDocId());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            tempV_TypeCode = srmTemp.getVoucherTypeCode();
                            tempV_TypeName = srmTemp.getVoucherTypeName();
                            tempV_Cat = srmTemp.getVoucherCategory();
                            tempV_Date = DmShared.dNull(srmTemp.getVoucherDate());
                            tempAcName = DmShared.xNull(srmTemp.getAccountName());
                            tempAcCode = DmShared.xNull(srmTemp.getAccountNameCode());
                            tempAcNature = DmShared.xNull(srmTemp.getAccountNameNature());
                            
                            new DmAlertMessageBox("Last voucher No : "+DmShared.xNull(srmTemp.getRecId()), (DmTextBox) null).show();
//                            new DmConfirmMessageBox("Last voucher No : "+DmShared.xNull(srmTemp.getRecId()), new DialogHideEvent.DialogHideHandler() {
//                                @Override
//                                public void onDialogHide(DialogHideEvent event) {
//                                    
//                                }
//                            }).show();
                                    
                            if (currentWidgetName.equalsIgnoreCase(new lmAccounts().PAYMENT_VOUCHER.caption) || currentWidgetName.equalsIgnoreCase(new lmAccounts().CONTRA_VOUCHER.caption) ) {
                                voucherPrintPopup.show(DmShared.xNull(txtVoucherDate.getText()), DmShared.xNull(txtVoucherDate.getText()),
                                        DmShared.xNull(txtHVoucherType.getTag2nd()), DmShared.xNull(txtHVoucherType.getTag()), DmShared.xNull(txtHVoucherType.getText()),
                                        DmShared.xNull(txtVoucherNo.getText()), topControl.getDocId(), DmShared.xNull(txtHAccountName.getText()), DmShared.iNull(txtHAccountName.getTag()).toString(), currentWidgetName, swi, DmShared.xNull(userControl.getTextBlog(approvedByBlog)));
                                topControl.btnClick("New");
                            } else {
                                new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
                                    @Override
                                    public void onDialogHide(DialogHideEvent event) {
                                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                            voucherPrint_Mdl.setPrintTitle(DmShared.xNull(currentWidgetName));
                                            voucherPrint_Mdl.setAccountCode(DmShared.xNull(txtHAccountName.getTag()));
                                            voucherPrint_Mdl.setAccountName(DmShared.xNull(txtHAccountName.getText()));
                                            voucherPrint_Mdl.setHeaderAccountCode(DmShared.xNull(txtHAccountName.getTag()));
                                            voucherPrint_Mdl.setHeaderAccountName(DmShared.xNull(txtHAccountName.getText()));
                                            voucherPrint_Mdl.setVoucherNo(DmShared.xNull(srmTemp.getRecId()));
                                            voucherPrint_Mdl.setVoucherNoCode(DmShared.xNull(srmTemp.getDocId()));
                                            voucherPrint_Mdl.setVoucherTypeTag(DmShared.xNull(txtHVoucherType.getTag()));
                                            voucherPrint_Mdl.setVoucherTypeText(DmShared.xNull(txtHVoucherType.getText()));
                                            voucherPrint_Mdl.setVoucherCategory(DmShared.xNull(txtHVoucherType.getTag2nd()));
                                            print(true);
                                        }
                                        topControl.btnClick("New");
                                    }
                                }).show();
                            }

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

    private void checkAndSave() {
        final VoucherEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();

            VoucherEntry_Vld.chkSave(mrTemp);

            if (mrTemp.getVoucherCategory().equalsIgnoreCase(lvc.BANK_PAYMENT)) {
                SrvAsync.invokeRPC(true).isNegativeCash(mrTemp.getSharedKeyFieldCode(), DmShared.xNull(mrTemp.getAccountNameCode()), DmShared.vNull(mrTemp.getTotalDebit()), new AsyncCallback<DmResult<Boolean>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<Boolean> result) {
                        if (result.isStatus()) {
                            if (result.getData()) {

                                new DmConfirmMessageBox("Account Name: " + mrTemp.getAccountName() + ", Is Having Negative Balance. Do You Want To Continue?", new DialogHideEvent.DialogHideHandler() {

                                    @Override
                                    public void onDialogHide(DialogHideEvent event) {
                                        if (event.getHideButton() == Dialog.PredefinedButton.NO) {
                                            throw new DmException(600, "Negative Balance");
                                        } else {
                                            topControlSave(mrTemp);
                                        }
                                    }
                                }).show();

                            } else {
                                topControlSave(mrTemp);
                            }
                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            } else {
                topControlSave(mrTemp);
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHAccountName).show();
                break;
            case 700:
                ((CellSelectionModel) grdAccountDetail.getGrid().getSelectionModel()).selectCell(0, 2);
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }

    }

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<VoucherEntry_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<VoucherEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void calculate() {
        int i;
        lbTotallCredit.setText("0");
        lblTotalDebit.setText("0");

        grdAccountDetail.getGrid().getStore().commitChanges();
        List<GrdVoucherEntry_Mdl> listGrid;
        listGrid = grdAccountDetail.getGrid().getStore().getAll();

        for (i = 0; i < listGrid.size(); i++) {
            if (!DmShared.xNull(listGrid.get(i).getAccName()).equalsIgnoreCase("") && !listGrid.get(i).isHeaderAcRecord()) {
                lbTotallCredit.setText(DmClient.vNull(DmShared.vNull(lbTotallCredit.getText()) + DmShared.vNull(listGrid.get(i).getCredit()), "0.00"));
                lblTotalDebit.setText(DmClient.vNull(DmShared.vNull(lblTotalDebit.getText()) + DmShared.vNull(listGrid.get(i).getDebit()), "0.00"));
            }
        }

        if (DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_PAYMENT)
                || DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
            for (i = 0; i < listGrid.size(); i++) {
                if (!DmShared.xNull(listGrid.get(i).getAccCode()).equalsIgnoreCase("") && listGrid.get(i).isHeaderAcRecord()) {
                    GrdVoucherEntry_Mdl node = grdAccountDetail.getGrid().getStore().get(i);
                    if (DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_PAYMENT)) {
                        node.setCredit(DmClient.vNull(DmShared.vNull(lblTotalDebit.getText()).doubleValue() - DmShared.vNull(lbTotallCredit.getText()).doubleValue(), "0.00"));
                        lbTotallCredit.setText(DmClient.vNull(DmShared.vNull(lbTotallCredit.getText()) + DmShared.vNull(listGrid.get(i).getCredit()), "0.00"));
                        break;
                    } else if (DmShared.xNull(txtHVoucherType.getTag2nd()).trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
                        node.setDebit(DmClient.vNull(DmShared.vNull(lbTotallCredit.getText()).doubleValue() - DmShared.vNull(lblTotalDebit.getText()).doubleValue(), "0.00"));
                        lblTotalDebit.setText(DmClient.vNull(DmShared.vNull(lblTotalDebit.getText()) + DmShared.vNull(listGrid.get(i).getDebit()), "0.00"));
                        break;
                    }
                }
            }
        }
        grdAccountDetail.commitChanges();

        lblDiff_DebCre.setTitle("0.00");
        lblDiff_DebCre.setText("0.00");
        if ((DmShared.vNull(lblTotalDebit.getText()).doubleValue() - DmShared.vNull(lbTotallCredit.getText()).doubleValue()) != 0) {
            lblDiff_DebCre.setTitle(DmClient.vNull(DmShared.vNull(lblTotalDebit.getText()).doubleValue() - DmShared.vNull(lbTotallCredit.getText()).doubleValue(), "0.00"));
            if (DmShared.vNull(lblDiff_DebCre.getTitle()).doubleValue() > 0) {
                lblDiff_DebCre.setText("Cr " + DmClient.vNull(DmShared.vNull(lblTotalDebit.getText()).doubleValue() - DmShared.vNull(lbTotallCredit.getText()).doubleValue(), "0.00"));
            } else if (DmShared.vNull(lblDiff_DebCre.getTitle()).doubleValue() < 0) {
                lblDiff_DebCre.setText("Dr " + DmClient.vNull(DmShared.vNull(lbTotallCredit.getText()).doubleValue() - DmShared.vNull(lblTotalDebit.getText()).doubleValue(), "0.00"));
            }
        }
    }

    private void clear() {
        headerAcRow = new GrdVoucherEntry_Mdl();
        headerAcManualCode = "";
        txtWTACode.setText("");
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");
        clearGrid();
    }

    private void clearGrid() {
        grdAccountDetail.getGrid().getStore().clear();
        if (!DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Browse")) {
            grdAccountDetail.getGrid().getStore().add(new GrdVoucherEntry_Mdl());
        }
        lblLedgerBalance.setText("");
        lbTotallCredit.setText("0.00");
        lblTotalDebit.setText("0.00");
        lblDiff_DebCre.setText("0.00");
        lblDiff_DebCre.setTitle("0.00");
        lblPartyBalance.setText("");
    }

    private void topControlMoveRecSuccess(DmResult<VoucherEntry_Mdl> result) {
        VoucherEntry_Mdl mrTemp = null;
        try {
            clear();
            if (result.isStatus()) {
                mrTemp = result.getData();
                setDisplay(mrTemp.getVoucherCategory());

                userControl.setTextBlog(preparedByBlog, mrTemp.getPreparedBy());
                userControl.setTextBlog(modifiedByBlog, mrTemp.getModifiedBy());
                userControl.setTextBlog(approvedByBlog, mrTemp.getApprovedBy());
                topControl.setDocId(mrTemp.getSharedKeyFieldCode());
                txtVoucherNo.setText(mrTemp.getSharedKeyFieldName());
                txtVoucherNo.setTag(mrTemp.getVoucherPrefix());
                txtVoucherDate.setTag(DmShared.iNull(mrTemp.getObjRecId().getCounter()).toString());
                txtVoucherNo.setTag2nd(DmShared.xNull(mrTemp.getObjRecId().getPrefix()));

                txtWTACode.setText(DmShared.xNull(mrTemp.getWtaCode()));

                txtHVoucherType.setText(mrTemp.getVoucherTypeName());
                txtHVoucherType.setTag(mrTemp.getVoucherTypeCode());
                txtHVoucherType.setTag2nd(mrTemp.getVoucherCategory());

                txtHCostCenter.setText(mrTemp.getCosetCenterName());
                txtHCostCenter.setTag(mrTemp.getCosetCenterCode());

                txtVoucherDate.setText(mrTemp.getVoucherDate());
                txtHAccountName.setText(mrTemp.getAccountName());
                txtHAccountName.setTag(mrTemp.getAccountNameCode());
                txtHAccountName.setTag2nd(mrTemp.getAccountNameNature());
                headerAcManualCode = DmShared.xNull(mrTemp.getAccountManualCode());

                txtRefNo.setText(mrTemp.getReferenceNo());
                txtRefDate.setText(mrTemp.getReferenceDate());

                txtRemark.setText(mrTemp.getRemark());

                grdAccountDetail.getGrid().getStore().clear();
                grdAccountDetail.getGrid().getStore().addAll(mrTemp.getgrdData());
                for (GrdVoucherEntry_Mdl node : grdAccountDetail.getGrid().getStore().getAll()) {
                    if (DmShared.iNull(node.getAccCode()).longValue() != 0 && node.isHeaderAcRecord()) {
                        headerAcRow = node.clone();
                    }
                }

            } else {
                new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();

            }
            topControl.setDisplay(true);
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
        }
        calculate();
    }

    public VoucherEntry(UserMenu menuPermission, SetWidgetInvoker swi, String menuText) {
        super();

        SrvAsync= new ServiceInvoker<VoucherEntry_SrvAsync>(GWT.create(VoucherEntry_Srv.class), VoucherEntry.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.currentWidgetName = menuText;
        this.asWidget().setTitle(menuText);

        this.swi = swi;
        lmAccounts pmMenu = new lmAccounts();
        if (menuText.equalsIgnoreCase(pmMenu.PAYMENT_VOUCHER.caption)) {
            this.voucherCategory = lvc.BANK_PAYMENT;

        } else if (menuText.equalsIgnoreCase(pmMenu.RECEIPT_VOUCHER.caption)) {
            this.voucherCategory = lvc.BANK_RECEIPT;

        } else if (menuText.equalsIgnoreCase(pmMenu.JOURNAL_VOUCHER.caption)) {
            this.voucherCategory = lvc.JOURNAL;
            flwlblAccountName.clear();

        } else if (menuText.equalsIgnoreCase(pmMenu.CONTRA_VOUCHER.caption)) {
            this.voucherCategory = lvc.CONTRA;
            flwlblAccountName.clear();

        }
        pmMenu = null;

        topControlEvents(menuPermission);
        textHelpEvents();
        btnApprovedBy.addClickHandler(this);
        radioAll.addClickHandler(this);
        radioPosted.addClickHandler(this);
        radioUnPosted.addClickHandler(this);
        txtVoucherDate.addBlurHandler(this);
        txtRefDate.addBlurHandler(this);
        gridEvents();
        findEvents();
        VoucherPrint();
        swi.setWidget(VoucherEntry.this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private void setDisplay(String category) {
        if (DmShared.xNull(category).trim().equalsIgnoreCase(lvc.BANK_PAYMENT)) {
            lblAccName.setText("Credit A/c");
            txtHAccountName.setEnabled(true);
            txtHVoucherType.setTag2nd(category);
//            grdAccountDetail.getGrid().getColumnModel().setHidden(colIndex_Credit, true);
//            grdAccountDetail.getGrid().getColumnModel().setHidden(colIndex_Debit, false);
        } else if (DmShared.xNull(category).trim().equalsIgnoreCase(lvc.BANK_RECEIPT)) {
            lblAccName.setText("Debit A/c");
            txtHVoucherType.setTag2nd(category);
            txtHAccountName.setEnabled(true);
//            grdAccountDetail.getGrid().getColumnModel().setHidden(colIndex_Debit, true);
//            grdAccountDetail.getGrid().getColumnModel().setHidden(colIndex_Credit, false);
        } else if (DmShared.xNull(category).trim().equalsIgnoreCase(lvc.JOURNAL)
                || DmShared.xNull(category).trim().equalsIgnoreCase(lvc.CONTRA)) {
            lblAccName.setText("A/c Name");
            txtHVoucherType.setTag2nd(category);
            txtHAccountName.setEnabled(false);
            // grdAccountDetail.getGrid().getColumnModel().setHidden(GridIndex.debit, false);
            // grdAccountDetail.getGrid().getColumnModel().setHidden(GridIndex.credit, false);
        } else {
            txtHVoucherType.setTag2nd("");
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            voucherEntryGridEdit.hide();
            voucherPrintPopup.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == btnApprovedBy) {
            if (DmShared.iNull(topControl.getDocId()).longValue() != 0) {
                if (DmShared.xNull(userControl.getTextBlog(approvedByBlog)).equalsIgnoreCase("")) {
                    setApproved(topControl.getDocId(), true);
                } else {
                    setApproved(topControl.getDocId(), false);
                }
            }
        } else if (event.getSource() == radioAll) {
            posted = "A";
            topControlCard();
        } else if (event.getSource() == radioPosted) {
            posted = "P";
            topControlCard();
        } else if (event.getSource() == radioUnPosted) {
            posted = "U";
            topControlCard();
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtVoucherDate) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
            if (isFutureDate(txtVoucherDate.getText())) {
                txtVoucherDate.setText("");
                new DmAlertMessageBox("Voucher Date should be less than or equal to current date.", txtVoucherDate).show();
                return;
            }
        } else if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
        }
    }

    private void setApproved(String docId, boolean isApprove) {
        try {
            if (DmShared.iNull(txtWTACode.getText()).longValue() != 0) {
                walkThrough.show(txtWTACode.getText());
            } else {
                SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, docId, new AsyncCallback<DmResult<String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<String> result) {
                        if (result.isStatus()) {
                            topControlMoveRec(result.getData());
                            topControlCard();
                        } else {
                            topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
                        }
                    }
                });
            }
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private boolean isFutureDate(String date) {
        boolean flag = false;
        if (!DmShared.dNull(date).trim().equals("")) {
            if (DmShared.convertDate(date).getTime() > DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                flag = true;
            }
        }
        return flag;
    }

    //<editor-fold defaultstate="collapsed" desc="Manage TDS Records">
    private void manageTdsRow(GrdVoucherEntry_Mdl voucherEntry_Mdl, String parentAccountCode) {
        List<GrdVoucherEntry_Mdl> existingGridList = new ArrayList<>();
        List<GrdVoucherEntry_Mdl> newList = new ArrayList<>();

        List<Integer> lstIndex = new ArrayList<>();
        int differenceRows = 0;

        for (GrdVoucherEntry_Mdl node : grdAccountDetail.getGrid().getStore().getAll()) {
            if (DmShared.iNull(node.getAccCode()).longValue() != 0 && !node.isHeaderAcRecord()) {
                existingGridList.add(node.clone());
            }

        }

        for (GrdVoucherEntry_Mdl node : existingGridList) {
            String tempParentAccountCode = "";
            if (voucherEntry_Mdl == null) {
                tempParentAccountCode = DmShared.xNull(parentAccountCode);
            } else {
                tempParentAccountCode = voucherEntry_Mdl.getAccCode();
            }
            if (DmShared.iNull(tempParentAccountCode).longValue() > 0) {

                if (DmShared.iNull(node.getParentAccountCode()).longValue() == DmShared.iNull(tempParentAccountCode).longValue()
                        && node.isTdsRecord()) {
                    lstIndex.add(existingGridList.indexOf(node));
                }
            }
        }

        for (int i = 0; i < lstIndex.size(); i++) {
            clearAddGridRow(existingGridList, lstIndex.get(i), true, null);
        }

        if (voucherEntry_Mdl != null) {
            int currentTdsRows = 0;
            if (voucherEntry_Mdl.getTds_Mdl().getTdsGridList().size() > 0) {
                newList.clear();
                newList.addAll(getTDSDetail(voucherEntry_Mdl));
                currentTdsRows = newList.size();
            }
            int previousTdsRows = lstIndex.size();
            differenceRows = previousTdsRows - currentTdsRows;
            int commonRows = (previousTdsRows > currentTdsRows) ? currentTdsRows : previousTdsRows;
            int lastRowIndex = -1;
            for (int i = 0; i < commonRows; i++) {
                GrdVoucherEntry_Mdl details = newList.get(i);
                clearAddGridRow(existingGridList, lstIndex.get(i), false, details);
                lastRowIndex = i;
            }

            if (differenceRows < 0) {
                for (int i = 0; i < (differenceRows * -1); i++) {
                    int newRowIndex = existingGridList.size() - 1;
                    if (DmShared.iNull(existingGridList.get(newRowIndex).getAccCode()).longValue() > 0) {
                        existingGridList.add(new GrdVoucherEntry_Mdl());
                    }
                    newRowIndex = existingGridList.size() - 1;
                    lastRowIndex++;
                    GrdVoucherEntry_Mdl details = newList.get(lastRowIndex);
                    clearAddGridRow(existingGridList, newRowIndex, false, details);
                }
            }
        }

        grdAccountDetail.getGrid().getStore().clear();
        grdAccountDetail.getGrid().getStore().addAll(existingGridList);
        if (DmShared.iNull(headerAcRow.getAccCode()).longValue() != 0 && headerAcRow.isHeaderAcRecord()) {
            grdAccountDetail.getGrid().getStore().add(headerAcRow.clone());
        }

        if (voucherEntry_Mdl != null) {
            grdAccountDetail.getGrid().getStore().add(new GrdVoucherEntry_Mdl());
        }

        grdAccountDetail.commitChanges();

        if (differenceRows > 0 || voucherEntry_Mdl == null) {
            List<GrdVoucherEntry_Mdl> grdLstVrDetail_Mdl = new ArrayList<>();

            grdLstVrDetail_Mdl.addAll(grdAccountDetail.getGrid().getStore().getAll());

            for (GrdVoucherEntry_Mdl node : grdAccountDetail.getGrid().getStore().getAll()) {
                if (node.isTdsRecord()
                        && DmShared.iNull(node.getParentAccountCode()).longValue() == 0) {
                    grdLstVrDetail_Mdl.remove(node);
                }
            }

            if (grdLstVrDetail_Mdl.size() < grdAccountDetail.getGrid().getStore().getAll().size()) {
                grdAccountDetail.getGrid().getStore().clear();
                grdAccountDetail.getGrid().getStore().addAll(grdLstVrDetail_Mdl);
                int size = grdAccountDetail.getGrid().getStore().size() - 1;
                GrdVoucherEntry_Mdl grdVoucherEntry_Mdl = grdAccountDetail.getGrid().getStore().get(size);

                if (DmShared.iNull(grdVoucherEntry_Mdl.getAccCode()).longValue() > 0) {
                    grdAccountDetail.getGrid().getStore().add(new GrdVoucherEntry_Mdl());
                }
                grdAccountDetail.commitChanges();
            }
        }

        calculate();
    }

    private List<GrdVoucherEntry_Mdl> getTDSDetail(GrdVoucherEntry_Mdl voucherEntryMdl) {
        List<GrdVoucherEntry_Mdl> lstTDSDetail = new ArrayList<>();

        for (TdsCategoryGrid_Mdl node : voucherEntryMdl.getTds_Mdl().getTdsGridList()) {
            GrdVoucherEntry_Mdl tdsDetail = new GrdVoucherEntry_Mdl();
            tdsDetail.setAccCode(DmShared.xNull(node.getPostingAccountCode()));
            tdsDetail.setParentAccountCode(DmShared.xNull(voucherEntryMdl.getParentAccountCode()));
            tdsDetail.setAccName(DmShared.xNull(node.getPostingAccount()));

            tdsDetail.setAccMCode(DmShared.xNull(node.getPostingManualCode()));
            tdsDetail.setNarration(DmShared.xNull(voucherEntryMdl.getNarration()));
            tdsDetail.setCredit(null);
            tdsDetail.setDebit(null);
            if (DmShared.xNull(voucherEntryMdl.getTds_Mdl().getTdsForTag()).equalsIgnoreCase("P")) {
                if (DmShared.vNull(node.getAmount()).doubleValue() > 0) {
                    tdsDetail.setCredit(DmShared.xNull(node.getAmount()));
                }

            } else if (DmShared.xNull(voucherEntryMdl.getTds_Mdl().getTdsForTag()).equalsIgnoreCase("R")) {
                if (DmShared.vNull(node.getAmount()).doubleValue() > 0) {
                    tdsDetail.setDebit(DmShared.xNull(node.getAmount()));
                }

            }

            tdsDetail.setTdsRecord(true);
            lstTDSDetail.add(tdsDetail);

        }

        GrdVoucherEntry_Mdl tdsDetail = new GrdVoucherEntry_Mdl();

        tdsDetail.setAccCode(DmShared.xNull(voucherEntryMdl.getTds_Mdl().getTdsDeductFormCode()));
        tdsDetail.setParentAccountCode(DmShared.xNull(voucherEntryMdl.getParentAccountCode()));
        tdsDetail.setAccName(DmShared.xNull(voucherEntryMdl.getTds_Mdl().getTdsDeductFormName()));
        tdsDetail.setAccMCode(DmShared.xNull(voucherEntryMdl.getTds_Mdl().getTdsDeductManualCode()));

        tdsDetail.setNarration(DmShared.xNull(voucherEntryMdl.getNarration()));
        double totalValue = 0d;
        for (TdsCategoryGrid_Mdl node1 : voucherEntryMdl.getTds_Mdl().getTdsGridList()) {
            totalValue = totalValue + DmShared.vNull(node1.getAmount());
        }

        if (DmShared.xNull(voucherEntryMdl.getTds_Mdl().getTdsForTag()).equalsIgnoreCase("P")) {

            tdsDetail.setDebit(DmClient.vNull(totalValue, "0.00"));

        } else if (DmShared.xNull(voucherEntryMdl.getTds_Mdl().getTdsForTag()).equalsIgnoreCase("R")) {

            tdsDetail.setCredit(DmClient.vNull(totalValue, "0.00"));

        }

        tdsDetail.setTdsRecord(true);
        lstTDSDetail.add(tdsDetail);

        return lstTDSDetail;
    }

    private void clearAddGridRow(List<GrdVoucherEntry_Mdl> grdVrList, int rowIndex, boolean isClear, GrdVoucherEntry_Mdl headerAcRow) {
//        GrdVoucherEntry_Mdl grdVoucherEntry = grdAccountDetail.getGrid().getStore().get(rowIndex);
        GrdVoucherEntry_Mdl grdVoucherEntry = grdVrList.get(rowIndex);
        if (isClear) {
            TDS_Mdl tDS_Mdl = new TDS_Mdl();
            ChequeDetail_Mdl chequeDetail_Mdl = new ChequeDetail_Mdl();
            List<CostCenterGrid_Mdl> listCostCenterGrid = new ArrayList();
            List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
            List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();

            grdVoucherEntry.setSharedKeyFieldCode("");
            grdVoucherEntry.setSharedKeyFieldName("");
            grdVoucherEntry.setVSNo("");
            grdVoucherEntry.setAccNature("");
            grdVoucherEntry.setAccName("");
            grdVoucherEntry.setAccCode("");
            grdVoucherEntry.setAccMCode("");
            grdVoucherEntry.setNarration("");
            grdVoucherEntry.setDebit("");
            grdVoucherEntry.setCredit("");
            grdVoucherEntry.setTdsRecord(true);
            grdVoucherEntry.setHeaderAcRecord(false);
            grdVoucherEntry.setParentAccountCode("");
            grdVoucherEntry.setTdsOfV_Sno("");
            grdVoucherEntry.setTan("");
            grdVoucherEntry.setPan("");

            grdVoucherEntry.setTds_Mdl(tDS_Mdl);

            grdVoucherEntry.setChequeDetail_Mdl(chequeDetail_Mdl);

            grdVoucherEntry.setCostCenterGrdData(listCostCenterGrid);

            grdVoucherEntry.setLstAdjustments(lstAdjustments);

            grdVoucherEntry.setLstItemAdjustments(lstItemAdjustments);
        } else {

            TDS_Mdl tDS_Mdl = new TDS_Mdl();
            ChequeDetail_Mdl chequeDetail_Mdl = new ChequeDetail_Mdl();
            List<CostCenterGrid_Mdl> listCostCenterGrid = new ArrayList();
            List<GrdAdjustment_Mdl> lstAdjustments = new ArrayList<>();
            List<GrdItemAdjustment_Mdl> lstItemAdjustments = new ArrayList<>();

            grdVoucherEntry.setVSNo(headerAcRow.getVSNo());
            grdVoucherEntry.setAccNature(headerAcRow.getAccNature());
            grdVoucherEntry.setAccName(headerAcRow.getAccName());
            grdVoucherEntry.setAccCode(headerAcRow.getAccCode());
            grdVoucherEntry.setAccMCode(headerAcRow.getAccMCode());
            grdVoucherEntry.setNarration(headerAcRow.getNarration());
            grdVoucherEntry.setDebit(headerAcRow.getDebit());
            grdVoucherEntry.setCredit(headerAcRow.getCredit());
            grdVoucherEntry.setTdsRecord(headerAcRow.isTdsRecord());
            grdVoucherEntry.setHeaderAcRecord(headerAcRow.isHeaderAcRecord());
            grdVoucherEntry.setParentAccountCode(headerAcRow.getParentAccountCode());
            grdVoucherEntry.setTdsOfV_Sno(headerAcRow.getTdsOfV_Sno());
            grdVoucherEntry.setTan(DmShared.xNull(headerAcRow.getTan()));
            grdVoucherEntry.setPan(DmShared.xNull(headerAcRow.getPan()));

            if (tDS_Mdl != null) {
                tDS_Mdl = headerAcRow.getTds_Mdl().clone();
                grdVoucherEntry.setTds_Mdl(tDS_Mdl);
            }

            if (chequeDetail_Mdl != null) {
                chequeDetail_Mdl = headerAcRow.getChequeDetail_Mdl().clone();
                grdVoucherEntry.setChequeDetail_Mdl(chequeDetail_Mdl);
            }

            if (headerAcRow.getCostCenterGrdData() != null) {
                for (int i = 0; i < headerAcRow.getCostCenterGrdData().size(); i++) {
                    listCostCenterGrid.add(headerAcRow.getCostCenterGrdData().get(i).clone());
                }
                grdVoucherEntry.setCostCenterGrdData(listCostCenterGrid);
            }

            if (headerAcRow.getLstAdjustments() != null) {
                for (int i = 0; i < headerAcRow.getLstAdjustments().size(); i++) {
                    lstAdjustments.add(headerAcRow.getLstAdjustments().get(i).clone());
                }
                grdVoucherEntry.setLstAdjustments(lstAdjustments);
            }

            if (headerAcRow.getLstItemAdjustments() != null) {
                for (int i = 0; i < headerAcRow.getLstItemAdjustments().size(); i++) {
                    lstItemAdjustments.add(headerAcRow.getLstItemAdjustments().get(i).clone());
                }
                grdVoucherEntry.setLstItemAdjustments(lstItemAdjustments);
            }
        }
    }
    //</editor-fold>
}
