package dataman.accounts.bankReconciliationEntry.client;

import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.bankReconciliationEntry.shared.TxtHBank_Mdl;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;

abstract class Setting_Dsg extends IsWidgetExtended {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwOk = new FlowPanel();
    final Button btnOk = new Button();
    final FlowPanel flwCancel = new FlowPanel();
    final Button btnCancel = new Button();

    final DmTextBox txtFromDate = new DmTextBox(15, true);
    final DmTextBox txtToDate = new DmTextBox(15, true);

    //<editor-fold defaultstate="collapsed" desc="txtHBankName">
    interface TxtHBank_PrpAcs extends KeyField_T_PrpAcs<TxtHBank_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHBank_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHBank_Mdl, String> manualCode();
    }
    final DmTextHelp<TxtHBank_Mdl, TxtHBank_PrpAcs> txtHBankName = new DmTextHelp(GWT.create(TxtHBank_PrpAcs.class), 5, true);

    private void iniTxtHBankName() {
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 150, "bankManualCode");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().cityName(), "City", 150, "City");
        txtHBankName.getDmGrid().initializeGrid(txtHBankName.getDmGrid().getPropertyAccess().code(), 500, 0, 1);
        txtHBankName.getHelper().addSort("Name", DmSortType.ASC);
        txtHBankName.getHelper().addSort("bankManualCode", DmSortType.ASC);
        txtHBankName.getHelper().addSort("City", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHStatus">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHStatus = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4);

    private void iniTxtHStatus() {
        txtHStatus.getDmGrid().addTextColumn(txtHStatus.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHStatus.getDmGrid().initializeGrid(txtHStatus.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHideEntry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHideEntry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniTxtHHideEntriesOnDate() {
        txtHHideEntry.getDmGrid().addTextColumn(txtHHideEntry.getDmGrid().getPropertyAccess().name(), "Type", 0, "Type");
        txtHHideEntry.getDmGrid().initializeGrid(txtHHideEntry.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

     //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 3);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 300, 0, 2);
        txtHSite.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
    public Setting_Dsg() {
        ini();
        iniTxtHBankName();
        iniTxtHHideEntriesOnDate();
        iniTxtHStatus();
        iniTxtHSite();
        iniToolContorls();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");

        vrtInnerForm.add(toolContainer);
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private void iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("OK");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");
        flwCancel.add(btnCancel);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "80px", null, null, DmPosition.CENTER, true);


        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Bank Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHBankName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Status");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHStatus));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Hide Entries Cleared On Date‎");
        lblTemp.getElement().getStyle().setMarginLeft(3, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHideEntry));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("From Date");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFromDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("To Date");
        lblTemp.getElement().getStyle().setMarginLeft(5, Style.Unit.PCT);
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtToDate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Site Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
