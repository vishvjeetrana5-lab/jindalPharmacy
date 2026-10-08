package dataman.utility.guiConfig.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.*;

public abstract class SiteGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHSite">
    interface TxtHSite_PrpAcs extends KeyField_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedModifiedBy")
        ValueProvider<KeyFieldExtended, String> manualCode();
    }
    final DmTextHelp<KeyFieldExtended, TxtHSite_PrpAcs> txtHSite = new DmTextHelp(GWT.create(TxtHSite_PrpAcs.class), 4, true);

    private void iniTxtHSite() {
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().name(), "Site Name", 0, "name");
        txtHSite.getDmGrid().addTextColumn(txtHSite.getDmGrid().getPropertyAccess().manualCode(), "Short Name", 85, "manualCode");
        txtHSite.getDmGrid().initializeGrid(txtHSite.getDmGrid().getPropertyAccess().code(), 225, 0, 1);
        txtHSite.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    public SiteGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHSite();
        container.add(section());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel section() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "20px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHSite));
        flwTR.add(flwTD);

        flwTable.add(flwTR);

        return flwTable;
    }
}
