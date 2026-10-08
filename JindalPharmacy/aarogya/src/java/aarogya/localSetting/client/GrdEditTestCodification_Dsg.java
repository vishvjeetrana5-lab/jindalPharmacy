package aarogya.localSetting.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.Button;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.*;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmBase.self.shared.KeyField_T_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmHelpGrid.client.DmHelpGrid;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public abstract class GrdEditTestCodification_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtMachineTestName = new DmTextBox(50, true);
    final DmTextBox txtManualTestName = new DmTextBox(50, true);
    final FlowPanel flwSubTestSearch = new FlowPanel();
    final Button btnSubTestSearch = new Button("...");

    public GrdEditTestCodification_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHSubtest();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    //<editor-fold defaultstate="collapsed" desc="txtHSubTest">
    interface Help_PrpAcs extends KeyFieldHashCode_T_PrpAcs<KeyFieldExtended> {

        @Editor.Path("SharedPreparedBy")
        ValueProvider<KeyFieldExtended, String> subTest();
    }
    final DmHelpGrid<KeyFieldExtended, Help_PrpAcs> grdHSubTest = new DmHelpGrid(GWT.create(Help_PrpAcs.class), 18);

    private void iniTxtHSubtest() {
        grdHSubTest.getDmGrid().addTextColumn(grdHSubTest.getDmGrid().getPropertyAccess().name(), "Test Name", 242, "testName");
        grdHSubTest.getDmGrid().addTextColumn(grdHSubTest.getDmGrid().getPropertyAccess().subTest(), "Sub Test Name", 242, "subTest");
        grdHSubTest.getDmGrid().initializeGrid(grdHSubTest.getDmGrid().getPropertyAccess().fieldHashCode(), 484, 0, 1);

        grdHSubTest.getHelper().addSort("testName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        HorizontalPanel hrtPnl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "45px", null, null, DmPosition.CENTER, true);

        //======================================1st Row ==================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Machine Subtest Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMachineTestName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        // ======================================2nd Row ==================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Our Subtest Name");
        flwTd.add(lbl);
        flwTr.add(flwTd);

//        flwTd = new FlowPanel();
//        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
//        flwTd.add(vl.add(txtManualTestName));
//        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        hrtPnl = new HorizontalPanel();
        DmClient.setStyle(hrtPnl, "100%", "100%", null, null, DmPosition.NONE, false);
        hrtPnl.add(vl.add(txtManualTestName));
        hrtPnl.setCellWidth(txtManualTestName, "100%");
        DmClient.setStyle(flwSubTestSearch, "100%", "20px", "left", null, DmPosition.NONE, false);
        btnSubTestSearch.setWidth("20px");
        btnSubTestSearch.setHeight("20px");
        btnSubTestSearch.getElement().setAttribute("style", btnSubTestSearch.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;");
        btnSubTestSearch.addStyleName("dm-button-grad");
        flwSubTestSearch.add(vl.add(btnSubTestSearch));
        btnSubTestSearch.setTitle("Search SubTest");
        hrtPnl.add(vl.add(flwSubTestSearch));
        flwTd.add(hrtPnl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
