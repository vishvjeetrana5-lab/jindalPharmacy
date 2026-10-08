package dataman.webJobs.smsManager.client;

import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextArea.client.DmTextArea;

public abstract class SmsManagerGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextArea txtMsgArea = new DmTextArea();

    public SmsManagerGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", null, null, null, DmPosition.NONE, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", "370px", "left", null, DmPosition.NONE, false);
        txtMsgArea.setHeight("100%");
        flwTr.add(vl.add(txtMsgArea));
        flwTable.add(flwTr);
        /*====================================< ******* >====================================*/
        return flwTable;
    }
}
