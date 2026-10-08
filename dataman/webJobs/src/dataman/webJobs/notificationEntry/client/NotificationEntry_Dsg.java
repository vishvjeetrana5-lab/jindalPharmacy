package dataman.webJobs.notificationEntry.client;

import dataman.dmBase.self.shared.DmEnum.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.libraryMenu.self.shared.lmWebJobs;
import dataman.webJobs.notificationEntry.shared.Find_Mdl;

abstract class NotificationEntry_Dsg extends IsWidgetExtended {

    final String widgetName = new lmWebJobs().NOTIFICATION_ENTRY.caption;
    public final static String preparedByBlog = "Prepared By";
    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    final DmTextArea txtNotification = new DmTextArea(500, false);
    final DmTextBox txtEntryDate = new DmTextBox(11, false);
    final DmTextBox txtExpiryDate = new DmTextBox(11, false);

//    final MenuItem mnuManageSite = new MenuItem("Manage Site");
//    final MenuItem mnuExportCard = new MenuItem("Export Department");
//    final Menu menu = new Menu();
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("EntryDate")
        ValueProvider<Find_Mdl, String> entryDate();

        @Editor.Path("ExpiryDate")
        ValueProvider<Find_Mdl, String> expiryDate();

    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 24);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Notification", 0, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entryDate(), "Entry Date", 100, "entryDate");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().expiryDate(), "Expiry Date", 170, "expiryDate");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);

        // dmFind.getHelper().addSort("NAME", DmSortType.ASC);
        dmFind.getHelper().addSort("entryDate", DmSortType.ASC);
        //   dmFind.getHelper().addSort("nature", DmSortType.ASC, "Nature");

    }
    //</editor-fold>

    public NotificationEntry_Dsg() {
        ini();
        // iniOtherMenu();
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
        flw.add(workinArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "62%", "476px", null, null, DmPosition.CENTER, true);

         flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "40%", "15px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", "15px", "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Notification");
//        lbl.getElement().setAttribute("style", lbl.getElement().getAttribute("style")
//                + ";font-weight:bold");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "200px", "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "200px", "left", null, DmPosition.NONE, false);
        txtNotification.getElement().getStyle().setHeight(200, Style.Unit.PX);
        flwTd.add(vl.add(txtNotification));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Short Name
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.getElement().getStyle().setMarginTop(5, Style.Unit.PCT);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entry Date");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtEntryDate));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Expiry Date");
        lbl.getElement().getStyle().setMarginLeft(19, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtExpiryDate));
        flwTr.add(flwTd);

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

//    private Menu iniOtherMenu() {
//        menu.add(mnuManageSite);
//        menu.add(mnuExportCard);
//        return menu;
//    }
}
