package dataman.dmToolkit.partyDetailPopup.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.partyDetailPopup.shared.*;

public class PartyDetailPopup_Dsg {

    public static final String widgetName = "Party Detail";
    final PopupPanel pnlPopUp = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnOk = new Button();
    final Button btnCancel = new Button();
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwCancel = new FlowPanel();

    final DmTextBox txtPartyName = new DmTextBox(100, true);
    final DmTextBox txtDisplayName = new DmTextBox(100, true);
    final DmTextBox txtLegalName = new DmTextBox(100, false);
    final DmTextBox txtTradeName = new DmTextBox(100, false);
    final DmTextBox txtGstin = new DmTextBox(15, false);
    final DmTextBox txtTransporterId = new DmTextBox(15, false);
    final DmTextBox txtGstinDate = new DmTextBox(11, false);
    final DmTextBox txtAddress1 = new DmTextBox(100, false);
    final DmTextBox txtAddress2 = new DmTextBox(100, false);
    final DmTextBox txtPhoneNo = new DmTextBox(35, false);
    final DmTextBoxMobile txtMobileNo = new DmTextBoxMobile(false);
    final DmTextBox txtEMail = new DmTextBox(250, false);
    final DmTextBoxNumeric txtPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtDistrict = new DmTextBox(100, false);
    final DmTextBox txtState = new DmTextBox(100, false);
    final DmTextBox txtStateNumericCode = new DmTextBox(100, false);
    final DmTextBox txtCountry = new DmTextBox(100, false);
    final DmTextBox txtGstCountryCode = new DmTextBox(100, false);
    final DmTextBox txtPAN = new DmTextBox(11, false);
    final DmTextBox txtTAN = new DmTextBox(11, false);
    final FlowPanel flwTopContainer = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    interface TxtHCity_Mdl_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

        @Editor.Path("DistrictName")
        ValueProvider<TxtHCity_Mdl, String> districtName();

    }

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHCity = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().districtName(), "District", 100, "districtName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHAdPlaceOfBusiness">
    interface TxtHAdPlaceOfBusiness_Mdl_PrpAcs extends KeyField_T_PrpAcs<TxtHAdPlaceOfBusiness_Mdl> {
        
        @Editor.Path("address1")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> address1();
        
        @Editor.Path("address2")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> address2();
        
        @Editor.Path("cityName")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> cityName();
        
        @Editor.Path("stateName")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> stateName();
        
        @Editor.Path("countryName")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> countryName();
        
        @Editor.Path("pin")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> pin();
        
        @Editor.Path("email")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> email();
        
        @Editor.Path("mobile")
        ValueProvider<TxtHAdPlaceOfBusiness_Mdl, String> mobile();

    }

    final DmTextHelp<TxtHAdPlaceOfBusiness_Mdl, TxtHAdPlaceOfBusiness_Mdl_PrpAcs> txtHAdPlaceOfBusiness = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHAdPlaceOfBusiness_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHAdPlaceOfBusiness() {
        txtHAdPlaceOfBusiness.getDmGrid().addTextColumn(txtHAdPlaceOfBusiness.getDmGrid().getPropertyAccess().name(), "Place", 250, "name");
        txtHAdPlaceOfBusiness.getDmGrid().addTextColumn(txtHAdPlaceOfBusiness.getDmGrid().getPropertyAccess().cityName(), "City", 150, "city");
        txtHAdPlaceOfBusiness.getDmGrid().addTextColumn(txtHAdPlaceOfBusiness.getDmGrid().getPropertyAccess().stateName(), "State", 150, "state");
        txtHAdPlaceOfBusiness.getDmGrid().addTextColumn(txtHAdPlaceOfBusiness.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "country");
        txtHAdPlaceOfBusiness.getDmGrid().addTextColumn(txtHAdPlaceOfBusiness.getDmGrid().getPropertyAccess().pin(), "Pin", 60, "pin");
        txtHAdPlaceOfBusiness.getDmGrid().initializeGrid(txtHAdPlaceOfBusiness.getDmGrid().getPropertyAccess().code(), 820, 250);

        txtHAdPlaceOfBusiness.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public PartyDetailPopup_Dsg() {
        ini();
        iniTxtHCity();
        iniTxtHAdPlaceOfBusiness();
    }

    private void ini() {
        VerticalPanel vrtPanel = new VerticalPanel();
        DmClient.setStyle(vrtPanel, "100%", null, null, null, DmPosition.NONE, true);
        pnlPopUp.setWidth("500px");

        FlowPanel flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");

        vrtPanel.add(iniToolContainer());

        flw.add(workingArea());
        vrtPanel.add(flw);
        vrtPanel.setCellHeight(flw, "100%");
        pnlPopUp.add(vrtPanel);
    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok: (ALT + K)");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        DmClient.setStyle(btnOk, "3%", null, "left", null, DmPosition.NONE, false);
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        DmClient.setStyle(btnCancel, "3%", null, "left", null, DmPosition.NONE, false);
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        flwTopContainer.setWidth("100%");
        DmClient.setStyle(flwTopContainer, "100%", null, null, null, DmPosition.NONE, true);
        toolContainer.addWidget(flwTopContainer);
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTabel, flwTr, flwTd;
        DmLabel lbl;

        flwTabel = new FlowPanel();
        DmClient.setStyle(flwTabel, "99%", null, null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPartyName));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Display Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);


        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Legal Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLegalName));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Trade Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTradeName));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);
        
        
        
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GSTIN");
        flwTd.add(lbl);
        flwTr.add(flwTd);  
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);  
        
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GSTIN Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
         flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "37%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Transporter Id");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTabel.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGstin));
        flwTr.add(flwTd);  
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);  
        
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "16%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGstinDate));
        flwTr.add(flwTd);
        
         flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "3%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtTransporterId));
        flwTr.add(flwTd);
        
        flwTabel.add(flwTr);
        
         flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Additional Place Of Business");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAdPlaceOfBusiness));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress1));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress2));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("District");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Pincode");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtDistrict));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtPinCode));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("State");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Country");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtState));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.add(vl.add(txtStateNumericCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "39%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtCountry));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtGstCountryCode));
        flwTr.add(flwTd);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("Phone");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobileNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtPhoneNo));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("E-mail");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEMail));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("PAN");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        lbl = new DmLabel("TAN");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPAN));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(vl.add(txtTAN));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTabel.add(flwTr);

        return flwTabel;
    }
}
