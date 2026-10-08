package dataman.utility.guiConfig.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import dataman.dmBase.self.shared.DmEnum;
import dataman.dmToolkit.dmGrid.client.DmGrid;
import dataman.dmToolkit.dmGridEditPanel.client.DmGridEditPanel;
import dataman.dmToolkit.dmLabel.client.DmLabel;
import dataman.dmToolkit.dmStatic.client.DmClient;
import dataman.dmToolkit.dmTextBox.client.DmTextBox;
import dataman.dmToolkit.dmTextBoxNumeric.client.DmTextBoxNumeric;
import dataman.dmToolkit.dmTextHelp.client.DmTextHelp;
import dataman.dmBase.self.shared.DmEnum.DmPosition;
import dataman.dmBase.self.shared.*;

public abstract class ColumnGridEdit_Dsg<T1, T2> extends DmGridEditPanel<T1, T2> {

    final FlowPanel flwForm = new FlowPanel();

    final DmTextBox txtFieldCaption = new DmTextBox(250, false);
    final DmTextBox txtDefaultText = new DmTextBox(50, false);
    final DmTextBox txtDefaultCode = new DmTextBox(50, false);
    final DmTextBoxNumeric txtSNo = new DmTextBoxNumeric(0, false, 3, true);
    final DmTextBoxNumeric txtDecimal = new DmTextBoxNumeric(0, false, 1, false);
    final DmTextBoxNumeric txtCaptionWidth = new DmTextBoxNumeric(2, false, 3, false);
    final DmTextBoxNumeric txtHelpWidth = new DmTextBoxNumeric(2, false, 3, false);
    final DmTextBoxNumeric txtHelpRow = new DmTextBoxNumeric(0, false, 2, false);
    final DmTextBoxNumeric txtControlWidth = new DmTextBoxNumeric(2, false, 3, false);
    final DmTextBoxNumeric txtMaxLength = new DmTextBoxNumeric(0, false, 4, false);
    final DmTextBox txtQuery = new DmTextBox();
    final DmTextBox txtCustomFieldName = new DmTextBox(30, false);
    final DmTextBox txtCaptionStyle = new DmTextBox(250, false);
    final DmTextBox txtControlStyle = new DmTextBox(250, false);
    final DmTextBox txtFieldName = new DmTextBox(50, false);

    final FlowPanel flwQuery = new FlowPanel();
    final Button btnQuery = new Button("Q");

    //<editor-fold defaultstate="collapsed" desc="TxtHMandatory">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHMandatory = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniYesNo() {
        txtHMandatory.getDmGrid().addTextColumn(txtHMandatory.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHMandatory.getDmGrid().initializeGrid(txtHMandatory.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHCopy">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHCopy = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 3, true, true);

    private void iniCopy() {
        txtHCopy.getDmGrid().addTextColumn(txtHCopy.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHCopy.getDmGrid().initializeGrid(txtHCopy.getDmGrid().getPropertyAccess().code(), 100, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHAllowNegative">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHAllowNegative = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 4, true, false);

    private void iniAllowNegative() {
        txtHAllowNegative.getDmGrid().addTextColumn(txtHAllowNegative.getDmGrid().getPropertyAccess().name(), "Name", 100, "Name");
        txtHAllowNegative.getDmGrid().initializeGrid(txtHAllowNegative.getDmGrid().getPropertyAccess().code(), 150, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="txtHDataType">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHDataType = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, true, true);

    private void iniTxtHDefault() {
        txtHDataType.getDmGrid().addTextColumn(txtHDataType.getDmGrid().getPropertyAccess().name(), "Data Type", 0, "Data Type");
        txtHDataType.getDmGrid().initializeGrid(txtHDataType.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHSaveInField">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHSaveInField = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHSave() {
        txtHSaveInField.getDmGrid().addTextColumn(txtHSaveInField.getDmGrid().getPropertyAccess().name(), "Column", 10, "columnName");
        txtHSaveInField.getDmGrid().initializeGrid(txtHSaveInField.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
        txtHSaveInField.getHelper().addSort("columnName", DmEnum.DmSortType.ASC);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHViewCode">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHViewCode = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHViewCode() {
        txtHViewCode.getDmGrid().addTextColumn(txtHViewCode.getDmGrid().getPropertyAccess().name(), "Column", 10, "Name");
        txtHViewCode.getDmGrid().initializeGrid(txtHViewCode.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TxtHViewText">
    final DmTextHelp<KeyFieldExtended, KeyField_PrpAcs> txtHViewText = new DmTextHelp(GWT.create(KeyField_PrpAcs.class), 10, false);

    private void iniTxtHViewText() {
        txtHViewText.getDmGrid().addTextColumn(txtHViewText.getDmGrid().getPropertyAccess().name(), "Column", 10, "Name");
        txtHViewText.getDmGrid().initializeGrid(txtHViewText.getDmGrid().getPropertyAccess().code(), 175, 0, 1);
    }
    //</editor-fold>

    public ColumnGridEdit_Dsg(DmGrid<T1, T2> grid, String title, String width, String height) {
        super(grid, title, width, height);
        iniTxtHDefault();
        iniYesNo();
        iniTxtHSave();
        iniTxtHViewText();
        iniTxtHViewCode();
        iniAllowNegative();
        iniCopy();
        container.add(workingArea());
        container.setStyleName("dm-FlwWorkingArea");
    }

    private FlowPanel workingArea() {
        FlowPanel flwTable, flwTr, flwTd;
        DmLabel lbl;

        flwTable = new FlowPanel();
        DmClient.setStyle(flwTable, "98%", "210px", null, null, DmPosition.CENTER, true);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("S.No.");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtSNo));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Save In Field");
        lbl.getElement().getStyle().setMarginTop(2, Style.Unit.PCT);
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHSaveInField));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Field Caption");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFieldCaption));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Field Data Type");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHDataType));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Caption Width %");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCaptionWidth));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Control Width %");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtControlWidth));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Custom Field Name");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCustomFieldName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);
        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Caption Style");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtCaptionStyle));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Control Style");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtControlStyle));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Max Length");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtMaxLength));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Decimal");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDecimal));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Is Mandatory");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHMandatory));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Copy");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHCopy));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Allow Negative");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHAllowNegative));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("View Field Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHViewCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("View Field Text");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHViewText));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Code");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDefaultCode));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Default Text");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtDefaultText));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Help Query");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "77%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtQuery));
        flwTr.add(flwTd);

        DmClient.setStyle(flwQuery, "3%", "20px", "left", null, DmPosition.NONE, true);
        btnQuery.setWidth("100%");
        btnQuery.setHeight("20px");
        btnQuery.getElement().setAttribute("style", btnQuery.getElement().getAttribute("style")
                + ";font-size:11px;padding:0px;margin-left: 2px;");
        btnQuery.addStyleName("dm-button-grad");
        flwQuery.add(btnQuery);
        flwTr.add(flwQuery);

        flwTable.add(flwTr);

        /*================================< Row >================================*/
        flwTr = new FlowPanel();
        DmClient.setStyle(flwTr, "100%", null, "left", null, DmPosition.NONE, true);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Help Width");
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHelpWidth));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "14%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Help Row/Page");
        lbl.getElement().getStyle().setMarginLeft(10, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "8%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtHelpRow));
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "20%", null, "left", null, DmPosition.NONE, false);
        lbl = new DmLabel("Field Name");
        lbl.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);
        flwTd.add(lbl);
        flwTr.add(flwTd);

        flwTd = new FlowPanel();
        DmClient.setStyle(flwTd, "30%", null, "left", null, DmPosition.NONE, false);
        flwTd.add(vl.add(txtFieldName));
        flwTr.add(flwTd);

        flwTable.add(flwTr);

        return flwTable;
    }

}
