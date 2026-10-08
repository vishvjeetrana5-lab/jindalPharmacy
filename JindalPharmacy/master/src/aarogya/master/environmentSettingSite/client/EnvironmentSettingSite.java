package aarogya.master.environmentSettingSite.client;

import aarogya.master.environmentSettingSite.shared.*;
import aarogya.selfToolkit.stStatic.shared.STEnum.*;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class EnvironmentSettingSite extends EnvironmentSettingSite_Dsg implements AttachEvent.Handler, ClickHandler, DmRootEvents, BlurHandler, KeyDownHandler {

    private final ServiceInvoker<EnvironmentSettingSite_SrvAsync> srvAsync;
    private String imageFilePath = "", tempURL = "", destSiteName = "", serverFileName = "", ServerDirectory = "", destSiteCode = "",
            isCopy = "";

    private final StatusGUITempGridEdit<GrdStatusGUITemp_Mdl, GrdStatusGUITemp_PrpAcs> statusGUIEdit = new StatusGUITempGridEdit(grdDischargeStatusGUI);
    private final DepartmentSiteLinkGridEdit<GrdLinkDepartment_Mdl, GrdLinkDepartment_PrpAcs> departmentSiteLinkEdit = new DepartmentSiteLinkGridEdit(grdLinkDepartment);
    private final DiagnosticFileGridEdit<GrdDiagnosticFile_Mdl, GrdDiagnosticFile_PrpAcs> diagnosticFileEdit = new DiagnosticFileGridEdit(grdDiagnosticFile, fileRuleTypeDocument);

    protected List<GrdQueueSequence_Mdl> lstQueue = new ArrayList<GrdQueueSequence_Mdl>();
//    private final TimingGridEdit<GrdTiming_Mdl, GrdTiming_PrpAcs> timingGridEdit = new TimingGridEdit(grdTimingDetail);
    private final QueuePopup queuePopup = new QueuePopup();
//    private DefaultValue_Mdl defaultValue;

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

        txtHEodPosting.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHEodPosting(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEodPosting).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEodPosting.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEodPosting).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHServiceUnitEditable.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHServiceUnitEditable(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHServiceUnitEditable).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHServiceUnitEditable.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHServiceUnitEditable).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOPDRefConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHReferred(DmShared.xNull(txtHSite.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDRefConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDRefConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDRefConsultant).show();
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

        txtHOPDConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(DmShared.xNull(txtHSite.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDConsultant).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDConsultant.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDConsultant).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsExistingPatientDiagnostic.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsExistingPatientDiagnostic).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsExistingPatientDiagnostic.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsExistingPatientDiagnostic).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHIsBillH_Approval.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsBillH_Approval).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsBillH_Approval.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsBillH_Approval).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHIsCommonPricelist.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsCommonPricelist).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsCommonPricelist.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsCommonPricelist).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHIsExistingPatient.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsExistingPatient).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsExistingPatient.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsExistingPatient).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHroomMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHroomMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHroomMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHroomMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOPDCase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCase(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDCase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDCase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDCase).show();
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

        txtHCity.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<TxtHCity_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCity(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCity_Mdl>>>() {

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
            }

        });

        txtHCountry.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHNationality(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCountry).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCountry.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCountry).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHSecondaryCurrency.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSecondaryCurrency(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSecondaryCurrency).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSecondaryCurrency.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSecondaryCurrency).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOPDRelegionMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDRelegionMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDRelegionMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDRelegionMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHDirectPrescription.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDirectPrescription(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDirectPrescription).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDirectPrescription.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDirectPrescription).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOPDIdTypeMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDIdTypeMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDIdTypeMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDIdTypeMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIPDInfantUnit.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHInfantUnit(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIPDInfantUnit).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIPDInfantUnit.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIPDInfantUnit).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {

            }
        });

        txtHIPDRoomClean.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHRoomVacant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIPDRoomClean).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIPDRoomClean.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIPDRoomClean).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIPDIsAutoRoom.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIsManualRoom(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIPDIsAutoRoom).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIPDIsAutoRoom.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIPDIsAutoRoom).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
//                    manageDisplay();
                }
            }
        });

        txtHTimeFormatRoom.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHTimeFormatRoom(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTimeFormatRoom).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTimeFormatRoom.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTimeFormatRoom).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended tRow) {
                if (blnStatus && tRow != null) {
                    selectTimeFormat();
                }

            }
        });

        txtHIPDControlAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIPDControlAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIPDControlAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIPDControlAc).show();
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

        txtHOPDControlAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIPDControlAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDControlAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDControlAc).show();
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

        txtHDiagnosticControlAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDiagnosticControlAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDiagnosticControlAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDiagnosticControlAc).show();
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

        txtHDisallowType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDisallowType(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDisallowType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDisallowType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDisallowType).show();
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

        txtHDirectSampleLabColl.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDirectSampleLabColl).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDirectSampleLabColl.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDirectSampleLabColl).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsDiagAuto.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsDiagAuto).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsDiagAuto.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsDiagAuto).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPrntYsNo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPrntYsNo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrntYsNo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPrntYsNo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPatientInitial.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPatientInitial).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPatientInitial.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPatientInitial).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
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
        txtHGuardianInitial.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGuardianInitial).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGuardianInitial.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGuardianInitial).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHCityMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCityMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCityMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCityMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHMaritalStatus.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHMaritalStatus).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHMaritalStatus.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHMaritalStatus).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHCasteMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCasteMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCasteMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCasteMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHBloodGroup.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHBloodGroup).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHBloodGroup.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHBloodGroup).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHEmail.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmail).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEmail.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmail).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHEmergencyControlAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHEmergencyControlAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHEmergencyControlAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHEmergencyControlAc).show();
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
        txtHDayCareControlAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDayCareControlAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDayCareControlAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDayCareControlAc).show();
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

        txtHDiagnosticHospitalAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDiagnosticHospitalAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDiagnosticHospitalAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDiagnosticHospitalAc).show();
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
        txtHDiagnosticSelfAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDiagnosticSelfAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDiagnosticSelfAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDiagnosticSelfAc).show();
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

        TxtHDiagnosticRegAddition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getServices(helper, blnToEndOfFile, new ServiceNature[]{ServiceNature.OPERATION}, "", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), TxtHDiagnosticRegAddition).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            TxtHDiagnosticRegAddition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), TxtHDiagnosticRegAddition).show();
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

        TxtHBloodBankCipAddition.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getServices(helper, blnToEndOfFile, new ServiceNature[]{ServiceNature.OVERHEAD_SERVICE, ServiceNature.SERVICE}, "", new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), TxtHBloodBankCipAddition).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            TxtHBloodBankCipAddition.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), TxtHBloodBankCipAddition).show();
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

        txtHOrgReceiptCard.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCardType(null, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOrgReceiptCard).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOrgReceiptCard.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOrgReceiptCard).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
        txtHCardType.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCardType(DmEnum.CardType.WALLET, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHCardType).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHCardType.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHCardType).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHHospitalHoldCard.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHCardType(null, helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHospitalHoldCard).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHospitalHoldCard.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHospitalHoldCard).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPatientImgMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPatientImgMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPatientImgMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPatientImgMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHAreaMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAreaMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAreaMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAreaMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHGuardianNameMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGuardianNameMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGuardianNameMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGuardianNameMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHPrintedOnLetterhead.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPrintedOnLetterhead).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrintedOnLetterhead.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPrintedOnLetterhead).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHHourlyServiceRoundOff.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHHourlyServiceRoundOff(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHHourlyServiceRoundOff).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHHourlyServiceRoundOff.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHHourlyServiceRoundOff).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHTokenBlock.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTokenBlockNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHTokenBlock).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHTokenBlock.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHTokenBlock).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHOPDAllowPatientForSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHOPDAllowPatientForSite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHOPDAllowPatientForSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHOPDAllowPatientForSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHDefPharmacySite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefPharmacySite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefPharmacySite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefPharmacySite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHLockVoucherPrefix.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHVoucherPrefix(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHLockVoucherPrefix).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHLockVoucherPrefix.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHLockVoucherPrefix).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHIPDAllowPatientForSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIPDAllowPatientForSite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIPDAllowPatientForSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIPDAllowPatientForSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHDiagAllowPatientForSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDiagAllowPatientForSite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDiagAllowPatientForSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDiagAllowPatientForSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });
        txtHDAYCAllowPatientForSite.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHSite(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDAYCAllowPatientForSite).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDAYCAllowPatientForSite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDAYCAllowPatientForSite).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHDefPharmacyDepartment.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, true, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDefPharmacyDepartment).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDefPharmacyDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDefPharmacyDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHInvDefaultPurchaseDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHInvDefaultPurchaseDepartment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHInvDefaultPurchaseDepartment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHInvDefaultPurchaseDepartment).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPackage.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPackage(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPackage).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPackage.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPackage).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                }
            }
        });

//        txtHBooldBagDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHBooldBagDepartment).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
//                        if (result.isStatus()) {
//                            txtHBooldBagDepartment.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHBooldBagDepartment).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
//            }
//        });
//        txtHComponentDepartment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
//            @Override
//            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
//                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepartment(helper, blnToEndOfFile, false, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
//
//                    @Override
//                    public void onFailure(Throwable caught) {
//                        new DmAlertMessageBox(caught.getMessage(), txtHComponentDepartment).show();
//                    }
//
//                    @Override
//                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
//                        if (result.isStatus()) {
//                            txtHComponentDepartment.setData(result.getData(), result.getPaging());
//                        } else {
//                            new DmAlertMessageBox(result.getErrorMessage(), txtHComponentDepartment).show();
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
//            }
//        });
        txtHDonorCampCode.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDonorCampCode(DmShared.xNull(txtHSite.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDonorCampCode).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDonorCampCode.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDonorCampCode).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHQueueAlgo.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHQueueAlgoNature(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHQueueAlgo).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHQueueAlgo.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQueueAlgo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                if (blnStatus && t1Row != null) {
                    queuePopup.show(btnQueue, lstQueue);
                }
            }
        });
        txtHConsultationRegistration.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHConsulatationRegistration(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHConsultationRegistration).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHConsultationRegistration.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHQueueAlgo).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        queuePopup.addCallBack(new QueuePopup.CallBack() {
            @Override
            public void onOk(List<GrdQueueSequence_Mdl> lstSequence) {
                if (lstSequence.size() > 0) {
                    lstQueue = new ArrayList<GrdQueueSequence_Mdl>();
                    for (GrdQueueSequence_Mdl queue : lstSequence) {
                        lstQueue.add(queue.clone());
                    }
                }
            }

            @Override
            public void onClose() {
//                txtRemark.setFocus(true);
            }
        });

        txtHReceiptGenerateOnFullDiscount.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHReceiptGenerateOnFullDiscount).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHReceiptGenerateOnFullDiscount.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHReceiptGenerateOnFullDiscount).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIsOtpRequiredForRefund.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIsOtpRequiredForRefund).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIsOtpRequiredForRefund.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIsOtpRequiredForRefund).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIcdxMandatory.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIcdxMandatory).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIcdxMandatory.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIcdxMandatory).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAppointmentPaymentGatewayAllowed.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAppointmentPaymentGatewayAllowed).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAppointmentPaymentGatewayAllowed.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAppointmentPaymentGatewayAllowed).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHPaymentOption.addTextHelpMultiCallBack(new DmTextHelpMulti.TextHelpMultiCallBack<KeyFieldExtended>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPaymentOption(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPaymentOption).show();

                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPaymentOption.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPaymentOption).show();
                        }
                    }
                });
            }

            @Override
            public void getSelection(boolean blnStatus, String keyCode, String keyName) {
            }
        });

        txtHAutoOPDRegAfterAppointment.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAutoOPDRegAfterAppointment).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAutoOPDRegAfterAppointment.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAutoOPDRegAfterAppointment).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });
        txtHRunFromWebsite.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHyesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRunFromWebsite).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRunFromWebsite.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRunFromWebsite).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHIncludeTaxInCost.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIncludeTaxInCost).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIncludeTaxInCost.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIncludeTaxInCost).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPurchaseSalesAcPostingBasedOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHPurchaseSalesAcPostingBasedOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPurchaseSalesAcPostingBasedOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPurchaseSalesAcPostingBasedOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPurchaseSalesAcPostingBasedOn).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHIndentRequired.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHIndentRequired(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHIndentRequired).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHIndentRequired.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHIndentRequired).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHRequisitionMandatoryInTransfer.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHRequisitionMandatoryInTransfer).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHRequisitionMandatoryInTransfer.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHRequisitionMandatoryInTransfer).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHSinglePOFlowInPurchase.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getHYesNo(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHSinglePOFlowInPurchase).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHSinglePOFlowInPurchase.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHSinglePOFlowInPurchase).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHAutoBedConfirmation.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAutoBedConfirmation(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAutoBedConfirmation).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAutoBedConfirmation.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAutoBedConfirmation).show();
                        }
                    }
                });

            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
            }
        });

        txtHAssetDepreciationAc.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHAccountName(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHAssetDepreciationAc).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHAssetDepreciationAc.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHAssetDepreciationAc).show();
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

        txtHDepreciationPostOn.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                srvAsync.invokeRPC(blnCallBackWait).getTxtHDepreciationPostOn(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHDepreciationPostOn).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHDepreciationPostOn.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHDepreciationPostOn).show();
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
    }

    private void gridEvents() {

        statusGUIEdit.addCallBack(new StatusGUITempGridEdit.CallBack() {
            @Override
            public void onOk(GrdStatusGUITemp_Mdl StatusGUITemp_Mdl) {
                grdDischargeStatusGUI.commitChanges();
            }

            @Override
            public void onDelete() {
                grdDischargeStatusGUI.commitChanges();
            }
        });
        grdDischargeStatusGUI.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showGridEdit(event.getRowIndex(), grdDischargeStatusGUI);
            }
        });
        grdDischargeStatusGUI.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showGridEdit(grdDischargeStatusGUI.getGrid().getStore().indexOf(grdDischargeStatusGUI.getGrid().getSelectionModel().getSelectedItem()), grdDischargeStatusGUI);
                }
            }
        }, KeyDownEvent.getType());

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

        diagnosticFileEdit.addCallBack(new DiagnosticFileGridEdit.CallBack() {
            @Override
            public void onOk(GrdDiagnosticFile_Mdl diagnosticFile_Mdl) {
//                int a = grdDiagnosticFile.getSelectedCell().getRow();
//                grdDiagnosticFile.getGrid().getStore().remove(a);
//                grdDiagnosticFile.getGrid().getStore().add(a, diagnosticFile_Mdl);
                grdDiagnosticFile.commitChanges();
            }

            @Override

            public void onDelete() {

            }
        });
        grdDiagnosticFile.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
            @Override
            public void onRowClick(RowClickEvent event) {
                showDiagnosticGridEdit(event.getRowIndex());
            }
        });
        grdDiagnosticFile.getGrid().addDomHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
                    showDiagnosticGridEdit(grdDiagnosticFile.getGrid().getStore().indexOf(grdDiagnosticFile.getGrid().getSelectionModel().getSelectedItem()));
                }
            }
        }, KeyDownEvent.getType());

//        timingGridEdit.addCallBack(new TimingGridEdit.CallBack() {
//
//            @Override
//            public void onOk(GrdTiming_Mdl grdTime) {
//                grdTimingDetail.commitChanges();
//            }
//
//            @Override
//            public void onDelete() {
//                grdTimingDetail.commitChanges();
//            }
//
//        });
//        grdTimingDetail.getGrid().addRowClickHandler(new RowClickEvent.RowClickHandler() {
//
//            @Override
//            public void onRowClick(RowClickEvent event) {
//                gridTimingEdit(event.getRowIndex());
//            }
//        });
//        grdTimingDetail.getGrid().addDomHandler(new KeyDownHandler() {
//
//            @Override
//            public void onKeyDown(KeyDownEvent event) {
//                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !event.getNativeEvent().getCtrlKey()) {
//                    gridTimingEdit(grdTimingDetail.getGrid().getStore().indexOf(grdTimingDetail.getGrid().getSelectionModel().getSelectedItem()));
//                }
//            }
//        }, KeyDownEvent.getType());
    }

//    private void gridTimingEdit(int currentRowIndex) {
//
//        timingGridEdit.show(currentRowIndex, true, true, true);
//
//    }
    private void showDiagnosticGridEdit(int currentRowIndex) {
        diagnosticFileEdit.show(currentRowIndex, this);
    }

    private void showGridEdit(int currentRowIndex, DmGrid grd) {
        if (grd.equals(grdDischargeStatusGUI)) {
            statusGUIEdit.show(currentRowIndex, true, false, false);
        } else if (grd.equals(grdLinkDepartment)) {
            departmentSiteLinkEdit.show(currentRowIndex, true, true, true);
        }
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource().equals(txtIncludeDDInMonth)) {
            if (DmShared.vNull(txtIncludeDDInMonth.getText()) > 11) {
                txtIncludeDDInMonth.setText("");
                new DmAlertMessageBox("Please enter value less than 12.", txtIncludeDDInMonth).show();
                txtIncludeDDInMonth.setFocus(true);
                return;
            }
        }
        if (event.getSource().equals(txtTransactionLockDate)) {
            txtTransactionLockDate.setText(DmClient.returnDate(txtTransactionLockDate.getText()));
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        try {
            if (event.getSource().equals(btnQueue)) {
                queuePopup.show(btnQueue, lstQueue);
            }
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
            } //            else if (event.getSource().equals(btnDownloadDocument)) {
            //                downloadDocument();
            //            }
            else if (event.getSource().equals(btnExport)) {
                new DmConfirmMessageBox("Are you sure? You want to export records.", new DialogHideEvent.DialogHideHandler() {

                    @Override
                    public void onDialogHide(DialogHideEvent event) {
                        if (event.getHideButton() == Dialog.PredefinedButton.YES) {
                            exportJSON();
                        }
                    }
                }).show();
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
        textHelpEvents();
        flwForm.addAttachHandler(this);
        gridEvents();
        manageEvent();
        defaultValues(swi);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    private EnvironmentSettingSite_Mdl topControlSaveInitialize() {
        EnvironmentSettingSite_Mdl mrTemp = new EnvironmentSettingSite_Mdl();

        mrTemp.setIsReceiptGenerateWithFullDiscountTag(DmShared.xNull(txtHReceiptGenerateOnFullDiscount.getTag()));
        mrTemp.setIsReceiptGenerateWithFullDiscountText(DmShared.xNull(txtHReceiptGenerateOnFullDiscount.getText()));
        mrTemp.setSharedKeyFieldCode(DmShared.xNull(txtHSite.getTag()));
        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtHSite.getText()));
        mrTemp.setAddressAlphabateCaseCode(DmShared.xNull(txtHAddAlphaCase.getTag()));
        mrTemp.setAddressAlphabateCaseName(DmShared.xNull(txtHAddAlphaCase.getText()));
        mrTemp.setPatientAlphabateCaseCode(DmShared.xNull(txtHPatAlphaCase.getTag()));
        mrTemp.setPatientAlphabateCaseName(DmShared.xNull(txtHPatAlphaCase.getText()));
        mrTemp.setOpdCaseCode(DmShared.xNull(txtHOPDCase.getTag()));
        mrTemp.setOpdCaseName(DmShared.xNull(txtHOPDCase.getText()));
        mrTemp.setOpdConsultantCode(DmShared.xNull(txtHOPDConsultant.getTag()));
        mrTemp.setOpdConsultantName(DmShared.xNull(txtHOPDConsultant.getText()));
        mrTemp.setOpdRefConsultantCode(DmShared.xNull(txtHOPDRefConsultant.getTag()));
        mrTemp.setOpdRefConsultantName(DmShared.xNull(txtHOPDRefConsultant.getText()));
        mrTemp.setDefaultRegFee(DmShared.xNull(txtDefaultRegFee.getText()));
        mrTemp.setIncludeDDInAge(DmShared.xNull(txtIncludeDDInMonth.getText()));
        mrTemp.setIncludeMMInAge(DmShared.xNull(txtIncludeMMInMonth.getText()));
        mrTemp.setCancelDaysOPD(DmShared.xNull(txtCancelDaysOPD.getText()));
        mrTemp.setServiceDaysOPD(DmShared.xNull(txtServiceDaysOPD.getText()));
        mrTemp.setHistoryViewDays(DmShared.xNull(txtHistoryViewDays.getText()));
        mrTemp.setAppointmentHistoryViewDays(DmShared.xNull(txtAppointmentHistoryViewDays.getText()));

        mrTemp.setDocumentFooter(DmShared.xNull(txtDocumentFooter.getText()));
        mrTemp.setDocumentFooterRegional(DmShared.xNull(txtDocumentFooterRegional.getText()));
        mrTemp.setDocumentHeader(DmShared.xNull(txtDocumentHeader.getText()));
        mrTemp.setDirectPrescriptionTag(DmShared.xNull(txtHDirectPrescription.getTag()));
        mrTemp.setServerFileName(serverFileName);
        mrTemp.setCityCode(DmShared.xNull(txtHCity.getTag()));

        mrTemp.setIpdControlAcTag(DmShared.xNull(txtHIPDControlAc.getTag()));
        mrTemp.setOpdControlAcTag(DmShared.xNull(txtHOPDControlAc.getTag()));
        mrTemp.setDiagnosticControlAcTag(DmShared.xNull(txtHDiagnosticControlAc.getTag()));
        mrTemp.setEmergencyControlAcTag(DmShared.xNull(txtHEmergencyControlAc.getTag()));
        mrTemp.setDayCareControlAcTag(DmShared.xNull(txtHDayCareControlAc.getTag()));
        mrTemp.setEodPostingTypeCode(DmShared.xNull(txtHEodPosting.getTag()));

        mrTemp.setNationalityTag(DmShared.xNull(txtHCountry.getTag()));
        mrTemp.setNationalityText(DmShared.xNull(txtHCountry.getText()));

        mrTemp.setIpdInfantUnitTag(DmShared.xNull(txtHIPDInfantUnit.getTag()));
        mrTemp.setIpdInfantUnitText(DmShared.xNull(txtHIPDInfantUnit.getText()));

        mrTemp.setIpdIsAutoRoomTag(DmShared.xNull(txtHIPDIsAutoRoom.getTag()));
        mrTemp.setIpdIsAutoRoomText(DmShared.xNull(txtHIPDIsAutoRoom.getText()));

        mrTemp.setIpdHalfDayUpToMin(DmClient.vNull(txtIPDHalfDayUpTo.getText(), "0"));
        mrTemp.setIpdHalfDayPercentage(DmShared.xNull(txtPercentageRoom.getText()));

        mrTemp.setIpdTimeFormatRoomTag(DmShared.xNull(txtHTimeFormatRoom.getTag()));
        mrTemp.setIpdTimeFormatRoomText(DmShared.xNull(txtHTimeFormatRoom.getText()));
        mrTemp.setIpdCheckoutTime(DmShared.xNull(txtTimeCheckout.getText()));
        mrTemp.setIpdGraceMinuteIn(DmShared.xNull(txtGraceMinuteIn.getText()));
        mrTemp.setIpdGraceMinuteOut(DmShared.xNull(txtGraceMinuteOut.getText()));
        mrTemp.setIpdNoOfSticker(DmClient.vNull(txtIPDNoOfSticker.getText(), "0"));
        mrTemp.setIpdAllowPatientForSiteTag(DmShared.xNull(txtHIPDAllowPatientForSite.getTag()));

        mrTemp.setServerDirectory(ServerDirectory);
        mrTemp.setDestSiteCode(destSiteCode);

        mrTemp.setTokenBlockTag(DmShared.xNull(txtHTokenBlock.getTag()));
        mrTemp.setOpdIsReligionMandatory(DmShared.xNull(txtHOPDRelegionMandatory.getTag()));
        mrTemp.setOpdIsIdTypeMandatory(DmShared.xNull(txtHOPDIdTypeMandatory.getTag()));
        mrTemp.setIpdIsRoomCleanTag(DmShared.xNull(txtHIPDRoomClean.getTag()));
        mrTemp.setOpdNoOfSticker(DmClient.vNull(txtOPDNoOfSticker.getText(), "0"));

        mrTemp.setPanCardLimit(DmClient.vNull(txtPancardLimit.getText(), "0"));
        mrTemp.setCashPaymentLimit(DmClient.vNull(txtCashpaymentLimit.getText(), "0"));
        mrTemp.setIsExistingPatientTag(DmShared.xNull(txtHIsExistingPatient.getTag()));
        mrTemp.setIsLABExistingPatientTag(DmShared.xNull(txtHIsExistingPatientDiagnostic.getTag()));
        mrTemp.setIsBillHApprovalTag(DmShared.xNull(txtHIsBillH_Approval.getTag()));
        mrTemp.setIsCommonPricelistForSiteTag(DmShared.xNull(txtHIsCommonPricelist.getTag()));
        mrTemp.setHourlyServiceRoundOffTag(DmShared.xNull(txtHHourlyServiceRoundOff.getTag()));

        mrTemp.setNewBornDay(DmClient.vNull(txtNewBornDay.getText(), "0"));
        mrTemp.setNewBornMonth(DmClient.vNull(txtNewBornMonth.getText(), "0"));
        mrTemp.setNewBornYear(DmClient.vNull(txtNewBornYear.getText(), "0"));
        mrTemp.setInfantMonth(DmClient.vNull(txtInfantMonth.getText(), "0"));
        mrTemp.setInfantYear(DmClient.vNull(txtInfantYear.getText(), "0"));
        mrTemp.setChildMonth(DmClient.vNull(txtChildMonth.getText(), "0"));
        mrTemp.setChildYear(DmClient.vNull(txtChildYear.getText(), "0"));
        mrTemp.setDisallowTypeTag(DmShared.xNull(txtHDisallowType.getTag()));
        mrTemp.setSecodaryCurrencyTag(DmShared.xNull(txtHSecondaryCurrency.getTag()));

        mrTemp.setTransactionLockDate(DmShared.xNull(txtTransactionLockDate.getText()));
        mrTemp.setTransactionLockYear(DmShared.xNull(txtHLockVoucherPrefix.getTag()));

        // Diagnostic Tab
        mrTemp.setDiagnosticNoOfSticker(DmClient.vNull(txtDiagnosticNoOfSticker.getText(), "0"));
        mrTemp.setDiagnosticSampleCollectionTag(DmShared.xNull(txtHDirectSampleLabColl.getTag()));
        mrTemp.setDiagnosticIsDiagAutoTag(DmShared.xNull(txtHIsDiagAuto.getTag()));
        mrTemp.setDiagnosticHospitalAcTag(DmShared.xNull(txtHDiagnosticHospitalAc.getTag()));
        mrTemp.setDiagnosticSelfAcTag(DmShared.xNull(txtHDiagnosticSelfAc.getTag()));
        mrTemp.setCancelDaysDiagnostic(DmShared.xNull(txtCancelDaysDiagnostic.getText()));
        mrTemp.setDiagnosticDays(DmShared.xNull(txtDiagnosticDays.getText()));
        mrTemp.setLabHistoryViewDays(DmShared.xNull(txtLabHistoryViewDays.getText()));
        //Diagnostic Tab
        mrTemp.setPrntyesnotag(DmShared.xNull(txtHPrntYsNo.getTag()));
        mrTemp.setPrntyesnotext(DmShared.xNull(txtHPrntYsNo.getText()));

        mrTemp.setUhidDefaultCardName(DmShared.xNull(txtHCardType.getText()));
        mrTemp.setUhidDefaultCardTypeCode(DmShared.xNull(txtHCardType.getTag()));

        mrTemp.setDiagIsPrintOnLetterheadTag(DmShared.xNull(txtHPrintedOnLetterhead.getTag()));
        mrTemp.setDiagAllowPatientForSiteTag(DmShared.xNull(txtHDiagAllowPatientForSite.getTag()));

        mrTemp.setRoomMandatoryTag(DmShared.xNull(txtHroomMandatory.getTag()));
        mrTemp.setRoomMandatoryText(DmShared.xNull(txtHroomMandatory.getText()));

        mrTemp.setServiceUnitEditableCode(DmShared.xNull(txtHServiceUnitEditable.getTag()));
        mrTemp.setServiceUnitEditableName(DmShared.xNull(txtHServiceUnitEditable.getText()));
        //OPD Tab
        mrTemp.setIsPatientInitialTag(DmShared.xNull(txtHPatientInitial.getTag()));
        mrTemp.setIsPatientInitialText(DmShared.xNull(txtHPatientInitial.getText()));
        mrTemp.setIsAddressTag(DmShared.xNull(txtHAddress.getTag()));
        mrTemp.setIsAddressText(DmShared.xNull(txtHAddress.getText()));
        mrTemp.setIsCityMandatoryTag(DmShared.xNull(txtHCityMandatory.getTag()));
        mrTemp.setIsCityMandatoryText(DmShared.xNull(txtHCityMandatory.getText()));
        mrTemp.setIsBloodGroupTag(DmShared.xNull(txtHBloodGroup.getTag()));
        mrTemp.setIsBloodGroupText(DmShared.xNull(txtHBloodGroup.getText()));
        mrTemp.setIsCasteTag(DmShared.xNull(txtHCasteMandatory.getTag()));
        mrTemp.setIsCasteText(DmShared.xNull(txtHCasteMandatory.getText()));
        mrTemp.setIsEmailTag(DmShared.xNull(txtHEmail.getTag()));
        mrTemp.setIsEmailText(DmShared.xNull(txtHEmail.getText()));
        mrTemp.setIsMaritalStatusTag(DmShared.xNull(txtHMaritalStatus.getTag()));
        mrTemp.setIsMaritalStatusText(DmShared.xNull(txtHMaritalStatus.getText()));
        mrTemp.setIsGuardianInitialTag(DmShared.xNull(txtHGuardianInitial.getTag()));
        mrTemp.setIsGuardianInitialText(DmShared.xNull(txtHGuardianInitial.getText()));
        mrTemp.setNightshiftFrom(DmShared.xNull(txtFrom.getText()));
        mrTemp.setNightshiftTo(DmShared.xNull(txtTo.getText()));
        mrTemp.setOpdAllowPatientForSiteTag(DmShared.xNull(txtHOPDAllowPatientForSite.getTag()));
        mrTemp.setPackageCode(DmShared.xNull(txtHPackage.getTag()));

        //Dietary Tab
        mrTemp.setDietScheduleDays(DmShared.xNull(txtDietScheduleDays.getText()));
        mrTemp.setDietaryProperty1(DmShared.xNull(txtDietaryProperty1.getText()));
        mrTemp.setDietaryProperty2(DmShared.xNull(txtDietaryProperty2.getText()));
        mrTemp.setDietaryProperty3(DmShared.xNull(txtDietaryProperty3.getText()));
        mrTemp.setDietaryProperty4(DmShared.xNull(txtDietaryProperty4.getText()));
        mrTemp.setDietaryProperty5(DmShared.xNull(txtDietaryProperty5.getText()));
        mrTemp.setDietaryProperty6(DmShared.xNull(txtDietaryProperty6.getText()));
        //---------------------------------------------------
        //=---------------------Day Care
        mrTemp.setDaycAllowPatientForSiteTag(DmShared.xNull(txtHDAYCAllowPatientForSite.getTag()));
        //------------------------
        mrTemp.setDiagnosticRegAdditionTag(DmShared.xNull(TxtHDiagnosticRegAddition.getTag()));
        mrTemp.setDiagnosticRegAdditionText(DmShared.xNull(TxtHDiagnosticRegAddition.getText()));

        mrTemp.setOrgReceiptCardTypeTag(DmShared.xNull(txtHOrgReceiptCard.getTag()));
        mrTemp.setHospitalHoldCardTypeTag(DmShared.xNull(txtHHospitalHoldCard.getTag()));

        mrTemp.setIsPatientImgMandatoryTag(DmShared.xNull(txtHPatientImgMandatory.getTag()));
        mrTemp.setIsAreaMandatoryTag(DmShared.xNull(txtHAreaMandatory.getTag()));
        mrTemp.setIsGuardianNameMandatoryTag(DmShared.xNull(txtHGuardianNameMandatory.getTag()));

//        mrTemp.setBloodBagDepartmentCode(DmShared.xNull(txtHBooldBagDepartment.getTag()));
//        mrTemp.setBloodComponentDepartmentCode(DmShared.xNull(txtHComponentDepartment.getTag()));
//
//        mrTemp.setBloodBagDepartmentText(DmShared.xNull(txtHBooldBagDepartment.getText()));
//        mrTemp.setBloodComponentDepartmentText(DmShared.xNull(txtHComponentDepartment.getText()));
        mrTemp.getGrdLinkDept().addAll(grdLinkDepartment.getGrid().getStore().getAll());
        mrTemp.getGrdStatusTemplate().addAll(grdDischargeStatusGUI.getGrid().getStore().getAll());

        mrTemp.setDefaultOpdPharmacyDepartmentTag(txtHDefPharmacyDepartment.getTag());
        mrTemp.setDefaultPharmacySiteTag(DmShared.xNull(txtHDefPharmacySite.getTag()));

        mrTemp.setBloodBankCipAdditionTag(DmShared.xNull(TxtHBloodBankCipAddition.getTag()));

        mrTemp.getGrdDiagnosticFilelist().addAll(grdDiagnosticFile.getGrid().getStore().getAll());
        mrTemp.setValidUpTo(DmClient.vNull(txtValidUpTo.getText(), "0"));
        mrTemp.setNoOfVisit(DmShared.xNull(txtNoOfVisit.getText()));
        mrTemp.setQueueTag(DmShared.xNull(txtHQueueAlgo.getTag()));
        mrTemp.setConsultationRegistrationTag(DmShared.iNull(txtHConsultationRegistration.getTag()).toString());
        mrTemp.setDonorDefaultCampCode(DmShared.xNull(txtHDonorCampCode.getTag()));
        mrTemp.setIsOtpRequiredForRefundTag(DmShared.xNull(txtHIsOtpRequiredForRefund.getTag()));
        mrTemp.setIsIcdxMandatoryTag(DmShared.xNull(txtHIcdxMandatory.getTag()));

//        mrTemp.getGrdTimingDetail().addAll(grdTimingDetail.getGrid().getStore().getAll());
        mrTemp.getLstQueue().addAll(lstQueue);
        mrTemp.setAutoBedConfirmation(DmShared.xNull(txtHAutoBedConfirmation.getTag()));

        //------------------------------Online Portal tab----------------------------------
        mrTemp.setAppointmentBookUptoDays(DmShared.xNull(txtAppointmentBookUptoDays.getText()));
        mrTemp.setAppointmentPaymentGatewayAllowedTag(DmShared.xNull(txtHAppointmentPaymentGatewayAllowed.getTag()));
        mrTemp.setPaymentGatewayURL(DmShared.xNull(txtPaymentGatewayURL.getText()));
        mrTemp.setPaymentOptionTag(DmShared.xNull(txtHPaymentOption.getTag()));
        mrTemp.setAutoOPDRegAfterAppointmentTag(DmShared.xNull(txtHAutoOPDRegAfterAppointment.getTag()));
        mrTemp.setRunFromWebsiteTag(DmShared.xNull(txtHRunFromWebsite.getTag()));
        mrTemp.setAppointmentCountMonthwise(DmShared.xNull(txtAppointmentCountMonthWise.getText()));
        //------------------------------Inventory-----------------------------
        mrTemp.setIncludeTaxInCost(DmShared.xNull(txtHIncludeTaxInCost.getTag()));
        mrTemp.setPurchaseSalesAcPostingBasedOnCode(DmShared.xNull(txtHPurchaseSalesAcPostingBasedOn.getTag()));
        mrTemp.setIndentRequired(DmShared.xNull(txtHIndentRequired.getTag()));
        mrTemp.setRequisitionMandatoryInTransfer(DmShared.xNull(txtHRequisitionMandatoryInTransfer.getTag()));
        mrTemp.setSinglePOFlowInPurchase(DmShared.xNull(txtHSinglePOFlowInPurchase.getTag()));
        mrTemp.setInv_DefaultPurchaseDepartment(DmShared.xNull(txtHInvDefaultPurchaseDepartment.getTag()));

        //------------------------------Asset-----------------------------
        mrTemp.setAsset_DepreciationAcTag(DmShared.xNull(txtHAssetDepreciationAc.getTag()));
        mrTemp.setAsset_DepreciationPostOn(DmShared.xNull(txtHDepreciationPostOn.getTag()));

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
            case 200:
                tabPanel.selectTab(2);
                new DmAlertMessageBox(errorMessage, txtHIPDRoomClean).show();
                break;
            case 300:
                tabPanel.selectTab(2);
                new DmAlertMessageBox(errorMessage, txtHTimeFormatRoom).show();
                break;
            case 400:
                tabPanel.selectTab(2);
                new DmAlertMessageBox(errorMessage, txtTimeCheckout).show();
                break;
            case 500:
                tabPanel.selectTab(2);
                new DmAlertMessageBox(errorMessage, txtIPDHalfDayUpTo).show();
                break;

            case 600:
                new DmAlertMessageBox(errorMessage, txtNewBornDay).show();
                break;

            case 700:
                new DmAlertMessageBox(errorMessage, txtNewBornMonth).show();
                break;

            case 800:
                new DmAlertMessageBox(errorMessage, txtNewBornYear).show();
                break;

            case 900:
                new DmAlertMessageBox(errorMessage, txtInfantMonth).show();
                break;

            case 1000:
                new DmAlertMessageBox(errorMessage, txtInfantYear).show();
                break;

            case 1100:
                new DmAlertMessageBox(errorMessage, txtChildMonth).show();
                break;

            case 1200:
                new DmAlertMessageBox(errorMessage, txtChildYear).show();
                break;

            default:
                tabPanel.selectTab(0);
                new DmAlertMessageBox(errorMessage, txtHSite).show();
        }

    }

    private void topControlMoveRec(String strKey) {
        srvAsync.invokeRPC().getMoveRec(strKey, fileRuleTypeDocument.getRuleCode(), new AsyncCallback<DmResult<EnvironmentSettingSite_Mdl>>() {

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

            if (DmShared.xNull(data.getIncludeDDInAge()).equalsIgnoreCase("")) {
                txtIncludeDDInMonth.setText("0");
            } else {
                txtIncludeDDInMonth.setText(DmShared.xNull(data.getIncludeDDInAge()));
            }

            if (DmShared.xNull(data.getIncludeMMInAge()).equalsIgnoreCase("")) {
                txtIncludeMMInMonth.setText("0");
            } else {
                txtIncludeMMInMonth.setText(DmShared.xNull(data.getIncludeMMInAge()));
            }

            txtHOPDRelegionMandatory.setTag(DmShared.xNull(data.getOpdIsReligionMandatory()));
            txtHOPDRelegionMandatory.setText(DmShared.xNull(data.getOpdIsReligionMandatoryName()));

            txtHOPDIdTypeMandatory.setTag(DmShared.xNull(data.getOpdIsIdTypeMandatory()));
            txtHOPDIdTypeMandatory.setText(DmShared.xNull(data.getOpdIsIdTypeMandatoryName()));

            txtHOPDCase.setText(DmShared.xNull(data.getOpdCaseName()));
            txtHOPDCase.setTag(DmShared.xNull(data.getOpdCaseCode()));
            txtHOPDRefConsultant.setTag(DmShared.xNull(data.getOpdRefConsultantCode()));
            txtHOPDRefConsultant.setText(DmShared.xNull(data.getOpdRefConsultantName()));
            txtHOPDConsultant.setText(DmShared.xNull(data.getOpdConsultantName()));
            txtHOPDConsultant.setTag(DmShared.xNull(data.getOpdConsultantCode()));
            txtHCardType.setText(DmShared.xNull(data.getUhidDefaultCardName()));
            txtHCardType.setTag(DmShared.xNull(data.getUhidDefaultCardTypeCode()));
            txtDefaultRegFee.setText(DmShared.xNull(data.getDefaultRegFee()));

            txtCancelDaysOPD.setText(DmShared.xNull(data.getCancelDaysOPD()));
            txtServiceDaysOPD.setText(DmShared.xNull(data.getServiceDaysOPD()));
            txtHistoryViewDays.setText(DmShared.xNull(data.getHistoryViewDays()));
            txtAppointmentHistoryViewDays.setText(DmShared.xNull(data.getAppointmentHistoryViewDays()));
            txtDocumentFooter.setText(DmShared.xNull(data.getDocumentFooter()));
            txtDocumentFooterRegional.setText(DmShared.xNull(data.getDocumentFooterRegional()));
            txtDocumentHeader.setText(DmShared.xNull(data.getDocumentHeader()));
            txtHCity.setText(DmShared.xNull(data.getCityName()));
            txtHCity.setTag(DmShared.xNull(data.getCityCode()));
            txtHCountry.setTag(DmShared.xNull(data.getNationalityTag()));
            txtHCountry.setText(DmShared.xNull(data.getNationalityText()));

            txtHEodPosting.setTag(DmShared.xNull(data.getEodPostingTypeCode()));
            txtHEodPosting.setText(DmShared.xNull(data.getEodPostingTypeName()));

            txtNewBornDay.setText(DmClient.vNull(data.getNewBornDay(), "0"));
            txtNewBornMonth.setText(DmClient.vNull(data.getNewBornMonth(), "0"));
            txtNewBornYear.setText(DmClient.vNull(data.getNewBornYear(), "0"));

            txtInfantMonth.setText(DmClient.vNull(data.getInfantMonth(), "0"));
            txtInfantYear.setText(DmClient.vNull(data.getInfantYear(), "0"));

            txtChildMonth.setText(DmClient.vNull(data.getChildMonth(), "0"));
            txtChildYear.setText(DmClient.vNull(data.getChildYear(), "0"));

            txtHIPDInfantUnit.setTag(DmShared.xNull(data.getIpdInfantUnitTag()));
            txtHIPDInfantUnit.setText(DmShared.xNull(data.getIpdInfantUnitText()));
            txtHIPDRoomClean.setTag(DmShared.xNull(data.getIpdIsRoomCleanTag()));
            txtHIPDRoomClean.setText(DmShared.xNull(data.getIpdIsRoomCleanText()));

            txtHIPDIsAutoRoom.setTag(DmShared.xNull(data.getIpdIsAutoRoomTag()));
            txtHIPDIsAutoRoom.setText(DmShared.xNull(data.getIpdIsAutoRoomText()));
            txtHTimeFormatRoom.setTag(DmShared.xNull(data.getIpdTimeFormatRoomTag()));
            txtHTimeFormatRoom.setText(DmShared.xNull(data.getIpdTimeFormatRoomText()));
            txtHTokenBlock.setTag(DmShared.xNull(data.getTokenBlockTag()));
            txtHTokenBlock.setText(DmShared.xNull(data.getTokenBlockText()));
            txtHIPDControlAc.setTag(DmShared.xNull(data.getIpdControlAcTag()));
            txtHIPDControlAc.setText(DmShared.xNull(data.getIpdControlAcText()));
            txtHOPDControlAc.setTag(DmShared.xNull(data.getOpdControlAcTag()));
            txtHOPDControlAc.setText(DmShared.xNull(data.getOpdControlAcText()));
            txtOPDNoOfSticker.setText(DmClient.vNull(data.getOpdNoOfSticker(), "0"));
            txtHDiagnosticControlAc.setTag(DmShared.xNull(data.getDiagnosticControlAcTag()));
            txtHDiagnosticControlAc.setText(DmShared.xNull(data.getDiagnosticControlAcText()));
            txtHEmergencyControlAc.setTag(DmShared.xNull(data.getEmergencyControlAcTag()));
            txtHEmergencyControlAc.setText(DmShared.xNull(data.getEmergencyControlAcText()));
            txtHDayCareControlAc.setTag(DmShared.xNull(data.getDayCareControlAcTag()));
            txtHDayCareControlAc.setText(DmShared.xNull(data.getDayCareControlAcText()));
            txtHDirectPrescription.setTag(DmShared.xNull(data.getDirectPrescriptionTag()));
            txtHDirectPrescription.setText(DmShared.xNull(data.getDirectPrescriptionText()));

            txtPancardLimit.setText(DmShared.xNull(data.getPanCardLimit()));
            txtCashpaymentLimit.setText(DmShared.xNull(data.getCashPaymentLimit()));

            txtHIsExistingPatient.setTag(DmShared.xNull(data.getIsExistingPatientTag()));
            txtHIsExistingPatient.setText(DmShared.xNull(data.getIsExistingPatientText()));

            txtHIsExistingPatient.setTag(DmShared.xNull(data.getIsLABExistingPatientTag()));
            txtHIsExistingPatient.setText(DmShared.xNull(data.getIsLABExistingPatientText()));

            txtHroomMandatory.setTag(DmShared.xNull(data.getRoomMandatoryTag()));
            txtHroomMandatory.setText(DmShared.xNull(data.getRoomMandatoryText()));

            txtHServiceUnitEditable.setTag(DmShared.xNull(data.getServiceUnitEditableCode()));
            txtHServiceUnitEditable.setText(DmShared.xNull(data.getServiceUnitEditableName()));
            txtHHourlyServiceRoundOff.setTag(DmShared.xNull(data.getHourlyServiceRoundOffTag()));
            txtHHourlyServiceRoundOff.setText(DmShared.xNull(data.getHourlyServiceRoundOffText()));

            txtIPDHalfDayUpTo.setText(DmClient.vNull(data.getIpdHalfDayUpToMin(), "0"));
            txtIPDNoOfSticker.setText(DmClient.vNull(data.getIpdNoOfSticker(), "0"));
            txtTimeCheckout.setText(DmShared.xNull(data.getIpdCheckoutTime()));
            txtGraceMinuteIn.setText(DmShared.xNull(data.getIpdGraceMinuteIn()));
            txtGraceMinuteOut.setText(DmShared.xNull(data.getIpdGraceMinuteOut()));
            txtPercentageRoom.setText(DmShared.xNull(data.getIpdHalfDayPercentage()));

            txtHPatientInitial.setTag(DmShared.xNull(data.getIsPatientInitialTag()));
            txtHPatientInitial.setText(DmShared.xNull(data.getIsPatientInitialText()));
            txtHAddress.setTag(DmShared.xNull(data.getIsAddressTag()));
            txtHAddress.setText(DmShared.xNull(data.getIsAddressText()));
            txtHBloodGroup.setTag(DmShared.xNull(data.getIsBloodGroupTag()));
            txtHBloodGroup.setText(DmShared.xNull(data.getIsBloodGroupText()));
            txtHCasteMandatory.setTag(DmShared.xNull(data.getIsCasteTag()));
            txtHCasteMandatory.setText(DmShared.xNull(data.getIsCasteText()));
            txtHCityMandatory.setTag(DmShared.xNull(data.getIsCityMandatoryTag()));
            txtHCityMandatory.setText(DmShared.xNull(data.getIsCityMandatoryText()));
            txtHEmail.setTag(DmShared.xNull(data.getIsEmailTag()));
            txtHEmail.setText(DmShared.xNull(data.getIsEmailText()));
            txtHGuardianInitial.setTag(DmShared.xNull(data.getIsGuardianInitialTag()));
            txtHGuardianInitial.setText(DmShared.xNull(data.getIsGuardianInitialText()));
            txtHMaritalStatus.setTag(DmShared.xNull(data.getIsMaritalStatusTag()));
            txtHMaritalStatus.setText(DmShared.xNull(data.getIsMaritalStatusText()));
            txtHIsDiagAuto.setTag(DmShared.xNull(data.getDiagnosticIsDiagAutoTag()));
            txtHIsDiagAuto.setText(DmShared.xNull(data.getDiagnosticIsDiagAutoText()));

            txtHPrntYsNo.setTag(DmShared.xNull(data.getPrntyesnotag()));
            txtHPrntYsNo.setText(DmShared.xNull(data.getPrntyesnotext()));

            txtFrom.setText(DmShared.xNull(data.getNightshiftFrom()));
            txtTo.setText(DmShared.xNull(data.getNightshiftTo()));
            TxtHDiagnosticRegAddition.setTag(DmShared.xNull(data.getDiagnosticRegAdditionTag()));
            TxtHDiagnosticRegAddition.setText(DmShared.xNull(data.getDiagnosticRegAdditionText()));
            txtHDisallowType.setTag(DmShared.xNull(data.getDisallowTypeTag()));
            txtHDisallowType.setText(DmShared.xNull(data.getDisallowTypeText()));

            txtHSecondaryCurrency.setText(DmShared.xNull(data.getSecodaryCurrencyText()));
            txtHSecondaryCurrency.setTag(DmShared.xNull(data.getSecodaryCurrencyTag()));

            txtHReceiptGenerateOnFullDiscount.setTag(DmShared.xNull(data.getIsReceiptGenerateWithFullDiscountTag()));
            txtHReceiptGenerateOnFullDiscount.setText(DmShared.xNull(data.getIsReceiptGenerateWithFullDiscountText()));

//            manageDsisplay();
            selectTimeFormat();

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
                txtHDonorCampCode.setTag(DmShared.xNull(data.getDonorDefaultCampCode()));
                txtHDonorCampCode.setText(DmShared.xNull(data.getDonorDefaultCampName()));
            }

            //Diagnostic Tab
            txtCancelDaysDiagnostic.setText(DmShared.xNull(data.getCancelDaysDiagnostic()));

            txtDiagnosticDays.setText(DmShared.xNull(data.getDiagnosticDays()));
            txtLabHistoryViewDays.setText(DmShared.xNull(data.getLabHistoryViewDays()));

            txtDiagnosticNoOfSticker.setText(DmClient.vNull(data.getDiagnosticNoOfSticker(), "0"));
            if (DmShared.xNull(data.getDiagnosticSampleCollectionTag()).equalsIgnoreCase("")) {
                txtHDirectSampleLabColl.setTag("0");
                txtHDirectSampleLabColl.setText("No");
            } else {
                txtHDirectSampleLabColl.setTag(DmShared.xNull(data.getDiagnosticSampleCollectionTag()));
                txtHDirectSampleLabColl.setText(DmShared.xNull(data.getDiagnosticSampleCollectionText()));
            }
            //Dietary Tab
            txtDietScheduleDays.setText(DmShared.xNull(data.getDietScheduleDays()));
            txtDietaryProperty1.setText(DmShared.xNull(data.getDietaryProperty1()));
            txtDietaryProperty2.setText(DmShared.xNull(data.getDietaryProperty2()));
            txtDietaryProperty3.setText(DmShared.xNull(data.getDietaryProperty3()));
            txtDietaryProperty4.setText(DmShared.xNull(data.getDietaryProperty4()));
            txtDietaryProperty5.setText(DmShared.xNull(data.getDietaryProperty5()));
            txtDietaryProperty6.setText(DmShared.xNull(data.getDietaryProperty6()));

            //---------------------
            txtHDiagnosticHospitalAc.setTag(DmShared.xNull(data.getDiagnosticHospitalAcTag()));
            txtHDiagnosticHospitalAc.setText(DmShared.xNull(data.getDiagnosticHospitalAcText()));
            txtHDiagnosticSelfAc.setTag(DmShared.xNull(data.getDiagnosticSelfAcTag()));
            txtHDiagnosticSelfAc.setText(DmShared.xNull(data.getDiagnosticSelfAcText()));
            txtHDiagAllowPatientForSite.setTag(DmShared.xNull(data.getDiagAllowPatientForSiteTag()));
            txtHDiagAllowPatientForSite.setText(DmShared.xNull(data.getDiagAllowPatientForSiteText()));

            txtHIPDAllowPatientForSite.setTag(DmShared.xNull(data.getIpdAllowPatientForSiteTag()));
            txtHIPDAllowPatientForSite.setText(DmShared.xNull(data.getIpdAllowPatientForSiteText()));
            txtHOPDAllowPatientForSite.setTag(DmShared.xNull(data.getOpdAllowPatientForSiteTag()));
            txtHOPDAllowPatientForSite.setText(DmShared.xNull(data.getOpdAllowPatientForSiteText()));
            txtHDAYCAllowPatientForSite.setTag(DmShared.xNull(data.getDaycAllowPatientForSiteTag()));
            txtHDAYCAllowPatientForSite.setText(DmShared.xNull(data.getDaycAllowPatientForSiteText()));

            txtHPrintedOnLetterhead.setTag(DmShared.xNull(data.getDiagIsPrintOnLetterheadTag()));
            txtHPrintedOnLetterhead.setText(DmShared.xNull(data.getDiagIsPrintOnLetterheadText()));

            txtHHospitalHoldCard.setText(DmShared.xNull(data.getHospitalHoldCardTypeText()));
            txtHHospitalHoldCard.setTag(DmShared.xNull(data.getHospitalHoldCardTypeTag()));
            txtHOrgReceiptCard.setText(DmShared.xNull(data.getOrgReceiptCardTypeText()));
            txtHOrgReceiptCard.setTag(DmShared.xNull(data.getOrgReceiptCardTypeTag()));

            txtHPatientImgMandatory.setText(DmShared.xNull(data.getIsPatientImgMandatoryText()));
            txtHPatientImgMandatory.setTag(DmShared.xNull(data.getIsPatientImgMandatoryTag()));

            txtHAreaMandatory.setText(DmShared.xNull(data.getIsAreaMandatoryText()));
            txtHAreaMandatory.setTag(DmShared.xNull(data.getIsAreaMandatoryTag()));

            txtHGuardianNameMandatory.setText(DmShared.xNull(data.getIsGuardianNameMandatoryText()));
            txtHGuardianNameMandatory.setTag(DmShared.xNull(data.getIsGuardianNameMandatoryTag()));

            txtHDefPharmacyDepartment.setTag(DmShared.xNull(data.getDefaultOpdPharmacyDepartmentTag()));
            txtHDefPharmacyDepartment.setText(DmShared.xNull(data.getDefaultOpdPharmacyDepartmentText()));

            txtHDefPharmacySite.setTag(DmShared.xNull(data.getDefaultPharmacySiteTag()));
            txtHDefPharmacySite.setText(DmShared.xNull(data.getDefaultPharmacySiteText()));

            grdLinkDepartment.getGrid().getStore().clear();
            grdLinkDepartment.getGrid().getStore().addAll(data.getGrdLinkDept());
            grdLinkDepartment.getGrid().getStore().add(new GrdLinkDepartment_Mdl());

            grdDischargeStatusGUI.getGrid().getStore().clear();
            grdDischargeStatusGUI.getGrid().getStore().addAll(data.getGrdStatusTemplate());

            txtHPackage.setText(DmShared.xNull(data.getPackageName()));
            txtHPackage.setTag(DmShared.xNull(data.getPackageCode()));

//            txtHBooldBagDepartment.setTag(DmShared.xNull(data.getBloodBagDepartmentCode()));
//            txtHComponentDepartment.setTag(DmShared.xNull(data.getBloodComponentDepartmentCode()));
//
//            txtHBooldBagDepartment.setText(DmShared.xNull(data.getBloodBagDepartmentText()));
//            txtHComponentDepartment.setText(DmShared.xNull(data.getBloodComponentDepartmentText()));
            TxtHBloodBankCipAddition.setText(DmShared.xNull(data.getBloodBankCipAdditionText()));
            TxtHBloodBankCipAddition.setTag(DmShared.xNull(data.getBloodBankCipAdditionTag()));

            grdDiagnosticFile.getGrid().getStore().clear();
            grdDiagnosticFile.getGrid().getStore().addAll(data.getGrdDiagnosticFilelist());

            txtHQueueAlgo.setTag(data.getQueueTag());
            txtHQueueAlgo.setText(data.getQueueText());
            txtNoOfVisit.setText(data.getNoOfVisit());
            txtValidUpTo.setText(DmClient.vNull(data.getValidUpTo(), "0"));
            txtHConsultationRegistration.setTag(data.getConsultationRegistrationTag());
            txtHConsultationRegistration.setText(data.getConsultationRegistrationText());

            txtHIsOtpRequiredForRefund.setText(DmShared.xNull(data.getIsOtpRequiredForRefundText()));
            txtHIsOtpRequiredForRefund.setTag(DmShared.xNull(data.getIsOtpRequiredForRefundTag()));

            txtHIcdxMandatory.setText(DmShared.xNull(data.getIsIcdxMandatoryText()));
            txtHIcdxMandatory.setTag(DmShared.xNull(data.getIsIcdxMandatoryTag()));

            //-----------------------------------Online Portal Tab-------------------------------------
            if (DmShared.xNull(data.getAppointmentBookUptoDays()).equalsIgnoreCase("")) {
                txtAppointmentBookUptoDays.setText("0");
            } else {
                txtAppointmentBookUptoDays.setText(DmShared.xNull(data.getAppointmentBookUptoDays()));
            }
            txtAppointmentCountMonthWise.setText(DmShared.xNull(data.getAppointmentCountMonthwise()));
            txtHAppointmentPaymentGatewayAllowed.setTag(DmShared.xNull(data.getAppointmentPaymentGatewayAllowedTag()));
            txtHAppointmentPaymentGatewayAllowed.setText(DmShared.xNull(data.getAppointmentPaymentGatewayAllowedText()));

            txtPaymentGatewayURL.setText(DmShared.xNull(data.getPaymentGatewayURL()));

            txtHPaymentOption.setTag(DmShared.xNull(data.getPaymentOptionTag()));
            txtHPaymentOption.setText(DmShared.xNull(data.getPaymentOptionText()));

            txtHAutoOPDRegAfterAppointment.setTag(DmShared.xNull(data.getAutoOPDRegAfterAppointmentTag()));
            txtHAutoOPDRegAfterAppointment.setText(DmShared.xNull(data.getAutoOPDRegAfterAppointmentText()));

            txtHRunFromWebsite.setTag(DmShared.xNull(data.getRunFromWebsiteTag()));
            txtHRunFromWebsite.setText(DmShared.xNull(data.getRunFromWebsiteText()));
            //-----------------------------------------------------------------------------------------
            lstQueue = data.getLstQueue();

            if (data.getIncludeTaxInCost().equalsIgnoreCase("0")) {
                txtHIncludeTaxInCost.setTag("0");
                txtHIncludeTaxInCost.setText("No");
            } else if (data.getIncludeTaxInCost().equalsIgnoreCase("1")) {
                txtHIncludeTaxInCost.setTag("1");
                txtHIncludeTaxInCost.setText("Yes");
            }

            txtHPurchaseSalesAcPostingBasedOn.setTag(DmShared.xNull(data.getPurchaseSalesAcPostingBasedOnCode()));
            txtHPurchaseSalesAcPostingBasedOn.setText(DmShared.xNull(data.getPurchaseSalesAcPostingBasedOnName()));

            if (DmShared.xNull(data.getIndentRequired()).equalsIgnoreCase("0")) {
                txtHIndentRequired.setTag("0");
                txtHIndentRequired.setText("Depend On Master");
            } else if (DmShared.xNull(data.getIndentRequired()).equalsIgnoreCase("1")) {
                txtHIndentRequired.setTag("1");
                txtHIndentRequired.setText("Yes");
            } else {
                txtHIndentRequired.setTag("2");
                txtHIndentRequired.setText("No");
            }

            if (data.getRequisitionMandatoryInTransfer().equalsIgnoreCase("0")) {
                txtHRequisitionMandatoryInTransfer.setTag("0");
                txtHRequisitionMandatoryInTransfer.setText("No");
            } else if (data.getRequisitionMandatoryInTransfer().equalsIgnoreCase("1")) {
                txtHRequisitionMandatoryInTransfer.setTag("1");
                txtHRequisitionMandatoryInTransfer.setText("Yes");
            }
            if (data.getSinglePOFlowInPurchase().equalsIgnoreCase("0")) {
                txtHSinglePOFlowInPurchase.setTag("0");
                txtHSinglePOFlowInPurchase.setText("No");
            } else if (data.getSinglePOFlowInPurchase().equalsIgnoreCase("1")) {
                txtHSinglePOFlowInPurchase.setTag("1");
                txtHSinglePOFlowInPurchase.setText("Yes");
            }
            if (data.getAutoBedConfirmation().equalsIgnoreCase("0")) {
                txtHAutoBedConfirmation.setTag("0");
                txtHAutoBedConfirmation.setText("No");
            } else if (data.getAutoBedConfirmation().equalsIgnoreCase("1")) {
                txtHAutoBedConfirmation.setTag("1");
                txtHAutoBedConfirmation.setText("Yes");
            }
            txtHIsExistingPatientDiagnostic.setTag(data.getIsLABExistingPatientTag());
            txtHIsExistingPatientDiagnostic.setText(data.getIsLABExistingPatientText());

            txtHIsBillH_Approval.setTag(data.getIsBillHApprovalTag());
            txtHIsBillH_Approval.setText(data.getIsBillHApprovalText());

            txtHIsCommonPricelist.setTag(data.getIsCommonPricelistForSiteTag());
            txtHIsCommonPricelist.setText(data.getIsCommonPricelistForSiteText());

            txtHIsExistingPatientDiagnostic.setTag(data.getIsExistingPatientTag());
            txtHIsExistingPatientDiagnostic.setText(data.getIsExistingPatientText());
            txtHInvDefaultPurchaseDepartment.setTag(DmShared.xNull(data.getInv_DefaultPurchaseDepartment()));
            txtHInvDefaultPurchaseDepartment.setText(DmShared.xNull(data.getInv_DefaultPurchaseDepartmentText()));

            txtTransactionLockDate.setText(DmShared.xNull(data.getTransactionLockDate()));

            txtHLockVoucherPrefix.setText(DmShared.xNull(data.getTransactionLockYear()));
            txtHLockVoucherPrefix.setTag(DmShared.xNull(data.getTransactionLockYear()));

            //------------------------------Asset-----------------------------
            txtHAssetDepreciationAc.setTag(DmShared.xNull(data.getAsset_DepreciationAcTag()));
            txtHAssetDepreciationAc.setText(DmShared.xNull(data.getAsset_DepreciationAcText()));

            txtHDepreciationPostOn.setTag(DmShared.xNull(data.getAsset_DepreciationPostOn()));
            txtHDepreciationPostOn.setText(DmShared.xNull(data.getAsset_DepreciationPostOnText()));
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
            statusGUIEdit.hide();
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
                    txtHDirectPrescription.setEnabled(false);
                    txtHDirectPrescription.setTag("0");
                    txtHDirectPrescription.setText("Disallow");
                    txtHDisallowType.setTag("1");
                    txtHDisallowType.setText("Disallow (Reg. wise)");

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

    private void selectTimeFormat() {
        if (DmShared.iNull(txtHTimeFormatRoom.getTag()).longValue() == 0) {
            txtTimeCheckout.setEnabled(false);
            txtTimeCheckout.setText("");
        } else {
            txtTimeCheckout.setEnabled(true);
        }
    }

    private void manageEvent() {
        btnClose.addClickHandler(this);
        btnSave.addClickHandler(this);
        btnCopy.addClickHandler(this);
        btnDelete.addClickHandler(this);
        txtUploadStatus.setEnabled(false);
        btnExport.addClickHandler(this);

        txtIncludeDDInMonth.addBlurHandler(this);
        txtTransactionLockDate.addBlurHandler(this);
        imgView.addClickHandler(this);
        btnQueue.addClickHandler(this);

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

    private void exportJSON() {
        if (DmShared.iNull(txtHSite.getTag()).intValue() == 0) {
            new DmAlertMessageBox("Site Name is mandatory.", txtHSite).show();
        }
        srvAsync.invokeRPC().exportJSON(DmRequestBuilder.getSessionId(), DmShared.xNull(txtHSite.getTag()), new AsyncCallback<DmResult<KeyFieldExtended>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<KeyFieldExtended> result) {
                if (result.isStatus()) {
                    KeyFieldExtended field = result.getData();
                    startDownloading(DmShared.xNull(field.getSharedKeyFieldName()), DmShared.xNull(field.getSharedKeyFieldCode()));
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), btnExport).show();
                }
            }
        });
    }
}
