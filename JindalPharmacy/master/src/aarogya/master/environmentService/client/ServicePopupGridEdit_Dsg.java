package aarogya.master.environmentService.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class ServicePopupGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    //<editor-fold defaultstate="collapsed" desc="txtHConsultant">    
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDefConsultant = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true, true);

    private void iniTxtHDefConsultant() {
        txtHDefConsultant.getDmGrid().addTextColumn(txtHDefConsultant.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHDefConsultant.getDmGrid().initializeGrid(txtHDefConsultant.getDmGrid().getPropertyAccess().code(), 350, 0, 1);
        txtHDefConsultant.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHService">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHService = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true);

    private void iniTxtHSerivce() {
        txtHService.getDmGrid().addTextColumn(txtHService.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHService.getDmGrid().initializeGrid(txtHService.getDmGrid().getPropertyAccess().code(), 350, 0, 1);

        txtHService.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    public ServicePopupGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHSerivce();
        iniTxtHDefConsultant();

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "53px", null, null, DmPosition.CENTER, true);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTr.getElement().getStyle().setMarginTop(2, Style.Unit.PX);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Service");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "82%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHService));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Consultant");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "82%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDefConsultant));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
