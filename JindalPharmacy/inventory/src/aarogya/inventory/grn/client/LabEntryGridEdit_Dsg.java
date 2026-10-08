package aarogya.inventory.grn.client;

import com.google.gwt.dom.client.Style;
import dataman.dmBase.self.shared.DmEnum.*;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmRemarkTextBox.client.DmRemarkTextBox;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;

public abstract class LabEntryGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();
    final DmTextBox txtItemImpurity = new DmTextBox(100, false);
    final DmTextBoxNumeric txtRange1 = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBoxNumeric txtRange2 = new DmTextBoxNumeric(4, false, 12, false);
    final DmTextBox txtMainFoundValue = new DmTextBox(250, false);
    final FlowPanel flwPopup = new FlowPanel();

    final FlowPanel flwContainer = new FlowPanel();
    final FlowPanel flwRangeContainer = new FlowPanel();
    final FlowPanel flwHelp = new FlowPanel();
    final FlowPanel flwRemark = new FlowPanel();
    final FlowPanel flwRangeTextBox = new FlowPanel();
    final FlowPanel flwRange = new FlowPanel();
    final DmTextBox txtRangeUnit = new DmTextBox(10, false);
    final DmTextBox txtRangeUnit1 = new DmTextBox(10, false);
    final DmTextBox txtParameterUnit = new DmTextBox(10, false);
    final DmTextBox txtRangeTextBoxUnit = new DmTextBox(10, false);
    final DmTextBoxNumeric txtlabQty = new DmTextBoxNumeric(4, true, 12, false);
    final DmTextBoxNumeric txtlabAmt = new DmTextBoxNumeric(4, true, 12, false);

    final DmTextBoxNumeric txtRangeTextBox = new DmTextBoxNumeric(4, false, 12, true);
    final DmRemarkTextBox txtRemark = new DmRemarkTextBox(250, false);

    //<editor-fold defaultstate="collapsed" desc="txtHHelp">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHHelp = new DmTextHelp(com.google.gwt.core.client.GWT.create(KeyField_PrpAcs.class), 4, true);

    private void iniTxtHHHelp() {
        txtHHelp.getDmGrid().addTextColumn(txtHHelp.getDmGrid().getPropertyAccess().name(), "Name", 300, "");
        txtHHelp.getDmGrid().initializeGrid(txtHHelp.getDmGrid().getPropertyAccess().code(), 400, 0);
        txtHHelp.getHelper().addSort("", DmSortType.ASC);
    }

    //</editor-fold>
    public LabEntryGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
        iniTxtHHHelp();
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTR, flwTD;
        DmLabel lbl;
        HorizontalPanel hrtPnl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "100%", "75px", null, null, DmPosition.NONE, true);

        //=========================================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "80%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Parameter");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "1%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Unit");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //=========================================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "79%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtItemImpurity));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "1%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "20%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(txtParameterUnit);
        txtParameterUnit.setReadOnly(true);
        txtParameterUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 1st Row >====================================*/
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "99%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(range());
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //============================================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "99%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Found Value");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //============================================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "99%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(help());
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //============================================================================
        //=========================================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Qty.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Amt.");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        //=========================================================================
        flwTR = new FlowPanel();
        DmClient.setStyle(flwTR, "100%", null, "left", null, DmPosition.NONE, false);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "48%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtlabQty));
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTD.add(lbl);
        flwTR.add(flwTD);

        flwTD = new FlowPanel();
        DmClient.setStyle(flwTD, "47%", null, "left", null, DmPosition.NONE, false);
        flwTD.add(vl.add(txtlabAmt));
        flwTR.add(flwTD);

        flwTable.add(flwTR);
        /*====================================< 1st Row >====================================*/

        return flwTable;
    }

    private FlowPanel help() {
        FlowPanel flwTr, flwTd;

        //===============================< 1st Row >===============================//   
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHHelp));
        flwTr.add(flwTd);

        //===============================< ******* >===============================//
        flwHelp.add(flwTr);
        flwContainer.add(flwHelp);
        remark();
        numericTextBox();

        return flwContainer;
    }

    private void remark() {
        FlowPanel flwTr, flwTd;
        DmLabel lbl;

        //===============================< 1st Row >===============================//   
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "100%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRemark));
        flwTr.add(flwTd);

        //===============================< ******* >===============================//
        flwRemark.add(flwTr);
    }

    private void numericTextBox() {
        FlowPanel flwTr, flwTd;
        DmLabel lbl;

        //===============================< 1st Row >===============================//   
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtRangeTextBox));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtRangeTextBoxUnit);
        txtRangeTextBoxUnit.setReadOnly(true);
        txtRangeTextBoxUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTr.add(flwTd);

        //===============================< ******* >===============================//
        flwRangeTextBox.add(flwTr);
    }

    private FlowPanel range() {
        FlowPanel panel, flwTr, flwTd;
        DmLabel lbl;

        panel = new FlowPanel();
        DmClient.setStyle(panel, "100%", "40px", null, null, DmPosition.NONE, true);
        //===============================< 1st Row >===============================//   
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Range 1");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "1%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "48%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Range 2");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        panel.add(flwTr);
        //============================
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, false);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRange1));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtRangeUnit);
        txtRangeUnit.setReadOnly(true);
        txtRangeUnit.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "4%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtRange2));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "18%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(txtRangeUnit1);
        txtRangeUnit1.setReadOnly(true);
        txtRangeUnit1.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
        flwTr.add(flwTd);

        panel.add(flwTr);
        //===============================< ******* >===============================//
        flwRange.add(panel);
        flwRangeContainer.add(flwRange);

        return flwRangeContainer;
    }

}
