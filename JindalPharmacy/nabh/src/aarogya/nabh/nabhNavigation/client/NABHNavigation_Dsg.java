package aarogya.nabh.nabhNavigation.client;

import aarogya.nabh.nabhNavigation.shared.GrdNABHNavigation_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxTime.client.DmTextBoxTime;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class NABHNavigation_Dsg extends IsWidgetExtended {

    final String widgetName = "GUI_EntryForm";
    public String widgetNameDisplay = widgetName;

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final DmUserControl userControl = new DmUserControl();

    final DmTextBox txtPatient = new DmTextBox(50, false);
    final DmTextBox txtGender = new DmTextBox(50, false);
    final DmTextBox txtGuardian = new DmTextBox(50, false);
    final DmTextBox txtUHIDNo = new DmTextBox(20, false);
    final DmTextBox txtAddress = new DmTextBox(200, false);
    final DmTextBox txtAge = new DmTextBox(15, false);
    final DmTextBox txtMobile = new DmTextBox(35, false);
    final DmTextBox txtRegConsultant = new DmTextBox(50, false);
    final DmTextBox txtOrganization = new DmTextBox(50, false);
    final DmTextBox txtSearchValue = new DmTextBox(50, false);
    final DmTextBox txtIpdNo = new DmTextBox(20, true);
    final DmTextBoxTime txtAdmittingTime = new DmTextBoxTime(false);
    final DmTextBox txtAdmittingDate = new DmTextBox(11, true);

    final DmToolContainer toolContainer = new DmToolContainer();
    final FlowPanel flwTr = new FlowPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button("");

    //<editor-fold defaultstate="collapsed" desc="TxtHType"> 
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHType() {
        txtHType.getDmGrid().addTextColumn(txtHType.getDmGrid().getPropertyAccess().name(), "Type", 150, "Type");
        txtHType.getDmGrid().initializeGrid(txtHType.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold> 

    public NABHNavigation_Dsg() {
        ini();
        iniToolContorls();
        iniTxtHType();

    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("506px");
        vrtInnerForm.add(toolContainer);

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        flwClose.add(btnClose);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        CaptionPanel cPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "460px", null, null, DmPosition.CENTER, true);
        /*====================================< 1st Row >====================================*/

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);
        flwTr.add(topSection());
        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        // UHID Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmPosition.NONE, true);
        cPnl = new CaptionPanel();
        cPnl.getElement().setAttribute("style", "border-right: none; border-left: none; width 98%; height: 35px;");
        cPnl.add(uhidSection());
        flwTr.add(cPnl);
        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "405px", "left", null, DmPosition.NONE, true);
        flwTr.add(SearchHistoryTabArea());
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "20px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////1st Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtIpdNo);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegConsultant));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "9%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Organization");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOrganization));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "6%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Date");
        lbl.getElement().getStyle().setMarginLeft(27, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdmittingDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdmittingTime));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    private FlowPanel uhidSection() {
        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "48px", null, null, DmPosition.CENTER, true);

        ////////////////////////////////1st Row//////////////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "48px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", "48px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftUhid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", "48px", "left", null, DmPosition.NONE, true);
        flwTd.add(middleUhid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "21%", "48px", "left", null, DmPosition.NONE, true);
        flwTd.add(rightUhid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel leftUhid() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Patient Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPatient));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Guardian Name");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGuardian));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "82%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel middleUhid() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gender");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGender));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Age");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAge));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightUhid() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("UHID No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtUHIDNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "34%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        flwTd.add(lbl);
        lbl.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "66%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel SearchHistoryTabArea() {
        FlowPanel flwTable, flwTr, flwTd;

        HorizontalPanel hrtPanel;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "398px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        flwTr.getElement().getStyle().setBackgroundColor("lightgray");
        DmClient.setStyle(flwTr, "100%", "25px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20.5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSearchValue));
        txtSearchValue.setHeight("21px");
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "right", null, DmPosition.NONE, false);
        hrtPanel = new HorizontalPanel();
        hrtPanel.addStyleName("dm-StripLabel");
        hrtPanel.getElement().getStyle().setBackgroundColor("lightgray");
        hrtPanel.getElement().getStyle().setWidth(100, Style.Unit.PCT);

        flwTd.add(hrtPanel);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "390px", "left", null, DmPosition.NONE, true);
        flwTr.add(inigrdNabhIndicator());
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdNABHIndicator">
    interface GrdNABHIndicatorl_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdNABHNavigation_Mdl> {

        @Editor.Path("IndicatorDescription")
        ValueProvider<GrdNABHNavigation_Mdl, String> indicatorDescription();

        @Editor.Path("Std")
        ValueProvider<GrdNABHNavigation_Mdl, String> std();

        @Editor.Path("MenuLink")
        ValueProvider<GrdNABHNavigation_Mdl, String> menuLink();

        @Editor.Path("ViewEntryPoint")
        ValueProvider<GrdNABHNavigation_Mdl, String> viewEntryPoint();

        @Editor.Path("ViewReport")
        ValueProvider<GrdNABHNavigation_Mdl, String> viewReport();
    }

    final DmGrid<GrdNABHNavigation_Mdl, GrdNABHIndicatorl_PrpAcs> grdNabhIndicator = new DmGrid(GWT.create(GrdNABHIndicatorl_PrpAcs.class));

    private Grid inigrdNabhIndicator() {
        ColumnConfig<GrdNABHNavigation_Mdl, String> indicatorDescription, viewReport, viewEntryPoint;
        grdNabhIndicator.addRowNumberColumn();
        indicatorDescription = grdNabhIndicator.addTextColumn(grdNabhIndicator.getPropertyAccess().indicatorDescription(), "Indicator Description", 50, "");
        grdNabhIndicator.addTextColumn(grdNabhIndicator.getPropertyAccess().std(), "STD", 110, "");
        grdNabhIndicator.addTextColumn(grdNabhIndicator.getPropertyAccess().menuLink(), "Menu Link", 90, "");
        viewEntryPoint = grdNabhIndicator.addTextColumn(grdNabhIndicator.getPropertyAccess().viewEntryPoint(), "", 50, "");
        viewReport = grdNabhIndicator.addTextColumn(grdNabhIndicator.getPropertyAccess().viewReport(), "", 50, "");
        grdNabhIndicator.initializeGrid(grdNabhIndicator.getPropertyAccess().fieldHashCode(), 1280, 365, 2);

        indicatorDescription.setCell(new AbstractCell<String>() {

            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<div style='white-space: normal; word-wrap: break-word;'>" + DmShared.xNull(value) + "</div>");
            }
        });

        grdNabhIndicator.getGrid().getView().setViewConfig(new GridViewConfig<GrdNABHNavigation_Mdl>() {
            @Override
            public String getColStyle(GrdNABHNavigation_Mdl model, ValueProvider<? super GrdNABHNavigation_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";

                if (DmShared.xNull(model.getStatusTag()).equalsIgnoreCase("1")) {
                    styleClass += "dm-GridCellDairyCancel";

                }
                return styleClass;
            }

            @Override
            public String getRowStyle(GrdNABHNavigation_Mdl model, int rowIndex) {
                return "";
            }
        });

        viewReport.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        viewReport.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-file-excel-o' style='font-size:12px; color: rgb(125, 125, 125);'></span>");
            }
        });
        viewEntryPoint.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        viewEntryPoint.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span class = 'fa fa-file-o' style='font-size:12px; color: rgb(125, 125, 125);'></span>");
            }
        });

        return grdNabhIndicator.getGrid();
    }
    //</editor-fold>

}
