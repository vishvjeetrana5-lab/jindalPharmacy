package aarogya.pharmacy.environmentSettingSite.client;

import aarogya.pharmacy.environmentSettingSite.shared.*;
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import dataman.dmBase.dmFileManager.shared.DmIFile_Mdl;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmBase.dmSession.client.DmRequestBuilder;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmFileUploadButton.shared.IFileUploadNotifier;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import java.util.HashMap;
import java.util.List;

public final class EnvironmentSettingSite extends EnvironmentSettingSite_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, BlurHandler, KeyDownHandler {

    private final ServiceInvoker<EnvironmentSettingSite_SrvAsync> srvAsync;
    private String imageFilePath = "", tempURL = "", destSiteName = "", serverFileName = "", ServerDirectory = "", destSiteCode = "", isCopy = "";
    private final DepartmentSiteLinkGridEdit<GrdLinkDepartment_Mdl, GrdLinkDepartment_PrpAcs> departmentSiteLinkEdit = new DepartmentSiteLinkGridEdit(grdLinkDepartment);

    private void textHelpEvents() {
        txtHSite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        topControlMoveRec(DmShared.xNull(txtHSite.getTag()));
                        destSiteCode = txtHSite.getTag();
                        destSiteName = txtHSite.getText();
                    }
                }
            }
        });

        txtHPharmacyFinalDiscountAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPharmacyFinalDiscountAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPharmacyFinalDiscountAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPharmacyFinalDiscountAc).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPharmacyFinalDiscountOH.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getPharmacyFinalDiscountOH(DmShared.xNull(txtHSite.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPharmacyFinalDiscountOH).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPharmacyFinalDiscountOH.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPharmacyFinalDiscountOH).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPatAlphaCase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAddAlphaCase(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPatAlphaCase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPatAlphaCase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPatAlphaCase).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAddAlphaCase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAddAlphaCase(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAddAlphaCase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAddAlphaCase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAddAlphaCase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSiteCopy.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSiteCopy.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus) {
                    if (t1Row != null) {
                        ServerDirectory = imageFilePath + "/" + t1Row.getSharedKeyFieldCode();
                        serverFileName = EnvironmentSettingSite_Mdl.getReceiptLogo();
                        isCopy = t1Row.getSharedKeyFieldCode();
                        topControlMoveRec(t1Row.getSharedKeyFieldCode());
                        txtHSite.setText(DmShared.xNull(destSiteName));
                        txtHSite.setTag(DmShared.iNull(destSiteCode).toString());
                    }
                }
            }
        });

        txtHAllowReturnWithooutSale.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAllowReturnWithooutSale).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAllowReturnWithooutSale.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAllowReturnWithooutSale).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsIncludeTaxInCost.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsIncludeTaxInCost).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsIncludeTaxInCost.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsIncludeTaxInCost).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPharmacyControlAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPharmacyControlAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPharmacyControlAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPharmacyControlAc).show();
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

        txtHAddress.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAddress).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAddress.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAddress).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHMedicineAlphaCase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAddAlphaCase(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineAlphaCase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineAlphaCase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineAlphaCase).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPharmacyHospitalAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPharmacyHospitalAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPharmacyHospitalAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPharmacyHospitalAc).show();
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

        txtHPharmacySelfAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPharmacySelfAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPharmacySelfAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPharmacySelfAc).show();
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

        txtHMedicineSaleOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHMedicineSaleOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMedicineSaleOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMedicineSaleOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMedicineSaleOn).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPHAllowPatientForSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPHAllowPatientForSite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPHAllowPatientForSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPHAllowPatientForSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHPHAllowSaleBillForSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPHAllowSaleBillForSite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPHAllowSaleBillForSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPHAllowSaleBillForSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHDefaultService.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getServices(helper, blnToEndOfFile, null, "", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefaultService).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefaultService.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefaultService).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPharmacyBarcodeActive.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPharmacyBarcodeActive).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPharmacyBarcodeActive.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPharmacyBarcodeActive).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHAutoInTransitByMedicineTransfer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAutoInTransitByMedicineTransfer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAutoInTransitByMedicineTransfer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAutoInTransitByMedicineTransfer).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSRateEditInPurchase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSRateEditInPurchase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSRateEditInPurchase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSRateEditInPurchase).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHCentralStore.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCentralStore(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCentralStore).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCentralStore.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCentralStore).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
            }
        });
    }

    private void gridEvents() {

        departmentSiteLinkEdit.addCallBack(new DepartmentSiteLinkGridEdit.CallBack() {
            @Override
            public void onOk(GrdLinkDepartment_Mdl departmentDetail_Mdl) {
                grdLinkDepartment.commitChanges();
            }

            @Override
            public void onDelete() {
                grdLinkDepartment.commitChanges();
            }
        });
        grdLinkDepartment.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGridEdit(event.getRowIndex(), grdLinkDepartment);
            }
        });
        grdLinkDepartment.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGridEdit(grdLinkDepartment.getGrid().getStore().indexOf(grdLinkDepartment.getGrid().getSelectionModel().getSelectedItem()), grdLinkDepartment);
                }
            }
        }, KeyDownEvent.getType());

    }

    private void showGridEdit(int currentRowIndex, DmGrid grd) {
        if (grd.equals(grdLinkDepartment)) {
            departmentSiteLinkEdit.show(currentRowIndex, true, true, true);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {

    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnSave)) {
                topControlSave();
            }
            if (event.getSource().equals(btnClose)) {
                onClose();
            } else if (event.getSource().equals(imgView)) {
                startDownloading(EnvironmentSettingSite_Mdl.getReceiptLogo(), imageFilePath + "/" + DmShared.iNull(txtHSite.getTag()).toString());
            } else if (event.getSource().equals(btnCopy)) {
                txtHSiteCopy.show(btnCopy, "");
                txtHSiteCopy.reQuery("");

            } else if (event.getSource().equals(btnDelete)) {
                serverFileName = "";
                ServerDirectory = "";
                flwImage.clear();

            }
        } catch (DmException e) {
        }

    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (!txtHSite.getTag().equalsIgnoreCase("")) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_CTRL && event.getNativeKeyCode() == KeyCodes.KEY_S) {
                topControlSave();
            }
        }
    }

    public EnvironmentSettingSite(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        srvAsync = new ServiceInvoker<>(GWT.create(EnvironmentSettingSite_Srv.class), EnvironmentSettingSite.class.getName());
        this.asWidget().setTitle(widgetName);
        toolContainer.setTitle(this.asWidget().getTitle());

        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCopy.addClickHandler(this);
        btnDelete.addClickHandler(this);
        txtUploadStatus.setEnabled(false);

        imgView.addClickHandler(this);
        textHelpEvents();
        flwForm.addAttachHandler(this);
        gridEvents();
        manageEvent();
        defaultValues(swi);
        //grdGeneralTab();
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private EnvironmentSettingSite_Mdl topControlSaveInitialize() {
        EnvironmentSettingSite_Mdl mrTemp = new EnvironmentSettingSite_Mdl();

        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));
        mrTemp.setAddressAlphabateCaseCode(DmShared.xNull(txtHAddAlphaCase.getTag()));
        mrTemp.setAddressAlphabateCaseName(DmShared.xNull(txtHAddAlphaCase.getText()));
        mrTemp.setPatientAlphabateCaseCode(DmShared.xNull(txtHPatAlphaCase.getTag()));
        mrTemp.setPatientAlphabateCaseName(DmShared.xNull(txtHPatAlphaCase.getText()));
        mrTemp.setMedicineAlphabateCaseCode(DmShared.xNull(txtHMedicineAlphaCase.getTag()));
        mrTemp.setMedicineAlphabateCaseName(DmShared.xNull(txtHMedicineAlphaCase.getText()));

        mrTemp.setAllowReturnWithoutSaleCode(DmShared.xNull(txtHAllowReturnWithooutSale.getTag()));
        mrTemp.setAllowReturnWithoutSaleName(DmShared.xNull(txtHAllowReturnWithooutSale.getText()));

        mrTemp.setIsIncludeTaxInCostTag(DmShared.xNull(txtHIsIncludeTaxInCost.getTag()));
        mrTemp.setIsIncludeTaxInCostText(DmShared.xNull(txtHIsIncludeTaxInCost.getText()));

        mrTemp.setPharmacyCentralStoreTag(DmShared.xNull(txtHCentralStore.getTag()));
        mrTemp.setPharmacyCentralStoreText(DmShared.xNull(txtHCentralStore.getText()));

        mrTemp.setServerFileName(serverFileName);

        mrTemp.setServerDirectory(ServerDirectory);
        mrTemp.setDestSiteCode(destSiteCode);

        // Pharmacy Tab
        mrTemp.setPharmacyHospitalAcTag(DmShared.xNull(txtHPharmacyHospitalAc.getTag()));
        mrTemp.setPharmacySelfAcTag(DmShared.xNull(txtHPharmacySelfAc.getTag()));
        mrTemp.setExpAlertBeforeDays(DmShared.xNull(txtExpiryAlertDays.getText()));
        mrTemp.setMedicineSaleOnText(DmShared.xNull(txtHMedicineSaleOn.getText()));
        mrTemp.setMedicineSaleOnTag(DmShared.xNull(txtHMedicineSaleOn.getTag()));
        mrTemp.setPhAllowPatientForSiteTag(DmShared.xNull(txtHPHAllowPatientForSite.getTag()));
        mrTemp.setPhAllowSaleBillForSiteTag(DmShared.xNull(txtHPHAllowSaleBillForSite.getTag()));

        mrTemp.setPharmacyFinalDiscountAcCode(DmShared.xNull(txtHPharmacyFinalDiscountAc.getTag()));
        mrTemp.setPharmacyFinalDiscountAcName(DmShared.xNull(txtHPharmacyFinalDiscountAc.getText()));
        mrTemp.setPharmacyFinalDiscountOHCode(DmShared.xNull(txtHPharmacyFinalDiscountOH.getTag()));
        mrTemp.setPharmacyFinalDiscountOHName(DmShared.xNull(txtHPharmacyFinalDiscountOH.getText()));

        mrTemp.setDefaultServiceTag(DmShared.xNull(txtHDefaultService.getTag()));
        mrTemp.setDefaultServiceText(DmShared.xNull(txtHDefaultService.getText()));

        mrTemp.setPharmacyControlAcTag(DmShared.xNull(txtHPharmacyControlAc.getTag()));
        mrTemp.setPharmacyControlAcText(DmShared.xNull(txtHPharmacyControlAc.getText()));

        mrTemp.setPharmacyBarcodeActive(DmShared.xNull(txtHPharmacyBarcodeActive.getTag()));
        mrTemp.setPharmacyBarcodeActiveName(DmShared.xNull(txtHPharmacyBarcodeActive.getText()));

        mrTemp.setAutoInTransitByMedicineTransfer(DmShared.xNull(txtHAutoInTransitByMedicineTransfer.getTag()));
        mrTemp.setAutoInTransitByMedicineTransferName(DmShared.xNull(txtHAutoInTransitByMedicineTransfer.getText()));

        mrTemp.setPh_SRateEditInPurchase(DmShared.xNull(txtHSRateEditInPurchase.getTag()));

        mrTemp.getGrdLinkDept().addAll(grdLinkDepartment.getGrid().getStore().getAll());

//        mrTemp.setDefaultPharmacySiteTag(DmShared.xNull(txtHDefPharmacySite.getTag()));
        mrTemp.setReturnDays(DmShared.xNull(txtReturnDays.getText()));

        return mrTemp;
    }

    private void topControlSave() {
        EnvironmentSettingSite_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            EnvironmentSettingSite_Vld.chkSave(mrTemp);

            srvAsync.invokeRPC().saveRecord(mrTemp, new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHSite).show();
                }

                @Override
                public void onSuccess(DmResult<String> result) {
                    if (result.isStatus()) {
                        topControlMoveRec(result.getData());
                        new DmAlertMessageBox("Record saved successfully", txtHSite).show();
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage());
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
                new DmAlertMessageBox(errorMessage, txtHSite).show();
                break;
            default:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtHSite).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<EnvironmentSettingSite_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<EnvironmentSettingSite_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<EnvironmentSettingSite_Mdl> result) {
        EnvironmentSettingSite_Mdl data;

        if (result.isStatus()) {
            data = result.getData();

            if (DmShared.xNull(data.getPatientAlphabateCaseCode()).equalsIgnoreCase("")) {
                txtHPatAlphaCase.setTag("U");
                txtHPatAlphaCase.setText("UPPER");
            } else {
                txtHPatAlphaCase.setTag(DmShared.xNull(data.getPatientAlphabateCaseCode()));
                txtHPatAlphaCase.setText(DmShared.xNull(data.getPatientAlphabateCaseName()));
            }

            if (DmShared.xNull(data.getAddressAlphabateCaseCode()).equalsIgnoreCase("")) {
                txtHAddAlphaCase.setTag("U");
                txtHAddAlphaCase.setText("UPPER");
            } else {
                txtHAddAlphaCase.setTag(DmShared.xNull(data.getAddressAlphabateCaseCode()));
                txtHAddAlphaCase.setText(DmShared.xNull(data.getAddressAlphabateCaseName()));
            }
            if (DmShared.xNull(data.getMedicineAlphabateCaseCode()).equalsIgnoreCase("")) {
                txtHMedicineAlphaCase.setTag("U");
                txtHMedicineAlphaCase.setText("UPPER");
            } else {
                txtHMedicineAlphaCase.setTag(DmShared.xNull(data.getMedicineAlphabateCaseCode()));
                txtHMedicineAlphaCase.setText(DmShared.xNull(data.getMedicineAlphabateCaseName()));
            }

            txtHPharmacyFinalDiscountAc.setTag(DmShared.xNull(data.getPharmacyFinalDiscountAcCode()));
            txtHPharmacyFinalDiscountAc.setText(DmShared.xNull(data.getPharmacyFinalDiscountAcName()));
            txtHPharmacyFinalDiscountOH.setTag(DmShared.xNull(data.getPharmacyFinalDiscountOHCode()));
            txtHPharmacyFinalDiscountOH.setText(DmShared.xNull(data.getPharmacyFinalDiscountOHName()));

            txtHAllowReturnWithooutSale.setTag(DmShared.xNull(data.getAllowReturnWithoutSaleCode()));
            txtHAllowReturnWithooutSale.setText(DmShared.xNull(data.getAllowReturnWithoutSaleName()));

            txtHIsIncludeTaxInCost.setTag(DmShared.xNull(data.getIsIncludeTaxInCostTag()));
            txtHIsIncludeTaxInCost.setText(DmShared.xNull(data.getIsIncludeTaxInCostText()));
//
//            txtHDefaultService.setTag(DmShared.xNull(data.getDefaultServiceTag()));
//            txtHDefaultService.setText(DmShared.xNull(data.getDefaultServiceText()));

            txtHPharmacyControlAc.setTag(DmShared.xNull(data.getPharmacyControlAcTag()));
            txtHPharmacyControlAc.setText(DmShared.xNull(data.getPharmacyControlAcText()));

            //        manageDsisplay();
            String dateUser = DmShared.xNull(data.getuName()) + "  -  " + (data.getDate()) + "  -  " + DmShared.xNull(data.getTime());
            userControl.setTextBlog(modifiedByBlog, dateUser);

            txtHSiteCopy.getDmGrid().getGrid().getData("");
            if (!DmShared.xNull(isCopy).equalsIgnoreCase("")) {
                loadImage(DmShared.iNull(isCopy).toString());
                ServerDirectory = imageFilePath + "/" + (DmShared.xNull(isCopy));
                serverFileName = (DmShared.xNull(EnvironmentSettingSite_Mdl.getReceiptLogo()));
                isCopy = "";
            } else {
                ServerDirectory = imageFilePath + "/" + (DmShared.xNull(txtHSite.getTag()));
                serverFileName = (DmShared.xNull(EnvironmentSettingSite_Mdl.getReceiptLogo()));
                loadImage(DmShared.xNull(txtHSite.getTag()));
            }

            //Pharamacy Tab
            txtHPharmacyHospitalAc.setTag(DmShared.xNull(data.getPharmacyHospitalAcTag()));
            txtHPharmacyHospitalAc.setText(DmShared.xNull(data.getPharmacyHospitalAcText()));
            txtHPharmacySelfAc.setTag(DmShared.xNull(data.getPharmacySelfAcTag()));
            txtHPharmacySelfAc.setText(DmShared.xNull(data.getPharmacySelfAcText()));
            txtExpiryAlertDays.setText(DmShared.xNull(data.getExpAlertBeforeDays()));
            txtHMedicineSaleOn.setTag(DmShared.xNull(data.getMedicineSaleOnTag()));
            txtHMedicineSaleOn.setText(DmShared.xNull(data.getMedicineSaleOnText()));
            txtHPHAllowPatientForSite.setTag(DmShared.xNull(data.getPhAllowPatientForSiteTag()));
            txtHPHAllowPatientForSite.setText(DmShared.xNull(data.getPhAllowPatientForSiteText()));
            txtHPHAllowSaleBillForSite.setTag(DmShared.xNull(data.getPhAllowSaleBillForSiteTag()));
            txtHPHAllowSaleBillForSite.setText(DmShared.xNull(data.getPhAllowSaleBillForSiteText()));
            txtHDefaultService.setTag(DmShared.iNull(data.getDefaultServiceTag()).toString());
            txtHDefaultService.setText(DmShared.xNull(data.getDefaultServiceText()));
            txtReturnDays.setText(DmShared.xNull(data.getReturnDays()));
            txtHPharmacyBarcodeActive.setTag(DmShared.xNull(data.getPharmacyBarcodeActive()));
            txtHPharmacyBarcodeActive.setText(DmShared.xNull(data.getPharmacyBarcodeActiveName()));
            
            txtHCentralStore.setTag(DmShared.xNull(data.getPharmacyCentralStoreTag()));
            txtHCentralStore.setText(DmShared.xNull(data.getPharmacyCentralStoreText()));

            txtHAutoInTransitByMedicineTransfer.setTag(DmShared.xNull(data.getAutoInTransitByMedicineTransfer()));
            txtHAutoInTransitByMedicineTransfer.setText(DmShared.xNull(data.getAutoInTransitByMedicineTransferName()));

            txtHSRateEditInPurchase.setTag(DmShared.xNull(data.getPh_SRateEditInPurchase()));
            txtHSRateEditInPurchase.setText(DmShared.xNull(data.getPh_SRateEditInPurchasetext()));

            grdLinkDepartment.getGrid().getStore().clear();
            grdLinkDepartment.getGrid().getStore().addAll(data.getGrdLinkDept());
            grdLinkDepartment.getGrid().getStore().add(new GrdLinkDepartment_Mdl());
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {

        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
            //  statusGUIEdit.hide();
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    public void defaultValues(final SetWidgetInvoker swi) {
        srvAsync.invokeRPC().getDefaultValue(new AsyncCallback<DmResult<DefaultValue_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DefaultValue_Mdl> result) {
                if (result.isStatus()) {
                    imageFilePath = DmShared.xNull(result.getData().getImagePath());
                    txtHSite.setTag(DmShared.xNull(result.getData().getSharedKeyFieldCode()));
                    txtHSite.setText(DmShared.xNull(result.getData().getSharedKeyFieldName()));
                    destSiteCode = DmShared.xNull(result.getData().getSharedKeyFieldCode());
                    destSiteName = DmShared.xNull(result.getData().getSharedKeyFieldName());

                    txtExpiryAlertDays.setText("0");

                    swi.setWidget(EnvironmentSettingSite.this);
                    topControlMoveRec(txtHSite.getTag());
                    txtHSite.setFocus(true);

                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void onClose() {
        new DmConfirmMessageBox("Are you sure? You want to close.", new DialogHideEvent.DialogHideHandler() {

            @Override
            public void onDialogHide(DialogHideEvent event) {
                if (event.getHideButton().equals(Dialog.PredefinedButton.YES)) {
                    DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(EnvironmentSettingSite.this);
                }
            }
        }).show();
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
            onClose();
        }
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_S && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            topControlSave();
        }
    }

    public void viewImage(String fileName, String path) {
        progressLbl.setText("");
        progressLbl.setVisible(false);
        flwImage.clear();
        flwImage.add(imgView);
//        String tempURL = "";
        tempURL = tempURL + "A";
        HashMap<String, String> params = new HashMap<>();
        params.put("u", DmRequestBuilder.getSessionId());
        params.put("f", fileName);
        params.put("p", path);
        params.put("temp", tempURL);
        params.put("w", "100"); //width
        params.put("h", "100"); //height
        imgView.setUrl("");
        imgView.setUrl(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileViewer_Srvlt", params));
    }

    public void loadImage(String siteCode) {
        viewImage(EnvironmentSettingSite_Mdl.getReceiptLogo(), imageFilePath + "/" + DmShared.iNull(siteCode).toString());
    }

    private void startDownloading(String fileName, String filePath) {
        try {
            flwImage.clear();
            flwImage.add(imgView);

            HashMap<String, String> params = new HashMap<>();
            params.put("u", DmRequestBuilder.getSessionId());
            params.put("f", fileName);
            params.put("p", filePath);
            Window.open(DmClient.concatURL("dataman/dmToolkit/dmFileUploadButton/servlets/DmFileDownloader_Srvlt", params), "", "");
        } catch (Exception ex) {
        }
    }

    private void manageEvent() {
        btnUpload.setNotifier(new IFileUploadNotifier() {
            @Override
            public void notifyForUpload(String notifier) {
                progressLbl.setText(notifier);
                flwImage.add(progressLbl);

            }

            @Override
            public void processAfterUploaded(DmIFile_Mdl file) {
                serverFileName = file.getServerFileName();
                ServerDirectory = file.getServerDirectory();
                viewImage(serverFileName, ServerDirectory);
            }
        });

    }

}
