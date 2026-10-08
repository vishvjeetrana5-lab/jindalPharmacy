package dataman.utility.walkThroughMaster.client;

import dataman.dmBase.self.shared.*;
import dataman.dmToolkit.self.shared.*;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelpMulti.client.DmTextHelpMulti;

public abstract class AuthorityGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtauthorityName = new DmTextBox(35, true);
    final DmTextBoxNumeric txtSrlNo = new DmTextBoxNumeric(DmTextBoxNumeric.DecimalNature.ROUNDOFF, true, 3, true);

    //<editor-fold defaultstate="collapsed" desc="txtHLinkedUser">
    final DmTextHelpMulti<KeyFieldExtended, KeyField_PrpAcs> txtHLinkedUser = new DmTextHelpMulti(GWT.create(KeyField_PrpAcs.class), 6, true);

    public void iniTxtHLinkedUser() {
        txtHLinkedUser.getDmGrid().addTextColumn(txtHLinkedUser.getDmGrid().getPropertyAccess().name(), "User Name", 0, "Name");
        txtHLinkedUser.getDmGrid().initializeGrid(txtHLinkedUser.getDmGrid().getPropertyAccess().code(), 150, 0, 2);
        txtHLinkedUser.getHelper().addSort("name", DmSortType.ASC);

    }
    //</editor-fold>

    AuthorityGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);

        iniTxtHLinkedUser();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "60px", null, null, DmPosition.CENTER, true);

        //============================== Upper Area ==================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "60px", "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", "60px", "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(upperArea()));
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        //============================== END ========================================

        return flwTable;
    }

    private FlowPanel upperArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "60px", null, null, DmPosition.CENTER, true);
        //================================ 1st Row ====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("S.No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSrlNo));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //============================== END =========================================
        //================================ 2nd Row ====================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Authority");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtauthorityName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        //============================== END =========================================
        //================================ 3rd Row ===================================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked User");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "70%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkedUser));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //============================== END =========================================
        return flwTable;
    }

}
