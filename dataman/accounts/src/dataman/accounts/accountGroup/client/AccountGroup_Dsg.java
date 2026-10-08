package dataman.accounts.accountGroup.client;

import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.accounts.accountGroup.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class AccountGroup_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().ACCOUNT_GROUP.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmLabel lblIsSytemGenrate = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmSuggestBox txtGroupName = new DmSuggestBox(0, 10, false, "GroupName", DmDataType.STRING);
    final DmTextBox txtContraName = new DmTextBox();
    final Menu menu = new Menu();
    final MenuItem mnuExportData = new MenuItem("Export Data");

    //<editor-fold defaultstate="collapsed" desc="txtHGroupUnder">
    interface TxtHGroupUnder_PrpAcs extends KeyField_T_PrpAcs<TxtHGroupUnder_Mdl> {

        @Editor.Path("mainGroupName")
        ValueProvider<TxtHGroupUnder_Mdl, String> MainGroupName();

    }
    final DmTextHelp<TxtHGroupUnder_Mdl, TxtHGroupUnder_PrpAcs> txtHGroupUnder = new DmTextHelp(com.google.gwt.core.shared.GWT.create(TxtHGroupUnder_PrpAcs.class), 6, false);

    private void iniTxtHGroupUnder() {
        txtHGroupUnder.getDmGrid().addTextColumn(txtHGroupUnder.getDmGrid().getPropertyAccess().name(), "Group", 0, "GroupName");
        txtHGroupUnder.getDmGrid().addTextColumn(txtHGroupUnder.getDmGrid().getPropertyAccess().MainGroupName(), "Main Group", 150, "GroupNatureName");
        txtHGroupUnder.getDmGrid().initializeGrid(txtHGroupUnder.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGroupUnder.getHelper().addSort("GroupName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHMainGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMainGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    public void iniMainGroup() {
        txtHMainGroup.getDmGrid().addTextColumn(txtHMainGroup.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHMainGroup.getDmGrid().initializeGrid(txtHMainGroup.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //112
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    public void iniNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Name", 1, "Nature");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHNature.getHelper().addSort("Nature", DmSortType.ASC);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ContraGroupName")
        ValueProvider<Find_Mdl, String> contraGroupName();

        @Editor.Path("GroupUnderName")
        ValueProvider<Find_Mdl, String> groupUnderName();

        @Editor.Path("GroupNatureName")
        ValueProvider<Find_Mdl, String> groupNatureName();

        @Editor.Path("Nature")
        ValueProvider<Find_Mdl, String> nature();

        @Editor.Path("SysGroup")
        ValueProvider<Find_Mdl, String> sysGroup();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "A/c Group Name", 1, "GroupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().contraGroupName(), "Contra Group Name", 200, "ContraGroupName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().groupUnderName(), "Group Under", 200, "GroupUnderName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().groupNatureName(), "Main Group", 100, "GroupNature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Nature", 100, "Nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().sysGroup(), "System", 50, "isSysGroup");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);
        dmFind.getHelper().addSort("GroupName", DmSortType.ASC);
        dmFind.getHelper().addSort("ContraGroupName", DmSortType.ASC);
        dmFind.getHelper().addSort("GroupUnderName", DmSortType.ASC);
    }
    //</editor-fold>

    public AccountGroup_Dsg() {
        super();
        ini();
        iniMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHGroupUnder();
        iniMainGroup();
        iniNature();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(section1());
        vrtInnerForm.add(flw);

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGeneralBlog(lblIsSytemGenrate);
        lblIsSytemGenrate.getElement().getStyle().setColor("red");
        lblIsSytemGenrate.setHeight("25px");
        userControl.addGap(100);

        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);

    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "100px", null, null, DmPosition.CENTER, true);

        //A/c Group Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("A/c Group Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        txtGroupName.setMandatory(true);
        //txtGroupName.setMaxLength(50);
        flwTd.add(vl.add(txtGroupName));
        txtGroupName.setMaxLength(50);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Contra Group Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Contra Group Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        txtContraName.setMandatory(true);
        txtContraName.setMaxLength(50);
        flwTd.add(vl.add(txtContraName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Group Under
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Group Under");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroupUnder));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Main Group
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Main Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        txtHMainGroup.setMandatory(true);
        flwTd.add(vl.add(txtHMainGroup));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Nature
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        txtHNature.setMandatory(true);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportData);
        return menu;
    }
}
