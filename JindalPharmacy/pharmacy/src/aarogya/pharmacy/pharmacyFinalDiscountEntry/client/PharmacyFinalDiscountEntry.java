package aarogya.pharmacy.pharmacyFinalDiscountEntry.client;

import aarogya.pharmacy.walkInSearch.client.WalkInSearch;
import aarogya.pharmacy.ledgerMaster.shared.LedgerMaster_Mdl;
import aarogya.pharmacy.partySearch.client.PartySearch;
import aarogya.pharmacy.partySearch.shared.PartySearch_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.DefaultValue_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.Find_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.GrdDiscountDetails_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.HSN_TAX_WISE_PopUp_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.PharmacyFinalDiscountEntry_Mdl;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.PharmacyFinalDiscountEntry_Vld;
import aarogya.projectMenu.self.shared.ProjectVoucherCategory;
import aarogya.selfToolkit.modifyReason.client.ModifyReason;
import aarogya.selfToolkit.modifyReason.shared.ModifyReason_Mdl;
import aarogya.selfToolkit.opdSearch.client.OPDSearch;
import aarogya.selfToolkit.patientSearchRoomWise.client.PatientSearchRoomWise;
import aarogya.selfToolkit.patientSearchRoomWise.shared.SearchValueRoomWise_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import java.util.List;
import aarogya.pharmacy.pharmacyFinalDiscountEntry.shared.*;
import aarogya.projectMenu.self.shared.pmDayCare;
import aarogya.projectMenu.self.shared.pmOPD;
import aarogya.selfToolkit.opdSearch.shared.SearchValue_Mdl;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmReportProperty_Mdl;
import dataman.dmBase.self.shared.FieldUDT_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.openSourceReport.client.OpenSourceReport;
import java.util.ArrayList;
import java.util.HashMap;

public class PharmacyFinalDiscountEntry extends PharmacyFinalDiscountEntry_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents, ClickHandler, KeyPressHandler {

    private final ServiceInvoker<PharmacyFinalDiscountEntry_SrvAsync> srvAsync;
    private final ModifyReason modifyReason = new ModifyReason();
    private DefaultValue_Mdl defaultValue;
    private ModifyReason_Mdl modifyReasonMdl;
    private boolean dateEditable = false;

    private final ProjectVoucherCategory pvc = new ProjectVoucherCategory();
    private final OPDSearch opdSearch = new OPDSearch();
    private final PatientSearchRoomWise roomSearchIPD = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.IPD_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private final PatientSearchRoomWise roomSearchEMRG = new PatientSearchRoomWise(SearchValueRoomWise_Mdl.FilterType.NOT_DISCHARGED, SearchValueRoomWise_Mdl.FilterType.EMERGENCY_ONLY, SearchValueRoomWise_Mdl.FilterType.ALLOW_SITE_FOR_PHARMACY);
    private PatientSearchRoomWise.CallBack roomSearchCallBack;
    private final PartySearch ledgerSearch = new PartySearch();
    private final WalkInSearch walkInSearch = new WalkInSearch();
    private HSN_TAX_WISE_PopUp hsnTaxWisePopup = new HSN_TAX_WISE_PopUp();
    private List<GrdDiscountDetails_Mdl> lstGrdDetails = new ArrayList<>();
    private double totalNetAmtToSave = 0, totalTaxableAmountToSave = 0, totalSgstAmt = 0, totalCgstAmt = 0, totalUgstAmt = 0;
    private String regDocId = "", uhidDocId = "";
    boolean opdAllow = false, ipdAllow = false, dayCareAllow = false, emergencyAllow = false, walkInAllow = false, ledgerAllow = false;

    public PharmacyFinalDiscountEntry(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(PharmacyFinalDiscountEntry_Srv.class), PharmacyFinalDiscountEntry.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        handlerEvents();
        iniDefaultValues(swi);
    }

    private void iniDefaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC(true).getDefaultValues(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    defaultValue = result.getData();
                    txtDisplayName.setTag2nd(defaultValue.getPartyType());
                    flwAdjustParent.clear();
                    manageDisplay(false);
                    swi.setWidget(PharmacyFinalDiscountEntry.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void handlerEvents() {
        flwForm.addAttachHandler(this);
        btnOPDSearch.addClickHandler(this);
        btnIPDSearch.addClickHandler(this);
        btnLedgerSearch.addClickHandler(this);
        btnAdjust.addClickHandler(this);
        btnWalkInSearch.addClickHandler(this);
        txtUniqueId.addKeyPressHandler(this);
        txtUniqueId.addBlurHandler(this);
        txtDiscountPer.addBlurHandler(this);
        txtVoucherDate.addBlurHandler(this);
        txtAmount.addBlurHandler(this);
        roomSearchEMRG.addCallBack(roomSearchCallBack);
        roomSearchIPD.addCallBack(roomSearchCallBack);
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());
        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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
                if (blnStatus && tRow != null) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PharmacyFinalDiscountEntry_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PharmacyFinalDiscountEntry_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PharmacyFinalDiscountEntry_Mdl> result) {
        clear();
        if (result.isStatus()) {
            PharmacyFinalDiscountEntry_Mdl data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtVoucherDate.setText(data.getVoucherDate());
            txtVoucherDate.setTag(data.getUserEntDtLatestLine());
            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            txtUniqueId.setTag(data.getLmCode());
            txtUniqueId.setTag2nd(data.getManualCode());
            txtUniqueId.setText(data.getManualCode());
            txtDiscount.setText(data.getNetAmtToSave());
            txtDiscount.setTag(data.getTaxableAmountToSave());
            txtDiscountPer.setText("");

            totalNetAmtToSave = DmShared.vNull(data.getNetAmtToSave()).doubleValue();
            totalTaxableAmountToSave = DmShared.vNull(data.getTaxableAmountToSave()).doubleValue();
            totalSgstAmt = DmShared.vNull(data.getSgstAmt()).doubleValue();
            totalCgstAmt = DmShared.vNull(data.getCgstAmt()).doubleValue();
            totalUgstAmt = DmShared.vNull(data.getUgstAmt()).doubleValue();

            txtHDisReason.setText(data.getRemark());

            txtHCustomerType.setTag(data.getCustomerTypeCode());
            txtHCustomerType.setTag2nd(data.getCustomerTypeCode());
            txtHCustomerType.setText(data.getCustomerTypeName());

            //Display Only fileds
            txtDisplayName.setTag2nd(data.getPartyType());
            txtDisplayName.setText(data.getDisplayName());
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
                txtDisplayName.setTag(data.getSubCode());
            }
            regDocId = data.getRegDocId();
            uhidDocId = data.getUhidDocId();
            txtIdType.setText(data.getIdTypeName());
            txtIdType.setTag(data.getIdTypeCode());
            txtIdNo.setText(data.getIdNo());
            txtAddress.setText(data.getAddress());
            txtCity.setText(data.getCityName());
            txtCity.setTag(data.getCityCode());
            txtCity.setTag2nd(data.getPlaceOfSupply());
            txtMobile.setText(data.getMobile());
            lblPharmacyBalance.setText(DmShared.vNull(data.getPharmacyBalance()) != 0 ? "Pharmacy Balance : " + data.getPharmacyBalance() : "");

            txtAmount.setText(data.getPharmacyBalance());
            txtAmount.setTag(data.getPharmacyBalance());
            // set the Receipt Details for payment popup in Moverec
            txtVoucherNo.setText(DmShared.xNull(data.getRecId().getRecIdValue()));
            txtVoucherNo.setTag(DmShared.xNull(data.getRecId().getPrefix()));
            txtVoucherNo.setTag2nd(DmShared.iNull(data.getRecId().getCounter()).toString());
            txtVoucherType.setText(data.getVoucherTypeName());
            txtVoucherType.setTag(data.getVoucherTypeCode());
            txtReceiptType.setText(data.getRecptVoucherTypeName());
            txtReceiptType.setTag(data.getRecptVoucherTypeCode());
            txtReceiptType.setTag2nd(data.getRecptAttachedPaymentMode());

            grdDiscountDetail.getGrid().getStore().clear();
            grdDiscountDetail.getGrid().getStore().addAll(data.getGrdDiscountDetails());
            if (DmShared.iNull(data.getIsCancel()).intValue() == 1) {
                cancelBy.setText("Discarded");
            } else {
                cancelBy.setText("");
            }
            flwSearchBtn.clear();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        topControl.setDisplay(true);
        manageDisplay(false);
    }

    private void fillV_TypeDetail(TxtHVType_Mdl txtHVType_Mdl) {
        txtVoucherType.setTag(txtHVType_Mdl.getSharedKeyFieldCode());
        txtVoucherType.setText(txtHVType_Mdl.getSharedKeyFieldName());
    }

    private void clear() {
        txtHCustomerType.setText("");
        txtHCustomerType.setTag("");
        txtHCustomerType.setTag2nd("");

        txtVoucherDate.setText("");
        txtVoucherDate.setTag("");
        txtVoucherDate.setTag2nd("");

        totalNetAmtToSave = 0;
        totalTaxableAmountToSave = 0;
        totalSgstAmt = 0;
        totalCgstAmt = 0;
        totalUgstAmt = 0;

        txtDisplayName.setTag2nd(defaultValue.getPartyType());

        grdDiscountDetail.getGrid().getStore().clear();
        resetTxtHCoustomerType();
    }

    private void manageDisplay(boolean flag) {
        txtUniqueId.setEnabled(flag);
        txtReceiptType.setEnabled(false);
        txtVoucherNo.setEnabled(false);
        txtVoucherDate.setEnabled(false);
        txtDisplayName.setEnabled(false);
        txtIdType.setEnabled(false);
        txtIdNo.setEnabled(false);
        txtAddress.setEnabled(false);
        txtCity.setEnabled(false);
        txtMobile.setEnabled(false);
        txtDiscount.setEnabled(false);
        txtDiscountPer.setEnabled(false);
        flwSearchBtn.clear();
        flwAdjustParent.clear();
        if (topControl.getMode().trim().equalsIgnoreCase("New")) {
            manageBtnSearchRoom();
            flwAdjustParent.add(flwAdjustChild);
            txtDiscount.setEnabled(true);
            txtDiscountPer.setEnabled(true);
            txtVoucherDate.setEnabled(dateEditable);
        } else if (topControl.getMode().trim().equalsIgnoreCase("Edit")) {
            txtHCustomerType.setEnabled(false);
            txtUniqueId.setEnabled(false);
            txtVoucherDate.setEnabled(dateEditable);
        }
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setOtherRequired(false);
        topControl.setRefreshRequired(false);
        topControl.setDeleteCaptionToDiscard();

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(dateEditableBlog)) {
                dateEditable = true;
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
        topControl.setUserPermission(menuPermission, new VoucherPermissionHandler(DmClient.getVoucherPermission(new String[]{new ProjectVoucherCategory().PHARMACY_BILL_FINAL_DISCOUNT})) {
            @Override
            public String getVoucherType() {
                return defaultValue.getVoucherTypeMdl().getSharedKeyFieldCode();
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

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                if (!DmShared.xNull(cancelBy.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("This Record has been discarded.You can't edit this record.", (DmTextBox) null).show();
                    topControl.btnClick("");
                    return;
                }
                manageDisplay(true);
                txtDiscount.setFocus(true);
            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
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
            public void moveRec(String fetchCode) {
                topControlMoveRec(fetchCode);
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
                if (topControl.getMode().equalsIgnoreCase("Edit")) {
                    showModifyReason();
                } else {
                    topControlSave();
                }
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PharmacyFinalDiscountEntry.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void prnRecord(boolean printToPrinter) {
        HashMap<String, FieldUDT_Mdl> hashMapFilter = new HashMap<>();
        hashMapFilter.put("docId", new FieldUDT_Mdl("Voucher No.", DmShared.xNull(txtVoucherNo.getText()), DmShared.xNull(topControl.getDocId())));
        new OpenSourceReport(DmShared.xNull(txtVoucherType.getTag()), "", "Pharmacy Final Disount", widgetName, hashMapFilter, printToPrinter, new OpenSourceReport.CallBack() {
            @Override
            public DmReportProperty_Mdl setReportProperty(DmReportProperty_Mdl reportProperty) {
                DmClient.localSettingPrintMap(reportProperty, moduleName, widgetName, "Print");
                return reportProperty;
            }
        }, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(topControl.getPermissions(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                if (result.isStatus()) {
                    topControl.setNavigator(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void textHelpEvents() {
        opdSearch.addCallBack(new OPDSearch.CallBack() {

            @Override
            public void ok(String regRecId, String regDocId) {
                getCustomerDetails(regRecId);
            }

            @Override
            public void close() {
                txtUniqueId.setFocus(true);
            }
        });
        roomSearchCallBack = new PatientSearchRoomWise.CallBack() {
            @Override
            public void ok(String regDocId, String regRecId, String roomCode) {
                getCustomerDetails(regRecId);
            }

            @Override
            public void close() {
                txtUniqueId.setFocus(true);
            }
        };
        walkInSearch.addCallBack(new WalkInSearch.CallBack() {
            @Override
            public void ok(String lmCode, String lmManualCode) {
                getCustomerDetails(lmManualCode);
            }

            @Override
            public void close() {
                txtUniqueId.setFocus(true);
            }

        });
        ledgerSearch.addCallBack(new PartySearch.CallBack() {
            @Override
            public void ok(PartySearch_Mdl partySearchDetails) {
                getCustomerDetails(partySearchDetails.getManualCode());
            }

            @Override
            public void close(boolean isErrorClose) {
                txtUniqueId.setFocus(true);
            }
        });
        txtHCustomerType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCustomerType(helper, blnToEndOfFile, opdAllow, ipdAllow, dayCareAllow, emergencyAllow, walkInAllow, ledgerAllow, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCustomerType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCustomerType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCustomerType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (!DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(DmShared.xNull(txtHCustomerType.getTag2nd()))) {
                    resetTxtHCoustomerType();
                    txtHCustomerType.setTag2nd(DmShared.xNull(txtHCustomerType.getTag()));
                }
                flwSearchBtn.clear();
                if (t1Row != null && blnStatus) {
                    manageBtnSearchRoom();
                }
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        if (!DmShared.xNull(txtHCustomerType.getTag()).trim().equalsIgnoreCase("")) {
                            txtUniqueId.setFocus(true);
                        }
                    }
                });
            }
        });
        hsnTaxWisePopup.addCallBack(new HSN_TAX_WISE_PopUp.CallBack() {
            @Override
            public void ok(List<HSN_TAX_WISE_PopUp_Mdl> listDetails, String overAllDiscountAmt) {
                if (!listDetails.isEmpty()) {
                    GrdDiscountDetails_Mdl mdlDetails;
                    lstGrdDetails = new ArrayList<>();
                    totalNetAmtToSave = 0;
                    totalTaxableAmountToSave = 0;
                    totalSgstAmt = 0;
                    totalCgstAmt = 0;
                    totalUgstAmt = 0;
                    for (HSN_TAX_WISE_PopUp_Mdl data : listDetails) {
                        mdlDetails = new GrdDiscountDetails_Mdl();
                        mdlDetails.setSharedKeyFieldCode(DmShared.xNull(defaultValue.getItemCode()));
                        mdlDetails.setSharedKeyFieldName(DmShared.xNull(defaultValue.getItemName()));
                        mdlDetails.setGstUnitCode(DmShared.xNull(defaultValue.getGstUnitCode()));
                        mdlDetails.setSaleUnitCode(DmShared.xNull(defaultValue.getSaleUnitCode()));
                        mdlDetails.setItemGSTNature(DmShared.xNull(data.getItemGSTNature()));
                        mdlDetails.setGstPer(DmClient.vNull(DmShared.vNull(data.getGstPer()), "0.00"));
                        mdlDetails.setHsnCode(DmShared.xNull(data.getHsnCode()));
                        mdlDetails.setHsnName(DmShared.xNull(data.getHsnName()));
                        mdlDetails.setCgstPer(DmShared.xNull(data.getCgstPer()));
                        mdlDetails.setSgstPer(DmShared.xNull(data.getSgstPer()));
                        mdlDetails.setUgstPer(DmShared.xNull(data.getUgstPer()));
                        mdlDetails.setIgstPer(DmShared.xNull(data.getIgstPer()));
                        mdlDetails.setCgstAcCode(DmShared.xNull(data.getCgstAcCode()));
                        mdlDetails.setSgstAcCode(DmShared.xNull(data.getSgstAcCode()));
                        mdlDetails.setUgstAcCode(DmShared.xNull(data.getUgstAcCode()));
                        mdlDetails.setSaleAcCode(DmShared.xNull(defaultValue.getSalesAcCode()));
                        mdlDetails.setDisAmt(DmClient.vNull(DmShared.vNull(data.getDisAmt()), "0.00"));
                        mdlDetails.setDisPer(DmClient.vNull(DmShared.vNull(data.getDisPer()), "0.00"));
                        mdlDetails.setQty("-1");
                        calculate(mdlDetails);
                        lstGrdDetails.add(mdlDetails);
                    }

                    txtDiscount.setText(DmClient.vNull(DmShared.vNull(totalNetAmtToSave), "0.00"));
                    txtDiscount.setTag(DmClient.vNull(DmShared.vNull(totalTaxableAmountToSave), "0.00"));
                    txtDiscountPer.setText("");
                    grdDiscountDetail.getGrid().getStore().clear();
                    grdDiscountDetail.getGrid().getStore().addAll(lstGrdDetails);
                    grdDiscountDetail.commitChanges();

                    txtHDisReason.setFocus(true);
                }
            }

            @Override
            public void close() {
                txtHDisReason.setFocus(true);
            }
        });
        txtHDisReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDiscountReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDisReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDisReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDisReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    }

    private void topControlSaveError(int errorNo, String errorSource, String errorSourceCode, String errorMessage) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtHCustomerType).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtHDisReason).show();
                break;
            case 500:
                int indexTemp = DmShared.iNull(errorSourceCode).intValue();
                final int gridRowIndex = indexTemp;
                new DmAlertMessageBox(errorMessage, new HideEvent.HideHandler() {
                    @Override
                    public void onHide(HideEvent event) {
                        ((CellSelectionModel) grdDiscountDetail.getGrid().getSelectionModel()).selectCell(gridRowIndex, 1);
                    }
                }).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHDisReason).show();
        }

    }

    private void calculate(GrdDiscountDetails_Mdl detail) {

        double netValue = 0, taxableAmt = 0, taxPer = 0, cgstAmt = 0, sgstAmt = 0, ugstAmt = 0, totalTaxAmt = 0;
        netValue = 0 - DmShared.vNull(detail.getDisAmt()).doubleValue();
        taxPer = DmShared.vNull(detail.getGstPer()).doubleValue();

        taxableAmt = (netValue * 100) / (100 + taxPer);
        cgstAmt = DmShared.vNull(DmClient.vNull((DmShared.vNull(detail.getCgstPer()).doubleValue() * taxableAmt * 0.01), "0.00")).doubleValue();
        sgstAmt = DmShared.vNull(DmClient.vNull((DmShared.vNull(detail.getSgstPer()).doubleValue() * taxableAmt * 0.01), "0.00")).doubleValue();
        ugstAmt = DmShared.vNull(DmClient.vNull((DmShared.vNull(detail.getUgstPer()).doubleValue() * taxableAmt * 0.01), "0.00")).doubleValue();

        totalCgstAmt += cgstAmt;
        totalSgstAmt += sgstAmt;
        totalUgstAmt += ugstAmt;
        totalNetAmtToSave += netValue;
        totalTaxableAmountToSave += taxableAmt;

        detail.setNetValue(DmClient.vNull(DmShared.vNull(netValue), "0.00"));
        detail.setTaxableValue(DmClient.vNull(DmShared.vNull(taxableAmt), "0.00"));
        detail.setCgstAmt(DmClient.vNull(DmShared.vNull(cgstAmt), "0.00"));
        detail.setSgstAmt(DmClient.vNull(DmShared.vNull(sgstAmt), "0.00"));
        detail.setUgstAmt(DmClient.vNull(DmShared.vNull(ugstAmt), "0.00"));
        detail.setRate(DmClient.vNull(Math.abs(netValue), "0.00"));
        detail.setAmount(DmClient.vNull(DmShared.vNull(netValue), "0.00"));

        totalTaxAmt = DmShared.vNull(detail.getCgstAmt()).doubleValue() + DmShared.vNull(detail.getSgstAmt()).doubleValue() + DmShared.vNull(detail.getUgstAmt()).doubleValue();
        detail.setGstAmt(DmClient.vNull(DmShared.vNull(totalTaxAmt), "0.00"));

        //for managing minor 'paisa' differences in amount.
        if (DmShared.vNull(detail.getNetValue()).doubleValue() != (DmShared.vNull(detail.getTaxableValue()).doubleValue() + totalTaxAmt)) {
            taxableAmt = 0 - (Math.abs(DmShared.vNull(detail.getTaxableValue()).doubleValue()) + (Math.abs(DmShared.vNull(detail.getNetValue()).doubleValue()) - Math.abs(DmShared.vNull(detail.getTaxableValue()).doubleValue() + totalTaxAmt)));
            detail.setTaxableValue(DmClient.vNull(taxableAmt, "0.00"));
        }

    }

    private void onAdd() {
        clear();
        manageDisplay(true);
        if (defaultValue.getVoucherTypeMdl() != null) {
            fillV_TypeDetail(defaultValue.getVoucherTypeMdl());
        }
        txtHCustomerType.setFocus(true);
        txtVoucherDate.setText(DmShared.dNull(defaultValue.getCurrentDate()));
    }

    private PharmacyFinalDiscountEntry_Mdl topControlSaveInitialize() {
        PharmacyFinalDiscountEntry_Mdl mrTemp = new PharmacyFinalDiscountEntry_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
            mrTemp.getRecId().setRecIdValue(DmShared.xNull(txtVoucherNo.getText()));
            mrTemp.getRecId().setPrefix(DmShared.xNull(txtVoucherNo.getTag()));
            mrTemp.getRecId().setCounter(DmShared.iNull(txtVoucherNo.getTag2nd()).longValue());
        }

        mrTemp.setVoucherTypeCode(DmShared.xNull(txtVoucherType.getTag()));
        mrTemp.setVoucherTypeName(DmShared.xNull(txtVoucherType.getText()));

        mrTemp.setSharedPreparedBy(userControl.getTextBlog("Prepared By"));
        mrTemp.setVoucherDate(DmShared.dNull(txtVoucherDate.getText()));
        mrTemp.setUserEntDtLatestLine(DmShared.xNull(txtVoucherDate.getTag()));
        ////////////////////////////////////////////////////////////////////////
        mrTemp.setLmCode(DmShared.xNull(txtUniqueId.getTag()));
//        mrTemp.setUniqueId(DmShared.xNull(txtUniqueId.getText()));
        mrTemp.setManualCode(DmShared.xNull(txtUniqueId.getText()));
        mrTemp.setDisplayName(DmShared.xNull(txtDisplayName.getText()));
        mrTemp.setPartyType(DmShared.xNull(txtDisplayName.getTag2nd()));
        if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
            mrTemp.setSubCode(DmShared.xNull(txtDisplayName.getTag()));
        } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In")) {
        } else {
            mrTemp.setRegDocId(DmShared.xNull(regDocId));
            mrTemp.setUhidDocId(DmShared.xNull(uhidDocId));
        }
        mrTemp.setDefaultValue(defaultValue);
        mrTemp.setCustomerTypeCode(DmShared.xNull(txtHCustomerType.getTag()));
        mrTemp.setCustomerTypeName(DmShared.xNull(txtHCustomerType.getText()));
        mrTemp.setAddress(DmShared.xNull(txtAddress.getText()));
        mrTemp.setCityCode(DmShared.xNull(txtCity.getTag()));
        mrTemp.setCityName(DmShared.xNull(txtCity.getText()));
        mrTemp.setMobile(DmShared.xNull(txtMobile.getText()));
        mrTemp.setIdTypeCode(DmShared.xNull(txtIdType.getTag()));
        mrTemp.setIdTypeName(DmShared.xNull(txtIdType.getText()));
        mrTemp.setIdNo(DmShared.xNull(txtIdNo.getText()));
        mrTemp.setPlaceOfSupply(DmShared.xNull(txtCity.getTag2nd()));
        mrTemp.setSiteStateCode(DmShared.xNull(defaultValue.getStateCode()));
        ////////////////////////////////////////////////////////////////////////
        mrTemp.setCgstAmt(DmShared.vNull(totalCgstAmt).toString());
        mrTemp.setSgstAmt(DmShared.vNull(totalSgstAmt).toString());
        mrTemp.setUgstAmt(DmShared.vNull(totalUgstAmt).toString());

        mrTemp.setTaxableAmountToSave(DmShared.vNull(txtDiscount.getTag()).toString());
        mrTemp.setNetAmtToSave(DmShared.vNull(txtDiscount.getText()).toString());
        mrTemp.setPharmacyBalance(DmShared.vNull(txtAmount.getTag()).toString());
        mrTemp.setRemark(DmShared.xNull(txtHDisReason.getText()));
        mrTemp.getGrdDiscountDetails().addAll(grdDiscountDetail.getGrid().getStore().getAll());
        if (modifyReasonMdl != null) {
            mrTemp.setModifyReason(modifyReasonMdl);
        }
        return mrTemp;
    }

    private void topControlSave() {
        PharmacyFinalDiscountEntry_Mdl mrTemp;
        try {
            mrTemp = topControlSaveInitialize();
            PharmacyFinalDiscountEntry_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(topControl.getPermissions(), mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtMobile).show();
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

    private void showModifyReason() {
        modifyReason.show(topControl.getMode(), topControl.getDocId());
        modifyReason.addCallBack(new ModifyReason.CallBack() {
            @Override
            public void onOk(ModifyReason_Mdl modifyReason) {
                PharmacyFinalDiscountEntry.this.modifyReasonMdl = modifyReason;
                if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                    topControlDelete(topControl.getDocId());
                } else {
                    topControlSave();
                }
            }
        });
    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(topControl.getPermissions(), strKey, modifyReasonMdl, regDocId, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlMoveRec(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void manageBtnSearchRoom() {
        flwSearchBtn.clear();
        if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
            flwSearchBtn.add(btnIPDSearch);
        } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
            flwSearchBtn.add(btnOPDSearch);
        } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In"))) {
            flwSearchBtn.add(btnWalkInSearch);
        } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger"))) {
            flwSearchBtn.add(btnLedgerSearch);
        }
    }

    private void getCustomerDetails(String uniqueId) {
        srvAsync.invokeRPC().getCustomerDetails(txtHCustomerType.getTag(), uniqueId, txtVoucherDate.getText(), new AsyncCallback<DmResult<LedgerMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtUniqueId).show();
            }

            @Override
            public void onSuccess(DmResult<LedgerMaster_Mdl> result) {
                resetTxtHCoustomerType();
                if (result.isStatus()) {
                    fillData(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), new HideEvent.HideHandler() {
                        @Override
                        public void onHide(HideEvent event) {
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                                @Override
                                public void execute() {
                                    txtUniqueId.setFocus(true);
                                }
                            });
                        }
                    }).show();
                }
            }
        });
    }

    private void fillData(LedgerMaster_Mdl lmMdl) {
        if (lmMdl != null) {
            txtUniqueId.setTag(lmMdl.getSharedKeyFieldCode());
            txtUniqueId.setTag2nd(lmMdl.getManualCode());
            txtUniqueId.setText(lmMdl.getManualCode());
            txtDisplayName.setText(lmMdl.getDisplayName());
            if (!DmShared.xNull(lmMdl.getRegType()).equalsIgnoreCase("")) {
                txtDisplayName.setTag2nd(lmMdl.getRegType());
            } else {
                txtDisplayName.setTag2nd(defaultValue.getPartyType());
            }
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
                txtDisplayName.setTag(lmMdl.getAccountCode());
            }

            regDocId = lmMdl.getRegDocId();
            uhidDocId = lmMdl.getUhidDocid();

            txtMobile.setText(lmMdl.getMobile());
            txtIdType.setTag(lmMdl.getIdTypeCode());
            txtIdType.setText(lmMdl.getIdTypeName());
            txtIdNo.setText(lmMdl.getIdNo());
            txtAddress.setText(lmMdl.getAddress());
            setPlaceOfSupply(lmMdl.getStateCode());
            txtCity.setTag(lmMdl.getCityCode());
            txtCity.setText(lmMdl.getCityName());
            txtAmount.setTag(DmClient.vNull(lmMdl.getPharmacyBalance(), "0.00"));
            txtAmount.setText(DmClient.vNull(lmMdl.getPharmacyBalance(), "0.00"));
            lblPharmacyBalance.setText(DmShared.vNull(lmMdl.getPharmacyBalance()) != 0 ? "Pharmacy Balance: " + lmMdl.getPharmacyBalance() : "");
            txtDiscountPer.setFocus(true);
        } else {
            new DmAlertMessageBox("No record found. Try Again.", txtUniqueId).show();
        }
    }

    private void resetTxtHCoustomerType() {
        regDocId = "";
        uhidDocId = "";

        txtUniqueId.setText("");
        txtUniqueId.setTag("");
        txtUniqueId.setTag2nd("");

        txtVoucherNo.setText("");
        txtVoucherNo.setTag("");
        txtVoucherNo.setTag2nd("");

        txtReceiptType.setText("");
        txtReceiptType.setTag("");
        txtReceiptType.setTag2nd("");

        txtDisplayName.setText("");
        txtDisplayName.setTag("");
        txtDisplayName.setTag2nd("");

        txtMobile.setText("");
        txtMobile.setTag("");
        txtMobile.setTag2nd("");

        txtIdType.setText("");
        txtIdType.setTag("");
        txtIdType.setTag2nd("");

        txtIdNo.setText("");
        txtIdNo.setTag("");
        txtIdNo.setTag2nd("");

        txtAddress.setText("");
        txtAddress.setTag("");
        txtAddress.setTag2nd("");

        txtCity.setText("");
        txtCity.setTag("");
        txtCity.setTag2nd(defaultValue.getStateCode());

        txtAmount.setText("");
        txtAmount.setTag("");
        txtAmount.setTag2nd("");

        txtHDisReason.setText("");
        txtHDisReason.setTag("");
        txtHDisReason.setTag2nd("");

        cancelBy.setText("");
        lblPharmacyBalance.setText("");

        modifyReasonMdl = new ModifyReason_Mdl();

        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
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
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtVoucherDate)) {
            txtVoucherDate.setText(DmClient.returnDate(txtVoucherDate.getText()));
        } else if (event.getSource().equals(txtAmount)) {
            if (DmShared.vNull(txtAmount.getText()) == 0) {
                txtAmount.setText(DmShared.vNull(txtAmount.getTag()).toString());
            }
        } else if (event.getSource().equals(txtUniqueId)) {
            txtUniqueId.setText(DmShared.xNull(txtUniqueId.getTag2nd()));
        } else if (event.getSource().equals(txtDiscountPer)) {
            setDiscountAmt();
        }
    }

    private void setDiscountAmt() {
        if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 0) {
            if (DmShared.vNull(txtAmount.getText()).doubleValue() > 0) {
                double discountAmt = (DmShared.vNull(txtAmount.getText()).doubleValue() * DmShared.vNull(txtDiscountPer.getText()).doubleValue()) / 100;
                txtDiscount.setText(DmClient.vNull(DmClient.vNull(discountAmt, "0"), "0.00"));
                txtDiscountPer.setText("");
            }
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            roomSearchIPD.hide();
            roomSearchEMRG.hide();
            opdSearch.hide();
            ledgerSearch.hide();
            walkInSearch.hide();
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {

    }

    @Override
    public void onClick(ClickEvent event) {
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
        if (button == btnIPDSearch) {
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.IPD)) {
                roomSearchIPD.show();
            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.EMERGENCY)) {
                roomSearchEMRG.show();
            }
        } else if (button == btnOPDSearch) {
            if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.OPD)) {
                opdSearch.show(new pmOPD().moduleName, SearchValue_Mdl.OPDFilterType.SERVICE_DAYS,
                        SearchValue_Mdl.OPDFilterType.OPD_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.DAY_CARE)) {
                opdSearch.show(new pmDayCare().moduleName, SearchValue_Mdl.OPDFilterType.CANCEL_DAYS,
                        SearchValue_Mdl.OPDFilterType.DAYCARE_ONLY, SearchValue_Mdl.OPDFilterType.ALLOW_SITE_FOR_PHARMACY);
            }
        } else if (button == btnWalkInSearch) {
            walkInSearch.show();
        } else if (button == btnLedgerSearch) {
            ledgerSearch.show("", "");
        } else if (button == btnAdjust) {
            if (topControl.getMode().equalsIgnoreCase("Browse")) {
                return;
            }
            if (DmShared.vNull(txtAmount.getText()).doubleValue() <= 0) {
                new DmAlertMessageBox("No Balance Amount.Cannot give discount.", txtDiscountPer).show();
                return;
            }
            if (DmShared.vNull(txtDiscount.getText()).doubleValue() == 0 && DmShared.vNull(txtDiscountPer.getText()).doubleValue() == 0) {
                new DmAlertMessageBox("Discount Percentage or Amount is mandatory", txtDiscountPer).show();
                return;
            }
            if (DmShared.vNull(txtDiscountPer.getText()).doubleValue() > 100) {
                new DmAlertMessageBox("Discount Percentage can't be greater than 100.", txtDiscountPer).show();
                return;
            }
            hsnTaxWisePopup.show("", DmClient.vNull(Math.abs(DmShared.vNull(txtDiscount.getText())), "0.00"), txtVoucherDate.getText(), txtUniqueId.getTag());

        }
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getSource().equals(txtUniqueId)) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (!DmShared.xNull(txtUniqueId.getText()).trim().equalsIgnoreCase("")) {
                    if (DmShared.xNull(txtHCustomerType.getTag()).trim().equalsIgnoreCase("")) {
                        new DmAlertMessageBox("Type is mandatory.", txtHCustomerType).show();
                        return;
                    }
                    if (!DmShared.xNull(txtUniqueId.getText()).trim().equalsIgnoreCase(txtUniqueId.getTag2nd())) {
                        if (DmShared.xNull(txtHCustomerType.getTag()).trim().equalsIgnoreCase("Walk-In")) {
                            if (!DmShared.xNull(txtUniqueId.getText()).toUpperCase().contains(":W")) {
                                txtUniqueId.setText(DmShared.xNull(txtUniqueId.getText()) + ":W");
                            }
                        }
                        getCustomerDetails(txtUniqueId.getText());
                    } else {
                        txtDiscount.setFocus(true);
                    }
                } else {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.IPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.EMERGENCY))) {
                                btnIPDSearch.setFocus(true);
                            } else if ((DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.OPD)) || (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase(pvc.DAY_CARE))) {
                                btnOPDSearch.setFocus(true);
                            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Walk-In")) {
                                btnWalkInSearch.setFocus(true);
                            } else if (DmShared.xNull(txtHCustomerType.getTag()).equalsIgnoreCase("Ledger")) {
                                btnLedgerSearch.setFocus(true);
                            }
                        }
                    });
                }
            }
        }
    }

    private void setPlaceOfSupply(String stateCode) {
        if (DmShared.xNull(txtDisplayName.getTag2nd()).equalsIgnoreCase(DmEnum.DmPartyType.Unregistered.getId())) {
            txtCity.setTag2nd(DmShared.xNull(defaultValue.getStateCode()));
        } else {
            if (DmShared.iNull(stateCode).longValue() == 0) {
                txtCity.setTag2nd(DmShared.xNull(defaultValue.getStateCode()));
            } else {
                txtCity.setTag2nd(DmShared.xNull(stateCode));
            }
        }
    }

}
