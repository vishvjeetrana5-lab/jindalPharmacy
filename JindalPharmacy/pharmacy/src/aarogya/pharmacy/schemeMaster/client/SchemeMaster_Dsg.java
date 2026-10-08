package aarogya.pharmacy.schemeMaster.client;

import aarogya.pharmacy.schemeMaster.shared.GrdSchemeDetails_Mdl;
import aarogya.pharmacy.schemeMaster.shared.TxtHMedicine_Mdl;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmStatic.shared.DmShared;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmToolContainer.client.DmToolContainer;
import dataman.dmToolkit.isWidgetExtended.client.IsWidgetExtended;

abstract class SchemeMaster_Dsg extends IsWidgetExtended {

    final String widgetName = "Scheme Details";
    final DmClient.ViaList vl = new DmClient.ViaList();
    final DmToolContainer toolContainer = new DmToolContainer();
    int colIndexChkBox = 0;

    //<editor-fold defaultstate="collapsed" desc="Panels">
    final PopupPanel popupPanel = new PopupPanel();
    final VerticalPanel vrtInnerForm = new VerticalPanel();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="TEXTBOX">
    final DmTextBox txtUnit = new DmTextBox(50, false);
    final DmTextBox txtMedicineName = new DmTextBox();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="LABELS">
    DmLabel lblSaleQty = new DmLabel("");
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="BUTTON">
    final Button btnCancel = new Button();
    final Button btnOk = new Button();
//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="dmFind">

//    interface DmFind_PrpAcs extends KeyField_T_PrpAcs<Find_Mdl> {
//
//    }
//    final DmFind<Find_Mdl, DmFind_PrpAcs> dmFind = new DmFind(GWT.create(DmFind_PrpAcs.class), 18);
//
//    public void iniFind() {
//        dmFind.getDmGrid().addTextColumn(dmFind.getDmGrid().getPropertyAccess().name(), "Item Name", 250, "name");
//        dmFind.getDmGrid().initializeGrid(dmFind.getDmGrid().getPropertyAccess().code(), 960, 0, 1);
//
//        dmFind.getHelper().addSort("name", DmEnum.DmSortType.ASC);
//    }
    //</editor-fold>
    public SchemeMaster_Dsg() {
        ini();
    }

    private void ini() {
        FlowPanel flw;
        DmClient.setStyle(vrtInnerForm, "100%", "100%", null, null, DmEnum.DmPosition.NONE, true);
        popupPanel.setGlassEnabled(true);
        popupPanel.setWidth("500px");
        popupPanel.setHeight("");

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

        label = new InlineLabel("Close");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-times");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnCancel.setTitle("Close");
        btnCancel.addStyleName("dm-top-control-btnStyle");
        btnCancel.getElement().appendChild(aBtn.getElement());
        btnCancel.getElement().appendChild(label.getElement());
        btnCancel.setWidth("70px");

        label = new InlineLabel("Ok");
        aBtn = new Anchor();
        aBtn.addStyleName("fa fa-check");
        aBtn.getElement().getStyle().setColor("#7d7d7d");
        btnOk.setTitle("Ok");
        btnOk.addStyleName("dm-top-control-btnStyle");
        btnOk.getElement().appendChild(aBtn.getElement());
        btnOk.getElement().appendChild(label.getElement());
        btnOk.setWidth("70px");

        toolContainer.setTitle(widgetName);
        toolContainer.addWidget(vl.add(btnOk));
        toolContainer.addWidget(btnCancel);
        toolContainer.addGap();

        return toolContainer;
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        flwTr.add(topArea());

        flwTable.add(flwTr);
        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "", "left", null, DmEnum.DmPosition.NONE, true);
        flwTr.add(vl.add(createGrid()));

        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel topArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "", null, null, DmEnum.DmPosition.NONE, true);

        //-------------------------------------------------1st Row---------------------------------------//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Medicine Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "40%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(txtMedicineName);
        txtMedicineName.setEnabled(false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "11%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(lblSaleQty);
        lblSaleQty.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="grdSchemeDetail">
    interface Grd_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdSchemeDetails_Mdl> {

        @Editor.Path("SchemeOnQty")
        ValueProvider<GrdSchemeDetails_Mdl, String> schemeOnQty();

        @Editor.Path("SchemeFreeQty")
        ValueProvider<GrdSchemeDetails_Mdl, String> schemeFreeQty();

        @Editor.Path("FreeItemUnitName")
        ValueProvider<GrdSchemeDetails_Mdl, String> freeItemUnitName();

        @Editor.Path("Description")
        ValueProvider<GrdSchemeDetails_Mdl, String> description();

        @Editor.Path("IsChecked")
        ValueProvider<GrdSchemeDetails_Mdl, String> isChecked();

        @Editor.Path("FreeQty")
        ValueProvider<GrdSchemeDetails_Mdl, String> freeQty();

    }

    final DmGrid<GrdSchemeDetails_Mdl, Grd_PrpAcs> grdSchemeDetail = new DmGrid(GWT.create(Grd_PrpAcs.class));

    public Grid createGrid() {
        ColumnConfig<GrdSchemeDetails_Mdl, String> selectCheckBox;

        int i = 0;
        grdSchemeDetail.addRowNumberColumn();
        colIndexChkBox = ++i;
        selectCheckBox = grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().isChecked(), "", 30, "");
        ++i;
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().name(), "Medicine Name", 200, "");
        ++i;
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().freeItemUnitName(), "Unit", 60, "");
        ++i;
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().schemeOnQty(), "Sch. On", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().schemeFreeQty(), "Sch. Free", 75, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().freeQty(), "Free Qty.", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        ++i;
        grdSchemeDetail.addTextColumn(grdSchemeDetail.getPropertyAccess().description(), "Description", 130, "");
        grdSchemeDetail.initializeGrid(grdSchemeDetail.getPropertyAccess().fieldHashCode(), 710, 450, 3);

        selectCheckBox.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        selectCheckBox.setCellPadding(false);

        selectCheckBox.setCell(new AbstractCell<String>() {
            @Override
            public void render(Cell.Context context, String value, SafeHtmlBuilder sb) {
                int val = DmShared.vNull(grdSchemeDetail.getGrid().getStore().get(context.getIndex()).getIsChecked()).intValue();
                if (val == 1) {
                    sb.appendHtmlConstant("<span class = 'fa fa-check-square-o' style='font-size:1.3em'></span>");
                } else if (val == 0) {
                    sb.appendHtmlConstant("<span class = 'fa fa-square-o' style='font-size:1.3em'></span>");
                }
            }
        });

        return grdSchemeDetail.getGrid();
    }
    //</editor-fold>

}
