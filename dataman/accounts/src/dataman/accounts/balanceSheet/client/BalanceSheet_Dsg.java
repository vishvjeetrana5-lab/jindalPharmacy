package dataman.accounts.balanceSheet.client;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import dataman.accounts.balanceSheet.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;

abstract class BalanceSheet_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().BALANCE_SHEET_DISPLAY.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnSetting = new Button();
    final Button btnBack = new Button();
    final Button btnPrint = new Button();
    final DmLabel lblHeading = new DmLabel("");
    final DmLabel lblDate = new DmLabel("");
    final DmLabel lblLiabilitiesAmount = new DmLabel("");
    final DmLabel lblAssestsAmount = new DmLabel("");
    final DmLabel lblBalance = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="grdBalanceSheet">
    interface GrdDisplay_PrpAcs extends KeyFieldHashCode_T_PrpAcs<BalanceSheet_Mdl> {

        @Editor.Path("LiabilityName")
        ValueProvider<BalanceSheet_Mdl, String> liabilityName();

        @Editor.Path("LiabilityChildBalance")
        ValueProvider<BalanceSheet_Mdl, String> liabilityChildBalance();

        @Editor.Path("LiabilityAmount")
        ValueProvider<BalanceSheet_Mdl, String> liabilityAmount();

        @Editor.Path("AssetName")
        ValueProvider<BalanceSheet_Mdl, String> assetName();

        @Editor.Path("AssetChildBalance")
        ValueProvider<BalanceSheet_Mdl, String> assetChildBalance();

        @Editor.Path("AssetAmount")
        ValueProvider<BalanceSheet_Mdl, String> assetAmount();
    }

    final DmGrid<BalanceSheet_Mdl, GrdDisplay_PrpAcs> grdDisplay = new DmGrid(GWT.create(GrdDisplay_PrpAcs.class));

    private Grid iniGrdDisplay() {
        ColumnConfig<BalanceSheet_Mdl, String> liabilityNameColumn, liabilityBalanceColumn, liabilityAmountColumn,
                assetsNameColumn, assetsBalanceColumn, assetsAmountColumn;

        liabilityNameColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().liabilityName(), "Liabilities", 343, "");
        liabilityNameColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheet_Mdl bsm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                if (DmShared.iNull(bsm.getLiabilityChildSubCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal;' > " + DmShared.xNull(bsm.getLiabilityChildSubName()) + " </div>";
                } else if (DmShared.iNull(bsm.getLiabilityChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal; font-weight:bold;' > " + DmShared.xNull(bsm.getLiabilityChildGroupName()) + " </div>";
                } else {
                    valHtml = "<div style='font-weight:bold;' >" + DmShared.xNull(bsm.getLiabilityName()) + "</div>";
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        liabilityBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().liabilityChildBalance(), "", 150, "");
        liabilityBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        liabilityBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheet_Mdl bsm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                if (DmShared.iNull(bsm.getLiabilityChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(bsm.getLiabilityChildBalance());
                } else if (DmShared.iNull(bsm.getLiabilityChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(bsm.getLiabilityChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(bsm.getLiabilityChildBalance());
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        liabilityAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().liabilityAmount(), "Amount", 150, "");
        liabilityAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        liabilityAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheet_Mdl bsm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(bsm.getLiabilityAmount()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });

        assetsNameColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().assetName(), "Assets", 343, "");
        assetsNameColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheet_Mdl bsm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                if (DmShared.iNull(bsm.getAssetChildSubCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal;' > " + DmShared.xNull(bsm.getAssetChildSubName()) + " </div>";
                } else if (DmShared.iNull(bsm.getAssetChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='width:90%; float:right; white-space:normal; font-weight:bold;' > " + DmShared.xNull(bsm.getAssetChildGroupName()) + " </div>";
                } else {
                    valHtml = "<div style='font-weight:bold;' >" + DmShared.xNull(bsm.getAssetName()) + "</div>";
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        assetsBalanceColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().assetChildBalance(), "", 150, "");
        assetsBalanceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        assetsBalanceColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheet_Mdl bsm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                if (DmShared.iNull(bsm.getAssetChildSubCode()).longValue() != 0) {
                    valHtml = DmShared.xNull(bsm.getAssetChildBalance());
                } else if (DmShared.iNull(bsm.getAssetChildGroupCode()).longValue() != 0) {
                    valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(bsm.getAssetChildBalance()) + " </div>";
                } else {
                    valHtml = DmShared.xNull(bsm.getAssetChildBalance());
                }

                sb.appendHtmlConstant(valHtml);
            }
        });

        assetsAmountColumn = grdDisplay.addTextColumn(grdDisplay.getPropertyAccess().assetAmount(), "Amount", 150, "");
        assetsAmountColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        assetsAmountColumn.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                BalanceSheet_Mdl bsm = grdDisplay.getGrid().getStore().get(context.getIndex());
                String valHtml = "";

                valHtml = "<div style='font-weight:bold;' > " + DmShared.xNull(bsm.getAssetAmount()) + " </div>";

                sb.appendHtmlConstant(valHtml);
            }
        });

        grdDisplay.initializeGrid(grdDisplay.getPropertyAccess().fieldHashCode(), 1290, 465);

        grdDisplay.getGrid().getView().setViewConfig(new GridViewConfig<BalanceSheet_Mdl>() {

            @Override
            public String getColStyle(BalanceSheet_Mdl model, ValueProvider<? super BalanceSheet_Mdl, ?> valueProvider, int rowIndex, int colIndex) {
                String styleClass = " ";

                if (colIndex == 0 || colIndex == 2) {
                    if (DmShared.xNull(model.getLiabilityCode()).trim().equalsIgnoreCase("NP")) {
                        styleClass += "dm-colorGreen ";
                        styleClass += "dm-fontWeightBold ";
                    }
                }

                if (colIndex == 3 || colIndex == 5) {
                    if (DmShared.xNull(model.getAssetCode()).trim().equalsIgnoreCase("NL")) {
                        styleClass += "dm-colorRed ";
                        styleClass += "dm-fontWeightBold ";
                    }
                }

                return styleClass;
            }

            @Override
            public String getRowStyle(BalanceSheet_Mdl model, int rowIndex) {
                return "";
            }
        });

        return grdDisplay.getGrid();
    }
    //</editor-fold>

    public BalanceSheet_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight("524px");

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
        FlowPanel flw;

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-filter");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-print");
        btnPrint.setTitle("Print");
        btnPrint.addStyleName("dm-top-control-btnStyle");
        btnPrint.getElement().appendChild(aBtn.getElement());

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-caret-left");
        btnBack.setTitle("Back");
        btnBack.addStyleName("dm-top-control-btnStyle");
        btnBack.getElement().appendChild(aBtn.getElement());

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
        DmClient.setStyle(flwTable, "100%", "499px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "465px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdDisplay()));
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "35px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "470px", "35px", "left", null, DmPosition.NONE, true);
        lblBalance.getElement().setAttribute("style", "width: 330px !important; height: 15px; color: red; margin-top: 3.5%; font-weight: bold;");
        flwTd.add(lblBalance);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "297px", "35px", "left", null, DmPosition.NONE, true);
        lblLiabilitiesAmount.getElement().setAttribute("style", " text-align: right; width: 150px !important; height: 15px; margin-top: 5%; font-weight: bold;");
        flwTd.add(lblLiabilitiesAmount);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "516px", "35px", "left", null, DmPosition.NONE, true);
        lblAssestsAmount.setHeight("25px");
        lblAssestsAmount.getElement().setAttribute("style", " text-align: right; width: 495px !important; height: 15px; margin-top: 2.5%; font-weight: bold;");
        flwTd.add(lblAssestsAmount);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
