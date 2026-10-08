package aarogya.master.consultantSharingMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class SharingDetailGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBoxNumeric txtHeaderConsAmount = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtHeaderRefAmount = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtLineConsAmount = new DmTextBoxNumeric(2, false, 10, false);
    final DmTextBoxNumeric txtLineRefAmount = new DmTextBoxNumeric(2, false, 10, false);

    //<editor-fold defaultstate="collapsed" desc="txtHEmpanelment">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHEmpanelment = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHEmpanelment() {
        txtHEmpanelment.getDmGrid().addTextColumn(txtHEmpanelment.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHEmpanelment.getDmGrid().initializeGrid(txtHEmpanelment.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHEmpanelment.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceCat">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCat = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHServiceCat() {
        txtHServiceCat.getDmGrid().addTextColumn(txtHServiceCat.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHServiceCat.getDmGrid().initializeGrid(txtHServiceCat.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHServiceCat.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHServiceGroup">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceGroup = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHServiceGroup() {
        txtHServiceGroup.getDmGrid().addTextColumn(txtHServiceGroup.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHServiceGroup.getDmGrid().initializeGrid(txtHServiceGroup.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHServiceGroup.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHService">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHService = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHService() {
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().name(), "Name", 100, "name");
        txtHService.getDmGrid().initializeGrid(txtHService.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHService.getHelper().addSort("name", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHeaderConsType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHeaderConsType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHHeaderConsType() {
        txtHHeaderConsType.getDmGrid().addTextColumn(txtHHeaderConsType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHHeaderConsType.getDmGrid().initializeGrid(txtHHeaderConsType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHHeaderConsType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHHeaderRefType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHeaderRefType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHHeaderRefType() {
        txtHHeaderRefType.getDmGrid().addTextColumn(txtHHeaderRefType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHHeaderRefType.getDmGrid().initializeGrid(txtHHeaderRefType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHHeaderRefType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLineConsType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLineConsType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHLineConsType() {
        txtHLineConsType.getDmGrid().addTextColumn(txtHLineConsType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHLineConsType.getDmGrid().initializeGrid(txtHLineConsType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHLineConsType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHLineRefType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHLineRefType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniTxtHLineRefType() {
        txtHLineRefType.getDmGrid().addTextColumn(txtHLineRefType.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHLineRefType.getDmGrid().initializeGrid(txtHLineRefType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
        txtHLineRefType.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHModule">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHModule = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 5, true, false);

    private void iniTxtHModule() {
        txtHModule.getDmGrid().addTextColumn(txtHModule.getDmGrid().getPropertyAccess().name(), "Type", 0, "Name");
        txtHModule.getDmGrid().initializeGrid(txtHModule.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
        txtHModule.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHRoomCategory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHRoomCategory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHRoomCategory() {
        txtHRoomCategory.getDmGrid().addTextColumn(txtHRoomCategory.getDmGrid().getPropertyAccess().name(), "Name", 0, "Name");
        txtHRoomCategory.getDmGrid().initializeGrid(txtHRoomCategory.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHRoomCategory.getHelper().addSort("Name", DmSortType.ASC);
    }
    //</editor-fold>

    public SharingDetailGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHEmpanelment();
        iniTxtHModule();
        iniTxtHServiceCat();
        iniTxtHServiceGroup();
        iniTxtHService();
        iniTxtHHeaderConsType();
        iniTxtHHeaderRefType();
        iniTxtHLineConsType();
        iniTxtHLineRefType();
        iniTxtHRoomCategory();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "210px", null, null, DmPosition.CENTER, true);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Room Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHRoomCategory));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Empanelment");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHEmpanelment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Module Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHModule));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceCat));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Group");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceGroup));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHService));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Header Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHeaderConsType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHeaderConsAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Header Referred Cons.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHeaderRefType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHeaderRefAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Performing Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLineConsType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLineConsAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Performing Referred Cons.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "27%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLineRefType));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "15%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        lbl.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "28%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtLineRefAmount));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
