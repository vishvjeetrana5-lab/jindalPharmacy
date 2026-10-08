package aarogya.master.prescriptionCustomItemMast.client;

import dataman.dmBase.self.shared.DmEnum.*;
import aarogya.master.prescriptionCustomItemMast.shared.*;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class PrescriptionCustomItemMast_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().PRESCRIPTION_CUSTOM_ITEM_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";

    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final PopupPanel ppForm = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtName = new DmSuggestBox(200, 10, true, "name", DmDataType.STRING);
    final DmTextBox txtRegionalName = new DmTextBox(500, false);
    final DmTextBox txtShortName = new DmTextBox(10, false);
    final DmTextBoxNumeric txtPrntSerial = new DmTextBoxNumeric(0, true, 4, false);
    final DmTextBoxNumeric txtGraceMinute = new DmTextBoxNumeric(0, true, 3, false);

    final DmLabel lblGroup = new DmLabel("Group Name");

    final MenuItem mnuExportCard = new MenuItem("Export CustomItem");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHGroupName">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGroupName = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, false);

    private void iniTxtHGroupName() {
        txtHGroupName.getDmGrid().addTextColumn(txtHGroupName.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGroupName.getDmGrid().initializeGrid(txtHGroupName.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHGroupName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHNature">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHNature = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, true);

    private void iniTxtHNature() {
        txtHNature.getDmGrid().addTextColumn(txtHNature.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHNature.getDmGrid().initializeGrid(txtHNature.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<Find_Mdl, String> shortName();

        @Editor.Path("Nature")
        ValueProvider<Find_Mdl, String> nature();

        @Editor.Path("GroupName")
        ValueProvider<Find_Mdl, String> groupName();
    }

    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 1, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().shortName(), "Short Name ", 110, "shortName");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().nature(), "Nature", 300, "nature");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().groupName(), "Group Name", 300, "groupName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public PrescriptionCustomItemMast_Dsg() {
        ini();
        iniTxtHGroupName();
        iniTxtHNature();
        iniMenu();
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
        vrtInnerForm.add(createBottom());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "75%", "476px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Name (Regional)");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        txtRegionalName.setHeight("20px");
        txtRegionalName.addStyleName(DmEnum.RegionalLanguage.getValueFrom(DmClient.getRegionalLanguage()).getCssStyleName());
        flwTd.add(vl.add(txtRegionalName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Short Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Nature");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHNature));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print S.No.");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPrntSerial));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Grace Minute");
        lbl.getElement().getStyle().setMarginLeft(25, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtGraceMinute));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lblGroup.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblGroup.getElement().getStyle().setColor("blue");
        flwTd.add(lblGroup);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "80%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGroupName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl createBottom() {
        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        userControl.addGap(100);
        return userControl;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }

}
