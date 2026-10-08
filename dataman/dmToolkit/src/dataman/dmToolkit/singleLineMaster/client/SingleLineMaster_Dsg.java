package dataman.dmToolkit.singleLineMaster.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class SingleLineMaster_Dsg extends IsWidgetExtended {

    final DmClient.ViaList vl = new DmClient.ViaList();
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final FlowPanel flwForm = new FlowPanel();
    final PopupPanel pnlForm = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final DmSuggestBox txtField = new DmSuggestBox(200, 10, true, "Name", DmDataType.STRING);
    final DmLabel lblField = new DmLabel("Name");
    final DmFind<KeyFieldExtended, KeyField_PrpAcs> dmFind = new DmFind(GWT.create(KeyField_PrpAcs.class), 7);

    final MenuItem mnuExportCard = new MenuItem("Export Data");
    final Menu menu = new Menu();

    public SingleLineMaster_Dsg() {
        ini();
        iniMenu();
    }

    private void ini() {
        FlowPanel flw;

        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        flwForm.setStyleName("dm-FlwForm");
        flwForm.setHeight(String.valueOf(dmFind.getGridHeight() + 26) + "px"); //"204px"

        vrtInnerForm.add(vl.add(topControl));
        iniFind();
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Name", 1, "Name");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 0, 1);

        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTD;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "50%", "40px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "33%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(lblField);
        flwTr.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "65%", null, "left", null, DmPosition.NONE, false);
        txtField.setMandatory(true);
        flwTD.add(vl.add(txtField));
        txtField.setMaxLength(50);
        flwTr.add(flwTD);
        flwTable.add(flwTr);

        return flwTable;
    }

    private DmUserControl iniUserControl() {

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
