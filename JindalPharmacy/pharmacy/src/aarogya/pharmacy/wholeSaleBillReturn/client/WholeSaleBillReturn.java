package aarogya.pharmacy.wholeSaleBillReturn.client;

import aarogya.pharmacy.partySearch.client.PartySearch;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHSaleBillNo_Mdl;
import aarogya.pharmacy.saleBillItemSearch.client.SaleBillItemSearch;
import aarogya.pharmacy.wholeSaleBillReturn.client.WholeSaleBillReturn_Dsg.GrdMediDetails_PrpAcs;
import aarogya.pharmacy.wholeSaleBillReturn.shared.Default_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.Find_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.GrdMedicine_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.MedicineReturnRequest_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.SearchUHID_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHMedicine_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHVType_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.TxtHVendor_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.WholeSaleBillReturn_Mdl;
import aarogya.pharmacy.wholeSaleBillReturn.shared.WholeSaleBillReturn_Srv;
import aarogya.pharmacy.wholeSaleBillReturn.shared.WholeSaleBillReturn_SrvAsync;
import aarogya.pharmacy.wholeSaleBillReturn.shared.WholeSaleBillReturn_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.pmPharmacy;
import aarogya.selfToolkit.cardPaymentPopup.client.CardPaymentPopup;
import aarogya.selfToolkit.cardPaymentPopup.shared.CardPaymentPopup_Mdl;
import aarogya.selfToolkit.cardPaymentPopup.shared.GrdCardPayment_Mdl;
import dataman.dmToolkit.modifyReason.client.ModifyReason;
import dataman.dmToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmException;
import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.DmResult;
import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.ServiceInvoker;
import dataman.dmBase.self.shared.SetWidgetInvoker;
import dataman.dmBase.self.shared.UserMenu;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.eGstPopup.client.EGstPopup;
import dataman.dmToolkit.gst.shared.EGst_Mdl;
import dataman.dmToolkit.gst.shared.MasterEGst_Mdl;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import dataman.dmToolkit.partyDetailPopup.client.PartyDetailPopup;
import dataman.dmToolkit.partyDetailPopup.shared.PartyDetailPopup_Mdl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WholeSaleBillReturn extends WholeSaleBillReturn_Dsg implements KeyPressHandler, BlurHandler, AttachEvent.Handler, ClickHandler, SelectionHandler<Item>, DmRootEvents {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String voucher_Cat = "", widgetName = "";
    private String documentTypeCode = "", documentNatureCode = "", refundDocId = "";
    private final ServiceInvoker<WholeSaleBillReturn_SrvAsync> SrvAsync;
    private CardPaymentPopup_Mdl cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
    private ModifyReason_Mdl modifyReasonMdl;
    private final ModifyReason modifyReason = new ModifyReason();
    private boolean dateEditable = false, isDepartmentEditable = false;
    private boolean billedMedicineSearch;
    protected boolean rateEditable = false, discountEditable = false, isAllowAddBatch = false;
    boolean ctoAllow = false, ctpAllow = false, ctrAllow = false, receiptAllow = false,
            walkInAllow = false, debtorAllow = false,
            ledgerAllow = false, paymentMode = false, saveCardInfo = true, showModifyReason = true;
    private final MedicineDetailsEdit<GrdMedicine_Mdl, GrdMediDetails_PrpAcs> mediDetails = new MedicineDetailsEdit(grdMediDetails, this);
    protected Default_Mdl defaultValue;
    private WholeSaleBillReturn_Mdl previousMdl;
    protected String partyTypeForEntry = "";
    protected STEnum.MedicineSaleOn saleOn;
    public String expiryAlertBeforeDays = "";
    private String userEntDtLatestLine = "", sbNetAmt = "", sbV_Type = "";
    String billDocId = "", nature = "", partyType = "", subCode = "", partyManualCode = "", placeOfSupply = "", partyGstin = "", partyGstinDate = "";
    private final CardPaymentPopup cardPaymentPopupSaleBillReturn = new CardPaymentPopup();
    private boolean isOpenedFromSbHelp = false, printRecord = false;
    private boolean flagRefund = false;
    HashMap<String, FieldUDT_Mdl> hashMapFilterPrint = new HashMap<>();
    final SaleBillItemSearch saleBillItemSearch = new SaleBillItemSearch();

    final PartySearch partySearch = new PartySearch();
    private PartySearch_Mdl partySearch_Mdl;
    private boolean flagCardPayment = false, isIncludeTaxInCost = false;
    double landedValue = 0;
    protected String convertedPaymentSource = null, mongoLinkId = "";

    private String moduleName = new pmPharmacy().moduleName;
    String profitPercentage = "0";

    private String returnRequestDocID = "";
    private String returnRequestReasonCode = "";
    private String returnRequestReasonName = "";
    private String approvalRequired = "";
    boolean isCreditToRegistration = false;

    private String saleReturnVTypeShortName = "";
    private String saleBillPaymentSourceCode = "";
    private final EGstPopup eGstPopup;
    protected String InvIrnNo = "";
    private final PartyDetailPopup partyDetailPopup = new PartyDetailPopup();
    protected final HashMap<Integer, PartyDetailPopup_Mdl> hmPartyDetail_Mdl = new HashMap<>();
    protected PartyDetailPopup_Mdl partyDetailPopup_Mdl;

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setCardViewRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setDeleteCaptionToDiscard();

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(rateEditableBlog)) {
                rateEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(departmentEditableBlog)) {
                isDepartmentEditable = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(addBatchBlog)) {
                isAllowAddBatch = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctoAllowBlog)) {
                ctoAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctpAllowBlog)) {
                ctpAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctrAllowBlog)) {
                ctrAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(receiptAllowBlog)) {
                receiptAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(walkInAllowBlog)) {
                walkInAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(debtorAllowBlog)) {
                debtorAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(paymentModeBlog)) {
                paymentMode = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(medicineSearchBlog)) {
                billedMedicineSearch = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(discountEditableBlog)) {
                discountEditable = true;
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
                if (DmShared.xNull(lblDiscarded.getText()).equalsIgnoreCase("Discarded")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                    return;
                }
                onEdit();
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                if (DmShared.xNull(lblDiscarded.getText()).equalsIgnoreCase("Discarded")) {
                    new DmAlertMessageBox("Record is already discarded.", (DmTextBox) null).show();
                    return;
                }

                SrvAsync.invokeRPC().isEodExist(txtDate.getText(), new AsyncCallback<DmResult<Boolean>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void onSuccess(DmResult<Boolean> result) {
                        if (!result.getData()) {
                            new DmConfirmMessageBox("Are you sure? You want to discard this record.", new DialogHideEvent.DialogHideHandler() {
                                @Override
                                public void onDialogHide(DialogHideEvent event) {
                                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                        showModifyReason();
                                    }
                                }
                            }).show();
                        } else {
                            new DmAlertMessageBox("Eod has been initialized can't be add / edit/ delet.", (DmTextBox) null).show();
                            return;
                        }
                    }
                });
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
                if (DmShared.iNull(txtHSaleBillNo.getTag()).longValue() > 0) {
                    saveCardInfo = true;
                    if (!DmShared.xNull(approvalRequired).trim().equalsIgnoreCase("1")) {
                        if (DmShared.xNull(txtRoundoff.getTag()).trim().equals("")) {
                            getRoundOffAcCode(true);
                        } else {
                            flagCardPayment = false;
                        }
                    } else {
                        if (!DmShared.xNull(txtHVType.getTag2nd()).trim().equalsIgnoreCase("PHCSR")) {
                            if (DmShared.xNull(txtRoundoff.getTag()).trim().equals("")) {
                                getRoundOffAcCode(true);
                            } else {
                                flagCardPayment = false;
                            }
                        } else if (!paymentMode) {
                            saveCardInfo = false;
                        } else {
                            if (DmShared.xNull(txtRoundoff.getTag()).trim().equals("")) {
                                getRoundOffAcCode(true);
                            } else {
                                flagCardPayment = false;
                            }
                        }
                    }
                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        topControlSave();
                    } else {
                        showModifyReason();
                    }
                } else {
                    new DmAlertMessageBox("Sale bill No.", txtHSaleBillNo).show();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(WholeSaleBillReturn.this);
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

    private WholeSaleBillReturn_Mdl chkValidation() {
        WholeSaleBillReturn_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        WholeSaleBillReturn_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    private void prnRecord(boolean printToPrinter) {
        hashMapFilterPrint.put("docId", new FieldUDT_Mdl("Return No.", DmShared.xNull(txtBillNo.getText()), DmShared.xNull(topControl.getDocId())));
        hashMapFilterPrint.put("returnDocId", new FieldUDT_Mdl("Sale Bill No.", DmShared.xNull(txtHSaleBillNo.getText()), DmShared.xNull(txtHSaleBillNo.getTag())));

        new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                    printRecord = true;
                    finalPrint(printRecord, hashMapFilterPrint);
                } else {
                    printRecord = false;
                    finalPrint(printRecord, hashMapFilterPrint);
                }
            }
        }).show();
    }

    private void finalPrint(boolean printToPrinter, HashMap hashMapFilter) {
        hashMapFilterPrint.put("mongoLinkId", new FieldUDT_Mdl("mongo Link Id", DmShared.xNull(txtBillNo.getText()), DmShared.xNull(this.mongoLinkId)));
        hashMapFilterPrint.put("returnDocId", new FieldUDT_Mdl("Sale ReturnBill No.", DmShared.xNull(txtBillNo.getText()), DmShared.xNull(this.topControl.getDocId())));

        new OpenSourceReport(this.txtHVType.getTag(), "", "Sale Bill Return Whole Sale", widgetName, hashMapFilterPrint, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));

    }

    private void textHelpEvents() {

        partySearch.addCallBack(new PartySearch.CallBack() {
            @Override
            public void ok(PartySearch_Mdl partySearchDetails) {
                partySearch_Mdl = partySearchDetails;
                fillPartyDetails();
            }

            @Override
            public void close(boolean isErrorClose) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                    }
                });
            }
        });

        saleBillItemSearch.addCallBack(new SaleBillItemSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                fillSaleBillDetails(listKF);
            }

            @Override
            public void close() {
            }
        });

        cardPaymentPopupSaleBillReturn.addCallBack(new CardPaymentPopup.CallBack() {

            @Override
            public void onOk(CardPaymentPopup_Mdl cardPaymentMdl) {
                cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
                cardPaymentSaleBillReturn = cardPaymentMdl.clone();

                if (topControl.getMode().equalsIgnoreCase("New")) {
                    topControlSave();
                } else {
                    if (showModifyReason) {
                        showModifyReason();
                    } else {
                        topControlSave();
                    }
                }
            }

            @Override
            public void onCancel(CardPaymentPopup_Mdl cardPaymentMdl) {

            }
        });

        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                WholeSaleBillReturn.this.modifyReasonMdl = modifyReason;
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVType(topControl.getPermissions(), voucher_Cat, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>>() {

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
                    saleReturnVTypeShortName = "";
                    fillV_TypeDetail(t1Row);
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());

                    paymentSource = DmShared.xNull(t1Row.getPaymentSource());
                    saleReturnVTypeShortName = DmShared.xNull(t1Row.getShortName());
                }
            }
        });

        txtHMedicineReturnRequest.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<MedicineReturnRequest_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getMedicineReturnRequests(txtHDepartment.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<MedicineReturnRequest_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<MedicineReturnRequest_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicineReturnRequest.setData(result.getData(), result.getPaging());
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, MedicineReturnRequest_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    returnRequestDocID = t1Row.getSharedKeyFieldCode();
                    returnRequestReasonCode = t1Row.getGstReasonCode();
                    returnRequestReasonName = t1Row.getGstReasonName();
                    getReturnMedicineRequests(t1Row.getSharedKeyFieldCode());
                }
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

        txtHSaleBillNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHSaleBillNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSaleBillNo(helper, paymentSource,txtHDebtorsName.getTag(), blnToEndOfFile, txtHDepartment.getTag(), txtDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), DmShared.xNull(defaultValue.getPharamcyReturnDays()), DmShared.xNull(txtHVType.getTag2nd()), new AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSaleBillNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHSaleBillNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHSaleBillNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSaleBillNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHSaleBillNo_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    isOpenedFromSbHelp = true;
                    saleBillPaymentSourceCode = "";
                    saleBillPaymentSourceCode = DmShared.iNull(t1Row.getPaymentSourceCode()).toString();
                    txtHSaleBillNo.setTag2nd(DmShared.dNull(t1Row.getSaleBillDate()));
                    sbNetAmt = t1Row.getNetAmount();
                    sbV_Type = t1Row.getV_Type();

                    if (DmShared.xNull(t1Row.getPaymentSourceName()).equalsIgnoreCase(STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumName())) {
                        isCreditToRegistration = true;
                    } else {
                        isCreditToRegistration = false;
                    }

                    if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
                        txtHDepartment.setTag(DmShared.xNull(t1Row.getDepartmentCode()));
                        txtHDepartment.setText(DmShared.xNull(t1Row.getDepartmentName()));
                    }
                }
                showSaleBillItemSearch();
                getUHIDDetail();
                enableDisable_RefNo_SaleBillNo();
            }
        });

        txtHGstReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGstReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGstReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGstReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHDebtorsName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHDebtorsName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDebtorsName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDebtorsName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDebtorsName).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                profitPercentage = "0";
                if (blnStatus && t1Row != null) {
                    profitPercentage = DmShared.xNull(t1Row.getSharedCancelledBy());
                    partyGstin = DmShared.xNull(t1Row.getSharedPreparedBy());
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
                    fillVendorDetails(txtHTransporter.getTag(), DmEnum.PartyTransRecordType.TRANSPORTER.getCode());
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

    private void getUHIDDetail() {
        if (DmShared.dNull(txtDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Voucher Date is mandatory.", txtDate).show();
            return;
        }
        SearchUHID_Mdl searchMdl = new SearchUHID_Mdl();
        searchMdl.setDocIdSaleBill(DmShared.xNull(txtHSaleBillNo.getTag()));
        searchMdl.setVoucherDate(DmShared.dNull(txtDate.getText()));
        SrvAsync.invokeRPC().getUHIDDetails(searchMdl, new AsyncCallback<DmResult<WholeSaleBillReturn_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHSaleBillNo).show();
            }

            @Override
            public void onSuccess(DmResult<WholeSaleBillReturn_Mdl> result) {
                if (result.isStatus()) {
                    txtHDebtorsName.setTag(DmShared.xNull(result.getData().getDebtorCode()));
                    txtHDebtorsName.setText(DmShared.xNull(result.getData().getDebtorName()));
                    txtPlaceOfSupply.setTag(DmShared.xNull(result.getData().getPlaceOfSupply()));
                    txtPlaceOfSupply.setText(DmShared.xNull(result.getData().getPlaceOfSupplyName()));
                    fillVendorDetails(txtHDebtorsName.getTag(), DmEnum.PartyTransRecordType.PARTY.getCode());
                    txtHDebtorsName.setEnabled(false);
                    txtHGstReason.setFocus(true);
                }
            }
        });

    }

    private void fillVendorDetails(String vendorCode, final int transactionPartyNatureCode) {
        SrvAsync.invokeRPC().fillVendorDetails(vendorCode, transactionPartyNatureCode, new AsyncCallback<DmResult<PartyDetailPopup_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
            }

            @Override
            public void onSuccess(DmResult<PartyDetailPopup_Mdl> result) {
                if (result.isStatus()) {
                    partyDetailPopup_Mdl = result.getData();
                    hmPartyDetail_Mdl.put(transactionPartyNatureCode, result.getData());
                    partyTypeForEntry = partyDetailPopup_Mdl.getPartyGstType();
                    partyGstin = DmShared.xNull(partyDetailPopup_Mdl.getGstin());
                    partyGstinDate = DmShared.xNull(partyDetailPopup_Mdl.getGstinDate());
                    if (!DmShared.xNull(partyDetailPopup_Mdl.getSharedPreparedIP()).equalsIgnoreCase("")) {
                        txtHLocation.setTag(DmShared.xNull(partyDetailPopup_Mdl.getSharedPreparedIP()));
                        txtHLocation.setText(DmShared.xNull(partyDetailPopup_Mdl.getSharedPreparedBy()));
                    }

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHVType).show();
                }
            }
        });
    }

    private void fillPartyDetails() {
        resetPatientDetails();
        if (txtHDebtorsName.isEnabled()) {
            txtHDebtorsName.setEnabled(false);
        }
        if (partySearch_Mdl != null) {
            txtHDebtorsName.setText(partySearch_Mdl.getSharedKeyFieldName());
            partyType = DmShared.xNull(partySearch_Mdl.getPartTypeCode());
            subCode = partySearch_Mdl.getSharedKeyFieldCode();
            partyManualCode = partySearch_Mdl.getManualCode();
            setPlaceOfSupply(partySearch_Mdl.getStateCode());
            btnLedgerSearch.removeFromParent();
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                txtHGstReason.setFocus(true);
            }
        });

    }

    private void resetPatientDetails() {
        partyGstin = "";
        partyGstinDate = "";
        txtHDebtorsName.setText("");
        partyType = defaultValue.getPartyType();
        subCode = "";
        partyManualCode = "";
        placeOfSupply = defaultValue.getStateCode();
        flagCardPayment = false;
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
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtHVType).show();
            }
        }
        if (event.getSource().equals(mnuAutoVoucherAdjustment)) {
            if (topControl.getMode().equalsIgnoreCase("Browse")
                    && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                new DmConfirmMessageBox("Are you sure? You want to set adjustment.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            processAutoVoucherAdjustment();
                        }
                    }
                }).show();

            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtHVType).show();
            }
        }
    }

    private void processAutoVoucherAdjustment() {
        String docId = topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId();
        if (DmShared.iNull(DmShared.xNull(docId)).longValue() == 0) {
            new DmAlertMessageBox("Voucher ID is required for voucher adjustment.", (DmTextBox) null).show();
            return;
        }

        SrvAsync.invokeRPC(true).processAutoVoucherAdjustment(DmShared.xNull(docId), DmShared.xNull(txtHDebtorsName.getTag()), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    new DmAlertMessageBox("Voucher Adjustment is processed successfully!", (DmTextBox) null).show();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });

    }

    private void fillSaleBillDetails(List<KeyFieldExtended> listKF) {
        SrvAsync.invokeRPC().getSaleBillItemDetails(listKF, DmShared.dNull(txtDate.getText()), DmShared.iNull(txtHDepartment.getTag()).toString(), new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {

                if (result.isStatus()) {
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().addAll(result.getData());
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                    grdMediDetails.commitChanges();
                    refreshItemDetails(result.getData());

                    if (!isOpenedFromSbHelp) {
                        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                        txtHSaleBillNo.setTag2nd("");
                        txtHSaleBillNo.setTag("");
                        txtHSaleBillNo.setText("");
                        txtRefNo.setText("");
                        txtRefDate.setText("");
                    }
                    txtHSaleBillNo.setEnabled(false);
                    txtRefNo.setEnabled(false);
                    txtRefNo.setEnabled(false);
                    if (grdMediDetails.getGrid().getStore().size() - 1 >= 0) {
                        ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell((grdMediDetails.getGrid().getStore().size() - 1), 1);
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }

            }
        });
    }

    private void refreshItemDetails(List<GrdMedicine_Mdl> lstGrdItemDetail) {
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

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
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

    private void gridEvents() {
        eGstPopup.addCallBack(new EGstPopup.CallBack() {
            @Override
            public void onOk(EGst_Mdl eGst_Mdl) {
                topControlMoveRec(DmShared.xNull(eGst_Mdl.getDocid()));
            }
        });

        mediDetails.addCallBack(new MedicineDetailsEdit.CallBack() {

            @Override
            public void onOk(GrdMedicine_Mdl grdtest) {
                grdMediDetails.commitChanges();
                calculate();
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
                if (DmShared.xNull(returnRequestDocID).trim().equalsIgnoreCase("")) {
                    gridMediDetailsEdit(event.getRowIndex());
                }
            }
        });

        grdMediDetails.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    if (DmShared.xNull(returnRequestDocID).trim().equalsIgnoreCase("")) {
                        gridMediDetailsEdit(grdMediDetails.getGrid().getStore().indexOf(grdMediDetails.getGrid().getSelectionModel().getSelectedItem()));
                    }
                }
            }
        }, KeyDownEvent.getType());
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == labelDiscount) {
        } else if (event.getSource().equals(lblTransporter)) {
            showPartyDetailPopup(lblTransporter, DmEnum.PartyTransRecordType.TRANSPORTER.getCode());
        }
        btnClick((Button) event.getSource());
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
                if (!DmShared.xNull(txtHDebtorsName.getTag()).trim().equalsIgnoreCase("")) {
                    if (!DmShared.xNull(partyTypeForEntry).equals(DmEnum.DmPartyType.Unregistered.getId())) {
                        isNameEditable = true;
                        isAddressEditable = true;
                    } else {
                        isAdPlaceOfBusinessEditable = true;
                    }
                }
            }
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
        if (button == btnSaleBillSearch) {
            isOpenedFromSbHelp = false;
            showSaleBillItemSearch();
        } else if (button == btnLedgerSearch) {
            partySearch.show(DmShared.dNull(txtDate.getText()), defaultValue.getVoucherTypeMdl().getSharedKeyFieldCode());
        } else if (button == btnPayment) {
            if (widgetName.equalsIgnoreCase(new pmPharmacy().WHOLE_SALE_BILL_RETURN.caption)) {

            } else {
                new DmAlertMessageBox("Not Allow!", txtHVType).show();
            }
        }

    }

    private void showSaleBillItemSearch() {

        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please Select Department.", txtHDepartment).show();
            return;
        }
        if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Date.", txtDate).show();
            return;
        }
        saleBillItemSearch.show(txtHSaleBillNo.getTag(), DmShared.dNull(txtDate.getText()), voucher_Cat, txtHVType.getTag2nd(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), DmShared.xNull(txtHDepartment.getTag()), "", "", DmShared.xNull(defaultValue.getPharamcyReturnDays()), "wholeSaleBillReturn");
    }

    private void gridMediDetailsEdit(Integer currentRowIndex) {
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please Select Department.", txtHDepartment).show();
            return;
        }
        if (DmShared.xNull(txtHDebtorsName.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Patient Name.", txtHDebtorsName).show();
            return;
        }
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            mediDetails.show(currentRowIndex, false, false, false);
        } else {
            if (DmShared.iNull(txtHSaleBillNo.getTag()).longValue() != 0) {
                mediDetails.show(currentRowIndex, true, false, true);
            } else {
                mediDetails.show(currentRowIndex, true, true, true);
            }
        }
    }

    public WholeSaleBillReturn(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super(paramHashMap != null ? (String) paramHashMap.get("widgetName") : new pmPharmacy().WHOLE_SALE_BILL_RETURN.caption);
        SrvAsync = new ServiceInvoker<>(GWT.create(WholeSaleBillReturn_Srv.class), WholeSaleBillReturn.class.getName());
        if (paramHashMap != null) {
            widgetName = (String) paramHashMap.get("widgetName");
            voucher_Cat = (String) paramHashMap.get("vCategory");
        }

        this.asWidget().setTitle(widgetName);
        eGstPopup = new EGstPopup(SrvAsync);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        handlerEvents();
        iniDefaultValues(swi);
        grdMediDetails.setDefaultColumnIndex(8);    //TBC
    }

    private void handlerEvents() {
        btnSaleBillSearch.addClickHandler(this);
        txtDate.addBlurHandler(this);
        txtRefDate.addBlurHandler(this);
        txtRefNo.addBlurHandler(this);
        flwForm.addAttachHandler(this);
        labelDiscount.addClickHandler(this);
        btnPayment.addClickHandler(this);
        btnLedgerSearch.addClickHandler(this);
        txtLRDate.addBlurHandler(this);
        mnuGstActions.addSelectionHandler(this);
        mnuAutoVoucherAdjustment.addSelectionHandler(this);

        if (!paymentMode) {
            btnPayment.setEnabled(false);
        } else if (!paymentMode) {
            btnPayment.setEnabled(true);
        }
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultVal(voucher_Cat, topControl.getPermissions(), new AsyncCallback<DmResult<Default_Mdl>>() {

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
                    saleOn = defaultValue.getMedicineSaleOn() != null ? defaultValue.getMedicineSaleOn() : MedicineSaleOn.SALE_RATE;
                    approvalRequired = DmShared.xNull(defaultValue.getApprovalRequired()).trim();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    manageDisplay(false);
                    swi.setWidget(WholeSaleBillReturn.this);
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

    private WholeSaleBillReturn_Mdl topControlSaveInitialize() {
        WholeSaleBillReturn_Mdl mrTemp = new WholeSaleBillReturn_Mdl();

        setPatientName_AddressText();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdSaleBillReturn().setRecIdValue(DmShared.xNull(txtBillNo.getText()));
            mrTemp.getRecIdSaleBillReturn().setCounter(DmShared.iNull(txtBillNo.getTag()).longValue());
            mrTemp.getRecIdSaleBillReturn().setPrefix(DmShared.xNull(txtBillNo.getTag2nd()));
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtBillNo.getText()));
        mrTemp.setControlAccount(defaultValue.getControlAccount());
        mrTemp.setDocumentNatureCode(DmShared.xNull(documentNatureCode));
        mrTemp.setDocumentTypeCode(DmShared.xNull(documentTypeCode));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVType.getTag()));
        mrTemp.setVoucherShortName(DmShared.xNull(txtHVType.getTag2nd()));
        mrTemp.setApprovalRequired(DmShared.xNull(approvalRequired));
        mrTemp.setVoucherDate(DmShared.dNull(txtDate.getText()));
        mrTemp.setDocIdSaleBill(DmShared.xNull(txtHSaleBillNo.getTag()));
        mrTemp.setRecIdSaleBill(DmShared.xNull(txtHSaleBillNo.getText()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setDebtorName(DmShared.xNull(txtHDebtorsName.getText()));
        mrTemp.setPartySubCode(DmShared.xNull(txtHDebtorsName.getTag()));
        mrTemp.setV_TypeSaleBill(DmShared.xNull(sbV_Type));
        mrTemp.setNet_AmtSaleBill(DmShared.xNull(sbNetAmt));
        mrTemp.setCardPaymentPopupNetAmount(DmShared.xNull(DmShared.vNull(0 - DmShared.vNull(txtNetAmount.getText()).doubleValue()).toString()));
        mrTemp.setFlagCardPayment(flagCardPayment ? "1" : "0");

        mrTemp.setsBillType(DmShared.iNull(STEnum.WholeSaleType.DEBTOR.getId()).toString());
        mrTemp.setPartyType(DmShared.xNull(partyTypeForEntry));
        mrTemp.setSubCode(DmShared.xNull(txtHDebtorsName.getTag()));
        mrTemp.setGstReasonCode(DmShared.xNull(txtHGstReason.getTag()));
        mrTemp.setGstReasonName(DmShared.xNull(txtHGstReason.getText()));
        mrTemp.setPartyManualCode(DmShared.xNull(partyManualCode));
        mrTemp.setSiteStateCode(DmShared.xNull(defaultValue.getStateCode()));
        mrTemp.setProfitPercentage(DmShared.xNull(profitPercentage));

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
        mrTemp.setPlaceOfSupply(DmShared.xNull(txtPlaceOfSupply.getTag()));
        mrTemp.setPlaceOfSupplyName(DmShared.xNull(txtPlaceOfSupply.getText()));
        mrTemp.setVehicleTypeCode(DmShared.xNull(txtHVehicleType.getTag()));
        mrTemp.setVehicleTypeName(DmShared.xNull(txtHVehicleType.getText()));

        mrTemp.setDefaultPharmacyServiceCode(DmShared.xNull(defaultValue.getDefaultPharmacyServiceCode()));

        mrTemp.setRefDate(DmShared.dNull(txtRefDate.getText()));
        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));
        mrTemp.setPartyGstin(DmShared.xNull(partyGstin));
        mrTemp.setPartyGstinDate(DmShared.xNull(partyGstinDate));

        mrTemp.setUserEntDtLatestLine(DmShared.xNull(userEntDtLatestLine));

        mrTemp.setPartyDetail_Mdl((HashMap<Integer, PartyDetailPopup_Mdl>) hmPartyDetail_Mdl.clone());

        if (modifyReasonMdl != null) {
            mrTemp.setModifyReasonMdl(modifyReasonMdl);
        }
        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setNetAmount(DmClient.vNull(txtNetAmount.getText(), "0.00"));
        mrTemp.setLandedValue(DmClient.vNull(landedValue, "0.00"));
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
        if (widgetName.equalsIgnoreCase(new pmPharmacy().WHOLE_SALE_BILL_RETURN.caption)) {
            mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList().addAll(new ArrayList<GrdCardPayment_Mdl>());
            mrTemp.setCardPaymentSaleBillReturn(cardPaymentSaleBillReturn.clone());
        }

        mrTemp.setSaleReturnVTypeShortName(saleReturnVTypeShortName);
        mrTemp.setSaleBillPaymentSourceCode(saleBillPaymentSourceCode);

        return mrTemp;
    }

    private void topControlSave() {
        WholeSaleBillReturn_Mdl mrTemp;

        try {
            calculate();
            mrTemp = chkValidation();
            SrvAsync.invokeRPC().saveRecord(voucher_Cat, mrTemp, previousMdl, DmClient.getVoucherPrefix(), returnRequestDocID, saveCardInfo, showModifyReason, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        topControlMoveRec(topControl.getDocId());
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
//            case 110:
//                new DmAlertMessageBox(errorMessage, txtHType).show();
//                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtDate).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            case 400:
                new DmAlertMessageBox(errorMessage, txtHDebtorsName).show();
                break;
            case 410:
                new DmAlertMessageBox(errorMessage, txtHGstReason).show();
                break;
            case 420:
                new DmAlertMessageBox(errorMessage, txtHLocation).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdMedicine_Mdl> listTemp = grdMediDetails.getGrid().getStore().getAll();
                    for (GrdMedicine_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getSbDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getSbV_Sno()).equalsIgnoreCase(errorSourceCode)) {
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
                new DmAlertMessageBox(errorMessage, txtRemark).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<WholeSaleBillReturn_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<WholeSaleBillReturn_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<WholeSaleBillReturn_Mdl> result) {
        WholeSaleBillReturn_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            previousMdl = new WholeSaleBillReturn_Mdl(data);
            txtDate.setEnabled(false);
            txtHDepartment.setEnabled(false);
            topControl.setDocId(data.getSharedKeyFieldCode());
            paymentSource = DmShared.xNull(data.getPaymentSource());
            txtBillNo.setText(data.getSharedKeyFieldName());
            txtBillNo.setTag(DmShared.iNull(data.getRecIdSaleBillReturn().getCounter()).toString());
            txtBillNo.setTag2nd(DmShared.xNull(data.getRecIdSaleBillReturn().getPrefix()));

            txtHVType.setText(DmShared.xNull(data.getVoucherTypeName()));
            txtHVType.setTag(DmShared.xNull(data.getVoucherTypeCode()));
            txtHVType.setTag2nd(DmShared.xNull(data.getVoucherCategory()));
            documentNatureCode = DmShared.xNull(data.getDocumentNatureCode());
            documentTypeCode = DmShared.xNull(data.getDocumentTypeCode());

            partyTypeForEntry = data.getPartyType();
            placeOfSupply = DmShared.xNull(data.getPlaceOfSupply());
            subCode = DmShared.xNull(data.getSubCode());
            partyManualCode = DmShared.xNull(data.getPartyManualCode());
            InvIrnNo = DmShared.xNull(data.geteInvIrnNo());
            partyGstin = data.getPartyGstin();
            partyGstinDate = data.getPartyGstinDate();
            profitPercentage = DmShared.xNull(data.getProfitPercentage());

            txtHTransportMode.setText(data.getTransportModeName());
            txtHTransportMode.setTag(data.getTransportModeCode());
            txtLRNo.setText(data.getLrNo());
            txtLRDate.setText(data.getLrDate());
            txtEWayBillDate.setTag(data.geteWayBillDateTime());
            txtEWayBillDate.setText(data.geteWayBillDateTime());
            txtEWayBillNo.setText(data.geteWayBillNo());
            txtHVehicleType.setTag(DmShared.xNull(data.getVehicleTypeCode()));
            txtHVehicleType.setText(DmShared.xNull(data.getVehicleTypeName()));
            txtPlaceOfSupply.setTag(DmShared.xNull(data.getPlaceOfSupply()));
            txtPlaceOfSupply.setText(DmShared.xNull(data.getPlaceOfSupplyName()));
            txtVehicleNo.setText(data.getVehicleNo());
            txtHTransporter.setText(data.getTransporterName());
            txtHTransporter.setTag(data.getTransporterCode());
            txtHLocation.setText(data.getLocationName());
            txtHLocation.setTag(data.getLocationCode());
            txtHLocation.setTag2nd(data.getLocationCode());

            txtDate.setText(DmShared.dNull(data.getVoucherDate()));
            txtDate.setTag2nd(DmShared.dNull(data.getVoucherDate()));
            txtHDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtHDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            txtHDebtorsName.setTag(DmShared.xNull(data.getPartySubCode()));
            txtHDebtorsName.setText(DmShared.xNull(data.getDebtorName()));
            txtHSaleBillNo.setTag(DmShared.xNull(data.getDocIdSaleBill()));
            txtHSaleBillNo.setText(DmShared.xNull(data.getRecIdSaleBill()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            mongoLinkId = DmShared.xNull(data.getMongoLinkId());

            txtHGstReason.setText(DmShared.xNull(data.getGstReasonName()));
            txtHGstReason.setTag(DmShared.iNull(data.getGstReasonCode()).toString());

            txtRefDate.setText(DmShared.dNull(data.getRefDate()));
            txtRefNo.setText(DmShared.xNull(data.getRefNo()));

            sbNetAmt = DmShared.xNull(data.getNet_AmtSaleBill());
            sbV_Type = DmShared.xNull(data.getV_TypeSaleBill());

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
            txtNetAmount.setText(DmClient.vNull(data.getNetAmount(), "0.00"));
            txtNetAmount.setTag(DmClient.vNull(data.getNetAmount(), "0.00"));
            landedValue = DmShared.vNull(data.getLandedValue());
            userEntDtLatestLine = DmShared.xNull(data.getUserEntDtLatestLine());

            if (DmShared.vNull(data.getPharmacyBalance()).doubleValue() != 0) {
                lblPharmacyBalance.setText("Pharmacy Balance : " + data.getPharmacyBalance());
            }

            hmPartyDetail_Mdl.clear();
            hmPartyDetail_Mdl.putAll((HashMap<Integer, PartyDetailPopup_Mdl>) data.getPartyDetail_Mdl().clone());

            cardPaymentSaleBillReturn = data.getCardPaymentSaleBillReturn();
            cardPaymentSaleBillReturn.getTxtHReceiptTypeVList().addAll(defaultValue.getCardPaymentReturn().getTxtHReceiptTypeVList());

            convertedPaymentSource = DmShared.xNull(data.getPaymentSource());
            lblDiscarded.setText(DmShared.iNull(data.getCancelRecord()).intValue() == 1 ? "Discarded" : "");
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            grdMediDetails.getGrid().getStore().clear();
            grdMediDetails.getGrid().getStore().addAll(data.getGrdMediDetails());

            returnRequestDocID = DmShared.xNull(data.getMedicineReturnRequestDocID());
            refundDocId = DmShared.xNull(data.getRefundDocId());

            resetTaxSummary();

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
        manageDisplay(false);

    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, modifyReasonMdl, DmShared.xNull(txtHSaleBillNo.getTag()), DmShared.xNull(txtHVType.getTag2nd()), new AsyncCallback<DmResult<String>>() {

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
                        getRoundOffAcCode(false);
                    }
                }
            } catch (Exception e) {
            }
        }
        if (event.getSource() == txtRefDate) {
            txtRefDate.setText(DmClient.returnDate(txtRefDate.getText()));
            enableDisable_RefNo_SaleBillNo();
        } else if (event.getSource() == txtRefNo) {
            enableDisable_RefNo_SaleBillNo();
        } else if (event.getSource() == txtLRDate) {
            txtLRDate.setText(DmClient.returnDate(txtLRDate.getText()));
        } else if (event.getSource().equals(txtHDebtorsName)) {
            setPatientName_AddressText();
        }
    }

    private void enableDisable_RefNo_SaleBillNo() {
        if (!txtRefDate.isEnabled()) {
            txtRefDate.setEnabled(true);
        }
        if (!txtRefNo.isEnabled()) {
            txtRefNo.setEnabled(true);
        }
        if (!txtHSaleBillNo.isEnabled()) {
            txtHSaleBillNo.setEnabled(true);
        }
        if (DmShared.iNull(txtHSaleBillNo.getTag()).longValue() == 0) {
            if (!DmShared.dNull(txtRefDate.getText()).equalsIgnoreCase("") || !DmShared.xNull(txtRefNo.getText()).equalsIgnoreCase("")) {
                txtHSaleBillNo.setEnabled(false);
                txtHSaleBillNo.setText("");
                txtHSaleBillNo.setTag("");
                txtHSaleBillNo.setTag2nd("");
                sbNetAmt = "";
                sbV_Type = "";
            }
        } else if (DmShared.iNull(txtHSaleBillNo.getTag()).longValue() > 0) {
            txtRefNo.setEnabled(false);
            txtRefDate.setEnabled(false);
            txtRefNo.setText(DmShared.xNull(txtHSaleBillNo.getText()));
            txtRefDate.setText(DmShared.dNull(txtHSaleBillNo.getTag2nd()));
        }
    }

    private void getRoundOffAcCode(final boolean isSaveCall) {
        SrvAsync.invokeRPC().getRoundOffAcCode(DmShared.dNull(txtDate.getText()), new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtDate).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    txtRoundoff.setTag(result.getData());
                    if (isSaveCall) {
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            mediDetails.hide();
            eGstPopup.hide();
        }

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        setDefaultValue();
    }

    private void onEdit() {
        manageDisplay(true);
        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
        txtRemark.setFocus(true);
        isExistEod();
    }

    private void setDefaultValue() {
        if (defaultValue.getVoucherTypeMdl() != null) {
            fillV_TypeDetail(defaultValue.getVoucherTypeMdl());
        }

        txtHDepartment.setText(defaultValue.getDepartmentName());
        txtHDepartment.setTag(defaultValue.getDepartmentCode());
        txtRoundoff.setTag(defaultValue.getRoundOffAcCode());

        grdMediDetails.getGrid().getStore().clear();
        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());

        getServerTime();
    }

    private void manageDisplay(boolean flag) {
        txtDate.setEnabled(false);
        txtBillNo.setEnabled(false);
        txtHDepartment.setEnabled(false);
        txtItemGross.setEnabled(false);
        txtDiscount.setEnabled(false);
        txtBillAmt.setEnabled(false);
        txtRoundoff.setEnabled(false);
        txtNetAmount.setEnabled(false);
        txtTaxableAmt.setEnabled(false);
        txtTaxAmount.setEnabled(false);
        txtCGSTAmt.setEnabled(false);
        txtSgstUgstAmt.setEnabled(false);
        txtIGSTAmt.setEnabled(false);
        txtRefNo.setEnabled(false);
        txtRefDate.setEnabled(false);
        txtDiscount2.setEnabled(false);

        flwPayment.clear();

        manageSaleBillSearch();

        if (widgetName.equalsIgnoreCase(new pmPharmacy().WHOLE_SALE_BILL_RETURN.caption)) {
            flwPayment.add(btnPayment);
        }
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtDate.setEnabled(dateEditable);
            txtHDebtorsName.setEnabled(false);
            txtHDepartment.setEnabled(false);
            txtHVType.setEnabled(false);
            txtHSaleBillNo.setEnabled(false);
        } else if (topControl.getMode().equalsIgnoreCase("New")) {
            txtHVType.setEnabled(true);
            txtHSaleBillNo.setEnabled(true);
            txtDate.setEnabled(dateEditable);
            txtHDepartment.setEnabled(false);
            if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0) {
                txtHDepartment.setEnabled(isDepartmentEditable);
            }
        }

        if (flag) {
            enableDisable_RefNo_SaleBillNo();
        }
        lblSgstUgst.setText(defaultValue.isUnionTerritory() ? "UGST Amount" : "SGST Amount");
    }

    private void manageSaleBillSearch() {
        flwSaleBillSearch.clear();
        if (topControl.getMode().equalsIgnoreCase("New")) {
            flwSaleBillSearch.add(btnSaleBillSearch);
        }
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
                        txtHDepartment.setFocus(true);
                    }
                });
                isExistEod();
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
                    discount += DmShared.vNull(medicineDetailMdl.getDiscount());
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
                    totalGrossAmount += Math.abs(DmShared.vNull(detailMdl.getTaxableAmt()));
                }
            }
        }

        double landedSkuValue = 0, totalLandedSkuValue = 0;
        GrdMedicine_Mdl lastLineMdl = null;
        for (GrdMedicine_Mdl lineMdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (lineMdl != null) {
                if (!DmShared.xNull(lineMdl.getSharedKeyFieldCode()).trim().equals("")) {
                    lineGrossAmount = Math.abs(DmShared.vNull(lineMdl.getTaxableAmt()));

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

    private void clear() {
        previousMdl = null;
        landedValue = 0;
        hmPartyDetail_Mdl.clear();

        grdMediDetails.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
        cardPaymentSaleBillReturn = defaultValue.getCardPaymentReturn().clone();

        lblPharmacyBalance.setText("");

        txtDate.setText("");
        txtDate.setTag("");
        txtDate.setTag2nd("");
        txtBillNo.setText("");
        txtBillNo.setTag("");
        txtBillNo.setTag2nd("");
        txtRefNo.setText("");
        txtRefNo.setTag("");
        txtRefNo.setTag2nd("");
        txtRefDate.setText("");
        txtRefDate.setTag("");
        txtRefDate.setTag2nd("");
        profitPercentage = "0";

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");
        billDocId = "";
        nature = "";

        sbNetAmt = "";
        sbV_Type = "";

        lblDiscarded.setText("");

        saleOn = defaultValue.getMedicineSaleOn();
        partyType = defaultValue.getPartyType();
        documentTypeCode = "";
        documentNatureCode = "";
        userEntDtLatestLine = "";

        subCode = "";
        partyManualCode = "";
        placeOfSupply = defaultValue.getStateCode();
        flagCardPayment = false;
        returnRequestDocID = "";
        returnRequestReasonCode = "";
        returnRequestReasonName = "";
        //  List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        taxSummary.reset(new ArrayList<List<OverHeadLine_Mdl>>());
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                }
            });
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
        saleReturnVTypeShortName = "";
        txtHVType.setTag(txtHVType_Mdl.getSharedKeyFieldCode());
        txtHVType.setText(txtHVType_Mdl.getSharedKeyFieldName());
        txtHVType.setTag2nd(txtHVType_Mdl.getVoucherCategory());
        documentTypeCode = DmShared.xNull(txtHVType_Mdl.getDocumentTypeCode());
        documentNatureCode = DmShared.xNull(txtHVType_Mdl.getDocumentNatureCode());
        paymentSource = DmShared.xNull(txtHVType_Mdl.getPaymentSource());
        saleReturnVTypeShortName = DmShared.xNull(txtHVType_Mdl.getShortName());
    }

    private void calculateGrid() {
        for (GrdMedicine_Mdl mdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                double purchaseUnitConversionRate = 1, itemValue = 0, totalDiscount = 0, totalTaxRate = 0, taxableAmt = 0, purchaseSkuQty = 0, freeSkuQty = 0, totalSkuQty = 0, totalTaxAmt = 0;
                int purchaseUnitDecimalPlaces = 4, skuDecimalPlaces = 4;

                if (mdl.getMedicineMdl() != null) {
                    purchaseUnitDecimalPlaces = mdl.getMedicineMdl().getAllowDecimalPlaces();
                    skuDecimalPlaces = mdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                    purchaseUnitConversionRate = DmShared.vNull(mdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();
                }

                purchaseSkuQty = DmShared.vNull(DmClient.vNull((DmShared.vNull(mdl.getQuantity()).doubleValue() * purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();
                freeSkuQty = DmShared.vNull(DmClient.vNull((DmShared.vNull(mdl.getFreeQty()).doubleValue() * purchaseUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();
                totalSkuQty = DmShared.vNull(DmClient.vNull((purchaseSkuQty + freeSkuQty), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();

                mdl.setQuantity(DmClient.vNull(mdl.getQuantity(), DmShared.getNumberFormat(DmShared.iNull(purchaseUnitDecimalPlaces).intValue())));

                mdl.setQuantitySKU(DmClient.vNull(DmShared.vNull(totalSkuQty), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

                mdl.setAmount(DmClient.vNull((DmShared.vNull(purchaseSkuQty).doubleValue() * DmShared.vNull(mdl.getSkuRate()).doubleValue()), "0.00"));
                if (DmShared.vNull(mdl.getMedicineMdl().getDiscountPer()).doubleValue() > 0) {
                    mdl.setDiscount(DmClient.vNull((DmShared.vNull(mdl.getAmount()).doubleValue() * DmShared.vNull(mdl.getMedicineMdl().getDiscountPer()).doubleValue() * 0.01), "0.00"));
                } else {
                    if (DmShared.vNull(mdl.getMedicineMdl().getTempDiscountPer()).doubleValue() > 0) {
                        mdl.setDiscount(DmClient.vNull((DmShared.vNull(mdl.getAmount()).doubleValue() * DmShared.vNull(mdl.getMedicineMdl().getTempDiscountPer()).doubleValue() * 0.01), "0.00"));
                    } else {
                        mdl.setDiscount(DmClient.vNull(DmShared.vNull(mdl.getDiscount()), "0.00"));
                    }
                }
                if (DmShared.vNull(mdl.getDiscountPer2()).doubleValue() > 0) {
                    double tempAmt = 0;
                    tempAmt = DmShared.vNull(mdl.getAmount()).doubleValue() - DmShared.vNull(mdl.getDiscount()).doubleValue();
                    mdl.setDiscount2(DmClient.vNull((tempAmt * DmShared.vNull(mdl.getDiscountPer2()).doubleValue() * 0.01), "0.00"));
                } else {
                    if (DmShared.vNull(mdl.getTempDiscountPer2()).doubleValue() > 0) {
                        mdl.setDiscount2(DmClient.vNull((DmShared.vNull(mdl.getAmount()).doubleValue() * DmShared.vNull(mdl.getTempDiscountPer2()).doubleValue() * 0.01), "0.00"));
                    } else {
                        mdl.setDiscount2(DmClient.vNull(DmShared.vNull(mdl.getDiscount2()), "0.00"));
                    }
                }
                totalDiscount = DmShared.vNull(mdl.getDiscount()).doubleValue() + DmShared.vNull(mdl.getDiscount2()).doubleValue();
                itemValue = DmShared.vNull(mdl.getAmount()).doubleValue() - DmShared.vNull(totalDiscount).doubleValue();
                totalTaxRate = DmShared.vNull(mdl.getCgstPer()).doubleValue() + DmShared.vNull(mdl.getIgstPer()).doubleValue() + (DmShared.vNull(mdl.getSgstPer()).doubleValue() > 0 ? DmShared.vNull(mdl.getSgstPer()).doubleValue() : DmShared.vNull(mdl.getUgstPer()).doubleValue());
                taxableAmt = (itemValue * 100.00) / (100.00 + totalTaxRate);
                //<editor-fold defaultstate="collapsed" desc="For EGST: taxableAmt Re-Calculation">
                String tempItemRate = "0.00";
                double tempQtyForRate = DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQty()).doubleValue();
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
                mdl.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
                mdl.setNetAmt(DmClient.vNull(itemValue, "0.00"));

                totalTaxAmt = DmShared.vNull(mdl.getCgstAmt()).doubleValue() + DmShared.vNull(mdl.getSgstAmt()).doubleValue() + DmShared.vNull(mdl.getUgstAmt()).doubleValue() + DmShared.vNull(mdl.getIgstAmt()).doubleValue();

                //for managing minor 'paisa' differences in amount.
                if (DmShared.vNull(mdl.getNetAmt()).doubleValue() != (DmShared.vNull(mdl.getTaxableAmt()).doubleValue() + totalTaxAmt)) {
                    taxableAmt = DmShared.vNull(mdl.getTaxableAmt()).doubleValue() + (DmShared.vNull(mdl.getNetAmt()).doubleValue() - (DmShared.vNull(mdl.getTaxableAmt()).doubleValue() + totalTaxAmt));
                    mdl.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
                }

                grdMediDetails.commitChanges();
            }
        }
        calculate();
    }

    private void setPatientName_AddressText() {
        String patientName = DmShared.xNull(txtHDebtorsName.getText());
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
        txtHDebtorsName.setText(patientName);
    }

    private void setPlaceOfSupply(String stateCode) {
        if (DmShared.xNull(partyType).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
            placeOfSupply = DmShared.xNull(defaultValue.getStateCode());
        } else {
            if (DmShared.iNull(stateCode).longValue() == 0) {
                placeOfSupply = DmShared.xNull(defaultValue.getStateCode());
            } else {
                placeOfSupply = DmShared.xNull(stateCode);
            }
        }
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
                overheadLineRow.setAmount(grdMdl.getTaxableAmt());
                overheadLine.add(overheadLineRow);

                lstOverheadLine.add(overheadLine);
                //loop end
            }
        }
        taxSummary.reset(lstOverheadLine);
    }

    private void getReturnMedicineRequests(String returnRequestDocID) {
        SrvAsync.invokeRPC().getReturnRequestDetail(returnRequestDocID, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                getSaleDetailWithReturnDocID(result.getData());
            }
        });
    }

    private void getSaleDetailWithReturnDocID(List<KeyFieldExtended> lstKeyField) {
        fillSaleBillDetails(lstKeyField);
        txtHGstReason.setText(returnRequestReasonName);
        txtHGstReason.setTag(returnRequestReasonCode);
    }

    private void isExistEod() {
        SrvAsync.invokeRPC().isEodExist(txtDate.getText(), new AsyncCallback<DmResult<Boolean>>() {
            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onSuccess(DmResult<Boolean> result) {
                if (result.getData()) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Eod has been initialized can't be add / edit/ delet.", (DmTextBox) null).show();
                    return;
                }
            }
        });
    }

    private HashMap iniEgstPopupValue() {
        HashMap paramHashMap = new HashMap();
        paramHashMap.put("EWAY_DOCTYPE", false);
        paramHashMap.put("EWAY_SUBSUPPLYTYPE", false);
        paramHashMap.put("IS_EWAY_TRANSACTION_TYPE", false);
        paramHashMap.put("EINV_SUPPLYTYPE", true);
        paramHashMap.put("EINV_DOCTYPE", true);
        paramHashMap.put("TRANSPORTER", false);
        paramHashMap.put("TRANSPORTER_DETAIL", false);
        paramHashMap.put("GST_DOCUMENT", true);
        paramHashMap.put("IS_INTRA_STATE_AND_JOB_WORK", false);
        paramHashMap.put("IS_BUYER_ADDRESS_EDITABLE", true);
        paramHashMap.put("IS_CONSIGNEE_ADDRESS_EDITABLE", true);
        paramHashMap.put("IS_SHIP_TO_ADDRESS_EDITABLE", true);
        return paramHashMap;
    }

}
