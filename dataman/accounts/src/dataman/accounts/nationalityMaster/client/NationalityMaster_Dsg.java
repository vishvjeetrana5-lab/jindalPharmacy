package dataman.accounts.nationalityMaster.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.accounts.nationalityMaster.shared.Find_Mdl;
import dataman.accounts.nationalityMaster.shared.TxtHCountryLocale_Mdl;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class NationalityMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().NATIONALITY_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final DmUserControl userControl = new DmUserControl();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtNationality = new DmSuggestBox(20, 7, true, "Name", DmDataType.STRING);

    final MenuItem mnuExportCard = new MenuItem("Export Nationality");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHCountryLocale">   
    interface CountryLocale_PrpAcs extends KeyField_T_PrpAcs<TxtHCountryLocale_Mdl> {

        @Editor.Path("countryName")
        ValueProvider<TxtHCountryLocale_Mdl, String> countryName();

        @Editor.Path("currencyName")
        ValueProvider<TxtHCountryLocale_Mdl, String> currencyName();
    }

    final DmTextHelp<TxtHCountryLocale_Mdl, CountryLocale_PrpAcs> txtHCountryLocale = new DmTextHelp(GWT.create(CountryLocale_PrpAcs.class), 4, true);

    private void iniTxtHCountryLocale() {
        txtHCountryLocale.getDmGrid().addTextColumn(txtHCountryLocale.getDmGrid().getPropertyAccess().name(), "Locale Name", 100, "");
        txtHCountryLocale.getDmGrid().addTextColumn(txtHCountryLocale.getDmGrid().getPropertyAccess().countryName(), "Country Name ", 150, "");
        txtHCountryLocale.getDmGrid().addTextColumn(txtHCountryLocale.getDmGrid().getPropertyAccess().currencyName(), "Currency Name", 150, "");
        txtHCountryLocale.getDmGrid().initializeGrid(txtHCountryLocale.getDmGrid().getPropertyAccess().code(), 420, 0);
        txtHCountryLocale.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Locale")
        ValueProvider<Find_Mdl, String> locale();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 8);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Nationality", 1, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().locale(), "Locale", 400, "countryLocale");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);
        dmFind.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    public NationalityMaster_Dsg() {
        ini();
        iniMenu();
        iniTxtHCountryLocale();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "60px", null, null, DmPosition.CENTER, true);


        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nationality");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        txtNationality.setMandatory(true);
        flwTD.add(vl.add(txtNationality));

        flwTr.add(flwTD);
        flwTable.add(flwTr);

        //Country Locale
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Country Locale");
        flwTD.add(lbl);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCountryLocale));
        flwTr.add(flwTD);

        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
