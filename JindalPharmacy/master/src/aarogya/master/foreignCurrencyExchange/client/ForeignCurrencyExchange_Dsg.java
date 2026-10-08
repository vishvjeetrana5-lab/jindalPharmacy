package aarogya.master.foreignCurrencyExchange.client;

import aarogya.master.foreignCurrencyExchange.shared.GrdForeignCurrencyExchange_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class ForeignCurrencyExchange_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().FOREIGN_CURRENCY_EXCHANGE.caption;
    public final static String modifiedByBlog = "Modified By & Date";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtExchangeDate = new DmTextBox(11, true);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface find_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

    }
    final DmFind<KeyFieldExtended, find_PrpAcs> dmFind = new DmFind(GWT.create(find_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Exchange Rate For Date", 150, "exchangeDate");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 250, 1);

        dmFind.getHelper().addSort("exchangeDate", DmSortType.DESC);
    }
    //</editor-fold>

    public ForeignCurrencyExchange_Dsg() {

        ini();
        iniFind();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);

        userControl.addGap(40);
        userControl.addTextBlog(modifiedByBlog, 200, true, true);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "517px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "22px", "left", null, DmPosition.NONE, false);
        flwTr.add(topArea());
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "450px", "left", null, DmPosition.NONE, false);
        flwTr.add(vl.add(iniGrdForeignCurrency()));
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;

    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "320px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Exchange Rate For Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExchangeDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;

    }

    //<editor-fold defaultstate="collapsed" desc="ForeignCurrencyGrid">
    interface GrdForeignCurrency_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdForeignCurrencyExchange_Mdl> {

        @Editor.Path("Base")
        ValueProvider<GrdForeignCurrencyExchange_Mdl, String> base();

        @Editor.Path("Rate")
        ValueProvider<GrdForeignCurrencyExchange_Mdl, String> rate();
    }
    final DmGrid<GrdForeignCurrencyExchange_Mdl, GrdForeignCurrency_PrpAcs> grdForeignCurrency = new DmGrid(GWT.create(GrdForeignCurrency_PrpAcs.class));

    private Grid iniGrdForeignCurrency() {
        grdForeignCurrency.addRowNumberColumn();
        grdForeignCurrency.addTextColumn(grdForeignCurrency.getPropertyAccess().name(), "Currency", 200, "");
        grdForeignCurrency.addTextColumn(grdForeignCurrency.getPropertyAccess().rate(), "Rate", 300, "");
        grdForeignCurrency.addTextColumn(grdForeignCurrency.getPropertyAccess().base(), "Base", 100, "");
        grdForeignCurrency.initializeGrid(grdForeignCurrency.getPropertyAccess().fieldHashCode(), 645, 450, 2);

        return grdForeignCurrency.getGrid();
    }

    //</editor-fold>
}
