package aarogya.pharmacy.additionalPlaceOfBusiness.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.Find_Mdl;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.TxtHCity_Mdl;
import aarogya.pharmacy.additionalPlaceOfBusiness.shared.TxtHLinkWithAccount_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmAccounts;

abstract class AdditionalPlaceOfBusiness_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().ADDITIONAL_PLACE_OF_BUSINESS.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmTextBox txtAddress1 = new DmTextBox(50, false);
    final DmTextBox txtAddress2 = new DmTextBox(50, false);
    final DmTextBoxNumeric txtPinNo = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtState = new DmTextBox(100, false);
    final DmTextBox txtCountry = new DmTextBox(100, false);
    final DmTextBox txtEmail = new DmTextBox(250, false);
    final DmTextBox txtPanNo = new DmTextBox(10, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmSuggestBox txtPlace = new DmSuggestBox(50, 10, true, "place", DmEnum.DmDataType.STRING);
    final DmTextBoxNumeric txtIsdCode = new DmTextBoxNumeric(0, false, 5, false);

    //<editor-fold defaultstate="collapsed" desc="TxtHLinkWithAccount">
    interface txtHLinkWithAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHLinkWithAccount_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHLinkWithAccount_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHLinkWithAccount_Mdl, String> manualCode();
    }
    final DmTextHelp<TxtHLinkWithAccount_Mdl, txtHLinkWithAccount_PrpAcs> txtHAccount = new DmTextHelp(GWT.create(txtHLinkWithAccount_PrpAcs.class), 7, true);

    private void iniLinkAccount() {
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().name(), "Name", 240, "NAME");
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 103, "manualCode");
        txtHAccount.getDmGrid().addTextColumn(txtHAccount.getDmGrid().getPropertyAccess().cityName(), "City", 114, "city");
        txtHAccount.getDmGrid().initializeGrid(txtHAccount.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHAccount.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHAccount.getHelper().addSort("manualCode", DmEnum.DmSortType.ASC);
        txtHAccount.getHelper().addSort("city", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    interface TxtHCity_Mdl_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

    }

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 350, 0, 1);

        txtHCity.getHelper().addSort("CityName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyFieldHashCode_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Place")
        ValueProvider<Find_Mdl, String> place();

        @Editor.Path("Address1")
        ValueProvider<Find_Mdl, String> address1();

        @Editor.Path("Address2")
        ValueProvider<Find_Mdl, String> address2();

        @Editor.Path("City")
        ValueProvider<Find_Mdl, String> city();

        @Editor.Path("Mobile")
        ValueProvider<Find_Mdl, String> mobile();

        @Editor.Path("Pin")
        ValueProvider<Find_Mdl, String> pin();

        @Editor.Path("Email")
        ValueProvider<Find_Mdl, String> eMail();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 8, true);

    private void iniFind() {

        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().place(), "Place", 130, "place");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Link With A/c", 200, "accountName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().address1(), "Address 1", 175, "add1");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().address2(), "Address 2", 175, "add2");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().city(), "City", 150, "cityCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 100, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().pin(), "Pin", 80, "pin");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().eMail(), "Email", 150, "eMail");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().fieldHashCode(), 1260, 0);
        dmFind.getHelper().addSort("accountName", DmEnum.DmSortType.ASC);
        dmFind.getHelper().addSort("sNo", DmEnum.DmSortType.ASC, "sNo");

    }
    //</editor-fold>

    public AdditionalPlaceOfBusiness_Dsg() {

        ini();
        iniTxtHCity();
        iniFind();
        iniLinkAccount();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        flwForm.addStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "65%", null, null, null, DmEnum.DmPosition.CENTER_HORIZONTAL, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTR.getElement().getStyle().setMarginTop(40, Style.Unit.PX);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Link With A/c");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHAccount));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Place");
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(vl.add(txtPlace));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        //Address
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        lbl = new DmLabel("Address");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(vl.add(txtAddress1));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        /////////////////////////// Address2 /////////////////////////////////////////
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "85%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(vl.add(txtAddress2));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        // City , PIN,state,country
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        lbl = new DmLabel("City");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(vl.add(txtHCity));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "6%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("PIN");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "10%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(vl.add(txtPinNo));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("State");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "17%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(vl.add(txtState));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Country");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "16%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTD.add(vl.add(txtCountry));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        // Email and phone No
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "61%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtEmail));
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "8%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Mobile No.");
        lbl.getElement().getStyle().setMarginLeft(8, Style.Unit.PX);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtIsdCode));
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);

        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtMobile));
        flwTD.getElement().getStyle().setMarginTop(1, Style.Unit.PX);
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
