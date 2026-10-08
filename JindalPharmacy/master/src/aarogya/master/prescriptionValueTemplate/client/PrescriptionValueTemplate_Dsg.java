package aarogya.master.prescriptionValueTemplate.client;

import aarogya.master.prescriptionValueTemplate.shared.Find_Mdl;
import aarogya.master.prescriptionValueTemplate.shared.TxtHCopyGUI_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGUI.client.DmGUI;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PrescriptionValueTemplate_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().PRESCRIPTION_VALUE_TEMPLATE.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    public final static String addMedicineEditable = "Medicine Editable";
    public final static String addFrequencyEditable = "Frequency Editable";
    public final static String addAllergyEditable = "Allergy Editable";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmGUI gui = new DmGUI(topControl, 490);

    final DmSuggestBox txtName = new DmSuggestBox(35, 10, true, "NAME", DmDataType.STRING);

    final DmTextArea txtAllergy = new DmTextArea();
    final DmTextArea txtComplaint = new DmTextArea();
    final DmTextArea txtComorbid = new DmTextArea();
    final DmTextArea txtInstruction = new DmTextArea();
    final DmTextArea txtFamilyHistory = new DmTextArea();
    final DmTextArea txtHabit = new DmTextArea();
    final DmTextArea txtHistoryOfPastIllness = new DmTextArea();
    final DmTextArea txtHistoryOfPresentIllness = new DmTextArea();
    final DmTextArea txtPhysicalExamination = new DmTextArea();
    final DmTextArea txtSystemicExamination = new DmTextArea();
    final DmTextArea txtPrecaution = new DmTextArea();
    final DmTextArea txtUrgentCare = new DmTextArea();
    final DmTextArea txtPersonalHistory = new DmTextArea();
    final DmTextArea txtObstetricsHistory = new DmTextArea();
    final DmTextArea txtMenstrualHistory = new DmTextArea();
    final DmTextArea txtDevelopmentHistory = new DmTextArea();
    final DmTextArea txtPerinatalHistory = new DmTextArea();
    final DmTextArea txtImmunizationHistory = new DmTextArea();
    final DmTextArea txtFeedingHistory = new DmTextArea();
    final DmTextArea txtObstetricExamination = new DmTextArea();
    final DmTextArea txtGynaecologicalExamination = new DmTextArea();
    final DmTextArea txtMenstrualExamination = new DmTextArea();

    final DmLabel lblAllergy = new DmLabel("Allergy");
    final DmLabel lblComplaint = new DmLabel("Complaints");
    final DmLabel lblComorbid = new DmLabel("Comorbid");
    final DmLabel lblInstruction = new DmLabel("Instruction");
    final DmLabel lblFamilyHistory = new DmLabel("Family History");
    final DmLabel lblHabit = new DmLabel("Habit");
    final DmLabel lblHistoryOfPastIllness = new DmLabel("History Of Past Illness");
    final DmLabel lblHistoryOfPresentIllness = new DmLabel("History Of Present Illness");
    final DmLabel lblPhysicalExamination = new DmLabel("Physical Examination");
    final DmLabel lblSystemicExamination = new DmLabel("Systemic Examination");
    final DmLabel lblPrecaution = new DmLabel("Precaution");
    final DmLabel lblUrgentCare = new DmLabel("Urgent Care");
    final DmLabel lblPersonalHistory = new DmLabel("Personal History");
    final DmLabel lblObstetricsHistory = new DmLabel("Obstetrics History");
    final DmLabel lblMenstrualHistory = new DmLabel("Menstrual History");
    final DmLabel lblDevelopmentHistory = new DmLabel("Development History");
    final DmLabel lblPerinatalHistory = new DmLabel("Perinatal History");
    final DmLabel lblImmunizationHistory = new DmLabel("Immunization History");
    final DmLabel lblFeedingHistory = new DmLabel("Feeding History");
    final DmLabel lblObstetricExamination = new DmLabel("Obstetric Examination");
    final DmLabel lblGynaecologicalExamination = new DmLabel("Gynaecological Examination");
    final DmLabel lblMenstrualExamination = new DmLabel("Menstrual Examination");

    final Menu menu = new Menu();
    MenuItem mnuApplyTemplate, mnuCustomFields;
    SelectionHandler<MenuItem> selectionHandler;

    abstract void menuClick(SelectionEvent<MenuItem> event);

    //<editor-fold defaultstate="collapsed" desc="txtHGUITemplate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGUITemplate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHGUITemplate() {
        txtHGUITemplate.getDmGrid().addTextColumn(txtHGUITemplate.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHGUITemplate.getDmGrid().initializeGrid(txtHGUITemplate.getDmGrid().getPropertyAccess().code(), 210, 0, 1);
        txtHGUITemplate.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHConsultant">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHConsultant() {
        txtHConsultant.getDmGrid().addTextColumn(txtHConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHConsultant.getDmGrid().initializeGrid(txtHConsultant.getDmGrid().getPropertyAccess().code(), 370, 0, 1);
        txtHConsultant.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCopyGUI">
    interface TxtHCopyGUI_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHCopyGUI_Mdl> {

        @Editor.Path("ConsultantName")
        ValueProvider<TxtHCopyGUI_Mdl, String> consultantName();

        @Editor.Path("GuiConfigName")
        ValueProvider<TxtHCopyGUI_Mdl, String> guiConfigName();
    }
    final DmHelpGrid<TxtHCopyGUI_Mdl, TxtHCopyGUI_PrpAcs> txtHCopyGUI = new DmHelpGrid(GWT.create(TxtHCopyGUI_PrpAcs.class), 7);

    private void iniTxtHCopyGUI() {
        txtHCopyGUI.getDmGrid().addTextColumn(txtHCopyGUI.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHCopyGUI.getDmGrid().addTextColumn(txtHCopyGUI.getDmGrid().getPropertyAccess().consultantName(), "Consultant", 175, "consultantName");
        txtHCopyGUI.getDmGrid().addTextColumn(txtHCopyGUI.getDmGrid().getPropertyAccess().guiConfigName(), "GUI", 150, "guiConfigName");
        txtHCopyGUI.getDmGrid().initializeGrid(txtHCopyGUI.getDmGrid().getPropertyAccess().fieldHashCode(), 500, 0, 1);
        txtHCopyGUI.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("GuiName")
        ValueProvider<Find_Mdl, String> guiName();

        @Editor.Path("ConsultantName")
        ValueProvider<Find_Mdl, String> consultantName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().guiName(), "GUI Name", 400, "guiConfigName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().consultantName(), "Consultant Name", 400, "consultantName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public PrescriptionValueTemplate_Dsg() {
        ini();
        iniTxtHGUITemplate();
        iniTxtHConsultant();
        iniTxtHCopyGUI();

        txtAllergy.setReadOnly(true);
        txtComplaint.setReadOnly(true);
        txtComorbid.setReadOnly(true);
        txtInstruction.setReadOnly(true);
        txtFamilyHistory.setReadOnly(true);
        txtHabit.setReadOnly(true);
        txtHistoryOfPastIllness.setReadOnly(true);
        txtHistoryOfPresentIllness.setReadOnly(true);
        txtPhysicalExamination.setReadOnly(true);
        txtSystemicExamination.setReadOnly(true);
        txtPrecaution.setReadOnly(true);
        txtUrgentCare.setReadOnly(true);
        txtPersonalHistory.setReadOnly(true);
        txtObstetricsHistory.setReadOnly(true);
        txtMenstrualHistory.setReadOnly(true);
        txtDevelopmentHistory.setReadOnly(true);
        txtPerinatalHistory.setReadOnly(true);
        txtImmunizationHistory.setReadOnly(true);
        txtFeedingHistory.setReadOnly(true);
        txtObstetricExamination.setReadOnly(true);
        txtGynaecologicalExamination.setReadOnly(true);
        txtMenstrualExamination.setReadOnly(true);
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        iniMenu();
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTD;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "520px", "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "82%", "520px", "left", null, DmPosition.NONE, false);
        flwTD.add(leftArea());
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "18%", "520px", "left", null, DmPosition.NONE, false);
        flwTD.add(rightArea());
        flwTr.add(flwTD);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.getElement().setAttribute("style", flwTr.getElement().getAttribute("style") + "border-bottom : 1px solid lightgray;");
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        DmClient.setStyle(flwTr, "100%", "490px", "left", null, DmPosition.NONE, true);
        gui.setTabIndexAfter(txtName);
        gui.setBannerVisible(false);
        flwTr.add(gui);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsultant));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GUI");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGUITemplate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightArea() {
        FlowPanel flwTable, flwTR;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "520px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTR.add(createNameGrid());
        flwTable.add(flwTR);

        return flwTable;
    }

    private Menu iniMenu() {
        selectionHandler = new SelectionHandler<MenuItem>() {

            @Override
            public void onSelection(SelectionEvent<MenuItem> event) {
                menuClick(event);
            }
        };

        mnuApplyTemplate = new MenuItem("Apply Template", selectionHandler);
        menu.add(mnuApplyTemplate);

        mnuCustomFields = new MenuItem("Custom Fields", selectionHandler);
        menu.add(mnuCustomFields);

        return menu;
    }

    //<editor-fold defaultstate="collapsed" desc="Grid Name">
    interface Grd_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmGrid<KeyFieldExtended, Grd_PrpAcs> gridName = new DmGrid(com.google.gwt.core.shared.GWT.create(Grd_PrpAcs.class));

    public Grid createNameGrid() {
        gridName.addRowNumberColumn();
        gridName.addTextColumn(gridName.getPropertyAccess().name(), "Name", 205, "");
        gridName.initializeGrid(gridName.getPropertyAccess().code(), 227, 520, 2);

        return gridName.getGrid();
    }
    //</editor-fold>
}
