package dataman.accounts.bulkChequePrinting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.bulkChequePrinting.shared.TxtHAccountName_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHBankName_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHVoucherNo_Mdl;
import dataman.accounts.bulkChequePrinting.shared.TxtHVoucherType_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class Settings_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();

    //<editor-fold defaultstate="collapsed" desc="POPUP">
    final PopupPanel popupPanel = new PopupPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="PANELS">
    final FlowPanel flwOk = new FlowPanel();
    final FlowPanel flwCancel = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BUTTONS">
    final Button btnCancel = new Button();
    final Button btnOk = new Button();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TEXTBOX">
    final DmTextBox txtFromDate = new DmTextBox(20, false);
    final DmTextBox txtToDate = new DmTextBox(11, true);
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TEXTHELPS">
    //<editor-fold defaultstate="collapsed" desc="TxtHVoucherType">

    interface TxtHVoucherType extends KeyField_T_PrpAcs<TxtHVoucherType_Mdl> {

    }

    final DmTextHelp<TxtHVoucherType_Mdl, TxtHVoucherType> txtHVoucherType = new DmTextHelp(GWT.create(TxtHVoucherType.class), 7, true);

    private void iniTxtHVoucherType() {
        txtHVoucherType.getDmGrid().addTextColumn(txtHVoucherType.getDmGrid().getPropertyAccess().name(), "Name", 1, "Description");
        txtHVoucherType.getDmGrid().initializeGrid(txtHVoucherType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHVoucherType.getHelper().addSort("Description", DmEnum.DmSortType.ASC);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHBankName">

    interface TxtHAccountName extends KeyField_T_PrpAcs<TxtHBankName_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHBankName_Mdl, String> City();

        @Editor.Path("SCode")
        ValueProvider<TxtHBankName_Mdl, String> SCode();

    }
    final DmTextHelp<TxtHBankName_Mdl, TxtHAccountName> txtHBankName = new DmTextHelp(GWT.create(TxtHAccountName.class), 7, true);

    private void iniTxtHBankName() {
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().City(), "City", 100, "CityName");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().SCode(), "Code", 50, "ManualCode");
        txtHBankName.getDmGrid().initializeGrid(txtHBankName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHBankName.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
        txtHBankName.getHelper().addSort("CityName", DmEnum.DmSortType.ASC);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHAccountName">

    interface TxtHAccount_PrpAcs extends KeyField_T_PrpAcs<TxtHAccountName_Mdl> {

        @Editor.Path("City")
        ValueProvider<TxtHAccountName_Mdl, String> City();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHAccountName_Mdl, String> manualCode();
    }

    final DmTextHelpMulti<TxtHAccountName_Mdl, TxtHAccount_PrpAcs> txtHAccountName = new DmTextHelpMulti(GWT.create(TxtHAccount_PrpAcs.class), 7, false);

    private void iniTxtHAccountName() {
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().City(), "City", 100, "CityName");
        txtHAccountName.getDmGrid().addTextColumn(txtHAccountName.getDmGrid().getPropertyAccess().manualCode(), "Code", 50, "ManualCode");
        txtHAccountName.getDmGrid().initializeGrid(txtHAccountName.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHAccountName.getHelper().addSort("name", DmEnum.DmSortType.ASC);
        txtHAccountName.getHelper().addSort("CityName", DmEnum.DmSortType.ASC);
    }
//</editor-fold>   
    //<editor-fold defaultstate="collapsed" desc="TxtHVoucherNo">

    interface TxtHVoucherNo extends KeyField_T_PrpAcs<TxtHVoucherNo_Mdl> {

    }

    final DmTextHelpMulti<TxtHVoucherNo_Mdl, TxtHVoucherNo> txtHVoucherNo = new DmTextHelpMulti(GWT.create(TxtHVoucherNo.class), 7, false);

    private void iniTxtHVoucherNo() {
        txtHVoucherNo.getDmGrid().addTextColumn(txtHVoucherNo.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        txtHVoucherNo.getDmGrid().initializeGrid(txtHVoucherNo.getDmGrid().getPropertyAccess().code(), 250, 0);
        txtHVoucherNo.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TxtHRecordsType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRecordsType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHRecordsType() {
        txtHRecordsType.getDmGrid().addTextColumn(txtHRecordsType.getDmGrid().getPropertyAccess().name(), "Name", 0, "NAME");
        txtHRecordsType.getDmGrid().initializeGrid(txtHRecordsType.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHRecordsType.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>

    public Settings_Dsg() {
        ini();
        iniTxtHBankName();
        iniTxtHVoucherType();
        iniTxtHAccountName();
        iniTxtHVoucherNo();
        iniTxtHRecordsType();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("400px");
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
        btnOk.setTitle("Back");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Back");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTd.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.getElement().getStyle().setMarginBottom(1, Style.Unit.PCT);
        flwTd.add(vl.add(viewArea()));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel viewArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(2, Style.Unit.PCT);
        lbl = new DmLabel("To Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Bank Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBankName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Voucher No");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHVoucherNo));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Exclude Acounts");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAccountName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Record Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRecordsType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
