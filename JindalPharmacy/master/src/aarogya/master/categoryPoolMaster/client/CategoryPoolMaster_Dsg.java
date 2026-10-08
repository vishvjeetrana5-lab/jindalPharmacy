package aarogya.master.categoryPoolMaster.client;

import aarogya.master.categoryPoolMaster.shared.Find_Mdl;
import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class CategoryPoolMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().CATEGORY_POOL_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBoxNumeric txtHeaderConsAmount = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtHeaderRefAmount = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtLineConsAmount = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtLineRefAmount = new DmTextBoxNumeric(2, false, 10, false);

    //<editor-fold defaultstate="collapsed" desc="txtHCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, false);

    private void iniTxtHCategory() {
        txtHCategory.getDmGrid().addTextColumn(txtHCategory.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHCategory.getDmGrid().initializeGrid(txtHCategory.getDmGrid().getPropertyAccess().code(), 380, 0, 1);
        txtHCategory.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceCat">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCat = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHServiceCat() {
        txtHServiceCat.getDmGrid().addTextColumn(txtHServiceCat.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHServiceCat.getDmGrid().initializeGrid(txtHServiceCat.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHServiceCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ServiceText")
        ValueProvider<Find_Mdl, String> serviceText();

        @Editor.Path("CategoryText")
        ValueProvider<Find_Mdl, String> categoryText();

        @Editor.Path("ModuleName")
        ValueProvider<Find_Mdl, String> moduleName();

        @Editor.Path("ServiceName")
        ValueProvider<Find_Mdl, String> serviceName();

        @Editor.Path("HeaderConTypeName")
        ValueProvider<Find_Mdl, String> headerConTypeName();

        @Editor.Path("HeaderRefTypeName")
        ValueProvider<Find_Mdl, String> headerRefTypeName();

        @Editor.Path("LineConTypeName")
        ValueProvider<Find_Mdl, String> lineConTypeName();

        @Editor.Path("LineRefTypeName")
        ValueProvider<Find_Mdl, String> lineRefTypeName();

        @Editor.Path("GroupName")
        ValueProvider<Find_Mdl, String> groupName();

        @Editor.Path("OrgName")
        ValueProvider<Find_Mdl, String> orgName();

        @Editor.Path("LineReferredAmount")
        ValueProvider<Find_Mdl, String> lineReferredAmount();

        @Editor.Path("LineConsultantAmount")
        ValueProvider<Find_Mdl, String> lineConsultantAmount();

        @Editor.Path("HeaderReferredAmount")
        ValueProvider<Find_Mdl, String> headerReferredAmount();

        @Editor.Path("HeaderConsultantAmount")
        ValueProvider<Find_Mdl, String> headerConsultantAmount();

    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().categoryText(), "Room Category Name", 160, "roomCategoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().orgName(), "Empanelment", 150, "organizationName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().moduleName(), "Module Name", 100, "moduleName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serviceText(), "Service Name", 200, "serviceCategoryName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().groupName(), "Service Group", 100, "groupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().serviceName(), "Service", 200, "serviceName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().headerConTypeName(), "Header Con Type Name", 50, "headerConsultantIsAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().headerConsultantAmount(), "Header Con Amount", 50, "headerConsultantAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().headerRefTypeName(), "Header Ref Type Name", 50, "headerReferredIsAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().headerReferredAmount(), "Header Ref Amount", 50, "headerReferredAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().lineConTypeName(), "Line Con Type Name", 50, "lineConsultantIsAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().lineConsultantAmount(), "Line Con Amount", 50, "lineConsultantAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().lineRefTypeName(), "Line Ref Type Name", 50, "lineReferredIsAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().lineReferredAmount(), "Line Ref Amount", 50, "lineReferredAmount");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
//        dmFind.getHelper().addSort("organizationName", DmSortType.ASC);
//        dmFind.getHelper().addSort("moduleName", DmSortType.ASC);
//        dmFind.getHelper().addSort("serviceCategoryName", DmSortType.ASC);
//        dmFind.getHelper().addSort("groupName", DmSortType.ASC);
//        dmFind.getHelper().addSort("serviceName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEmpanelment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmpanelment = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHEmpanelment() {
        txtHEmpanelment.getDmGrid().addTextColumn(txtHEmpanelment.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHEmpanelment.getDmGrid().initializeGrid(txtHEmpanelment.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHEmpanelment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceGroup = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHServiceGroup() {
        txtHServiceGroup.getDmGrid().addTextColumn(txtHServiceGroup.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHServiceGroup.getDmGrid().initializeGrid(txtHServiceGroup.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHServiceGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHService">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHService = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHService() {
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHService.getDmGrid().initializeGrid(txtHService.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHService.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHeaderConsType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHeaderConsType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHHeaderConsType() {
        txtHHeaderConsType.getDmGrid().addTextColumn(txtHHeaderConsType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHHeaderConsType.getDmGrid().initializeGrid(txtHHeaderConsType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHHeaderConsType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHeaderRefType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHeaderRefType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHHeaderRefType() {
        txtHHeaderRefType.getDmGrid().addTextColumn(txtHHeaderRefType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHHeaderRefType.getDmGrid().initializeGrid(txtHHeaderRefType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHHeaderRefType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLineConsType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLineConsType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHLineConsType() {
        txtHLineConsType.getDmGrid().addTextColumn(txtHLineConsType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHLineConsType.getDmGrid().initializeGrid(txtHLineConsType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHLineConsType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLineRefType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLineRefType = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true, false);

    private void iniTxtHLineRefType() {
        txtHLineRefType.getDmGrid().addTextColumn(txtHLineRefType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHLineRefType.getDmGrid().initializeGrid(txtHLineRefType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHLineRefType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHModule">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHModule.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public CategoryPoolMaster_Dsg() {
        ini();

        iniTxtHEmpanelment();
        iniTxtHModule();
        iniTxtHServiceCat();
        iniTxtHServiceGroup();
        iniTxtHService();
        iniTxtHHeaderConsType();
        iniTxtHHeaderRefType();
        iniTxtHLineConsType();
        iniTxtHLineRefType();
        iniTxtHCategory();

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
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "476px", null, null, DmPosition.CENTER, true);
        flwTable.getElement().getStyle().setMarginLeft(32, Style.Unit.PCT);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Room Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCategory));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Empanelment");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmpanelment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Module Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHModule));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceCat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "55%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHService));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Header Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHeaderConsType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHeaderConsAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Header Referred Cons.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHeaderRefType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHeaderRefAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Performing Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLineConsType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLineConsAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Performing Referred Cons.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLineRefType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLineRefAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        return flwTable;
    }
}
