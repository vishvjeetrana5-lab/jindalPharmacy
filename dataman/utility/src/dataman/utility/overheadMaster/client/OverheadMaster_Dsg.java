package dataman.utility.overheadMaster.client;

import dataman.libraryMenu.self.shared.lmUtility;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.utility.overheadMaster.shared.Find_Mdl;

abstract class OverheadMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new lmUtility().OVERHEAD_MASTER.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmSuggestBox txtOverheadName = new DmSuggestBox(50, 10, true, "Name", DmDataType.STRING);
    final DmTextBox txtManualCode = new DmTextBox(10, true);
    final DmTextBox txtStatus = new DmTextBox(50, true);
    final DmLabel lblStatus = new DmLabel("");

    final MenuItem mnuExportCard = new MenuItem("Export OverHead");
    final Menu menu = new Menu();

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtItem_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> sharedModifiedBy();

    }
    final DmTextHelp<KeyFieldExtended, TxtItem_PrpAcs> txtHItemName = new DmTextHelp(GWT.create(TxtItem_PrpAcs.class), 7, false);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().sharedModifiedBy(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().code(), 300, 0, 1);

        txtHItemName.getHelper().addSort("NAME", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("manualCode")
        ValueProvider<Find_Mdl, String> manualCode();

        @Editor.Path("ItemName")
        ValueProvider<Find_Mdl, String> itemName();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Overhead Name", 1, "Name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 200, "manualCode");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().itemName(), "Item Name", 200, "itemName");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 940, 183, 1);

        dmFind.getHelper().addSort("Name", DmSortType.ASC);
        dmFind.getHelper().addSort("manualCode", DmSortType.ASC);
    }

    //</editor-fold>
    public OverheadMaster_Dsg() {
        ini();
        iniTxtHItemName();
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
        vrtInnerForm.setCellHeight(flw, "100%");

        userControl.addTextBlog(preparedByBlog, 100, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 100, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "90px", null, null, DmPosition.CENTER, true);

        //Nursing Station
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "40%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(lblStatus));
//        lblStatus.getElement().getStyle().setMarginLeft(115, Style.Unit.PX);
        lblStatus.getElement().getStyle().setMarginBottom(20, Style.Unit.PX);
        lblStatus.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        lblStatus.getElement().getStyle().setColor("RED");
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Overhead Name
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Overhead Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "45%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtOverheadName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Manual Code
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Manual Code");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "45%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtManualCode));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //ITEM NAME
        flwTR = new FlowPanel();
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Item Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "45%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHItemName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

    private Menu iniMenu() {
        menu.add(mnuExportCard);
        return menu;
    }
}
