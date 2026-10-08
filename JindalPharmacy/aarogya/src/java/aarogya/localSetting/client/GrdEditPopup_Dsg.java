package aarogya.localSetting.client;

import aarogya.localSetting.shared.TxtHMenuText_Mdl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class GrdEditPopup_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    interface DmtxtHmenuText_PrpAcs extends KeyField_T_PrpAcs<TxtHMenuText_Mdl> {

        @Editor.Path("MenuText")
        ValueProvider<TxtHMenuText_Mdl, String> menuText();

        @Editor.Path("MenuModuleName")
        ValueProvider<TxtHMenuText_Mdl, String> menuModuleName();

    }
    final DmTextHelp<TxtHMenuText_Mdl, DmtxtHmenuText_PrpAcs> txtHMenuText = new DmTextHelp(com.google.gwt.core.client.GWT.create(DmtxtHmenuText_PrpAcs.class), 10, true);

    private void initxtHMenuText() {

        txtHMenuText.getDmGrid().addTextColumn(txtHMenuText.getDmGrid().getPropertyAccess().menuText(), "Menu Text", 150, "MnuText");
        txtHMenuText.getDmGrid().addTextColumn(txtHMenuText.getDmGrid().getPropertyAccess().menuModuleName(), "Module Name", 100, "MnuModule");
        txtHMenuText.getDmGrid().initializeGrid(txtHMenuText.getDmGrid().getPropertyAccess().code(), 600, 0, 1);
        txtHMenuText.getHelper().addSort("MnuText", DmEnum.DmSortType.ASC);
        txtHMenuText.getHelper().addSort("MnuModule", DmEnum.DmSortType.ASC);

    }

    final DmTextBox txtModule = new DmTextBox(50, true);

    final DmTextBox txtReportName = new DmTextBox(50, true);

    public GrdEditPopup_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {

        super(grid, title, width, height);

        initxtHMenuText();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");

    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "95%", "60px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Menu Text");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMenuText));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Module Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtModule));
        flwTr.add(flwTd);
        flwTable.add(flwTr);


        /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Print Menu Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtReportName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }

}
