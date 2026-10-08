package dataman.accounts.acDisplaySetting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.acDisplaySetting.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

public class AcDisplaySetting_Dsg {

    public static final String widgetName = "Setting";
    final PopupPanel pnlPopUp = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    DmClient.ViaList vl = new DmClient.ViaList();
    final Button btnOk = new Button();
    final Button btnCancel = new Button();

    final DmTextBox txtDateFrom = new DmTextBox(11, false);
    final DmTextBox txtDateTo = new DmTextBox(11, false);
    final DmTextBoxNumeric txtClosing = new DmTextBoxNumeric(2, false, 15, false);

    //<editor-fold defaultstate="collapsed" desc="txtHYN">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHZeroValue = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDetail = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNarration = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHOpening = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniTxtHYN() {
        txtHZeroValue.getDmGrid().addTextColumn(txtHZeroValue.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHZeroValue.getDmGrid().initializeGrid(txtHZeroValue.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHDetail.getDmGrid().addTextColumn(txtHDetail.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHDetail.getDmGrid().initializeGrid(txtHDetail.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHNarration.getDmGrid().addTextColumn(txtHNarration.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHNarration.getDmGrid().initializeGrid(txtHNarration.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHOpening.getDmGrid().addTextColumn(txtHOpening.getDmGrid().getPropertyAccess().name(), "Name", 100, "");
        txtHOpening.getDmGrid().initializeGrid(txtHOpening.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLedger">
    interface TxtHLedger_PrpAcs extends KeyField_T_PrpAcs<AcntLedger_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<AcntLedger_Mdl, String> manualCode();

        @Editor.Path("City")
        ValueProvider<AcntLedger_Mdl, String> city();
    }
    final DmTextHelpMulti<AcntLedger_Mdl, TxtHLedger_PrpAcs> txtHLedger = new DmTextHelpMulti(GWT.create(TxtHLedger_PrpAcs.class), 7);

    private void iniTxtHLedger() {
        txtHLedger.getDmGrid().addTextColumn(txtHLedger.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHLedger.getDmGrid().addTextColumn(txtHLedger.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 50, "manualCode");
        txtHLedger.getDmGrid().addTextColumn(txtHLedger.getDmGrid().getPropertyAccess().city(), "City", 100, "cityName");
        txtHLedger.getDmGrid().initializeGrid(txtHLedger.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHLedger.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHSite = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 7);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 150, "name");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 350, 0, 2);
        txtHSite.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public AcDisplaySetting_Dsg() {
        ini();
    }

    private void ini() {
        VerticalPanel vrtPanel = new VerticalPanel();
        DmClient.setStyle(vrtPanel, "100%", "100%", null, null, DmPosition.NONE, true);
        pnlPopUp.setWidth("500px");
        pnlPopUp.setHeight("160px");

        FlowPanel flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");

        vrtPanel.add(iniToolContainer());

        iniTxtHYN();
        iniTxtHLedger();
        iniTxtHSite();

        flw.add(workingArea());
        vrtPanel.add(flw);
        vrtPanel.setCellHeight(flw, "100%");
        pnlPopUp.add(vrtPanel);
    }

    private DmToolContainer iniToolContainer() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();
        toolContainer.setTitle(widgetName);
        toolContainer.setModeVisible(false);

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTabel, flwTr, flwTd;
        DmLabel lbl;

        flwTabel = new FlowPanel();
        DmClient.setStyle(flwTabel, "99%", "130px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("From Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDateFrom));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("To Date");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDateTo));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Show Zero Value A/c");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHZeroValue));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Show Detail");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDetail));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Show Narration");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNarration));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Include Opening");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOpening));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Ledger");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLedger));
        flwTr.add(flwTd);
        flwTabel.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Closing Stock");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtClosing));
        flwTr.add(flwTd);

        flwTabel.add(flwTr);

        return flwTabel;
    }
}
