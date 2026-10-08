package aarogya.inventory.projectMaster.client;

import aarogya.projectMenu.self.shared.pmInventory;
import aarogya.inventory.projectMaster.shared.Find_Mdl;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ProjectMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmInventory().PROJECT_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmSuggestBox txtProject = new DmSuggestBox(200, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtManualCode = new DmTextBox(20, true);
    final DmRemarkTextBox txtSpecification = new DmRemarkTextBox(500, false);
    final DmTextBox txtStartDate = new DmTextBox(11, false);
    final DmTextBox txtEndDate = new DmTextBox(11, false);
    final DmTextBoxNumeric txtApprovedAmt=new DmTextBoxNumeric(2, false, 13, false);

    SelectionHandler<MenuItem> selectionHandler;
    final Menu menu = new Menu();
    final MenuItem mnuExportCard = new MenuItem("Export Data");
    final MenuItem mnuManageSite = new MenuItem("Manage Site");

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ProjectHeadName")
        ValueProvider<Find_Mdl, String> projectHeadName();

        @Editor.Path("ProjectAcName")
        ValueProvider<Find_Mdl, String> projectAcName();
        
        @Editor.Path("ApprovedAmount")
        ValueProvider<Find_Mdl, String> approvedAmount();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 16);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Project", 350, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().projectHeadName(), "Project Head", 350, "projectHeadName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().projectAcName(), "Project A/c", 350, "projectAcName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().approvedAmount(), "Approved Amt.", 350, "approvedAmount");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);

    }

//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHProjectHead">
    interface KeyField_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {
        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHProjectHead = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, false);
    private void iniTxtHProjectHead() {
        txtHProjectHead.getDmGrid().addTextColumn(txtHProjectHead.getDmGrid().getPropertyAccess().name(), "Project Head", 100, "Name");
        txtHProjectHead.getDmGrid().addTextColumn(txtHProjectHead.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code",100,"manualCode");
        txtHProjectHead.getDmGrid().initializeGrid(txtHProjectHead.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHProjectHead.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="txtHProjectAc">
    interface KeyField_PrpAcs5 extends KeyField_T_PrpAcs<KeyFieldExtended> {
        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> sharedPreparedBy();
    }
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs5> txtHProjectAc = new DmTextHelp(GWT.create(KeyField_PrpAcs5.class), 6, false);
    private void iniTxtHProjectAc() {
        txtHProjectAc.getDmGrid().addTextColumn(txtHProjectAc.getDmGrid().getPropertyAccess().name(), "Project A/c", 100, "Name");
        txtHProjectAc.getDmGrid().addTextColumn(txtHProjectAc.getDmGrid().getPropertyAccess().sharedPreparedBy(), "Manual Code",100,"manualCode");
        txtHProjectAc.getDmGrid().initializeGrid(txtHProjectAc.getDmGrid().getPropertyAccess().code(), 275, 0, 1);
        txtHProjectAc.getHelper().addSort("Name", DmSortType.ASC);
    }

    //</editor-fold>
    
//<editor-fold defaultstate="collapsed" desc="TxtHActive">
    final DmTextHelp<KeyFieldExtended, dataman.dmBase.self.shared.KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(dataman.dmBase.self.shared.KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>
    
    public ProjectMaster_Dsg() {
        ini();
        iniMenu();
        iniTxtHProjectHead();
        iniTxtHProjectAc();
        iniTxtHActive();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "280px", null, null, DmPosition.CENTER, true);

    
    flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "68px", "left", null, DmPosition.NONE, true);

////======================= 1st Row (Project) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Project Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtProject));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //======================= 3rd Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END =========================================

        //======================= 2nd Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Project Head");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHProjectHead));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        //======================= 3rd Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Project A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHProjectAc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        //======================= 4th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Start Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtStartDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("End Date");
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEndDate));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14.6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Approved Amt.");
        flwTd.add(lbl);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtApprovedAmt));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //======================= 5th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Specification");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSpecification));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //======================= 6th Row (ITEM GROUP) ==========================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHActive));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
       return flwTable;
    }

    
    private Menu iniMenu() {
        menu.add(mnuManageSite);
        menu.add(mnuExportCard);
        return menu;
    }

}
