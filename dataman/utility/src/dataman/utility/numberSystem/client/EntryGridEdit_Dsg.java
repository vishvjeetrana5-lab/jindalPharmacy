package dataman.utility.numberSystem.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class EntryGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtNumberFormat = new DmTextBox(150, true);
    final DmTextBox txtCounterFormat = new DmTextBox(10, true);

    //<editor-fold defaultstate="collapsed" desc="txtHSiteCode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSiteCode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10);

    public void iniTxtHSiteCode() {
        txtHSiteCode.getDmGrid().addTextColumn(txtHSiteCode.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHSiteCode.getDmGrid().initializeGrid(txtHSiteCode.getDmGrid().getPropertyAccess().code(), 200, 0, 1); //244
        txtHSiteCode.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsSiteWise">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsSiteWise = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    public void iniTxtHIsSiteWise() {
        txtHIsSiteWise.getDmGrid().addTextColumn(txtHIsSiteWise.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIsSiteWise.getDmGrid().initializeGrid(txtHIsSiteWise.getDmGrid().getPropertyAccess().code(), 100, 0, 1); //244
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHIsYearWise">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHIsYearWise = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true);

    public void iniTxtHIsYearWise() {
        txtHIsYearWise.getDmGrid().addTextColumn(txtHIsYearWise.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHIsYearWise.getDmGrid().initializeGrid(txtHIsYearWise.getDmGrid().getPropertyAccess().code(), 100, 0, 1); //244
    }
    //</editor-fold>

    public EntryGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHSiteCode();
        iniTxtHIsSiteWise();
        iniTxtHIsYearWise();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "110px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "110px", "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add(leftSection());
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", "110px", "left", null, DmPosition.NONE, true);
        flwTd.add(vl.add(createGrid()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        return flwTable;
    }

    private FlowPanel leftSection() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "80px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Number Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtNumberFormat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Counter Format");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        txtCounterFormat.getElement().setPropertyString("placeholder", "0000000000");
        flwTd.add(vl.add(txtCounterFormat));

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Site Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSiteCode));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reset Site Wise");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsSiteWise));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "31%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Reset Year Wise");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "17%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHIsYearWise));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="dmNuberFormatGrd">
    final DmGrid<KeyFieldExtended, KeyField_PrpAcs> dmNuberFormatGrd = new DmGrid(com.google.gwt.core.shared.GWT.create(KeyField_PrpAcs.class));

    private Grid createGrid() {
        dmNuberFormatGrd.addRowNumberColumn();
        dmNuberFormatGrd.addTextColumn(dmNuberFormatGrd.getPropertyAccess().name(), "Number Format", 150, "");
        dmNuberFormatGrd.initializeGrid(dmNuberFormatGrd.getPropertyAccess().code(), 177, 110, 2);

        return dmNuberFormatGrd.getGrid();
    }
    //</editor-fold>
}
