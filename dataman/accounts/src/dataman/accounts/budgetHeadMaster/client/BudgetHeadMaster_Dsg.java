package dataman.accounts.budgetHeadMaster.client;

import dataman.accounts.budgetHeadMaster.client.*;
import dataman.libraryMenu.self.shared.lmAccounts;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.accounts.budgetHeadMaster.shared.*;
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

abstract class BudgetHeadMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmAccounts().BUDGET_HEAD_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmLabel lblIsSytemGenrate = new DmLabel("");
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmSuggestBox txtHeadName = new DmSuggestBox(0, 10, false, "headName", DmDataType.STRING);
    final DmTextBox txtManualCode = new DmTextBox(20, true);
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHHeadUnder">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHeadUnder = new DmTextHelp(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class), 6, false);

    private void iniTxtHHeadUnder() {
        txtHHeadUnder.getDmGrid().addTextColumn(txtHHeadUnder.getDmGrid().getPropertyAccess().name(), "Head", 0, "headName");
        txtHHeadUnder.getDmGrid().initializeGrid(txtHHeadUnder.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHHeadUnder.getHelper().addSort("headName", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {
        @Editor.Path("HeadUnderName")
        ValueProvider<Find_Mdl, String> headUnderName();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Budget Head Name", 1, "headName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().headUnderName(), "Head Under", 200, "headUnderName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        dmFind.getHelper().addSort("headName", DmSortType.ASC);
        dmFind.getHelper().addSort("headUnderName", DmSortType.ASC);
    }
    //</editor-fold>

    public BudgetHeadMaster_Dsg() {
        super();
        ini();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px");

        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        iniTxtHHeadUnder();
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

        //Budget Head Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Head Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        txtHeadName.setMandatory(true);
        //txtHeadName.setMaxLength(50);
        flwTd.add(vl.add(txtHeadName));
        txtHeadName.setMaxLength(50);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Head Under
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Head Under");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "67%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHeadUnder));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //ManualCode
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        txtManualCode.setMandatory(true);
        flwTd.add(vl.add(txtManualCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
