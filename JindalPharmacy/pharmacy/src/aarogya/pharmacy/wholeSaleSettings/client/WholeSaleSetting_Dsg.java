package aarogya.pharmacy.wholeSaleSettings.client;

import aarogya.pharmacy.wholeSaleSettings.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract public class WholeSaleSetting_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().WHOLE_SALE_ENVIRONMENT_SETTING.caption;
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmTextBox txtCity = new DmTextBox();
    final DmTextBox txtManualCode = new DmTextBox();
    final DmUserControl userControl = new DmUserControl();
//    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final MenuItem mnuManageSite = new MenuItem("Manage Site");
    final MenuItem mnuExportCard = new MenuItem("Export Department");
    final Menu menu = new Menu();
    final Button btnSave = new Button();
    final Button btnClose = new Button();
    final DmTextBoxNumeric txtEwayBillLimit = new DmTextBoxNumeric(2, true, 12, false);
    final DmToolContainer toolContainer = new DmToolContainer();

    //<editor-fold defaultstate="collapsed" desc="txtHCentralStore">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCentralStore = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 5, true);

    public void iniCentralStore() {
        txtHCentralStore.getDmGrid().addTextColumn(txtHCentralStore.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHCentralStore.getDmGrid().initializeGrid(txtHCentralStore.getDmGrid().getPropertyAccess().code(), 250, 0, 1);

        txtHCentralStore.getHelper().addSort("Name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("IsActive")
        ValueProvider<Find_Mdl, String> isActive();

        @Editor.Path("Description")
        ValueProvider<Find_Mdl, String> description();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 14);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Property", 0, "Name");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("Name", DmEnum.DmSortType.ASC);

    }

    //</editor-fold>
    public WholeSaleSetting_Dsg() {

        ini();
        iniOtherMenu();
        iniCentralStore();
    }

    private Menu iniOtherMenu() {
        menu.add(mnuManageSite);
        menu.add(mnuExportCard);
        return menu;
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");
//        vrtInnerForm.add(vl.add(topControl));
        vrtInnerForm.add(iniToolContorls());
//        iniFind();

        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        label = new InlineLabel("Close");
        //label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        // aBtn.getElement().getStyle().setColor("red");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnSave.addStyleName("dm-top-control-btnStyle");
        btnSave.setTitle("Save");
        btnSave.getElement().appendChild(aBtn.getElement());
        btnSave.getElement().appendChild(label.getElement());
        btnSave.setWidth("70px");

        label = new InlineLabel("Refresh");
        //  label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        // aBtn.getElement().getStyle().setColor("orange");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.setTitle("Close");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");

        toolContainer.addWidget(btnSave);
        toolContainer.addWidget(btnClose);

        toolContainer.addGap();
        toolContainer.setTitle(widgetName);

        return toolContainer;
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "90%", "170px", null, null, DmEnum.DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Central Store For Sale");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtHCentralStore));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
//===========================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("E-way Bill Limit");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "22%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTD.add(vl.add(txtEwayBillLimit));
        txtEwayBillLimit.getElement().setAttribute("style", txtEwayBillLimit.getElement().getAttribute("style")
                + ";text-alignt:right; ");
        txtEwayBillLimit.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }

}
