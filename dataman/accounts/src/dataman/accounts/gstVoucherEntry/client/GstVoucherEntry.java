package dataman.accounts.gstVoucherEntry.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.accounts.gstVoucherEntry.shared.*;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
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
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.walkThrough.client.WalkThrough;
import dataman.libraryMenu.self.shared.LibraryVoucherCategory;
import java.util.HashMap;
import java.util.List;

public class GstVoucherEntry extends GstVoucherEntry_Dsg implements AttachEvent.Handler, DmRootEvents, ClickHandler, BlurHandler {

    ServiceInvoker<GstVoucherEntry_SrvAsync> SrvAsync;
    LibraryVoucherCategory lvc = new LibraryVoucherCategory();
    final AcDetailsGridEdit<GridAcDetailsMdl, Grd_PrpAcs> acDetailsGridEdit = new AcDetailsGridEdit(grdAccountDetail);
//    final HSNDetailsGridEdit<GridHSNDetailsMdl, Grd_PrpAcs> hsnDetailsGridEdit = new HSNDetailsGridEdit(grdHsnDetail);
    private String tempV_TypeCode = "", tempV_TypeName = "", tempV_Cat = "", tempV_Date = "";
    WalkThrough walkThrough;
    protected DefaultValue_Mdl defaultValue;
    String partyType = "", selectedAcCode = "", selectedPlaceOfSupply = "";
    boolean isIGST = false;
    double totalDebit = 0, totalCredit = 0;

    public GstVoucherEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        SrvAsync = new ServiceInvoker<GstVoucherEntry_SrvAsync>(GWT.create(GstVoucherEntry_Srv.class), GstVoucherEntry.class.getName());
        walkThrough = new WalkThrough(SrvAsync);
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        handlerEvents();
        iniDefaultValues(swi);
    }

    private void gridEvents() {
        //<editor-fold defaultstate="collapsed" desc="AC GRID">
        acDetailsGridEdit.addCallBack(new AcDetailsGridEdit.CallBack() {

            @Override
            public void onOk(GridAcDetailsMdl grdAcDetl) {
                grdAccountDetail.commitChanges();
                calculate();
            }

            @Override
            public void onDelete() {
                grdAccountDetail.getGrid().getStore().add(new GridAcDetailsMdl());
                grdAccountDetail.commitChanges();
                calculate();
            }
        });

        grdAccountDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showAcDetailsGridEdit(event.getRowIndex());
            }
        });

        grdAccountDetail.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    showAcDetailsGridEdit(grdAccountDetail.getGrid().getStore().indexOf(grdAccountDetail.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());
    }

    private void showAcDetailsGridEdit(int currentRowIndex) {

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            acDetailsGridEdit.show(currentRowIndex, this, false, false, false);
        } else {
            if (DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.DEBIT_NOTE_GST)
                    || DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
                if (DmShared.xNull(txtHVoucherType.getTag()).trim().equals("")) {
                    new DmAlertMessageBox("Please select Voucher Type", txtHVoucherType).show();
                    return;
                }
                if (DmShared.dNull(txtVoucherDate.getText()).trim().equals("")) {
                    new DmAlertMessageBox("Please fiil Voucher Date", txtVoucherDate).show();
                    return;
                }
                if (DmShared.xNull(txtRefNo.getText()).trim().equals("")) {
                    new DmAlertMessageBox("Please enter Reference No.", txtRefNo).show();
                    return;
                }
                if (DmShared.dNull(txtRefDate.getText()).trim().equals("")) {
                    new DmAlertMessageBox("Please enter Reference Date.", txtRefDate).show();
                    return;
                }
                if ((DmShared.convertDate(txtVoucherDate.getText()).getTime() < (DmShared.convertDate(txtRefDate.getText()).getTime()))) {
                    new DmAlertMessageBox("Voucher Date should be greater than or equal to Ref. Date.", txtVoucherDate).show();
                    return;
                }
            }
            acDetailsGridEdit.show(currentRowIndex, this, true, true, true);
        }
    }

    private void handlerEvents() {
        btnApprovedBy.addClickHandler(this);
        txtVoucherDate.addBlurHandler(this);
        txtRefDate.addBlurHandler(this);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    manageDisplay(false);
                    swi.setWidget(GstVoucherEntry.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void setDefault() {
        txtHRcm.setTag("0");
        txtHRcm.setText("No");
        txtHGSTRounding.setTag(defaultValue.getRoundOffGSTCode());
        txtHGSTRounding.setText(defaultValue.getRoundOffGSTName());
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), txtHVoucherType.getTag2nd(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{lvc.CREDIT_INVOICE_GST, lvc.CREDIT_NOTE_GST, lvc.DEBIT_NOTE_GST, lvc.DEBIT_INVOICE_GST})) {

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
                onEdit();
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
                prnRecord(false);

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                save();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(GstVoucherEntry.this);
            }
        });
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(topControl.getPermissions(), txtHVoucherType.getTag2nd(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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

    private void save() {
        final GstVoucherEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();

            GstVoucherEntry_Vld.chkSave(mrTemp);

            topControlSave(mrTemp);

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage());
        }
    }

    private void topControlSave(GstVoucherEntry_Mdl mrTemp) {

        try {
            tempV_TypeCode = "";
            tempV_TypeName = "";
            tempV_Cat = "";
            tempV_Date = "";

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

    private void topControlMoveRec(String strKey) {
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<GstVoucherEntry_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<GstVoucherEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void calculateGrid() {
        for (GridAcDetailsMdl mdl : grdAccountDetail.getGrid().getStore().getAll()) {
            if (DmShared.iNull(mdl.getHsnCode()).longValue() > 0) {
                double cgstAmt = 0, sgstAmt = 0, igstAmt = 0, ugstAmt = 0, netAmt = 0, taxableAmt = 0, totalTax = 0;

                if (DmShared.iNull(txtHRcm.getTag()).intValue() == 1) {
                    mdl.setIsRCM("1");
                    mdl.setNetAmt(mdl.getTaxableAmt());
                }
                if (!DmShared.xNull(txtHGSTRounding.getTag()).equalsIgnoreCase("")) {
                    mdl.setRoundOffGST(DmShared.xNull(txtHGSTRounding.getTag()));

                    taxableAmt = DmShared.vNull(mdl.getTaxableAmt()).doubleValue();

                    cgstAmt = roundOff((taxableAmt * DmShared.vNull(mdl.getCgstPer()).doubleValue() * 0.01), txtHGSTRounding.getTag());
                    igstAmt = roundOff((taxableAmt * DmShared.vNull(mdl.getIgstPer()).doubleValue() * 0.01), txtHGSTRounding.getTag());
                    ugstAmt = roundOff((taxableAmt * DmShared.vNull(mdl.getUgstPer()).doubleValue() * 0.01), txtHGSTRounding.getTag());
                    sgstAmt = roundOff((taxableAmt * DmShared.vNull(mdl.getSgstPer()).doubleValue() * 0.01), txtHGSTRounding.getTag());

                    totalTax = cgstAmt + igstAmt + ugstAmt + sgstAmt;
                    if (DmShared.iNull(txtHRcm.getTag()).intValue() == 1) {
                        netAmt = taxableAmt;
                    } else {
                        netAmt = GstVoucherEntry.roundOff((taxableAmt + totalTax), txtHGSTRounding.getTag());
                    }

                    mdl.setGrossAmt(DmClient.vNull(taxableAmt, "0.00"));
                    mdl.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
                    mdl.setCgstAmt(DmClient.vNull(cgstAmt, "0.00"));
                    mdl.setSgstAmt(DmClient.vNull(sgstAmt, "0.00"));
                    mdl.setUgstAmt(DmClient.vNull(ugstAmt, "0.00"));
                    mdl.setSgstUgstAmt(DmClient.vNull(sgstAmt + ugstAmt, "0.00"));
                    mdl.setIgstAmt(DmClient.vNull(igstAmt, "0.00"));
                    mdl.setNetAmt(DmClient.vNull(netAmt, "0.00"));
                }
            }
        }
        grdAccountDetail.commitChanges();
    }

    private void topControlMoveRecSuccess(DmResult<GstVoucherEntry_Mdl> result) {
        GstVoucherEntry_Mdl mrTemp = null;
        try {
            clear();
            if (result.isStatus()) {
                mrTemp = result.getData();

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

                txtVoucherDate.setText(mrTemp.getVoucherDate());
                txtHAccountName.setText(mrTemp.getAccountName());
                txtHAccountName.setTag(mrTemp.getAccountNameCode());
                txtHAccountName.setTag2nd(mrTemp.getAccountNameNature());
//                txtHRefVoucher.setText(DmShared.iNull(mrTemp.getRefDocId()).longValue() > 0 ? mrTemp.getReferenceNo() : "");
                txtHRefVoucher.setText(DmShared.xNull(mrTemp.getRefRecId()));  //Ag - TBC
                txtHRefVoucher.setTag(mrTemp.getRefDocId());  //Ag - TBC
                txtHRefVoucher.setTag2nd(DmShared.dNull(mrTemp.getRefV_Date()));  //Ag - TBC
                txtRefNo.setText(mrTemp.getReferenceNo());
                txtRefNo.setTag(DmShared.xNull(mrTemp.getRefV_SNo()));
                txtRefNo.setTag2nd(DmShared.xNull(mrTemp.getRefV_Type()));
                txtRefDate.setText(mrTemp.getReferenceDate());
                txtRefDate.setTag(DmClient.vNull(mrTemp.getRefDocValue(), "0.00"));
                txtHReason.setTag(mrTemp.getGstReasonCode());
                txtHReason.setText(mrTemp.getGstReasonName());

                selectedPlaceOfSupply = mrTemp.getPlaceOfSupplyCode();
                txtHState.setTag(mrTemp.getPlaceOfSupplyCode());
                txtHState.setText(mrTemp.getPlaceOfSupplyName());

                txtHGSTType.setTag(mrTemp.getGstTypeCode());
                txtHGSTType.setText(mrTemp.getGstTypeName());

                if (mrTemp.getGrdAcDetails().size() > 0) {
                    txtHRcm.setTag(DmShared.iNull(mrTemp.getGrdAcDetails().get(0).getIsRCM()).toString());
                    if (DmShared.iNull(mrTemp.getGrdAcDetails().get(0).getIsRCM()).intValue() == 0) {
                        txtHRcm.setText("No");
                    } else if (DmShared.iNull(mrTemp.getGrdAcDetails().get(0).getIsRCM()).intValue() == 1) {
                        txtHRcm.setText("Yes");
                    }

                    txtHGSTRounding.setTag(DmShared.xNull(mrTemp.getGrdAcDetails().get(0).getRoundOffGST()));
                    if (DmShared.xNull(mrTemp.getGrdAcDetails().get(0).getRoundOffGST()).equalsIgnoreCase("Y")) {
                        txtHGSTRounding.setText("Yes");
                    } else if (DmShared.xNull(mrTemp.getGrdAcDetails().get(0).getRoundOffGST()).equalsIgnoreCase("N")) {
                        txtHGSTRounding.setText("No");
                    } else if (DmShared.xNull(mrTemp.getGrdAcDetails().get(0).getRoundOffGST()).equalsIgnoreCase("D")) {
                        txtHGSTRounding.setText("Disable");
                    } else if (DmShared.xNull(mrTemp.getGrdAcDetails().get(0).getRoundOffGST()).equalsIgnoreCase("H")) {
                        txtHGSTRounding.setText("Higher");
                    } else if (DmShared.xNull(mrTemp.getGrdAcDetails().get(0).getRoundOffGST()).equalsIgnoreCase("L")) {
                        txtHGSTRounding.setText("Lower");
                    }
                }

                if (DmShared.iNull(mrTemp.getPlaceOfSupplyCode()).intValue() != DmShared.iNull(defaultValue.getSiteStateCode()).intValue()) {
                    isIGST = true;
                }

                partyType = mrTemp.getConsigneeType();
                selectedAcCode = mrTemp.getAccountNameCode();

                txtRemark.setText(mrTemp.getRemark());

                grdAccountDetail.getGrid().getStore().clear();
                grdAccountDetail.getGrid().getStore().addAll(mrTemp.getGrdAcDetails());

                calculate();
            } else {
                new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
            }

            topControl.setDisplay(true);
            manageDisplay(false);
        } catch (Exception ex) {
            new DmAlertMessageBox(ex.getMessage(), (DmTextBox) null).show();
        }
    }

    private GstVoucherEntry_Mdl topControlSaveInitialize() {
        GstVoucherEntry_Mdl mrTemp = new GstVoucherEntry_Mdl();
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

        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVoucherType.getText()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVoucherType.getTag()));
        mrTemp.setVoucherCategory(DmShared.xNull(txtHVoucherType.getTag2nd()));

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtVoucherNo.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));

        mrTemp.setReferenceNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setReferenceDate(DmShared.dNull(txtRefDate.getText()));

        mrTemp.setRefRecId(DmShared.xNull(txtHRefVoucher.getText()));     //Ag - TBC
        mrTemp.setRefV_Date(DmShared.dNull(txtHRefVoucher.getTag2nd()));     //Ag - TBC
        mrTemp.setRefDocId(DmShared.xNull(txtHRefVoucher.getTag()));     //Ag - TBC
        mrTemp.setRefV_SNo(DmShared.xNull(txtRefNo.getTag()));     //Ag - TBC
        mrTemp.setRefV_Type(DmShared.xNull(txtRefNo.getTag2nd()));     //Ag - TBC
        mrTemp.setRefDocValue(DmClient.vNull(txtRefDate.getTag(), "0.00"));     //Ag - TBC

        mrTemp.setGstReasonCode(DmShared.xNull(txtHReason.getTag()));
        mrTemp.setGstReasonName(DmShared.xNull(txtHReason.getText()));

        mrTemp.setAccountName(DmShared.xNull(txtHAccountName.getText()));

        mrTemp.setAccountNameCode(DmShared.xNull(txtHAccountName.getTag()));
        mrTemp.setAccountNameNature(DmShared.xNull(txtHAccountName.getTag2nd()));

        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setPlaceOfSupplyCode(DmShared.xNull(txtHState.getTag()));
        mrTemp.setPlaceOfSupplyName(DmShared.xNull(txtHState.getText()));
        mrTemp.setConsigneeType(DmShared.xNull(partyType));
        mrTemp.setGstTypeCode(DmShared.xNull(txtHGSTType.getTag()));
        mrTemp.setGstTypeName(DmShared.xNull(txtHGSTType.getText()));
        mrTemp.setDefaultValueMdl(defaultValue);
        mrTemp.getGrdAcDetails().clear();
//        mrTemp.getGrdHsnDetails().clear();
        mrTemp.getGrdAcDetails().addAll(grdAccountDetail.getGrid().getStore().getAll());
//        mrTemp.getGrdHsnDetails().addAll(grdHsnDetail.getGrid().getStore().getAll());

        mrTemp.setTotalCredit(DmClient.vNull(totalDebit, "0.00"));
        mrTemp.setTotalDebit(DmClient.vNull(totalCredit, "0.00"));

        return mrTemp;
    }

    private void calculate() {
        double totalAcAmount = 0, totalCgstAmt = 0, totalUgstAmt = 0, totalSgstAmt = 0, totalIgstAmt = 0, totalTaxbleAmt = 0;

        totalDebit = 0;
        totalCredit = 0;

        for (GridAcDetailsMdl mdlTemp : grdAccountDetail.getGrid().getStore().getAll()) {
            if (DmShared.iNull(mdlTemp.getAccCode()).longValue() > 0) {
                totalDebit += DmShared.vNull(mdlTemp.getDebit()).doubleValue();
                totalCredit += DmShared.vNull(mdlTemp.getCredit()).doubleValue();
                totalAcAmount += DmShared.vNull(mdlTemp.getAmount()).doubleValue();
            }
            if (DmShared.iNull(mdlTemp.getHsnCode()).longValue() > 0) {
                totalTaxbleAmt += DmShared.vNull(mdlTemp.getTaxableAmt()).doubleValue();
                totalSgstAmt += DmShared.vNull(mdlTemp.getSgstAmt()).doubleValue();
                totalUgstAmt += DmShared.vNull(mdlTemp.getUgstAmt()).doubleValue();
                totalCgstAmt += DmShared.vNull(mdlTemp.getCgstAmt()).doubleValue();
                totalIgstAmt += DmShared.vNull(mdlTemp.getIgstAmt()).doubleValue();
            }
        }

//        for (GridHSNDetailsMdl mdl : grdHsnDetail.getGrid().getStore().getAll()) {
//            if (DmShared.iNull(mdl.getHsnCode()).longValue() > 0) {
//                totalTaxbleAmt += DmShared.vNull(mdl.getTaxableAmt()).doubleValue();
//                totalSgstAmt += DmShared.vNull(mdl.getSgstAmt()).doubleValue();
//                totalUgstAmt += DmShared.vNull(mdl.getUgstAmt()).doubleValue();
//                totalCgstAmt += DmShared.vNull(mdl.getCgstAmt()).doubleValue();
//                totalIgstAmt += DmShared.vNull(mdl.getIgstAmt()).doubleValue();
//            }
//        }

        lblStripHSNTaxableAmtTotal.setText(DmClient.vNull(totalTaxbleAmt, "0.00"));
        lblStripHSNCGSTAmtTotal.setText(DmClient.vNull(totalCgstAmt, "0.00"));
        lblStripHSNSGSTAmtTotal.setText(DmClient.vNull(totalSgstAmt, "0.00"));
        lblStripHSNIGSTAmtTotal.setText(DmClient.vNull(totalIgstAmt, "0.00"));
        lblStripHSNUGSTAmtTotal.setText(DmClient.vNull(totalUgstAmt, "0.00"));
        lblStripHSN_SGST_UGSTAmtTotal.setText(DmClient.vNull(totalUgstAmt + totalSgstAmt, "0.00"));
        lblStripAcAmtTotal.setText(DmClient.vNull(totalAcAmount, "0.00"));
    }

    public static double roundOff(double value, String rounding) {
        if (DmShared.xNull(rounding).trim().equalsIgnoreCase("D")) {
            return DmShared.vNull(DmClient.vNull(value, "0.00"));
        } else {
            if (DmShared.xNull(rounding).trim().equalsIgnoreCase("Y")) {
                return DmShared.vNull(DmClient.vNull(value, "0"));
            } else if (DmShared.xNull(rounding).trim().equalsIgnoreCase("H")) {
                return Math.ceil(value);
            } else if (DmShared.xNull(rounding).trim().equalsIgnoreCase("L")) {
                return Math.floor(value);
            }
        }
        return value;
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

    private void onAdd() {
        try {
            clear();
            setDefault();
            manageDisplay(true);
            if (!DmShared.xNull(tempV_TypeCode).trim().equalsIgnoreCase("")) {
                txtHVoucherType.setText(tempV_TypeName);
                txtHVoucherType.setTag(tempV_TypeCode);
                txtHVoucherType.setTag2nd(tempV_Cat);
                iniGstType(tempV_Cat);
                txtVoucherDate.setText(tempV_Date);

                enableDisableFields();
            }
            getServerTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onEdit() {
        try {
            manageDisplay(true);
            enableDisableFields();
            txtHAccountName.setFocus(true);

            grdAccountDetail.getGrid().getStore().add(new GridAcDetailsMdl());
            } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
            e.printStackTrace();
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

        txtHReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, Object t1Row) {
            }
        });

        txtHVoucherType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVoucherType_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVoucherType_Mdl>>>() {
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
                if (blnStatus && tRow != null) {
                    txtHVoucherType.setTag2nd(tRow.getCategory());
                    iniGstType(tRow.getCategory());
                    enableDisableFields();
                }
            }

        });

        txtHGSTRounding.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRoundOffGst(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGSTRounding.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    calculateGrid();
                }
            }

        });

        txtHGSTRounding.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });
        txtHRcm.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });
        txtHState.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });

        txtHAccountName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHAccountName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, DmShared.xNull(txtHVoucherType.getTag()), txtVoucherDate.getText(), new AsyncCallback<DmResultPageing<List<TxtHAccountName_Mdl>>>() {

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
                if (blnStatus && tRow != null) {
                    if (selectedAcCode != tRow.getSharedKeyFieldCode()) {
                        selectedAcCode = tRow.getSharedKeyFieldCode();
                        txtHRefVoucher.setText("");
                        txtHRefVoucher.setTag("");
                        txtHRefVoucher.setTag2nd("");

                        txtHReason.setText("");
                        txtHReason.setTag("");
                        txtHReason.setTag2nd("");

                        txtRefNo.setText("");
                        txtRefNo.setTag("");
                        txtRefNo.setTag2nd("");

                        txtRefDate.setText("");
                        txtRefDate.setTag("");
                        txtRefDate.setTag2nd("");
                    }
                    txtHAccountName.setTag2nd(tRow.getNatureManualCode());
                    partyType = tRow.getPartyTypeCode();
                    txtHState.setText(tRow.getStateName());
                    txtHState.setTag(tRow.getStateCode());
                    selectedPlaceOfSupply = tRow.getStateCode();
                    if (DmShared.iNull(tRow.getStateCode()).intValue() != DmShared.iNull(defaultValue.getSiteStateCode()).intValue()) {
                        isIGST = true;
                    }
                }
            }
        });

        txtHGSTType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGstType(DmShared.xNull(txtHVoucherType.getTag2nd()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGSTType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                }
            }
        });

        txtHRcm.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRcm.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    calculateGrid();
                }
            }
        });

        txtHState.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHState(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHState).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHState.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHState).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    if (DmShared.iNull(tRow.getSharedKeyFieldCode()).intValue() != DmShared.iNull(defaultValue.getSiteStateCode()).intValue()) {
                        isIGST = true;
                    }
                    if (DmShared.iNull(selectedPlaceOfSupply).intValue() != DmShared.iNull(tRow.getSharedKeyFieldCode()).intValue()) {
                        selectedPlaceOfSupply = tRow.getSharedKeyFieldCode();
                        clearGrid(true, false);
                    }
                }
            }
        });

        txtHRefVoucher.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHRefVoucher_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRefVoucher(helper, blnToEndOfFile, txtHAccountName.getTag(), new AsyncCallback<DmResultPageing<List<TxtHRefVoucher_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHState).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHRefVoucher_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRefVoucher.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), txtHState).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHRefVoucher_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    if (DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.DEBIT_NOTE_GST)
                            || DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
                        txtRefNo.setText(DmShared.xNull(t1Row.getRefNo()));
                        txtRefNo.setTag(DmShared.xNull(t1Row.getRefV_SNo()));
                        txtRefNo.setTag2nd(DmShared.xNull(t1Row.getRefV_Type()));
                        txtRefDate.setText(DmShared.dNull(t1Row.getRefDate()));
                        txtRefDate.setTag(DmShared.dNull(t1Row.getRefDocValue()));
                        txtRefDate.setTag2nd(DmShared.dNull(t1Row.getRefDocType()));
                        txtHRefVoucher.setTag2nd(DmShared.dNull(t1Row.getRefV_Date()));

                        if (txtRefNo.isEnabled()) {
                            txtRefNo.setEnabled(false);
                        }
                        if (txtRefDate.isEnabled()) {
                            txtRefDate.setEnabled(false);
                        }
                    }
                } else {
                    txtRefNo.setText("");
                    txtRefNo.setTag("");
                    txtRefNo.setTag2nd("");

                    txtRefDate.setText("");
                    txtRefDate.setTag("");
                    txtRefDate.setTag2nd("");

                    if (!txtRefNo.isEnabled()) {
                        txtRefNo.setEnabled(true);
                    }
                    if (!txtRefDate.isEnabled()) {
                        txtRefDate.setEnabled(true);
                    }
                }
            }
        });
    }

    private void enableDisableFields() {
        if (DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
            txtHRcm.setEnabled(true);
        } else {
            txtHRcm.setEnabled(false);
        }
        if (DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.DEBIT_INVOICE_GST)) {
            txtHRefVoucher.setEnabled(false);
            txtHReason.setEnabled(false);
            txtRefNo.setEnabled(false);
            txtRefDate.setEnabled(false);

            txtHRefVoucher.setText("");
            txtHRefVoucher.setTag("");
            txtHRefVoucher.setTag2nd("");

            txtHReason.setText("");
            txtHReason.setTag("");
            txtHReason.setTag2nd("");

            txtRefNo.setText("");
            txtRefNo.setTag("");
            txtRefNo.setTag2nd("");

            txtRefDate.setText("");
            txtRefDate.setTag("");
            txtRefDate.setTag2nd("");
        }
        if (DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)
                || DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.DEBIT_NOTE_GST)
                || DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
            if (DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.DEBIT_NOTE_GST)
                    || DmShared.xNull(txtHVoucherType.getTag2nd()).equalsIgnoreCase(lvc.CREDIT_NOTE_GST)) {
                if (!txtHRefVoucher.isEnabled()) {
                    txtHRefVoucher.setEnabled(true);
                }
                if (!txtHReason.isEnabled()) {
                    txtHReason.setEnabled(true);
                }
            } else {
                txtHRefVoucher.setText("");
                txtHRefVoucher.setTag("");
                txtHRefVoucher.setTag2nd("");

                txtHReason.setText("");
                txtHReason.setTag("");
                txtHReason.setTag2nd("");

                txtHRefVoucher.setEnabled(false);
                txtHReason.setEnabled(false);
            }
            if (!txtRefNo.isEnabled()) {
                txtRefNo.setEnabled(true);
            }
            if (!txtRefDate.isEnabled()) {
                txtRefDate.setEnabled(true);
            }
        }
    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVoucherType).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtVoucherDate.setText(result[0]);
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtHVoucherType.setFocus(true);
                    }
                });
            }
        });
    }

    private void clear() {
        totalDebit = 0;
        totalCredit = 0;

        selectedAcCode = "";
        selectedPlaceOfSupply = "";

        txtWTACode.setText("");
        txtHGSTRounding.setText("");
        txtHGSTRounding.setTag("");
        txtHGSTRounding.setTag2nd("");

        txtHRcm.setText("");
        txtHRcm.setTag("");
        txtHRcm.setTag2nd("");

        txtHState.setText("");
        txtHState.setTag("");
        txtHState.setTag2nd("");

        txtVoucherNo.setText("");
        txtVoucherNo.setTag("");
        txtVoucherNo.setTag2nd("");

        isIGST = false;
        partyType = "";
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        userControl.setTextBlog(approvedByBlog, "");

        clearGrid(true, true);
    }

    private void clearGrid(boolean hsnGrid, boolean acGrid) {
        if (acGrid) {
            grdAccountDetail.getGrid().getStore().clear();
            grdAccountDetail.getGrid().getStore().add(new GridAcDetailsMdl());
            lblStripAcAmtTotal.setText("");
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

    private void topControlSaveError(int errorNo, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVoucherType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtVoucherDate).show();
                break;
            case 210:
                new DmAlertMessageBox(errorMessage, txtHGSTRounding).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtRefNo).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtRefDate).show();
                break;
            case 410:
                new DmAlertMessageBox(errorMessage, txtHReason).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtHAccountName).show();
                break;
            case 600:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            case 700:
                ((CellSelectionModel) grdAccountDetail.getGrid().getSelectionModel()).selectCell(0, 2);
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtHGSTType).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
        }

    }

    private void manageDisplay(boolean flag) {
        txtVoucherNo.setEnabled(false);
        txtHReason.setEnabled(false);

        txtHGSTRounding.setEnabled(flag);
        txtHState.setEnabled(flag);
        txtHRcm.setEnabled(flag);
        txtVoucherDate.setEnabled(flag);

        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtHVoucherType.setEnabled(false);
            txtVoucherNo.setEnabled(false);
        }
    }

    @Override
    public Widget asWidget() {
        return flwForm;
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
            acDetailsGridEdit.hide();
//            hsnDetailsGridEdit.hide();
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

    private boolean isFutureDate(String date) {
        boolean flag = false;
        if (!DmShared.dNull(date).trim().equals("")) {
            if (DmShared.convertDate(date).getTime() > DmShared.convertDate(DmClient.getLoginDate()).getTime()) {
                flag = true;
            }
        }
        return flag;
    }

    private void iniGstType(String voucherCategory) {
        if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.CREDIT_INVOICE_GST)) {
            txtHGSTType.setTag(String.valueOf(DmEnum.DmGSTType.INPUT.toInt()));
            txtHGSTType.setText(String.valueOf(DmEnum.DmGSTType.INPUT.toString()));
        } else if (DmShared.xNull(voucherCategory).equalsIgnoreCase(lvc.DEBIT_INVOICE_GST)) {
            txtHGSTType.setTag(String.valueOf(DmEnum.DmGSTType.OUTPUT.toInt()));
            txtHGSTType.setText(String.valueOf(DmEnum.DmGSTType.OUTPUT.toString()));
        }
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Voucher No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtHVoucherType.getTag()), "", "GST VOUCHER", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }
}
