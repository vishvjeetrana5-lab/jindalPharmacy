package aarogya.master.eod.client;

import aarogya.master.eod.shared.GrdProcessDetail_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.stStatic.shared.STEnum;
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
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;

abstract class EOD_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().EOD.caption;
    public final static String roomPostingBlog = "Room Posting Blog";
    public final static String labConsumptionBlog = "Lab Consumption Blog";
    public final static String opdAcPostingBlog = "OPD A/c Posting Blog";
    public final static String ipdAcPostingBlog = "IPD A/c Posting Blog";
    public final static String dayCareAcPostingBlog = "Day Care A/c Posting Blog";
    public final static String diagnosticAcPostingBlog = "Diagnostic A/c Posting Blog";
    public final static String emergencyAcPostingBlog = "Emergency A/c Posting Blog";
    public final static String pharmacyAcPostingBlog = "Pharmacy A/c Posting Blog";
    public final static String misAppUpdation = STEnum.ProcessEOD.MIS_APP_UPDATION.getValue();

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final Button btnRun = new Button();
    final Button btnClose = new Button();
    final Button btnHistory = new Button();
    final DmToolContainer toolContainer = new DmToolContainer();

    final DmTextBox txtDate = new DmTextBox(11, true);

    public EOD_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("280px");
        vrtInnerForm.add(toolContainer);
        createToolControl();

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
        DmClient.setStyle(flwTable, "100%", "399px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "400px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdProcess()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private void createToolControl() {
        Anchor aBtn;
        InlineLabel label;

        txtDate.setWidth("110px");
        txtDate.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        txtDate.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        txtDate.getElement().setPropertyString("placeholder", "Date");

        label = new InlineLabel("Run");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRun.setTitle("Run");
        btnRun.addStyleName("dm-top-control-btnStyle");
        btnRun.getElement().appendChild(aBtn.getElement());
        btnRun.getElement().appendChild(label.getElement());
        btnRun.setWidth("40px");

        label = new InlineLabel("History");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-history");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnHistory.setTitle("History");
        btnHistory.addStyleName("dm-top-control-btnStyle");
        btnHistory.getElement().appendChild(aBtn.getElement());
        btnHistory.getElement().appendChild(label.getElement());
        btnHistory.setWidth("40px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");

        toolContainer.addWidget(vl.add(txtDate));
//        toolContainer.addWidget(btnRun);
        toolContainer.addWidget(btnHistory);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Process Detail">
    interface GridProcess_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdProcessDetail_Mdl> {

        @Editor.Path("Remark")
        ValueProvider<GrdProcessDetail_Mdl, String> remark();

        @Editor.Path("LastRun")
        ValueProvider<GrdProcessDetail_Mdl, String> lastRun();

        @Editor.Path("User")
        ValueProvider<GrdProcessDetail_Mdl, String> user();

    }
    final DmGrid<GrdProcessDetail_Mdl, GridProcess_PrpAcs> grdProcessDetail = new DmGrid(GWT.create(GridProcess_PrpAcs.class));

    private Grid iniGrdProcess() {
        ColumnConfig<GrdProcessDetail_Mdl, String> vacantImage;
        grdProcessDetail.addRowNumberColumn();
        grdProcessDetail.addTextColumn(grdProcessDetail.getPropertyAccess().name(), "Process Name", 270, "");
        grdProcessDetail.addTextColumn(grdProcessDetail.getPropertyAccess().remark(), "Remark", 670, "");
        grdProcessDetail.addTextColumn(grdProcessDetail.getPropertyAccess().lastRun(), "Last Run", 145, "");
        grdProcessDetail.addTextColumn(grdProcessDetail.getPropertyAccess().user(), "User", 90, "");
        vacantImage = grdProcessDetail.addTextColumn(grdProcessDetail.getPropertyAccess().code(), "Delete", 55, "");
        grdProcessDetail.initializeGrid(grdProcessDetail.getPropertyAccess().fieldHashCode(), 1280, 544);

        vacantImage.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        vacantImage.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-times' style='font-size:12px; color:red;'></span>");
            }
        });

        return grdProcessDetail.getGrid();
    }
    //</editor-fold>

}
