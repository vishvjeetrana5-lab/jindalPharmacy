/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aarogya.pharmacy.purchaseOrderApproval.client;

import dataman.dmBase.self.shared.DmEnum.*;

import com.google.gwt.user.client.ui.FlowPanel;

import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;

import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

/**
 *
 * @author dataman
 */
public abstract class UserEntryPOGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBoxNumeric txtValue = new DmTextBoxNumeric(2, false, 5, false);

    public UserEntryPOGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);

        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;
        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "25px", null, null, DmPosition.NONE, true);


        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "46%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Ordered Qty :-");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "54%", null, "left", null, DmPosition.NONE, false);
     
        flwTd.add(vl.add(txtValue));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        return flwTable;

    }

}
