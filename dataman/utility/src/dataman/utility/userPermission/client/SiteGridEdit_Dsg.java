package dataman.utility.userPermission.client;

import dataman.utility.userPermission.shared.GrdSite_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class SiteGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtShortName = new DmTextBox(20, false);

    //<editor-fold defaultstate="collapsed" desc="txtHSiteGrid">
    interface txtHSiteData_PrpAcs extends KeyField_T_PrpAcs<GrdSite_Mdl> {

        @Editor.Path("ShortName")
        ValueProvider<GrdSite_Mdl, String> ShortName();
    }
    final DmTextHelp<GrdSite_Mdl, txtHSiteData_PrpAcs> txtHSite = new DmTextHelp(GWT.create(txtHSiteData_PrpAcs.class), 10, true);

    private void iniTxtHSiteGrid() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Name", 215, "Name");
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().ShortName(), "Code", 80, "ManualCode");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 350, 310, 1);

        txtHSite.getHelper().addSort("Name", DmSortType.ASC);
    }
//</editor-fold>

    public SiteGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(section1());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHSiteGrid();
    }

    private FlowPanel section1() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSite));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        txtShortName.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
