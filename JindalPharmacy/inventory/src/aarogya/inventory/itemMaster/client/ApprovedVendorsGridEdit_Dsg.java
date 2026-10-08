package aarogya.inventory.itemMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.inventory.itemMaster.shared.TxtHApprovedVendors_Mdl;

public abstract class ApprovedVendorsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtRate = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBox txtRateUnit = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHApprovedVendors">
    interface GridApprovedVendors_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHApprovedVendors_Mdl> {

        @Editor.Path("CityName")
        ValueProvider<TxtHApprovedVendors_Mdl, String> cityName();

        @Editor.Path("ManualCode")
        ValueProvider<TxtHApprovedVendors_Mdl, String> manualCode();
    }
    final DmTextHelp<TxtHApprovedVendors_Mdl, GridApprovedVendors_PrpAcs> txtHApprovedVendors = new DmTextHelp(GWT.create(GridApprovedVendors_PrpAcs.class), 6, true);

    private void iniTxtHApprovedVendors() {
        txtHApprovedVendors.getDmGrid().addTextColumn(txtHApprovedVendors.getDmGrid().getPropertyAccess().name(), "Name", 250, "name");
        txtHApprovedVendors.getDmGrid().addTextColumn(txtHApprovedVendors.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "");
        txtHApprovedVendors.getDmGrid().addTextColumn(txtHApprovedVendors.getDmGrid().getPropertyAccess().cityName(), "City", 100, "");
        txtHApprovedVendors.getDmGrid().initializeGrid(txtHApprovedVendors.getDmGrid().getPropertyAccess().fieldHashCode(), 500, 0, 1);
        txtHApprovedVendors.getHelper().addSort("name", DmEnum.DmSortType.ASC);

    }
    //</editor-fold>

    public ApprovedVendorsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHApprovedVendors();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "42px", null, null, DmPosition.CENTER, true);
        
        //////////////////////////// 1st Row ////////////////////////
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        
        flwTd = new FlowPanel();
        flwTd.getElement().getStyle().setMarginTop(-2, Style.Unit.PX);
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Approved Suppliers");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHApprovedVendors));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, null, null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRate));
        flwTr.add(flwTd);
        
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        txtRateUnit.setReadOnly(true);
        txtRateUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTd.add(txtRateUnit);
        flwTr.add(flwTd);
        
        flwTable.add(flwTr);

        return flwTable;
    }

}
