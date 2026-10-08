package dataman.utility.userMaster.client;

import dataman.utility.userMaster.shared.GrdLinkedUser_Mdl;
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

public abstract class LinkedUserEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtShortName = new DmTextBox();

    //<editor-fold defaultstate="collapsed" desc="txtHLinkedUser">
    interface txtHLinkedUserData_PrpAcs extends KeyField_T_PrpAcs<GrdLinkedUser_Mdl> {

        @Editor.Path("Description")
        ValueProvider<GrdLinkedUser_Mdl, String> Description();
    }

    final DmTextHelp<GrdLinkedUser_Mdl, txtHLinkedUserData_PrpAcs> txtHLinkedUserData = new DmTextHelp(GWT.create(txtHLinkedUserData_PrpAcs.class), 10, true);

    private void iniTxtHLinkedUserData() {
        txtHLinkedUserData.getDmGrid().addTextColumn(txtHLinkedUserData.getDmGrid().getPropertyAccess().name(), "Name", 80, "Code");
        txtHLinkedUserData.getDmGrid().addTextColumn(txtHLinkedUserData.getDmGrid().getPropertyAccess().Description(), "Description", 210, "Name");
        txtHLinkedUserData.getDmGrid().initializeGrid(txtHLinkedUserData.getDmGrid().getPropertyAccess().code(), 350, 310, 1);
        txtHLinkedUserData.getHelper().addSort("Code", DmSortType.ASC, "User_Name");
        txtHLinkedUserData.getHelper().addSort("Name", DmSortType.ASC, "Description");
    }
    //</editor-fold>

    public LinkedUserEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workinArea());
        iniTxtHLinkedUserData();
    }

    private FlowPanel workinArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "40px", null, null, DmPosition.CENTER, true);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Linked User");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHLinkedUserData));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< New Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "33%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "65%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtShortName));
        txtShortName.setEnabled(false);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;
    }
}
