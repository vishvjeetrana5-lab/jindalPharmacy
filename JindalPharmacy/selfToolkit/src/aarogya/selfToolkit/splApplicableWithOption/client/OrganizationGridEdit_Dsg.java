package aarogya.selfToolkit.splApplicableWithOption.client;

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

public abstract class OrganizationGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    //<editor-fold defaultstate="collapsed" desc="txtHOrganization">
    interface TxtHOrganization_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> shortName();

    }
    final DmTextHelp<KeyFieldExtended, TxtHOrganization_PrpAcs> txtHOrganization = new DmTextHelp(GWT.create(TxtHOrganization_PrpAcs.class), 7, true);

    private void iniOrganization() {
        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHOrganization.getDmGrid().addTextColumn(txtHOrganization.getDmGrid().getPropertyAccess().shortName(), "Short Name", 100, "shortName");
        txtHOrganization.getDmGrid().initializeGrid(txtHOrganization.getDmGrid().getPropertyAccess().code(), 400, 0, 1);

        txtHOrganization.getHelper().addSort("name", DmSortType.ASC);
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

    public OrganizationGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHApplicableDate();
        iniOrganization();
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
        lbl = new DmLabel("Orgnaization");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHOrganization));
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
