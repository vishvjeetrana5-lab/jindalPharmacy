package dataman.webJobs.userEmailConfiguration.client;

import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public abstract class UserEmailPopupGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtEmail = new DmTextBox(50, true);

    public UserEmailPopupGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);

        container.add(workingArea());
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbla;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "20px", null, null, DmPosition.CENTER, true);

        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, null, null, DmPosition.NONE, true);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbla = new DmLabel("Email ");
        flwTD.add(lbla);
        flwTR.add(flwTD);
        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "80%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtEmail));
        flwTR.add(flwTD);
        flwTable.add(flwTR);
        return flwTable;
    }

}
