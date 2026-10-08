package aarogya.master.cardTypeMaster.client;

import aarogya.master.cardTypeMaster.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class CardTypeMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().CARD_TYPE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final FlowPanel flwForm = new FlowPanel();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmSuggestBox txtCardName = new DmSuggestBox(50, 10, true, "Name", DmDataType.STRING);
    final DmSuggestBox txtShortName = new DmSuggestBox(10, 10, true, "shortName", DmDataType.STRING);
    final DmTextBoxNumeric txtOnAmt = new DmTextBoxNumeric(2, false, 8, false);
    final DmTextBoxNumeric txtValidDays = new DmTextBoxNumeric(0, false, 4, false);
    final DmTextBoxNumeric txtPoints = new DmTextBoxNumeric(2, false, 8, false);
    final DmTextBoxNumeric txtValuePerPoint = new DmTextBoxNumeric(2, false, 8, false);

    final MenuItem mnuExportCard = new MenuItem("Export CardType");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="TxtHLinkedAccount">
    interface TxtHLinkedAccount_PrpAcs extends KeyField_T_PrpAcs<LinkedAccount_Mdl> {

        @Editor.Path("City")
        ValueProvider<LinkedAccount_Mdl, String> city();

        @Editor.Path("Group")
        ValueProvider<LinkedAccount_Mdl, String> group();
    }
    final DmTextHelp<LinkedAccount_Mdl, TxtHLinkedAccount_PrpAcs> txtHLinkedAccount = new DmTextHelp(GWT.create(TxtHLinkedAccount_PrpAcs.class), 7, true);

    private void iniLinkedAccount() {
        txtHLinkedAccount.getDmGrid().addTextColumn(txtHLinkedAccount.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHLinkedAccount.getDmGrid().addTextColumn(txtHLinkedAccount.getDmGrid().getPropertyAccess().city(), "City", 100, "cityName");
        txtHLinkedAccount.getDmGrid().addTextColumn(txtHLinkedAccount.getDmGrid().getPropertyAccess().group(), "Group", 150, "groupName");
        txtHLinkedAccount.getDmGrid().initializeGrid(txtHLinkedAccount.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHLinkedAccount.getHelper().addSort("name", DmSortType.ASC);
        txtHLinkedAccount.getHelper().addSort("groupName", DmSortType.ASC);
        txtHLinkedAccount.getHelper().addSort("cityName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    public void iniNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Option", 0, "Option");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSms">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSms = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniSms() {
        txtHSms.getDmGrid().addTextColumn(txtHSms.getDmGrid().getPropertyAccess().name(), "Option", 0, "Option");
        txtHSms.getDmGrid().initializeGrid(txtHSms.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHEmail">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmail = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void initxtHEmail() {
        txtHEmail.getDmGrid().addTextColumn(txtHEmail.getDmGrid().getPropertyAccess().name(), "Option", 0, "Option");
        txtHEmail.getDmGrid().initializeGrid(txtHEmail.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniActive() {
        txtHActive.getDmGrid().addTextColumn(txtHActive.getDmGrid().getPropertyAccess().name(), "Option", 0, "Option");
        txtHActive.getDmGrid().initializeGrid(txtHActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHPhysicalIdReq">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPhysicalIdReq = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniPhysicalIdReq() {
        txtHPhysicalIdReq.getDmGrid().addTextColumn(txtHPhysicalIdReq.getDmGrid().getPropertyAccess().name(), "Option", 0, "Opiton");
        txtHPhysicalIdReq.getDmGrid().initializeGrid(txtHPhysicalIdReq.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCardType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCardType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniCardType() {
        txtHCardType.getDmGrid().addTextColumn(txtHCardType.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHCardType.getDmGrid().initializeGrid(txtHCardType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCurrencyType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCurrencyType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, false);

    private void iniCurrencyType() {
        txtHCurrencyType.getDmGrid().addTextColumn(txtHCurrencyType.getDmGrid().getPropertyAccess().name(), "Type", 0, "name");
        txtHCurrencyType.getDmGrid().initializeGrid(txtHCurrencyType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHCurrencyType.getHelper().addSort("name", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("LinkedAccount")
        ValueProvider<Find_Mdl, String> linkedAccount();

        @Editor.Path("CardTypeName")
        ValueProvider<Find_Mdl, String> cardTypeName();

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("Nature")
        ValueProvider<Find_Mdl, String> nature();

        @Editor.Path("Active")
        ValueProvider<Find_Mdl, String> active();

        @Editor.Path("PhysicallyRequired")
        ValueProvider<Find_Mdl, String> physicallyRequired();

        @Editor.Path("OnAmount")
        ValueProvider<Find_Mdl, String> onAmount();

        @Editor.Path("Points")
        ValueProvider<Find_Mdl, String> points();

        @Editor.Path("ValuePerPoint")
        ValueProvider<Find_Mdl, String> valuePerPoint();

        @Editor.Path("ValidDays")
        ValueProvider<Find_Mdl, String> validDays();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Card Name", 140, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name", 90, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().linkedAccount(), "Linked Account", 150, "linkAccount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cardTypeName(), "Card Type", 130, "cardType");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Nature", 90, "nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().active(), "Active", 70, "isActive");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().physicallyRequired(), "Physically Req.", 100, "isPhysicallyRequired");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().onAmount(), "On Amount", 90, "onAmount");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().points(), "Point (S)", 80, "points");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().valuePerPoint(), "Value Per Point", 100, "valuePerPoint");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().validDays(), "Valid Days", 80, "validDays");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAccountEditable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAccountEditable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 2, true, true);

    private void iniAccountEditable() {
        txtHAccountEditable.getDmGrid().addTextColumn(txtHAccountEditable.getDmGrid().getPropertyAccess().name(), "Option", 0, "Option");
        txtHAccountEditable.getDmGrid().initializeGrid(txtHAccountEditable.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //=================================<MJ>====================================//
    //<editor-fold defaultstate="collapsed" desc="txtHPOSIntegration">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPOSIntegration = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, true);

    private void iniHPOSIntegration() {
        txtHPOSIntegration.getDmGrid().addTextColumn(txtHPOSIntegration.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHPOSIntegration.getDmGrid().initializeGrid(txtHPOSIntegration.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHPOSIntegrationType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHPOSIntegrationType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true, false);

    private void iniHPOSIntegrationType() {
        txtHPOSIntegrationType.getDmGrid().addTextColumn(txtHPOSIntegrationType.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHPOSIntegrationType.getDmGrid().initializeGrid(txtHPOSIntegrationType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
//</editor-fold>
    final DmRemarkTextBox txtIntegrationParams = new DmRemarkTextBox(700, false);
    //=================================<****>=====================================//

    public CardTypeMaster_Dsg() {
        ini();
        iniLinkedAccount();
        iniCardType();
        initxtHEmail();
        iniPhysicalIdReq();
        iniSms();
        iniActive();
        iniMenu();
        iniCurrencyType();
        iniAccountEditable();
        iniHPOSIntegration();
        iniHPOSIntegrationType();
    }

    private void ini() {

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.CENTER, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
        vrtInnerForm.add(vl.add(topControl));
        iniFind();

        FlowPanel flwWorkingArea = new FlowPanel();
        flwWorkingArea.setStyleName("dm-FlwWorkingArea");
        flwWorkingArea.add(workingArea());
        vrtInnerForm.add(flwWorkingArea);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flwWorkingArea, "100%");
        flwForm.add(vrtInnerForm);

    }

    private DmUserControl iniUserControl() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "476px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCardName));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked Account");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkedAccount));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Card Type");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCardType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Sms Alert");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSms));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Email Alert");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmail));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Currency");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCurrencyType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("On Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtOnAmt));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Point (s)");
        lbl.getElement().getStyle().setMarginLeft(12, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPoints));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Value Per Point");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValuePerPoint));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        iniNature();
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Valid Days");
        lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtValidDays));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Physically Require");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHPhysicalIdReq);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Active");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "7%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtHActive);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Editable");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountEditable));
        flwTr.add(flwTd);

        //==================================<MJ>====================================//
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "13%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("POS Integration");
        lbl.getElement().getStyle().setMarginLeft(13, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "12%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPOSIntegration));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("POS Integration Type");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHPOSIntegrationType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "67px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Integration Params");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIntegrationParams));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //================================<*******>=================================//
        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
