package aarogya.master.eod.client;

import aarogya.master.eod.shared.GrdServiceDetail_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmStatic.shared.DmShared;

abstract class RoomPosting_Dsg {

    final String widgetName = "Room Posting";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final PopupPanel popupPanel = new PopupPanel();
    final DmToolContainer toolContainer = new DmToolContainer();
    final VerticalPanel vrtInnerForm = new VerticalPanel();

    final DmTextBox txtForDate = new DmTextBox(11, true);

    final FlowPanel flwPosting = new FlowPanel();
    final Button btnPosting = new Button();
    final FlowPanel flwDelete = new FlowPanel();
    final Button btnDelete = new Button();
    final Button btnClose = new Button();
    final FlowPanel flwWorkingArea = new FlowPanel();

    public RoomPosting_Dsg() {
        iniGrdRoomPostingDelete();
        iniGrdRoomPostingDetail();
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("1260px");
        popupPanel.setHeight("380px");

        vrtInnerForm.add(iniToolContorls());
        flw = new FlowPanel();
        flw.setStyleName("dm-FlwWorkingArea");
        flw.add(workingArea());
        vrtInnerForm.add(flw);
        vrtInnerForm.setCellHeight(flw, "100%");
        popupPanel.add(vrtInnerForm);
    }

    private DmToolContainer iniToolContorls() {
        Anchor aBtn;
        InlineLabel label;

        txtForDate.setWidth("110px");
        txtForDate.getElement().getStyle().setFontSize(15, Style.Unit.PX);
        txtForDate.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        txtForDate.getElement().setPropertyString("placeholder", "Date");
        txtForDate.setEnabled(false);

        label = new InlineLabel("Posting");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-floppy-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnPosting.setTitle("Posting");
        btnPosting.addStyleName("dm-top-control-btnStyle");
        btnPosting.getElement().appendChild(aBtn.getElement());
        btnPosting.getElement().appendChild(label.getElement());
        btnPosting.setWidth("40px");
        flwPosting.add(vl.add(btnPosting));

        label = new InlineLabel("Delete");
        label.setWidth("40px");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-trash-o");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnDelete.setTitle("Delete");
        btnDelete.addStyleName("dm-top-control-btnStyle");
        btnDelete.getElement().appendChild(aBtn.getElement());
        btnDelete.getElement().appendChild(label.getElement());
        btnDelete.setWidth("40px");
        flwDelete.add(btnDelete);

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnClose.setTitle("Close: (Escap)");
        btnClose.addStyleName("dm-top-control-btnStyle");
        btnClose.getElement().appendChild(aBtn.getElement());
        btnClose.getElement().appendChild(label.getElement());
        btnClose.setWidth("40px");

        toolContainer.addWidget(txtForDate);
        toolContainer.addWidget(flwPosting);
        toolContainer.addWidget(flwDelete);
        toolContainer.addWidget(btnClose);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "350px", "left", null, DmPosition.NONE, false);
        DmClient.setStyle(flwWorkingArea, "100%", "350px", "left", null, DmPosition.NONE, false);
        flwWorkingArea.add(postingArea());
        flwWorkingArea.add(deleteArea());
        flwTr.add(flwWorkingArea);
        flwTable.add(flwTr);

        return flwTable;
    }

    protected FlowPanel postingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(grdRoomPostingDetail.getGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    protected FlowPanel deleteArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTr.add(grdRoomPostingDelete.getGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Room Posting Detail">
    interface GridRoomPostingDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServiceDetail_Mdl> {

        @Editor.Path("IpdRecId")
        ValueProvider<GrdServiceDetail_Mdl, String> ipdRecId();

        @Editor.Path("PatientName")
        ValueProvider<GrdServiceDetail_Mdl, String> patientName();

        @Editor.Path("UhidRecId")
        ValueProvider<GrdServiceDetail_Mdl, String> uhidRecId();

        @Editor.Path("Age")
        ValueProvider<GrdServiceDetail_Mdl, String> age();

        @Editor.Path("Gender")
        ValueProvider<GrdServiceDetail_Mdl, String> gender();

        @Editor.Path("AdmitDate")
        ValueProvider<GrdServiceDetail_Mdl, String> admitDate();

        @Editor.Path("SrvName")
        ValueProvider<GrdServiceDetail_Mdl, String> srvName();

        @Editor.Path("RoomCatName")
        ValueProvider<GrdServiceDetail_Mdl, String> roomCatName();

        @Editor.Path("OrgName")
        ValueProvider<GrdServiceDetail_Mdl, String> orgName();

        @Editor.Path("NetAmount")
        ValueProvider<GrdServiceDetail_Mdl, String> netAmount();

    }
    final DmGrid<GrdServiceDetail_Mdl, GridRoomPostingDetail_PrpAcs> grdRoomPostingDetail = new DmGrid(GWT.create(GridRoomPostingDetail_PrpAcs.class));

    private Grid iniGrdRoomPostingDetail() {
        grdRoomPostingDetail.addRowNumberColumn();
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().name(), "Room No.", 110, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().ipdRecId(), "Reg. No.", 100, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().patientName(), "Patient Name", 130, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().uhidRecId(), "UHID No.", 100, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().age(), "Age", 50, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().gender(), "Gender", 50, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().admitDate(), "Admit Date", 75, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().roomCatName(), "Room Category", 110, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().orgName(), "Organization", 100, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().srvName(), "Service", 110, "");
        grdRoomPostingDetail.addTextColumn(grdRoomPostingDetail.getPropertyAccess().netAmount(), "Net Amt.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdRoomPostingDetail.initializeGrid(grdRoomPostingDetail.getPropertyAccess().fieldHashCode(), 1260, 350, 11);

        return grdRoomPostingDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Room Posting Delete">
    interface GridRoomPostingDelete_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdServiceDetail_Mdl> {

        @Editor.Path("IpdRecId")
        ValueProvider<GrdServiceDetail_Mdl, String> ipdRecId();

        @Editor.Path("PatientName")
        ValueProvider<GrdServiceDetail_Mdl, String> patientName();

        @Editor.Path("UhidRecId")
        ValueProvider<GrdServiceDetail_Mdl, String> uhidRecId();

        @Editor.Path("Age")
        ValueProvider<GrdServiceDetail_Mdl, String> age();

        @Editor.Path("Gender")
        ValueProvider<GrdServiceDetail_Mdl, String> gender();

        @Editor.Path("AdmitDate")
        ValueProvider<GrdServiceDetail_Mdl, String> admitDate();

        @Editor.Path("SrvName")
        ValueProvider<GrdServiceDetail_Mdl, String> srvName();

        @Editor.Path("RoomCatName")
        ValueProvider<GrdServiceDetail_Mdl, String> roomCatName();

        @Editor.Path("OrgName")
        ValueProvider<GrdServiceDetail_Mdl, String> orgName();

        @Editor.Path("NetAmount")
        ValueProvider<GrdServiceDetail_Mdl, String> netAmount();

        @Editor.Path("IsChecked")
        ValueProvider<GrdServiceDetail_Mdl, String> isChecked();

    }
    final DmGrid<GrdServiceDetail_Mdl, GridRoomPostingDelete_PrpAcs> grdRoomPostingDelete = new DmGrid(GWT.create(GridRoomPostingDelete_PrpAcs.class));

    private Grid iniGrdRoomPostingDelete() {
        ColumnConfig<GrdServiceDetail_Mdl, String> selectCheckBox;
        selectCheckBox = grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().isChecked(), "", 30, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().name(), "Room No.", 110, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().ipdRecId(), "Reg. No.", 100, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().patientName(), "Patient Name", 130, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().uhidRecId(), "UHID No.", 100, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().age(), "Age", 50, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().gender(), "Gender", 50, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().admitDate(), "Admit Date", 75, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().roomCatName(), "Room Category", 110, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().orgName(), "Organization", 100, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().srvName(), "Service", 110, "");
        grdRoomPostingDelete.addTextColumn(grdRoomPostingDelete.getPropertyAccess().netAmount(), "Net Amt.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdRoomPostingDelete.initializeGrid(grdRoomPostingDelete.getPropertyAccess().fieldHashCode(), 1260, 350, 11);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdRoomPostingDelete.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdRoomPostingDelete.getGrid();
    }
    //</editor-fold>

}
