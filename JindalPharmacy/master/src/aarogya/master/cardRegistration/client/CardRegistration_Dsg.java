package aarogya.master.cardRegistration.client;

import aarogya.master.cardRegistration.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import aarogya.selfToolkit.walletSystem.shared.TxtHCardType_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class CardRegistration_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().CARD_REGISTRATION.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextBox txtNature = new DmTextBox(2, true);
    final DmTextBox txtVrNo = new DmTextBox(15, true);
    final DmTextBoxNumeric txtOnAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxNumeric txtValPerPoint = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(true);
    final DmTextBox txtAllotmentDate = new DmTextBox(15, true);
    final DmTextBox txtContactPerson = new DmTextBox(50, true);
    final DmTextBox txtAddress = new DmTextBox(100, false);
    final DmTextBox txtDOB = new DmTextBox(15, false);
    final DmTextBox txtDOA = new DmTextBox(15, false);
    final DmTextBox txtEmail = new DmTextBox(30, false);
    final DmTextBox txtCardNo = new DmTextBox(30, true);
    final DmTextBox txtExpiryDate = new DmTextBox(30, false);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(150, false);
    final DmTextBox txtPoint = new DmTextBox(15, false);
    final DmTextBox txtUHID = new DmTextBox(15, false);
    final Button btnUHID = new Button();

    final Button btnCardHelp = new Button();
    final FlowPanel flwSearchPatient = new FlowPanel();
    final DmLabel lblCardBalance = new DmLabel("");
    final Button btnCardRecharge = new Button(new pmMaster().CARD_RECHARGE.caption);
    final Button btnCardDischarge = new Button(new pmMaster().CARD_REFUND.caption);
    final Button btnCardDeactive = new Button(new pmMaster().CARD_DEACTIVE.caption);

    //<editor-fold defaultstate="collapsed" desc="TxtHCity">
    interface TxtHCity_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHCity_Mdl, String> city();

        @Editor.Path("State")
        ValueProvider<TxtHCity_Mdl, String> state();

    }
    final DmTextHelp<TxtHCity_Mdl, TxtHCity_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_PrpAcs.class), 10, false);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().city(), "City", 0, "cityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().state(), "State", 100, "stateName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHCity.getHelper().addSort("cityName", DmSortType.ASC);
        txtHCity.getHelper().addSort("stateName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHPrefix">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPrefix = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHPrefix() {
        txtHPrefix.getDmGrid().addTextColumn(txtHPrefix.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHPrefix.getDmGrid().initializeGrid(txtHPrefix.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHPrefix.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCardSerial">
    final DmHelpGrid<KeyFieldExtended, KeyField_PrpAcs> txtHCardHelp = new DmHelpGrid(GWT.create(KeyField_PrpAcs.class), 10);

    private void iniTxtHCardSerial() {
        txtHCardHelp.getDmGrid().addTextColumn(txtHCardHelp.getDmGrid().getPropertyAccess().name(), "Name", 0, "cardNo");
        txtHCardHelp.getDmGrid().initializeGrid(txtHCardHelp.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHCardHelp.getHelper().addSort("cardNo", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHLinkAccount">
    interface TxtHLinkAccount_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> city();

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> group();

    }

    final DmTextHelp<KeyFieldExtended, TxtHLinkAccount_PrpAcs> txtHLinkAccount = new DmTextHelp(GWT.create(TxtHLinkAccount_PrpAcs.class), 10, true);

    private void iniTxtHLinkAccount() {
        txtHLinkAccount.getDmGrid().addTextColumn(txtHLinkAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHLinkAccount.getDmGrid().addTextColumn(txtHLinkAccount.getDmGrid().getPropertyAccess().city(), "City", 100, "cityName");
        txtHLinkAccount.getDmGrid().addTextColumn(txtHLinkAccount.getDmGrid().getPropertyAccess().group(), "Group", 150, "groupName");
        txtHLinkAccount.getDmGrid().initializeGrid(txtHLinkAccount.getDmGrid().getPropertyAccess().code(), 520, 0, 1);

        txtHLinkAccount.getHelper().addSort("name", DmSortType.ASC);
        txtHLinkAccount.getHelper().addSort("groupName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHCardType">
    interface TxtHCardType_PrpAcs extends KeyField_T_PrpAcs<TxtHCardType_Mdl> {

        @Editor.Path("Nature")
        ValueProvider<TxtHCardType_Mdl, String> nature();

        @Editor.Path("LinkAccount")
        ValueProvider<TxtHCardType_Mdl, String> linkAccount();

    }

    final DmTextHelp<TxtHCardType_Mdl, TxtHCardType_PrpAcs> txtHCardType = new DmTextHelp(GWT.create(TxtHCardType_PrpAcs.class), 10, true);

    private void iniTxtHCardType() {
        txtHCardType.getDmGrid().addTextColumn(txtHCardType.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHCardType.getDmGrid().addTextColumn(txtHCardType.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "Nature");
        txtHCardType.getDmGrid().addTextColumn(txtHCardType.getDmGrid().getPropertyAccess().linkAccount(), "Link Account", 150, "SGName");
        txtHCardType.getDmGrid().initializeGrid(txtHCardType.getDmGrid().getPropertyAccess().code(), 520, 0, 1);

        txtHCardType.getHelper().addSort("NAME", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("CardNo")
        ValueProvider<Find_Mdl, String> cardNo();

        @Editor.Path("CardName")
        ValueProvider<Find_Mdl, String> cardName();

        @Editor.Path("DtAllotment")
        ValueProvider<Find_Mdl, String> dtAllotment();

        @Editor.Path("DtExpiry")
        ValueProvider<Find_Mdl, String> dtExpiry();

        @Editor.Path("Person")
        ValueProvider<Find_Mdl, String> person();

        @Editor.Path("Address")
        ValueProvider<Find_Mdl, String> address();

        @Editor.Path("City")
        ValueProvider<Find_Mdl, String> city();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Card Reg No.", 0, "recId");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dtAllotment(), "Allotment Date", 100, "AllotmentDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().dtExpiry(), "Valid Through", 100, "ExpiryDate", DmDataType.DATE);
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardName(), "Card Name", 150, "cardName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardNo(), "Card No.", 120, "cardNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().person(), "Contact Person", 200, "personName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().city(), "City", 120, "cityName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().address(), "Address", 300, "address");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("v_Date", DmSortType.DESC);
        dmFind.getHelper().addSort("short_Name", DmSortType.ASC);
        dmFind.getHelper().addSort("v_No", DmSortType.DESC);
        dmFind.getHelper().addSort("recId", DmSortType.ASC);
    }
    //</editor-fold>

    public CardRegistration_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);

        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));

        iniFind();
        iniTxtHCardSerial();
        iniTxtHCity();
        iniTxtHLinkAccount();
        iniTxtHCardType();
        iniTxtHPrefix();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl iniUserControl() {
        Button button;

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addGeneralBlog(lblCardBalance);
        lblCardBalance.setHeight("15Px");
        lblCardBalance.getElement().getStyle().setColor("red");
        lblCardBalance.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblCardBalance.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblCardBalance.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
        lblCardBalance.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        btnCardDeactive.setWidth("100Px");
        btnCardDischarge.setWidth("100Px");
        btnCardRecharge.setWidth("100Px");
        btnCardDeactive.addStyleName("dm-button-grad");
        btnCardDischarge.addStyleName("dm-button-grad");
        btnCardRecharge.addStyleName("dm-button-grad");
        userControl.addGeneralBlog(btnCardRecharge);
        userControl.addGeneralBlog(btnCardDischarge);
        userControl.addGeneralBlog(btnCardDeactive);

        userControl.addTextBlog(modifiedByBlog, 100, false, false);

        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "200px", null, null, DmPosition.CENTER, true);

        //Layout
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "60%", "517px", "left", null, DmPosition.CENTER, true);
        flwTd.add(leftSection());
        flwTable.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", "517px", "left", null, DmPosition.CENTER, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        flwTd.add(rightSection());
        flwTable.add(flwTd);

        return flwTable;
    }

    private FlowPanel leftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "517px", null, null, DmPosition.NONE, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reg No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVrNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allotment Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAllotmentDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCardType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Linked Account
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked Account");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkAccount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "26%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtCardNo);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        btnCardHelp.addStyleName("dm-button-grad");
        btnCardHelp.setText("...");
        btnCardHelp.setTitle("Card No. Help");
        btnCardHelp.setWidth("20px");
        btnCardHelp.setHeight("20px");
        flwTd.add(btnCardHelp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNature));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Value Per Point");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValPerPoint));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("On Amount");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        txtOnAmount.setAlignment(ValueBoxBase.TextAlignment.LEFT);
        flwTd.add(vl.add(txtOnAmount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPoint));

        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Card Curr Bal & Value Per Point
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Valid Through");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpiryDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("UHID No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);

        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, true);

        hrtPnl.add(vl.add(txtUHID));
        hrtPnl.setCellWidth(txtUHID, "100%");

        DmClient.setStyle(flwSearchPatient, "100%", "18px", "left", null, DmPosition.NONE, false);
        btnUHID.addStyleName("dm-button-grad");
        btnUHID.setText("...");
        btnUHID.setWidth("23px");
        btnUHID.setHeight("20px");
        btnUHID.getElement().setAttribute("style", btnUHID.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnUHID.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);
        btnUHID.setTitle("UHID Search");
        flwSearchPatient.add(vl.add(btnUHID));
        hrtPnl.add(flwSearchPatient);
        hrtPnl.setCellWidth(flwSearchPatient, "20px");
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Contact Person
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contact Person");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPrefix));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.add(vl.add(txtContactPerson));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Address
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Address");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //City & Mobile
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("City");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Mobile");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        txtMobile.setAlignment(ValueBoxBase.TextAlignment.LEFT);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel rightSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "517px", null, null, DmPosition.CENTER, true);

        //Card Curr Bal & Value Per Point
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("D.O.B.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOB));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Annv. Date");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDOA));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Email
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEmail));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //Remark
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Remark");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
