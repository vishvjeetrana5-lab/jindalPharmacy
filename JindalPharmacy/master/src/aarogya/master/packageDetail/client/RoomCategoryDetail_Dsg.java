package aarogya.master.packageDetail.client;

import aarogya.master.packageDetail.shared.GrdRoomCategory_Mdl;
import com.google.gwt.core.client.GWT;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;

abstract class RoomCategoryDetail_Dsg {

    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
    final FlowPanel flwClose = new FlowPanel();
    final Button btnClose = new Button();

    public RoomCategoryDetail_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", null, null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("700px");
        popupPanel.setHeight("285px");
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

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("70px");;
        flwClose.add(btnClose);
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "255px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "255px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniGrdRoomCategory()));
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Room Category">
    interface GrdCkLst_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRoomCategory_Mdl> {

        @Editor.Path("PackageCode")
        ValueProvider<GrdRoomCategory_Mdl, String> packageCode();

        @Editor.Path("FromDate")
        ValueProvider<GrdRoomCategory_Mdl, String> fromDate();

        @Editor.Path("ToDate")
        ValueProvider<GrdRoomCategory_Mdl, String> toDate();

        @Editor.Path("Active")
        ValueProvider<GrdRoomCategory_Mdl, String> active();

        @Editor.Path("PackageAmount")
        ValueProvider<GrdRoomCategory_Mdl, String> packageAmount();
    }
    final DmGrid<GrdRoomCategory_Mdl, GrdCkLst_PrpAcs> grdRoomCategory = new DmGrid(GWT.create(GrdCkLst_PrpAcs.class));

    private Grid iniGrdRoomCategory() {
        grdRoomCategory.addRowNumberColumn();
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().name(), "Room Category", 1, "");
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().packageCode(), "Package Code", 90, "");
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().fromDate(), "From Date", 77, "");
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().toDate(), "To Date", 77, "");
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().active(), "Active", 47, "");
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().packageAmount(), "Amount", 90, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdRoomCategory.initializeGrid(grdRoomCategory.getPropertyAccess().fieldHashCode(), 700, 255, 2);

        return grdRoomCategory.getGrid();
    }
    //</editor-fold>

}
