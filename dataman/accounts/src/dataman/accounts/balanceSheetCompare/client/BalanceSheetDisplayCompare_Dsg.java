package dataman.accounts.balanceSheetCompare.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.accounts.balanceSheetCompare.shared.BalanceSheetDisplayCompare_Mdl;
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

abstract class BalanceSheetDisplayCompare_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().BALANCE_SHEET_COMPARE_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSetting = new Button();
    final Button btnBack = new Button();
    final Button btnPrint = new Button();
    final DmLabel lblHeading = new DmLabel("");
    final DmLabel lblDate = new DmLabel("");
    final DmLabel lblLstDate = new DmLabel("");
    final DmLabel lblTotal = new DmLabel("");
    final DmLabel lblTotal1 = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="grdBalanceSheet">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<BalanceSheetDisplayCompare_Mdl> {

        @Editor.Path("A_LHeadNameName")
        ValueProvider<BalanceSheetDisplayCompare_Mdl, String> a_LHeadNameName();

        @Editor.Path("A_LChildBalance")
        ValueProvider<BalanceSheetDisplayCompare_Mdl, String> a_LChildBalance();

        @Editor.Path("A_LAmount")
        ValueProvider<BalanceSheetDisplayCompare_Mdl, String> a_LAmount();

        @Editor.Path("A_LDrCr")
        ValueProvider<BalanceSheetDisplayCompare_Mdl, String> a_LDrCr();

        @Editor.Path("LstA_LChildBalance")
        ValueProvider<BalanceSheetDisplayCompare_Mdl, String> lstA_LChildBalance();

        @Editor.Path("LstA_LAmount")
        ValueProvider<BalanceSheetDisplayCompare_Mdl, String> lstA_LAmount();

        @Editor.Path("LstA_LDrCr")
        ValueProvider<BalanceSheetDisplayCompare_Mdl, String> lstA_LDrCr();
    }

    final DmGrid<BalanceSheetDisplayCompare_Mdl, GrdDisplay_PrpAcs> grdDisplay = new DmGrid(GWT.create(GrdDisplay_PrpAcs.class));

    private Grid iniGrdDisplay() {
        ColumnConfig<BalanceSheetDisplayCompare_Mdl, String> a_LHeadNameColumn, a_LBalanceColumn, a_LAmountColumn,
                lstA_LBalanceColumn, lstA_LAmountColumn, a_LDrCrColumn, lstA_LDrCrColumn;

        a_LHeadNameColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().a_LHeadNameName(), "Particular", 500, "");
        a_LHeadNameColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getA_LChildSubCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal;' > " + DmShared.xNull(plm.getA_LChildSubName()) + " </div>";
                } else if (DmShared.iNull(plm.getA_LChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal; font-weight:bold;' > " + DmShared.xNull(plm.getA_LChildGroupName()) + " </div>";
                } else {
                    valHtml = "<div style='font-weight:bold;' >" + DmShared.xNull(plm.getA_LHeadNameName()) + "</div>";
                }

                if (DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("AS")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                    valHtml = "<div style='font-weight:bold; font-size:12px' > " + DmShared.xNull(plm.getA_LHeadNameName()) + " </div>";
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        a_LBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().a_LChildBalance(), "", 170, "");
        a_LBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        a_LBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getA_LChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(plm.getA_LChildBalance());
                } else if (DmShared.iNull(plm.getA_LChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getA_LChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(plm.getA_LChildBalance());
                }

                
                if (DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("AS")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                    valHtml = "<div style='font-weight:bold; font-size:12px' > " + DmShared.xNull(plm.getA_LChildBalance()) + " </div>";
                }
                
                sb.appendHtmlConstant(valHtml);
            }
        });

        a_LAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().a_LAmount(), "Amount", 170, "");
        a_LAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        a_LAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getA_LAmount()) + " </div>";

                if (DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("AS")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                    valHtml = "<div style='font-weight:bold; font-size:12px' > " + DmShared.xNull(plm.getA_LAmount()) + " </div>";
                }
                
                sb.appendHtmlConstant(valHtml);
            }
        });
        a_LDrCrColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().a_LDrCr(), "", 40, "");
        a_LDrCrColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getA_LDrCr()) + " </div>";

                if (DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("AS")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                    valHtml = "<div style='font-weight:bold; font-size:12px' > " + DmShared.xNull(plm.getA_LDrCr()) + " </div>";
                }
                
                sb.appendHtmlConstant(valHtml);
            }
        });

///*/*/**/**/*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
        lstA_LBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().lstA_LChildBalance(), "", 170, "");
        lstA_LBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lstA_LBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getA_LChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(plm.getA_LChildBalance());
                } else if (DmShared.iNull(plm.getA_LChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getLstA_LChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(plm.getA_LChildBalance());
                }

                if (DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("AS")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                    valHtml = "<div style='font-weight:bold; font-size:12px' > " + DmShared.xNull(plm.getLstA_LChildBalance()) + " </div>";
                }
                
                sb.appendHtmlConstant(valHtml);
            }
        });

        lstA_LAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().lstA_LAmount(), "Amount", 170, "");
        lstA_LAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lstA_LAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getLstA_LAmount()) + " </div>";

                if (DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("AS")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                    valHtml = "<div style='font-weight:bold; font-size:12px' > " + DmShared.xNull(plm.getLstA_LAmount()) + " </div>";
                }
                
                sb.appendHtmlConstant(valHtml);
            }
        });

        lstA_LDrCrColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().lstA_LDrCr(), "", 40, "");
        lstA_LDrCrColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheetDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getLstA_LDrCr()) + " </div>";

                if (DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("AS")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NL")
                        || DmShared.xNull(plm.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                    valHtml = "<div style='font-weight:bold; font-size:12px' > " + DmShared.xNull(plm.getLstA_LDrCr()) + " </div>";
                }
                
                sb.appendHtmlConstant(valHtml);
            }
        });

        grdDisplay.initializeGrid(grdDisplay.getPropertyAccess().fieldHashCode(), 1283, 485);

        grdDisplay.getGrid().getView().setViewConfig(new GridViewConfig<BalanceSheetDisplayCompare_Mdl>() {

            @Override
            public String getColStyle(BalanceSheetDisplayCompare_Mdl model, ValueProvider<? super BalanceSheetDisplayCompare_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";

                if (DmShared.xNull(model.getA_LHeadCode()).trim().equalsIgnoreCase("TT")) {
                    styleClass += "dm-colorWhite dm-backGroundGray dm-fontWeightBold ";
                } else {

                    if (DmShared.xNull(model.getA_LHeadCode()).trim().equalsIgnoreCase("EL")
                            || DmShared.xNull(model.getA_LHeadCode()).trim().equalsIgnoreCase("AS")) {
                        styleClass += "dm-colorBlue ";
                    } else if (DmShared.xNull(model.getA_LHeadCode()).trim().equalsIgnoreCase("NL")) {
                        styleClass += "dm-colorRed ";
                    } else if (DmShared.xNull(model.getA_LHeadCode()).trim().equalsIgnoreCase("NP")) {
                        styleClass += "dm-colorRed ";
                    }
                    if (DmShared.xNull(model.getA_LHeadNameName()).trim().equalsIgnoreCase("Net Profit/Loss")) {
                        if ((colIndex == 2 || colIndex == 3) && DmShared.xNull(model.getA_LDrCr()).trim().equalsIgnoreCase("NL")) {
                            styleClass += "dm-colorRed ";
                        }
                        if ((colIndex == 2 || colIndex == 3) && DmShared.xNull(model.getA_LDrCr()).trim().equalsIgnoreCase("NP")) {
                            styleClass += "dm-colorGreen ";
                        }
                        if ((colIndex == 5 || colIndex == 6) && DmShared.xNull(model.getLstA_LDrCr()).trim().equalsIgnoreCase("NL")) {
                            styleClass += "dm-colorRed ";
                        }
                        if ((colIndex == 5 || colIndex == 6) && DmShared.xNull(model.getLstA_LDrCr()).trim().equalsIgnoreCase("NP")) {
                            styleClass += "dm-colorGreen ";
                        }
                    }

                }

                return styleClass;
            }

            @Override
            public String getRowStyle(BalanceSheetDisplayCompare_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdDisplay.getGrid();
    }
    //</editor-fold>

    public BalanceSheetDisplayCompare_Dsg() {
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

        flw = new FlowPanel();
        DmClient.setStyle(flw, "525px", null, "left", null, DmPosition.NONE, true);
        flw.setStyleName("dm-StripLabel");
        lblDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblDate.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        lblDate.setHeight("20px");
        lblDate.setWidth("200px");
        flw.add(lblDate);
        
        flw.setStyleName("dm-StripLabel");
        lblLstDate.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblLstDate.getElement().getStyle().setFontSize(11, Style.Unit.PX);
        lblLstDate.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        lblLstDate.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lblLstDate.setHeight("20px");
        lblLstDate.setWidth("200px");
        flw.add(lblLstDate);

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
