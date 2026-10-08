package dataman.utility.voucherType.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.Grid;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.utility.voucherType.shared.*;

public abstract class WebJobsGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtTemplateType = new DmTextBox(50, false);

    //<editor-fold defaultstate="collapsed" desc="txtHTemplateName">
    interface Grid_TemplateName extends KeyField_T_PrpAcs<TxtHTemplate_Mdl> {
    }

    final DmTextHelp<TxtHTemplate_Mdl, Grid_TemplateName> txtHTemplateName = new DmTextHelp(GWT.create(Grid_TemplateName.class), 7, true);

    public Grid iniTemplateName() {
        txtHTemplateName.getDmGrid().addTextColumn(txtHTemplateName.getDmGrid().getPropertyAccess().name(), "Template Name", 1, "name");
        txtHTemplateName.getDmGrid().initializeGrid(txtHTemplateName.getDmGrid().getPropertyAccess().code(), 250, 200, 1);
        txtHTemplateName.getHelper().addSort("name", DmSortType.ASC);
        return txtHTemplateName.getDmGrid().getGrid();

    }
//</editor-fold>

    public WebJobsGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTemplateName();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "97%", "40px", null, null, DmPosition.CENTER, true);

        //Name
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Template Name");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtHTemplateName));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        //Type
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Template Type");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "70%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtTemplateType));
        flwTR.add(flwTD);
        flwTable.add(flwTR);

        return flwTable;
    }

}
