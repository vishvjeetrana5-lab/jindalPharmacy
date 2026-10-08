package aarogya.pharmacy.vehicleTypeMaster.client;

import aarogya.pharmacy.vehicleTypeMaster.shared.Find_Mdl;
import aarogya.projectMenu.self.shared.pmPharmacy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmFind.client.DmFind;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTopControl.client.DmTopControl;
import dataman.dmToolkit.dmUserControl.client.DmUserControl;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmSuggestBox.client.DmSuggestBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

abstract class VehicleTypeMaster_Dsg extends IsWidgetExtended {

    final String widgetName = new pmPharmacy().VEHICLE_TYPE_MASTER.caption;
    public final static String preparedByBlog = "Prepared By & Date";
    public final static String modifiedByBlog = "Modified By & Date";
    final DmUserControl userControl = new DmUserControl();
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmTopControl topControl = new DmTopControl(vl, userControl);
    final FlowPanel flwForm = new FlowPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final DmSuggestBox txtVehicleType = new DmSuggestBox(35, 10, true, "Name", DmDataType.STRING);

    //<editor-fold defaultstate="collapsed" desc="dmFind">
    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {

        @Editor.Path("GstType")
        ValueProvider<Find_Mdl, String> gstType();
    }
    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 7);

    public void iniFind() {
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Vehicle Type", 250, "name");
        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().gstType(), "GST Type", 100, "gstVehicleType");
        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 1260, 183, 1);
        dmFind.getHelper().addSort("name", DmSortType.ASC);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="txtHGstVehicleType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstVehicleType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true);

    private void iniTxtHGstVehicleType() {
        txtHGstVehicleType.getDmGrid().addTextColumn(txtHGstVehicleType.getDmGrid().getPropertyAccess().name(), "GST Type", 0, "gstVehicleType");
        txtHGstVehicleType.getDmGrid().initializeGrid(txtHGstVehicleType.getDmGrid().getPropertyAccess().code(), 300, 0, 1);
        txtHGstVehicleType.getHelper().addSort("gstVehicleType", DmEnum.DmSortType.ASC);
    }//</editor-fold>

    public VehicleTypeMaster_Dsg() {
        ini();
        iniTxtHGstVehicleType();
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

        userControl.addTextBlog(preparedByBlog, 200, false, true);
        userControl.addGap(100);
        userControl.addTextBlog(modifiedByBlog, 200, true, false);
        vrtInnerForm.add(userControl);

        flwForm.add(vrtInnerForm);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "35%", "65px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Vehicle Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtVehicleType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Gst Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstVehicleType));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
