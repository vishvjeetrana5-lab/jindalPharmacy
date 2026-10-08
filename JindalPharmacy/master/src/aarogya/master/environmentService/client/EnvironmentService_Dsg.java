package aarogya.master.environmentService.client;

import aarogya.master.environmentService.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class EnvironmentService_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().ENVIRONMENT_SERVICE.caption;
    public final static String modifiedByBlog = "Modified By & Date";

    public static final String tabServiceCategory = "Service Category";
    public static final String tabDiagnosticCategory = "Diagnostic Category";
    public static final String tabBloodComponent = "Blood Component";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final FlowPanel flwGeneral = new FlowPanel();
    final FlowPanel flwBloodComponent = new FlowPanel();
    final FlowPanel flwDiagnosticCategory = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final Button btnSave = new Button();
    final Button btnCopy = new Button();
    final Button btnClose = new Button();
    final Button btnDelete = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();

    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);

    //<editor-fold defaultstate="collapsed" desc="txtHSiteCopy">
    final DmHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHSiteCopy = new DmHelpGrid(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniSiteCopy() {
        txtHSiteCopy.getDmGrid().addTextColumn(txtHSiteCopy.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteCopy.getDmGrid().initializeGrid(txtHSiteCopy.getDmGrid().getPropertyAccess().code(), 120, 0, 1);

        txtHSiteCopy.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHSite.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public EnvironmentService_Dsg() {
        ini();
        iniSite();
        iniSiteCopy();
//        iniGrdGeneral();
//        iniGrdBloodComponent();
//        iniGrdDiagnosticCategory();
    }

    private DmToolContainer iniToolControl() {
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
        btnSave.setWidth("70px");

        label = new InlineLabel("Copy");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-copy");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCopy.setTitle("Copy");
        btnCopy.addStyleName("dm-top-control-btnStyle");
        btnCopy.getElement().appendChild(aBtn.getElement());
        btnCopy.getElement().appendChild(label.getElement());
        btnCopy.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(vl.add(btnSave));
        toolContainer.addWidget(vl.add(btnCopy));
        toolContainer.addWidget(vl.add(btnClose));
        toolContainer.addGap();
        return toolContainer;

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("577px");
        vrtInnerForm.add(vl.add(iniToolControl()));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "533px", null, null, DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        // Grid Tab
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "510px", "left", null, DmPosition.NONE, true);
        flwTr.add(tabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "75%", "20px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "93%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "518px", null, null, DmPosition.NONE, true);

        DmClient.setStyle(flwGeneral, "100%", "485px", null, null, DmPosition.CENTER, true);
        flwGeneral.add(iniGrdGeneral());

        DmClient.setStyle(flwDiagnosticCategory, "100%", "485px", null, null, DmPosition.CENTER, true);
        flwDiagnosticCategory.add(iniGrdDiagnosticCategory());

        DmClient.setStyle(flwBloodComponent, "100%", "485px", null, null, DmPosition.CENTER, true);
        flwBloodComponent.add(iniGrdBloodComponent());

        return tabPanel;
    }

    //<editor-fold defaultstate="collapsed" desc="grdGneral">
    interface GrdGeneralTwo_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServiceCategory_Mdl> {

        @Editor.Path("DefConsultantText")
        ValueProvider<GrdServiceCategory_Mdl, String> defConsultantText();

        @Editor.Path("DefDepartmentText")
        ValueProvider<GrdServiceCategory_Mdl, String> defDepartmentText();

//        @Editor.Path("PurchaseServiceText")
//        ValueProvider<GrdServiceCategory_Mdl, String> purchaseServiceText();
//        @Editor.Path("ItsText")
//        ValueProvider<GrdServiceCategory_Mdl, String> itsText();
//
        @Editor.Path("LinkedServiceText")
        ValueProvider<GrdServiceCategory_Mdl, String> linkedServiceText();

    }
    final DmGrid<GrdServiceCategory_Mdl, GrdGeneralTwo_PrpAcs> grdServiceCategory = new DmGrid(GWT.create(GrdGeneralTwo_PrpAcs.class));

    private Grid iniGrdGeneral() {
        ColumnConfig<GrdServiceCategory_Mdl, String> grdPop;
        grdServiceCategory.addRowNumberColumn();
        grdServiceCategory.addTextColumn(grdServiceCategory.getPropertyAccess().name(), "Serivce Category", 340, "");
        grdServiceCategory.addTextColumn(grdServiceCategory.getPropertyAccess().defConsultantText(), "Default Consultant", 310, "");
        grdServiceCategory.addTextColumn(grdServiceCategory.getPropertyAccess().defDepartmentText(), "Default Department", 260, "");
        grdServiceCategory.addTextColumn(grdServiceCategory.getPropertyAccess().linkedServiceText(), "Link Service", 265, "");
        grdPop = grdServiceCategory.addTextColumn(grdServiceCategory.getPropertyAccess().code(), "", 30, "");
        grdServiceCategory.initializeGrid(grdServiceCategory.getPropertyAccess().fieldHashCode(), 1250, 472);

        grdPop.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdPop.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (grdServiceCategory.getGrid().getStore().get(context.getIndex()).isHaveServices()) {
                    sb.appendHtmlConstant("<span class = 'fa fa-bars' style='font-size:12px; color:red;'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-bars' style='font-size:12px; color:Black;'></span>");
                }
            }
        });

        return grdServiceCategory.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="diagnostic Category">
    final DmGrid<GrdServiceCategory_Mdl, GrdGeneralTwo_PrpAcs> grdDiagnosticCategory = new DmGrid(GWT.create(GrdGeneralTwo_PrpAcs.class));

    private Grid iniGrdDiagnosticCategory() {
        ColumnConfig<GrdServiceCategory_Mdl, String> grdDiagonosticPop;
        grdDiagnosticCategory.addRowNumberColumn();
        grdDiagnosticCategory.addTextColumn(grdDiagnosticCategory.getPropertyAccess().name(), "Diagnostic Category", 340, "");
        grdDiagnosticCategory.addTextColumn(grdDiagnosticCategory.getPropertyAccess().defConsultantText(), "Default Consultant", 310, "");
        grdDiagnosticCategory.addTextColumn(grdDiagnosticCategory.getPropertyAccess().defDepartmentText(), "Default Department", 260, "");
        grdDiagnosticCategory.addTextColumn(grdDiagnosticCategory.getPropertyAccess().linkedServiceText(), "Link Service", 265, "");
        grdDiagonosticPop = grdDiagnosticCategory.addTextColumn(grdDiagnosticCategory.getPropertyAccess().code(), "", 30, "");
        grdDiagnosticCategory.initializeGrid(grdDiagnosticCategory.getPropertyAccess().fieldHashCode(), 1250, 472);

        grdDiagonosticPop.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        grdDiagonosticPop.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                if (grdDiagnosticCategory.getGrid().getStore().get(context.getIndex()).isHaveServices()) {
                    sb.appendHtmlConstant("<span class = 'fa fa-bars' style='font-size:12px; color:red;'></span>");
                } else {
                    sb.appendHtmlConstant("<span class = 'fa fa-bars' style='font-size:12px; color:Black;'></span>");
                }
            }
        });

        return grdDiagnosticCategory.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="blood Component">
    interface GrdBloodComponent_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdBloodComponent_Mdl> {

        @Editor.Path("linkedServiceText")
        ValueProvider<GrdBloodComponent_Mdl, String> linkedServiceText();
    }

    final DmGrid<GrdBloodComponent_Mdl, GrdBloodComponent_PrpAcs> grdBloodComponent = new DmGrid(GWT.create(GrdBloodComponent_PrpAcs.class));

    private Grid iniGrdBloodComponent() {
        grdBloodComponent.addRowNumberColumn();
        grdBloodComponent.addTextColumn(grdBloodComponent.getPropertyAccess().name(), "Blood Component", 600, "");
        grdBloodComponent.addTextColumn(grdBloodComponent.getPropertyAccess().linkedServiceText(), "Link Service", 605, "");
        grdBloodComponent.initializeGrid(grdBloodComponent.getPropertyAccess().fieldHashCode(), 1250, 472);
        return grdBloodComponent.getGrid();
    }
    //</editor-fold>

}
