package aarogya.inventory.indentApproval.client;

import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum.*;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;

public abstract class ReasonGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtRemark = new DmTextBox(50, true);



    public ReasonGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);

        container.setStyleName("dm-FlwWorkingArea");
        container.add(section());
    }

    private FlowPanel section() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;
  

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99%", "30px", null, null, DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmPosition.NONE, false);
        lblTemp = new DmLabel("Remark");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "75%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);
  
        flwTable.add(flwTr);

        return flwTable;
    }

}
