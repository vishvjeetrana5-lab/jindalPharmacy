package dataman.accounts.chequePrintSetup.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.accounts.chequePrintSetup.shared.BankName_Mdl;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;

abstract class CopyPopup_Dsg {

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
    //<editor-fold defaultstate="collapsed" desc="TextHelp">
    //<editor-fold defaultstate="collapsed" desc="txtHBankName">
        interface BankName_PrpAcs extends KeyField_T_PrpAcs<BankName_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<BankName_Mdl, String> manualCode();

        @Editor.Path("CityName")
        ValueProvider<BankName_Mdl, String> cityName();

    }
    final DmTextHelp<BankName_Mdl, BankName_PrpAcs> txtHBankName = new DmTextHelp(com.google.gwt.core.shared.GWT.create(BankName_PrpAcs.class), 6, true);

    private void iniTxtHBank() {
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().name(), "Bank", 100, "bankName");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHBankName.getDmGrid().addTextColumn(txtHBankName.getDmGrid().getPropertyAccess().cityName(), "City", 100, "cityName");
        txtHBankName.getDmGrid().initializeGrid(txtHBankName.getDmGrid().getPropertyAccess().code(), 375, 0, 1);
        txtHBankName.getHelper().addSort("bankName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>
//</editor-fold>

    public CopyPopup_Dsg() {
        ini();
        iniTxtHBank();
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
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");
        flwOk.add(vl.add(btnOk));

        label = new InlineLabel("Close");
        label.setWidth("40px");
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

        /*====================================< 2nd Row >====================================*/
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

        return flwTable;
    }

}
