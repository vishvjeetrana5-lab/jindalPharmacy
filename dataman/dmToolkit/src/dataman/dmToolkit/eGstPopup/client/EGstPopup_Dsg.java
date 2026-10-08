package dataman.dmToolkit.eGstPopup.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.eGstPopup.shared.TxtHCity_Mdl;
import dataman.dmToolkit.eGstPopup.shared.TxtHTranporter_Mdl;

abstract class EGstPopup_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTextBox txtLrNo = new DmTextBox();
    final DmTextBox txtLrDate = new DmTextBox();
    final DmTextBox txtTransporterId = new DmTextBox();
    final DmTextBox txtTransporterGstinDate = new DmTextBox(11, false);
    final DmTextBox txtVehicleNo = new DmTextBox();
    final DmTextBox txtSubSupplyDesc = new DmTextBox(20, false);
    final DmTextBoxNumeric txtDistance = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBox txtEWayDate = new DmTextBox();
    final DmTextBox txtEWayValidUpto = new DmTextBox();
    final DmRemarkTextBox txtEWayMsg = new DmRemarkTextBox();
    final DmTextBox txtEInvAckNo = new DmTextBox();
    final DmTextBox txtEInvAckDate = new DmTextBox();
    final DmTextBox txtEInvIrnNo = new DmTextBox();
    final DmTextBox txtEInvStatus = new DmTextBox();
    final DmTextBox txtEWayBillNo = new DmTextBox(50, false);
    final TabLayoutPanel tabPanel = new TabLayoutPanel(1.6, Style.Unit.EM);
    final FlowPanel flwQuickLink = new FlowPanel();
    private final String buttonStyleName = "dm-quick-text";
    private final String buttonStyleAttr = "white-space: nowrap; width: auto; display: inline-block; padding: 7px 10px; background-color: rgb(81, 99, 149); color: rgb(255, 255, 255); font-weight: bold; border-radius: 4px 4px 4px 4px; margin-right: 10px; margin-top:10px; ";

    interface TxtHCity_Mdl_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

        @Editor.Path("DistrictName")
        ValueProvider<TxtHCity_Mdl, String> districtName();

    }

    //<editor-fold defaultstate="collapsed" desc="Token">
    final DmTextBox txtAuthToken = new DmTextBox();
    final DmTextBox txtTokenExpiryAt = new DmTextBox();
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Consigner">
    final DmTextBox txtConsignerName = new DmTextBox();
    final DmTextBox txtConsignerGstin = new DmTextBox();
    final DmTextBox txtConsignerGstinDate = new DmTextBox();
    final DmTextBox txtConsignerLegalName = new DmTextBox();
    final DmTextBox txtConsignerTradeName = new DmTextBox();
    final DmTextBox txtConsignerAddress1 = new DmTextBox();
    final DmTextBox txtConsignerAddress2 = new DmTextBox();
    final DmTextBox txtConsignerState = new DmTextBox();
    final DmTextBox txtConsignerPhone = new DmTextBox();
    final DmTextBox txtConsignerEmail = new DmTextBox();
    final DmTextBoxNumeric txtConsignerPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtConsignerCountry = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHConsignerCity">
    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHConsignerCity = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHConsignerCity() {
        txtHConsignerCity.getDmGrid().addTextColumn(txtHConsignerCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHConsignerCity.getDmGrid().addTextColumn(txtHConsignerCity.getDmGrid().getPropertyAccess().districtName(), "District", 100, "districtName");
        txtHConsignerCity.getDmGrid().addTextColumn(txtHConsignerCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHConsignerCity.getDmGrid().addTextColumn(txtHConsignerCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHConsignerCity.getDmGrid().initializeGrid(txtHConsignerCity.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHConsignerCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Dispatch From">
    final DmTextBox txtDispatcherName = new DmTextBox();
    final DmTextBox txtDispatcherGstin = new DmTextBox();
    final DmTextBox txtDispatcherGstinDate = new DmTextBox();
    final DmTextBox txtDispatcherLegalName = new DmTextBox();
    final DmTextBox txtDispatcherTradeName = new DmTextBox();
    final DmTextBox txtDispatcherAddress1 = new DmTextBox();
    final DmTextBox txtDispatcherAddress2 = new DmTextBox();
    final DmTextBox txtDispatcherState = new DmTextBox();
    final DmTextBox txtDispatcherPhone = new DmTextBox();
    final DmTextBox txtDispatcherEmail = new DmTextBox();
    final DmTextBoxNumeric txtDispatcherPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtDispatcherCountry = new DmTextBox();
    //<editor-fold defaultstate="collapsed" desc="txtHDispatcherCity">

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHDispatcherCity = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHDispatcherCity() {
        txtHDispatcherCity.getDmGrid().addTextColumn(txtHDispatcherCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHDispatcherCity.getDmGrid().addTextColumn(txtHDispatcherCity.getDmGrid().getPropertyAccess().districtName(), "District", 100, "districtName");
        txtHDispatcherCity.getDmGrid().addTextColumn(txtHDispatcherCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHDispatcherCity.getDmGrid().addTextColumn(txtHDispatcherCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHDispatcherCity.getDmGrid().initializeGrid(txtHDispatcherCity.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHDispatcherCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BillTo">
    final DmTextBox txtBuyerName = new DmTextBox();
    final DmTextBox txtBuyerGstin = new DmTextBox();
    final DmTextBox txtBuyerGstinDate = new DmTextBox();
    final DmTextBox txtBuyerLegalName = new DmTextBox();
    final DmTextBox txtBuyerTradeName = new DmTextBox();
    final DmTextBox txtBuyerAddress1 = new DmTextBox();
    final DmTextBox txtBuyerAddress2 = new DmTextBox();
    final DmTextBox txtBuyerState = new DmTextBox();
    final DmTextBox txtBuyerPhone = new DmTextBox();
    final DmTextBox txtBuyerEmail = new DmTextBox();
    final DmTextBoxNumeric txtBuyerPinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtBuyerCountry = new DmTextBox();
    //<editor-fold defaultstate="collapsed" desc="txtHBuyerCity">

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHBuyerCity = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHBuyerCity() {
        txtHBuyerCity.getDmGrid().addTextColumn(txtHBuyerCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHBuyerCity.getDmGrid().addTextColumn(txtHBuyerCity.getDmGrid().getPropertyAccess().districtName(), "District", 100, "districtName");
        txtHBuyerCity.getDmGrid().addTextColumn(txtHBuyerCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHBuyerCity.getDmGrid().addTextColumn(txtHBuyerCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHBuyerCity.getDmGrid().initializeGrid(txtHBuyerCity.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHBuyerCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Consignee">
    final DmTextBox txtConsigneeName = new DmTextBox();
    final DmTextBox txtConsigneeGstin = new DmTextBox();
    final DmTextBox txtConsigneeGstinDate = new DmTextBox();
    final DmTextBox txtConsigneeLegalName = new DmTextBox();
    final DmTextBox txtConsigneeTradeName = new DmTextBox();
    final DmTextBox txtConsigneeAddress1 = new DmTextBox();
    final DmTextBox txtConsigneeAddress2 = new DmTextBox();
    final DmTextBox txtConsigneeState = new DmTextBox();
    final DmTextBox txtConsigneePhone = new DmTextBox();
    final DmTextBox txtConsigneeEmail = new DmTextBox();
    final DmTextBoxNumeric txtConsigneePinCode = new DmTextBoxNumeric(0, false, 6, false);
    final DmTextBox txtConsigneeCountry = new DmTextBox();
    //<editor-fold defaultstate="collapsed" desc="txtHConsigneeCity">

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHConsigneeCity = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHConsigneeCity() {
        txtHConsigneeCity.getDmGrid().addTextColumn(txtHConsigneeCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHConsigneeCity.getDmGrid().addTextColumn(txtHConsigneeCity.getDmGrid().getPropertyAccess().districtName(), "District", 100, "districtName");
        txtHConsigneeCity.getDmGrid().addTextColumn(txtHConsigneeCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHConsigneeCity.getDmGrid().addTextColumn(txtHConsigneeCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHConsigneeCity.getDmGrid().initializeGrid(txtHConsigneeCity.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHConsigneeCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="ShipTo">
    final DmTextBox txtShipToPartyName = new DmTextBox();
    final DmTextBox txtShipToPartyGstin = new DmTextBox();
    final DmTextBox txtShipToPartyGstinDate = new DmTextBox();
    final DmTextBox txtShipToPartyLegalName = new DmTextBox();
    final DmTextBox txtShipToPartyTradeName = new DmTextBox();
    final DmTextBox txtShipToPartyAddress1 = new DmTextBox();
    final DmTextBox txtShipToPartyAddress2 = new DmTextBox();
    final DmTextBox txtShipToPartyState = new DmTextBox();
    final DmTextBox txtShipToPartyPhone = new DmTextBox();
    final DmTextBox txtShipToPartyEmail = new DmTextBox();
    final DmTextBox txtShipToPartyPinCode = new DmTextBox();
    final DmTextBox txtShipToPartyCountry = new DmTextBox();
    //<editor-fold defaultstate="collapsed" desc="txtHShipToPartyCity">

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHShipToPartyCity = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHShipToPartyCity() {
        txtHShipToPartyCity.getDmGrid().addTextColumn(txtHShipToPartyCity.getDmGrid().getPropertyAccess().name(), "City", 0, "CityName");
        txtHShipToPartyCity.getDmGrid().addTextColumn(txtHShipToPartyCity.getDmGrid().getPropertyAccess().districtName(), "District", 100, "districtName");
        txtHShipToPartyCity.getDmGrid().addTextColumn(txtHShipToPartyCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHShipToPartyCity.getDmGrid().addTextColumn(txtHShipToPartyCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHShipToPartyCity.getDmGrid().initializeGrid(txtHShipToPartyCity.getDmGrid().getPropertyAccess().code(), 450, 0, 1);

        txtHShipToPartyCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="POPUP">
    final PopupPanel popupPanel = new PopupPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PANELS">
    final FlowPanel flwCancel = new FlowPanel();
    final FlowPanel flwSetting = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BUTTONS">
    final Button btnCancel = new Button();
    final Button btnSetting = new Button();
    final DmLabel btnAuthentication = new DmLabel("Refresh Token");
    final DmLabel btnGenrateEway = new DmLabel("Genrate E-Way");
    final DmLabel btnGenrateEwayViaIrn = new DmLabel("E-Way via IRN");
    final DmLabel btnCancelEway = new DmLabel("Cancel E-Way");
    final DmLabel btnCancelEinv = new DmLabel("Cancel E-invoice");
    final DmLabel btnPrintEwb = new DmLabel("Print E-way");
    final DmLabel btnPrintDeatiledEwb = new DmLabel("Print Detail E-way");
    final DmLabel btnGenrateEinv = new DmLabel("Genrate E-invoice");
    final DmLabel btnSaveGst = new DmLabel("Update GST Doc.");

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TextHelp">
    //<editor-fold defaultstate="collapsed" desc="txtHTransporter">
    interface TxtHtransporter_PrpAcs extends KeyField_T_PrpAcs<TxtHTranporter_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHTranporter_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<TxtHTranporter_Mdl, String> cityName();
    }

    final DmTextHelp<TxtHTranporter_Mdl, TxtHtransporter_PrpAcs> txtHTransporter = new DmTextHelp(GWT.create(TxtHtransporter_PrpAcs.class), 6, true);

    private void iniTxtHTransporter() {
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHTransporter.getDmGrid().addTextColumn(txtHTransporter.getDmGrid().getPropertyAccess().cityName(), "City", 150, "cityName");
        txtHTransporter.getDmGrid().initializeGrid(txtHTransporter.getDmGrid().getPropertyAccess().code(), 450, 0, 1);
        txtHTransporter.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHTransporter.getHelper().addSort("cityName", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHTransportMode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHTransportMode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, false);

    private void iniTxtHTrasportMode() {
        txtHTransportMode.getDmGrid().addTextColumn(txtHTransportMode.getDmGrid().getPropertyAccess().name(), "Name", 0, "");
        txtHTransportMode.getDmGrid().initializeGrid(txtHTransportMode.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHTransportMode.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHVehicleType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHVehicleType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHVehicleType() {
        txtHVehicleType.getDmGrid().addTextColumn(txtHVehicleType.getDmGrid().getPropertyAccess().name(), "Vehicle Type", 0, "name");
        txtHVehicleType.getDmGrid().initializeGrid(txtHVehicleType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHVehicleType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEwaySubSupplyType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEwaySubSupplyType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHEwaySubSupplyType() {
        txtHEwaySubSupplyType.getDmGrid().addTextColumn(txtHEwaySubSupplyType.getDmGrid().getPropertyAccess().name(), "SubSupply Type", 0, "name");
        txtHEwaySubSupplyType.getDmGrid().initializeGrid(txtHEwaySubSupplyType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHEwaySubSupplyType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEwayDocumentType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEwayDocumentType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHEwayDocumentType() {
        txtHEwayDocumentType.getDmGrid().addTextColumn(txtHEwayDocumentType.getDmGrid().getPropertyAccess().name(), "Document Type", 0, "name");
        txtHEwayDocumentType.getDmGrid().initializeGrid(txtHEwayDocumentType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHEwayDocumentType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEwayTransactionType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEwayTransactionType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHEwayTransactionType() {
        txtHEwayTransactionType.getDmGrid().addTextColumn(txtHEwayTransactionType.getDmGrid().getPropertyAccess().name(), "Transaction Type", 0, "name");
        txtHEwayTransactionType.getDmGrid().initializeGrid(txtHEwayTransactionType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHEwayTransactionType.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEinvoiceSubSupplyType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEinvoiceSupplyType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHEinvoiceSupplyType() {
        txtHEinvoiceSupplyType.getDmGrid().addTextColumn(txtHEinvoiceSupplyType.getDmGrid().getPropertyAccess().name(), "Supply Type", 0, "name");
        txtHEinvoiceSupplyType.getDmGrid().initializeGrid(txtHEinvoiceSupplyType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHEinvoiceSupplyType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHEinvoiceDocType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEinvoiceDocType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHEinvoiceDocType() {
        txtHEinvoiceDocType.getDmGrid().addTextColumn(txtHEinvoiceDocType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHEinvoiceDocType.getDmGrid().initializeGrid(txtHEinvoiceDocType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHEinvoiceDocType.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHIgstOnIntra">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIgstOnIntra = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHIgstOnIntra() {
        txtHIgstOnIntra.getDmGrid().addTextColumn(txtHIgstOnIntra.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHIgstOnIntra.getDmGrid().initializeGrid(txtHIgstOnIntra.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHIgstOnIntra.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="txtHReverseCharge">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHReverseCharge = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHReverseCharge() {
        txtHReverseCharge.getDmGrid().addTextColumn(txtHReverseCharge.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHReverseCharge.getDmGrid().initializeGrid(txtHReverseCharge.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHReverseCharge.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHDocumentType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstDocumentType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, false);

    private void iniDocumentType() {
        txtHGstDocumentType.getDmGrid().addTextColumn(txtHGstDocumentType.getDmGrid().getPropertyAccess().name(), "Name", 50, "name");
        txtHGstDocumentType.getDmGrid().initializeGrid(txtHGstDocumentType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGstDocumentType.getHelper().addSort("Name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHDocumentNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstDocumentNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 7, true, false);

    private void iniDocumentNature() {
        txtHGstDocumentNature.getDmGrid().addTextColumn(txtHGstDocumentNature.getDmGrid().getPropertyAccess().name(), "Name", 50, "name");
        txtHGstDocumentNature.getDmGrid().initializeGrid(txtHGstDocumentNature.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGstDocumentNature.getHelper().addSort("Name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>
    //</editor-fold>

    public EGstPopup_Dsg() {
        ini();
        iniTxtHTransporter();
        iniTxtHTrasportMode();
        iniTxtHVehicleType();
        iniTxtHEwaySubSupplyType();
        iniTxtHEwayDocumentType();
        iniTxtHEinvoiceSupplyType();
        iniTxtHEinvoiceDocType();
        iniTxtHReverseCharge();
        iniTxtHIgstOnIntra();
        iniDocumentType();
        iniDocumentNature();
        iniTxtHEwayTransactionType();
        iniTxtHConsignerCity();
        iniTxtHDispatcherCity();
        iniTxtHBuyerCity();
        iniTxtHConsigneeCity();
        iniTxtHShipToPartyCity();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("700px");
        iniToolControls();
        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolControls() {

        InlineLabel label;
        Anchor aBtn;
        label = new InlineLabel("Ok");

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");

        aBtn.getElement().getStyle().setColor("#7d7d7d");

        label = new InlineLabel("Close");

        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");

        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.setTitle("Close");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);

        label = new InlineLabel("Setting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-cogs");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSetting.setTitle("Setting");
        btnSetting.addStyleName("dm-top-control-btnStyle");
        btnSetting.getElement().appendChild(aBtn.getElement());
        btnSetting.getElement().appendChild(label.getElement());
        btnSetting.setWidth("70px");
        flwSetting.add(btnSetting);

    }

    private FlowPanel workingArea() {

        FlowPanel flwTable, flwTr, flwTd;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "100%", "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.getElement().getStyle().setMarginBottom(1, Style.Unit.PCT);
        flwTd.add(vl.add(viewArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(tabArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.add(vl.add(actionArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel viewArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "100%", null, null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-way sub-supply type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-way doc. type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-invoice supply type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-invoice doc. type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEwaySubSupplyType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEwayDocumentType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEinvoiceSupplyType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEinvoiceDocType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-way Transcation type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-way sub-supply type description");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEwayTransactionType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSubSupplyDesc));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/

 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Auth Token");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Expiry at");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAuthToken));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTokenExpiryAt));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private TabLayoutPanel tabArea() {
        DmClient.setStyle(tabPanel, "100%", "240px", "left", null, DmEnum.DmPosition.NONE, true);

        tabPanel.add(vl.add(ewayArea()), "E-way / E-invoice");
        tabPanel.add(vl.add(transporter()), "Transporter");
        tabPanel.add(vl.add(consigner()), "Consigner");
        tabPanel.add(vl.add(dispatchFrom()), "Dispatch From");
        tabPanel.add(vl.add(billTo()), "Bill To");
        tabPanel.add(vl.add(consignee()), "Consignee");
        tabPanel.add(vl.add(shipTo()), "Ship To");
        tabPanel.add(vl.add(gstDetail()), "GST Doc. Detail");
        tabPanel.selectTab(0);
        tabPanel.ensureDebugId("cwTabPanel");

        return tabPanel;
    }

    private FlowPanel actionArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        flwTable.addStyleName("col-12");
        /*====================================< 1st Column >====================================*/

        flwTr = new FlowPanel();
        flwTr.addStyleName("card");
        flwTr.getElement().setAttribute("style", "position: relative;");
        flwTd = new FlowPanel();
        flwTd.setStyleName("card-header");
        DmLabel lbl = new DmLabel("Actions");
        lbl.setStyleName("card-header-text");
        lbl.getElement().setAttribute("style", "font-size: 14px;");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        flwTd.addStyleName("card-body");
        flwTd.getElement().setAttribute("style", "padding: 7px 8px;");
        flwQuickLink.setStyleName("dm-quick-link-style");

        //<editor-fold defaultstate="collapsed" desc="btnAuthentication">
        btnAuthentication.setStyleName(buttonStyleName);
        btnAuthentication.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnGenrateEway">
        btnGenrateEway.setStyleName(buttonStyleName);
        btnGenrateEway.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnGenrateEinv">
        btnGenrateEinv.setStyleName(buttonStyleName);
        btnGenrateEinv.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnCancelEway">
        btnCancelEway.setStyleName(buttonStyleName);
        btnCancelEway.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnPrintEwb">
        btnPrintEwb.setStyleName(buttonStyleName);
        btnPrintEwb.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnPrintDeatiledEwb">
        btnPrintDeatiledEwb.setStyleName(buttonStyleName);
        btnPrintDeatiledEwb.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnGenrateEwayViaIrn">
        btnGenrateEwayViaIrn.setStyleName(buttonStyleName);
        btnGenrateEwayViaIrn.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnCancelEinv">
        btnCancelEinv.setStyleName(buttonStyleName);
        btnCancelEinv.getElement().setAttribute("style", buttonStyleAttr);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="btnSaveGst">
        btnSaveGst.setStyleName(buttonStyleName);
        btnSaveGst.getElement().setAttribute("style", buttonStyleAttr);
//</editor-fold>

        flwTd.add(flwQuickLink);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel transporter() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Transporter");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Transport Id");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransporter));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTransporterId));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtTransporterGstinDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Transport Mode");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Distance");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHTransportMode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDistance));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Vehicle Type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Vehicle No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVehicleType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtVehicleNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("LR No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtLrNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtLrDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel ewayArea() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-invoice Ack. No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-invoice Ack. Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-invoice Status");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEInvAckNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEInvAckDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEInvStatus));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-invoice IRN No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEInvIrnNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-Way No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-Way Bill Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-Way Bill Valid Upto");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEWayBillNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEWayDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "32%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEWayValidUpto));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("E-Way Message");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtEWayMsg));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel consigner() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("GSTIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerGstin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerGstinDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Legal Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Trade Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerLegalName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerTradeName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 1");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 2");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerAddress1));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("State");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsignerCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerState));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Pincode");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Phone/Mobile");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Email");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerPinCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerPhone));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsignerEmail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel dispatchFrom() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("GSTIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("GSTIN Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherGstin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherGstinDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Legal Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Trade Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherLegalName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherTradeName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 1");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 2");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherAddress1));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("State");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDispatcherCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherState));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Pincode");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Phone/Mobile");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Email");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherPinCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherPhone));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDispatcherEmail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel billTo() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party GSTIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party GSTIN Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerGstin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerGstinDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Legal Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Trade Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerLegalName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerTradeName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 1");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 2");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerAddress1));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("State");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBuyerCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerState));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Pincode");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Phone/Mobile");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Email");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerPinCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerPhone));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtBuyerEmail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel consignee() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party GSTIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party GSTIN Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeGstin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeGstinDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Legal Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Trade Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeLegalName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeTradeName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 1");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 2");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeAddress1));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("State");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHConsigneeCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeState));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Pincode");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Phone/Mobile");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Email");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneePinCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneePhone));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtConsigneeEmail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel shipTo() {

        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party GSTIN");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Party GSTIN Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyGstin));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "24%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyGstinDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Legal Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Trade Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyLegalName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyTradeName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 1");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Address 2");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyAddress1));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyAddress2));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("State");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHShipToPartyCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyState));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Pincode");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Phone/Mobile");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Email");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyPinCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyPhone));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtShipToPartyEmail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

    private FlowPanel gstDetail() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("GST Document Type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("GST Document Nature");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstDocumentType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "49%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstDocumentNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
        return flwTable;
    }

}
