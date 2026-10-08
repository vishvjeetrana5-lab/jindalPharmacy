package aarogya.pharmacy.ledgerMaster.client;

import aarogya.pharmacy.ledgerMaster.shared.*;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxMobile.client.DmTextBoxMobile;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;

class LedgerMaster_Dsg {

    final String widgetName = new pmPharmacy().LEDGER_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtName = new DmTextBox(150, false);
    final DmTextBox txtDisplayName = new DmTextBox(100, true);
    final DmTextBox txtManualCode = new DmTextBox(25, false);
    final DmTextBoxMobile txtMobile = new DmTextBoxMobile(false);
    final DmTextBox txtAddress = new DmTextBox(250, false);
    final DmTextBox txtIdNo = new DmTextBox(50, false);
    final DmTextBox txtAccountName = new DmTextBox();
    final DmTextBox txtRegNo = new DmTextBox();
    final DmLabel lblPharmacyBalance = new DmLabel("");

    //<editor-fold defaultstate="collapsed" desc="txtHCity">
    interface TxtHCity_Mdl_PrpAcs extends KeyField_T_PrpAcs<TxtHCity_Mdl> {

        @Editor.Path("StateName")
        ValueProvider<TxtHCity_Mdl, String> stateName();

        @Editor.Path("CountryName")
        ValueProvider<TxtHCity_Mdl, String> countryName();

    }

    final DmTextHelp<TxtHCity_Mdl, TxtHCity_Mdl_PrpAcs> txtHCity = new DmTextHelp(GWT.create(TxtHCity_Mdl_PrpAcs.class), 4, false);

    private void iniTxtHCity() {
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().name(), "City", 0, "cityName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().stateName(), "State", 100, "stateName");
        txtHCity.getDmGrid().addTextColumn(txtHCity.getDmGrid().getPropertyAccess().countryName(), "Country", 100, "countryName");
        txtHCity.getDmGrid().initializeGrid(txtHCity.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHCity.getHelper().addSort("CityName", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHIdType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIdType = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 8, false);

    private void iniTxtHIdType() {
        txtHIdType.getDmGrid().addTextColumn(txtHIdType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHIdType.getDmGrid().initializeGrid(txtHIdType.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHIdType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHIntial">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHInitial = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHInitial() {
        txtHInitial.getDmGrid().addTextColumn(txtHInitial.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHInitial.getDmGrid().initializeGrid(txtHInitial.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHInitial.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHIsActive">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsActive = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, false);

    private void iniTxtHIsActive() {
        txtHIsActive.getDmGrid().addTextColumn(txtHIsActive.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIsActive.getDmGrid().initializeGrid(txtHIsActive.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
        txtHIsActive.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<LedgerMaster_Mdl> {

        @Editor.Path("DisplayName")
        ValueProvider<LedgerMaster_Mdl, String> displayName();

        @Editor.Path("ManualCode")
        ValueProvider<LedgerMaster_Mdl, String> manualCode();

        @Editor.Path("Address")
        ValueProvider<LedgerMaster_Mdl, String> address();

        @Editor.Path("CityName")
        ValueProvider<LedgerMaster_Mdl, String> cityName();

        @Editor.Path("Mobile")
        ValueProvider<LedgerMaster_Mdl, String> mobile();

        @Editor.Path("IdTypeName")
        ValueProvider<LedgerMaster_Mdl, String> idTypeName();

        @Editor.Path("IdNo")
        ValueProvider<LedgerMaster_Mdl, String> idNo();

        @Editor.Path("AccountName")
        ValueProvider<LedgerMaster_Mdl, String> accountName();

        @Editor.Path("RegRecId")
        ValueProvider<LedgerMaster_Mdl, String> regRecId();
    }

    final DmFind<LedgerMaster_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 10, true);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 200, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().displayName(), "Display Name", 200, "displayName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().address(), "Address", 150, "address");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().cityName(), "City", 100, "city");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().mobile(), "Mobile", 80, "mobile");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().idTypeName(), "ID Type", 100, "idTypeName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().idNo(), "ID No.", 80, "idNo");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().accountName(), "Account Name", 100, "accountName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().regRecId(), "Reg. No.", 100, "regRecId");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 200);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public LedgerMaster_Dsg() {
        ini();
        iniTxtHCity();
        iniTxtHIdType();
        iniTxtHInitial();
        iniTxtHIsActive();
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
        vrtInnerForm.setCellHeight(flw, "100%");
        vrtInnerForm.add(createBottom());
        flwForm.add(vrtInnerForm);
    }

    private DmUserControl createBottom() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);

        userControl.addGap(100);

        userControl.addGeneralBlog(lblPharmacyBalance);
        lblPharmacyBalance.setHeight("15Px");
        lblPharmacyBalance.getElement().getStyle().setColor("red");
        lblPharmacyBalance.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        lblPharmacyBalance.getElement().getStyle().setMarginTop(6, Style.Unit.PX);
        lblPharmacyBalance.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        lblPharmacyBalance.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
        lblPharmacyBalance.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        userControl.addTextBlog(modifiedByBlog, 100, true, false);

        return userControl;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "170px", null, null, DmPosition.CENTER, true);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Display Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHInitial));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "64%", null, "left", null, DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(1, Style.Unit.PCT);
        flwTd.add(vl.add(txtDisplayName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Manual Code");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Address");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAddress));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCity));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Mobile");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMobile));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 6th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("ID Type");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIdType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("ID No.");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtIdNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 7th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Account Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAccountName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 8th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Reg. No.");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRegNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Active");
        lblTemp.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsActive));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;
    }

}
