package dataman.accounts.voucherAdjustmentEntry.client;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;

public abstract class VoucherAdjustmentEntryGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtAcManualCode = new DmTextBox();
    final DmTextBox txtAcName = new DmTextBox();
    final DmTextBox txtCity = new DmTextBox();
    final DmTextBox txtNarration = new DmTextBox();
    final DmTextBox txtDebit = new DmTextBox();
    final DmTextBox txtCredit = new DmTextBox();
    final DmTextBoxNumeric txtAdjustment = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtItemAdjustment = new DmTextBoxNumeric(2, true, 12, false);
    
    final DmLabel lblAdjustment = new DmLabel("");
    final DmLabel lblItemAdjustment = new DmLabel("");

    public VoucherAdjustmentEntryGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lblTemp;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "99.5%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTable.getElement().getStyle().setMarginTop(3, Style.Unit.PX);
        //====================================< 1st Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("A/c Code");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAcManualCode));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 2nd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("A/c Name");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAcName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 3rd Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("City");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCity));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 4th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Narration");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtNarration));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Debit");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "right", null, DmEnum.DmPosition.NONE, false);
        lblTemp = new DmLabel("Credit");
        flwTd.add(lblTemp);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtDebit));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "right", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCredit));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//
        //====================================< 5th Row >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblAdjustment.setText("Account Adj.");
        lblAdjustment.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblAdjustment.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblAdjustment.getElement().getStyle().setColor("blue");
        flwTd.add(lblAdjustment);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "right", null, DmEnum.DmPosition.NONE, false);
        lblItemAdjustment.setText("Item Adj.");
        lblItemAdjustment.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        lblItemAdjustment.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
        lblItemAdjustment.getElement().getStyle().setColor("blue");
        flwTd.add(lblItemAdjustment);
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ------- >====================================//
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAdjustment));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "45%", null, "right", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtItemAdjustment));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        //====================================< ******* >====================================//

        return flwTable;

    }

}
