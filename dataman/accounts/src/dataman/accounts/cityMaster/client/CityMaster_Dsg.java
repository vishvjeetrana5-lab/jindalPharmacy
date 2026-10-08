package dataman.accounts.cityMaster.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.accounts.cityMaster.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class CityMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().CITY_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtCityName = new DmSuggestBox(0, 10, false, "CityName", DmDataType.STRING);
    final DmTextBox txtSTDCode = new DmTextBox();

    final MenuItem mnuExportCity = new MenuItem("Export City");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="TxtHState">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHState = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHState() {
        txtHState.getDmGrid().addTextColumn(txtHState.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHState.getDmGrid().initializeGrid(txtHState.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHState.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="TxtHCountry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCountry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true);

    private void iniTxtHCountry() {
        txtHCountry.getDmGrid().addTextColumn(txtHCountry.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHCountry.getDmGrid().initializeGrid(txtHCountry.getDmGrid().getPropertyAccess().code(), 100, 0, 1);

        txtHCountry.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>    

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("StateText")
        ValueProvider<Find_Mdl, String> stateName();

        @Editor.Path("StdCode")
        ValueProvider<Find_Mdl, String> stdCode();

        @Editor.Path("CountryText")
        ValueProvider<Find_Mdl, String> countryCode();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "City Name", 1, "cityName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().stateName(), "State Name", 200, "stateName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().stdCode(), "STD Code", 200, "stdCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().countryCode(), "Country", 200, "countryName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);
        dmFind.getHelper().addSort("cityName", DmSortType.ASC);
        dmFind.getHelper().addSort("stateName", DmSortType.ASC);
        dmFind.getHelper().addSort("countryName", DmSortType.ASC);
    }
//</editor-fold>

    public CityMaster_Dsg() {
        ini();
        iniTxtHState();
        iniTxtHCountry();
        iniMenu();
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
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "45%", "80px", null, null, DmPosition.CENTER, true);

        //City Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtCityName.setMandatory(true);
        flwTd.add(vl.add(txtCityName));
        txtCityName.setMaxLength(50);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //State Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("State Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHState));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //STD COde
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("STD Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtSTDCode.setMaxLength(15);
        flwTd.add(vl.add(txtSTDCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        ////////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Country Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCountry));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCity);
        return menu;
    }
}
