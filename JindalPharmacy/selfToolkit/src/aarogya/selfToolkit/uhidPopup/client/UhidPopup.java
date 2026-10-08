package aarogya.selfToolkit.uhidPopup.client;

import dataman.dmBase.self.shared.DmResultPageing;
import dataman.dmBase.self.shared.DmHelper;
import aarogya.selfToolkit.patientSearch.client.PatientSearch;
import aarogya.selfToolkit.patientSearch.shared.SearchValue_Mdl.FilterType;
import aarogya.selfToolkit.uhidPopup.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import dataman.dmToolkit.dmMessageBox.client.*;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.dmBaseStatic.client.DmRootEvent;
import dataman.dmBase.dmBaseStatic.client.DmRootEvents;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import java.util.List;

public final class UhidPopup extends UhidPopup_Dsg implements IsWidget, ClickHandler, DmRootEvents, AttachEvent.Handler, BlurHandler, KeyPressHandler {

    @Override
    public void onKeyPress(KeyPressEvent event) {
        int charCode = event.getCharCode();
        if (charCode == 32) {
            txtFirstName.cancelKey();
            txtMiddleName.cancelKey();
            txtLastName.cancelKey();
        }
    }

    public interface CallBack {

        public void onOk(UhidPopup_Mdl mrTemp);

        public void onClose();
    }

    private final ServiceInvoker<UhidPopup_SrvAsync> SrvAsync;
    private final PatientSearch patientSearch = new PatientSearch();
    private CallBack callBack;
    private FilterType[] filterType;
    private boolean isIdTypeShow = false;
    private boolean disAllowEscape = false;

    public void addCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void textHelpEvent() {
        patientSearch.addCallBack(new PatientSearch.CallBack() {

            @Override
            public void ok(String uhidRecId, String uhidDocId) {
                getUHIDDetail(uhidRecId, uhidDocId, "", "", txtUHID);
            }

            @Override
            public void close() {
                txtHPrefixPatient.setFocus(true);

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
        txtHPrefixPatient.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPrefixPatient(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPrefixPatient).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrefixPatient.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPrefixPatient).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });

        txtHPrefixGuardian.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHPrefixPatient(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHPrefixGuardian).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHPrefixGuardian.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHPrefixGuardian).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {

            }
        });
    }

    public UhidPopup() {
        super();
        SrvAsync = new ServiceInvoker<UhidPopup_SrvAsync>(GWT.create(UhidPopup_Srv.class), UhidPopup.class.getName());
        this.asWidget().setTitle(widgetName);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        searchPatient.addClickHandler(this);
        manageEvent();
        this.filterType = filterType;
        textHelpEvent();
        if (!this.disAllowEscape) {
            btnCancel.addClickHandler(this);
            toolContainer.addWidget(flwCancel);
            toolContainer.addGap();
        }
    }

    public UhidPopup(boolean disAllowEscape) {
        super();
        SrvAsync = new ServiceInvoker<UhidPopup_SrvAsync>(GWT.create(UhidPopup_Srv.class), UhidPopup.class.getName());
        this.asWidget().setTitle(widgetName);
        btnCancel.addClickHandler(this);
        btnOk.addClickHandler(this);
        popupPanel.addAttachHandler(this);
        searchPatient.addClickHandler(this);
        manageEvent();
        this.filterType = filterType;
        textHelpEvent();
        this.disAllowEscape = disAllowEscape;
        if (!this.disAllowEscape) {
            btnCancel.addClickHandler(this);
            toolContainer.addWidget(flwCancel);
            toolContainer.addGap();
        }

    }

    private void workingArea(List<Layout_Mdl> lstLayout) {
        FlowPanel flwTd;
        HorizontalPanel hrtPnl;
        DmLabel lbl;
        DmTextHelp firstTextHelp = null;
        DmTextBox firstTextBox = null;
        DmTextBoxMobile firstTextBoxMobile = null;
        int height = 0;

        try {
            flwWorkignArea.clear();
            flwTable.clear();
            flwTrEmail.clear();
            flwTrGuardian.clear();
            flwTrID.clear();
            flwTrMobile.clear();
            flwTrPatient.clear();
            flwTrRegistration.clear();
            flwTrUHID.clear();
            vl.get().clear();
            vl.add(btnOk);
            vl.add(btnCancel);

            if (lstLayout != null && lstLayout.size() > 0) {
                height = lstLayout.size() * 22;
                DmClient.setStyle(flwTable, "98%", height + "px", null, null, DmPosition.CENTER, true);
                popupPanel.setHeight((height + 35) + "px");

                for (Layout_Mdl lm : lstLayout) {
                    if (DmShared.xNull(lm.getCode()).equalsIgnoreCase("uhid")) {
                        DmClient.setStyle(flwTrUHID, "100%", null, "left", null, DmPosition.NONE, true);
                        txtUHID.setMandatory(lm.isIsMandatory());

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        lbl = new DmLabel("UHID No.");
                        flwTd.add(lbl);
                        flwTrUHID.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "50%", "20px", "left", null, DmPosition.NONE, false);

                        hrtPnl = new HorizontalPanel();
                        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
                        hrtPnl.add(vl.add(txtUHID));
                        hrtPnl.setCellWidth(txtUHID, "100%");
                        DmClient.setStyle(flwPatientSearch, "100%", "20px", "left", null, DmPosition.NONE, false);
                        searchPatient.setWidth("20px");
                        searchPatient.setHeight("20px");
                        searchPatient.getElement().setAttribute("style", searchPatient.getElement().getAttribute("style")
                                + ";font-size:11px;padding:0px;");
                        flwPatientSearch.add(searchPatient);
                        searchPatient.setTitle("Search Patient (Room Wise)");
                        hrtPnl.add(vl.add(flwPatientSearch));
                        flwTd.add(hrtPnl);
                        flwTrUHID.add(flwTd);
                        flwTable.add(flwTrUHID);
                        if (firstTextBox == null && firstTextHelp == null && firstTextBoxMobile == null) {
                            firstTextBox = txtUHID;
                        }

                    } else if (DmShared.xNull(lm.getCode()).equalsIgnoreCase("email")) {
                        DmClient.setStyle(flwTrEmail, "100%", null, "left", null, DmPosition.NONE, true);
                        txtEmail.setMandatory(lm.isIsMandatory());

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        lbl = new DmLabel("Email Id");
                        flwTd.add(lbl);
                        flwTrEmail.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
                        flwTd.add(vl.add(txtEmail));
                        flwTrEmail.add(flwTd);
                        flwTable.add(flwTrEmail);
                        if (firstTextBox == null && firstTextHelp == null && firstTextBoxMobile == null) {
                            firstTextBox = txtEmail;
                        }
                    } else if (DmShared.xNull(lm.getCode()).equalsIgnoreCase("id")) {
                        DmClient.setStyle(flwTrID, "100%", null, "left", null, DmPosition.NONE, true);
                        txtHIdType.setMandatory(lm.isIsMandatory());
                        txtIdNo.setMandatory(lm.isIsMandatory());
                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        lbl = new DmLabel("Id Type");
                        flwTd.add(lbl);
                        flwTrID.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
                        hrtPnl = new HorizontalPanel();
                        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
                        hrtPnl.add(vl.add(txtHIdType));
                        hrtPnl.setCellWidth(txtHIdType, "30%");
                        hrtPnl.add(vl.add(txtIdNo));
                        hrtPnl.setCellWidth(txtIdNo, "70%");
                        flwTd.add(hrtPnl);
                        flwTrID.add(flwTd);
                        flwTable.add(flwTrID);
                        if (firstTextBox == null && firstTextHelp == null && firstTextBoxMobile == null) {
                            firstTextHelp = txtHIdType;
                        }
                        isIdTypeShow = true;

                    } else if (DmShared.xNull(lm.getCode()).equalsIgnoreCase("mobile")) {
                        DmClient.setStyle(flwTrMobile, "100%", null, "left", null, DmPosition.NONE, true);
                        txtMobile.setMandatory(lm.isIsMandatory());
                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        lbl = new DmLabel("Mobile No.");
                        lbl.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
                        flwTd.add(lbl);
                        flwTrMobile.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
                        flwTd.add(vl.add(txtMobile));
                        flwTrMobile.add(flwTd);
                        flwTable.add(flwTrMobile);
                        if (firstTextBox == null && firstTextHelp == null && firstTextBoxMobile == null) {
                            firstTextBoxMobile = txtMobile;
                        }
                    } else if (DmShared.xNull(lm.getCode()).equalsIgnoreCase("guardianName")) {
                        DmClient.setStyle(flwTrGuardian, "100%", null, "left", null, DmPosition.NONE, true);
                        txtGuardianName.setMandatory(lm.isIsMandatory());
                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        lbl = new DmLabel("Guardian Name");
                        flwTd.add(lbl);
                        flwTrGuardian.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
                        flwTd.add(vl.add(txtHPrefixGuardian));
                        flwTrGuardian.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
                        hrtPnl = new HorizontalPanel();
                        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
                        hrtPnl.add(vl.add(txtGuardianName));
                        hrtPnl.setCellWidth(txtGuardianName, "68%");
                        flwTd.add(hrtPnl);
                        flwTrGuardian.add(flwTd);
                        flwTable.add(flwTrGuardian);
                        if (firstTextBox == null && firstTextHelp == null && firstTextBoxMobile == null) {
                            firstTextBox = txtGuardianName;
                        }
                    } else if (DmShared.xNull(lm.getCode()).equalsIgnoreCase("patientName")) {
                        DmClient.setStyle(flwTrPatient, "100%", null, "left", null, DmPosition.NONE, true);
                        txtFirstName.setMandatory(lm.isIsMandatory());

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        lbl = new DmLabel("Patient Name");
                        flwTd.add(lbl);
                        flwTrPatient.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
                        flwTd.add(vl.add(txtHPrefixPatient));
                        flwTrPatient.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
                        hrtPnl = new HorizontalPanel();
                        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
                        hrtPnl.add(vl.add(txtFirstName));
                        hrtPnl.setCellWidth(txtFirstName, "65%");
                        txtFirstName.getElement().setPropertyString("placeholder", "First Name");
                        flwTd.add(hrtPnl);
                        flwTrPatient.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
                        hrtPnl = new HorizontalPanel();
                        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
                        hrtPnl.add(vl.add(txtMiddleName));
                        hrtPnl.setCellWidth(txtMiddleName, "22%");
                        txtMiddleName.getElement().setPropertyString("placeholder", "Middle Name");
                        flwTd.add(hrtPnl);
                        flwTrPatient.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
                        hrtPnl = new HorizontalPanel();
                        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
                        hrtPnl.add(vl.add(txtLastName));
                        hrtPnl.setCellWidth(txtLastName, "23%");
                        txtLastName.getElement().setPropertyString("placeholder", "Last Name");
                        flwTd.add(hrtPnl);
                        flwTrPatient.add(flwTd);

                        flwTable.add(flwTrPatient);
                        if (firstTextBox == null && firstTextHelp == null && firstTextBoxMobile == null) {
                            firstTextHelp = txtHPrefixPatient;
                        }
                    } else if (DmShared.xNull(lm.getCode()).equalsIgnoreCase("registration")) {
                        DmClient.setStyle(flwTrRegistration, "100%", null, "left", null, DmPosition.NONE, true);
                        txtRegNo.setMandatory(lm.isIsMandatory());

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
                        lbl = new DmLabel("Reg. No.");
                        flwTd.add(lbl);
                        flwTrRegistration.add(flwTd);

                        flwTd = new FlowPanel();
                        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, true);
                        flwTd.add(vl.add(txtRegNo));
                        flwTrRegistration.add(flwTd);

                        flwTable.add(flwTrRegistration);
                        if (firstTextBox == null && firstTextHelp == null && firstTextBoxMobile == null) {
                            firstTextBox = txtRegNo;
                        }
                    }
                }
                flwWorkignArea.add(flwTable);
                popupPanel.show();
                popupPanel.center();
                popupPanel.setGlassEnabled(true);
                clear();
                manageDisplay(true);
                setFocusToFirstControl(firstTextHelp, firstTextBox, firstTextBoxMobile);
            } else {
                hide(true);
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), btnOk).show();
        }
    }

    private void setFocusToFirstControl(final DmTextHelp firstTextHelp, final DmTextBox firstTextBox, final DmTextBoxMobile firstTextBoxMobile) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (firstTextBox != null) {
                    firstTextBox.setFocus(true);
                } else if (firstTextHelp != null) {
                    firstTextHelp.setFocus(true);
                } else if (firstTextBoxMobile != null) {
                    firstTextBoxMobile.setFocus(true);
                }
            }
        });
    }

    private void manageEvent() {
        txtRegNo.addBlurHandler(this);
        txtUHID.addBlurHandler(this);
        manageDisplay(false);

        txtFirstName.addKeyPressHandler(this);
        txtMiddleName.addKeyPressHandler(this);
        txtLastName.addKeyPressHandler(this);
    }

    private void manageDisplay(boolean flag) {

        txtFirstName.setEnabled(flag);
        txtGuardianName.setEnabled(flag);
        txtMobile.setEnabled(flag);
        txtEmail.setEnabled(flag);
        txtRegNo.setEnabled(flag);
        txtIdNo.setEnabled(flag);
        txtHIdType.setEnabled(flag);
        txtLastName.setEnabled(flag);
        txtMiddleName.setEnabled(flag);
        txtHPrefixPatient.setEnabled(flag);
        txtHPrefixGuardian.setEnabled(flag);
    }

    public void show(List<Layout_Mdl> lstLayout, FilterType... filterType) {
        if (lstLayout != null) {
            isIdTypeShow = false;
            workingArea(lstLayout);
        }
        this.filterType = filterType;

    }

    public void hide() {
        hide(false);
    }

    public void hide(boolean withCloseEvent) {
        this.popupPanel.hide();
        if (withCloseEvent) {
            callBack.onClose();
        }
    }

    @Override
    public void onClick(ClickEvent event) {
        boolean valueFound = false;
        if (event.getSource() == btnCancel) {
            hide(true);
        } else if (event.getSource() == btnOk) {

            if (DmShared.iNull(txtUHID.getTag()).longValue() == 0) {
                if (txtUHID.isMandatory()) {
                    new DmAlertMessageBox("UHID No. is mandatory.", txtUHID).show();
                    return;
                }
            } else {
                valueFound = true;
            }

            if (DmShared.iNull(txtRegNo.getTag()).longValue() == 0) {
                if (txtRegNo.isMandatory()) {
                    new DmAlertMessageBox("Reg. No. is mandatory.", txtRegNo).show();
                    return;
                }
            } else {
                valueFound = true;
            }

            if (DmShared.xNull(txtEmail.getText()).equalsIgnoreCase("")) {
                if (txtEmail.isMandatory()) {
                    new DmAlertMessageBox("eMail is mandatory.", txtEmail).show();
                    return;
                }
            } else {
                valueFound = true;
            }

            if (!DmShared.xNull(txtHPrefixPatient.getText()).equalsIgnoreCase("")) {
                valueFound = true;
            }
            if (!DmShared.xNull(txtHPrefixGuardian.getText()).equalsIgnoreCase("")) {
                valueFound = true;
            }
            if (DmShared.xNull(txtFirstName.getText()).equalsIgnoreCase("")) {
                if (txtFirstName.isMandatory()) {
                    new DmAlertMessageBox("Patient Name is mandatory.", txtFirstName).show();
                    return;
                }
            } else {
                valueFound = true;
            }

            if (DmShared.xNull(txtGuardianName.getText()).equalsIgnoreCase("")) {
                if (txtGuardianName.isMandatory()) {
                    new DmAlertMessageBox("Guardian Name is mandatory.", txtGuardianName).show();
                    return;
                }
            } else {
                if (DmShared.xNull(txtFirstName.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Patient Name is mandatory, if you fill Guardian Name.", txtFirstName).show();
                    return;
                }
                valueFound = true;
            }

            if (DmShared.xNull(txtHIdType.getTag()).equalsIgnoreCase("")) {
                if (txtHIdType.isMandatory()) {
                    new DmAlertMessageBox("Id Type is mandatory.", txtHIdType).show();
                    return;
                }
            } else {
                if (DmShared.xNull(txtIdNo.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Id No. is mandatory, if you fill Id Type.", txtIdNo).show();
                    return;
                }
                valueFound = true;
            }

            if (DmShared.xNull(txtIdNo.getText()).equalsIgnoreCase("")) {
                if (txtIdNo.isMandatory()) {
                    new DmAlertMessageBox("Id No. is mandatory.", txtIdNo).show();
                    return;
                }
            } else {
                if (DmShared.xNull(txtHIdType.getText()).equalsIgnoreCase("")) {
                    new DmAlertMessageBox("Id Type is mandatory, if you fill Id No.", txtHIdType).show();
                    return;
                }
                valueFound = true;
            }

            if (DmShared.xNull(txtMobile.getText()).equalsIgnoreCase("")) {
                if (txtMobile.isMandatory()) {
                    new DmAlertMessageBox("Mobile No. is mandatory.", txtMobile).show();
                    return;
                }
            } else {
                valueFound = true;
            }

            if (valueFound) {
                fillModel();
            } else {
                new DmAlertMessageBox("Please fill any field", txtMobile).show();
                return;
            }

        } else if (event.getSource() == searchPatient) {
            patientSearch.show(true, filterType);
        }
    }

    @Override
    public void rootKeyInvoked(DmRootEvent rootEvent) {
        rootEvent.stopPropagation();

        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE && !disAllowEscape) {
            hide(true);
        }
        if (rootEvent.getEvent().getNativeEvent().getKeyCode() == KeyCodes.KEY_O && rootEvent.getEvent().getNativeEvent().getCtrlKey()) {
            fillModel();
        }
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            DmClient.removeRootKeyEvent(this, Event.ONKEYDOWN);
        } else {
            DmClient.addRootKeyEvent(this, Event.ONKEYDOWN);
        }
    }

    @Override
    public Widget asWidget() {
        return this.popupPanel;
    }

    @Override
    public void onBlur(BlurEvent event) {
        if (event.getSource() == txtRegNo) {
            if (!DmShared.xNull(txtRegNo.getText()).trim().equalsIgnoreCase("")) {
                getUHIDDetail("", "", "", DmShared.xNull(txtRegNo.getText()), txtRegNo);
            }
        } else if (event.getSource() == txtUHID) {
            if (!DmShared.xNull(txtUHID.getText()).trim().equalsIgnoreCase("")) {
                getUHIDDetail(DmShared.xNull(txtUHID.getText()), "", "", "", txtUHID);
            }
        }
    }

    private void fillModel() {
        try {
            UhidPopup_Mdl mrTemp = new UhidPopup_Mdl();
            mrTemp.setDocIdUHID(DmShared.xNull(txtUHID.getTag()));
            mrTemp.setRecIdUHID(DmShared.xNull(txtUHID.getText()));
            mrTemp.setDocIdRegistration(DmShared.xNull(txtRegNo.getTag()));
            mrTemp.setRecIdRegistration(DmShared.xNull(txtRegNo.getText()));
            mrTemp.setPatientName(DmShared.removeSpace(DmShared.xNull(txtFirstName.getText()).trim() + DmShared.xNull(txtMiddleName.getText()).trim() + DmShared.xNull(txtLastName.getText()).trim()).trim());
            mrTemp.setFirstName(DmShared.xNull(txtFirstName.getText()));
            mrTemp.setMiddleName(DmShared.xNull(txtMiddleName.getText()));
            mrTemp.setLastName(DmShared.xNull(txtLastName.getText()));
            mrTemp.setGuardianName(DmShared.xNull(txtGuardianName.getText()));
            mrTemp.setMobileNo(DmShared.xNull(txtMobile.getText()));
            mrTemp.setEmail(DmShared.xNull(txtEmail.getText()));
            mrTemp.setIdType(DmShared.xNull(txtHIdType.getText()));
            mrTemp.setIdTypeCode(DmShared.xNull(txtHIdType.getTag()));
            mrTemp.setIdNo(DmShared.xNull(txtIdNo.getText()));
            mrTemp.setPatientInitial(DmShared.xNull(txtHPrefixPatient.getTag()));
            mrTemp.setGuardianInitial(DmShared.xNull(txtHPrefixGuardian.getTag()));

            UhidPopup_Vld.chkSave(mrTemp);

            callBack.onOk(mrTemp);
            hide(false);
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage(), txtUHID);
        }
    }

    private void getUHIDDetail(String recIdUHID, String docIdUHID, String docIdRegistration, String recIdRegistration, final DmTextBox txtBox) {
        SrvAsync.invokeRPC().getUHIDDetails(recIdUHID, docIdUHID, docIdRegistration, recIdRegistration, new AsyncCallback<DmResult<UhidPopup_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                topControlSaveError(0, caught.getMessage(), txtBox);
            }

            @Override
            public void onSuccess(DmResult<UhidPopup_Mdl> result) {
                clear();
                if (result.isStatus()) {
                    setUHIDDetail(result.getData());
                    manageDisplay(false);
                    btnOk.setFocus(true);
                } else {
                    manageDisplay(true);
                    txtUHID.setFocus(true);
                    txtRegNo.setTag("");
                    txtRegNo.setText("");
                    topControlSaveError(result.getErrorNumber(), result.getErrorMessage(), txtBox);
                }
            }
        });
    }

    private void setUHIDDetail(UhidPopup_Mdl data) {

        txtUHID.setTag(DmShared.iNull(data.getDocIdUHID()).toString());
        txtUHID.setText(DmShared.xNull(data.getRecIdUHID()));
        txtRegNo.setTag(DmShared.iNull(data.getDocIdRegistration()).toString());
        txtRegNo.setText(DmShared.xNull(data.getRecIdRegistration()));
        txtFirstName.setText(DmShared.xNull(data.getFirstName()));
        txtMiddleName.setText(DmShared.xNull(data.getMiddleName()));
        txtLastName.setText(DmShared.xNull(data.getLastName()));
        txtGuardianName.setText(DmShared.xNull(data.getGuardianName()));
        txtMobile.setText(DmShared.xNull(data.getMobileNo()));
        txtEmail.setText(DmShared.xNull(data.getEmail()));
        txtHPrefixPatient.setTag(DmShared.xNull(data.getPatientInitial()));
        txtHPrefixPatient.setText(DmShared.xNull(data.getPatientInitial()));
        txtHPrefixGuardian.setTag(DmShared.xNull(data.getGuardianInitial()));
        txtHPrefixGuardian.setText(DmShared.xNull(data.getGuardianInitial()));

        if (isIdTypeShow) {
            txtHIdType.setTag(DmShared.xNull(data.getIdTypeCode()));
            txtHIdType.setText(DmShared.xNull(data.getIdType()));
            txtIdNo.setText(DmShared.xNull(data.getIdNo()));
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage, DmTextBox txtBox) {
        switch (errorNo) {
            case 100:
                new DmAlertMessageBox(errorMessage, txtEmail).show();
                break;
            case 200:
                new DmAlertMessageBox(errorMessage, txtMobile).show();
                break;
            case 300:
                new DmAlertMessageBox(errorMessage, txtIdNo).show();
                break;
            default:
                new DmAlertMessageBox(errorMessage, txtBox).show();
                break;
        }
    }

    private void clear() {
        txtUHID.setText("");
        txtUHID.setTag("");
        txtFirstName.setText("");
        txtMiddleName.setText("");
        txtLastName.setText("");
        txtGuardianName.setText("");
        txtMobile.setText("");
        txtEmail.setText("");
        txtHIdType.setTag("");
        txtHIdType.setText("");
        txtIdNo.setText("");
        txtRegNo.setTag("");
        txtRegNo.setText("");
        txtHPrefixPatient.setTag("");
        txtHPrefixPatient.setText("");
        txtHPrefixGuardian.setTag("");
        txtHPrefixGuardian.setText("");
    }

}
