package aarogya.selfToolkit.serviceGrid.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.sencha.gxt.core.client.ValueProvider;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmBase.self.shared.DmEnum.DmSortType;
import dataman.dmBase.self.shared.KeyFieldExtended;
import dataman.dmBase.self.shared.KeyFieldHashCode_T_PrpAcs;
import dataman.dmBase.self.shared.KeyField_PrpAcs;
import dataman.dmToolkit.dmCheckBox.client.DmCheckBox;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import aarogya.selfToolkit.serviceGrid.shared.TxtHServiceDetail_Mdl;

public abstract class ServiceGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final DmTextBoxNumeric txtAmount = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtNetAmount = new DmTextBoxNumeric(2, false, 12, false);

    final DmTextBoxNumeric txtSgstUgstPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtSGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtUGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtCGSTPer = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtIGSTPer = new DmTextBoxNumeric(2, false, 12, false);

    final DmTextBoxNumeric txtSgstUgst = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtSGST = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtUGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBoxNumeric txtCGST = new DmTextBoxNumeric(2, false, 12, false);
    final DmTextBoxNumeric txtIGST = new DmTextBoxNumeric(2, true, 12, false);
    final DmTextBox txtHsn = new DmTextBox();
    final DmTextBoxNumeric txtServiceEvalRate = new DmTextBoxNumeric(2, false, 12, true);

    final DmCheckBox cbIsManualGstType = new DmCheckBox();
    final DmCheckBox cbIsManualTax = new DmCheckBox();
    DmLabel lblManualtax = new DmLabel("Manual Tax");
    DmLabel lblSgstUgst;
    final FlowPanel FlwManualGstType = new FlowPanel();
    final FlowPanel FlwManualTax = new FlowPanel();
    
    public ServiceGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHItemName();
        iniTxtHITSType();
        iniTxtHValuationType();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "295px", null, null, DmEnum.DmPosition.CENTER, true);

        /*====================================< 1st Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "95%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Service");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHItemName));
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< 2nd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("SAC");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("GST Type");
        flwTd.getElement().getStyle().setMarginLeft(44, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(7, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginBottom(3, Style.Unit.PX);
        cbIsManualGstType.setTitle("Check it for manual GTS");
       // flwTd.add(vl.add(cbIsManualGstType));
        flwTd.add(FlwManualGstType);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHsn));
        txtHsn.setReadOnly(true);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "58%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHGstType));
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------------------------------------------
        /*====================================< 4tht Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "95%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Valuation Type");
        flwTd.add(lbl);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtHServiceEvalType));
        txtAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTable.add(flwTr);

        /*====================================< ******* >====================================*/
 /*====================================< 3rd Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Value");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Amount");
        flwTd.getElement().getStyle().setMarginLeft(44, Style.Unit.PX);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "38%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtServiceEvalRate));
        txtServiceEvalRate.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "58%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtAmount));
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        txtAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------------------------------------------
        /*====================================< 5th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lblSgstUgst = new DmLabel("SGST/UGST");
        flwTd.add(lblSgstUgst);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "25%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(43, Style.Unit.PCT);
        // lbl = new DmLabel("Manual Tax");
        flwTd.add(lblManualtax);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "5%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTd.getElement().getStyle().setMarginBottom(1, Style.Unit.PX);
        cbIsManualTax.setTitle("Check it for manual TAX");
        //flwTd.add(vl.add(cbIsManualTax));
        flwTd.add(FlwManualTax);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSgstUgstPer));
        txtSgstUgstPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);

        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtSgstUgst));
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        txtSgstUgst.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);
        flwTable.add(flwTr);
        /*====================================< 6th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "95%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("CGST");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCGSTPer));
        txtCGSTPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);

        flwTr.add(flwTd);
        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtCGST));
        txtCGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*====================================< 7th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "95%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("IGST");
        flwTd.add(lbl);

        flwTr.add(flwTd);
        flwTable.add(flwTr);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "23%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtIGSTPer));
        txtIGSTPer.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);

        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "73%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtIGST));
        txtIGST.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTd.getElement().getStyle().setMarginLeft(5, Style.Unit.PX);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        //-------------------------------------------------------------------------------------
        /*====================================< 8th Row >====================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "95%", null, "left", null, DmEnum.DmPosition.NONE, false);
        lbl = new DmLabel("Total Amount");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmEnum.DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "98%", null, "left", null, DmEnum.DmPosition.NONE, false);
        flwTd.add(vl.add(txtNetAmount));
        txtNetAmount.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

    //<editor-fold defaultstate="collapsed" desc="txtHItemName">
    interface TxtHItemName_PrpAcs extends KeyFieldHashCode_T_PrpAcs<TxtHServiceDetail_Mdl> {

        @Editor.Path("ManualCode")
        ValueProvider<TxtHServiceDetail_Mdl, String> manualCode();

        @Editor.Path("Hsn")
        ValueProvider<TxtHServiceDetail_Mdl, String> hsn();

    }

    final DmTextHelp<TxtHServiceDetail_Mdl, TxtHItemName_PrpAcs> txtHItemName = new DmTextHelp(com.google.gwt.core.client.GWT.create(TxtHItemName_PrpAcs.class), 7, true);

    private void iniTxtHItemName() {
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().name(), "Service Name", 200, "name");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().manualCode(), "Manual Code", 100, "manualCode");
        txtHItemName.getDmGrid().addTextColumn(txtHItemName.getDmGrid().getPropertyAccess().hsn(), "SAC", 80, "hsn");
        txtHItemName.getDmGrid().initializeGrid(txtHItemName.getDmGrid().getPropertyAccess().fieldHashCode(), 400, 0, 1);
        txtHItemName.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHGstType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHGstType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 6, true);

    private void iniTxtHITSType() {
        txtHGstType.getDmGrid().addTextColumn(txtHGstType.getDmGrid().getPropertyAccess().name(), "Name", 0, "name");
        txtHGstType.getDmGrid().initializeGrid(txtHGstType.getDmGrid().getPropertyAccess().code(), 150, 0, 1);

        txtHGstType.getHelper().addSort("printSrNo", DmSortType.ASC);
        txtHGstType.getHelper().addSort("name", DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHValuationType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHServiceEvalType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, true);

    private void iniTxtHValuationType() {
        txtHServiceEvalType.getDmGrid().addTextColumn(txtHServiceEvalType.getDmGrid().getPropertyAccess().name(), "Type", 0, "");
        txtHServiceEvalType.getDmGrid().initializeGrid(txtHServiceEvalType.getDmGrid().getPropertyAccess().code(), 200, 0, 1);
    }

    //</editor-fold>
}
