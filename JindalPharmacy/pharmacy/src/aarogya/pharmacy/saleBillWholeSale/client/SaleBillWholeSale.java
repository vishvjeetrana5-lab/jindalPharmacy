package aarogya.pharmacy.saleBillWholeSale.client;

import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.saleBillWholeSale.shared.*;
import aarogya.pharmacy.schemeMaster.client.SchemeMaster;
import aarogya.pharmacy.schemeMaster.shared.GrdSchemeDetails_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.*;
import aarogya.selfToolkit.cardPaymentPopup.client.CardPaymentPopup;
import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.pharmacyDiscountPopup.client.PharmacyDiscountPopup;
import aarogya.selfToolkit.pharmacyDiscountPopup.shared.PharmacyDiscountPopup_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import aarogya.selfToolkit.stStatic.shared.STShared;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.eGstPopup.client.EGstPopup;
import dataman.dmToolkit.gst.shared.EGst_Mdl;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.partyDetailPopup.client.PartyDetailPopup;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import java.util.*;

public final class SaleBillWholeSale extends SaleBillWholeSale_Dsg implements KeyPressHandler, BlurHandler, AttachEvent.Handler, ClickHandler, DmRootEvents, SelectionHandler<Item> {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String documentTypeCode = "", documentNatureCode = "";
    private final ServiceInvoker<SaleBillWholeSale_SrvAsync> SrvAsync;
    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();
    private final CardPaymentPopup cardPaymentPopup = new CardPaymentPopup();
    private ModifyReason_Mdl modifyReasonMdl;
    private final PartyDetailPopup partyDetailPopup = new PartyDetailPopup();
    private final ModifyReason modifyReason = new ModifyReason();
    private boolean dateEditable = false, isDepartmentEditable = false;

    boolean ctoAllow = false, ctpAllow = false, ctrAllow = false, receiptAllow = false,
            vendorAllow = false, walkInAllow = false;

    protected boolean rateEditable = false, isAllowSaleReturn = false, discountEditable = false, isAllowAddBatch = false, schemeEditable = false;
    private final MedicineDetailsEdit<GrdMedicine_Mdl, GrdMediDetails_PrpAcs> mediDetails = new MedicineDetailsEdit(grdMediDetails, this);
    protected Default_Mdl defaultValue;
    protected MedicineSaleOn saleOn;
    private PharmacyDiscountPopup_Mdl pharmacyDiscountPopup_Mdl;
    private final PharmacyDiscountPopup pharmacyDiscountPopup = new PharmacyDiscountPopup(false);
    private String selectedBatchSQL = "", userEntDtLatestLine = "", isReturnExists = "", partyType = "", roomCode = "", partyGstIn = "",partyGstinDate = "";
    protected String partyTypeForEntry = "", InvIrnNo = "", mongoLinkId = "";
    public String expiryAlertBeforeDays = "", callFrom = "";
    double itemTotalQtyInPurchaseUnit = 0, landedValue = 0;

    final PrintPopup printPopup = new PrintPopup();
    private String moduleName = new pmPharmacy().moduleName;
    SchemeMaster schemeMaster;
//    final PartySearch partySearch = new PartySearch();
//    private PartySearch_Mdl partySearch_Mdl;
    protected TxtHVendor_Mdl vendorMasterMdl;
    private boolean flagCardPayment = false, isIncludeTaxInCost = false;
    boolean isSchemeExists = false, isPatientDetailsFromType = false;
    List<String> lstItemWithSchemeDetails = new ArrayList<>();
    SaleBillWholeSale_Mdl freeWithItems_Mdl, previousMdl;
    protected String convertedPaymentSource = null;
    protected boolean isAllowDiscountToParty = false;

    String profitPercentage = "0";
    protected EGstPopup eGstPopup;
    protected final HashMap<Integer, PartyDetailPopup_Mdl> hmPartyDetail_Mdl = new HashMap<>();
    protected PartyDetailPopup_Mdl partyDetailPopup_Mdl;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setCardViewRequired(false);
        topControl.setRefreshRequired(false);
        if (!DmShared.xNull(callFrom).equalsIgnoreCase("")) {
            topControl.setOtherRequired(false);
            flwApproveButton.add(btnApprovedBy);
        }
        if (!voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
            topControl.setDeleteCaptionToDiscard();
        }

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(rateEditableBlog)) {
                rateEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(saleReturnBlog)) {
                isAllowSaleReturn = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                isDepartmentEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(addBatchBlog)) {
                isAllowAddBatch = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(discountEditableBlog)) {
                discountEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(schemeEditableBlog)) {
                schemeEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctoAllowBlog)) {
                ctoAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctpAllowBlog)) {
                ctpAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctrAllowBlog)) {
                ctrAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(receiptAllowBlog)) {
                receiptAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(vendorAllowBlog)) {
                vendorAllow = true;
            }
        }

        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{voucher_Cat})) {

            @Override
            public String getVoucherType() {
                return txtHVType.getTag();
            }

            @Override
            public String getVoucherDate() {
                return txtDate.getText();
            }

            @Override
            public String getVoucherValue() {
                return txtNetAmount.getText();
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (!DmShared.xNull(InvIrnNo).equalsIgnoreCase("")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("E-Invoice is genrated,can't Update this record", (DmTextBox) null).show();
                    return;
                }
                if (!DmShared.xNull(txtEWayBillNo.getText()).equalsIgnoreCase("")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("E-Way is genrated,can't Update this record", (DmTextBox) null).show();
                    return;
                }
                if (DmShared.xNull(lblDiscarded.getText()).equalsIgnoreCase("Discarded")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                    return;
                }
                if (isReturnExists.equalsIgnoreCase("1")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Return has been done, can't update record", (DmTextBox) null).show();
                    return;
                }
                onEdit();
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (!voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
                    if (DmShared.xNull(lblDiscarded.getText()).equalsIgnoreCase("Discarded")) {
                        new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                        return;
                    }

                }

                new DmConfirmMessageBox("Are you sure? You want to discard this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            if (voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
                                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                                    topControlDelete(topControl.getDocId());
                                }
                            } else {
                                showModifyReason();
                            }

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

                if (DmShared.xNull(txtGatePassNo.getText()).equalsIgnoreCase("") && !DmShared.xNull(callFrom).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Record Can't Approved", txtNetAmount).show();
                    return;
                } else {
                    printRecord();
                }
            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {

                if (DmShared.vNull(txtNetAmount.getText()) == 0) {
                    new DmAlertMessageBox("Net Amount Can't Null.", txtNetAmount).show();
                    return;
                }

                if (grdMediDetails.getGrid().getStore().getAll().get(0).getMedicineMdl().getSharedKeyFieldName().equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Medicine is mandatory.", txtNetAmount).show();
                    return;
                }

                if (DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CASH)) {
                    flagCardPayment = false;
                    showCardPayment();//OK
                } else {
                    topControlSave();
                }
            }
        }
        );

        topControl.addCloseHandeller(
                new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SaleBillWholeSale.this);
            }
        }
        );

        topControl.addOtherHandeller(
                new DmTopControl.eOther() {
            @Override
            public void otherClicked() {
                menu.show(topControl.getButton("Other"));
            }
        }
        );

        topControl.setDisplay(
                true);
    }

    private void textHelpEvents() {

        printPopup.addCallBack(new PrintPopup.CallBack() {

            @Override
            public void hide(boolean isCancel) {

            }
        });

        schemeMaster.addCallBack(new SchemeMaster.CallBack() {
            @Override
            public void ok(GrdSchemeDetails_Mdl detailMdl) {
                lstItemWithSchemeDetails.add(detailMdl.getFreeWithItemCode());
                fillSchemeQtyAutoFill(detailMdl);
                formatGrid();
            }

            @Override
            public void close(boolean isErrorClose) {
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell((grdMediDetails.getGrid().getStore().size() - 1), 1);
            }
        });

        pharmacyDiscountPopup.addCallBack(new PharmacyDiscountPopup.CallBack() {
            @Override
            public void onOk(PharmacyDiscountPopup_Mdl info_Mdl) {
                pharmacyDiscountPopup_Mdl = info_Mdl;
                setDiscount();
            }

            @Override
            public void onCancel() {
                txtHVendor.setFocus(true);
            }

            @Override
            public void onClear() {
                if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                    clearDiscount();
                    pharmacyDiscountPopup_Mdl = null;
                }
            }
        });

        cardPaymentPopup.addCallBack(new CardPaymentPopup.CallBack() {

            @Override
            public void onOk(CardPaymentPopup_Mdl cardPaymentMdl) {
                cardPayment = new CardPaymentPopup_Mdl();
                cardPayment = cardPaymentMdl.clone();
                txtReciptType.setText(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypeName())));
                txtReciptType.setTag(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypeCode())));
                txtReciptType.setTag2nd(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypePM())));

                if (topControl.getMode().equalsIgnoreCase("New")) {
                    topControlSave();
                } else {
                    showModifyReason();
                }
            }

            @Override
            public void onCancel(CardPaymentPopup_Mdl cardPaymentMdl) {

            }
        });
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                SaleBillWholeSale.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
                }

            }
        });

        txtHVType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVType_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVType(voucher_Cat, voucher_Cat1, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVType_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVType_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    fillV_TypeDetail(t1Row);
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                    txtHVType.setTag2nd(DmShared.xNull(t1Row.getVoucherCategory()).trim());
                    paymentSource = DmShared.xNull(t1Row.getPaymentSource());
                    clear2();
                }
            }
        });
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsutant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });
        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCustomerType(helper, blnToEndOfFile, vendorAllow, walkInAllow, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                flwContainer.clear();
                txtVendor.setText("");
                txtHVendor.setText("");
                txtPlaceOfSupply.setTag("");
                txtPlaceOfSupply.setText("");
                txtHLocation.setText("");
                txtHLocation.setText("");
                isPatientDetailsFromType = true;
                txtPlaceOfSupply.setEnabled(true);
                if (t1Row != null && blnStatus) {
                    if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("Walk-In")) {
                        flwContainer.add(flwTxtVendor);
                        txtVendor.setFocus(true);
                        txtPlaceOfSupply.setEnabled(false);
                        txtHLocation.setEnabled(false);
                        txtPlaceOfSupply.setTag(DmShared.xNull(defaultValue.getStateCode()));
                        txtPlaceOfSupply.setText(DmShared.xNull(defaultValue.getStateName()));
                        txtHLocation.setTag(DmShared.iNull(DmEnum.DmLocation.IntraState.getId()).toString());
                        txtHLocation.setText(DmShared.xNull(DmEnum.DmLocation.IntraState.getValue()));
                        DmClient.setStyleHyperlinkLabel(lblParty, false, lblParty.getTitle(), false);

                    } else {
                        flwContainer.add(flwTxtHVendor);
                        txtHVendor.setFocus(true);
                    }
                }

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {

                    }
                });
            }
        });
        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                }
            }
        });

        txtHVendor.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVendor_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVendor(voucher_Cat, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVendor_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVendor).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVendor_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHVendor.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVendor).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVendor_Mdl t1Row) {
                txtPlaceOfSupply.setTag("");
                txtPlaceOfSupply.setText("");
                txtHLocation.setText("");
                txtHLocation.setText("");
                txtDueDate.setTag("");
                txtDueDate.setTag2nd("");
                txtDueDate.setText("");
                profitPercentage = "0";
                isAllowDiscountToParty = false;
                if (blnStatus && t1Row != null) {
                    if (DmShared.iNull(t1Row.getAllowDiscount()).intValue() == 1) {
                        isAllowDiscountToParty = true;
                    }
                    fillVendorDetails(txtHVendor.getTag(), DmEnum.PartyTransRecordType.PARTY.getCode(), false);
                    txtPlaceOfSupply.setTag(DmShared.xNull(t1Row.getStateCode()));
                    txtPlaceOfSupply.setText(DmShared.xNull(t1Row.getStateName()));
                    profitPercentage = DmShared.xNull(t1Row.getProfitPercentage());
                    txtDueDate.setTag2nd(DmShared.vNull(t1Row.getDueDays()).toString());
                    calculateDueDate();
                    clear2();
                }
            }
        });

        txtHTransportMode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTransportMode(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransportMode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTransportMode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransportMode).show();
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
        txtHTransporter.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHVendor_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHTransporter(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVendor_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTransporter).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHVendor_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHTransporter.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTransporter).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHVendor_Mdl tRow) {
                boolean flagTransporterChange = false;
                if (!DmShared.xNull(txtHTransporter.getTag()).trim().equalsIgnoreCase(DmShared.xNull(txtHTransporter.getTag2nd()))) {
                    hmPartyDetail_Mdl.remove(DmEnum.PartyTransRecordType.TRANSPORTER.getCode());
                    fillVendorDetails(txtHTransporter.getTag(), DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), true);
                    txtHTransporter.setTag2nd(DmShared.xNull(txtHTransporter.getTag()));
                    flagTransporterChange = true;
                }

                if (blnStatus && tRow != null) {
                    if (flagTransporterChange) {
//                        fillAdditionalPartyDetail(DmEnum.PartyTransRecordType.TRANSPORTER.getCode(), DmShared.xNull(txtHTransporter.getTag()));
                    }
                }
                DmClient.setStyleHyperlinkLabel(lblTransporter, !DmShared.xNull(txtHTransporter.getTag()).trim().equalsIgnoreCase(""), lblTransporter.getTitle(), false);
            }
        });
        txtHEWayBill.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEWayBill).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEWayBill.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEWayBill).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHLocation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHLocation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLocation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLocation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLocation).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (!DmShared.xNull(txtHLocation.getTag()).equalsIgnoreCase(DmShared.xNull(txtHLocation.getTag2nd()))) {
                    txtHLocation.setTag2nd(DmShared.xNull(txtHLocation.getTag()));
//                    clearGrid();
                }
            }
        });
        txtHVehicleType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait
            ) {
                SrvAsync.invokeRPC(blnCallBackWait).getHVehicleType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHVehicleType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHVehicleType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHVehicleType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row
            ) {
            }
        });

    }

    private void calculateDueDate() {
        if (!DmShared.dNull(txtDate.getText()).trim().equalsIgnoreCase("")) {
            if ((DmShared.iNull(txtDueDate.getTag2nd()).intValue() > 0)) {
                String dueDate = STClient.addDaysToDate(txtDate.getText(), txtDueDate.getTag2nd());
                txtDueDate.setTag(DmShared.dNull(dueDate));
                if (DmShared.xNull(txtDueDate.getText()).trim().equalsIgnoreCase("")) {
                    txtDueDate.setText(DmShared.dNull(dueDate));
                }
            }
        }
    }

    private void fillVendorDetails(String vendorCode, final int transactionPartyNatureCode, boolean isTransporter) {
        SrvAsync.invokeRPC().fillVendorDetails(vendorCode, transactionPartyNatureCode, isTransporter, new AsyncCallback<DmResult<PartyDetailPopup_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
            }

            @Override
            public void onSuccess(DmResult<PartyDetailPopup_Mdl> result) {
                if (result.isStatus()) {
                    hmPartyDetail_Mdl.put(transactionPartyNatureCode, result.getData());
                    partyDetailPopup_Mdl = result.getData();
                    partyGstIn = "" ;
                    partyGstinDate = "" ;
                    partyGstIn = DmShared.xNull(partyDetailPopup_Mdl.getGstin());
                    partyGstinDate = DmShared.xNull(partyDetailPopup_Mdl.getGstinDate());
                    if (transactionPartyNatureCode == DmEnum.PartyTransRecordType.PARTY.getCode()) {
                        partyTypeForEntry = partyDetailPopup_Mdl.getPartyGstType();
                        if (!DmShared.xNull(partyDetailPopup_Mdl.getSharedPreparedIP()).equalsIgnoreCase("")) {
                            txtHLocation.setTag(DmShared.xNull(partyDetailPopup_Mdl.getSharedPreparedIP()));
                            txtHLocation.setText(DmShared.xNull(partyDetailPopup_Mdl.getSharedPreparedBy()));
                        }
                        DmClient.setStyleHyperlinkLabel(lblParty, true, lblParty.getTitle(), true);
                    } else if (transactionPartyNatureCode == DmEnum.PartyTransRecordType.TRANSPORTER.getCode()) {
                        DmClient.setStyleHyperlinkLabel(lblTransporter, true, lblTransporter.getTitle(), true);
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHVType).show();
                }
            }
        });
    }

    private void fillSchemeQtyAutoFill(GrdSchemeDetails_Mdl detailMdl) {
        GrdMedicine_Mdl mdl;
        double blncQty = DmShared.vNull(detailMdl.getTotalFreeQty()).doubleValue();
        if (Math.abs(blncQty) >= DmShared.vNull(detailMdl.getSchemeFreeQty()).doubleValue()) {
            freeWithItems_Mdl = new SaleBillWholeSale_Mdl();
            //<editor-fold defaultstate="collapsed" desc="SELECTED BATCH FILL">
            if (Math.abs(blncQty) > 0) {
                mdl = new GrdMedicine_Mdl();

                double purchaseUnitConversionRate = 0, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, tempRate = 0, tempSkuRate = 0;
                int purchaseUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
                if (detailMdl.getMedicineMdl() != null) {
                    setITSGrid(detailMdl.getLstITS(), mdl);
                }

                purchaseUnitDecimalPlaces = DmShared.iNull(detailMdl.getDecimalPlacesPurchaseUnit()).intValue();
                skuDecimalPlaces = detailMdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                purchaseUnitConversionRate = DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();

                mdl.setSaleAcCode(DmShared.xNull(detailMdl.getSaleAcCode()));
                mdl.setPurchaseUnitConversionRate(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate());
                mdl.setSharedKeyFieldCode(detailMdl.getSharedKeyFieldCode());
                mdl.setSharedKeyFieldName(detailMdl.getSharedKeyFieldName());
                mdl.setSchemeItem(true);
                mdl.setFreeWithItemCode(detailMdl.getFreeWithItemCode());
                mdl.setFreeWithItemCode(detailMdl.getFreeWithItemCode());
                freeWithItems_Mdl.getLstFreeWithItemCodes().add(detailMdl.getFreeWithItemCode());
                //<editor-fold defaultstate="collapsed" desc="SET MEDICINE MDL">
                TxtHMedicine_Mdl listNode = new TxtHMedicine_Mdl();
                listNode.setSharedKeyFieldCode(DmShared.xNull(detailMdl.getMedicineMdl().getSharedKeyFieldCode()));
                listNode.setSharedKeyFieldName(DmShared.xNull(detailMdl.getMedicineMdl().getSharedKeyFieldName()));
                listNode.setSkuCode(DmShared.xNull(detailMdl.getMedicineMdl().getSkuCode()));
                listNode.setSkuName(DmShared.xNull(detailMdl.getMedicineMdl().getSkuName()));
                listNode.setItemProperty(DmShared.xNull(detailMdl.getMedicineMdl().getItemProperty()));
                listNode.setAllowDecimalPlaces(DmShared.iNull(detailMdl.getMedicineMdl().getAllowDecimalPlaces()).intValue());
                listNode.setAllowSkuDecimalPlaces(DmShared.iNull(detailMdl.getMedicineMdl().getAllowSkuDecimalPlaces()).intValue());
                listNode.setRestrictGroup(DmShared.xNull(detailMdl.getMedicineMdl().getRestrictGroup()));
                listNode.setHsnCode(DmShared.xNull(detailMdl.getMedicineMdl().getHsnCode()));
                listNode.setHsnName(DmShared.xNull(detailMdl.getMedicineMdl().getHsnName()));
                listNode.setItemGstNature(DmShared.xNull(detailMdl.getMedicineMdl().getItemGstNature()));
                listNode.setItsBasedOn(DmShared.xNull(detailMdl.getMedicineMdl().getItsBasedOn()));
                listNode.setBestBefore(detailMdl.getMedicineMdl().isBestBefore());
                listNode.setDiscountPer(DmShared.vNull(detailMdl.getMedicineMdl().getDiscountPer()).toString());
                listNode.setPurchaseUnitConversionRate(DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).toString());
                listNode.setPurchaseUnit(DmShared.xNull(detailMdl.getMedicineMdl().getPurchaseUnit()));
                listNode.setPurchaseUnitName(DmShared.xNull(detailMdl.getMedicineMdl().getPurchaseUnitName()));
                listNode.setIsSaleRateEditable(DmShared.vNull(detailMdl.getMedicineMdl().getIsSaleRateEditable()).toString());
                listNode.setAllowNegativeStock(detailMdl.getMedicineMdl().isAllowNegativeStock());
                listNode.setBarcodeActive(detailMdl.getMedicineMdl().isBarcodeActive());
                listNode.setCompositionCode(DmShared.xNull(detailMdl.getMedicineMdl().getCompositionCode()));
                listNode.setManualCode(DmShared.xNull(detailMdl.getMedicineMdl().getManualCode()));
                listNode.setCompositionName(DmShared.xNull(detailMdl.getMedicineMdl().getCompositionName()));
                listNode.setItemPacking(DmShared.xNull(detailMdl.getMedicineMdl().getItemPacking()));
                listNode.setReturnableMedicine(detailMdl.getMedicineMdl().isReturnableMedicine());

                mdl.setMedicineMdl(listNode);
//</editor-fold>
                mdl.setUnit(detailMdl.getMedicineMdl().getPurchaseUnitName());
                //<editor-fold defaultstate="collapsed" desc="BATCH DETAILS">
                mdl.setBatchNo(detailMdl.getBatchNo_Mdl().getSharedKeyFieldName());
                mdl.setIbCode(detailMdl.getBatchNo_Mdl().getSharedKeyFieldCode());
                mdl.setBestBefore(detailMdl.getMedicineMdl().isBestBefore());
                mdl.setExpDate(detailMdl.getBatchNo_Mdl().getExpiryDate());
                mdl.setMfgDate(detailMdl.getBatchNo_Mdl().getManufacturingDate());
                mdl.setMrp(detailMdl.getBatchNo_Mdl().getMrp());
                //<editor-fold defaultstate="collapsed" desc="RATE">
                if (saleOn == MedicineSaleOn.MRP) {
                    tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getMrp()).doubleValue();
                    tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuMrp()).doubleValue();
                } else {
                    tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSaleRate()).doubleValue();
                    tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuSaleRate()).doubleValue();
                }
                mdl.setRate(DmClient.vNull(tempRate, "0.00"));
                //</editor-fold>
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="QTY IN SKU">
                if (blncQty > DmShared.vNull(detailMdl.getBatchNo_Mdl().getBalanceQtyInPurcUnit()).doubleValue()) {
                    if (DmShared.xNull(detailMdl.getAllowNegativeStockFreeItem()).equalsIgnoreCase("1")) {
                        mdl.setFreeQuantityInSku(DmClient.vNull(blncQty, DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                    } else {
                        mdl.setFreeQuantityInSku(DmClient.vNull(detailMdl.getBatchNo_Mdl().getBalanceQty(), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                    }
                } else {
                    mdl.setFreeQuantityInSku(DmClient.vNull((blncQty * purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                }

                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="QTY IN SALE UNIT">
                if (purchaseUnitDecimalPlaces > 0) {
                    mdl.setFreeQuantityInPurchaseUnit(DmClient.vNull((DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(purchaseUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
                } else {
                    mdl.setFreeQuantityInPurchaseUnit(DmClient.vNull(Math.floor(DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(purchaseUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
                }
                mdl.setBatchQuantityInPurchaseUnit(DmClient.vNull(DmShared.vNull(detailMdl.getBatchNo_Mdl().getBalanceQtyInPurcUnit()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
//</editor-fold>
                blncQty -= DmShared.vNull(mdl.getFreeQuantityInPurchaseUnit());
                mdl.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(mdl.getQuantitySKU()).doubleValue() + DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue()), "0.00"));
                mdl.setAmount(DmClient.vNull((DmShared.vNull(mdl.getQuantitySKU()).doubleValue() * DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuSaleRate()).doubleValue()), "0.00"));
                if (DmShared.vNull(mdl.getDiscountPer()).doubleValue() > 0) {
                    mdl.setDiscount(DmClient.vNull((DmShared.vNull(mdl.getAmount()).doubleValue() * DmShared.vNull(mdl.getDiscountPer()).doubleValue() * 0.01), "0.00"));
                } else {
                    mdl.setDiscount(DmClient.vNull(DmShared.vNull(mdl.getDiscount()), "0.00"));
                }
                itemValue = DmShared.vNull(mdl.getAmount()).doubleValue() - DmShared.vNull(mdl.getDiscount()).doubleValue();
                totalTaxRate = DmShared.vNull(mdl.getCgstPer()).doubleValue() + (DmShared.vNull(mdl.getSgstPer()).doubleValue() > 0 ? DmShared.vNull(mdl.getSgstPer()).doubleValue() : DmShared.vNull(mdl.getUgstPer()).doubleValue());
                taxableAmt = (itemValue * 100.00) / (100.00 + totalTaxRate);

                mdl.setCgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getCgstPer()).doubleValue() * 0.01), "0.00"));
                mdl.setSgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getSgstPer()).doubleValue() * 0.01), "0.00"));
                mdl.setUgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getUgstPer()).doubleValue() * 0.01), "0.00"));
                mdl.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
                mdl.setNetAmt(DmClient.vNull(itemValue, "0.00"));

                grdMediDetails.getGrid().getStore().add(mdl);
            }
//</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="BATCH FILL LOOP WISE">
            for (BatchNo_Mdl batchMdl : detailMdl.getLstBatchDetails()) {
                if (DmShared.iNull(batchMdl.getSharedKeyFieldCode()).longValue() != DmShared.iNull(detailMdl.getBatchNo_Mdl().getSharedKeyFieldCode()).longValue()) {
                    if (Math.abs(blncQty) > 0) {
                        mdl = new GrdMedicine_Mdl();

                        double purchaseUnitConversionRate = 0, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, tempRate = 0; //, tempSkuRate = 0
                        int purchaseUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
                        if (mdl.getMedicineMdl() != null) {
                            setITSGrid(detailMdl.getLstITS(), mdl);
                        }

                        purchaseUnitDecimalPlaces = DmShared.iNull(detailMdl.getDecimalPlacesPurchaseUnit()).intValue();
                        skuDecimalPlaces = detailMdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                        purchaseUnitConversionRate = DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();

                        mdl.setSaleAcCode(DmShared.xNull(detailMdl.getSaleAcCode()));
                        mdl.setPurchaseUnitConversionRate(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate());
                        mdl.setSharedKeyFieldCode(detailMdl.getSharedKeyFieldCode());
                        mdl.setSharedKeyFieldName(detailMdl.getSharedKeyFieldName());
                        mdl.setSchemeItem(true);
                        mdl.setFreeWithItemCode(detailMdl.getFreeWithItemCode());
                        freeWithItems_Mdl.getLstFreeWithItemCodes().add(detailMdl.getFreeWithItemCode());
                        //<editor-fold defaultstate="collapsed" desc="SET MEDICINE MDL">
                        TxtHMedicine_Mdl listNode = new TxtHMedicine_Mdl();
                        listNode.setSharedKeyFieldCode(DmShared.xNull(detailMdl.getMedicineMdl().getSharedKeyFieldCode()));
                        listNode.setSharedKeyFieldName(DmShared.xNull(detailMdl.getMedicineMdl().getSharedKeyFieldName()));
                        listNode.setSkuCode(DmShared.xNull(detailMdl.getMedicineMdl().getSkuCode()));
                        listNode.setSkuName(DmShared.xNull(detailMdl.getMedicineMdl().getSkuName()));
                        listNode.setItemProperty(DmShared.xNull(detailMdl.getMedicineMdl().getItemProperty()));
                        listNode.setAllowDecimalPlaces(DmShared.iNull(detailMdl.getMedicineMdl().getAllowDecimalPlaces()).intValue());
                        listNode.setAllowSkuDecimalPlaces(DmShared.iNull(detailMdl.getMedicineMdl().getAllowSkuDecimalPlaces()).intValue());
                        listNode.setRestrictGroup(DmShared.xNull(detailMdl.getMedicineMdl().getRestrictGroup()));
                        listNode.setHsnCode(DmShared.xNull(detailMdl.getMedicineMdl().getHsnCode()));
                        listNode.setHsnName(DmShared.xNull(detailMdl.getMedicineMdl().getHsnName()));
                        listNode.setItemGstNature(DmShared.xNull(detailMdl.getMedicineMdl().getItemGstNature()));
                        listNode.setItsBasedOn(DmShared.xNull(detailMdl.getMedicineMdl().getItsBasedOn()));
                        listNode.setBestBefore(detailMdl.getMedicineMdl().isBestBefore());
                        listNode.setDiscountPer(DmShared.vNull(detailMdl.getMedicineMdl().getDiscountPer()).toString());
                        listNode.setPurchaseUnitConversionRate(DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).toString());
                        listNode.setPurchaseUnit(DmShared.xNull(detailMdl.getMedicineMdl().getPurchaseUnit()));
                        listNode.setPurchaseUnitName(DmShared.xNull(detailMdl.getMedicineMdl().getPurchaseUnitName()));
                        listNode.setIsSaleRateEditable(DmShared.vNull(detailMdl.getMedicineMdl().getIsSaleRateEditable()).toString());
                        listNode.setAllowNegativeStock(detailMdl.getMedicineMdl().isAllowNegativeStock());
                        listNode.setBarcodeActive(detailMdl.getMedicineMdl().isBarcodeActive());
                        listNode.setCompositionCode(DmShared.xNull(detailMdl.getMedicineMdl().getCompositionCode()));
                        listNode.setManualCode(DmShared.xNull(detailMdl.getMedicineMdl().getManualCode()));
                        listNode.setCompositionName(DmShared.xNull(detailMdl.getMedicineMdl().getCompositionName()));
                        listNode.setItemPacking(DmShared.xNull(detailMdl.getMedicineMdl().getItemPacking()));
                        listNode.setReturnableMedicine(detailMdl.getMedicineMdl().isReturnableMedicine());

                        mdl.setMedicineMdl(listNode);
//</editor-fold>
                        mdl.setUnit(detailMdl.getMedicineMdl().getPurchaseUnitName());
                        //<editor-fold defaultstate="collapsed" desc="BATCH DETAILS">
                        mdl.setBatchNo(batchMdl.getSharedKeyFieldName());
                        mdl.setIbCode(batchMdl.getSharedKeyFieldCode());
                        mdl.setBestBefore(detailMdl.getMedicineMdl().isBestBefore());
                        mdl.setExpDate(batchMdl.getExpiryDate());
                        mdl.setMfgDate(batchMdl.getManufacturingDate());
                        mdl.setMrp(batchMdl.getMrp());
//                        mdl.setSkuMrp(batchMdl.getSkuMrp());
                        //<editor-fold defaultstate="collapsed" desc="RATE">
                        if (saleOn == MedicineSaleOn.MRP) {
                            tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getMrp()).doubleValue();
//                            tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuMrp()).doubleValue();
                        } else {
                            tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSaleRate()).doubleValue();
//                            tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuSaleRate()).doubleValue();
                        }
                        mdl.setRate(DmClient.vNull(tempRate, "0.00"));
//                        mdl.setSkuRate(DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7)));
                        //</editor-fold>
                        //</editor-fold>
                        //<editor-fold defaultstate="collapsed" desc="QTY IN SKU">
//                if (blncQty > DmShared.vNull(detailMdl.getBatchNo_Mdl().getBalanceQty()).doubleValue()) {
                        if (blncQty > DmShared.vNull(batchMdl.getBalanceQtyInPurcUnit()).doubleValue()) {
//                        mdl.setFreeQuantityInSku(DmClient.vNull(detailMdl.getBatchNo_Mdl().getBalanceQty(), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                            mdl.setFreeQuantityInSku(DmClient.vNull(batchMdl.getBalanceQty(), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                        } else {
                            mdl.setFreeQuantityInSku(DmClient.vNull((blncQty * purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                        }
                        //</editor-fold>
                        mdl.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(mdl.getQuantitySKU()).doubleValue() + DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue()), "0.00"));
                        mdl.setBatchQuantityInPurchaseUnit(DmClient.vNull(DmShared.vNull(batchMdl.getBalanceQtyInPurcUnit()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
                        //<editor-fold defaultstate="collapsed" desc="QTY IN SALE UNIT">
                        if (purchaseUnitDecimalPlaces > 0) {
                            mdl.setFreeQuantityInPurchaseUnit(DmClient.vNull((DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(purchaseUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
                        } else {
                            mdl.setFreeQuantityInPurchaseUnit(DmClient.vNull(Math.floor(DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(purchaseUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
                        }
//</editor-fold>
                        blncQty -= DmShared.vNull(mdl.getFreeQuantityInPurchaseUnit());
                        mdl.setAmount(DmClient.vNull((DmShared.vNull(mdl.getQuantitySKU()).doubleValue() * DmShared.vNull(batchMdl.getSkuSaleRate()).doubleValue()), "0.00"));
                        if (DmShared.vNull(mdl.getDiscountPer()).doubleValue() > 0) {
                            mdl.setDiscount(DmClient.vNull((DmShared.vNull(mdl.getAmount()).doubleValue() * DmShared.vNull(mdl.getDiscountPer()).doubleValue() * 0.01), "0.00"));
                        } else {
                            mdl.setDiscount(DmClient.vNull(DmShared.vNull(mdl.getDiscount()), "0.00"));
                        }
                        itemValue = DmShared.vNull(mdl.getAmount()).doubleValue() - DmShared.vNull(mdl.getDiscount()).doubleValue();
                        totalTaxRate = DmShared.vNull(mdl.getCgstPer()).doubleValue() + (DmShared.vNull(mdl.getSgstPer()).doubleValue() > 0 ? DmShared.vNull(mdl.getSgstPer()).doubleValue() : DmShared.vNull(mdl.getUgstPer()).doubleValue());
                        taxableAmt = (itemValue * 100.00) / (100.00 + totalTaxRate);

                        mdl.setCgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getCgstPer()).doubleValue() * 0.01), "0.00"));
                        mdl.setSgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getSgstPer()).doubleValue() * 0.01), "0.00"));
                        mdl.setUgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getUgstPer()).doubleValue() * 0.01), "0.00"));
                        mdl.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
                        mdl.setNetAmt(DmClient.vNull(itemValue, "0.00"));

                        grdMediDetails.getGrid().getStore().add(mdl);
                    }
                }
            }

            grdMediDetails.commitChanges();
//</editor-fold>
        }

    }

    private void formatGrid() {
        List<GrdMedicine_Mdl> lstGridDetails = new ArrayList<>();
        for (GrdMedicine_Mdl mdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                lstGridDetails.add(mdl);
            }
        }
        grdMediDetails.getGrid().getStore().clear();
        grdMediDetails.getGrid().getStore().addAll(lstGridDetails);
        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
    }

    private void setDefaultLocation() {
        String locationCode = "", locationName = "";
        txtHLocation.setTag("");
        txtHLocation.setText("");
        txtHLocation.setTag2nd("");
        if (DmShared.iNull(defaultValue.getStateCode()).intValue() != DmShared.iNull(txtPlaceOfSupply.getTag()).intValue()) {
            locationCode = DmShared.xNull(STShared.getGstLocation_InterState(defaultValue.getGstLocationMap()).getCode());
            locationName = DmShared.xNull(STShared.getGstLocation_InterState(defaultValue.getGstLocationMap()).getName());
        } else {
            if (DmShared.iNull(defaultValue.isUnionTerritory()).intValue() == 1) {
                locationCode = DmShared.xNull(STShared.getGstLocation_UnionTerritory(defaultValue.getGstLocationMap()).getCode());
                locationName = DmShared.xNull(STShared.getGstLocation_UnionTerritory(defaultValue.getGstLocationMap()).getName());
            } else {
                locationCode = DmShared.xNull(STShared.getGstLocation_IntraState(defaultValue.getGstLocationMap()).getCode());
                locationName = DmShared.xNull(STShared.getGstLocation_IntraState(defaultValue.getGstLocationMap()).getName());
            }
        }
        txtHLocation.setTag(DmShared.xNull(locationCode));
        txtHLocation.setText(DmShared.xNull(locationName));
        txtHLocation.setTag2nd(txtHLocation.getTag());
    }

    private void setDiscount() {
        if (pharmacyDiscountPopup_Mdl != null) {
            int lastIndex = 0;
            double totalAmt = 0, discountRate = 0, totalDisc = 0; //, discAmt = 0;
            boolean isGroup = false, isDiscount = false, flag = false;
            double netAmt = 0, amount = 0, discount = 0;

            /* ==============================<< Check wether discount is applicable for Service Group or not >>================================= */
            if ((DmShared.iNull(pharmacyDiscountPopup_Mdl.getGroupCode()).longValue() != 0)) {
                isGroup = true;
            }

            /* ==============================<< Get total amount (and group by total amount) >>================================= */
            if ((DmShared.iNull(pharmacyDiscountPopup_Mdl.getDiscountPer()).doubleValue() == 0) && (DmShared.iNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue() != 0)) {
                int cnt = 0;
                for (GrdMedicine_Mdl grdMedicine_Mdl : grdMediDetails.getGrid().getStore().getAll()) {
                    if ((DmShared.iNull(grdMedicine_Mdl.getSharedKeyFieldCode()).longValue() != 0)
                            && grdMedicine_Mdl.getMedicineMdl().isDiscountAllow()) {

                        if (isGroup) {
                            if (DmShared.iNull(pharmacyDiscountPopup_Mdl.getGroupCode()).longValue() == DmShared.iNull(grdMedicine_Mdl.getMedicineMdl().getMedicineGroupCode()).longValue()) {
                                totalAmt += DmShared.vNull(grdMedicine_Mdl.getAmount()).doubleValue() - DmShared.vNull(grdMedicine_Mdl.getDiscount()).doubleValue();
                                lastIndex = cnt;
                            }
                        } else {
                            totalAmt += DmShared.vNull(grdMedicine_Mdl.getAmount()).doubleValue() - DmShared.vNull(grdMedicine_Mdl.getDiscount()).doubleValue();
                            lastIndex = cnt;
                        }
                    }
                    cnt++;
                }
                isDiscount = true;
                if (totalAmt != 0) {
                    discountRate = DmShared.vNull((DmClient.vNull(DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue() / totalAmt, "0.0000000"))).doubleValue();
                }
            }

            for (int i = 0; i < grdMediDetails.getGrid().getStore().getAll().size(); i++) {
                if (grdMediDetails.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
                    if (discountEditable && grdMediDetails.getGrid().getStore().get(i).getMedicineMdl().isDiscountAllow()) {
                        if (DmShared.iNull(grdMediDetails.getGrid().getStore().get(i).getSharedKeyFieldCode()).longValue() != 0) {
                            if (isGroup) {
                                if (DmShared.iNull(pharmacyDiscountPopup_Mdl.getGroupCode()).longValue() == DmShared.iNull(grdMediDetails.getGrid().getStore().get(i).getMedicineGroupCode()).longValue()) {
                                    flag = true;
                                }
                            } else {
                                flag = true;
                            }

                            if (flag) {
                                amount = DmShared.vNull(grdMediDetails.getGrid().getStore().get(i).getAmount()).doubleValue() - DmShared.vNull(grdMediDetails.getGrid().getStore().get(i).getDiscount()).doubleValue();

                                if (isDiscount) {
                                    /* ==============================<< For Discount value >>================================= */
                                    discount = DmShared.vNull(DmClient.vNull((discountRate * amount), "0.00")).doubleValue();
                                    totalDisc += discount;
                                    if (i == lastIndex - 1) {
                                        double adjAmt = 0;
                                        double tempTotalDiscount = 0;
                                        tempTotalDiscount = DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue();
                                        if (totalDisc > tempTotalDiscount) {
                                            adjAmt = DmShared.vNull(DmClient.vNull((totalDisc - tempTotalDiscount), "0.00")).doubleValue();
                                            discount -= adjAmt;
                                        } else if (totalDisc < tempTotalDiscount) {
                                            adjAmt = DmShared.vNull(DmClient.vNull((tempTotalDiscount - totalDisc), "0.00")).doubleValue();
                                            discount += adjAmt;
                                        }
                                    }
                                } else {
                                    /* ==============================<< For Discount Percentage >>================================= */
                                    discount = (amount * DmShared.vNull(DmClient.vNull(pharmacyDiscountPopup_Mdl.getDiscountPer(), "0.00")).doubleValue()) / 100;
                                }

                                grdMediDetails.getGrid().getStore().get(i).setDiscount2(DmClient.vNull(discount, "0.00"));
                                netAmt = amount - DmShared.vNull(DmClient.vNull(grdMediDetails.getGrid().getStore().get(i).getDiscount2(), "0.00")).doubleValue();
                                grdMediDetails.getGrid().getStore().get(i).setDiscountPer2(DmClient.vNull(pharmacyDiscountPopup_Mdl.getDiscountPer(), "0.00"));
                                grdMediDetails.getGrid().getStore().get(i).setDiscountGivenByTag(DmShared.xNull(pharmacyDiscountPopup_Mdl.getGivenByCode()));
                                grdMediDetails.getGrid().getStore().get(i).setDiscountGivenByText(DmShared.xNull(pharmacyDiscountPopup_Mdl.getGivenByName()));
                                grdMediDetails.getGrid().getStore().get(i).setDiscountReasonCode(DmShared.xNull(pharmacyDiscountPopup_Mdl.getDiscountReasonCode()));
                                grdMediDetails.getGrid().getStore().get(i).setDiscountReasonName(DmShared.xNull(pharmacyDiscountPopup_Mdl.getDiscountReasonName()));
                                grdMediDetails.getGrid().getStore().get(i).setNetAmt(DmClient.vNull(netAmt, "0.00"));
                                flag = false;
                            }

                        }
                    }
                }
            }
        }

        grdMediDetails.commitChanges();
        fillItemDetail(grdMediDetails.getGrid().getStore().getAll());
    }

    private void clearDiscount() {
        for (int i = 0; i < grdMediDetails.getGrid().getStore().getAll().size(); i++) {
            GrdMedicine_Mdl grdMedicine_Mdl = grdMediDetails.getGrid().getStore().get(i);
            if (DmShared.iNull(grdMedicine_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if (discountEditable && grdMediDetails.getGrid().getStore().get(i).getMedicineMdl().isDiscountAllow()) {
                    grdMediDetails.getGrid().getStore().get(i).setDiscount2("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountPer2("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountGivenByTag("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountGivenByText("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountReasonCode("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountReasonName("");
                    grdMediDetails.getGrid().getStore().get(i).setAmount(DmClient.vNull(grdMediDetails.getGrid().getStore().get(i).getAmount(), "0.00"));

                }
            }
        }
        grdMediDetails.commitChanges();
        fillItemDetail(grdMediDetails.getGrid().getStore().getAll());
    }

    private void fillItemDetail(List<GrdMedicine_Mdl> lstGrdItemDetail) {
        for (int i = 0; i < lstGrdItemDetail.size(); i++) {
            GrdMedicine_Mdl detail_Mdl = lstGrdItemDetail.get(i);
            if (DmShared.iNull(detail_Mdl.getSharedKeyFieldCode()).longValue() > 0) {
                mediDetails.displayRow(detail_Mdl);
                mediDetails.calculate();
                mediDetails.fillRowData(detail_Mdl);
            }
        }
        grdMediDetails.commitChanges();
        calculate();
    }

    private void calculateGrid() {  //<Ag> Sync this function with item grid popup
        for (GrdMedicine_Mdl mdl : grdMediDetails.getGrid().getStore().getAll()) {
            double purchaseUnitConversionRate = 0, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, totalTaxAmt = 0, totalDiscount = 0, netAmt = 0;
            int purchaseUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
            if (mdl.getMedicineMdl() != null) {
                setITSGrid(mdl.getMedicineMdl().getLstITS(), mdl);
            }

            if (mdl.getMedicineMdl() != null) {
                purchaseUnitDecimalPlaces = mdl.getMedicineMdl().getAllowDecimalPlaces();
                skuDecimalPlaces = mdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                purchaseUnitConversionRate = DmShared.vNull(mdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();
            }

            mdl.setQuantity(DmClient.vNull(mdl.getQuantity(), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));
            mdl.setQuantitySKU(DmClient.vNull((DmShared.vNull(mdl.getQuantity()).doubleValue() * purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
            mdl.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

            mdl.setAmount(DmClient.vNull((DmShared.vNull(mdl.getQuantity()).doubleValue() * DmShared.vNull(mdl.getRate()).doubleValue()), "0.00"));
            if (DmShared.vNull(mdl.getDiscountPer()).doubleValue() > 0) {
                mdl.setDiscount(DmClient.vNull((DmShared.vNull(mdl.getAmount()).doubleValue() * DmShared.vNull(mdl.getDiscountPer()).doubleValue() * 0.01), "0.00"));
            } else {
                mdl.setDiscount(DmClient.vNull(DmShared.vNull(mdl.getDiscount()), "0.00"));
            }
            if (DmShared.vNull(mdl.getDiscountPer2()).doubleValue() > 0) {
                double tempAmt = 0;
                tempAmt = DmShared.vNull(mdl.getAmount()).doubleValue() - DmShared.vNull(mdl.getDiscount()).doubleValue();
                mdl.setDiscount2(DmClient.vNull((tempAmt * DmShared.vNull(mdl.getDiscountPer2()).doubleValue() * 0.01), "0.00"));
            } else {
                mdl.setDiscount2(DmClient.vNull(DmShared.vNull(mdl.getDiscount2()), "0.00"));
            }

            totalDiscount = DmShared.vNull(mdl.getDiscount()).doubleValue() + DmShared.vNull(mdl.getDiscount2()).doubleValue();
            itemValue = DmShared.vNull(mdl.getAmount()).doubleValue() - totalDiscount;
            totalTaxRate = DmShared.vNull(mdl.getCgstPer()).doubleValue() + (DmShared.vNull(mdl.getSgstPer()).doubleValue() > 0 ? DmShared.vNull(mdl.getSgstPer()).doubleValue() : DmShared.vNull(mdl.getUgstPer()).doubleValue());
            taxableAmt = (itemValue * 100.00) / (100.00 + totalTaxRate);
            netAmt = itemValue + totalTaxRate;
            //<editor-fold defaultstate="collapsed" desc="For EGST: taxableAmt Re-Calculation">
            String tempItemRate = "0.00";
            double tempQtyForRate = DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQuantityInPurchaseUnit()).doubleValue();
            if (DmShared.vNull(tempQtyForRate).doubleValue() != 0) {
                tempItemRate = DmClient.vNull((DmShared.vNull(taxableAmt).doubleValue() / DmShared.vNull(tempQtyForRate).doubleValue()), "0.00");
            }
            String tempGrossAmt = DmClient.vNull((DmShared.vNull(tempItemRate).doubleValue() * DmShared.vNull(tempQtyForRate).doubleValue()), "0.00");
            String tempDiff = DmClient.vNull((DmShared.vNull(tempGrossAmt).doubleValue() - DmShared.vNull(taxableAmt).doubleValue()), "0.00");

            if (DmShared.vNull(tempDiff).doubleValue() != 0) {
                taxableAmt = DmShared.vNull(tempGrossAmt).doubleValue();
            }
//</editor-fold>

            mdl.setCgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getCgstPer()).doubleValue() * 0.01), "0.00"));
            mdl.setSgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getSgstPer()).doubleValue() * 0.01), "0.00"));
            mdl.setUgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getUgstPer()).doubleValue() * 0.01), "0.00"));
            mdl.setIgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getIgstPer()).doubleValue() * 0.01), "0.00"));

            mdl.setGstPer(DmClient.vNull(DmShared.vNull(mdl.getCgstPer()).doubleValue() + DmShared.vNull(mdl.getSgstPer()).doubleValue() + DmShared.vNull(mdl.getUgstPer()).doubleValue() + DmShared.vNull(mdl.getIgstPer()).doubleValue(), "0.00"));
            mdl.setGstAmount(DmClient.vNull(DmShared.vNull(mdl.getCgstAmt()).doubleValue() + DmShared.vNull(mdl.getSgstAmt()).doubleValue() + DmShared.vNull(mdl.getUgstAmt()).doubleValue() + DmShared.vNull(mdl.getIgstAmt()).doubleValue(), "0.00"));

            mdl.setTotalDiscount(DmClient.vNull(totalDiscount, "0.00"));
            mdl.setTaxableAmt(DmClient.vNull(itemValue, "0.00"));
            mdl.setNetAmt(DmClient.vNull(netAmt, "0.00"));

            totalTaxAmt = DmShared.vNull(mdl.getCgstAmt()).doubleValue() + DmShared.vNull(mdl.getSgstAmt()).doubleValue() + DmShared.vNull(mdl.getUgstAmt()).doubleValue();

            //for managing minor 'paisa' differences in amount.
            if (DmShared.vNull(mdl.getNetAmt()).doubleValue() != (DmShared.vNull(mdl.getTaxableAmt()).doubleValue() + totalTaxAmt)) {
                taxableAmt = DmShared.vNull(mdl.getTaxableAmt()).doubleValue() + (DmShared.vNull(mdl.getNetAmt()).doubleValue() - (DmShared.vNull(mdl.getTaxableAmt()).doubleValue() + totalTaxAmt));
                mdl.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
            }
            grdMediDetails.commitChanges();
        }
        calculate();
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
    }

    private void showCardPayment() { //OK

        if (DmShared.xNull(txtHVendor.getText()).equalsIgnoreCase("") && DmShared.xNull(txtVendor.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Debtor Name.", txtHVendor).show();
            return;
        }

        if (DmShared.vNull(txtNetAmount.getText()).doubleValue() == 0) {
            new DmAlertMessageBox("No amount to receive.", (DmTextBox) null).show();
            return;
        }

        flagCardPayment = true;
        CardPaymentPopup_Mdl cardPaymentMdl = new CardPaymentPopup_Mdl();
        cardPaymentMdl = this.cardPayment.clone();
        cardPaymentMdl.setAmount(DmClient.vNull(txtNetAmount.getText(), "0.00"));
        cardPaymentMdl.setPatientName(DmShared.xNull(txtHVendor.getText()));
        cardPaymentMdl.setReceiptNo(DmShared.xNull(txtReciptNo.getText()));
        cardPaymentMdl.setReceiptTypeCode(DmShared.xNull(txtReciptType.getTag()));
        cardPaymentMdl.setReceiptTypeName(DmShared.xNull(txtReciptType.getText()));
        cardPaymentMdl.setReceiptTypePM(DmShared.xNull(txtReciptType.getTag2nd()));

        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Debtor") || DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("WALK-IN")) {

            if (!DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CREDIT)) {
                cardPaymentPopup.setCTOAllowed(false);
                cardPaymentPopup.setCTRAllowed(false);
                cardPaymentPopup.setReceiptAllowed(true);
            }

        }

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            cardPaymentPopup.show(cardPaymentMdl, DmShared.xNull(topControl.getMode()));
        } else {
            try {
                calculate();
                SaleBillWholeSale_Mdl mrTemp = topControlSaveInitialize();
                SaleBillWholeSale_Vld.chkSave(mrTemp, voucher_Cat);
                cardPaymentPopup.show(cardPaymentMdl, DmShared.xNull(topControl.getMode()));
            } catch (DmException e) {
                if (topControl.getMode().equalsIgnoreCase("Browse")) {
                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
                } else {
                    topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
                }
            }
        }

    }

    private void findEvents() {

        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(voucher_Cat, topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<Find_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox());
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

    private void manageBatchSelectedQty(String itemCode) {
        selectedBatchSQL = "";
        itemTotalQtyInPurchaseUnit = 0;
        for (GrdMedicine_Mdl service : grdMediDetails.getGrid().getStore().getAll()) {
            if (DmShared.iNull(service.getSharedKeyFieldCode()).longValue() == DmShared.iNull(itemCode).longValue()) {
                itemTotalQtyInPurchaseUnit += (DmShared.vNull(service.getQuantity()).doubleValue() + DmShared.vNull(service.getFreeQuantityInPurchaseUnit()).doubleValue());
                if (DmShared.iNull(service.getIbCode()).longValue() > 0) {
                    if (!selectedBatchSQL.equalsIgnoreCase("")) {
                        selectedBatchSQL += " UNION ALL ";
                    }
                    selectedBatchSQL += " Select " + DmShared.iNull(service.getIbCode()).longValue() + " as ibCode, " + DmShared.vNull(service.getTotalQuantitySKU()).doubleValue() + " as qty";
                }
            }
        }
    }

    private void gridEvents() {
        mediDetails.addCallBack(new MedicineDetailsEdit.CallBack() {
            @Override
            public void onOk(GrdMedicine_Mdl grdDetail, List<GrdMedicine_Mdl> mdlLst) {
                grdMediDetails.commitChanges();
                if (mdlLst != null && !mdlLst.isEmpty()) {
                    for (GrdMedicine_Mdl details : mdlLst) {
                        grdMediDetails.getGrid().getStore().add(details);
                    }
                    formatGrid();
                }
                calculate();

                if (grdDetail.getMedicineMdl().isSchemeExists()) {
                    if ((lstItemWithSchemeDetails != null) && !(lstItemWithSchemeDetails.contains(grdDetail.getSharedKeyFieldCode()))) {
                        isSchemeExists = true;
                        manageBatchSelectedQty(grdDetail.getSharedKeyFieldCode());
                        showSchemePopUp(grdDetail);
                    }
                } else {
                    isSchemeExists = false;
                }
            }

            @Override
            public void onDelete() {
                grdMediDetails.commitChanges();
                calculate();
            }
        });

        grdMediDetails.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridMediDetailsEdit(event.getRowIndex());
            }
        });

        grdMediDetails.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridMediDetailsEdit(grdMediDetails.getGrid().getStore().indexOf(grdMediDetails.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

        eGstPopup.addCallBack(new EGstPopup.CallBack() {
            @Override
            public void onOk(EGst_Mdl eGst_Mdl) {
                topControlMoveRec(DmShared.xNull(eGst_Mdl.getDocid()));
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(labelBillDiscount)) {
            if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                if (pharmacyDiscountPopup_Mdl == null) {
                    pharmacyDiscountPopup_Mdl = new PharmacyDiscountPopup_Mdl();
                }
                pharmacyDiscountPopup.show(pharmacyDiscountPopup_Mdl, DmShared.dNull(txtDate.getText()), DmShared.xNull(txtHVType.getTag()));
            }
        } else if (event.getSource().equals(lblParty)) {
//             DmClient.setStyleHyperlinkLabel(lblParty, !DmShared.xNull(txtHVendor.getTag()).trim().equalsIgnoreCase(""), lblParty.getTitle(), false);
            if (!DmShared.xNull(txtHVendor.getTag()).equalsIgnoreCase("")) {
                showPartyDetailPopup(lblParty, DmEnum.PartyTransRecordType.PARTY.getCode());
            }
        } else if (event.getSource().equals(lblTransporter)) {
            showPartyDetailPopup(lblTransporter, DmEnum.PartyTransRecordType.TRANSPORTER.getCode());
        }
        if (event.getSource() == btnApprovedBy) {
            if (DmShared.xNull(txtGatePassNo.getText()).equalsIgnoreCase("")) {
                setApproved(previousMdl.getGatePassDocId(), true, topControl.getDocId());
            } else {
                setApproved(previousMdl.getGatePassDocId(), false, topControl.getDocId());
            }

        } else {

            btnClick(event);

        }
    }

    private void setApproved(String gatePassDocId, boolean isApprove, String docId) {
        try {
            SrvAsync.invokeRPC().setApproval(DmClient.getLoginUserName(), isApprove, gatePassDocId, docId, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                    } else {
                        topControlSaveError(result.getErrorNumber(), "", "", result.getErrorMessage());
                    }
                }
            });

        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), "", "", e.getMessage());
        }
    }

    private void showPartyDetailPopup(Widget widget, final int transactionPartyNatureCode) {
        if (hmPartyDetail_Mdl.get(transactionPartyNatureCode) != null) {
            partyDetailPopup.show(widget, transactionPartyNatureCode, hmPartyDetail_Mdl.get(transactionPartyNatureCode), iniPartyDetailPopupControlValue(transactionPartyNatureCode));
        }
    }

    private HashMap iniPartyDetailPopupControlValue(final int transactionPartyNatureCode) {
        HashMap paramHashMap = new HashMap();
        boolean isNameEditable = false, isAdPlaceOfBusinessEditable = false, isAddressEditable = false, isGstDetailEditable = false, isPanTanEditable = false, isTransporterIdEditable = false, isOkEequired = false;
        if (DmEnum.PartyTransRecordType.getValue(transactionPartyNatureCode) != null
                && !DmShared.xNull(topControl.getMode()).trim().equalsIgnoreCase("Browse")) {
            if (DmEnum.PartyTransRecordType.getValue(transactionPartyNatureCode) == DmEnum.PartyTransRecordType.PARTY) {
                if (!DmShared.xNull(txtHVendor.getTag()).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(partyTypeForEntry).equals(DmEnum.DmPartyType.Unregistered.getId())) {
                        isNameEditable = true;
                        isAddressEditable = true;
                    } else {
                        isAdPlaceOfBusinessEditable = true;
                    }
                }
            }
//            else if (DmEnum.PartyTransRecordType.getValue(transactionPartyNatureCode) == DmEnum.PartyTransRecordType.DELIVERY_TO_PARTY) {
//                if (!DmShared.xNull(txtHDeliveryTo.getTag()).trim().equalsIgnoreCase("")) {
//                    isAdPlaceOfBusinessEditable = true;
//                }
//            }
        }
        paramHashMap.put("IS_NAME_EDITABLE", isNameEditable);
        paramHashMap.put("IS_ADDITIONAL_PLACE_OF_BUSINESS_EDITABLE", isAdPlaceOfBusinessEditable);
        paramHashMap.put("IS_ADDRESS_EDITABLE", isAddressEditable);
        paramHashMap.put("IS_GST_DETAIL_EDITABLE", isGstDetailEditable);
        paramHashMap.put("IS_TRANSPORTER_ID_EDITABLE", isTransporterIdEditable);
        paramHashMap.put("IS_PAN_TAN_EDITABLE", isPanTanEditable);
        if (isNameEditable || isAdPlaceOfBusinessEditable || isAddressEditable || isGstDetailEditable || isTransporterIdEditable || isPanTanEditable) {
            isOkEequired = true;
        }
        paramHashMap.put("IS_OK_REQUIRED", isOkEequired);

        return paramHashMap;
    }

    private void btnClick(ClickEvent event) {
        if (event.getSource().equals(btnPayment)) {
            if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL_WHOLE_SALE.caption)) {
                if (DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CASH)) {
                    flagCardPayment = false;
                    showCardPayment();
                }
            } else {
                new DmAlertMessageBox("Not Allow!", txtHVType).show();
            }
        }
    }

    private void gridMediDetailsEdit(Integer currentRowIndex) {
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please Select Department.", txtHDepartment).show();
            return;
        }
        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Select Debtor Type.", txtHType).show();
            return;
        }
        if (DmShared.xNull(txtHVendor.getText()).equalsIgnoreCase("") && DmShared.xNull(txtVendor.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Debtor Name.", txtHVendor).show();
            return;
        }
        if (grdMediDetails.getGrid().getStore().size() == 1) {
            if (DmShared.iNull(grdMediDetails.getGrid().getStore().get(0).getSharedKeyFieldCode()).longValue() == 0) {
                isSchemeExists = false;
                lstItemWithSchemeDetails = new ArrayList<>();
                freeWithItems_Mdl = null;
            }
        }
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            mediDetails.show(currentRowIndex, false, false, false, txtHLocation.getTag());
        } else {
            mediDetails.show(currentRowIndex, true, true, true, txtHLocation.getTag());
        }
    }

    private pmPharmacy pmMenu = new pmPharmacy();

    public SaleBillWholeSale(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super(paramHashMap);
        SrvAsync = new ServiceInvoker<>(GWT.create(SaleBillWholeSale_Srv.class), SaleBillWholeSale.class.getName());
        eGstPopup = new EGstPopup(SrvAsync);
        this.asWidget().setTitle(widgetName);

        if (paramHashMap != null) {
            if (paramHashMap.containsKey("callFrom")) {
                callFrom = DmShared.xNull(paramHashMap.get("callFrom").toString());

                if (DmShared.xNull(callFrom).equalsIgnoreCase(pmMenu.WHOLE_SALE_GATE_PASS_OUT.caption)) {
                    menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("A", "*"));
                    menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
                    menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
                }
            }
        }

        topControlEvents(menuPermission);
        schemeMaster = new SchemeMaster(schemeEditable);
        textHelpEvents();
        findEvents();
        gridEvents();
        handlerEvents();
        iniDefaultValues(swi);
        grdMediDetails.setDefaultColumnIndex(8);

    }

    private void handlerEvents() {
        btnApprovedBy.addClickHandler(this);
        txtDate.addBlurHandler(this);
        flwForm.addAttachHandler(this);
        labelBillDiscount.addClickHandler(this);
        btnPayment.addClickHandler(this);
        mnuGstActions.addSelectionHandler(this);
        lblParty.addClickHandler(this);
        lblTransporter.addClickHandler(this);
        txtLRDate.addBlurHandler(this);
        txtDate.addBlurHandler(this);
        txtDueDate.addBlurHandler(this);
        txtPartyPODate.addBlurHandler(this);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultVal(voucher_Cat1, new AsyncCallback<DmResult<Default_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
            }

            @Override
            public void onSuccess(DmResult<Default_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    isIncludeTaxInCost = defaultValue.isIncludeTaxInCost();
                    partyType = defaultValue.getPartyType();
                    expiryAlertBeforeDays = defaultValue.getExpiryAlertBeforeDays();
                    saleOn = defaultValue.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE : defaultValue.getMedicineSaleOn();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    txtHEWayBill.setText("No");
                    txtHEWayBill.setTag("0");
                    manageDisplay(false);
                    swi.setWidget(SaleBillWholeSale.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHVType).show();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private SaleBillWholeSale_Mdl topControlSaveInitialize() {
        SaleBillWholeSale_Mdl mrTemp = new SaleBillWholeSale_Mdl();

        setPatientName_AddressText();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdSaleBill().setRecIdValue(DmShared.xNull(txtBillNo.getText()));
            mrTemp.getRecIdSaleBill().setCounter(DmShared.iNull(txtBillNo.getTag()).longValue());
            mrTemp.getRecIdSaleBill().setPrefix(DmShared.xNull(txtBillNo.getTag2nd()));

            mrTemp.getRecIdRecipt().setRecIdValue(DmShared.xNull(txtReciptNo.getText()));
            mrTemp.getRecIdRecipt().setCounter(DmShared.iNull(txtReciptNo.getTag()).longValue());
            mrTemp.getRecIdRecipt().setPrefix(DmShared.xNull(txtReciptNo.getTag2nd()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtBillNo.getText()));
        mrTemp.setControlAccount(defaultValue.getControlAccount());
        mrTemp.setDocumentNatureCode(DmShared.xNull(documentNatureCode));
        mrTemp.setDocumentTypeCode(DmShared.xNull(documentTypeCode));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVType.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtDate.getText()));
        mrTemp.setInvoiceDate(DmShared.dNull(txtDate.getText()));
        mrTemp.setRoomCode(DmShared.xNull(roomCode));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setConsultantCode(DmShared.xNull(txtHConsultant.getTag()));

        mrTemp.setLocationCode(txtHLocation.getTag());
        mrTemp.setLocationName(txtHLocation.getText());
        mrTemp.setVehicleNo(DmShared.xNull(txtVehicleNo.getText()));
        mrTemp.setTransporterCode(DmShared.xNull(txtHTransporter.getTag()));
        mrTemp.setTransporterName(DmShared.xNull(txtHTransporter.getText()));
        mrTemp.setTransportModeCode(DmShared.xNull(txtHTransportMode.getTag()));
        mrTemp.setTransportModeName(DmShared.xNull(txtHTransportMode.getText()));
        mrTemp.setLrNo(DmShared.xNull(txtLRNo.getText()));
        mrTemp.setLrDate(DmShared.dNull(txtLRDate.getText()));
        mrTemp.seteWayBillNo(DmShared.xNull(txtEWayBillNo.getText()));
        mrTemp.seteWayBillDateTime(DmShared.xNull(txtEWayBillDate.getText()));
        mrTemp.seteWayBillRequiredCode(DmShared.xNull(txtHEWayBill.getTag()));
        mrTemp.seteWayBillRequiredName(DmShared.xNull(txtHEWayBill.getText()));
        mrTemp.setPlaceOfSupply(DmShared.xNull(txtPlaceOfSupply.getTag()));
        mrTemp.setPlaceOfSupplyName(DmShared.xNull(txtPlaceOfSupply.getText()));
        mrTemp.setVehicleTypeCode(DmShared.xNull(txtHVehicleType.getTag()));
        mrTemp.setVehicleTypeName(DmShared.xNull(txtHVehicleType.getText()));

        mrTemp.setPartySubCode(DmShared.xNull(txtHVendor.getTag()));
        if (!txtHVendor.getText().equalsIgnoreCase("")) {
            mrTemp.setPatientName(DmShared.xNull(txtHVendor.getText()));
        } else {
            mrTemp.setPatientName(DmShared.xNull(txtVendor.getText()));
        }

        if (DmShared.xNull(txtHType.getText()).equalsIgnoreCase("Walk-In")) {
            mrTemp.setTypeCode(DmShared.iNull(STEnum.WholeSaleType.WALK_IN.getId()).toString());
            mrTemp.setTypeName(DmShared.xNull(STEnum.WholeSaleType.WALK_IN.getValue()));
        } else {
            mrTemp.setTypeCode(DmShared.iNull(STEnum.WholeSaleType.DEBTOR.getId()).toString());
            mrTemp.setTypeName(DmShared.xNull(STEnum.WholeSaleType.DEBTOR.getValue()));
        }
        mrTemp.setReceiptTypeCode(DmShared.xNull(txtReciptType.getTag()));
        mrTemp.setReceiptTypeName(DmShared.xNull(txtReciptType.getText()));
        mrTemp.setReceiptTypeCateory(DmShared.xNull(txtReciptType.getTag2nd()));
        mrTemp.setDefaultPharmacyServiceCode(DmShared.xNull(defaultValue.getDefaultPharmacyServiceCode()));

        //=======================================<MJ>======================================//
        mrTemp.setDueDate(DmShared.dNull(txtDueDate.getText()));
        mrTemp.setDueDaysMaster(DmShared.xNull(txtDueDate.getTag2nd()));
        //=======================================<****>======================================//
        mrTemp.setPartyType(DmShared.xNull(partyTypeForEntry));
        mrTemp.setProfitPercentage(DmShared.xNull(profitPercentage));

        mrTemp.setPartyPONO(DmShared.xNull(txtPartyPONo.getText()));
        mrTemp.setPartyPODate(DmShared.dNull(txtPartyPODate.getText()));

        mrTemp.setSiteStateCode(DmShared.xNull(defaultValue.getStateCode()));

        mrTemp.getRecIdRecipt().setRecIdValue(DmShared.xNull(txtReciptNo.getText()));

        mrTemp.setUserEntDtLatestLine(DmShared.xNull(userEntDtLatestLine));

        mrTemp.setPartyDetail_Mdl((HashMap<Integer, PartyDetailPopup_Mdl>) hmPartyDetail_Mdl.clone());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setLandedValue(DmClient.vNull(landedValue, "0.00"));
        mrTemp.setNetAmount(DmClient.vNull(txtNetAmount.getText(), "0.00"));
        mrTemp.setBillAmt(DmClient.vNull(txtBillAmt.getText(), "0.00"));
        mrTemp.setRoundOffAmt(DmClient.vNull(txtRoundoff.getText(), "0.00"));
        mrTemp.setRoundOffAcCode(DmShared.xNull(txtRoundoff.getTag()));
        mrTemp.setGrossAmt(DmClient.vNull(txtItemGross.getText(), "0.00"));
        mrTemp.setDiscountAmt(DmClient.vNull(txtDiscount.getText(), "0.00"));
        mrTemp.setDiscountAmt2(DmClient.vNull(txtDiscount2.getText(), "0.00"));
        mrTemp.setTaxableAmt(DmClient.vNull(txtTaxableAmt.getText(), "0.00"));
        mrTemp.setCgstAmt(DmClient.vNull(txtCGSTAmt.getText(), "0.00"));
        mrTemp.setIgstAmt(DmClient.vNull(txtIGSTAmt.getText(), "0.00"));
        if (defaultValue.isUnionTerritory()) {
            mrTemp.setUgstAmt(DmClient.vNull(txtSgstUgstAmt.getText(), "0.00"));
        } else {
            mrTemp.setSgstAmt(DmClient.vNull(txtSgstUgstAmt.getText(), "0.00"));
        }
        mrTemp.getGrdMediDetails().addAll(grdMediDetails.getGrid().getStore().getAll());
        if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL_WHOLE_SALE.caption)) {
            mrTemp.getCardPayment().getGrdCardPaymentList().addAll(new ArrayList<GrdCardPayment_Mdl>());
            mrTemp.setCardPayment(cardPayment.clone());
        }
        mrTemp.setFlagCardPayment(flagCardPayment ? "1" : "0");

        if (DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CASH)) {
            mrTemp.setPaymentMode(DmShared.iNull(STEnum.WholeSalePaymentMode.CASH.getId()).toString());
        } else if (DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_WHOLESALE_CREDIT)) {
            mrTemp.setPaymentMode(DmShared.iNull(STEnum.WholeSalePaymentMode.CREDIT.getId()).toString());
        }
        mrTemp.setPartyGstIn(DmShared.xNull(partyGstIn));
        mrTemp.setPartyGstinDate(DmShared.dNull(partyGstinDate));

        mrTemp.setVoucherCategory(DmShared.xNull(txtHVType.getTag2nd()));

        return mrTemp;
    }

    private void topControlSave() {
        SaleBillWholeSale_Mdl mrTemp;

        try {
            calculate();
            mrTemp = topControlSaveInitialize();
            SaleBillWholeSale_Vld.chkSave(mrTemp, voucher_Cat);
            SrvAsync.invokeRPC().saveRecord(txtHVType.getTag2nd(), mrTemp, previousMdl, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        topControlMoveRec(topControl.getDocId());
                        manageEway();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getValue1(), result.getValue2(), result.getErrorMessage());
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
        }
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHVType).show();
                break;
            case 110:
                new DmAlertMessageBox(errorMessage, txtHType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHVendor).show();
                break;
            case 410:
                new DmAlertMessageBox(errorMessage, txtDueDate).show();
                break;
            case 500:
                new DmAlertMessageBox(errorMessage, txtPlaceOfSupply).show();
                break;
            case 540:
                new DmAlertMessageBox(errorMessage, txtHLocation).show();
                break;
            case 550:
                new DmAlertMessageBox(errorMessage, txtHTransporter).show();
                break;
            case 560:
                new DmAlertMessageBox(errorMessage, txtHEWayBill).show();
                break;

            case 600:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdMedicine_Mdl> listTemp = grdMediDetails.getGrid().getStore().getAll();
                    for (GrdMedicine_Mdl node : listTemp) {
                        indexTemp = DmShared.iNull(listTemp.indexOf(node)).intValue();
                        if (errorMessage != null && !errorMessage.isEmpty()) {
                            if (Character.isDigit(errorMessage.charAt(errorMessage.length() - 1))) {
                                if (node.getMedicineMdl() != null) {
                                    errorMessage += " " + DmShared.xNull(node.getMedicineMdl().getPurchaseUnitName());
                                }
                            }
                            errorMessage += " in line no. " + (indexTemp + 1);
                        }
                        break;
                    }
                }
                final int gridRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtHType).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;
            case 740:
                new DmAlertMessageBox(errorMessage, txtHConsultant).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
        }

    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(voucher_Cat, topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<SaleBillWholeSale_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SaleBillWholeSale_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<SaleBillWholeSale_Mdl> result) {
        SaleBillWholeSale_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            previousMdl = new SaleBillWholeSale_Mdl(data);
            freeWithItems_Mdl = result.getData();
            txtDate.setEnabled(false);
            txtHDepartment.setEnabled(false);
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtBillNo.setText(data.getSharedKeyFieldName());
            txtBillNo.setTag(DmShared.iNull(data.getRecIdSaleBill().getCounter()).toString());
            txtBillNo.setTag2nd(DmShared.xNull(data.getRecIdSaleBill().getPrefix()));
            txtHVType.setText(DmShared.xNull(data.getVoucherTypeName()));
            txtHVType.setTag(DmShared.xNull(data.getVoucherTypeCode()));
            txtHVType.setTag2nd(DmShared.xNull(data.getVoucherCategory()));
            documentNatureCode = DmShared.xNull(data.getDocumentNatureCode());
            documentTypeCode = DmShared.xNull(data.getDocumentTypeCode());
            paymentSource = DmShared.xNull(data.getPaymentSource());
            txtGatePassNo.setText(DmShared.xNull(data.getGatePassRecId()));

            InvIrnNo = DmShared.xNull(data.geteInvIrnNo());

            txtDate.setText(DmShared.dNull(data.getVoucherDate()));
            txtDate.setTag2nd(DmShared.dNull(data.getVoucherDate()));
            //txtTime.setText(DmShared.xNull(data.getTime()));
            if (DmShared.xNull(data.getTypeCode()).equals(DmShared.iNull(STEnum.WholeSaleType.WALK_IN.getId()).toString())) {
                txtHType.setText(DmShared.xNull(STEnum.WholeSaleType.WALK_IN.getValue()));
                txtVendor.setText(DmShared.xNull(data.getPatientName()));
                flwContainer.add(flwTxtVendor);
            } else {
                txtHType.setText(DmShared.xNull(STEnum.WholeSaleType.DEBTOR.getValue()));
                txtHVendor.setText(DmShared.xNull(data.getPatientName()));
                flwContainer.add(flwTxtHVendor);
            }

            txtHType.setTag(DmShared.xNull(data.getTypeCode()));
            txtHDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtHDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            roomCode = DmShared.xNull(data.getRoomCode());
            txtHVendor.setTag(DmShared.xNull(data.getPartySubCode()));
            profitPercentage = DmShared.xNull(data.getProfitPercentage());

            txtHConsultant.setText(DmShared.xNull(data.getConsultantName()));
            txtHConsultant.setTag(DmShared.xNull(data.getConsultantCode()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));

            partyTypeForEntry = data.getPartyType();

            //=================================<MJ>==================================//
            txtDueDate.setTag(DmShared.dNull(data.getDueDate()));
            txtDueDate.setText(DmShared.dNull(data.getDueDate()));
            txtDueDate.setTag2nd(DmShared.xNull(data.getDueDaysMaster()));
            //=================================<****>==================================//
            txtPartyPONo.setText(DmShared.xNull(data.getPartyPONO()));
            txtPartyPODate.setText(DmShared.dNull(data.getPartyPODate()));

            txtHTransportMode.setText(data.getTransportModeName());
            txtHTransportMode.setTag(data.getTransportModeCode());
            txtLRNo.setText(data.getLrNo());
            txtLRDate.setText(data.getLrDate());
            txtHEWayBill.setTag(data.geteWayBillRequiredCode());
            txtHEWayBill.setText(data.geteWayBillRequiredName());
            txtEWayBillDate.setTag(data.geteWayBillDateTime());
            txtEWayBillDate.setText(data.geteWayBillDateTime());
            txtEWayBillNo.setText(data.geteWayBillNo());
            txtHVehicleType.setTag(DmShared.xNull(data.getVehicleTypeCode()));
            txtHVehicleType.setText(DmShared.xNull(data.getVehicleTypeName()));
            txtHLocation.setTag(DmShared.xNull(data.getLocationCode()));
            txtPlaceOfSupply.setTag(DmShared.xNull(data.getPlaceOfSupply()));
            txtPlaceOfSupply.setText(DmShared.xNull(data.getPlaceOfSupplyName()));
            txtVehicleNo.setText(data.getVehicleNo());
            txtHTransporter.setText(data.getTransporterName());
            txtHTransporter.setTag(data.getTransporterCode());
            txtHLocation.setText(data.getLocationName());
            txtHLocation.setTag(data.getLocationCode());
            txtHLocation.setTag2nd(data.getLocationCode());

            // set the Receipt Details for payment popup in Moverec
            txtReciptNo.setText(data.getRecIdRecipt().getRecIdValue());
            txtReciptNo.setTag(DmShared.iNull(data.getRecIdRecipt().getCounter()).toString());
            txtReciptNo.setTag2nd(DmShared.xNull(data.getRecIdRecipt().getPrefix()));
            txtReciptType.setText(data.getPrecVDescription());
            txtReciptType.setTag(data.getPrecVType());
            txtReciptType.setTag2nd(data.getPrecAttachPaymentMode());
            mongoLinkId = DmShared.xNull(data.getMongoLinkId());

            txtItemGross.setText(DmClient.vNull(data.getGrossAmt(), "0.00"));
            txtDiscount.setText(DmClient.vNull(data.getDiscountAmt(), "0.00"));
            txtDiscount2.setText(DmClient.vNull(data.getDiscountAmt2(), "0.00"));
            txtTaxableAmt.setText(DmClient.vNull(data.getTaxableAmt(), "0.00"));
            txtTaxAmount.setText(DmClient.vNull(data.getGstAmt(), "0.00"));
            txtCGSTAmt.setText(DmClient.vNull(data.getCgstAmt(), "0.00"));
            txtIGSTAmt.setText(DmClient.vNull(data.getIgstAmt(), "0.00"));
            if (defaultValue.isUnionTerritory()) {
                txtSgstUgstAmt.setText(DmClient.vNull(data.getUgstAmt(), "0.00"));
            } else {
                txtSgstUgstAmt.setText(DmClient.vNull(data.getSgstAmt(), "0.00"));
            }
            txtBillAmt.setText(DmClient.vNull(data.getBillAmt(), "0.00"));
            txtRoundoff.setText(DmClient.vNull(data.getRoundOffAmt(), "0.00"));
            txtRoundoff.setTag(DmShared.xNull(data.getRoundOffAcCode()));
            landedValue = DmShared.vNull(data.getLandedValue()).doubleValue();
            txtNetAmount.setText(DmClient.vNull(data.getNetAmount(), "0.00"));
            userEntDtLatestLine = DmShared.xNull(data.getUserEntDtLatestLine());

            isReturnExists = DmShared.xNull(data.getIsReturnExist());

            hmPartyDetail_Mdl.clear();
            hmPartyDetail_Mdl.putAll((HashMap<Integer, PartyDetailPopup_Mdl>) data.getPartyDetail_Mdl().clone());

            lblDiscarded.setText(DmShared.iNull(data.getCancelRecord()).intValue() == 1 ? "Discarded" : "");
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            if (!DmShared.xNull(callFrom).equalsIgnoreCase("")) {
                userControl.setTextBlog(approvedByBlog, data.getApprovedBy());
            }
            cardPayment = data.getCardPayment();
            cardPayment.getTxtHReceiptTypeVList().addAll(defaultValue.getCardPayment().getTxtHReceiptTypeVList());

            convertedPaymentSource = DmShared.xNull(data.getPaymentSource());
            grdMediDetails.getGrid().getStore().clear();
            grdMediDetails.getGrid().getStore().addAll(data.getGrdMediDetails());

            partyGstIn = data.getPartyGstIn();
            partyGstinDate = data.getPartyGstinDate();

            resetTaxSummary();

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
        manageDisplay(false);

    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, modifyReasonMdl, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlMoveRec(result.getData());
                } else {
                    new DmAlertMessageBox(DmShared.xNull(result.getErrorMessage()), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtDate) {
            txtDate.setText(DmClient.returnDate(txtDate.getText()));
            try {
                if (STClient.isFutureDate(DmShared.dNull(txtDate.getText()))) {
                    txtDate.setText("");
                    new DmAlertMessageBox("Bill Date cannot be greater than current date.", txtDate).show();
                    return;
                }
                if (!DmShared.dNull(txtDate.getText()).equalsIgnoreCase("") && !DmShared.dNull(txtDate.getTag2nd()).equalsIgnoreCase("")) {
                    if (DmShared.convertDate(txtDate.getText()).getTime() != DmShared.convertDate(txtDate.getTag2nd()).getTime()) {
                        txtDate.setTag2nd(txtDate.getText());

                    }
                }
            } catch (Exception e) {
            }
        } else if (event.getSource() == txtLRDate) {
            txtLRDate.setText(DmClient.returnDate(txtLRDate.getText()));
        } else if (event.getSource() == txtDate) {
            txtDate.setText(DmClient.returnDate(txtDate.getText()));
        } else if (event.getSource().equals(txtHVendor)) {
            setPatientName_AddressText();
        } else if (event.getSource() == txtDueDate) {
            txtDueDate.setText(DmClient.returnDate(txtDueDate.getText()));
        } else if (event.getSource() == txtPartyPODate) {
            txtPartyPODate.setText(DmClient.returnDate(txtPartyPODate.getText()));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            cardPaymentPopup.hide();
            pharmacyDiscountPopup.hide();
            mediDetails.hide();
            printPopup.hide();
        }

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        setDefaultValue();
        isSchemeExists = false;
        lstItemWithSchemeDetails = new ArrayList<>();
        freeWithItems_Mdl = null;
    }

    private void onEdit() {
        manageDisplay(true);
        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
        txtRemark.setFocus(true);
    }

    private void setDefaultValue() {
        if (defaultValue.getVoucherTypeMdl() != null) {
            fillV_TypeDetail(defaultValue.getVoucherTypeMdl());
        }
        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Debtor")) {
            flwContainer.add(flwTxtHVendor);
            txtHVendor.setFocus(true);
        }
        txtHDepartment.setText(defaultValue.getDepartmentName());
        txtHDepartment.setTag(defaultValue.getDepartmentCode());
        txtRoundoff.setTag(defaultValue.getRoundOffAcCode());
        txtHDepartment.setText(DmShared.xNull(defaultValue.getPharmacyCentralStoreName()));
        txtHDepartment.setTag(DmShared.xNull(defaultValue.getPharmacyCentralStoreCode()));

        grdMediDetails.getGrid().getStore().clear();
        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());

        getServerTime();
    }

    private void manageDisplay(boolean flag) {
        txtDate.setEnabled(false);
        txtBillNo.setEnabled(false);
        txtGatePassNo.setEnabled(false);
        txtHDepartment.setEnabled(false);
        txtEWayBillNo.setEnabled(false);
        txtEWayBillDate.setEnabled(false);
        txtItemGross.setEnabled(false);
        txtDiscount.setEnabled(false);
        txtDiscount2.setEnabled(false);
        txtBillAmt.setEnabled(false);
        txtRoundoff.setEnabled(false);
        txtNetAmount.setEnabled(false);
        txtTaxableAmt.setEnabled(false);
        txtTaxAmount.setEnabled(false);
        txtCGSTAmt.setEnabled(false);
        txtSgstUgstAmt.setEnabled(false);
        txtPlaceOfSupply.setEnabled(false);
        txtHConsultant.setEnabled(true);
        flwPayment.clear();

        if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL_WHOLE_SALE.caption)) {
            flwPayment.add(btnPayment);
        }
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtDate.setEnabled(dateEditable);
            txtHType.setEnabled(false);
            txtHDepartment.setEnabled(false);
            txtHVType.setEnabled(false);
            txtRemark.setEnabled(true);
        } else if (topControl.getMode().equalsIgnoreCase("New")) {
            txtHType.setEnabled(true);
            txtHVType.setEnabled(true);
            txtDate.setEnabled(dateEditable);
            txtRemark.setEnabled(true);
            if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0) {
                txtHDepartment.setEnabled(isDepartmentEditable);
            }
        } else if (topControl.getMode().equalsIgnoreCase("Browse")) {
            txtDate.setEnabled(false);
            txtHDepartment.setEnabled(false);
            txtHVType.setEnabled(false);
            txtHConsultant.setEnabled(false);
            txtRemark.setEnabled(false);
        }
        lblSgstUgst.setText(defaultValue.isUnionTerritory() ? "UGST Amount" : "SGST Amount");
    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<String[]>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
            }

            @Override
            public void onSuccess(String[] result) {
                txtDate.setText(result[0]);
                txtDate.setTag2nd(result[0]);
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0 || !isDepartmentEditable) {
                            txtHType.setFocus(true);
                        } else {
                            txtHDepartment.setFocus(true);
                        }
                    }
                });
            }
        });
    }

    private void calculate() {
        double discount = 0, itemGrossAmt = 0, roundoff = 0, netAmount = 0, cgstAmt = 0, sgstAmt = 0, igstAmt = 0,
                taxableAmt = 0, billAmt = 0, discount2 = 0;
        TxtHMedicine_Mdl medicineMdl;

        if (grdMediDetails.getGrid().getStore().getAll() != null) {
            for (GrdMedicine_Mdl medicineDetailMdl : grdMediDetails.getGrid().getStore().getAll()) {
                medicineMdl = medicineDetailMdl.getMedicineMdl();
                if (medicineMdl != null && DmShared.iNull(medicineMdl.getSharedKeyFieldCode()).longValue() > 0) {
                    billAmt += DmShared.vNull(medicineDetailMdl.getNetAmt());
                    itemGrossAmt += DmShared.vNull(medicineDetailMdl.getAmount());
                    discount += DmShared.vNull(medicineDetailMdl.getDiscount()).doubleValue();
                    discount2 += DmShared.vNull(medicineDetailMdl.getDiscount2()).doubleValue();
                    cgstAmt += DmShared.vNull(medicineDetailMdl.getCgstAmt());
                    igstAmt += DmShared.vNull(medicineDetailMdl.getIgstAmt());
                    if (defaultValue.isUnionTerritory()) {
                        sgstAmt += DmShared.vNull(medicineDetailMdl.getUgstAmt());
                    } else {
                        sgstAmt += DmShared.vNull(medicineDetailMdl.getSgstAmt());
                    }

                    taxableAmt += DmShared.vNull(medicineDetailMdl.getTaxableAmt());
                }
            }
        }
        grdMediDetails.commitChanges();
        roundoff = STClient.getRoundoff(billAmt);
        netAmount = roundoff + billAmt;
        fillItemGrossandDiscount(DmClient.vNull(itemGrossAmt, "0.00"), DmClient.vNull(discount, "0.00"), DmClient.vNull(taxableAmt, "0.00"), DmClient.vNull(cgstAmt, "0.00"),
                DmClient.vNull(sgstAmt, "0.00"), DmClient.vNull(igstAmt, "0.00"), DmClient.vNull(billAmt, "0.00"), DmClient.vNull(roundoff, "0.00"), DmClient.vNull(netAmount, "0.00"), DmClient.vNull(discount2, "0.00"));
        calculateLandedValue();
        resetTaxSummary();
    }

    public void calculateLandedValue() {
        double totalGrossAmount = 0, lineGrossAmount = 0;

        if (isIncludeTaxInCost) {
            landedValue = Math.abs(DmShared.vNull(txtNetAmount.getText()));
        } else {
            landedValue = Math.abs(DmShared.vNull(txtNetAmount.getText())) - Math.abs(DmShared.vNull(txtTaxAmount.getText()));
        }

        for (GrdMedicine_Mdl detailMdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (detailMdl != null) {
                if (!DmShared.xNull(detailMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    totalGrossAmount += Math.abs(DmShared.vNull(detailMdl.getAmount()));
                }
            }
        }

        double landedSkuValue = 0, totalLandedSkuValue = 0;
        GrdMedicine_Mdl lastLineMdl = null;
        for (GrdMedicine_Mdl lineMdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (lineMdl != null) {
                if (!DmShared.xNull(lineMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    lineGrossAmount = Math.abs(DmShared.vNull(lineMdl.getAmount()));

                    landedSkuValue = OverHead_Shared.getLandedValue(landedValue, totalGrossAmount, lineGrossAmount);
                    lineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));

                    totalLandedSkuValue += DmShared.vNull(lineMdl.getLandedSkuValue());
                    lastLineMdl = lineMdl;
                }
            }
        }

        if (lastLineMdl != null) {
            landedSkuValue = DmShared.vNull(DmClient.vNull(lastLineMdl.getLandedSkuValue(), "0.00")) - (DmShared.vNull(DmClient.vNull(totalLandedSkuValue, "0.00")) - DmShared.vNull(DmClient.vNull(landedValue, "0.00")));
            lastLineMdl.setLandedSkuValue(DmClient.vNull(landedSkuValue, "0.00"));
        }

        grdMediDetails.commitChanges();

    }

    private void fillItemGrossandDiscount(String ItemGrossAmt, String Discount, String taxableAmt, String cgstAmt, String sgstAmt, String igstAmt, String billAmt, String roundoff, String netAmt, String discount2) {
        txtItemGross.setText(ItemGrossAmt);
        txtDiscount.setText(Discount);
        txtDiscount2.setText(discount2);
        txtBillAmt.setText(billAmt);
        txtRoundoff.setText(roundoff);
        txtNetAmount.setText(netAmt);
        txtTaxableAmt.setText(taxableAmt);
        txtCGSTAmt.setText(cgstAmt);
        txtIGSTAmt.setText(igstAmt);
        txtSgstUgstAmt.setText(sgstAmt);
        txtTaxAmount.setText(DmClient.vNull(DmShared.vNull(sgstAmt).doubleValue() + DmShared.vNull(cgstAmt).doubleValue() + DmShared.vNull(igstAmt).doubleValue(), "0.00"));
    }

    private void clear() {
        hmPartyDetail_Mdl.clear();
        previousMdl = null;
        landedValue = 0;
        flwContainer.clear();
        lblPharmacyBalance.setText("");
        grdMediDetails.getGrid().getStore().clear();
        cardPayment = new CardPaymentPopup_Mdl();
        cardPayment = defaultValue.getCardPayment().clone();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        if (!DmShared.xNull(callFrom).equalsIgnoreCase("")) {
            userControl.setTextBlog(approvedByBlog, "");
        }

        txtGatePassNo.setText("");
        txtHVendor.setText("");
        txtHVendor.setTag("");
        txtVendor.setText("");
        txtVendor.setTag("");
        profitPercentage = "0";
        txtDate.setText("");
        txtDate.setTag("");
        txtDate.setTag2nd("");
        txtBillNo.setText("");
        txtBillNo.setTag("");
        txtBillNo.setTag2nd("");
        txtHConsultant.setText("");
        txtHConsultant.setTag("");
        txtHConsultant.setTag2nd("");
        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");

        txtReciptNo.setText("");
        txtReciptType.setText("");
        txtReciptType.setTag("");
        txtReciptType.setTag2nd("");
        txtHType.setTag("");
        txtHType.setText("");

        lblDiscarded.setText("");

        saleOn = defaultValue.getMedicineSaleOn();
        partyType = defaultValue.getPartyType();
        documentTypeCode = "";
        documentNatureCode = "";
        userEntDtLatestLine = "";
        roomCode = "";
        isReturnExists = "";
        flagCardPayment = false;
        // List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        taxSummary.reset(new ArrayList<List<OverHeadLine_Mdl>>());

        txtDueDate.setTag("");
        txtDueDate.setTag2nd("");
        txtDueDate.setText("");

    }

    private void clear2() {

        grdMediDetails.getGrid().getStore().clear();
        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
        taxSummary.reset(new ArrayList<List<OverHeadLine_Mdl>>());

        txtItemGross.setText("");
        txtDiscount.setText("");
        txtDiscount2.setText("");
        txtTaxAmount.setText("");
        txtRoundoff.setText("");
        txtNetAmount.setText("");

        txtItemGross.setTag("");
        txtDiscount.setTag("");
        txtDiscount2.setTag("");
        txtTaxAmount.setTag("");
        txtRoundoff.setTag("");
        txtNetAmount.setTag("");

        txtItemGross.setTag2nd("");
        txtDiscount.setTag2nd("");
        txtDiscount2.setTag2nd("");
        txtTaxAmount.setTag2nd("");
        txtRoundoff.setTag2nd("");
        txtNetAmount.setTag2nd("");

    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {

        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
            rootEvent.stopPropagation();
        }
    }

    String paymentSource = "";

    private void fillV_TypeDetail(TxtHVType_Mdl txtHVType_Mdl) {
        txtHVType.setTag(txtHVType_Mdl.getSharedKeyFieldCode());
        txtHVType.setText(txtHVType_Mdl.getSharedKeyFieldName());
        txtHVType.setTag2nd(txtHVType_Mdl.getVoucherCategory());
        txtHType.setTag("Debtor");
        txtHType.setText("Debtor");
        documentTypeCode = DmShared.xNull(txtHVType_Mdl.getDocumentTypeCode());
        documentNatureCode = DmShared.xNull(txtHVType_Mdl.getDocumentNatureCode());
        paymentSource = DmShared.xNull(txtHVType_Mdl.getPaymentSource());
    }

    private void setITSGrid(List<OverHeadITS_Mdl> lstITS, GrdMedicine_Mdl ourRow) {
        for (OverHeadITS_Mdl iTS_Mdl : lstITS) {
            if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("CGST")) {
                ourRow.setCgstPer(DmShared.xNull(iTS_Mdl.getAtRate()));
                ourRow.setCgstAcCode(DmShared.xNull(iTS_Mdl.getAccountCode()));
            } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("SGST")) {
                ourRow.setSgstPer(DmShared.xNull(iTS_Mdl.getAtRate()));
                ourRow.setSgstAcCode(DmShared.xNull(iTS_Mdl.getAccountCode()));
            } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("UGST")) {
                ourRow.setUgstPer(DmShared.xNull(iTS_Mdl.getAtRate()));
                ourRow.setUgstAcCode(DmShared.xNull(iTS_Mdl.getAccountCode()));
//            } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("G")) {
            } else if (DmShared.xNull(iTS_Mdl.getOverHeadManualCode()).equalsIgnoreCase("TV")) {
                ourRow.setSaleAcCode(DmShared.xNull(iTS_Mdl.getAccountCode()));
            }
        }

    }

    private void showSchemePopUp(GrdMedicine_Mdl detail) {
        if (isSchemeExists) {
            schemeMaster.show(detail.getSharedKeyFieldCode(), topControl.getMode(), txtHDepartment.getTag(), txtDate.getText(), selectedBatchSQL, String.valueOf(itemTotalQtyInPurchaseUnit));
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "FOCUS_ON_ADD":
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0 || !isDepartmentEditable) {
                                txtHType.setFocus(true);
                            } else {
                                txtHDepartment.setFocus(true);
                            }
                        }
                    });
                    break;
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void setPatientName_AddressText() {
        String patientName = DmShared.xNull(txtHVendor.getText());
        if (defaultValue != null) {
            if (DmShared.xNull(defaultValue.getPatientAlphabetCase()).equalsIgnoreCase("U")) {
                patientName = patientName.toUpperCase();
            } else if (DmShared.xNull(defaultValue.getPatientAlphabetCase()).equalsIgnoreCase("L")) {
                patientName = patientName.toLowerCase();
            } else if (DmShared.xNull(defaultValue.getPatientAlphabetCase()).equalsIgnoreCase("S")) {
                patientName = DmShared.sentenceCase(patientName);
            } else if (DmShared.xNull(defaultValue.getPatientAlphabetCase()).equalsIgnoreCase("P")) {
                patientName = DmShared.properCase(patientName);
            }
        }
        txtHVendor.setText(patientName);
    }

    private void resetTaxSummary() {
        List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        List<OverHeadLine_Mdl> overheadLine = null;
        OverHeadLine_Mdl overheadLineRow = null;

        for (GrdMedicine_Mdl grdMdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (grdMdl != null) {
                // loop start
                overheadLine = new ArrayList<OverHeadLine_Mdl>(); //1 line overhead

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode("CGST");
                overheadLineRow.setAtRate(grdMdl.getCgstPer());
                overheadLineRow.setAmount(grdMdl.getCgstAmt());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode("SGST");
                overheadLineRow.setAtRate(grdMdl.getSgstPer());
                overheadLineRow.setAmount(grdMdl.getSgstAmt());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode("UGST");
                overheadLineRow.setAtRate(grdMdl.getUgstPer());
                overheadLineRow.setAmount(grdMdl.getUgstAmt());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode("IGST");
                overheadLineRow.setAtRate(grdMdl.getIgstPer());
                overheadLineRow.setAmount(grdMdl.getIgstAmt());
                overheadLine.add(overheadLineRow);

                overheadLineRow = new OverHeadLine_Mdl();
                overheadLineRow.setOhManualCode("TV");
                //overheadLineRow.setAtRate(grdMdl.gett);
                overheadLineRow.setAmount(grdMdl.getTaxableAmt());
                overheadLine.add(overheadLineRow);

                lstOverheadLine.add(overheadLine);
                //loop end
            }
        }
        taxSummary.reset(lstOverheadLine);
    }

    private HashMap iniEgstPopupValue() {
        HashMap paramHashMap = new HashMap();
        boolean isEwayRequired = DmShared.iNull(txtHEWayBill.getTag()).intValue() == DmEnum.DmYesNo.YES.toInt();
        boolean isEinvRequired = true;
        paramHashMap.put("EWAY_DOCTYPE", isEwayRequired);
        paramHashMap.put("EWAY_SUBSUPPLYTYPE", isEwayRequired);
        paramHashMap.put("IS_EWAY_TRANSACTION_TYPE", isEwayRequired);
        paramHashMap.put("EINV_SUPPLYTYPE", isEinvRequired);
        paramHashMap.put("EINV_DOCTYPE", isEinvRequired);
        paramHashMap.put("TRANSPORTER", isEwayRequired);
        paramHashMap.put("TRANSPORTER_DETAIL", isEwayRequired);
        paramHashMap.put("GST_DOCUMENT", true);
//        paramHashMap.put("IS_BUYER_ADDRESS_EDITABLE", isEwayRequired || isEinvRequired);
        paramHashMap.put("IS_INTRA_STATE_AND_JOB_WORK", false);
//        paramHashMap.put("IS_CONSIGNEE_ADDRESS_EDITABLE", (isEwayRequired || isEinvRequired) && !DmShared.xNull(txtHConsignee.getTag()).trim().equalsIgnoreCase(""));
//        paramHashMap.put("IS_SHIP_TO_ADDRESS_EDITABLE", isEwayRequired || isEinvRequired);
        return paramHashMap;
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuGstActions)) {
            if (topControl.getMode().equalsIgnoreCase("Browse")
                    && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                SrvAsync.invokeRPC().getEgstDetail(DmShared.xNull(topControl.getDocId()), DmShared.xNull(txtHVType.getTag()), new AsyncCallback<DmResult<MasterEGst_Mdl>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResult<MasterEGst_Mdl> result) {
                        if (result.isStatus() && result.getData() != null) {
                            eGstPopup.show(result.getData(), iniEgstPopupValue());
                            lblDiscarded.setText(!DmShared.xNull(result.getData().geteGst_Mdl().getEinv_cancelRmrk()).equalsIgnoreCase("") ? "Discarded" : "");
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtHVType).show();
            }

        }
    }

    private void printRecord() {
        boolean isEwayRequired = DmShared.iNull(txtHEWayBill.getTag()).intValue() == DmEnum.DmYesNo.YES.toInt();
        boolean isWalkIn = DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("1");
        if (!isWalkIn) {
            if (!isEwayRequired) {
                if (DmShared.xNull(partyTypeForEntry).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                    showPrintPopup(false);
                } else {
                    if (DmShared.xNull(InvIrnNo).equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Printing Not Allowed ! E-Invoice is Mandatory.", (DmTextBox) null).show();
                        return;
                    } else {
                        showPrintPopup(false);
                    }
                }
            } else {
                if (DmShared.vNull(txtNetAmount.getText()).doubleValue() > DmShared.vNull(defaultValue.getEwayBillLimit()).doubleValue()) {
                    if (DmShared.xNull(partyTypeForEntry).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                        if (DmShared.xNull(txtEWayBillNo.getText()).equalsIgnoreCase("")) {
                            new DmAlertMessageBox("Printing Not Allowed ! E-Way is Mandatory.", (DmTextBox) null).show();
                            return;
                        } else {
                            showPrintPopup(false);
                        }
                    } else {
                        if (DmShared.xNull(txtEWayBillNo.getText()).equalsIgnoreCase("")
                                || DmShared.xNull(InvIrnNo).equalsIgnoreCase("")) {
                            new DmAlertMessageBox("Printing Not Allowed ! E-Way and E-Invoice is Mandatory.", (DmTextBox) null).show();
                            return;
                        } else {
                            showPrintPopup(false);
                        }
                    }
                } else {
                    if (DmShared.xNull(partyTypeForEntry).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                        showPrintPopup(false);
                    } else {
                        if (DmShared.xNull(InvIrnNo).equalsIgnoreCase("")) {
                            new DmAlertMessageBox("Printing Not Allowed ! E-Invoice is Mandatory.", (DmTextBox) null).show();
                            return;
                        } else {
                            showPrintPopup(false);
                        }
                    }
                }

            }
        } else {
            showPrintPopup(false);
        }
    }

    private void showPrintPopup(boolean printActualBill) {
        printPopup.show(this, printActualBill, true, callFrom);
    }

    private void manageEway() {
        boolean isEwayRequired = DmShared.iNull(txtHEWayBill.getTag()).intValue() == DmEnum.DmYesNo.YES.toInt();
        boolean isWalkIn = DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("1");

        if (!isWalkIn) {
            if (!isEwayRequired && !DmShared.xNull(partyTypeForEntry).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                new DmConfirmMessageBox("E-Invoice is Mandatory ! ", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            getEgstDetail();
                        }
                    }
                }).show();
            }
            if (isEwayRequired) {
                if (DmShared.xNull(partyTypeForEntry).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
                    if (DmShared.vNull(txtNetAmount.getText()).doubleValue() > DmShared.vNull(defaultValue.getEwayBillLimit()).doubleValue()) {
                        new DmConfirmMessageBox("E-Way is Mandatory ! ", new DialogHideEvent.DialogHideHandler() {
                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    getEgstDetail();
                                }
                            }
                        }).show();
                    }
                } else {
                    if (DmShared.vNull(txtNetAmount.getText()).doubleValue() > DmShared.vNull(defaultValue.getEwayBillLimit()).doubleValue()) {
                        new DmConfirmMessageBox("E-Invoice and E-Way is Mandatory ! ", new DialogHideEvent.DialogHideHandler() {
                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    getEgstDetail();
                                }
                            }
                        }).show();
                    } else {
                        new DmConfirmMessageBox("E-Invoice is Mandatory ! ", new DialogHideEvent.DialogHideHandler() {
                            @Override
                            public void onDialogHide(DialogHideEvent event) {
                                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                    getEgstDetail();
                                }
                            }
                        }).show();
                    }
                }
            }
        }
    }

    private void getEgstDetail() {
        SrvAsync.invokeRPC().getEgstDetail(DmShared.xNull(topControl.getDocId()), DmShared.xNull(txtHVType.getTag()), new AsyncCallback<DmResult<MasterEGst_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<MasterEGst_Mdl> result) {
                if (result.isStatus() && result.getData() != null) {
                    eGstPopup.show(result.getData(), iniEgstPopupValue());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
