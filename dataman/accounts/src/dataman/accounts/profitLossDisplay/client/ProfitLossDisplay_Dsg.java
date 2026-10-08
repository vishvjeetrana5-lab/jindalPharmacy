package dataman.accounts.profitLossDisplay.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.accounts.profitLossDisplay.shared.ProfitLossDisplay_Mdl;
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
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class ProfitLossDisplay_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().PROFIT_AND_LOSS_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSetting = new Button();
    final Button btnBack = new Button();
    final Button btnPrint = new Button();
    final DmLabel lblHeading = new DmLabel("");
    final DmLabel lblDate = new DmLabel("");
    final DmLabel lblTotal = new DmLabel("");
    final DmLabel lblTotal1 = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final Button btnFinancialBook = new Button();

    //<editor-fold defaultstate="collapsed" desc="grdProfitLoss">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<ProfitLossDisplay_Mdl> {

        @Editor.Path("ExpensesName")
        ValueProvider<ProfitLossDisplay_Mdl, String> expensesName();

        @Editor.Path("ExpensesChildBalance")
        ValueProvider<ProfitLossDisplay_Mdl, String> expensesChildBalance();

        @Editor.Path("ExpensesAmount")
        ValueProvider<ProfitLossDisplay_Mdl, String> expensesAmount();

        @Editor.Path("RevenueName")
        ValueProvider<ProfitLossDisplay_Mdl, String> revenueName();

        @Editor.Path("RevenueAmount")
        ValueProvider<ProfitLossDisplay_Mdl, String> revenueAmount();

        @Editor.Path("RevenueChildBalance")
        ValueProvider<ProfitLossDisplay_Mdl, String> revenueChildBalance();
    }

    final DmGrid<ProfitLossDisplay_Mdl, GrdDisplay_PrpAcs> grdDisplay = new DmGrid(GWT.create(GrdDisplay_PrpAcs.class));

    private Grid iniGrdDisplay() {
        ColumnConfig<ProfitLossDisplay_Mdl, String> expensesNameColumn, expensesBalanceColumn, expensesAmountColumn,
                revenueNameColumn, revenueBalanceColumn, revenueAmountColumn;

        expensesNameColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().expensesName(), "Particular", 340, "");
        expensesNameColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplay_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getExpensesChildSubCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal;' > " + DmShared.xNull(plm.getExpensesChildSubName()) + " </div>";
                } else if (DmShared.iNull(plm.getExpensesChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal; font-weight:bold;' > " + DmShared.xNull(plm.getExpensesChildGroupName()) + " </div>";
                } else {
                    valHtml = "<div style='font-weight:bold;' >" + DmShared.xNull(plm.getExpensesName()) + "</div>";
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        expensesBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().expensesChildBalance(), "", 150, "");
        expensesBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        expensesBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplay_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getExpensesChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(plm.getExpensesChildBalance());
                } else if (DmShared.iNull(plm.getExpensesChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getExpensesChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(plm.getExpensesChildBalance());
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        expensesAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().expensesAmount(), "Amount", 150, "");
        expensesAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        expensesAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplay_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getExpensesAmount()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });

        revenueNameColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().revenueName(), "Particular", 340, "");
        revenueNameColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplay_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getRevenueChildSubCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal;' > " + DmShared.xNull(plm.getRevenueChildSubName()) + " </div>";
                } else if (DmShared.iNull(plm.getRevenueChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal; font-weight:bold;' > " + DmShared.xNull(plm.getRevenueChildGroupName()) + " </div>";
                } else {
                    valHtml = "<div style='font-weight:bold;' >" + DmShared.xNull(plm.getRevenueName()) + "</b>";
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        revenueBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().revenueChildBalance(), "", 150, "");
        revenueBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        revenueBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplay_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getRevenueChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(plm.getRevenueChildBalance());
                } else if (DmShared.iNull(plm.getRevenueChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getRevenueChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(plm.getRevenueChildBalance());
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        revenueAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().revenueAmount(), "Amount", 150, "");
        revenueAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        revenueAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplay_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getRevenueAmount()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });

        grdDisplay.initializeGrid(grdDisplay.getPropertyAccess().fieldHashCode(), 1283, 485);

        grdDisplay.getGrid().getView().setViewConfig(new GridViewConfig<ProfitLossDisplay_Mdl>() {

            @Override
            public String getColStyle(ProfitLossDisplay_Mdl model, ValueProvider<? super ProfitLossDisplay_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";

                if (DmShared.xNull(model.getExpensesCode()).trim().equalsIgnoreCase("TT")) {
                    styleClass += "dm-colorWhite dm-backGroundGray dm-fontWeightBold ";
                } else {

                    if (colIndex == 0 || colIndex == 2) {
                        if (DmShared.xNull(model.getExpensesCode()).trim().equalsIgnoreCase("GP")
                                || DmShared.xNull(model.getExpensesCode()).trim().equalsIgnoreCase("NP")) {
                            styleClass += "dm-colorGreen ";
                        } else if (DmShared.xNull(model.getExpensesCode()).trim().equalsIgnoreCase("GL")) {
                            styleClass += "dm-colorRed ";
                        }
                    }

                    if (colIndex == 3 || colIndex == 5) {
                        if (DmShared.xNull(model.getRevenueCode()).trim().equalsIgnoreCase("GP")) {
                            styleClass += "dm-colorGreen ";
                        } else if (DmShared.xNull(model.getRevenueCode()).trim().equalsIgnoreCase("GL")
                                || DmShared.xNull(model.getRevenueCode()).trim().equalsIgnoreCase("NL")) {
                            styleClass += "dm-colorRed ";
                        }
                    }
                }

                return styleClass;
            }

            @Override
            public String getRowStyle(ProfitLossDisplay_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdDisplay.getGrid();
    }
    //</editor-fold>

    public ProfitLossDisplay_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("550px");

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
        btnBack.setTitle("Back");
        btnBack.addStyleName("dm-top-control-btnStyle");
        btnBack.getElement().appendChild(aBtn.getElement());
        btnBack.getElement().appendChild(label.getElement());
        btnBack.setWidth("55px");

        label = new InlineLabel("Financial Book Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-book");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnFinancialBook.setTitle("Financial Book Close");
        btnFinancialBook.addStyleName("dm-top-control-btnStyle");
        btnFinancialBook.getElement().appendChild(aBtn.getElement());
        btnFinancialBook.getElement().appendChild(label.getElement());
        btnFinancialBook.setWidth("55px");

        flw = new FlowPanel();
        DmClient.setStyle(flw, "190px", null, "left", null, DmPosition.NONE, true);
        flw.setStyleName("dm-StripLabel");
        lblDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblDate.setHeight("20px");
        flw.add(lblDate);

        FlowPanel flwHeading = new FlowPanel();
        DmClient.setStyle(flwHeading, "300px", null, "left", null, DmPosition.NONE, true);
        flwHeading.setStyleName("dm-StripLabel");
        lblHeading.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblHeading.getElement().getStyle().setFontSize(14, Style.Unit.PX);
        lblHeading.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblHeading.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblHeading.setHeight("20px");
        flwHeading.add(lblHeading);

        toolContainer.addWidget(btnBack);
        toolContainer.addWidget(btnSetting);
        toolContainer.addWidget(btnPrint);
        toolContainer.addWidget(btnFinancialBook);
        toolContainer.addWidget(flwHeading);
        toolContainer.addGap();
        toolContainer.addWidget(flw);
        toolContainer.setTitle(widgetName);
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "520px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "485px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdDisplay()));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "35px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "780px", "35px", "left", null, DmPosition.NONE, true);
        lblTotal.getElement().setAttribute("style", " text-align: right; width: 618px !important; height: 15px; margin-top: 12px;font-weight: bold;");
        flwTd.add(lblTotal);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "495px", "35px", "left", null, DmPosition.NONE, true);
        lblTotal1.setHeight("25px");
        lblTotal1.getElement().setAttribute("style", " text-align: right; width: 495px !important; height: 15px; margin-top: 12px;font-weight: bold;");
        flwTd.add(lblTotal1);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
