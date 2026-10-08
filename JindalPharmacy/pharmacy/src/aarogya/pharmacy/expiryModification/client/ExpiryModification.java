package aarogya.pharmacy.expiryModification.client;

import aarogya.pharmacy.expiryModification.shared.*;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import java.util.List;
import aarogya.pharmacy.expiryModification.shared.ExpiryModification_Srv;
import aarogya.pharmacy.expiryModification.shared.ExpiryModification_SrvAsync;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;

public final class ExpiryModification extends ExpiryModification_Dsg implements BlurHandler, AttachEvent.Handler, DmRootEvents {

    private ServiceInvoker<ExpiryModification_SrvAsync> srvAsync;
    protected DefaultValue_Mdl defaultValue;
    private String selectedFilterType = "", selectedFilterValue = "";
    private String isBestBefore = "", selectedIbCode = "", selectedMedicineCode = "", purchaseUnitConversionRate = "", selectedDepartment = "";
    private String oldExpiryDate = "";

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);
        topControl.setOtherRequired(false);
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("D", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("E", "*"));
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                txtHMedicineName.setFocus(true);

            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                txtHMedicineName.setFocus(true);

            }
        });

        topControl.addDeleteHandeller(new DmTopControl.eDelete() {
            @Override
            public void deleteClicked() {
                new DmConfirmMessageBox("Are you sure? You want to delete this record.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                            //topControlDelete(topControl.getDocId());
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
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(ExpiryModification.this);
            }
        });

        topControl.setDisplay(true);
    }

    private void textHelpEvents() {
        txtHFilterType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedFilterType = DmShared.xNull(txtHFilterType.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFilterType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFilterType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFilterType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFilterType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.iNull(selectedFilterType).longValue() != DmShared.iNull(txtHFilterType.getTag()).longValue()) {
                    txtHFilterValue.setText("");
                    txtHFilterValue.setTag("");
                }
            }
        });

        txtHFilterValue.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedFilterValue = DmShared.xNull(txtHFilterValue.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHFilterValue(txtHFilterType.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHFilterValue).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHFilterValue.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHFilterValue).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (DmShared.iNull(selectedFilterValue).longValue() != DmShared.iNull(txtHFilterValue.getTag()).longValue()) {
                }
            }
        });

        txtHDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedDepartment = DmShared.xNull(txtHDepartment.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                if (blnStatus) {
                    if (DmShared.iNull(selectedDepartment).longValue() != DmShared.iNull(txtHDepartment.getTag()).longValue()) {
                        txtHMedicineName.setText("");
                        txtHMedicineName.setTag("");
                        txtHMedicineName.setTag2nd("");
                        resetMedicine();
                    }
                }
            }
        });
        txtHMedicineName.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHMedicineName_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedMedicineCode = DmShared.xNull(txtHMedicineName.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineName(txtHDepartment.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHMedicineName_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHMedicineName_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHMedicineName.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineName).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHMedicineName_Mdl t1Row) {
                if (!DmShared.xNull(selectedMedicineCode).equalsIgnoreCase(DmShared.xNull(txtHMedicineName.getTag()))) {
                    resetMedicine();
                }
                if (blnStatus && t1Row != null) {
                    txtHMedicineName.setTag2nd(DmShared.xNull(t1Row.getItemProperty()));
                    txtSKU.setText(DmShared.xNull(t1Row.getSkuName()));
                    txtSKU.setTag(DmShared.xNull(t1Row.getSkuCode()));
                    txtSKU.setTag2nd(DmShared.xNull(t1Row.getSkuDecimalPlaces()));
                    purchaseUnitConversionRate = DmShared.xNull(t1Row.getPurchaseUnitConversionRate());
                    if (DmShared.iNull(t1Row.getPurchaseUnitCode()).longValue() != DmShared.iNull(t1Row.getSkuCode()).longValue()) {
                        txtSaleRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                        txtMRPDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                        txtRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()) + " = " + DmShared.xNull(t1Row.getPurchaseUnitConversionRate()) + " " + DmShared.xNull(t1Row.getSkuName()));
                    } else {
                        txtSaleRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                        txtMRPDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                        txtRateDisplay.setText(DmShared.xNull(t1Row.getPurchaseUnitName()));
                    }
                    txtSaleRateDisplay.setTag(DmShared.xNull(t1Row.getPurchaseUnitCode()));
                    txtSaleRateDisplay.setTag2nd(DmShared.xNull(t1Row.getPurchaseUnitName()));
                }
            }
        });

        txtHBatchNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHBatchNo_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                selectedIbCode = DmShared.xNull(txtHBatchNo.getTag());
                srvAsync.invokeRPC(blnCallBackWait).getTxtHBatchNo(txtHMedicineName.getTag(), txtHDepartment.getTag(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHBatchNo_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBatchNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHBatchNo_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHBatchNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBatchNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHBatchNo_Mdl t1Row) {
                oldExpiryDate = "";
                if (!DmShared.xNull(selectedIbCode).equalsIgnoreCase(DmShared.xNull(txtHBatchNo.getTag()))) {
                    resetBatchNo();
                    setRate(txtHMedicineName.getTag(), txtHBatchNo.getTag(), txtHDepartment.getTag(), DmClient.getLoginDate(), DmShared.xNull(purchaseUnitConversionRate));
                }
                if (blnStatus && t1Row != null) {
                    txtMRP.setText(DmShared.xNull(t1Row.getMrp()));
                    txtMRP.setTag(DmShared.xNull(t1Row.getSkuMrp()));
                    txtSaleRate.setText(DmShared.xNull(t1Row.getSaleRate()));
                    txtSaleRate.setTag(DmShared.xNull(t1Row.getSkuSaleRate()));
                    oldExpiryDate = DmShared.dNull(t1Row.getExpireDate());
                    txtExpiryDate.setText(DmShared.dNull(t1Row.getExpireDate()));
                    txtExpiryDate.setTag(DmShared.dNull(t1Row.getManufacturingDate()));
                }
            }
        });

    }

    private void setRate(String itemCode, String ibCode, String departmentCode, String v_Date, final String purchaseUnitCoversionRate) {
        srvAsync.invokeRPC(true).getSkuLandedRate(itemCode, ibCode, departmentCode, v_Date, new AsyncCallback<DmResult<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHMedicineName).show();
            }

            @Override
            public void onSuccess(DmResult<String> result) {
                if (result.isStatus()) {
                    String skuLandedRate = result.getData();
                    double SkuRate = DmShared.vNull(skuLandedRate).doubleValue();
                    double rate = SkuRate * DmShared.vNull(purchaseUnitCoversionRate).doubleValue();
                    txtRate.setText(DmClient.vNull(rate, "0.00"));
                    txtRate.setTag2nd(DmClient.vNull(rate, "0.00"));
                    txtRate.setTag(DmClient.vNull(SkuRate, DmShared.getNumberFormat(7)));
                }
            }
        });
    }

    private void calculate() {
        if (DmShared.vNull(txtRate.getText()).doubleValue() != DmShared.vNull(txtRate.getTag2nd()).doubleValue()) {
            double SkuRate = DmShared.vNull(txtRate.getText()).doubleValue() / DmShared.vNull(purchaseUnitConversionRate).doubleValue();
            txtRate.setTag(DmClient.vNull(SkuRate, DmShared.getNumberFormat(7)));
            txtRate.setTag2nd(DmClient.vNull(txtRate.getText(), "0.00"));
        }
        txtRate.setText(DmClient.vNull(txtRate.getText(), "0.00"));
        txtRate.setTag(DmClient.vNull(txtRate.getTag(), DmShared.getNumberFormat(7)));
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtRate) {
            calculate();
        }
        if (event.getSource() == txtExpiryDate) {
            txtExpiryDate.setText(DmClient.returnDate(txtExpiryDate.getText()));
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (event.isAttached()) {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        if (rootEvent.getEvent().getNativeEvent().getCtrlKey() && rootEvent.getEvent().getNativeEvent().getShiftKey() && rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
            rootEvent.stopPropagation();
        }
    }

    public ExpiryModification(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<ExpiryModification_SrvAsync>(GWT.create(ExpiryModification_Srv.class), ExpiryModification.class.getName());
        this.asWidget().setTitle(widgetName);
        txtExpiryDate.addBlurHandler(this);
        flwForm.addAttachHandler(this);
        topControlEvents(menuPermission);
        findEvents();
        textHelpEvents();
        onAdd();
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
                    swi.setWidget(ExpiryModification.this);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<dmFind_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getFind(topControl.getPermissions(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<dmFind_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), dmFind.getTextBox()).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<dmFind_Mdl>> result) {
                        if (result.isStatus()) {
                            dmFind.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), dmFind.getTextBox()).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, dmFind_Mdl tRow) {
                flwForm.remove(dmFind);
                flwForm.add(vrtInnerForm);
                if (blnStatus) {
                    topControlMoveRec(tRow.getSharedKeyFieldCode());
                }
            }
        });
    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<ExpiryModification_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<ExpiryModification_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
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
                    if (result.isStatus()) {
                        topControl.setNavigator(result.getData());
                    } else {
                        new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                    }
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void searchRecord(String key) {
    }

    private ExpiryModification_Mdl topControlSaveInitialize() {
        ExpiryModification_Mdl mrTemp = new ExpiryModification_Mdl();
        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }
        mrTemp.setDepartmentCode(DmShared.xNull(txtHDepartment.getText()));
        mrTemp.setDepartmentName(DmShared.xNull(txtHDepartment.getTag()));
        mrTemp.setItemCode(DmShared.xNull(txtHMedicineName.getTag()));
        mrTemp.setIbCode(DmShared.xNull(txtHBatchNo.getTag()));
        mrTemp.setBatchNo(DmShared.xNull(txtHBatchNo.getText()));
        mrTemp.setOldExpiryDate(DmShared.xNull(oldExpiryDate));
        mrTemp.setNewExpiryDate(DmShared.xNull(txtExpiryDate.getText()));
        mrTemp.setSkuName(DmShared.xNull(txtSKU.getText()));
        mrTemp.setPurchaseUnitName(DmShared.xNull(txtSaleRateDisplay.getText()));
        mrTemp.setSaleRate(DmShared.vNull(txtSaleRate.getText()).toString());
        mrTemp.setMRP(DmShared.vNull(txtMRP.getText()).toString());
        mrTemp.setVoucherDate(DmClient.getLoginDate());
        return mrTemp;
    }

    private void topControlSave() {
        ExpiryModification_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            ExpiryModification_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHDepartment).show();
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

//                        new DmAlertMessageBox(DmShared.xNull(result.getData()), (DmTextBox) null).show();
//                        txtHMedicineName.setText("");
//                        txtHMedicineName.setTag("");
//                        txtHMedicineName.setTag2nd("");
//                        resetBatchNo();
//                        resetMedicine();
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
                new DmAlertMessageBox(errorMessage, txtExpiryDate).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, (DmTextBox) null).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtHDepartment).show();
                break;
        }
    }

    private void topControlMoveRecSuccess(DmResult<ExpiryModification_Mdl> result) {
        clear();
        if (result.isStatus()) {
            ExpiryModification_Mdl mrTemp = result.getData();
            topControl.setDocId(mrTemp.getSharedKeyFieldCode());

            txtHMedicineName.setText(DmShared.xNull(mrTemp.getItemName()));
            txtHMedicineName.setTag(DmShared.xNull(mrTemp.getItemCode()));

            txtHBatchNo.setText(DmShared.xNull(mrTemp.getBatchNo()));
            txtHBatchNo.setTag(DmShared.xNull(mrTemp.getIbCode()));

            txtSKU.setText(DmShared.xNull(mrTemp.getSkuName()));
            txtExpiryDate.setText(DmShared.dNull(mrTemp.getNewExpiryDate()));
            txtMRP.setText(DmClient.vNull(mrTemp.getMRP(), "0.00"));
            txtMRPDisplay.setText(DmShared.xNull(mrTemp.getPurchaseUnitName()));

            txtSaleRate.setText(DmClient.vNull(mrTemp.getSaleRate(), "0.00"));
            txtSaleRateDisplay.setText(DmShared.xNull(mrTemp.getPurchaseUnitName()));

        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }
        manageDisplay();
    }

    private void manageDisplay() {
        txtSKU.setEnabled(false);
        txtMRP.setEnabled(false);
        txtSaleRate.setEnabled(false);
        txtRate.setEnabled(false);
    }

    private void onAdd() {
        clear();
        manageDisplay();
        txtHDepartment.setFocus(true);
    }

    private void clear() {
        txtSaleRateDisplay.setText("");
        txtSaleRateDisplay.setTag("");
        txtSaleRateDisplay.setTag2nd("");
        txtMRPDisplay.setText("");
        txtRateDisplay.setText("");
    }

    private void resetMedicine() {
        isBestBefore = "";
        selectedMedicineCode = "";

        txtSKU.setText("");
        txtSKU.setTag("");
        txtSKU.setTag2nd("");
        purchaseUnitConversionRate = "";

        txtHBatchNo.setText("");
        txtHBatchNo.setTag("");
        txtHBatchNo.setTag2nd("");
        resetBatchNo();
    }

    private void resetBatchNo() {
        txtExpiryDate.setText("");
        txtExpiryDate.setTag("");
        txtExpiryDate.setTag2nd("");

        txtRate.setText("");
        txtRate.setTag("");
        txtRate.setTag2nd("");

        txtMRP.setText("");
        txtMRP.setTag("");
        txtMRP.setTag2nd("");

        txtSaleRate.setText("");
        txtSaleRate.setTag("");
        txtSaleRate.setTag2nd("");

    }

}
