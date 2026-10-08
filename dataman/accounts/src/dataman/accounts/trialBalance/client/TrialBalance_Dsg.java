package dataman.accounts.trialBalance.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.libraryMenu.self.shared.lmAccounts;
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
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.accounts.trialBalance.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class TrialBalance_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().TRIAL_BALANCE_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSetting = new Button();
    final Button btnBack = new Button();
    final Button btnPrint = new Button();
    final DmLabel lblDate = new DmLabel("");
    final DmLabel lblAmtDr = new DmLabel("");
    final DmLabel lblAmtCr = new DmLabel("");
    final DmLabel lblAmtDiff = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="grdTrialBalance">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TrialBalance_Mdl> {

        @Editor.Path("ChildBalance")
        ValueProvider<TrialBalance_Mdl, String> childBalance();

        @Editor.Path("Debit")
        ValueProvider<TrialBalance_Mdl, String> debit();

        @Editor.Path("Credit")
        ValueProvider<TrialBalance_Mdl, String> credit();
    }

    final DmGrid<TrialBalance_Mdl, GrdDisplay_PrpAcs> grdDisplay = new DmGrid(GWT.create(GrdDisplay_PrpAcs.class));

    private Grid iniGrdDisplay() {
        ColumnConfig<TrialBalance_Mdl, String> nameColumn, balanceColumn, debitColumn, creditColumn;

        nameColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().name(), "Particulars", 575, "");
        nameColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                TrialBalance_Mdl tbm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                if (DmShared.iNull(tbm.getChildSubCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal;' > " + DmShared.xNull(tbm.getChildSubName()) + " </div>";
                } else if (DmShared.iNull(tbm.getChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal; font-weight:bold;' > " + DmShared.xNull(tbm.getChildGroupName()) + " </div>";
                } else {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(tbm.getSharedKeyFieldName()) + "</div>";
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        balanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().childBalance(), "", 200, "");
        balanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        balanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                TrialBalance_Mdl tbm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                if (DmShared.iNull(tbm.getChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(tbm.getChildBalance());
                } else if (DmShared.iNull(tbm.getChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(tbm.getChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(tbm.getChildBalance());
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        debitColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().debit(), "Debit", 200, "");
        debitColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        debitColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                TrialBalance_Mdl tbm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(tbm.getDebit()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });

        creditColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().credit(), "Credit", 200, "");
        creditColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        creditColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                TrialBalance_Mdl tbm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(tbm.getCredit()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });

        grdDisplay.initializeGrid(grdDisplay.getPropertyAccess().fieldHashCode(), 1283, 465, 2);

        return grdDisplay.getGrid();
    }
    //</editor-fold>

    public TrialBalance_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("525px");

        vrtInnerForm.add(vl.add(iniToolContainer()));

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
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());
        btnPrint.getElement().appendChild(label.getElement());
        btnPrint.setWidth("55px");

        label = new InlineLabel("Back");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-caret-left");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnBack.setTitle("Copy");
        btnBack.addStyleName("dm-top-control-btnStyle");
        btnBack.getElement().appendChild(aBtn.getElement());
        btnBack.getElement().appendChild(label.getElement());
        btnBack.setWidth("55px");

        flw = new FlowPanel();
        DmClient.setStyle(flw, "206px", null, "left", null, DmPosition.NONE, true);
        flw.setStyleName("dm-StripLabel");
        lblDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblDate.setHeight("20px");
        flw.add(lblDate);
        toolContainer.addWidget(btnBack);
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnPrint);
        toolContainer.addGap();
        toolContainer.addWidget(flw);
        toolContainer.setTitle(widgetName);
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "500px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "465px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdDisplay()));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        flwTr.setStyleName("dm-StripLabel");
        flwTr.getElement().setAttribute("style", "border-top: 1px solid lightgray; height:35px !important;");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "896px", "35px", "left", null, DmPosition.NONE, true);
        lblAmtDiff.getElement().setAttribute("style", "font-weight: bold; color: red; height: 20px !important; margin-top: 2%;");
        flwTd.add(lblAmtDiff);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "174px", "35px", "left", null, DmPosition.NONE, true);
        lblAmtDr.getElement().setAttribute("style", " text-align: right; width: 173px !important; height: 15px !important; margin-top: 10%; font-weight: bold;");
        flwTd.add(lblAmtDr);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "203px", "35px", "left", null, DmPosition.NONE, true);
        lblAmtCr.getElement().setAttribute("style", " text-align: right; width: 203px !important; height: 15px !important; margin-top: 8%; font-weight: bold;");
        flwTd.add(lblAmtCr);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        return flwTable;
    }
}
