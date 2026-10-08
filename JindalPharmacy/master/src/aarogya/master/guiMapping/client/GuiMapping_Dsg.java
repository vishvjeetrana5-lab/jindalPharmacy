package aarogya.master.guiMapping.client;

import aarogya.master.guiMapping.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmMaster;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class GuiMapping_Dsg extends IsWidgetExtended {

    final String widgetName = new pmMaster().GUI_MAPPING_MASTER.caption;

//    public final static String preparedByBlog = "Prepared By";
//    public final static String modifiedByBlog = "Modified By";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmUserControl userControl = new DmUserControl();
    final DmTopControl topControl = new DmTopControl(vl, userControl);

    //<editor-fold defaultstate="collapsed" desc="txtHEntry">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEntry = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHEntry() {
        txtHEntry.getDmGrid().addTextColumn(txtHEntry.getDmGrid().getPropertyAccess().name(), "Entry", 0, "name");
        txtHEntry.getDmGrid().initializeGrid(txtHEntry.getDmGrid().getPropertyAccess().code(), 250, 0, 1);
        txtHEntry.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHGuiTemplate">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGuiTemplate = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniGuiTemplate() {
        txtHGuiTemplate.getDmGrid().addTextColumn(txtHGuiTemplate.getDmGrid().getPropertyAccess().name(), "Gui Template", 0, "name");
        txtHGuiTemplate.getDmGrid().initializeGrid(txtHGuiTemplate.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHGuiTemplate.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("Entrypoint")
        ValueProvider<Find_Mdl, String> entrypoint();

        @Editor.Path("Gui")
        ValueProvider<Find_Mdl, String> gui();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 15);

    private void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().entrypoint(), "EntryPoint", 150, "entrypoint");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gui(), "GUI", 950, "gui");

        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 2);

        dmFind.getHelper().addSort("EntryPoint", DmSortType.ASC);

    }
    //</editor-fold>

    public GuiMapping_Dsg() {
        ini();
        iniTxtHEntry();
        iniGuiTemplate();
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
//        vrtInnerForm.add(iniUserControl());
        vrtInnerForm.setCellHeight(flw, "100%");
        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "32%", "175px", null, null, DmPosition.CENTER, true);

        //Date
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Entrypoint");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEntry));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Holiday
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("GUI Template");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGuiTemplate));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }
//
//    private DmUserControl iniUserControl() {
//
//        userControl.addTextBlog(preparedByBlog, 100, false, true);
//        userControl.addGap(100);
//        userControl.addTextBlog(modifiedByBlog, 100, false, true);
//        return userControl;
//
//    }

}
