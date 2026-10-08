package aarogya.master.eod.client;

import aarogya.master.eod.shared.GrdLabConsumptionDetail_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
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
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmLabel.client.DmLabel;

abstract class LabConsumption_Dsg {

    final String widgetName = "Lab Consumption";
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

    public LabConsumption_Dsg() {
        iniGrdLCDeleteDetail();
        iniGrdLCPostingDetail();
        iniGrdPendingDates();
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
        txtForDate.getElement().setPropertyString("placeholder", "Upto Date");
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
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "350px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "200px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", "200px", "left", null, DmPosition.NONE, false);
        flwTd.add(grdPendingDate.getGrid());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "92%", "200px", "left", null, DmPosition.NONE, false);
        flwTd.add(grdLCPostingDetail.getGrid());
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "20px", "left", null, DmPosition.NONE, true);
        flwTr.setStyleName("dm-StripLabel");
        lbl = new DmLabel("Already Posted Detail :-");
        lbl.setHeight("15px");
        lbl.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.add(lbl);
        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "130px", "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "130px", "left", null, DmPosition.NONE, false);
        flwTd.add(grdLCDeleteDetail.getGrid());
        flwTr.add(flwTd);

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
        flwTr.add(grdLCDeleteDetail.getGrid());
        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="Grd Lab Consumption Posting">
    interface GridLCPostingDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLabConsumptionDetail_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<GrdLabConsumptionDetail_Mdl, String> departmentName();

        @Editor.Path("UnitName")
        ValueProvider<GrdLabConsumptionDetail_Mdl, String> unitName();

        @Editor.Path("Quantity")
        ValueProvider<GrdLabConsumptionDetail_Mdl, String> quantity();
    }
    final DmGrid<GrdLabConsumptionDetail_Mdl, GridLCPostingDetail_PrpAcs> grdLCPostingDetail = new DmGrid(GWT.create(GridLCPostingDetail_PrpAcs.class));

    private Grid iniGrdLCPostingDetail() {
        grdLCPostingDetail.addRowNumberColumn();
        grdLCPostingDetail.addTextColumn(grdLCPostingDetail.getPropertyAccess().departmentName(), "Department", 300, "");
        grdLCPostingDetail.addTextColumn(grdLCPostingDetail.getPropertyAccess().name(), "Item", 660, "");
        grdLCPostingDetail.addTextColumn(grdLCPostingDetail.getPropertyAccess().unitName(), "Unit", 80, "");
        grdLCPostingDetail.addTextColumn(grdLCPostingDetail.getPropertyAccess().quantity(), "Quantity", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdLCPostingDetail.initializeGrid(grdLCPostingDetail.getPropertyAccess().fieldHashCode(), 1159, 200);

        return grdLCPostingDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Lab Consumption Delete">
    interface GridLCDeleteDetail_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdLabConsumptionDetail_Mdl> {

        @Editor.Path("DepartmentName")
        ValueProvider<GrdLabConsumptionDetail_Mdl, String> departmentName();

        @Editor.Path("voucherNo")
        ValueProvider<GrdLabConsumptionDetail_Mdl, String> voucherNo();

        @Editor.Path("UnitName")
        ValueProvider<GrdLabConsumptionDetail_Mdl, String> unitName();

        @Editor.Path("Quantity")
        ValueProvider<GrdLabConsumptionDetail_Mdl, String> quantity();
    }
    final DmGrid<GrdLabConsumptionDetail_Mdl, GridLCDeleteDetail_PrpAcs> grdLCDeleteDetail = new DmGrid(GWT.create(GridLCDeleteDetail_PrpAcs.class));

    private Grid iniGrdLCDeleteDetail() {
        grdLCDeleteDetail.addRowNumberColumn();
        grdLCDeleteDetail.addTextColumn(grdLCDeleteDetail.getPropertyAccess().voucherNo(), "Voucher No.", 120, "");
        grdLCDeleteDetail.addTextColumn(grdLCDeleteDetail.getPropertyAccess().departmentName(), "Department", 300, "");
        grdLCDeleteDetail.addTextColumn(grdLCDeleteDetail.getPropertyAccess().name(), "Item", 650, "");
        grdLCDeleteDetail.addTextColumn(grdLCDeleteDetail.getPropertyAccess().unitName(), "Unit", 70, "");
        grdLCDeleteDetail.addTextColumn(grdLCDeleteDetail.getPropertyAccess().quantity(), "Quantity", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdLCDeleteDetail.initializeGrid(grdLCDeleteDetail.getPropertyAccess().fieldHashCode(), 1260, 350);

        return grdLCDeleteDetail.getGrid();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Grd Pending Dates">
    interface GridPendingDate_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

    }
    final DmGrid<KeyFieldExtended, GridPendingDate_PrpAcs> grdPendingDate = new DmGrid(GWT.create(GridPendingDate_PrpAcs.class));

    private Grid iniGrdPendingDates() {
        grdPendingDate.addTextColumn(grdPendingDate.getPropertyAccess().name(), "Pending Date", 79, "");
        grdPendingDate.initializeGrid(grdPendingDate.getPropertyAccess().fieldHashCode(), 99, 200);

        return grdPendingDate.getGrid();
    }
    //</editor-fold>

}
