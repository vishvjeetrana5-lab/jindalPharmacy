package aarogya.master.prescriptionValueTemplate.client;

import dataman.dmBase.self.shared.DmHelper;
import dataman.dmBase.self.shared.DmResultPageing;
import aarogya.master.prescriptionValueTemplate.shared.*;
import aarogya.selfToolkit.allergy.client.Allergy;
import aarogya.selfToolkit.allergy.shared.Allergy_Shared;
import aarogya.selfToolkit.allergy.shared.GrdAllergy_Mdl;
import aarogya.selfToolkit.customFieldPopup.client.CustomFieldPopup;
import aarogya.selfToolkit.prescriptionCustomItem.client.PrescriptionCustomItem;
import aarogya.selfToolkit.prescriptionCustomItem.shared.PrescriptionCustomItem_Shared;
import aarogya.selfToolkit.prescriptionCustomItem.shared.GrdPrescriptionCustomItem_Mdl;
import aarogya.selfToolkit.stStatic.shared.STEnum;
import dataman.dmBase.self.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Vld;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmMessageBox.client.DmAlertMessageBox;
import dataman.dmToolkit.dmMessageBox.client.DmConfirmMessageBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmBase.self.shared.VoucherPermissionHandler;
import dataman.dmToolkit.dmGUI.shared.DmGUIControl_Mdl;
import dataman.dmToolkit.dmGUI.shared.DmGUI_Mdl;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class PrescriptionValueTemplate extends PrescriptionValueTemplate_Dsg implements AttachEvent.Handler, ClickHandler, KeyDownHandler {

    private final ServiceInvoker<PrescriptionValueTemplate_SrvAsync> SrvAsync;
    public boolean medicineEditable = false, frequencyEditable = false, allergyEditable = false;
    private boolean isCopyGUI = false;
    private String guiCopyFromDocId = "";

    private final CustomFieldPopup customFieldPopup = new CustomFieldPopup();
    private final Allergy allergy = new Allergy();
    private List<GrdAllergy_Mdl> lstAllergy = new ArrayList<GrdAllergy_Mdl>();
    private final PrescriptionCustomItem complaint = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstComplaint = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem comorbid = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstComorbid = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem instruction = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstInstruction = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem familyHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstFamilyHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem habit = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstHabit = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem historyOfPastIllness = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstHistoryOfPastIllness = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem historyOfPresentIllness = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstHistoryOfPresentIllness = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem physicalExamination = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstPhysicalExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem systemicExamination = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstSystemicExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem precaution = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstPrecaution = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem urgentCare = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstUrgentCare = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem personalHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstPersonalHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem obstetricsHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstObstetricsHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem menstrualHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstMenstrualHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem developmentHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstDevelopmentHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem perinatalHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstPerinatalHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem immunizationHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstImmunizationHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem feedingHistory = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstFeedingHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem obstetricExamination = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstObstetricExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem gynaecologicalExamination = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstGynaecologicalExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    private final PrescriptionCustomItem menstrualExamination = new PrescriptionCustomItem();
    private List<GrdPrescriptionCustomItem_Mdl> lstMenstrualExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();

    private void topControlEvents(UserMenu menuPermission) {
        topControl.setTitle(this.asWidget().getTitle());
        topControl.setRefreshRequired(false);

        for (String controlPermission : menuPermission.getControlPermission()) {
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(addMedicineEditable)) {
                medicineEditable = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(addFrequencyEditable)) {
                frequencyEditable = true;
            }
            if (DmShared.xNull(controlPermission).equalsIgnoreCase(addAllergyEditable)) {
                allergyEditable = true;
            }
        }

        topControl.setUserPermission(menuPermission, (VoucherPermissionHandler) null);

        gui.addCallback(new DmGUI.Callback() {

            @Override
            public void initialized() {
                if (isCopyGUI) {
                    initializeGUI(gui.getConfigCode(), DmShared.xNull(guiCopyFromDocId));
                    isCopyGUI = false;
                    guiCopyFromDocId = "";
                }
            }

            @Override
            public String getGUITemplate() {
                return "";
            }

            @Override
            public Object addCustom(DmGUIControl_Mdl dmGUIControl_Mdl, FlowPanel flwTr, Object tabIndexAfter) {
                return manageCustomControl(dmGUIControl_Mdl, flwTr, tabIndexAfter);
            }

            @Override
            public void removeCustom(DmTopControl topControl) {
            }

            @Override
            public void guiDataCopied(DmGUI_Mdl gui_Mdl, List<String> listCustomWidgetToCopy, String copyFromDocId, HashMap<String, String> param) {
                if (gui_Mdl != null) {
                    gui.initialize(gui_Mdl);
                    copyPrescriptionCustomData(copyFromDocId, listCustomWidgetToCopy);
                }
            }
        });

        topControl.addNewHandeller(new DmTopControl.eNew() {
            @Override
            public void newClicked() {
                clear();
                manageCustomWidgetDisplay();
                txtHConsultant.setFocus(true);
            }
        });

        topControl.addEditHandeller(new DmTopControl.eEdit() {
            @Override
            public void editClicked() {
                manageCustomWidgetDisplay();
                txtHConsultant.setFocus(true);
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

        topControl.addSaveHandeller(new DmTopControl.eSave() {
            @Override
            public void saveClicked() {
                topControlSave();
            }
        });

        topControl.addCloseHandeller(new DmTopControl.eClose() {
            @Override
            public void closeClicked() {
                DmClient.<SetWidgetInvoker>getStoreItem("swiEvent").closeWidget(PrescriptionValueTemplate.this);
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

    private Object manageCustomControl(DmGUIControl_Mdl dmGUIControl_Mdl, FlowPanel flwTd, Object tabIndexAfter) {
        Object obj = null;
        try {
            if (dmGUIControl_Mdl != null) {
                FlowPanel flwTemp;

                if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Allergy")) {
                    /*====================================< Add Allergy >====================================*/
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblAllergy);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblAllergy.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblAllergy.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblAllergy.setText("Allergy");
                        lblAllergy.setTitle("Allergy");
                    }
                    lblAllergy.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblAllergy.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblAllergy.getElement().getStyle().setColor("blue");
                    lblAllergy.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblAllergy.getElement().setAttribute("style", lblAllergy.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }

                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtAllergy;
                    flwTemp.add(topControl.viaList.add(txtAllergy, tabIndexAfter));
                    txtAllergy.setTitle("Allergy");
                    txtAllergy.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtAllergy.getElement().setAttribute("style", txtAllergy.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("AllergyCpt")) {
                    /*====================================< Add Allergy >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblAllergy);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblAllergy.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblAllergy.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblAllergy.setText("Allergy");
                        lblAllergy.setTitle("Allergy");
                    }
                    lblAllergy.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblAllergy.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblAllergy.getElement().getStyle().setColor("blue");
                    lblAllergy.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblAllergy.getElement().setAttribute("style", lblAllergy.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("AllergyCtrl")) {
                    /*====================================< Add Allergy >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtAllergy;
                    flwTemp.add(topControl.viaList.add(txtAllergy, tabIndexAfter));
                    txtAllergy.setTitle("Allergy");
                    txtAllergy.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtAllergy.getElement().setAttribute("style", txtAllergy.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Complaints")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Complaints >====================================*/
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblComplaint);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblComplaint.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblComplaint.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblComplaint.setText("Complaints");
                        lblComplaint.setTitle("Complaints");
                    }
                    lblComplaint.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblComplaint.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblComplaint.getElement().getStyle().setColor("blue");
                    lblComplaint.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblComplaint.getElement().setAttribute("style", lblComplaint.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtComplaint;
                    flwTemp.add(topControl.viaList.add(txtComplaint, tabIndexAfter));
                    txtComplaint.setTitle("Complaint");
                    txtComplaint.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtComplaint.getElement().setAttribute("style", txtComplaint.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ComplaintsCpt")) {
                    /*====================================< Add Complaints >====================================*/
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblComplaint);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblComplaint.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblComplaint.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblComplaint.setText("Complaints");
                        lblComplaint.setTitle("Complaints");
                    }
                    lblComplaint.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblComplaint.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblComplaint.getElement().getStyle().setColor("blue");
                    lblComplaint.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblComplaint.getElement().setAttribute("style", lblComplaint.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ComplaintsCtrl")) {
                    /*====================================< Add Complaints >====================================*/
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtComplaint;
                    flwTemp.add(topControl.viaList.add(txtComplaint, tabIndexAfter));
                    txtComplaint.setTitle("Complaint");
                    txtComplaint.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtComplaint.getElement().setAttribute("style", txtComplaint.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Comorbid")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Complaints >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblComorbid);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblComorbid.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblComorbid.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblComorbid.setText("Comorbid");
                        lblComorbid.setTitle("Comorbid");
                    }
                    lblComorbid.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblComorbid.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblComorbid.getElement().getStyle().setColor("blue");
                    lblComorbid.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblComorbid.getElement().setAttribute("style", lblComorbid.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtComorbid;
                    flwTemp.add(topControl.viaList.add(txtComorbid, tabIndexAfter));
                    txtComorbid.setTitle("Comorbid");
                    txtComorbid.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtComorbid.getElement().setAttribute("style", txtComorbid.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ComorbidCpt")) {
                    /*====================================< Add Complaints >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblComorbid);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblComorbid.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblComorbid.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblComorbid.setText("Comorbid");
                        lblComorbid.setTitle("Comorbid");
                    }
                    lblComorbid.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblComorbid.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblComorbid.getElement().getStyle().setColor("blue");
                    lblComorbid.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblComorbid.getElement().setAttribute("style", lblComorbid.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ComorbidCtrl")) {

                    /*====================================< Add Complaints >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtComorbid;
                    flwTemp.add(topControl.viaList.add(txtComorbid, tabIndexAfter));
                    txtComorbid.setTitle("Comorbid");
                    txtComorbid.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtComorbid.getElement().setAttribute("style", txtComorbid.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Instruction")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add PrescriptionCustomItem >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblInstruction);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblInstruction.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblInstruction.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblInstruction.setText("Instruction");
                        lblInstruction.setTitle("Instruction");
                    }
                    lblInstruction.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblInstruction.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblInstruction.getElement().getStyle().setColor("blue");
                    lblInstruction.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblInstruction.getElement().setAttribute("style", lblInstruction.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtInstruction;
                    flwTemp.add(topControl.viaList.add(txtInstruction, tabIndexAfter));
                    txtInstruction.setTitle("Instruction");
                    txtInstruction.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtInstruction.getElement().setAttribute("style", txtInstruction.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("InstructionCpt")) {
                    /*====================================< Add PrescriptionCustomItem >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblInstruction);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblInstruction.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblInstruction.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblInstruction.setText("Instruction");
                        lblInstruction.setTitle("Instruction");
                    }
                    lblInstruction.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblInstruction.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblInstruction.getElement().getStyle().setColor("blue");
                    lblInstruction.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblInstruction.getElement().setAttribute("style", lblInstruction.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("InstructionCtrl")) {
                    /*====================================< Add PrescriptionCustomItem >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtInstruction;
                    flwTemp.add(topControl.viaList.add(txtInstruction, tabIndexAfter));
                    txtInstruction.setTitle("Instruction");
                    txtInstruction.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtInstruction.getElement().setAttribute("style", txtInstruction.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Family History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Family History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblFamilyHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblFamilyHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblFamilyHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblFamilyHistory.setText("Family History");
                        lblFamilyHistory.setTitle("Family History");
                    }
                    lblFamilyHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblFamilyHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblFamilyHistory.getElement().getStyle().setColor("blue");
                    lblFamilyHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblFamilyHistory.getElement().setAttribute("style", lblFamilyHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtFamilyHistory;
                    flwTemp.add(topControl.viaList.add(txtFamilyHistory, tabIndexAfter));
                    txtFamilyHistory.setTitle("Family History");
                    txtFamilyHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtFamilyHistory.getElement().setAttribute("style", txtFamilyHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("FamilyHistoryCpt")) {
                    /*====================================< Add Family History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblFamilyHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblFamilyHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblFamilyHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblFamilyHistory.setText("Family History");
                        lblFamilyHistory.setTitle("Family History");
                    }
                    lblFamilyHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblFamilyHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblFamilyHistory.getElement().getStyle().setColor("blue");
                    lblFamilyHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblFamilyHistory.getElement().setAttribute("style", lblFamilyHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("FamilyHistoryCtrl")) {
                    /*====================================< Add Family History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtFamilyHistory;
                    flwTemp.add(topControl.viaList.add(txtFamilyHistory, tabIndexAfter));
                    txtFamilyHistory.setTitle("Family History");
                    txtFamilyHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtFamilyHistory.getElement().setAttribute("style", txtFamilyHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Habit")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Habit >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblHabit);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblHabit.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblHabit.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblHabit.setText("Habit");
                        lblHabit.setTitle("Habit");
                    }
                    lblHabit.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblHabit.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblHabit.getElement().getStyle().setColor("blue");
                    lblHabit.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblHabit.getElement().setAttribute("style", lblHabit.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtHabit;
                    flwTemp.add(topControl.viaList.add(txtHabit, tabIndexAfter));
                    txtHabit.setTitle("Habit");
                    txtHabit.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtHabit.getElement().setAttribute("style", txtHabit.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("HabitCpt")) {
                    /*====================================< Add Habit >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblHabit);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblHabit.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblHabit.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblHabit.setText("Habit");
                        lblHabit.setTitle("Habit");
                    }
                    lblHabit.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblHabit.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblHabit.getElement().getStyle().setColor("blue");
                    lblHabit.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblHabit.getElement().setAttribute("style", lblHabit.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("HabitCtrl")) {
                    /*====================================< Add Habit >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtHabit;
                    flwTemp.add(topControl.viaList.add(txtHabit, tabIndexAfter));
                    txtHabit.setTitle("Habit");
                    txtHabit.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtHabit.getElement().setAttribute("style", txtHabit.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("History Of Past Illness")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Past History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblHistoryOfPastIllness);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPastIllness.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblHistoryOfPastIllness.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblHistoryOfPastIllness.setText("History Of Past Illness");
                        lblHistoryOfPastIllness.setTitle("History Of Past Illness");
                    }
                    lblHistoryOfPastIllness.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblHistoryOfPastIllness.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblHistoryOfPastIllness.getElement().getStyle().setColor("blue");
                    lblHistoryOfPastIllness.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPastIllness.getElement().setAttribute("style", lblHistoryOfPastIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtHistoryOfPastIllness;
                    flwTemp.add(topControl.viaList.add(txtHistoryOfPastIllness, tabIndexAfter));
                    txtHistoryOfPastIllness.setTitle("History Of Past Illness");
                    txtHistoryOfPastIllness.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtHistoryOfPastIllness.getElement().setAttribute("style", txtHistoryOfPastIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("HistoryOfPastIllnessCpt")) {
                    /*====================================< Add Past History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblHistoryOfPastIllness);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPastIllness.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblHistoryOfPastIllness.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblHistoryOfPastIllness.setText("History Of Past Illness");
                        lblHistoryOfPastIllness.setTitle("History Of Past Illness");
                    }
                    lblHistoryOfPastIllness.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblHistoryOfPastIllness.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblHistoryOfPastIllness.getElement().getStyle().setColor("blue");
                    lblHistoryOfPastIllness.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPastIllness.getElement().setAttribute("style", lblHistoryOfPastIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("HistoryOfPastIllnessCtrl")) {
                    /*====================================< Add Past History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtHistoryOfPastIllness;
                    flwTemp.add(topControl.viaList.add(txtHistoryOfPastIllness, tabIndexAfter));
                    txtHistoryOfPastIllness.setTitle("History Of Past Illness");
                    txtHistoryOfPastIllness.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtHistoryOfPastIllness.getElement().setAttribute("style", txtHistoryOfPastIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("History Of Present Illness")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Present History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblHistoryOfPresentIllness);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPresentIllness.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblHistoryOfPresentIllness.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblHistoryOfPresentIllness.setText("History Of Present Illness");
                        lblHistoryOfPresentIllness.setTitle("History Of Present Illness");
                    }
                    lblHistoryOfPresentIllness.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblHistoryOfPresentIllness.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblHistoryOfPresentIllness.getElement().getStyle().setColor("blue");
                    lblHistoryOfPresentIllness.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPresentIllness.getElement().setAttribute("style", lblHistoryOfPresentIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtHistoryOfPresentIllness;
                    flwTemp.add(topControl.viaList.add(txtHistoryOfPresentIllness, tabIndexAfter));
                    txtHistoryOfPresentIllness.setTitle("History Of Present Illnesss");
                    txtHistoryOfPresentIllness.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtHistoryOfPresentIllness.getElement().setAttribute("style", txtHistoryOfPresentIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("HistoryOfPresentIllnessCpt")) {
                    /*====================================< Add Present History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblHistoryOfPresentIllness);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPresentIllness.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblHistoryOfPresentIllness.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblHistoryOfPresentIllness.setText("History Of Present Illness");
                        lblHistoryOfPresentIllness.setTitle("History Of Present Illness");
                    }
                    lblHistoryOfPresentIllness.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblHistoryOfPresentIllness.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblHistoryOfPresentIllness.getElement().getStyle().setColor("blue");
                    lblHistoryOfPresentIllness.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblHistoryOfPresentIllness.getElement().setAttribute("style", lblHistoryOfPresentIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("HistoryOfPresentIllnessCtrl")) {
                    /*====================================< Add Present History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtHistoryOfPresentIllness;
                    flwTemp.add(topControl.viaList.add(txtHistoryOfPresentIllness, tabIndexAfter));
                    txtHistoryOfPresentIllness.setTitle("History Of Present Illnesss");
                    txtHistoryOfPresentIllness.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtHistoryOfPresentIllness.getElement().setAttribute("style", txtHistoryOfPresentIllness.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Physical Examination")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Physical Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPhysicalExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPhysicalExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPhysicalExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPhysicalExamination.setText("Physical Examination");
                        lblPhysicalExamination.setTitle("Physical Examination");
                    }
                    lblPhysicalExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPhysicalExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPhysicalExamination.getElement().getStyle().setColor("blue");
                    lblPhysicalExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPhysicalExamination.getElement().setAttribute("style", lblPhysicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPhysicalExamination;
                    flwTemp.add(topControl.viaList.add(txtPhysicalExamination, tabIndexAfter));
                    txtPhysicalExamination.setTitle("Physical Examination");
                    txtPhysicalExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPhysicalExamination.getElement().setAttribute("style", txtPhysicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PhysicalExaminationCpt")) {
                    /*====================================< Add Physical Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPhysicalExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPhysicalExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPhysicalExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPhysicalExamination.setText("Physical Examination");
                        lblPhysicalExamination.setTitle("Physical Examination");
                    }
                    lblPhysicalExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPhysicalExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPhysicalExamination.getElement().getStyle().setColor("blue");
                    lblPhysicalExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPhysicalExamination.getElement().setAttribute("style", lblPhysicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PhysicalExaminationCtrl")) {
                    /*====================================< Add Physical Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPhysicalExamination;
                    flwTemp.add(topControl.viaList.add(txtPhysicalExamination, tabIndexAfter));
                    txtPhysicalExamination.setTitle("Physical Examination");
                    txtPhysicalExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPhysicalExamination.getElement().setAttribute("style", txtPhysicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Systemic Examination")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Systemic Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblSystemicExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblSystemicExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblSystemicExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblSystemicExamination.setText("Systemic Examination");
                        lblSystemicExamination.setTitle("Systemic Examination");
                    }
                    lblSystemicExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblSystemicExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblSystemicExamination.getElement().getStyle().setColor("blue");
                    lblSystemicExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblSystemicExamination.getElement().setAttribute("style", lblSystemicExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtSystemicExamination;
                    flwTemp.add(topControl.viaList.add(txtSystemicExamination, tabIndexAfter));
                    txtSystemicExamination.setTitle("Systemic Examination");
                    txtSystemicExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtSystemicExamination.getElement().setAttribute("style", txtSystemicExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("SystemicExaminationCpt")) {
                    /*====================================< Add Systemic Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblSystemicExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblSystemicExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblSystemicExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblSystemicExamination.setText("Systemic Examination");
                        lblSystemicExamination.setTitle("Systemic Examination");
                    }
                    lblSystemicExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblSystemicExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblSystemicExamination.getElement().getStyle().setColor("blue");
                    lblSystemicExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblSystemicExamination.getElement().setAttribute("style", lblSystemicExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("SystemicExaminationCtrl")) {
                    /*====================================< Add Systemic Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtSystemicExamination;
                    flwTemp.add(topControl.viaList.add(txtSystemicExamination, tabIndexAfter));
                    txtSystemicExamination.setTitle("Systemic Examination");
                    txtSystemicExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtSystemicExamination.getElement().setAttribute("style", txtSystemicExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);
                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Precaution")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Precaution >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPrecaution);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPrecaution.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPrecaution.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPrecaution.setText("Precaution");
                        lblPrecaution.setTitle("Precaution");
                    }
                    lblPrecaution.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPrecaution.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPrecaution.getElement().getStyle().setColor("blue");
                    lblPrecaution.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPrecaution.getElement().setAttribute("style", lblPrecaution.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPrecaution;
                    flwTemp.add(topControl.viaList.add(txtPrecaution, tabIndexAfter));
                    txtPrecaution.setTitle("Precaution");
                    txtPrecaution.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPrecaution.getElement().setAttribute("style", txtPrecaution.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PrecautionCpt")) {
                    /*====================================< Add Precaution >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPrecaution);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPrecaution.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPrecaution.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPrecaution.setText("Precaution");
                        lblPrecaution.setTitle("Precaution");
                    }
                    lblPrecaution.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPrecaution.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPrecaution.getElement().getStyle().setColor("blue");
                    lblPrecaution.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPrecaution.getElement().setAttribute("style", lblPrecaution.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PrecautionCtrl")) {
                    /*====================================< Add Precaution >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPrecaution;
                    flwTemp.add(topControl.viaList.add(txtPrecaution, tabIndexAfter));
                    txtPrecaution.setTitle("Precaution");
                    txtPrecaution.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPrecaution.getElement().setAttribute("style", txtPrecaution.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Urgent Care")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Urgent Care >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblUrgentCare);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblUrgentCare.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblUrgentCare.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblUrgentCare.setText("Urgent Care");
                        lblUrgentCare.setTitle("Urgent Care");
                    }
                    lblUrgentCare.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblUrgentCare.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblUrgentCare.getElement().getStyle().setColor("blue");
                    lblUrgentCare.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblUrgentCare.getElement().setAttribute("style", lblUrgentCare.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtUrgentCare;
                    flwTemp.add(topControl.viaList.add(txtUrgentCare, tabIndexAfter));
                    txtUrgentCare.setTitle("Urgent Care");
                    txtUrgentCare.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtUrgentCare.getElement().setAttribute("style", txtUrgentCare.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("UrgentCareCpt")) {
                    /*====================================< Add Urgent Care >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblUrgentCare);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblUrgentCare.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblUrgentCare.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblUrgentCare.setText("Urgent Care");
                        lblUrgentCare.setTitle("Urgent Care");
                    }
                    lblUrgentCare.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblUrgentCare.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblUrgentCare.getElement().getStyle().setColor("blue");
                    lblUrgentCare.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblUrgentCare.getElement().setAttribute("style", lblUrgentCare.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("UrgentCareCtrl")) {
                    /*====================================< Add Urgent Care >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtUrgentCare;
                    flwTemp.add(topControl.viaList.add(txtUrgentCare, tabIndexAfter));
                    txtUrgentCare.setTitle("Urgent Care");
                    txtUrgentCare.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtUrgentCare.getElement().setAttribute("style", txtUrgentCare.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Personal History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Personal History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPersonalHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPersonalHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPersonalHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPersonalHistory.setText("Personal History");
                        lblPersonalHistory.setTitle("Personal History");
                    }
                    lblPersonalHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPersonalHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPersonalHistory.getElement().getStyle().setColor("blue");
                    lblPersonalHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPersonalHistory.getElement().setAttribute("style", lblPersonalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPersonalHistory;
                    flwTemp.add(topControl.viaList.add(txtPersonalHistory, tabIndexAfter));
                    txtPersonalHistory.setTitle("Personal History");
                    txtPersonalHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPersonalHistory.getElement().setAttribute("style", txtPersonalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PersonalHistoryCpt")) {
                    /*====================================< Add Personal History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPersonalHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPersonalHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPersonalHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPersonalHistory.setText("Personal History");
                        lblPersonalHistory.setTitle("Personal History");
                    }
                    lblPersonalHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPersonalHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPersonalHistory.getElement().getStyle().setColor("blue");
                    lblPersonalHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPersonalHistory.getElement().setAttribute("style", lblPersonalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PersonalHistoryCtrl")) {
                    /*====================================< Add Personal History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPersonalHistory;
                    flwTemp.add(topControl.viaList.add(txtPersonalHistory, tabIndexAfter));
                    txtPersonalHistory.setTitle("Personal History");
                    txtPersonalHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPersonalHistory.getElement().setAttribute("style", txtPersonalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Obstetrics History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Obstetrics History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblObstetricsHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblObstetricsHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblObstetricsHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblObstetricsHistory.setText("Obstetrics History");
                        lblObstetricsHistory.setTitle("Obstetrics History");
                    }
                    lblObstetricsHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblObstetricsHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblObstetricsHistory.getElement().getStyle().setColor("blue");
                    lblObstetricsHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblObstetricsHistory.getElement().setAttribute("style", lblObstetricsHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtObstetricsHistory;
                    flwTemp.add(topControl.viaList.add(txtObstetricsHistory, tabIndexAfter));
                    txtObstetricsHistory.setTitle("Obstetrics History");
                    txtObstetricsHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtObstetricsHistory.getElement().setAttribute("style", txtObstetricsHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ObstetricsHistoryCpt")) {
                    /*====================================< Add Obstetrics History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblObstetricsHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblObstetricsHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblObstetricsHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblObstetricsHistory.setText("Obstetrics History");
                        lblObstetricsHistory.setTitle("Obstetrics History");
                    }
                    lblObstetricsHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblObstetricsHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblObstetricsHistory.getElement().getStyle().setColor("blue");
                    lblObstetricsHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblObstetricsHistory.getElement().setAttribute("style", lblObstetricsHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ObstetricsHistoryCtrl")) {
                    /*====================================< Add Obstetrics History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtObstetricsHistory;
                    flwTemp.add(topControl.viaList.add(txtObstetricsHistory, tabIndexAfter));
                    txtObstetricsHistory.setTitle("Obstetrics History");
                    txtObstetricsHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtObstetricsHistory.getElement().setAttribute("style", txtObstetricsHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Menstrual History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Menstrual History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblMenstrualHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblMenstrualHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblMenstrualHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblMenstrualHistory.setText("Menstrual History");
                        lblMenstrualHistory.setTitle("Menstrual History");
                    }
                    lblMenstrualHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblMenstrualHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblMenstrualHistory.getElement().getStyle().setColor("blue");
                    lblMenstrualHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblMenstrualHistory.getElement().setAttribute("style", lblMenstrualHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtMenstrualHistory;
                    flwTemp.add(topControl.viaList.add(txtMenstrualHistory, tabIndexAfter));
                    txtMenstrualHistory.setTitle("Menstrual History");
                    txtMenstrualHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtMenstrualHistory.getElement().setAttribute("style", txtMenstrualHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("MenstrualHistoryCpt")) {
                    /*====================================< Add Menstrual History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblMenstrualHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblMenstrualHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblMenstrualHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblMenstrualHistory.setText("Menstrual History");
                        lblMenstrualHistory.setTitle("Menstrual History");
                    }
                    lblMenstrualHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblMenstrualHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblMenstrualHistory.getElement().getStyle().setColor("blue");
                    lblMenstrualHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblMenstrualHistory.getElement().setAttribute("style", lblMenstrualHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("MenstrualHistoryCtrl")) {
                    /*====================================< Add Menstrual History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtMenstrualHistory;
                    flwTemp.add(topControl.viaList.add(txtMenstrualHistory, tabIndexAfter));
                    txtMenstrualHistory.setTitle("Menstrual History");
                    txtMenstrualHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtMenstrualHistory.getElement().setAttribute("style", txtMenstrualHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Development History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Development History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblDevelopmentHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblDevelopmentHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblDevelopmentHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblDevelopmentHistory.setText("Development History");
                        lblDevelopmentHistory.setTitle("Development History");
                    }
                    lblDevelopmentHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblDevelopmentHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblDevelopmentHistory.getElement().getStyle().setColor("blue");
                    lblDevelopmentHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblDevelopmentHistory.getElement().setAttribute("style", lblDevelopmentHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtDevelopmentHistory;
                    flwTemp.add(topControl.viaList.add(txtDevelopmentHistory, tabIndexAfter));
                    txtDevelopmentHistory.setTitle("Development History");
                    txtDevelopmentHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtDevelopmentHistory.getElement().setAttribute("style", txtDevelopmentHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("DevelopmentHistoryCpt")) {
                    /*====================================< Add Development History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblDevelopmentHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblDevelopmentHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblDevelopmentHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblDevelopmentHistory.setText("Development History");
                        lblDevelopmentHistory.setTitle("Development History");
                    }
                    lblDevelopmentHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblDevelopmentHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblDevelopmentHistory.getElement().getStyle().setColor("blue");
                    lblDevelopmentHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblDevelopmentHistory.getElement().setAttribute("style", lblDevelopmentHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("DevelopmentHistoryCtrl")) {
                    /*====================================< Add Development History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtDevelopmentHistory;
                    flwTemp.add(topControl.viaList.add(txtDevelopmentHistory, tabIndexAfter));
                    txtDevelopmentHistory.setTitle("Development History");
                    txtDevelopmentHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtDevelopmentHistory.getElement().setAttribute("style", txtDevelopmentHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Perinatal History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Perinatal History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPerinatalHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPerinatalHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPerinatalHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPerinatalHistory.setText("Perinatal History");
                        lblPerinatalHistory.setTitle("Perinatal History");
                    }
                    lblPerinatalHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPerinatalHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPerinatalHistory.getElement().getStyle().setColor("blue");
                    lblPerinatalHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPerinatalHistory.getElement().setAttribute("style", lblPerinatalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPerinatalHistory;
                    flwTemp.add(topControl.viaList.add(txtPerinatalHistory, tabIndexAfter));
                    txtPerinatalHistory.setTitle("Perinatal History");
                    txtPerinatalHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPerinatalHistory.getElement().setAttribute("style", txtPerinatalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PerinatalHistoryCpt")) {
                    /*====================================< Add Perinatal History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblPerinatalHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblPerinatalHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblPerinatalHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblPerinatalHistory.setText("Perinatal History");
                        lblPerinatalHistory.setTitle("Perinatal History");
                    }
                    lblPerinatalHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblPerinatalHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblPerinatalHistory.getElement().getStyle().setColor("blue");
                    lblPerinatalHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblPerinatalHistory.getElement().setAttribute("style", lblPerinatalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("PerinatalHistoryCtrl")) {
                    /*====================================< Add Perinatal History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtPerinatalHistory;
                    flwTemp.add(topControl.viaList.add(txtPerinatalHistory, tabIndexAfter));
                    txtPerinatalHistory.setTitle("Perinatal History");
                    txtPerinatalHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtPerinatalHistory.getElement().setAttribute("style", txtPerinatalHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Immunization History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Immunization History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblImmunizationHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblImmunizationHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblImmunizationHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblImmunizationHistory.setText("Immunization History");
                        lblImmunizationHistory.setTitle("Immunization History");
                    }
                    lblImmunizationHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblImmunizationHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblImmunizationHistory.getElement().getStyle().setColor("blue");
                    lblImmunizationHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblImmunizationHistory.getElement().setAttribute("style", lblImmunizationHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtImmunizationHistory;
                    flwTemp.add(topControl.viaList.add(txtImmunizationHistory, tabIndexAfter));
                    txtImmunizationHistory.setTitle("Immunization History");
                    txtImmunizationHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtImmunizationHistory.getElement().setAttribute("style", txtImmunizationHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ImmunizationHistoryCpt")) {
                    /*====================================< Add Immunization History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblImmunizationHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblImmunizationHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblImmunizationHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblImmunizationHistory.setText("Immunization History");
                        lblImmunizationHistory.setTitle("Immunization History");
                    }
                    lblImmunizationHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblImmunizationHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblImmunizationHistory.getElement().getStyle().setColor("blue");
                    lblImmunizationHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblImmunizationHistory.getElement().setAttribute("style", lblImmunizationHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ImmunizationHistoryCtrl")) {
                    /*====================================< Add Immunization History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtImmunizationHistory;
                    flwTemp.add(topControl.viaList.add(txtImmunizationHistory, tabIndexAfter));
                    txtImmunizationHistory.setTitle("Immunization History");
                    txtImmunizationHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtImmunizationHistory.getElement().setAttribute("style", txtImmunizationHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Feeding History")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Feeding History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblFeedingHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblFeedingHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblFeedingHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblFeedingHistory.setText("Feeding History");
                        lblFeedingHistory.setTitle("Feeding History");
                    }
                    lblFeedingHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblFeedingHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblFeedingHistory.getElement().getStyle().setColor("blue");
                    lblFeedingHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblFeedingHistory.getElement().setAttribute("style", lblFeedingHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtFeedingHistory;
                    flwTemp.add(topControl.viaList.add(txtFeedingHistory, tabIndexAfter));
                    txtFeedingHistory.setTitle("Feeding History");
                    txtFeedingHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtFeedingHistory.getElement().setAttribute("style", txtFeedingHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("FeedingHistoryCpt")) {
                    /*====================================< Add Feeding History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblFeedingHistory);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblFeedingHistory.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblFeedingHistory.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblFeedingHistory.setText("Feeding History");
                        lblFeedingHistory.setTitle("Feeding History");
                    }
                    lblFeedingHistory.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblFeedingHistory.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblFeedingHistory.getElement().getStyle().setColor("blue");
                    lblFeedingHistory.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblFeedingHistory.getElement().setAttribute("style", lblFeedingHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("FeedingHistoryCtrl")) {
                    /*====================================< Add Feeding History >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtFeedingHistory;
                    flwTemp.add(topControl.viaList.add(txtFeedingHistory, tabIndexAfter));
                    txtFeedingHistory.setTitle("Feeding History");
                    txtFeedingHistory.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtFeedingHistory.getElement().setAttribute("style", txtFeedingHistory.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Obstetric Examination")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Obstetric Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblObstetricExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblObstetricExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblObstetricExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblObstetricExamination.setText("Obstetric Examination");
                        lblObstetricExamination.setTitle("Obstetric Examination");
                    }
                    lblObstetricExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblObstetricExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblObstetricExamination.getElement().getStyle().setColor("blue");
                    lblObstetricExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblObstetricExamination.getElement().setAttribute("style", lblObstetricExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtObstetricExamination;
                    flwTemp.add(topControl.viaList.add(txtObstetricExamination, tabIndexAfter));
                    txtObstetricExamination.setTitle("Obstetric Examination");
                    txtObstetricExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtObstetricExamination.getElement().setAttribute("style", txtSystemicExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ObstetricExaminationCpt")) {
                    /*====================================< Add Obstetric Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblObstetricExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblObstetricExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblObstetricExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblObstetricExamination.setText("Obstetric Examination");
                        lblObstetricExamination.setTitle("Obstetric Examination");
                    }
                    lblObstetricExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblObstetricExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblObstetricExamination.getElement().getStyle().setColor("blue");
                    lblObstetricExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblObstetricExamination.getElement().setAttribute("style", lblObstetricExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("ObstetricExaminationCtrl")) {
                    /*====================================< Add Obstetric Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtObstetricExamination;
                    flwTemp.add(topControl.viaList.add(txtObstetricExamination, tabIndexAfter));
                    txtObstetricExamination.setTitle("Obstetric Examination");
                    txtObstetricExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtObstetricExamination.getElement().setAttribute("style", txtObstetricExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Gynaecological Examination")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Gynaecological Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblGynaecologicalExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblGynaecologicalExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblGynaecologicalExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblGynaecologicalExamination.setText("Gynaecological Examination");
                        lblGynaecologicalExamination.setTitle("Gynaecological Examination");
                    }
                    lblGynaecologicalExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblGynaecologicalExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblGynaecologicalExamination.getElement().getStyle().setColor("blue");
                    lblGynaecologicalExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblGynaecologicalExamination.getElement().setAttribute("style", lblGynaecologicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtGynaecologicalExamination;
                    flwTemp.add(topControl.viaList.add(txtGynaecologicalExamination, tabIndexAfter));
                    txtGynaecologicalExamination.setTitle("Gynaecological Examination");
                    txtGynaecologicalExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtGynaecologicalExamination.getElement().setAttribute("style", txtGynaecologicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("GynaecologicalExaminationCpt")) {
                    /*====================================< Add Gynaecological Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblGynaecologicalExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblGynaecologicalExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblGynaecologicalExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblGynaecologicalExamination.setText("Gynaecological Examination");
                        lblGynaecologicalExamination.setTitle("Gynaecological Examination");
                    }
                    lblGynaecologicalExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblGynaecologicalExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblGynaecologicalExamination.getElement().getStyle().setColor("blue");
                    lblGynaecologicalExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblGynaecologicalExamination.getElement().setAttribute("style", lblGynaecologicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("GynaecologicalExaminationCtrl")) {
                    /*====================================< Add Gynaecological Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtGynaecologicalExamination;
                    flwTemp.add(topControl.viaList.add(txtGynaecologicalExamination, tabIndexAfter));
                    txtGynaecologicalExamination.setTitle("Gynaecological Examination");
                    txtGynaecologicalExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtGynaecologicalExamination.getElement().setAttribute("style", txtGynaecologicalExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("Menstrual Examination")) {
                    if (DmShared.vNull(dmGUIControl_Mdl.getCaptionWidth()).doubleValue() == 0.0) {
                        dmGUIControl_Mdl.setCaptionWidth("10");
                        dmGUIControl_Mdl.setControlWidth(String.valueOf(DmShared.iNull(dmGUIControl_Mdl.getControlWidth()).doubleValue() - 10));
                    }
                    /*====================================< Add Menstrual Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblMenstrualExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblMenstrualExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblMenstrualExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblMenstrualExamination.setText("Menstrual Examination");
                        lblMenstrualExamination.setTitle("Menstrual Examination");
                    }
                    lblMenstrualExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblMenstrualExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblMenstrualExamination.getElement().getStyle().setColor("blue");
                    lblMenstrualExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblMenstrualExamination.getElement().setAttribute("style", lblMenstrualExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtMenstrualExamination;
                    flwTemp.add(topControl.viaList.add(txtMenstrualExamination, tabIndexAfter));
                    txtMenstrualExamination.setTitle("Menstrual Examination");
                    txtMenstrualExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtMenstrualExamination.getElement().setAttribute("style", txtMenstrualExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("MenstrualExaminationCpt")) {
                    /*====================================< Add Menstrual Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getCaptionWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    flwTemp.add(lblMenstrualExamination);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim().equalsIgnoreCase("")) {
                        lblMenstrualExamination.setText(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                        lblMenstrualExamination.setTitle(DmShared.xNull(dmGUIControl_Mdl.getFieldCaption()).trim());
                    } else {
                        lblMenstrualExamination.setText("Menstrual Examination");
                        lblMenstrualExamination.setTitle("Menstrual Examination");
                    }
                    lblMenstrualExamination.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                    lblMenstrualExamination.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
                    lblMenstrualExamination.getElement().getStyle().setColor("blue");
                    lblMenstrualExamination.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
                    if (!DmShared.xNull(dmGUIControl_Mdl.getCaptionStyle()).trim().equalsIgnoreCase("")) {
                        lblMenstrualExamination.getElement().setAttribute("style", lblMenstrualExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getCaptionStyle());
                    }
                    flwTd.add(flwTemp);

                } else if (DmShared.xNull(dmGUIControl_Mdl.getCustomFieldName()).trim().equalsIgnoreCase("MenstrualExaminationCtrl")) {
                    /*====================================< Add Menstrual Examination >====================================*/
                    flwTemp = new FlowPanel();
                    DmClient.setStyle(flwTemp, DmShared.xNull(dmGUIControl_Mdl.getControlWidth()).trim() + "%", null, "left", null, DmEnum.DmPosition.NONE, false);
                    obj = txtMenstrualExamination;
                    flwTemp.add(topControl.viaList.add(txtMenstrualExamination, tabIndexAfter));
                    txtMenstrualExamination.setTitle("Menstrual Examination");
                    txtMenstrualExamination.getElement().getStyle().setColor("rgb(100, 0, 0)");
                    if (!DmShared.xNull(dmGUIControl_Mdl.getControlStyle()).trim().equalsIgnoreCase("")) {
                        txtMenstrualExamination.getElement().setAttribute("style", txtMenstrualExamination.getElement().getAttribute("style") + ";" + dmGUIControl_Mdl.getControlStyle());
                    }
                    flwTd.add(flwTemp);

                }
            }
        } catch (Exception e) {
            new DmAlertMessageBox(e.getMessage(), (DmTextBox) null).show();
        }

        return obj;
    }

    private void textHelpEvents() {
        txtName.addCallBack(new DmSuggestBox.CallBack() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getSuggestionName(DmShared.xNull(txtHGUITemplate.getTag()), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<String>>>() {

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
        txtHGUITemplate.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHGUITemplate(DmShared.iNull(txtHConsultant.getTag2nd()).toString(), helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), txtHGUITemplate).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<KeyFieldExtended>> result) {
                        if (result.isStatus()) {
                            txtHGUITemplate.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), txtHGUITemplate).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, KeyFieldExtended t1Row) {
                gridName.getGrid().getStore().clear();
                clearGUI();
                if (t1Row != null && blnStatus) {
                    gui.initializeViaConfigCode(txtHGUITemplate.getTag(), false);
                    getTamplateNames();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            txtName.setFocus(true);
                        }
                    });
                }
            }
        });
        txtHConsultant.addTextHelpCallBack(new DmTextHelp.TextHelpCallBack<KeyFieldExtended>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHConsultant(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<KeyFieldExtended>>>() {

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
                gridName.getGrid().getStore().clear();
                clearGUI();
                if (blnStatus && t1Row != null) {
                    txtHConsultant.setTag2nd(DmShared.iNull(t1Row.getSharedPreparedBy()).toString());
                    txtHGUITemplate.setTag("");
                    txtHGUITemplate.setText("");
                }
            }
        });
        txtHCopyGUI.addHelpGridCallBack(new DmHelpGrid.HelpGridCallBack<TxtHCopyGUI_Mdl>() {
            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getTxtHCopyGUI(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<TxtHCopyGUI_Mdl>>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
                    }

                    @Override
                    public void onSuccess(DmResultPageing<List<TxtHCopyGUI_Mdl>> result) {
                        if (result.isStatus()) {
                            txtHCopyGUI.setData(result.getData(), result.getPaging());
                        } else {
                            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                        }
                    }
                });
            }

            @Override
            public void getSelectedRow(boolean blnStatus, TxtHCopyGUI_Mdl t1Row) {
                if (blnStatus && t1Row != null) {
                    if (DmShared.iNull(gui.getConfigCode()).longValue() != 0 && DmShared.iNull(t1Row.getGuiConfigCode()).longValue() != 0) {
                        initializeGUI(t1Row.getGuiConfigCode(), DmShared.xNull(t1Row.getSharedKeyFieldCode()));
                    }
                }
            }
        });

        allergy.addCallBack(new Allergy.CallBack() {
            @Override
            public void onOk(List<GrdAllergy_Mdl> listAllergy) {
                if (listAllergy != null) {
                    try {
                        lstAllergy.clear();
                        lstAllergy.addAll(listAllergy);
                        txtAllergy.setText(Allergy_Shared.getFormattedString(lstAllergy));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtAllergy).show();
                    }
                }
                txtAllergy.setFocus(true);
            }

            @Override
            public void onClose() {
                txtAllergy.setFocus(true);
            }
        });
        comorbid.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listComorbid) {
                if (listComorbid != null) {
                    try {
                        lstComorbid.clear();
                        lstComorbid.addAll(listComorbid);
                        txtComorbid.setText(PrescriptionCustomItem_Shared.getFormattedString(lstComorbid));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtComorbid).show();
                    }
                }
                txtComorbid.setFocus(true);
            }

            @Override
            public void onClose() {
                txtComorbid.setFocus(true);
            }
        });
        complaint.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listComplaint) {
                if (listComplaint != null) {
                    try {
                        lstComplaint.clear();
                        lstComplaint.addAll(listComplaint);
                        txtComplaint.setText(PrescriptionCustomItem_Shared.getFormattedString(lstComplaint));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtComplaint).show();
                    }
                }
                txtComplaint.setFocus(true);
            }

            @Override
            public void onClose() {
                txtComplaint.setFocus(true);
            }
        });
        instruction.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listDiagnosis) {
                if (listDiagnosis != null) {
                    try {
                        lstInstruction.clear();
                        lstInstruction.addAll(listDiagnosis);
                        txtInstruction.setText(PrescriptionCustomItem_Shared.getFormattedString(lstInstruction));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtInstruction).show();
                    }
                }
                txtInstruction.setFocus(true);
            }

            @Override
            public void onClose() {
                txtInstruction.setFocus(true);
            }
        });
        familyHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listFamilyHistory) {
                if (listFamilyHistory != null) {
                    try {
                        lstFamilyHistory.clear();
                        lstFamilyHistory.addAll(listFamilyHistory);
                        txtFamilyHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstFamilyHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtFamilyHistory).show();
                    }
                }
                txtFamilyHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtFamilyHistory.setFocus(true);
            }
        });
        habit.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listHabit) {
                if (listHabit != null) {
                    try {
                        lstHabit.clear();
                        lstHabit.addAll(listHabit);
                        txtHabit.setText(PrescriptionCustomItem_Shared.getFormattedString(lstHabit));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtHabit).show();
                    }
                }
                txtHabit.setFocus(true);
            }

            @Override
            public void onClose() {
                txtHabit.setFocus(true);
            }
        });
        historyOfPastIllness.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listHistoryOfPastIllness) {
                if (listHistoryOfPastIllness != null) {
                    try {
                        lstHistoryOfPastIllness.clear();
                        lstHistoryOfPastIllness.addAll(listHistoryOfPastIllness);
                        txtHistoryOfPastIllness.setText(PrescriptionCustomItem_Shared.getFormattedString(lstHistoryOfPastIllness));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtHistoryOfPastIllness).show();
                    }
                }
                txtHistoryOfPastIllness.setFocus(true);
            }

            @Override
            public void onClose() {
                txtHistoryOfPastIllness.setFocus(true);
            }
        });
        historyOfPresentIllness.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listHistoryOfPresentIllness) {
                if (listHistoryOfPresentIllness != null) {
                    try {
                        lstHistoryOfPresentIllness.clear();
                        lstHistoryOfPresentIllness.addAll(listHistoryOfPresentIllness);
                        txtHistoryOfPresentIllness.setText(PrescriptionCustomItem_Shared.getFormattedString(lstHistoryOfPresentIllness));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtHistoryOfPresentIllness).show();
                    }
                }
                txtHistoryOfPresentIllness.setFocus(true);
            }

            @Override
            public void onClose() {
                txtHistoryOfPresentIllness.setFocus(true);
            }
        });
        physicalExamination.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listPhysicalExamination) {
                if (listPhysicalExamination != null) {
                    try {
                        lstPhysicalExamination.clear();
                        lstPhysicalExamination.addAll(listPhysicalExamination);
                        txtPhysicalExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPhysicalExamination));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtPhysicalExamination).show();
                    }
                }
                txtPhysicalExamination.setFocus(true);
            }

            @Override
            public void onClose() {
                txtPhysicalExamination.setFocus(true);
            }
        });
        systemicExamination.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listSystemicExamination) {
                if (listSystemicExamination != null) {
                    try {
                        lstSystemicExamination.clear();
                        lstSystemicExamination.addAll(listSystemicExamination);
                        txtSystemicExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstSystemicExamination));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtSystemicExamination).show();
                    }
                }
                txtSystemicExamination.setFocus(true);
            }

            @Override
            public void onClose() {
                txtSystemicExamination.setFocus(true);
            }
        });
        precaution.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listPrecaution) {
                if (listPrecaution != null) {
                    try {
                        lstPrecaution.clear();
                        lstPrecaution.addAll(listPrecaution);
                        txtPrecaution.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPrecaution));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtPrecaution).show();
                    }
                }
                txtPrecaution.setFocus(true);
            }

            @Override
            public void onClose() {
                txtPrecaution.setFocus(true);
            }
        });
        urgentCare.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listUrgentCare) {
                if (listUrgentCare != null) {
                    try {
                        lstUrgentCare.clear();
                        lstUrgentCare.addAll(listUrgentCare);
                        txtUrgentCare.setText(PrescriptionCustomItem_Shared.getFormattedString(lstUrgentCare));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtUrgentCare).show();
                    }
                }
                txtUrgentCare.setFocus(true);
            }

            @Override
            public void onClose() {
                txtUrgentCare.setFocus(true);
            }
        });
        personalHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listPersonalHistory) {
                if (listPersonalHistory != null) {
                    try {
                        lstPersonalHistory.clear();
                        lstPersonalHistory.addAll(listPersonalHistory);
                        txtPersonalHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPersonalHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtPersonalHistory).show();
                    }
                }
                txtPersonalHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtPersonalHistory.setFocus(true);
            }
        });
        obstetricsHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listObstetricsHistory) {
                if (listObstetricsHistory != null) {
                    try {
                        lstObstetricsHistory.clear();
                        lstObstetricsHistory.addAll(listObstetricsHistory);
                        txtObstetricsHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstObstetricsHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtObstetricsHistory).show();
                    }
                }
                txtObstetricsHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtObstetricsHistory.setFocus(true);
            }
        });
        menstrualHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listMenstrualHistory) {
                if (listMenstrualHistory != null) {
                    try {
                        lstMenstrualHistory.clear();
                        lstMenstrualHistory.addAll(listMenstrualHistory);
                        txtMenstrualHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstMenstrualHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtMenstrualHistory).show();
                    }
                }
                txtMenstrualHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtMenstrualHistory.setFocus(true);
            }
        });
        developmentHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listDevelopmentHistory) {
                if (listDevelopmentHistory != null) {
                    try {
                        lstDevelopmentHistory.clear();
                        lstDevelopmentHistory.addAll(listDevelopmentHistory);
                        txtDevelopmentHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstDevelopmentHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtDevelopmentHistory).show();
                    }
                }
                txtDevelopmentHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtDevelopmentHistory.setFocus(true);
            }
        });
        perinatalHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listPerinatalHistory) {
                if (listPerinatalHistory != null) {
                    try {
                        lstPerinatalHistory.clear();
                        lstPerinatalHistory.addAll(listPerinatalHistory);
                        txtPerinatalHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPerinatalHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtPerinatalHistory).show();
                    }
                }
                txtPerinatalHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtPerinatalHistory.setFocus(true);
            }
        });
        immunizationHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listImmunizationHistory) {
                if (listImmunizationHistory != null) {
                    try {
                        lstImmunizationHistory.clear();
                        lstImmunizationHistory.addAll(listImmunizationHistory);
                        txtImmunizationHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstImmunizationHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtImmunizationHistory).show();
                    }
                }
                txtImmunizationHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtImmunizationHistory.setFocus(true);
            }
        });
        feedingHistory.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listFeedingHistory) {
                if (listFeedingHistory != null) {
                    try {
                        lstFeedingHistory.clear();
                        lstFeedingHistory.addAll(listFeedingHistory);
                        txtFeedingHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstFeedingHistory));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtFeedingHistory).show();
                    }
                }
                txtFeedingHistory.setFocus(true);
            }

            @Override
            public void onClose() {
                txtFeedingHistory.setFocus(true);
            }
        });
        obstetricExamination.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listObstetricExamination) {
                if (listObstetricExamination != null) {
                    try {
                        lstObstetricExamination.clear();
                        lstObstetricExamination.addAll(listObstetricExamination);
                        txtObstetricExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstObstetricExamination));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtObstetricExamination).show();
                    }
                }
                txtObstetricExamination.setFocus(true);
            }

            @Override
            public void onClose() {
                txtObstetricExamination.setFocus(true);
            }
        });
        gynaecologicalExamination.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listGynaecologicalExamination) {
                if (listGynaecologicalExamination != null) {
                    try {
                        lstGynaecologicalExamination.clear();
                        lstGynaecologicalExamination.addAll(listGynaecologicalExamination);
                        txtGynaecologicalExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstGynaecologicalExamination));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtGynaecologicalExamination).show();
                    }
                }
                txtGynaecologicalExamination.setFocus(true);
            }

            @Override
            public void onClose() {
                txtGynaecologicalExamination.setFocus(true);
            }
        });
        menstrualExamination.addCallBack(new PrescriptionCustomItem.CallBack() {
            @Override
            public void onOk(List<GrdPrescriptionCustomItem_Mdl> listMenstrualExamination) {
                if (listMenstrualExamination != null) {
                    try {
                        lstMenstrualExamination.clear();
                        lstMenstrualExamination.addAll(listMenstrualExamination);
                        txtMenstrualExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstMenstrualExamination));
                    } catch (Exception e) {
                        new DmAlertMessageBox(e.getMessage(), txtMenstrualExamination).show();
                    }
                }
                txtMenstrualExamination.setFocus(true);
            }

            @Override
            public void onClose() {
                txtMenstrualExamination.setFocus(true);
            }
        });
    }

    private void findEvents() {
        dmFind.setTitle(this.asWidget().getTitle());

        dmFind.addHelpGridCallBack(new DmFind.FindCallBack<Find_Mdl>() {

            @Override
            public void populateData(DmHelper helper, boolean blnToEndOfFile, boolean blnCallBackWait) {
                SrvAsync.invokeRPC(blnCallBackWait).getFind(helper, blnToEndOfFile, new AsyncCallback<DmResultPageing<List<Find_Mdl>>>() {

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

    private void showCustomFieldPopup() {
        customFieldPopup.show(getCustomFields());
    }

    private void showApplyTemplate() {
        txtHCopyGUI.show(mnuApplyTemplate, "");
        txtHCopyGUI.reQuery("");
    }

    public PrescriptionValueTemplate(UserMenu menuPermission, SetWidgetInvoker swi) {
        super();
        menuPermission.setUserPermission(DmShared.xNull(menuPermission.getUserPermission()).replace("P", "*"));
        SrvAsync = new ServiceInvoker<>(GWT.create(PrescriptionValueTemplate_Srv.class), PrescriptionValueTemplate.class.getName());
        this.asWidget().setTitle(widgetName);
        topControlEvents(menuPermission);
        textHelpEvents();
        findEvents();
        handlerEvents(new DmLabel[]{lblInstruction, lblAllergy, lblComorbid, lblComplaint, lblFamilyHistory, lblHabit,
            lblHistoryOfPastIllness, lblHistoryOfPresentIllness, lblPhysicalExamination, lblSystemicExamination, lblPrecaution, lblUrgentCare, lblPersonalHistory,
            lblObstetricsHistory, lblMenstrualHistory, lblDevelopmentHistory, lblPerinatalHistory, lblImmunizationHistory, lblFeedingHistory, lblObstetricExamination,
            lblGynaecologicalExamination, lblMenstrualExamination});
        swi.setWidget(this);
    }

    private void handlerEvents(DmLabel[] labels) {
        flwForm.addAttachHandler(this);
        for (final DmLabel lbl : labels) {
            lbl.addClickHandler(this);
            lbl.addMouseOverHandler(new MouseOverHandler() {
                @Override
                public void onMouseOver(MouseOverEvent event) {
                    lbl.getElement().getStyle().setColor("purple");
                }
            });
            lbl.addMouseOutHandler(new MouseOutHandler() {

                @Override
                public void onMouseOut(MouseOutEvent event) {
                    lbl.getElement().getStyle().setColor("blue");
                }
            });
        }

        txtAllergy.addKeyDownHandler(this);
        txtComorbid.addKeyDownHandler(this);
        txtComplaint.addKeyDownHandler(this);
        txtInstruction.addKeyDownHandler(this);
        txtFamilyHistory.addKeyDownHandler(this);
        txtHabit.addKeyDownHandler(this);
        txtHistoryOfPastIllness.addKeyDownHandler(this);
        txtHistoryOfPresentIllness.addKeyDownHandler(this);
        txtPhysicalExamination.addKeyDownHandler(this);
        txtSystemicExamination.addKeyDownHandler(this);
        txtPrecaution.addKeyDownHandler(this);
        txtUrgentCare.addKeyDownHandler(this);
        txtPersonalHistory.addKeyDownHandler(this);
        txtObstetricsHistory.addKeyDownHandler(this);
        txtMenstrualHistory.addKeyDownHandler(this);
        txtDevelopmentHistory.addKeyDownHandler(this);
        txtPerinatalHistory.addKeyDownHandler(this);
        txtImmunizationHistory.addKeyDownHandler(this);
        txtFeedingHistory.addKeyDownHandler(this);
        txtObstetricExamination.addKeyDownHandler(this);
        txtGynaecologicalExamination.addKeyDownHandler(this);
        txtMenstrualExamination.addKeyDownHandler(this);
    }

    @Override
    public Widget asWidget() {
        return flwForm;
    }

    @Override
    public void onClick(ClickEvent event) {
        if (event.getSource() == lblAllergy) {
            showCustomWidgets(txtAllergy);
        } else if (event.getSource() == lblComorbid) {
            showCustomWidgets(txtComorbid);
        } else if (event.getSource() == lblComplaint) {
            showCustomWidgets(txtComplaint);
        } else if (event.getSource() == lblInstruction) {
            showCustomWidgets(txtInstruction);
        } else if (event.getSource() == lblFamilyHistory) {
            showCustomWidgets(txtFamilyHistory);
        } else if (event.getSource() == lblHabit) {
            showCustomWidgets(txtHabit);
        } else if (event.getSource() == lblHistoryOfPastIllness) {
            showCustomWidgets(txtHistoryOfPastIllness);
        } else if (event.getSource() == lblHistoryOfPresentIllness) {
            showCustomWidgets(txtHistoryOfPresentIllness);
        } else if (event.getSource() == lblPhysicalExamination) {
            showCustomWidgets(txtPhysicalExamination);
        } else if (event.getSource() == lblSystemicExamination) {
            showCustomWidgets(txtSystemicExamination);
        } else if (event.getSource() == lblPrecaution) {
            showCustomWidgets(txtPrecaution);
        } else if (event.getSource() == lblUrgentCare) {
            showCustomWidgets(txtUrgentCare);
        } else if (event.getSource() == lblPersonalHistory) {
            showCustomWidgets(txtPersonalHistory);
        } else if (event.getSource() == lblObstetricsHistory) {
            showCustomWidgets(txtObstetricsHistory);
        } else if (event.getSource() == lblMenstrualHistory) {
            showCustomWidgets(txtMenstrualHistory);
        } else if (event.getSource() == lblDevelopmentHistory) {
            showCustomWidgets(txtDevelopmentHistory);
        } else if (event.getSource() == lblPerinatalHistory) {
            showCustomWidgets(txtPerinatalHistory);
        } else if (event.getSource() == lblImmunizationHistory) {
            showCustomWidgets(txtImmunizationHistory);
        } else if (event.getSource() == lblFeedingHistory) {
            showCustomWidgets(txtFeedingHistory);
        } else if (event.getSource() == lblObstetricExamination) {
            showCustomWidgets(txtObstetricExamination);
        } else if (event.getSource() == lblGynaecologicalExamination) {
            showCustomWidgets(txtGynaecologicalExamination);
        } else if (event.getSource() == lblMenstrualExamination) {
            showCustomWidgets(txtMenstrualExamination);
        }
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
            if (event.getSource() == txtAllergy) {
                showCustomWidgets(txtAllergy);
            } else if (event.getSource() == txtComorbid) {
                showCustomWidgets(txtComorbid);
            } else if (event.getSource() == txtComplaint) {
                showCustomWidgets(txtComplaint);
            } else if (event.getSource() == txtInstruction) {
                showCustomWidgets(txtInstruction);
            } else if (event.getSource() == txtFamilyHistory) {
                showCustomWidgets(txtFamilyHistory);
            } else if (event.getSource() == txtHabit) {
                showCustomWidgets(txtHabit);
            } else if (event.getSource() == txtHistoryOfPastIllness) {
                showCustomWidgets(txtHistoryOfPastIllness);
            } else if (event.getSource() == txtHistoryOfPresentIllness) {
                showCustomWidgets(txtHistoryOfPresentIllness);
            } else if (event.getSource() == txtPhysicalExamination) {
                showCustomWidgets(txtPhysicalExamination);
            } else if (event.getSource() == txtSystemicExamination) {
                showCustomWidgets(txtSystemicExamination);
            } else if (event.getSource() == txtPrecaution) {
                showCustomWidgets(txtPrecaution);
            } else if (event.getSource() == txtUrgentCare) {
                showCustomWidgets(txtUrgentCare);
            } else if (event.getSource() == txtPersonalHistory) {
                showCustomWidgets(txtPersonalHistory);
            } else if (event.getSource() == txtObstetricsHistory) {
                showCustomWidgets(txtObstetricsHistory);
            } else if (event.getSource() == txtMenstrualHistory) {
                showCustomWidgets(txtMenstrualHistory);
            } else if (event.getSource() == txtDevelopmentHistory) {
                showCustomWidgets(txtDevelopmentHistory);
            } else if (event.getSource() == txtPerinatalHistory) {
                showCustomWidgets(txtPerinatalHistory);
            } else if (event.getSource() == txtImmunizationHistory) {
                showCustomWidgets(txtImmunizationHistory);
            } else if (event.getSource() == txtFeedingHistory) {
                showCustomWidgets(txtFeedingHistory);
            } else if (event.getSource() == txtObstetricExamination) {
                showCustomWidgets(txtObstetricExamination);
            } else if (event.getSource() == txtGynaecologicalExamination) {
                showCustomWidgets(txtGynaecologicalExamination);
            } else if (event.getSource() == txtMenstrualExamination) {
                showCustomWidgets(txtMenstrualExamination);
            }
        }
    }

    @Override
    void menuClick(SelectionEvent<MenuItem> event) {
        if (event.getSelectedItem().getText().equalsIgnoreCase(mnuApplyTemplate.getText())) {
            if (!topControl.getMode().equalsIgnoreCase("Browse")) {
                showApplyTemplate();
            }
        } else if (event.getSelectedItem().getText().equalsIgnoreCase(mnuCustomFields.getText())) {
            showCustomFieldPopup();
        }
    }

    @Override
    public void searchRecord(String key) {
        topControlMoveRec(key);
    }

    @Override
    public void onAttachOrDetach(AttachEvent event) {
        if (!event.isAttached()) {
            allergy.hide();
            comorbid.hide();
            complaint.hide();
            instruction.hide();
            familyHistory.hide();
            habit.hide();
            historyOfPastIllness.hide();
            historyOfPresentIllness.hide();
            physicalExamination.hide();
            systemicExamination.hide();
            precaution.hide();
            urgentCare.hide();
            customFieldPopup.hide();
        }
    }

    @Override
    public void action(String nature, Object object, DmCallBack callBack) {
        nature = DmShared.xNull(nature).trim().toUpperCase();
        switch (DmShared.xNull(nature).trim()) {
            case "NEW":
                topControl.btnClick("New");
                HashMap paramHashMap = ((HashMap) object);
                String consultantCode = "";
                String guiConfigCode = "";

                if (paramHashMap != null && paramHashMap.containsKey("consultantCode")) {
                    consultantCode = (String) paramHashMap.get("consultantCode");
                }
                if (paramHashMap != null && paramHashMap.containsKey("guiConfigCode")) {
                    guiConfigCode = (String) paramHashMap.get("guiConfigCode");
                }

                if (paramHashMap != null && paramHashMap.containsKey("docId")) {
                    isCopyGUI = true;
                    guiCopyFromDocId = (String) paramHashMap.get("docId");
                }

                getRecordViaAction(consultantCode, guiConfigCode, callBack);
                break;

            case "FOCUS_ON_ADD":
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        txtName.setFocus(true);
                    }
                });
                break;
        }
    }

    private void getRecordViaAction(final String consultantCode, final String guiConfigCode, final DmCallBack callBack) {
        SrvAsync.invokeRPC().getRecordViaAction(consultantCode, guiConfigCode, new AsyncCallback<DmResult<PrescriptionValueTemplate_Mdl>>() {
            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtName).show();
            }

            @Override
            public void onSuccess(DmResult<PrescriptionValueTemplate_Mdl> result) {
                gridName.getGrid().getStore().clear();
                clearGUI();
                txtHConsultant.setTag2nd("");
                txtHGUITemplate.setTag("");
                txtHGUITemplate.setText("");
                if (result.isStatus()) {
                    PrescriptionValueTemplate_Mdl data = result.getData();
                    txtHConsultant.setTag(consultantCode);
                    txtHConsultant.setText(data.getConsultantName());
                    txtHGUITemplate.setTag(data.getConfigCode());
                    txtHGUITemplate.setText(data.getConfigName());
                    gui.initializeViaConfigCode(guiConfigCode, false);
                    if (callBack != null) {
                        DmResult<Object> dmrTemp = new DmResult<>();
                        dmrTemp.setStatus(true);
                        callBack.result(dmrTemp);
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtName).show();
                }
            }
        });
    }

    private PrescriptionValueTemplate_Mdl topControlSaveInitialize() {
        PrescriptionValueTemplate_Mdl mrTemp = new PrescriptionValueTemplate_Mdl();

        if (!topControl.getMode().equalsIgnoreCase("New")) {
            mrTemp.setSharedKeyFieldCode(topControl.getDocId());
        }

        mrTemp.setSharedKeyFieldName(DmShared.xNull(txtName.getText()));
        mrTemp.setConfigCode(DmShared.xNull(txtHGUITemplate.getTag()));
        mrTemp.setConfigName(DmShared.xNull(txtHGUITemplate.getText()));
        mrTemp.setConsultantCode(DmShared.xNull(txtHConsultant.getTag()));
        mrTemp.setConsultantName(DmShared.xNull(txtHConsultant.getText()));
        mrTemp.setGui_Mdl(gui.getGUI_Mdl());

        mrTemp.getLstAllergy().addAll(this.lstAllergy);
        mrTemp.getLstComorbid().addAll(this.lstComorbid);
        mrTemp.getLstComplaint().addAll(this.lstComplaint);
        mrTemp.getLstInstruction().addAll(this.lstInstruction);
        mrTemp.getLstFamilyHistory().addAll(this.lstFamilyHistory);
        mrTemp.getLstHabit().addAll(this.lstHabit);
        mrTemp.getLstHistoryOfPastIllness().addAll(this.lstHistoryOfPastIllness);
        mrTemp.getLstHistoryOfPresentIllness().addAll(this.lstHistoryOfPresentIllness);
        mrTemp.getLstPhysicalExamination().addAll(this.lstPhysicalExamination);
        mrTemp.getLstSystemicExamination().addAll(this.lstSystemicExamination);
        mrTemp.getLstPrecaution().addAll(this.lstPrecaution);
        mrTemp.getLstUrgentCare().addAll(this.lstUrgentCare);
        mrTemp.getLstPersonalHistory().addAll(this.lstPersonalHistory);
        mrTemp.getLstObstetricsHistory().addAll(this.lstObstetricsHistory);
        mrTemp.getLstMenstrualHistory().addAll(this.lstMenstrualHistory);
        mrTemp.getLstDevelopmentHistory().addAll(this.lstDevelopmentHistory);
        mrTemp.getLstPerinatalHistory().addAll(this.lstPerinatalHistory);
        mrTemp.getLstImmunizationHistory().addAll(this.lstImmunizationHistory);
        mrTemp.getLstFeedingHistory().addAll(this.lstFeedingHistory);
        mrTemp.getLstObstetricExamination().addAll(this.lstObstetricExamination);
        mrTemp.getLstGynaecologicalExamination().addAll(this.lstGynaecologicalExamination);
        mrTemp.getLstMenstrualExamination().addAll(this.lstMenstrualExamination);

        return mrTemp;
    }

    private void topControlSave() {
        PrescriptionValueTemplate_Mdl mrTemp;

        try {
            mrTemp = topControlSaveInitialize();
            PrescriptionValueTemplate_Vld.chkSave(mrTemp);
            DmGUI_Vld.chkSave(mrTemp.getGui_Mdl().getGuiRowList());

            SrvAsync.invokeRPC().saveRecord(mrTemp, DmClient.getVoucherPrefix(), new AsyncCallback<DmResult<String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    new DmAlertMessageBox(caught.getMessage(), txtHGUITemplate).show();
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
                    } else {
                        topControlSaveError(result.getErrorNumber(), result.getErrorMessage(), "", "");
                    }
                }
            });
        } catch (DmException e) {
            topControlSaveError(e.getErrorNo(), e.getMessage(), e.getErrorSource(), e.getErrorSourceCode());
        }
    }

    private void topControlSaveError(int errorNo, String errorMessage, String errorSource, String errorSourceCode) {
        if (DmShared.xNull(errorSource).trim().equalsIgnoreCase("GUI")) {
            new DmAlertMessageBox(errorMessage, gui, errorSourceCode).show();
        } else {
            switch (errorNo) {
                case 100:
                    new DmAlertMessageBox(errorMessage, txtHConsultant).show();
                    break;
                case 200:
                    new DmAlertMessageBox(errorMessage, txtHGUITemplate).show();
                    break;
                case 300:
                    new DmAlertMessageBox(errorMessage, txtName).show();
                    break;
                default:
                    new DmAlertMessageBox(errorMessage, txtHGUITemplate).show();
            }
        }
    }

    private void topControlCard() {
        SrvAsync.invokeRPC().getNavigator(new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {
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
        SrvAsync.invokeRPC().getMoveRec(strKey, new AsyncCallback<DmResult<PrescriptionValueTemplate_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PrescriptionValueTemplate_Mdl> result) {
                topControlMoveRecSuccess(result);
            }
        });
    }

    private void topControlMoveRecSuccess(DmResult<PrescriptionValueTemplate_Mdl> result) {
        PrescriptionValueTemplate_Mdl data;
        clear();

        if (result.isStatus()) {
            data = result.getData();
            topControl.setDocId(data.getSharedKeyFieldCode());

            txtHGUITemplate.setTag(DmShared.xNull(data.getConfigCode()));
            txtHGUITemplate.setText(DmShared.xNull(data.getConfigName()));
            txtName.setText(DmShared.xNull(data.getSharedKeyFieldName()));
            txtHConsultant.setTag(DmShared.xNull(data.getConsultantCode()));
            txtHConsultant.setText(DmShared.xNull(data.getConsultantName()));

            this.lstAllergy.addAll(data.getLstAllergy());
            this.lstComorbid.addAll(data.getLstComorbid());
            this.lstComplaint.addAll(data.getLstComplaint());
            this.lstInstruction.addAll(data.getLstInstruction());
            this.lstFamilyHistory.addAll(data.getLstFamilyHistory());
            this.lstHabit.addAll(data.getLstHabit());
            this.lstHistoryOfPastIllness.addAll(data.getLstHistoryOfPastIllness());
            this.lstHistoryOfPresentIllness.addAll(data.getLstHistoryOfPresentIllness());
            this.lstPhysicalExamination.addAll(data.getLstPhysicalExamination());
            this.lstSystemicExamination.addAll(data.getLstSystemicExamination());
            this.lstPrecaution.addAll(data.getLstPrecaution());
            this.lstUrgentCare.addAll(data.getLstUrgentCare());
            this.lstPersonalHistory.addAll(data.getLstPersonalHistory());
            this.lstObstetricsHistory.addAll(data.getLstObstetricsHistory());
            this.lstMenstrualHistory.addAll(data.getLstMenstrualHistory());
            this.lstDevelopmentHistory.addAll(data.getLstDevelopmentHistory());
            this.lstPerinatalHistory.addAll(data.getLstPerinatalHistory());
            this.lstImmunizationHistory.addAll(data.getLstImmunizationHistory());
            this.lstFeedingHistory.addAll(data.getLstFeedingHistory());
            this.lstObstetricExamination.addAll(data.getLstObstetricExamination());
            this.lstGynaecologicalExamination.addAll(data.getLstGynaecologicalExamination());
            this.lstMenstrualExamination.addAll(data.getLstMenstrualExamination());

            userControl.setTextBlog(preparedByBlog, data.getSharedPreparedBy());
            userControl.setTextBlog(modifiedByBlog, data.getSharedModifiedBy());

            gridName.getGrid().getStore().clear();
            gridName.getGrid().getStore().addAll(data.getListTemplateName());
            gui.initialize(data.getGui_Mdl());
            setCustomWidgetValue();
        } else {
            new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
        }

        topControl.setDisplay(true);
    }

    private void topControlDelete(String keyCode) {
        SrvAsync.invokeRPC().deleteRecord(txtHGUITemplate.getTag(), keyCode, new AsyncCallback<DmResult<String>>() {

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

    private void initializeGUI(final String guiCodeFrom, final String copyFromDocId) {
        SrvAsync.invokeRPC(true).getInitializeGUI(gui.getConfigCode(), new AsyncCallback<DmResult<DmGUI_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<DmGUI_Mdl> result) {
                if (result.isStatus()) {
                    if (result.getData() != null) {
                        gui.initialize(result.getData());
                        gui.copyGUI(guiCodeFrom, copyFromDocId, null);
                    }
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void copyPrescriptionCustomData(String copyFromDocId, final List<String> listCustomWidgetToCopy) {
        SrvAsync.invokeRPC(true).getCopyPrescriptionCustomData(copyFromDocId, listCustomWidgetToCopy, new AsyncCallback<DmResult<PrescriptionValueTemplate_Mdl>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), (DmTextBox) null).show();
            }

            @Override
            public void onSuccess(DmResult<PrescriptionValueTemplate_Mdl> result) {
                if (result.isStatus()) {
                    fillCopyPrescription(result.getData(), listCustomWidgetToCopy);
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), (DmTextBox) null).show();
                }
            }
        });
    }

    private void fillCopyPrescription(PrescriptionValueTemplate_Mdl data, List<String> listCustomWidgetToCopy) {
        if (data != null) {
            clearCustomGUI();

            if (listCustomWidgetToCopy != null && listCustomWidgetToCopy.size() > 0) {
                for (String customWidget : listCustomWidgetToCopy) {
                    if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Allergy")) {
                        this.lstAllergy.addAll(data.getLstAllergy());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Complaints")) {
                        this.lstComplaint.addAll(data.getLstComplaint());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Comorbid")) {
                        this.lstComorbid.addAll(data.getLstComorbid());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Family History")) {
                        this.lstFamilyHistory.addAll(data.getLstFamilyHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Habit")) {
                        this.lstHabit.addAll(data.getLstHabit());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("History Of Past Illness")) {
                        this.lstHistoryOfPastIllness.addAll(data.getLstHistoryOfPastIllness());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("History Of Present Illness")) {
                        this.lstHistoryOfPresentIllness.addAll(data.getLstHistoryOfPresentIllness());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Instruction")) {
                        this.lstInstruction.addAll(data.getLstInstruction());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Physical Examination")) {
                        this.lstPhysicalExamination.addAll(data.getLstPhysicalExamination());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Systemic Examination")) {
                        this.lstSystemicExamination.addAll(data.getLstSystemicExamination());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Precaution")) {
                        this.lstPrecaution.addAll(data.getLstPrecaution());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Urgent Care")) {
                        this.lstUrgentCare.addAll(data.getLstUrgentCare());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Personal History")) {
                        this.lstPersonalHistory.addAll(data.getLstPersonalHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Obstetrics History")) {
                        this.lstObstetricsHistory.addAll(data.getLstObstetricsHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Menstrual History")) {
                        this.lstMenstrualHistory.addAll(data.getLstMenstrualHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Development History")) {
                        this.lstDevelopmentHistory.addAll(data.getLstDevelopmentHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Perinatal History")) {
                        this.lstPerinatalHistory.addAll(data.getLstPerinatalHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Immunization History")) {
                        this.lstImmunizationHistory.addAll(data.getLstImmunizationHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Feeding History")) {
                        this.lstFeedingHistory.addAll(data.getLstFeedingHistory());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Obstetric Examination")) {
                        this.lstObstetricExamination.addAll(data.getLstObstetricExamination());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Gynaecological Examination")) {
                        this.lstGynaecologicalExamination.addAll(data.getLstGynaecologicalExamination());
                    } else if (DmShared.xNull(customWidget).trim().equalsIgnoreCase("Menstrual Examination")) {
                        this.lstMenstrualExamination.addAll(data.getLstMenstrualExamination());
                    }
                }
            }

            setCustomWidgetValue();
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                    txtName.setFocus(true);
                }
            });
        }
    }

    private void getTamplateNames() {
        SrvAsync.invokeRPC().getTemplateName(txtHGUITemplate.getTag(), txtHConsultant.getTag(), new AsyncCallback<DmResult<List<KeyFieldExtended>>>() {

            @Override
            public void onFailure(Throwable caught) {
                new DmAlertMessageBox(caught.getMessage(), txtHGUITemplate).show();
            }

            @Override
            public void onSuccess(DmResult<List<KeyFieldExtended>> result) {
                gridName.getGrid().getStore().clear();
                if (result.isStatus()) {
                    gridName.getGrid().getStore().addAll(result.getData());
                } else {
                    new DmAlertMessageBox(result.getErrorMessage(), txtHGUITemplate).show();
                }
            }
        });
    }

    private List<KeyFieldExtended> getCustomFields() {
        List<KeyFieldExtended> listKF = new ArrayList<KeyFieldExtended>();
        KeyFieldExtended kf;

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Allergy");
        kf.setSharedKeyFieldName("Allergy (AllergyCpt, AllergyCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Complaints");
        kf.setSharedKeyFieldName("Complaints (ComplaintsCpt, ComplaintsCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Comorbid");
        kf.setSharedKeyFieldName("Comorbid (ComorbidCpt, ComorbidCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Family History");
        kf.setSharedKeyFieldName("Family History (FamilyHistoryCpt, FamilyHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Habit");
        kf.setSharedKeyFieldName("Habit (HabitCpt, HabitCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("History Of Past Illness");
        kf.setSharedKeyFieldName("History Of Past Illness (HistoryOfPastIllnessCpt, HistoryOfPastIllnessCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("History Of Present Illness");
        kf.setSharedKeyFieldName("History Of Present Illness (HistoryOfPresentIllnessCpt, HistoryOfPresentIllnessCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Instruction");
        kf.setSharedKeyFieldName("Instruction (InstructionCpt, InstructionCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Physical Examination");
        kf.setSharedKeyFieldName("Physical Examination (PhysicalExaminationCpt, PhysicalExaminationCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Precaution");
        kf.setSharedKeyFieldName("Precaution (PrecautionCpt, PrecautionCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Systemic Examination");
        kf.setSharedKeyFieldName("Systemic Examination (SystemicExaminationCpt, SystemicExaminationCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Urgent Care");
        kf.setSharedKeyFieldName("Urgent Care (UrgentCareCpt, UrgentCareCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Personal History");
        kf.setSharedKeyFieldName("Personal History (PersonalHistoryCpt, PersonalHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Obstetrics History");
        kf.setSharedKeyFieldName("Obstetrics History (ObstetricsHistoryCpt, ObstetricsHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Menstrual History");
        kf.setSharedKeyFieldName("Menstrual History (MenstrualHistoryCpt, MenstrualHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Development History");
        kf.setSharedKeyFieldName("Development History (DevelopmentHistoryCpt, DevelopmentHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Perinatal History");
        kf.setSharedKeyFieldName("Perinatal History (PerinatalHistoryCpt, PerinatalHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Immunization History");
        kf.setSharedKeyFieldName("Immunization History (ImmunizationHistoryCpt, ImmunizationHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Feeding History");
        kf.setSharedKeyFieldName("Feeding History (FeedingHistoryCpt, FeedingHistoryCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Obstetric Examination");
        kf.setSharedKeyFieldName("Obstetric Examination (ObstetricExaminationCpt, ObstetricExaminationCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Gynaecological Examination");
        kf.setSharedKeyFieldName("Gynaecological Examination (GynaecologicalExaminationCpt, GynaecologicalExaminationCtrl)");
        listKF.add(kf);

        kf = new KeyFieldExtended();
        kf.setSharedKeyFieldCode("Menstrual Examination");
        kf.setSharedKeyFieldName("Menstrual Examination (MenstrualExaminationCpt, MenstrualExaminationCtrl)");
        listKF.add(kf);

        return listKF;
    }

    private void showCustomWidgets(DmTextArea txtBox) {
        if (txtBox == txtAllergy) {
            allergy.show(DmShared.iNull(topControl.getDocId()).toString(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), allergyEditable, lstAllergy, null);
        } else if (txtBox == txtComorbid) {
            comorbid.show(STEnum.CustomWidgetNature.COMORBID.getId(), lblComorbid.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstComorbid);
        } else if (txtBox == txtComplaint) {
            complaint.show(STEnum.CustomWidgetNature.COMPLAINTS.getId(), lblComplaint.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstComplaint);
        } else if (txtBox == txtInstruction) {
            instruction.show(STEnum.CustomWidgetNature.INSTRUCTION.getId(), lblInstruction.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstInstruction);
        } else if (txtBox == txtFamilyHistory) {
            familyHistory.show(STEnum.CustomWidgetNature.FAMILY_HISTORY.getId(), lblFamilyHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstFamilyHistory);
        } else if (txtBox == txtHabit) {
            habit.show(STEnum.CustomWidgetNature.HABIT.getId(), lblHabit.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstHabit);
        } else if (txtBox == txtHistoryOfPastIllness) {
            historyOfPastIllness.show(STEnum.CustomWidgetNature.HISTORY_OF_PAST_ILLNESS.getId(), lblHistoryOfPastIllness.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstHistoryOfPastIllness);
        } else if (txtBox == txtHistoryOfPresentIllness) {
            historyOfPresentIllness.show(STEnum.CustomWidgetNature.HISTORY_OF_PRESENT_ILLNESS.getId(), lblHistoryOfPresentIllness.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstHistoryOfPresentIllness);
        } else if (txtBox == txtPhysicalExamination) {
            physicalExamination.show(STEnum.CustomWidgetNature.PHYSICAL_EXAMINATION.getId(), lblPhysicalExamination.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstPhysicalExamination);
        } else if (txtBox == txtSystemicExamination) {
            systemicExamination.show(STEnum.CustomWidgetNature.SYSTEMIC_EXAMINATION.getId(), lblSystemicExamination.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstSystemicExamination);
        } else if (txtBox == txtUrgentCare) {
            urgentCare.show(STEnum.CustomWidgetNature.URGENT_CARE.getId(), lblUrgentCare.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstUrgentCare);
        } else if (txtBox == txtPrecaution) {
            precaution.show(STEnum.CustomWidgetNature.PRECAUTION.getId(), lblPrecaution.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstPrecaution);
        } else if (txtBox == txtPersonalHistory) {
            personalHistory.show(STEnum.CustomWidgetNature.PERSONAL_HISTORY.getId(), lblPersonalHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstPersonalHistory);
        } else if (txtBox == txtObstetricsHistory) {
            obstetricsHistory.show(STEnum.CustomWidgetNature.OBSTETRICS_HISTORY.getId(), lblObstetricsHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstObstetricsHistory);
        } else if (txtBox == txtMenstrualHistory) {
            menstrualHistory.show(STEnum.CustomWidgetNature.MENSTRUAL_HISTORY.getId(), lblMenstrualHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstMenstrualHistory);
        } else if (txtBox == txtDevelopmentHistory) {
            developmentHistory.show(STEnum.CustomWidgetNature.DEVELOPMENT_HISTORY.getId(), lblDevelopmentHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstDevelopmentHistory);
        } else if (txtBox == txtPerinatalHistory) {
            perinatalHistory.show(STEnum.CustomWidgetNature.PERINATAL_HISTORY.getId(), lblPerinatalHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstPerinatalHistory);
        } else if (txtBox == txtImmunizationHistory) {
            immunizationHistory.show(STEnum.CustomWidgetNature.IMMUNIZATION_HISTORY.getId(), lblImmunizationHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstImmunizationHistory);
        } else if (txtBox == txtFeedingHistory) {
            feedingHistory.show(STEnum.CustomWidgetNature.FEEDING_HISTORY.getId(), lblFeedingHistory.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstFeedingHistory);
        } else if (txtBox == txtObstetricExamination) {
            obstetricExamination.show(STEnum.CustomWidgetNature.OBSTETRIC_EXAMINATION.getId(), lblObstetricExamination.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstObstetricExamination);
        } else if (txtBox == txtGynaecologicalExamination) {
            gynaecologicalExamination.show(STEnum.CustomWidgetNature.GYNAECOLOGICAL_EXAMINATION.getId(), lblGynaecologicalExamination.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstGynaecologicalExamination);
        } else if (txtBox == txtMenstrualExamination) {
            menstrualExamination.show(STEnum.CustomWidgetNature.MENSTRUAL_EXAMINATION.getId(), lblMenstrualExamination.getTitle(), DmShared.iNull(txtHConsultant.getTag()).toString(), !topControl.getMode().equalsIgnoreCase("Browse"), lstMenstrualExamination);
        }
    }

    private void setCustomWidgetValue() {
        txtAllergy.setText(Allergy_Shared.getFormattedString(lstAllergy));
        txtComorbid.setText(PrescriptionCustomItem_Shared.getFormattedString(lstComorbid));
        txtComplaint.setText(PrescriptionCustomItem_Shared.getFormattedString(lstComplaint));
        txtInstruction.setText(PrescriptionCustomItem_Shared.getFormattedString(lstInstruction));
        txtFamilyHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstFamilyHistory));
        txtHabit.setText(PrescriptionCustomItem_Shared.getFormattedString(lstHabit));
        txtHistoryOfPastIllness.setText(PrescriptionCustomItem_Shared.getFormattedString(lstHistoryOfPastIllness));
        txtHistoryOfPresentIllness.setText(PrescriptionCustomItem_Shared.getFormattedString(lstHistoryOfPresentIllness));
        txtPhysicalExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPhysicalExamination));
        txtPrecaution.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPrecaution));
        txtSystemicExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstSystemicExamination));
        txtUrgentCare.setText(PrescriptionCustomItem_Shared.getFormattedString(lstUrgentCare));
        txtPersonalHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPersonalHistory));
        txtObstetricsHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstObstetricsHistory));
        txtMenstrualHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstMenstrualHistory));
        txtDevelopmentHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstDevelopmentHistory));
        txtPerinatalHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstPerinatalHistory));
        txtImmunizationHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstImmunizationHistory));
        txtFeedingHistory.setText(PrescriptionCustomItem_Shared.getFormattedString(lstFeedingHistory));
        txtObstetricExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstObstetricExamination));
        txtGynaecologicalExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstGynaecologicalExamination));
        txtMenstrualExamination.setText(PrescriptionCustomItem_Shared.getFormattedString(lstMenstrualExamination));
    }

    private void manageCustomWidgetDisplay() {
        /*====================================< Manage Custom Field Enable >====================================*/
        boolean isEnable = false;
        if ((topControl.getMode().trim().equalsIgnoreCase("New")) || (topControl.getMode().trim().equalsIgnoreCase("Edit"))) {
            isEnable = true;
        }

        txtAllergy.setEnabled(isEnable);
        txtComplaint.setEnabled(isEnable);
        txtComorbid.setEnabled(isEnable);
        txtInstruction.setEnabled(isEnable);
        txtFamilyHistory.setEnabled(isEnable);
        txtHabit.setEnabled(isEnable);
        txtHistoryOfPastIllness.setEnabled(isEnable);
        txtHistoryOfPresentIllness.setEnabled(isEnable);
        txtPhysicalExamination.setEnabled(isEnable);
        txtSystemicExamination.setEnabled(isEnable);
        txtPrecaution.setEnabled(isEnable);
        txtUrgentCare.setEnabled(isEnable);
    }

    private void clear() {
        clearGUI();
        txtHGUITemplate.setText("");
        txtHGUITemplate.setTag("");
        userControl.setTextBlog(preparedByBlog, "");
        userControl.setTextBlog(modifiedByBlog, "");
        gridName.getGrid().getStore().clear();
    }

    private void clearGUI() {
        gui.clear();
        clearCustomGUI();
    }

    private void clearCustomGUI() {
        txtAllergy.setText("");
        txtComplaint.setText("");
        txtComorbid.setText("");
        txtInstruction.setText("");
        txtFamilyHistory.setText("");
        txtHabit.setText("");
        txtHistoryOfPastIllness.setText("");
        txtHistoryOfPresentIllness.setText("");
        txtPhysicalExamination.setText("");
        txtPrecaution.setText("");
        txtSystemicExamination.setText("");
        txtUrgentCare.setText("");
        txtPersonalHistory.setText("");
        txtObstetricsHistory.setText("");
        txtMenstrualHistory.setText("");
        txtDevelopmentHistory.setText("");
        txtPerinatalHistory.setText("");
        txtImmunizationHistory.setText("");
        txtFeedingHistory.setText("");
        txtObstetricExamination.setText("");
        txtGynaecologicalExamination.setText("");
        txtMenstrualExamination.setText("");

        lstAllergy = new ArrayList<GrdAllergy_Mdl>();
        lstComplaint = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstComorbid = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstInstruction = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstFamilyHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstHabit = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstHistoryOfPastIllness = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstHistoryOfPresentIllness = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstPhysicalExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstPrecaution = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstSystemicExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstUrgentCare = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstPersonalHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstObstetricsHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstMenstrualHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstDevelopmentHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstPerinatalHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstImmunizationHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstFeedingHistory = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstObstetricExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstGynaecologicalExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
        lstMenstrualExamination = new ArrayList<GrdPrescriptionCustomItem_Mdl>();
    }
}
