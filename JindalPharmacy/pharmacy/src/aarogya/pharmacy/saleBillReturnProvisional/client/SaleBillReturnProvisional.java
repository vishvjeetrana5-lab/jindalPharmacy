package aarogya.pharmacy.saleBillReturnProvisional.client;

import aarogya.pharmacy.partySearch.client.PartySearch;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.saleBillItemSearch.client.SaleBillItemSearch;
import aarogya.pharmacy.saleBillReturnProvisional.shared.*;
import aarogya.pharmacy.saleBillReturnProvisional.client.ReturnMedicineBatchDetail;
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
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import dataman.dmToolkit.overHead.shared.OverHead_Shared;
import dataman.dmToolkit.overHeadLine.shared.OverHeadLine_Mdl;
import java.util.*;

public final class SaleBillReturnProvisional extends SaleBillReturnProvisional_Dsg implements KeyPressHandler, BlurHandler, AttachEvent.Handler, ClickHandler, DmRootEvents {

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private String textBoxData = "";
    private String voucher_Cat = "", widgetName = "";
    private String documentTypeCode = "", documentNatureCode = "";
    private final ServiceInvoker<SaleBillReturnProvisional_SrvAsync> SrvAsync;
//    private CardPaymentPopup_Mdl cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
    private ModifyReason_Mdl modifyReasonMdl;
    private final ModifyReason modifyReason = new ModifyReason();
    private boolean dateEditable = false, isDepartmentEditable = false;
    protected boolean rateEditable = false, isAllowAddBatch = false; //, isAllowCTP = false, isCTRAllowed = false, isCTOAllowed = false
    boolean ctoAllow = false, ctpAllow = false, ctrAllow = false, receiptAllow = false,
            opdAllow = false, ipdAllow = false, dayCareAllow = false, emergencyAllow = false, walkInAllow = false, ledgerAllow = false;
    private final MedicineDetailsEdit<SaleBillReturnProvisional_Mdl, GrdMediDetails_PrpAcs> mediDetails = new MedicineDetailsEdit(grdMediDetails);
    protected Default_Mdl defaultValue;
    private SaleBillReturnProvisional_Mdl previousMdl;
    private final UHIDDuplicacy duplicacyPopup = new UHIDDuplicacy();
    private final UhidPopup uhidPopup = new UhidPopup();
    private final OPDSearch opdSearch = new OPDSearch();
    private final PatientSearchRoomWise roomSearchIPD = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.IPD_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private final PatientSearchRoomWise roomSearchEMRG = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.EMERGENCY_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private PatientSearchRoomWise.CallBack roomSearchCallBack;
    protected MedicineSaleOn saleOn;
    public String expiryAlertBeforeDays = "";
    private String userEntDtLatestLine = "", sbNetAmt = "", sbV_Type = "";
    String billDocId = "", nature = "", lmCode = "", partyType = "", subCode = "", partyManualCode = "", placeOfSupply = "", roomCode = "", lmName = "", lmManualCode = "";
    private Organization_Mdl organizationMdl;
    private final OrganizationPopup orgPopup = new OrganizationPopup();
//    private final CardPaymentPopup cardPaymentPopupSaleBillReturn = new CardPaymentPopup();
    private boolean isOpenedFromSbHelp = false, printRecord = false;
    HashMap<String, FieldUDT_Mdl> hashMapFilterPrint = new HashMap<>();

    final SaleBillItemSearch saleBillItemSearch = new SaleBillItemSearch();
    private ReturnMedicineBatchDetail.CallBack invoicedMedicineCallBack;
    final PartySearch partySearch = new PartySearch();
    private PartySearch_Mdl partySearch_Mdl;
    private boolean flagCardPayment = false, isIncludeTaxInCost = false, flagOnOk = true, flagRemove = true;
    double landedValue = 0;
    protected String convertedPaymentSource = null;

    private String moduleName = new pmPharmacy().moduleName;

    private ReturnMedicineBatchDetail returnMedicineBatchDetail;

    private List<KeyFieldExtended> lstReturnedMedicine = new ArrayList<>();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setCardViewRequired(false);
        topControl.setOtherRequired(false);
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
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctoAllowBlog)) {
//                ctoAllow = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctpAllowBlog)) {
//                ctpAllow = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(ctrAllowBlog)) {
//                ctrAllow = true;
//            } else if (DmShared.xNull(controlPermission).equalsIgnoreCase(receiptAllowBlog)) {
//                receiptAllow = true;
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
                if (DmShared.xNull(lblApproved.getText()).equalsIgnoreCase("Approved")) {
                    topControl.btnClick("");
                    new DmAlertMessageBox("Record is Approved.", (DmTextBox) null).show();
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

                if (DmShared.xNull(lblApproved.getText()).equalsIgnoreCase("Approved")) {
                    new DmAlertMessageBox("Record is Approved.", (DmTextBox) null).show();
                    return;
                }

                new DmConfirmMessageBox("Are you sure? You want to discard this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            showModifyReason();
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
                if (DmShared.xNull(txtRoundoff.getTag()).trim().equals("")) {
//                    getRoundOffAcCode(true);
//                    topControlSave(); //by Vivek
                    topControlSave();
                } else {
                    flagCardPayment = false;
                    if (topControl.getMode().equalsIgnoreCase("New")) {
                        topControlSave();
                    } else {
                        showModifyReason();
                    }
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(SaleBillReturnProvisional.this);
            }
        });

        topControl.setDisplay(true);
    }

    private SaleBillReturnProvisional_Mdl chkValidation() {
        SaleBillReturnProvisional_Mdl mrTemp;
        mrTemp = topControlSaveInitialize();
        SaleBillReturnProvisional_Vld.chkSave(mrTemp);
        return mrTemp;
    }

    private void prnRecord(boolean printToPrinter) {
        hashMapFilterPrint.put("docId", new FieldUDT_Mdl("Return No.", DmShared.xNull(txtBillNo.getText()), DmShared.xNull(topControl.getDocId())));
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            new DmConfirmMessageBox("Are you sure? You want to print this record.", new DialogHideEvent.DialogHideHandler() {
                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        printRecord = true;
                    } else {
                        printRecord = false;
                    }
                    finalPrint(printRecord, hashMapFilterPrint);
                }
            }).show();
        } else {
            printRecord = printToPrinter;
            finalPrint(printRecord, hashMapFilterPrint);
        }
    }

    private void finalPrint(boolean printToPrinter, HashMap hashMapFilter) {
        new OpenSourceReport(txtHVType.getTag(), "", "Sale Return", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
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
                        txtHType.setFocus(true);
                    }
                });
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

        saleBillItemSearch.addCallBack(new SaleBillItemSearch.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                fillSaleBillDetails(listKF);
            }

            @Override
            public void close() {
                txtHType.setFocus(true);
            }
        });

//        cardPaymentPopupSaleBillReturn.addCallBack(new CardPaymentPopup.CallBack() {
//
//            @Override
//            public void onOk(CardPaymentPopup_Mdl cardPaymentMdl) {
////                cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
////                cardPaymentSaleBillReturn = cardPaymentMdl.clone();
//                txtReciptType.setText(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypeName())));
//                txtReciptType.setTag(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypeCode())));
//                txtReciptType.setTag2nd(DmShared.xNull(DmShared.xNull(cardPaymentMdl.getReceiptTypePM())));
//
//                if (topControl.getMode().equalsIgnoreCase("New")) {
//                    topControlSave();
//                } else {
//                    showModifyReason();
//                }
//            }
//
//            @Override
//            public void onCancel(CardPaymentPopup_Mdl cardPaymentMdl) {
//
//            }
//        });
        opdSearch.addCallBack(new OPDSearch.CallBack() {

            @Override
            public void ok(String recIdReg, String docIdReg) {
                getUHIDDetail("", "", "", docIdReg);
            }

            @Override
            public void close() {
                txtUHIDNo.setFocus(true);
            }
        });
        roomSearchCallBack = new PatientSearchRoomWise.CallBack() {
            @Override
            public void ok(String regDocid, String regRecid, String roomCode) {
                getUHIDDetail("", "", "", regDocid);
            }

            @Override
            public void close() {
                txtUHIDNo.setFocus(true);
            }
        };
        uhidPopup.addCallBack(new UhidPopup.CallBack() {
            @Override
            public void onOk(UhidPopup_Mdl mrTemp) {
                if (DmShared.iNull(mrTemp.getDocIdUHID()).longValue() > 0 || DmShared.iNull(mrTemp.getDocIdRegistration()).longValue() > 0) {
                    getUHIDDetail("", DmShared.iNull(mrTemp.getDocIdUHID()).toString(), "", DmShared.iNull(mrTemp.getDocIdRegistration()).toString());
                } else {
                    duplicacyPopup.show(mrTemp);
                }
                txtHType.setTag("Walk-In");
                txtHType.setText("Walk-In");
                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }

            @Override
            public void onClose() {
                txtHGstReason.setFocus(true);
            }
        });
        duplicacyPopup.addCallBack(new UHIDDuplicacy.CallBack() {
            @Override
            public void ok(String uhidDocId, String uhidNo) {
                if (DmShared.iNull(uhidDocId).longValue() > 0) {
                    getUHIDDetail(uhidNo, uhidDocId, "", "");
                }
            }

            @Override
            public void close(UhidPopup_Mdl uhidPopupMdl) {
                txtPatient.setText(DmShared.xNull(uhidPopupMdl.getPatientName()));
                txtMobile.setText(DmShared.xNull(uhidPopupMdl.getMobileNo()));
                txtPatient.setFocus(true);
            }
        });
        modifyReason.addCallBack(new ModifyReason.CallBack() {

            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                SaleBillReturnProvisional.this.modifyReasonMdl = modifyReason;
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHVType(voucher_Cat, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHVType_Mdl>>>() {

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
            }
        });
        txtHType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCustomerType(helper, blnToEndOfFile, opdAllow, ipdAllow, dayCareAllow, emergencyAllow, walkInAllow, ledgerAllow, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
            public void getSelectedRow(boolean blnStatus, final KeyFieldExtended t1Row) {
                flwSearchRoom.clear();
                flwPatientSearchTd.clear();
                if (t1Row != null && blnStatus) {
                    setIPD_OPD_Dsiplay();
                    if (!DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("Walk-In")) {
                        txtRegNo.setEnabled(true);
                    } else if (DmShared.xNull(t1Row.getSharedKeyFieldCode()).equalsIgnoreCase("Walk-In")) {
                        txtRegNo.setEnabled(false);
                        flwPatientSearchTd.add(flwPatientSearch);
                    }
                } else {
                    flwSearchRoom.clear();
//                    flwPatientSearchTd.add(flwPatientSearch);
                }

                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHType.getTag()).trim().equalsIgnoreCase("")) {
                            if (!DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Walk-In")) {
                                txtRegNo.setFocus(true);
                            }
                        } else {
                            txtRegNo.setEnabled(false);
                        }
                    }
                });
//<editor-fold defaultstate="collapsed" desc="OLD">

//                if (blnStatus && t1Row != null) {
//                    txtUHIDNo.setTag("");
//                    txtUHIDNo.setTag2nd("");
//                    txtUHIDNo.setText("");
//                   txtRegNo.setTag("");
//                    txtRegNo.setTag2nd("");
//                    txtRegNo.setText("");
//                    if (t1Row.getSharedKeyFieldCode().equalsIgnoreCase(pvc.OPD)) {
//                        flwPrescription.add(btnPrescription);
//                    }
//                    flwRegSearch.add(btnRegSearch);
//                    flwUHIDSearch.add(btnUHIDSearch);
//                    txtRegNo.setEnabled(true);
//                    txtUHIDNo.setEnabled(true);
//                    txtRegNo.setFocus(true);
//                } else {
//                    txtRegNo.setEnabled(false);
//                    txtUHIDNo.setEnabled(false);
//                }
//</editor-fold>
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
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHSaleBillNo(helper, blnToEndOfFile, txtHDepartment.getTag(), txtRegNo.getTag(), txtDate.getText(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), lmCode, txtHType.getTag(), DmShared.xNull(defaultValue.getPharamcyReturnDays()), DmShared.xNull(txtHVType.getTag()), new AsyncCallback<DmResultPageing<List<TxtHSaleBillNo_Mdl>>>() {

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
                    txtHSaleBillNo.setTag2nd(DmShared.dNull(t1Row.getSaleBillDate()));
                    sbNetAmt = t1Row.getNetAmount();
                    sbV_Type = t1Row.getV_Type();
                    lmCode = DmShared.xNull(t1Row.getLmCode());
                    lmName = DmShared.xNull(t1Row.getLmName());
                    lmManualCode = DmShared.xNull(t1Row.getLmManualCode());

                    if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
                        txtHDepartment.setTag(DmShared.xNull(t1Row.getDepartmentCode()));
                        txtHDepartment.setText(DmShared.xNull(t1Row.getDepartmentName()));
                    }
                    getUHIDDetail("", "", "", "");
                }
                enableDisable_RefNo_SaleBillNo();
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

        returnMedicineBatchDetail = new ReturnMedicineBatchDetail("");
        invoicedMedicineCallBack = new ReturnMedicineBatchDetail.CallBack() {
            @Override
            public void ok(List<KeyFieldExtended> listKF) {
                fillInvoicedMedicineDetails(listKF, true);
            }

            @Override
            public void close() {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
                    }
                });
                txtHType.setFocus(true);
            }

            @Override
            public void delete(List<KeyFieldExtended> listKF) {
                fillInvoicedMedicineDetails(listKF, false);
//                grdMediDetails.commitChanges();
//                lstReturnedMedicine.remove(listKF);
//                grdMediDetails.commitChanges();
            }

        };

        returnMedicineBatchDetail.addCallBack(invoicedMedicineCallBack);
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
                txtHGstReason.setFocus(true);
//                ((CellSelectionModel) grdMediDetails.getGrid().getSelectionModel()).selectCell(0, 1);
            }
        });

    }

    private void enableIdType(boolean flag) {
        if (flag) {
            if (!txtAddress.isEnabled()) {
                txtAddress.setEnabled(true);
            }
            if (!txtMobile.isEnabled()) {
                txtMobile.setEnabled(true);
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
            if (!DmShared.xNull(txtHType.getText()).equalsIgnoreCase("Walk-In")) {
                if (!DmShared.xNull(txtAddress.getText()).equalsIgnoreCase("")) {
                    if (txtAddress.isEnabled()) {
                        txtAddress.setEnabled(false);
                    }
                }
                if (txtHCity.isEnabled()) {
                    txtHCity.setEnabled(false);
                }
            }
            if (txtMobile.isEnabled()) {
                txtMobile.setEnabled(false);
            }
            if (txtHIdType.isEnabled()) {
                txtHIdType.setEnabled(false);
            }
            if (txtIdNo.isEnabled()) {
                txtIdNo.setEnabled(false);
            }
        }

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
        placeOfSupply = defaultValue.getStateCode();
        txtRegNo.setText("");
        txtRegNo.setTag("");
        txtRegNo.setTag2nd("");
        flagCardPayment = false;
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
                    calculateGrid();
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
                    returnMedicineBatchDetail.getClear();
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }

            }
        });
    }

    private void setIPD_OPD_Dsiplay() {
        ManageBtnSearchRoom();
    }

    private void getSaleBillData() {
        if (DmShared.iNull(txtHSaleBillNo.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Sale Bill No. is mandatory.", txtHSaleBillNo).show();
            return;
        }
        showSaleBillItemSearch();
        //<editor-fold defaultstate="collapsed" desc="comment">
//        SrvAsync.invokeRPC(true).getSaleBillDetail(DmShared.iNull(txtHSaleBillNo.getTag()).toString(), topControl.getMode().equalsIgnoreCase("New") ? "" : DmShared.xNull(topControl.getDocId()), DmShared.dNull(txtDate.getText()), new AsyncCallback<DmResult<List<GrdMedicine_Mdl>>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                new DmAlertMessageBox(caught.getMessage(), txtHSaleBillNo).show();
//            }
//
//            @Override
//            public void onSuccess(DmResult<List<GrdMedicine_Mdl>> result) {
//                if (result.isStatus()) {
//                    grdMediDetails.getGrid().getStore().clear();
//                    grdMediDetails.getGrid().getStore().addAll(result.getData());
//
//                    flwSaleBillSearch.clear();
//                    calculateGrid();
////                    grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
//                } else {
//                    new DmAlertMessageBox(result.getErrorMessage(), txtHSaleBillNo).show();
//                }
//            }
//        });
//</editor-fold>

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
//                textBoxData=grdMediDetails.getGrid().getStore().get(event.getRowIndex());

                isExistpendingRequest(txtRegNo.getTag(), txtHDepartment.getTag(),topControl.getMode(), grdMediDetails.getGrid().getStore().get(event.getRowIndex()));
//                showInvoicedMedicineDetails(txtRegNo.getTag(), lstReturnedMedicine);

            }
        });

        grdMediDetails.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showInvoicedMedicineDetails(txtRegNo.getTag(), lstReturnedMedicine, null);
//                    gridMediDetailsEdit(grdMediDetails.getGrid().getStore().indexOf(grdMediDetails.getGrid().getSelectionModel().getSelectedItem()));

                }
            }
        }, KeyDownEvent.getType());
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == labelDiscount) {
        }
        btnClick((Button) event.getSource());
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
        } else if (button == btnSaleBillSearch) {
            isOpenedFromSbHelp = false;
            showSaleBillItemSearch();
        } else if (button == btnLedgerSearch) {
            partySearch.show(DmShared.dNull(txtDate.getText()), defaultValue.getVoucherTypeMdl().getSharedKeyFieldCode());
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
        } else if (button == btnOrganisation) {
            fill_ShowOrgPopUp(organizationMdl, true);
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
        if (DmShared.xNull(txtPatient.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Patient Name", txtPatient).show();
            return;
        }
        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(new ProjectVoucherCategory().IPD)) {
            if (DmShared.iNull(txtRegNo.getTag()).longValue() == 0) {
                new DmAlertMessageBox("Reg. No. is mandatory", btnSearchRoom).show();
                return;
            }
        }
        saleBillItemSearch.show(txtHSaleBillNo.getTag(), DmShared.dNull(txtDate.getText()), voucher_Cat, txtHVType.getTag2nd(), topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), DmShared.xNull(txtHDepartment.getTag()), DmShared.iNull(txtRegNo.getTag()).toString(), DmShared.iNull(txtRegNo.getTag()).longValue() > 0 ? "" : lmCode, DmShared.xNull(defaultValue.getPharamcyReturnDays()),"saleBillReturnProvisional");
    }

    private void gridMediDetailsEdit(Integer currentRowIndex) {
        if (DmShared.iNull(txtHDepartment.getTag()).longValue() == 0) {
            new DmAlertMessageBox("Please Select Department.", txtHDepartment).show();
            return;
        }
        if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Select Customer Type.", txtHType).show();
            return;
        }
        if (DmShared.xNull(txtPatient.getText()).equalsIgnoreCase("")) {
            new DmAlertMessageBox("Please Fill Patient Name.", txtPatient).show();
            return;
        }
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            mediDetails.show(currentRowIndex, false, false, false, this);
        } else {
            if (DmShared.iNull(txtHSaleBillNo.getTag()).longValue() != 0) {
                mediDetails.show(currentRowIndex, true, false, true, this);
            } else {
                mediDetails.show(currentRowIndex, true, true, true, this);
            }
        }
    }

    public SaleBillReturnProvisional(UserMenu menuPermission, HashMap paramHashMap, SetWidgetInvoker swi) {
        super(paramHashMap != null ? (String) paramHashMap.get("widgetName") : new pmPharmacy().SALE_BILL_RETURN_PROVISIONAL.caption);
        SrvAsync = new ServiceInvoker<>(GWT.create(SaleBillReturnProvisional_Srv.class), SaleBillReturnProvisional.class.getName());
        if (paramHashMap != null) {
            widgetName = (String) paramHashMap.get("widgetName");
            voucher_Cat = (String) paramHashMap.get("vCategory");
        }

        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        handlerEvents();
        iniDefaultValues(swi);
        grdMediDetails.setDefaultColumnIndex(8);    //TBC
    }

    private void handlerEvents() {
        btnPatientSearch.addClickHandler(this);
        btnOPDSearch.addClickHandler(this);
        btnSaleBillSearch.addClickHandler(this);
        btnOrganisation.addClickHandler(this);
        txtDate.addBlurHandler(this);
        txtRefDate.addBlurHandler(this);
        txtRegNo.addBlurHandler(this);
        txtPatient.addBlurHandler(this);
        txtRefNo.addBlurHandler(this);
        txtUHIDNo.addBlurHandler(this);
        txtUHIDNo.addKeyPressHandler(this);
        flwForm.addAttachHandler(this);
        labelDiscount.addClickHandler(this);
//        btnPayment.addClickHandler(this);
        btnSearchRoom.addClickHandler(this);
        txtRegNo.addKeyPressHandler(this);
        roomSearchEMRG.addCallBack(roomSearchCallBack);
        roomSearchIPD.addCallBack(roomSearchCallBack);
        btnLedgerSearch.addClickHandler(this);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        SrvAsync.invokeRPC().getDefaultVal(voucher_Cat, new AsyncCallback<DmResult<Default_Mdl>>() {

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
                    flwSearchRoom.clear();
                    taxSummary.initialize(defaultValue.isGstApplicable());
                    manageDisplay(false);
                    swi.setWidget(SaleBillReturnProvisional.this);
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

    private SaleBillReturnProvisional_Mdl topControlSaveInitialize() {
        SaleBillReturnProvisional_Mdl mrTemp = new SaleBillReturnProvisional_Mdl();

        setPatientName_AddressText();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecIdSaleBillReturn().setRecIdValue(DmShared.xNull(txtBillNo.getText()));
            mrTemp.getRecIdSaleBillReturn().setCounter(DmShared.iNull(txtBillNo.getTag()).longValue());
            mrTemp.getRecIdSaleBillReturn().setPrefix(DmShared.xNull(txtBillNo.getTag2nd()));

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
        mrTemp.setVoucherDate(DmShared.dNull(txtDate.getText()));
        mrTemp.setRegDocid(DmShared.xNull(txtRegNo.getTag()));
        mrTemp.setRegRecID(DmShared.xNull(txtRegNo.getText()));
        mrTemp.setRegSiteCode(DmShared.xNull(txtPatient.getTag2nd()));
        mrTemp.setRoomCode(DmShared.xNull(roomCode));
        mrTemp.setUhidDocid(DmShared.xNull(txtUHIDNo.getTag()));
        mrTemp.setUhidRecId(DmShared.xNull(txtUHIDNo.getText()));
        mrTemp.setIdNo(DmShared.xNull(txtIdNo.getText()));
        mrTemp.setIdTypeCode(DmShared.xNull(txtHIdType.getTag()));
        mrTemp.setIdTypeName(DmShared.xNull(txtHIdType.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setDocIdSaleBill(DmShared.xNull(txtHSaleBillNo.getTag()));
        mrTemp.setRecIdSaleBill(DmShared.xNull(txtHSaleBillNo.getText()));
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setConsultantCode(DmShared.xNull(txtHConsultant.getTag()));
        mrTemp.setPatientName(DmShared.xNull(txtPatient.getText()));
        mrTemp.setReceiptTypeCode(DmShared.xNull(txtReciptType.getTag()));
        mrTemp.setReceiptTypeName(DmShared.xNull(txtReciptType.getText()));
        mrTemp.setReceiptTypeCateory(DmShared.xNull(txtReciptType.getTag2nd()));
        mrTemp.setV_TypeSaleBill(DmShared.xNull(sbV_Type));
        mrTemp.setNet_AmtSaleBill(DmShared.xNull(sbNetAmt));
        mrTemp.setCardPaymentPopupNetAmount(DmShared.xNull(DmShared.vNull(0 - DmShared.vNull(txtNetAmount.getText()).doubleValue()).toString()));
        mrTemp.setFlagCardPayment(flagCardPayment ? "1" : "0");

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
        mrTemp.setGstReasonCode(DmShared.xNull(txtHGstReason.getTag()));
        mrTemp.setGstReasonName(DmShared.xNull(txtHGstReason.getText()));
        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setPartyManualCode(DmShared.xNull(partyManualCode));
        mrTemp.setPlaceOfSupply(DmShared.xNull(placeOfSupply));
        mrTemp.setSiteStateCode(DmShared.xNull(defaultValue.getStateCode()));

        mrTemp.setDefaultPharmacyServiceCode(DmShared.xNull(defaultValue.getDefaultPharmacyServiceCode()));
        mrTemp.getRecIdRecipt().setRecIdValue(DmShared.xNull(txtReciptNo.getText()));

        mrTemp.setRefDate(DmShared.dNull(txtRefDate.getText()));
        mrTemp.setRefNo(DmShared.xNull(txtRefNo.getText()));

        mrTemp.setUserEntDtLatestLine(DmShared.xNull(userEntDtLatestLine));

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
        if (defaultValue.isUnionTerritory()) {
            mrTemp.setUgstAmt(DmClient.vNull(txtSgstUgstAmt.getText(), "0.00"));
        } else {
            mrTemp.setSgstAmt(DmClient.vNull(txtSgstUgstAmt.getText(), "0.00"));
        }
        mrTemp.getGrdMediDetails().addAll(grdMediDetails.getGrid().getStore().getAll());
        if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL_RETURN_PROVISIONAL.caption)) {
            mrTemp.getCardPaymentSaleBillReturn().getGrdCardPaymentList().addAll(new ArrayList<GrdCardPayment_Mdl>());
//            mrTemp.setCardPaymentSaleBillReturn(cardPaymentSaleBillReturn.clone());
        }

        return mrTemp;
    }

    private void topControlSave() {
        SaleBillReturnProvisional_Mdl mrTemp;

        try {
            calculate();
            mrTemp = chkValidation();

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
            case 410:
                new DmAlertMessageBox(errorMessage, txtHGstReason).show();
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
                new DmAlertMessageBox(errorMessage, txtHIdType).show();
                break;
            case 760:
                new DmAlertMessageBox(errorMessage, txtIdNo).show();
                break;
            case 770:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 780:
                if (!txtAddress.isEnabled()) {
                    txtAddress.setEnabled(true);
                }
                if (!txtHCity.isEnabled()) {
                    txtAddress.setEnabled(true);
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<SaleBillReturnProvisional_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<SaleBillReturnProvisional_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<SaleBillReturnProvisional_Mdl> result) {
        SaleBillReturnProvisional_Mdl data;
        clear();
        if (result.isStatus()) {
            data = result.getData();
            previousMdl = new SaleBillReturnProvisional_Mdl(data);
            enableIdType(false);
            txtDate.setEnabled(false);
            txtHDepartment.setEnabled(false);
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtBillNo.setText(data.getSharedKeyFieldName());
            txtBillNo.setTag(DmShared.iNull(data.getRecIdSaleBillReturn().getCounter()).toString());
            txtBillNo.setTag2nd(DmShared.xNull(data.getRecIdSaleBillReturn().getPrefix()));

            txtHVType.setText(DmShared.xNull(data.getVoucherTypeName()));
            txtHVType.setTag(DmShared.xNull(data.getVoucherTypeCode()));

            documentNatureCode = DmShared.xNull(data.getDocumentNatureCode());
            documentTypeCode = DmShared.xNull(data.getDocumentTypeCode());

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

            txtDate.setText(DmShared.dNull(data.getVoucherDate()));
            txtDate.setTag2nd(DmShared.dNull(data.getVoucherDate()));
            txtHType.setText(DmShared.xNull(data.getTypeName()));
            txtHType.setTag(DmShared.xNull(data.getTypeCode()));
            txtRegNo.setText(DmShared.xNull(data.getRegRecID()));
            txtRegNo.setTag(DmShared.xNull(data.getRegDocid()));
            txtHDepartment.setText(DmShared.xNull(data.getDepartmentName()));
            txtHDepartment.setTag(DmShared.xNull(data.getDepartmentCode()));
            txtPatient.setTag2nd(DmShared.xNull(data.getRegSiteCode()));
            roomCode = DmShared.xNull(data.getRoomCode());
            txtPatient.setTag(DmShared.xNull(data.getPatientIntial()));
            txtPatient.setText(DmShared.xNull(data.getPatientName()));
            txtUHIDNo.setText(DmShared.xNull(data.getUhidRecId()));
            txtUHIDNo.setTag(DmShared.xNull(data.getUhidDocid()));
            txtHSaleBillNo.setTag(DmShared.xNull(data.getDocIdSaleBill()));
            txtHSaleBillNo.setText(DmShared.xNull(data.getRecIdSaleBill()));
            txtHConsultant.setText(DmShared.xNull(data.getConsultantName()));
            txtHConsultant.setTag(DmShared.xNull(data.getConsultantCode()));
            txtRemark.setText(DmShared.xNull(data.getRemark()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtIdNo.setText(DmShared.xNull(data.getIdNo()));
            txtHIdType.setTag(DmShared.xNull(data.getIdTypeCode()));
            txtHIdType.setText(DmShared.xNull(data.getIdTypeName()));
            // set the Receipt Details for payment popup in Moverec
            txtReciptNo.setText(data.getRecIdRecipt().getRecIdValue());
            txtReciptNo.setTag(DmShared.iNull(data.getRecIdRecipt().getCounter()).toString());
            txtReciptNo.setTag2nd(DmShared.xNull(data.getRecIdRecipt().getPrefix()));

            txtReciptType.setText(data.getPrecVDescription());
            txtReciptType.setTag(data.getPrecVType());
            txtReciptType.setTag2nd(data.getPrecAttachPaymentMode());

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

            organizationMdl = data.getOrganizationMdl();
            fill_ShowOrgPopUp(organizationMdl, false);

            if (DmShared.vNull(data.getPharmacyBalance()).doubleValue() != 0) {
                lblPharmacyBalance.setText("Pharmacy Balance : " + data.getPharmacyBalance());
            }

//            cardPaymentSaleBillReturn = data.getCardPaymentSaleBillReturn();
//            cardPaymentSaleBillReturn.getTxtHReceiptTypeVList().addAll(defaultValue.getCardPaymentReturn().getTxtHReceiptTypeVList());
            convertedPaymentSource = DmShared.xNull(data.getPaymentSource());
            lblDiscarded.setText(DmShared.iNull(data.getCancelRecord()).intValue() == 1 ? "Discarded" : "");
            lblApproved.setText(DmShared.iNull(data.getApprovedRecord()).intValue() == 1 ? "Approved" : "");
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
            grdMediDetails.getGrid().getStore().clear();
            grdMediDetails.getGrid().getStore().addAll(data.getGrdMediDetails());
            flwSearchRoom.clear();

            resetTaxSummary();

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
        manageDisplay(false);

    }

    private void topControlDelete(String strKey) {
        SrvAsync.invokeRPC().deleteRecord(strKey, modifyReasonMdl, DmShared.xNull(txtRegNo.getTag()), DmShared.xNull(txtHSaleBillNo.getTag()), new AsyncCallback<DmResult<String>>() {

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
        } else if (event.getSource().equals(txtRegNo)) {
            txtRegNo.setText(DmShared.xNull(txtRegNo.getTag2nd()));
//            isExistpendingRequest(txtRegNo.getTag(), txtHDepartment.getTag());
            if (!DmShared.xNull(txtRegNo.getTag()).trim().equalsIgnoreCase("")) {
                isExistpendingRequest(txtRegNo.getTag(), txtHDepartment.getTag(),topControl.getMode(), null);
            }
        } else if (event.getSource().equals(txtUHIDNo)) {
            txtUHIDNo.setText(DmShared.xNull(txtUHIDNo.getTag2nd()));
        } else if (event.getSource() == txtRefNo) {
            enableDisable_RefNo_SaleBillNo();
        } else if (event.getSource().equals(txtPatient)) {
            enableIdType(true);
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
//                        showCardPayment();
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
//            cardPaymentPopup.hide();
            mediDetails.hide();
            uhidPopup.hide();
            roomSearchIPD.hide();
            roomSearchEMRG.hide();
            opdSearch.hide();
        }

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        setDefaultValue();
        lstReturnedMedicine = new ArrayList<>();
    }

    private void onEdit() {
        manageDisplay(true);
        enableIdType(false);
        grdMediDetails.getGrid().getStore().add(new GrdMedicine_Mdl());
        txtRemark.setFocus(true);
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
        txtRegNo.setEnabled(false);
        txtUHIDNo.setEnabled(false);
        txtOrganisation.setEnabled(false);
        txtRefNo.setEnabled(false);
        txtRefDate.setEnabled(false);
        txtHConsultant.setEnabled(false);
        txtDiscount2.setEnabled(false);

//        flwPayment.clear();
        flwPatientSearchTd.clear();

//        manageSaleBillSearch();
        if (widgetName.equalsIgnoreCase(new pmPharmacy().SALE_BILL_RETURN_PROVISIONAL.caption)) {
//            flwPayment.add(btnPayment);
        }
        if (topControl.getMode().equalsIgnoreCase("Edit")) {
            txtDate.setEnabled(dateEditable);
            txtRegNo.setEnabled(false);
            txtPatient.setEnabled(false);
            txtHType.setEnabled(false);
            txtHDepartment.setEnabled(false);
            txtHVType.setEnabled(false);
            txtHSaleBillNo.setEnabled(false);
            txtHConsultant.setEnabled(true);
        } else if (topControl.getMode().equalsIgnoreCase("New")) {
            txtRegNo.setEnabled(true);
            txtHType.setEnabled(true);
            txtHVType.setEnabled(true);
            txtHSaleBillNo.setEnabled(true);
            txtDate.setEnabled(dateEditable);
            txtHDepartment.setEnabled(true);
            if (DmShared.iNull(txtHDepartment.getTag()).longValue() > 0) {
                txtHDepartment.setEnabled(isDepartmentEditable);
            }
            txtHConsultant.setEnabled(true);
            txtRegNo.setEnabled(!DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(""));
//            flwPatientSearchTd.add(flwPatientSearch);
        }

        if (flag) {
            enableDisable_RefNo_SaleBillNo();
        }
        if (topControl.getMode().trim().equalsIgnoreCase("New")) {
            ManageBtnSearchRoom();
        }
        lblSgstUgst.setText(defaultValue.isUnionTerritory() ? "UGST Amount" : "SGST Amount");
    }

    private void ManageBtnSearchRoom() {
        flwSearchRoom.clear();
//        flwSaleBillSearch.clear();
        if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
            flwSearchRoom.add(btnSearchRoom);
//            flwSaleBillSearch.add(btnSaleBillSearch);
        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
            flwSearchRoom.add(btnOPDSearch);
        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("Ledger"))) {
            flwSearchRoom.add(btnLedgerSearch);
        } else {
            flwSearchRoom.clear();
        }

    }

//    private void manageSaleBillSearch() {
//        flwSaleBillSearch.clear();
//        if (topControl.getMode().equalsIgnoreCase("New")) {
//            flwSaleBillSearch.add(btnSaleBillSearch);
//        }
//    }
    private void getUHIDDetail(String recIdUHID, String docIdUHID, String recIdReg, String docIdReg) {
        if (DmShared.dNull(txtDate.getText()).trim().equalsIgnoreCase("")) {
            new DmAlertMessageBox("Voucher Date is mandatory.", txtDate).show();
            return;
        }
        SearchUHID_Mdl searchMdl = new SearchUHID_Mdl();
        searchMdl.setDocIdReg(DmShared.xNull(docIdReg));
        searchMdl.setDocIdSaleBill(DmShared.xNull(txtHSaleBillNo.getTag()));
        searchMdl.setDocIdUHID(DmShared.xNull(docIdUHID));
        searchMdl.setRecIdReg(DmShared.xNull(recIdReg));
        searchMdl.setRecIdUHID(DmShared.xNull(recIdUHID));
        searchMdl.setVoucherCategory(DmShared.xNull(txtHType.getTag()));
        searchMdl.setVoucherDate(DmShared.dNull(txtDate.getText()));
        SrvAsync.invokeRPC().getUHIDDetails(searchMdl, new AsyncCallback<DmResult<SaleBillReturnProvisional_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUHIDNo).show();
            }

            @Override
            public void onSuccess(DmResult<SaleBillReturnProvisional_Mdl> result) {
                if (result.isStatus()) {
                    fillHeader(result);
                    txtUHIDNo.setEnabled(false);
                    txtRegNo.setEnabled(false);
                    txtPatient.setEnabled(false);
                    txtHType.setEnabled(false);
                    txtHGstReason.setFocus(true);
                    flwPatientSearchTd.clear();
                } else {
                    txtUHIDNo.setTag2nd("");
                    txtUHIDNo.setTag("");
                    txtUHIDNo.setText("");
                    txtRegNo.setTag2nd("");
                    txtRegNo.setTag("");
                    txtRegNo.setText("");
//                    flwPatientSearchTd.add(flwPatientSearch);
                    if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
                        new DmAlertMessageBox(result.getErrorMessage(), btnSearchRoom).show();
                    } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
                        new DmAlertMessageBox(result.getErrorMessage(), btnOPDSearch).show();
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), txtUHIDNo).show();
                    }
                }
            }
        });

    }

    private void fillHeader(DmResult<SaleBillReturnProvisional_Mdl> result) {
        SaleBillReturnProvisional_Mdl data;
        if (result.isStatus()) {
            data = result.getData();
            saleOn = data.getSaleMedicine();

            organizationMdl = data.getOrganizationMdl();
            lmCode = DmShared.xNull(data.getLmCode());
            lmName = DmShared.xNull(data.getLmName());
            lmManualCode = DmShared.xNull(data.getLmManualCode());

            fill_ShowOrgPopUp(organizationMdl, false);
//            if (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("")) {
            txtHType.setTag(DmShared.xNull(data.getTypeCode()));
            txtHType.setText(DmShared.xNull(data.getTypeName()));
//            }
            txtPatient.setTag2nd(DmShared.xNull(data.getRegSiteCode()));
            roomCode = DmShared.xNull(data.getRoomCode());
            txtPatient.setTag(DmShared.xNull(data.getPatientIntial()));
            txtPatient.setText(DmShared.xNull(data.getPatientName()));
            txtUHIDNo.setText(DmShared.xNull(data.getUhidRecId()));
            txtUHIDNo.setTag2nd(DmShared.xNull(data.getUhidRecId()));
            txtUHIDNo.setTag(DmShared.xNull(data.getUhidDocid()));
            txtRegNo.setText(DmShared.xNull(data.getRegRecID()));
            txtRegNo.setTag2nd(DmShared.xNull(data.getRegRecID()));
            txtRegNo.setTag(DmShared.xNull(data.getRegDocid()));
            txtHConsultant.setText(DmShared.xNull(data.getConsultantName()));
            txtHConsultant.setTag(DmShared.xNull(data.getConsultantCode()));
            if (DmShared.iNull(data.getDocIdSaleBill()).longValue() > 0) {
                getSaleBillData();
            }

//            if (DmShared.iNull(data.getRegDocid()).longValue() > 0) {
//                getRegistrationBalance(data.getRegDocid());
//            }
            if (DmShared.iNull(data.getLmCode()).longValue() > 0) {
                getPharmacyBalance();
            }

            txtHIdType.setTag(DmShared.iNull(data.getIdTypeCode()).toString());
            txtHIdType.setText(DmShared.xNull(data.getIdTypeName()));
            txtIdNo.setText(DmShared.xNull(data.getIdNo()));
            txtMobile.setText(DmShared.xNull(data.getMobile()));
            txtAddress.setText(DmShared.xNull(data.getAddress()));
            txtHCity.setTag(DmShared.xNull(data.getCityCode()));
            txtHCity.setText(DmShared.xNull(data.getCityName()));

            isExistpendingRequest(txtRegNo.getTag(), txtHDepartment.getTag(),topControl.getMode(), null);
//            showInvoicedMedicineDetails(txtRegNo.getTag(), lstReturnedMedicine);
            enableIdType(false);
            flwSearchRoom.clear();

            txtHDepartment.setFocus(true);
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
        double discount = 0, itemGrossAmt = 0, roundoff = 0, netAmount = 0, cgstAmt = 0, sgstAmt = 0,
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
                DmClient.vNull(sgstAmt, "0.00"), DmClient.vNull(billAmt, "0.00"), DmClient.vNull(roundoff, "0.00"), DmClient.vNull(netAmount, "0.00"), DmClient.vNull(discount2, "0.00"));
        calculateLandedValue();
        resetTaxSummary();
    }

    private void fillItemGrossandDiscount(String ItemGrossAmt, String Discount, String taxableAmt, String cgstAmt, String sgstAmt, String billAmt, String roundoff, String netAmt, String discount2) {
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

    private void clear() {
        previousMdl = null;
        landedValue = 0;
        flwPatientSearchTd.clear();

        grdMediDetails.getGrid().getStore().clear();
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
//        cardPaymentSaleBillReturn = new CardPaymentPopup_Mdl();
//        cardPaymentSaleBillReturn = defaultValue.getCardPaymentReturn().clone();

        lblPharmacyBalance.setText("");
//        lblRegistrationBalance.setText("");

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

        txtRemark.setText("");
        txtRemark.setTag("");
        txtRemark.setTag2nd("");
        txtReciptNo.setText("");
        txtReciptType.setText("");
        txtReciptType.setTag("");
        txtReciptType.setTag2nd("");
        txtHConsultant.setText("");
        txtHConsultant.setTag("");
        txtHConsultant.setTag2nd("");
        txtUHIDNo.setTag2nd("");
        txtUHIDNo.setTag("");
        txtUHIDNo.setText("");
        txtRegNo.setTag2nd("");
        txtRegNo.setTag("");
        txtRegNo.setText("");
        txtHType.setTag("");
        txtHType.setText("");
        billDocId = "";
        nature = "";
        roomCode = "";

        sbNetAmt = "";
        sbV_Type = "";

        lblDiscarded.setText("");
        organizationMdl = null;

        saleOn = defaultValue.getMedicineSaleOn();
        partyType = defaultValue.getPartyType();
        documentTypeCode = "";
        documentNatureCode = "";
        userEntDtLatestLine = "";

        lmCode = "";
        lmName = "";
        lmManualCode = "";
        subCode = "";
        partyManualCode = "";
        placeOfSupply = defaultValue.getStateCode();
        flagCardPayment = false;
        //  List<List<OverHeadLine_Mdl>> lstOverheadLine = new ArrayList<List<OverHeadLine_Mdl>>();
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
                        getUHIDDetail("", "", txtRegNo.getText(), "");
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
                    getUHIDDetail(txtUHIDNo.getText(), "", "", "");
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

    private void fillV_TypeDetail(TxtHVType_Mdl txtHVType_Mdl) {
        txtHVType.setTag(txtHVType_Mdl.getSharedKeyFieldCode());
        txtHVType.setText(txtHVType_Mdl.getSharedKeyFieldName());
        documentTypeCode = DmShared.xNull(txtHVType_Mdl.getDocumentTypeCode());
        documentNatureCode = DmShared.xNull(txtHVType_Mdl.getDocumentNatureCode());
    }

//    private void showCardPayment() {
//        boolean isTPA = false;
//
//        flagCardPayment = true;
//
//        CardPaymentPopup_Mdl cardPaymentMdl = new CardPaymentPopup_Mdl();
////        cardPaymentMdl = this.cardPaymentSaleBillReturn.clone();
//        cardPaymentMdl.setAmount(DmClient.vNull(DmShared.vNull(0 - DmShared.vNull(txtNetAmount.getText()).doubleValue()), "0.00"));
//        cardPaymentMdl.setPatientName(DmShared.xNull(txtPatient.getText()));
//        cardPaymentMdl.setUHIDNo(DmShared.xNull(txtUHIDNo.getText()));
//        cardPaymentMdl.setUHIDDocid(DmShared.xNull(txtUHIDNo.getTag()));
//        cardPaymentMdl.setRegNo(DmShared.xNull(txtRegNo.getText()));
//        cardPaymentMdl.setRegNoDocid(DmShared.xNull(txtRegNo.getTag()));
//        cardPaymentMdl.setReceiptNo(DmShared.xNull(txtReciptNo.getText()));
//        cardPaymentMdl.setReceiptTypeCode(DmShared.xNull(txtReciptType.getTag()));
//        cardPaymentMdl.setReceiptTypeName(DmShared.xNull(txtReciptType.getText()));
//        cardPaymentMdl.setReceiptTypePM(DmShared.xNull(txtReciptType.getTag2nd()));
//        if (organizationMdl != null) {
//            cardPaymentMdl.setCTOCardType(organizationMdl.getSharedKeyFieldName());
//            cardPaymentMdl.setCTOCardNo(organizationMdl.getPolicyNo());
//            cardPaymentMdl.setCTOCardHolderName(organizationMdl.getHolderName());
//            cardPaymentMdl.setCTORelation(organizationMdl.getRelationshipName());
//            cardPaymentMdl.setCTORankNo(organizationMdl.getRankNo());
//            isTPA = organizationMdl.isTpa();
//        }
//
//        if (((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))
//                || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE)))) {
//            cardPaymentPopupSaleBillReturn.setCTOAllowed(false);
//            cardPaymentPopupSaleBillReturn.setCTRAllowed(ctrAllow);
//        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("LEDGER")) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase("WALK-IN"))) {
//            cardPaymentPopupSaleBillReturn.setCTOAllowed(false);
//            cardPaymentPopupSaleBillReturn.setCTRAllowed(false);
//        } else {
//            cardPaymentPopupSaleBillReturn.setCTOAllowed(ctoAllow);
//            cardPaymentPopupSaleBillReturn.setCTRAllowed(false);
//        }
//
//        cardPaymentPopupSaleBillReturn.setCTPAllowed(ctpAllow);
//        cardPaymentPopupSaleBillReturn.setReceiptAllowed(receiptAllow);
//
//        if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(""))) {
//            cardPaymentPopupSaleBillReturn.setDefaultPaymentSource(isTPA ? STEnum.PaymentSource.CREDIT_TO_ORGANIZATION : STEnum.PaymentSource.PAYMENT);
//        } else if ((DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) || (DmShared.xNull(txtHType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
//            cardPaymentPopupSaleBillReturn.setDefaultPaymentSource(isTPA ? STEnum.PaymentSource.CREDIT_TO_REGISTRATION : STEnum.PaymentSource.PAYMENT);
//        } else {
//            cardPaymentPopupSaleBillReturn.setDefaultPaymentSource(STEnum.PaymentSource.PAYMENT);
//        }
//
//        if (convertedPaymentSource != null && !DmShared.xNull(convertedPaymentSource).trim().equalsIgnoreCase("")) {
//            cardPaymentMdl.setRegPaymentSource(STEnum.PaymentSource.getValueOf(DmShared.iNull(convertedPaymentSource).intValue()));
//        }
//
//        if (topControl.getMode().equalsIgnoreCase("Browse")) {
//            cardPaymentPopupSaleBillReturn.show(cardPaymentMdl, DmShared.xNull(topControl.getMode()));
//        } else {
//            try {
//                cardPaymentPopupSaleBillReturn.show(cardPaymentMdl, DmShared.xNull(topControl.getMode()));
//            } catch (DmException e) {
//                if (topControl.getMode().equalsIgnoreCase("Browse")) {
//                    new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
//                } else {
//                    topControlSaveError(e.getErrorNo(), e.getErrorSource(), e.getErrorSourceCode(), e.getMessage());
//                }
//            }
//        }
//
//    }
    private void fill_ShowOrgPopUp(Organization_Mdl orgMdl, boolean isButtonClick) {
        txtOrganisation.setText("");
        txtOrganisation.setTag("");
        txtOrganisation.setTag2nd("");
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

    private void calculateGrid() {
        for (GrdMedicine_Mdl mdl : grdMediDetails.getGrid().getStore().getAll()) {
            if (DmShared.iNull(mdl.getSharedKeyFieldCode()).longValue() > 0) {
                double conversionRate = 1, itemValue = 0, totalDiscount = 0, totalTaxRate = 0, taxableAmt = 0, saleSkuQty = 0, freeSkuQty = 0, totalSkuQty = 0, totalTaxAmt = 0;
                int saleUnitDecimalPlaces = 4, skuDecimalPlaces = 4;

                if (mdl.getMedicineMdl() != null) {
                    saleUnitDecimalPlaces = mdl.getMedicineMdl().getAllowDecimalPlaces();
                    skuDecimalPlaces = mdl.getMedicineMdl().getAllowSkuDecimalPlaces();
                    conversionRate = DmShared.vNull(mdl.getMedicineMdl().getConversionRate()).doubleValue();
                }

                saleSkuQty = DmShared.vNull(DmClient.vNull((DmShared.vNull(mdl.getQuantity()).doubleValue() * conversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();
                freeSkuQty = DmShared.vNull(DmClient.vNull((DmShared.vNull(mdl.getFreeQty()).doubleValue() * conversionRate), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();
                totalSkuQty = DmShared.vNull(DmClient.vNull((saleSkuQty + freeSkuQty), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue()))).doubleValue();

                mdl.setQuantity(DmClient.vNull(mdl.getQuantity(), DmShared.getNumberFormat(DmShared.iNull(saleUnitDecimalPlaces).intValue())));

                mdl.setQuantitySKU(DmClient.vNull(DmShared.vNull(totalSkuQty), DmShared.getNumberFormat(DmShared.iNull(skuDecimalPlaces).intValue())));

                mdl.setAmount(DmClient.vNull((DmShared.vNull(saleSkuQty).doubleValue() * DmShared.vNull(mdl.getSkuRate()).doubleValue()), "0.00"));
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
        }
        calculate();
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

    private void showInvoicedMedicineDetails(String regDocID, List<KeyFieldExtended> lstReturnedMedicine, final GrdMedicine_Mdl grdMedicineMdl) {
        returnMedicineBatchDetail = new ReturnMedicineBatchDetail(regDocID);
        if (topControl.getMode().trim().equalsIgnoreCase("Browse")) {
            returnMedicineBatchDetail.show(regDocID, DmShared.iNull(txtHDepartment.getTag()).toString(), lstReturnedMedicine, grdMedicineMdl);
        } else {
            returnMedicineBatchDetail.show(regDocID, DmShared.iNull(txtHDepartment.getTag()).toString(), lstReturnedMedicine, grdMedicineMdl);
        }
        returnMedicineBatchDetail.addCallBack(invoicedMedicineCallBack);
    }

    private void fillInvoicedMedicineDetails(List<KeyFieldExtended> listKF, boolean flagRemove) {
        boolean isItemExist = false;
        int itemIndex = 0;
        final KeyFieldExtended field = new KeyFieldExtended();
        field.setSharedKeyFieldCode(DmShared.xNull(listKF.get(0).getSharedKeyFieldCode()));
        field.setSharedKeyFieldName(DmShared.xNull(listKF.get(0).getSharedKeyFieldName()));
        field.setSharedModifiedBy(DmShared.xNull(listKF.get(0).getSharedModifiedBy()));

        if (flagRemove == false && lstReturnedMedicine.size() == 0) {
            for (int i = 0; i < lstReturnedMedicine.size(); i++) {
                KeyFieldExtended currRecord = lstReturnedMedicine.get(i);
                if ((DmShared.xNull(currRecord.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(listKF.get(0).getSharedKeyFieldCode()).trim()))
                        && (DmShared.xNull(currRecord.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(listKF.get(0).getSharedKeyFieldName()).trim()))) {
                    itemIndex = i;
                    isItemExist = true;
                    break;
                } 
            }
            if (isItemExist && flagRemove) {
                lstReturnedMedicine.remove(itemIndex);
                lstReturnedMedicine.add(field);
            } else if (flagRemove == false) {
                lstReturnedMedicine.remove(itemIndex);
            } else {
                lstReturnedMedicine.add(field);
            }

        }

        if (lstReturnedMedicine.size() != 0) {
            for (int i = 0; i < lstReturnedMedicine.size(); i++) {
                KeyFieldExtended currRecord = lstReturnedMedicine.get(i);
                if ((DmShared.xNull(currRecord.getSharedKeyFieldCode()).trim().equalsIgnoreCase(DmShared.xNull(listKF.get(0).getSharedKeyFieldCode()).trim()))
                        && (DmShared.xNull(currRecord.getSharedKeyFieldName()).trim().equalsIgnoreCase(DmShared.xNull(listKF.get(0).getSharedKeyFieldName()).trim()))) {
                    itemIndex = i;
                    isItemExist = true;
                    break;
                } 
            }
            if (isItemExist && flagRemove) {
                lstReturnedMedicine.remove(itemIndex);
                lstReturnedMedicine.add(field);
            } else if (flagRemove == false) {
                lstReturnedMedicine.remove(itemIndex);
            } else {
                if (!DmShared.xNull(listKF.get(0).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(listKF.get(0).getSharedKeyFieldName()).trim().equalsIgnoreCase("")){
                lstReturnedMedicine.add(field);
            }
            }
        } else {
            if (!DmShared.xNull(listKF.get(0).getSharedKeyFieldCode()).trim().equalsIgnoreCase("")
                    && !DmShared.xNull(listKF.get(0).getSharedKeyFieldName()).trim().equalsIgnoreCase("")){
                lstReturnedMedicine.add(field);
            }
        }
        SrvAsync.invokeRPC().getInvoicedItemDetails(txtRegNo.getTag(), DmShared.iNull(txtHDepartment.getTag()).toString(), lstReturnedMedicine, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtRemark).show();
            }

            @Override
            public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    fillSaleBillDetails(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHVType).show();
                }
            }
        });
    }

    private void isExistpendingRequest(String regDocID, String departmentCode,String entryMode, final GrdMedicine_Mdl grdMedicineMdl) {
        SrvAsync.invokeRPC().getIsExistpendingRequest(regDocID, departmentCode,entryMode, new AsyncCallback<DmResult<Boolean>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<Boolean> result) {
                if (!result.isStatus()) {
                    showInvoicedMedicineDetails(txtRegNo.getTag(), lstReturnedMedicine, grdMedicineMdl);
                } else {
                    new DmAlertMessageBox("Already a return request is pending for approval. Can't be generate new return request", (DmTextBox) null).show();
                }
            }
        });
    }
}
