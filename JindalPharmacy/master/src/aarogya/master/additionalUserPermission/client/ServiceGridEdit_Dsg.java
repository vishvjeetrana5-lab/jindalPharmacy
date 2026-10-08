package aarogya.master.additionalUserPermission.client;

import aarogya.master.additionalUserPermission.shared.TxtHService_Mdl;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.*;

public abstract class ServiceGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    //<editor-fold defaultstate="collapsed" desc="txtHServiceCat">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceCat = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHServiceCategory() {
        txtHServiceCat.getDmGrid().addTextColumn(txtHServiceCat.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHServiceCat.getDmGrid().initializeGrid(txtHServiceCat.getDmGrid().getPropertyAccess().code(), 285, 0, 1);
        txtHServiceCat.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHService">
    interface TxtHService_PrpAcs extends KeyField_T_PrpAcs<TxtHService_Mdl> {

        @Editor.Path("ServiceCatName")
        ValueProvider<TxtHService_Mdl, String> serviceCatName();

    }
    protected final DmTextHelp<TxtHService_Mdl, TxtHService_PrpAcs> txtHService = new DmTextHelp(GWT.create(TxtHService_PrpAcs.class), 10, false);

    private void iniTxtHService() {
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().name(), "Service", 0, "serviceName");
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().serviceCatName(), "Service Category Name", 125, "categoryName");
        txtHService.getDmGrid().initializeGrid(txtHService.getDmGrid().getPropertyAccess().code(), 400, 0, 1);
        txtHService.getHelper().addSort("serviceName", DmSortType.ASC);
    }
    //</editor-fold>

    public ServiceGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHService();
        iniTxtHServiceCategory();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Category");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceCat));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTable.add(flwTr);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHService));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
