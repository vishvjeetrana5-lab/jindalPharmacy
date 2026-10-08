package aarogya.pharmacy.saleBill.client;

import aarogya.pharmacy.batchNo.shared.BatchNo_Mdl;
import aarogya.pharmacy.partySearch.client.PartySearch;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.requisitionSearch.client.RequisitionSearch;
import aarogya.pharmacy.saleBill.shared.*;
import aarogya.pharmacy.schemeMaster.client.SchemeMaster;
import aarogya.pharmacy.schemeMaster.shared.GrdSchemeDetails_Mdl;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.projectMenu.self.shared.*;
import aarogya.selfToolkit.cardPaymentPopup.client.CardPaymentPopup;
import aarogya.selfToolkit.cardPaymentPopup.shared.*;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.opdSearch.client.OPDSearch;
import aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl;
import aarogya.selfToolkit.organizationPopup.client.OrganizationPopup;
import aarogya.selfToolkit.organizationPopup.shared.Organization_Mdl;
import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.shared.SearchValueRoomWise_Mdl;
import aarogya.selfToolkit.pharmacyDiscountPopup.client.PharmacyDiscountPopup;
import aarogya.selfToolkit.pharmacyDiscountPopup.shared.PharmacyDiscountPopup_Mdl;
import aarogya.selfToolkit.stStatic.client.STClient;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
import aarogya.selfToolkit.uhidDuplicacy.client.UHIDDuplicacy;
import aarogya.selfToolkit.uhidPopup.client.UhidPopup;
import aarogya.selfToolkit.uhidPopup.shared.UhidPopup_Mdl;
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
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.dmBaseStatic.client.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.narrationSearch.client.NarrationSearch;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadITS_Mdl;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.*;

public final class SaleBill extends SaleBill_Dsg implements KeyPressHandler, BlurHandler, AttachEvent.Handler, ClickHandler, DmRootEvents, KeyDownHandler {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String documentTypeCode = "", documentNatureCode = "";
    private final ServiceInvoker<SaleBill_SrvAsync> SrvAsync;
    private CardPaymentPopup_Mdl cardPayment = new CardPaymentPopup_Mdl();
    private final CardPaymentPopup cardPaymentPopup = new CardPaymentPopup();
    private ModifyReason_Mdl modifyReasonMdl;
    private final ModifyReason modifyReason = new ModifyReason();
    private boolean dateEditable = false, isDepartmentEditable = false; //, isAllowCTP = false, isCTRAllowed = false, isCTOAllowed = false
    private String regNo_HIS = "";
    private String uhidNo_HIS = "";
    private String regDocId_HIS = "";
    private String siteCode_HIS = "";

    boolean ctoAllow = false, ctpAllow = false, ctrAllow = false, receiptAllow = false,
            opdAllow = false, ipdAllow = false, dayCareAllow = false, emergencyAllow = false, walkInAllow = false, ledgerAllow = false, uhidAllow = false,
            requisitionWithoutUHID = false, linkParentDatabase = false;

    protected boolean rateEditable = false, isAllowSaleReturn = false, discountEditable = false, isAllowAddBatch = false, schemeEditable = false;
    private final MedicineDetailsEdit<SaleBill_Mdl, GrdMediDetails_PrpAcs> mediDetails = new MedicineDetailsEdit(grdMediDetails);
    protected Default_Mdl defaultValue;
    private final Prescription prescriptionPopup = new Prescription();
    private final UHIDDuplicacy duplicacyPopup = new UHIDDuplicacy();
    private final UhidPopup uhidPopup = new UhidPopup();
    private final OPDSearch opdSearch = new OPDSearch();
    private final PatientSearchRoomWise roomSearchIPD = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.IPD_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private final PatientSearchRoomWise roomSearchEMRG = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.EMERGENCY_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private PatientSearchRoomWise.CallBack roomSearchCallBack;
    private List<GrdMedicine_Mdl> grdPrescDetails_Mdl = new ArrayList<>();
    protected MedicineSaleOn saleOn;
    protected Organization_Mdl organizationMdl;
    private PharmacyDiscountPopup_Mdl pharmacyDiscountPopup_Mdl;
    private final PharmacyDiscountPopup pharmacyDiscountPopup = new PharmacyDiscountPopup(false);
    private final OrganizationPopup orgPopup = new OrganizationPopup();
    private String selectedBatchSQL = "", userEntDtLatestLine = "", isReturnExists = "", lmName = "", lmManualCode = "", partyType = "", roomCode = "", subCode = "", partyManualCode = "", placeOfSupply = "", isEstimateSaleCreated = "", requisitionDocId = "";
    protected String lmCode = "";
    private RequisitionSearch requisitionSearch;
    public String expiryAlertBeforeDays = "", backDaysForEstimateInSaleBill = "";
    double itemTotalQtyInSaleUnit = 0, landedValue = 0;

    private String moduleName = new pmPharmacy().moduleName;
    SchemeMaster schemeMaster;
    final PartySearch partySearch = new PartySearch();
    private PartySearch_Mdl partySearch_Mdl;
    private boolean flagCardPayment = false, isIncludeTaxInCost = false;
    boolean isSchemeExists = false, isPatientDetailsFromType = false;
    List<String> lstItemWithSchemeDetails = new ArrayList<>();
    SaleBill_Mdl freeWithItems_Mdl, previousMdl;
    protected String convertedPaymentSource = null;
    final NarrationSearch narrationSearch = new NarrationSearch();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setCardViewRequired(false);
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
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
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(opdAllowBlog)) {
                opdAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ipdAllowBlog)) {
                ipdAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(dayCareAllowBlog)) {
                dayCareAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(emergencyAllowBlog)) {
                emergencyAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(walkInAllowBlog)) {
                walkInAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ledgerAllowBlog)) {
                ledgerAllow = true;
            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(uhidAllowBlog)) {
                uhidAllow = true;
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
                if (voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
                    if (isEstimateSaleCreated.equalsIgnoreCase("1")) {
                        topControl.btnClick("");
                        new DmAlertMessageBox("Bill is generated, can't update record.", (DmTextBox) null).show();
                        return;
                    }
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
                if (voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_ESTIMATE)) {
                    if (isEstimateSaleCreated.equalsIgnoreCase("1")) {
                        topControl.btnClick("");
                        new DmAlertMessageBox("Bill is generated, can't update record.", (DmTextBox) null).show();
                        return;
                    }
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
                new DmConfirmMessageBox("Do you want to send print to printer?", new DialogHideEvent.DialogHideHandler() {
                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            prnRecord(true);
                        } else {
                            prnRecord(false);
                        }
                    }
                }).show();

            }
        });

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                if (!isEstimate && DmShared.iNull(txtHEstimate.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Estimate No.", txtHEstimate).show();
                } else {
                    if ((voucher_Cat.indexOf(new ProjectVoucherCategory().PHARMACY_BILL.trim()) >= 0 || voucher_Cat.indexOf(new ProjectVoucherCategory().PHARMACY_BILL_CASH.trim()) >= 0) && DmShared.vNull(txtNetAmount.getText()) != 0) {
                        if (DmShared.xNull(txtRoundoff.getTag()).trim().equals("")) {
                            getRoundOffAcCode(true);
                        } else {
                            flagCardPayment = false;
                            showCardPayment();
                        }
                    } else {
                        topControlSave();
                    }
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SaleBill.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        String caption = "", title = "";

        if (isEstimate) {
            caption = "Estimate No.";
        } else {
            caption = "Bill No.";
        }
        hashMapFilter.put("docId", new FieldUDT_Mdl(caption, DmShared.xNull(txtBillNo.getText()), DmShared.xNull(topControl.getDocId())));
        if (voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL) || voucher_Cat.equalsIgnoreCase(new ProjectVoucherCategory().PHARMACY_BILL_CASH)) {
            title = "Tax Invoice";
        } else {
            title = widgetName;
        }
//        new OpenSourceReport(txtHVType.getTag(), "_" + DmShared.xNull(defaultValue.getSiteShortName()), title, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
        new OpenSourceReport(txtHVType.getTag(), "", title, widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {

        narrationSearch.addCallBack(new NarrationSearch.CallBack() {
            @Override
            public void ok(String narration) {
                txtRemark.setText(DmShared.xNull(narration));
                txtRemark.setFocus(true);
            }

            @Override
            public void close() {
                txtRemark.setFocus(true);
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

        prescriptionPopup.addCallBack(new Prescription.CallBack() {

            @Override
            public void ok(List<GrdMedicine_Mdl> mrtemp) {
                grdMediDetails.getGrid().getStore().clear();
                grdMediDetails.getGrid().getStore().addAll(mrtemp);
                grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                calculateGrid();
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }

            @Override
            public void close() {
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

        orgPopup.addCallBack(new OrganizationPopup.CallBack() {
            @Override
            public void ok(Organization_Mdl ctMdl) {

            }

            @Override
            public void close() {
                txtRemark.setFocus(true);
            }
        });

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
                        txtRegNo.setFocus(true);
                    }
                });
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
                txtPatient.setFocus(true);
            }

            @Override
            public void onClear() {
                if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                    clearDiscount();
                    pharmacyDiscountPopup_Mdl = null;
                }
            }
        });

        opdSearch.addCallBack(new OPDSearch.CallBack() {

            @Override
            public void ok(String recIdReg, String docIdReg) {
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
                getUHIDDetail("", "", "", docIdReg, false, null);
            }

            @Override
            public void close() {
                txtRegNo.setFocus(true);
            }
        });
        roomSearchCallBack = new PatientSearchRoomWise.CallBack() {
            @Override
            public void ok(String regDocid, String regRecid, String roomCode) {
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
                getUHIDDetail("", "", "", regDocid, false, null);
            }

            @Override
            public void close() {
                txtRegNo.setFocus(true);
            }
        };

        txtHEstimate.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });
        txtHOperation.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });
        txtHMedicine.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });
        txtHOldOpd.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

            }
        });
        uhidPopup.addCallBack(new UhidPopup.CallBack() {
            @Override
            public void onOk(UhidPopup_Mdl mrTemp) {

                if (DmShared.iNull(mrTemp.getDocIdUHID()).longValue() > 0 || DmShared.iNull(mrTemp.getDocIdRegistration()).longValue() > 0) {
                    getUHIDDetail("", DmShared.iNull(mrTemp.getDocIdUHID()).toString(), "", DmShared.iNull(mrTemp.getDocIdRegistration()).toString(), false, null);
                } else {
                    duplicacyPopup.show(mrTemp);
                }
                txtHType.setTag("Walk-In");
                txtHType.setText("Walk-In");
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }

            @Override
            public void onClose() {
                txtHType.setFocus(true);
            }
        });
        duplicacyPopup.addCallBack(new UHIDDuplicacy.CallBack() {
            @Override
            public void ok(String uhidDocId, String uhidNo) {
                if (DmShared.iNull(uhidDocId).longValue() > 0) {
                    getUHIDDetail(uhidNo, uhidDocId, "", "", false, null);
                }
            }

            @Override
            public void close(UhidPopup_Mdl uhidPopupMdl) {
                txtPatient.setText(DmShared.xNull(uhidPopupMdl.getPatientName()));
                txtMobile.setText(DmShared.xNull(uhidPopupMdl.getMobileNo()));
                txtPatient.setFocus(true);
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
                SaleBill.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
                }

            }
        });
        txtHOldOpd.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHOldOpd_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.iNull(txtHUHID.getTag()).longValue() > 0) {
                    SrvAsync.invokeRPC(blnCallBackWait).getTxtHOldOpd(DmShared.xNull(txtUHIDNo.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHOldOpd_Mdl>>>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHOldOpd).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<TxtHOldOpd_Mdl>> result) {
                            if (result.isStatus()) {
                                txtHOldOpd.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHOldOpd).show();
                            }
                        }
                    });
                } else {
                    new DmAlertMessageBox("Please Select UHID No.", txtHDepartment).show();
                }

            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHOldOpd_Mdl tRow) {
                getPrescription(DmShared.xNull(tRow.getSharedKeyFieldCode()), DmShared.xNull(tRow.getConsultantCode()));
            }
        });

        txtHUHID.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<UhidPopup_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHUHID(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<UhidPopup_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHUHID).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<UhidPopup_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHUHID.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHUHID).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, UhidPopup_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
//                    txtHUHID.setText(DmShared.xNull(t1Row.getRecIdUHID()).trim());
//                    txtHUHID.setTag(DmShared.xNull(t1Row.getDocIdUHID()).trim());
                    if (DmShared.iNull(txtHUHID.getTag()).longValue() > 0) {
                        getUHIDDetail("", DmShared.iNull(txtHUHID.getTag()).toString(), "", "", false, null);
                    }
//                    ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
//                    txtPatient.setText(DmShared.xNull(t1Row.getFirstName()).trim()+" "+DmShared.xNull(t1Row.getMiddleName()).trim()+" "+DmShared.xNull(t1Row.getLastName()).trim());
//                    txtMobile.setText(DmShared.xNull(t1Row.getMobileNo()).trim());
                }
                grdMediDetails.getGrid().getStore().clear();
                grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                txtRemark.setText("");
                txtRemark.setTag("");
                txtRemark.setFocus(true);
            }
        });

        txtHVType.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {
                if (!isEstimate && event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtHEstimate.setFocus(true);
                        }
                    });
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
                    fillV_TypeDetail(t1Row);
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                    txtHVType.setTag2nd(DmShared.xNull(t1Row.getVoucherCategory()).trim());
                    paymentSource = DmShared.xNull(t1Row.getPaymentSource());
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCustomerType(helper, blnToEndOfFile, opdAllow, ipdAllow, dayCareAllow, emergencyAllow, walkInAllow, ledgerAllow, uhidAllow, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                flwSearchRoom.clear();
                flwPatientSearchTd.clear();
                flwPrescription.clear();
                isPatientDetailsFromType = true;
                if (t1Row != null && blnStatus) {
                    setIPD_OPD_Dsiplay();
                    if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("UHID")) {
                        flwRegID_UHID.clear();
                        flwRegID_UHID.add(txtHUHID);
                        txtRegNo.setVisible(false);
                        txtHUHID.setVisible(true);
                        txtHUHID.setEnabled(true);
                        txtHUHID.setFocus(true);
                        linkParentDatabase = true;
                    } //                    else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("IPD")) {
                    ////                        flwRegID_UHID.clear();
                    ////                        flwRegID_UHID.add(txtHUHID);
                    ////                        txtRegNo.setVisible(false);
                    ////                        txtHUHID.setVisible(true);
                    ////                        txtHUHID.setEnabled(true);
                    ////                        txtHUHID.setFocus(true);
                    //                        linkParentDatabase = true;
                    //                    } 
                    //                    else if (!DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("Walk-In")) {
                    //                        flwRegID_UHID.clear();
                    //                        flwRegID_UHID.add(txtRegNo);
                    //                        txtHUHID.setVisible(false);
                    //                        txtRegNo.setEnabled(true);
                    //                          linkParentDatabase = true;
                    //                    }
                    else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("IPD")) {
                        flwRegID_UHID.clear();
                        flwRegID_UHID.add(txtRegNo);
                        txtHUHID.setVisible(false);
                        txtRegNo.setEnabled(false);
                        txtRegNo.setVisible(true);
                        roomSearchIPD.show();
                        linkParentDatabase = true;
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("Walk-In")) {
                        flwRegID_UHID.clear();
                        flwRegID_UHID.add(txtRegNo);
                        txtHUHID.setVisible(false);
                        txtRegNo.setEnabled(false);
                        flwPatientSearchTd.add(flwPatientSearch);
                        txtPatient.setFocus(true);
                    }
                    if (!DmShared.xNull(txtHVType.getTag2nd()).trim().equalsIgnoreCase(pvc.PHARMACY_BILL_CASH)) {
                        manageRequisitionButton();
                    }
                } else {
                    flwSearchRoom.clear();
                }

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHType.getTag()).trim().equalsIgnoreCase("")) {
                            if (!DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Walk-In")) {
                                txtRegNo.setFocus(true);
                            } else if (!DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("UHID")) {
                                txtHUHID.setFocus(true);
                            }
                        } else {
                            txtRegNo.setEnabled(false);
                        }
                    }
                });
            }
        });

        ///
        txtRemark.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHRemark(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtRemark).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtRemark.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtRemark).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        ///
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
        txtHOperation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0) {
                    SrvAsync.invokeRPC(blnCallBackWait).getTxtHOperation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHOperation).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                            if (result.isStatus()) {
                                txtHOperation.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHOperation).show();
                            }
                        }
                    });
                } else {
                    new DmAlertMessageBox("Please Select Department", txtHDepartment).show();
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    getOperationDetail();

                    ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        });

        txtHMedicine.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0) {
                    SrvAsync.invokeRPC(blnCallBackWait).getTxtHMedicine(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();
                        }

                        @Override
                        public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                            if (result.isStatus()) {
                                txtHMedicine.setData(result.getData(), result.getPaging());
                            } else {
                                new DmAlertMessageBox(result.getErrorMessage(), txtHMedicine).show();
                            }
                        }
                    });
                } else {
                    new DmAlertMessageBox("Please Select Department", txtHDepartment).show();
                }
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    getMedicineDetail();

                    ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
                }
            }
        });

        txtHEstimate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHEstimate_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHEstimate(helper, blnToEndOfFile, txtHDepartment.getTag(), txtRegNo.getTag(), txtDate.getText(), lmCode, backDaysForEstimateInSaleBill, new AsyncCallback<DmResultPageing<List<TxtHEstimate_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEstimate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHEstimate_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHEstimate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEstimate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHEstimate_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
                        txtHDepartment.setTag(DmShared.xNull(t1Row.getDepartmentCode()));
                        txtHDepartment.setText(DmShared.xNull(t1Row.getDepartmentName()));
                    }
                    isPatientDetailsFromType = false;
                    getUHIDDetail("", "", "", "", true, null);

                    lmCode = DmShared.iNull(t1Row.getLmCode()).toString();
                    lmName = DmShared.xNull(t1Row.getLmName()).toString();
                    lmManualCode = DmShared.xNull(t1Row.getLmManualCode()).toString();
                    txtMobile.setText(DmShared.xNull(t1Row.getMobile()));
                    txtAddress.setText(DmShared.xNull(t1Row.getAddress()));

                    txtHCity.setTag(DmShared.xNull(t1Row.getCityCode()));
                    txtHCity.setText(DmShared.xNull(t1Row.getCityName()));
                    partyType = DmShared.xNull(t1Row.getPartyType());
                    subCode = t1Row.getSubCode();
                    placeOfSupply = t1Row.getStateCode();
                    ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);

                    regNo_HIS = t1Row.getRegNo_HIS();
                    uhidNo_HIS = t1Row.getUhidNo_HIS();
                    regDocId_HIS = t1Row.getRegDocId_HIS();

                }
            }
        });
        txtHIdType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHIdType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIdType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIdType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIdType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCity).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCity_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCity.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCity).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCity_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    setPlaceOfSupply(t1Row.getStateCode());
                }
            }
        });
    }

    private void fillSchemeQtyAutoFill(GrdSchemeDetails_Mdl detailMdl) {
        GrdMedicine_Mdl mdl;
        double blncQty = DmShared.vNull(detailMdl.getTotalFreeQty()).doubleValue();
        if (Math.abs(blncQty) >= DmShared.vNull(detailMdl.getSchemeFreeQty()).doubleValue()) {
            freeWithItems_Mdl = new SaleBill_Mdl();
            //<editor-fold defaultstate="collapsed" desc="SELECTED BATCH FILL">
            if (Math.abs(blncQty) > 0) {
                mdl = new GrdMedicine_Mdl();

                double purchaseUnitConversionRate = 0, saleUnitConversionRate = 1, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, tempRate = 0, tempSkuRate = 0;
                int saleUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
                if (detailMdl.getMedicineMdl() != null) {
                    setITSGrid(detailMdl.getLstITS(), mdl);
                }

                saleUnitDecimalPlaces = DmShared.iNull(detailMdl.getDecimalPlacesSaleUnit()).intValue();
                skuDecimalPlaces = detailMdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                saleUnitConversionRate = DmShared.vNull(detailMdl.getMedicineMdl().getConversionRate()).doubleValue();
                purchaseUnitConversionRate = DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();

                mdl.setSaleAcCode(DmShared.xNull(detailMdl.getSaleAcCode()));
                mdl.setSaleUnitConversionRate(detailMdl.getMedicineMdl().getConversionRate());
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
                listNode.setSaleUnitCode(DmShared.xNull(detailMdl.getMedicineMdl().getSaleUnitCode()));
                listNode.setSaleUnitName(DmShared.xNull(detailMdl.getMedicineMdl().getSaleUnitName()));
                listNode.setConversionRate(DmShared.vNull(detailMdl.getMedicineMdl().getConversionRate()).toString());
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
                mdl.setUnit(detailMdl.getMedicineMdl().getSaleUnitName());
                //<editor-fold defaultstate="collapsed" desc="BATCH DETAILS">
                mdl.setBatchNo(detailMdl.getBatchNo_Mdl().getSharedKeyFieldName());
                mdl.setIbCode(detailMdl.getBatchNo_Mdl().getSharedKeyFieldCode());
                mdl.setBestBefore(detailMdl.getMedicineMdl().isBestBefore());
                mdl.setExpDate(detailMdl.getBatchNo_Mdl().getExpiryDate());
                mdl.setMfgDate(detailMdl.getBatchNo_Mdl().getManufacturingDate());
                mdl.setMrp(detailMdl.getBatchNo_Mdl().getMrp());
                mdl.setSkuMrp(detailMdl.getBatchNo_Mdl().getSkuMrp());
                //<editor-fold defaultstate="collapsed" desc="RATE">
                if (saleOn == MedicineSaleOn.MRP) {
                    tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getMrp()).doubleValue();
                    tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuMrp()).doubleValue();
                } else if (saleOn == MedicineSaleOn.COST_PRICE) {
                    tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuMrp()).doubleValue() * DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();
                    tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuLandedRate()).doubleValue();
                } else {
                    tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSaleRate()).doubleValue();
                    tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuSaleRate()).doubleValue();
                }
                mdl.setRate(DmClient.vNull(tempRate, "0.00"));
                mdl.setSkuRate(DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7)));
                //</editor-fold>
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="QTY IN SKU">
                if (blncQty > DmShared.vNull(detailMdl.getBatchNo_Mdl().getBalanceQtyInSaleUnit()).doubleValue()) {
                    if (DmShared.xNull(detailMdl.getAllowNegativeStockFreeItem()).equalsIgnoreCase("1")) {
                        mdl.setFreeQuantityInSku(DmClient.vNull(blncQty, DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                    } else {
                        mdl.setFreeQuantityInSku(DmClient.vNull(detailMdl.getBatchNo_Mdl().getBalanceQty(), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                    }
                } else {
                    mdl.setFreeQuantityInSku(DmClient.vNull((blncQty * saleUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                }

                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="QTY IN SALE UNIT">
                if (saleUnitDecimalPlaces > 0) {
                    mdl.setFreeQuantityInSaleUnit(DmClient.vNull((DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(saleUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
                } else {
                    mdl.setFreeQuantityInSaleUnit(DmClient.vNull(Math.floor(DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(saleUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
                }
                mdl.setBatchQuantityInSaleUnit(DmClient.vNull(DmShared.vNull(detailMdl.getBatchNo_Mdl().getBalanceQtyInSaleUnit()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
//</editor-fold>
                blncQty -= DmShared.vNull(mdl.getFreeQuantityInSaleUnit());
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

                        double purchaseUnitConversionRate = 0, saleUnitConversionRate = 1, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, tempRate = 0, tempSkuRate = 0;
                        int saleUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
                        if (mdl.getMedicineMdl() != null) {
                            setITSGrid(detailMdl.getLstITS(), mdl);
                        }

                        saleUnitDecimalPlaces = DmShared.iNull(detailMdl.getDecimalPlacesSaleUnit()).intValue();
                        skuDecimalPlaces = detailMdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                        saleUnitConversionRate = DmShared.vNull(detailMdl.getMedicineMdl().getConversionRate()).doubleValue();
                        purchaseUnitConversionRate = DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();

                        mdl.setSaleAcCode(DmShared.xNull(detailMdl.getSaleAcCode()));
                        mdl.setSaleUnitConversionRate(detailMdl.getMedicineMdl().getConversionRate());
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
                        listNode.setSaleUnitCode(DmShared.xNull(detailMdl.getMedicineMdl().getSaleUnitCode()));
                        listNode.setSaleUnitName(DmShared.xNull(detailMdl.getMedicineMdl().getSaleUnitName()));
                        listNode.setConversionRate(DmShared.vNull(detailMdl.getMedicineMdl().getConversionRate()).toString());
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
                        mdl.setUnit(detailMdl.getMedicineMdl().getSaleUnitName());
                        //<editor-fold defaultstate="collapsed" desc="BATCH DETAILS">
                        mdl.setBatchNo(batchMdl.getSharedKeyFieldName());
                        mdl.setIbCode(batchMdl.getSharedKeyFieldCode());
                        mdl.setBestBefore(detailMdl.getMedicineMdl().isBestBefore());
                        mdl.setExpDate(batchMdl.getExpiryDate());
                        mdl.setMfgDate(batchMdl.getManufacturingDate());
                        mdl.setMrp(batchMdl.getMrp());
                        mdl.setSkuMrp(batchMdl.getSkuMrp());
                        //<editor-fold defaultstate="collapsed" desc="RATE">
                        if (saleOn == MedicineSaleOn.MRP) {
                            tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getMrp()).doubleValue();
                            tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuMrp()).doubleValue();
                        } else if (saleOn == MedicineSaleOn.COST_PRICE) {
                            tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuMrp()).doubleValue() * DmShared.vNull(detailMdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();
                            tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuLandedRate()).doubleValue();
                        } else {
                            tempRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSaleRate()).doubleValue();
                            tempSkuRate = DmShared.vNull(detailMdl.getBatchNo_Mdl().getSkuSaleRate()).doubleValue();
                        }
                        mdl.setRate(DmClient.vNull(tempRate, "0.00"));
                        mdl.setSkuRate(DmClient.vNull(tempSkuRate, DmShared.getNumberFormat(7)));
                        //</editor-fold>
                        //</editor-fold>
                        //<editor-fold defaultstate="collapsed" desc="QTY IN SKU">
//                if (blncQty > DmShared.vNull(detailMdl.getBatchNo_Mdl().getBalanceQty()).doubleValue()) {
                        if (blncQty > DmShared.vNull(batchMdl.getBalanceQtyInSaleUnit()).doubleValue()) {
//                        mdl.setFreeQuantityInSku(DmClient.vNull(detailMdl.getBatchNo_Mdl().getBalanceQty(), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                            mdl.setFreeQuantityInSku(DmClient.vNull(batchMdl.getBalanceQty(), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                        } else {
                            mdl.setFreeQuantityInSku(DmClient.vNull((blncQty * saleUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
                        }
                        //</editor-fold>
                        mdl.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(mdl.getQuantitySKU()).doubleValue() + DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue()), "0.00"));
                        mdl.setBatchQuantityInSaleUnit(DmClient.vNull(DmShared.vNull(batchMdl.getBalanceQtyInSaleUnit()).doubleValue(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
                        //<editor-fold defaultstate="collapsed" desc="QTY IN SALE UNIT">
                        if (saleUnitDecimalPlaces > 0) {
                            mdl.setFreeQuantityInSaleUnit(DmClient.vNull((DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(saleUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
                        } else {
                            mdl.setFreeQuantityInSaleUnit(DmClient.vNull(Math.floor(DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue() / DmShared.vNull(saleUnitConversionRate).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
                        }
//</editor-fold>
                        blncQty -= DmShared.vNull(mdl.getFreeQuantityInSaleUnit());
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

    private void fillPartyDetails() {
        resetPatientDetails();
        if (txtHType.isEnabled()) {
            txtHType.setEnabled(false);
        }
        if (txtRegNo.isEnabled()) {
            txtRegNo.setEnabled(false);
        }
        if (txtPatient.isEnabled()) {
            txtPatient.setEnabled(false);
        }
        enableIdType(false);
        if (partySearch_Mdl != null) {
            txtPatient.setText(partySearch_Mdl.getSharedKeyFieldName());
            lmCode = DmShared.iNull(partySearch_Mdl.getLmCode()).toString();
            lmName = DmShared.xNull(partySearch_Mdl.getLmName());
            lmManualCode = DmShared.xNull(partySearch_Mdl.getLmManualCode());
            txtHIdType.setTag(DmShared.iNull(partySearch_Mdl.getLmIdTypeCode()).toString());
            txtHIdType.setText(DmShared.xNull(partySearch_Mdl.getLmIdTypeName()));
            txtIdNo.setText(DmShared.xNull(partySearch_Mdl.getLmIdNo()));
            txtMobile.setText(DmShared.xNull(partySearch_Mdl.getMobile()));
            txtAddress.setText(DmShared.xNull(partySearch_Mdl.getAddress()));
            txtHCity.setTag(DmShared.xNull(partySearch_Mdl.getCityCode()));
            txtHCity.setText(DmShared.xNull(partySearch_Mdl.getCityName()));
            partyType = DmShared.xNull(partySearch_Mdl.getPartTypeCode());
            txtRegNo.setText(DmShared.xNull(partySearch_Mdl.getManualCode()));
            subCode = partySearch_Mdl.getSharedKeyFieldCode();
            partyManualCode = partySearch_Mdl.getManualCode();
            setPlaceOfSupply(partySearch_Mdl.getStateCode());
            btnLedgerSearch.removeFromParent();
        }

        if (!DmShared.xNull(lmCode).equalsIgnoreCase("")) {
            getPharmacyBalance();
        }

        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

    }

    private void getPharmacyBalance() {
        SrvAsync.invokeRPC().getPharmacyBalance(lmCode, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtRemark).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    lblPharmacyBalance.setText("Pharmacy Balance : " + result.getData());
                }
            }
        });
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
//    private void getRegistrationBalance(String regDocId) {
//        SrvAsync.invokeRPC().getRegistrationBalance(regDocId, new AsyncCallback<DmResult<String>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtRemark).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<String> result) {
//                if (result.isStatus()) {
//                    lblRegistrationBalance.setText("Registration Balance : " + result.getData());
//                }
//            }
//        });
//    }
    //</editor-fold>
    private void enableIdType(boolean flag) {
        if (flag) {
            if (!txtAddress.isEnabled()) {
                txtAddress.setEnabled(true);
            }
            if (!txtHCity.isEnabled()) {
                txtHCity.setEnabled(true);
            }
            if (!txtHIdType.isEnabled()) {
                txtHIdType.setEnabled(true);
            }
            if (!txtIdNo.isEnabled()) {
                txtIdNo.setEnabled(true);
            }
        } else {
//            if (!DmShared.xNull(txtHType.getText()).equalsIgnoreCase("Walk-In")) {
//                if (!DmShared.xNull(txtAddress.getText()).equalsIgnoreCase("")) {
//                    if (txtAddress.isEnabled()) {
//                        txtAddress.setEnabled(false);
//                    }
//                }
//                if (txtHCity.isEnabled()) {
//                    txtHCity.setEnabled(false);
//                }
//            }
            if (txtMobile.isEnabled()) {
                txtMobile.setEnabled(false);
            }
//            if (txtHIdType.isEnabled()) {
//                txtHIdType.setEnabled(false);
//            }
//            if (txtIdNo.isEnabled()) {
//                txtIdNo.setEnabled(false);
//            }
        }
        txtMobile.setEnabled(true);
    }

    private void resetPatientDetails() {
        lmCode = "";
        lmName = "";
        lmManualCode = "";
        txtPatient.setText("");
        txtHIdType.setTag("");
        txtHIdType.setText("");
        txtIdNo.setText("");
        txtMobile.setText("");
        txtAddress.setText("");
        txtHCity.setTag("");
        txtHCity.setText("");
        partyType = defaultValue.getPartyType();
        subCode = "";
        partyManualCode = "";
        placeOfSupply = DmShared.xNull(defaultValue.getStateCode());
        txtRegNo.setText("");
        txtRegNo.setTag("");
        txtRegNo.setTag2nd("");
        flagCardPayment = false;
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
                    cnt++;
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
                }
                isDiscount = true;
                if (totalAmt != 0) {
                    discountRate = DmShared.vNull((DmClient.vNull(DmShared.vNull(pharmacyDiscountPopup_Mdl.getDiscount()).doubleValue() / totalAmt, "0.0000000"))).doubleValue();
                }
            }

            for (int i = 0; i < grdMediDetails.getGrid().getStore().getAll().size(); i++) {
                if (grdMediDetails.getGrid().getStore().get(i).getSharedKeyFieldCode() != null) {
//                    if ((organizationMdl != null ? (organizationMdl.isPharmacyDiscount() && discountEditable && grdMediDetails.getGrid().getStore().get(i).getMedicineMdl().isDiscountAllow()) : (discountEditable && grdMediDetails.getGrid().getStore().get(i).getMedicineMdl().isDiscountAllow()))) {
                    if ((discountEditable && grdMediDetails.getGrid().getStore().get(i).getMedicineMdl().isDiscountAllow())) {
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
                                grdMediDetails.getGrid().getStore().get(i).setDiscountReason(DmShared.xNull(pharmacyDiscountPopup_Mdl.getDiscountReason()));
                                grdMediDetails.getGrid().getStore().get(i).setNetAmt(DmClient.vNull(netAmt, "0.00"));
                                flag = false;
                            }

                        }
                    }
                }
            }
        }

        grdMediDetails.commitChanges();
        calculateGrid();
        calculate();
    }

    private void clearDiscount() {
        for (int i = 0; i < grdMediDetails.getGrid().getStore().getAll().size(); i++) {
            GrdMedicine_Mdl grdMedicine_Mdl = grdMediDetails.getGrid().getStore().get(i);
            if (DmShared.iNull(grdMedicine_Mdl.getSharedKeyFieldCode()).longValue() != 0) {
                if ((organizationMdl != null ? (organizationMdl.isPharmacyDiscount() && discountEditable && grdMediDetails.getGrid().getStore().get(i).getMedicineMdl().isDiscountAllow()) : (discountEditable && grdMediDetails.getGrid().getStore().get(i).getMedicineMdl().isDiscountAllow()))) {
                    grdMediDetails.getGrid().getStore().get(i).setDiscount2("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountPer2("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountGivenByTag("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountGivenByText("");
                    grdMediDetails.getGrid().getStore().get(i).setDiscountReason("");
                    grdMediDetails.getGrid().getStore().get(i).setAmount(DmClient.vNull(grdMediDetails.getGrid().getStore().get(i).getAmount(), "0.00"));

                }
            }
        }
        grdMediDetails.commitChanges();
        calculateGrid();
        calculate();
    }

    private void fillRequisitionItemDetails(List<KeyFieldExtended> listKF) {

        SrvAsync.invokeRPC().getRequisitionMedicineDetails(listKF, DmShared.dNull(txtDate.getText()), DmShared.iNull(txtHDepartment.getTag()).toString(), organizationMdl != null ? DmShared.xNull(organizationMdl.getPharmacyDiscountPer()) : "0.00", saleOn, organizationMdl != null ? organizationMdl.isPharmacyDiscount() : true, requisitionWithoutUHID, new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {
                if (result.isStatus()) {
                    if (result.isStatus()) {
                        getUHIDDetail("", "", "", result.getData().get(0).getPatientRegDocId(), false, null);
                        grdMediDetails.getGrid().getStore().clear();
                        grdMediDetails.getGrid().getStore().addAll(result.getData());
                        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                        calculateGrid();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    private void setIPD_OPD_Dsiplay() {

        flwPrescription.clear();

        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD) || DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE) || DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.UHID)) {
            flwPrescription.add(btnPrescription);
        }

        ManageBtnSearchRoom();
    }

    private void getOperationDetail() {
        if (DmShared.iNull(txtHOperation.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Operation is mandatory.", txtHOperation).show();
            return;
        }
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please select Department.", txtHDepartment).show();
            return;
        }
        SrvAsync.invokeRPC(true).getOperationMedicine(DmShared.iNull(txtHOperation.getTag()).toString(), DmShared.dNull(txtDate.getText()), DmShared.iNull(txtHDepartment.getTag()).toString(), saleOn, organizationMdl != null ? DmShared.xNull(organizationMdl.getPharmacyDiscountPer()) : "0.00", organizationMdl != null ? organizationMdl.isPharmacyDiscount() : true, new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHOperation).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {
                if (result.isStatus()) {
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().addAll(result.getData());
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                    calculateGrid();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHOperation).show();
                }
            }
        });
    }

    private void getMedicineDetail() {
        if (DmShared.iNull(txtHMedicine.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Medicine is mandatory.", txtHMedicine).show();
            return;
        }
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please select Department.", txtHDepartment).show();
            return;
        }
        SrvAsync.invokeRPC(true).getMedicine(DmShared.iNull(txtHMedicine.getTag()).toString(), DmShared.dNull(txtDate.getText()), DmShared.iNull(txtHDepartment.getTag()).toString(), saleOn, organizationMdl != null ? DmShared.xNull(organizationMdl.getPharmacyDiscountPer()) : "0.00", organizationMdl != null ? organizationMdl.isPharmacyDiscount() : true, new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMedicine).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {
                if (result.isStatus()) {
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().addAll(result.getData());
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                    calculateGrid();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHMedicine).show();
                }
            }
        });
    }

    private void getEstimateData() {
        if (DmShared.iNull(txtHEstimate.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Estimate No. is mandatory.", txtHEstimate).show();
            return;
        }
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please Fill Department", txtHDepartment).show();
            return;
        }
        if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Date is mandatory", txtDate).show();
            return;
        }
        SrvAsync.invokeRPC(true).getEstimateDetail(DmShared.iNull(txtHEstimate.getTag()).toString(), DmShared.dNull(txtDate.getText()), DmShared.iNull(txtHDepartment.getTag()).toString(), saleOn, new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHEstimate).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {
                if (result.isStatus()) {
                    freeWithItems_Mdl = new SaleBill_Mdl();
                    grdMediDetails.getGrid().getStore().clear();
                    grdMediDetails.getGrid().getStore().addAll(result.getData());
                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
                    for (GrdMedicine_Mdl mdl : result.getData()) {
                        if (DmShared.iNull(mdl.getFreeWithItemCode()).longValue() > 0) {
                            freeWithItems_Mdl.getLstFreeWithItemCodes().add(mdl.getFreeWithItemCode());
                        }
                    }
                    calculateGrid();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHEstimate).show();
                }
            }
        });
    }

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
    }

    private void showCardPayment() {
        boolean isTPA = false;
        if (DmShared.xNull(txtPatient.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Patient Name.", txtPatient).show();
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
        cardPaymentMdl.setPatientName(DmShared.xNull(txtPatient.getText()));
        cardPaymentMdl.setUHIDNo(DmShared.xNull(txtUHIDNo.getText()));
        cardPaymentMdl.setUHIDDocid(DmShared.xNull(txtUHIDNo.getTag()));
        cardPaymentMdl.setRegNo(DmShared.xNull(txtRegNo.getText()));
        cardPaymentMdl.setRegNoDocid(DmShared.xNull(txtRegNo.getTag()));
        cardPaymentMdl.setReceiptNo(DmShared.xNull(txtReciptNo.getText()));
        cardPaymentMdl.setReceiptTypeCode(DmShared.xNull(txtReciptType.getTag()));
        cardPaymentMdl.setReceiptTypeName(DmShared.xNull(txtReciptType.getText()));
        cardPaymentMdl.setReceiptTypePM(DmShared.xNull(txtReciptType.getTag2nd()));
        if (convertedPaymentSource != null && !DmShared.xNull(convertedPaymentSource).trim().equalsIgnoreCase("")) {
            cardPaymentMdl.setRegPaymentSource(STEnum.PaymentSource.getValueOf(DmShared.iNull(convertedPaymentSource).intValue()));
        }
        if (organizationMdl != null) {
            cardPaymentMdl.setCTOCardType(organizationMdl.getSharedKeyFieldName());
            cardPaymentMdl.setCTOCardNo(organizationMdl.getPolicyNo());
            cardPaymentMdl.setCTOCardHolderName(organizationMdl.getHolderName());
            cardPaymentMdl.setCTORelation(organizationMdl.getRelationshipName());
            cardPaymentMdl.setCTORankNo(organizationMdl.getRankNo());
            isTPA = organizationMdl.isTpa();
        }

        if (((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))
                || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE)))) {
            if (!DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_CASH)) {
                cardPaymentPopup.setCTOAllowed(false);
                cardPaymentPopup.setCTRAllowed(ctrAllow);
                cardPaymentPopup.setReceiptAllowed(false);
            } else {
//                cardPaymentPopup.setCTOAllowed(false);
//                cardPaymentPopup.setCTRAllowed(false);
//                cardPaymentPopup.setReceiptAllowed(true);
                cardPaymentPopup.setCTOAllowed(false);
                cardPaymentPopup.setCTRAllowed(ctrAllow);
                cardPaymentPopup.setReceiptAllowed(false);
            }

        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("LEDGER")) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("WALK-IN"))) {
            cardPaymentPopup.setCTOAllowed(false);
            cardPaymentPopup.setCTRAllowed(false);
        } else {
            if (!DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_CASH)) {
                cardPaymentPopup.setCTOAllowed(ctoAllow);
                cardPaymentPopup.setCTRAllowed(false);
                cardPaymentPopup.setReceiptAllowed(false);
            } else {
                cardPaymentPopup.setCTOAllowed(false);
                cardPaymentPopup.setCTRAllowed(false);
                cardPaymentPopup.setReceiptAllowed(true);
            }
        }
        if (!DmShared.xNull(txtHVType.getTag2nd()).equalsIgnoreCase(pvc.PHARMACY_BILL_CASH)) {
            cardPaymentPopup.setCTPAllowed(ctpAllow);
            cardPaymentPopup.setReceiptAllowed(false);
        } else {
            cardPaymentPopup.setCTPAllowed(false);
            cardPaymentPopup.setReceiptAllowed(true);
        }

        cardPaymentPopup.setCTPAllowed(false);

        //===============================Re-Set PaymentSource===================
        if (DmShared.iNull(paymentSource).intValue() == STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue() && (DmShared.xNull(txtHType.getText()).equalsIgnoreCase(pvc.IPD))) {
            cardPaymentPopup.setCTRAllowed(ctrAllow);
            cardPaymentPopup.setReceiptAllowed(false);
        } else if (DmShared.iNull(paymentSource).intValue() == STEnum.PaymentSource.PAYMENT.getEnumValue()) {
            cardPaymentPopup.setCTRAllowed(false);
            cardPaymentPopup.setReceiptAllowed(receiptAllow);
        }
        //===============================Re-Set PaymentSource End===================

        if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(""))) {
            cardPaymentPopup.setDefaultPaymentSource(isTPA ? STEnum.PaymentSource.CREDIT_TO_ORGANIZATION : STEnum.PaymentSource.PAYMENT);
        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
            cardPaymentPopup.setDefaultPaymentSource(isTPA ? STEnum.PaymentSource.CREDIT_TO_REGISTRATION : STEnum.PaymentSource.PAYMENT);
        } else {
            cardPaymentPopup.setDefaultPaymentSource(STEnum.PaymentSource.PAYMENT);
        }

        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            cardPaymentPopup.show(cardPaymentMdl, DmShared.xNull(topControl.getMode()));
        } else {
            try {
                calculate();
                SaleBill_Mdl mrTemp = topControlSaveInitialize();
                SaleBill_Vld.chkSave(mrTemp, voucher_Cat);

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
        itemTotalQtyInSaleUnit = 0;
        for (GrdMedicine_Mdl service : grdMediDetails.getGrid().getStore().getAll()) {
            if (DmShared.iNull(service.getSharedKeyFieldCode()).longValue() == DmShared.iNull(itemCode).longValue()) {
//                itemTotalQtyInSaleUnit += DmShared.vNull(service.getTotalQuantitySKU()).doubleValue();
                itemTotalQtyInSaleUnit += (DmShared.vNull(service.getQuantity()).doubleValue() + DmShared.vNull(service.getFreeQuantityInSaleUnit()).doubleValue());
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
        } else {
            btnClick((Button) event.getSource());
        }
    }

    private void getPrescription(String opdNo, String consultantCode) {
        if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Date is mandatory", txtDate).show();
            txtHOldOpd.setText("");
            txtHOldOpd.setTag("");
            return;
        }
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please fill department", txtHDepartment).show();
            txtHOldOpd.setText("");
            txtHOldOpd.setTag("");
            return;
        }
        oldOpdPrescMed(opdNo, consultantCode);
    }

    private void openPrescription() {
        prescriptionPopup.show(grdPrescDetails_Mdl, this);
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
        if (button == btnPatientSearch) {
            uhidPopup.show(defaultValue.getLstLayout(), aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl.FilterType.WITHOUT_EXPIRED);
        } else if (button == btnRequisitionSearch) {
            openRequisition();
        } else if (button == btnSearchRoom) {
            if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) {
                roomSearchIPD.show();
            } else if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) {
                roomSearchEMRG.show();
            }
        } else if (button == btnOPDSearch) {
            if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) {
                opdSearch.show(new pmOPD().moduleName, SearchValue_Mdl.OPDFilterType.SERVICE_DAYS,
                        SearchValue_Mdl.OPDFilterType.OPD_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            } else if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE)) {
                opdSearch.show(new pmDayCare().moduleName, SearchValue_Mdl.OPDFilterType.CANCEL_DAYS,
                        SearchValue_Mdl.OPDFilterType.DAYCARE_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            }
        } else if (button == btnLedgerSearch) {
            partySearch.show(DmShared.dNull(txtDate.getText()), defaultValue.getVoucherTypeMdl().getSharedKeyFieldCode());
        } else if (button == btnPayment) {
            if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL.caption)) {
                flagCardPayment = false;
                showCardPayment();
            } else {
                new DmAlertMessageBox("Not Allow!", txtHVType).show();
            }
        } else if (button == btnPrescription) {
            if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Date is mandatory", txtDate).show();
                return;
            }
            if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Please fill department", txtHDepartment).show();
                return;
            }
            prescriptionPopup.show(grdPrescDetails_Mdl, this);
        } else if (button == btnOrganisation) {
            fill_ShowOrgPopUp(organizationMdl, true);
        } else if (button == btnremarks) {
            if (!DmShared.xNull(topControl.getMode()).equalsIgnoreCase("Browse")) {
                showNarrationSearch();
            }
        }
    }

    private void showNarrationSearch() {
        narrationSearch.show();
    }

    private void openRequisition() {
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please Select Department.", txtHDepartment).show();
            return;
        }
        if (DmShared.dNull(txtDate.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Date.", txtDate).show();
            return;
        }
//        ****Change for Jindal temp
        if (!requisitionWithoutUHID) {
            if (DmShared.xNull(txtPatient.getText()).equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please Fill Patient Name", txtRegNo).show();
                return;
            }

            if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(new ProjectVoucherCategory().IPD)) {
                if (DmShared.iNull(txtRegNo.getTag()).longValue() == 0) {
                    new DmAlertMessageBox("Reg. No. is mandatory", txtPatient).show();
                    return;
                }
            }
        }

        if (requisitionWithoutUHID) {
            requisitionSearch.show("", DmShared.dNull(txtDate.getText()), voucher_Cat, topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), DmShared.xNull(txtHDepartment.getTag()), DmShared.iNull(txtRegNo.getTag()).toString(), requisitionDocId, true);
        } else {
            requisitionSearch.show("", DmShared.dNull(txtDate.getText()), voucher_Cat, topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), DmShared.xNull(txtHDepartment.getTag()), DmShared.iNull(txtRegNo.getTag()).toString(), requisitionDocId);
        };

    }

    private void gridMediDetailsEdit(Integer currentRowIndex) {
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please Select Department.", txtHDepartment).show();
            return;
        }
        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Select Patient Type.", txtHType).show();
            return;
        }
        if (DmShared.xNull(txtPatient.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Patient Name.", txtPatient).show();
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
            mediDetails.show(currentRowIndex, false, false, false, this);
        } else {
            mediDetails.show(currentRowIndex, true, true, true, this);
        }
    }

    public SaleBill(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super(paramHashMap);
        SrvAsync = new ServiceInvoker<>(GWT.create(SaleBill_Srv.class), SaleBill.class.getName());

        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        schemeMaster = new SchemeMaster(schemeEditable);
        findEvents();
        gridEvents();

        textHelpEvents();
        handlerEvents();
        iniDefaultValues(swi);

        grdMediDetails.setDefaultColumnIndex(8);    //TBC
    }

    private void handlerEvents() {
        btnPatientSearch.addClickHandler(this);
        btnPrescription.addClickHandler(this);
        btnOPDSearch.addClickHandler(this);
        txtDate.addBlurHandler(this);
        txtUHIDNo.addBlurHandler(this);
        txtPatient.addBlurHandler(this);
        txtUHIDNo.addKeyPressHandler(this);
        flwForm.addAttachHandler(this);
        labelBillDiscount.addClickHandler(this);
        btnPayment.addClickHandler(this);
        btnSearchRoom.addClickHandler(this);
        btnLedgerSearch.addClickHandler(this);
        btnOrganisation.addClickHandler(this);
        btnRequisitionSearch.addClickHandler(this);
        txtRegNo.addKeyPressHandler(this);
        roomSearchEMRG.addCallBack(roomSearchCallBack);
        roomSearchIPD.addCallBack(roomSearchCallBack);
        btnremarks.addClickHandler(this);
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
                    backDaysForEstimateInSaleBill = defaultValue.getBackDateForEstimateHelpInSaleBill();
                    saleOn = defaultValue.getMedicineSaleOn() == null ? MedicineSaleOn.SALE_RATE : defaultValue.getMedicineSaleOn();
                    if (defaultValue.getRequisitionWithoutUHID().equalsIgnoreCase("1")) {
                        requisitionWithoutUHID = true;
                    } else {
                        requisitionWithoutUHID = false;
                    }
                    requisitionSearch = new RequisitionSearch(requisitionWithoutUHID);
                    requisitionSearch.addCallBack(new RequisitionSearch.CallBack() {
                        @Override
                        public void ok(List<KeyFieldExtended> listKF) {
                            fillRequisitionItemDetails(listKF);
                        }

                        @Override
                        public void close() {
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                @Override
                                public void execute() {
                                    ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
                                }
                            });

                            txtHVType.setFocus(true);
                        }
                    });
                    flwSearchRoom.clear();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    manageDisplay(false);
                    swi.setWidget(SaleBill.this);
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

    private SaleBill_Mdl topControlSaveInitialize() {
        SaleBill_Mdl mrTemp = new SaleBill_Mdl();

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
        mrTemp.setTypeCode(DmShared.xNull(txtHType.getTag()));
        mrTemp.setVoucherTypeCode(DmShared.xNull(txtHVType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtHVType.getText()));
        mrTemp.setVoucherDate(DmShared.dNull(txtDate.getText()));

        if (regDocId_HIS.equalsIgnoreCase("")) {
            mrTemp.setRegDocid(DmShared.xNull(txtRegNo.getTag()));
        } else {
            mrTemp.setRegDocid(DmShared.xNull(regDocId_HIS));
        }

        mrTemp.setRegRecID(DmShared.xNull(txtRegNo.getText()));
        mrTemp.setRegSiteCode(DmShared.xNull(txtPatient.getTag2nd()));
        mrTemp.setRoomCode(DmShared.xNull(roomCode));
        mrTemp.setUhidDocid(DmShared.xNull(txtUHIDNo.getTag()));
        mrTemp.setUhidRecId(DmShared.xNull(txtUHIDNo.getText()));
        mrTemp.setIdNo(DmShared.xNull(txtIdNo.getText()));
        mrTemp.setIdTypeCode(DmShared.xNull(txtHIdType.getTag()));
        mrTemp.setIdTypeName(DmShared.xNull(txtHIdType.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setOperationCode(DmShared.xNull(txtHOperation.getTag()));
        mrTemp.setOperationName(DmShared.xNull(txtHOperation.getText()));
        mrTemp.setMedicineCode(DmShared.xNull(txtHMedicine.getTag()));
        mrTemp.setMedicineName(DmShared.xNull(txtHMedicine.getText()));
        mrTemp.setDocIdEstimate(DmShared.xNull(txtHEstimate.getTag()));
        mrTemp.setRecIdEstimate(DmShared.xNull(txtHEstimate.getText()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setConsultantCode(DmShared.xNull(txtHConsultant.getTag()));
        mrTemp.setPatientName(DmShared.xNull(txtPatient.getText()));
        mrTemp.setReceiptTypeCode(DmShared.xNull(txtReciptType.getTag()));
        mrTemp.setReceiptTypeName(DmShared.xNull(txtReciptType.getText()));
        mrTemp.setReceiptTypeCateory(DmShared.xNull(txtReciptType.getTag2nd()));
        mrTemp.setDefaultPharmacyServiceCode(DmShared.xNull(defaultValue.getDefaultPharmacyServiceCode()));

//        if (DmShared.iNull(subCode).longValue() == 0) {
//            partyType = defaultValue.getPartyType();
//        }
        mrTemp.setPartyType(DmShared.xNull(partyType));
        mrTemp.setSubCode(DmShared.xNull(subCode));
        mrTemp.setLmCode(DmShared.xNull(lmCode));
        mrTemp.setLmName(DmShared.xNull(lmName));
        mrTemp.setLmManualCode(DmShared.xNull(lmManualCode));
        mrTemp.setCityCode(DmShared.xNull(txtHCity.getTag()));
        mrTemp.setCityName(DmShared.xNull(txtHCity.getText()));
        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setPartyManualCode(DmShared.xNull(partyManualCode));
        mrTemp.setPlaceOfSupply(DmShared.xNull(placeOfSupply));
        mrTemp.setSiteStateCode(DmShared.xNull(defaultValue.getStateCode()));

        mrTemp.getRecIdRecipt().setRecIdValue(DmShared.xNull(txtReciptNo.getText()));

        mrTemp.setUserEntDtLatestLine(DmShared.xNull(userEntDtLatestLine));

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
        if (defaultValue.isUnionTerritory()) {
            mrTemp.setUgstAmt(DmClient.vNull(txtSgstUgstAmt.getText(), "0.00"));
        } else {
            mrTemp.setSgstAmt(DmClient.vNull(txtSgstUgstAmt.getText(), "0.00"));
        }
        mrTemp.getGrdMediDetails().addAll(grdMediDetails.getGrid().getStore().getAll());
        if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL.caption)) {
            mrTemp.getCardPayment().getGrdCardPaymentList().addAll(new ArrayList<GrdCardPayment_Mdl>());
            mrTemp.setCardPayment(cardPayment.clone());
        }
        mrTemp.setFlagCardPayment(flagCardPayment ? "1" : "0");

        if (!DmShared.xNull(siteCode_HIS).equalsIgnoreCase("")) {
            mrTemp.setSiteCode_HIS(siteCode_HIS);
        }

        return mrTemp;
    }

    private void topControlSave() {
        SaleBill_Mdl mrTemp;

        try {

            //======================Check Payment Source and Vourcher==========
            if ((DmShared.xNull(txtHType.getText()).equalsIgnoreCase(pvc.UHID)) && (DmShared.iNull(paymentSource).intValue() == STEnum.PaymentSource.CREDIT_TO_REGISTRATION.getEnumValue())) {
                new DmAlertMessageBox("Credit Sale not allowed for UHID", txtHVType).show();
                return;
            }

            //===================================End===========================
            calculate();
            mrTemp = topControlSaveInitialize();
            SaleBill_Vld.chkSave(mrTemp, voucher_Cat);
            SrvAsync.invokeRPC().saveRecord(voucher_Cat, mrTemp, previousMdl, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            if (!isEstimate) {
                                new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
                                    @Override
                                    public void onDialogHide(DialogHideEvent event) {
                                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                                            prnRecord(true);
                                        }
                                        topControl.btnClick("New");
                                    }
                                }).show();
                            } else {
                                topControl.btnClick("New");
                            }

                        } else {
                            topControlMoveRec(topControl.getDocId());
                        }
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
                new DmAlertMessageBox(errorMessage, txtPatient).show();
                break;
            case 800:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                if (!DmShared.xNull(errorSource).equalsIgnoreCase("GridRow")) {
                    List<GrdMedicine_Mdl> listTemp = grdMediDetails.getGrid().getStore().getAll();
                    for (GrdMedicine_Mdl node : listTemp) {
                        if (DmShared.xNull(node.getReqDocId()).equalsIgnoreCase(errorSource)
                                && DmShared.xNull(node.getReqVSno()).equalsIgnoreCase(errorSourceCode)) {
                            indexTemp = DmShared.iNull(listTemp.indexOf(node)).intValue();
                            if (errorMessage != null && !errorMessage.isEmpty()) {
                                if (Character.isDigit(errorMessage.charAt(errorMessage.length() - 1))) {
                                    if (node.getMedicineMdl() != null) {
                                        errorMessage += " " + DmShared.xNull(node.getMedicineMdl().getSaleUnitName());
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
            case 600:
                new DmAlertMessageBox(errorMessage, txtHType).show();
                break;
            case 700:
                new DmAlertMessageBox(errorMessage, txtRemark).show();
                break;
            case 740:
                new DmAlertMessageBox(errorMessage, txtHConsultant).show();
                break;
            case 750:
                if (!txtHIdType.isEnabled()) {
                    txtHIdType.setEnabled(true);
                }
                if (!txtIdNo.isEnabled()) {
                    txtIdNo.setEnabled(true);
                }
                new DmAlertMessageBox(errorMessage, txtHIdType).show();
                break;
            case 760:
                if (!txtHIdType.isEnabled()) {
                    txtHIdType.setEnabled(true);
                }
                if (!txtIdNo.isEnabled()) {
                    txtIdNo.setEnabled(true);
                }
                new DmAlertMessageBox(errorMessage, txtIdNo).show();
                break;
            case 770:
                if (!txtMobile.isEnabled()) {
                    txtMobile.setEnabled(true);
                }
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 775:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 780:
                if (!txtAddress.isEnabled()) {
                    txtAddress.setEnabled(true);
                }
                if (!txtHCity.isEnabled()) {
                    txtHCity.setEnabled(true);
                }
                new DmAlertMessageBox(errorMessage, txtAddress).show();
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<SaleBill_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SaleBill_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<SaleBill_Mdl> result) {
        SaleBill_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            saleOn = data.getSaleMedicine();
            previousMdl = new SaleBill_Mdl(data);
            freeWithItems_Mdl = result.getData();
            enableIdType(false);
            txtDate.setEnabled(false);
            txtHDepartment.setEnabled(false);
            topControl.setDocId(data.getSharedKeyFieldCode());
            paymentSource = DmShared.xNull(data.getPaymentSource());
            txtBillNo.setText(data.getSharedKeyFieldName());
            txtBillNo.setTag(DmShared.iNull(data.getRecIdSaleBill().getCounter()).toString());
            txtBillNo.setTag2nd(DmShared.xNull(data.getRecIdSaleBill().getPrefix()));
            txtHVType.setText(DmShared.xNull(data.getVoucherTypeName()));
            txtHVType.setTag(DmShared.xNull(data.getVoucherTypeCode()));
            txtHVType.setTag2nd(DmShared.xNull(data.getVoucherCategory()));

            documentNatureCode = DmShared.xNull(data.getDocumentNatureCode());
            documentTypeCode = DmShared.xNull(data.getDocumentTypeCode());

            txtDate.setText(DmShared.dNull(data.getVoucherDate()));
            txtDate.setTag2nd(DmShared.dNull(data.getVoucherDate()));
            //txtTime.setText(DmShared.xNull(data.getTime()));
            txtHType.setText(DmShared.xNull(data.getTypeName()));
            txtHType.setTag(DmShared.xNull(data.getTypeCode()));
            txtRegNo.setText(DmShared.xNull(data.getRegRecID()));
            txtHUHID.setText(DmShared.xNull(data.getRegRecID()));
            txtRegNo.setTag(DmShared.xNull(data.getRegDocid()));
            lblPatientClass.setText(DmShared.xNull(data.getPatientClassType()));
            lblPackegedInfo.setText(DmShared.xNull(data.getPatientPackagedInfo()));
            txtHDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtHDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            txtHOperation.setText(DmShared.xNull(data.getOperationName()));
            txtHOperation.setTag(DmShared.xNull(data.getOperationCode()));
            txtHMedicine.setText(DmShared.xNull(data.getMedicineName()));
            txtHMedicine.setTag(DmShared.xNull(data.getMedicineCode()));
            txtPatient.setTag2nd(DmShared.xNull(data.getRegSiteCode()));
            roomCode = DmShared.xNull(data.getRoomCode());
            txtPatient.setTag(DmShared.xNull(data.getPatientIntial()));
            txtPatient.setText(DmShared.xNull(data.getPatientName()));

            txtUHIDNo.setText(DmShared.xNull(data.getUhidRecId()));
            txtUHIDNo.setTag(DmShared.xNull(data.getUhidDocid()));
            if (DmShared.xNull(txtHType.getText().trim()).equalsIgnoreCase("UHID")) {
                txtUHIDNo.setText(DmShared.xNull(data.getLmManualCode()));
                txtUHIDNo.setTag(DmShared.xNull(data.getLmCode()));
            }

//            txtHRequisition.setTag(DmShared.xNull(data.getDocIdRequisition()));
//            txtHRequisition.setText(DmShared.xNull(data.getRecIdRequisition()));
            txtHEstimate.setTag(DmShared.xNull(data.getDocIdEstimate()));
            txtHEstimate.setText(DmShared.xNull(data.getRecIdEstimate()));
            txtHConsultant.setText(DmShared.xNull(data.getConsultantName()));
            txtHConsultant.setTag(DmShared.xNull(data.getConsultantCode()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtIdNo.setText(DmShared.xNull(data.getIdNo()));
            txtHIdType.setTag(DmShared.xNull(data.getIdTypeCode()));
            txtHIdType.setText(DmShared.xNull(data.getIdTypeName()));

            txtHCity.setTag(DmShared.xNull(data.getCityCode()));
            txtHCity.setText(DmShared.xNull(data.getCityName()));
            txtAddress.setText(DmShared.xNull(data.getAddress()));
            partyType = data.getPartyType();
            placeOfSupply = DmShared.xNull(data.getPlaceOfSupply());
            lmCode = DmShared.iNull(data.getLmCode()).toString();
            lmName = DmShared.xNull(data.getLmName()).toString();
            lmManualCode = DmShared.xNull(data.getLmManualCode()).toString();
            subCode = DmShared.xNull(data.getSubCode());
            partyManualCode = DmShared.xNull(data.getPartyManualCode());

            // set the Receipt Details for payment popup in Moverec
            txtReciptNo.setText(data.getRecIdRecipt().getRecIdValue());
            txtReciptNo.setTag(DmShared.iNull(data.getRecIdRecipt().getCounter()).toString());
            txtReciptNo.setTag2nd(DmShared.xNull(data.getRecIdRecipt().getPrefix()));
            txtReciptType.setText(data.getPrecVDescription());
            txtReciptType.setTag(data.getPrecVType());
            txtReciptType.setTag2nd(data.getPrecAttachPaymentMode());

            if (DmShared.vNull(data.getPharmacyBalance()).doubleValue() != 0) {
                lblPharmacyBalance.setText("Pharmacy Balance : " + data.getPharmacyBalance());
            }

//            if (DmShared.vNull(data.getRegistrationBalance()).doubleValue() != 0) {
//                lblRegistrationBalance.setText("Registration Balance : " + data.getRegistrationBalance());
//            }
            txtItemGross.setText(DmClient.vNull(data.getGrossAmt(), "0.00"));
            txtDiscount.setText(DmClient.vNull(data.getDiscountAmt(), "0.00"));
            txtDiscount2.setText(DmClient.vNull(data.getDiscountAmt2(), "0.00"));
            txtTaxableAmt.setText(DmClient.vNull(data.getTaxableAmt(), "0.00"));
            txtTaxAmount.setText(DmClient.vNull(data.getGstAmt(), "0.00"));
            txtCGSTAmt.setText(DmClient.vNull(data.getCgstAmt(), "0.00"));
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
            isEstimateSaleCreated = DmShared.xNull(data.getIsEstimateSaleCreated());

            organizationMdl = data.getOrganizationMdl();
            fill_ShowOrgPopUp(organizationMdl, false);

            lblDiscarded.setText(DmShared.iNull(data.getCancelRecord()).intValue() == 1 ? "Discarded" : "");
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            cardPayment = data.getCardPayment();
            cardPayment.getTxtHReceiptTypeVList().addAll(defaultValue.getCardPayment().getTxtHReceiptTypeVList());

            convertedPaymentSource = DmShared.xNull(data.getPaymentSource());
            grdMediDetails.getGrid().getStore().clear();
            grdMediDetails.getGrid().getStore().addAll(data.getGrdMediDetails());
            flwSearchRoom.clear();

            resetTaxSummary();
            calcMRPDiscount();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
        manageDisplay(false);

//        if (widgetName.equalsIgnoreCase("Sale Bill")) {
        if (DmShared.xNull(txtHType.getText()).equalsIgnoreCase("UHID")) {
            flwRegID_UHID.clear();
            flwRegID_UHID.add(txtHUHID);
            txtHUHID.setEnabled(false);

            linkParentDatabase = true;
        } else if (DmShared.xNull(txtHType.getText()).equalsIgnoreCase("IPD")) {

            flwRegID_UHID.clear();
            flwRegID_UHID.add(txtRegNo);
            txtRegNo.setEnabled(false);

            linkParentDatabase = true;
        }

        if (!regNo_HIS.equalsIgnoreCase("")) {
            txtRegNo.setText(regNo_HIS);
            txtUHIDNo.setText(uhidNo_HIS);
        }

//        }
    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(voucher_Cat, strKey, modifyReasonMdl, DmShared.xNull(txtRegNo.getTag()), new AsyncCallback<DmResult<String>>() {

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
        } else if (event.getSource().equals(txtRegNo)) {
            txtRegNo.setText(DmShared.xNull(txtRegNo.getTag2nd()));
        } else if (event.getSource().equals(txtHType)) {
            openRequisition();
        } else if (event.getSource().equals(txtUHIDNo)) {
            txtUHIDNo.setText(DmShared.xNull(txtUHIDNo.getTag2nd()));
        } else if (event.getSource().equals(txtPatient)) {
            enableIdType(true);
            setPatientName_AddressText();
        }
    }

    private void getRoundOffAcCode(final boolean isSaveCall) {
        flagCardPayment = false;
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
                        showCardPayment();
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
            cardPaymentPopup.hide();
            pharmacyDiscountPopup.hide();
            mediDetails.hide();
            uhidPopup.hide();
            roomSearchIPD.hide();
            roomSearchEMRG.hide();
            opdSearch.hide();
            narrationSearch.hide();
        }

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        setDefaultValue();
        isSchemeExists = false;
        lstItemWithSchemeDetails = new ArrayList<>();
        freeWithItems_Mdl = null;

//        txtRemark.setText("Routine");
//        txtRemark.setTag("Routine");
    }

    private void onEdit() {
        manageDisplay(true);
        enableIdType(false);
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
        if (isEstimate) {
            txtHOldOpd.setEnabled(flag);
        } else {
            txtHOldOpd.setEnabled(false);
        }
        txtDate.setEnabled(false);
        txtBillNo.setEnabled(false);
        txtHDepartment.setEnabled(false);
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
        txtRegNo.setEnabled(false);
        txtUHIDNo.setEnabled(false);
        txtMRP_Amount.setEnabled(false);
        txtDiffPercentOfAmountWithMRP.setEnabled(false);
        txtDiscountOnMRPValue.setEnabled(false);

        txtHConsultant.setEnabled(true);

        txtOrganisation.setEnabled(false);

        flwPayment.clear();
        flwPrescription.clear();
        flwPatientSearchTd.clear();
        manageRequisitionButton();

        if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL.caption)) {
            flwPayment.add(btnPayment);
        }
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtDate.setEnabled(dateEditable);
            txtRegNo.setEnabled(false);
            txtPatient.setEnabled(false);
            txtHType.setEnabled(false);
            txtHDepartment.setEnabled(false);
            txtHVType.setEnabled(false);
            txtHOperation.setEnabled(false);
            txtHMedicine.setEnabled(false);
            txtHEstimate.setEnabled(false);
            txtRemark.setEnabled(true);
            txtAddress.setEnabled(true);
            txtHCity.setEnabled(true);
        } else if (topControl.getMode().equalsIgnoreCase("New")) {
            txtRegNo.setEnabled(true);
            txtHType.setEnabled(true);
            txtHOperation.setEnabled(true);
            txtHMedicine.setEnabled(true);
            txtHVType.setEnabled(true);
            txtHEstimate.setEnabled(true);
            txtDate.setEnabled(dateEditable);
            txtHDepartment.setEnabled(true);
            txtRemark.setEnabled(true);
            if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0) {
                txtHDepartment.setEnabled(isDepartmentEditable);
            }
            txtRegNo.setEnabled(!DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(""));
        } else if (topControl.getMode().equalsIgnoreCase("Browse")) {
            txtDate.setEnabled(false);
            txtHDepartment.setEnabled(false);
            txtHVType.setEnabled(false);
            txtHOperation.setEnabled(false);
            txtHMedicine.setEnabled(false);
            txtHEstimate.setEnabled(false);
            txtHConsultant.setEnabled(false);
            txtRemark.setEnabled(false);
        }
        flwSearchRoom.clear();
        if (topControl.getMode().trim().equalsIgnoreCase("New")) {
            ManageBtnSearchRoom();
        }
        lblSgstUgst.setText(defaultValue.isUnionTerritory() ? "UGST Amount" : "SGST Amount");
    }

    private void ManageBtnSearchRoom() {
        flwSearchRoom.clear();
        if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
            flwSearchRoom.add(btnSearchRoom);
        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
            flwSearchRoom.add(btnOPDSearch);
        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Ledger"))) {
            flwSearchRoom.add(btnLedgerSearch);
        } else {
            flwSearchRoom.clear();
        }

    }

    private void getUHIDDetail(String recIdUHID, String docIdUHID, String recIdReg, String docIdReg, boolean callByEstimate, final DmCallBack callback) {
        if (DmShared.dNull(txtDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Voucher Date is mandatory.", txtDate).show();
            return;
        }
        SearchUHID_Mdl searchMdl = new SearchUHID_Mdl();
        searchMdl.setDocIdReg(DmShared.xNull(docIdReg));
        searchMdl.setDocIdRequisition(DmShared.xNull(requisitionDocId));
        searchMdl.setDocIdEstimate(DmShared.xNull(txtHEstimate.getTag()));
        searchMdl.setDocIdUHID(DmShared.xNull(docIdUHID));
        searchMdl.setRecIdReg(DmShared.xNull(recIdReg));
        searchMdl.setRecIdUHID(DmShared.xNull(recIdUHID));
        searchMdl.setVoucherCategory(DmShared.xNull(txtHType.getTag()));
        searchMdl.setVoucherDate(DmShared.dNull(txtDate.getText()));
        searchMdl.setCallByEstimate(callByEstimate);
        SrvAsync.invokeRPC().getUHIDDetails(searchMdl, (!DmShared.xNull(txtHEstimate.getText()).trim().equalsIgnoreCase("") ? false : linkParentDatabase), new AsyncCallback<DmResult<SaleBill_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUHIDNo).show();
            }

            @Override
            public void onSuccess(DmResult<SaleBill_Mdl> result) {
                if (result.isStatus()) {
                    fillHeader(result);
                    txtUHIDNo.setEnabled(false);
                    txtRegNo.setEnabled(false);
                    txtPatient.setEnabled(false);
                    txtHType.setEnabled(false);
                    flwPatientSearchTd.clear();
                    if (callback != null) {
                        DmResult<Object> dmrTemp = new DmResult<>();
                        dmrTemp.setStatus(true);
                        callback.result(dmrTemp);
                    }
                } else {
//                    txtHRequisition.setTag2nd("");
//                    txtHRequisition.setTag("");
//                    txtHRequisition.setText("");
                    txtUHIDNo.setTag2nd("");
                    txtUHIDNo.setTag("");
                    txtUHIDNo.setText("");
                    txtRegNo.setTag2nd("");
                    txtRegNo.setTag("");
                    txtRegNo.setText("");
                    if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
                        new DmAlertMessageBox(result.getErrorMessage(), btnSearchRoom).show();
                    } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
                        new DmAlertMessageBox(result.getErrorMessage(), btnOPDSearch).show();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtUHIDNo).show();
                    }
                }
                txtRemark.setFocus(true);
            }
        });

    }

    private void fillHeader(DmResult<SaleBill_Mdl> result) {
        SaleBill_Mdl data;
        if (result.isStatus()) {

            data = result.getData();
            saleOn = data.getSaleMedicine();
            organizationMdl = data.getOrganizationMdl();

            fill_ShowOrgPopUp(organizationMdl, false);

            siteCode_HIS = data.getSiteCode_HIS();

            if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("")) {
                txtHType.setTag(DmShared.xNull(data.getTypeCode()));
                txtHType.setText(DmShared.xNull(data.getTypeName()));
            }
            txtUHIDNo.setText(DmShared.xNull(data.getUhidRecId()));
            txtUHIDNo.setTag2nd(DmShared.xNull(data.getUhidRecId()));
            txtUHIDNo.setTag(DmShared.xNull(data.getUhidDocid()));
            if (DmShared.xNull(txtHType.getText()).equalsIgnoreCase("UHID")) {
                txtHUHID.setText(DmShared.xNull(data.getUhidRecId()));
                txtHUHID.setTag(DmShared.xNull(data.getUhidDocid()));
            }

            if (!DmShared.xNull(widgetName).equalsIgnoreCase("Estimate")) {
                txtRemark.setText(DmShared.xNull(data.getRemark()));
            }

            txtHIdType.setTag(DmShared.iNull(data.getIdTypeCode()).toString());
            txtHIdType.setText(DmShared.xNull(data.getIdTypeName()));
            txtIdNo.setText(DmShared.xNull(data.getIdNo()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtAddress.setText(DmShared.xNull(data.getAddress()));
            txtHCity.setTag(DmShared.xNull(data.getCityCode()));
            txtHCity.setText(DmShared.xNull(data.getCityName()));

            txtPatient.setTag2nd(DmShared.xNull(data.getRegSiteCode()));
            roomCode = DmShared.xNull(data.getRoomCode());
            txtPatient.setTag(DmShared.xNull(data.getPatientIntial()));
            txtPatient.setText(DmShared.xNull(data.getPatientName()));
            txtRegNo.setText(DmShared.xNull(data.getRegRecID()));
            txtRegNo.setTag2nd(DmShared.xNull(data.getRegRecID()));
            txtRegNo.setTag(DmShared.xNull(data.getRegDocid()));
            lblPatientClass.setText(DmShared.xNull(data.getPatientClassType()));
            lblPackegedInfo.setText(DmShared.xNull(data.getPatientPackagedInfo()));

            lmName = DmShared.xNull(data.getLmName());
            if (!DmShared.xNull(txtHType.getText()).trim().equalsIgnoreCase("UHID")) {
                lmCode = DmShared.xNull(data.getLmCode());
                lmManualCode = DmShared.xNull(data.getLmManualCode());
            } else {
                lmCode = DmShared.xNull(data.getUhidDocid());
                lmManualCode = DmShared.xNull(data.getUhidRecId());
                txtUHIDNo.setTag(data.getUhidDocid());
            }

            if (DmShared.iNull(data.getLmCode()).longValue() > 0) {
                getPharmacyBalance();
            }

            ///////////////==================Wrong Data Set But Client Agreed============================
            txtHConsultant.setText(DmShared.xNull(data.getConsultantName()));
            txtHConsultant.setTag(DmShared.xNull(data.getConsultantCode()));
//            txtHConsultant.setText("");
//            txtHConsultant.setTag("");

            if (DmShared.xNull(data.getConsultantCode()).equalsIgnoreCase("")) {
                txtHConsultant.setText("");
            }

            ///////////////==================Wrong Data Set But Client Agreed End ====================== 
            if (DmShared.iNull(data.getDocIdRequisition()).longValue() > 0) {
                txtHDepartment.setTag(data.getDepartmentCode());
                txtHDepartment.setText(data.getDepartmentName());
            }
            if (DmShared.iNull(data.getDocIdEstimate()).longValue() > 0) {
                getEstimateData();
            }
            grdPrescDetails_Mdl = result.getData().getGrdMediDetails();
            if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD) && grdPrescDetails_Mdl.size() > 0) {
                prescriptionPopup.show(grdPrescDetails_Mdl, this);
            }

            flwSearchRoom.clear();
            enableIdType(false);

            if (!requisitionWithoutUHID) {
                if ((data.isPatientsReqExists() && DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD) && isPatientDetailsFromType) || (DmShared.iNull(data.getDocIdRequisition()).longValue() > 0 && DmShared.iNull(requisitionDocId).longValue() > 0)) {
                    if (!btnRequisitionSearch.isAttached()) {
                        flwRequisitionSearch.clear();
                        flwRequisitionSearch.add(btnRequisitionSearch);
                    }
//                    openRequisition();
                }
            }
        }

        if (widgetName.equalsIgnoreCase("Sale Bill")) {
            //========================
            if (DmShared.xNull(txtHType.getText()).equalsIgnoreCase("UHID")) {
                flwRegID_UHID.clear();
                flwRegID_UHID.add(txtHUHID);
                txtHUHID.setEnabled(false);

                linkParentDatabase = true;
            } else if (DmShared.xNull(txtHType.getText()).equalsIgnoreCase("IPD")) {

                flwRegID_UHID.clear();
                flwRegID_UHID.add(txtRegNo);
                txtRegNo.setEnabled(false);

                linkParentDatabase = true;
            }

            if (!regNo_HIS.equalsIgnoreCase("")) {
                txtRegNo.setText(regNo_HIS);
                txtUHIDNo.setText(uhidNo_HIS);
            }

//============================
        }
//
//        txtHConsultant.setText("");
//        txtHConsultant.setTag("");
//        txtHConsultant.setTag2nd("");

    }

    private void getServerTime() {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<DmResult<String[]>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
            }

            @Override
            public void onSuccess(DmResult<String[]> result) {
                if (result.isStatus() && result.getData() != null) {
                    txtDate.setText(result.getData()[0]);
                    txtDate.setTag2nd(result.getData()[0]);
                    isExistEod();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0 || !isDepartmentEditable) {
                                txtHType.setFocus(true);
                            } else {
                                txtHVType.setFocus(true);
                            }
                        }
                    });
                }

            }
        });
    }

    private void calculate() {
        double discount = 0, itemGrossAmt = 0, roundoff = 0, netAmount = 0, cgstAmt = 0, sgstAmt = 0,
                taxableAmt = 0, billAmt = 0, discount2 = 0, mrpValue = 0, discountOnMRPValue = 0, discountPercentOnMRPValue = 0;
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
                    mrpValue += DmShared.vNull(medicineDetailMdl.getSkuMrp()).doubleValue() * DmShared.vNull(medicineDetailMdl.getQuantitySKU()).doubleValue();
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
        discountOnMRPValue = mrpValue - netAmount;
        discountPercentOnMRPValue = 100 - (netAmount * 100) / mrpValue;
        fillItemGrossandDiscount(DmClient.vNull(itemGrossAmt, "0.00"), DmClient.vNull(discount, "0.00"), DmClient.vNull(taxableAmt, "0.00"),
                DmClient.vNull(cgstAmt, "0.00"), DmClient.vNull(sgstAmt, "0.00"), DmClient.vNull(billAmt, "0.00"), DmClient.vNull(roundoff, "0.00"),
                DmClient.vNull(netAmount, "0.00"), DmClient.vNull(discount2, "0.00"),
                DmClient.vNull(mrpValue, "0.00"), DmClient.vNull(discountOnMRPValue, "0.00"), DmClient.vNull(discountPercentOnMRPValue, "0.00"));
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
//                    totalGrossAmount += Math.abs(DmShared.vNull(detailMdl.getAmount()));
                    totalGrossAmount += Math.abs(DmShared.vNull(detailMdl.getTaxableAmt()));
                }
            }
        }

        double landedSkuValue = 0, totalLandedSkuValue = 0;
        GrdMedicine_Mdl lastLineMdl = null;
        for (GrdMedicine_Mdl lineMdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (lineMdl != null) {
                if (!DmShared.xNull(lineMdl.getSharedKeyFieldCode()).trim().equals("")) {
//                    lineGrossAmount = Math.abs(DmShared.vNull(lineMdl.getAmount()));
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

    private void fillItemGrossandDiscount(String ItemGrossAmt, String Discount, String taxableAmt, String cgstAmt, String sgstAmt,
            String billAmt, String roundoff, String netAmt, String discount2, String mrpValue, String discountOnMRPValue, String discountPercentOnMRPValue) {
        txtItemGross.setText(ItemGrossAmt);
        txtDiscount.setText(Discount);
        txtDiscount2.setText(discount2);
        txtBillAmt.setText(billAmt);
        txtRoundoff.setText(roundoff);
        txtNetAmount.setText(netAmt);
        txtTaxableAmt.setText(taxableAmt);
        txtCGSTAmt.setText(cgstAmt);
        txtSgstUgstAmt.setText(sgstAmt);
        txtTaxAmount.setText(DmClient.vNull(DmShared.vNull(sgstAmt).doubleValue() + DmShared.vNull(cgstAmt).doubleValue(), "0.00"));
        if (isEstimate) {
            txtMRP_Amount.setText(mrpValue);
            txtDiffPercentOfAmountWithMRP.setText(discountOnMRPValue);
            txtDiscountOnMRPValue.setText(discountPercentOnMRPValue);
        }

    }

    private void clear() {
        previousMdl = null;
        landedValue = 0;
        flwPrescription.clear();
        flwPatientSearchTd.clear();
        lblPharmacyBalance.setText("");
//        lblRegistrationBalance.setText("");
        requisitionDocId = "";
        grdMediDetails.getGrid().getStore().clear();
        cardPayment = new CardPaymentPopup_Mdl();
        cardPayment = defaultValue.getCardPayment().clone();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        lblPatientClass.setText("");
        lblPackegedInfo.setText("");
        txtDate.setText("");
        txtDate.setTag("");
        txtDate.setTag2nd("");
        txtBillNo.setText("");
        txtBillNo.setTag("");
        txtBillNo.setTag2nd("");
        txtHEstimate.setText("");
        txtHEstimate.setTag("");
        txtHEstimate.setTag2nd("");
        txtHOperation.setText("");
        txtHOperation.setTag("");
        txtHOperation.setTag2nd("");
        txtHMedicine.setText("");
        txtHMedicine.setTag("");
        txtHMedicine.setTag2nd("");
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
        txtUHIDNo.setTag2nd("");
        txtUHIDNo.setTag("");
        txtUHIDNo.setText("");
        txtRegNo.setTag2nd("");

        txtOrganisation.setTag("");
        txtOrganisation.setText("");
        txtOrganisation.setTag2nd("");

        txtHOldOpd.setText("");
        txtHOldOpd.setTag("");

        txtRegNo.setTag("");
        txtRegNo.setText("");
        txtHType.setTag("");
        txtHType.setText("");

        lblDiscarded.setText("");

//        txtHRequisition.setTag("");
//        txtHRequisition.setTag2nd("");
//        txtHRequisition.setText("");
        saleOn = defaultValue.getMedicineSaleOn();
        partyType = defaultValue.getPartyType();
        organizationMdl = null;
        documentTypeCode = "";
        documentNatureCode = "";
        userEntDtLatestLine = "";
        lmCode = "";
        lmName = "";
        lmManualCode = "";
        subCode = "";
        roomCode = "";
        partyManualCode = "";
        placeOfSupply = DmShared.xNull(defaultValue.getStateCode());
        isEstimateSaleCreated = "";
        isReturnExists = "";
        flagCardPayment = false;
        // List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
        taxSummary.reset(new ArrayList<List<OverHeadLine_Mdl>>());

    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
            if (event.getSource().equals(txtRegNo)) {
                if (!DmShared.xNull(txtRegNo.getText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.xNull(txtHType.getTag()).trim().equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Type is mandatory.", txtHType).show();
                        return;
                    }
                    if (DmShared.xNull(txtHType.getTag()).trim().equalsIgnoreCase("Ledger")) {
                        getLedger_partyDetails(txtRegNo.getText());
                    } else {
                        getUHIDDetail("", "", txtRegNo.getText(), "", false, null);
                    }
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
                                btnSearchRoom.setFocus(true);
                            } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
                                btnOPDSearch.setFocus(true);
                            } else if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Ledger")) {
                                btnLedgerSearch.setFocus(true);
                            }
                        }
                    });
                }
            } else if (event.getSource().equals(txtUHIDNo)) {
                if (!DmShared.xNull(txtUHIDNo.getText()).trim().equalsIgnoreCase("")) {
                    getUHIDDetail(txtUHIDNo.getText(), "", "", "", false, null);
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            btnPatientSearch.setFocus(true);
                        }
                    });
                }
            }
        }
    }

    private void getLedger_partyDetails(String regNo) {
        SrvAsync.invokeRPC(true).getLedger(regNo, txtDate.getText(), new AsyncCallback<DmResult<PartySearch_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtRegNo).show();
            }

            @Override
            public void onSuccess(DmResult<PartySearch_Mdl> result) {
                if (result.isStatus()) {
                    partySearch_Mdl = result.getData();
                    fillPartyDetails();
                } else {
                    resetPatientDetails();
                    if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Ledger")) {
                        new DmAlertMessageBox(result.getErrorMessage(), btnLedgerSearch).show();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }

                }
            }
        });
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            btnClick(btnPatientSearch);
            rootEvent.stopPropagation();
        } else if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_R) {
            btnClick(btnSearchRoom);
            rootEvent.stopPropagation();
        }
    }
    String paymentSource = "";

    private void fillV_TypeDetail(TxtHVType_Mdl txtHVType_Mdl) {
        txtHVType.setTag(txtHVType_Mdl.getSharedKeyFieldCode());
        txtHVType.setText(txtHVType_Mdl.getSharedKeyFieldName());
        txtHVType.setTag2nd(txtHVType_Mdl.getVoucherCategory());
        documentTypeCode = DmShared.xNull(txtHVType_Mdl.getDocumentTypeCode());
        documentNatureCode = DmShared.xNull(txtHVType_Mdl.getDocumentNatureCode());
        paymentSource = DmShared.xNull(txtHVType_Mdl.getPaymentSource());
    }

    private void fill_ShowOrgPopUp(Organization_Mdl orgMdl, boolean isButtonClick) {
        if (orgMdl != null) {
            if (orgMdl.isTpa()) {
                if (DmShared.xNull(txtOrganisation.getText()).equalsIgnoreCase("")) {
                    txtOrganisation.setText(DmShared.xNull(orgMdl.getOrgShortName()));
                }
                if (isButtonClick) {
                    orgPopup.show(orgMdl, DmShared.dNull(txtDate.getText()), "Browse");
                }
            }
        }
    }

    private void manageRequisitionButton() {
        flwRequisitionSearch.clear();
        if (topControl.getMode().equalsIgnoreCase("New") && (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(new ProjectVoucherCategory().IPD) || DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(new ProjectVoucherCategory().EMERGENCY))) {
            flwRequisitionSearch.add(btnRequisitionSearch);
        }
    }

    private void calculateGrid() {
        for (GrdMedicine_Mdl mdl : grdMediDetails.getGrid().getStore().getAll()) {
            double purchaseUnitConversionRate = 0, saleUnitConversionRate = 1, itemValue = 0, totalTaxRate = 0, taxableAmt = 0, totalTaxAmt = 0, totalDiscount = 0;
            int saleUnitDecimalPlaces = 4, skuDecimalPlaces = 4;
            if (mdl.getMedicineMdl() != null) {
                setITSGrid(mdl.getMedicineMdl().getLstITS(), mdl);
            }

            if (mdl.getMedicineMdl() != null) {
                saleUnitDecimalPlaces = mdl.getMedicineMdl().getAllowDecimalPlaces();
                skuDecimalPlaces = mdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                saleUnitConversionRate = DmShared.vNull(mdl.getMedicineMdl().getConversionRate()).doubleValue();
                purchaseUnitConversionRate = DmShared.vNull(mdl.getMedicineMdl().getPurchaseUnitConversionRate()).doubleValue();
            }

            mdl.setQuantity(DmClient.vNull(mdl.getQuantity(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));
            mdl.setQuantitySKU(DmClient.vNull((DmShared.vNull(mdl.getQuantity()).doubleValue() * saleUnitConversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));
            mdl.setTotalQuantitySKU(DmClient.vNull((DmShared.vNull(mdl.getQuantity()).doubleValue() + DmShared.vNull(mdl.getFreeQuantityInSku()).doubleValue()), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

            mdl.setAmount(DmClient.vNull((DmShared.vNull(mdl.getQuantitySKU()).doubleValue() * DmShared.vNull(mdl.getSkuRate()).doubleValue()), "0.00"));
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

            mdl.setCgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getCgstPer()).doubleValue() * 0.01), "0.00"));
            mdl.setSgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getSgstPer()).doubleValue() * 0.01), "0.00"));
            mdl.setUgstAmt(DmClient.vNull((taxableAmt * DmShared.vNull(mdl.getUgstPer()).doubleValue() * 0.01), "0.00"));

            mdl.setGstPer(DmClient.vNull(DmShared.vNull(mdl.getCgstPer()).doubleValue() + DmShared.vNull(mdl.getSgstPer()).doubleValue() + DmShared.vNull(mdl.getUgstPer()).doubleValue(), "0.00"));
            mdl.setGstAmount(DmClient.vNull(DmShared.vNull(mdl.getCgstAmt()).doubleValue() + DmShared.vNull(mdl.getSgstAmt()).doubleValue() + DmShared.vNull(mdl.getUgstAmt()).doubleValue(), "0.00"));

            mdl.setTotalDiscount(DmClient.vNull(totalDiscount, "0.00"));
            mdl.setTaxableAmt(DmClient.vNull(taxableAmt, "0.00"));
            mdl.setNetAmt(DmClient.vNull(itemValue, "0.00"));

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
            schemeMaster.show(detail.getSharedKeyFieldCode(), topControl.getMode(), txtHDepartment.getTag(), txtDate.getText(), selectedBatchSQL, String.valueOf(itemTotalQtyInSaleUnit));
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callback) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        try {
            switch (nature) {
                case "ADD_REQUISITION":
                    openWithRequisition((String) object, callback);
                    break;
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

    public void openWithRequisition(final String reqDocId, final DmCallBack callback) {
        SrvAsync.invokeRPC().getServerDateTime(new AsyncCallback<DmResult<String[]>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
            }

            @Override
            public void onSuccess(DmResult<String[]> result) {
                if (result.isStatus() && result.getData() != null) {
                    topControl.btnClick("New");
                    txtDate.setText(result.getData()[0]);
                    txtDate.setTag2nd(result.getData()[0]);
                    requisitionDocId = reqDocId;
                    getUHIDDetail("", "", "", "", false, callback);
                }
            }
        });

    }

    public void oldOpdPrescMed(String opdNo, String consultantCode) {
        SrvAsync.invokeRPC().getPrescMedicineDetail(opdNo, consultantCode, new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHVType).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {
                if (result.isStatus() && result.getData() != null) {
                    grdPrescDetails_Mdl = result.getData();
                    openPrescription();
                }
            }
        });

    }

    private void setPatientName_AddressText() {
        String patientName = DmShared.xNull(txtPatient.getText());
        String address = DmShared.xNull(txtAddress.getText());
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

            if (DmShared.xNull(defaultValue.getAddressAlphabetCase()).equalsIgnoreCase("U")) {
                address = address.toUpperCase();
            } else if (DmShared.xNull(defaultValue.getAddressAlphabetCase()).equalsIgnoreCase("L")) {
                address = address.toLowerCase();
            } else if (DmShared.xNull(defaultValue.getAddressAlphabetCase()).equalsIgnoreCase("S")) {
                address = DmShared.sentenceCase(address);
            } else if (DmShared.xNull(defaultValue.getAddressAlphabetCase()).equalsIgnoreCase("P")) {
                address = DmShared.properCase(address);
            }
        }
        txtPatient.setText(patientName);
        txtAddress.setText(address);
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
                //overheadLineRow.setAtRate(grdMdl.gett);
                overheadLineRow.setAmount(grdMdl.getTaxableAmt());
                overheadLine.add(overheadLineRow);

                lstOverheadLine.add(overheadLine);
                //loop end
            }
        }
        taxSummary.reset(lstOverheadLine);
    }

    private void isExistEod() {
        SrvAsync.invokeRPC().isEodExist(txtDate.getText(), new AsyncCallback<DmResult<Boolean>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(DmResult<Boolean> result) {
                if (result.getData()) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Eod has been initialized can't be add / edit/ delet.", (DmTextBox) null).show();
                    return;
                } else {
                    if (!isEstimate) {
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                txtHEstimate.setFocus(true);
                            }
                        });
                    }
                }
            }
        });
    }

    private void calcMRPDiscount() {
        double mrpValue = 0, discountOnMRPValue = 0, discountPercentOnMRPValue = 0;
        TxtHMedicine_Mdl medicineMdl;

        if (grdMediDetails.getGrid().getStore().getAll() != null) {
            for (GrdMedicine_Mdl medicineDetailMdl : grdMediDetails.getGrid().getStore().getAll()) {
                medicineMdl = medicineDetailMdl.getMedicineMdl();
                if (medicineMdl != null && DmShared.iNull(medicineMdl.getSharedKeyFieldCode()).longValue() > 0) {
                    mrpValue += DmShared.vNull(medicineDetailMdl.getSkuMrp()).doubleValue() * DmShared.vNull(medicineDetailMdl.getQuantitySKU()).doubleValue();
                }
            }
        }
        grdMediDetails.commitChanges();
        discountOnMRPValue = mrpValue - DmShared.vNull(txtNetAmount.getText()).doubleValue();
        discountPercentOnMRPValue = 100 - (DmShared.vNull(txtNetAmount.getText()).doubleValue() * 100) / mrpValue;
        txtMRP_Amount.setText(DmClient.vNull(mrpValue, "0.00"));
        txtDiffPercentOfAmountWithMRP.setText(DmClient.vNull(discountOnMRPValue, "0.00"));
        txtDiscountOnMRPValue.setText(DmClient.vNull(discountPercentOnMRPValue, "0.00"));
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
//        if (!isEstimate && event.getSource() == txtHVType && event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
//            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
//                @Override
//                public void execute() {
//                    txtHEstimate.setFocus(true);
//                }
//            });
//        }
    }
}
