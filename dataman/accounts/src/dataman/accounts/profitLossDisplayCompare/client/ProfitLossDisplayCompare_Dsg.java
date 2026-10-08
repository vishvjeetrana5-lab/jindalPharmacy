package dataman.accounts.profitLossDisplayCompare.client;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.accounts.profitLossDisplayCompare.shared.ProfitLossDisplayCompare_Mdl;
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

abstract class ProfitLossDisplayCompare_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().PROFIT_AND_LOSS_COMPARE_DISPLAY.caption;
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

    //<editor-fold defaultstate="collapsed" desc="grdProfitLoss">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<ProfitLossDisplayCompare_Mdl> {

        @Editor.Path("E_RHeadNameName")
        ValueProvider<ProfitLossDisplayCompare_Mdl, String> e_RHeadNameName();

        @Editor.Path("E_RChildBalance")
        ValueProvider<ProfitLossDisplayCompare_Mdl, String> e_RChildBalance();

        @Editor.Path("E_RAmount")
        ValueProvider<ProfitLossDisplayCompare_Mdl, String> e_RAmount();

        @Editor.Path("E_RDrCr")
        ValueProvider<ProfitLossDisplayCompare_Mdl, String> e_RDrCr();

        @Editor.Path("LstE_RChildBalance")
        ValueProvider<ProfitLossDisplayCompare_Mdl, String> lstE_RChildBalance();

        @Editor.Path("LstE_RAmount")
        ValueProvider<ProfitLossDisplayCompare_Mdl, String> lstE_RAmount();

        @Editor.Path("LstE_RDrCr")
        ValueProvider<ProfitLossDisplayCompare_Mdl, String> lstE_RDrCr();
    }

    final DmGrid<ProfitLossDisplayCompare_Mdl, GrdDisplay_PrpAcs> grdDisplay = new DmGrid(GWT.create(GrdDisplay_PrpAcs.class));

    private Grid iniGrdDisplay() {
        ColumnConfig<ProfitLossDisplayCompare_Mdl, String> e_RHeadNameColumn, e_RBalanceColumn, e_RAmountColumn,
                lstE_RBalanceColumn, lstE_RAmountColumn,e_RDrCrColumn,lstE_RDrCrColumn;

        e_RHeadNameColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().e_RHeadNameName(), "Particular", 500, "");
        e_RHeadNameColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getE_RChildSubCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal;' > " + DmShared.xNull(plm.getE_RChildSubName()) + " </div>";
                } else if (DmShared.iNull(plm.getE_RChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal; font-weight:bold;' > " + DmShared.xNull(plm.getE_RChildGroupName()) + " </div>";
                } else {
                    valHtml = "<div style='font-weight:bold;' >" + DmShared.xNull(plm.getE_RHeadNameName()) + "</div>";
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        e_RBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().e_RChildBalance(), "", 170, "");
        e_RBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        e_RBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getE_RChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(plm.getE_RChildBalance());
                } else if (DmShared.iNull(plm.getE_RChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getE_RChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(plm.getE_RChildBalance());
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        e_RAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().e_RAmount(), "Amount", 170, "");
        e_RAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        e_RAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getE_RAmount()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });
        e_RDrCrColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().e_RDrCr(), "", 40, "");
        e_RDrCrColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getE_RDrCr()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });
        
///*/*/**/**/*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/**/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
        lstE_RBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().lstE_RChildBalance(), "", 170, "");
        lstE_RBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lstE_RBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                if (DmShared.iNull(plm.getE_RChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(plm.getE_RChildBalance());
                } else if (DmShared.iNull(plm.getE_RChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getLstE_RChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(plm.getE_RChildBalance());
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        lstE_RAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().lstE_RAmount(), "Amount", 170, "");
        lstE_RAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        lstE_RAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getLstE_RAmount()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });
        
        lstE_RDrCrColumn=grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().lstE_RDrCr(), "", 40, "");
        lstE_RDrCrColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                ProfitLossDisplayCompare_Mdl plm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml;

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(plm.getLstE_RDrCr()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });
        
        grdDisplay.initializeGrid(grdDisplay.getPropertyAccess().fieldHashCode(), 1283, 485);

        grdDisplay.getGrid().getView().setViewConfig(new GridViewConfig<ProfitLossDisplayCompare_Mdl>() {

            @Override
            public String getColStyle(ProfitLossDisplayCompare_Mdl model, ValueProvider<? super ProfitLossDisplayCompare_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";

                if (DmShared.xNull(model.getE_RHeadCode()).trim().equalsIgnoreCase("TT")) {
                    styleClass += "dm-colorWhite dm-backGroundGray dm-fontWeightBold ";
                } else {

                    if (DmShared.xNull(model.getE_RHeadCode()).trim().equalsIgnoreCase("RV")
                            || DmShared.xNull(model.getE_RHeadCode()).trim().equalsIgnoreCase("EX")) {
                        styleClass += "dm-colorBlue ";
                    } else if (DmShared.xNull(model.getE_RHeadCode()).trim().equalsIgnoreCase("NP")) {
                        styleClass += "dm-colorGreen ";
                    }else if (DmShared.xNull(model.getE_RHeadCode()).trim().equalsIgnoreCase("NL")) {
                        styleClass += "dm-colorRed ";
                    }
                }

                return styleClass;
            }

            @Override
            public String getRowStyle(ProfitLossDisplayCompare_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdDisplay.getGrid();
    }
    //</editor-fold>

    public ProfitLossDisplayCompare_Dsg() {
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
