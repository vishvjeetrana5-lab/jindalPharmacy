package aarogya.dashBoard.dataModificationLog.client;

import aarogya.dashBoard.dataModificationLog.shared.GrdDataModificationLog_Mdl;
import aarogya.projectMenu.self.shared.pmDashBoard;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.shared.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import dataman.dmToolkit.dmGridPaging.client.DmGridPaging;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class DataModificationLog_Dsg extends IsWidgetExtended {

    final String widgetName = new pmDashBoard().DATA_MODIFICATION_LOG.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();

    final Button btnSetting = new Button();
    final Button btnRefresh = new Button();

    final Button btnPrint = new Button();
    final Button btnExcel = new Button();
    final Button btnClose = new Button();
    final DmTextBox txtSearchValue = new DmTextBox(100, false);
    final DmLabel lblDate = new DmLabel("");

    final FlowPanel flwForm = new FlowPanel();

    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    public DataModificationLog_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("550px");
        vrtInnerForm.add(vl.add(iniToolContainer()));

        iniGrdLedger();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;
        FlowPanel flw;

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");

        label = new InlineLabel("Print");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-file-pdf-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");

        label = new InlineLabel("Excel");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-table");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnExcel.setTitle("Excel");
        btnExcel.addStyleName("dm-top-control-btnStyle");
        btnExcel.getElement().appendChild(aBtn.getElement());
        btnExcel.getElement().appendChild(label.getElement());
        btnExcel.setWidth("55px");

        label = new InlineLabel("Refresh");
        label.setWidth("30px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-refresh");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnRefresh.setTitle("Refresh");
        btnRefresh.addStyleName("dm-top-control-btnStyle");
        btnRefresh.getElement().appendChild(aBtn.getElement());
        btnRefresh.getElement().appendChild(label.getElement());
        btnRefresh.setWidth("55px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escape)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");

        flw = new FlowPanel();
        DmClient.setStyle(flw, "100%", null, "left", null, DmPosition.NONE, true);
        flw.setStyleName("dm-StripLabel");
        lblDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginRight(5, Style.Unit.PX);
        lblDate.setHeight("20px");
        flw.add(lblDate);

        toolContainer.addWidget(btnSetting);
//        toolContainer.addWidget(btnPrint);
//        toolContainer.addWidget(btnExcel);
        toolContainer.addWidget(btnRefresh);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();
        toolContainer.addWidget(flw);
        toolContainer.setTitle(widgetName);
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "529px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "99.7%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(txtSearchValue));
        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(grdDisplay));
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Display">
    interface GrdDataModification_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdDataModificationLog_Mdl> {

        @Editor.Path("LogDateTime")
        ValueProvider<GrdDataModificationLog_Mdl, String> logDateTime();

        @Editor.Path("UserName")
        ValueProvider<GrdDataModificationLog_Mdl, String> userName();

        @Editor.Path("Mode")
        ValueProvider<GrdDataModificationLog_Mdl, String> mode();

        @Editor.Path("RecordName1")
        ValueProvider<GrdDataModificationLog_Mdl, String> recordName1();

        @Editor.Path("RecordDesc1")
        ValueProvider<GrdDataModificationLog_Mdl, String> recordDesc1();

        @Editor.Path("Menu")
        ValueProvider<GrdDataModificationLog_Mdl, String> menu();

        @Editor.Path("Module")
        ValueProvider<GrdDataModificationLog_Mdl, String> module();

        @Editor.Path("ModifyReason")
        ValueProvider<GrdDataModificationLog_Mdl, String> modifyReason();

        @Editor.Path("Remark")
        ValueProvider<GrdDataModificationLog_Mdl, String> remark();

        @Editor.Path("RecordName2")
        ValueProvider<GrdDataModificationLog_Mdl, String> recordName2();

        @Editor.Path("RecordDesc2")
        ValueProvider<GrdDataModificationLog_Mdl, String> recordDesc2();

        @Editor.Path("ShortSiteName")
        ValueProvider<GrdDataModificationLog_Mdl, String> shortSiteName();

        @Editor.Path("Financialyear")
        ValueProvider<GrdDataModificationLog_Mdl, String> financialyear();

        @Editor.Path("VrTypeShortName")
        ValueProvider<GrdDataModificationLog_Mdl, String> vrTypeShortName();
    }

    final DmGridPaging<GrdDataModificationLog_Mdl, GrdDataModification_PrpAcs, KeyFieldExtended> grdDisplay = new DmGridPaging(GWT.create(GrdDataModification_PrpAcs.class), 22, false);

    private void iniGrdLedger() {
        ColumnConfig<GrdDataModificationLog_Mdl, String> recordName1, view;
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().logDateTime(), "Log Date/ Time", 130, "logdate", DmDataType.DATETIME);
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().userName(), "User Name", 60, "username");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().mode(), "Mode", 55, "mode");
        recordName1 = grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().recordName1(), "Record Name-1", 160, "keyname_1");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().recordDesc1(), "Record Desc.-1", 140, "keydescription_1");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().menu(), "Menu Name", 150, "menuname");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().module(), "Module Name", 90, "modulename");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().modifyReason(), "Modify Reason", 90, "modifyreason");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().remark(), "Remark", 100, "remark");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().shortSiteName(), "A/c Unit", 55, "sitemcode");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().financialyear(), "F.Y.", 65, "financialyear");
        grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().vrTypeShortName(), "Vr. Type", 60, "v_type_s_name");
        view = grdDisplay.getDmGrid().addTextColumn(grdDisplay.getDmGrid().getPropertyAccess().code(), "", 40, "");

        grdDisplay.getDmGrid().initializeGrid(grdDisplay.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 487, 5);

        recordName1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        recordName1.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {

                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word; color: #1D53D2;'>" + DmShared.xNull(value) + "</div>");

            }
        });

        view.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        view.setCellPadding(false);
        view.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'gwt-Anchor fa fa-eye' style='font-size:1.3em; margin-top:10%; color :green; cursor: pointer;'></span>");
            }
        });

        grdDisplay.getHelper().addSort("logdate", DmSortType.DESC, "logdate");
        grdDisplay.getHelper().addSort("sessionid", DmSortType.ASC, "sessionid");
    }
    //</editor-fold>
}
