package aarogya.master.organizationMaster.client;

import aarogya.master.organizationMaster.shared.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ServicesGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtPSNo = new DmTextBoxNumeric(0, false, 3, false);
    final DmTextBoxNumeric txtDiscountPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtDiscount = new DmTextBoxNumeric(2, true, 12, false);

    //<editor-fold defaultstate="collapsed" desc="iniTxtHServiceCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCat = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHServiceCategory() {
        txtHServiceCat.getDmGrid().addTextColumn(txtHServiceCat.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHServiceCat.getDmGrid().initializeGrid(txtHServiceCat.getDmGrid().getPropertyAccess().code(), 235, 0, 1);
        txtHServiceCat.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="iniTxtHService">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHService = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHService() {
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHService.getDmGrid().initializeGrid(txtHService.getDmGrid().getPropertyAccess().code(), 235, 0, 1);
        txtHService.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public ServicesGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHServiceCategory();
        iniTxtHService();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "384px", null, null, DmPosition.CENTER, true);

        // Control Detail
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "110px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        // Grid Tab
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "267px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniRoomCategory()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "105px", null, null, DmPosition.CENTER, true);

        //Category
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceCat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Services
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHService));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Rate
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        txtRate.setAlignment(ValueBoxBase.TextAlignment.LEFT);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Print S.No.");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(1, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "10%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtPSNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //Discoubnt
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "35%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Discount %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        txtDiscountPer.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTd.add(vl.add(txtDiscountPer));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "22%", null, "left", null, DmPosition.NONE, false);
        txtDiscount.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        flwTd.add(vl.add(txtDiscount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

    interface GrdRoomCate_PrpAcs extends KeyFieldHashCode_T_PrpAcs<GrdRoomCategory_Mdl> {

        @Editor.Path("Rate")
        ValueProvider<GrdRoomCategory_Mdl, String> rate();

        @Editor.Path("DiscountAmount")
        ValueProvider<GrdRoomCategory_Mdl, String> discount();

        @Editor.Path("DiscountPercentage")
        ValueProvider<GrdRoomCategory_Mdl, String> percent();

    }
    final DmGrid<GrdRoomCategory_Mdl, GrdRoomCate_PrpAcs> grdRoomCategory = new DmGrid(GWT.create(GrdRoomCate_PrpAcs.class));

    private Grid iniRoomCategory() {
        grdRoomCategory.addRowNumberColumn();
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().name(), "Room Category", 150, "");
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().rate(), "Rate", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().percent(), "Discount %", 80, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().discount(), "Discount", 70, "").setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdRoomCategory.initializeGrid(grdRoomCategory.getPropertyAccess().fieldHashCode(), 425, 269, 2);
        return grdRoomCategory.getGrid();
    }

}
