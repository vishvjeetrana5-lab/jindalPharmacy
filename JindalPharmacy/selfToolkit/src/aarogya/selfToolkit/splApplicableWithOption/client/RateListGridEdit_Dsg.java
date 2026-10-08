package aarogya.selfToolkit.splApplicableWithOption.client;

import aarogya.selfToolkit.splApplicableWithOption.shared.RateList_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class RateListGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    //<editor-fold defaultstate="collapsed" desc="txtHRateList">
    interface RateList_PrpAcs extends KeyFieldHashCode_T_PrpAcs<RateList_Mdl> {

        @Editor.Path("RateListType")
        ValueProvider<RateList_Mdl, String> rateListType();

    }
    final DmTextHelp<RateList_Mdl, RateList_PrpAcs> txtHRateList = new DmTextHelp(com.google.gwt.core.client.GWT.create(RateList_PrpAcs.class), 10, true);

    private void iniTxtHRateList() {
        txtHRateList.getDmGrid().addTextColumn(txtHRateList.getDmGrid().getPropertyAccess().name(), "Name", 120, "Name");
        txtHRateList.getDmGrid().addTextColumn(txtHRateList.getDmGrid().getPropertyAccess().rateListType(), "Type", 70, "name");
        txtHRateList.getDmGrid().initializeGrid(txtHRateList.getDmGrid().getPropertyAccess().fieldHashCode(), 300, 0, 1);
        txtHRateList.getHelper().addSort("name", DmSortType.ASC);
        txtHRateList.getHelper().addSort("rateListType", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDtApplicable">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDtApplicable = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 8);

    private void iniTxtHApplicableDate() {
        txtHDtApplicable.getDmGrid().addTextColumn(txtHDtApplicable.getDmGrid().getPropertyAccess().name(), "Name", 0, "applicableFrom", DmDataType.DATE);
        txtHDtApplicable.getDmGrid().initializeGrid(txtHDtApplicable.getDmGrid().getPropertyAccess().code(), 200, 0, 1);

        txtHDtApplicable.getHelper().addSort("applicableFrom", DmSortType.DESC);
    }
    //</editor-fold>

    public RateListGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHApplicableDate();
        iniTxtHRateList();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "45px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Rate List");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRateList));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Get Rate From");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDtApplicable));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
