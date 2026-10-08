package aarogya.master.consultantMaster.client;

import aarogya.master.consultantMaster.shared.GrdRoomCategory_Mdl;
import aarogya.master.consultantMaster.shared.TxtHService_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;

public abstract class ServiceGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.DEFAULT, false, 10, false);

    interface TxtHService_PrpAcs extends KeyField_T_PrpAcs<TxtHService_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<TxtHService_Mdl, String> shortName();
    }
    final DmTextHelp<TxtHService_Mdl, TxtHService_PrpAcs> txtHService = new DmTextHelp(GWT.create(TxtHService_PrpAcs.class), 10, true);

    private void iniService() {
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHService.getDmGrid().initializeGrid(txtHService.getDmGrid().getPropertyAccess().code(), 500, 0, 1);

        txtHService.getHelper().addSort("name", DmSortType.ASC);

    }

    interface GrdRoomCategory_PrpAcs extends KeyField_T_PrpAcs<GrdRoomCategory_Mdl> {

        @Editor.Path("Amount")
        ValueProvider<GrdRoomCategory_Mdl, String> amount();

    }
    final DmGrid<GrdRoomCategory_Mdl, GrdRoomCategory_PrpAcs> grdRoomCategory = new DmGrid(GWT.create(GrdRoomCategory_PrpAcs.class));

    private Grid iniRoomCategory() {
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().name(), "Room Category", 0, null);
        grdRoomCategory.addTextColumn(grdRoomCategory.getPropertyAccess().amount(), "Rate", 100, null).setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        grdRoomCategory.initializeGrid(grdRoomCategory.getPropertyAccess().code(), 450, 170, 1);

        return grdRoomCategory.getGrid();
    }

    public ServiceGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniService();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "220px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "50px", "left", null, DmPosition.NONE, true);
        flwTr.add(entryArea());
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "170px", "left", null, DmPosition.NONE, true);
        flwTr.add(vl.add(iniRoomCategory()));
        flwTable.add(flwTr);

        return flwTable;
    }

    private FlowPanel entryArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "70%", "40px", null, null, DmPosition.CENTER, true);

        //Description
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Operation Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHService));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
