package aarogya.master.packageDetail.client;

import aarogya.master.packageDetail.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class PackageDetail_Dsg {

    public final static String widgetName = "Package Detail";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();
    final FlowPanel flwSave = new FlowPanel();
    final Button btnSave = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final FlowPanel flwRoomCategory = new FlowPanel();
    final Button btnRoomCategory = new Button();
    final FlowPanel flwCopyPackage = new FlowPanel();
    final Button btnCopyPackage = new Button();

    final FlowPanel flwRoomCategoryName = new FlowPanel();
    final DmLabel lblRoomCategoryName = new DmLabel("");
    final FlowPanel flwPackageCode = new FlowPanel();
    final DmLabel lblPackageCode = new DmLabel("");

    final DmTextBox txtPackageName = new DmTextBox();
    final DmTextBox txtPackageShortName = new DmTextBox();
    final DmTextBox txtPackageNature = new DmTextBox();
    final DmTextBox txtOrganizationName = new DmTextBox();
    final DmTextBox txtOrgCode = new DmTextBox(10, false);
    final DmTextBox txtFromDate = new DmTextBox(11, true);
    final DmTextBox txtToDate = new DmTextBox(11, true);
    final DmTextBoxNumeric txtPackageAmt = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtRateListAmt = new DmTextBoxNumeric(2, false, 10, false);
    final DmLabel lblActualValue = new DmLabel("0.00");
    final DmLabel lblPackageValue = new DmLabel("0.00");
    final DmLabel lblDifference = new DmLabel("0.00");
    final DmLabel lblMedicineItemAmt = new DmLabel("0.00");
    final DmLabel lblInvestigationAmt = new DmLabel("0.00");

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHActive.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSchedule">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSchedule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHSchedule() {
        txtHSchedule.getDmGrid().addTextColumn(txtHSchedule.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHSchedule.getDmGrid().initializeGrid(txtHSchedule.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHSchedule.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPackage">
    interface TxtHPackage_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHPackage_Mdl> {

        @Editor.Path("PackageName")
        ValueProvider<TxtHPackage_Mdl, String> packageName();

        @Editor.Path("PackageShortName")
        ValueProvider<TxtHPackage_Mdl, String> packageShortName();

        @Editor.Path("OrganizationManualCode")
        ValueProvider<TxtHPackage_Mdl, String> organizationManualCode();

        @Editor.Path("OrganizationName")
        ValueProvider<TxtHPackage_Mdl, String> organizationName();

        @Editor.Path("RoomCategoryName")
        ValueProvider<TxtHPackage_Mdl, String> roomCategoryName();

        @Editor.Path("FromDate")
        ValueProvider<TxtHPackage_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<TxtHPackage_Mdl, String> toDate();

        @Editor.Path("PackageNature")
        ValueProvider<TxtHPackage_Mdl, String> packageNature();

        @Editor.Path("PackageAmount")
        ValueProvider<TxtHPackage_Mdl, String> packageAmount();
    }
    final DmHelpGrid<TxtHPackage_Mdl, TxtHPackage_PrpAcs> txtHPackage = new DmHelpGrid(GWT.create(TxtHPackage_PrpAcs.class), 10);

    private void iniTxtHPackage() {
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageName(), "Name", 1, "packageName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageShortName(), "Short Name", 80, "shortName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageNature(), "Nature", 60, "nature");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().organizationManualCode(), "Org. Code", 80, "organizationManualCode");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().fromDate(), "From Date", 80, "fromDate", DmDataType.DATE);
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().toDate(), "To Date", 80, "toDate", DmDataType.DATE);
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().organizationName(), "Organization", 120, "organizationName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().roomCategoryName(), "Room Category", 120, "roomCategoryName");
        txtHPackage.getDmGrid().addTextColumn(txtHPackage.getDmGrid().getPropertyAccess().packageAmount(), "Package Amt.", 90, "packageAmount", DmDataType.DOUBLE).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        txtHPackage.getDmGrid().initializeGrid(txtHPackage.getDmGrid().getPropertyAccess().fieldHashCode(), 900, 0, 1);

        txtHPackage.getHelper().addSort("packageName", DmSortType.ASC);
    }
    //</editor-fold>

    public PackageDetail_Dsg() {
        ini();
        iniTxtHSchedule();
        iniTxtHActive();
        iniTxtHPackage();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("950px");
        popupPanel.setHeight("470px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Save");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.setTitle("Save: (Ctrl + S)");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("60px");
        flwSave.add(btnSave);

        label = new InlineLabel("Delete");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.setTitle("Delete: (Ctrl + D)");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("65px");
        flwDelete.add(btnDelete);

        label = new InlineLabel("Copy Package");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-files-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopyPackage.setTitle("Copy Package");
        btnCopyPackage.addStyleName("dm-top-control-btnStyle");
        btnCopyPackage.getElement().appendChild(aBtn.getElement());
        btnCopyPackage.getElement().appendChild(label.getElement());
        btnCopyPackage.setWidth("105px");
        flwCopyPackage.add(btnCopyPackage);

        label = new InlineLabel("Room Category List");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-list");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRoomCategory.setTitle("Room Category List");
        btnRoomCategory.addStyleName("dm-top-control-btnStyle");
        btnRoomCategory.getElement().appendChild(aBtn.getElement());
        btnRoomCategory.getElement().appendChild(label.getElement());
        btnRoomCategory.setWidth("125px");
        flwRoomCategory.add(btnRoomCategory);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close: (Escap)");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        lblPackageCode.setHeight("18px");
        lblPackageCode.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblPackageCode.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        lblPackageCode.getElement().setAttribute("style", lblPackageCode.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:red; padding-right:5px;");
        flwPackageCode.add(lblPackageCode);

        lblRoomCategoryName.setHeight("18px");
        lblRoomCategoryName.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblRoomCategoryName.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lblRoomCategoryName.getElement().setAttribute("style", lblRoomCategoryName.getElement().getAttribute("style")
                + ";font-weight: bold;"
                + "font-size: 13px;color:black; padding-right:5px;");
        flwRoomCategoryName.add(lblRoomCategoryName);

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(vl.add(flwSave));
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(flwCopyPackage);
        toolContainer.addWidget(flwRoomCategory);
        toolContainer.addWidget(flwCancel);
        toolContainer.addWidget(flwPackageCode);
        toolContainer.addWidget(flwRoomCategoryName);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "440px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Package Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPackageName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPackageShortName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPackageNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPackageAmt));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Organization");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrganizationName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Org. Code");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrgCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Schedule");
        lbl.getElement().getStyle().setMarginLeft(7, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSchedule));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHActive));
        flwTr.add(flwTd);

        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate List Amt.");
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRateListAmt));
        flwTr.add(flwTd);

        
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "375px", "left", null, DmPosition.NONE, false);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "375px", null, null, DmPosition.NONE, true);

        tabPanel.add(vl.add(packageArea()), "Package");
        tabPanel.add(vl.add(medicineItemDiagnosticArea()), "Medicine, Item & Diagnostic Info.");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel packageArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "345px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "320px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGrdService()));
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, false);
        flwTr.add(pkgBottomArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel medicineItemDiagnosticArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "345px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "320px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "320px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdItem()));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "50%", "320px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(iniGrdInvestigation()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, false);
        flwTr.add(medBottomArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel pkgBottomArea() {
        DmLabel lbl;
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", "25px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Actual value :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "25px", "left", null, DmPosition.NONE, true);
        lblActualValue.setStyleName("dm-HLabel");
        lblActualValue.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lblActualValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblActualValue.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lblActualValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        lblActualValue.setHeight("15px");
        flwTd.add(lblActualValue);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", "25px", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel(" - ");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", "25px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Package value :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "25px", "left", null, DmPosition.NONE, true);
        lblPackageValue.setStyleName("dm-HLabel");
        lblPackageValue.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lblPackageValue.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblPackageValue.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lblPackageValue.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        lblPackageValue.setHeight("15px");
        flwTd.add(lblPackageValue);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", "25px", "left", null, DmPosition.NONE, true);
        lbl = new DmLabel(" = ");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(50, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", "25px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Difference :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "25px", "left", null, DmPosition.NONE, true);
        lblDifference.setStyleName("dm-HLabel");
        lblDifference.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lblDifference.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDifference.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lblDifference.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        lblDifference.setHeight("15px");
        flwTd.add(lblDifference);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel medBottomArea() {
        DmLabel lbl;
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", "25px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Medicine / Item Amount :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", "25px", "left", null, DmPosition.NONE, true);
        lblMedicineItemAmt.setStyleName("dm-HLabel");
        lblMedicineItemAmt.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lblMedicineItemAmt.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblMedicineItemAmt.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lblMedicineItemAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        lblMedicineItemAmt.setHeight("15px");
        flwTd.add(lblMedicineItemAmt);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", "25px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Diagnostic Amount :");
        lbl.setStyleName("dm-HLabel");
        lbl.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        lbl.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lbl.setHeight("15px");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "29%", "25px", "left", null, DmPosition.NONE, true);
        lblInvestigationAmt.setStyleName("dm-HLabel");
        lblInvestigationAmt.getElement().getStyle().setMarginTop(7, Style.Unit.PX);
        lblInvestigationAmt.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblInvestigationAmt.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        lblInvestigationAmt.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        lblInvestigationAmt.setHeight("15px");
        flwTd.add(lblInvestigationAmt);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Service">
    interface GridService_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServiceMaster_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<GrdServiceMaster_Mdl, String> amt();

        @Editor.Path("Rate")
        ValueProvider<GrdServiceMaster_Mdl, String> rate();

        @Editor.Path("Quantity")
        ValueProvider<GrdServiceMaster_Mdl, String> quantity();

        @Editor.Path("PkgAmount")
        ValueProvider<GrdServiceMaster_Mdl, String> pkgAmount();

        @Editor.Path("PkgRate")
        ValueProvider<GrdServiceMaster_Mdl, String> pkgRate();
    }
    final DmGrid<GrdServiceMaster_Mdl, GridService_PrpAcs> grdService = new DmGrid(GWT.create(GridService_PrpAcs.class));

    private Grid iniGrdService() {
        grdService.addRowNumberColumn();
        grdService.addTextColumn(grdService.getPropertyAccess().name(), "Service", 400, "");
        grdService.addTextColumn(grdService.getPropertyAccess().quantity(), "Quantity", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdService.addTextColumn(grdService.getPropertyAccess().rate(), "Rate", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdService.addTextColumn(grdService.getPropertyAccess().amt(), "Amount", 110, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdService.addTextColumn(grdService.getPropertyAccess().pkgRate(), "Package Rate", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdService.addTextColumn(grdService.getPropertyAccess().pkgAmount(), "Package Amount", 120, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdService.initializeGrid(grdService.getPropertyAccess().fieldHashCode(), 940, 320);

        return grdService.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Item">
    interface GridItem_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServiceMaster_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<GrdServiceMaster_Mdl, String> amt();

        @Editor.Path("Rate")
        ValueProvider<GrdServiceMaster_Mdl, String> rate();

        @Editor.Path("Quantity")
        ValueProvider<GrdServiceMaster_Mdl, String> quantity();
    }
    final DmGrid<GrdServiceMaster_Mdl, GridItem_PrpAcs> grdItem = new DmGrid(GWT.create(GridItem_PrpAcs.class));

    private Grid iniGrdItem() {
        grdItem.addRowNumberColumn();
        grdItem.addTextColumn(grdItem.getPropertyAccess().name(), "Medicine / Item", 185, "");
        grdItem.addTextColumn(grdItem.getPropertyAccess().quantity(), "Qty.", 55, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdItem.addTextColumn(grdItem.getPropertyAccess().rate(), "Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdItem.addTextColumn(grdItem.getPropertyAccess().amt(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdItem.initializeGrid(grdItem.getPropertyAccess().fieldHashCode(), 465, 320);

        return grdItem.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Investigation">
    interface GridInvestigation_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServiceMaster_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<GrdServiceMaster_Mdl, String> amt();

        @Editor.Path("Rate")
        ValueProvider<GrdServiceMaster_Mdl, String> rate();

        @Editor.Path("Quantity")
        ValueProvider<GrdServiceMaster_Mdl, String> quantity();
    }
    final DmGrid<GrdServiceMaster_Mdl, GridInvestigation_PrpAcs> grdInvestigation = new DmGrid(GWT.create(GridInvestigation_PrpAcs.class));

    private Grid iniGrdInvestigation() {
        grdInvestigation.addRowNumberColumn();
        grdInvestigation.addTextColumn(grdInvestigation.getPropertyAccess().name(), "Investigation", 190, "");
        grdInvestigation.addTextColumn(grdInvestigation.getPropertyAccess().quantity(), "Qty.", 55, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdInvestigation.addTextColumn(grdInvestigation.getPropertyAccess().rate(), "Rate", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdInvestigation.addTextColumn(grdInvestigation.getPropertyAccess().amt(), "Amount", 100, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);;
        grdInvestigation.initializeGrid(grdInvestigation.getPropertyAccess().fieldHashCode(), 470, 320);

        return grdInvestigation.getGrid();
    }
    //</editor-fold>
}
