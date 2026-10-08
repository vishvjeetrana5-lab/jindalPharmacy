package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.*;
import aarogya.master.reports.client.RptOrganizationMaster;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.STEnum.MedicineSaleOn;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Vld;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmToolkit.manageSite.client.ManageSite;
import dataman.dmToolkit.self.client.SetWidgetInvokerImpl;
import dataman.libraryMenu.self.shared.lmAccounts;
import java.util.*;

public final class OrganizationMaster extends OrganizationMaster_Dsg implements BlurHandler, AttachEvent.Handler, ClickHandler, SelectionHandler<Item> {

    private String subCode = "", currentDate = "";
    private final ServiceInvoker<OrganizationMaster_SrvAsync> srvAsync;
    private boolean isTpa = true, isOrganization = true;
    private final ManageSite manageSite = new ManageSite();
    private final OperationPercentagePopup operationPercentPopup = new OperationPercentagePopup();
    private final GrdDiscountPercent_Mdl discountPercentMdl = new GrdDiscountPercent_Mdl();
    private boolean isTabGUI = true, isTabContact = true;
    private String guiConfigCode = "";
    private DmGUI_Mdl dmGUI_Mdl;
    private final PercentPopup organizationPercentPopup = new PercentPopup();
    private final ServicesGridEdit<GrdServices_Mdl, GrdServices_PrpAcs> serviceGridEdit = new ServicesGridEdit(grdService, "Service");
    protected List<GrdRoomCategory_Mdl> roomCategory = new ArrayList<GrdRoomCategory_Mdl>();
    private List<GrdCategoryDiscount_Mdl> lstServiceCategory = new ArrayList<>();

    public OrganizationMaster(UserMenu menuPermission, HashMap hmParam, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(OrganizationMaster_Srv.class), OrganizationMaster.class.getName());
        if (hmParam != null) {
            widgetName = (String) hmParam.get("widgetName");
            isOrganization = (Boolean) hmParam.get("isOrganization");
        }

        this.asWidget().setTitle(widgetName);
        flwOrgPercent.clear();

        manageEvents();
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        gridEvents();
        manageTabArea(menuPermission);
        gui.setBannerVisible(false);

        defaultValues(swi);

    }

    private void defaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultVal(widgetName, new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    roomCategory.clear();
                    roomCategory.addAll(result.getData().getLstCategory());
                    guiConfigCode = DmShared.iNull(result.getValue1()).toString();
                    if (result.getData() != null) {
                        dmGUI_Mdl = result.getData().getGuiMdl();
                    }
                    currentDate = result.getData().getCurrentDate();
                    manageDisplay(false);
                    swi.setWidget(OrganizationMaster.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                }
            }
        });
    }

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        for (String ctrlPermission : menuPermission.getControlPermission()) {
            if (ctrlPermission.equalsIgnoreCase(percentBlog)) {
                flwOrgPercent.add(btnOrgDiscountPercent);

            }
        }

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                onAdd();
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageDisplay(true);
                if (DmShared.xNull(txtShortName.getText()).trim().equalsIgnoreCase("CP")) {
                    txtName.setEnabled(false);
                    txtShortName.setEnabled(false);
                } else {
                    txtName.setFocus(true);
                }
                txtHNature.setEnabled(isTpa);
                grdContactPerson.getGrid().getStore().add(new GrdContactPerson_Mdl());
                grdService.getGrid().getStore().add(new GrdServices_Mdl());
                gui.initialize(dmGUI_Mdl);

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
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(OrganizationMaster.this);
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

    private void prnRecord(boolean flag) {
        RptOrganizationMaster orgMast = new RptOrganizationMaster();
        LinkedHashMap<String, FieldUDT_Mdl> hashMapFilter = new LinkedHashMap();
        FieldUDT_Mdl filter;

        filter = new FieldUDT_Mdl();
        filter.setText(DmShared.xNull(txtName.getText()));
        filter.setTag(DmShared.xNull(topControl.getDocId()));
        hashMapFilter.put("CODE", filter);

        orgMast.setDefault(hashMapFilter, "", flag, DmClient.<SetWidgetInvoker>getStoreItem("swiEvent"));
    }

    private void textHelpEvents() {
        
        txtHReason.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDiscountReason(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReason).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReason.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReason).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
    

        
        txtHDiscountGiven.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDiscountGivenBy(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDiscountGiven).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDiscountGiven.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDiscountGiven).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        
        txtHRateList.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<RateList_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRateList(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<RateList_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRateList).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<RateList_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHRateList.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRateList).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, RateList_Mdl tRow) {
                if (tRow != null && blnStatus) {
                    clearServiceGrid();
                    getOrganizationPer(topControl.getMode().equalsIgnoreCase("New") ? "" : topControl.getDocId(), txtHRateList.getTag());
                }

            }

        });

        txtName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestion(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                        }
                    }
                });
            }
        });
        txtShortName.addCallBack(new DmSuggestBox.CallBack() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getSuggestionShortName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtShortName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<String>> result) {
                        if (result.isStatus()) {
                            txtShortName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtShortName).show();
                        }
                    }
                });
            }
        });
        txtHNature.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNature(helper, blnToEndOfFile, isOrganization, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHNature).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHNature.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHNature).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHTDSAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTDSAc(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTDSAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTDSAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTDSAc).show();
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

        txtHTPAControlAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTDSAc(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTPAControlAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTPAControlAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTPAControlAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHExcessecurityAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTDSAc(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHExcessecurityAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHExcessecurityAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHExcessecurityAc).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHActive.addKeyDownHandler(new DmTextHelp.TxtHelpKeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event, Object objSource) {

                txtHActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

                    @Override
                    public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                        srvAsync.invokeRPC(blnCallBackWait).getTxtHActive(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                new DmAlertMessageBox(caught.getMessage(), txtHActive).show();
                            }

                            @Override
                            public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                                if (result.isStatus()) {
                                    txtHActive.setData(result.getData(), result.getPaging());
                                } else {
                                    new DmAlertMessageBox(result.getErrorMessage(), txtHActive).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

                    }
                });
            }
        });

        operationPercentPopup.addCallBack(new OperationPercentagePopup.CallBack() {
            @Override
            public void onOk(GrdDiscountPercent_Mdl mrTemp) {
                setPrintPopupData(mrTemp);
            }

            @Override
            public void onClose() {

            }
        });
        organizationPercentPopup.addCallBack(new PercentPopup.CallBack() {
            @Override
            public void onOk(DiscountPercent_Mdl mrTemp) {
                if (mrTemp != null) {
                    txtPharmacyDiscountAllow.setText(mrTemp.isPharmacyDiscountAllow() ? "Yes" : "No");
                    txtPharmacyDiscountAllow.setTag(mrTemp.isPharmacyDiscountAllow() ? "1" : "0");
                    txtMedicineSaleOn.setTag(DmShared.iNull(mrTemp.getMedicineSaleOn().getId()).toString());
                    txtMedicineSaleOn.setText(mrTemp.getMedicineSaleOn().getValue());
                    txtPharmacyDiscount.setText(mrTemp.getPharmacyDiscountPercentage());
                    lstServiceCategory.clear();
                    lstServiceCategory.addAll(mrTemp.getLstCategoryDiscount());
                }
            }

            @Override
            public void onClose() {

            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, isOrganization, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void clearServiceGrid() {
        grdService.getGrid().getStore().clear();
        grdService.getGrid().getStore().add(new GrdServices_Mdl());
        txtPharmacyDiscount.setText("");
        txtPharmacyDiscount.setTag("");
        txtPharmacyDiscount.setTag2nd("");
        txtPharmacyDiscountAllow.setText("");
        txtPharmacyDiscountAllow.setTag("");
        txtPharmacyDiscountAllow.setTag2nd("");
        txtMedicineSaleOn.setText("");
        txtMedicineSaleOn.setTag("");
        txtMedicineSaleOn.setTag2nd("");
    }

    private void gridEvents() {
        grdContactPerson.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                int r1 = event.getRowIndex();
                subCode = grdContactPerson.getGrid().getStore().get(r1).getSharedKeyFieldCode();
                showAddressBook();
            }
        });
        serviceGridEdit.addCallBack(new ServicesGridEdit.CallBack() {
            @Override
            public void onOk(GrdServices_Mdl mrTemp) {
                grdService.commitChanges();
                grdService.getGrid().getStore().add(new GrdServices_Mdl());
            }
        });
        grdService.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {

            @Override
            public void onRowClick(RowClickEvent event) {
                gridServiceEdit(event.getRowIndex());
            }
        });
        grdService.getGrid().addDomHandler(new KeyDownHandler() {

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    gridServiceEdit(grdService.getGrid().getStore().indexOf(grdService.getGrid().getSelectionModel().getSelectedItem()));
                }

            }
        }, KeyDownEvent.getType());
    }

    private void gridServiceEdit(int currentRowIndex) {
        if (topControl.getMode().equalsIgnoreCase("Browse")) {
            serviceGridEdit.show(currentRowIndex, false, false, this);
        } else {
            if (DmShared.iNull(txtHRateList.getTag()).longValue() > 0) {
                serviceGridEdit.show(currentRowIndex, true, true, this);
            } else {
                topControlSaveError(900, "Rate List is Mandatory.", "", "");
            }

        }
    }

//    private void topControlSaveError(int errorNo, String errorMessage) {
//
//        switch (errorNo) {
//            case 100:
//                txtName.setFocus(true);
//                new DmAlertMessageBox(errorMessage, txtName).show();
//                break;
//            case 200:
//                txtShortName.setFocus(true);
//                new DmAlertMessageBox(errorMessage, txtShortName).show();
//                break;
//            case 400:
//                new DmAlertMessageBox(errorMessage, txtHNature).show();
//                break;
//            case 500:
//                new DmAlertMessageBox(errorMessage, txtHActive).show();
//                break;
//            case 600:
//                new DmAlertMessageBox(errorMessage, txtAgreementDate).show();
//                break;
//            case 700:
//                new DmAlertMessageBox(errorMessage, txtMobile).show();
//                break;
//            case 800:
//                new DmAlertMessageBox(errorMessage, txtEmail).show();
//                break;
//            case 900:
//                new DmAlertMessageBox(errorMessage, txtHRateList).show();
//                break;
//            case 1000:
//                new DmAlertMessageBox(errorMessage, txtEffectiveDate).show();
//                break;
//            default:
//                new DmAlertMessageBox(errorMessage, txtName).show();
//        }
//
//    }
    private void showAddressBook() {
        final HashMap<String, String> hm = new HashMap<>();
        hm.put("addressCode", subCode);
        hm.put("orgCode", topControl.getDocId());
        hm.put("orgName", DmShared.xNull(txtName.getText()));
        DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new lmAccounts().moduleName, new lmAccounts().ADDRESS_BOOK.caption, null, null, new SetWidgetInvokerImpl() {
            @Override
            public void setWidget(final IsWidget wgtObject) {
                ((IsWidgetExtended) wgtObject).action("ORGANIZATION", hm, new DmCallBack() {
                    @Override
                    public void result(DmResult result) {
                        if (result != null) {
                            if (result.isStatus()) {
                                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                ((IsWidgetExtended) wgtObject).action("focus_On_Add", null, null);
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource().equals(priceListBtn)) {
            if (topControl.getDocId().equalsIgnoreCase("")) {
                new DmAlertMessageBox("Please select a Record.", priceListBtn).show();
                return;
            } else {
                showOrganizationMaster();
            }
        } else if (event.getSource().equals(btnOperationPercentage)) {
            showPercentagePopup();
        } else if (event.getSource().equals(btnOrgDiscountPercent)) {
            DiscountPercent_Mdl mrTemp = new DiscountPercent_Mdl();
            mrTemp.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(txtMedicineSaleOn.getTag()).intValue()));
            mrTemp.setPharmacyDiscountAllow(DmShared.iNull(txtPharmacyDiscountAllow.getTag()).intValue() == 1);
            mrTemp.setPharmacyDiscountPercentage(DmClient.vNull(txtPharmacyDiscount.getText(), "0.00"));
            mrTemp.getLstCategoryDiscount().addAll(lstServiceCategory);
            organizationPercentPopup.show(mrTemp, topControl.getMode());

        }

    }

    private void manageTabArea(UserMenu menuPermission) {
//        tabPanel.clear();
//        for (String controlPermission : menuPermission.getControlPermission()) {
//            if (controlPermission.equalsIgnoreCase(tabContactPerson)) {
//                isTabContact = true;
//            } else if (controlPermission.equalsIgnoreCase(tabGUI)) {
//                isTabGUI = true;
//            }
//        }
//        tabPanel.add(serviceGrd(), tabRateList);
//        if (isTabContact) {
//            tabPanel.add(flwContact, tabContactPerson);
//        }
//        if (isTabGUI) {
//            tabPanel.add(flwGUI, tabGUI);
//        }
    }

    private void displayFirstTab() {
        tabPanel.selectTab(0);
    }

    private void manageEvents() {
        txtRevisionDate.addBlurHandler(this);
        txtAgreementDate.addBlurHandler(this);
        txtTarrifRevisionDate.addBlurHandler(this);
        mnuManageSite.addSelectionHandler(this);
        mnuExportOrganization.addSelectionHandler(this);
        flwForm.addAttachHandler(this);
        priceListBtn.addClickHandler(this);
        btnOrgDiscountPercent.addClickHandler(this);
        txtEffectiveDate.addBlurHandler(this);
        btnOperationPercentage.addClickHandler(this);
        gui.addCallback(new DmGUI.Callback() {

            @Override
            public void initialized() {

            }

            @Override
            public String getGUITemplate() {
                return "";
            }

            @Override
            public Object addCustom(DmGUIControl_Mdl dmGUIControl_Mdl, FlowPanel flwTr, Object tabIndexAfter) {
                return null;
            }

            @Override
            public void removeCustom(DmTopControl topControl) {
            }

            @Override
            public void guiDataCopied(DmGUI_Mdl gui_Mdl, List<String> listCustomWidgetToCopy, String copyFromDocId, HashMap<String, String> param) {
            }
        });

    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private OrganizationMaster_Mdl topControlSaveInitialize() {
        OrganizationMaster_Mdl mrTemp = new OrganizationMaster_Mdl();
        DiscountPercent_Mdl discountMdl = new DiscountPercent_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setShortName(DmShared.xNull(txtShortName.getText()));

        mrTemp.setRemark(DmShared.xNull(txtRemark.getText()));
        mrTemp.setGstinNo(DmShared.xNull(txtGSTIN.getText()));
        mrTemp.setIsNatureTag(DmShared.xNull(txtHNature.getTag()));
        mrTemp.setActiveTag(DmShared.xNull(txtHActive.getTag()));
        mrTemp.setAgreementDate(DmShared.xNull(txtAgreementDate.getText()));
        mrTemp.setAgreementNo(DmShared.xNull(txtAgreementNo.getText()));
        mrTemp.setRevisionDate(DmShared.dNull(txtRevisionDate.getText()));
        mrTemp.setTarrifRevisionDate(DmShared.dNull(txtTarrifRevisionDate.getText()));
        mrTemp.setTdsAcTag(DmShared.xNull(txtHTDSAc.getTag()));

        mrTemp.setTpaControlAcTag(DmShared.xNull(txtHTPAControlAc.getTag()));

        mrTemp.setExcessecurityAcTag(DmShared.xNull(txtHExcessecurityAc.getTag()));
        mrTemp.setDiscountReason(DmShared.xNull(txtHReason.getText()));
        mrTemp.setDiscountGivenByText(DmShared.xNull(txtHDiscountGiven.getText()));
        mrTemp.setDiscountGivenByTag(DmShared.xNull(txtHDiscountGiven.getTag()));

        mrTemp.setBiilingPer(DmShared.vNull(txtBillingPer.getText()).toString());
        mrTemp.setEmail(DmShared.xNull(txtEmail.getText()));
        mrTemp.setMobileNo(DmShared.xNull(txtMobile.getText()));
        mrTemp.setRateListCode(DmShared.xNull(txtHRateList.getTag()));
        mrTemp.setRateList_Sno(DmShared.xNull(txtHRateList.getTag2nd()));

        mrTemp.setDiscountPercentMdl(discountPercentMdl);
        mrTemp.setGuiMdl(gui.getGUI_Mdl());
        mrTemp.setGuiConfigCode(guiConfigCode);

        discountMdl.setMedicineSaleOn(MedicineSaleOn.getKeyOf(DmShared.iNull(txtMedicineSaleOn.getTag()).intValue()));
        discountMdl.setPharmacyDiscountAllow(DmShared.iNull(txtPharmacyDiscountAllow.getTag()).intValue() == 1);
        discountMdl.setPharmacyDiscountPercentage(DmClient.vNull(txtPharmacyDiscount.getText(), "0.00"));
        discountMdl.getLstCategoryDiscount().addAll(lstServiceCategory);
        mrTemp.setDiscountMdl(discountMdl);
        mrTemp.getLstService().addAll(grdService.getGrid().getStore().getAll());
        mrTemp.setApplicableDate(DmShared.dNull(txtEffectiveDate.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        OrganizationMaster_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            DmGUI_Vld.chkSave(mrTemp.getGuiMdl().getGuiRowList());

            OrganizationMaster_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtName).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControl.setDocId(result.getData());
                        if (topControl.getMode().equalsIgnoreCase("New")) {
                            topControl.btnClick("New");
                        } else {
                            if (!DmShared.xNull(result.getValue1()).equalsIgnoreCase("")) {
                                new DmAlertMessageBox(result.getValue1(), (DmTextBox) null).show();
                            }
                            topControlMoveRec(topControl.getDocId());
                        }
                    } else {

                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage(), "", "");

                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage(), "", "");

        }
    }

    private void topControlSaveError(int errorNo, String errorMessage, String errorSource, String errorSourceCode) {

        if (DmShared.xNull(errorSource).trim().equalsIgnoreCase("GUI")) {
            new DmAlertMessageBox(errorMessage, gui, errorSourceCode).show();
        } else {

            switch (errorNo) {
                case 100:
                    txtName.setFocus(true);
                    new DmAlertMessageBox(errorMessage, txtName).show();
                    break;
                case 200:
                    txtShortName.setFocus(true);
                    new DmAlertMessageBox(errorMessage, txtShortName).show();
                    break;
                case 400:
                    new DmAlertMessageBox(errorMessage, txtHNature).show();
                    break;
                case 500:
                    new DmAlertMessageBox(errorMessage, txtHActive).show();
                    break;
                case 600:
                    new DmAlertMessageBox(errorMessage, txtAgreementDate).show();
                    break;
                case 700:
                    new DmAlertMessageBox(errorMessage, txtMobile).show();
                    break;
                case 800:
                    new DmAlertMessageBox(errorMessage, txtEmail).show();
                    break;
                case 900:
                    new DmAlertMessageBox(errorMessage, txtHRateList).show();
                    break;
                case 1000:
                    new DmAlertMessageBox(errorMessage, txtEffectiveDate).show();
                    break;
                default:
                    new DmAlertMessageBox(errorMessage, txtName).show();
            }

        }
    }

    private void topControlCard() {
        srvAsync.invokeRPC().getNavigator(isOrganization, new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        topControlMoveRec(strKey, null);
    }

    private void topControlMoveRec(String strKey, final DmCallBack callBack) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<OrganizationMaster_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<OrganizationMaster_Mdl> result) {
                if (result.isStatus()) {
                    clearServiceGrid();
                    topControlMoveRecSuccess(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
                if (callBack != null && result.isStatus()) {
                    topControl.btnClick("Edit");
                    DmResult<Object> dmrTemp = new DmResult<>();
                    dmrTemp.setStatus(true);
                    callBack.result(dmrTemp);
                } else {
                    topControl.setDisplay(true);
                }
            }
        });
    }

    private void topControlMoveRecSuccess(OrganizationMaster_Mdl data) {
        displayFirstTab();
        clear();

        topControl.setDocId(data.getSharedKeyFieldCode());
        txtName.setText(DmShared.xNull(data.getSharedKeyFieldName()));
        txtShortName.setText(data.getShortName());
        txtRemark.setText(data.getRemark());
        txtGSTIN.setText(data.getGstinNo());
        isTpa = !data.isTPA();
        txtHNature.setTag(DmShared.xNull(data.getIsNatureTag()));
        txtHNature.setText(DmShared.xNull(data.getIsNatureText()));

        txtHTDSAc.setTag(DmShared.xNull(data.getTdsAcTag()));
        txtHTDSAc.setText(DmShared.xNull(data.getTdsAcText()));

        txtHTPAControlAc.setTag(DmShared.xNull(data.getTpaControlAcTag()));
        txtHTPAControlAc.setText(DmShared.xNull(data.getTpaControlAcText()));

        txtHExcessecurityAc.setTag(DmShared.xNull(data.getExcessecurityAcTag()));
        txtHExcessecurityAc.setText(DmShared.xNull(data.getExcessecurityAcText()));
        txtHReason.setText(DmShared.xNull(data.getDiscountReason()));
        txtHDiscountGiven.setText(DmShared.xNull(data.getDiscountGivenByText()));
        txtHDiscountGiven.setTag(DmShared.xNull(data.getDiscountGivenByTag()));

        txtRevisionDate.setText(DmShared.xNull(data.getRevisionDate()));
        txtHActive.setText(DmShared.xNull(data.getActiveText()));
        txtHActive.setTag(DmShared.xNull(data.getActiveTag()));
        txtAgreementNo.setText(DmShared.xNull(data.getAgreementNo()));
        txtAgreementDate.setText(DmShared.dNull(data.getAgreementDate()));
        txtTarrifRevisionDate.setText(DmShared.dNull(data.getTarrifRevisionDate()));
        txtBillingPer.setText(DmClient.vNull(data.getBiilingPer(), "0.00"));
        txtMobile.setText(DmShared.xNull(data.getMobileNo()));
        txtEmail.setText(DmShared.xNull(data.getEmail()));
        manageDisplay(false);
        userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
        userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());
        grdContactPerson.getGrid().getStore().clear();
        grdContactPerson.getGrid().getStore().addAll(data.getListContectPerson());

        grdRateList.getGrid().getStore().clear();
        grdRateList.getGrid().getStore().addAll(data.getLstRateList());
        grdService.getGrid().getStore().clear();
        grdService.getGrid().getStore().addAll(data.getLstService());

        txtHRateList.setText(data.getRateListName());
        txtHRateList.setTag(data.getRateListCode());
        txtHRateList.setTag2nd(data.getRateList_Sno());
        lstServiceCategory.clear();
        lstServiceCategory.addAll(data.getDiscountMdl().getLstCategoryDiscount());
        txtEffectiveDate.setText(DmShared.dNull(data.getApplicableDate()));

        discountPercentMdl.setOrganizationCode(DmShared.xNull(data.getDiscountPercentMdl().getOrganizationCode()));
        discountPercentMdl.setPercentages(data.getDiscountPercentMdl().getPercentages());

        // guiConfigCode = DmShared.iNull(data.getGuiConfigCode()).toString();
        gui.initialize(data.getGuiMdl());

    }

    private void topControlDelete(String strKey) {
        srvAsync.invokeRPC().deleteRecord(strKey, guiConfigCode, new AsyncCallback<DmResult<String>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    topControlCard();
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
        if (event.getSource() == txtAgreementDate) {
            txtAgreementDate.setText(DmClient.returnDate(txtAgreementDate.getText()));
        } else if (event.getSource() == txtRevisionDate) {
            txtRevisionDate.setText(DmClient.returnDate(txtRevisionDate.getText()));
        } else if (event.getSource() == txtTarrifRevisionDate) {
            txtTarrifRevisionDate.setText(DmClient.returnDate(txtTarrifRevisionDate.getText()));
        } else if (event.getSource() == txtEffectiveDate) {
            txtEffectiveDate.setText(DmClient.returnDate(txtEffectiveDate.getText()));
        }

    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
        }
    }

    private void clear() {
        // guiConfigCode = "";
        gui.clear();
    }

    public void onAdd() {
        isTpa = true;
        txtHNature.setEnabled(isTpa);
        txtName.setFocus(true);
        txtShortName.setEnabled(true);
        txtHNature.setText(isOrganization ? "General" : "Collection Center");
        txtHNature.setTag(isOrganization ? "0" : "2");
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        txtHActive.setTag("1");
        txtHActive.setText("Yes");
        txtPharmacyDiscount.setTag("");
        txtPharmacyDiscount.setText("");
        txtPharmacyDiscountAllow.setTag("");
        txtPharmacyDiscountAllow.setText("");
        txtMedicineSaleOn.setTag("");
        txtMedicineSaleOn.setText("");
        manageDisplay(true);
        grdService.getGrid().getStore().clear();
        grdService.getGrid().getStore().add(new GrdServices_Mdl());
        grdRateList.getGrid().getStore().clear();
        grdRateList.getGrid().getStore().add(new GrdRateList_Mdl());
        lstServiceCategory.clear();
        txtEffectiveDate.setText(currentDate);

        displayFirstTab();

        gui.initialize(dmGUI_Mdl);

    }

    private void manageDisplay(boolean flag) {
        txtHActive.setEnabled(flag);
        txtHNature.setEnabled(isOrganization && !topControl.getMode().equalsIgnoreCase("Browse"));
        txtConsFeesOrganizationService.setEnabled(false);

        txtMedicineSaleOn.setEnabled(false);
        txtPharmacyDiscount.setEnabled(false);
        txtPharmacyDiscountAllow.setEnabled(false);

    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        HashMap<String, String> hmOrganization = ((HashMap<String, String>) object);
        try {
            if (hmOrganization != null) {
                switch (nature) {
                    case "OPEN_ORGANIZATION":
                        topControlMoveRec(((String) hmOrganization.get("organizationCode")), callBack);
                        break;
                    case "ADD_INVESTIGATION_SERVICE":
                        break;
                }
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }
    }

    private void showOrganizationMaster() {
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(new pmMaster().moduleName, new pmMaster().RATE_LIST_MASTER.caption, null, null, new SetWidgetInvokerImpl() {
                @Override
                public void setWidget(final IsWidget wgtObject) {
                    final HashMap<String, String> hmOrganization = new HashMap<String, String>();
//                    hmOrganization.put("organizationCode", topControl.getDocId());
                    hmOrganization.put("organizationCode", txtHRateList.getTag());
                    hmOrganization.put("serviceCode", "");
                    ((IsWidgetExtended) wgtObject).action("OPEN_ORGANIZATION", hmOrganization, new DmCallBack() {
                        @Override
                        public void result(DmResult result) {
                            if (result != null) {
                                if (result.isStatus()) {
                                    ((IsWidgetExtended) wgtObject).action("ADD_INVESTIGATION_SERVICE", hmOrganization, null);
                                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").setWidget(wgtObject);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void showPercentagePopup() {
        operationPercentPopup.show(btnOperationPercentage, discountPercentMdl, topControl.getMode());
    }

    @Override
    public void onSelection(SelectionEvent<Item> event) {
        if (event.getSource().equals(mnuManageSite)) {
            if (topControl.getMode().equalsIgnoreCase("Browse") && DmShared.iNull(topControl.getDocId()).longValue() > 0) {
                manageSite.show("subGroupSiteList", "subCode", topControl.getDocId(), "site_Code");
            } else {
                new DmAlertMessageBox("Can be use in Browse Mode only and select a record.", txtName).show();
            }
        } else if (event.getSource().equals(mnuExportOrganization)) {
            new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                @Override
                public void onDialogHide(DialogHideEvent event) {
                    if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                        exportCSV();
                    }
                }
            }).show();
        }
    }

    private void exportCSV() {
        srvAsync.invokeRPC().exportCSV(DmRequestBuilder.getSessionId(), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                }
            }
        });
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void setPrintPopupData(GrdDiscountPercent_Mdl mrTemp) {
        discountPercentMdl.setOrganizationCode(DmShared.xNull(mrTemp.getOrganizationCode()));
        discountPercentMdl.setPercentages(mrTemp.getPercentages());
        operationPercentPopup.hide();
    }

    private void getOrganizationPer(String organizationCode, String rateListCode) {
        srvAsync.invokeRPC().getOrganizationPer(organizationCode, rateListCode, new AsyncCallback<DmResult<List<GrdCategoryDiscount_Mdl>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<List<GrdCategoryDiscount_Mdl>> result) {
                if (result.isStatus()) {
                    lstServiceCategory.clear();
                    lstServiceCategory.addAll(result.getData());

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }
}
